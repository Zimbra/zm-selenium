/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.touch.ui.calendar;

import java.util.*;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.touch.ui.*;

public class PageCalendar extends AbsTab {
	
	public boolean activeFlag = false;

	public static class Locators {
		
		public static final String zNavigationButton	= "css=span[class='x-button-icon x-shown organizer']";
		
		// Buttons
		public static final String NewButton 			= "css=div[id^='ext-button'] span[class='x-button-icon x-shown add']";
		public static final String SaveButton 			= "css=div[id^='ext-button'] span[class='x-button-label']:contains('Save')";
		public static final String CancelButton 		= "css=div[id^='ext-button'] span[class='x-button-label']:contains('Cancel')";
		public static final String BackButton 			= "css=span[class='x-button-icon x-shown back']";
		public static final String AddAttendeeButton 	= "css=div[class='form-add-field-button-text']:contains('Add Attendee')";
		public static final String RemoveAttendeeButton = "css=span[class='x-button-icon x-shown contactFormMinus']";
		public static final String EditButton 			= "css=div[id='editAppt'] span[class='x-button-icon x-shown edit']";
		public static final String DeleteButton 		= "css=div[id='deleteAppt'] span[class='x-button-icon x-shown trash']";
		public static final String CalendarFolder 		= "css=div[class='zcs-menu-label']:contains('Calendar')";
		public static final String TrashFolder 			= "css=div[class='zcs-menu-label']:contains('Trash')";
		public static final String DayButton 			= "css=span[class='x-button-label']:contains('Day')";
		public static final String MonthButton 			= "css=span[class='x-button-label']:contains('Month')";
		public static final String LocationButton 		= "css=span[class='x-button-icon x-shown organizer']";
		public static final String PreviosButton 		= "css=div[id^='ext-element'] table th[class='goto-prev style=']";
		public static final String NextButton 			= "css=div[id^='ext-element'] table th[class='goto-next style=']";
		public static final String ActionsButton 		= "css=div[id='inviteActionsAppt'] span[class='x-button-icon x-shown arrow_down']";
		public static final String OKButton 			= "css=span[class='x-button-label']:contains('OK')";
		public static final String PermissionDeniedMsg	= "css=div[id='ext-sheet-1'] div[class='x-innerhtml']:contains('Permission denied.')";
		
		// Menus
		public static final String OpenInstanceMenu 	= "css=div[id^='ext-button'] span[class='x-button-label']:contains('Open Instance')";
		public static final String OpenSeriesMenu 		= "css=div[class^='x-button-normal'] span[class='x-button-label']:contains('Open Series')";
		public static final String AcceptMenu 			= "css=div[id='ext-listitem-1'] div:contains('Accept')";
		public static final String TentativeMenu 		= "css=div[id='ext-listitem-2'] div:contains('Tentative')";
		public static final String DeclineMenu 			= "css=div[id='ext-listitem-3'] div:contains('Decline')";
		
		public static final String OpenMenu = "id=VIEW_APPOINTMENT_title";
		public static final String PrintMenu = "css=div[id='zm__Calendar'] tr[id='POPUP_PRINT']";		
		public static final String EditReplyMenu = "id=INVITE_REPLY_MENU_title";
		public static final String EditReplyAcceptSubMenu = "id=EDIT_REPLY_ACCEPT_title";
		public static final String EditReplyTentativeSubMenu = "id=EDIT_REPLY_TENTATIVE_title";
		public static final String EditReplyDeclineSubMenu = "id=EDIT_REPLY_DECLINE_title";
		public static final String ProposeNewTimeMenu = "id=PROPOSE_NEW_TIME_title";
		public static final String CreateACopyMenu = "id=DUPLICATE_APPT_title";
		public static final String ReplyMenu = "css=div[id='zm__Calendar'] tr[id='POPUP_REPLY']";
		public static final String ReplyToAllMenu = "css=div[id='zm__Calendar'] tr[id='POPUP_REPLY_ALL']";
		public static final String ForwardMenu = "css=div[id='zm__Calendar'] tr[id='POPUP_FORWARD_APPT']";
		public static final String DeleteMenu = "css=div[id='zm__Calendar'] tr[id='POPUP_DELETE']";
		public static final String CancelMenu = "css=div#zm__Calendar div#DELETE td[id$='_title']";
		public static final String MoveMenu = "css=div[id^='zm__Calendar__'] td[id^='MOVE__DWT'][id$='_title']";
		public static final String TagAppointmentMenu = "css=div[id='zm__Calendar'] tr[id='POPUP_TAG_MENU']";
		public static final String TagAppointmentNewTagSubMenu = "id=calendar_newtag_title";
		public static final String TagAppointmentRemoveTagSubMenu = "css=div[id^='TAG_MENU'] div[id^='calendar_removetag'] td[id^='calendar_removetag'][class='ZWidgetTitle']";
		public static final String ShowOriginalMenu = "css=div[id='zm__Calendar'] tr[id^='POPUP_SHOW_ORIG']";
		public static final String ShowOriginalMenuOrg = "css=div[id='zm__Calendar'] tr[id='POPUP_SHOW_ORIG']";
		public static final String QuickCommandsMenu = "css=div[id='zm__Calendar'] tr[id='POPUP_QUICK_COMMANDS']";
		
		public static final String InstanceMenu = "id=VIEW_APPT_INSTANCE_title";
		public static final String SeriesMenu = "id=VIEW_APPT_SERIES_title";
		
		public static final String AcceptRecurringMenu = "css=div[id^='zm__Calendar__DWT'] td[id^='REPLY_ACCEPT'][id$='title']";
		public static final String DeclineRecurringMenu = "css=div[id^='zm__Calendar__DWT'] td[id^='REPLY_DECLINE'][id$='title']";
		public static final String CreateACopyRecurringMenu = "css=div[id^='zm__Calendar__DWT'] td[id^='DUPLICATE_APPT'][id$='title']";
		public static final String ForwardInstanceMenu = "id=FORWARD_APPT_INSTANCE_title";
		public static final String ForwardSeriesMenu = "id=FORWARD_APPT_SERIES_title";
		public static final String DeleteInstanceMenu = "id=DELETE_INSTANCE_title";
		public static final String DeleteSeriesMenu = "id=DELETE_SERIES_title";
		
		public static final String NewAppointmentMenu = "id=NEW_APPT_title";
		public static final String NewAllDayAppointmentMenu = "id=NEW_ALLDAY_APPT_title";
		public static final String GoToTodayMenu = "id=TODAY_title";
		public static final String ViewMenu = "id=CAL_VIEW_MENU_title";
		public static final String ViewDaySubMenu = "id=DAY_VIEW_title";
		public static final String ViewWorkWeekSubMenu = "id=WORK_WEEK_VIEW_title";
		public static final String ViewWeekSubMenu = "id=WEEK_VIEW_title";
		public static final String ViewMonthSubMenu = "id=MONTH_VIEW_title";
		public static final String ViewListSubMenu = "id=CAL_LIST_VIEW_title";
		public static final String ViewScheduleSubMenu = "id=SCHEDULE_VIEW_title";

		public static final String SendCancellationButton = "id=CNF_DEL_SENDEDIT_button4_title";
		public static final String EditMessageButton = "id=CNF_DEL_SENDEDIT_button5_title";
		public static final String CancelButton_ConfirmDelete = "id=CNF_DEL_SENDEDIT_button1_title";
		public static final String ForwardToTextArea = "css=input[id='APPT_COMPOSE_1_to_control_input']";
		
