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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.contacts;

import java.util.HashMap;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class MoveContactFromTrashToLocalFolder extends AjaxCommonTest  {
			
	public MoveContactFromTrashToLocalFolder() {
		logger.info("New "+ MoveContactFromTrashToLocalFolder.class.getCanonicalName());
		
		super.startingPage = app.zPageContacts;
		super.startingAccountPreferences = new HashMap<String , String>() {
		private static final long serialVersionUID = -8102550098554063084L;
		{
		    put("zimbraPrefShowSelectionCheckbox", "TRUE");		         
		}};					
	}
	
	@Test( description = "Bug 102685 - Delete a contact item then moved it back to contact folder from trash",	
			groups = { "functional", "L2"})
	
	public void MoveContactFromTrashToLocalFolder_01() throws HarnessException {

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

		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);
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
        
       // Go to trash folder 
        FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
        app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, trash);
        
        // Select and move the contact 
        app.zPageContacts.zListItem(Action.A_LEFTCLICK, contact.lastName);        
        app.zPageContacts.zToolbarPressPulldown(Button.B_MOVE, folder);
   
        // Verification
        
        ContactItem movedcontact = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact.firstName); 
        ZAssert.assertNotNull(movedcontact, "Verify the contact moved from the trash");
        app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, folder);   
        
        // Go to system contact folder 
        FolderItem foldercontacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);
        app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, foldercontacts);
        
        // Verify that the contact  moved to system contact folder
        ContactItem movedcontactsystem = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact.firstName); 
        ZAssert.assertNotNull(movedcontactsystem, "Verify the contact is moved system contact folder");            
   	}
	
}
