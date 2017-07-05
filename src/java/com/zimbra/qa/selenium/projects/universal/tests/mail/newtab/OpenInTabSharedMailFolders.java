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
package com.zimbra.qa.selenium.projects.universal.tests.mail.newtab;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderMountpointItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.*;

public class OpenInTabSharedMailFolders extends PrefGroupMailByMessageTest {

	public OpenInTabSharedMailFolders() {
		logger.info("New "+ OpenInTabMailFolder.class.getCanonicalName());

		// All tests start at the login page
		super.startingPage = app.zPageMail;
	}

	@Test( description = "Verify Open in new tab option for shared folder - custom folder",
			groups = { "functional", "L2" })
	public void OpenInTabSharedMailFolders_01() throws HarnessException {

		String foldername = "folder" + ConfigProperties.getUniqueString();
		String subject = "subject" + ConfigProperties.getUniqueString();
		String mountpointname = "mountpoint" + ConfigProperties.getUniqueString();
		
		FolderItem inbox = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox);
		
		// Create a folder to share
		ZimbraAccount.AccountA().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" + inbox.getId() + "'/>"
				+	"</CreateFolderRequest>");
		
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), foldername);
		
		// Share it
		ZimbraAccount.AccountA().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidxa'/>"
				+		"</action>"
				+	"</FolderActionRequest>");
		
		// Add a message to it
		ZimbraAccount.AccountA().soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
        		+		"<m l='"+ folder.getId() +"' f='u'>"
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
		
		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointname +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.AccountA().ZimbraId +"'/>"
				+	"</CreateMountpointRequest>");
		
		FolderMountpointItem mountpoint = FolderMountpointItem.importFromSOAP(app.zGetActiveAccount(), mountpointname);
		
		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Right click on folder, select "Open in Tab"
		app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_OPENTAB, mountpoint);
		
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
	
	@Test( description = "Verify Open in new tab option for shared folder - Inbox folder",
			groups = { "functional", "L2" })
	public void OpenInTabSharedMailFolders_02() throws HarnessException {

		String foldername = "tabmountfolder" + ConfigProperties.getUniqueString();
		String subject = "subject" + ConfigProperties.getUniqueString();
		String mountpointname = "mountpoint" + ConfigProperties.getUniqueString();
		String locator= "css=div[id^='zti__main_Mail'] span:contains("+ foldername +")";
		String OpenInTab = "css=div[id='ZmActionMenu_mail_FOLDER'] div[id^='OPEN_IN_TAB'] td[id$='_title']";
		
		FolderItem inbox = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox);
		
		// Create a folder 
		ZimbraAccount.AccountA().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" + inbox.getId() + "'/>"
				+	"</CreateFolderRequest>");
	
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), foldername);
		
		// Share it
		ZimbraAccount.AccountA().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ inbox.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidxa'/>"
				+		"</action>"
				+	"</FolderActionRequest>");
		
		// Add a message to it
		ZimbraAccount.AccountA().soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
        		+		"<m l='"+ folder.getId() +"' f='u'>"
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
		
		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointname +"'  rid='"+ inbox.getId() +"' zid='"+ ZimbraAccount.AccountA().ZimbraId +"'/>"
				+	"</CreateMountpointRequest>");
		
		FolderMountpointItem mountpoint = FolderMountpointItem.importFromSOAP(app.zGetActiveAccount(), mountpointname);
		
		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		
		// Expand mountpoint folder
		app.zTreeMail.zTreeItem(Action.A_TREE_EXPAND, mountpoint);
		
		//Click on subfolder
		SleepUtil.sleep(5000);
		app.zTreeMail.zRightClickAt(locator,"");
		
		//Click on Open in tab option
		app.zTreeMail.zClickAt(OpenInTab, "");
				
		// Wait for the page
		SleepUtil.sleep(5000);
		
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
