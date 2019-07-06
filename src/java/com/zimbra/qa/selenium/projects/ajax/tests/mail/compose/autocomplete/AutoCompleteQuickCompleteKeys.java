/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.AutocompleteEntry;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class AutoCompleteQuickCompleteKeys extends SetGroupMailByMessagePreference {

	private static String FirstName = "James" + ConfigProperties.getUniqueString();
	private static String LastName = "Smith" + ConfigProperties.getUniqueString();
	private static ZimbraAccount SampleAccount = null;

	public AutoCompleteQuickCompleteKeys() throws HarnessException {
		logger.info("New "+ AutoCompleteQuickCompleteKeys.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
		super.startingAccountPreferences.put("zimbraPrefGalAutoCompleteEnabled", "TRUE");
		super.startingAccountPreferences.put("zimbraPrefAutoCompleteQuickCompletionOnComma", "TRUE");
	}


	@Test (description = "Type comma (',') to automatically accept autocomplete",
			groups = { "sanity" })

	public void AutoCompleteQuickCompleteKeys_01() throws HarnessException {

		if ( SampleAccount == null ) {
			SampleAccount = new ZimbraAccount();
			SampleAccount.DisplayName = FirstName + " " + LastName;
			SampleAccount.provision();
			SampleAccount.authenticate();
		}

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
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, FirstName);
		ZAssert.assertGreaterThan(entries.size(), 0, "Verify some results are returned");
		mailform.sType("css=div>input[id^=zv__COMPOSE][id$=_to_control]", ",");
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(SampleAccount, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}


	@Test (description = "Type semicolon (';') to automatically accept autocomplete",
			groups = { "sanity" })

	public void AutoCompleteQuickCompleteKeys_02() throws HarnessException {

		if ( SampleAccount == null ) {
			SampleAccount = new ZimbraAccount();
			SampleAccount.DisplayName = FirstName + " " + LastName;
			SampleAccount.provision();
			SampleAccount.authenticate();
		}

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
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, FirstName);
		ZAssert.assertGreaterThan(entries.size(), 0, "Verify some results are returned");
		mailform.sType("css=div>input[id^=zv__COMPOSE][id$=_to_control]", ";");
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(SampleAccount, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}


	@Test (description = "Type tab ('	') to automatically accept autocomplete",
			groups = { "sanity" })

	public void AutoCompleteQuickCompleteKeys_03() throws HarnessException {

		if ( SampleAccount == null ) {
			SampleAccount = new ZimbraAccount();
			SampleAccount.DisplayName = FirstName + " " + LastName;
			SampleAccount.provision();
			SampleAccount.authenticate();
		}

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
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(Field.To, FirstName);
		ZAssert.assertGreaterThan(entries.size(), 0, "Verify some results are returned");
		mailform.sType("css=div>input[id^=zv__COMPOSE][id$=_to_control]", "	");
		mailform.zSubmit();

		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(SampleAccount, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
	}
}