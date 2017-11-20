/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.mail.tags;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.universal.pages.mail.PageMail.Locators;

public class UnTagMessage extends SetGroupMailByMessagePreference {

	public UnTagMessage() {
		logger.info("New " + UnTagMessage.class.getCanonicalName());


	}

	@Test (description = "Remove a tag from a message using Toolbar -> Tag ->Remove Tag", groups = { "smoke", "L1" })
	public void UnTagMessage_01() throws HarnessException {

		String subject = "subject" + ConfigProperties.getUniqueString();
		String tagname = "tag" + ConfigProperties.getUniqueString();

		// Create a tag
		app.zGetActiveAccount().soapSend(
				"<CreateTagRequest xmlns='urn:zimbraMail'>" + "<tag name='"
						+ tagname + "' color='1' />" + "</CreateTagRequest>");
		String tagid = app.zGetActiveAccount().soapSelectValue(
				"//mail:CreateTagResponse/mail:tag", "id");

		// Add a message to the mailbox
		FolderItem inboxFolder = FolderItem.importFromSOAP(app
				.zGetActiveAccount(), SystemFolder.Inbox);
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>" + "<m l='"
						+ inboxFolder.getId() + "' t='" + tagid + "'>"
						+ "<content>From: foo@foo.com\n" + "To: foo@foo.com \n"
						+ "Subject: " + subject + "\n" + "MIME-Version: 1.0 \n"
						+ "Content-Type: text/plain; charset=utf-8 \n"
						+ "Content-Transfer-Encoding: 7bit\n" + "\n"
						+ "simple text string in the body\n" + "</content>"
						+ "</m>" + "</AddMsgRequest>");

		// Get the message data from SOAP
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(),
				"subject:(" + subject + ")");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Untag it
		app.zPageMail.zToolbarPressPulldown(Button.B_TAG,Button.O_TAG_REMOVETAG);

		app.zGetActiveAccount().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='"
						+ mail.getId() + "'/>" + "</GetMsgRequest>");
		String mailTags = app.zGetActiveAccount().soapSelectValue(
				"//mail:GetMsggResponse//mail:m", "t");

		ZAssert.assertNull(mailTags,
				"Verify that the tag is removed from the message");

	}
	
	@Test (description = "Remove a tag from a message using Keyboard shortcut u", groups = { "functional", "L2" })
	public void UnTagMessage_02() throws HarnessException {

		String subject = "subject" + ConfigProperties.getUniqueString();
		String tagname = "tag" + ConfigProperties.getUniqueString();
		
		Shortcut shortcut = Shortcut.S_UNTAG;

		// Create a tag
		app.zGetActiveAccount().soapSend(
				"<CreateTagRequest xmlns='urn:zimbraMail'>" + "<tag name='"
						+ tagname + "' color='1' />" + "</CreateTagRequest>");
		String tagid = app.zGetActiveAccount().soapSelectValue(
				"//mail:CreateTagResponse/mail:tag", "id");

		// Add a message to the mailbox
		FolderItem inboxFolder = FolderItem.importFromSOAP(app
				.zGetActiveAccount(), SystemFolder.Inbox);
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>" + "<m l='"
						+ inboxFolder.getId() + "' t='" + tagid + "'>"
						+ "<content>From: foo@foo.com\n" + "To: foo@foo.com \n"
						+ "Subject: " + subject + "\n" + "MIME-Version: 1.0 \n"
						+ "Content-Type: text/plain; charset=utf-8 \n"
						+ "Content-Transfer-Encoding: 7bit\n" + "\n"
						+ "simple text string in the body\n" + "</content>"
						+ "</m>" + "</AddMsgRequest>");

		// Get the message data from SOAP
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(),
				"subject:(" + subject + ")");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Untag it		
		
		app.zPageMail.zKeyboardShortcut(shortcut);
		SleepUtil.sleepMedium();
			
		app.zGetActiveAccount().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='"
						+ mail.getId() + "'/>" + "</GetMsgRequest>");
		String mailTags = app.zGetActiveAccount().soapSelectValue(
				"//mail:GetMsggResponse//mail:m", "t");

		ZAssert.assertNull(mailTags,
				"Verify that the tag is removed from the message");

	}
	
	@Test (description = "Remove a tag from a message clicking 'x' from tag bubble", groups = { "functional", "L2" })
	public void UnTagMessage_03() throws HarnessException {

		String subject = "subject" + ConfigProperties.getUniqueString();
		String tagname = "tag" + ConfigProperties.getUniqueString();
		
		

		// Create a tag
		app.zGetActiveAccount().soapSend(
				"<CreateTagRequest xmlns='urn:zimbraMail'>" + "<tag name='"
						+ tagname + "' color='1' />" + "</CreateTagRequest>");
		String tagid = app.zGetActiveAccount().soapSelectValue(
				"//mail:CreateTagResponse/mail:tag", "id");

		// Add a message to the mailbox
		FolderItem inboxFolder = FolderItem.importFromSOAP(app
				.zGetActiveAccount(), SystemFolder.Inbox);
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>" + "<m l='"
						+ inboxFolder.getId() + "' t='" + tagid + "'>"
						+ "<content>From: foo@foo.com\n" + "To: foo@foo.com \n"
						+ "Subject: " + subject + "\n" + "MIME-Version: 1.0 \n"
						+ "Content-Type: text/plain; charset=utf-8 \n"
						+ "Content-Transfer-Encoding: 7bit\n" + "\n"
						+ "simple text string in the body\n" + "</content>"
						+ "</m>" + "</AddMsgRequest>");

		// Get the message data from SOAP
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(),
				"subject:(" + subject + ")");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Untag it	pressing 'x' from tag bubble
			
		app.zPageMail.sClickAt(Locators.zUntagBubble,"");
		SleepUtil.sleepMedium();
		
	
		app.zGetActiveAccount().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='"
						+ mail.getId() + "'/>" + "</GetMsgRequest>");
		String mailTags = app.zGetActiveAccount().soapSelectValue(
				"//mail:GetMsggResponse//mail:m", "t");

		ZAssert.assertNull(mailTags,
				"Verify that the tag is removed from the message");

	}


}
