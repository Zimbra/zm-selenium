/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.mobile.tests.addressbook.contacts;

import java.util.List;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.mobile.core.MobileCommonTest;
import com.zimbra.qa.selenium.projects.mobile.ui.FormContactNew;


public class CreateContact extends MobileCommonTest {

	public CreateContact() {
		logger.info("New "+ CreateContact.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageContacts;
		super.startingAccount = null;
		
	}
	
	@Test( description = "Create a new contact",
			groups = { "sanity" })
	public void CreateContact_01() throws HarnessException, InterruptedException {

		// Define a new contact
		ContactItem contact = new ContactItem();
		contact.lastName = "last" + ConfigProperties.getUniqueString();
		contact.firstName = "first" + ConfigProperties.getUniqueString();
		contact.email = "email" + ConfigProperties.getUniqueString() + "@example.com";

		// Get the "new contact" page and fill it out (automatically)
		FormContactNew form = (FormContactNew) app.zPageContacts.zToolbarPressButton(Button.B_NEW);
		form.zFill(contact);
		form.zSubmit();

		// Reset the contacts list
		app.zPageContacts.zRefresh();
		
		
		// Get the list of contacts
		List<ContactItem> contacts = app.zPageContacts.zListGetContacts();
		
		ZAssert.assertGreaterThan(contacts.size(), 0, "Verify the contact list size is greater than 0");

		// Verify that the sent mail is in the list
		boolean found = false;
		for (ContactItem c : contacts) {
			if ( contact.email.equals(c.gEmail) ) {
				found = true;		// Found the message!
				break;
			}
		}
		
		ZAssert.assertTrue(found, "Verify that the newly created contact is in the contacts folder");

		
	}

}
