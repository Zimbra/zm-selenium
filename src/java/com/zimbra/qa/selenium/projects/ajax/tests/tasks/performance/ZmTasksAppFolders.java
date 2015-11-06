/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.tasks.performance;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.performance.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;



public class ZmTasksAppFolders extends AjaxCommonTest {

	public ZmTasksAppFolders() {
		logger.info("New "+ ZmTasksAppFolders.class.getCanonicalName());


		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = null;

	}

	@Test(	description = "Measure the time to load the tasks app, 1 task list",
			groups = { "performance" })
	public void ZmTasksAppFolders_01() throws HarnessException {

		// Create a folder
		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.UserRoot);
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
					"<folder name='task"+ ZimbraSeleniumProperties.getUniqueString() + "' view='task' l='"+ root.getId() +"'/>" +
				"</CreateFolderRequest>");


		// Sync the changes to the client (notification block)
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);
		
		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmTasksAppOverviewPanel, "Load the tasks app, 1 task list");

		// Currently in the mail app
		// Navigate to the addressbook
		//app.zPageTasks.zNavigateTo();
		 app.zPageContacts.zClickAt("css=td[id='zb__App__Tasks_title']","");

		PerfMetrics.waitTimestamp(token);

		// Wait for the app to load
		app.zPageTasks.zWaitForActive();


	}

	@Test(	description = "Measure the time to load the tasks app, 100 task lists",
			groups = { "performance" })
	public void ZmTasksAppFolders_02() throws HarnessException {

		// Create 100 folders
		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.UserRoot);
		for (int i = 0; i < 100; i++) {
			app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
					"<folder name='task"+ ZimbraSeleniumProperties.getUniqueString() + "' view='task' l='"+ root.getId() +"'/>" +
			"</CreateFolderRequest>");
		}


		// Sync the changes to the client (notification block)
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmTasksAppOverviewPanel, "Load the tasks app, 100 task lists");

		// Currently in the mail app
		// Navigate to the addressbook
		//app.zPageTasks.zNavigateTo();
		app.zPageContacts.zClickAt("css=td[id='zb__App__Tasks_title']","");

		PerfMetrics.waitTimestamp(token);

		// Wait for the app to load
		app.zPageTasks.zWaitForActive();

	}


}
