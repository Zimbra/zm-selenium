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
package com.zimbra.qa.selenium.projects.universal.tests.contacts.folders;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogWarning;

public class EmptyTrashFolder extends AjaxCommonTest {

	public EmptyTrashFolder() {
		logger.info("New " + EmptyTrashFolder.class.getCanonicalName());

		// All tests start at the login page
		super.startingPage = app.zPageContacts;

		// Enable user preference checkboxes

	}

	@Test(description = "Delete a contact, group, and folder permanently by Empty Trash folder on context menu", 
			groups = {"smoke", "L1"})
	public void ClickOK_02() throws HarnessException {

		// -- Data

		// The trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);

		// Create a contact group via Soap
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Move to trash
		app.zGetActiveAccount().soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>" + "<action op='move' id='"
				+ group.getId() + "' l='" + trash.getId() + "'/>" + "</ItemActionRequest>");

		// Create a contact via Soap
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());

		// Move to trash
		app.zGetActiveAccount().soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>" + "<action op='move' id='"
				+ contact.getId() + "' l='" + trash.getId() + "'/>" + "</ItemActionRequest>");

		// Create a new folder in trash
		String name = "ab" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name
				+ "' view='contact' l='" + trash.getId() + "'/>" + "</CreateFolderRequest>");

		// -- GUI

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Now open empty trash dialog
		DialogWarning dialogWarning = (DialogWarning) app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK,
				Button.B_TREE_FOLDER_EMPTY, trash);

		// Click OK
		dialogWarning.zClickButton(Button.B_OK);

		// -- Verification

		// Verify items are permanently deleted

		// Verify Trash folder is empty
		ContactItem actualContact = ContactItem.importFromSOAP(app.zGetActiveAccount(),
				"is:anywhere #firstname:" + contact.firstName);
		ZAssert.assertNull(actualContact, "Verify the contact is deleted");

		ContactGroupItem actualGroup = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(),
				"is:anywhere " + group.getName());
		ZAssert.assertNull(actualGroup, "Verify the contact group is deleted");

		FolderItem actualAddressbook = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNull(actualAddressbook, "Verify the addressbook is deleted");

	}

	@Test(description = "Cancel Empty Trash folder option", 
			groups = { "functional", "L2"})
	public void ClickCancel_03() throws HarnessException {

		// -- Data

		// The trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);

		// Create a contact group via Soap
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Move to trash
		app.zGetActiveAccount().soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>" + "<action op='move' id='"
				+ group.getId() + "' l='" + trash.getId() + "'/>" + "</ItemActionRequest>");

		// Create a contact via Soap
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());

		// Move to trash
		app.zGetActiveAccount().soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>" + "<action op='move' id='"
				+ contact.getId() + "' l='" + trash.getId() + "'/>" + "</ItemActionRequest>");

		// Create a new folder in trash
		String name = "ab" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name
				+ "' view='contact' l='" + trash.getId() + "'/>" + "</CreateFolderRequest>");

		// -- GUI

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Now open empty trash dialog
		DialogWarning dialogWarning = (DialogWarning) app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK,
				Button.B_TREE_FOLDER_EMPTY, trash);

		// Click OK
		dialogWarning.zClickButton(Button.B_CANCEL);

		// -- Verification

		// Verify items are permanently deleted

		// Verify Trash folder is empty
		ContactItem actualContact = ContactItem.importFromSOAP(app.zGetActiveAccount(),
				"is:anywhere #firstname:" + contact.firstName);
		ZAssert.assertNotNull(actualContact, "Verify the contact is not deleted");

		ContactGroupItem actualGroup = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(),
				"is:anywhere " + group.getName());
		ZAssert.assertNotNull(actualGroup, "Verify the contact group is not deleted");

		FolderItem actualAddressbook = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(actualAddressbook, "Verify the addressbook is not deleted");

	}

}
