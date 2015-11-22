
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.recurring.modify;

import java.util.*;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.*;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogConfirmationDeleteAppointment;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew.Field;

public class ModifyInstanceModifySeries extends CalendarWorkWeekTest {

	public ModifyInstanceModifySeries() {
		logger.info("New "+ ModifyInstanceModifySeries.class.getCanonicalName());
		super.startingPage =  app.zPageCalendar;
		super.startingAccountPreferences = null;
	}
	
	@Bugs(ids = "66800")	
	@Test(description = "Delete series from third instance and onwards", 
			groups = { "functional" })
	public void ModifyInstanceModifySeries_01() throws HarnessException {

		// ------------------------ Test data ------------------------------------

		Calendar now = this.calendarWeekDayUTC;
		String tz = ZTimeZone.TimeZoneEST.getID();
		AppointmentItem appt = new AppointmentItem();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 8, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
		String apptSubject = ZimbraSeleniumProperties.getUniqueString();
		String apptBody = ZimbraSeleniumProperties.getUniqueString();
		
		ZDate modifiedStartUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
		ZDate modifiedEndUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		String modifiedSecondInstanceBody = ZimbraSeleniumProperties.getUniqueString();
		String modifiedFourthInstanceBody = ZimbraSeleniumProperties.getUniqueString();
		
		
		// --------------- Creating invitation (organizer) ----------------------------

		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
					"<m>"+
						"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
							"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.AccountA().EmailAddress + "'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='WEE'>" +
										"<interval ival='1'/>" +
										"<count num='12'/>" +
									"</rule>" +
								"</add>" +
							"</recur>" +
						"</inv>" +
						"<e a='"+ ZimbraAccount.AccountA().EmailAddress +"' t='t'/>" +
						"<mp content-type='text/plain'>" +
							"<content>"+ apptBody +"</content>" +
						"</mp>" +
						"<su>"+ apptSubject +"</su>" +
					"</m>" +
				"</CreateAppointmentRequest>");
		
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		DialogOpenRecurringItem openRecurring = (DialogOpenRecurringItem) app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
		openRecurring.zClickButton(Button.B_OPEN_THIS_INSTANCE);
		openRecurring.zClickButton(Button.B_OK);
		
		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zVerifyDisabledControlInOpenInstance();
		
