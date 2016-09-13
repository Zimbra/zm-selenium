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
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.Toaster;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.*;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.FormContactNew.Field;

public class EditContact extends AjaxCommonTest {
	public EditContact() {
		logger.info("New " + EditContact.class.getCanonicalName());

		// All tests start at the Address page
		super.startingPage = app.zPageContacts;

		// Make sure we are using an account with conversation view
		super.startingAccountPreferences = null;

	}

	@Test( description = "Edit a contact item, click Edit on toolbar and verify toast msg", groups = { "functional" })
	public void EditContactToastMsg_01() throws HarnessException {

		// Create a contact
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());

		// The new first name
		String firstname = "new" + ConfigProperties.getUniqueString();

		// Refresh
		app.zPageContacts.zRefresh();

		// Select the contact
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, contact.getName());

		// Click "Edit" from the toolbar
		FormContactNew form = (FormContactNew) app.zPageContacts.zToolbarPressButton(Button.B_EDIT);

		// Change the first name
		form.zFillField(Field.FirstName, firstname);
		
		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled", ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			// this method won't wait for some sec after submitting data so toast message disappears and testcase fails (JS COVERAGE)
			app.zPageContacts.zClickAt("css=div#" + form.getToolbarID() + " div[id$='__SAVE'] td[id$='_title']", "0,0");
		} else {
			form.zSubmit();
		}		

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "Contact Saved","Verify toast message: Contact Saved");

	}

	@Test( description = "Edit a contact item, Right click then click Edit and verify toast msg", groups = { "functional" })
	public void EditContactToastMsg_02() throws HarnessException {

		// Create a contact
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());

		// The new first name
		String firstname = "new" + ConfigProperties.getUniqueString();

		// Refresh
		app.zPageContacts.zRefresh();

		// Rigth Click -> "Edit"
		FormContactNew form = (FormContactNew) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_EDIT, contact.getName());

		// Change the first name
		form.zFillField(Field.FirstName, firstname);
		
		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled", ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			// this method won't wait for some sec after submitting data so toast message disappears and testcase fails (JS COVERAGE)
			app.zPageContacts.zClickAt("css=div#" + form.getToolbarID() + " div[id$='__SAVE'] td[id$='_title']", "0,0");
		} else {
			form.zSubmit();
		}		

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "Contact Saved","Verify toast message: Contact Saved");

	}

	@Test( description = "Edit a contact item, double click the contact and verify toast msg", groups = { "functional" })
	public void EditContactToastMsg_03() throws HarnessException {

		// Create a contact
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());

		// The new first name
		String firstname = "new" + ConfigProperties.getUniqueString();

		// Refresh
		app.zPageContacts.zRefresh();

		// Double click contact
		FormContactNew form = (FormContactNew) app.zPageContacts.zListItem(Action.A_DOUBLECLICK, contact.getName());

		// Change the first name
		form.zFillField(Field.FirstName, firstname);
		
		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled", ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			// this method won't wait for some sec after submitting data so toast message disappears and testcase fails (JS COVERAGE)
			app.zPageContacts.zClickAt("css=div#" + form.getToolbarID() + " div[id$='__SAVE'] td[id$='_title']", "0,0");
		} else {
			form.zSubmit();
		}		

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "Contact Saved","Verify toast message: Contact Saved");

	}
}
