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
package com.zimbra.qa.selenium.projects.ajax.ui.calendar;

import java.awt.event.KeyEvent;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;

public class QuickAddAppointment extends AbsTab {

	public static class Locators {

		// Quick add appointment
		public static final String SubjectFieldQuickAdd = "css=div[class='DwtDialog'] td[id$='_subject'] input";
		public static final String LocationFieldQuickAdd = "css=div[class='DwtDialog'] td[id$='_location'] input";
		public static final String DisplayDropdpwnQuickAdd = "css=div[class='DwtDialog'] td[id$='_showAs']";
		public static final String MarkAsDropdownQuickAdd = "css=div[class='DwtDialog'] td[id$='_privacy']";
		public static final String CalendarDropdownQuickAdd = "css=div[class='DwtDialog'] td[id$='_calendar']";
		public static final String StartDateFieldQuickAdd = "css=div[class='DwtDialog'] input[id$='_startDate']";
		public static final String EndDateFieldQuickAdd = "css=div[class='DwtDialog'] input[id$='_endDate']";
		public static final String StartTimeFieldQuickAdd = "css=div[class='DwtDialog'] td[id$='_startTime'] td[id$='_timeSelectInput'] input";
		public static final String EndTimeFieldQuickAdd = "css=div[class='DwtDialog'] td[id$='_endTime'] td[id$='_timeSelectInput'] input";
		public static final String RepeatDropdownQuickAdd = "css=div[class='DwtDialog'] td[id$='_repeat'] td[id$='_title']";
		public static final String ReminderDropdownQuickAdd = "css=div[class='DwtDialog'] td[id$='_reminderSelect']";
		public static final String OKButtonQuickAdd = "css=div[class='DwtDialog'] td[id$='_button2_title']:contains("
				+ "'OK'" + ")";
		public static final String CancelButtonQuickAdd = "css=div[class='DwtDialog'] td[id$='_button1_title']:contains("
				+ "'Cancel'" + ")";
		public static final String MoreDetailsButtonQuickAdd = "css=div[class='DwtDialog'] div[id$='_buttons'] td[id^='More Details..._DWT'] td[id$='_title']";
		public static final String ConfigureReminder = "css=div[class='DwtDialog'] div[class='FakeAnchor']:contains('Configure')";

		public static final String NoneMenuItem = "css=div[id*='_Menu'] div[id^='NON'] td[id$='title']:contains('None')";
		public static final String NoneButton = "css=td[id$='_title']:contains('None')";
		public static final String EveryDayMenuItem = "css=div[id*='_Menu'] div[id^='DAI'] td[id$='title']:contains('Every Day')";
		public static final String EveryDayButton = "css=td[id$='_title']:contains('Every Day')";
		public static final String EveryWeekMenuItem = "css=div[id*='_Menu'] div[id^='WEE'] td[id$='title']:contains('Every Week')";
		public static final String EveryWeekButton = "css=td[id$='_title']:contains('Every Week')";
		public static final String EveryMonthMenuItem = "css=div[id*='_Menu'] div[id^='MON'] td[id$='title']:contains('Every Month')";
		public static final String EveryMonthButton = "css=td[id$='_title']:contains('Every Month')";
		public static final String EveryYearMenuItem = "css=div[id*='_Menu'] div[id^='YEA'] td[id$='title']:contains('Every Year')";
		public static final String EveryYearButton = "css=td[id$='_title']:contains('Every Year')";
		public static final String CustomMenuItem = "css=div[id*='_Menu'] div[id^='CUS'] td[id$='title']:contains('Custom')";
		public static final String CustomButton = "css=td[id$='_title']:contains('Custom')";
		public static final String RepeatEnabled = "css=div[id$='_repeatDesc']div[class='FakeAnchor']";
		public static final String RepeatDisabled = "css=div[id$='_repeatDesc']div[class='DisabledText']";

		public static final String QuickAddDialog = "css=div[class='DwtDialog'] td[class='DwtDialogTitle']:contains('QuickAdd Appointment')";

	}

	public static class Field {

		public static final Field Subject = new Field("Subject");
		public static final Field Location = new Field("Location");
		public static final Field Display = new Field("Display");
		public static final Field MarkAs = new Field("MarkAs");
		public static final Field Calendar = new Field("Calendar");
		public static final Field StartDate = new Field("StartDate");
		public static final Field StartTime = new Field("StartTime");
		public static final Field EndDate = new Field("EndDate");
		public static final Field EndTime = new Field("EndTime");
		public static final Field Repeat = new Field("Repeat");
		public static final Field Reminder = new Field("Reminder");

		private String field;

		private Field(String name) {
			field = name;
		}

		@Override
		public String toString() {
			return (field);
		}

	}

	public QuickAddAppointment(AbsApplication application) {
		super(application);

		logger.info("new " + QuickAddAppointment.class.getCanonicalName());
	}

