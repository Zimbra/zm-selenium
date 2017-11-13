/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.appointments.views.day;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;

public class VerifySplitView extends UniversalCommonTest {

	public VerifySplitView() {
		logger.info("New "+ VerifySplitView.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
		// Make sure we are using an account with day view
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L;
		{
		    put("zimbraPrefCalendarInitialView", "day");
		}};
	}


	@Bugs (ids = "66603")
	@Test (description = "Verify that in Day-split view all calendars are visible correctly",
			groups = { "smoke", "L1" } )
	
	public void VerifySplitView_01() throws HarnessException {

		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);

		// Create folders
		String folder1 = ConfigProperties.getUniqueString();
		String folder2 = ConfigProperties.getUniqueString();
		String folder3 = ConfigProperties.getUniqueString();
		String folder4 = ConfigProperties.getUniqueString();
		String folder5 = ConfigProperties.getUniqueString();
		String mergeLocator = "css=td[class='ZWidgetTitle']:contains('Merge')";
		String splitLocator = "css=td[class='ZWidgetTitle']:contains('Split')";

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ folder1+"' l='"+ root.getId() +"' view='appointment'/>" +
                "</CreateFolderRequest>");

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ folder2+"' l='"+ root.getId() +"' view='appointment'/>" +
                "</CreateFolderRequest>");

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ folder3+"' l='"+ root.getId() +"' view='appointment'/>" +
                "</CreateFolderRequest>");

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ folder4+"' l='"+ root.getId() +"' view='appointment'/>" +
                "</CreateFolderRequest>");

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ folder5+"' l='"+ root.getId() +"' view='appointment'/>" +
                "</CreateFolderRequest>");

        app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		app.zTreeCalendar.zMarkOnOffCalendarFolder(folder1);
		SleepUtil.sleepSmall();
		app.zTreeCalendar.zMarkOnOffCalendarFolder(folder2);
		SleepUtil.sleepSmall();
		app.zTreeCalendar.zMarkOnOffCalendarFolder(folder3);
		SleepUtil.sleepSmall();
		app.zTreeCalendar.zMarkOnOffCalendarFolder(folder4);
		SleepUtil.sleepSmall();
		app.zTreeCalendar.zMarkOnOffCalendarFolder(folder5);
		SleepUtil.sleepSmall();

		if (!app.zPageCalendar.sIsElementPresent(mergeLocator)) {
			app.zPageCalendar.sClickAt(splitLocator, "");
			SleepUtil.sleepMedium();
		}

        ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent("css=div[class='ZmCalDayTab ZmCalDayMerged']:contains('" + folder1 + "')"), "First folder is present");
        ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent("css=div[class='ZmCalDayTab ZmCalDayMerged']:contains('" + folder2 + "')"), "Second folder is present");
        ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent("css=div[class='ZmCalDayTab ZmCalDayMerged']:contains('" + folder3 + "')"), "Third folder is present");
        ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent("css=div[class='ZmCalDayTab ZmCalDayMerged']:contains('" + folder4 + "')"), "Fourth folder is present");
        ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent("css=div[class='ZmCalDayTab ZmCalDayMerged']:contains('" + folder5 + "')"), "Fifth folder is present");
        ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent("css=div[class='ZmCalDayTab ZmCalDayMerged']:contains('Calendar')"), "Default calendar folder is present");


	}
}