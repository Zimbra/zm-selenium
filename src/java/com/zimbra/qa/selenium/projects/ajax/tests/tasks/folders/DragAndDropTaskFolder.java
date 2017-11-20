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
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;

public class DragAndDropTaskFolder extends AjaxCore {

	@SuppressWarnings("serial")
	public DragAndDropTaskFolder() {
		logger.info("New " + DragAndDropTaskFolder.class.getCanonicalName());

		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String , String>() {{
			put("zimbraPrefTasksReadingPaneLocation", "bottom");
			put("zimbraPrefShowSelectionCheckbox", "TRUE");
		}};
	}


	@Bugs (ids = "69661")
	@Test (description = "Drag one Task folder and Drop into other",
			groups = { "smoke", "L1" })

	public void DragAndDropTaskFolder_01() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);
		ZAssert.assertNotNull(taskFolder, "Verify the task is available");

		// Create the subTaskList
		String name1 = "taskList1" + ConfigProperties.getUniqueString();
		String name2 = "taskList2" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ name1 +"' l='"+ taskFolder.getId() +"'/>" +
                "</CreateFolderRequest>");

		FolderItem subTaskList1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name1);
		ZAssert.assertNotNull(subTaskList1, "Verify the first subfolder is available");
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ name2 +"' l='"+ taskFolder.getId() +"'/>" +
                "</CreateFolderRequest>");

		FolderItem subTaskList2 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name2);
		ZAssert.assertNotNull(subTaskList2, "Verify the second subfolder is available");

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		app.zPageMail.zDragAndDrop(
				"//td[contains(@id, 'zti__main_Tasks__" + subTaskList1.getId() + "_textCell') and contains(text(), '"+ name1 + "')]",
				"//td[contains(@id, 'zti__main_Tasks__" + subTaskList2.getId() + "_textCell') and contains(text(),'"+ name2 + "')]");

		// Verify the folder is now in the other subfolder
		subTaskList1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name1);
		ZAssert.assertNotNull(subTaskList1, "Verify the subfolder is again available");
		ZAssert.assertEquals(subTaskList2.getId(), subTaskList1.getParentId(), "Verify the subfolder's parent is now the other subfolder");
	}
}