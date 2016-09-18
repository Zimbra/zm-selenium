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
package com.zimbra.qa.selenium.projects.touch.ui.calendar;

import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;

public class FormApptNew extends AbsForm {
	
	public static String locatorValue;
	public WebDriver webDriver = ClientSessionFactory.session().webDriver();;
	
	PageCalendar pageCal = new PageCalendar(MyApplication);

	public static class Locators {
		
		public static final String SubjectField 		= "css=div[id^='ext-newapptpanel'] div[id='ext-textfield-1'] input[name='subject']";
		public static final String AttendeesField 		= "css=div[id^='ext-newapptpanel'] div[id^='ext-contactfield'] input";
		public static final String AttendeesField2 		= "css=div[id^='ext-newapptpanel'] div[id='ext-contactfield-2'] div input";
		public static final String LocationField 		= "css=div[id^='ext-newapptpanel'] div[id='ext-textfield-2'] input[name='location']";
		public static final String StartTime 			= "css=div[id^='ext-timepickerfield-1'] div[id^='ext-input'] div";
		public static final String EndTime 				= "css=div[id^='ext-timepickerfield-1'] div[id^='ext-input'] div";
		public static final String StartDate 			= "css=div[id^='ext-datepickerfield-1'] div[id^='ext-input'] div";
		public static final String EndDate 				= "css=div[id^='ext-datepickerfield-2'] div[id^='ext-input'] div";
		public static final String CalendarFolder 		= "css=div[id^='ext-newapptpanel'] div[id='ext-selectfield-2'] div[class='x-field-mask']";
		public static final String DisplayOptions 		= "css=div[id^='ext-newapptpanel'] div[id='ext-selectfield-3'] div[class='x-field-mask']";
		
		public static final String SaveButton 			= "css=div[id^='ext-button'] span[class='x-button-label']:contains('Save')";
		public static final String CancelButton 		= "css=div[id^='ext-button'] span[class='x-button-label']:contains('Cancel')";
		public static final String AddAttendeeButton 	= "css=div[class='form-add-field-button-text']:contains('Add Attendee')";
		public static final String RemoveAttendeeButton = "css=span[class='x-button-icon x-shown contactFormMinus']";
		public static final String DoneButton 			= "css=div[id^='ext-button'] span[class='x-button-label']:contains('Done')";
		
		public static final String ConfirmationDialogYesButton 	= "css=div[id^='ext-sheet'] span[class='x-button-label']:contains('Yes')";
		public static final String ConfirmationDialogNoButton	= "css=div[id^='ext-sheet'] span[class='x-button-label']:contains('No')";
		
		public static final String NoneButton			= "css=div[id^='ext-newapptpanel'] div[id='ext-selectfield-1'] div[class='x-field-mask']";
		public static final String NoneMenuItem 		= "css=div[id^='ext-panel'] div[class^='x-list-item'] span[class='x-list-label']:contains('None')";
		public static final String EveryDayButton 		= "css=div[id^='ext-newapptpanel'] div[id='ext-selectfield-1'] div[class='x-field-mask']";
		public static final String EveryDayMenuItem 	= "css=div[id^='ext-panel'] div[class^='x-list-item'] span[class='x-list-label']:contains('Every Day')";
		public static final String EveryWeekButton 		= "css=div[id^='ext-newapptpanel'] div[id='ext-selectfield-1'] div[class='x-field-mask']";
		public static final String EveryWeekMenuItem 	= "css=div[id^='ext-panel'] div[class^='x-list-item'] span[class='x-list-label']:contains('Every Week')";
		public static final String EveryMonthButton 	= "css=div[id^='ext-newapptpanel'] div[id='ext-selectfield-1'] div[class='x-field-mask']";
		public static final String EveryMonthMenuItem 	= "css=div[id^='ext-panel'] div[class^='x-list-item'] span[class='x-list-label']:contains('Every Month')";
		public static final String EveryYearButton 		= "css=div[id^='ext-newapptpanel'] div[id='ext-selectfield-1'] div[class='x-field-mask']";
		public static final String EveryYearMenuItem 	= "css=div[id^='ext-panel'] div[class^='x-list-item'] span[class='x-list-label']:contains('Every Year')";
		
		public static final String SubjectDisabled = "css=div[id^='APPT_COMPOSE_'] td[id$='_subject'] div[class='DwtInputField-disabled']";
		public static final String ToDisabled = "css=div[id^='APPT_COMPOSE_'] td tr[id$='_forward_options'] div[class='ZButton ZPicker ZWidget ZHasText ZDisabled']";
		public static final String AttendeesDisabled = "css=div[id^='APPT_COMPOSE_'] td tr[id$='_attendeesContainer'] div[class='ZButton ZPicker ZWidget ZHasText ZDisabled']";
		public static final String OptionalDisabled = "css=div[id^='APPT_COMPOSE_'] td tr[id$='_optionalContainer'] div[class='ZButton ZPicker ZWidget ZHasText ZDisabled']";
		public static final String LocationDisabled = "css=div[id^='APPT_COMPOSE_'] td tr[id$='_forward_options'] div[class='ZButton ZPicker ZWidget ZHasText ZDisabled']";
		public static final String EquipmentDisabled = "css=div[id^='APPT_COMPOSE_'] td tr[id$='_resourcesContainer'] div[class='ZButton ZPicker ZWidget ZHasText ZDisabled']";
		public static final String DisplayDisabled = "css=div[id^='APPT_COMPOSE_'] td[id$='_showAsSelect'] div[class$='ZHasDropDown ZDisabled ZHasLeftIcon']";
		public static final String FolderDisabled = "css=div[id^='APPT_COMPOSE_'] td[id$='_folderSelect'] div[class$='ZHasDropDown ZDisabled ZHasLeftIcon']";
		public static final String PrivateDisabled = "css=div[id^='APPT_COMPOSE_'] td input[id$='_privateCheckbox'][type='checkbox'][disabled]";
		
