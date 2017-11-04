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

import java.awt.event.KeyEvent;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class DeleteFolder extends AjaxCommonTest {

	public DeleteFolder() {
		logger.info("New " + DeleteFolder.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}

	@Test(description = "Delete a top level addressbook - Right click, Delete", 
			groups = { "smoke", "L1"})
	public void DeleteTopLevelFolderFromContextmenu_01() throws HarnessException {

		// -- Data

		FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);

		String name = "ab" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name
				+ "' view='contact' l='" + userRoot.getId() + "'/>" + "</CreateFolderRequest>");
		FolderItem folderItem = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// -- GUI

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Delete the folder using context menu
		app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, Button.B_DELETE, folderItem);

		// -- Verification

		// Get the folder again
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Verify the ab is moved to trash
		ZAssert.assertEquals(actual.getParentId(), trash.getId(), "Verify the ab is moved to trash");

	}

	@Test(description = "Delete a sub folder - Right click, Delete", 
			groups = { "functional", "L2"})
	public void DeleteSubFolderFromContextmenu_02() throws HarnessException {

		// -- Data

		FolderItem contact = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);

		String name = "ab" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name
				+ "' view='contact' l='" + contact.getId() + "'/>" + "</CreateFolderRequest>");
		FolderItem folderItem = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// -- GUI

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Delete the folder using context menu
		app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, Button.B_DELETE, folderItem);

		// -- Verification

		// Get the folder again
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Verify the ab is moved to trash
		ZAssert.assertEquals(actual.getParentId(), trash.getId(), "Verify the ab is moved to trash");

	}

	@Test(description = "Drag one sub folder to Trash folder", 
			groups = { "functional", "L2"})
	public void DnDFromSubFolderToTrash_03() throws HarnessException {

		// -- Data

		FolderItem contact = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);

		String name = "ab" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name
				+ "' view='contact' l='" + contact.getId() + "'/>" + "</CreateFolderRequest>");
		FolderItem folderItem = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// -- GUI

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Delete the folder DND
		app.zPageContacts.zDragAndDrop(
				"css=td#zti__main_Contacts__" + folderItem.getId() + "_textCell:contains(" + folderItem.getName() + ")",
				"css=td#zti__main_Contacts__" + trash.getId() + "_textCell:contains(" + trash.getName() + ")");

		// -- Verification

		// Get the folder again
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Verify the ab is moved to trash
		ZAssert.assertEquals(actual.getParentId(), trash.getId(), "Verify the ab is moved to trash");

	}

	@Bugs(ids = "103601")
	@Test(description = "Delete an addressbook folder- Use shortcut Del", 
			groups = { "functional", "application-bug"})

	public void UseShortcutDel_04() throws HarnessException {

		// -- Data

		FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);

		String name = "ab" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name
				+ "' view='contact' l='" + userRoot.getId() + "'/>" + "</CreateFolderRequest>");
		FolderItem folderItem = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// -- GUI

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the folder
		app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, folderItem);

		// Delete the folder using shortcut Del
		app.zPageContacts.zKeyboardKeyEvent(KeyEvent.VK_DELETE);

		// -- Verification

		// Get the folder again
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Verify the ab is moved to trash
		ZAssert.assertEquals(actual.getParentId(), trash.getId(), "Verify the ab is moved to trash");

	}

}
