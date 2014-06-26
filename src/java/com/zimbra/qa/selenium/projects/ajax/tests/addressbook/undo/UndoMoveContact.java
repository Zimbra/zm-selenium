/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.addressbook.undo;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.*;

public class UndoMoveContact extends AjaxCommonTest {

	public UndoMoveContact() {
		logger.info("New " + UndoMoveContact.class.getCanonicalName());

		
		// All tests start at the Address page
		super.startingPage = app.zPageAddressbook;
		super.startingAccountPreferences = null;

	}


	@Test(
			description = "Undone moved contact", 
			groups = { "functional" })
	public void UndoMoveContact_01() throws HarnessException {
		 

		//-- Data
		
		// The contacts folder
		FolderItem contacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);

		// Create the sub addressbook
		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		String foldername = "ab"+ ZimbraSeleniumProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
					"<folder name='" + foldername +"' l='"+ root.getId() +"' view='contact'/>" +
				"</CreateFolderRequest>");
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		 // Create a contact
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());
		
	
		// -- GUI

		// Get a toaster object
		Toaster toast = app.zPageMain.zGetToaster();		

		// Refresh to get the contact into the client
		app.zPageAddressbook.zRefresh();

		// Select the contact
		app.zPageAddressbook.zListItem(Action.A_LEFTCLICK, contact.firstName);

		// Wait for the toaster (if any) to close
		toast.zWaitForClose();
		
		 // Click Move -> addressbook
        app.zPageAddressbook.zToolbarPressPulldown(Button.B_MOVE, folder);

		// Click undo from the toaster message
		toast.zWaitForActive();
		toast.zClickUndo();


		// Verify contact come back into Contacts folder
		ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact.firstName);
		ZAssert.assertNotNull(actual, "Verify the contact is not deleted from the addressbook");
		ZAssert.assertEquals(actual.getFolderId(), contacts.getId(), "Verify the contact is back in the contacts folder");

	}


}

