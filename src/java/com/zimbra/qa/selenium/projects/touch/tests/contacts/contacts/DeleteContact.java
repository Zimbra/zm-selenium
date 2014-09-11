/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.touch.tests.contacts.contacts;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.TouchCommonTest;

public class DeleteContact extends TouchCommonTest  {
	
	public DeleteContact() {
		logger.info("New "+ DeleteContact.class.getCanonicalName());
		super.startingPage = app.zPageAddressbook;
	}
	
	@Test(	description = "Delete a contact item",
			groups = { "sanity" })
	
	public void DeleteContact_01() throws HarnessException {

		//-- Data
		
		// Create a contact item
		ContactItem contact = new ContactItem();
		contact.firstName = "First" + ZimbraSeleniumProperties.getUniqueString();
		contact.lastName = "Last" + ZimbraSeleniumProperties.getUniqueString();
		contact.email = "email" + ZimbraSeleniumProperties.getUniqueString() + "@domain.com";

		app.zGetActiveAccount().soapSend(
	                "<CreateContactRequest xmlns='urn:zimbraMail'>" +
	                		"<cn >" +
	                			"<a n='firstName'>" + contact.firstName +"</a>" +
	                			"<a n='lastName'>" + contact.lastName +"</a>" +
	                			"<a n='email'>" + contact.email + "</a>" +
                			"</cn>" +
	                "</CreateContactRequest>");

		//-- GUI
		
		// Refresh to get the contact into the client
		app.zPageAddressbook.zRefresh();
		SleepUtil.sleepSmall();
		
		// Select the contact from contact list
		String locator = contact.lastName + ", " + contact.firstName;
		app.zPageAddressbook.zClick(app.zPageAddressbook.zGetContactLocator(locator));
		
        // Click delete button from action menu
        app.zPageAddressbook.zToolbarPressPulldown(Button.B_ACTIONS,Button.B_DELETE);
        
        //-- Verification
        
        // Verify contact deleted
        ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact.firstName);
        ZAssert.assertNull(actual, "Verify the contact is deleted from the addressbook");
        
        // Verify contact in trash
        actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #firstname:"+ contact.firstName);
        ZAssert.assertNotNull(actual, "Verify the contact is in the trash");
        
   	}
}
