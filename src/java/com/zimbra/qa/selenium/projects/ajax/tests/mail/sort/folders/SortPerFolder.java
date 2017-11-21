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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.sort.folders;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;

public class SortPerFolder extends SetGroupMailByMessagePreference {

	public SortPerFolder() {
		logger.info("New "+ SortPerFolder.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefReadingPaneLocation", "bottom");
	}


	@Bugs (ids = "30319")
	@Test (description = "Sort a list of messages by subject in folderA and by From in folderB",
			groups = { "functional", "L2" })

	public void SortPerFolder_01() throws HarnessException {

		// Create the message data
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		String subject1 = "bbbb" + ConfigProperties.getUniqueString();
		String subject2 = "aaaa" + ConfigProperties.getUniqueString();
		String subject3 = "bbbb" + ConfigProperties.getUniqueString();
		String subject4 = "aaaa" + ConfigProperties.getUniqueString();
		String foldername1 = "folder1" + ConfigProperties.getUniqueString();
		String foldername2 = "folder2" + ConfigProperties.getUniqueString();

		// Create the folders
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername1 +"' l='"+ inbox.getId() +"'/>" +
                "</CreateFolderRequest>");
		FolderItem folder1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername1);

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername2 +"' l='"+ inbox.getId() +"'/>" +
                "</CreateFolderRequest>");
		FolderItem folder2 = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername2);

		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='"+ folder1.getId() +"' f='f'>"
        	+			"<content>From: bbbb@foo.com\n" // From: bbbb
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

		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='"+ folder1.getId() +"' >"
        	+			"<content>From: aaaa@foo.com\n" // From: aaaa
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

		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='"+ folder2.getId() +"' f='f'>"
        	+			"<content>From: bbbb@foo.com\n" //  From: bbbb
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

		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='"+ folder2.getId() +"' >"
        	+			"<content>From: aaaa@foo.com\n" //  From: aaaa
        	+				"To: foo@foo.com \n"
        	+				"Subject: "+ subject4 +"\n"
        	+				"MIME-Version: 1.0 \n"
        	+				"Content-Type: text/plain; charset=utf-8 \n"
        	+				"Content-Transfer-Encoding: 7bit\n"
        	+				"\n"
        	+				"simple text string in the body\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Click on folder1
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, folder1);

		// First, sort by subject to clear the order
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_LIST_SORTBY_FLAGGED);

		// Now, click on "subject"
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_LIST_SORTBY_SUBJECT);

		// Click on folder2
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, folder2);

		// First, sort by subject to clear the order
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_LIST_SORTBY_FLAGGED);

		// Now, click on "subject"
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_LIST_SORTBY_FROM);

		// Verification

		// Log the preferences
		app.zGetActiveAccount().soapSend(
				"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
    		+		"<pref name='zimbraPrefSortOrder'/>"
			+	"</GetPrefsRequest>");

		// Click on folder1
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, folder1);

		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		// Make sure message1 appears before message2 in the list
		MailItem itemB = null;
		for (MailItem m : messages) {
			if ( subject2.equals(m.gSubject) ) {
				itemB = m;
			}
			if ( subject1.equals(m.gSubject) ) {
				// Item B must be found before Item A (i.e. unflagged appears before flagged)
				ZAssert.assertNotNull(itemB, "Item A is in the list.  Verify Item B has already been found.");
			}
		}

		ZAssert.assertNotNull(itemB, "Verify Item B was found.");

		// Click on folder1
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, folder1);

		messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		// Make sure message1 appears before message2 in the list
		itemB = null;
		for (MailItem m : messages) {
			if ( subject2.equals(m.gSubject) ) {
				itemB = m;
			}
			if ( subject1.equals(m.gSubject) ) {
				// Item B must be found before Item A (i.e. unflagged appears before flagged)
				ZAssert.assertNotNull(itemB, "Item A is in the list.  Verify Item B has already been found.");
			}
		}

		ZAssert.assertNotNull(itemB, "Verify Item B was found.");
	}
}