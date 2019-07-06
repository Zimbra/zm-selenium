/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.newtab;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.search.PageSearch;

public class OpenInTabMailFolder extends SetGroupMailByMessagePreference {

	public OpenInTabMailFolder() {
		logger.info("New "+ OpenInTabMailFolder.class.getCanonicalName());
		super.startingPage = app.zPageMail;
	}


	@Test (description = "Verify Open in new tab option for mail app - Inbox",
			groups = { "bhr" })

	public void OpenInTabMailFolder_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();

		// Send the message from AccountA to the ZCS user
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>"+ "body" + ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Right click on folder, select "Open in Tab"
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_OPENTAB,inbox);

		// Remember to close the search view
		try {

			// Get all the messages in the inbox
			List<MailItem> messages = app.zPageSearch.zListGetMessages();
			ZAssert.assertNotNull(messages, "Verify the message list exists");

			ZAssert.assertEquals(messages.size(), 1, "Verify only the one message was returned");
			ZAssert.assertEquals(messages.get(0).gSubject, subject, "Verify the message's subject matches");

		} finally {
			// Remember to close the search view
			app.zPageSearch.zClose();
		}
	}


	@Test (description = "Verify Open in new tab option for mail app - Draft",
			groups = { "sanity" })

	public void OpenInTabMailFolder_02() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);

		// Save the message
		mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
		mailform.zToolbarPressButton(Button.B_CANCEL);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Get the message from the server
		MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(),
				"subject:("+ subject +")");

		// Right click on folder, select "Open in Tab"
		FolderItem draftsFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Drafts);
		app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_OPENTAB,draftsFolder);

		// Remember to close the search view
		try {

			// Verify search window is opened
			app.zPageSearch.zIsActive();

			// Verify the draft message is exist
			ZAssert.assertEquals(draft.dSubject, subject, "Verify the subject field is correct");

		} finally {
			// Remember to close the search view
			app.zPageSearch.zClose();
		}
	}


	@Test (description = "Verify Open in new tab option for mail app - Custom folder",
			groups = { "sanity" })

	public void OpenInTabMailFolder_03() throws HarnessException {

		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		ZAssert.assertNotNull(inbox, "Verify the inbox is available");

		// Create the subfolder
		String name1 = "folder" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='"+ name1 +"' l='"+ inbox.getId() +"'/>" +
				"</CreateFolderRequest>");

		FolderItem subfolder1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name1);
		ZAssert.assertNotNull(subfolder1, "Verify the subfolder is available");

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();

		// Add message to the new subfolder
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>" +
				"<m l='"+ subfolder1.getId() +"' f='u'>" +
				"<content>From: foo@foo.com\n" +
				"To: foo@foo.com \n" +
				"Subject: "+ subject +"\n" +
				"MIME-Version: 1.0 \n" +
				"Content-Type: text/plain; charset=utf-8 \n" +
				"Content-Transfer-Encoding: 7bit\n" +
				"\n" +
				"simple text string in the body\n" +
				"</content>" +
				"</m>" +
		"</AddMsgRequest>");

		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Right click on folder, select "Open in Tab"
		app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_OPENTAB, subfolder1);
		ZAssert.assertTrue(app.zPageSearch.sIsElementPresent(PageSearch.Locators.zSearchTab), "Search tab should open");

		// Remember to close the search view
		try {

			// Get all the messages in the inbox
			List<MailItem> messages = app.zPageSearch.zListGetMessages();
			ZAssert.assertNotNull(messages, "Verify the message list exists");

			ZAssert.assertEquals(messages.size(), 1, "Verify only the one message was returned");
			ZAssert.assertEquals(messages.get(0).gSubject, subject, "Verify the message's subject matches");

		} finally {
			// Remember to close the search view
			app.zPageSearch.zClose();
		}
	}
}