/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mail;

import java.awt.AWTException;
import java.util.List;
import org.openqa.selenium.Keys;
import org.testng.annotations.*;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.*;

public class DeleteMail extends SetGroupMailByMessagePreference {

	public DeleteMail() {
		logger.info("New "+ DeleteMail.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox", "TRUE");
	}


	@Bugs (ids = "98054")
	@Test (description = "Delete a mail using toolbar delete button",
			groups = { "smoke", "L1" })

	public void DeleteMail_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject"+ ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Click delete
		app.zPageMail.zToolbarPressButton(Button.B_DELETE);

		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem found = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for "+ mail.dSubject +" found: "+ m.gSubject);
			if ( mail.dSubject.equals(m.gSubject) ) {
				found = m;
				break;
			}
		}
		ZAssert.assertNull(found, "Verify the message is no longer in the inbox");
	}


	@Test (description = "Delete a mail using checkbox and toolbar delete button",
			groups = { "functional", "L2" })

	public void DeleteMail_02() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject"+ ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Check the item
		app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, mail.dSubject);

		// Click delete
		app.zPageMail.zToolbarPressButton(Button.B_DELETE);

		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem found = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for "+ mail.dSubject +" found: "+ m.gSubject);
			if ( mail.dSubject.equals(m.gSubject) ) {
				found = m;
				break;
			}
		}
		ZAssert.assertNull(found, "Verify the message is no longer in the inbox");
	}


	@DataProvider(name = "DataProviderDeleteKeys")
	public Object[][] DataProviderDeleteKeys() {
	  return new Object[][] {
	    new Object[] { "DELETE", Keys.DELETE },
	    new Object[] { "BACK_SPACE", Keys.BACK_SPACE },
	  };
	}

	@Test (description = "Delete a mail by selecting and typing 'delete' keyboard",
			groups = { "functional", "L2" },
			dataProvider = "DataProviderDeleteKeys")

	public void DeleteMail_03(String name, Keys keyEvent) throws HarnessException {

		// Create the message data to be sent
		String subject = "subject"+ ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Check the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Click delete
		logger.info("Typing shortcut key "+ name + " KeyEvent: "+ keyEvent);
		app.zPageMail.zKeyboardKeyEvent(keyEvent);

		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem found = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for "+ mail.dSubject +" found: "+ m.gSubject);
			if ( mail.dSubject.equals(m.gSubject) ) {
				found = m;
				break;
			}
		}
		ZAssert.assertNull(found, "Verify the message is no longer in the inbox");
	}


	@Test (description = "Delete a mail by selecting and typing '.t' shortcut",
			groups = { "functional", "L3" } )

	public void DeleteMail_04() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject"+ ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Check the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Click delete
		app.zPageMail.zKeyboardShortcut(Shortcut.S_MAIL_MOVETOTRASH);

		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem found = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for "+ mail.dSubject +" found: "+ m.gSubject);
			if ( mail.dSubject.equals(m.gSubject) ) {
				found = m;
				break;
			}
		}
		ZAssert.assertNull(found, "Verify the message is no longer in the inbox");
	}


	@Test (description = "Delete multiple messages (3) by select and toolbar delete",
			groups = { "functional", "L2" })

	public void DeleteMail_05() throws HarnessException {

		// Create the message data to be sent
		String subject1 = "subject"+ ConfigProperties.getUniqueString();
		String subject2 = "subject"+ ConfigProperties.getUniqueString();
		String subject3 = "subject"+ ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject1 +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject2 +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject3 +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		// Import each message into MailItem objects
		MailItem mail1 = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject1 +")");
		MailItem mail2 = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject2 +")");
		MailItem mail3 = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject3 +")");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select all three items
		app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, mail1.dSubject);
		app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, mail2.dSubject);
		app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, mail3.dSubject);

		// Click toolbar delete button
		app.zPageMail.zToolbarPressButton(Button.B_DELETE);

		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem found1 = null;
		MailItem found2 = null;
		MailItem found3 = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking at: "+ m.gSubject);
			if ( mail1.dSubject.equals(m.gSubject) ) {
				found1 = m;
			}
			if ( mail2.dSubject.equals(m.gSubject) ) {
				found2 = m;
			}
			if ( mail3.dSubject.equals(m.gSubject) ) {
				found3 = m;
			}
		}
		ZAssert.assertNull(found1, "Verify the message "+ mail1.dSubject +" is no longer in the inbox");
		ZAssert.assertNull(found2, "Verify the message "+ mail2.dSubject +" is no longer in the inbox");
		ZAssert.assertNull(found3, "Verify the message "+ mail3.dSubject +" is no longer in the inbox");
	}


	@Test (description = "Delete a mail using context menu delete button",
			groups = { "functional", "L2" })

	public void DeleteMail_06() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject"+ ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Right click the item, select delete
		app.zPageMail.zListItem(Action.A_RIGHTCLICK, Button.B_DELETE, mail.dSubject);

		// Make sure the message no longer appears in the list
		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem found = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for "+ mail.dSubject +" found: "+ m.gSubject);
			if ( mail.dSubject.equals(m.gSubject) ) {
				found = m;
				break;
			}
		}
		ZAssert.assertNull(found, "Verify the message is no longer in the inbox");
	}


	@Test (description = "Verify that a mail which was present towards bottom of the list does not appear after deleting it.",
			groups = { "functional", "L3" })

	public void DeleteMail_07() throws HarnessException, AWTException {

		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ app.zGetActiveAccount().ZimbraId +"</id>"
			+		"<a n='zimbraPrefGroupMailBy'>message</a>"
			+	"</ModifyAccountRequest>");

		String[] subject = new String[60];

		// Creating test data by sending 60 mails to test account

		for (int i=0; i<60; i++) {
			subject[i] = "subject" + i;
			ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
							"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<e t='c' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
							"<su>"+ subject[i] +"</su>" +
							"<mp ct='text/plain'>" +
							"<content>"+ "body" + ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
							"</m>" +
					"</SendMsgRequest>");
		}

		// Refresh current view
		app.zPageMain.zRefreshMainUI();

		// Refreshing it twice as single refresh does not work sometimes
		app.zPageMail.zVerifyMailExists(subject[52]);

		//scroll down to 51st mail
		app.zPageMail.zScrollTo(550);

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject[9] +")");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject[9]);

		// Click delete
		app.zPageMail.zToolbarPressButton(Button.B_DELETE);

		// Verify that deleted mail does not appear in the list
		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem found = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for "+ mail.dSubject +" found: "+ m.gSubject);
			if ( mail.dSubject.equals(m.gSubject) ) {
				found = m;
				break;
			}
		}

		ZAssert.assertNull(found, "Verify the message is no longer in the inbox");

		//UI verification
	    ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(subject[9]), "Verify that Mail does not exist");
	}


	@Bugs (ids = "53564")
	@Test (description = "Hard-delete a mail by selecting and typing 'shift-del' shortcut",
			groups = { "functional", "L2" } )

	public void HardDeleteMail_08() throws HarnessException {
		app.zGetActiveAccount().soapSend(
				"<GetFolderRequest xmlns='urn:zimbraMail'/>");

		// Create the message data to be sent
		String subject = "subject"+ ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>"
				+			"<su>"+ subject +"</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content"+ ConfigProperties.getUniqueString() +"</content>"
				+			"</mp>"
				+		"</m>"
				+	"</SendMsgRequest>");

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");


		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Check the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Click shift-delete
		DialogWarning dialog = (DialogWarning)app.zPageMail.zKeyboardShortcut(Shortcut.S_MAIL_HARDELETE);
		dialog.zPressButton(Button.B_OK);

		// Verify the message is no longer in the mailbox
		app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>subject:("+ subject +") is:anywhere</query>"
				+	"</SearchRequest>");

		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:m");
		ZAssert.assertEquals(nodes.length, 0, "Verify the message is no longer in the inbox");
	}


	@Bugs (ids = "53564")
	@Test (description = "Hard-delete multiple messages (3) by selecting and typing 'shift-del' shortcut",
			groups = { "functional", "L2" })

	public void HardDeleteMail_09() throws HarnessException {

		// Create the message data to be sent
		String subject1 = "subject"+ ConfigProperties.getUniqueString();
		String subject2 = "subject"+ ConfigProperties.getUniqueString();
		String subject3 = "subject"+ ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject1 +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject2 +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject3 +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		// Import each message into MailItem objects
		MailItem mail1 = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject1 +")");
		MailItem mail2 = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject2 +")");
		MailItem mail3 = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject3 +")");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select all three items
		app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, mail1.dSubject);
		app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, mail2.dSubject);
		app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, mail3.dSubject);

		DialogWarning dialog = (DialogWarning)app.zPageMail.zKeyboardShortcut(Shortcut.S_MAIL_HARDELETE);
		dialog.zPressButton(Button.B_OK);

		// Verify the message is no longer in the mailbox
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ subject1 +") is:anywhere</query>"
			+	"</SearchRequest>");

		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:m");
		ZAssert.assertEquals(nodes.length, 0, "Verify the message (subject1) is no longer in the inbox");

		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>subject:("+ subject2 +") is:anywhere</query>"
				+	"</SearchRequest>");

		nodes = app.zGetActiveAccount().soapSelectNodes("//mail:m");
		ZAssert.assertEquals(nodes.length, 0, "Verify the message (subject2) is no longer in the inbox");

		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>subject:("+ subject2 +") is:anywhere</query>"
				+	"</SearchRequest>");

		nodes = app.zGetActiveAccount().soapSelectNodes("//mail:m");
		ZAssert.assertEquals(nodes.length, 0, "Verify the message (subject2) is no longer in the inbox");
	}


	@Test (description = "Delete a mail from trash - confirm warning dialog",
			groups = { "functional", "L2" })

	public void DeleteMailFromTrash_10() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject"+ ConfigProperties.getUniqueString();
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Trash);

		// Add a message to the trash
		app.zGetActiveAccount().soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
        		+		"<m l='"+ trash.getId() +"' >"
            	+			"<content>From: foo@foo.com\n"
            	+				"To: foo@foo.com \n"
            	+				"Subject: "+ subject +"\n"
            	+				"MIME-Version: 1.0 \n"
            	+				"Content-Type: text/plain; charset=utf-8 \n"
            	+				"Content-Transfer-Encoding: 7bit\n"
            	+				"\n"
            	+				"simple text string in the body\n"
            	+			"</content>"
            	+		"</m>"
				+	"</AddMsgRequest>");

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:("+ subject +")");

		try {

			// Refresh current view
			app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

			// Select the trash
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, trash);

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

			// Click delete
			app.zPageMail.zToolbarPressButton(Button.B_DELETE);

			// Warning dialog will appear
			DialogWarning dialog = new DialogWarning(DialogWarning.DialogWarningID.PermanentlyDeleteTheItem,
											app,
											((AjaxPages) app).zPageMail);
			ZAssert.assertTrue(dialog.zIsActive(), "Verify the warning dialog opens");
			dialog.zPressButton(Button.B_OK);

		} finally {
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox));
		}

		// Verification
		mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:("+ subject +")");
		ZAssert.assertNull(mail, "Verify the message no longer exists in the mailbox");
	}


	@Test (description = "Delete multiple messages (3) from trash by select and toolbar delete - confirm warning dialog",
			groups = { "functional", "L2" })

	public void DeleteMailFromTrash_11() throws HarnessException {

		// Create the message data to be sent
		String subject1 = "subject"+ ConfigProperties.getUniqueString();
		String subject2 = "subject"+ ConfigProperties.getUniqueString();
		String subject3 = "subject"+ ConfigProperties.getUniqueString();
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Trash);

		// Add a message to the trash
		app.zGetActiveAccount().soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
        		+		"<m l='"+ trash.getId() +"' >"
            	+			"<content>From: foo@foo.com\n"
            	+				"To: foo@foo.com \n"
            	+				"Subject: "+ subject1 +"\n"
            	+				"MIME-Version: 1.0 \n"
            	+				"Content-Type: text/plain; charset=utf-8 \n"
            	+				"Content-Transfer-Encoding: 7bit\n"
            	+				"\n"
            	+				"simple text string in the body\n"
            	+			"</content>"
            	+		"</m>"
				+	"</AddMsgRequest>");

		// Add a message to the trash
		app.zGetActiveAccount().soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
        		+		"<m l='"+ trash.getId() +"' >"
            	+			"<content>From: foo@foo.com\n"
            	+				"To: foo@foo.com \n"
            	+				"Subject: "+ subject2 +"\n"
            	+				"MIME-Version: 1.0 \n"
            	+				"Content-Type: text/plain; charset=utf-8 \n"
            	+				"Content-Transfer-Encoding: 7bit\n"
            	+				"\n"
            	+				"simple text string in the body\n"
            	+			"</content>"
            	+		"</m>"
				+	"</AddMsgRequest>");

		// Add a message to the trash
		app.zGetActiveAccount().soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
        		+		"<m l='"+ trash.getId() +"' >"
            	+			"<content>From: foo@foo.com\n"
            	+				"To: foo@foo.com \n"
            	+				"Subject: "+ subject3 +"\n"
            	+				"MIME-Version: 1.0 \n"
            	+				"Content-Type: text/plain; charset=utf-8 \n"
            	+				"Content-Transfer-Encoding: 7bit\n"
            	+				"\n"
            	+				"simple text string in the body\n"
            	+			"</content>"
            	+		"</m>"
				+	"</AddMsgRequest>");

		// Import each message into MailItem objects
		MailItem mail1 = MailItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:("+ subject1 +")");
		MailItem mail2 = MailItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:("+ subject2 +")");
		MailItem mail3 = MailItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:("+ subject3 +")");

		try {

			// Refresh current view
			app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

			// Select the trash
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, trash);

			// Select all three items
			app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, mail1.dSubject);
			app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, mail2.dSubject);
			app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, mail3.dSubject);

			// Click delete
			app.zPageMail.zToolbarPressButton(Button.B_DELETE);

			// Warning dialog will appear
			DialogWarning dialog = new DialogWarning(DialogWarning.DialogWarningID.PermanentlyDeleteTheItem,
											app, ((AjaxPages) app).zPageMail);
			ZAssert.assertTrue(dialog.zIsActive(), "Verify the warning dialog opens");
			dialog.zPressButton(Button.B_OK);

		} finally {
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox));
		}

		// Verification
		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem found1 = null;
		MailItem found2 = null;
		MailItem found3 = null;

		for (MailItem m : messages) {
			logger.info("Subject: looking at: "+ m.gSubject);
			if ( mail1.dSubject.equals(m.gSubject) ) {
				found1 = m;
			}
			if ( mail2.dSubject.equals(m.gSubject) ) {
				found2 = m;
			}
			if ( mail3.dSubject.equals(m.gSubject) ) {
				found3 = m;
			}
		}
		ZAssert.assertNull(found1, "Verify the message "+ mail1.dSubject +" is no longer in the inbox");
		ZAssert.assertNull(found2, "Verify the message "+ mail2.dSubject +" is no longer in the inbox");
		ZAssert.assertNull(found3, "Verify the message "+ mail3.dSubject +" is no longer in the inbox");
	}


	@Bugs (ids = "79188")
	@Test (description = "Delete a message from drafts",
			groups = { "functional", "L2" })

	public void DeleteMailFromDrafts_12() throws HarnessException {

		String subject = "subject"+ ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<SaveDraftRequest xmlns='urn:zimbraMail'>" +
					"<m >" +
						"<e t='t' a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>body "+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SaveDraftRequest>");

		// Get the system folders
		FolderItem drafts = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Drafts);
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);

		try {

			// Refresh current view
			app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

			// Click in Drafts
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, drafts);

			// Select the conversation or message (in 8.X, only messages are shown in drafts, not conversations)
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

			// Click Delete
			app.zPageMail.zToolbarPressButton(Button.B_DELETE);

		} finally {
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox));
		}

		// Verify draft is no longer in drafts folder
		MailItem m = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:(" + subject +") inid:"+ drafts.getId());
		ZAssert.assertNull(m, "Verify message is deleted from drafts");

		// Verify draft is in trash folder
		m = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:(" + subject +") inid:"+ trash.getId());
		ZAssert.assertNotNull(m, "Verify message is moved to trash");
	}
}