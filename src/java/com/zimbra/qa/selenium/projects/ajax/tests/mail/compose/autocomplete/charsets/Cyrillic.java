/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.autocomplete.charsets;

import java.util.*;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;


public class Cyrillic extends PrefGroupMailByMessageTest {


	
	
	public Cyrillic() {
		logger.info("New "+ Cyrillic.class.getCanonicalName());
		
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
		super.startingAccountPreferences.put("zimbraPrefGalAutoCompleteEnabled", "TRUE");
	
	}
	
	@Bugs(ids = "48736")
	@Test(	description = "Autocomplete using Cyrillic characters in the name - local contact",
			groups = { "functional" })
	public void AutoComplete_01() throws HarnessException {
		
		// Create a contact
		ZimbraAccount contact = new ZimbraAccount();
		contact.provision();
		contact.authenticate();

		
		// Cyrillic: http://jrgraphix.net/r/Unicode/0400-04FF
		String firstname = "\u0422\u0435\u0441\u0442\u043e\u0432\u0430\u044f";
		String lastname = "Wilson";
		
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
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		String body = "body" + ZimbraSeleniumProperties.getUniqueString();
		
		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Set the To field
		// Don't use the autocomplete code, since the Cyrillic will be rejected
		//mailform.zFillField(Field.To, firstname);
		//mailform.zAutocompleteFillField(Field.To, ";");
		//workaround
		mailform.zAutocompleteFillField(Field.To, firstname);

		// Type ';'
		if  (ZimbraSeleniumProperties.isWebDriver()) {
			
		    mailform.sType("css=div>input[id^=zv__COMPOSE][id$=_to_control]", ";");
		    
		} else {
			
			SleepUtil.sleepSmall();
		    mailform.sKeyDown("css=div>input[id^=zv__COMPOSE][id$=_to_control]", "\\59");
		    
		}

		// Send the message
		mailform.zSubmit();

		
		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(contact, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
		
	}

	@Bugs(ids = "48736")
	@Test(	description = "Autocomplete using Cyrillic characters in the name - GAL contact",
			groups = { "functional" })
	public void AutoComplete_02() throws HarnessException {
		
		final String givenName = "\u0422\u0435\u0441\u0442\u043e\u0432\u0430\u044f" + ZimbraSeleniumProperties.getUniqueString();
		final String sn = "Wilson" + ZimbraSeleniumProperties.getUniqueString();
		final String displayName = givenName + " " + sn;
		
		// Create a GAL Entry
		ZimbraAccount contact = new ZimbraAccount();
		Map<String,String> attrs = new HashMap<String, String>() {
			private static final long serialVersionUID = -939087201049217526L;
			{
				put("givenName", givenName);
				put("sn", sn);
				put("displayName", displayName);
			}};
			contact.setAccountPreferences(attrs);
		contact.provision();
		contact.authenticate();
		
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);
		
		// Message properties
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		String body = "body" + ZimbraSeleniumProperties.getUniqueString();
		
		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Set the To field
		// Don't use the autocomplete code, since the Cyrillic will be rejected
		//mailform.zFillField(Field.To, contact.getPref("givenName"));
		//mailform.zAutocompleteFillField(Field.To, ";");
		//workaround
		mailform.zAutocompleteFillField(Field.To, givenName);
		
		// Type ';'
		if  (ZimbraSeleniumProperties.isWebDriver()) {
			
		    mailform.sType("css=div>input[id^=zv__COMPOSE][id$=_to_control]", ";");
		    
		} else {
			
			SleepUtil.sleepSmall();
		    mailform.sKeyDown("css=div>input[id^=zv__COMPOSE][id$=_to_control]", "\\59");
		    
		}

		// Send the message
		mailform.zSubmit();

		
		// Log into the destination account and make sure the message is received
		MailItem received = MailItem.importFromSOAP(contact, "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
		
	}



}
