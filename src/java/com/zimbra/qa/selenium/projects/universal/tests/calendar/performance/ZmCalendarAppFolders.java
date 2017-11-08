/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.performance;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.performance.*;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekTest;



public class ZmCalendarAppFolders extends CalendarWorkWeekTest {

	public ZmCalendarAppFolders() {
		logger.info("New "+ ZmCalendarAppFolders.class.getCanonicalName());


		super.startingPage = app.zPageMail;
		

	}

	@Test( description = "Measure the time to load the calendar app, 1 calendar",
			groups = { "performance", "L4" })
	public void ZmCalendarAppFolders_01() throws HarnessException {

		// Create a folder
		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.UserRoot);
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
					"<folder name='cal"+ ConfigProperties.getUniqueString() + "' view='appointment' l='"+ root.getId() +"'/>" +
				"</CreateFolderRequest>");


		// Sync the changes to the client (notification block)
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		
		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmCalendarAppOverviewPanel, "Load the calendar app, 1 calendar");

		// Currently in the mail app
		// Navigate to the addressbook
		//app.zPageCalendar.zNavigateTo();
		
		app.zPageCalendar.sClickAt("css=td[id='zb__App__Calendar_title']","");

		PerfMetrics.waitTimestamp(token);

		// Wait for the app to load
		app.zPageCalendar.zWaitForActive();


	}

	@Test( description = "Measure the time to load the calendar app, 100 calendars",
			groups = { "performance", "L4" })
	public void ZmCalendarAppFolders_02() throws HarnessException {

		// Create 100 folders
		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.UserRoot);
		for (int i = 0; i < 100; i++) {
			app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='cal"+ ConfigProperties.getUniqueString() + "' view='appointment' l='"+ root.getId() +"'/>" +
					"</CreateFolderRequest>");
		}


		// Sync the changes to the client (notification block)
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmCalendarAppOverviewPanel, "Load the calendar app, 100 calendars");

		// Currently in the mail app
		// Navigate to the addressbook
		//app.zPageCalendar.zNavigateTo();
		
		app.zPageCalendar.sClickAt("css=td[id='zb__App__Calendar_title']","");

		PerfMetrics.waitTimestamp(token);

		// Wait for the app to load
		app.zPageCalendar.zWaitForActive();


	}


}
