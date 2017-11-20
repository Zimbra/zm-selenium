/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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

import java.util.*;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class AutoCompleteForget extends SetGroupMailByMessagePreference {

	public AutoCompleteForget() {
		logger.info("New "+ AutoCompleteForget.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
		super.startingAccountPreferences.put("zimbraPrefAutoAddAddressEnabled", "FALSE");
	}


	/**
	 * Steps:
	 * 1. Compose a message to a new email address (i.e. non-contact)
	 * 2. Send
	 * 3. Compose a message to the same email address
	 * 4. Forget the auto-complete address
	 * 5. Compose a message to the same email address
	 * 6. Verify the auto-complete does not suggest the address
	 */

	@Bugs (ids = "102053")
	@Test (description = "Forget an autocomplete address - invalid email",
			groups = { "functional-skip", "application-bug" })

	public void AutoCompleteForget_01() throws HarnessException {

		// Create a contact
		String emailaddress = "foo"+ ConfigProperties.getUniqueString() + "@testdomain.zimbra.com";

		// Send a message to the invalid email

		// Message properties
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);
		mailform.zFillField(Field.To, emailaddress);
		mailform.zSubmit();

		// Open the new mail form
		mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Auto complete a name
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, emailaddress.substring(0, emailaddress.indexOf('@')));
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(emailaddress) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");

		// Forget this address
		mailform.zAutocompleteForgetItem(found);

		// Cancel the compose
		DialogWarning dialog = (DialogWarning)mailform.zToolbarPressButton(Button.B_CANCEL);
		if ( dialog.zIsActive() ) {
			dialog.zPressButton(Button.B_NO);
		}

		// Open the new mail form
		mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Auto complete a name
		entries = mailform.zAutocompleteFillField(Field.To, emailaddress.substring(0, emailaddress.indexOf('@')));
		found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(emailaddress) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNull(found, "Verify the autocomplete does not return the same email");

		// Cancel the compose
		dialog = (DialogWarning)mailform.zToolbarPressButton(Button.B_CANCEL);
		if ( dialog.zIsActive() ) {
			dialog.zPressButton(Button.B_NO);
		}
	}


	@Test (description = "Verify the 'forget' link for Contacts",
			groups = { "functional", "L2" })

	public void AutoCompleteForget_02() throws HarnessException {

		// Create two contacts
		String emailaddress = "admin@" + ConfigProperties.getStringProperty("server.host");
		String firstname = "Michael" + ConfigProperties.getUniqueString();
		String lastname = "Williams" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn>"
				+			"<a n='firstName'>"+ firstname +"</a>"
				+			"<a n='lastName'>"+ lastname +"</a>"
				+			"<a n='email'>"+ emailaddress +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");

		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>"
			+		"<m>"
			+			"<e t='t' a='"+ emailaddress +"'/>"
			+			"<su>subject"+ ConfigProperties.getUniqueString() +"</su>"
			+			"<mp ct='text/plain'>"
			+				"<content>content"+ ConfigProperties.getUniqueString() +"</content>"
			+			"</mp>"
			+		"</m>"
			+	"</SendMsgRequest>");

		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Send a message to the invalid email

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
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, emailaddress.substring(0, emailaddress.indexOf('@')));
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(emailaddress) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");

		// Forget the item
		mailform.zAutocompleteForgetItem(found);

		// Cancel the compose
		DialogWarning dialog = (DialogWarning)mailform.zToolbarPressButton(Button.B_CANCEL);
		if ( dialog.zIsActive() ) {
			dialog.zPressButton(Button.B_NO);
		}
	}


	@Bugs (ids = "97118")
	@Test (description = "Verify the 'forget' link for GAL",
			groups = { "functional", "L3" })

	public void AutoCompleteForget_03() throws HarnessException {

		final String givenName = "Christopher" + ConfigProperties.getUniqueString();
		final String sn = "White" + ConfigProperties.getUniqueString();
		final String displayName = givenName + " " + sn;

		// Create a GAL Entry
		ZimbraAccount account = new ZimbraAccount();
		Map<String,String> attrs = new HashMap<String, String>() {
			private static final long serialVersionUID = -939087202049217526L;
			{
				put("givenName", givenName);
				put("sn", sn);
				put("displayName", displayName);
			}};
		account.setAccountPreferences(attrs);
		account.provision();
		account.authenticate();

		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>"
			+		"<m>"
			+			"<e t='t' a='"+ account.EmailAddress +"'/>"
			+			"<su>subject"+ ConfigProperties.getUniqueString() +"</su>"
			+			"<mp ct='text/plain'>"
			+				"<content>content"+ ConfigProperties.getUniqueString() +"</content>"
			+			"</mp>"
			+		"</m>"
			+	"</SendMsgRequest>");

		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Send a message to the invalid email

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
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, givenName);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(account.EmailAddress) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");

		// Forget the item
		mailform.zAutocompleteForgetItem(found);

		// Cancel the compose
		DialogWarning dialog = (DialogWarning)mailform.zToolbarPressButton(Button.B_CANCEL);
		if ( dialog.zIsActive() ) {
			dialog.zPressButton(Button.B_NO);
		}
	}


	/**
	 * 1. Create two contacts, Acontact and Bcontact
	 * 2. Send one message to Acontact
	 * 3. Send two messages to Bcontact
	 * 4. Compose new message, verify Bcontact comes before Acontact
	 * 5. Forget Bcontact
	 * 6. Compose new message, verify Acontact comes before Bcontact
	 */

	@Bugs (ids = "97123")
	@Test (description = "Verify 'forget' functionality resets the ranking order - Contacts",
			groups = { "functional", "L2" })

	public void AutoCompleteForget_04() throws HarnessException {

		// Create two contacts
		ZimbraAccount contact1 = new ZimbraAccount();
		contact1.provision();
		contact1.authenticate();

		String emailaddress1 = contact1.EmailAddress;
		String firstname1 = "PaulOne" + ConfigProperties.getUniqueString();
		String lastname1 = "Harris" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn>"
				+			"<a n='firstName'>"+ firstname1 +"</a>"
				+			"<a n='lastName'>"+ lastname1 +"</a>"
				+			"<a n='email'>"+ emailaddress1 +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");

		// Send one message
		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>"
			+		"<m>"
			+			"<e t='t' a='"+ emailaddress1 +"'/>"
			+			"<su>subject"+ ConfigProperties.getUniqueString() +"</su>"
			+			"<mp ct='text/plain'>"
			+				"<content>content"+ ConfigProperties.getUniqueString() +"</content>"
			+			"</mp>"
			+		"</m>"
			+	"</SendMsgRequest>");

		// Create a second contact
		ZimbraAccount contact2 = new ZimbraAccount();
		contact2.provision();
		contact2.authenticate();

		String emailaddress2 = contact2.EmailAddress;
		String firstname2 = "PaulTwo" + ConfigProperties.getUniqueString();
		String lastname2 = "Harris" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateContactRequest xmlns='urn:zimbraMail'>"
				+		"<cn>"
				+			"<a n='firstName'>"+ firstname2 +"</a>"
				+			"<a n='lastName'>"+ lastname2 +"</a>"
				+			"<a n='email'>"+ emailaddress2 +"</a>"
				+		"</cn>"
				+	"</CreateContactRequest>");

		// Send two messages
		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>"
			+		"<m>"
			+			"<e t='t' a='"+ emailaddress2 +"'/>"
			+			"<su>subject"+ ConfigProperties.getUniqueString() +"</su>"
			+			"<mp ct='text/plain'>"
			+				"<content>content"+ ConfigProperties.getUniqueString() +"</content>"
			+			"</mp>"
			+		"</m>"
			+	"</SendMsgRequest>");

		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>"
			+		"<m>"
			+			"<e t='t' a='"+ emailaddress2 +"'/>"
			+			"<su>subject"+ ConfigProperties.getUniqueString() +"</su>"
			+			"<mp ct='text/plain'>"
			+				"<content>content"+ ConfigProperties.getUniqueString() +"</content>"
			+			"</mp>"
			+		"</m>"
			+	"</SendMsgRequest>");


		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Send a message to the invalid email

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
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, "Paul");
		ZAssert.assertNotNull(entries, "Verify the autocomplete entry exists in the returned list");

		// Determine the ranking
		int index1 = 0;
		int index2 = 0;
		AutocompleteEntry found2 = null;
		for (int i = 0; i < entries.size(); i++) {
			if ( entries.get(i).getAddress().contains(emailaddress1) ) {
				index1 = i;
			}
			if ( entries.get(i).getAddress().contains(emailaddress2) ) {
				index2 = i;
				found2 = entries.get(i);
			}
		}

		ZAssert.assertLessThan(index2, index1, "Verify that Contact2 (two messages) is ranked higher than Contact1 (one message)");

		// Forget contact2
		mailform.zAutocompleteForgetItem(found2);

		// Cancel the compose
		DialogWarning dialog = (DialogWarning)mailform.zToolbarPressButton(Button.B_CANCEL);
		if ( dialog.zIsActive() ) {
			dialog.zPressButton(Button.B_NO);
		}

		// Compose again, verify contact1 (one message) is higher than contact2 (forgotten)
		mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Auto complete a name
		entries = mailform.zAutocompleteFillField(Field.To, "Paul");
		ZAssert.assertNotNull(entries, "Verify the autocomplete entry exists in the returned list");

		// Determine the ranking
		index1 = 0;
		index2 = 0;
		found2 = null;
		for (int i = 0; i < entries.size(); i++) {
			if ( entries.get(i).getAddress().contains(emailaddress1) ) {
				index1 = i;
			}
			if ( entries.get(i).getAddress().contains(emailaddress2) ) {
				index2 = i;
				found2 = entries.get(i);
			}
		}

		ZAssert.assertLessThan(index1, index2, "Verify that Contact1 (one message) is ranked higher than Contact2 (forgotten)");

		// Cancel the compose
		dialog = (DialogWarning)mailform.zToolbarPressButton(Button.B_CANCEL);
		if ( dialog.zIsActive() ) {
			dialog.zPressButton(Button.B_NO);
		}
	}


	/**
	 * 1. Create two GAL accounts, Acontact and Bcontact
	 * 2. Send one message to Acontact
	 * 3. Send two messages to Bcontact
	 * 4. Compose new message, verify Bcontact comes before Acontact
	 * 5. Forget Bcontact
	 * 6. Compose new message, verify Acontact comes before Bcontact
	 */

	@Bugs (ids = "97118")
	@Test (description = "Verify 'forget' functionality resets the ranking order - GAL",
			groups = { "functional", "L3" })

	public void AutoCompleteForget_05() throws HarnessException {

		// Create a GAL Entry
		final String givenName1 = "Mark" + ConfigProperties.getUniqueString();
		final String sn1 = "Martin" + ConfigProperties.getUniqueString();
		final String displayName1 = givenName1 + " " + sn1;

		// Create a GAL Entry
		ZimbraAccount account1 = new ZimbraAccount();
		Map<String,String> attrs1 = new HashMap<String, String>() {
			private static final long serialVersionUID = -939087202048217526L;
			{
				put("givenName", givenName1);
				put("sn", sn1);
				put("displayName", displayName1);
			}};
		account1.setAccountPreferences(attrs1);
		account1.provision();
		account1.authenticate();

		// Send one message
		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>"
			+		"<m>"
			+			"<e t='t' a='"+ account1.EmailAddress +"'/>"
			+			"<su>subject"+ ConfigProperties.getUniqueString() +"</su>"
			+			"<mp ct='text/plain'>"
			+				"<content>content"+ ConfigProperties.getUniqueString() +"</content>"
			+			"</mp>"
			+		"</m>"
			+	"</SendMsgRequest>");

		final String givenName2 = "Mark" + ConfigProperties.getUniqueString();
		final String sn2 = "Martin" + ConfigProperties.getUniqueString();
		final String displayName2 = givenName2 + " " + sn2;

		// Create a GAL Entry
		ZimbraAccount account2 = new ZimbraAccount();
		Map<String, String> attrs2 = new HashMap<String, String>() {
			private static final long serialVersionUID = -939087201048217526L;
			{
				put("givenName", givenName2);
				put("sn", sn2);
				put("displayName", displayName2);
			}};
		account2.setAccountPreferences(attrs2);
		account2.provision();
		account2.authenticate();

		// Send two messages
		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>"
			+		"<m>"
			+			"<e t='t' a='"+ account2.EmailAddress +"'/>"
			+			"<su>subject"+ ConfigProperties.getUniqueString() +"</su>"
			+			"<mp ct='text/plain'>"
			+				"<content>content"+ ConfigProperties.getUniqueString() +"</content>"
			+			"</mp>"
			+		"</m>"
			+	"</SendMsgRequest>");

		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>"
			+		"<m>"
			+			"<e t='t' a='"+ account2.EmailAddress +"'/>"
			+			"<su>subject"+ ConfigProperties.getUniqueString() +"</su>"
			+			"<mp ct='text/plain'>"
			+				"<content>content"+ ConfigProperties.getUniqueString() +"</content>"
			+			"</mp>"
			+		"</m>"
			+	"</SendMsgRequest>");

		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Send a message to the invalid email

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
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, "Mark");
		ZAssert.assertNotNull(entries, "Verify the autocomplete entry exists in the returned list");

		// Determine the ranking
		int index1 = 0;
		int index2 = 0;
		AutocompleteEntry found2 = null;
		for (int i = 0; i < entries.size(); i++) {
			if ( entries.get(i).getAddress().contains(account1.EmailAddress) ) {
				index1 = i;
			}
			if ( entries.get(i).getAddress().contains(account2.EmailAddress) ) {
				index2 = i;
				found2 = entries.get(i);
			}
		}

		ZAssert.assertLessThan(index2, index1, "Verify that Contact2 (two messages) is ranked higher than Contact1 (one message)");

		// Forget contact2
		mailform.zAutocompleteForgetItem(found2);

		// Cancel the compose
		DialogWarning dialog = (DialogWarning)mailform.zToolbarPressButton(Button.B_CANCEL);
		if ( dialog.zIsActive() ) {
			dialog.zPressButton(Button.B_NO);
		}

		// Compose again, verify contact1 (one message) is higher than contact2 (forgotten)
		mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Auto complete a name
		entries = mailform.zAutocompleteFillField(Field.To, "Mark");
		ZAssert.assertNotNull(entries, "Verify the autocomplete entry exists in the returned list");

		// Determine the ranking
		index1 = 0;
		index2 = 0;
		found2 = null;
		for (int i = 0; i < entries.size(); i++) {
			if ( entries.get(i).getAddress().contains(account1.EmailAddress) ) {
				index1 = i;
			}
			if ( entries.get(i).getAddress().contains(account2.EmailAddress) ) {
				index2 = i;
				found2 = entries.get(i);
			}
		}

		ZAssert.assertLessThan(index1, index2, "Verify that Contact1 (one message) is ranked higher than Contact2 (forgotten)");

		// Cancel the compose
		dialog = (DialogWarning)mailform.zToolbarPressButton(Button.B_CANCEL);
		if ( dialog.zIsActive() ) {
			dialog.zPressButton(Button.B_NO);
		}
	}
}