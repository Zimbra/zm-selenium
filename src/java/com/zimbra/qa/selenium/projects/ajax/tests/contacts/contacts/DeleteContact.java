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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.contacts;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class DeleteContact extends AjaxCommonTest  {
	
	public DeleteContact() {
		logger.info("New "+ DeleteContact.class.getCanonicalName());
		
		super.startingPage = app.zPageContacts;

		// Enable user preference checkboxes
		super.startingAccountPreferences = new HashMap<String , String>() {
		private static final long serialVersionUID = -8102550098554063084L;
			{
			    put("zimbraPrefShowSelectionCheckbox", "TRUE");		         
			}};
	}
		
	@Test( description = "Delete a contact item",
			groups = { "smoke", "L0"})
	
	public void ClickDeleteOnToolbar_01() throws HarnessException {

		//-- Data
		
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
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);
		
		// Select the contact
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, contact.firstName);
		
        // delete contact
        app.zPageContacts.zToolbarPressButton(Button.B_DELETE);
       
        
        //-- Verification
        
        //verify contact deleted
        ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact.firstName);
        ZAssert.assertNull(actual, "Verify the contact is deleted from the addressbook");
        
        // Verify contact in trash
        actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #firstname:"+ contact.firstName);
        ZAssert.assertNotNull(actual, "Verify the contact is in the trash");
   	}
	
	@Test( description = "Delete a contact item selected with checkbox",
			groups = { "functional", "L2" })
	
	public void DeleteContactSelectedWithCheckbox_02() throws HarnessException {

		//-- Data
		
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
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);
		
		// By default newly created contact will be checked.
		// Select the contact's checkbox
		//app.zPageContacts.zListItem(Action.A_CHECKBOX, contact.firstName);
		
        // delete contact
        app.zPageContacts.zToolbarPressButton(Button.B_DELETE);       
        
        //-- Verification
        
        //verify contact deleted
        ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact.firstName);
        ZAssert.assertNull(actual, "Verify the contact is deleted from the addressbook");        
   	}
	
	@DataProvider(name = "DataProviderDeleteKeys")
	public Object[][] DataProviderDeleteKeys() {
	  return new Object[][] {
	    new Object[] { "VK_DELETE", KeyEvent.VK_DELETE },
	    new Object[] { "VK_BACK_SPACE", KeyEvent.VK_BACK_SPACE },
	  };
	}
	
	@Test( description = "Delete a contact item using keyboard short cut Del",
			groups = { "functional", "L2"},
			dataProvider = "DataProviderDeleteKeys")
	
	public void UseShortcut_03(String name, int keyEvent) throws HarnessException {
        
		//-- Data
		
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
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);
		SleepUtil.sleepMedium();
		
		// Select the contact
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, contact.firstName);
		SleepUtil.sleepSmall();
		
		// Delete the contact
		logger.info("Typing shortcut key "+ name + " KeyEvent: "+ keyEvent);
		app.zPageMail.zKeyboardKeyEvent(keyEvent);
		SleepUtil.sleepSmall();
        
        //-- Verification
        
        //verify contact deleted
        ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact.firstName);
        ZAssert.assertNull(actual, "Verify the contact is deleted from the addressbook");    
   	}
	
	@Test( description = "Right click then click delete",
			groups = { "smoke", "L0"})
	
	public void DeleteFromContextMenu_04() throws HarnessException {		

		//-- Data	
		
		// Create a contact item
		ContactItem contact = new ContactItem();
		contact.firstName = "First" + ConfigProperties.getUniqueString();
		contact.lastName = "Last" + ConfigProperties.getUniqueString();
		contact.email = "email" + ConfigProperties.getUniqueString() + "@domain.com";
		contact.fileAs = contact.lastName + ", " + contact.firstName;

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
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);
		
		//select delete option
        app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_DELETE, contact.fileAs);       
        
        //-- Verification
        
        //verify contact deleted
        ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact.firstName);
        ZAssert.assertNull(actual, "Verify the contact is deleted from the addressbook");
   	}
	
	@Test( description = "Delete multiple contact items",
			groups = { "functional", "L2"})
	
	public void DeleteMultipleContacts_05() throws HarnessException {

		//-- Data
		
		// Create a contact items
		ContactItem contact1 = new ContactItem();
		contact1.firstName = "First" + ConfigProperties.getUniqueString();
		contact1.lastName = "Last" + ConfigProperties.getUniqueString();
		contact1.email = "email" + ConfigProperties.getUniqueString() + "@domain.com";
		contact1.fileAs = contact1.lastName + ", " + contact1.firstName;

		app.zGetActiveAccount().soapSend(
	                "<CreateContactRequest xmlns='urn:zimbraMail'>" +
	                		"<cn >" +
	                			"<a n='firstName'>" + contact1.firstName +"</a>" +
	                			"<a n='lastName'>" + contact1.lastName +"</a>" +
	                			"<a n='email'>" + contact1.email + "</a>" +
                			"</cn>" +
	                "</CreateContactRequest>");

		ContactItem contact2 = new ContactItem();
		contact2.firstName = "First" + ConfigProperties.getUniqueString();
		contact2.lastName = "Last" + ConfigProperties.getUniqueString();
		contact2.email = "email" + ConfigProperties.getUniqueString() + "@domain.com";
		contact2.fileAs = contact2.lastName + ", " + contact2.firstName;

		app.zGetActiveAccount().soapSend(
	                "<CreateContactRequest xmlns='urn:zimbraMail'>" +
	                		"<cn >" +
	                			"<a n='firstName'>" + contact2.firstName +"</a>" +
	                			"<a n='lastName'>" + contact2.lastName +"</a>" +
	                			"<a n='email'>" + contact2.email + "</a>" +
                			"</cn>" +
	                "</CreateContactRequest>");

		ContactItem contact3 = new ContactItem();
		contact3.firstName = "First" + ConfigProperties.getUniqueString();
		contact3.lastName = "Last" + ConfigProperties.getUniqueString();
		contact3.email = "email" + ConfigProperties.getUniqueString() + "@domain.com";
		contact3.fileAs = contact3.lastName + ", " + contact3.firstName;

		app.zGetActiveAccount().soapSend(
	                "<CreateContactRequest xmlns='urn:zimbraMail'>" +
	                		"<cn >" +
	                			"<a n='firstName'>" + contact3.firstName +"</a>" +
	                			"<a n='lastName'>" + contact3.lastName +"</a>" +
	                			"<a n='email'>" + contact3.email + "</a>" +
                			"</cn>" +
	                "</CreateContactRequest>");

		//-- GUI
		
		// Refresh to get the contact into the client
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);
		
	    // Select the item
	    app.zPageContacts.zListItem(Action.A_CHECKBOX, contact1.fileAs);
	    app.zPageContacts.zListItem(Action.A_CHECKBOX, contact2.fileAs);
	   // app.zPageContacts.zListItem(Action.A_CHECKBOX, contact3.fileAs);
	   
		//delete 3 contacts
        app.zPageContacts.zToolbarPressButton(Button.B_DELETE);       
        
        //-- Verification
        
        //verify contacts deleted
        ContactItem actual1 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact1.firstName);
        ZAssert.assertNull(actual1, "Verify the contact is deleted from the addressbook");
        
        ContactItem actual2 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact1.firstName);
        ZAssert.assertNull(actual2, "Verify the contact is deleted from the addressbook");
        
        ContactItem actual3 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact1.firstName);
        ZAssert.assertNull(actual3, "Verify the contact is deleted from the addressbook");
   	}
}