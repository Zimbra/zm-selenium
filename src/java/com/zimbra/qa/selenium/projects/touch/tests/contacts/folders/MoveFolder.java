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
package com.zimbra.qa.selenium.projects.touch.tests.contacts.folders;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.touch.ui.PageCreateFolder;

public class MoveFolder extends PrefGroupMailByMessageTest {

	public MoveFolder() {
		logger.info("New "+ CreateFolder.class.getCanonicalName());
		super.startingPage = app.zPageAddressbook;
	}

	@Test( description = "Move and existing contact folder",
			groups = { "smoke" })
	
	public void MoveFolder_01() throws HarnessException  {
		
		// Create a folder
		FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		
		String folderName = "ab"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='"+ folderName + "' view='contact' l='"+ userRoot.getId() +"'/>" +
				"</CreateFolderRequest>");
		FolderItem addressbook = FolderItem.importFromSOAP(app.zGetActiveAccount(), folderName);
		
		// Create a second folder
		String folderName1 = "ab"+ ConfigProperties.getUniqueString();
		app.zPageAddressbook.zRefresh();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='"+ folderName1 + "' view='contact' l='"+ userRoot.getId() +"'/>" +
				"</CreateFolderRequest>");
		FolderItem addressbook1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), folderName1);
		app.zPageAddressbook.zRefresh();
				
		// Select the folder from the list
		PageCreateFolder createFolderPage = new PageCreateFolder(app, startingPage);
		createFolderPage.zClickButton(Button.B_EDIT);
		createFolderPage.zSelectFolder(folderName1);
		
		// Moving folder to another folder
		createFolderPage.zClickButton(Button.B_LOCATION);
		createFolderPage.zSelectFolder(folderName);
		createFolderPage.zClickButton(Button.B_SAVE);
		
        // Verification
		
		// SOAP
		// Verify the folder is in the other Subfolder
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), addressbook1.getName());
		ZAssert.assertNotNull(actual, "Verify the subfolder is again available");
		ZAssert.assertEquals(actual.getParentId(), addressbook.getId(), "Verify the subfolder's parent is now the other subfolder");

        // UI Verify the folder is in the other Subfolder
		createFolderPage.zClickButton(Button.B_SUBFOLDER_ICON);
		ZAssert.assertTrue(createFolderPage.zVerifyFolderExists(folderName1), "Verify folder name exist");
	}	
}