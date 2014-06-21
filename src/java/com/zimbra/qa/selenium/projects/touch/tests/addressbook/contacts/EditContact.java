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
package com.zimbra.qa.selenium.projects.touch.tests.addressbook.contacts;


import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.addressbook.*;

import com.zimbra.qa.selenium.projects.touch.core.TouchCommonTest;
import com.zimbra.qa.selenium.projects.touch.ui.addressbook.FormContactNew;
import com.zimbra.qa.selenium.projects.touch.ui.addressbook.FormContactNew.Field;



public class EditContact extends TouchCommonTest  {
	public EditContact() {
		logger.info("New "+ EditContact.class.getCanonicalName());
		
		// All tests start at the Address page
		super.startingPage =  app.zPageAddressbook;
		
	}
	
	@Test(	description = "Edit a contact item and save it",
			groups = { "sanity" })
	public void EditContact() throws HarnessException {
		
		//-- Data
	
		// create a contact
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());
		
		// new first name
		String firstname = "new" + ZimbraSeleniumProperties.getUniqueString();
		
		
		
		//-- GUI
		
		// refresh to get the contact into the client
		app.zPageAddressbook.zRefresh();
		SleepUtil.sleepSmall();
		
		// select the contact
		String nameInList = contact.lastName + ", " + contact.firstName;
		String locator = "css=div[id^='ext-contactslistview'] div[class='zcs-contactList-name']:contains('"+nameInList+"')";
		app.zPageAddressbook.zClick(locator);
				
		// click "Edit" from the toolbar
        FormContactNew form = (FormContactNew) app.zPageAddressbook.zToolbarPressButton(Button.B_EDIT);
        
		// change the first name and click "Save"
        form.zFillField(Field.FirstName, firstname);
        form.zToolbarPressButton(Button.B_SAVE);
        
         
        //-- Verification
        
        // get the contact data stored on server with soap
        ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ firstname);
        // make sure the data stored and first name of the data is edited one.
        ZAssert.assertNotNull(actual, "Verify the contact is found");
        ZAssert.assertEquals(actual.firstName, firstname, "Verify the new first name is saved");
        
	}


	
}

