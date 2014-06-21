/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.addressbook.contactgroups;


import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;





public class DragAndDropContactGroup extends AjaxCommonTest  {
	public DragAndDropContactGroup() {
		logger.info("New "+ DragAndDropContactGroup.class.getCanonicalName());
		
		// All tests start at the Address page
		super.startingPage = app.zPageAddressbook;

		// Enable user preference checkboxes
		super.startingAccountPreferences = new HashMap<String , String>() {
			private static final long serialVersionUID = 8205837641007378158L;

		{
		    	put("zimbraPrefShowSelectionCheckbox", "TRUE");		         
		   }};				
		
	}
	
	@Test(	description = "Move a contact group to folder Emailed Contacts by drag and drop",
			groups = { "functional" })
	public void DnDToEmailedContacts() throws HarnessException {
		
		//-- Data
		
		// Create the sub addressbook
		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		String foldername = "ab"+ ZimbraSeleniumProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
					"<folder name='" + foldername +"' l='"+ root.getId() +"' view='contact'/>" +
				"</CreateFolderRequest>");
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
	
	
		//-- GUI
		
		// Refresh
		app.zPageAddressbook.zRefresh();
		
		
    
		app.zPageAddressbook.zDragAndDrop(
				"css=div#zlif__CNS-main__" + group.getId() + "__fileas:contains("+ group.getName() + ")",
				"css=td#zti__main_Contacts__" + folder.getId() + "_textCell:contains("+ folder.getName() + ")");
			
	  
        //-- Verification
        
        ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #nickname:"+ group.getName());
        ZAssert.assertNotNull(actual, "Verify the contact group exists");
        
        // Verify the contact group is in the trash
        ZAssert.assertEquals(actual.getFolderId(), folder.getId(), "Verify the contact group is in the sub addressbook");
        
           
    
   	}
	

	@Test(	description = "Move a contact group to trash folder by drag and drop",
			groups = { "functional" })
	public void DnDToTrash() throws HarnessException {

		//--  Data
		
		// The trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		
		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		
		
		
		//-- GUI
		
		// Refresh
		app.zPageAddressbook.zRefresh();
		
		app.zPageAddressbook.zDragAndDrop(
				"css=div#zlif__CNS-main__" + group.getId() + "__fileas:contains("+ group.getName() + ")",
				"css=td#zti__main_Contacts__" + trash.getId() + "_textCell:contains("+ trash.getName() + ")");

        
        //-- Verification
        
        ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #nickname:"+ group.getName());
        ZAssert.assertNotNull(actual, "Verify the contact group exists");

        // Verify the contact group is in the trash
        ZAssert.assertEquals(actual.getFolderId(), trash.getId(), "Verify the contact group is in the trash");
        

   	}

}
