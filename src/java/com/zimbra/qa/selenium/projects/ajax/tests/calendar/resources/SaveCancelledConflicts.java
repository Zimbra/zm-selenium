/*
 * ***** BEGIN LICENSE BLOCK *****
 *
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
 *
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.resources;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ZTimeZone;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraResource;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.DialogWarningConflictingResources;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew.Locators;
import java.util.Calendar;
import org.testng.annotations.Test;

public class SaveCancelledConflicts extends AjaxCore {

	public SaveCancelledConflicts() {
		logger.info("New " + SaveCancelledConflicts.class.getCanonicalName());
		this.startingPage = this.app.zPageCalendar;
	}


	@Bugs (ids = "77991,75434")
	@Test (description = "Unable to save when cancelling conflicts", 
			groups = { "sanity" })

	public void SaveCancelledConflicts_01() throws HarnessException  {

		ZimbraResource location = new ZimbraResource(ZimbraResource.Type.LOCATION);
		String apptLocation = location.EmailAddress;
		String apptAttendeeEmail1 = ZimbraAccount.AccountA().EmailAddress;
		String apptAttendeeEmail2 = ZimbraAccount.AccountA().EmailAddress;
		String apptContent = ConfigProperties.getUniqueString();

		AppointmentItem appt = new AppointmentItem();
		String apptSubject1 = ConfigProperties.getUniqueString();
		String apptSubject2 = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 15, 0, 0);

		// Get local timezone value
		String tz = ZTimeZone.getLocalTimeZone().getID();

		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" + "<m>"
						+ "<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='O' transp='O' allDay='0' name='"
						+ apptSubject1 + "' loc='" + apptLocation + "'>" + "<s d='"
						+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() + "' tz='" + tz + "'/>" + "<e d='"
						+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() + "' tz='" + tz + "'/>" + "<or a='"
						+ app.zGetActiveAccount().EmailAddress + "'/>" + "<at role='REQ' ptst='NE' rsvp='1' a='"
						+ apptAttendeeEmail1 + "' d='2'/>" + "<at cutype='RES' a='" + apptLocation
						+ "' rsvp='1' role='REQ' url='" + apptLocation + "' ptst='NE' fb='O' fba='O'/>" + "</inv>"
						+ "<e a='" + apptLocation + "' t='t'/>" + "<mp content-type='text/plain'>" + "<content>"
						+ ConfigProperties.getUniqueString() + "</content>" + "</mp>" + "<su>" + apptSubject1 + "</su>"
						+ "</m>" + "</CreateAppointmentRequest>");
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		appt.setSubject(apptSubject2);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 15, 0, 0));
		appt.setContent(apptContent);
		appt.setLocation(apptLocation);
		appt.setAttendeeName(apptAttendeeEmail2);
		appt.setRecurring("EVERYDAY", "");

		// Create series appointment
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		SleepUtil.sleepMedium();

		// Verify the compose page shows note below resource about conflicting resources
		ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent(Locators.ConflictResourceNote),  "Verify that the conflicting resource note appears on appt compose page");

		// Send invite
		DialogWarningConflictingResources  dialog = (DialogWarningConflictingResources) app.zPageCalendar.zToolbarPressButton(Button.B_SEND_WITH_CONFLICT);
		String dialogContent = dialog.zGetResourceConflictWarningDialogText();
		ZAssert.assertTrue(dialogContent.contains("The selected resources/location cannot be scheduled for the following instances"), "Verify that the dialog shows expected text");
		ZAssert.assertTrue(dialogContent.contains(apptLocation + " (Busy)"), "Verify that the dialog shows location name on conflict warning");
		dialog.zPressButton(Button.B_CANCEL_INSTANCE_LINK);
		dialog.zPressButton(Button.B_SAVE_WITH_CONFLICT);

		// Verify that the appointment is saved
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject2 +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject2, "Subject: Verify the appointment data");

		String instance = app.zGetActiveAccount().soapSelectValue("//mail:inst[@ex='1']", "id");
		ZAssert.assertEquals(instance, null, "Verify that exception is not created");

	}
}