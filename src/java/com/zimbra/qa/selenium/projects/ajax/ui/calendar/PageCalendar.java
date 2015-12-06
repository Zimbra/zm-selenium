/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui.calendar;

import java.awt.event.KeyEvent;
import java.util.*;

import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.ui.*;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogOpenRecurringItem.Confirmation;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DialogCreateFolder;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;

public class PageCalendar extends AbsTab {

	public static class Locators {

		// Buttons
		public static final String NewButton = "css=td#zb__CLWW__NEW_MENU_title";
		public static final String SendButton = "css=div[id^='ztb__APPT-'] td[id$='_SEND_INVITE_title']";
		public static final String SaveButton = "css=div[id^='ztb__APPTRO'] td[id$='__SAVE_title']";
		public static final String OrganizerSaveButton = "css=div[id$='zb__APPT-1__SAVE'] ";
		public static final String CloseButton = "css=td[id$='__CANCEL_title']:contains('Close')";
		public static final String ViewButton = "id=zb__CLD__VIEW_MENU_dropdown";
		public static final String DeleteButton = "css=td[id='zb__CLD__DELETE_title']";
		public static final String CalendarFolder = "id=zti__main_Calendar__10_textCell";

		// Menus
		public static final String ViewDayMenu = "css=div[id='zm__Calendar'] tr[id='POPUP_DAY_VIEW']";
		public static final String ViewWorkWeekMenu = "css=div[id='zm__Calendar'] tr[id='POPUP_WORK_WEEK_VIEW']";
		public static final String ViewWeekMenu = "css=div[id='zm__Calendar'] tr[id='POPUP_WEEK_VIEW']";
		public static final String ViewMonthMenu = "css=div[id='zm__Calendar'] tr[id='POPUP_MONTH_VIEW']";
		public static final String ViewListMenu = "css=div[id='zm__Calendar'] tr[id='POPUP_CAL_LIST_VIEW']";
		public static final String ViewScheduleMenu = "css=div[id='zm__Calendar'] tr[id='POPUP_SCHEDULE_VIEW']";

		public static final String MonthButton = "css=td[id='zb__CLD__MONTH_VIEW_title']";

		public static final String OpenMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__VIEW_APPOINTMENT_title']";
		public static final String PrintMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__PRINT_title']";
		public static final String AcceptMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__REPLY_ACCEPT_title']";
		public static final String TentativeMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__REPLY_TENTATIVE_title']";
		public static final String DeclineMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__REPLY_DECLINE_title']";
		public static final String EditReplyMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__INVITE_REPLY_MENU_title']";
		public static final String EditReplyAcceptSubMenu = "css=div[id^='POPUP_DWT'] div[id='EDIT_REPLY_ACCEPT'] td[id='EDIT_REPLY_ACCEPT_title']";
		public static final String EditReplyTentativeSubMenu = "css=div[id^='POPUP_DWT'] div[id='EDIT_REPLY_TENTATIVE'] td[id='EDIT_REPLY_TENTATIVE_title']";
		public static final String EditReplyDeclineSubMenu = "css=div[id^='POPUP_DWT'] div[id='EDIT_REPLY_DECLINE'] td[id='EDIT_REPLY_DECLINE_title']";
		public static final String ProposeNewTimeMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__PROPOSE_NEW_TIME_left_icon']";
		public static final String ReinvitesAttendeesMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__REINVITE_ATTENDEES_left_icon']";
		public static final String CreateACopyMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__DUPLICATE_APPT_left_icon']";
		public static final String ReplyMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__REPLY_title']";
		public static final String ReplyToAllMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__REPLY_ALL_title']";
		public static final String ForwardMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__FORWARD_APPT_title']";
		public static final String DeleteMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__DELETE_title']";
		public static final String CancelMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__DELETE_title']";
		public static final String MoveMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__MOVE_title']";
		public static final String TagAppointmentMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__TAG_MENU_title']";
		public static final String TagAppointmentNewTagSubMenu = "id=calendar_newtag_title";
		public static final String TagAppointmentRemoveTagSubMenu = "id=calendar_removetag_title";
		public static final String ShowOriginalMenu = "css=div[id='zm__Calendar'] td[id='zmi__Calendar__SHOW_ORIG_title']";
		public static final String QuickCommandsMenu = "css=div[id='zm__Calendar'] tr[id='POPUP_QUICK_COMMANDS']";

		public static final String NeedsActionButton_ViewAppt = "css=div[id^='DWT'] td[id$='_responseActionSelectCell'] td[id$='_select_container'] td[id$='_title']";
		public static final String NeedsActionValue_ViewAppt = "css=td[id$='_responseActionSelectCell'] td[id$='_select_container'] td[id$='_title']";
		public static final String NeedsActionMenu_ViewAppt = "css=div[id*='_Menu_'] div[id^='AC'] td[id^='AC']:contains('Needs Action')";
		public static final String AcceptedMenu_ViewAppt = "css=div[id*='_Menu_'] td[id^='ZmNeedActionOption_AC']:contains('Accepted')";
		public static final String TentativeMenu_ViewAppt = "css=div[id*='_Menu_'] td[id^='ZmNeedActionOption_TE']:contains('Tentative')";
		public static final String DeclinedMenu_ViewAppt = "css=div[id*='_Menu_'] td[id^='ZmNeedActionOption_DE']:contains('Declined')";
		public static final String DeclinedMenu3_ViewAppt = "css=div[id='ZmNeedActionSelect_3_Menu_1'] td[id='ZmNeedActionOption_DE_3_title']:contains('Declined')";
		public static final String TagButton_ViewAppt = "css=div[id^='ztb__APPTRO'] td[id$='TAG_MENU_dropdown']";
		public static final String NewTagMenu_ViewAppt = "css=div[id$='__TAG_MENU|MENU'] td[id='viewAppointment_newtag_title']";
		public static final String RemoveTagMenu_ViewAppt = "css=div[id$='__TAG_MENU|MENU'] td[id='viewAppointment_removetag_title']";
		public static final String ActionsButton_ViewAppt = "css=div[id^='ztb__APPTRO'] td[id$='ACTIONS_MENU_title']";
		public static final String EditMenu_ViewAppt = "css=div[id^='zm__APPTRO'] td[id$='EDIT_title']";
		public static final String CreateACopyMenu_ViewAppt = "css=div[id^='zmi__APPTRO'] td[id$='DUPLICATE_APPT_title']";
		public static final String ReplyMenu_ViewAppt = "css=div[id^='zmi__APPTRO'] td[id$='REPLY_title']";
		public static final String ReplyToAllMenu_ViewAppt = "css=div[id^='zmi__APPTRO'] td[id$='REPLY_ALL_title']";
		public static final String ForwardMenu_ViewAppt = "css=div[id^='zm__APPTRO'] td[id$='FORWARD_APPT_title']";
		public static final String ProposeNewTimeMenu_ViewAppt = "css=div[id^='zm__APPTRO'] td[id$='PROPOSE_NEW_TIME_title']";
		public static final String DeleteMenu_ViewAppt = "css=div[id^='zm__APPTRO'] td[id$='DELETE_title']";
		public static final String ShowOriginalMenu_ViewAppt = "css=div[id^='zm__APPTRO'] td[id$='SHOW_ORIG_title']";

		public static final String InstanceMenu = "id=VIEW_APPT_INSTANCE_title";
		public static final String SeriesMenu = "id=VIEW_APPT_SERIES_title";
		public static final String OpenInstanceMenu = "id=OPEN_APPT_INSTANCE_title";
		public static final String OpenSeriesMenu = "id=OPEN_APPT_SERIES_title";
		public static final String AcceptSeriesMenu = "css=div[id='zm__Calendar_VIEW_APPT_SERIES'] td[id='zmi__Calendar_VIEW_APPT_SERIES__REPLY_ACCEPT_title']";
		public static final String DeclineSeriesMenu = "css=div[id='zm__Calendar_VIEW_APPT_SERIES'] td[id='zmi__Calendar_VIEW_APPT_SERIES__REPLY_DECLINE_title']";
		public static final String CreateACopySeriesMenu = "css=div[id='zm__Calendar_VIEW_APPT_SERIES'] td[id='zmi__Calendar_VIEW_APPT_SERIES__DUPLICATE_APPT_title']";
		public static final String ForwardSeriesMenu = "css=div[id='zm__Calendar_VIEW_APPT_SERIES'] td[id='zmi__Calendar_VIEW_APPT_SERIES__FORWARD_APPT_SERIES_title']";
		public static final String DeleteSeriesMenu = "css=div[id='zm__Calendar_VIEW_APPT_SERIES'] td[id='zmi__Calendar_VIEW_APPT_SERIES__DELETE_SERIES_title']";
		public static final String MoveSeriesMenu = "css=div[id='zm__Calendar_VIEW_APPT_SERIES'] td[id='zmi__Calendar_VIEW_APPT_SERIES__MOVE_title']";
		public static final String AcceptInstanceMenu = "css=div[id='zm__Calendar_VIEW_APPT_INSTANCE'] td[id='zmi__Calendar_VIEW_APPT_INSTANCE__REPLY_ACCEPT_title']";
		public static final String DeclineInstanceMenu = "css=div[id='zm__Calendar_VIEW_APPT_INSTANCE'] td[id='zmi__Calendar_VIEW_APPT_INSTANCE__REPLY_DECLINE_title']";
		public static final String CreateACopyInstanceMenu = "css=div[id='zm__Calendar_VIEW_APPT_INSTANCE'] td[id='zmi__Calendar_VIEW_APPT_INSTANCE__DUPLICATE_APPT_title']";
		public static final String ForwardInstanceMenu = "css=div[id='zm__Calendar_VIEW_APPT_INSTANCE'] td[id='zmi__Calendar_VIEW_APPT_INSTANCE__FORWARD_APPT_INSTANCE_title']";
		public static final String DeleteInstanceMenu = "css=div[id='zm__Calendar_VIEW_APPT_INSTANCE'] td[id='zmi__Calendar_VIEW_APPT_INSTANCE__DELETE_INSTANCE_title']";

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

