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

import java.awt.event.KeyEvent;
import java.util.*;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.DialogWarningConflictingResources;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;

public class CreateMeetingWithEquipmentConflict extends AjaxCore {

	public CreateMeetingWithEquipmentConflict() {
		logger.info("New "+ CreateMeetingWithEquipmentConflict.class.getCanonicalName());
		super.startingPage =  app.zPageCalendar;
	}


	@Bugs (ids = "102271")
	@Test (description = "Verify sending appt invite when Equipment resource has conflicts shows conflict dialog",
			groups = { "functional-skip", "application-bug" })

	public void CreateMeetingWithEquipmentConflict_01() throws HarnessException {

		// Creating object for meeting data
		ZimbraResource equipment = new ZimbraResource(ZimbraResource.Type.EQUIPMENT);

		String tz, apptSubject1,apptSubject2, apptAttendeeEmail;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptAttendeeEmail = ZimbraAccount.AccountA().EmailAddress;
		String apptEquipment = equipment.EmailAddress;

		String apptContent = ConfigProperties.getUniqueString();
		AppointmentItem appt = new AppointmentItem();
		apptSubject1 = ConfigProperties.getUniqueString();
		apptSubject2 = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);

		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject1 +"'>" +
                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     		"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendeeEmail + "' d='2'/>" +
                     		"<at cutype='RES' a='" + apptEquipment + "' rsvp='1' role='REQ' url='" + apptEquipment + "' ptst='AC'/>" +
                     	"</inv>" +
                     	"<e a='"+ apptEquipment +"' t='t'/>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ ConfigProperties.getUniqueString() +"</content>" +
                     	"</mp>" +
                     "<su>"+ apptSubject1 +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");
        app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Create appointment data
		appt.setSubject(apptSubject2);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 15, 0, 0));
		appt.setContent(apptContent);

		// Create meeting which has Equipment conflict with above created appointment
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zAddEquipmentFromScheduler(apptEquipment, KeyEvent.VK_ENTER);

		// Verify the conflicting resource dialog appears
		DialogWarningConflictingResources  dialog = (DialogWarningConflictingResources) app.zPageCalendar.zToolbarPressButton(Button.B_SEND_WITH_CONFLICT);
		String dialogContent = dialog.zGetResourceConflictWarningDialogText();
		ZAssert.assertTrue(dialogContent.contains("The selected resources/location cannot be scheduled for the following instances"), "Verify that the dialog shows expected text");
		ZAssert.assertTrue(dialogContent.contains(apptEquipment + " (Busy)"), "Verify that the dialog shows Equipment name on conflict warning");

        // Save appt with Equipment conflict
		dialog.zPressButton(Button.B_SAVE_WITH_CONFLICT);

        // Verify that equipment with conflict and subject are present in the appointment
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject2 +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject2, "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getEquipment().trim(), apptEquipment, "equipment: Verify the Equipment is present in the appointment");

		// Verify Equipment free/busy status shows as ptst=DE
		String equipmentStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptEquipment +"']", "ptst");
		ZAssert.assertEquals(equipmentStatus, "DE", "Verify that the Equipment status shows as 'DECLINED'");
    }


	@Bugs (ids = "ZCS-3343")
	@Test (description = "Verify Saving meeting invite when Equipment resource has conflicts shows conflict dialog",
			groups = { "functional", "L2" })

	public void CreateMeetingWithEquipmentConflict_02() throws HarnessException {

		// Creating object for meeting data
		ZimbraResource equipment = new ZimbraResource(ZimbraResource.Type.EQUIPMENT);

		String tz, apptSubject1,apptSubject2, apptAttendeeEmail;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptAttendeeEmail = ZimbraAccount.AccountA().EmailAddress;
		String apptEquipment = equipment.EmailAddress;

		String apptContent = ConfigProperties.getUniqueString();
		AppointmentItem appt = new AppointmentItem();
		apptSubject1 = ConfigProperties.getUniqueString();
		apptSubject2 = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);

		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject1 +"'>" +
                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     		"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendeeEmail + "' d='2'/>" +
                     		"<at cutype='RES' a='" + apptEquipment + "' rsvp='1' role='REQ' url='" + apptEquipment + "' ptst='AC'/>" +
                     	"</inv>" +
                     	"<e a='"+ apptEquipment +"' t='t'/>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ ConfigProperties.getUniqueString() +"</content>" +
                     	"</mp>" +
                     "<su>"+ apptSubject1 +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");
        app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Create appointment data
		appt.setSubject(apptSubject2);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 15, 0, 0));
		appt.setContent(apptContent);

		// Create meeting which has Equipment conflict for above created appointment
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zAddEquipmentFromScheduler(apptEquipment, KeyEvent.VK_ENTER);

		// Verify the conflicting resource dialog appears
		DialogWarningConflictingResources  dialog = (DialogWarningConflictingResources) app.zPageCalendar.zToolbarPressButton(Button.B_SAVE_WITH_CONFLICT);
		String dialogContent = dialog.zGetResourceConflictWarningDialogText();
		ZAssert.assertTrue(dialogContent.contains("The selected resources/location cannot be scheduled for the following instances"), "Verify that the dialog shows expected text");
		ZAssert.assertTrue(dialogContent.contains(apptEquipment + " (Busy)"), "Verify that the dialog shows equipment name on conflict warning");

		// Save appointment with Equipment conflict
        ZAssert.assertTrue(dialog.zIsActive(), "Verify 'Conflicting Resource' dialog is Open");
        dialog.zPressButton(Button.B_SAVE_WITH_CONFLICT);

        // Verify that modified Equipment and subject are present in the appointment
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject2 +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject2, "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getEquipment().trim(), apptEquipment, "equipment: Verify the equipment is present in the appointment");

		// Verify Equipment free/busy status shows as ptst=NE
		String equipmentStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptEquipment +"']", "ptst");
		ZAssert.assertEquals(equipmentStatus, "NE", "Verify that the Equipment status shows as 'NEEDS ACTION'");
	}


	@Test (description = "Verify Cancelling create appt when Equipment resource has conflicts shows conflict dialog",
			groups = { "functional", "L2" })

	public void CreateMeetingWithEquipmentConflict_03() throws HarnessException {

		// Creating object for meeting data
		ZimbraResource equipment = new ZimbraResource(ZimbraResource.Type.EQUIPMENT);

		String tz, apptSubject1,apptSubject2, apptAttendeeEmail;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptAttendeeEmail = ZimbraAccount.AccountA().EmailAddress;
		String apptEquipment = equipment.EmailAddress;

		String apptContent = ConfigProperties.getUniqueString();
		AppointmentItem appt = new AppointmentItem();
		apptSubject1 = ConfigProperties.getUniqueString();
		apptSubject2 = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 16, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 18, 0, 0);

		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject1 +"'>" +
                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     		"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendeeEmail + "' d='2'/>" +
                     		"<at cutype='RES' a='" + apptEquipment + "' rsvp='1' role='REQ' url='" + apptEquipment + "' ptst='AC'/>" +
                     	"</inv>" +
                     	"<e a='"+ apptEquipment +"' t='t'/>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ ConfigProperties.getUniqueString() +"</content>" +
                     	"</mp>" +
                     "<su>"+ apptSubject1 +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject1), "Verify appointment displayed in current view");

        // Create meeting which has Equipment conflict for above created appointment
		appt.setSubject(apptSubject2);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 16, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 19, 0, 0));
		appt.setContent(apptContent);

		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		SleepUtil.sleepSmall();
		apptForm.zFill(appt);
		apptForm.zAddEquipmentFromScheduler(apptEquipment, KeyEvent.VK_ENTER);
		SleepUtil.sleepMedium();

		// Verify the compose page shows note below resource about conflicting resources and conflicting resource dialog appears
		DialogWarningConflictingResources  dialog = (DialogWarningConflictingResources) app.zPageCalendar.zToolbarPressButton(Button.B_SEND_WITH_CONFLICT);
		String dialogContent = dialog.zGetResourceConflictWarningDialogText();
		ZAssert.assertTrue(dialogContent.contains("The selected resources/location cannot be scheduled for the following instances"), "Verify that the dialog shows expected text");
		ZAssert.assertTrue(dialogContent.contains(apptEquipment + " (Busy)"), "Verify that the dialog shows Equipment name on conflict warning");

        // Verify Canceling the 'Conflicting Resource' closes the dialog
		dialog.zPressButton(Button.B_CANCEL_CONFLICT);
        ZAssert.assertFalse(dialog.zIsActive(), "Verify 'Conflicting Resource' dialog is closed");

        // Verify new appt page is still open
        ZAssert.assertTrue(apptForm.zVerifyNewApptTabRemainsOpened(), "Verify new appt page is still open");

        // Verify that appointment subject is not modified
        AppointmentItem modifyAppt = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject2 +")");
        ZAssert.assertNull(modifyAppt, "Verify new appointment with conflicting Resource has not been created");
	}


	@Test (description = "Verify organizer can close modified appointment with Equipment Conflict",
			groups = { "functional", "L2" })

	public void CreateMeetingWithEquipmentConflict_04() throws HarnessException {

		// Creating object for meeting data
		ZimbraResource equipment = new ZimbraResource(ZimbraResource.Type.EQUIPMENT);

		String tz, apptSubject1,apptSubject2, apptAttendeeEmail;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptAttendeeEmail = ZimbraAccount.AccountA().EmailAddress;
		String apptEquipment = equipment.EmailAddress;

		String apptContent = ConfigProperties.getUniqueString();
		AppointmentItem appt = new AppointmentItem();
		apptSubject1 = ConfigProperties.getUniqueString();
		apptSubject2 = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);

		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject1 +"'>" +
                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     		"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendeeEmail + "' d='2'/>" +
                     		"<at cutype='RES' a='" + apptEquipment + "' rsvp='1' role='REQ' url='" + apptEquipment + "' ptst='AC'/>" +
                     	"</inv>" +
                     	"<e a='"+ apptEquipment +"' t='t'/>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ ConfigProperties.getUniqueString() +"</content>" +
                     	"</mp>" +
                     "<su>"+ apptSubject1 +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject1), "Verify appointment displayed in current view");

		// Create appointment data
		appt.setSubject(apptSubject2);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 15, 0, 0));
		appt.setContent(apptContent);

		// Create meeting which has Equipment conflict for above created appointment
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zAddEquipmentFromScheduler(apptEquipment, KeyEvent.VK_ENTER);
		SleepUtil.sleepSmall();

        // Verify that appointment subject is not modified
        AppointmentItem modifyAppt = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject2 +")");
        ZAssert.assertNull(modifyAppt, "Verify new appointment with conflicting Resource has not been created");
	}
}
