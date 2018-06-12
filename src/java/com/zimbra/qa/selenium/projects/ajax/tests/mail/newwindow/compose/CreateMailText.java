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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.compose;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.SeparateWindowFormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class CreateMailText extends SetGroupMailByMessagePreference {

	public CreateMailText() {
		logger.info("New " + CreateMailText.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
		super.startingAccountPreferences.put("zimbraPrefComposeInNewWindow", "TRUE");
	}


	@Test (description = "Send a mail using Text editor - in a separate window",
			groups = { "sanity", "L0" })

	public void CreateMailText_01() throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyText = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		SeparateWindowFormMailNew window = null;
		String windowTitle = ": Compose";

		try {

			window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW_IN_NEW_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			// Fill out the form with the data
			window.zFill(mail);

			// Send the message
			window.zToolbarPressButton(Button.B_SEND);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

		for (int i = 0; i < 30; i++) {
			ZimbraAccount.AccountA().soapSend("<SearchRequest types='message' xmlns='urn:zimbraMail'>"
					+ "<query>subject:(" + mail.dSubject + ")</query>" + "</SearchRequest>");
			com.zimbra.common.soap.Element node = ZimbraAccount.AccountA().soapSelectNode("//mail:m", 1);
			if (node != null) {
				break;
			}
			SleepUtil.sleep(1000);
		}

		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:(" + mail.dSubject + ")");

		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress,
				"Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountA().EmailAddress,
				"Verify the to field is correct");
		ZAssert.assertEquals(received.dSubject, mail.dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(received.dBodyText, mail.dBodyText, "Verify the body field is correct");
	}


	@DataProvider(name = "DataProvideNewMessageShortcuts")
	public Object[][] DataProvideNewMessageShortcuts() {
		return new Object[][] {
				new Object[] { Shortcut.S_NEWITEM_IN_NEW_WINDOW, Shortcut.S_NEWITEM_IN_NEW_WINDOW.getKeys() },
				new Object[] { Shortcut.S_NEWMESSAGE_IN_NEW_WINDOW, Shortcut.S_NEWMESSAGE_IN_NEW_WINDOW.getKeys() },
				new Object[] { Shortcut.S_NEWMESSAGE2_IN_NEW_WINDOW, Shortcut.S_NEWMESSAGE2_IN_NEW_WINDOW.getKeys() } };
	}

	@Test (description = "Send a mail using Text editor using keyboard shortcuts - in separate window",
			groups = { "functional", "L2" }, dataProvider = "DataProvideNewMessageShortcuts")

	public void CreateMailText_02(Shortcut shortcut, String keys) throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyText = "body" + ConfigProperties.getUniqueString();

		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);

		// Open the new mail form
		SeparateWindowFormMailNew window = null;
		String windowTitle = ": Compose";

		try {

			window = (SeparateWindowFormMailNew) app.zPageMail.zKeyboardShortcut(shortcut);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			// Fill out the form with the data
			window.zFill(mail);

			// Send the message
			window.zToolbarPressButton(Button.B_SEND);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);

		// From the receipient end, make sure the message is received
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:(" + mail.dSubject + ")");

		ZAssert.assertNotNull(received, "Verify the message is received");
	}


	@Test (description = "Send a mail with CC - in a separate window",
			groups = { "functional", "L2" })

	public void CreateMailText_03() throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA(), RecipientItem.RecipientType.To));
		mail.dCcRecipients.add(new RecipientItem(ZimbraAccount.AccountB(), RecipientItem.RecipientType.Cc));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyText = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		SeparateWindowFormMailNew window = null;
		String windowTitle = ": Compose";

		try {

			window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW_IN_NEW_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			// Fill out the form with the data
			window.zFill(mail);

			// Send the message
			window.zToolbarPressButton(Button.B_SEND);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:(" + mail.dSubject + ")");
		ZAssert.assertNotNull(sent, "Verify the message is in the sent folder");

		StringBuilder to = new StringBuilder();
		for (RecipientItem r : sent.dToRecipients) {
			to.append(r.dEmailAddress).append(",");
		}
		ZAssert.assertStringContains(to.toString(), ZimbraAccount.AccountA().EmailAddress, "Verify TO contains AccountA");

		StringBuilder cc = new StringBuilder();
		for (RecipientItem r : sent.dCcRecipients) {
			cc.append(r.dEmailAddress).append(",");
		}
		ZAssert.assertStringContains(cc.toString(), ZimbraAccount.AccountB().EmailAddress, "Verify CC contains AccountB");

		MailItem toReceived = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:(" + mail.dSubject + ")");
		ZAssert.assertNotNull(toReceived, "Verify the TO recipient receives the message");

		MailItem ccReceived = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:(" + mail.dSubject + ")");
		ZAssert.assertNotNull(ccReceived, "Verify the CC recipient receives the message");
	}


	@Test (description = "Send a mail with BCC",
			groups = { "deprecated" })

	public void CreateMailText_04() throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA(), RecipientItem.RecipientType.To));
		mail.dBccRecipients.add(new RecipientItem(ZimbraAccount.AccountB(), RecipientItem.RecipientType.Bcc));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyText = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		SeparateWindowFormMailNew window = null;
		String windowTitle = ": Compose";

		try {

			window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW_IN_NEW_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			// Fill out the form with the data
			window.zFill(mail);

			// Send the message
			window.zToolbarPressButton(Button.B_SEND);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:(" + mail.dSubject + ")");
		ZAssert.assertNotNull(sent, "Verify the message is in the sent folder");

		StringBuilder to = new StringBuilder();
		for (RecipientItem r : sent.dToRecipients) {
			to.append(r.dEmailAddress).append(",");
		}
		ZAssert.assertStringContains(to.toString(), ZimbraAccount.AccountA().EmailAddress, "Verify TO contains AccountA");

		MailItem toReceived = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:(" + mail.dSubject + ")");
		ZAssert.assertNotNull(toReceived, "Verify the TO recipient receives the message");

		MailItem bccReceived = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:(" + mail.dSubject + ")");
		ZAssert.assertNotNull(bccReceived, "Verify the BCC recipient receives the message");
	}


	@DataProvider(name = "DataProvidePriorities")
	public Object[][] DataProvidePriorities() {
		return new Object[][] { new Object[] { Button.O_PRIORITY_HIGH, "!" },
				new Object[] { Button.O_PRIORITY_NORMAL, "" }, new Object[] { Button.O_PRIORITY_LOW, "?" } };
	}

	@Test (description = "Send a mail with different priorities high/normal/low - in a separate window",
			groups = { "functional", "L2" }, dataProvider = "DataProvidePriorities")

	public void CreateMailText_05(Button option, String verify) throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		SeparateWindowFormMailNew window = null;
		String windowTitle = ": Compose";

		try {

			window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW_IN_NEW_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			window.waitForComposeWindow();

			// Change the priority
			window.zToolbarPressPulldown(Button.B_PRIORITY, option);

			// Fill out the rest of the form
			window.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
			window.zFillField(Field.Subject, subject);
			window.zFillField(Field.Body, body);

			// Send the message
			window.zToolbarPressButton(Button.B_SEND);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:(" + subject + ")");

		ZAssert.assertNotNull(received, "Verify the message is received");
		ZAssert.assertStringContains(received.getFlags(), verify, "Verify the correct priority was sent");
	}
}