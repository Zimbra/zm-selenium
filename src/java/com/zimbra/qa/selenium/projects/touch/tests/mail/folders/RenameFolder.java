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
package com.zimbra.qa.selenium.projects.touch.tests.mail.folders;

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
	}

	@Test(description = "Rename mail folder",
			groups = { "smoke" })
	
	public void RenameFolder_01() throws HarnessException  {
		
		// Create a folder
		FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		String folderName = "folder" + ZimbraSeleniumProperties.getUniqueString();
		String renameFolder = "folder" + ZimbraSeleniumProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ folderName +"' l='"+ userRoot.getId() +"'/>" +
                "</CreateFolderRequest>");
		app.zPageMail.zRefresh();
				
		// Select the folder from the list and Rename
		PageCreateFolder createFolderPage = new PageCreateFolder(app, startingPage);
		createFolderPage.zClickButton(Button.B_EDIT);
		createFolderPage.zSelectFolder(folderName);
		createFolderPage.zEnterFolderName(renameFolder);
		createFolderPage.zClickButton(Button.B_SAVE);

        //-- Verification
        
        // SOAP
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(),renameFolder);
		ZAssert.assertNotNull(folder, "Verify the renamed folder");
		ZAssert.assertEquals(folder.getName(), renameFolder,"Verify the server and client folder names match");
		
		// UI verification
		ZAssert.assertTrue(createFolderPage.zVerifyFolderExists(renameFolder), "Verify renmaed folder visible in folder pane");
	}
}