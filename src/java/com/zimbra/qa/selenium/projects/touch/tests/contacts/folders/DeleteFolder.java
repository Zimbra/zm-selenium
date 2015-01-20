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
package com.zimbra.qa.selenium.projects.touch.tests.contacts.contacts.Folders;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.touch.ui.PageCreateFolder;

public class DeleteFolder extends PrefGroupMailByMessageTest {

	public DeleteFolder() {
		logger.info("New "+ CreateFolder.class.getCanonicalName());
		super.startingPage = app.zPageAddressbook;
	}

	@Test(description = "Delete contact folder",
			groups = { "smoke" })
	
	public void DeleteFolder_01() throws HarnessException  {
		
		// Create a folder
		FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);

		String folderName = "ab"+ ZimbraSeleniumProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='"+ folderName + "' view='contact' l='"+ userRoot.getId() +"'/>" +
				"</CreateFolderRequest>");
		
		app.zPageAddressbook.zRefresh();
				
		// Select the folder from the list
		PageCreateFolder createFolderPage = new PageCreateFolder(app, startingPage);
		createFolderPage.zClickButton(Button.B_EDIT);
		createFolderPage.zSelectFolder(folderName);
		createFolderPage.zClickButton(Button.B_DELETE);

        //-- Verification
        
        // SOAP
     	FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), folderName);
     	ZAssert.assertEquals(actual.getParentId(), trash.getId(), "Verify folder is moved to trash");
     		
        // UI (rest of UI verification will be covered with permanent delete folder test case)
		ZAssert.assertTrue(createFolderPage.zVerifyFolderExists(folderName), "Verify deleted folder moved to trash");
	}
}