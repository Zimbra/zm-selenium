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
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DialogEditFolder;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DialogEditFolder.FolderColor;

public class ChangeColorTaskFolder extends AjaxCore {

	@SuppressWarnings("serial")
	public ChangeColorTaskFolder() {
		logger.info("New " + ChangeColorTaskFolder.class.getCanonicalName());

		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String , String>() {{
			put("zimbraPrefTasksReadingPaneLocation", "bottom");
			put("zimbraPrefShowSelectionCheckbox", "TRUE");
		}};
	}


	@Test (description = "Edit a folder, change the color (Context menu -> Edit)",
			groups = { "sanity" })

	public void ChangeColorTaskFolder_01() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);
		ZAssert.assertNotNull(taskFolder, "Verify the task is available");

		// Create the subTaskList
		String name = "taskList" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name
				+ "' l='" + taskFolder.getId() + "'/>" + "</CreateFolderRequest>");

		FolderItem subTaskList = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(subTaskList, "Verify the subfolder is available");

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Edit the folder using context menu
		DialogEditFolder dialog = (DialogEditFolder) app.zTreeTasks.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, subTaskList);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		// Change the color, click OK
		dialog.zSetNewColor(FolderColor.Blue);
		dialog.zPressButton(Button.B_OK);

		// Check the color
		app.zGetActiveAccount().soapSend("<GetFolderRequest xmlns='urn:zimbraMail'>" + "<folder id='"
				+ subTaskList.getId() + "'/>" + "</GetFolderRequest>");

		String color = app.zGetActiveAccount().soapSelectValue("//mail:folder[@name='" + subTaskList.getName() + "']", "color");
		ZAssert.assertEquals(color, "1", "Verify the color of the folder is set to blue (1)");
	}
}