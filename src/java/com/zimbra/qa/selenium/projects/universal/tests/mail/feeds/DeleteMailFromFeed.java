/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.mail.feeds;

import java.net.MalformedURLException;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogWarning;

public class DeleteMailFromFeed extends PrefGroupMailByMessageTest {

	public DeleteMailFromFeed() {
		logger.info("New " + DeleteMailFromFeed.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox", "TRUE");
	}

	@Bugs( ids = "102261")
	@Test( description = "Verify the deletion of a mail from feed folder", 
			groups = { "functional", "L2" })

	public void DeleteMailFromFeed_01() throws HarnessException, MalformedURLException {

		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(),
				SystemFolder.UserRoot);
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(),
				SystemFolder.Trash);

		String foldername = "feed"
				+ ConfigProperties.getUniqueString();

		// feed.rss=http://server/files/Service/RSS/Basic/basic.xml
		String feed = ConfigProperties.getStringProperty("rss.sample");
		String feedMailSubject = "RssItemNumberOneTitle";

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
						+ "<folder name='" + foldername + "' l='"
						+ root.getId() + "' url='" + feed + "'/>"
						+ "</CreateFolderRequest>");

		FolderItem feedFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(),
				foldername);

		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Click on the "Feed"
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, feedFolder);
		
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, feedMailSubject);

		// Click delete
		app.zPageMail.zToolbarPressButton(Button.B_DELETE);
		
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(feedMailSubject),"The deleted feed mail is present in feed folder even after deletion!");
		
		//Verify that the deleted feed mail is present is Trash
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, trash);
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(feedMailSubject),"The deleted feed mail is not present in trash!");
	}
	
	@Test( description = "Verify the deletion of mutiple mails from feed folder using context menu delete button", 
			groups = { "functional", "L3" })

	public void DeleteMailFromFeed_02() throws HarnessException, MalformedURLException {

		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(),
				SystemFolder.UserRoot);
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(),
				SystemFolder.Trash);

		String foldername = "feed"
				+ ConfigProperties.getUniqueString();

		// feed.rss=http://server/files/Service/RSS/Basic/basic.xml
		String feed = ConfigProperties.getStringProperty("rss.sample");
		String feedMailSubject1 = "RssItemNumberOneTitle";
		String feedMailSubject2 = "RssItemNumberTwoTitle";

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
						+ "<folder name='" + foldername + "' l='"
						+ root.getId() + "' url='" + feed + "'/>"
						+ "</CreateFolderRequest>");

		FolderItem feedFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(),
				foldername);

		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Click on the "Feed"
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, feedFolder);

		// Select all two mail
		app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, feedMailSubject1);
		app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, feedMailSubject2);

		// Right click the item, select delete
		app.zPageMail.zListItem(Action.A_RIGHTCLICK, Button.B_DELETE, feedMailSubject2);
		
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(feedMailSubject1),"The deleted feed mail is present in feed folder even after deletion!");
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(feedMailSubject2),"The deleted feed mail is present in feed folder even after deletion!");
		
		//Verify that the deleted feed mail is present is Trash
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, trash);
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(feedMailSubject1),"The deleted feed mail is not present in trash!");
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(feedMailSubject2),"The deleted feed mail is not present in trash!");
	}
	
	@Test( description = "Verify the hard deletion of a mail from feed folder using 'shift-del' shortcut", 
			groups = { "functional", "L3" })

	public void DeleteMailFromFeed_03() throws HarnessException, MalformedURLException {

		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(),
				SystemFolder.UserRoot);
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(),
				SystemFolder.Trash);

		String foldername = "feed"
				+ ConfigProperties.getUniqueString();

		// feed.rss=http://server/files/Service/RSS/Basic/basic.xml
		String feed = ConfigProperties.getStringProperty("rss.sample");
		String feedMailSubject = "RssItemNumberOneTitle";

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
						+ "<folder name='" + foldername + "' l='"
						+ root.getId() + "' url='" + feed + "'/>"
						+ "</CreateFolderRequest>");

		FolderItem feedFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(),
				foldername);

		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Click on the "Feed"
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, feedFolder);
		
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, feedMailSubject);

		// Click shift-delete
		DialogWarning dialog = (DialogWarning)app.zPageMail.zKeyboardShortcut(Shortcut.S_MAIL_HARDELETE);
		dialog.zPressButton(Button.B_OK);
		
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(feedMailSubject),"The deleted feed mail is present in feed folder even after deletion!");
		
		//Verify that the deleted feed mail is not present is Trash as well
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, trash);
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(feedMailSubject),"The deleted feed mail is present in trash!");
	}
}
