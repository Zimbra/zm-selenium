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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.undo;

import java.util.HashMap;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.Toaster;

public class UndoDeleteContact extends AjaxCommonTest {

	public UndoDeleteContact() {
		logger.info("New " + UndoDeleteContact.class.getCanonicalName());

		super.startingPage = app.zPageContacts;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -8102550098554063084L; {
				put("zimbraPrefShowSelectionCheckbox", "TRUE");
			}
		};
	}


	@Test (description = "Undone deleted contact",
			groups = { "functional", "L3" })

	public void UndoDeleteContact_01() throws HarnessException {

		// The contacts folder
		FolderItem contacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);

		// Create a contact item
		ContactItem contact = new ContactItem();
		contact.firstName = "First" + ConfigProperties.getUniqueString();
		contact.lastName = "Last" + ConfigProperties.getUniqueString();
		contact.email = "email" + ConfigProperties.getUniqueString()+ "@domain.com";

		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >"
				+ "<a n='firstName'>" + contact.firstName + "</a>"
				+ "<a n='lastName'>" + contact.lastName + "</a>"
				+ "<a n='email'>" + contact.email + "</a>" + "</cn>"
				+ "</CreateContactRequest>");

		// Refresh to get the contact into the client
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, contact.firstName);

		// Delete contact
		app.zPageContacts.zToolbarPressButton(Button.B_DELETE);

		// Click undo from the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		toast.zWaitForActive();
		toast.sClickUndo();


		// Verify contact come back into Contacts folder
		ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact.firstName);
		ZAssert.assertNotNull(actual, "Verify the contact is not deleted from the addressbook");
		ZAssert.assertEquals(actual.getFolderId(), contacts.getId(), "Verify the contact is back in the contacts folder");
	}


	@Test (description = "Undone deleted a contact item selected with checkbox",
			groups = { "functional", "L3" })

	public void UndoDeleteContact_02() throws HarnessException {

		// The contacts folder
		FolderItem contacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);

		// Create a contact item
		ContactItem contact = new ContactItem();
		contact.firstName = "First" + ConfigProperties.getUniqueString();
		contact.lastName = "Last" + ConfigProperties.getUniqueString();
		contact.email = "email" + ConfigProperties.getUniqueString()+ "@domain.com";

		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >"
				+ "<a n='firstName'>" + contact.firstName + "</a>"
				+ "<a n='lastName'>" + contact.lastName + "</a>"
				+ "<a n='email'>" + contact.email + "</a>" + "</cn>"
				+ "</CreateContactRequest>");

		// Refresh to get the contact into the client
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact's checkbox
		app.zPageContacts.zListItem(Action.A_CHECKBOX, contact.firstName);

		// Delete contact
		app.zPageContacts.zToolbarPressButton(Button.B_DELETE);

		// Click undo from the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		toast.zWaitForActive();
		toast.sClickUndo();

		// Verify contact come back into Contacts folder
		ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact.firstName);
		ZAssert.assertNotNull(actual, "Verify the contact is not deleted from the addressbook");
		ZAssert.assertEquals(actual.getFolderId(), contacts.getId(), "Verify the contact is back in the contacts folder");
	}


	@Test (description = "Undone deleted multiple contact items",
			groups = { "functional", "L3" })

	public void UndoDeleteContact_03() throws HarnessException {

		// The contacts folder
		FolderItem contacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);

		// Create a contact items
		ContactItem contact1 = new ContactItem();
		contact1.firstName = "First"+ ConfigProperties.getUniqueString();
		contact1.lastName = "Last" + ConfigProperties.getUniqueString();
		contact1.email = "email" + ConfigProperties.getUniqueString()+ "@domain.com";
		contact1.fileAs = contact1.lastName + ", " + contact1.firstName;

		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >"
				+ "<a n='firstName'>" + contact1.firstName + "</a>"
				+ "<a n='lastName'>" + contact1.lastName + "</a>"
				+ "<a n='email'>" + contact1.email + "</a>" + "</cn>"
				+ "</CreateContactRequest>");

		ContactItem contact2 = new ContactItem();
		contact2.firstName = "First"+ ConfigProperties.getUniqueString();
		contact2.lastName = "Last" + ConfigProperties.getUniqueString();
		contact2.email = "email" + ConfigProperties.getUniqueString() + "@domain.com";
		contact2.fileAs = contact2.lastName + ", " + contact2.firstName;

		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >"
				+ "<a n='firstName'>" + contact2.firstName + "</a>"
				+ "<a n='lastName'>" + contact2.lastName + "</a>"
				+ "<a n='email'>" + contact2.email + "</a>" + "</cn>"
				+ "</CreateContactRequest>");

		ContactItem contact3 = new ContactItem();
		contact3.firstName = "First" + ConfigProperties.getUniqueString();
		contact3.lastName = "Last" + ConfigProperties.getUniqueString();
		contact3.email = "email" + ConfigProperties.getUniqueString() + "@domain.com";
		contact3.fileAs = contact3.lastName + ", " + contact3.firstName;

		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >"
				+ "<a n='firstName'>" + contact3.firstName + "</a>"
				+ "<a n='lastName'>" + contact3.lastName + "</a>"
				+ "<a n='email'>" + contact3.email + "</a>" + "</cn>"
				+ "</CreateContactRequest>");

		// Refresh to get the contact into the client
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the item
		app.zPageContacts.zListItem(Action.A_CHECKBOX, contact1.fileAs);
		app.zPageContacts.zListItem(Action.A_CHECKBOX, contact2.fileAs);
		app.zPageContacts.zListItem(Action.A_CHECKBOX, contact3.fileAs);

		// Delete 3 contacts
		app.zPageContacts.zToolbarPressButton(Button.B_DELETE);

		// Click undo from the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		toast.zWaitForActive();
		toast.sClickUndo();

		// Verify all 3 contacts are come back into Contacts folder
		ContactItem actual1 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact1.firstName);
		ZAssert.assertNotNull(actual1, "Verify the contact1 is not deleted from the addressbook");
		ZAssert.assertEquals(actual1.getFolderId(), contacts.getId(), "Verify the contact is back in the contacts folder");

		ContactItem actual2 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact2.firstName);
		ZAssert.assertNotNull(actual2, "Verify the contact2  not deleted from the addressbook");
		ZAssert.assertEquals(actual2.getFolderId(), contacts.getId(), "Verify the contact is back in the contacts folder");

		ContactItem actual3 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact3.firstName);
		ZAssert.assertNotNull(actual3, "Verify the contact3 not deleted from the addressbook");
		ZAssert.assertEquals(actual3.getFolderId(), contacts.getId(), "Verify the contact is back in the contacts folder");
	}
}