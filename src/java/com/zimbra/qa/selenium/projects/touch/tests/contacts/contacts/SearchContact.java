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

package com.zimbra.qa.selenium.projects.touch.tests.contacts.contacts;


import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.TouchCommonTest;

public class SearchContact extends TouchCommonTest  {
	
	public SearchContact() {
		logger.info("New "+ CreateContact.class.getCanonicalName());
		super.startingPage = app.zPageAddressbook;
	}

	@Test( description = "Search contact item with first name",
			groups = { "smoke" })

	public void CreateContact_01() throws HarnessException{

		//-- DATA
		
		// Create a contact item
		ContactItem contact = new ContactItem();
		contact.firstName = "First" + ConfigProperties.getUniqueString();
		contact.lastName = "Last" + ConfigProperties.getUniqueString();
		contact.email = "email" + ConfigProperties.getUniqueString() + "@domain.com";
		String locator = contact.lastName + ", " + contact.firstName;

		app.zGetActiveAccount().soapSend(
	                "<CreateContactRequest xmlns='urn:zimbraMail'>" +
	                		"<cn >" +
	                			"<a n='firstName'>" + contact.firstName +"</a>" +
	                			"<a n='lastName'>" + contact.lastName +"</a>" +
	                			"<a n='email'>" + contact.email + "</a>" +
                			"</cn>" +
	                "</CreateContactRequest>");

		//-- Data Verification
		
		// Refresh to get the contact into the client
		app.zPageAddressbook.zRefresh();
		
		// Enter first name & search contact
		app.zPageAddressbook.zSearchContact(Button.B_SEARCH, contact.firstName);
		
		//GUI verification    
		ZAssert.assertEquals(app.zPageAddressbook.zVerifyContactExists(locator), true, "Verify contact is removed from Trash folder");
		
	}

	@Test( description = "Search contact item with last name",
			groups = { "functional" })

	public void CreateContact_02() throws HarnessException {

		//-- DATA
		
		// Create a contact item
		ContactItem contact = new ContactItem();
		contact.firstName = "First" + ConfigProperties.getUniqueString();
		contact.lastName = "Last" + ConfigProperties.getUniqueString();
		contact.email = "email" + ConfigProperties.getUniqueString() + "@domain.com";
		String locator = contact.lastName + ", " + contact.firstName;

		app.zGetActiveAccount().soapSend(
	                "<CreateContactRequest xmlns='urn:zimbraMail'>" +
	                		"<cn >" +
	                			"<a n='firstName'>" + contact.firstName +"</a>" +
	                			"<a n='lastName'>" + contact.lastName +"</a>" +
	                			"<a n='email'>" + contact.email + "</a>" +
                			"</cn>" +
	                "</CreateContactRequest>");

		//-- Data Verification
		
		// Refresh to get the contact into the client
		app.zPageAddressbook.zRefresh();
		
		// Enter last name & search contact
		app.zPageAddressbook.zSearchContact(Button.B_SEARCH, contact.lastName);
		
		//GUI verification    
		ZAssert.assertEquals(app.zPageAddressbook.zVerifyContactExists(locator), true, "Verify contact is removed from Trash folder");
		
	}
	
	@Test( description = "Search contact item with email",
			groups = { "functional" })

	public void CreateContact_03() throws HarnessException {

		//-- DATA
		
		// Create a contact item
		ContactItem contact = new ContactItem();
		contact.firstName = "First" + ConfigProperties.getUniqueString();
		contact.lastName = "Last" + ConfigProperties.getUniqueString();
		contact.email = "email" + ConfigProperties.getUniqueString() + "@domain.com";
		String locator = contact.lastName + ", " + contact.firstName;

		app.zGetActiveAccount().soapSend(
	                "<CreateContactRequest xmlns='urn:zimbraMail'>" +
	                		"<cn >" +
	                			"<a n='firstName'>" + contact.firstName +"</a>" +
	                			"<a n='lastName'>" + contact.lastName +"</a>" +
	                			"<a n='email'>" + contact.email + "</a>" +
                			"</cn>" +
	                "</CreateContactRequest>");
		
		// Refresh to get the contact into the client
		app.zPageAddressbook.zRefresh();
		
		// Search contact with email
		app.zPageAddressbook.zSearchContact(Button.B_SEARCH, contact.email);
		
		//GUI verification    
		ZAssert.assertEquals(app.zPageAddressbook.zVerifyContactExists(locator), true, "Verify contact is removed from Trash folder");
		
	}

}