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
import com.zimbra.qa.selenium.projects.touch.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.touch.pages.PageCreateFolder;

public class DeleteFolder extends SetGroupMailByMessagePreference {

	public DeleteFolder() {
		logger.info("New "+ CreateFolder.class.getCanonicalName());
		super.startingPage = app.zPageAddressbook;
	}

	@Test (description = "Delete contact folder",
			groups = { "smoke" })
	
	public void DeleteFolder_01() throws HarnessException  {
		
		// Create a folder
		FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);

		String folderName = "ab"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='"+ folderName + "' view='contact' l='"+ userRoot.getId() +"'/>" +
				"</CreateFolderRequest>");
		
		app.zPageAddressbook.zRefresh();
				
		// Select the folder from the list
		PageCreateFolder createFolderPage = new PageCreateFolder(app, startingPage);
		createFolderPage.zPressButton(Button.B_EDIT);
		createFolderPage.zSelectFolder(folderName);
		createFolderPage.zPressButton(Button.B_DELETE);

        //-- Verification
        
        // SOAP
     	FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), folderName);
     	ZAssert.assertEquals(actual.getParentId(), trash.getId(), "Verify folder is moved to trash");
     		
        // UI (rest of UI verification will be covered with permanent delete folder test case)
		ZAssert.assertTrue(createFolderPage.zVerifyFolderExists(folderName), "Verify deleted folder moved to trash");
	}
}