		public static final String NeedsActionButton_ViewAppt = "css=div[id^='DWT'] td[id$='_responseActionSelectCell'] td[id$='_select_container'] td[id$='_title']";
		public static final String NeedsActionValue_ViewAppt = "css=td[id$='_responseActionSelectCell'] td[id$='_select_container'] td[id$='_title']";
		public static final String NeedsActionMenu_ViewAppt = "css=div[id*='_Menu_'] div[id^='AC'] td[id^='AC']:contains('Needs Action')";
		public static final String AcceptedMenu_ViewAppt = "css=div[id*='_Menu_'] div[id^='AC'] td[id^='AC']:contains('Accepted')";
		public static final String TentativeMenu_ViewAppt = "css=div[id*='_Menu_'] div[id^='TE'] td[id^='TE']:contains('Tentative')";
		public static final String DeclinedMenu_ViewAppt = "css=div[id*='_Menu_'] div[id^='DE'] td[id^='DE']:contains('Declined')";
		public static final String DeclinedMenu3_ViewAppt = "css=div[id$='_Menu_3'] div[id^='DE_3'] td[id='DE_3_title']:contains('Declined')";
		public static final String TagButton_ViewAppt = "css=div[id^='ztb__APPTRO'] td[id$='TAG_MENU_dropdown']";
		public static final String NewTagMenu_ViewAppt = "css=div[id$='TAG_MENU|MENU'] td[id$='TAG_MENU|MENU|NEWTAG_title']";
		public static final String RemoveTagMenu_ViewAppt = "css=div[id$='TAG_MENU|MENU'] td[id$='TAG_MENU|MENU|REMOVETAG_title']";
		public static final String ActionsButton_ViewAppt = "css=div[id^='ztb__APPTRO'] td[id$='ACTIONS_MENU_title']";
		public static final String EditMenu_ViewAppt = "css=div[id^='zm__APPTRO'] td[id^='EDIT_title']";
		public static final String CreateACopyMenu_ViewAppt = "css=div[id^='zm__APPTRO'] td[id^='DUPLICATE_APPT_title']";
		public static final String ReplyMenu_ViewAppt = "css=div[id^='zm__APPTRO'] td[id^='REPLY'][id$='_title']";
		public static final String ReplyToAllMenu_ViewAppt = "css=div[id^='zm__APPTRO'] td[id^='REPLY_ALL'][id$='_title']";
		public static final String ForwardMenu_ViewAppt = "css=div[id^='zm__APPTRO'] td[id^='FORWARD_APPT_title']";
		public static final String ProposeNewTimeMenu_ViewAppt = "css=div[id^='zm__APPTRO'] td[id^='PROPOSE_NEW_TIME_title']";
		public static final String DeleteMenu_ViewAppt = "css=div[id^='zm__APPTRO'] td[id^='DELETE_title']";
		public static final String ShowOriginalMenu_ViewAppt = "css=div[id^='zm__APPTRO'] td[id^='SHOW_ORIG'][id$='_title']";
		
		// Radio buttons
		public static final String OpenThisInstanceRadioButton = "css=td input[id*='_defaultRadio']";
		public static final String OpenTheSeriesRadioButton = "css=td input[id$='_openSeries']";
		public static final String DeleteThisInstanceRadioButton = "css=td input[id*='_defaultRadio']";
		public static final String DeleteTheSeriesRadioButton = "css=td input[id$='_openSeries']";
		
		public static final String CalendarViewListCSS			= "css=div[id='" + "ext-calendar-3" + "']";
		public static final String CalendarViewDayCSS			= "css=div[id='" + "ext-calendar-3" + "']";
		public static final String CalendarViewWeekCSS			= "css=div[id='" + "ext-calendar-3" + "']";
		public static final String CalendarViewWorkWeekCSS		= "css=div[id='" + "ext-calendar-3" + "']";
		public static final String CalendarViewMonthCSS			= "css=div[id='" + "ext-calendar-1" + "']";
		public static final String CalendarViewScheduleCSS		= "css=div[id='" + "ext-calendar-3" + "']";
		public static final String CalendarViewFreeBusyCSS		= "css=div[id='" + "ext-calendar-3" + "']";
		public static final String CalendarWorkWeekViewApptCount= "css=div[id='" + "ext-calendar-3" + "']";
		
		public static final String CalendarViewDayItemCSS		= "css=div[id='" + "ext-calendar-3" + "']";
		public static final String CalendarViewWeekItemCSS		= "css=div[id='" + "ext-calendar-3" + "']";
		public static final String CalendarViewWorkWeekItemCSS	= "css=div[id='" + "ext-calendar-3" + "']";

		// Dialog locators
		public static final String DialogDivID = "CNF_DEL_YESNO";
		public static final String DialogDivCss = "css=div[id='CNF_DEL_YESNO']";
		
		public static final String NextWeek = "css= td[id='zb__CAL__Nav__PAGE_FORWARD_left_icon']";
		public static final String NextMonth = "css= td[id='zb__CAL__Nav__PAGE_FORWARD_left_icon']";
		public static final String TodayButton = "css=td[id='zb__CLD__TODAY_title']";
		public static final String TodayHighlighted = "css=div[class='calendar_heading_day_today']";
		public static final String TodaySelelcted = "css=div[class='calendar_heading_day_today-selected']";	
		
		//move appt
		public static final String MoveToolbar = "css=td[id='zb__CLD__MOVE_MENU_left_icon']";
		public static final String MoveFolderOption = "css=div[class='ZmFolderChooser'] div[class='DwtTreeItemLevel1ChildDiv'] td[class='DwtTreeItem-Text']:contains('";  // append the foldername and close the parenthesis
		public static final String MoveToNewFolderOption = "css=div[id='ZmMoveButton_CAL'] div[id='ZmMoveButton_CAL|NEWFOLDER']";
		public static final String LocationName= "css=div[class='DwtDialog'] div[id$='_content'] table tr td:nth-child(2) input";
		public static final String zAttachmentsLabel= "css= tr[id$='_attachment_container'] fieldset[class='ZmFieldset']:contains('Attachments')";
		
		public static final String NextPage = "css=div[id='zb__CAL__Nav__PAGE_FORWARD'] div[class='ImgRightArrow']";
		public static final String PreviousPage = "css=div[id='zb__CAL__Nav__PAGE_BACK'] div[class='ImgLeftArrow']";
		public static final String ImgPrivateAppt = "css= div[class='ImgReadOnly']";
		
		public static final String LocationFirstSearchResult = "css= div[class='DwtChooserListView'] div[class='DwtListView-Rows'] div";

	}

	public PageCalendar(AbsApplication application) {
		super(application);

		logger.info("new " + PageCalendar.class.getCanonicalName());
	}
	
	public String zGetMoveLocator(String folderName) throws HarnessException {
		return Locators.MoveFolderOption +  folderName + "')";
		
	}
	
	public void zSelectFolder(String folderName) throws HarnessException {
		
		logger.info(myPageName() + " zSelectFolder("+ folderName +")");
		
		if ( folderName == null ) 
			throw new HarnessException("folder must not be null");
		
		String locator = "css=div[class='x-dock x-dock-vertical x-sized'] div[class='zcs-menu-label']:contains('" + folderName + "')";
		
		if ( folderName != "Calendar") {
			this.sRefresh();
			SleepUtil.sleepVeryLong();
			SleepUtil.sleepLong();
		}
		
		// Click to locations to select sub folder
		this.sClickAt(Locators.LocationButton, "");
		SleepUtil.sleepSmall();
		
		this.sClickAt(locator, "");
		SleepUtil.sleepMedium();

	}

	@SuppressWarnings("unused")
	private String getLocatorBySubject(String subject) throws HarnessException {
		int count;
		String locator;

		// Organizer's view
		locator = "css=div[class^='event-bar']:contains('"+ subject +"')";
		count = this.sGetCssCount(locator);
		if ( count == 1 ) {
			return (locator);
		} else if ( count > 1 ) {
			for ( int i = 1; i <= count; i++ ) {
				if ( this.zIsVisiblePerPosition(locator + ":nth-child("+ i +")", 0, 0) ) {
					return (locator + ":nth-child("+ i +")");
				}
			}
		}
		
		throw new HarnessException("Unable to locate appointment!");
		
	}
	
	public String zGetApptLocator(String apptSubject) throws HarnessException {
		SleepUtil.sleepSmall();
		if (sIsElementPresent("css=div[class^='event-bar']:contains('" + apptSubject + "')") == true) {
			return "css=td.appt_name:contains('" + apptSubject + "')";
		} else {
			throw new HarnessException("Unable to locate subject: "+ apptSubject);
		}
	}
	
