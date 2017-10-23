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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.trash;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Checkbox;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ZTimeZone;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekTest;

public class MoveAppointment extends CalendarWorkWeekTest {

	public MoveAppointment() {
		logger.info("New "+ MoveAppointment.class.getCanonicalName());

		// All tests start at the Calendar page
		super.startingPage = app.zPageCalendar;
	}

	@Test( description = "Verify the move of an appointment from trash to Calendar",
			groups = { "sanity", "L1" })

	public void MoveAppointment_01() throws HarnessException {

		// Create the appointment on the server
		String apptSubject = ConfigProperties.getUniqueString();
		String location = "location" + ConfigProperties.getUniqueString();
		String content = "content" + ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);

		// Get local timezone value
		String tz = ZTimeZone.getLocalTimeZone().getID();

		FolderItem Calendar = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Calendar);

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

		String invId = app.zGetActiveAccount().soapSelectValue("//mail:CreateAppointmentResponse", "invId");

		app.zGetActiveAccount().soapSend(
				"<CancelAppointmentRequest xmlns='urn:zimbraMail' id='" + invId +"' comp='0'>"
						+       "<m>"
						+			"<su>Cancelled: "+ apptSubject + "</su>"
						+			"<mp ct='text/plain'>"
						+				"<content>"+ content +"</content>"
						+			"</mp>"
						+		"</m>"
						+	"</CancelAppointmentRequest>");

		// Enable trash
		app.zPageCalendar.zCheckboxSet(Checkbox.C_TRASH, true);

		// Verify the presence of appointment in Trash
		ZAssert.assertTrue(app.zPageCalendar.zIsAppointmentPresentInTrash(apptSubject), "Verify appointment is present in Trash!");

		// Select the appointment
		app.zPageCalendar.zListItemTrashView(Action.A_LEFTCLICK, apptSubject);

		// Move appointment using toolbar move menu
		app.zPageCalendar.zToolbarPressPulldown(Button.B_MOVE, Calendar);

		// Verify meeting disappears from the view
		ZAssert.assertTrue(app.zPageCalendar.zIsAppointmentExists(apptSubject), "Verify appointment is present in  organizer's calendar view");

		// Verify the presence of appointment in Trash
		ZAssert.assertFalse(app.zPageCalendar.zIsAppointmentPresentInTrash(apptSubject), "Verify appointment is not present in Trash!");

	}

}
