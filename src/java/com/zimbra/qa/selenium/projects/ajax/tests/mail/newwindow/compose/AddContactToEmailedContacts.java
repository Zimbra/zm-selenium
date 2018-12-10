/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.SeparateWindowFormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class AddContactToEmailedContacts extends SetGroupMailByMessagePreference {

	public AddContactToEmailedContacts() {
		logger.info("New "+ AddContactToEmailedContacts.class.getCanonicalName());

		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences.put("zimbraPrefAutoAddAddressEnabled", "TRUE");
	}


	@Test (description = "Bug 62727 - Sending email to from new window should register contact under email contact list ",
			groups = { "functional", "L3" })

	public void AddContactToEmailedContacts_01() throws HarnessException {

		// Set 'Always compose in new window'from preferences
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);
		String composelocator = "css=input[id$='_MAIL_NEW_WINDOW_COMPOSE']"; //Locator for selecting 'Always compose new window'
		app.zPagePreferences.zCheckboxSet(composelocator, true);
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		FolderItem emailedContacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.EmailedContacts);
		String windowTitle = "Zimbra: Compose";

		ZimbraAccount receiver = new ZimbraAccount();
		receiver.provision();
		receiver.authenticate();

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(receiver));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		SeparateWindowFormMailNew window = null;

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

		// Soap verification
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='contact'>"
						+		"<query>"+ receiver.EmailAddress +"</query>"
						+	"</SearchRequest>");

		String email = app.zGetActiveAccount().soapSelectValue("//mail:cn//mail:a[@n='email']", null);
		ZAssert.assertEquals(email, receiver.EmailAddress, "Verify the recipient is in the contacts folder");

		String folder = app.zGetActiveAccount().soapSelectValue("//mail:cn", "l");
		ZAssert.assertEquals(folder, emailedContacts.getId(), "Verify the recipient is in the Emailed Contacts folder");

		// UI verification
		app.zPageContacts.zNavigateTo();
		app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, emailedContacts);

		ZAssert.assertTrue(app.zPageContacts.sIsElementPresent("css=div[id^='zlif__CNS-main__'][id$='__fileas']:contains('"+ receiver.DisplayName + "')"), "Verify that receiver contact is present in emailed contact list");
	}
}