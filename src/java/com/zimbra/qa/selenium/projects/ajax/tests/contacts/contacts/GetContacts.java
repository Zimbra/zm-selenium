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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.contacts;

import java.util.*;
import java.util.Map.Entry;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.PageContacts;

public class GetContacts extends AjaxCommonTest  {
	public GetContacts() {
		logger.info("New "+ GetContacts.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test( description = "Click Alphabetbar button All: Verify contact started with digit and A-Z listed ",
			groups = { "functional", "L2"})

	public void GetContact_All_Button_01() throws HarnessException {

		String lastname;

		// Create three contact
		lastname = "B" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
							"<a n='firstName'>first"+ ConfigProperties.getUniqueString() +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>email@domain.com</a>" +
						"</cn>" +
				"</CreateContactRequest>" );
   		ContactItem contact1 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#lastname:"+ lastname);

   		lastname = "5" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
						"<a n='firstName'>first"+ ConfigProperties.getUniqueString() +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>email@domain.com</a>" +
						"</cn>" +
				"</CreateContactRequest>" );
   		ContactItem contact2 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#lastname:"+ lastname);

   		lastname = "b" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
						"<a n='firstName'>first"+ ConfigProperties.getUniqueString() +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>email@domain.com</a>" +
						"</cn>" +
				"</CreateContactRequest>" );
   		ContactItem contact3 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#lastname:"+ lastname);

   		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

        // Click All
		app.zPageContacts.zToolbarPressButton(Button.B_AB_ALL);

		// Verify group name and members displayed
		List<ContactItem> items = app.zPageContacts.zListGetContacts();

		boolean found1 = false;
		boolean found2 = false;
		boolean found3 = false;

		for (ContactItem item : items) {
			if ( item.getName().equals(contact1.getName()) ) {
				found1 = true;
			}
			if ( item.getName().equals(contact2.getName()) ) {
				found2 = true;
			}
			if ( item.getName().equals(contact3.getName()) ) {
				found3 = true;
			}
		}

		ZAssert.assertTrue(found1, "Verify contact starting with B is listed");
		ZAssert.assertTrue(found2, "Verify contact starting with 5 is listed");
		ZAssert.assertTrue(found3, "Verify contact starting with b is listed");
	}


	@Test( description = "Click Alphabetbar button 123: Verify contact started with digit listed and A-Z not-listed ",
			groups = { "functional", "L2"})

	public void GetContact_123_Button_02() throws HarnessException {

		String lastname;

		// Create three contact
		lastname = "B" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
						"<a n='firstName'>first"+ ConfigProperties.getUniqueString() +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>email@domain.com</a>" +
						"</cn>" +
				"</CreateContactRequest>" );
   		ContactItem contact1 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#lastname:"+ lastname);

   		lastname = "5" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
						"<a n='firstName'>first"+ ConfigProperties.getUniqueString() +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>email@domain.com</a>" +
						"</cn>" +
				"</CreateContactRequest>" );
   		ContactItem contact2 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#lastname:"+ lastname);

   		lastname = "b" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
						"<a n='firstName'>first"+ ConfigProperties.getUniqueString() +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>email@domain.com</a>" +
						"</cn>" +
				"</CreateContactRequest>" );
   		ContactItem contact3 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#lastname:"+ lastname);

   		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

        // Click All
		app.zPageContacts.zToolbarPressButton(Button.B_AB_123);

		// Verify group name and members displayed
		List<ContactItem> items = app.zPageContacts.zListGetContacts();

		boolean found1 = false;
		boolean found2 = false;
		boolean found3 = false;

		for (ContactItem item : items) {

			if ( item.getName().equals(contact1.getName()) ) {
				found1 = true;
			}
			if ( item.getName().equals(contact2.getName()) ) {
				found2 = true;
			}
			if ( item.getName().equals(contact3.getName()) ) {
				found3 = true;
			}
		}

		ZAssert.assertFalse(found1, "Verify contact starting with B is not listed");
		ZAssert.assertTrue(found2, "Verify contact starting with 5 is listed");
		ZAssert.assertFalse(found3, "Verify contact starting with b is not listed");
	}


	@Test( description = "Click Alphabetbar button B: Verify only contact started with B|b is listed ",
			groups = { "functional", "L3"})

	public void GetContact_B_Button_03() throws HarnessException {

		String lastname;

		// Create three contact
		lastname = "B" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
						"<a n='firstName'>first"+ ConfigProperties.getUniqueString() +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>email@domain.com</a>" +
						"</cn>" +
				"</CreateContactRequest>" );
   		ContactItem contact1 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#lastname:"+ lastname);

   		lastname = "5" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
						"<a n='firstName'>first"+ ConfigProperties.getUniqueString() +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>email@domain.com</a>" +
						"</cn>" +
				"</CreateContactRequest>" );
   		ContactItem contact2 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#lastname:"+ lastname);

   		lastname = "b" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
						"<a n='firstName'>first"+ ConfigProperties.getUniqueString() +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>email@domain.com</a>" +
						"</cn>" +
				"</CreateContactRequest>" );
   		ContactItem contact3 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#lastname:"+ lastname);

   		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

        // Click All
		app.zPageContacts.zToolbarPressButton(Button.B_AB_B);

		// Verify group name and members displayed
		List<ContactItem> items = app.zPageContacts.zListGetContacts();

		boolean found1 = false;
		boolean found2 = false;
		boolean found3 = false;

		for (ContactItem item : items) {
			if ( item.getName().equals(contact1.getName()) ) {
				found1 = true;
			}
			if ( item.getName().equals(contact2.getName()) ) {
				found2 = true;
			}
			if ( item.getName().equals(contact3.getName()) ) {
				found3 = true;
			}
		}

		ZAssert.assertTrue(found1, "Verify contact starting with B is listed");
		ZAssert.assertFalse(found2, "Verify contact starting with 5 is not listed");
		ZAssert.assertTrue(found3, "Verify contact starting with b is listed");
	}


	@Test( description = "Click all Alphabetbar buttons: Verify only contact started with the alphabet is listed ",
			groups = { "functional", "L2"})

	public void GetContact_Iterate_Buttons_04() throws HarnessException {

		// A map of buttons to ContactGroupItem
		HashMap<Button, ContactItem> contacts = new HashMap<Button, ContactItem>();

		// Create contact groups with each letter
		for ( Entry<Character, Button> entry : PageContacts.buttons.entrySet() ) {

			Character c = entry.getKey();
			Button b = entry.getValue();

			String lastname = c + ConfigProperties.getUniqueString();
	   		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>" +
							"<cn >" +
							"<a n='firstName'>first"+ ConfigProperties.getUniqueString() +"</a>" +
								"<a n='lastName'>"+ lastname +"</a>" +
								"<a n='email'>email@domain.com</a>" +
							"</cn>" +
					"</CreateContactRequest>" );
	   		ContactItem contact = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#lastname:"+ lastname);

	   		contacts.put(b, contact);
		}

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		for ( Entry<Button, ContactItem> entry : contacts.entrySet() ) {
			Button b = entry.getKey();
			ContactItem c = entry.getValue();

			// Click each button
			app.zPageContacts.zToolbarPressButton(b);

			// Verify the group is listed
			boolean found = false;
			for (ContactItem i : app.zPageContacts.zListGetContacts()) {
				if ( i.getName().equals(c.getName()) ) {
					found = true;
				}
			}
			ZAssert.assertTrue(found, "Verify contact "+ c.getName() +" is listed");
		}

	}
}