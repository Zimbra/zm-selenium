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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.folders;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogRenameFolder;

public class RenameFolder extends AjaxCore {

	public RenameFolder() {
		logger.info("New " + RenameFolder.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test (description = "Rename a folder - Context menu -> Rename",
			groups = { "smoke", "L1" })

	public void SelectFolderRenameOnContextMenu_01() throws HarnessException {

		// Root folder
		FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);

		// Subfolders in root
		String name = "ab" + ConfigProperties.getUniqueString();
		String name2 = "ab" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name
				+ "' view='contact' l='" + userRoot.getId() + "'/>" + "</CreateFolderRequest>");
		FolderItem folderItem = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Rename the folder using context menu
		DialogRenameFolder dialog = (DialogRenameFolder) app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, Button.B_RENAME, folderItem);

		dialog.zSetNewName(name2);
		dialog.zPressButton(Button.B_OK);

		// Verification
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNull(actual, "Verify the old folder name no longer exists");

		actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), name2);
		ZAssert.assertNotNull(actual, "Verify the new folder name exists");
	}


	@Test (description = "Rename a sub folder - Context menu -> Rename",
			groups = { "functional", "L3" })

	public void SelectSubFolderRenameOnContextMenu_02() throws HarnessException {

		// Contacts folder
		FolderItem contacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);

		// Subfolders in root
		String name = "ab" + ConfigProperties.getUniqueString();
		String name2 = "ab" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name
				+ "' view='contact' l='" + contacts.getId() + "'/>" + "</CreateFolderRequest>");
		FolderItem folderItem = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Rename the folder using context menu
		DialogRenameFolder dialog = (DialogRenameFolder) app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, Button.B_RENAME, folderItem);

		dialog.zSetNewName(name2);
		dialog.zPressButton(Button.B_OK);

		// Verification
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNull(actual, "Verify the old folder name no longer exists");

		actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), name2);
		ZAssert.assertNotNull(actual, "Verify the new folder name exists");
	}


	@Test (description = "Cannot rename an addressbook system folder- Right click, Rename option disabled",
			groups = { "functional", "L3" })

	public void SystemFoldersRenameButtonDisabledFromContextmenu_03() throws HarnessException {

		boolean exists;
		String locator = "css=div[id^='ZmActionMenu_contacts_ADDRBOOK'] div[id^='RENAME_FOLDER'].ZDisabled";

		// -- GUI
		FolderItem contacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);

		// Rename the folder using context menu
		app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, contacts);

		// Verify Rename option is disabled
		exists = app.zTreeContacts.sIsElementPresent(locator);
		ZAssert.assertTrue(exists, "Verify Rename option is disabled");

		// -- GUI
		FolderItem emailedContacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.EmailedContacts);

		// Rename the folder using context menu
		app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, emailedContacts);

		// Verify Rename option is disabled
		exists = app.zTreeContacts.sIsElementPresent(locator);
		ZAssert.assertTrue(exists, "Verify Rename option is disabled");

		// -- GUI
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);

		// Rename the folder using context menu
		app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, trash);

		// Verify Rename option is disabled
		exists = app.zTreeContacts.sIsElementPresent(locator);
		ZAssert.assertTrue(exists, "Verify Rename option is disabled");
	}
}