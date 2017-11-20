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

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.ContactGroupItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.Toaster;
import com.zimbra.qa.selenium.projects.ajax.pages.contacts.*;
import com.zimbra.qa.selenium.projects.ajax.pages.contacts.FormContactGroupNew.Field;

public class EditContactGroup extends AjaxCore {

	public EditContactGroup() {
		logger.info("New " + EditContactGroup.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Bugs (ids = "97157")
	@Test (description = "Edit a contact group by click Edit on Toolbar button and verify toast message",
			groups = { "functional", "L2" })

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
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageContacts
				.zToolbarPressButton(Button.B_EDIT);

		// Change the group name
		formContactGroupNew.zFillField(Field.GroupName, newname);

		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
				ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			app.zPageContacts.sClickAt(
					"css=div#" + formContactGroupNew.getToolbarID() + " div[id$='__SAVE'] td[id$='_title']", "0,0");

		} else {
			formContactGroupNew.zSubmit();
		}

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMessage = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMessage, "Group Saved", "Verify toast message: Group Saved bug:97157");
	}


	@Bugs (ids = "97157")
	@Test (description = "Edit a contact group by click Edit Group on Context Menu and verify toast message",
			groups = { "functional", "L2" })

	public void EditContactGroup_02() throws HarnessException {

		// A new group name
		String newname = "edit" + ConfigProperties.getUniqueString();

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Right click -> Edit
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageContacts.zListItem(Action.A_RIGHTCLICK,
				Button.B_EDIT, group.getName());

		// Change the group name
		formContactGroupNew.zFillField(Field.GroupName, newname);

		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
				ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			app.zPageContacts.sClickAt(
					"css=div#" + formContactGroupNew.getToolbarID() + " div[id$='__SAVE'] td[id$='_title']", "0,0");
		} else {
			formContactGroupNew.zSubmit();
		}

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMessage = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMessage, "Group Saved", "Verify toast message: Group Saved bug:97157");
	}


	@Bugs (ids = "97157")
	@Test (description = "Edit a contact group by double click on the contact group and verify toast message  ",
			groups = { "functional", "L2" })

	public void EditContactGroup_03() throws HarnessException {

		// A new group name
		String newname = "edit" + ConfigProperties.getUniqueString();

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Double click
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageContacts
				.zListItem(Action.A_DOUBLECLICK, group.getName());

		// Change the group name
		formContactGroupNew.zFillField(Field.GroupName, newname);

		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
				ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			app.zPageContacts.sClickAt(
					"css=div#" + formContactGroupNew.getToolbarID() + " div[id$='__SAVE'] td[id$='_title']", "0,0");
		} else {
			formContactGroupNew.zSubmit();
		}

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMessage = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMessage, "Group Saved", "Verify toast message: Group Saved bug:97157");
	}
}