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

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.contacts.FormContactNew;
import com.zimbra.qa.selenium.projects.ajax.pages.contacts.FormContactNew.Field;

public class ViewContact extends AjaxCore  {
	public ViewContact() {
		logger.info("New "+ ViewContact.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	// First, Last
	@Test (description = "View a contact, file as First Last",
			groups = { "functional", "L2" })

	public void FileAsFirstLast_01() throws HarnessException {

		// Create a contact item
		String firstname = "first"+ ConfigProperties.getUniqueString();
		String lastname = "last"+ ConfigProperties.getUniqueString();
		String email = "email"+ ConfigProperties.getUniqueString() + "@example.com";
		String company = "company"+ ConfigProperties.getUniqueString();

		String expected = String.format("%s %s", firstname, lastname);

		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
							"<a n='firstName'>"+ firstname +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>"+ email +"</a>" +
							"<a n='company'>"+ company +"</a>" +
						"</cn>" +
				"</CreateContactRequest>" );

		// Refresh the app
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact
		FormContactNew form = (FormContactNew) app.zPageContacts.zListItem(Action.A_DOUBLECLICK, firstname);

		// Change File As -> First, Last
		form.zToolbarPressPulldown(Button.B_FILEAS, Button.O_FILEAS_FIRSTLAST);

		// Verify that the title bar changes
		String fileas = form.zGetFieldText(FormContactNew.Field.FullName);
		ZAssert.assertEquals(fileas, expected, "Verify the contact edit form title");
		form.zToolbarPressButton(Button.B_SAVE);

		// Verify the contact list shows modified file as value
		boolean found = false;
		for (ContactItem c : app.zPageContacts.zListGetContacts()) {
			if (c.fileAs.equals(expected))
			{
				found = true;
				break;
			}
		}

		ZAssert.assertTrue(found, "Verify contact (" + expected + ") displayed with First Last ");
	}


	// Last, First
	@Test (description = "View a contact, file as Last, First",
			groups = { "functional", "L2" })

	public void FileAsLastFirst_02() throws HarnessException {

		// Create a contact item
		String firstname = "first"+ ConfigProperties.getUniqueString();
		String lastname = "last"+ ConfigProperties.getUniqueString();
		String email = "email"+ ConfigProperties.getUniqueString() + "@example.com";
		String company = "company"+ ConfigProperties.getUniqueString();

		String expected = String.format("%s, %s", lastname, firstname);

		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
							"<a n='firstName'>"+ firstname +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>"+ email +"</a>" +
							"<a n='company'>"+ company +"</a>" +
						"</cn>" +
				"</CreateContactRequest>" );

		// Refresh the app
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact
		FormContactNew form = (FormContactNew) app.zPageContacts.zListItem(Action.A_DOUBLECLICK, firstname);

		// Change File As -> Last, First
		form.zFillField(Field.Email, app.zGetActiveAccount().EmailAddress); // work around to enable Save button
		form.zToolbarPressPulldown(Button.B_FILEAS, Button.O_FILEAS_LASTFIRST);

		// Verify that the title bar changes
		String fileas = form.zGetFieldText(FormContactNew.Field.FullName);
		ZAssert.assertEquals(fileas, expected, "Verify the contact edit form title");
		form.zToolbarPressButton(Button.B_SAVE);

		// Verify the contact list shows modified file as value
		boolean found = false;
		for (ContactItem c : app.zPageContacts.zListGetContacts()) {
			if (c.fileAs.equals(expected))
			{
				found = true;
				break;
			}
		}

		ZAssert.assertTrue(found, "Verify contact (" + expected + ") displayed with Last, First ");
	}


	// Company (Last, First)
	@Test (description = "View a contact, file as Company(Last, First)",
			groups = { "functional", "L2" })

