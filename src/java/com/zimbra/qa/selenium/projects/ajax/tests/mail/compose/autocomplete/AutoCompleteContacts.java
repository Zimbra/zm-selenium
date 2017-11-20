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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.autocomplete;

import java.util.List;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class AutoCompleteContacts extends SetGroupMailByMessagePreference {

	public AutoCompleteContacts() {
		logger.info("New "+ AutoCompleteContacts.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}


	@Test (description = "Autocomplete using a Contact - First Name",
			groups = { "functional", "L2" })

	public void AutoCompleteContacts_01() throws HarnessException {

		// Create a contact
		ZimbraAccount contact = new ZimbraAccount();
		contact.provision();
		contact.authenticate();

		String firstname = "Michael" + ConfigProperties.getUniqueString();
		String lastname = "Williams" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn>"
				+			"<a n='firstName'>"+ firstname +"</a>"
				+			"<a n='lastName'>"+ lastname +"</a>"
				+			"<a n='email'>"+ contact.EmailAddress +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");

		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Message properties
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Auto complete a name
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, firstname);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		mailform.zAutocompleteSelectItem(found);

		// Send the message
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(contact, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}


	@Test (description = "Autocomplete using the Contacts - Partial First Name",
			groups = { "functional", "L2" })

	public void AutoCompleteContacts_02() throws HarnessException {

		// Create a contact
		ZimbraAccount contact = new ZimbraAccount();
		contact.provision();
		contact.authenticate();

		String firstname = "Michael" + ConfigProperties.getUniqueString();
		String lastname = "Williams" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn>"
				+			"<a n='firstName'>"+ firstname +"</a>"
				+			"<a n='lastName'>"+ lastname +"</a>"
				+			"<a n='email'>"+ contact.EmailAddress +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");

		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Message properties
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Auto complete a name
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, firstname.substring(0, 5));
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		mailform.zAutocompleteSelectItem(found);

		// Send the message
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(contact, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}


	@Test (description = "Autocomplete using a Contact - Last Name",
			groups = { "functional", "L3" })

	public void AutoCompleteContacts_03() throws HarnessException {

		// Create a contact
		ZimbraAccount contact = new ZimbraAccount();
		contact.provision();
		contact.authenticate();

		String firstname = "Michael" + ConfigProperties.getUniqueString();
		String lastname = "Williams" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn>"
				+			"<a n='firstName'>"+ firstname +"</a>"
				+			"<a n='lastName'>"+ lastname +"</a>"
				+			"<a n='email'>"+ contact.EmailAddress +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");

		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Message properties
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Auto complete a name
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, lastname);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		mailform.zAutocompleteSelectItem(found);

		// Send the message
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(contact, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}


	@Test (description = "Autocomplete using a Contact - Partial Last Name",
			groups = { "functional", "L3" })

	public void AutoCompleteContacts_04() throws HarnessException {

		// Create a contact
		ZimbraAccount contact = new ZimbraAccount();
		contact.provision();
		contact.authenticate();

		String firstname = "Michael" + ConfigProperties.getUniqueString();
		String lastname = "Williams" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn>"
				+			"<a n='firstName'>"+ firstname +"</a>"
				+			"<a n='lastName'>"+ lastname +"</a>"
				+			"<a n='email'>"+ contact.EmailAddress +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");

		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Message properties
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Auto complete a name
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, lastname.substring(0, 5));
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		mailform.zAutocompleteSelectItem(found);

		// Send the message
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(contact, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}


	@Test (description = "Autocomplete using a Contact - Full Name",
			groups = { "functional", "L3" })

	public void AutoCompleteContacts_05() throws HarnessException {

		// Create a contact
		ZimbraAccount contact = new ZimbraAccount();
		contact.provision();
		contact.authenticate();

		String firstname = "Michael" + ConfigProperties.getUniqueString();
		String lastname = "Williams" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn>"
				+			"<a n='firstName'>"+ firstname +"</a>"
				+			"<a n='lastName'>"+ lastname +"</a>"
				+			"<a n='email'>"+ contact.EmailAddress +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");

		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Message properties
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Auto complete a name
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, firstname + " " + lastname);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		mailform.zAutocompleteSelectItem(found);

		// Send the message
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(contact, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}


	@Test (description = "Autocomplete using a Contact - First Name and Last Initial",
			groups = { "functional", "L3" })

	public void AutoCompleteContacts_07() throws HarnessException {

		// Create a contact
		ZimbraAccount contact = new ZimbraAccount();
		contact.provision();
		contact.authenticate();

		String firstname = "Michael" + ConfigProperties.getUniqueString();
		String lastname = "Williams" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn>"
				+			"<a n='firstName'>"+ firstname +"</a>"
				+			"<a n='lastName'>"+ lastname +"</a>"
				+			"<a n='email'>"+ contact.EmailAddress +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");

		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Message properties
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Auto complete a name
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, firstname + " " + lastname.substring(0, 1));
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		mailform.zAutocompleteSelectItem(found);

		// Send the message
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(contact, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}


	@Test (description = "Autocomplete using a Contact - Multiple Matches",
			groups = { "functional", "L2" })

	public void AutoCompleteContacts_08() throws HarnessException {

		int count = 3;

		// Message properties
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		String firstname = "Jayden" + ConfigProperties.getUniqueString();
		for (int i = 0; i < count; i++) {

			// Create a contact
			ZimbraAccount contact = new ZimbraAccount();
			contact.provision();
			contact.authenticate();

			String lastname = "Jones" + ConfigProperties.getUniqueString();

			app.zGetActiveAccount().soapSend(
						"<CreateContactRequest xmlns='urn:zimbraMail'>"
					+		"<cn>"
					+			"<a n='firstName'>"+ firstname +"</a>"
					+			"<a n='lastName'>"+ lastname +"</a>"
					+			"<a n='email'>"+ contact.EmailAddress +"</a>"
					+		"</cn>"
					+	"</CreateContactRequest>");
		}

		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Auto complete a name
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, firstname);

		ZAssert.assertEquals(entries.size(), count, "Verify the correct number of results were returned");

		mailform.zAutocompleteSelectItem(entries.get(1));

		// Send the message
		mailform.zSubmit();
	}


	@Test (description = "Autocomplete using a Contact - No Matches",
			groups = { "functional", "L2" })

	public void AutoCompleteContacts_09() throws HarnessException {

		// Create a contact
		ZimbraAccount contact = new ZimbraAccount();
		contact.provision();
		contact.authenticate();

		String firstname = "Michael" + ConfigProperties.getUniqueString();
		String lastname = "Williams" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn>"
				+			"<a n='firstName'>"+ firstname +"</a>"
				+			"<a n='lastName'>"+ lastname +"</a>"
				+			"<a n='email'>"+ contact.EmailAddress +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");

		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Message properties
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Auto complete a name
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, "nomatchstring");

		ZAssert.assertEquals(entries.size(), 0, "Verify zero results were returned");

		DialogWarning dialog = (DialogWarning)mailform.zToolbarPressButton(Button.B_CANCEL);
		if ( dialog.zIsActive() ) {
			dialog.zPressButton(Button.B_NO);
		}
	}


	@Test (description = "Autocomplete should match contacts in addressbooks and subaddressbooks",
			groups = { "functional", "L3" })

	public void AutoCompleteContacts_10() throws HarnessException {

		// Create a contact in a addressbook
		ZimbraAccount contact1 = new ZimbraAccount();
		contact1.provision();
		contact1.authenticate();

		String firstname = "Charles" + ConfigProperties.getUniqueString();
		String lastname = "Anderson" + ConfigProperties.getUniqueString();

		// Create an addressbook
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='addressbook" + ConfigProperties.getUniqueString() +"' l='"+ FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Contacts).getId() +"' view='contact'/>"
				+	"</CreateFolderRequest>");
		String folderID = app.zGetActiveAccount().soapSelectValue("//mail:folder", "id");

		// Create a contact
		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn l='"+ folderID +"'>"
				+			"<a n='firstName'>"+ firstname +"</a>"
				+			"<a n='lastName'>"+ lastname +"</a>"
				+			"<a n='email'>"+ contact1.EmailAddress +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");

		// Create a contact in a addressbook
		ZimbraAccount contact2 = new ZimbraAccount();
		contact2.provision();
		contact2.authenticate();

		firstname = "Charles" + ConfigProperties.getUniqueString();
		lastname = "Thomas" + ConfigProperties.getUniqueString();

		// Create an addressbook
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='subaddressbook" + ConfigProperties.getUniqueString() +"' l='"+ folderID +"' view='contact'/>"
				+	"</CreateFolderRequest>");
		String subfolderID = app.zGetActiveAccount().soapSelectValue("//mail:folder", "id");

		// Create a contact
		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn l='"+ subfolderID +"'>"
				+			"<a n='firstName'>"+ firstname +"</a>"
				+			"<a n='lastName'>"+ lastname +"</a>"
				+			"<a n='email'>"+ contact2.EmailAddress +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");

		// Refresh changes
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Message properties
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, "Charles");
		AutocompleteEntry found1 = null;
		AutocompleteEntry found2 = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(contact1.EmailAddress) ) {
				found1 = entry;
			}
			if ( entry.getAddress().contains(contact2.EmailAddress) ) {
				found2 = entry;
			}
		}

		ZAssert.assertNotNull(found1, "Verify contacts in an addresbook are matched");
		ZAssert.assertNotNull(found2, "Verify contacts in a sub-addresbook are matched");

		// Cancel the compose
		DialogWarning dialog = (DialogWarning)mailform.zToolbarPressButton(Button.B_CANCEL);
		if ( dialog.zIsActive() ) {
			dialog.zPressButton(Button.B_NO);
		}
	}


	@Bugs (ids = "47045")
	@Test (description = "Autocomplete including a period/dot '.' in the string",
			groups = { "functional", "L3" })

	public void AutoCompleteContactsBug47045_11() throws HarnessException {

		// Create a contact
		ZimbraAccount contact = new ZimbraAccount();
		contact.provision();
		contact.authenticate();

		String firstname = "Michael" + ConfigProperties.getUniqueString();
		String lastname = "Williams" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn>"
				+			"<a n='firstName'>"+ firstname +"</a>"
				+			"<a n='lastName'>"+ lastname +"</a>"
				+			"<a n='email'>"+ contact.EmailAddress +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");

		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Message properties
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Auto complete a name based on email address such as "user@domain."
		String value = contact.EmailAddress.substring(0, contact.EmailAddress.indexOf('.') + 1);
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, value);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		mailform.zAutocompleteSelectItem(found);

		// Send the message
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(contact, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}


	@Bugs (ids = "47045")
	@Test (description = "Autocomplete including a period/dot '.' in the string",
			groups = { "functional", "L3" })

	public void AutoCompleteContactsBug47045_12() throws HarnessException {

		// Create a contact
		ZimbraAccount contact = new ZimbraAccount();
		contact.provision();
		contact.authenticate();

		String firstname = "Michael" + ConfigProperties.getUniqueString();
		String lastname = "Williams" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn>"
				+			"<a n='firstName'>"+ firstname +"</a>"
				+			"<a n='lastName'>"+ lastname +"</a>"
				+			"<a n='email'>"+ contact.EmailAddress +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");

		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Message properties
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Auto complete a name based on email address such as "user@domain.c"
		String value = contact.EmailAddress.substring(0, contact.EmailAddress.indexOf('.') + 2);
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, value);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		mailform.zAutocompleteSelectItem(found);

		// Send the message
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(contact, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}


	@Test (description = "Autocomplete should not match trashed contacts",
			groups = { "functional", "L2" })

	public void AutoCompleteContacts_Bug47044_13() throws HarnessException {

		// Create a contact
		ZimbraAccount contact = new ZimbraAccount();
		contact.provision();
		contact.authenticate();

		String firstname = "Richard" + ConfigProperties.getUniqueString();
		String lastname = "Moore" + ConfigProperties.getUniqueString();

		// Create a contact
		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn>"
				+			"<a n='firstName'>"+ firstname +"</a>"
				+			"<a n='lastName'>"+ lastname +"</a>"
				+			"<a n='email'>"+ contact.EmailAddress +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");
		String contactID = app.zGetActiveAccount().soapSelectValue("//mail:cn", "id");

		// Move to trash
		app.zGetActiveAccount().soapSend(
					"<ItemActionRequest xmlns='urn:zimbraMail'>"
				+		"<action op='move' l='"+ FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Trash).getId() +"' id='"+ contactID + "'/>"
				+	"</ItemActionRequest>");

		// Refresh changes
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Message properties
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, firstname);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}

		ZAssert.assertNull(found, "Verify contacts in trash are not matched");

		// Cancel the compose
		DialogWarning dialog = (DialogWarning)mailform.zToolbarPressButton(Button.B_CANCEL);
		if ( dialog.zIsActive() ) {
			dialog.zPressButton(Button.B_NO);
		}
	}


	@Test (description = "Autocomplete should not match deleted contacts",
			groups = { "functional", "L2" })

	public void AutoCompleteContacts_Bug47044_14() throws HarnessException {

		// Create a contact
		ZimbraAccount contact = new ZimbraAccount();
		contact.provision();
		contact.authenticate();

		String firstname = "Richard" + ConfigProperties.getUniqueString();
		String lastname = "Moore" + ConfigProperties.getUniqueString();

		// Create a contact
		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn>"
				+			"<a n='firstName'>"+ firstname +"</a>"
				+			"<a n='lastName'>"+ lastname +"</a>"
				+			"<a n='email'>"+ contact.EmailAddress +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");
		String contactID = app.zGetActiveAccount().soapSelectValue("//mail:cn", "id");

		// Move to trash
		app.zGetActiveAccount().soapSend(
					"<ItemActionRequest xmlns='urn:zimbraMail'>"
				+		"<action op='delete' id='"+ contactID + "'/>"
				+	"</ItemActionRequest>");


		// Refresh changes
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Message properties
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, firstname);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}

		ZAssert.assertNull(found, "Verify contacts in trash are not matched");

		// Cancel the compose
		DialogWarning dialog = (DialogWarning)mailform.zToolbarPressButton(Button.B_CANCEL);
		if ( dialog.zIsActive() ) {
			dialog.zPressButton(Button.B_NO);
		}
	}


	@Bugs (ids = "47044")
	@Test (description = "Autocomplete should not match contacts in trashed addressbook",
			groups = { "functional", "L3" })

	public void AutoCompleteContacts_Bug47044_15() throws HarnessException {

		// Create a contact
		ZimbraAccount contact = new ZimbraAccount();
		contact.provision();
		contact.authenticate();

		String firstname = "Richard" + ConfigProperties.getUniqueString();
		String lastname = "Moore" + ConfigProperties.getUniqueString();

		String foldername = "addressbook" + ConfigProperties.getUniqueString();

		// Create an addressbook
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='"+ foldername +"' l='"+ FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Contacts).getId() +"' view='contact'/>"
				+	"</CreateFolderRequest>");
		String folderID = app.zGetActiveAccount().soapSelectValue("//mail:folder", "id");

		// Create a contact
		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn l='"+ folderID +"'>"
				+			"<a n='firstName'>"+ firstname +"</a>"
				+			"<a n='lastName'>"+ lastname +"</a>"
				+			"<a n='email'>"+ contact.EmailAddress +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");

		// Move the addressbook to trash
		app.zGetActiveAccount().soapSend(
					"<ItemActionRequest xmlns='urn:zimbraMail'>"
				+		"<action op='move' id='"+ folderID + "' l='"+ FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Trash).getId() +"'/>"
				+	"</ItemActionRequest>");

		// Refresh changes
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Message properties
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, firstname);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}

		ZAssert.assertNull(found, "Verify contacts in an addresbook in trash are not matched");

		// Cancel the compose
		DialogWarning dialog = (DialogWarning)mailform.zToolbarPressButton(Button.B_CANCEL);
		if ( dialog.zIsActive() ) {
			dialog.zPressButton(Button.B_NO);
		}
	}
}