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
package com.zimbra.qa.selenium.projects.universal.tests.contacts.contactgroups;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogMove;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.FormContactGroupNew;

public class MoveContactGroup extends AjaxCommonTest {
	public MoveContactGroup() {
		logger.info("New " + MoveContactGroup.class.getCanonicalName());

		// All tests start at the Address page
		super.startingPage = app.zPageContacts;

		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox", "FALSE");

	}

	@Test(description = "Move a contact group to folder Emailed Contacts by click Move dropdown on toolbar", 
			groups = {"smoke", "L0"})
	public void MoveToEmailedContactsFromMoveDropdownOnToolbar_01() throws HarnessException {

		// -- Data

		// Create the sub addressbook
		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		String foldername = "ab" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + foldername
				+ "' l='" + root.getId() + "' view='contact'/>" + "</CreateFolderRequest>");
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// -- GUI

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the group
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());

		// delete contact group by click Delete on Context menu
		app.zPageContacts.zToolbarPressPulldown(Button.B_MOVE, folder);

		// -- Verification

		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(),
				"is:anywhere #nickname:" + group.getName());
		ZAssert.assertNotNull(actual, "Verify the contact group exists");

		// Verify the contact group is in the trash
		ZAssert.assertEquals(actual.getFolderId(), folder.getId(),
				"Verify the contact group is in the sub addressbook");

	}

	@Test(description = "Move a contact group to folder Emailed Contacts by click Move on Context menu", 
			groups = {"functional", "L2"})
	public void MoveToEmailedContactsClickMoveOnContextmenu_02() throws HarnessException {

		// -- Data

		// Create the sub addressbook
		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		String foldername = "ab" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + foldername
				+ "' l='" + root.getId() + "' view='contact'/>" + "</CreateFolderRequest>");
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// -- GUI

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		DialogMove dialogContactMove = (DialogMove) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_MOVE,
				group.getName());

		// enter the moved folder
		dialogContactMove.zClickTreeFolder(folder);
		dialogContactMove.zClickButton(Button.B_OK);

		// -- Verification

		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(),
				"is:anywhere #nickname:" + group.getName());
		ZAssert.assertNotNull(actual, "Verify the contact group exists");

		// Verify the contact group is in the trash
		ZAssert.assertEquals(actual.getFolderId(), folder.getId(),
				"Verify the contact group is in the sub addressbook");

	}

	@Test(description = "Move a contact group to folder Emailed Contacts with shortcut m", 
			groups = { "functional", "L2"})
	public void MoveToEmailedContactsClickShortcutm_03() throws HarnessException {

		// -- Data

		// Create the sub addressbook
		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		String foldername = "ab" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + foldername
				+ "' l='" + root.getId() + "' view='contact'/>" + "</CreateFolderRequest>");
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// -- GUI

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		SleepUtil.sleepMedium();

		DialogMove dialogContactMove = (DialogMove) app.zPageContacts.zKeyboardShortcut(Shortcut.S_MOVE);
		SleepUtil.sleepMedium();

		// enter the moved folder
		dialogContactMove.zClickTreeFolder(folder);
		SleepUtil.sleepMedium();
		dialogContactMove.zClickButton(Button.B_OK);

		// -- Verification

		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(),
				"is:anywhere #nickname:" + group.getName());
		ZAssert.assertNotNull(actual, "Verify the contact group exists");

		// Verify the contact group is in the trash
		ZAssert.assertEquals(actual.getFolderId(), folder.getId(),
				"Verify the contact group is in the sub addressbook");

	}

	@Test(description = "Move a group to folder Emailed Contacts by click toolbar Edit then open folder dropdown", 
			groups = {"functional", "L2"})
	public void MoveToEmailedContactsClickToolbarEditThenFolderDropdown_04() throws HarnessException {

		// -- Data

		// Create the sub addressbook
		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		String foldername = "ab" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + foldername
				+ "' l='" + root.getId() + "' view='contact'/>" + "</CreateFolderRequest>");
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// -- GUI

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Click Edit
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageContacts
				.zToolbarPressButton(Button.B_EDIT);

		// click location's folder
		DialogMove dialogMove = (DialogMove) formContactGroupNew.zToolbarPressButton(Button.B_CHOOSE_ADDRESSBOOK);

		// enter the moved folder
		dialogMove.zClickTreeFolder(folder);
		dialogMove.zClickButton(Button.B_OK);

		// click Save button
		formContactGroupNew.zToolbarPressButton(Button.B_SAVE);

		// -- Verification

		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(),
				"is:anywhere #nickname:" + group.getName());
		ZAssert.assertNotNull(actual, "Verify the contact group exists");

		// Verify the contact group is in the trash
		ZAssert.assertEquals(actual.getFolderId(), folder.getId(),
				"Verify the contact group is in the sub addressbook");

	}

}
