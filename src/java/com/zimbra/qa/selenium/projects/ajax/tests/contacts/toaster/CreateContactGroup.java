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
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.Toaster;
import com.zimbra.qa.selenium.projects.ajax.pages.contacts.FormContactGroupNew;
import com.zimbra.qa.selenium.projects.ajax.pages.contacts.FormContactGroupNew.Field;

public class CreateContactGroup extends AjaxCore {

	public CreateContactGroup() {
		logger.info("New " + CreateContactGroup.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test (description = "Create a basic contact group with 2 addresses.  New -> Contact Group and verify toast message",
			groups = { "sanity" })

	public void CreateContactGroup_01() throws HarnessException {

		String groupName = "group" + ConfigProperties.getUniqueString();
		String member1 = "m" + ConfigProperties.getUniqueString() + "@" + ConfigProperties.getStringProperty("testdomain");
		String member2 = "m" + ConfigProperties.getUniqueString() + "@" + ConfigProperties.getStringProperty("testdomain");

		// Refresh the addressbook
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Open contact group form
		FormContactGroupNew form = (FormContactGroupNew) app.zPageContacts.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_CONTACTGROUP);

		// Fill in group name and email addresses
		form.zFillField(Field.GroupName, groupName);
		form.zFillField(Field.FreeFormAddress, member1);
		form.zFillField(Field.FreeFormAddress, member2);

		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
				ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			app.zPageContacts.sClickAt("css=div#" + form.getToolbarID() + " div[id$='__SAVE'] td[id$='_title']", "0,0");
		} else {
			form.zSubmit();
		}

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMessage = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMessage, "Group Created", "Verify toast message: Group Created");
	}


	@Test (description = "Create a contact group with existing contacts and verify toast message",
			groups = { "sanity" })

	public void CreateContactGroup_02() throws HarnessException {

		// The contact group name
		String groupName = "group" + ConfigProperties.getUniqueString();

		// Create two contacts
		ContactItem contact1 = ContactItem.createContactItem(app.zGetActiveAccount());
		ContactItem contact2 = ContactItem.createContactItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Open contact group form
		FormContactGroupNew form = (FormContactGroupNew) app.zPageContacts.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_CONTACTGROUP);

		// Fill in group name
		form.zFillField(Field.GroupName, groupName);

		// Select Contact search
		form.zToolbarPressPulldown(Button.B_CONTACTGROUP_SEARCH_TYPE, Button.O_CONTACTGROUP_SEARCH_CONTACTS);
		form.zFillField(Field.SearchField, contact1.email);
		form.zToolbarPressButton(Button.B_SEARCH);
		form.zToolbarPressButton(Button.B_CONTACTGROUP_ADD_SEARCH_RESULT);

		form.zToolbarPressPulldown(Button.B_CONTACTGROUP_SEARCH_TYPE, Button.O_CONTACTGROUP_SEARCH_CONTACTS);
		form.zFillField(Field.SearchField, contact2.email);
		form.zToolbarPressButton(Button.B_SEARCH);
		form.zToolbarPressButton(Button.B_CONTACTGROUP_ADD_SEARCH_RESULT);

		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
				ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			app.zPageContacts.sClickAt("css=div#" + form.getToolbarID() + " div[id$='__SAVE'] td[id$='_title']", "0,0");
		} else {
			form.zSubmit();
		}

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMessage = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMessage, "Group Created", "Verify toast message: Group Created");
	}
}