	public void zFillField(Field field, ZDate value) throws HarnessException {
		String stringFormat;

		if (field == Field.StartDate || field == Field.EndDate) {
			stringFormat = value.toMM_DD_YYYY();
		} else if (field == Field.StartTime || field == Field.EndTime) {
			stringFormat = value.tohh_mm_aa();
		} else {
			throw new HarnessException("zFillField() not implemented for field: " + field);
		}

		zFillField(field, stringFormat);
	}

	public void zFillField(Field field, String value) throws HarnessException {
		
		SleepUtil.sleepSmall();

		tracer.trace("Set " + field + " to " + value);

		String locator = null;
		String isRepeat = null;

		if (field == Field.Subject) {
			locator = Locators.SubjectFieldQuickAdd;

		} else if (field == Field.Location) {
			locator = Locators.LocationFieldQuickAdd;

		} else if (field == Field.StartDate) {
			locator = Locators.StartDateFieldQuickAdd;

		} else if (field == Field.StartTime) {
			locator = Locators.StartTimeFieldQuickAdd;

		} else if (field == Field.EndDate) {
			locator = Locators.EndDateFieldQuickAdd;

		} else if (field == Field.EndTime) {
			locator = Locators.EndTimeFieldQuickAdd;

		} else if (field == Field.Display) {
			locator = Locators.DisplayDropdpwnQuickAdd;

		} else if (field == Field.Calendar) {

			locator = Locators.CalendarDropdownQuickAdd;
			this.sClickAt(locator, "");

			value = "css=div[id*='_Menu_'] td[id$='_title']:contains('" + value + "')";
			this.sClickAt(value, "");

			return;

		} else if (field == Field.Repeat) {

			isRepeat = value;
			locator = Locators.RepeatDropdownQuickAdd;

		} else {
			throw new HarnessException("not implemented for field " + field);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for field " + field);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Field is not present field=" + field + " locator=" + locator);

		if (isRepeat != null) {
			this.sClickAt(locator, "");
			zRecurringOptions(locator, value, isRepeat);

		} else if (field == Field.StartDate || field == Field.EndDate || field == Field.StartTime
				|| field == Field.EndTime) {
			this.clearField(locator);
			this.sClickAt(locator, "");
			this.sTypeDateTime(locator, value);
			this.zKeyboardKeyEvent(KeyEvent.VK_TAB);
		} else {
			this.clearField(locator);
			this.sType(locator, value);
		}
		this.zWaitForBusyOverlay();
	}

	public void zNewAppointment() throws HarnessException {
		this.zRightClickAt(
				"css=div[class='calendar_hour_scroll'] td[class='calendar_grid_body_time_td'] div[id$='_10']", "");
		this.sClickAt("css=div[id^='POPUP_'] td[id='NEW_APPT_title']", "");
		SleepUtil.sleepMedium();
	}

	public void zNewAppointmentMonthView(Action action) throws HarnessException {
		if (action.equals(Action.A_DOUBLECLICK)) {
			this.sDoubleClick("css=td[class='calendar_month_cells_td']");
		} else if (action.equals(Action.A_RIGHTCLICK)) {
			this.zRightClickAt("css=td[class='calendar_month_cells_td']", "");
			this.sClick("css=div[id^='POPUP_'] td[id='NEW_APPT_title']");
		}
		SleepUtil.sleepMedium();
	}

	public void zNewAppointmentUsingMiniCal() throws HarnessException {
		this.sClick("css=div[class='DwtCalendar'] td[class='DwtCalendarButton'] div[class='ImgFwdArrowSmall']");
		if (this.sIsElementPresent("css=td[class='DwtCalendarDay']:contains('25')")) {
			this.zRightClickAt("css=td[class='DwtCalendarDay']:contains('25')", "");
		} else {
			this.zRightClickAt("css=td[class='DwtCalendarDay DwtCalendarDay-selected']:contains('25')", "");
		}
		this.sClick("css=div[id^='POPUP_'] td[id='NEW_APPT_title']");
		SleepUtil.sleepMedium();
	}

	public void zNewAllDayAppointment() throws HarnessException {
		this.zRightClickAt(
				"css=div[class='calendar_hour_scroll'] td[class='calendar_grid_body_time_td'] div[id$='_10']", "");
		this.sClick("css=div[id^='POPUP_'] td[id='NEW_ALLDAY_APPT_title']");
		SleepUtil.sleepMedium();
	}

	public void zNewAllDayAppointmentUsingMiniCal() throws HarnessException {
		this.sClick("css=div[class='DwtCalendar'] td[class='DwtCalendarButton'] div[class='ImgFwdArrowSmall']");
		if (this.sIsElementPresent("css=td[class='DwtCalendarDay']:contains('25')")) {
			this.zRightClickAt("css=td[class='DwtCalendarDay']:contains('25')", "");
		} else {
			this.zRightClickAt("css=td[class='DwtCalendarDay DwtCalendarDay-selected']:contains('25')", "");
		}
		this.sClick("css=div[id^='POPUP_'] td[id='NEW_ALLDAY_APPT_title']");
		SleepUtil.sleepMedium();
	}

