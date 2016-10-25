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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.day.singleday.tags;

import java.awt.AWTException;
import java.util.Calendar;
import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class UnTagAppointment extends AjaxCommonTest {

	public UnTagAppointment() {
		logger.info("New "+ UnTagAppointment.class.getCanonicalName());

		// All tests start at the Calendar page
		super.startingPage = app.zPageCalendar;

		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L;
		{
		    put("zimbraPrefCalendarInitialView", "day");
		}};
	}


	@Test( description = "Untag an appointment using toolbar button in day view",
			groups = { "functional" })

	public void UnTagAppointment_01() throws HarnessException {

		// Create objects
		String apptSubject, apptBody, tag1;
		tag1 = ConfigProperties.getUniqueString();
		apptSubject = ConfigProperties.getUniqueString();
		apptBody = ConfigProperties.getUniqueString();
		ZDate startDate = new ZDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 10, 0, 0);

		// Create new appointment
		AppointmentItem appt = AppointmentItem.createAppointmentSingleDay(app.zGetActiveAccount(), startDate, 60, null, apptSubject, apptBody, null, null);
        String apptId = appt.dApptID;

        // Create new tag and get tag ID
		app.zPageCalendar.zCreateTag(app, tag1, 2);
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Remove tag from appointment using toolbar button
		app.zGetActiveAccount().soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>" + "<action id='" + apptId +"' op='tag' tn='"+ tag1 +"'/>" + "</ItemActionRequest>");
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);
        app.zPageCalendar.zToolbarPressButton(Button.O_LISTVIEW_TAG);
        app.zPageCalendar.zToolbarPressButton(Button.O_LISTVIEW_REMOVETAG);
        SleepUtil.sleepSmall();

        // Verify appointment is not tagged
        app.zGetActiveAccount().soapSend("<GetAppointmentRequest xmlns='urn:zimbraMail' id='" + apptId + "'/>");
        ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:appt", "t"), null, "Verify appointment is not tagged");

		// Verify search result from UI
		app.zTreeCalendar.zTreeItem(Action.A_LEFTCLICK, tag1);
		SleepUtil.sleepMedium();
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify search result after clicking tag");
	}


	@Test( description = "Untag tagged appointment using context menu in day view",
			groups = { "functional" })

	public void UnTagAppointment_02() throws HarnessException, AWTException {

		// Create objects
		String apptSubject, apptBody, tag1;
		tag1 = ConfigProperties.getUniqueString();
		apptSubject = ConfigProperties.getUniqueString();
		apptBody = ConfigProperties.getUniqueString();
		ZDate startDate = new ZDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 12, 0, 0);

		// Create new appointment
		AppointmentItem appt = AppointmentItem.createAppointmentSingleDay(app.zGetActiveAccount(), startDate, 60, null, apptSubject, apptBody, null, null);
        String apptId = appt.dApptID;

        // Create new tag and get tag ID
		app.zPageCalendar.zCreateTag(app, tag1, 4);
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		app.zGetActiveAccount().soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");;
		app.zGetActiveAccount().soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>" + "<action id='" + apptId +"' op='tag' tn='"+ tag1 +"'/>" + "</ItemActionRequest>");

		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Work around
		app.zPageMain.sRefresh();
		app.zPageCalendar.zNavigateTo();

		// Remove tag from appointment using context menu
        app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, apptSubject);
        app.zPageCalendar.zMouseOver(Button.B_TAGAPPOINTMENTMENU);
        SleepUtil.sleepSmall(); //reqires to select sub menu otherwise test fails here
        app.zPageCalendar.zToolbarPressButton(Button.O_TAG_APPOINTMENT_REMOVE_TAG_SUB_MENU);

        // Verify appointment is not tagged
        app.zGetActiveAccount().soapSend("<GetAppointmentRequest xmlns='urn:zimbraMail' id='" + apptId + "'/>");
        ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:appt", "t"), null, "Verify appointment is not tagged");

	}
}