		public static final String RepeatOptionsDisabled = "css=td[id$='_repeat_options'] div[id^='DWT'][class$='ZHasDropDown ZDisabled']";
		public static final String RepeatDescriptionDisabled = "css=div[id$='_repeatDesc'][class='DisabledText']";

		public static final String ShowOptionalLink = "css=td[id$='_show_optional']";
		public static final String ShowEquipmentLink = "css=td[id$='_show_resources']";
		public static final String CustomizeLink = "css=div[id$='repeatDesc']:contains('Customize')";
		public static final String ConfigureLink = "css=div[class='FakeAnchor']:contains('Configure')";
		public static final String SuggestAtimeLink = "css=div[id$='_suggest_time']:contains('Suggest a time')";
		public static final String SuggestATime10AM = "css=div[id$='_suggest_view'] td:contains(10:00 AM)";
		public static final String SuggestALocationLink = "css=div[id$='_suggest_location']:contains('Suggest a location')";
		public static String SuggestedLocations = "css=div[id='zv__CSLP'] div[class$='ZmLocationSuggestion']:contains('"+ locatorValue + "')";
		public static final String ShowSchedulerLink = "css=div[id$='_scheduleButton']:contains('Show')";
		public static final String HideSchedulerLink = "css=div[id$='_scheduleButton']:contains('Hide')";
		public static final String SelectFirstFreeTimeFromSuggestTimePane = "css=div[id$='_suggest_view'] table:nth-child(2) tbody tr td:nth-child(2)";
		public static final String ShowTimesAnywayLink = "css=div[id$='_suggest_view'] div[class='NoSuggestions'] span[id$='showall']";
		
		public static final String OptionsDropdown = "css=div[id$='__COMPOSE_OPTIONS'] td[id$='COMPOSE_OPTIONS_title']";
		public static final String SuggestionPreferencesMenu = "css=div[class='DwtMenu ZHasCheck ZHasIcon'] td[id$='_title']:contains('Suggestion Preferences')";
		public static final String OnlyIncludeMyWorkingHoursCheckBox = "css=input[id$='_my_working_hrs_pref']";
		public static final String OnlyIncludeOtherAttendeeCheckBox = "css=input[id$='_others_working_hrs_pref']";
		public static final String NameLocationPreferencesField = "css=div[class='ZmTimeSuggestionPrefDialog'] table[id$='_locationpref'] input[id$='_name']";
		public static final String OKButtonSuggestionPreferencesDialog = "css=div[class='ZmTimeSuggestionPrefDialog'] td[id$='_button2_title']";
		public static final String CancelButtonSuggestionPreferencesDialog = "css=div[class='ZmTimeSuggestionPrefDialog'] td[id$='_button1_title']";
		
		public static final String EveryDayRadioButton = "css=div[id='REPEAT_DAILY_DIV'] input[id='DAILY_DEFAULT']";
		public static final String EveryWeekdayRadioButton = "css=div[id='REPEAT_DAILY_DIV'] input[id='DAILY_WEEKDAY']";
		public static final String EveryXdaysRadioButton = "css=div[id='REPEAT_DAILY_DIV'] input[id='DAILY_FIELD_RADIO']";
		public static final String EveryXdaysEditField = "css=input[id='RECUR_DAILY_FIELD']";
		
		public static final String EveryXRadioButton = "css=div[id='REPEAT_WEEKLY_DIV'] input[id='WEEKLY_DEFAULT']";
		public static final String EveryXDropdown = "css=div[id='REPEAT_WEEKLY_DIV'] td[id='WEEKLY_SELECT'] td[id$='_title']";
		public static final String EveryXweeksOnRadioButton = "css=div[id='REPEAT_WEEKLY_DIV'] input[id='WEEKLY_FIELD_RADIO']";
		public static final String EveryXweeksOnEditField = "css=div[id='REPEAT_WEEKLY_DIV'] input[id='RECUR_WEEKLY_FIELD']";
		public static final String SundayCheckBox = "css=div[id='REPEAT_WEEKLY_DIV'] input[id='WEEKLY_CHECKBOX_NAME_1']";
		public static final String MondayCheckBox = "css=div[id='REPEAT_WEEKLY_DIV'] input[id='WEEKLY_CHECKBOX_NAME_2']";
		public static final String TuesdayCheckBox = "css=div[id='REPEAT_WEEKLY_DIV'] input[id='WEEKLY_CHECKBOX_NAME_3']";
		public static final String WednesdayCheckBox = "css=div[id='REPEAT_WEEKLY_DIV'] input[id='WEEKLY_CHECKBOX_NAME_4']";
		public static final String ThursdayCheckBox = "css=div[id='REPEAT_WEEKLY_DIV'] input[id='WEEKLY_CHECKBOX_NAME_5']";
		public static final String FridayCheckBox = "css=div[id='REPEAT_WEEKLY_DIV'] input[id='WEEKLY_CHECKBOX_NAME_6']";
		public static final String SaturdayCheckBox = "css=div[id='REPEAT_WEEKLY_DIV'] input[id='WEEKLY_CHECKBOX_NAME_7']";
		
		public static final String DayXofEveryYmonthsRadioButton = "css=div[id='REPEAT_MONTHLY_DIV'] input[id='MONTHLY_DEFAULT']";
		public static final String TheXYofEveryZmonthsRadioButton = "css=div[id='REPEAT_MONTHLY_DIV'] input[id='MONTHLY_FIELD_RADIO']";
		
