/*
* ***** BEGIN LICENSE BLOCK *****
* Zimbra Collaboration Suite Server
* Copyright (C) 2016 Synacor, Inc.
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
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.DialogOpenRecurringItem;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew.Field;

public class ChangeLocationOfOneInstance extends AjaxCore {

	public ChangeLocationOfOneInstance() {
		logger.info("New "+ ChangeLocationOfOneInstance.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs (ids = "52682")
	@Test (description = "Can't change location of one instance", 
			groups = { "functional", "L2" } )

	public void ChangeLocationOfOneInstance_01() throws HarnessException {

		// Create a meeting
		ZimbraResource location = new ZimbraResource(ZimbraResource.Type.LOCATION);

		String tz = ZTimeZone.getLocalTimeZone().getID();
		String apptSubject = ConfigProperties.getUniqueString();
		String apptAttendee = ZimbraAccount.AccountA().EmailAddress;
		String apptLocation = location.EmailAddress;

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 8, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);

		app.zGetActiveAccount().soapSend(
	            "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
	                 "<m>"+
	                 	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
	                 		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
	                 		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
	                 		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
	                 		"<at role='REQ' ptst='AC' rsvp='1' a='" + apptAttendee + "' d='2'/>" +
							"<recur>" +
							"<add>" +
								"<rule freq='DAI'>" +
									"<interval ival='1'/>" +
								"</rule>" +
							"</add>" +
						"</recur>" +
	               	"</inv>" +
	                 	"<e a='"+ ZimbraAccount.AccountA().EmailAddress +"' t='t'/>" +
	                 	"<mp content-type='text/plain'>" +
	                 		"<content>"+ ConfigProperties.getUniqueString() +"</content>" +
	                 	"</mp>" +
	                 "<su>"+ apptSubject +"</su>" +
	                 "</m>" +
	           "</CreateAppointmentRequest>");

		// Verify appointment exists in current view
		ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

		// --------------- Modify first instance by adding location ----------------------------------------------------
		DialogOpenRecurringItem openRecurring = (DialogOpenRecurringItem) app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
		openRecurring.zPressButton(Button.B_OPEN_THIS_INSTANCE);
		openRecurring.zPressButton(Button.B_OK);

		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zVerifyDisabledControlInOpenInstance();
	    apptForm.zFillField(Field.Location, apptLocation);
	    apptForm.zSubmitWithResources();

		// Organizer: Search for the appointment
	    app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ apptSubject +"</query>"
				+	"</SearchRequest>");

		String exceptionLocation = app.zGetActiveAccount().soapSelectValue("//mail:inst[@ex='1']", "loc");

		ZAssert.assertStringContains(exceptionLocation, apptLocation, "Location: Verify the appointment data");

		// Verify location free/busy status is "Accepted"
		String locationStatus = app.zGetActiveAccount().soapSelectValue("//mail:inst[@ex='1']", "ptst");
		ZAssert.assertEquals(locationStatus, "AC", "Verify location status shows accepted");

	}

}