		apptForm.zFillField(Field.StartTime, modifiedStartUTC);
		apptForm.zFillField(Field.EndTime, modifiedEndUTC);
		apptForm.zFillField(Field.Body, modifiedSecondInstanceBody);
		apptForm.zFill(appt);
        apptForm.zToolbarPressButton(Button.B_SEND);
        SleepUtil.sleepMedium();
	
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);

		openRecurring = (DialogOpenRecurringItem) app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
		openRecurring.zClickButton(Button.B_OPEN_THIS_INSTANCE);
		openRecurring.zClickButton(Button.B_OK);
		
		apptForm = new FormApptNew(app);
		apptForm.zVerifyDisabledControlInOpenInstance();
		
		apptForm.zFillField(Field.Body, modifiedFourthInstanceBody);
		apptForm.zFill(appt);
        apptForm.zToolbarPressButton(Button.B_SEND);
        SleepUtil.sleepMedium();
		
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		
		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_SERIES_MENU, Button.O_DELETE_MENU, apptSubject);
		DialogConfirmationDeleteAppointment dialog = (DialogConfirmationDeleteAppointment) new DialogConfirmationDeleteAppointment(app, app.zPageCalendar);
		dialog.zClickButton(Button.B_DELETE_THIS_AND_FUTURE_OCCURRENCES);	
		SleepUtil.sleepMedium();
        dialog.zClickButton(Button.B_YES);
        DialogConfirmDeleteOrganizer dialog2 = (DialogConfirmDeleteOrganizer) new DialogConfirmDeleteOrganizer(app, app.zPageCalendar);
		dialog2.zClickButton(Button.B_SEND_CANCELLATION);
		SleepUtil.sleepVeryLong();
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>"+ apptSubject +"</query>"
			+	"</SearchRequest>");
	
		String organizerInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(organizerInvId, "Verify that appointment is deleted");
		
	}

	@Bugs(ids = "66800")	
	@Test(description = "Delete series from third instance and onwards", 
			groups = { "functional" })
	public void ModifyInstanceModifySeries_02() throws HarnessException {

		// ------------------------ Test data ------------------------------------

		Calendar now = this.calendarWeekDayUTC;
		String tz = ZTimeZone.TimeZoneEST.getID();
		AppointmentItem appt = new AppointmentItem();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		String apptSubject = ZimbraSeleniumProperties.getUniqueString();
		String apptBody = ZimbraSeleniumProperties.getUniqueString();
		
		ZDate modifiedStartUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
		ZDate modifiedEndUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		String modifiedSecondInstanceBody = ZimbraSeleniumProperties.getUniqueString();
		String modifiedSixthInstanceBody = ZimbraSeleniumProperties.getUniqueString();
		
		
		// --------------- Creating invitation (organizer) ----------------------------

		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
					"<m>"+
						"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
							"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.AccountA().EmailAddress + "'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='WEE'>" +
										"<interval ival='1'/>" +
										"<count num='12'/>" +
									"</rule>" +
								"</add>" +
							"</recur>" +
						"</inv>" +
						"<e a='"+ ZimbraAccount.AccountA().EmailAddress +"' t='t'/>" +
						"<mp content-type='text/plain'>" +
							"<content>"+ apptBody +"</content>" +
						"</mp>" +
						"<su>"+ apptSubject +"</su>" +
					"</m>" +
				"</CreateAppointmentRequest>");
		
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Appointment not displayed in current view");

		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		DialogOpenRecurringItem openRecurring = (DialogOpenRecurringItem) app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
		openRecurring.zClickButton(Button.B_OPEN_THIS_INSTANCE);
		openRecurring.zClickButton(Button.B_OK);
		
		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zVerifyDisabledControlInOpenInstance();
		
		apptForm.zFillField(Field.StartTime, modifiedStartUTC);
		apptForm.zFillField(Field.EndTime, modifiedEndUTC);
		apptForm.zFillField(Field.Body, modifiedSecondInstanceBody);
		apptForm.zFill(appt);
        apptForm.zToolbarPressButton(Button.B_SEND);
        SleepUtil.sleepMedium();
	
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);

		openRecurring = (DialogOpenRecurringItem) app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
		openRecurring.zClickButton(Button.B_OPEN_THIS_INSTANCE);
		openRecurring.zClickButton(Button.B_OK);
		
		apptForm = new FormApptNew(app);
		apptForm.zVerifyDisabledControlInOpenInstance();
		
		apptForm.zFillField(Field.Body, modifiedSixthInstanceBody);
		apptForm.zFill(appt);
        apptForm.zToolbarPressButton(Button.B_SEND);
        SleepUtil.sleepMedium();
		
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		openRecurring = (DialogOpenRecurringItem) app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
		openRecurring.zClickButton(Button.B_OPEN_THE_SERIES);
		openRecurring.zClickButton(Button.B_OK);
		
		apptForm = new FormApptNew(app);	
		apptForm.zRepeat(Button.O_EVERY_WEEK_MENU, Button.B_EVERY_X_RADIO_BUTTON, "", Button.B_END_BY_DATE_RADIO_BUTTON, "01/01/2020");

		DialogConfirmRemoveAllExceptions dialog = (DialogConfirmRemoveAllExceptions)apptForm.zToolbarPressButton(Button.B_SEND);
		dialog.zClickButton(Button.B_YES);
        SleepUtil.sleepMedium();
        
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>"+ apptSubject +"</query>"
			+	"</SearchRequest>");
	
		String organizerInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(organizerInvId, "Verify that all exceptions are removed");
		
	}
	
}
