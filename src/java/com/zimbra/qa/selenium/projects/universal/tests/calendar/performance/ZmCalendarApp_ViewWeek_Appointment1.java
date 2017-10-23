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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.performance;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.performance.*;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekTest;


public class ZmCalendarApp_ViewWeek_Appointment1 extends CalendarWorkWeekTest {

	
	public ZmCalendarApp_ViewWeek_Appointment1() throws HarnessException {
		logger.info("New "+ ZmCalendarApp_ViewWeek_Appointment1.class.getCanonicalName());
		
		super.startingPage = app.zPageMail;
		
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 3038458962443347843L;
			{
				put("zimbraPrefCalendarInitialView", "week");
			}};


	}
	
	
	@Test( description = "Measure the time to load the Calendar, week view, initial load",
			groups = { "performance", "L4" })
	public void ZmCalendarApp_01() throws HarnessException {
		ZDate startDate = new ZDate(this.calendarWeekDayUTC.get(Calendar.YEAR), this.calendarWeekDayUTC.get(Calendar.MONTH) + 1, this.calendarWeekDayUTC.get(Calendar.DAY_OF_MONTH), this.calendarWeekDayUTC.get(Calendar.HOUR_OF_DAY), 0, 0);
		
		// Create an appointment
		String subject = "subject"+ ConfigProperties.getUniqueString();
		AppointmentItem.createAppointmentSingleDay(
				app.zGetActiveAccount(),
				startDate,
				60,
				null,
				subject,
				"content" + ConfigProperties.getUniqueString(),
				"location" + ConfigProperties.getUniqueString(),
				null);


		// Start the perf token
		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmCalendarApp, "Load the calendar app, week view, initial load");

		// Go to calendar
		//app.zPageCalendar.zNavigateTo();
		app.zPageCalendar.zClickAt("css=td[id='zb__App__Calendar_title']","");

		PerfMetrics.waitTimestamp(token);
				
		// Wait for the app to load
		app.zPageCalendar.zWaitForActive();
		

	}

	@Test( description = "Measure the time to load the Calendar, week view, 1 appointment",
			groups = { "performance", "L4" })
	public void ZmCalendarApp_02() throws HarnessException {
		ZDate startDate = new ZDate(this.calendarWeekDayUTC.get(Calendar.YEAR), this.calendarWeekDayUTC.get(Calendar.MONTH) + 1, this.calendarWeekDayUTC.get(Calendar.DAY_OF_MONTH), this.calendarWeekDayUTC.get(Calendar.HOUR_OF_DAY), 0, 0);
		
		// Create an appointment
		String subject = "subject"+ ConfigProperties.getUniqueString();
		AppointmentItem.createAppointmentSingleDay(
				app.zGetActiveAccount(),
				startDate,
				60,
				null,
				subject,
				"content" + ConfigProperties.getUniqueString(),
				"location" + ConfigProperties.getUniqueString(),
				null);


		// Start the perf token
		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmCalendarApp, "Load the calendar app, week view, 1 appointment");

		// Go to calendar
		//app.zPageCalendar.zNavigateTo();
		app.zPageCalendar.zClickAt("css=td[id='zb__App__Calendar_title']","");

		PerfMetrics.waitTimestamp(token);
				
		// Wait for the app to load
		app.zPageCalendar.zWaitForActive();
		

	}

	@Test( description = "Measure the time to load the Calendar, week view, 100 appointment",
			groups = { "performance", "L4" })
	public void ZmCalendarApp_03() throws HarnessException {
		
		// What is today?
		Calendar sunday = this.calendarWeekDayUTC;		sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		Calendar monday = this.calendarWeekDayUTC;		monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Calendar tuesday = this.calendarWeekDayUTC;		tuesday.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		Calendar wednesday = this.calendarWeekDayUTC;	wednesday.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		Calendar thursday = this.calendarWeekDayUTC;		thursday.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		Calendar friday = this.calendarWeekDayUTC;		friday.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		Calendar saturday = this.calendarWeekDayUTC;		saturday.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

		// Import 100 appointments using Calendar.ics and REST
		String filename = ConfigProperties.getBaseDirectory() + "/data/public/ics/calendar04/Calendar.ics";
		File file = null;

		// Modify the ICS in two ways:
		// 1. Make the current account the organizer
		// 2. Make the current date equal to today
		file = RestUtil.FileUtils.replaceInFile("user@domain.com", app.zGetActiveAccount().EmailAddress, new File(filename));
		file = RestUtil.FileUtils.replaceInFile("20111030", (new SimpleDateFormat("yyyyMMdd")).format(sunday.getTime()), file);
		file = RestUtil.FileUtils.replaceInFile("20111031", (new SimpleDateFormat("yyyyMMdd")).format(monday.getTime()), file);
		file = RestUtil.FileUtils.replaceInFile("20111101", (new SimpleDateFormat("yyyyMMdd")).format(tuesday.getTime()), file);
		file = RestUtil.FileUtils.replaceInFile("20111102", (new SimpleDateFormat("yyyyMMdd")).format(wednesday.getTime()), file);
		file = RestUtil.FileUtils.replaceInFile("20111103", (new SimpleDateFormat("yyyyMMdd")).format(thursday.getTime()), file);
		file = RestUtil.FileUtils.replaceInFile("20111104", (new SimpleDateFormat("yyyyMMdd")).format(friday.getTime()), file);
		file = RestUtil.FileUtils.replaceInFile("20111105", (new SimpleDateFormat("yyyyMMdd")).format(saturday.getTime()), file);

		RestUtil rest = new RestUtil();
		rest.setAuthentication(app.zGetActiveAccount());
		rest.setPath("/service/home/~/Calendar");
		rest.setQueryParameter("fmt", "ics");
		rest.setUploadFile(file);
		rest.doPost();



		// Start the perf token
		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmCalendarApp, "Load the calendar app, week view, 100 appointment");

		// Go to calendar
		//app.zPageCalendar.zNavigateTo();
		app.zPageCalendar.zClickAt("css=td[id='zb__App__Calendar_title']","");

		PerfMetrics.waitTimestamp(token);
				
		// Wait for the app to load
		app.zPageCalendar.zWaitForActive();
		

	}


}
