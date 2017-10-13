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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.group.messages;

import java.util.List;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.OperatingSystem;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.PagePreferences.Locators;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;


public class SortByDateGroupByDateAscending extends PrefGroupMailByMessageTest {


	public SortByDateGroupByDateAscending() {
		logger.info("New "+ SortByDateGroupByDateAscending.class.getCanonicalName());
		super.startingPage = app.zPagePreferences;
		
		super.startingAccountPreferences.put("zimbraPrefReadingPaneLocation", "bottom");
		super.startingAccountPreferences.put("zimbraPrefGroupMailBy", "message");
		super.startingAccountPreferences.put("zimbraPrefMailPollingInterval", "500");
	}
	
	@Bugs(ids = "ZCS-1337")
	@Test( description = "Sort a list of messages by Date in ascending order and Group by Date",
			groups = { "functional", "L2" })
	
	public void SortByDateGroupByDateAscending_01() throws HarnessException {

		if (OperatingSystem.isWindows() == true && !ConfigProperties.getStringProperty("browser").contains("msedge")) {

			// File (export of an Inbox which contained mail of different past dates) to import. 
			// The is required so that mails can be grouped by date and issue can be reproduced.
			final String fileName = "pastMails.tgz";
			final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\tgz\\" + fileName;

			// Navigate to preferences -> Import/Export
			app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.ImportExport);

			// Click on Browse/Choose File button
			app.zPagePreferences.sClickAt(Locators.zBrowseFileButton,"0,0");

			// Enter the path of the exported file and upload
			zUpload(filePath);

			// Click on Import button
			app.zPagePreferences.zPressButton(Button.B_IMPORT);
			SleepUtil.sleepMedium();

			// Check that file is imported successfully
			ZAssert.assertStringContains(app.zPagePreferences.sGetText(Locators.zImportDialogContent),"Import succeeded.","Import is unsuccessfull!");

			// Click OK on confirmation dialog after import
			app.zPagePreferences.zPressButton(Button.B_IMPORT_OK);

			// Go to Mail tab
			app.zPageMail.zNavigateTo();

			// Create the message data
			FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
			String subjectA = "subjectA" + ConfigProperties.getUniqueString();
			String subjectB = "subjectB" + ConfigProperties.getUniqueString(); 

			// Add the message
			app.zGetActiveAccount().soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
							+		"<m l='"+ inbox.getId() +"'>"
							+			"<content>From: "+ ZimbraAccount.AccountA().EmailAddress + "\n"
							+				"To: foo@foo.com \n"
							+				"Subject: "+ subjectA +"\n"
							+				"MIME-Version: 1.0 \n"
							+				"Content-Type: text/plain; charset=utf-8 \n"
							+				"Content-Transfer-Encoding: 7bit\n"
							+				"\n"
							+				"simple text string in the body\n"
							+			"</content>"
							+		"</m>"
							+	"</AddMsgRequest>");
			// Group by Date
			app.zGetActiveAccount().soapSend(
					"<SetMailboxMetadataRequest xmlns='urn:zimbraMail'>"
							+		"<meta section='zwc:implicit'>"
							+			"<a n='zimbraPrefGroupByList'>2:GROUPBY_DATE</a>"
							+		"</meta>"
							+	"</SetMailboxMetadataRequest>");

			// Refresh web-client to load the changes done. It is required as the next soap request overwrites the changes done by previous request if done without refresh. 
			app.zPageMain.sRefresh();

			// Sort by Date in ascending order
			app.zGetActiveAccount().soapSend(
					"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
							+ 		"<pref name='zimbraPrefSortOrder'>2:dateAsc,BDLV:,CAL:,CLV:,CNS:,CNSRC:,CNTGT:,CV:,TKL:,TV:,TV-main:dateAsc</pref>"
							+ 	"</ModifyPrefsRequest>");

			// Refresh web-client to load the changes done
			app.zPageMain.sRefresh();

			// Click on Inbox
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);

			app.zPageMail.zListItem(Action.A_LEFTCLICK, subjectA);

			// Add a message
			app.zGetActiveAccount().soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
							+		"<m l='"+ inbox.getId() +"' >"
							+			"<content>From: "+ ZimbraAccount.AccountA().EmailAddress +"\n"
							+				"To: foo@foo.com \n"
							+				"Subject: "+ subjectB +"\n"
							+				"MIME-Version: 1.0 \n"
							+				"Content-Type: text/plain; charset=utf-8 \n"
							+				"Content-Transfer-Encoding: 7bit\n"
							+				"\n"
							+				"simple text string in the body\n"
							+			"</content>"
							+		"</m>"
							+	"</AddMsgRequest>");


			// Messages are stored in the list in the order in which they are displayed in mail list view.
			List<MailItem> messages = app.zPageMail.zListGetMessages();
			ZAssert.assertNotNull(messages, "Verify the message list exists");

			// Check that the last 2 messages received are at the bottom of the list.
			ZAssert.assertEquals(messages.get(messages.size()-2).gSubject, subjectA, "Mails are either not grouped and or not displayed in the Ascending Date order!");
			ZAssert.assertEquals(messages.get(messages.size()-1).gSubject, subjectB, "Mails are either not grouped and or not displayed in the Ascending Date order!");
		}
	}
}

