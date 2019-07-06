/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014, 2016 Synacor, Inc.
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

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.items.TagItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.TouchCore;
import com.zimbra.qa.selenium.projects.touch.pages.contacts.TagContactView;

public class TagContact extends TouchCore  {
		
	public TagContact() {
		logger.info("New "+ TagContact.class.getCanonicalName());
		super.startingPage = app.zPageAddressbook;			
	}
	
	@Test (description = "Add Tag to a contact item",
			groups = { "bhr" })
	
	public void TagContact_01() throws HarnessException {

		//-- Data
		
		// Create a tag
		TagItem tagItem = TagItem.CreateUsingSoap(app.zGetActiveAccount());	
		
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

		//-- GUI
		
		// Refresh to get the contact into the client
		app.zPageAddressbook.zRefresh();

		// Select the contact from contact list
		String locator = contact.lastName + ", " + contact.firstName;
		app.zPageAddressbook.zSelectContact(locator);
		
        // Choose tag action from action menu
        TagContactView tcv = (TagContactView)app.zPageAddressbook.zToolbarPressPulldown(Button.B_ACTIONS,Button.B_TAG);
        
        // Choose tag which is assigned to the contact 
        tcv.zTreeItem(Action.A_LEFTCLICK, tagItem);
		
        
        // Verification
        
        // Verify contact with the tag
        ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "tag:"+tagItem.getName()+" AND #firstname:"+ contact.firstName);
        ZAssert.assertNotNull(actual, "Verify the contact is with the tag");
   	}
}