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

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.contacts.DialogNewContactGroup;

public class ManageContactGroup extends AjaxCore  {

	public ManageContactGroup() {
		logger.info("New "+ ManageContactGroup.class.getCanonicalName());

		super.startingPage = app.zPageContacts;
		super.startingAccountPreferences = new HashMap<String , String>() {
		private static final long serialVersionUID = 8504391696323008278L; {
			put("zimbraPrefShowSelectionCheckbox", "TRUE");
		}};
	}


	@Test (description = "Create a new contact group by right click on existing contact",
			groups = { "smoke", "L1" })

	public void CreateContactGroupWith1Contact_01() throws HarnessException {

		// Create a contact
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());

		// The contact group name
		String groupname = "group" + ConfigProperties.getUniqueString();

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Right click on the contact
		DialogNewContactGroup dialog = (DialogNewContactGroup) app.zPageContacts.zListItem(
				Action.A_RIGHTCLICK,
				Button.B_CONTACTGROUP,
				Button.O_NEW_CONTACTGROUP,
				contact.firstName);

		dialog.zEnterGroupName(groupname);
		dialog.zPressButton(Button.B_OK);

		// Verify the contact group is created
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), groupname);
		ZAssert.assertNotNull(actual,  "Verify the contact group is created");

		// Verify the contact group contains the contact
		ZAssert.assertContains(
				actual.getMemberList(),
				new ContactGroupItem.MemberItemContact(contact),
				"Verify the contact group conatins the contact");
	}


	@Bugs (ids = "77882")
	@Test (description = "Add a contact to an existing group",
			groups = { "smoke", "L0" })

	public void Add1ContactToExistingContactGroup_02() throws HarnessException {

		// Create a contact
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Right click on the contact -> Group -> Existing Group Name
		app.zPageContacts.zListItem(
				Action.A_RIGHTCLICK,
				Button.B_CONTACTGROUP,
				group,
				contact.getName());

		// Verify the contact group is created
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), group.getName());
		ZAssert.assertNotNull(actual,  "Verify the contact group is created");

		// Verify the contact group contains the contact
		ZAssert.assertContains(
				actual.getMemberList(),
				new ContactGroupItem.MemberItemContact(contact),
				"Verify the contact group conatins the contact");
	}


	@Bugs (ids = "77882")
	@Test (description = "Add 3 contacts to an existing group",
			groups = { "functional", "L2" })

	public void Add3ContactsToExistingContactGroup_03() throws HarnessException {

		// Create a contact
		ContactItem contact1 = ContactItem.createContactItem(app.zGetActiveAccount());
		ContactItem contact2 = ContactItem.createContactItem(app.zGetActiveAccount());
		ContactItem contact3 = ContactItem.createContactItem(app.zGetActiveAccount());

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Check 3 contact items
	    app.zPageContacts.zListItem(Action.A_CHECKBOX, contact1.getName());
	    app.zPageContacts.zListItem(Action.A_CHECKBOX, contact2.getName());
	    app.zPageContacts.zListItem(Action.A_CHECKBOX, contact3.getName());

		// Right click on one contact -> Group -> Existing Group Name
		app.zPageContacts.zListItem(
				Action.A_RIGHTCLICK,
				Button.B_CONTACTGROUP,
				group,
				contact1.getName());

		// Verify the contact group is created
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), group.getName());
		ZAssert.assertNotNull(actual,  "Verify the contact group is created");

		// Verify the contact group contains the contact
		ZAssert.assertContains(
				actual.getMemberList(),
				new ContactGroupItem.MemberItemContact(contact1),
				"Verify the contact group conatins the contact");

		ZAssert.assertContains(
				actual.getMemberList(),
				new ContactGroupItem.MemberItemContact(contact2),
				"Verify the contact group conatins the contact");

		ZAssert.assertContains(
				actual.getMemberList(),
				new ContactGroupItem.MemberItemContact(contact3),
				"Verify the contact group conatins the contact");
	}


	@Test (description = "Create a contact group with 3 contacts",
			groups = { "functional", "L2" })

	public void CreateContactGroupWith3Contacts_04() throws HarnessException {

		// Create a contact
		ContactItem contact1 = ContactItem.createContactItem(app.zGetActiveAccount());
		ContactItem contact2 = ContactItem.createContactItem(app.zGetActiveAccount());

		ContactItem contact3 = ContactItem.createContactItem(app.zGetActiveAccount());
		ContactItem.createContactItem(app.zGetActiveAccount());

		// Create a contact group
		String groupname = "group"+ ConfigProperties.getUniqueString();

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Check 3 contact items
		app.zPageContacts.zListItem(Action.A_CHECKBOX, contact1.getName());
	    app.zPageContacts.zListItem(Action.A_CHECKBOX, contact2.getName());
	    app.zPageContacts.zListItem(Action.A_CHECKBOX, contact3.getName());

		// Right click on one contact -> Group -> Existing Group Name
	    DialogNewContactGroup dialog = (DialogNewContactGroup) app.zPageContacts.zListItem(
				Action.A_RIGHTCLICK,
				Button.B_CONTACTGROUP,
				Button.O_NEW_CONTACTGROUP,
				contact3.getName());

		dialog.zEnterGroupName(groupname);
		dialog.zPressButton(Button.B_OK);

		// Verify the contact group is created
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), groupname);
		ZAssert.assertNotNull(actual,  "Verify the contact group is created");

		// Verify the contact group contains the contact
		ZAssert.assertContains(
				actual.getMemberList(),
				new ContactGroupItem.MemberItemContact(contact1),
				"Verify the contact group conatins the contact");

		ZAssert.assertContains(
				actual.getMemberList(),
				new ContactGroupItem.MemberItemContact(contact2),
				"Verify the contact group conatins the contact");

		ZAssert.assertContains(
				actual.getMemberList(),
				new ContactGroupItem.MemberItemContact(contact3),
				"Verify the contact group conatins the contact");
	}


	@Bugs (ids = "65500,15646")
	@Test (description = "Create a contact group with 1 contact + 1 group",
			groups = { "functional", "L2" })

	public void CreateContactGroupWith1ContactAnd1ContactGroup_05() throws HarnessException {

		// Create a contact
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		String groupname = "group" + ConfigProperties.getUniqueString();

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Check 3 contact items
		app.zPageContacts.zListItem(Action.A_CHECKBOX, group.getName());
	    app.zPageContacts.zListItem(Action.A_CHECKBOX, contact.getName());

		// Right click on one contact -> Group -> Existing Group Name
	    DialogNewContactGroup dialog = (DialogNewContactGroup) app.zPageContacts.zListItem(
				Action.A_RIGHTCLICK,
				Button.B_CONTACTGROUP,
				Button.O_NEW_CONTACTGROUP,
				contact.getName());

		dialog.zEnterGroupName(groupname);
		dialog.zPressButton(Button.B_OK);

		// Verify the contact group is created
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), groupname);
		ZAssert.assertNotNull(actual,  "Verify the contact group is created");

		// Verify the contact group contains the contact
		ZAssert.assertContains(
				actual.getMemberList(),
				new ContactGroupItem.MemberItemContact(contact),
				"Verify the contact group conatins the contact");

		// The group members will be added to the new group
		for (ContactGroupItem.MemberItem m : group.getMemberList()) {
			ZAssert.assertContains(
					actual.getMemberList(),
					m,
					"Verify the contact group contains the group members");
		}
	}


	@Bugs (ids = "77882,15646")
	@Test (description = "Add 1 contact + 1 group to an existing group",
			groups = { "functional", "L2" })

	public void Add1ContactAnd1GroupToExistingContactGroup_06() throws HarnessException {

		// Create a contact
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());

		// Create a contact group
		ContactGroupItem group1 = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		ContactGroupItem group2 = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Check 3 contact items
	    app.zPageContacts.zListItem(Action.A_CHECKBOX, contact.getName());
	    app.zPageContacts.zListItem(Action.A_CHECKBOX, group1.getName());

		// Right click on one contact -> Group -> Existing Group Name
		app.zPageContacts.zListItem(
				Action.A_RIGHTCLICK,
				Button.B_CONTACTGROUP,
				group2,
				contact.getName());

		// Verify the contact group is created
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), group2.getName());
		ZAssert.assertNotNull(actual,  "Verify the contact group is created");

		// Verify the contact group contains the contact
		ZAssert.assertContains(
				actual.getMemberList(),
				new ContactGroupItem.MemberItemContact(contact),
				"Verify the contact group conatins the contact");

		// The group members will be added to the new group
		for (ContactGroupItem.MemberItem m : group2.getMemberList()) {
			ZAssert.assertContains(
					actual.getMemberList(),
					m,
					"Verify the contact group contains the group members");
		}
	}
}