		// Radio buttons
		public static final String OpenThisInstanceRadioButton = "css=td input[id*='_defaultRadio']";
		public static final String OpenTheSeriesRadioButton = "css=td input[id$='_openSeries']";
		public static final String DeleteThisInstanceRadioButton = "css=td input[id*='_defaultRadio']";
		public static final String DeleteTheSeriesRadioButton = "css=td input[id$='_openSeries']";

		public static final String CalendarViewListDivID		= "zv__CLL";
		public static final String CalendarViewDayDivID			= "zv__CLD";
		public static final String CalendarViewWeekDivID		= "zv__CLW";
		public static final String CalendarViewWorkWeekDivID	= "zv__CLWW";
		public static final String CalendarViewMonthDivID		= "zv__CLM";
		public static final String CalendarViewScheduleDivID	= "zv__CLS";
		public static final String CalendarViewFreeBusyDivID	= "zv__CLFB";

		public static final String CalendarViewListCSS			= "css=div[id^='" + CalendarViewListDivID + "']";
		public static final String CalendarViewSearchListCSS	= "css=div[id^='" + CalendarViewListDivID + "__DWT']";
		public static final String CalendarViewDayCSS			= "css=div#"+ CalendarViewDayDivID;
		public static final String CalendarViewWeekCSS			= "css=div#"+ CalendarViewWeekDivID;
		public static final String CalendarViewWorkWeekCSS		= "css=div#"+ CalendarViewWorkWeekDivID;
		public static final String CalendarViewMonthCSS			= "css=div#"+ CalendarViewMonthDivID;
		public static final String CalendarViewScheduleCSS		= "css=div#"+ CalendarViewScheduleDivID;
		public static final String CalendarViewFreeBusyCSS		= "css=div#"+ CalendarViewFreeBusyDivID;
		public static final String CalendarWorkWeekViewApptCount= "css=div[class='calendar_body'] div[id^='zli__CLWW__']";

		public static final String CalendarViewDayItemCSS		= CalendarViewDayCSS + " div[id^='zli__CLD__']>table[id^='zli__CLD__']";
		public static final String CalendarViewWeekItemCSS		= CalendarViewWeekCSS + " div[id^='zli__CLW__']>table[id^='zli__CLW__']";
		public static final String CalendarViewWorkWeekItemCSS	= CalendarViewWorkWeekCSS + " div[id^='zli__CLWW__']>table[id^='zli__CLWW__']";

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
		public static final String LocationName= "css=div[class='DwtDialog'] div[id='ZmAttendeePicker_LOCATION_content'] table tr td:nth-child(2) input";
		public static final String zAttachmentsLabel= "css= tr[id$='_attachment_container'] fieldset[class='ZmFieldset']:contains('Attachments')";

		public static final String NextPage = "css=div[id='zb__CAL__Nav__PAGE_FORWARD'] div[class='ImgRightArrow']";
		public static final String PreviousPage = "css=div[id='zb__CAL__Nav__PAGE_BACK'] div[class='ImgLeftArrow']";
		public static final String ImgPrivateAppt = "css= div[class='ImgReadOnly']";

		public static final String LocationFirstSearchResult = "css= div[class='DwtChooserListView'] div[class='DwtListView-Rows'] div";

		public static final String DayViewOnFBLink = "css=td[class='TbTop'] td[id='caltb'] a[id='CAL_DAY']";
		public static final String WorkWeekViewOnFBLink = "css=td[class='TbTop'] td[id='caltb'] a[id='CAL_WORK']";
		public static final String WeekViewOnFBLink = "css=td[class='TbTop'] td[id='caltb'] a[id='CAL_WEEK']";
		public static final String MonthViewOnFBLink = "css=td[class='TbTop'] td[id='caltb'] a[id='CAL_MONTH']";
		public static final String TodayViewOnFBLink = "css=td[class='TbTop'] td[id='caltb'] a[id='CAL_TODAY']";
		public static final String AttendeeBubbleAddr = "css=tr[id='zcs1_attendeesContainer'] td[id='zcs1_person'] div div span[class^='addrBubble']>span";
		public static final String CloseAptTab = "css= div[id^='zb__App__tab_APPT'] tr td>div[class='ImgCloseGray']";





	}

	public PageCalendar(AbsApplication application) {
		super(application);

		logger.info("new " + PageCalendar.class.getCanonicalName());
	}

	public String zGetMoveLocator(String folderName) throws HarnessException {
		return Locators.MoveFolderOption +  folderName + "')";

	}

	@SuppressWarnings("unused")
	private String getLocatorBySubject(String subject) throws HarnessException {
		int count;
		String locator;

		// Organizer's view
		locator = "css=td.appt_name:contains('"+ subject +"')";
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

		// Attendee's view
		locator = "css=td.appt_new_name:contains('"+ subject +"')";
		count = this.sGetCssCount(locator);
		if ( count == 1 ) {
			return (locator);
		} else if ( count > 1 ) {
			for ( int i = 1; i <= count; i++ ) {
				if ( this.zIsVisiblePerPosition(locator + ":nth-of-type("+ i +")", 0, 0) ) {
					return (locator + ":nth-of-type("+ i +")");
				}
			}
		}

		// All day, Organizer's view
		locator = "css=td.appt_allday_name:contains('"+ subject +"')";
		count = this.sGetCssCount(locator);
		if ( count == 1 ) {
			return (locator);
		} else if ( count > 1 ) {
			for ( int i = 1; i <= count; i++ ) {
				if ( this.zIsVisiblePerPosition(locator + ":nth-of-type("+ i +")", 0, 0) ) {
					return (locator + ":nth-of-type("+ i +")");
				}
			}
		}

		// All day, Attendee's view
		locator = "css=td.appt_allday_new_name:contains('"+ subject +"')";
		count = this.sGetCssCount(locator);
		if ( count == 1 ) {
			return (locator);
		} else if ( count > 1 ) {
			for ( int i = 1; i <= count; i++ ) {
				if ( this.zIsVisiblePerPosition(locator + ":nth-of-type("+ i +")", 0, 0) ) {
					return (locator + ":nth-of-type("+ i +")");
				}
			}
		}

		throw new HarnessException("Unable to locate appointment!");

	}

	public boolean zVerifyAppointmentExists (String apptSubject) throws HarnessException {

		boolean found = false;

		for (int i=1; i<=3; i++) {

			zToolbarPressButton(Button.B_REFRESH);

			List<AppointmentItem> items = zListGetAppointments();

			for (AppointmentItem item : items ) {
				if ( apptSubject.equals(item.getSubject()) ) {
					found = true;
					break;
				} else {
					logger.info("Appointment not displayed in current view");
					Stafpostqueue sp = new Stafpostqueue();
					sp.waitForPostqueue();
				}
			}

			if (found = true) {
				SleepUtil.sleepSmall();
				logger.info("Appointment displayed in current view");
				ZAssert.assertTrue(found, "Appointment not displayed in current view");
				break;
			}
		}

		return found;

	}

	public boolean zGetApptLocatorFreeBusyView(String attendeeEmail, String apptSubject) throws HarnessException {
		boolean attendeeEmailRow, apptSubjectRow;
		attendeeEmailRow = sIsElementPresent("css=div[id='zv__CLFB'] td[id$='_NAME_'] div[class='ZmSchedulerInputDisabled']:contains('" + attendeeEmail + "')");
		apptSubjectRow = sIsElementPresent("css=div[id^='zli__CLFB'] div[class='appt_allday_body']:contains('" + apptSubject + "')");
		if (attendeeEmailRow == true && apptSubjectRow == true) {
			return true;
		} else {
			return false;
		}
	}

	public String zGetApptLocator(String apptSubject) throws HarnessException {
		SleepUtil.sleepSmall();
		if (sIsElementPresent("css=td.appt_name:contains('" + apptSubject + "')") == true) {
			return "css=td.appt_name:contains('" + apptSubject + "')";
		} else if (sIsElementPresent("css=td.appt_new_name:contains('" + apptSubject + "')") == true) {
			return "css=td.appt_new_name:contains('" + apptSubject + "')";
		} else if (sIsElementPresent("css=span[id^='zli__CLM__']['_subject']:contains('" + apptSubject + "')") == true) {
			return "css=span[id^='zli__CLM__']['_subject']:contains('" + apptSubject + "')";
		} else {
			throw new HarnessException("Unable to locate subject: "+ apptSubject);
		}
	}