	public void FileAsCompanyLastFirst_03() throws HarnessException {

		// Create a contact item
		String firstname = "first"+ ConfigProperties.getUniqueString();
		String lastname = "last"+ ConfigProperties.getUniqueString();
		String email = "email"+ ConfigProperties.getUniqueString() + "@example.com";
		String company = "company"+ ConfigProperties.getUniqueString();

		String expected = String.format("%s (%s, %s)", company, lastname, firstname);

		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
							"<a n='firstName'>"+ firstname +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>"+ email +"</a>" +
							"<a n='company'>"+ company +"</a>" +
						"</cn>" +
				"</CreateContactRequest>" );

		// Refresh the app
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact
		FormContactNew form = (FormContactNew) app.zPageContacts.zListItem(Action.A_DOUBLECLICK, firstname);

		// Change File As -> Company (Last, First)
		form.zToolbarPressPulldown(Button.B_FILEAS, Button.O_FILEAS_COMPANYLASTFIRST);

		// Verify that the title bar changes
		String fileas = form.zGetFieldText(FormContactNew.Field.FullName);
		ZAssert.assertEquals(fileas, expected, "Verify the contact edit form title");
		form.zToolbarPressButton(Button.B_SAVE);

		// Verify the contact list shows modified file as value
		boolean found = false;
		for (ContactItem c : app.zPageContacts.zListGetContacts()) {
			if (c.fileAs.equals(expected))
			{
				found = true;
				break;
			}
		}

