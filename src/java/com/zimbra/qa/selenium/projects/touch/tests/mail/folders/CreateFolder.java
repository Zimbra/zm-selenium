/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.touch.ui.PageCreateFolder;

public class CreateFolder extends PrefGroupMailByMessageTest {

	private String folderName = null;

	public CreateFolder() {
		logger.info("New " + CreateFolder.class.getCanonicalName());
	}

	@Test (description = "Create a new folder by clicking to Edit -> 'New Folder'", 
			groups = { "sanity" })
	
	public void CreateFolder_01() throws HarnessException  {
		
		folderName = "folder" + ConfigProperties.getUniqueString();
	
		PageCreateFolder createFolderPage = (PageCreateFolder) app.zTreeMail.zPressButton(Button.B_NEW_FOLDER);
		
		createFolderPage.zEnterFolderName(folderName);
		createFolderPage.zPressButton(Button.B_SAVE);

		// Make sure the folder was created on the ZCS server
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(),folderName);
		ZAssert.assertNotNull(folder, "Verify the new folder was created");
		ZAssert.assertEquals(folder.getName(), folderName,"Verify the server and client folder names match");
		
		// UI verification
		ZAssert.assertTrue(createFolderPage.zVerifyFolderExists(folderName), "Verify created folder visible in folder pane");
	}
}