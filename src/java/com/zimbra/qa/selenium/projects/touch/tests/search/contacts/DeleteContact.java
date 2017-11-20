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
package com.zimbra.qa.selenium.projects.touch.tests.search.contacts;

import java.awt.AWTException;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.TouchCore;

public class DeleteContact extends TouchCore  {
	
	public DeleteContact() {
		logger.info("New "+ DeleteContact.class.getCanonicalName());
	super.startingPage = app.zPageAddressbook;
}

	@Test (description = "Search contact using email address and then Delete it",
		groups = { "smoke" })

	public void DeleteContact_01() throws HarnessException, AWTException {
	
		// Create a contact item
		ContactItem contact = new ContactItem();
		contact.firstName = "First" + ConfigProperties.getUniqueString();
		contact.lastName = "Last" + ConfigProperties.getUniqueString();
		contact.email = "email" + ConfigProperties.getUniqueString() + "@domain.com";
	
		app.zGetActiveAccount().soapSend(
	                "<CreateContactRequest xmlns='urn:zimbraMail'>" +
	                		"<cn >" +
	                			"<a n='firstName'>" + contact.firstName +"</a>" +
	                			"<a n='lastName'>" + contact.lastName +"</a>" +
	                			"<a n='email'>" + contact.email + "</a>" +
	            			"</cn>" +
	                "</CreateContactRequest>");
		app.zPageAddressbook.zRefresh();
		
		// Search the contact
		app.zTreeContacts.zFillField(Button.B_SEARCH, contact.email);
		
		// Select the contact from contact list
		String locator = contact.lastName + ", " + contact.firstName;
		app.zPageAddressbook.zSelectContact(locator);
		
	    // Click delete button from action menu
	    app.zPageAddressbook.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_DELETE);
	    
	    // Verify the contact is removed from Contacts folder
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='contact'>"
			+		"<query>#firstname:"+ contact.firstName +"</query>"
			+	"</SearchRequest>");
	
	    String contactId = app.zGetActiveAccount().soapSelectValue("//mail:cn", "id");
	    ZAssert.assertNull(contactId, "Verify the contact is not returned in the search");
	    
	 	// UI verification    
	    ZAssert.assertEquals(app.zPageAddressbook.zVerifyContactExists(locator), false, "Verify contact is removed from Contacts folder");
	   	}
}
