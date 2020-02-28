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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.resources;

import java.util.Calendar;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.core.ExecuteHarnessMain;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.DialogWarningConflictingResources;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew.Locators;

public class ResourceConflictWhenOOO extends AjaxCore {

	public ResourceConflictWhenOOO() {
		logger.info("New "+ ResourceConflictWhenOOO.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs (ids = "102271")
	@Test (description = "Verify if OOO status of Location causes double booking",
			groups = { "application-bug" })

	public void LocationConflictWhenOOO_01() throws HarnessException {
		String tz, apptSubject1,apptSubject2 , apptAttendeeEmail2 , apptAttendeeEmail3 ;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject1 = "app" + ConfigProperties.getUniqueString();
		apptAttendeeEmail2 = ZimbraAccount.AccountB().EmailAddress;
		apptAttendeeEmail3 = ZimbraAccount.AccountC().EmailAddress;
		String apptLocation = ExecuteHarnessMain.locations.get("location1")[1];
		ZimbraAccount apptAttendee = ZimbraAccount.AccountA();

		String apptContent = ConfigProperties.getUniqueString();
		AppointmentItem appt = new AppointmentItem();
		apptSubject1 = ConfigProperties.getUniqueString();
		apptSubject2 = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 18, 0, 0);

		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
				"<m>"+
				"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='O' transp='O' allDay='0' name='"+ apptSubject1 +"' loc='"+ apptLocation+"'>" +
				"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
				"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
				"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
				"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendeeEmail2 + "' d='2'/>" +
				"<at cutype='RES' a='" + apptLocation + "' rsvp='1' role='REQ' url='" + apptLocation + "' ptst='NE' fb='O' fba='O'/>" +
				"</inv>" +
				"<e a='"+ apptLocation +"' t='t'/>" +
				"<mp content-type='text/plain'>" +
				"<content>"+ ConfigProperties.getUniqueString() +"</content>" +
				"</mp>" +
				"<su>"+ apptSubject1 +"</su>" +
				"</m>" +
		"</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject1), "Verify appointment displayed in current view");

		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>subject:("+ apptSubject1 +")" + " " + "content:" + apptSubject1 + "</query>"
				+	"</SearchRequest>");
		ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:comp", "fba"), "T", "");

		// Verify location free/busy status shows as psts=AC
		String locationStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptLocation +"']", "ptst");
		ZAssert.assertEquals(locationStatus, "AC", "Verify that the location status shows as 'ACCEPTED'");

		// Logout from organizer and Login as attendee
		app.zPageMain.zLogout();
		app.zPageLogin.zLogin(apptAttendee);
		this.startingPage.zNavigateTo();

		// Create appointment data
		appt.setSubject(apptSubject2);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 15, 0, 0));
		appt.setContent(apptContent);
		appt.setLocation(apptLocation);
		appt.setAttendeeName(apptAttendeeEmail3);

		// Create meeting which has location conflict with above created appointment
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		SleepUtil.sleepMedium();

		// Verify the compose page shows note below resource about conflicting resources
		ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent(Locators.ConflictResourceNote),  "Verify that the conflicting resource note appears on appt compose page");

		// Send invite
		DialogWarningConflictingResources dialog = (DialogWarningConflictingResources) app.zPageCalendar.zToolbarPressButton(Button.B_SEND_WITH_CONFLICT);
		String dialogContent = dialog.zGetResourceConflictWarningDialogText();
		ZAssert.assertTrue(dialogContent.contains("The selected resources/location cannot be scheduled for the following instances"), "Verify that the dialog shows expected text");
		ZAssert.assertTrue(dialogContent.contains(apptLocation + " (Busy)"), "Verify that the dialog shows location name on conflict warning");

		// Save appt with location conflict
		dialog.zPressButton(Button.B_SAVE_WITH_CONFLICT);

		// Verify that location with conflict and subject are present in the appointment
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject2 +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject2, "Subject: Verify the appointment data");
		ZAssert.assertEquals(appt.getLocation(), apptLocation, "Location: Verify the location is present in the appointment");

		// Verify location free/busy status shows as psts=DE
		String locationStatus2 = ZimbraAccount.AccountA().soapSelectValue("//mail:at[@a='"+ apptLocation +"']", "ptst");
		ZAssert.assertEquals(locationStatus2, "DE", "Verify that the location status shows as 'DECLINED'");

	}
}
