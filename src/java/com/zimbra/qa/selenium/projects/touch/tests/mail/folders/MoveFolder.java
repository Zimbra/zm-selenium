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
package com.zimbra.qa.selenium.projects.touch.tests.mail.folders;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.touch.pages.PageCreateFolder;

public class MoveFolder extends SetGroupMailByMessagePreference {

	public MoveFolder() {
		logger.info("New "+ CreateFolder.class.getCanonicalName());
	}

	@Test (description = "Move mail folder",
			groups = { "bhr" })
	
	public void MoveFolder_01() throws HarnessException  {
		
		// Create a folder
		FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		String folderName = "folder" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ folderName +"' l='"+ userRoot.getId() +"'/>" +
                "</CreateFolderRequest>");
		FolderItem pagemail = FolderItem.importFromSOAP(app.zGetActiveAccount(), folderName);
		
		// Create a second folder
		String moveFolder = "folder" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ moveFolder +"' l='"+ userRoot.getId() +"'/>" +
                "</CreateFolderRequest>");
		FolderItem pagemail1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), moveFolder);
		app.zPageMail.zRefresh();
				
		// Select the folder from the list
		PageCreateFolder createFolderPage = new PageCreateFolder(app, startingPage);
		createFolderPage.zPressButton(Button.B_EDIT);
		createFolderPage.zSelectFolder(moveFolder);
		
		// Moving folder to another folder
		createFolderPage.zPressButton(Button.B_LOCATION);
		createFolderPage.zSelectFolder(folderName);
		createFolderPage.zPressButton(Button.B_SAVE);
		
        // Verification
        
		// SOAP Verify the folder is in the other Subfolder
		FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), pagemail1.getName());
		ZAssert.assertNotNull(actual, "Verify the subfolder is again available");
		ZAssert.assertEquals(actual.getParentId(), pagemail.getId(), "Verify the subfolder's parent is now the other subfolder");
		
		//To do- UI is not refreshing in automation
		// UI Verify the folder is in the other Subfolder
		//createFolderPage.zPressButton(Button.B_SUBFOLDER_ICON);
		//ZAssert.assertTrue(createFolderPage.zVerifyFolderExists(moveFolder), "Verify folder name exist");
	}
}