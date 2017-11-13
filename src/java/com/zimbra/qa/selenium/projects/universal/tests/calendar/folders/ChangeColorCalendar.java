/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.folders;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.universal.ui.AppUniversalClient;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DialogEditFolder;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DialogEditFolder.FolderColor;

public class ChangeColorCalendar extends CalendarWorkWeekTest {

	public ChangeColorCalendar() {
		logger.info("New " + ChangeColorCalendar.class.getCanonicalName());

		// All tests start at the login page
		super.startingPage = app.zPageCalendar;
		

	}

	@Test (description = "Edit a Calendar, change the color (Context menu -> Edit)", 
			groups = { "functional", "L2" })
	public void ChangeColorCalendar_01() throws HarnessException {

		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		ZAssert.assertNotNull(root, "Verify the inbox is available");

		// Create the subfolder
		String name = "calendar" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + name + "' l='" + root.getId() + "' view='appointment'/>"
				+	"</CreateFolderRequest>");

		FolderItem calendar = FolderItem.importFromSOAP(app .zGetActiveAccount(), name);
		ZAssert.assertNotNull(calendar, "Verify the subfolder is available");

		// Click on Get Mail to refresh the folder list
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Rename the folder using context menu
		DialogEditFolder dialog = (DialogEditFolder) app.zTreeCalendar.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, calendar);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		// Change the color, click OK
		dialog.zSetNewColor(FolderColor.Green, (AppUniversalClient)app);
		dialog.zPressButton(Button.B_OK);

		// Check the color
		app.zGetActiveAccount().soapSend(
				"<GetFolderRequest xmlns='urn:zimbraMail'>"
			+		"<folder id='" + calendar.getId() + "'/>"
			+	"</GetFolderRequest>");

		String color = app.zGetActiveAccount().soapSelectValue("//mail:folder[@name='" + calendar.getName() + "']", "color");
		ZAssert.assertEquals(color, "3", "Verify the color of the folder is set to gray (8)");
	}

}
