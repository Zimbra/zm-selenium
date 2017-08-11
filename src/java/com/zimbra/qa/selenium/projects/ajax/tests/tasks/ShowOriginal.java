/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.tasks;

import java.util.*;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.*;

public class ShowOriginal extends AjaxCommonTest {

	@SuppressWarnings("serial")
	public ShowOriginal() {
		logger.info("New " + ShowOriginal.class.getCanonicalName());

		// All tests start at the login page
		super.startingPage = app.zPageTasks;

		super.startingAccountPreferences = new HashMap<String, String>() {
			{
				put("zimbraPrefTasksReadingPaneLocation", "bottom");
				put("zimbraPrefShowSelectionCheckbox", "TRUE");
			}
		};

	}

	@Bugs(ids = "51017")
	@Test( description = "Show Original Pop Up should Get Open With Proper Content", 
		groups = { "smoke", "L2"})
	
	public void ShowOriginal_01() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Create a basic task
		String subject = "task" + ConfigProperties.getUniqueString();
		String windowUrl = "service/home/~/";

		app.zGetActiveAccount().soapSend(
				"<CreateTaskRequest xmlns='urn:zimbraMail'>" + "<m >" + "<inv>"
				+ "<comp name='" + subject + "'>" + "<or a='"
				+ app.zGetActiveAccount().EmailAddress + "'/>"
				+ "</comp>" + "</inv>" + "<su>" + subject + "</su>"
				+ "<mp ct='text/plain'>" + "<content>content"
				+ ConfigProperties.getUniqueString()
				+ "</content>" + "</mp>" + "</m>"
				+ "</CreateTaskRequest>");

		// Refresh the tasks view
		app.zPageTasks.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Select the item
		app.zPageTasks.zListItem(Action.A_MAIL_CHECKBOX, subject);

		// Right click the item, select Show Original
		SeparateWindow window = (SeparateWindow) app.zPageTasks.zListItem(Action.A_RIGHTCLICK, Button.O_SHOW_ORIGINAL, subject);

		try {
			window.zSetWindowTitle(windowUrl);
			ZAssert.assertTrue(window.zIsWindowOpen(windowUrl),"Verify the window is opened and switch to it");
			
			// Verify show original window content
			String ShowOrigBody = window.sGetBodyText();
			ZAssert.assertStringContains(ShowOrigBody, subject,"Verify subject in show original window");

		} finally {
			app.zPageMain.zCloseWindow(window,windowUrl, app);

		}

	}
}
