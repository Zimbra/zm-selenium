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
package com.zimbra.qa.selenium.projects.universal.tests.tasks.folders;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;

public class DeleteTaskFolder extends AjaxCommonTest {
	@SuppressWarnings("serial")
	public DeleteTaskFolder() {
		logger.info("New " + DeleteTaskFolder.class.getCanonicalName());

		// test starts at the task tab
		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String , String>() {{
			put("zimbraPrefTasksReadingPaneLocation", "bottom");
			put("zimbraPrefShowSelectionCheckbox", "TRUE");
		}};
	}	
	
	@Test( description = "Delete Task list -right click delete", 
			groups = { "smoke", "L0"})
	public void DeleteTaskFolder_01() throws HarnessException {
		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);
		ZAssert.assertNotNull(taskFolder, "Verify the task is available");
		
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		ZAssert.assertNotNull(trash, "Verify the trash is available");
		
		// Create the subTaskList
		String name = "taskList" + ConfigProperties.getUniqueString();
		
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ name +"' l='"+ taskFolder.getId() +"'/>" +
                "</CreateFolderRequest>");

		FolderItem subTaskList = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(subTaskList, "Verify the subfolder is available");
		
		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Delete the folder using context menu
		app.zTreeTasks.zTreeItem(Action.A_RIGHTCLICK, Button.B_DELETE, subTaskList);
		
		// Verify the folder is now in the trash
		subTaskList = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(subTaskList, "Verify the subfolder is again available");
		ZAssert.assertEquals(trash.getId(), subTaskList.getParentId(), "Verify the subfolder's parent is now the trash folder ID");

	}

}
