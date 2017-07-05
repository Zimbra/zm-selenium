/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DialogCreateFolder;

public class CreateCalendar extends AjaxCommonTest {


	public CreateCalendar() {
		logger.info("New " + CreateCalendar.class.getCanonicalName());

		// All tests start at the login page
		super.startingPage = app.zPageCalendar;
		
	}


	@Test( description = "Create a new calendar by clicking 'new folder' on folder tree", 
			groups = { "sanity", "L0" })
	
	public void CreateCalendar_01() throws HarnessException {

		
		// Set the new calendar name
		String calendarname = "calendar" + ConfigProperties.getUniqueString();

		
		// Click on the "New Calendar" button in the calendar tree
		DialogCreateFolder dialog = (DialogCreateFolder) app.zTreeCalendar.zPressButton(Button.B_TREE_NEWFOLDER);

		// Fill out the dialog
		dialog.zEnterFolderName(calendarname);
		dialog.zClickButton(Button.B_OK);

		
		// Make sure the folder was created on the ZCS server
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), calendarname);
		ZAssert.assertNotNull(folder, "Verify the new folder is found");
		ZAssert.assertEquals(folder.getName(), calendarname, "Verify the server and client folder names match");
		
	}

	@Test( description = "Create a new calendar using keyboard shortcuts", 
			groups = { "functional", "L2" })
	public void CreateCalendar_02() throws HarnessException {

		
		// Set the new calendar name
		String calendarname = "calendar" + ConfigProperties.getUniqueString();


		// Click on the "New Calendar" button in the calendar tree
		DialogCreateFolder dialog = (DialogCreateFolder) app.zPageCalendar.zKeyboardShortcut(Shortcut.S_NEWCALENDAR);

		// Fill out the dialog
		dialog.zEnterFolderName(calendarname);
		dialog.zClickButton(Button.B_OK);

		
		// Make sure the folder was created on the ZCS server
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), calendarname);
		ZAssert.assertNotNull(folder, "Verify the new folder is found");
		ZAssert.assertEquals(folder.getName(), calendarname, "Verify the server and client folder names match");

	}

	@Test( description = "Create a new folder using context menu from root folder", 
			groups = { "functional", "L2" })
	public void CreateCalendar_03() throws HarnessException {

		// Set the new calendar name
		String calendarname = "calendar" + ConfigProperties.getUniqueString();

		// Determine the calendar folder
		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.UserRoot);
		
		
		// Click on the "New Calendar" button in the calendar tree
		DialogCreateFolder dialog = (DialogCreateFolder) app.zTreeCalendar.zTreeItem(Action.A_RIGHTCLICK, Button.O_NEW_CALENDAR, root);

		// Fill out the dialog
		dialog.zEnterFolderName(calendarname);
		dialog.zClickButton(Button.B_OK);

		
		// Make sure the folder was created on the ZCS server
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), calendarname);
		ZAssert.assertNotNull(folder, "Verify the new folder is found");
		ZAssert.assertEquals(folder.getName(), calendarname, "Verify the server and client folder names match");
		
	}

	@Test( description = "Create a new folder using mail app New -> New Folder", 
			groups = { "functional", "L2" })
	public void CreateCalendar_04() throws HarnessException {

		// Set the new calendar name
		String calendarname = "calendar" + ConfigProperties.getUniqueString();

		// Create a new folder in the inbox
		// using the context menu + New Folder
		DialogCreateFolder dialog = (DialogCreateFolder) app.zPageCalendar.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_CALENDAR);
		ZAssert.assertNotNull(dialog, "Verify the new dialog opened");

		// Fill out the form with the basic details
		dialog.zEnterFolderName(calendarname);
		dialog.zClickButton(Button.B_OK);

		
		// Make sure the folder was created on the ZCS server
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), calendarname);
		ZAssert.assertNotNull(folder, "Verify the new folder is found");
		ZAssert.assertEquals(folder.getName(), calendarname, "Verify the server and client folder names match");

	}


}
