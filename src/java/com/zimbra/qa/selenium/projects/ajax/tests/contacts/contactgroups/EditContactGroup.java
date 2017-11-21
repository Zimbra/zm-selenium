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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.contactgroups;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.ContactGroupItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.pages.contacts.*;
import com.zimbra.qa.selenium.projects.ajax.pages.contacts.FormContactGroupNew.Field;

public class EditContactGroup extends AjaxCore  {

	public EditContactGroup() {
		logger.info("New "+ EditContactGroup.class.getCanonicalName());
		super.startingPage =  app.zPageContacts;
	}


	@Bugs (ids = "97157")
	@Test (description = "Edit a contact group by click Edit on Toolbar button",
			groups = { "smoke", "L0" })

	public void EditContactGroup_01() throws HarnessException {

		// A new group name
		String newname = "edit" + ConfigProperties.getUniqueString();

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact group
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());

		// Click Edit on Toolbar button
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageContacts.zToolbarPressButton(Button.B_EDIT);

		// Change the group name
		formContactGroupNew.zFillField(Field.GroupName, newname);
		formContactGroupNew.zSubmit();

		// Verification
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ newname);
		ZAssert.assertNotNull(actual, "Verify the contact group with new name exists");
	}


	@Bugs (ids = "97157")
	@Test (description = "Edit a contact group by click Edit Group on Context Menu ",
			groups = { "smoke", "L1" })

	public void EditContactGroup_02() throws HarnessException {

		// A new group name
		String newname = "edit" + ConfigProperties.getUniqueString();

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Right click -> Edit
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_EDIT, group.getName());

		// Change the group name
		formContactGroupNew.zFillField(Field.GroupName, newname);
		formContactGroupNew.zSubmit();

		// Verification
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ newname);
		ZAssert.assertNotNull(actual, "Verify the contact group with new name exists");
	}


	@Bugs (ids = "97157")
	@Test (description = "Edit a contact group by double click on the contact group",
			groups = { "smoke", "L1" })

	public void EditContactGroup_03() throws HarnessException {

		// A new group name
		String newname = "edit" + ConfigProperties.getUniqueString();

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Double click
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageContacts.zListItem(Action.A_DOUBLECLICK, group.getName());

		// Change the group name
		formContactGroupNew.zFillField(Field.GroupName, newname);
		formContactGroupNew.zSubmit();

		// Verification
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ newname);
		ZAssert.assertNotNull(actual, "Verify the contact group with new name exists");
	}


	@Test (description = "Cancel Editing a contact group by click Close",
			groups = { "functional", "L2" })

	public void EditContactGroup_04() throws HarnessException {

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact group
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());

		// Click Edit on Toolbar button
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageContacts.zToolbarPressButton(Button.B_EDIT);

		// Change the group name
		formContactGroupNew.zToolbarPressButton(Button.B_CLOSE);
		// Verification

		// Verify the form closed
		ZAssert.assertFalse(formContactGroupNew.zIsActive(), "Verify the form closed after clicking CLOSE");

		// Verify the contact still exists
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ group.getName());
		ZAssert.assertNotNull(actual, "Verify the contact group with new name exists");
	}


	@Test (description = "Cancel an edited contact group by click Close, then click No",
			groups = { "functional", "L3" })

	public void EditContactGroup_05() throws HarnessException {

		// A new group name
		String newname = "edit" + ConfigProperties.getUniqueString();

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact group
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());

		// Click Edit on Toolbar button
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageContacts.zToolbarPressButton(Button.B_EDIT);

		// Change the group name
		formContactGroupNew.zFillField(Field.GroupName, newname);
		DialogWarning dialog = (DialogWarning) formContactGroupNew.zToolbarPressButton(Button.B_CLOSE);

		// Wait for the dialog to appear
		dialog.zWaitForActive();

		// Click No: Don't save changes
		dialog.zPressButton(Button.B_NO);

		// Verify the form closed
		ZAssert.assertFalse(formContactGroupNew.zIsActive(), "Verify the form closed after clicking CLOSE");

		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ group.getName());
		ZAssert.assertNotNull(actual, "Verify the contact group with new name exists");

		actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ newname);
		ZAssert.assertNull(actual, "Verify the contact group with new name exists");
	}


	@Bugs (ids = "97157")
	@Test (description = "Cancel an edited contact by click Close, then click Cancel",
			groups = { "functional", "L2" })

	public void EditContactGroup_06() throws HarnessException {

		// A new group name
		String newname = "edit" + ConfigProperties.getUniqueString();

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact group
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());

		// Click Edit on Toolbar button
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageContacts.zToolbarPressButton(Button.B_EDIT);

		// Change the group name
		formContactGroupNew.zFillField(Field.GroupName, newname);
		DialogWarning dialog = (DialogWarning) formContactGroupNew.zToolbarPressButton(Button.B_CLOSE);

		// Wait for the dialog to appear
		dialog.zWaitForActive();

		// Click Cancel: Save changes?
		dialog.zPressButton(Button.B_CANCEL);

		// Click Save
		formContactGroupNew.zToolbarPressButton(Button.B_SAVE);

		// Verification
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ newname);
		ZAssert.assertNotNull(actual, "Verify the contact group with new name exists");
	}


	@Bugs (ids = "97157")
	@Test (description = "Cancel an edited contact by click Close, then click Yes",
		groups = { "functional", "L2" })

	public void EditContactGroup_07() throws HarnessException {

		// A new group name
		String newname = "edit" + ConfigProperties.getUniqueString();

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact group
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());

		// Click Edit on Toolbar button
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageContacts.zToolbarPressButton(Button.B_EDIT);

		// Change the group name
		formContactGroupNew.zFillField(Field.GroupName, newname);
		DialogWarning dialog = (DialogWarning) formContactGroupNew.zToolbarPressButton(Button.B_CLOSE);

		// Wait for the dialog to appear
		dialog.zWaitForActive();

		// Click Yes: Don't save changes
		dialog.zPressButton(Button.B_YES);

		// Verification
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ newname);
		ZAssert.assertNotNull(actual, "Verify the contact group with new name exists");
	}
}