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
package com.zimbra.qa.selenium.projects.universal.tests.contacts.contacts;

import java.util.HashMap;
import java.util.Map.Entry;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraCharsets.ZCharset;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.FormContactNew;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.FormContactNew.Field;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.FormContactNew.Locators;

public class CreateContact extends UniversalCommonTest {

	public CreateContact() {
		logger.info("New " + CreateContact.class.getCanonicalName());
		super.startingPage = app.zPageContacts;

	}

	@Test(description = "Create a basic contact item by click New in page Addressbook ", 
			groups = { "sanity", "L0"})

	public void ClickContact_01() throws HarnessException {

		// -- DATA

		String contactFirst = "First" + ConfigProperties.getUniqueString();
		String contactLast = "Last" + ConfigProperties.getUniqueString();
		String contactEmail = "email" + ConfigProperties.getUniqueString() + "@domain.com";

		// -- GUI Action

		FormContactNew formContactNew = (FormContactNew) app.zPageContacts.zToolbarPressButton(Button.B_NEW);

		// Fill in the form
		formContactNew.zFillField(Field.FirstName, contactFirst);
		formContactNew.zFillField(Field.LastName, contactLast);
		formContactNew.zFillField(Field.Email, contactEmail);
		formContactNew.zSubmit();

		// -- Data Verification

		app.zGetActiveAccount().soapSend("<SearchRequest xmlns='urn:zimbraMail' types='contact'>" + "<query>#firstname:"
				+ contactFirst + "</query>" + "</SearchRequest>");
		String contactId = app.zGetActiveAccount().soapSelectValue("//mail:cn", "id");

		ZAssert.assertNotNull(contactId, "Verify the contact is returned in the search");

		app.zGetActiveAccount().soapSend("<GetContactsRequest xmlns='urn:zimbraMail'>" + "<cn id='" + contactId + "'/>"
				+ "</GetContactsRequest>");

		String lastname = app.zGetActiveAccount()
				.soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='lastName']", null);
		String firstname = app.zGetActiveAccount()
				.soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='firstName']", null);
		String email = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='email']",
				null);

		ZAssert.assertEquals(lastname, contactLast, "Verify the last name was saved correctly");
		ZAssert.assertEquals(firstname, contactFirst, "Verify the first name was saved correctly");
		ZAssert.assertEquals(email, contactEmail, "Verify the email was saved correctly");

	}

	@Test(description = "Create a basic contact item by use PullDown Menu->Contacts", 
			groups = { "functional", "L2"})

	public void CreateContactFromPulldownMenu_02() throws HarnessException {

		// -- DATA
		ContactItem contact = new ContactItem();
		contact.firstName = "First" + ConfigProperties.getUniqueString();
		contact.lastName = "Last" + ConfigProperties.getUniqueString();
		contact.email = "email" + ConfigProperties.getUniqueString() + "@domain.com";

		// -- GUI Action

		// app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		FormContactNew formContactNew = (FormContactNew) app.zPageContacts.zToolbarPressPulldown(Button.B_NEW,
				Button.O_NEW_CONTACT);

		// Fill in the form
		formContactNew.zFill(contact);

		// Save it
		formContactNew.zSubmit();

		// -- Data Verification

		ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:" + contact.firstName);

		ZAssert.assertEquals(actual.lastName, contact.lastName, "Verify the last name was saved correctly");
		ZAssert.assertEquals(actual.firstName, contact.firstName, "Verify the last name was saved correctly");
		ZAssert.assertEquals(actual.email, contact.email, "Verify the last name was saved correctly");

	}

	@Test(description = "Cancel creating a contact item - Click Yes", 
			groups = { "functional", "L2"})

	public void CancelCreateContactClickYes_03() throws HarnessException {

		// -- DATA

		ContactItem contact = new ContactItem();
		contact.firstName = "First" + ConfigProperties.getUniqueString();
		contact.lastName = "Last" + ConfigProperties.getUniqueString();
		contact.email = "email" + ConfigProperties.getUniqueString() + "@domain.com";

		// -- GUI action

		FormContactNew formContactNew = (FormContactNew) app.zPageContacts.zToolbarPressButton(Button.B_NEW);

		// Fill the fields
		formContactNew.zFill(contact);

		// Click Cancel
		DialogWarning dialogWarning = (DialogWarning) formContactNew.zToolbarPressButton(Button.B_CANCEL);

		// Click Yes in popup dialog
		dialogWarning.zClickButton(Button.B_YES);

		// Verify contact created
		ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:" + contact.firstName);

		ZAssert.assertEquals(actual.lastName, contact.lastName, "Verify the last name was saved correctly");
		ZAssert.assertEquals(actual.firstName, contact.firstName, "Verify the last name was saved correctly");
		ZAssert.assertEquals(actual.email, contact.email, "Verify the last name was saved correctly");
	}

	@Test(description = "Cancel creating a contact item - Click No", 
			groups = { "functional", "L2"})

	public void CancelCreateContactClickNo_04() throws HarnessException {

		// -- DATA

		ContactItem contact = new ContactItem();
		contact.firstName = "First" + ConfigProperties.getUniqueString();
		contact.lastName = "Last" + ConfigProperties.getUniqueString();
		contact.email = "email" + ConfigProperties.getUniqueString() + "@domain.com";

		// -- GUI action

		FormContactNew formContactNew = (FormContactNew) app.zPageContacts.zToolbarPressButton(Button.B_NEW);

		// Fill the fields
		formContactNew.zFill(contact);

		// Click Cancel
		DialogWarning dialogWarning = (DialogWarning) formContactNew.zToolbarPressButton(Button.B_CANCEL);

		// Click Yes in popup dialog
		dialogWarning.zClickButton(Button.B_NO);

		// -- Data Verification

		// Verify contact created
		ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:" + contact.firstName);
		ZAssert.assertNull(actual, "Verify the contact is not created");

	}

	@Test(description = "Cancel creating a contact item - Click Cancel", 
			groups = { "functional", "L2"})

	public void CancelCreateContactClickCancel_05() throws HarnessException {

		// -- DATA

		ContactItem contact = new ContactItem();
		contact.firstName = "First" + ConfigProperties.getUniqueString();
		contact.lastName = "Last" + ConfigProperties.getUniqueString();
		contact.email = "email" + ConfigProperties.getUniqueString() + "@domain.com";

		// -- GUI action

		FormContactNew formContactNew = (FormContactNew) app.zPageContacts.zToolbarPressButton(Button.B_NEW);

		// Fill the fields
		formContactNew.zFill(contact);

		// Click Cancel
		DialogWarning dialogWarning = (DialogWarning) formContactNew.zToolbarPressButton(Button.B_CANCEL);

		// Click Yes in popup dialog
		dialogWarning.zClickButton(Button.B_CANCEL);

		// Verify the contact form comes back
		ZAssert.assertTrue(formContactNew.zIsActive(), "Verify the contact form comes back");

		// Save the contact
		formContactNew.zSubmit();

		// -- Data Verification

		ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:" + contact.firstName);

		ZAssert.assertEquals(actual.lastName, contact.lastName, "Verify the last name was saved correctly");
		ZAssert.assertEquals(actual.firstName, contact.firstName, "Verify the last name was saved correctly");
		ZAssert.assertEquals(actual.email, contact.email, "Verify the last name was saved correctly");

	}

	@Test(description = "Create a contact item with all attributes", 
			groups = { "functional", "L2"})

	public void CreateContactWithAllAttributes_06() throws HarnessException {

		// -- Data

		String firstname = "first" + ConfigProperties.getUniqueString();

		// Create a contact Item
		HashMap<String, String> attributes = new HashMap<String, String>();
		attributes.put("firstName", firstname);
		attributes.put("lastName", "lastname" + ConfigProperties.getUniqueString());
		attributes.put("email", "email" + ConfigProperties.getUniqueString() + "@zimbra.com");
		attributes.put("company", "company" + ConfigProperties.getUniqueString());
		attributes.put("middleName", "middleName" + ConfigProperties.getUniqueString());
		attributes.put("nickname", "nickname" + ConfigProperties.getUniqueString());
		attributes.put("nameSuffix", "Sr");
		attributes.put("namePrefix", "Mr");
		attributes.put("department", "department" + ConfigProperties.getUniqueString());
		attributes.put("jobTitle", "jobTitle" + ConfigProperties.getUniqueString());
		attributes.put("homeStreet", "123 Main St.");
		attributes.put("homeCity", "Anytown");
		attributes.put("homeCountry", "USA");
		attributes.put("homePostalCode", "95124");
		attributes.put("birthday", "1985-05-24");
		attributes.put("notes", "notes" + ConfigProperties.getUniqueString());
		attributes.put("maidenName", "maidenName" + ConfigProperties.getUniqueString());
		// attributes.put("mobilePhone", "1-408-555-1212");
		attributes.put("imAddress1", "free2rhyme@yahoo.com");
		attributes.put("homeURL", "http://www.zimbra.com");

		// -- GUI
		FormContactNew formContactNew = (FormContactNew) app.zPageContacts.zToolbarPressButton(Button.B_NEW);

		// show all hidden field for names:
		formContactNew.zDisplayHiddenName();

		// fill items
		for (Entry<String, String> entry : attributes.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			formContactNew.zFillField(Field.fromString(key), value);
		}

		// Save the contact
		formContactNew.zSubmit();

		// -- Verificaiton

		// verify toasted message 'contact created'
		ZAssert.assertStringContains(app.zPageMain.zGetToaster().zGetToastMessage(), "Contact Created",
				"Verify toast message 'Contact Created'");

		// -- Data Verification

		ContactItem contact = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:" + firstname);
		ZAssert.assertNotNull(contact, "Verify the contact was saved correctly");

		for (Entry<String, String> entry : attributes.entrySet()) {

			// Verify each attribute was saved correctly
			String key = entry.getKey();
			String expected = attributes.get(key);
			String actual = contact.ContactAttributes.get(key);

			if (key.equals("imAddress1")) {
				// IM address will be prepended with xmpp:// (for example), so
				// just make sure the address is there
				ZAssert.assertStringContains(actual, expected, "Verify the attribute " + key + " was saved correctly");
			} else {
				ZAssert.assertEquals(actual, expected, "Verify the attribute " + key + " was saved correctly");
			}
		}

	}

	@Bugs(ids = "99776")
	@Test(description = "Create a contacts with non-ASCII special characters", 
	groups = { "functional", "charsets" , "L3"}, dataProvider = "DataProviderSupportedCharsets")

	public void CreateContact_07(ZCharset charset, String charsetSample) throws HarnessException {

		// -- DATA

		String contactFirst = charsetSample;
		String contactLast = charsetSample;
		String contactEmail = charsetSample + "@domain.com";

		// -- GUI Action

		// app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		FormContactNew formContactNew = (FormContactNew) app.zPageContacts.zToolbarPressButton(Button.B_NEW);

		// Fill in the form
		formContactNew.zFillField(Field.FirstName, contactFirst);
		formContactNew.zFillField(Field.LastName, contactLast);
		formContactNew.zFillField(Field.Email, contactEmail);
		formContactNew.zSubmit();

		// -- Data Verification

		app.zGetActiveAccount().soapSend("<SearchRequest xmlns='urn:zimbraMail' types='contact'>" + "<query>#firstname:"
				+ contactFirst + "</query>" + "</SearchRequest>");
		String contactId = app.zGetActiveAccount().soapSelectValue("//mail:cn", "id");

		ZAssert.assertNotNull(contactId, "Verify the contact is returned in the search");

		app.zGetActiveAccount().soapSend("<GetContactsRequest xmlns='urn:zimbraMail'>" + "<cn id='" + contactId + "'/>"
				+ "</GetContactsRequest>");

		String lastname = app.zGetActiveAccount()
				.soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='lastName']", null);
		String firstname = app.zGetActiveAccount()
				.soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='firstName']", null);
		String email = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='email']",
				null);

		ZAssert.assertEquals(lastname, contactLast, "Verify the last name was saved correctly");
		ZAssert.assertEquals(firstname, contactFirst, "Verify the first name was saved correctly");
		ZAssert.assertEquals(email, contactEmail, "Verify the email was saved correctly");

	}

	@Bugs(ids = "66497")
	@Test(description = "Create contact by selecting birthday and anniversary date using date picker", 
	groups = {"functional", "L2"})

	public void CreateContactBySelectingDateUsingDatePicker_08() throws HarnessException {

		// -- Data

		String firstname = "first" + ConfigProperties.getUniqueString();

		// Create a contact Item
		HashMap<String, String> attributes = new HashMap<String, String>();
		attributes.put("firstName", firstname);
		attributes.put("lastName", "lastname" + ConfigProperties.getUniqueString());
		attributes.put("email", "email" + ConfigProperties.getUniqueString() + "@zimbra.com");

		// -- GUI
		FormContactNew formContactNew = (FormContactNew) app.zPageContacts.zToolbarPressButton(Button.B_NEW);

		// fill items
		for (Entry<String, String> entry : attributes.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			formContactNew.zFillField(Field.fromString(key), value);
		}

		// Add birthday as 20th of the current month from date picker
		formContactNew.sClickAt(Locators.zOther1DatePickerPullDown, "0,0");
		formContactNew.sClickAt(Locators.zDay20InDatePicker, "");

		// Add another row in Others filed to add Anniversary date
		formContactNew.sClickAt(Locators.zAddNewOtherRowBtn, "0,0");

		// Select the Anniversary Option from drop down
		formContactNew.sClick(Locators.zOther2OptionsPullDown);
		formContactNew.sClickAt(Locators.zAnniversaryOption, "0,0");

		// Select Anniversary date as 20th
		formContactNew.sClickAt(Locators.zOther2DatePickerPullDown, "0,0");
		formContactNew.sClickAt(Locators.zDay20InDatePicker, "");

		// Verify that selected dates are entered in the fields
		ZAssert.assertStringContains(formContactNew.sGetValue(Locators.zOther1EditField), "20",
				"Entered date is not matching with the date entered through date picker");
		ZAssert.assertStringContains(formContactNew.sGetValue(Locators.zOther2EditField), "20",
				"Entered date is not matching with the date entered through date picker");

		// Save the contact
		formContactNew.zSubmit();

		// -- Data Verification
		ContactItem contact = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:" + firstname);
		ZAssert.assertNotNull(contact, "Verify the contact was saved correctly");

		for (Entry<String, String> entry : attributes.entrySet()) {

			// Verify each attribute was saved correctly
			String key = entry.getKey();
			String expected = attributes.get(key);
			String actual = contact.ContactAttributes.get(key);

			if (key.equals("imAddress1")) {
				// IM address will be prepended with xmpp:// (for example), so
				// just make sure the address is there
				ZAssert.assertStringContains(actual, expected, "Verify the attribute " + key + " was saved correctly");
			} else {
				ZAssert.assertEquals(actual, expected, "Verify the attribute " + key + " was saved correctly");
			}
		}

	}

}
