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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.toaster;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.Toaster;

public class DeleteContactGroup extends AjaxCommonTest {
	public DeleteContactGroup() {
		logger.info("New " + DeleteContactGroup.class.getCanonicalName());

		super.startingPage = app.zPageContacts;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -263733102718446576L; {
				put("zimbraPrefShowSelectionCheckbox", "TRUE");
			}
		};
	}


	@Test (description = "Delete a contact group by click Delete button on toolbar and verify toast msg",
			groups = { "functional", "L2" })

	public void DeleteContactGroupToastMsg_01() throws HarnessException {

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact group
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());

		// Delete contact group by click Delete button on toolbar
		app.zPageContacts.zToolbarPressButton(Button.B_DELETE);

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "1 contact group moved to Trash",
				"Verify toast message: Contact group Moved to Trash");
	}


	@Test (description = "Delete a contact group by click Delete on Context Menu and verify toast msg",
			groups = { "functional", "L2" })

	public void DeleteContactGroupToastMsg_02() throws HarnessException {

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Delete contact group by click Delete on Context menu
		app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_DELETE, group.getName());

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "1 contact group moved to Trash", "Verify toast message: Contact group Moved to Trash");
	}


	@Test (description = "Delete a contact group selected by checkbox by click Delete button on toolbar and verify toast msg",
			groups = { "functional", "L2" })

	public void DeleteContactGroupToastMsg_03() throws HarnessException {

		ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Delete contact group by click Delete button on toolbar
		app.zPageContacts.zToolbarPressButton(Button.B_DELETE);

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "1 contact group moved to Trash", "Verify toast message: Contact group Moved to Trash");
	}


	@Test (description = "Delete a contact group use shortcut Del and verify toast msg",
			groups = { "functional", "L2" })

	public void DeleteContactGroupToastMsg_04() throws HarnessException {

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact group
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());

		// Delete contact group by click shortcut Del
		app.zPageContacts.zKeyboardKeyEvent(KeyEvent.VK_DELETE);

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "1 contact group moved to Trash", "Verify toast message: Contact group Moved to Trash");
	}


	@Test (description = "Delete a contact group use shortcut backspace and verify toast msg",
			groups = { "functional", "L2" })

	public void DeleteContactGroupToastMsg_05() throws HarnessException {

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact group
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());

		// Delete contact group by click shortcut Del
		app.zPageContacts.zKeyboardKeyEvent(KeyEvent.VK_BACK_SPACE);

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "1 contact group moved to Trash", "Verify toast message: Contact group Moved to Trash");
	}


	@Bugs(ids = "78829")
	@Test (description = "Delete multiple contact groups at once and verify toast msg",
			groups = { "functional", "L3" })

	public void DeleteContactGroupToastMsg_06() throws HarnessException {

		// Create a contact group
		ContactGroupItem group1 = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		ContactGroupItem group2 = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		ContactGroupItem group3 = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact group
		app.zPageContacts.zListItem(Action.A_CHECKBOX, group3.getName());
		app.zPageContacts.zListItem(Action.A_CHECKBOX, group1.getName());
		app.zPageContacts.zListItem(Action.A_CHECKBOX, group2.getName());

		// Delete contact group by click Delete button on toolbar
		app.zPageContacts.zToolbarPressButton(Button.B_DELETE);

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "3 contacts moved to Trash", "Verify toast message for moving contacts to contact group");
	}


	@Test (description = "Delete contact + contact group at once and verify toast msg",
			groups = { "functional", "L3" })

	public void DeleteContactGroupToastMsg_07() throws HarnessException {

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact group
		// By default newly created contact will be checked.
		app.zPageContacts.zListItem(Action.A_CHECKBOX, contact.getName());
		app.zPageContacts.zListItem(Action.A_CHECKBOX, group.getName());

		// Delete contact group by click Delete button on toolbar
		app.zPageContacts.zToolbarPressButton(Button.B_DELETE);

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "2 contacts moved to Trash", "Verify toast message: Contact group Moved to Trash");
	}


	@Test (description = "Move a contact group to folder Trash by expand Move dropdown then select Trash and verify toast msg",
			groups = { "functional", "L3" })

	public void DeleteContactGroupToastMsg_08() throws HarnessException {

		// The trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact group
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());

		// Delete contact group by click Delete button on toolbar
		app.zPageContacts.zToolbarPressPulldown(Button.B_MOVE, trash);

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "1 contact group moved to Trash", "Verify toast message: Contact group Moved to Trash");
	}
}