/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.folders;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.ZimbraCharsets.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.*;

public class CreateFolder extends PrefGroupMailByMessageTest {

	private String _folderName = null;

	public CreateFolder() {
		logger.info("New " + CreateFolder.class.getCanonicalName());
	}


	@Test( description = "Create a new folder by clicking 'new folder' on folder tree", 
			groups = { "sanity" })
	
	public void CreateFolder_01() throws HarnessException {
		
		_folderName = "folder" + ConfigProperties.getUniqueString();
		
		DialogCreateFolder createFolderDialog = (DialogCreateFolder) app.zTreeMail.zPressButton(Button.B_TREE_NEWFOLDER);
		
		createFolderDialog.zEnterFolderName(_folderName);
		createFolderDialog.zClickButton(Button.B_OK);
		
		SleepUtil.sleepMedium();
		//Explicitly click on Inbox folder due to bug 100506
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox));


		// Make sure the folder was created on the ZCS server
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(),_folderName);
		ZAssert.assertNotNull(folder, "Verify the new folder was created");
		ZAssert.assertEquals(folder.getName(), _folderName,"Verify the server and client folder names match");
		
	}

	
	@Test( description = "Create a new folder using keyboard shortcuts", groups = { "functional" })
	
	public void CreateFolder_02() throws HarnessException {

		Shortcut shortcut = Shortcut.S_NEWFOLDER;

		// Set the new folder name
		String name = "folder" + ConfigProperties.getUniqueString();

		DialogCreateFolder dialog = (DialogCreateFolder) app.zPageMail.zKeyboardShortcut(shortcut);
		ZAssert.assertNotNull(dialog, "Verify the new dialog opened");

		// Fill out the form with the basic details
		// TODO: does a folder in the tree need to be selected?
		dialog.zEnterFolderName(name);
		dialog.zClickButton(Button.B_OK);
		
		SleepUtil.sleepMedium();
		//Explicitly click on Inbox folder due to bug 100506
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox));

		// Make sure the folder was created on the server
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(),name);
		ZAssert.assertNotNull(folder, "Verify the new folder was created");
		ZAssert.assertEquals(folder.getName(), name, "Verify the server and client folder names match");
		
	}

	
	@Test( description = "Create a new folder using context menu from root folder", groups = { "functional" })
	
	public void CreateFolder_03() throws HarnessException {
		
		_folderName = "folder" + ConfigProperties.getUniqueString();

		// get the root folder to create a subfolder in
		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		
		// Right click on the "Folders" header
		DialogCreateFolder createFolderDialog = (DialogCreateFolder) app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.O_NEW_FOLDER, root);

		createFolderDialog.zEnterFolderName(_folderName);
		createFolderDialog.zClickButton(Button.B_OK);
		
		SleepUtil.sleepMedium();
		//Explicitly click on Inbox folder due to bug 100506
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox));

		// Make sure the folder was created on the ZCS server
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), _folderName);
		ZAssert.assertNotNull(folder, "Verify the new form opened");
		ZAssert.assertEquals(folder.getName(), _folderName, "Verify the server and client folder names match");
		
	}

	
	@Test( description = "Create a new folder using mail app New -> New Folder",	groups = { "functional" })
	
	public void CreateFolder_04() throws HarnessException {

		// Set the new folder name
		String name = "folder" + ConfigProperties.getUniqueString();

		// Create a new folder in the inbox
		DialogCreateFolder dialog = (DialogCreateFolder) app.zPageMail.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_FOLDER);
		ZAssert.assertNotNull(dialog, "Verify the new dialog opened");

		dialog.zEnterFolderName(name);
		dialog.zClickButton(Button.B_OK);
		
		SleepUtil.sleepMedium();
		//Explicitly click on Inbox folder due to bug 100506
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox));

		// Make sure the folder was created on the server
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(),name);
		ZAssert.assertNotNull(folder, "Verify the new folder was created");

		ZAssert.assertEquals(folder.getName(), name, "Verify the server and client folder names match");

	}

	
	@Test( description = "Create a folder with non-ASCII special characters", groups = { "functional" }, dataProvider = "DataProviderSupportedCharsets")
	
	public void CreateFolder_05(ZCharset charset, String foldername) throws HarnessException {


		DialogCreateFolder dialog = (DialogCreateFolder) app.zPageMail.zKeyboardShortcut(Shortcut.S_NEWFOLDER);
		ZAssert.assertNotNull(dialog, "Verify the new dialog opened");

		// Fill out the form with the basic details
		dialog.zEnterFolderName(foldername);
		dialog.zClickButton(Button.B_OK);
		SleepUtil.sleepMedium();
		//Explicitly click on Inbox folder due to bug 100506
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox));

		// Make sure the folder was created on the server
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(),foldername);
		ZAssert.assertNotNull(folder, "Verify the new folder was created");
		ZAssert.assertEquals(folder.getName(), foldername, "Verify the server and client folder names match for charset "+ charset);

	}
}