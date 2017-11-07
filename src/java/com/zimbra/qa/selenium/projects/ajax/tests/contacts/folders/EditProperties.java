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
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.DialogEditFolder;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DialogEditFolder.FolderColor;

public class EditProperties extends AjaxCommonTest {

	public EditProperties() {
		logger.info("New " + EditProperties.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test (description = "Edit a folder, change the color (Context menu -> Edit)",
			groups = { "functional", "L3" })

	public void ChangeColorOfTopLevelFolder_01() throws HarnessException {

		FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);

		// Create a folder
		String name = "ab" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name
				+ "' view='contact' l='" + userRoot.getId() + "'/>" + "</CreateFolderRequest>");
		FolderItem folderItem = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Change the folder's color using context menu
		DialogEditFolder dialog = (DialogEditFolder) app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, folderItem);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		dialog.zSetNewColor(FolderColor.Blue);
		dialog.zClickButton(Button.B_OK);

		// Get the folder again
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertEquals(actual.getColor(), "1", "Verify the color of the folder is set to blue (1)");
	}


	@Test (description = "Edit a folder, change the color (Context menu -> Edit)",
			groups = { "functional", "L3" })

	public void ChangeColorOfSystemFolders_02() throws HarnessException {

		FolderItem contacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Change the folder's color using context menu
		DialogEditFolder dialog = (DialogEditFolder) app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, contacts);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		dialog.zSetNewColor(FolderColor.Green);
		dialog.zClickButton(Button.B_OK);

		// Get the folder again
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), contacts.getName());
		ZAssert.assertEquals(actual.getColor(), "3", "Verify the color of the folder is set to green (3)");
	}


	@Test (description = "Edit a folder, change the color (Context menu -> Edit)",
			groups = { "functional", "L3" })

	public void ChangeColorOfSubFolder_03() throws HarnessException {

		FolderItem contacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);

		// Create a folder
		String name = "ab" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name
				+ "' view='contact' l='" + contacts.getId() + "'/>" + "</CreateFolderRequest>");
		FolderItem folderItem = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Change the folder's color using context menu
		DialogEditFolder dialog = (DialogEditFolder) app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, folderItem);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		dialog.zSetNewColor(FolderColor.Red);
		dialog.zClickButton(Button.B_OK);

		// Get the folder again
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertEquals(actual.getColor(), "5", "Verify the color of the folder is set to red (5)");
	}


	@Test (description = "Edit a folder, change name(Context menu -> Edit)",
			groups = { "smoke", "L1" })

	public void ChangeNameOfTopLevelFolder_04() throws HarnessException {

		FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);

		// Create a folder
		String name = "ab" + ConfigProperties.getUniqueString();
		String newname = "ab" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name
				+ "' view='contact' l='" + userRoot.getId() + "'/>" + "</CreateFolderRequest>");
		FolderItem folderItem = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Change the folder's color using context menu
		DialogEditFolder dialog = (DialogEditFolder) app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, folderItem);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		dialog.zSetNewName(newname);
		dialog.zClickButton(Button.B_OK);

		// Get the folder again
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNull(actual, "Verify the old addressbook does not exist");

		actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), newname);
		ZAssert.assertNotNull(actual, "Verify the new addressbook exists");
	}


	@Test (description = "Edit a folder, change name(Context menu -> Edit)",
			groups = { "smoke", "L1" })

	public void ChangeNameOfSubFolder_05() throws HarnessException {

		FolderItem contacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);

		// Create a folder
		String name = "ab" + ConfigProperties.getUniqueString();
		String newname = "ab" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name
				+ "' view='contact' l='" + contacts.getId() + "'/>" + "</CreateFolderRequest>");
		FolderItem folderItem = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Change the folder's color using context menu
		DialogEditFolder dialog = (DialogEditFolder) app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, folderItem);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		dialog.zSetNewName(newname);
		dialog.zClickButton(Button.B_OK);

		// Get the folder again
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNull(actual, "Verify the old addressbook does not exist");

		actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), newname);
		ZAssert.assertNotNull(actual, "Verify the new addressbook exists");
	}


	@Test (description = "Edit a top level folder, change name and color Context menu -> Edit)",
			groups = { "functional", "L2" })

	public void ChangeNameColorOfTopLevelFolder_06() throws HarnessException {

		FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);

		// Create a folder
		String name = "ab" + ConfigProperties.getUniqueString();
		String newname = "ab" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name
				+ "' view='contact' l='" + userRoot.getId() + "'/>" + "</CreateFolderRequest>");
		FolderItem folderItem = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Change the folder's color using context menu
		DialogEditFolder dialog = (DialogEditFolder) app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, folderItem);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		dialog.zSetNewColor(FolderColor.Yellow);
		dialog.zSetNewName(newname);
		dialog.zClickButton(Button.B_OK);

		// Get the folder again
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNull(actual, "Verify the old addressbook does not exist");

		actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), newname);
		ZAssert.assertNotNull(actual, "Verify the new addressbook exists");
		ZAssert.assertEquals(actual.getColor(), "6", "Verify the color of the folder is set to yellow (6)");
	}


	@Test (description = "Edit a subfolder, change name and color Context menu -> Edit)",
			groups = { "functional", "L2" })

	public void ChangeNameColorOfSubFolder_07() throws HarnessException {

		FolderItem contacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);

		// Create a folder
		String name = "ab" + ConfigProperties.getUniqueString();
		String newname = "ab" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name
				+ "' view='contact' l='" + contacts.getId() + "'/>" + "</CreateFolderRequest>");
		FolderItem folderItem = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Change the folder's color using context menu
		DialogEditFolder dialog = (DialogEditFolder) app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, folderItem);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		dialog.zSetNewColor(FolderColor.Orange);
		dialog.zSetNewName(newname);
		dialog.zClickButton(Button.B_OK);

		// Get the folder again
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNull(actual, "Verify the old addressbook does not exist");

		actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), newname);
		ZAssert.assertNotNull(actual, "Verify the new addressbook exists");
		ZAssert.assertEquals(actual.getColor(), "9", "Verify the color of the folder is set to orange (9)");
	}
}