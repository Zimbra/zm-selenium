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
package com.zimbra.qa.selenium.projects.ajax.tests.tasks.folders;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

import com.zimbra.qa.selenium.projects.ajax.ui.DialogError;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogRenameFolder;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogError.DialogErrorID;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DialogEditFolder;

public class RenameTaskFolder extends AjaxCommonTest {
	@SuppressWarnings("serial")
	public RenameTaskFolder() {
		logger.info("New " + RenameTaskFolder.class.getCanonicalName());

		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String , String>() {{
			put("zimbraPrefTasksReadingPaneLocation", "bottom");
			put("zimbraPrefShowSelectionCheckbox", "TRUE");
		}};
	}
	
	@Test( description = "Rename Task list -right click Rename", 
			groups = { "functional", "L2"})
	
	public void RenameTaskFolder_01() throws HarnessException {
		
		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);
		ZAssert.assertNotNull(taskFolder, "Verify the task is available");
		
		// Create the subTaskList
		String name = "taskList" + ConfigProperties.getUniqueString();
		
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ name +"' l='"+ taskFolder.getId() +"'/>" +
                "</CreateFolderRequest>");

		FolderItem subTaskList = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(subTaskList, "Verify the subfolder is available");
		
		// Refresh the tasks view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Rename the folder using context menu		
		DialogRenameFolder dlgRename = (DialogRenameFolder)app.zTreeTasks.zTreeItem(Action.A_RIGHTCLICK, Button.B_RENAME, subTaskList);
		ZAssert.assertNotNull(dlgRename, "Verify the dialog opened");
		
		String name2 = "1" + ConfigProperties.getUniqueString();
		dlgRename.zSetNewName(name2);
		dlgRename.zClickButton(Button.B_OK);

		app.zGetActiveAccount().soapSend("<GetFolderRequest xmlns = 'urn:zimbraMail'/>");
		
		Element[] eFolder1 = app.zGetActiveAccount().soapSelectNodes("//mail:folder[@name='"+ name +"']");
		ZAssert.assertEquals(eFolder1.length, 0, "Verify the old tasklist name no longer exists");
		
		Element[] eFolder2 = app.zGetActiveAccount().soapSelectNodes("//mail:folder[@name='"+ name2 +"']");
		ZAssert.assertEquals(eFolder2.length, 1, "Verify the new tasklist/folder name exists");
	}
	
	@Bugs(ids="62365")
	@Test( description = "Rename a tasklist - set to an invalid name with ':'", 
		groups = { "functional", "L3"})
	
	public void RenameTaskFolder_02() throws HarnessException {
		
		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);
		ZAssert.assertNotNull(taskFolder, "Verify the task is available");
		
		// Create the subTaskList
		String name = "taskList" + ConfigProperties.getUniqueString();
		
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ name +"' l='"+ taskFolder.getId() +"'/>" +
                "</CreateFolderRequest>");

		FolderItem subTaskList = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(subTaskList, "Verify the subfolder is available");
		
		// Refresh the tasks view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Rename the folder using context menu		
		DialogRenameFolder dlgRename = (DialogRenameFolder)app.zTreeTasks.zTreeItem(Action.A_RIGHTCLICK, Button.B_RENAME, subTaskList);
		ZAssert.assertNotNull(dlgRename, "Verify the dialog opened");
		
		String name2 = "2:" + ConfigProperties.getUniqueString();
		dlgRename.zSetNewName(name2);
		dlgRename.zClickButton(Button.B_OK);

		DialogError error = app.zPageMain.zGetErrorDialog(DialogErrorID.InvalidFolderName);
		ZAssert.assertTrue(error.zIsActive(), "Verify the error dialog appears");
		
		error.zClickButton(Button.B_OK);
		dlgRename.zClickButton(Button.B_CANCEL);
	}
	
	@Test( description = "Rename Task list -right click Edit, Change name(Context menu -> Edit)", 
			groups = { "functional", "L3"})
	
	public void RenameTaskFolder_03() throws HarnessException {
		
		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);
		ZAssert.assertNotNull(taskFolder, "Verify the task is available");
		
		// Create the subTaskList
		String name = "taskList" + ConfigProperties.getUniqueString();
		
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ name +"' l='"+ taskFolder.getId() +"'/>" +
                "</CreateFolderRequest>");

		FolderItem subTaskList = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(subTaskList, "Verify the subfolder is available");
		
		// Refresh the tasks view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Edit the folder using context menu		
		DialogEditFolder dialog = (DialogEditFolder) app.zTreeTasks.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, subTaskList);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");
		
		String name2 = "3" + ConfigProperties.getUniqueString();
		
		// Change the name, click OK
		dialog.zSetNewName(name2);		
		dialog.zClickButton(Button.B_OK);

		app.zGetActiveAccount().soapSend("<GetFolderRequest xmlns = 'urn:zimbraMail'/>");
		
		Element[] eFolder1 = app.zGetActiveAccount().soapSelectNodes("//mail:folder[@name='"+ name +"']");
		ZAssert.assertEquals(eFolder1.length, 0, "Verify the old tasklist name no longer exists");
		
		Element[] eFolder2 = app.zGetActiveAccount().soapSelectNodes("//mail:folder[@name='"+ name2 +"']");
		ZAssert.assertEquals(eFolder2.length, 1, "Verify the new tasklist/folder name exists");
	}

}

