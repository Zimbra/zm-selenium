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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.folders;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;




public class ContextMenuAddressbooks extends AjaxCommonTest {

	public ContextMenuAddressbooks() {
		logger.info("New "+ ContextMenuAddressbooks.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageContacts;
		
		// Enable user preference checkboxes
		super.startingAccountPreferences = null;
		
	}

	
	
	

				
	@Test(
			description = "Cannot delete an addressbook system folder- Right click, Delete",
			groups = { "functional" },
			dataProvider = "DataProviderSystemFolders"
			)
	public void SystemFoldersDeleteButtonDisabledFromContextmenu(String name, SystemFolder systemFolder) throws HarnessException {
		
		FolderItem folder= FolderItem.importFromSOAP(app.zGetActiveAccount(), systemFolder);
		ZAssert.assertNotNull(folder, "Verify can get the folder: "+ name);	

		// Right click on Folder 
		app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, folder);
		
		
		// Get the context menu
		String divLocator = "css=div#ZmActionMenu_contacts_ADDRBOOK";
		ZAssert.assertTrue(app.zTreeContacts.zIsVisiblePerPosition(divLocator, 0, 0), "Verify the context menu is visible");
		
		
		// Determine if the Edit option is enabled
		String editLocator = divLocator + " div#DELETE_WITHOUT_SHORTCUT.ZDisabled";
		ZAssert.assertTrue(app.zTreeContacts.sIsElementPresent(editLocator), "Verify the Delete Folder option is disabled");

	}	

	// These folders can change color or share only (i.e. has an edit dialog)
	@DataProvider(name = "DataProviderSystemFolders")
	public Object[][] DataProviderSystemFolders() {
	  return new Object[][] {
	    new Object[] { "Contacts", SystemFolder.Contacts },
	    new Object[] { "Emailed Contacts", SystemFolder.EmailedContacts },
//	    new Object[] { "Distribution Lists", SystemFolder.DistributionLists },
	    new Object[] { "Trash", SystemFolder.Trash },
	  };
	}
	
	@Test(
			description = "Verify 'Rename folder' dialog is not present from right click context menu",
			groups = { "functional" },
			dataProvider = "DataProviderSystemFolders"
			)
	public void CannotRenameSystemFolders(String name, SystemFolder systemFolder) throws HarnessException {

		FolderItem folder= FolderItem.importFromSOAP(app.zGetActiveAccount(), systemFolder);
		ZAssert.assertNotNull(folder, "Verify can get the folder: "+ name);	

		// Right click on Folder 
		app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, folder);
		
		
		// Get the context menu
		String divLocator = "css=div#ZmActionMenu_contacts_ADDRBOOK";
		ZAssert.assertTrue(app.zTreeContacts.zIsVisiblePerPosition(divLocator, 0, 0), "Verify the context menu is visible");
		
		
		// Determine if the Edit option is enabled
		String editLocator = divLocator + " div#RENAME_FOLDER.ZDisabled";
		ZAssert.assertTrue(app.zTreeContacts.sIsElementPresent(editLocator), "Verify the Rename Folder option is disabled");

		
	}	



}