		public static final String EveryYearOnXYRadioButton = "css=div[id='REPEAT_YEARLY_DIV'] input[id='YEALY_DEFAULT']";
		public static final String TheXYofEveryZRadioButton = "css=div[id='REPEAT_YEARLY_DIV'] input[id='YEARLY_FIELD_RADIO']";
		
		public static final String NoEndDateRadioButton = "css=div[id$='_content'] td input[id='NO_END_DATE_RADIO']";
		public static final String EndAfterXoccurrencesRadioButton = "css=div[id$='_content'] td input[id='END_AFTER_RADIO']";
		public static final String EndAfterXoccurrencesEditField = "css=div[id$='_content'] td input[id='RECUR_END_INTERVAL_FIELD']";
		public static final String EndByXDateRadioButton = "css=div[id$='_content'] td input[id='END_BY_RADIO']";
		public static final String EndByXDateEditField = "css=div[id$='_content'] td input[id='RECUR_END_BY_FIELD']";
		
		public static final String DeleteZimletContextMenu = "css=div[id^='POPUP_'] td[id='DELETE_title']";
		public static final String EditZimletContextMenu = "css=div[id^='POPUP_'] td[id='EDIT_title']";
		public static final String ExpandZimletContextMenu = "css=div[id^='POPUP_'] td[id='EXPAND_title']";
		public static final String AddToContactsZimletContextMenu = "css=div[id^='POPUP_'] td[id='CONTACT_title']";

		public static final String SendUpdatesToAddedRemovedRadioButton = "css=div[id='SEND_NOTIFY_DIALOG'] div[id='SEND_NOTIFY_DIALOG_content']>table tbody tr:nth-child(1) input";
		public static final String SendUpdatesToAllRadioButton = "css=div[id='SEND_NOTIFY_DIALOG'] div[id='SEND_NOTIFY_DIALOG_content']>table tbody tr:nth-child(2) input";

		public static final String Ok_changes = "css=td[id='CHNG_DLG_ORG_1_button2_title']";
		public static final String Cancel_changes = "css=td[id='CHNG_DLG_ORG_1_button1_title']";
		public static final String AddLocation = "css=td[id$='_title']:contains('Location:')";
		public static final String addEquipment = "css=td[id$='_title']:contains('Equipment:')";

		public static final String AddAttendees = "css=td[id$='_title']:contains('Attendees:')";
		public static final String AddOptiponalAttendees = "css=td[id$='_title']:contains('Optional:')";
		public static final String EquipmentName= "css=div[class='DwtDialog'] div[id$='_content'] table tr td:nth-child(2) input";
		public static final String ToolbarOptions= "css=td[id='zb__APPT-1__COMPOSE_OPTIONS_title']";
		public static final String RequestResponse= "css=td[id$='_title']:contains('Request Responses')";
		public static final String ConflictResourceNote = "css= div[id$='_location_status']:contains('One or more locations are not available at the selected time')";

	}

	public static class Field {

		public static final Field Subject = new Field("Subject");
		public static final Field Attendees = new Field("Attendees");
		public static final Field Optional = new Field("Optional");
		public static final Field Location = new Field("Location");
		public static final Field Equipment = new Field("Equipment");
		public static final Field StartDate = new Field("StartDate");
		public static final Field StartTime = new Field("StartTime");
		public static final Field EndDate = new Field("EndDate");
		public static final Field EndTime = new Field("EndTime");
		public static final Field AllDay = new Field("AllDay");
		public static final Field Repeat = new Field("Repeat");
		public static final Field Display = new Field("Display");
		public static final Field CalendarFolder = new Field("CalendarFolder");
		public static final Field Private = new Field("Private");
		public static final Field Reminder = new Field("Reminder");
		public static final Field Body = new Field("Body");

		private String field;

		private Field(String name) {
			field = name;
		}

		@Override
		public String toString() {
			return (field);
		}

	}

	/**
	 * Protected constuctor for this object. Only classes within this package
	 * should create DisplayMail objects.
	 * 
	 * @param application
	 */
	public FormApptNew(AbsApplication application) {
		super(application);

		logger.info("new " + FormApptNew.class.getCanonicalName());

	}
	
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zSubmit() throws HarnessException {
		logger.info("FormApptNew.submit()");
		zToolbarPressButton(Button.B_SAVE);

		this.zWaitForBusyOverlay();
		SleepUtil.sleepMedium();
		
		// Wait for the message to be delivered
		Stafpostqueue sp = new Stafpostqueue();
		sp.waitForPostqueue();
	}

	public String zGetSuggestedLocation(String apptLocation)
			throws HarnessException {
		return "css=div[id='zv__CSLP'] div[id^='zli__CSLP__']:contains('"
				+ apptLocation + "')";
	}

	public String zGetLocationVaueFromPopUp(String apptLocation)
			throws HarnessException {
		return "css=div[id^='POPUP_DWT'] td[id^='" + apptLocation + "']";

	}
	
	public Boolean zIsLocationExistsInSuggestPane(String apptLocation)
			throws HarnessException {
		return sIsElementPresent("css=div[id='zv__CSLP'] div[id^='zli__CSLP__']:contains('" + apptLocation + "')");
	}
	
	public void zAddRequiredAttendeeFromScheduler(String attendee, int keyEvent)
			throws HarnessException {
		zToolbarPressButton(Button.B_SHOW);
		SleepUtil.sleepSmall();
		this.zType("css=td[id$='_scheduler'] td[id$='_NAME_'] input", attendee);
		this.sClickAt("css=td[id$='_scheduler'] td[id$='_NAME_'] input", "");
		SleepUtil.sleepSmall();
		pageCal.zKeyboard.zTypeKeyEvent(keyEvent);
	}

