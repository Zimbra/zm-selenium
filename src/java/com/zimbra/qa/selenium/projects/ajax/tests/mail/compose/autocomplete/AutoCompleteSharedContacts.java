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
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class AutoCompleteSharedContacts extends SetGroupMailByMessagePreference {

	private ZimbraAccount Owner = null;
	private FolderItem OwnerFolder = null;
	private ZimbraAccount Contact = null;
	private String ContactFirstName = null;
	private String ContactLastName = null;

	public AutoCompleteSharedContacts() {
		logger.info("New "+ AutoCompleteSharedContacts.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
		super.startingAccountPreferences.put("zimbraPrefSharedAddrBookAutoCompleteEnabled", "TRUE");
	}


	@BeforeMethod(alwaysRun = true)
	public void CreateSharedContacts() throws HarnessException {

		Contact = new ZimbraAccount();
		Contact.provision();
		Contact.authenticate();

		Owner = new ZimbraAccount();
		Owner.provision();
		Owner.authenticate();

		ContactFirstName = "Jayden" + ConfigProperties.getUniqueString();
		ContactLastName = "Brown" + ConfigProperties.getUniqueString();
		String ownerFolderName = "folder" + ConfigProperties.getUniqueString();
		String mountpointName = "mountpoint" + ConfigProperties.getUniqueString();

		// Create a folder to share
		Owner.soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + ownerFolderName + "' view='contact' l='" + FolderItem.importFromSOAP(Owner, FolderItem.SystemFolder.Contacts).getId() + "'/>"
				+	"</CreateFolderRequest>");

		OwnerFolder = FolderItem.importFromSOAP(Owner, ownerFolderName);

		// Share it
		Owner.soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ OwnerFolder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='r'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Add a contact to it
		Owner.soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>"
			+		"<cn l='"+ OwnerFolder.getId() +"'>"
			+			"<a n='firstName'>"+ ContactFirstName +"</a>"
			+			"<a n='lastName'>"+ ContactLastName +"</a>"
			+			"<a n='email'>"+ Contact.EmailAddress +"</a>"
			+		"</cn>"
			+	"</CreateContactRequest>");

		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointName +"'  rid='"+ OwnerFolder.getId() +"' zid='"+ Owner.ZimbraId +"'/>"
				+	"</CreateMountpointRequest>");

		FolderMountpointItem mountpoint = FolderMountpointItem.importFromSOAP(app.zGetActiveAccount(), mountpointName);
		ZAssert.assertNotNull(mountpoint, "Verify the mountpoint exists");
	}


	@Test (description = "Autocomplete using a Shared Contact - First Name",
			groups = { "functional", "L2" })

	public void AutoCompleteSharedContacts_01() throws HarnessException {

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
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, ContactFirstName);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(Contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}

		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		mailform.zAutocompleteSelectItem(found);

		// Send the message
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(Contact, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}


	@Test (description = "Autocomplete using the Shared Contacts - Partial First Name",
			groups = { "functional", "L2" })

	public void AutoCompleteSharedContacts_02() throws HarnessException {

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
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, ContactFirstName.substring(0, 5));
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(Contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}

		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		mailform.zAutocompleteSelectItem(found);

		// Send the message
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(Contact, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}


	@Test (description = "Autocomplete using a Shared Contact - Last Name",
			groups = { "functional", "L3" })

	public void AutoCompleteSharedContacts_03() throws HarnessException {

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
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, ContactLastName);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(Contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}

		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		mailform.zAutocompleteSelectItem(found);

		// Send the message
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(Contact, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}


	@Test (description = "Autocomplete using a Shared Contact - Partial Last Name",
			groups = { "functional", "L3" })

	public void AutoCompleteSharedContacts_04() throws HarnessException {

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
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, ContactLastName.substring(0, 5));
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(Contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}

		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		mailform.zAutocompleteSelectItem(found);

		// Send the message
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(Contact, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}


	@Test (description = "Autocomplete using a Shared Contact - Full Name",
			groups = { "functional", "L3" })

	public void AutoCompleteSharedContacts_05() throws HarnessException {

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
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, ContactFirstName + " " + ContactLastName);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(Contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}

		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		mailform.zAutocompleteSelectItem(found);

		// Send the message
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(Contact, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}


	@Test (description = "Autocomplete using a Shared Contact - First Name and Last Initial",
			groups = { "functional", "L3" })

	public void AutoCompleteSharedContacts_07() throws HarnessException {

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
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, ContactFirstName + " " + ContactLastName.substring(0, 1));
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(Contact.EmailAddress) ) {
				found = entry;
				break;
			}
		}

		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		mailform.zAutocompleteSelectItem(found);

		// Send the message
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(Contact, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}


	@Test (description = "Autocomplete using a Shared Contact - Multiple Matches",
			groups = { "functional", "L2" })

	public void AutoCompleteSharedContacts_08() throws HarnessException {

		int count = 3;
		String firstname = "William" + ConfigProperties.getUniqueString();

		for (int i = 0; i < count; i++) {

			// Create a contact
			ZimbraAccount contact = new ZimbraAccount();
			contact.provision();
			contact.authenticate();

			String lastname = "Jones" + ConfigProperties.getUniqueString();

			Owner.soapSend(
						"<CreateContactRequest xmlns='urn:zimbraMail'>"
					+		"<cn l='"+ OwnerFolder.getId() +"'>"
					+			"<a n='firstName'>"+ firstname +"</a>"
					+			"<a n='lastName'>"+ lastname +"</a>"
					+			"<a n='email'>"+ contact.EmailAddress +"</a>"
					+		"</cn>"
					+	"</CreateContactRequest>");
		}

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
		ZAssert.assertEquals(entries.size(), count, "Verify the correct number of results were returned");

		mailform.zAutocompleteSelectItem(entries.get(0));

		// Send the message
		mailform.zSubmit();

		// Check sent message
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		ZAssert.assertNotNull(sent, "Verify the message is sent successfully");
	}


	@Test (description = "Autocomplete using a Shared Contact - No Matches",
			groups = { "functional", "L2" })

	public void AutoCompleteSharedContacts_09() throws HarnessException {

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
}