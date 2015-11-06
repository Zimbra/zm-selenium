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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.folders;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;


public class MoveFolder extends AjaxCommonTest {

	public MoveFolder() {
		logger.info("New "+ MoveFolder.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageContacts;
		super.startingAccountPreferences = null;
		
	}
	
	@Test(	description = "Drag one folder from top level and Drop into sub folder", groups = { "smoke" })
	public void DnDFromTopLevelToSubFolder() throws HarnessException {

		//-- Data
		
		// Root folder
		FolderItem userRoot= FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		
		// Subfolders in root
		String name1 = "ab"+ ZimbraSeleniumProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='"+ name1 + "' view='contact' l='"+ userRoot.getId() +"'/>" +
				"</CreateFolderRequest>");
		FolderItem addressbook1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name1);

		String name2 = "ab"+ ZimbraSeleniumProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='"+ name2 + "' view='contact' l='"+ userRoot.getId() +"'/>" +
				"</CreateFolderRequest>");
		FolderItem addressbook2 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name2);


		//-- GUI
		
		// Refresh to get new addressbooks
		app.zPageContacts.zRefresh();
	

		
		app.zPageContacts.zDragAndDrop(
				"css=div#zov__main_Contacts td#zti__main_Contacts__" + addressbook1.getId() + "_textCell:contains("+ addressbook1.getName() + ")",
				"css=div#zov__main_Contacts td#zti__main_Contacts__" + addressbook2.getId() + "_textCell:contains("+ addressbook2.getName() + ")");
			

		
		//-- Verification
		
		// Verify the folder is now in the other subfolder
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), addressbook1.getName());
		ZAssert.assertNotNull(actual, "Verify the subfolder is again available");
		ZAssert.assertEquals(actual.getParentId(), addressbook2.getId(), "Verify the subfolder's parent is now the other subfolder");


	}




}
