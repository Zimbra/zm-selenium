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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.assistant;

import java.util.List;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogAssistant;


public class CreateContact extends AjaxCommonTest {
	
	public CreateContact() {
		logger.info("New "+ CreateContact.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageAddressbook;

		// Make sure we are using an account with message view
		super.startingAccountPreferences = null;


	}
	
	@Test(	description = "Create a new conntact using the Zimbra Assistant",
			groups = { "deprecated" })
	public void CreateContact_01() throws HarnessException {
		
		// Create a contact item
		ContactItem contactItem = ContactItem.createContactItem(app.zGetActiveAccount());
	
		String command = "contact " + contactItem.firstName + " " + contactItem.lastName + " " + contactItem.email;

	
		DialogAssistant assistant = (DialogAssistant)app.zPageAddressbook.zKeyboardShortcut(Shortcut.S_ASSISTANT);
		assistant.zEnterCommand(command);
		assistant.zClickButton(Button.B_OK);
		
	  
	    // verify contact created
		List<ContactItem> contacts = app.zPageAddressbook.zListGetContacts(); 
 	           
        boolean isContactItemDisplayed=false;
	    for (ContactItem ci : contacts) {
		    if (ci.fileAs.toLowerCase().contains(contactItem.firstName)) 
			   {
              isContactItemDisplayed=true;
              break;
	  	    }
	      }
			
        ZAssert.assertTrue(isContactItemDisplayed, "Verify contact fileAs (" + contactItem.firstName + " displayed");
        

	}


}