	public void zAddOptionalAttendeeFromScheduler(String attendee, int keyEvent)
			throws HarnessException {
		zToolbarPressButton(Button.B_SHOW);
		SleepUtil.sleepSmall();
		this
				.zClickAt(
						"css=td[id$='_scheduler'] td[id$='_SELECT_'] td[id$='_dropdown']",
						"");
		this
				.zClickAt(
						"css=div[class='DwtMenu ZHasIcon'] td[id$='_title']:contains('Optional Attendee')",
						"");
		this.zType("css=td[id$='_scheduler'] td[id$='_NAME_'] input", attendee);
		this.sClickAt("css=td[id$='_scheduler'] td[id$='_NAME_'] input", "");
		SleepUtil.sleepSmall();
		pageCal.zKeyboard.zTypeKeyEvent(keyEvent);
	}

	public void zAddLocationFromScheduler(String location, int keyEvent)
			throws HarnessException {
		SleepUtil.sleepMedium();
		zToolbarPressButton(Button.B_SHOW);
		SleepUtil.sleepSmall();
		this
				.zClickAt(
						"css=td[id$='_scheduler'] td[id$='_SELECT_'] td[id$='_dropdown']",
						"");
		this
				.zClickAt(
						"css=div[class='DwtMenu ZHasIcon'] td[id$='_title']:contains('Location')",
						"");
		this.zType("css=td[id$='_scheduler'] td[id$='_NAME_'] input", location);
		this.sClickAt("css=td[id$='_scheduler'] td[id$='_NAME_'] input", "");
		SleepUtil.sleepSmall();
		pageCal.zKeyboard.zTypeKeyEvent(keyEvent);
	}

	public void zAddEquipmentFromScheduler(String equipment, int keyEvent)
			throws HarnessException {
		zToolbarPressButton(Button.B_SHOW);
		SleepUtil.sleepSmall();
		this
				.zClickAt(
						"css=td[id$='_scheduler'] td[id$='_SELECT_'] td[id$='_dropdown']",
						"");
		this
				.zClickAt(
						"css=div[class='DwtMenu ZHasIcon'] td[id$='_title']:contains('Equipment')",
						"");
		this
				.zType("css=td[id$='_scheduler'] td[id$='_NAME_'] input",
						equipment);
		this.sClickAt("css=td[id$='_scheduler'] td[id$='_NAME_'] input", "");
		SleepUtil.sleepSmall();
		pageCal.zKeyboard.zTypeKeyEvent(keyEvent);
	}

	public String zGetApptSubject() throws HarnessException {
		return sGetValue("css=td[id$='_subject'] input[id$='_subject_input']");
	}

	public Boolean zVerifyRequiredAttendee(String attendee)
			throws HarnessException {
		return sIsElementPresent("css=td[id$='_person'] span:contains('"
				+ attendee + "')");
	}

	public Boolean zVerifyOptionalAttendee(String attendee)
			throws HarnessException {
		return sIsElementPresent("css=td[id$='_optional'] span:contains('"
				+ attendee + "')");
	}

	public Boolean zVerifyLocation(String location) throws HarnessException {
		return sIsElementPresent("css=td[id$='_location'] span:contains('"
				+ location + "')");
	}

	public Boolean zVerifyEquipment(String equipment) throws HarnessException {
		return sIsElementPresent("css=td[id$='_resourcesData'] span:contains('"
				+ equipment + "')");
	}

	public String zGetApptBodyValue() throws HarnessException {
		return sGetValue("css=div[class='ZmHtmlEditor'] textarea[class='DwtHtmlEditorTextArea']");
	}
	
	public String zGetApptBodyText() throws HarnessException {
		String bodyText;
		this.sSelectFrame("css=iframe[id$='_content_ifr']");
		bodyText = sGetText("css=body[id='tinymce']"); 
		this.sSelectFrame("relative=top");
		return bodyText;
		
	}
	
	public void zSelectCalendarFolder(String calendarFolder) throws HarnessException {
		SleepUtil.sleepSmall();
		this.sClickAt(Locators.CalendarFolder, "0,0");
		SleepUtil.sleepSmall();
		this.sClickAt("css=div[id^='ext-panel'] div[id^='ext-simplelistitem'] span[class='x-list-label']:contains('" + calendarFolder + "')", "0,0");
		SleepUtil.sleepSmall();
	}

	public void zRemoveAttendee(String attendee) throws HarnessException {
		this.sClickAt(Locators.RemoveAttendeeButton, "");
		SleepUtil.sleepSmall();
		//this.sClickAt(Locators.ConfirmationDialogYesButton, "");
		//SleepUtil.sleepSmall();
	}

	public void zRemoveLocation(String location) throws HarnessException {
		this.zRightClickAt("css=td[id$='_location'] span:contains('" + location
				+ "')", "");
		this.zClickAt(Locators.DeleteZimletContextMenu, "");
		this.zClickAt("css=input[id$='_location_input']", "");
		SleepUtil.sleepMedium();
	}
	
	public void zVerifyDisabledControlInProposeNewTimeUI() throws HarnessException {
		SleepUtil.sleepMedium(); // opening appt takes some time so assert fails
		ZAssert.assertTrue(this.sIsElementPresent(Locators.ToDisabled), "Verify to is disabled while attendee propose new time");
		ZAssert.assertTrue(this.sIsElementPresent(Locators.SubjectDisabled), "Verify subject is disabled while attendee propose new time");		
		ZAssert.assertTrue(this.sIsElementPresent(Locators.AttendeesDisabled), "Verify attendees is disabled while attendee propose new time");
		ZAssert.assertTrue(this.sIsElementPresent(Locators.OptionalDisabled), "Verify optional is disabled while attendee propose new time");
		ZAssert.assertTrue(this.sIsElementPresent(Locators.LocationDisabled), "Verify location is disabled while attendee propose new time");
		ZAssert.assertTrue(this.sIsElementPresent(Locators.EquipmentDisabled), "Verify equipment is disabled while attendee propose new time");
		ZAssert.assertTrue(this.sIsElementPresent(Locators.DisplayDisabled), "Verify display is disabled while attendee propose new time");
		ZAssert.assertTrue(this.sIsElementPresent(Locators.FolderDisabled), "Verify folder is disabled while attendee propose new time");
		ZAssert.assertTrue(this.sIsElementPresent(Locators.PrivateDisabled), "Verify private is disabled while attendee propose new time");
	}
	