	public boolean zIsAppointmentExists(String apptSubject) throws HarnessException {
		SleepUtil.sleepMedium();
		if (sIsElementPresent("css=td.appt_name:contains('" + apptSubject + "')") == true ||
				sIsElementPresent("css=td.appt_new_name:contains('" + apptSubject + "')") == true ||
				sIsElementPresent("css=span[id^='zli__CLM__']['_subject']:contains('" + apptSubject + "')") == true) {
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
			return sIsElementPresent("css=div[id='zm__Calendar'] div[id='zmi__Calendar__REINVITE_ATTENDEES'].ZDisabled");
		} else if (buttonName.equals(Button.O_FORWARD_DISABLED)) {
			return sIsElementPresent("css=div[id='zm__Calendar'] div[id='zmi__Calendar__FORWARD_APPT'].ZDisabled");
		} else if (buttonName.equals(Button.O_CREATE_A_COPY_DISABLED)) {
			return sIsElementPresent("css=div[id='zm__Calendar'] div[id='zmi__Calendar__DUPLICATE_APPT'].ZDisabled");
		} else if (buttonName.equals(Button.O_SHOW_ORIGINAL_DISABLED)) {
			return sIsElementPresent("css=div[id='zm__Calendar'] div[id='zmi__Calendar__SHOW_ORIG'].ZDisabled");
		} else if (buttonName.equals(Button.O_DELETE_DISABLED)) {
			return sIsElementPresent("css=div[id='zm__Calendar'] div[id='zmi__Calendar__DELETE'].ZDisabled");
		} else if (buttonName.equals(Button.O_MOVE_DISABLED)) {
			return sIsElementPresent("css=div[id='zm__Calendar'] div[id='zmi__Calendar__MOVE'].ZDisabled");
		} else if (buttonName.equals(Button.O_TAG_APPOINTMENT_DISABLED)) {
			return sIsElementPresent("css=div[id='zm__Calendar'] div[id='zmi__Calendar__TAG_MENU'].ZDisabled");
		} else if (buttonName.equals(Button.O_REPLY_DISABLED)) {
			return sIsElementPresent("css=div[id='zm__Calendar'] div[id='zmi__Calendar__REPLY'].ZDisabled");

		} else if (buttonName.equals(Button.B_TAG_APPOINTMENT_DISABLED_READONLY_APPT)) {
			return sIsElementPresent("css=div[id='ztb__APPTRO-1'] div[id^='zb__APPTRO-1'][id$='TAG_MENU'].ZDisabled");
		} else if (buttonName.equals(Button.B_SAVE_DISABLED_READONLY_APPT)) {
			return sIsElementPresent("css=div[id='ztb__APPTRO-1'] div[id^='zb__APPTRO-1'][id$='SAVE'].ZDisabled");
		} else if (buttonName.equals(Button.B_ACCEPTED_DISABLED_READONLY_APPT)) {
			return sIsElementPresent("css=div[class='ZmMailMsgView'] td[id$='_responseActionSelectCell'] div.ZDisabled");

		} else if (buttonName.equals(Button.O_EDIT_DISABLED_READONLY_APPT)) {
			return sIsElementPresent("css=div[id='zm__APPTRO-1'] div[id='zmi__APPTRO-1__EDIT'].ZDisabled");
		} else if (buttonName.equals(Button.O_FORWARD_DISABLED_READONLY_APPT)) {
			return sIsElementPresent("css=div[id='zm__APPTRO-1'] div[id='zmi__APPTRO-1__FORWARD_APPT'].ZDisabled");
		} else if (buttonName.equals(Button.O_PROPOSE_NEW_TIME_DISABLED_READONLY_APPT)) {
			return sIsElementPresent("css=div[id='zm__APPTRO-1'] div[id='zmi__APPTRO-1__PROPOSE_NEW_TIME'].ZDisabled");
		} else if (buttonName.equals(Button.O_DELETE_DISABLED_READONLY_APPT)) {
			return sIsElementPresent("css=div[id='zm__APPTRO-1'] div[id='zmi__APPTRO-1__DELETE'].ZDisabled");

		} else {
			return false;
		}
	}

