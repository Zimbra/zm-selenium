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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.trash;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Checkbox;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ZTimeZone;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;

public class GetAppointment extends AjaxCommonTest {

	public GetAppointment() {
		logger.info("New "+ GetAppointment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test (description = "Verify the presence of appointment in Trash after deletion",
			groups = { "smoke", "L0" })

	public void GetAppointment_01() throws HarnessException {

		// Appointment data
		String apptSubject = ConfigProperties.getUniqueString();
		String location = "location" + ConfigProperties.getUniqueString();
		String content = "content" + ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 15, 0, 0);
		ZDate endUTC  = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 16, 0, 0);

		// If current day of the week is Sunday, create an appointment on Monday so that it remains in the current week view even after time zone adjustment.
		if ( now.get(Calendar.DAY_OF_WEEK) == 1 ) {
			startUTC.addDays(1);
			endUTC.addDays(1);
		}

		// Get local timezone value
		String tz = ZTimeZone.getLocalTimeZone().getID();

		// Create an appointment
		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
						+		"<m>"
						+			"<inv>"
						+				"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='0' name='"+ apptSubject +"' loc='"+ location +"' >"
						+					"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
						+					"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
						+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
						+					"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.AccountA().EmailAddress + "' d='2'/>"
						+				"</comp>"
						+			"</inv>"
						+			"<su>"+ apptSubject + "</su>"
						+			"<mp ct='text/plain'>"
						+				"<content>"+ content +"</content>"
						+			"</mp>"
						+		"</m>"
						+	"</CreateAppointmentRequest>");

		// Refresh the view
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Select the appointment
		app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);

		// Press Delete toolbar button
		DialogWarning dialog = (DialogWarning)app.zPageCalendar.zToolbarPressButton(Button.B_DELETE);

		// Click Send Cancellation
		dialog.zClickButton(Button.B_YES);

		// Verify meeting disappears from the view
		ZAssert.assertFalse(app.zPageCalendar.zIsAppointmentExists(apptSubject), "Verify appointment is deleted from organizer's calendar");

		// Enable trash
		app.zPageCalendar.zCheckboxSet(Checkbox.C_TRASH, true);

		// Verify the presence of appointment in Trash
		ZAssert.assertTrue(app.zPageCalendar.zIsAppointmentPresentInTrash(apptSubject), "Verify appointment is present in Trash!");
	}
}