	public void zVerifyDisabledControlInOpenInstance() throws HarnessException {
		ZAssert.assertTrue(this.sIsElementPresent(Locators.RepeatOptionsDisabled), "Verify repeat dropdown remains disabled");
		ZAssert.assertTrue(this.sIsElementPresent(Locators.RepeatDescriptionDisabled), "Verify repeat description remains disabled");
	}
	
	public void zAddAttendees(String attendee) throws HarnessException {
		this.sClickAt(Locators.AddAttendeeButton, "0,0");
		SleepUtil.sleepSmall();
		this.sFocus(Locators.AttendeesField2);
		this.zKeyboard.zTypeCharacters(attendee);
	}
	
	/**
	 * Press the toolbar button
	 * 
	 * @param button
	 * @return
	 * @throws HarnessException
	 */

	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		SleepUtil.sleepSmall();

		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Click button " + button);

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		AbsPage page = null;
		String locator = null;

		if (button == Button.B_SAVE) {

			locator = Locators.SaveButton;

			// Click on send
			this.zClick(locator);

			this.zWaitForBusyOverlay();

			// Wait for the message to be delivered
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
			
			SleepUtil.sleepMedium();

			return (page);

		} else if (button == Button.B_CANCEL) {

			locator = Locators.CancelButton;
			page = null;

			

		} else if (button == Button.B_SUGGESTATIME) {

			locator = Locators.SuggestAtimeLink;
			SleepUtil.sleepMedium();
			page = null;

			

		} else if (button == Button.B_10AM) {

			SleepUtil.sleepMedium();
			locator = Locators.SuggestATime10AM;
			
			if (this.sIsElementPresent(locator) == false) {
				this.sClickAt(Locators.ShowTimesAnywayLink, "");
				return null;
			}

			

		} else if (button == Button.B_SUGGESTALOCATION) {

			locator = Locators.SuggestALocationLink;
			SleepUtil.sleepMedium();
			page = null;

			

		} else if (button == Button.B_FIRST_TIME_SUGGESTION) {
			
			SleepUtil.sleepMedium();
			locator = Locators.SelectFirstFreeTimeFromSuggestTimePane;
			
			if (this.sIsElementPresent(locator) == false) {
				this.sClickAt(Locators.ShowTimesAnywayLink, "");
				return null;
			
			}
			
			page = null;

		} else if (button == Button.B_SHOW) {

			locator = Locators.ShowSchedulerLink;
			page = null;

			this.sClick(locator);

			this.zWaitForBusyOverlay();

			return (page);
		
		} else if (button == Button.B_SHOW_TIMES_ANYWAY) {

			locator = Locators.ShowTimesAnywayLink;
			page = null;

			if (this.sIsElementPresent(locator) == true) {
				this.sClickAt(Locators.ShowTimesAnywayLink, "");
				return null;
			}

			

		} else if (button == Button.B_LOCATION) {

			locator = Locators.AddLocation;
			this.sClickAt(locator, "");

			this.zWaitForBusyOverlay();
			page = null;
			return (page);

			

		} else if (button == Button.B_EQUIPMENT) {

			locator = Locators.addEquipment;
			this.sClickAt(locator, "");

			this.zWaitForBusyOverlay();
			page = null;
			return (page);

			

		} 
		else if (button == Button.B_TO) {

			locator = Locators.AddAttendees;
			this.sClickAt(locator, "");

			this.zWaitForBusyOverlay();
			page = null;
			return (page);

			

		} else if (button == Button.B_OPTIONAL) {

			locator = Locators.AddOptiponalAttendees;
			this.sClickAt(locator, "");

			this.zWaitForBusyOverlay();
			page = null;
			return (page);

			

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		// Make sure a locator was set
		if (locator == null)
			throw new HarnessException("locator was null for button " + button);

		// Default behavior, process the locator by clicking on it
		//

		// Click it
		this.sClickAt(locator, "");

		// if the app is busy, wait for it to become active again
		this.zWaitForBusyOverlay();

		// Return the page, if specified
		SleepUtil.sleepSmall();
		return (page);

	}

	public AbsPage zPressButton(Button button, String value)
			throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");
		SleepUtil.sleepMedium();

		tracer.trace("Click button " + button);

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		// Fallthrough objects
		AbsPage page = null;
		String locator = null;

		if (button == Button.B_SUGGESTEDLOCATION) {

			locator = zGetSuggestedLocation(value);
			page = null;

		} else if (button == Button.B_LOCATIONMENU) {

			locator = zGetLocationVaueFromPopUp(value);
			page = null;

			

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null)
			throw new HarnessException("locator was null for button " + button);

		this.sClickAt(locator, "");
		SleepUtil.sleepMedium(); // Let location bubble gets ready and adds
									// value in field

		this.zWaitForBusyOverlay();