	private AbsPage zListItemListView(Action action, String subject) throws HarnessException {
		logger.info(myPageName() + " zListItemListView("+ action +", "+ subject +")");

		// The default locator points at the subject
		String locator = "css=div[id='zl__CLL__rows'] td[id$='__su']:contains('" + subject + "')";
		AbsPage page = null;


		if ( action == Action.A_LEFTCLICK ) {

			// Left-Click on the item
			this.zClickAt(locator,"");
			this.zWaitForBusyOverlay();

			page = null;

			// FALL THROUGH

		} else if ( action == Action.A_CHECKBOX || action == Action.A_UNCHECKBOX ) {

			// Find the locator to the row
			locator = null;
			int count = this.sGetCssCount("css=div[id='zl__CLL__rows']>div");
			for (int i = 1; i <= count; i++) {

				String itemLocator = "css=div[id='zl__CLL__rows']>div:nth-of-type("+ i +")";
				String s = this.sGetText(itemLocator + " td[id$='__su']").trim();

				if ( s.contains(subject) ) {
					locator = itemLocator;
					break; // found it
				}

			}

			if ( locator == null )
				throw new HarnessException("Unable to locate row with subject: "+ subject);

			String selectLocator = locator + " div[id$='__se']";
			if ( !this.sIsElementPresent(selectLocator) )
				throw new HarnessException("Checkbox locator is not present "+ selectLocator);

			if ( action == Action.A_CHECKBOX ) {
				if ( this.sIsElementPresent(selectLocator +"[class*='ImgCheckboxChecked']"))
					throw new HarnessException("Trying to check box, but it was already checked");
			} else if ( action == Action.A_UNCHECKBOX ) {
				if ( this.sIsElementPresent(selectLocator +"[class*='ImgCheckboxUnchecked']"))
					throw new HarnessException("Trying to uncheck box, but it was already unchecked");
			}


			// Left-Click on the flag field
			this.zClick(selectLocator);

			this.zWaitForBusyOverlay();

			// No page to return
			page = null;

			// FALL THROUGH

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
			return (zListItemListView(action, subject));											// LIST
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewDayCSS, 0, 0) ) {
			return (zListItemGeneral(Locators.CalendarViewDayItemCSS, action, subject));		// DAY
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewWorkWeekCSS, 0, 0) ) {
			return (zListItemGeneral(Locators.CalendarViewWorkWeekItemCSS, action, subject));	// WORKWEEK
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewWeekCSS, 0, 0) ) {
			return (zListItemGeneral(Locators.CalendarViewWeekItemCSS, action, subject));		// WEEK
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewMonthCSS, 0, 0) ) {
			return (zListItemMonthView(action, subject));											// MONTH
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewScheduleCSS, 0, 0) ) {
			return (zListItemGeneral("TODO", action, subject));								// SCHEDULE
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

		SleepUtil.sleepMedium(); //test fails because selenium method runs fast so it doesn't find element
		if ( this.sIsElementPresent(itemsLocator +" td.appt_name:contains('"+ subject +"')")) {

			// Single occurrence locator
			locator = itemsLocator +" td.appt_name:contains('"+ subject +"')";

		} else if ( this.sIsElementPresent(itemsLocator +" td.appt_30_name:contains('"+ subject +"')")) {

			// Single occurrence locator
			locator = itemsLocator +" td.appt_30_name:contains('"+ subject +"')";

		} else if ( this.sIsElementPresent(itemsLocator +" td[class$='appt_new_name']:contains('"+ subject +"')")) {

			// Recurring appointment locator (might point to any visible instance)
			locator = itemsLocator +" td[class$='appt_new_name']:contains('"+ subject +"')";

		} else if ( this.sIsElementPresent(itemsLocator +" td.appt_allday_name>div:contains('"+ subject +"')")) {

			// All day single occurrence locator
			locator = itemsLocator +" td.appt_allday_name>div:contains('"+ subject +"')";

		} else if ( this.sIsElementPresent(itemsLocator +" td[class='appt_name']")) {

			// All day single occurrence locator
			locator = itemsLocator +" td[class='appt_name']";

		}

		// Make sure one of the locators found the appt
		if ( locator == null ) {
			throw new HarnessException("Unable to determine locator for appointment: "+ subject);
		}



		if ( action == Action.A_LEFTCLICK ) {

			this.zClickAt(locator, "");
			this.zWaitForBusyOverlay();

			page = null;

			// FALL THROUGH

		} else if ( action == Action.A_RIGHTCLICK) {

			this.zRightClickAt(locator, "");
			this.zWaitForBusyOverlay();

			page = null;

			// FALL THROUGH

		} else if ( action == Action.A_DOUBLECLICK) {

			this.sDoubleClick(locator);
			this.zWaitForBusyOverlay();
			SleepUtil.sleepLong();

			page = new DialogOpenRecurringItem(Confirmation.OPENRECURRINGITEM, MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}

			page = new FormApptNew(this.MyApplication);
			if ( page.zIsActive() ) {
				return (page);
			}

			// FALL THROUGH

		} else {
			throw new HarnessException("implement me!  action = "+ action);
		}


		if ( page != null ) {
			page.zWaitForActive();
		}

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
			SleepUtil.sleepLong();

			/*page = new DialogOpenRecurringItem(Confirmation.OPENRECURRINGITEM, MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}*/

			DialogOpenRecurringItem openRecurring = new DialogOpenRecurringItem(null, MyApplication, null);
			openRecurring.zClickButton(Button.B_OPEN_THE_SERIES);
			openRecurring.zClickButton(Button.B_OK);

			page = new FormApptNew(this.MyApplication);
			if ( page.zIsActive() ) {
				return (page);
			}
		} else {
			throw new HarnessException("implement me!  action = "+ action);
		}

		return(page);
	}

	private AbsPage zListItemListView(Action action, Button option, String subject) throws HarnessException {

		logger.info(myPageName() + " zListItemListView("+ action +", "+ option +", "+ subject +")");

		// The default locator points at the subject
		String itemlocator = "css=div[id='zl__CLL__rows'] td[id$='__su']:contains('" + subject + "')";
		String optionLocator = null;
		AbsPage page = null;

		if ( action == Action.A_RIGHTCLICK ) {

			// Right-Click on the item
			this.zRightClickAt(itemlocator,"");

			// Now the ContextMenu is opened
			// Click on the specified option

			if (option == Button.O_OPEN_MENU) {

				optionLocator = Locators.OpenMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if (option == Button.O_PRINT_MENU) {

				optionLocator = Locators.PrintMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if ( option == Button.O_ACCEPT_MENU ) {

				optionLocator = Locators.AcceptMenu;

				if ( optionLocator != null ) {

					this.zClickAt(optionLocator, "");
					SleepUtil.sleepSmall();
					this.zWaitForBusyOverlay();

				}
				page = null;

			} else if ( option == Button.O_TENTATIVE_MENU ) {

				optionLocator = Locators.TentativeMenu;
				if ( optionLocator != null ) {

					this.zClickAt(optionLocator, "");
					SleepUtil.sleepSmall();
					this.zWaitForBusyOverlay();

				}
				page = null;

			} else if ( option == Button.O_DECLINE_MENU ) {

				optionLocator = Locators.DeclineMenu;

				if ( optionLocator != null ) {

					this.zClickAt(optionLocator, "");
					SleepUtil.sleepSmall();
					this.zWaitForBusyOverlay();

				}
				page = null;

			} else if ( option == Button.O_PROPOSE_NEW_TIME_MENU ) {

				optionLocator = Locators.ProposeNewTimeMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if ( option == Button.O_CREATE_A_COPY_MENU ) {

				optionLocator = Locators.CreateACopyMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if ( option == Button.O_REPLY ) {

				optionLocator = Locators.ReplyMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if ( option == Button.O_REPLY_TO_ALL ) {

				optionLocator = Locators.ReplyToAllMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if ( option == Button.O_FORWARD ) {

				optionLocator = Locators.ForwardMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if ( option == Button.O_DELETE ) {

				optionLocator = Locators.DeleteMenu;
				page = new DialogConfirmDeleteAppointment(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

				// Depending on the type of appointment being deleted,
				// We may need to use a different type of page here
				// page = new DialogConfirmDeleteAttendee(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
				// page = new DialogConfirmDeleteOrganizer(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

			} else if ( option == Button.O_MOVE ) {

				optionLocator = Locators.MoveMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if ( option == Button.O_TAG_APPOINTMENT_MENU ) {

				optionLocator = Locators.TagAppointmentMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if (option == Button.O_TAG_APPOINTMENT_NEW_TAG_SUB_MENU) {

				optionLocator = Locators.TagAppointmentNewTagSubMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if (option == Button.O_TAG_APPOINTMENT_REMOVE_TAG_SUB_MENU) {

				optionLocator = Locators.TagAppointmentRemoveTagSubMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if ( option == Button.O_SHOW_ORIGINAL_MENU ) {

				optionLocator = Locators.ShowOriginalMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if ( option == Button.O_QUICK_COMMANDS_MENU ) {

				optionLocator = Locators.QuickCommandsMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if (option == Button.O_INSTANCE_MENU) {

				optionLocator = Locators.InstanceMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if (option == Button.O_SERIES_MENU) {

				optionLocator = Locators.SeriesMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if (option == Button.O_OPEN_INSTANCE_MENU) {

				optionLocator = Locators.OpenInstanceMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if (option == Button.O_FORWARD_INSTANCE_MENU) {

				optionLocator = Locators.ForwardInstanceMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if (option == Button.O_DELETE_INSTANCE_MENU) {

				optionLocator = Locators.DeleteInstanceMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if (option == Button.O_OPEN_SERIES_MENU) {

				optionLocator = Locators.OpenSeriesMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if (option == Button.O_FORWARD_SERIES_MENU) {

				optionLocator = Locators.ForwardSeriesMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if (option == Button.O_NEW_APPOINTMENT_MENU) {

				optionLocator = Locators.NewAppointmentMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if (option == Button.O_NEW_ALL_DAY_APPOINTMENT_MENU) {

				optionLocator = Locators.NewAllDayAppointmentMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if (option == Button.O_GO_TO_TODAY_MENU) {

				optionLocator = Locators.GoToTodayMenu;
				throw new HarnessException("implement action:"+ action +" option:"+ option);

			}
			else {
				throw new HarnessException("implement action:"+ action +" option:"+ option);
			}

			this.zClickAt(optionLocator,"");
			this.zWaitForBusyOverlay();

		} else {
			throw new HarnessException("implement me!  action = "+ action);
		}

		if ( page != null ) {
			page.zWaitForActive();
		}

		Stafpostqueue sp = new Stafpostqueue();
		sp.waitForPostqueue();

		return (page);

	}

	@Override
	public AbsPage zListItem(Action action, Button option, String subject) throws HarnessException {
		logger.info(myPageName() + " zListItem("+ action +", "+ option +", "+ subject +")");

		if ( this.zIsVisiblePerPosition(Locators.CalendarViewListCSS, 0, 0) ) {
			return (zListItemListView(action, option, subject));									// LIST

		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewDayCSS, 0, 0) ) {
			return (zListItemGeneral(Locators.CalendarViewDayItemCSS, action, option, subject));	// DAY

		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewWorkWeekCSS, 0, 0) ) {
			return (zListItemGeneral(Locators.CalendarViewWorkWeekItemCSS, action, option, subject));	// WORKWEEK

		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewWeekCSS, 0, 0) ) {
			return (zListItemGeneral(Locators.CalendarViewWeekItemCSS, action, option, subject));	// WEEK

		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewMonthCSS, 0, 0) ) {
			return (zListItemMonthView(action, option, subject));									// MONTH

		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewScheduleCSS, 0, 0) ) {
			return (zListItemGeneral("TODO", action, option, subject));								// SCHEDULE

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
		String subOptionLocator = null;

		if ( this.sIsElementPresent(itemsLocator +" td.appt_name:contains('"+ subject +"')")) {

			// Single occurrence locator
			locator = itemsLocator +" td.appt_name:contains('"+ subject +"')";

		} else if ( this.sIsElementPresent(itemsLocator +" td[class$='appt_new_name']:contains('"+ subject +"')")) {

			// Recurring appointment locator (might point to any visible instance)
			locator = itemsLocator +" td[class$='appt_new_name']:contains('"+ subject +"')";

		} else if ( this.sIsElementPresent(itemsLocator +" td.appt_allday_name>div:contains('"+ subject +"')")) {

			// All day single occurrence locator
			locator = itemsLocator +" td.appt_allday_name>div:contains('"+ subject +"')";

		} else if ( this.sIsElementPresent(itemsLocator +" td[id$='_responseActionSelectCell'] td[id$='_select_container']")) {

			// Read-only appointment locator
			locator = itemsLocator +" td[id$='_responseActionSelectCell'] td[id$='_select_container']";

		}

		else if(this.sIsElementPresent(itemsLocator +" td[class='appt_name']")){
			locator = itemsLocator +" td[class='appt_name']";
		}

		// Make sure one of the locators found the appt
		if ( locator == null ) {
			throw new HarnessException("Unable to determine locator for appointment: "+ subject);
		}

		if (action == Action.A_LEFTCLICK) {

			this.zClickAt(locator, "");
			this.zWaitForBusyOverlay();
			SleepUtil.sleepSmall();

			if ( (option == Button.B_DELETE)) {

				optionLocator = Locators.DeleteButton;

				if ( optionLocator != null ) {
					this.zClickAt(optionLocator, "");
					SleepUtil.sleepSmall();
					this.zWaitForBusyOverlay();
				}
				page = null;

			} else {

				throw new HarnessException("implement action:"+ action +" option:"+ option);
			}

		} else if (action == Action.A_RIGHTCLICK) {

			this.zRightClickAt(locator, "");
			this.zWaitForBusyOverlay();
			SleepUtil.sleepSmall();

			if ( (option == Button.O_DELETE) || (option == Button.O_CANCEL_MENU) ) {

				if (option == Button.O_DELETE) {
					optionLocator = Locators.DeleteMenu;

				} else if (option == Button.O_CANCEL_MENU) {
					optionLocator = Locators.CancelMenu;
				}

				if ( optionLocator != null ) {
					this.zClickAt(optionLocator, "");
					SleepUtil.sleepSmall();
					this.zWaitForBusyOverlay();
				}

				// If the organizer deletes an appointment, you get "Send Cancellation" dialog
				page = new DialogConfirmDeleteOrganizer(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
				if ( page.zIsActive() ) {
					return (page);
				}

				// If an attendee deletes an appointment, you get a "Confirm Delete" dialog with "Notify Organizer?"
				page = new DialogConfirmDeleteAttendee(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
				if ( page.zIsActive() ) {
					return (page);
				}

				// If an attendee deletes an appointment, you get a "Confirm Delete" dialog
				page = new DialogConfirmDeleteAppointment(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
				if ( page.zIsActive() ) {
					return (page);
				}

				page = new DialogConfirmDeleteRecurringAppointment(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
				if ( page.zIsActive() ) {
					return (page);
				}

				throw new HarnessException("Dialog box not opened after performing action");

			} else if ( option == Button.O_OPEN_MENU ) {

				optionLocator = Locators.OpenMenu;

				if ( optionLocator != null ) {
					this.zClickAt(optionLocator, "");
					SleepUtil.sleepMedium();
				}

				if (com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.attendee.singleday.actions.Open.organizerTest == false ||
						com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.attendee.singleday.viewappt.Close.organizerTest == false ||
						com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.attendee.singleday.actions.Open.organizerTest == false ||
						com.zimbra.qa.selenium.projects.ajax.tests.calendar.mountpoints.viewer.viewappt.VerifyDisabledUI.organizerTest == false ||
						com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday.create.CreateMeetingWithDL.organizerTest == false) {
					page = null;
				} else {
					page = new FormApptNew(this.MyApplication);
				}

			} else if ( option == Button.O_ACCEPT_MENU ) {

				optionLocator = Locators.AcceptMenu;

				if ( optionLocator != null ) {

					this.zClickAt(optionLocator, "");
					SleepUtil.sleepSmall();
					this.zWaitForBusyOverlay();

				}
				page = null;

			} else if ( option == Button.O_TENTATIVE_MENU ) {

				optionLocator = Locators.TentativeMenu;

				if ( optionLocator != null ) {

					this.zClickAt(optionLocator, "");
					SleepUtil.sleepSmall();
					this.zWaitForBusyOverlay();

				}
				page = null;

			} else if ( option == Button.O_DECLINE_MENU ) {

				optionLocator = Locators.DeclineMenu;

				if ( optionLocator != null ) {

					this.zClickAt(optionLocator, "");
					SleepUtil.sleepSmall();
					this.zWaitForBusyOverlay();

				}
				page = null;

			} else if ( option == Button.O_EDIT_REPLY_ACCEPT_SUB_MENU ) {

				optionLocator = Locators.EditReplyMenu;
				subOptionLocator = Locators.EditReplyAcceptSubMenu;

				if ( optionLocator != null ) {

					this.sMouseOver(optionLocator);
					SleepUtil.sleepSmall();

					this.sClickAt(subOptionLocator, "");
					SleepUtil.sleepSmall();

					this.zWaitForBusyOverlay();

				}
				page = new FormMailNew(this.MyApplication);

			} else if ( option == Button.O_EDIT_REPLY_TENTATIVE_SUB_MENU ) {

				optionLocator = Locators.EditReplyMenu;
				subOptionLocator = Locators.EditReplyTentativeSubMenu;

				if ( optionLocator != null ) {

					this.sMouseOver(optionLocator);
					SleepUtil.sleepSmall();

					this.sClickAt(subOptionLocator, "");
					SleepUtil.sleepSmall();

					this.zWaitForBusyOverlay();

				}
				page = new FormMailNew(this.MyApplication);

			} else if ( option == Button.O_EDIT_REPLY_DECLINE_SUB_MENU ) {

				optionLocator = Locators.EditReplyMenu;
				subOptionLocator = Locators.EditReplyDeclineSubMenu;

				if ( optionLocator != null ) {

					this.sMouseOver(optionLocator);
					SleepUtil.sleepSmall();

					this.sClickAt(subOptionLocator, "");
					SleepUtil.sleepSmall();

					this.zWaitForBusyOverlay();

				}
				page = new FormMailNew(this.MyApplication);

			} else if ( option == Button.O_PROPOSE_NEW_TIME_MENU ) {

				optionLocator = Locators.ProposeNewTimeMenu;

				if ( optionLocator != null ) {

					this.sClickAt(optionLocator, "");
					SleepUtil.sleepSmall();

					this.zWaitForBusyOverlay();

				}
				page = new FormApptNew(this.MyApplication);

			} else if ( option == Button.O_REINVITE ) {

				optionLocator = Locators.ReinvitesAttendeesMenu;

				if ( optionLocator != null ) {

					this.zClickAt(optionLocator, "");
					SleepUtil.sleepSmall();
					this.zWaitForBusyOverlay();

				}
				page = null;

			} else if ( option == Button.O_REINVITE ) {

				optionLocator = Locators.ReinvitesAttendeesMenu;

				if ( optionLocator != null ) {

					this.zClickAt(optionLocator, "");
					SleepUtil.sleepSmall();
					this.zWaitForBusyOverlay();

				}
				page = null;

			} else if ( option == Button.O_REPLY_MENU ) {

				optionLocator = Locators.ReplyMenu;

				if ( optionLocator != null ) {
					this.zClickAt(optionLocator, "");
					SleepUtil.sleepSmall();
					this.zWaitForBusyOverlay();
				}

				page = new FormMailNew(this.MyApplication);

			} else if ( option == Button.O_REPLY_TO_ALL_MENU ) {

				optionLocator = Locators.ReplyToAllMenu;

				if ( optionLocator != null ) {
					this.zClickAt(optionLocator, "");
					SleepUtil.sleepSmall();
					this.zWaitForBusyOverlay();
				}

				page = new FormMailNew(this.MyApplication);

			} else if ( option == Button.O_FORWARD_MENU) {

				optionLocator = Locators.ForwardMenu;

				if ( optionLocator != null ) {
					this.zClickAt(optionLocator, "");
					SleepUtil.sleepLong();
					this.zWaitForBusyOverlay();
				}

				page = null;

			} else if ( option == Button.B_MOVE ) {

				// Use default actionLocator
				optionLocator = Locators.MoveMenu;

				page = new DialogMove(MyApplication,((AppAjaxClient) MyApplication).zPageCalendar);

				this.zClickAt(optionLocator,"");

				// FALL THROUGH

			} else if ( option == Button.O_SHOW_ORIGINAL_MENU ) {

				optionLocator = Locators.ShowOriginalMenu;

				page = new SeparateWindow(this.MyApplication);
				((SeparateWindow)page).zInitializeWindowNames();
				this.zClickAt(optionLocator,"");
				this.zWaitForBusyOverlay();

				return (page);

				//throw new HarnessException("implement action:"+ action +" option:"+ option);

			} else if ( option == Button.O_CREATE_A_COPY_MENU) {

				optionLocator = Locators.CreateACopyMenu;

				if ( optionLocator != null ) {
					this.zClickAt(optionLocator, "");
					SleepUtil.sleepSmall();
					this.zWaitForBusyOverlay();
				}

				if (com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.attendee.singleday.actions.CreateACopy.organizerTest == false ||
						com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.attendee.singleday.viewappt.CreateACopy.organizerTest == false) {
					page = new DialogInformational(DialogInformational.DialogWarningID.InformationalDialog, MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
				} else {
					page = null;
				}

			}else if ( option == Button.O_OPEN) {

				optionLocator = Locators.OpenMenu;
				this.zClickAt(optionLocator, "");
				SleepUtil.sleepMedium();
			    page = new FormApptNew(this.MyApplication);
				return page;

			} else {

				throw new HarnessException("implement action:"+ action +" option:"+ option);
			}

		} else if (action == Action.A_DOUBLECLICK) {

			this.sDoubleClick(locator);
			SleepUtil.sleepMedium();

			if (option == Button.O_NEEDS_ACTION_MENU || option == Button.O_ACCEPTED_MENU || option == Button.O_TENTATIVE_MENU || option == Button.O_DECLINED_MENU) {
				this.zClickAt(Locators.NeedsActionButton_ViewAppt, "10,20");

			} else if (option == Button.O_NEW_TAG || option == Button.O_REMOVE_TAG) {
				zWaitForElementAppear(Locators.NewTagMenu_ViewAppt); //http://bugzilla.zimbra.com/show_bug.cgi?id=79016

				if (option == Button.O_REMOVE_TAG) {
					this.zClickAt(Locators.TagButton_ViewAppt, "");
				}

			} else if (option == Button.O_EDIT || option == Button.O_CREATE_A_COPY_MENU ||	option == Button.O_REPLY_MENU ||
					option == Button.O_REPLY_TO_ALL_MENU || option == Button.O_FORWARD_MENU || option == Button.O_PROPOSE_NEW_TIME_MENU ||
					option == Button.O_DELETE_MENU || option == Button.O_SHOW_ORIGINAL_MENU) {
				zWaitForElementAppear(Locators.ActionsButton_ViewAppt);

				this.zClickAt(Locators.ActionsButton_ViewAppt, "");
			}

			SleepUtil.sleepSmall();

			if ( option == Button.O_NEEDS_ACTION_MENU ) {

				optionLocator = Locators.NeedsActionMenu_ViewAppt;

				this.zClickAt(optionLocator, "");
				this.zWaitForBusyOverlay();
				page = null;

			} else 	if ( (option == Button.O_ACCEPTED_MENU)) {

				optionLocator = Locators.AcceptedMenu_ViewAppt;

				this.zClickAt(optionLocator, "");
				this.zWaitForBusyOverlay();

				page = null;

			} else if ( option == Button.O_TENTATIVE_MENU ) {

				optionLocator = Locators.TentativeMenu_ViewAppt;

				this.zClickAt(optionLocator, "");
				this.zWaitForBusyOverlay();

				page = null;

			} else if ( option == Button.O_DECLINED_MENU ) {

				boolean isMultipleMenu = this.sIsElementPresent(Locators.DeclinedMenu3_ViewAppt);
				if (isMultipleMenu == true) {
					optionLocator = Locators.DeclinedMenu3_ViewAppt;
				} else {
					optionLocator = Locators.DeclinedMenu_ViewAppt;
				}

				this.zClickAt(optionLocator, "");
				this.zWaitForBusyOverlay();

				page = null;

			} else if ( option == Button.O_NEW_TAG ) {

				optionLocator = Locators.NewTagMenu_ViewAppt;

				this.zClickAt(optionLocator, "");
				this.zWaitForBusyOverlay();

				page = new DialogTag(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

			} else if ( option == Button.O_REMOVE_TAG ) {

				optionLocator = Locators.RemoveTagMenu_ViewAppt;

				this.zClickAt(optionLocator, "");
				this.zWaitForBusyOverlay();

				page = null;

			} else if ( option == Button.O_EDIT ) {

				optionLocator = Locators.EditMenu_ViewAppt;

				this.zClickAt(optionLocator, "");
				this.zWaitForBusyOverlay();

				page = new DialogWarning(DialogWarning.DialogWarningID.ZmMsgDialog, MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

			} else if ( option == Button.O_CREATE_A_COPY_MENU ) {

				optionLocator = Locators.CreateACopyMenu_ViewAppt;

				this.zClickAt(optionLocator, "");
				this.zWaitForBusyOverlay();

				page = new DialogInformational(DialogInformational.DialogWarningID.InformationalDialog, MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

			} else if ( option == Button.O_REPLY_MENU ) {

				optionLocator = Locators.ReplyMenu_ViewAppt;

				this.zClickAt(optionLocator, "");
				this.zWaitForBusyOverlay();

				page = new FormMailNew(this.MyApplication);

			} else if ( option == Button.O_REPLY_TO_ALL_MENU ) {

				optionLocator = Locators.ReplyToAllMenu_ViewAppt;

				this.zClickAt(optionLocator, "");
				this.zWaitForBusyOverlay();

				page = new FormMailNew(this.MyApplication);

			} else if ( option == Button.O_FORWARD_MENU ) {

				optionLocator = Locators.ForwardMenu_ViewAppt;

				this.zClickAt(optionLocator, "");
				this.zWaitForBusyOverlay();

				page = null;

			} else if ( option == Button.O_PROPOSE_NEW_TIME_MENU ) {

				optionLocator = Locators.ProposeNewTimeMenu_ViewAppt;

				this.zClickAt(optionLocator, "");
				this.zWaitForBusyOverlay();

				page = new FormApptNew(this.MyApplication);

			} else if ( option == Button.O_DELETE_MENU ) {

				optionLocator = Locators.DeleteMenu_ViewAppt;

				this.zClickAt(optionLocator, "");
				this.zWaitForBusyOverlay();

				page = new DialogConfirmationDeclineAppointment(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

			} else if ( option == Button.O_SHOW_ORIGINAL_MENU ) {

				optionLocator = Locators.ShowOriginalMenu_ViewAppt;

				page = new SeparateWindow(this.MyApplication);
				((SeparateWindow)page).zInitializeWindowNames();
				this.zClickAt(optionLocator, "");
				this.zWaitForBusyOverlay();

				return (page);

			} else {
				throw new HarnessException("implement me!  option = "+ option);
			}

		} else {
			throw new HarnessException("implement me!  action = "+ action);
		}

		if ( page != null ) {
			page.zWaitForActive();
		}

		Stafpostqueue sp = new Stafpostqueue();
		sp.waitForPostqueue();

		return (page);
	}

	private AbsPage zListItemMonthView(Action action, Button option, String subject) throws HarnessException {
		throw new HarnessException("implement me!");
	}

	@SuppressWarnings("unused")
	private AbsPage zListItemMonthView(Action action, Button option, Button subOption, String subject) throws HarnessException {
		System.out.println("zimbra");
		return null;
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

		if ( this.sIsElementPresent(itemsLocator +" td.appt_name:contains('"+ subject +"')")) {

			// Single occurrence locator
			locator = itemsLocator +" td.appt_name:contains('"+ subject +"')";

		} else if ( this.sIsElementPresent(itemsLocator +" td[class$='appt_new_name']:contains('"+ subject +"')")) {

			// Recurring appointment locator (might point to any visible instance)
			locator = itemsLocator +" td[class$='appt_new_name']:contains('"+ subject +"')";

		} else if ( this.sIsElementPresent(itemsLocator +" td.appt_allday_name>div:contains('"+ subject +"')")) {

			// All day single occurrence locator
			locator = itemsLocator +" td.appt_allday_name>div:contains('"+ subject +"')";

		} else if ( this.sIsElementPresent(itemsLocator +" td[id$='_responseActionSelectCell'] td[id$='_select_container']")) {

			// Read-only appointment locator
			locator = itemsLocator +" td[id$='_responseActionSelectCell'] td[id$='_select_container']";

		} else if (this.sIsElementPresent("css=table.calendar_month_day_table td.calendar_month_day_item span[id$='_subject']:contains('"+ subject +"')")) {

			locator = "css=table.calendar_month_day_table td.calendar_month_day_item span[id$='_subject']:contains('"+ subject +"')";

		} else if (this.zIsVisiblePerPosition(Locators.CalendarViewDayCSS, 0, 0) ) {

			locator = "css=div[class='calendar_allday_appt'] div[name='_allDayDivId']";

		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewWorkWeekCSS, 0, 0)) {

			locator = "css=div[class='calendar_hour_scroll'] td[class='calendar_grid_body_time_td'] div[id$='_10']";

		} else if (this.zIsVisiblePerPosition(Locators.CalendarViewWeekCSS, 0, 0) ) {

			locator = "css=div[class='calendar_hour_scroll'] td[class='calendar_grid_body_time_td'] div[id$='_10']";

		} else if (this.zIsVisiblePerPosition(Locators.CalendarViewMonthCSS, 0, 0) ) {

			locator = "css=td[class='calendar_month_cells_td']";

		}

		if (action == Action.A_RIGHTCLICK) {

			if (option == Button.O_SERIES_MENU) {
				optionLocator = Locators.SeriesMenu;

				if ( subOption == Button.O_ACCEPT_MENU ) {
					subOptionLocator = Locators.AcceptSeriesMenu;

				} else if ( subOption == Button.O_DECLINE_MENU ) {
					subOptionLocator = Locators.DeclineSeriesMenu;

				} else if ( subOption == Button.O_FORWARD_MENU ) {
					subOptionLocator = Locators.ForwardSeriesMenu;

				} else if ( subOption == Button.O_DELETE_MENU ) {
					subOptionLocator = Locators.DeleteSeriesMenu;

				} else if ( subOption == Button.O_CREATE_A_COPY_MENU) {
					subOptionLocator = Locators.CreateACopySeriesMenu;

				} else if ( subOption == Button.O_MOVE_MENU) {
					subOptionLocator = Locators.MoveSeriesMenu;

				}

			} else if (option == Button.O_INSTANCE_MENU) {
				optionLocator = Locators.InstanceMenu;

				if ( subOption == Button.O_ACCEPT_MENU ) {
					subOptionLocator = Locators.AcceptInstanceMenu;

				} else if ( subOption == Button.O_DECLINE_MENU ) {
					subOptionLocator = Locators.DeclineInstanceMenu;

				} else if ( subOption == Button.O_FORWARD_MENU ) {
					subOptionLocator = Locators.ForwardInstanceMenu;

				} else if ( subOption == Button.O_DELETE_MENU ) {
					subOptionLocator = Locators.DeleteInstanceMenu;

				} else if ( subOption == Button.O_CREATE_A_COPY_MENU) {
					subOptionLocator = Locators.CreateACopyInstanceMenu;
				}

			} else if (option == Button.O_VIEW_MENU) {
				optionLocator = Locators.ViewMenu;

				if ( subOption == Button.O_VIEW_DAY_SUB_MENU ) {
					subOptionLocator = Locators.ViewDaySubMenu;

				} else if ( subOption == Button.O_VIEW_WORK_WEEK_SUB_MENU ) {
					subOptionLocator = Locators.ViewWorkWeekSubMenu;

				} else if ( subOption == Button.O_VIEW_WEEK_SUB_MENU) {
					subOptionLocator = Locators.ViewWeekSubMenu;

				} else if ( subOption == Button.O_VIEW_MONTH_SUB_MENU ) {
					subOptionLocator = Locators.ViewMonthSubMenu;

				} else if ( subOption == Button.O_VIEW_LIST_SUB_MENU) {
					subOptionLocator = Locators.ViewListSubMenu;
				}
			}

			this.zRightClickAt(locator, "");
			this.zWaitForBusyOverlay();

			this.sFocus(optionLocator);
			this.sMouseOver(optionLocator);
			this.zWaitForBusyOverlay();

			if ( subOptionLocator == null ) {
				throw new HarnessException("implement action:"+ action +" option:"+ option +" suboption:" + subOption);
			}

			this.sClickAt(subOptionLocator, "");
			this.zWaitForBusyOverlay();

			// Since we are not going to "wait for active", insert
			// a small delay to make sure the dialog shows up
			// before the zIsActive() method is called
			SleepUtil.sleepMedium();

			// If the organizer deletes an appointment, you get "Send Cancellation" dialog
			page = new DialogConfirmDeleteOrganizer(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}

			// If an attendee deletes an appointment, you get a "Confirm Delete / Notify Organizer" dialog
			page = new DialogConfirmDeleteAttendee(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}

			// If an organizer deletes an appointment (no attendees), you get a "Confirm Delete" dialog
			page = new DialogConfirmDeleteAppointment(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}

			page = new DialogConfirmationDeleteAppointment(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}


			page = new DialogConfirmDeleteRecurringAppointment(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}

			page = new DialogInformational(DialogInformational.DialogWarningID.InformationalDialog, MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}

			page = new DialogMove(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}

			// No dialog
			return (null);

		} else if ( action == Action.A_DOUBLECLICK) {

			this.sDoubleClick(locator);
			this.zWaitForBusyOverlay();
			SleepUtil.sleepLong();

			/*page = new DialogOpenRecurringItem(Confirmation.OPENRECURRINGITEM, MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}*/

			DialogOpenRecurringItem openRecurring = new DialogOpenRecurringItem(null, MyApplication, null);
			openRecurring.zClickButton(Button.B_OPEN_THE_SERIES);
			openRecurring.zClickButton(Button.B_OK);

			page = new FormApptNew(this.MyApplication);
			if ( page.zIsActive() ) {
				return (page);
			}

		} else {
			throw new HarnessException("implement action:"+ action +" option:"+ option +" suboption:" + subOption);
		}

		if (subOption == Button.O_NEEDS_ACTION_MENU || subOption == Button.O_ACCEPTED_MENU || subOption == Button.O_TENTATIVE_MENU || subOption == Button.O_DECLINED_MENU) {
			this.sClickAt(Locators.NeedsActionButton_ViewAppt, "");

		} else if (subOption == Button.O_NEW_TAG || subOption == Button.O_REMOVE_TAG) {
			zWaitForElementAppear(Locators.NewTagMenu_ViewAppt); //http://bugzilla.zimbra.com/show_bug.cgi?id=79016

			if (subOption == Button.O_REMOVE_TAG) {
				this.zClickAt(Locators.TagButton_ViewAppt, "");
			}

		} else if (subOption == Button.O_EDIT || subOption == Button.O_CREATE_A_COPY_MENU ||	subOption == Button.O_REPLY_MENU ||
				subOption == Button.O_REPLY_TO_ALL_MENU || subOption == Button.O_FORWARD_MENU || subOption == Button.O_PROPOSE_NEW_TIME_MENU ||
				subOption == Button.O_DELETE_MENU || subOption == Button.O_SHOW_ORIGINAL_MENU) {
			zWaitForElementAppear(Locators.ActionsButton_ViewAppt);

			this.zClickAt(Locators.ActionsButton_ViewAppt, "");
		}

		SleepUtil.sleepSmall();

		if ( subOption == Button.O_NEEDS_ACTION_MENU ) {

			optionLocator = Locators.NeedsActionMenu_ViewAppt;

			this.zClickAt(optionLocator, "");
			this.zWaitForBusyOverlay();
			page = null;

		} else 	if ( (subOption == Button.O_ACCEPTED_MENU)) {

			optionLocator = Locators.AcceptedMenu_ViewAppt;

			this.zClickAt(optionLocator, "");
			this.zWaitForBusyOverlay();

			page = null;

		} else if ( subOption == Button.O_TENTATIVE_MENU ) {

			optionLocator = Locators.TentativeMenu_ViewAppt;

			this.zClickAt(optionLocator, "");
			this.zWaitForBusyOverlay();

			page = null;

		} else if ( subOption == Button.O_DECLINED_MENU ) {

			boolean isMultipleMenu = this.sIsElementPresent(Locators.DeclinedMenu3_ViewAppt);
			if (isMultipleMenu == true) {
				optionLocator = Locators.DeclinedMenu3_ViewAppt;
			} else {
				optionLocator = Locators.DeclinedMenu_ViewAppt;
			}

			this.zClickAt(optionLocator, "");
			this.zWaitForBusyOverlay();

			page = null;

		} else if ( subOption == Button.O_NEW_TAG ) {

			optionLocator = Locators.NewTagMenu_ViewAppt;

			this.zClickAt(optionLocator, "");
			this.zWaitForBusyOverlay();

			page = new DialogTag(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

		} else if ( subOption == Button.O_REMOVE_TAG ) {

			optionLocator = Locators.RemoveTagMenu_ViewAppt;

			this.zClickAt(optionLocator, "");
			this.zWaitForBusyOverlay();

			page = null;

		} else if ( subOption == Button.O_EDIT ) {

			optionLocator = Locators.EditMenu_ViewAppt;

			this.zClickAt(optionLocator, "");
			this.zWaitForBusyOverlay();

			page = new DialogWarning(DialogWarning.DialogWarningID.ZmMsgDialog, MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

		} else if ( subOption == Button.O_CREATE_A_COPY_MENU ) {

			optionLocator = Locators.CreateACopyMenu_ViewAppt;

			this.zClickAt(optionLocator, "");
			this.zWaitForBusyOverlay();

			page = new DialogInformational(DialogInformational.DialogWarningID.InformationalDialog, MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

		} else if ( subOption == Button.O_REPLY_MENU ) {

			optionLocator = Locators.ReplyMenu_ViewAppt;

			this.zClickAt(optionLocator, "");
			this.zWaitForBusyOverlay();

			page = new FormMailNew(this.MyApplication);

		} else if ( subOption == Button.O_REPLY_TO_ALL_MENU ) {

			optionLocator = Locators.ReplyToAllMenu_ViewAppt;

			this.zClickAt(optionLocator, "");
			this.zWaitForBusyOverlay();

			page = new FormMailNew(this.MyApplication);

		} else if ( subOption == Button.O_FORWARD_MENU ) {

			optionLocator = Locators.ForwardMenu_ViewAppt;

			this.zClickAt(optionLocator, "");
			this.zWaitForBusyOverlay();

			page = null;

		} else if ( subOption == Button.O_PROPOSE_NEW_TIME_MENU ) {

			optionLocator = Locators.ProposeNewTimeMenu_ViewAppt;

			this.zClickAt(optionLocator, "");
			this.zWaitForBusyOverlay();

			page = new FormApptNew(this.MyApplication);

		} else if ( subOption == Button.O_DELETE_MENU ) {

			optionLocator = Locators.DeleteMenu_ViewAppt;

			this.zClickAt(optionLocator, "");
			this.zWaitForBusyOverlay();

			page = new DialogConfirmationDeclineAppointment(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

		} else if ( subOption == Button.O_SHOW_ORIGINAL_MENU ) {

			optionLocator = Locators.ShowOriginalMenu_ViewAppt;

			page = new SeparateWindow(this.MyApplication);
			((SeparateWindow)page).zInitializeWindowNames();
			this.zClickAt(optionLocator, "");
			this.zWaitForBusyOverlay();

			return (page);

		} else {
			throw new HarnessException("implement me!  option = "+ option);
		}

		if ( locator == null || optionLocator == null) {
			throw new HarnessException("implement action:"+ action +" option:"+ option +" suboption:" + subOption);
		}

		this.zWaitForBusyOverlay();

		if ( page != null ) {
			page.zWaitForActive();
		}

		Stafpostqueue sp = new Stafpostqueue();
		sp.waitForPostqueue();

		SleepUtil.sleepMedium();

		return (page);

	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		if (button == Button.B_REFRESH) {
			return (((AppAjaxClient)this.MyApplication).zPageMain.zToolbarPressButton(Button.B_REFRESH));

		} else if (button == Button.B_NEW) {
			locator = "css=td#zb__NEW_MENU_title";
			page = new FormApptNew(this.MyApplication);

		} else if (button == Button.B_CLOSE) {
			locator = Locators.CloseButton;
			page = null;

		} else if (button == Button.B_SAVE) {
			locator = Locators.SaveButton;
			page = null;
			SleepUtil.sleepMedium();

		} else if (button == Button.B_SEND) {
			locator = Locators.SendButton;
			page = null;
			SleepUtil.sleepMedium();

		} else if (button == Button.B_DELETE) {

			locator = Locators.DeleteButton;
			this.zClickAt(locator, "");
			this.zWaitForBusyOverlay();

			SleepUtil.sleepMedium();

			// If the organizer deletes an appointment, you get "Send Cancellation" dialog
			page = new DialogConfirmDeleteOrganizer(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}

			// If an attendee deletes an appointment, you get a "Confirm Delete / Notify Organizer" dialog
			page = new DialogConfirmDeleteAttendee(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}

			// If an organizer deletes an appointment (no attendees), you get a "Confirm Delete" dialog
			page = new DialogConfirmDeleteAppointment(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}

			page = new DialogConfirmDeleteRecurringAppointment(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}

			// No dialog
			return (null);

		} else if (button == Button.B_SEND_WITH_CONFLICT) {
			locator = Locators.SendButton;
			this.zClickAt(locator, "");
			SleepUtil.sleepMedium();
			page = new DialogWarningConflictingResources(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

			if ( page.zIsActive() ) {
				SleepUtil.sleepMedium();
				return (page);
			}else{
				return null;
			}


		} else if (button == Button.B_SAVE_WITH_CONFLICT) {
			locator = Locators.OrganizerSaveButton;
			this.zClickAt(locator, "");
			SleepUtil.sleepMedium();
			page = new DialogWarningConflictingResources(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

			if ( page.zIsActive() ) {
				SleepUtil.sleepMedium();
				return (page);
			}else{
				return null;
			}

		} else if (button == Button.B_MONTH) {

			locator = Locators.MonthButton;
			page = null;

		} else if (button == Button.B_NEXT_PAGE) {

			locator = Locators.NextPage;
			page = null;

		} else if (button == Button.B_PREVIOUS_PAGE) {

			locator = Locators.PreviousPage;
			page = null;

		} else if (button == Button.O_LISTVIEW_TAG) {

			locator = "css=[id=zb__CLD__TAG_MENU_dropdown]";
			page = null;

		} else if (button == Button.O_LISTVIEW_NEWTAG) {

			locator = "id=calendar_newtag_title";
			page = null;

		} else if (button == Button.O_LISTVIEW_REMOVETAG) {

			locator = "id=calendar_removetag_title";
			page = null;

		} else if (button == Button.O_LISTVIEW_DAY) {

			locator = "css=div[id='ztb__CLD'] div[id='zb__CLD__DAY_VIEW'] td[id$='_title']";
			page = null;

		} else if (button == Button.O_LISTVIEW_WEEK) {

			locator = "css=div[id='ztb__CLD'] div[id='zb__CLD__WEEK_VIEW'] td[id$='_title']";
			page = null;

		} else if (button == Button.O_LISTVIEW_WORKWEEK) {

			locator = "css=div[id='ztb__CLD'] div[id='zb__CLD__WORK_WEEK_VIEW'] td[id$='_title']";
			page = new ApptWorkWeekView(MyApplication);

		} else if (button == Button.O_LISTVIEW_LIST) {

			locator = "css=div[id='ztb__CLD'] div[id='zb__CLD__CAL_LIST_VIEW'] td[id$='_title']";
			page = null;

		} else if (button == Button.O_LISTVIEW_MONTH) {

			locator = "css=div[id='ztb__CLD'] div[id='zb__CLD__MONTH_VIEW'] td[id$='_title']";
			page = null;

		} else if (button == Button.O_LISTVIEW_FREEBUSY) {

			locator = "css=div[id='ztb__CLD'] div[id='zb__CLD__FB_VIEW'] td[id$='_title']";
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

		this.zClickAt(locator, "10,10");

		Stafpostqueue sp = new Stafpostqueue();
		sp.waitForPostqueue();

		this.zWaitForBusyOverlay();

		if (page != null) {
			page.zWaitForActive();
		}

		SleepUtil.sleepSmall();
		return (page);
	}

	public AbsPage zKeyboardKeyEvent(int keyEvent) throws HarnessException {
		AbsPage page = null;

		if ( keyEvent == KeyEvent.VK_DELETE || keyEvent == KeyEvent.VK_BACK_SPACE ) {


			this.zKeyboard.zTypeKeyEvent(keyEvent);
			this.zWaitForBusyOverlay();

			SleepUtil.sleepMedium();

			// If the organizer deletes an appointment, you get "Send Cancellation" dialog
			page = new DialogConfirmDeleteOrganizer(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}

			// If an attendee deletes an appointment, you get a "Confirm Delete" dialog with "Notify Organizer?"
			page = new DialogConfirmDeleteAttendee(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}

			// If an attendee deletes an appointment, you get a "Confirm Delete" dialog
			page = new DialogConfirmDeleteAppointment(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}

			page = new DialogConfirmDeleteRecurringAppointment(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);
			if ( page.zIsActive() ) {
				return (page);
			}

			return (page);


		}

		this.zKeyboard.zTypeKeyEvent(keyEvent);

		// If the app is busy, wait for it to become active
		this.zWaitForBusyOverlay();

		return (page);
	}

	@Override
	public AbsPage zKeyboardShortcut(Shortcut shortcut) throws HarnessException {
		AbsPage page = null;

		if ( shortcut == Shortcut.S_ASSISTANT ) {

			page = new DialogAssistant(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

		} else if ( shortcut == Shortcut.S_DELETE ) {

			page = new DialogConfirmDeleteAppointment(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

		} else if (
				shortcut == Shortcut.S_MAIL_MOVETOTRASH ||
				shortcut == Shortcut.S_MAIL_HARDELETE ) {

			page = new DialogConfirmDeleteAppointment(MyApplication,  ((AppAjaxClient) MyApplication).zPageCalendar);

		} else if ( shortcut == Shortcut.S_NEWCALENDAR ) {

			page = new DialogCreateFolder(MyApplication, ((AppAjaxClient)MyApplication).zPageCalendar);

		}

		// Type the characters
		zKeyboard.zTypeCharacters(shortcut.getKeys());

		// If the app is busy, wait for it to become active
		this.zWaitForBusyOverlay();

		// If a page is specified, wait for it to become active
		if ( page != null ) {
			page.zWaitForActive();	// This method throws a HarnessException if never active
		}
		return (page);

	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
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
				page = new DialogCreateFolder(MyApplication, ((AppAjaxClient)MyApplication).zPageCalendar);

			} else {

				throw new HarnessException("No logic defined for pulldown " + pulldown + " and option " + option);

			}

		} else if (pulldown == Button.B_LISTVIEW) {
			return (this.zToolbarPressButton(option));

		} else {

			throw new HarnessException("No logic defined for pulldown " + pulldown + " and option " + option);

		}

		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator " + pulldownLocator + " not present!");
			}

			zClickAt(pulldownLocator, "0,0");

			zWaitForBusyOverlay();

			if (optionLocator != null) {

				zClick(optionLocator);
				zWaitForBusyOverlay();

			}

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

	@Override
	public void zNavigateTo() throws HarnessException {

		// Check if this page is already active.
		if (zIsActive()) {
			return;
		}

		// Make sure we are logged in
		if (!((AppAjaxClient) MyApplication).zPageMain.zIsActive()) {
			((AppAjaxClient) MyApplication).zPageMain.zNavigateTo();
		}

		logger.info("Navigate to " + this.myPageName());

		this.zClick(PageMain.Locators.zAppbarCal);
		SleepUtil.sleepSmall();

		this.zWaitForBusyOverlay();
		zWaitForElementPresent(Locators.CalendarFolder);

		zWaitForActive();

		logger.info("Navigated to "+ this.myPageName() + " page");

	}

	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the main page is active
		if (!((AppAjaxClient) MyApplication).zPageMain.zIsActive()) {
			((AppAjaxClient) MyApplication).zPageMain.zNavigateTo();
		}

		String locator = "css=div#ztb__CLD";

		boolean loaded = this.sIsElementPresent(locator);
		if (!loaded)
			return (false);

		boolean active = this.zIsVisiblePerPosition(locator, 0, 0);
		if (!active)
			return (false);

		return (true);

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

	private List<AppointmentItem> zSearchListGetAppointmentsListView() throws HarnessException {
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
		for (int i = 1; i <= count-1; i++) {

			// Add the new item to the list
			AppointmentItem item = parseListViewRow(listLocator + ":nth-of-type("+ i +")");
			items.add(item);
			logger.info(item.prettyPrint());

		}

		// Return the list of items
		return (items);

	}


	private AppointmentItem parseAppointmentRow(String rowLocator) throws HarnessException {

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
			return (zListGetAppointmentsListView());											// LIST
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewSearchListCSS, 0, 0) ) {
			return (zSearchListGetAppointmentsListView());
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewDayCSS, 0, 0) ) {
			return (zListGetAppointmentsGeneral(Locators.CalendarViewDayItemCSS));				// DAY
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewWeekCSS, 0, 0) ) {
			return (zListGetAppointmentsGeneral(Locators.CalendarViewWeekItemCSS));				// WEEK
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewWorkWeekCSS, 0, 0) ) {
			return (zListGetAppointmentsGeneral(Locators.CalendarViewWorkWeekItemCSS));			// WORK WEEK
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewMonthCSS, 0, 0) ) {
			return (zListGetAppointmentsMonthView());											// MONTH
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewScheduleCSS, 0, 0) ) {
			return (zListGetAppointmentsScheduleView());										// SCHEDULE
		} else if ( this.zIsVisiblePerPosition(Locators.CalendarViewFreeBusyCSS, 0, 0) ) {
			return (zListGetAppointmentsFreeBusyView());										// FREE/BUSY
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

		locator = "css=div[id='zmi__Calendar__TAG_MENU|MENU'] td[id$='_title']:contains('" + tagName + "')";
		System.out.println(locator);
		AbsPage page = null;

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Unable to determine locator : " + locator);
		}

		this.zClickAt(locator, "");
		this.zWaitForBusyOverlay();

		return (page);
	}

	public void zCreateTag(AppAjaxClient app, String tagName, int tagColor) throws HarnessException {

		app.zGetActiveAccount().soapSend(
				"<CreateTagRequest xmlns='urn:zimbraMail'>" +
					"<tag name='" + tagName + "' color='" + tagColor + "'/>" +
				"</CreateTagRequest>");
	}

	public void zWaitForElementAppear(String locator) throws HarnessException {
		boolean isElementPresent = false;
		for (int i=0; i<=10; i++) {
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

	public boolean zClickToRefreshOnceIfApptDoesntExists (String apptSubject) throws HarnessException {

		SleepUtil.sleepMedium();

		if ( sIsElementPresent("css=td.appt_name:contains('" + apptSubject + "')")) {
			return true;

		} else if ( sIsElementPresent("css=td.appt_30_name:contains('" + apptSubject + "')")) {
			return true;

		} else if ( sIsElementPresent("css=td.appt_new_name:contains('" + apptSubject + "')")) {
			return true;

		} else if ( sIsElementPresent("css=td.appt_allday_name:contains('" + apptSubject + "')")) {
			return true;

		} else {
			return false;
		}
	}


	public void zRightClickAddressBubble() throws HarnessException {

		SleepUtil.sleepVeryLong();
		this.sMouseOut(Locators.AttendeeBubbleAddr);
		this.sMouseOver(Locators.AttendeeBubbleAddr);
		this.sClick(Locators.AttendeeBubbleAddr);
		this.zRightClick(Locators.AttendeeBubbleAddr);
		SleepUtil.sleepVeryLong();

	}


}