	public boolean zIsAppointmentExists(String apptSubject) throws HarnessException {
		SleepUtil.sleepMedium();
		if (sIsElementPresent("css=div[class^='event-bar']:contains('" + apptSubject + "')") == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zVerifyPermissionDeniedDialog() throws HarnessException {
		SleepUtil.sleepMedium();
		if (sIsElementPresent(Locators.PermissionDeniedMsg) == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zIsAllDayAppointmentExists(String apptSubject) throws HarnessException {
		SleepUtil.sleepMedium();
		if (sIsElementPresent("css=div[class^='event-bar']:contains('" + apptSubject + "')") == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zVerifyRepeatString(String repeatString) throws HarnessException {
		SleepUtil.sleepSmall();
		if (sIsElementPresent("css=div[class='zcs-apptview-field'] span:contains('" + repeatString + "')") == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zVerifyAppointmentSubjectInViewAppt(String subject) throws HarnessException {
		SleepUtil.sleepSmall();
		if (sIsElementPresent("css=div[class^='zcs-conv-title-bar'] div:contains('" + subject + "')") == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zVerifyAppointmentDateInViewAppt(String date) throws HarnessException {
		SleepUtil.sleepSmall();
		if (sIsElementPresent("css=div[class='view-appt-time'] span:contains('" + date + "')") == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zVerifyAppointmentTimeInViewAppt(String time) throws HarnessException {
		SleepUtil.sleepSmall();
		if (sIsElementPresent("css=div[class='view-appt-time'] span:contains('" + time + "')") == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zVerifyAppointmentLocationInViewAppt(String location) throws HarnessException {
		SleepUtil.sleepSmall();
		if (sIsElementPresent("css=div[class='zcs-apptview-field'] span:contains('" + location + "')") == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zVerifyAppointmentAttendeesLabelInViewAppt() throws HarnessException {
		SleepUtil.sleepSmall();
		if (sIsElementPresent("css=div[class='zcs-apptview-label']:contains('Attendees')") == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zVerifyAppointmentAttendeesInViewAppt(String attendees) throws HarnessException {
		SleepUtil.sleepSmall();
		if (sIsElementPresent("css=div[class='zcs-apptview-field'] span:contains('" + attendees + "')") == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zVerifyAppointmentCalendarLabelInViewAppt() throws HarnessException {
		SleepUtil.sleepSmall();
		if (sIsElementPresent("css=div[class='zcs-apptview-label']:contains('Calendar')") == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zVerifyAppointmentCalendarInViewAppt(String calendar) throws HarnessException {
		SleepUtil.sleepSmall();
		if (sIsElementPresent("css=div[class='zcs-apptview-field'] span:contains('" + calendar + "')") == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zVerifyAppointmentDisplayLabelInViewAppt() throws HarnessException {
		SleepUtil.sleepSmall();
		if (sIsElementPresent("css=div[class='zcs-apptview-label']:contains('Display')") == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zVerifyAppointmentDisplayInViewAppt(String display) throws HarnessException {
		SleepUtil.sleepSmall();
		if (sIsElementPresent("css=div[class='zcs-apptview-field'] span:contains('" + display + "')") == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zVerifyAppointmentReminderLabelInViewAppt() throws HarnessException {
		SleepUtil.sleepSmall();
		if (sIsElementPresent("css=div[class='zcs-apptview-label']:contains('Reminder')") == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zVerifyAppointmentReminderInViewAppt(String reminder) throws HarnessException {
		SleepUtil.sleepSmall();
		if (sIsElementPresent("css=div[class='zcs-apptview-field'] span:contains('" + reminder + "')") == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zVerifyAppointmentBodyInViewAppt(String body) throws HarnessException {
		SleepUtil.sleepSmall();
		this.sSelectFrame("css=iframe[name='ZCSIframe-apptDataNotes']");
		String bodyText = sGetText("css=body"); 
		this.sSelectFrame("relative=top");
		if (bodyText.equals(body)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean zGetViewApptLocator() throws HarnessException {
		return zIsVisiblePerPosition("css=td[id$='_responseActionSelectCell'] td[id$='_select_container']", 5, 5);
	}
	
	public boolean zGetViewApptLocator(String apptSubject) throws HarnessException {
		return sIsElementPresent("css=td.appt_name:contains('" + apptSubject + "')");
	}
	
	public String zGetNeedsActionDropdownValue() throws HarnessException {
		return sGetText("css=td[id$='_responseActionSelectCell'] td[id$='_select_container'] td[id$='_title']");	
	}
	
	public int zGetApptCountWorkWeekView() throws HarnessException {
		return sGetCssCount(Locators.CalendarWorkWeekViewApptCount);
	}
	
	public int zGetApptCountMonthView(String apptSubject) throws HarnessException {
		return sGetCssCount("css=td[class='calendar_month_day_item']");
	}
	public String zGetReadOnlyApptLocator(String apptSubject) throws HarnessException {
		return "css=td.appt_new_name:contains('" + apptSubject + "')";
	}
	
	public String zGetAllDayApptLocator(String apptSubject) throws HarnessException {
		return "css=td.appt_allday_name:contains('" + apptSubject + "')";
	}
	
	public String zGetReadOnlyAllDayApptLocator(String apptSubject) throws HarnessException {
		return "css=td.appt_allday_new_name:contains('" + apptSubject + "')";
	}
	
	public String zGetApptSubjectFromReadOnlyAppt() throws HarnessException {
		return sGetText("css=div[class='MsgHeader'] td[class='SubjectCol']");
	}
	
	public String zGetApptBodyFromReadOnlyAppt() throws HarnessException {
		String bodyValue;
		try {
			this.sSelectFrame("css=iframe[id$='__body__iframe']");
			bodyValue = this.sGetText("css=body");
			return bodyValue;
		} finally {
			this.sSelectFrame("relative=top");
		}
	}
	
	public boolean zVerifyTagBubble(String tagName) throws HarnessException {
		boolean tagNameBubble;
		tagNameBubble = sIsElementPresent("css=span[class='addrBubble TagBubble'] span[class='TagName']:contains('" + tagName + "')");
		if (tagNameBubble == true) {
			return true;
		} else {
			return false;
		}	
	}
	
	public String zGetRecurringLink() throws HarnessException {
		return sGetText("css=div[id$='repeatDesc']");
	}
	
	public boolean zVerifyDisabledControl(Button buttonName) throws HarnessException {
		
		if (buttonName.equals(Button.B_DELETE_DISABLED)) {
			return sIsElementPresent("css=div[id='zb__CLD__DELETE'].ZDisabled");
			
		} else if (buttonName.equals(Button.O_SHARE_CALENDAR_DISABLED)) {
			return sIsElementPresent("css=div[id='ZmActionMenu_calendar_CALENDAR'] div[id='SHARE_CALENDAR'].ZDisabled");	
		
		} else if (buttonName.equals(Button.O_REINVITE_ATTENDEES_DISABLED)) {
			return sIsElementPresent("css=div[id='zm__Calendar'] div[id='REINVITE_ATTENDEES'].ZDisabled");
		} else if (buttonName.equals(Button.O_FORWARD_DISABLED)) {
			return sIsElementPresent("css=div[id='zm__Calendar'] div[id^='FORWARD_APPT'].ZDisabled");
		} else if (buttonName.equals(Button.O_DELETE_DISABLED)) {
			return sIsElementPresent("css=div[id='zm__Calendar'] div[id='DELETE'].ZDisabled");
		} else if (buttonName.equals(Button.O_MOVE_DISABLED)) {
			return sIsElementPresent("css=div[id='zm__Calendar'] div[id^='MOVE'].ZDisabled");
		} else if (buttonName.equals(Button.O_TAG_APPOINTMENT_DISABLED)) {
			return sIsElementPresent("css=div[id='zm__Calendar'] div[id='TAG_MENU'].ZDisabled");
		} else if (buttonName.equals(Button.O_REPLY_DISABLED)) {
			return sIsElementPresent("css=div[id='zm__Calendar'] div[id^='REPLY'].ZDisabled");	
		
		} else if (buttonName.equals(Button.B_TAG_APPOINTMENT_DISABLED_READONLY_APPT)) {
			return sIsElementPresent("css=div[id='ztb__APPTRO-1'] div[id^='zb__APPTRO-1'][id$='TAG_MENU'].ZDisabled");
		} else if (buttonName.equals(Button.B_SAVE_DISABLED_READONLY_APPT)) {
			return sIsElementPresent("css=div[id='ztb__APPTRO-1'] div[id^='zb__APPTRO-1'][id$='SAVE'].ZDisabled");
		} else if (buttonName.equals(Button.B_ACCEPTED_DISABLED_READONLY_APPT)) {
			return sIsElementPresent("css=div[class='ZmMailMsgView'] td[id$='_responseActionSelectCell'] div.ZDisabled");	
			
		} else if (buttonName.equals(Button.O_EDIT_DISABLED_READONLY_APPT)) {
			return sIsElementPresent("css=div[id='zm__APPTRO-1'] div[id='EDIT'].ZDisabled");
		} else if (buttonName.equals(Button.O_FORWARD_DISABLED_READONLY_APPT)) {
			return sIsElementPresent("css=div[id='zm__APPTRO-1'] div[id^='FORWARD_APPT__'].ZDisabled");
		} else if (buttonName.equals(Button.O_PROPOSE_NEW_TIME_DISABLED_READONLY_APPT)) {
			return sIsElementPresent("css=div[id='zm__APPTRO-1'] div[id^='PROPOSE_NEW_TIME__'].ZDisabled");
		} else if (buttonName.equals(Button.O_DELETE_DISABLED_READONLY_APPT)) {
			return sIsElementPresent("css=div[id='zm__APPTRO-1'] div[id^='DELETE__'].ZDisabled");
		} else if (buttonName.equals(Button.O_DELETE_DISABLED_READONLY_APPT)) {
			return sIsElementPresent("css=div[id='zm__APPTRO-1'] div[id^='DELETE__'].ZDisabled");
		
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("unused")
	private AbsPage zListItemListView(Action action, String subject) throws HarnessException {
		logger.info(myPageName() + " zListItemListView("+ action +", "+ subject +")");

		// The default locator points at the subject
		String locator = "css=div[class^='event-bar']:contains('" + subject + "')";
		AbsPage page = null;

		if ( action == Action.A_LEFTCLICK ) {

			// Left-Click on the item
			this.sClickAt(locator,"");
			this.zWaitForBusyOverlay();

			page = null;

		} else {
			throw new HarnessException("implement me!  action = "+ action);
		}

		return (page);
	}
	
	public AbsPage zMouseOver(Button button) throws HarnessException {

		if ( button == null )
			throw new HarnessException("locator cannot be null");

		String locator = null;
		AbsPage page = null;

		if ( button == Button.B_TAGAPPOINTMENTMENU ) {
			
			locator = Locators.TagAppointmentMenu;
			
			this.sMouseOver(locator);
			this.zWaitForBusyOverlay();

			page = null;
			
			return (page);
			
		} else {
			throw new HarnessException("implement me!  button = "+ button);
		}

	}
	
	@Override
	public AbsPage zListItem(Action action, String subject) throws HarnessException {
		logger.info(myPageName() + " zListItem("+ action +", "+ subject +")");
		tracer.trace(action +" on subject = "+ subject);

		if ( this.zIsVisiblePerPosition(Locators.CalendarViewListCSS, 0, 0) ) {
			return (zListItemGeneral(Locators.CalendarViewDayItemCSS, action, subject));		// LIST
		
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewDayCSS, 0, 0) ) {
			return (zListItemGeneral(Locators.CalendarViewDayItemCSS, action, subject));		// DAY
		
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewWorkWeekCSS, 0, 0) ) {
			return (zListItemGeneral(Locators.CalendarViewWorkWeekItemCSS, action, subject));	// WORKWEEK
		
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewWeekCSS, 0, 0) ) {
			return (zListItemGeneral(Locators.CalendarViewWeekItemCSS, action, subject));		// WEEK

		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewMonthCSS, 0, 0) ) {
			return (zListItemMonthView(action, subject));										// MONTH

		} else {
			throw new HarnessException("Unknown calendar view");
		}

	}
	
	private AbsPage zListItemGeneral(String itemsLocator, Action action, String subject) throws HarnessException {

		if ( itemsLocator == null )
			throw new HarnessException("itemsLocator cannot be null");

		if ( action == null )
			throw new HarnessException("action cannot be null");

		if ( subject == null )
			throw new HarnessException("subject cannot be null");


		logger.info(myPageName() + " zListItemGeneral("+ itemsLocator +", "+ action +", "+ subject +")");
		tracer.trace(action +" on subject = "+ subject);

		// Default behavior variables
		String locator = null;
		AbsPage page = null;
		
		SleepUtil.sleepMedium();

		if ( this.sIsElementPresent(itemsLocator + " div[class^='event-bar']:contains('" + subject + "')")) {
			
			// Single occurrence locator
			locator = itemsLocator + " div[class^='event-bar']:contains('" + subject + "')";
		
		} else if ( this.sIsElementPresent(itemsLocator +" div[class^='event-bar']:contains('"+ subject +"')")) {
			
			// All day occurrence locator
			locator = itemsLocator +" div[class^='event-bar']:contains('"+ subject +"')";
		}
		
		// Make sure one of the locators found the appt
		if ( locator == null ) {
			throw new HarnessException("Unable to determine locator for appointment: "+ subject);
		}
		
		if ( action == Action.A_LEFTCLICK ) {
			
			this.sClickAt(locator, "");
			this.zWaitForBusyOverlay();

			page = new FormApptNew(this.MyApplication);
			
		} else {
			throw new HarnessException("implement me!  action = "+ action);
		}

		SleepUtil.sleepMedium();
		
		return (page);
	}

	private AbsPage zListItemMonthView(Action action, String subject) throws HarnessException {
		logger.info(myPageName() + " zListItemMonthView("+ action +", "+ subject +")");

		tracer.trace(action +" on subject = "+ subject);

		if ( action == null )
			throw new HarnessException("action cannot be null");

		if ( subject == null )
			throw new HarnessException("subject cannot be null");

		// Default behavior variables
		String locator = null;
		AbsPage page = null;

		// TODO: need some way to get a locator to all-day and non-all-day appts
		// For now, give pref to non-all-day.  If not present, try all-day
		
// 		locator = "css=td.appt_name:contains('" + subject + "')"; // non-all-day
		locator = "css=table.calendar_month_day_table td.calendar_month_day_item span[id$='_subject']:contains('"+ subject +"')"; // non-all-day
		
		if ( !this.sIsElementPresent(locator) ) {
			// locator = "css=td.appt_allday_name:contains('" + subject + "')"; // all-day
			locator = "css=td.appt_allday_name:contains('" + subject + "')"; // all-day

		}

		if ( action == Action.A_LEFTCLICK ) {
			
			this.zClickAt(locator, "");
			this.zWaitForBusyOverlay();

			page = null;
			
			return (page);
			
		} else if ( action == Action.A_RIGHTCLICK ) {
			
			this.zRightClickAt(locator, "");
			this.zWaitForBusyOverlay();

			page = null;
			
			return (page);
			
		} else if ( action == Action.A_DOUBLECLICK) {
			
			this.sDoubleClick(locator);
			this.zWaitForBusyOverlay();

			page = null; // Should probably return the read-only or organizer view of the appointment
			
			return (page);
			
		} else {
			throw new HarnessException("implement me!  action = "+ action);
		}


	}
	
	@SuppressWarnings("unused")
	private AbsPage zListItemListView(Action action, Button option, String subject) throws HarnessException {
		
		if ( action == null )
			throw new HarnessException("action cannot be null");
		if ( option == null )
			throw new HarnessException("button cannot be null");
		if ( subject == null || subject.trim().length() == 0)
			throw new HarnessException("subject cannot be null or blank");

		logger.info(myPageName() + " zListItemGeneral("+ action +", "+ option +", "+ subject +")");
		tracer.trace(action +" then "+ option +" on subject = "+ subject);

		// Default behavior variables
		String locator = null;
		AbsPage page = null;
		String optionLocator = null;
		boolean waitForPostfix = false;
		
		if ( this.sIsElementPresent("css=div[class^='event-bar']:contains('" + subject + "')")) {
			locator = "css=div[class^='event-bar']:contains('" + subject + "')";

		} else if ( this.sIsElementPresent("css=div[class^='event-bar']:contains('" + subject + "')")) {
			locator = "css=div[class^='event-bar']:contains('" + subject + "')";
		} else {
			throw new HarnessException("Unable to determine locator for appointment: "+ subject);
		}

		if (action == Action.A_LEFTCLICK) {
			
			this.zClickAt(locator, "");
			this.zWaitForBusyOverlay();
			SleepUtil.sleepSmall();
			
			if ( option == Button.O_OPEN_SERIES_MENU ) {
				
				optionLocator = Locators.OpenSeriesMenu;
				this.sClickAt(optionLocator, "");
				
				page = null;
				waitForPostfix = false;
			
			} else if ( option == Button.O_OPEN_INSTANCE_MENU ) {
					
				optionLocator = Locators.OpenInstanceMenu;
				this.sClickAt(optionLocator, "");
				
				page = null;
				waitForPostfix = false;
				
			} else {

				throw new HarnessException("implement action:"+ action +" option:"+ option);
			}
			
		} else {
			throw new HarnessException("implement me!  action = "+ action);
		}

		if (waitForPostfix == true) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}

		return (page);

	}

	@Override
	public AbsPage zListItem(Action action, Button option, String subject) throws HarnessException {
		logger.info(myPageName() + " zListItem("+ action +", "+ option +", "+ subject +")");

		if ( this.zIsVisiblePerPosition(Locators.CalendarViewListCSS, 0, 0) ) {
			return (zListItemGeneral(Locators.CalendarViewDayItemCSS, action, option, subject));		// LIST
		
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewDayCSS, 0, 0) ) {
			return (zListItemGeneral(Locators.CalendarViewDayItemCSS, action, option, subject));		// DAY
		
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewWorkWeekCSS, 0, 0) ) {
			return (zListItemGeneral(Locators.CalendarViewWorkWeekItemCSS, action, option, subject));	// WORKWEEK
		
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewWeekCSS, 0, 0) ) {
			return (zListItemGeneral(Locators.CalendarViewWeekItemCSS, action, option, subject));		// WEEK

		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewMonthCSS, 0, 0) ) {
			return (zListItemMonthView(action, subject));												// MONTH

		} else {
			throw new HarnessException("Unknown calendar view");
		}
	}
	
	private AbsPage zListItemGeneral(String itemsLocator, Action action, Button option, String subject) throws HarnessException {

		if ( itemsLocator == null )
			throw new HarnessException("itemsLocator cannot be null");
		if ( action == null )
			throw new HarnessException("action cannot be null");
		if ( option == null )
			throw new HarnessException("button cannot be null");
		if ( subject == null || subject.trim().length() == 0)
			throw new HarnessException("subject cannot be null or blank");

		logger.info(myPageName() + " zListItemGeneral("+ itemsLocator +", "+ action +", "+ option +", "+ subject +")");
		tracer.trace(action +" then "+ option +" on subject = "+ subject);

		// Default behavior variables
		String locator = null;
		AbsPage page = null;
		String optionLocator = null;
		boolean waitForPostfix = false;
		
		if ( this.sIsElementPresent(itemsLocator +" div[class^='event-bar']:contains('" + subject + "')")) {
			locator = itemsLocator +" div[class^='event-bar']:contains('" + subject + "')";

		} else if ( this.sIsElementPresent(itemsLocator +" div[class^='event-bar']:contains('" + subject + "')")) {
			locator = itemsLocator +" div[class^='event-bar']:contains('" + subject + "')";
			
		} else {
			throw new HarnessException("Unable to determine locator for appointment: "+ subject);
		}

		SleepUtil.sleepLong();
		if (action == Action.A_LEFTCLICK) {
			
			if (sIsElementPresent("css=div[id='ext-calendar-3'] div[class^='event-bar']:contains('" + subject + "')")) {
				locator = "css=div[id='ext-calendar-3'] div[class^='event-bar']:contains('" + subject + "')";
			}
			
			this.sClickAt(locator, "0,0");
			SleepUtil.sleepLong();
			
			if ( option == Button.O_OPEN_SERIES_MENU ) {
				
				optionLocator = Locators.OpenSeriesMenu;
				this.sClickAt(optionLocator, "");
				
				page = new FormApptNew(this.MyApplication);
				waitForPostfix = false;
			
			} else if ( option == Button.O_OPEN_INSTANCE_MENU ) {
					
				optionLocator = Locators.OpenInstanceMenu;
				this.sClickAt(optionLocator, "");
				
				page = new FormApptNew(this.MyApplication);
				waitForPostfix = false;
				
			} else if ( option == Button.B_EDIT ) {
				
				locator = Locators.EditButton;
				this.sClickAt(locator, "");
				SleepUtil.sleepMedium();
				
				if (this.sIsElementPresent("css=div[class^='x-container x-unsized x-msgbox-dark'] span[class='x-button-label']:contains('OK')")) {
					this.sClickAt("css=div[class^='x-container x-unsized x-msgbox-dark'] span[class='x-button-label']:contains('OK')", "");
					SleepUtil.sleepSmall();
				}
				
				page = new FormApptNew(this.MyApplication);
				waitForPostfix = false;
			
			} else if ( option == Button.B_DELETE ) {
				
				locator = Locators.DeleteButton;
				
				if (sIsElementPresent("css=div[id='ext-calendar-3'] div[id='deleteAppt'] span[class='x-button-icon x-shown trash']")) {
					locator = "css=div[id='ext-calendar-3'] div[id='deleteAppt'] span[class='x-button-icon x-shown trash']";
				}
				
				this.sClickAt(locator, "");
								
				waitForPostfix = true;
				
			} else if ( option == Button.O_ACCEPT_MENU ) {
				
				optionLocator = Locators.AcceptMenu;
				
				this.sClickAt(Locators.ActionsButton, "");
				SleepUtil.sleepSmall();
				
				this.sClickAt(optionLocator, "");
				
				page = null;
				
				waitForPostfix = true;
				
			} else if ( option == Button.O_DECLINE_MENU ) {
				
				optionLocator = Locators.DeclineMenu;

				this.sClickAt(Locators.ActionsButton, "");
				SleepUtil.sleepSmall();
				
				this.sClickAt(optionLocator, "");
				
				page = null;
				
				waitForPostfix = true;
				
			} else if ( option == Button.O_TENTATIVE_MENU ) {
				
				optionLocator = Locators.TentativeMenu;

				this.sClickAt(Locators.ActionsButton, "");
				SleepUtil.sleepSmall();
				
				this.sClickAt(optionLocator, "");
				
				page = null;
				
				waitForPostfix = true;
				
			} else {

				throw new HarnessException("implement action:"+ action +" option:"+ option);
			}
			
		} else {
			throw new HarnessException("implement me!  action = "+ action);
		}

		if (waitForPostfix == true) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}

		return (page);
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption, String subject) throws HarnessException {

		logger.info(myPageName() + " zListItem("+ action +", "+ option +", "+ subOption +", "+ subject +")");
		tracer.trace(action +" then "+ option + "," + subOption + " on item = "+ subject);

		if ( action == null )
			throw new HarnessException("action cannot be null");
		if ( option == null || subOption == null )
			throw new HarnessException("button cannot be null");
		if ( subject == null || subject.trim().length() == 0)
			throw new HarnessException("subject cannot be null or blank");

		// Default behavior variables
		String locator = null;
		AbsPage page = null;
		String optionLocator = null;
		String subOptionLocator = null;
		String itemsLocator = null;
		boolean waitForPostfix = false;
		
		if ( this.zIsVisiblePerPosition(Locators.CalendarViewListCSS, 0, 0) ) {
			itemsLocator = Locators.CalendarViewListCSS;									// LIST
			
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewDayCSS, 0, 0) ) {
			itemsLocator = Locators.CalendarViewDayItemCSS;									// DAY
			
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewWorkWeekCSS, 0, 0) ) {
			itemsLocator = Locators.CalendarViewWorkWeekItemCSS;							// WORKWEEK
			
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewWeekCSS, 0, 0) ) {
			itemsLocator = Locators.CalendarViewWeekItemCSS;								// WEEK
			
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewMonthCSS, 0, 0) ) {
			itemsLocator = Locators.CalendarViewMonthCSS;									// MONTH
			
		} else {
			throw new HarnessException("Unknown calendar view");
		}
				
		if ( this.sIsElementPresent(itemsLocator +" div[class^='event-bar']:contains('" + subject + "')")) {
			locator = itemsLocator +" div[class^='event-bar']:contains('" + subject + "')";

		} else if ( this.sIsElementPresent(itemsLocator +" div[class^='event-bar']:contains('" + subject + "')")) {
			locator = itemsLocator +" div[class^='event-bar']:contains('" + subject + "')";
			
		} else {
			throw new HarnessException("Unable to determine locator for appointment: "+ subject);
		}
		
		if (action == Action.A_LEFTCLICK) {
			
			this.sClickAt(locator, "0,0");
			SleepUtil.sleepLong();
			
			if ( option == Button.O_OPEN_SERIES_MENU ) {
				
				optionLocator = Locators.OpenSeriesMenu;
				this.sClickAt(optionLocator, "");
				SleepUtil.sleepLong();
				
				this.sClickAt(Locators.ActionsButton, "");
				SleepUtil.sleepSmall();
				
				if ( subOption == Button.O_ACCEPT_MENU ) {
				
					subOptionLocator = Locators.AcceptMenu;
					this.sClickAt(subOptionLocator, "");
					
				} else if ( subOption == Button.O_DECLINE_MENU ) {
				
					subOptionLocator = Locators.DeclineMenu;
					this.sClickAt(subOptionLocator, "");
					
				} else if ( subOption == Button.O_TENTATIVE_MENU ) {
					
					subOptionLocator = Locators.TentativeMenu;
					this.sClickAt(subOptionLocator, "");
				}
				
				page = null;
				waitForPostfix = true;
			
			} else if ( option == Button.O_OPEN_INSTANCE_MENU ) {
					
				optionLocator = Locators.OpenInstanceMenu;
				this.sClickAt(optionLocator, "");
				SleepUtil.sleepLong();
				
				this.sClickAt(Locators.ActionsButton, "");
				SleepUtil.sleepSmall();
				
				if ( subOption == Button.O_ACCEPT_MENU ) {
					
					subOptionLocator = Locators.AcceptMenu;
					this.sClickAt(subOptionLocator, "");
					
				} else if ( subOption == Button.O_DECLINE_MENU ) {
				
					subOptionLocator = Locators.DeclineMenu;
					this.sClickAt(subOptionLocator, "");
					
				} else if ( subOption == Button.O_TENTATIVE_MENU ) {
					
					subOptionLocator = Locators.TentativeMenu;
					this.sClickAt(subOptionLocator, "");
				}
				
				page = null;
				waitForPostfix = true;
				
			} else {

				throw new HarnessException("implement action:"+ action +" option:"+ option);
			}
			
		} else {
			throw new HarnessException("implement me!  action = "+ action);
		}
		
		if (waitForPostfix == true) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}
		
		SleepUtil.sleepSmall();
		return page;

	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		SleepUtil.sleepMedium();
		
		if (button == Button.B_NEW) {
			locator = Locators.NewButton;
			page = new FormApptNew(this.MyApplication);
			
		} else if (button == Button.B_EDIT) {
			locator = Locators.EditButton;
			page = new FormApptNew(this.MyApplication);
		
		} else if (button == Button.B_CANCEL) {
			locator = Locators.CancelButton;
			page = null;
		
		} else if (button == Button.B_SAVE) {
			locator = Locators.SaveButton;
			page = null;
			
			// Wait for the message to be delivered (if any)
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
			
		} else if (button == Button.B_BACK) {
			locator = Locators.BackButton;
			page = null;
			
		} else if (button == Button.B_DELETE) {

			locator = Locators.DeleteButton;
			this.sClickAt(locator, "");
			this.zWaitForBusyOverlay();
			
			// Wait for the message to be delivered (if any)
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
			
			SleepUtil.sleepMedium();
			
			return (null);
		
		} else if (button == Button.B_MONTH) {

			locator = Locators.MonthButton;
			page = null;
			
		} else if (button == Button.B_NEXT_PAGE) {

			locator = Locators.NextButton;
			page = null;
			
		} else if (button == Button.B_PREVIOUS_PAGE) {

			locator = Locators.PreviosButton;
			page = null;

		} else if (button == Button.B_OPEN_THE_SERIES) {
			
			locator = Locators.OpenTheSeriesRadioButton;
			page = null;
			
		} else if (button == Button.O_TAG_APPOINTMENT_NEW_TAG_SUB_MENU) {
			
			locator = Locators.TagAppointmentNewTagSubMenu;
			page = null;
			
		} else if (button == Button.O_TAG_APPOINTMENT_REMOVE_TAG_SUB_MENU) {
	
			locator = Locators.TagAppointmentRemoveTagSubMenu;
			page = null;
			
		} else if (button == Button.B_NEXT_WEEK) {
	
			locator = Locators.NextWeek;
			page = null;
			
		} else if (button == Button.B_NEXT_MONTH) {
	
			locator = Locators.NextMonth;
			page = null;
			
		} else if (button == Button.O_GO_TO_TODAY_MENU) {
	
			locator = Locators.TodayButton;
			page = null;
			
		} else if (button == Button.O_MOVE_MENU) {
	
			locator = Locators.MoveToolbar;
			page = null;
		
		} else if (button == Button.B_ACTIONS) {
			
			locator = Locators.ActionsButton_ViewAppt;
			page = null;
			
		} 
		else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		// Default behavior, process the locator by clicking on it
		//
		this.sClickAt(locator, "");
		SleepUtil.sleepSmall();

		// If the app is busy, wait for it to become active
		this.zWaitForBusyOverlay();

		// If page was specified, make sure it is active
		if (page != null) {

			// This function (default) throws an exception if never active
			page.zWaitForActive();

		}

		return (page);
	}
	
	public AbsPage zPressButton(Button button) throws HarnessException {
		
		SleepUtil.sleepMedium();
		
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;
		
		if (button == Button.B_OK) {
			locator = Locators.OKButton;
			page = null;
			
		} else if (button == Button.B_BACK) {
			locator = Locators.BackButton;
			page = null;
			
		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		this.sClickAt(locator, "");
		SleepUtil.sleepSmall();

		return (page);
	}


	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option)
	throws HarnessException {
		logger.info(myPageName() + " zToolbarPressPulldown(" + pulldown + ", "
				+ option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null)
			throw new HarnessException("Button cannot be null!");

		String pulldownLocator = null; // If set, this will be expanded
		String optionLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		if ( pulldown == Button.B_NEW ) {

			if ( option == Button.O_NEW_CALENDAR || option == Button.O_NEW_FOLDER) {

				pulldownLocator = "css=div[id='zb__NEW_MENU'] td[id$='_dropdown'] div[class='ImgSelectPullDownArrow']";
				optionLocator = "css=div[id='zb__NEW_MENU_NEW_CALENDAR'] td[id$='_title']";
				page = new PageCreateFolder(MyApplication, ((AppTouchClient)MyApplication).zPageCalendar);

			} else {

				throw new HarnessException("No logic defined for pulldown " + pulldown + " and option " + option);

			}

		} else if (pulldown == Button.B_LISTVIEW) {

			// In 8.0 D3, there is no pulldown for the view anymore.  There are just buttons.
			//
			// Redirect to the press button method
			//
			return (this.zToolbarPressButton(option));
			
		} else {

			throw new HarnessException("No logic defined for pulldown " + pulldown + " and option " + option);

		}

		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator " + pulldownLocator + " not present!");
			}

			
			sClickAt(pulldownLocator, "0,0");
			
			zWaitForBusyOverlay();

			if (optionLocator != null) {

				zClick(optionLocator);
				zWaitForBusyOverlay();

			}

			// If we click on pulldown/option and the page is specified, then
			// wait for the page to go active
			if (page != null) {
				page.zWaitForActive();
			}

		}
		return page;

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	private AppointmentItem parseListViewRow(String rowLocator) throws HarnessException {
		String locator;

		AppointmentItem item = new AppointmentItem();

		// Is the item checked/unchecked?
		locator = rowLocator + " div[id=$='__se'][class='ImgCheckboxChecked']";
		item.setGIsChecked(this.sIsElementPresent(locator));

		// Is the item tagged/untagged
		locator = rowLocator + " div[id=$='__tg'][class='ImgBlank_16']";
		if ( this.sIsElementPresent(locator) ) {
			// Not tagged
		} else {
			// Tagged : TODO
		}

		// Is there an attachment?
		locator = rowLocator + " div[id=$='__at'][class='ImgAttachment']";
		item.setGHasAttachment(this.sIsElementPresent(locator));

		// Get the fragment and the subject
		locator = rowLocator + " span[id$='__fm']";
		if ( this.sIsElementPresent(locator) ) {

			String fragment = this.sGetText(locator).trim();

			// Get the subject
			locator = rowLocator + " td[id$='__su']";
			String subject = this.sGetText(locator).trim();

			// The subject contains the fragment, e.g. "subject - fragment", so
			// strip it off
			item.setGFragment(fragment);
			item.setGSubject(subject.replace(fragment, "").trim());


		} else {

			// Only the subject is present
			locator = rowLocator + " td[id$='__su']";
			item.setGSubject(this.sGetText(locator).trim());

		}


		// What is the location
		locator = rowLocator + " td[id$='__lo']";
		if ( this.sIsElementPresent(locator) ) {
			String location = this.sGetText(locator).trim();
			item.setGLocation(location);
		}
		

		// What is the status
		locator = rowLocator + " span[id$='__st']";
		if ( this.sIsElementPresent(locator) ) {
			String status = this.sGetText(locator).trim();
			item.setGStatus(status);
		}

		// What calendar is it in
		// TODO

		// Is it recurring
		locator = rowLocator + " div[id=$='__re'][class='ImgApptRecur']";
		item.setGIsRecurring(this.sIsElementPresent(locator));

		// What is the start date
		locator = rowLocator + " td[id$='__dt']";
		item.setGStartDate(this.sGetText(locator));


		return (item);
	}

	private List<AppointmentItem> zListGetAppointmentsListView() throws HarnessException {
		List<AppointmentItem> items = new ArrayList<AppointmentItem>();

		String divLocator = "css=div[id='zl__CLL__rows']";
		String listLocator = divLocator +">div[id^='zli__CLL__']";

		// Make sure the div exists
		if ( !this.sIsElementPresent(divLocator) ) {
			throw new HarnessException("List View Rows is not present: " + divLocator);
		}

		// If the list doesn't exist, then no items are present
		if ( !this.sIsElementPresent(listLocator) ) {
			// return an empty list
			return (items);
		}

		// How many items are in the table?
		int count = this.sGetCssCount(listLocator);
		logger.debug(myPageName() + " zListGetAppointmentsListView: number of appointments: "+ count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {

			// Add the new item to the list
			AppointmentItem item = parseListViewRow(listLocator + ":nth-of-type("+ i +")");
			items.add(item);
			logger.info(item.prettyPrint());

		}

		// Return the list of items
		return (items);

	}

	private AppointmentItem parseAppointmentRow(String rowLocator) throws HarnessException {

		/**

		The DAY, WEEK, WORKWEEK, all use the same logic, just
		different DIV objects in the DOM.
		
		Based on the itemsLocator, which locates each individual
		appointment in the view, parse available appointments
		and return them.

		LIST, MONTH, SCHEDULE, (FREE/BUSY) use different logic.
		That processing must happen in a different method.
		
		*/

		AppointmentItem item = new AppointmentItem();
		
		// Initialize the locator (but narrow to the subject field, if found later)
		item.setLocator(rowLocator);

		// Get the location
		String locator = rowLocator + " div.appt_location";
		if ( this.sIsElementPresent(locator) ) {
			item.setLocation(this.sGetText(locator).trim());
		}


		// Get the name of the appointment (organizer view)
		locator = rowLocator + " td.appt_name";
		if ( this.sIsElementPresent(locator) ) {
			
			// The name field contains both the subject and location, if there is a location
			String subject = this.sGetText(locator);
			if ( item.getLocation() == null ) {
				item.setSubject(subject.trim());
			} else {
				item.setSubject(subject.replace(item.getLocation(), "").trim());
			}
			
			item.setLocator(locator); // Update the appointment locator to point to the subject field
			
		}
		
		// Get the name of the appointment (Attendee view)
		locator = rowLocator + " td.appt_new_name";
		if ( this.sIsElementPresent(locator) ) {
			
			// The name field contains both the subject and location, if there is a location
			String subject = this.sGetText(locator);
			if ( item.getLocation() == null ) {
				item.setSubject(subject.trim());
			} else {
				item.setSubject(subject.replace(item.getLocation(), "").trim());
			}
			
			item.setLocator(locator); // Update the appointment locator to point to the subject field
			
		}
		
		// Get the name of the appointment (Attendee view)
		locator = rowLocator + " td.appt_allday_name";
		if ( this.sIsElementPresent(locator) ) {
			
			// The name field contains both the subject and location, if there is a location
			String subject = this.sGetText(locator);
			if ( item.getLocation() == null ) {
				item.setSubject(subject.trim());
			} else {
				item.setSubject(subject.replace(item.getLocation(), "").trim());
			}
			
			item.setLocator(locator); // Update the appointment locator to point to the subject field
			
		}
		
		// TODO: parse other elements

		return (item);
	}
	
	private List<AppointmentItem> zListGetAppointmentsGeneral(String itemsLocator) throws HarnessException {
		logger.info(myPageName() + " zListGetAppointmentsGeneral("+ itemsLocator +")");

		/**

			The DAY, WEEK, WORKWEEK, all use the same logic, just
			different DIV objects in the DOM.
			
			Based on the itemsLocator, which locates each individual
			appointment in the view, parse available appointments
			and return them.

			LIST, MONTH, SCHEDULE, (FREE/BUSY) use different logic.
			That processing must happen in a different method.
			
		 */
		List<AppointmentItem> items = new ArrayList<AppointmentItem>();

		// If the list doesn't exist, then no items are present
		if ( !this.sIsElementPresent(itemsLocator) ) {
			// return an empty list
			return (items);
		}

		
		String locator = null;

		// How many items are in the table?
		int count = this.sGetCssCount(itemsLocator);
		logger.debug(myPageName() + " zListGetAppointments: number of appointments: "+ count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {

			locator = itemsLocator + ":nth-of-type("+ i +")";
			
			// Add the new item to the list
			AppointmentItem item = parseAppointmentRow(locator);
			items.add(item);
			logger.info(item.prettyPrint());

		}

		// Return the list of items
		return (items);

	}

	
	

	/**
	 * @param cssLocator
	 * @return
	 * @throws HarnessException
	 */
	private AppointmentItem parseMonthViewAllDay(String cssLocator) throws HarnessException {
		logger.info(myPageName() + " parseMonthViewAllDay("+ cssLocator +")");

		/*

  <div style=
  "position: absolute; width: 119px; height: 20px; overflow: hidden; padding-bottom: 4px; left: 133.208px; top: 85px;"
  class="appt" id="zli__CLM__258_DWT557">
    <div class="appt_allday_body ZmSchedulerApptBorder-free" id=
    "zli__CLM__258_DWT557_body" style="width: 119px; height: 16px;">
      <table cellspacing="0" cellpadding="0" style=
      "table-layout: fixed; height: 100%; background: -moz-linear-gradient(center top , rgb(255, 255, 255), rgb(235, 175, 96)) repeat scroll 0% 0% transparent; opacity: 0.4;"
      id="zli__CLM__258_DWT557_tableBody">
        <tbody>
          <tr style="background:-moz-linear-gradient(top,#FFFFFF, #ebaf60);">
            <td width="4px" style=
            "background:-moz-linear-gradient(top,#FFFFFF, #FFFFFF);" class=""></td>

            <td width="100%" class="appt_allday_name">
              <div style="overflow: hidden; white-space: nowrap;">
                appointment13213151729848
              </div>
            </td>

            <td width="20px" style="padding-right:3px;" id="zli__CLM__258_DWT557_tag">
              <div style="width:16" class="ImgBlank_16"></div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
		 */
		
		
		
		String locator;
		
		AppointmentItem appt = new AppointmentItem();
		appt.setLocator(cssLocator + " table tr td");
		
		appt.setGIsAllDay(true);
		
		// Get the subject
		locator = cssLocator + " table tr td + td";
		appt.setSubject(this.sGetText(locator)); // Subject contains start time + subject
		
		// TODO: get the tags
		
		
		return (appt);
	}
	
	private AppointmentItem parseMonthViewNonAllDay(String cssLocator) throws HarnessException {
		logger.info(myPageName() + " parseMonthViewNonAllDay("+ cssLocator +")");

		/*

      <td class="calendar_month_day_item">
        <div style="position:relative;" id="zli__CLM__258_DWT304_body" class="">
          <table width="100%" style=
          "table-layout:fixed; background:-moz-linear-gradient(top,#FFFFFF, #98b6e9);"
          id="zli__CLM__258_DWT304_tableBody">
            <tbody>
              <tr>
                <td width="4px" style=
                "background:-moz-linear-gradient(top,#FFFFFF, #FFFFFF);"></td>

                <td width="100%">
                  <div style="overflow:hidden;white-space:nowrap;" id=
                  "zli__CLM__258_DWT304_st_su">
                    <span id="zli__CLM__258_DWT304_start_time">&nbsp;9:00
                    PM</span><span id=
                    "zli__CLM__258_DWT304_subject">appointment13335134710154</span>
                  </div>
                </td>

                <td width="20px" style="padding-right:3px;" id=
                "zli__CLM__258_DWT304_tag">
                  <div style="width:16" class="ImgBlank_16"></div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </td>		 */
		
		String locator;
		
		AppointmentItem appt = new AppointmentItem();
		appt.setLocator(cssLocator + " tr td + td"); // Point at the appt name


		appt.setGIsAllDay(false);
		
		// Get the subject
		locator = cssLocator + " span[id$='_subject']";
		appt.setSubject(this.sGetText(locator));
		
		// TODO: get the tags		
		
		return (appt);
	}
	
	private List<AppointmentItem> zListGetAppointmentsMonthView() throws HarnessException {
		logger.info(myPageName() + " zListGetAppointmentsMonthView()");
		
		List<AppointmentItem> items = new ArrayList<AppointmentItem>();

		String divLocator = "css=div#zv__CLM"; 
		String itemsLocator = null;

		// Make sure the div exists
		if ( !this.sIsElementPresent(divLocator) ) {
			throw new HarnessException("Day View is not present: " + divLocator);
		}

		// Process the non-all-day items first

		itemsLocator = divLocator +" tr[id^='zli__CLM__']>td.calendar_month_day_item";
		if ( this.sIsElementPresent(itemsLocator) ) {

			int count = this.sGetCssCount(itemsLocator);
			logger.info(itemsLocator +" count: "+ count);

			//for (int i = 1; i <= count; i++) {

				//AppointmentItem item = parseMonthViewNonAllDay(itemsLocator + ":nth-of-type("+ i +")");
				AppointmentItem item = parseMonthViewNonAllDay(itemsLocator);
				items.add(item);
				logger.info(item.prettyPrint());

			//}

		}
		
		
		// Process the all-day items next
		
		itemsLocator = divLocator +" div[id^='zli__CLM__']>div[id$='_body']";
		if ( this.sIsElementPresent(itemsLocator) ) {

			int count = this.sGetCssCount(itemsLocator);
			logger.info(itemsLocator +" count: "+ count);

			for (int i = 1; i <= count; i++) {

				AppointmentItem item = parseMonthViewAllDay(itemsLocator + ":nth-of-type("+ i +")");
				items.add(item);
				logger.info(item.prettyPrint());

			}

		}

		return (items);
	}

	private List<AppointmentItem> zListGetAppointmentsScheduleView() throws HarnessException {
		throw new HarnessException("implement me");
	}

	private List<AppointmentItem> zListGetAppointmentsFreeBusyView() throws HarnessException {
		logger.info(myPageName() + " zListGetAppointmentsFreeBusyView()");
		
		
		List<AppointmentItem> items = new ArrayList<AppointmentItem>();
		
		String listLocator = Locators.CalendarViewFreeBusyCSS;
		String rowLocator = listLocator + " div[id^='zli__CLFB__']";
		String itemLocator = null;

		// Process the non-all-day items first
		if ( !this.sIsElementPresent(rowLocator) )
			throw new HarnessException("List View Rows is not present "+ rowLocator);

		// How many items are in the table?
		int count = this.sGetCssCount(rowLocator);
		logger.debug(myPageName() + " zListSelectItem: number of list items: "+ count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {
			
			itemLocator = rowLocator + ":nth-child("+ i +")";

			String clazz = this.sGetAttribute(itemLocator +"@class");
			if ( clazz.contains("appt") ) {
				
				// Look for the subject
				String s = this.sGetText(itemLocator).trim();
				
				if ( (s == null) || (s.length() == 0) ) {
					continue; // No subject
				}
				
				AppointmentItem item = new AppointmentItem();
				
				// Parse the subject (which is the only data available from F/B
				item.setSubject(s);
				
				// Add the item to the returned list
				items.add(item);
				logger.info(item.prettyPrint());

			}

		}
		
		
		// Process the all-day items next (?)
		// TODO
		
		// Process the recurring items next (?)
		// TODO
		
		
		return (items);
	}

	public List<AppointmentItem> zListGetAppointments() throws HarnessException {

		if ( this.zIsVisiblePerPosition(Locators.CalendarViewListCSS, 0, 0) ) {
			return (zListGetAppointmentsListView());								// LIST
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewDayCSS, 0, 0) ) {
			return (zListGetAppointmentsGeneral(Locators.CalendarViewDayItemCSS));			// DAY
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewWeekCSS, 0, 0) ) {
			return (zListGetAppointmentsGeneral(Locators.CalendarViewWeekItemCSS));		// WEEK
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewWorkWeekCSS, 0, 0) ) {
			return (zListGetAppointmentsGeneral(Locators.CalendarViewWorkWeekItemCSS));	// WORK WEEK
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewMonthCSS, 0, 0) ) {
			return (zListGetAppointmentsMonthView());								// MONTH
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewScheduleCSS, 0, 0) ) {
			return (zListGetAppointmentsScheduleView());							// SCHEDULE
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewFreeBusyCSS, 0, 0) ) {
			return (zListGetAppointmentsFreeBusyView());							// FREE/BUSY
		} else {
			throw new HarnessException("Unknown calendar view");
		}
	}
	
	public AbsPage zTagListView(String tagName) throws HarnessException {
		
		if ( tagName == null )
			throw new HarnessException("itemsLocator cannot be null");

		logger.info(myPageName() + " zTagListView(" + tagName +")");

		String locator = "css=div[id='zb__CLD__TAG_MENU|MENU'] td[id$='_title']:contains('" + tagName + "')";
		AbsPage page = null;

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Unable to determine locator : " + locator);
		}
		
		this.zClickAt(locator, "");
		this.zWaitForBusyOverlay();

		return (page);
	}
	
	public AbsPage zTagContextMenuListView(String tagName) throws HarnessException {
		
		String locator;
		
		if ( tagName == null )
			throw new HarnessException("itemsLocator cannot be null");

		logger.info(myPageName() + " zTagContextMenuListView(" + tagName +")");
		
		locator = "css=div[id='TAG_MENU|MENU'] td[id$='_title']:contains('" + tagName + "')";
		System.out.println(locator);
		AbsPage page = null;

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Unable to determine locator : " + locator);
		}
		
		this.zClickAt(locator, "");
		this.zWaitForBusyOverlay();

		return (page);
	}
	
	public void zCreateTag(AppTouchClient app, String tagName, int tagColor) throws HarnessException {
		
		app.zGetActiveAccount().soapSend(
				"<CreateTagRequest xmlns='urn:zimbraMail'>" + 
					"<tag name='" + tagName + "' color='" + tagColor + "'/>" + 
				"</CreateTagRequest>");
	}
	
	public void zWaitForElementAppear(String locator) throws HarnessException {
		boolean isElementPresent = false;
		for (int i=0; i<=50; i++) {
			isElementPresent = this.sIsElementPresent(locator);
			if (isElementPresent == false) {
				SleepUtil.sleepMedium();
				if (locator == Locators.NewTagMenu_ViewAppt) {
					this.zClickAt(Locators.TagButton_ViewAppt, "");
				}
			} else if (isElementPresent == true) {
				return;
			}	
		}
		if (isElementPresent == false) {
			throw new HarnessException("Element not found");
		}
	}
	
	@Override
	public void zNavigateTo() throws HarnessException {

		// Check if this page is already active.
		if (zIsActive()) {
			return;
		}

		// Make sure we are logged in
		if (!((AppTouchClient) MyApplication).zPageMain.zIsActive()) {
			((AppTouchClient) MyApplication).zPageMain.zNavigateTo();
		}

		tracer.trace("Navigate to " + this.myPageName());
		
		SleepUtil.sleepMedium();
		sClickAt(PageMain.Locators.zNavigationButton, "0,0");
		SleepUtil.sleepSmall();
		
		sClickAt(PageMain.Locators.zAppsButton, "0,0");
		SleepUtil.sleepSmall();
		
		sClickAt(PageMain.Locators.zCalendarApp, "0,0");
		SleepUtil.sleepSmall();

		if (!this.sIsElementPresent(Locators.MonthButton)) {
			throw new HarnessException("Unable to determine locator : " + Locators.MonthButton);
		}
		
		this.zWaitForBusyOverlay();
		
		activeFlag = true;

	}
	
	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the main page is active
		if (!((AppTouchClient) MyApplication).zPageMain.zIsActive()) {
			((AppTouchClient) MyApplication).zPageMain.zNavigateTo();
			zNavigateTo();
		}

		if (activeFlag == true) {
			return (true);
		} else {
			return false;
		}

	}
	
	public void zGoToToday(ZDate today) throws HarnessException {
		
		int NoOfNavigation = 0;
		Calendar calendar = Calendar.getInstance();
		
		if ( calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY ) {
			NoOfNavigation = -1;	// Change Friday to Saturday.
			
		} else if ( calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ) {
			NoOfNavigation = -2;	// Change Saturday to Thursday.
			
		} else if ( calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ) {
			NoOfNavigation = +2;	// Change Sunday to Tuesday.
			
		} else if ( calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY ) {
			NoOfNavigation = +1;	// Change Monday to Tuesday.
			
		}
		
		if (this.sIsVisible("css=div[id^='ext-button'] span[class='x-button-icon x-shown back']")) {
			this.sClickAt("css=div[id^='ext-button'] span[class='x-button-icon x-shown back']", "0,0");
			SleepUtil.sleepSmall();
		}
		
		if ( NoOfNavigation == -1) {
			SleepUtil.sleepSmall();
			sClickAt(Locators.PreviosButton, "0,0");
			
		} else if ( NoOfNavigation == -2) {
			SleepUtil.sleepSmall();
			sClickAt(Locators.PreviosButton, "0,0");
			SleepUtil.sleepMedium();
			sClickAt(Locators.PreviosButton, "0,0");
			
		} else if ( NoOfNavigation == +2) {
			SleepUtil.sleepSmall();
			sClickAt(Locators.NextButton, "0,0");
			SleepUtil.sleepMedium();
			sClickAt(Locators.NextButton, "0,0");
			
		} else if ( NoOfNavigation == +1) {
			SleepUtil.sleepSmall();
			sClickAt(Locators.NextButton, "0,0");
			
		}
		
		SleepUtil.sleepLong();
	}
	
	public void zRefresh() throws HarnessException {
		SleepUtil.sleepSmall();
		sClickAt(Locators.zNavigationButton, "0,0");
		SleepUtil.sleepSmall();
		sClickAt(Locators.CalendarFolder, "0,0");
		SleepUtil.sleepMedium();
	}
}