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

public class PermanentDeleteContact extends TouchCommonTest  {
	
	public PermanentDeleteContact() {
		logger.info("New "+ DeleteContact.class.getCanonicalName());
		super.startingPage = app.zPageAddressbook;
	}
	
	@Test(	description = "Permanently delete a contact item",
			groups = { "functional" })
	
	public void PermanentDeleteContact_01() throws HarnessException {

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
		
		// Select the contact from contact list
		String locator = contact.lastName + ", " + contact.firstName;
		app.zPageAddressbook.zSelectContact(locator);
		
        // Click delete button from action menu
        app.zPageAddressbook.zToolbarPressPulldown(Button.B_ACTIONS,Button.B_DELETE);
        
     	// Click on trash folder
     	app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, "Trash");
		
     	// Select the contact from contact list
     	app.zPageAddressbook.zSelectContact(locator);
		
		// Click delete button from action menu
        app.zPageAddressbook.zToolbarPressPulldown(Button.B_ACTIONS,Button.B_DELETE);
        
        // Click on Yes
      	app.zPageMail.zClickButton(Button.B_YES);
      			
        // Verify contact is removed from trash
		ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #firstname:"+ contact.firstName);
        ZAssert.assertNull(actual, "Verify the contact is in the trash");
        
        //GUI verification    
		ZAssert.assertEquals(app.zPageAddressbook.zVerifyContactExists(locator), false, "Verify contact is removed from Trash folder");
   	}
}
