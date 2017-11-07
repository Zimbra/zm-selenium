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

public class RenameFolder extends PrefGroupMailByMessageTest {

	public RenameFolder() {
		logger.info("New "+ CreateFolder.class.getCanonicalName());
		super.startingPage = app.zPageAddressbook;
	}

	@Test (description = "Rename contact folder",
			groups = { "smoke" })
	
	public void RenameFolder_01() throws HarnessException  {
		
		// Create a folder
		FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);

		String folderName = "ab"+ ConfigProperties.getUniqueString();
		String renamedfolder = "ab"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='"+ folderName + "' view='contact' l='"+ userRoot.getId() +"'/>" +
				"</CreateFolderRequest>");
		
		app.zPageAddressbook.zRefresh();
				
		// Selecting an existing folder
		PageCreateFolder createFolderPage = new PageCreateFolder(app, startingPage);
		createFolderPage.zClickButton(Button.B_EDIT);
		createFolderPage.zSelectFolder(folderName);
		
		// Renaming an existing folder
		createFolderPage.zEnterFolderName(renamedfolder);
		createFolderPage.zClickButton(Button.B_SAVE);

        //-- Verification
              	
		// SOAP
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(),folderName);
		ZAssert.assertNull(actual, "Verify the old folder name no longer exists");
		
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(),renamedfolder);
		ZAssert.assertNotNull(folder, "Verify the renamed folder was created");
		ZAssert.assertEquals(folder.getName(), renamedfolder,"Verify the renamed folder name exists");
		
		// UI
		ZAssert.assertTrue(createFolderPage.zVerifyFolderExists(renamedfolder), "Verify renamed folder visible in folder pane");
	}	
}