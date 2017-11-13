/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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

import java.util.*;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.AppAjaxClient;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.*;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.FormContactGroupNew.Field;

public class CheckPopulatedContactsWhenCreatingContactGroup extends AjaxCommonTest  {

	public CheckPopulatedContactsWhenCreatingContactGroup() {
		logger.info("New "+ CheckPopulatedContactsWhenCreatingContactGroup.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Bugs (ids = "65606,60652")
	@Test (description = "Contacts are not populated while creating a new contact group",
			groups = { "functional", "L2" })

	public void CheckPopulatedContactsWhenCreatingContactGroup_01() throws HarnessException {

		String groupname = "group" + ConfigProperties.getUniqueString();

		// Create a contact
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Open contact group form
		FormContactGroupNew formGroup = (FormContactGroupNew)app.zPageContacts.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_CONTACTGROUP);

		// Set the group name
		formGroup.zFillField(Field.GroupName, groupname);

		// select contacts option
		formGroup.zToolbarPressPulldown(Button.B_CONTACTGROUP_SEARCH_TYPE, Button.O_CONTACTGROUP_SEARCH_CONTACTS);

		// Get the displayed list
		ArrayList<ContactItem> ciArray = formGroup.zListGetSearchResults();

		boolean found=false;
		for (ContactItem ci: ciArray) {
			if ( ci.getName().equals(contact.getName()) ) {
				found = true;
				break;
			}
		}

		ZAssert.assertTrue(found, "Verify contact " + contact.getName() + " populated");

		// Try to close out the window
		formGroup.zToolbarPressButton(Button.B_CLOSE);

		DialogWarning dialog =  new DialogWarning(DialogWarning.DialogWarningID.CancelCreateContact, this.app, ((AppAjaxClient)this.app).zPageContacts);

		// Wait for the dialog to appear
		dialog.zWaitForActive();

		// Click No: Don't save changes
		dialog.zPressButton(Button.B_NO);
	}
}