		return (page);

	}

	public void zVerifySpecificTimeNotExists(String time)
			throws HarnessException {
		PageCalendar pageCal = new PageCalendar(MyApplication);
		String[] timeArray = time.split(",");
		for (int i = 0; i <= timeArray.length - 1; i++) {
			ZAssert
					.assertEquals(
							false,
							pageCal
									.sIsElementPresent("css=div[id$='_suggest_view'] td:contains('"+ timeArray[i] + "')"),
							"Verify busy timeslots are not showing while suggesting a time");
		}
	}

	public void zVerifySpecificTimeExists(String time) throws HarnessException {
		PageCalendar pageCal = new PageCalendar(MyApplication);
		String[] timeArray = time.split(",");
		for (int i = 0; i <= timeArray.length - 1; i++) {
			ZAssert
					.assertEquals(
							true,
							pageCal
									.sIsElementPresent("css=div[id$='_suggest_view'] td:contains('" + timeArray[i] + "')"),
							"Verify free timeslots are showing while suggesting a time");
		}
	}

	public void zVerifySpecificLocationNotExists(String location)
			throws HarnessException {
		PageCalendar pageCal = new PageCalendar(MyApplication);
		String[] locationArray = location.split(",");
		for (int i = 0; i <= locationArray.length - 1; i++) {
			ZAssert
					.assertEquals(
							false,
							pageCal
									.sIsElementPresent("css=div[id$='_suggest_view'] td:contains('"+ locationArray[i] + "')"),
							"Verify busy timeslot are not showing while suggesting a time");
		}
	}
	
	public void zSetTomorrowDate() throws HarnessException{
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.DATE, 2);
	    this.zTypeKeys("css=input[id$='_startDateField']", new SimpleDateFormat("MM/dd/yyyy").format(c.getTime()));
	}
	
	public String zGetTomorrowDate() throws HarnessException{
		Calendar c = Calendar.getInstance();
	    c.add(Calendar.DATE, 2);	    
	    return new SimpleDateFormat("yyyyMMdd").format(c.getTime());
	}

	/**
	 * Press the toolbar pulldown and the menu option
	 * 
	 * @param pulldown
	 * @param option
	 * @return
	 * @throws HarnessException
	 */
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option)
			throws HarnessException {
		
		logger.info(myPageName() + " zToolbarPressPulldown(" + pulldown + ", "
				+ option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		
		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		if (pulldown == Button.B_OPTIONS) {
			pulldownLocator = Locators.OptionsDropdown;

		} else {
			throw new HarnessException("no logic defined for pulldown "
					+ pulldown);
		}
		
		if (option == Button.O_SUGGESTION_PREFERENCES) {
			optionLocator = Locators.SuggestionPreferencesMenu;

		} else {
			throw new HarnessException("no logic defined for option "
					+ pulldown);
		}

		// Default behavior
		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option "
						+ option + " pulldownLocator " + pulldownLocator
						+ " not present!");
			}

			this.sClickAt(pulldownLocator, "");

			this.zWaitForBusyOverlay();

			if (optionLocator != null) {
				
				if (option == Button.O_SUGGESTION_PREFERENCES) {
					optionLocator = Locators.SuggestionPreferencesMenu;

				} else {
					throw new HarnessException("no logic defined for pulldown "
							+ pulldown);
				}

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown
							+ " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}

				this.sClickAt(optionLocator, "");

				this.zWaitForBusyOverlay();

			}


		}

		// Return the specified page, or null if not set
		return (page);
	}

	public void zFillField(Field field, String value) throws HarnessException {

		tracer.trace("Set " + field + " to " + value);

		String locator = null;
		String isRepeat = null;

		// subject
		if (field == Field.Subject) {

			locator = Locators.SubjectField;

		// attendees
		} else if (field == Field.Attendees) {

			locator = Locators.AttendeesField;

		// location
		} else if (field == Field.Location) {

			locator = Locators.LocationField;
			
		// start time
		} else if (field == Field.StartTime) {

			locator = Locators.StartTime;
			
			this.sClickAt(locator, "");
			String[] valueSplit = value.split(":");
			String[] valueSplit1 = value.split(" ");
			if (valueSplit[0].substring(0, 1).equalsIgnoreCase("0")) {
				this.sClickAt("css=div[id^='ext-timepicker-1'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + valueSplit[0].replace("0", "") + "')", "");
			} else {
				this.sClickAt("css=div[id^='ext-timepicker-1'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + valueSplit[0] + "')", "");
			}
			this.sClickAt("css=div[id^='ext-timepicker-1'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + valueSplit1[0].substring(3) + "')", "");
			this.sClickAt("css=div[id^='ext-timepicker-1'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + valueSplit1[1] + "')", "");
			
			this.sClickAt("css=div[id^='ext-timepicker-1'] div[id^='ext-button'] span[class='x-button-label']:contains('Done')", "");
		
		// end time
		} else if (field == Field.EndTime) {

			locator = Locators.EndTime;
			
			this.sClickAt(locator, "");
			String[] valueSplit = value.split(":");
			String[] valueSplit1 = value.split(" ");
			if (valueSplit[0].substring(0, 1).equalsIgnoreCase("0")) {
				this.sClickAt("css=div[id^='ext-timepicker-1'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + valueSplit[0].replace("0", "") + "')", "");
			} else {
				this.sClickAt("css=div[id^='ext-timepicker-1'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + valueSplit[0] + "')", "");
			}
			this.sClickAt("css=div[id^='ext-timepicker-1'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + valueSplit1[0].substring(3) + "')", "");
			this.sClickAt("css=div[id^='ext-timepicker-1'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + valueSplit1[1] + "')", "");
			
			this.sClickAt("css=div[id^='ext-timepicker-1'] div[id^='ext-button'] span[class='x-button-label']:contains('Done')", "");

		// start date
		} else if (field == Field.StartDate) {
			
			String index = null;
			
			locator = Locators.StartDate;
			
			this.sClickAt(locator, "");
			
			if (value.contains("/")) {
				String[] valueSplit = value.split("/");
				
				if (this.sIsElementPresent("css=div[id^='ext-datepicker-3'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + zGetCurrentMonth(valueSplit[0]) + "')")) {
					index = "3";
				} else {
					index = "1";
				}

				this.sClickAt("css=div[id^='ext-datepicker-" + index + "'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + zGetCurrentMonth(valueSplit[0]) + "')", "");
				if (valueSplit[1].substring(0, 1).equalsIgnoreCase("0")) {
					this.sClickAt("css=div[id^='ext-datepicker-" + index + "'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + valueSplit[1].replace("0", "") + "')", "");
				} else {
					this.sClickAt("css=div[id^='ext-datepicker-" + index + "'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + valueSplit[1] + "')", "");
				}
				this.sClickAt("css=div[id^='ext-datepicker-" + index + "'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + valueSplit[2] + "')", "");
			} else {
				
				if (this.sIsElementPresent("css=div[id^='ext-datepicker-3'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + value + "')") == true) {
					index = "3";
				} else {
					index = "1";
				}
				
				this.sClickAt("css=div[id^='ext-datepicker-" + index + "'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + value + "')", "");
			}
			
			if (this.sIsElementPresent("css=div[id^='ext-datepicker-3'] div[id^='ext-button'] span[class='x-button-label']:contains('Done')") == true) {
				index = "3";
			} else {
				index = "1";
			}
			
			this.sClickAt("css=div[id^='ext-datepicker-" + index + "'] div[id^='ext-button'] span[class='x-button-label']:contains('Done')", "");
			
		// end date
		} else if (field == Field.EndDate) {
			
			String index = null;

			locator = Locators.EndDate;
			
			this.sClickAt(locator, "");
			
			if (value.contains("/")) {
				String[] valueSplit = value.split("/");
				
				if (this.sIsElementPresent("css=div[id^='ext-datepicker-4'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + zGetCurrentMonth(valueSplit[0]) + "')")) {
					index = "4";
				} else {
					index = "2";
				}
				
				this.sClickAt("css=div[id^='ext-datepicker-" + index + "'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + zGetCurrentMonth(valueSplit[0]) + "')", "");
				if (valueSplit[1].substring(0, 1).equalsIgnoreCase("0")) {
					this.sClickAt("css=div[id^='ext-datepicker-" + index + "'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + valueSplit[1].replace("0", "") + "')", "");
				} else {
					this.sClickAt("css=div[id^='ext-datepicker-" + index + "'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + valueSplit[1] + "')", "");
				}
				this.sClickAt("css=div[id^='ext-datepicker-" + index + "'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + valueSplit[2] + "')", "");
			} else {
				
				if (this.sIsElementPresent("css=div[id^='ext-datepicker-4'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + value + "')") == true) {
					index = "4";
				} else {
					index = "2";
				}
				
				this.sClickAt("css=div[id^='ext-datepicker-" + index + "'] div[id^='ext-pickerslot'] div[class^='x-picker-item']:contains('" + value + "')", "");
			}
			
			if (this.sIsElementPresent("css=div[id^='ext-datepicker-4'] div[id^='ext-button'] span[class='x-button-label']:contains('Done')") == true) {
				index = "4";
			} else {
				index = "2";
			}
			
			this.sClickAt("css=div[id^='ext-datepicker-" + index + "'] div[id^='ext-button'] span[class='x-button-label']:contains('Done')", "");

		// repeat	
		} else if (field == Field.Repeat) {

			isRepeat = value;
			locator = Locators.NoneButton;
			
		// calendar folder
		} else if (field == Field.CalendarFolder) {

			locator = Locators.CalendarFolder;
			
			return;

		// display
		} else if (field == Field.Display) {

			locator = Locators.DisplayOptions;
		
		// body
		} else if (field == Field.Body) {
			webDriver = ClientSessionFactory.session().webDriver();
			if (webDriver instanceof JavascriptExecutor) {
				((JavascriptExecutor) webDriver).executeScript("return document.querySelector('div[contenteditable]').innerHTML = '" + value + "';");
			}
			return;
		} else {
			throw new HarnessException("not implemented for field " + field);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for field " + field);
		}

		// Make sure the button exists
		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Field is not present field=" + field
					+ " locator=" + locator);

		if (isRepeat != null) {
			this.sClickAt(locator, "");
			zRepeat(locator);
		} else {
			this.sClickAt(locator, "");
			this.clearField(locator);
			this.sClickAt(locator, "");
			this.sType(locator, value);
			SleepUtil.sleepSmall();

			if (field == Field.Attendees) {
				this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_TAB);
			}
		}
		this.zWaitForBusyOverlay();
	}
	
	public String zGetCurrentMonth (String valueSplit) throws HarnessException {
		
		if (valueSplit.equals("01")) {
			return "January";
		} else if (valueSplit.equals("02")) {
			return "February";
		} else if (valueSplit.equals("03")) {
			return "March";
		} else if (valueSplit.equals("04")) {
			return "April";
		} else if (valueSplit.equals("05")) {
			return "May";
		} else if (valueSplit.equals("06")) {
			return "June";
		} else if (valueSplit.equals("07")) {
			return "July";
		} else if (valueSplit.equals("08")) {
			return "August";
		} else if (valueSplit.equals("09")) {
			return "September";
		} else if (valueSplit.equals("10")) {
			return "October";
		} else if (valueSplit.equals("11")) {
			return "November";
		} else {
			return "December";
		}
	}
	
	@Override
	public void zFill(IItem item) throws HarnessException {
		logger.info(myPageName() + ".zFill(ZimbraItem)");
		logger.info(item.prettyPrint());

		// Make sure the item is a MailItem
		if (!(item instanceof AppointmentItem)) {
			throw new HarnessException(
					"Invalid item type - must be AppointmentItem");
		}

		AppointmentItem appt = (AppointmentItem) item;

		// Subject
		if (appt.getSubject() != null) {
			zFillField(Field.Subject, appt.getSubject());
		}

		// Location
		if (appt.getLocation() != null) {
			zFillField(Field.Location, appt.getLocation());
			SleepUtil.sleepSmall();
			this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		}
		
		// Attendees
		if (appt.getAttendees() != null) {
			sClickAt(Locators.AddAttendeeButton, "0,0");
			zFillField(Field.Attendees, appt.getAttendees());
			SleepUtil.sleepSmall();
		}
		
		// Start date-time
		if (appt.getStartTime() != null) {
			zFillField(Field.StartDate, appt.getStartTime());
			zFillField(Field.StartTime, appt.getStartTime());
		}

		// End date-time
		if (appt.getEndTime() != null) {
			zFillField(Field.EndDate, appt.getEndTime());
			zFillField(Field.EndTime, appt.getEndTime());
		}

		// Calendar folder
		if (appt.getFolder() != null) {
			zFillField(Field.CalendarFolder, appt.getFolder());
		}

		// Is recurring
		if (appt.getRecurring() != null) {
			zFillField(Field.Repeat, appt.getRecurring());
		}

		// Is all day
		if (appt.getIsAllDay() == true) {
			zFillField(Field.AllDay);
		}

		// Is private
		if (appt.getIsPrivate() == true) {
			String locator = "css=input[id$='_privateCheckbox']";
			 this.sCheck(locator);
			 this.zWaitForBusyOverlay();
		}
		
		// Body
		if (appt.getContent() != null) {
			zFillField(Field.Body, appt.getContent());
		}

	}
	
	public void zSelectAddressUsingAutoComplete(Field field, String value) throws HarnessException {
		String locator = Locators.AttendeesField;
		this.sClickAt(Locators.AddAttendeeButton, "0,0");
		SleepUtil.sleepSmall();
		this.sClickAt(locator, "0,0");
		SleepUtil.sleepSmall();
		this.zKeyboard.zTypeCharacters(value.split("@")[0]);
		SleepUtil.sleepVeryLong();
		boolean exists = this.sIsElementPresent("css=span[class='zcs-auto-complete-email']:contains(" + value + ")");
		System.out.println(exists);
		this.sClickAt("css=div[id^='ext-listitem'] span[class='zcs-auto-complete-email']:contains(" + value + ")", "0,0");
	}
	
	public void zFillField(Field field, ZDate value) throws HarnessException {
		String stringFormat;

		if (field == Field.StartDate || field == Field.EndDate) {
			stringFormat = value.toMM_DD_YYYY();
		} else if (field == Field.StartTime || field == Field.EndTime) {
			stringFormat = value.tohh_mm_aa();
		} else {
			throw new HarnessException(
					"zFillField() not implemented for field: " + field);
		}

		zFillField(field, stringFormat);
	}

	public void zFillField(Field field) throws HarnessException {

		tracer.trace("Set " + field);

		String locator = null;

		// all day
		if (field == Field.AllDay) {

			locator = "css=div[id='ext-slider-1'] div[id='ext-thumb-1'] div";

			// repeat
		} else if (field == Field.Private) {

			locator = "css=input[id$='_privateCheckbox']";

		} else {
			throw new HarnessException("not implemented for field " + field);
		}

		// Make sure the element exists
		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Field is not present field=" + field
					+ " locator=" + locator);

		this.sClickAt(locator, "");

		this.zWaitForBusyOverlay();

	}

	public void zRepeat(String recurringType) throws HarnessException {
		
		logger.info(myPageName() + " zRepeat(recurringType, " + recurringType + ")");
		
		if (recurringType.equals(null)) {
			throw new HarnessException("Repeat options can't be null!");
		}
		
		SleepUtil.sleepSmall();
		this.sClickAt(Locators.NoneButton, "");
		
		if (recurringType.equals("EVERY_DAY")) {
			this.sClickAt(Locators.EveryDayMenuItem,"");
			
		} else if (recurringType.equals("EVERY_WEEK")) {
			this.sClickAt(Locators.EveryWeekMenuItem,"");
			
		} else if (recurringType.equals("EVERY_MONTH")) {
			this.sClickAt(Locators.EveryMonthMenuItem,"");
			
		} else if (recurringType.equals("EVERY_YEAR")) {
			this.sClickAt(Locators.EveryYearMenuItem,"");
		}
		
		SleepUtil.sleepSmall();
	}
	
	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = "css=div[class='x-container x-sheet zcs-appt-form x-floating x-paint-monitored x-sized'] div[class='x-innerhtml']:contains('Event')";

		if (!this.sIsElementPresent(locator)) {
			return (false);
		}

		if (!this.zIsVisiblePerPosition(locator, 150, 75)) {
			return (false);
		}

		SleepUtil.sleep(5000);

		logger.info(myPageName() + " zIsActive() = true");
		return (true);
	}

	public String zGetApptSubject(String subject) throws HarnessException {
		return this.sGetText("css=td[id*='_subject']:contains('" + subject
				+ "')");
	}

	public String zGetApptAttendees(String attendee) throws HarnessException {
		return this.sGetText("css=td[id*='_person']:contains('" + attendee
				+ "')");
	}

	public String zGetApptOptional(String optional) throws HarnessException {
		return this.sGetText("css=td[id*='_optional']:contains('" + optional
				+ "')");
	}

	public String zGetApptLocation(String location) throws HarnessException {
		return this.sGetText("css=td[id*='_location']:contains('" + location
				+ "')");
	}

	public String zGetApptLocationFloating(String location)
			throws HarnessException {
		return this.sGetValue("css=input[id$='_location_input']");
	}

	public String zGetApptEquipment(String equipment) throws HarnessException {
		return this.sGetText("css=td[id*='_resourcesData']:contains('"
				+ equipment + "')");
	}

}
