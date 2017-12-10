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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.PageCalendar.Locators;

public class ChangeCalendarView extends AjaxCore {

	public ChangeCalendarView() {
		logger.info("New "+ ChangeCalendarView.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test (description = "Verify changing calendar view works using right click menu options",
			groups = { "sanity", "L0" } )

	public void ChangeCalendarView_01() throws HarnessException {

		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_VIEW_MENU, Button.O_VIEW_DAY_SUB_MENU, "Day");
        ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent(Locators.CalendarViewDayCSS), "Verify view changed to day view");

		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_VIEW_MENU, Button.O_VIEW_WORK_WEEK_SUB_MENU, "WorkWeek");
        ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent(Locators.CalendarViewWorkWeekCSS), "Verify view changed to work week view");

        // Context menu hidden after switching views so applying work around
        app.zPageMain.zRefreshMainUI();
        app.zPageCalendar.zNavigateTo();

		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_VIEW_MENU, Button.O_VIEW_WEEK_SUB_MENU, "Week");
        ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent(Locators.CalendarViewWeekCSS), "Verify view changed to week view");

        // Context menu hidden after switching views so applying work around
        app.zPageMain.zRefreshMainUI();
        app.zPageCalendar.zNavigateTo();

		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_VIEW_MENU, Button.O_VIEW_MONTH_SUB_MENU, "Month");
        ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent(Locators.CalendarViewMonthCSS), "Verify view changed to month view");

		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_VIEW_MENU, Button.O_VIEW_LIST_SUB_MENU, "List");
        ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent(Locators.CalendarViewListCSS), "Verify view changed to list view");
	}
}