		ZAssert.assertTrue(found, "Verify contact (" + expected + ") displayed with Company(Last, First) ");
	}


	// Company
	@Test (description = "View a contact, file as Company",
			groups = { "functional", "L2" })

	public void FileAsCompany_04() throws HarnessException {

		// Create a contact item
		String firstname = "first"+ ConfigProperties.getUniqueString();
		String lastname = "last"+ ConfigProperties.getUniqueString();
		String email = "email"+ ConfigProperties.getUniqueString() + "@example.com";
		String company = "company"+ ConfigProperties.getUniqueString();
		String expected = String.format("%s", company);

		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
							"<a n='firstName'>"+ firstname +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>"+ email +"</a>" +
							"<a n='company'>"+ company +"</a>" +
						"</cn>" +
				"</CreateContactRequest>" );

		// Refresh the app
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact
		FormContactNew form = (FormContactNew) app.zPageContacts.zListItem(Action.A_DOUBLECLICK, firstname);

		// Change File As -> Company
		form.zToolbarPressPulldown(Button.B_FILEAS, Button.O_FILEAS_COMPANY);

		// Verify that the title bar changes
		String fileas = form.zGetFieldText(FormContactNew.Field.FullName);
		ZAssert.assertEquals(fileas, expected, "Verify the contact edit form title");
		form.zToolbarPressButton(Button.B_SAVE);

		// Verify the contact list shows company
		boolean found = false;
		for (ContactItem c : app.zPageContacts.zListGetContacts()) {
			if (c.fileAs.equals(expected))
			{
				found = true;
				break;
			}
		}

		ZAssert.assertTrue(found, "Verify contact (" + expected + ") displayed with company ");
	}


	// Last, First (Company)
	@Test (description = "View a contact, file as Last, First (Company)",
			groups = { "functional", "L2" })

	public void FileAsLastFirstCompany_05() throws HarnessException {

		// Create a contact item
		String firstname = "first"+ ConfigProperties.getUniqueString();
		String lastname = "last"+ ConfigProperties.getUniqueString();
		String email = "email"+ ConfigProperties.getUniqueString() + "@example.com";
		String company = "company"+ ConfigProperties.getUniqueString();

		String expected = String.format("%s, %s (%s)", lastname, firstname, company);

		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
							"<a n='firstName'>"+ firstname +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>"+ email +"</a>" +
							"<a n='company'>"+ company +"</a>" +
						"</cn>" +
				"</CreateContactRequest>" );

		// Refresh the app
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact
		FormContactNew form = (FormContactNew) app.zPageContacts.zListItem(Action.A_DOUBLECLICK, firstname);

		// Change File As -> Last, First (Company)
		form.zToolbarPressPulldown(Button.B_FILEAS, Button.O_FILEAS_LASTFIRSTCOMPANY);

		// Verify that the title bar changes
		String fileas = form.zGetFieldText(FormContactNew.Field.FullName);
		ZAssert.assertEquals(fileas, expected, "Verify the contact edit form title");
		form.zToolbarPressButton(Button.B_SAVE);

		// Verify the contact list shows modified file as value
		boolean found = false;
		for (ContactItem c : app.zPageContacts.zListGetContacts()) {
			if (c.fileAs.equals(expected))
			{
				found = true;
				break;
			}
		}

		ZAssert.assertTrue(found, "Verify contact (" + expected + ") displayed with 'Last, First (company)' ");
	}


	// First Last (Company)
	@Test (description = "View a contact, file as First Last (Company)",
			groups = { "functional", "L2" })

	public void FileAsFirstLastCompany_06() throws HarnessException {

		// Create a contact item
		String firstname = "first"+ ConfigProperties.getUniqueString();
		String lastname = "last"+ ConfigProperties.getUniqueString();
		String email = "email"+ ConfigProperties.getUniqueString() + "@example.com";
		String company = "company"+ ConfigProperties.getUniqueString();

		String expected = String.format("%s %s (%s)", firstname, lastname, company);

		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
							"<a n='firstName'>"+ firstname +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>"+ email +"</a>" +
							"<a n='company'>"+ company +"</a>" +
						"</cn>" +
				"</CreateContactRequest>" );

		// Refresh the app
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact
		FormContactNew form = (FormContactNew) app.zPageContacts.zListItem(Action.A_DOUBLECLICK, firstname);

		// Change File As -> First Last (Company)
		form.zToolbarPressPulldown(Button.B_FILEAS, Button.O_FILEAS_FIRSTLASTCOMPANY);

		// Verify that the title bar changes
		String fileas = form.zGetFieldText(FormContactNew.Field.FullName);
		ZAssert.assertEquals(fileas, expected, "Verify the contact edit form title");
		form.zToolbarPressButton(Button.B_SAVE);

		// Verify the contact list shows modified file as value
		boolean found = false;
		for (ContactItem c : app.zPageContacts.zListGetContacts()) {
			if (c.fileAs.equals(expected))
			{
				found = true;
				break;
			}
		}

		ZAssert.assertTrue(found, "Verify contact (" + expected + ") displayed with First Last (Company) ");
	}


	// Company (First Last)
	@Test (description = "View a contact, file as Company (First Last)",
			groups = { "functional", "L2" })

	public void FileAsCompanyFirstLast_07() throws HarnessException {

		// Create a contact item
		String firstname = "first"+ ConfigProperties.getUniqueString();
		String lastname = "last"+ ConfigProperties.getUniqueString();
		String email = "email"+ ConfigProperties.getUniqueString() + "@example.com";
		String company = "company"+ ConfigProperties.getUniqueString();

		String expected = String.format("%s (%s %s)", company, firstname, lastname);

		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
							"<a n='firstName'>"+ firstname +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>"+ email +"</a>" +
							"<a n='company'>"+ company +"</a>" +
						"</cn>" +
				"</CreateContactRequest>" );

		// Refresh the app
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact
		FormContactNew form = (FormContactNew) app.zPageContacts.zListItem(Action.A_DOUBLECLICK, firstname);

		// Change File As -> Company (First Last)
		form.zToolbarPressPulldown(Button.B_FILEAS, Button.O_FILEAS_COMPANYFIRSTLAST);

		// Verify that the title bar changes
		String fileas = form.zGetFieldText(FormContactNew.Field.FullName);
		ZAssert.assertEquals(fileas, expected, "Verify the contact edit form title");
		form.zToolbarPressButton(Button.B_SAVE);

		// Verify the contact list shows modified file as value
		boolean found = false;
		for (ContactItem c : app.zPageContacts.zListGetContacts()) {
			if (c.fileAs.equals(expected))
			{
				found = true;
				break;
			}
		}

		ZAssert.assertTrue(found, "Verify contact (" + expected + ") displayed with Company (First Last) ");
	}
}