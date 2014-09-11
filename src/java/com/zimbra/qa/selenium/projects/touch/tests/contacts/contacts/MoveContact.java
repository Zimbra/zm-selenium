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
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.TouchCommonTest;
import com.zimbra.qa.selenium.projects.touch.ui.contacts.MoveContactView;

public class MoveContact extends TouchCommonTest  {
	
	public MoveContact() {
		logger.info("New "+ MoveContact.class.getCanonicalName());
		super.startingPage = app.zPageAddressbook;
	}
	
	@Test(	description = "Move a contact item to EmailedContacts Address Book",
			groups = { "sanity" })
	
	public void MoveContact_01() throws HarnessException {

		//-- Data
		
		// fetch info of EmailedContacts AddressBook
		FolderItem emailedcontacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.EmailedContacts);
		
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
		
		// Select the contact from Contacts AddressBook
		String nameInList = contact.lastName + ", " + contact.firstName;
		String locator = "css=div[id^='ext-contactslistview'] div[class='zcs-contactList-name']:contains('"+nameInList+"')";
		app.zPageAddressbook.zClick(locator);
		
        // Choose move button from action menu
		MoveContactView mcv = (MoveContactView)app.zPageAddressbook.zToolbarPressPulldown(Button.B_ACTIONS,Button.B_MOVE);
        
		// Choose EmailedContact as target AddressBook which you move the contact to
        mcv.zTreeItem(Action.A_LEFTCLICK, emailedcontacts);
		
        //-- Verification
        
        // Verify contact moved from Contacts AddressBook
        ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "in:contacts AND #firstname:"+ contact.firstName);
        ZAssert.assertNull(actual, "Verify the contact is deleted from the addressbook");
        
        // Verify the contact in EmailedContacts AddressBook 
        actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "in:\"Emailed Contacts\" AND #firstname:"+ contact.firstName);
        ZAssert.assertNotNull(actual, "Verify the contact is in the trash");
        
   	}	
	
}