	public void zVerifyQuickAddDialog(Boolean status) throws HarnessException {
		SleepUtil.sleepSmall();
		ZAssert.assertEquals(this.sIsElementPresent(Locators.QuickAddDialog), status,
				"Verify quick add appt dialog status");
		this.sClick(Locators.CancelButtonQuickAdd);
		SleepUtil.sleepSmall();
	}

	public void zVerifyConfigureReminderLink(Boolean status) throws HarnessException {
		SleepUtil.sleepSmall();
		ZAssert.assertEquals(this.sIsElementPresent(Locators.ConfigureReminder), status,
				"Verify configure reminder link");
	}

	public void zMoreDetails() throws HarnessException {
		SleepUtil.sleepSmall();
		this.sClick(Locators.MoreDetailsButtonQuickAdd);
		this.zWaitForBusyOverlay();
		SleepUtil.sleepLongMedium();
	}

	public void zFill(IItem item) throws HarnessException {

		logger.info(myPageName() + ".zFill(ZimbraItem)");

		// Make sure the item is a MailItem
		if (!(item instanceof AppointmentItem)) {
			throw new HarnessException("Invalid item type - must be AppointmentItem");
		}

		AppointmentItem appt = (AppointmentItem) item;

		// Subject
		if (appt.getSubject() != null) {
			zFillField(Field.Subject, appt.getSubject());
		}

		// Location
		if (appt.getLocation() != null) {
			zFillField(Field.Location, appt.getLocation());
			this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		}

		// Start date-time
		if (appt.getStartTime() != null) {
			zFillField(Field.StartDate, appt.getStartTime());

			if (com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.quickadd.CreateAllDayAppointment.allDayTest == false
					|| com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.allday.minicalendar.CreateAllDayMeeting.allDayTest == false) {
				zFillField(Field.StartTime, appt.getStartTime());
			}
		}

		// End date-time
		if (appt.getEndTime() != null) {
			zFillField(Field.EndDate, appt.getEndTime());

			if (com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.quickadd.CreateAllDayAppointment.allDayTest == false
					|| com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.allday.minicalendar.CreateAllDayMeeting.allDayTest == false) {
				zFillField(Field.EndTime, appt.getEndTime());
			}
		}

		// Calendar
		if (appt.getFolder() != null) {
			zFillField(Field.Calendar, appt.getFolder());
		}

		// Is recurring
		if (appt.getRecurring() != null) {
			zFillField(Field.Repeat, appt.getRecurring());
		}

	}

	public void zRecurringOptions(String locator, String recurringType, String endBy) throws HarnessException {

		if (recurringType.split(",")[0].toUpperCase().equals("NONE")) {
			this.sClickAt(Locators.NoneMenuItem, "");

		} else if (recurringType.split(",")[0].toUpperCase().equals("EVERYDAY")) {
			this.sClickAt(Locators.EveryDayMenuItem, "");

		} else if (recurringType.split(",")[0].toUpperCase().equals("EVERYWEEK")) {
			this.sClickAt(Locators.EveryWeekMenuItem, "");

		} else if (recurringType.split(",")[0].toUpperCase().equals("EVERYMONTH")) {
			this.sClickAt(Locators.EveryMonthMenuItem, "");

		} else if (recurringType.split(",")[0].toUpperCase().equals("EVERYYEAR")) {
			this.sClickAt(Locators.EveryYearMenuItem, "");

		} else if (recurringType.split(",")[0].toUpperCase().equals("CUSTOM")) {
			this.sClickAt(Locators.CustomMenuItem, "");
		} else {
			this.sType(locator, recurringType);
		}
	}

	public void zSubmit() throws HarnessException {
		logger.info("PageCalendar.submit()");
		this.zClickAt(Locators.OKButtonQuickAdd, "");
		SleepUtil.sleepMedium();
	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException {
		return null;
	}

	@Override
	public void zNavigateTo() throws HarnessException {
	}

	public AbsPage zClick(Button button) throws HarnessException {
		SleepUtil.sleepSmall();
		if (button.equals(Button.B_SUGGESTALOCATION)) {
			this.sClickAt("css=div[id$='_suggest_location']:contains('Suggest a location')", "");
			SleepUtil.sleepMedium();
		} else if (button.equals(Button.B_SUGGESTEDLOCATION)) {
			this.sClickAt("css=div[id$='_suggest_view'] div[id^='zli__CSLP__DWT']:contains('" + 111 + "')", "");
			SleepUtil.sleepMedium();

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}
		return null;
	}

	public AbsPage zClick(Button button, String value) throws HarnessException {
		SleepUtil.sleepSmall();
		if (button.equals(Button.B_SUGGESTEDLOCATION)) {
			this.sClickAt("css=div[id$='_suggest_view'] div[id^='zli__CSLP__DWT']:contains('" + value + "')", "");
			SleepUtil.sleepMedium();

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}
		return null;
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		return null;
	}

	@Override
	public String myPageName() {
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		return false;
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		return null;
	}

}
