/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.day.singleday.tags;

import java.util.Calendar;
import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.TagItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogDeleteTag;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogConfirmDeleteAppointment;

@SuppressWarnings("unused")
public class DeleteTagAppointment extends AjaxCommonTest {

	public DeleteTagAppointment() {
		logger.info("New "+ DeleteTagAppointment.class.getCanonicalName());

		// All tests start at the Calendar page
		super.startingPage = app.zPageCalendar;

		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L;
		{
		    put("zimbraPrefCalendarInitialView", "day");
		}};
	}

	@Bugs(ids = "75711")
	@Test(description = "Apply tag to appointment and delete same tag in day view",
			groups = { "functional" })
	public void DeleteTagAppointment_01() throws HarnessException {
		
		// Create objects
		String tz, apptSubject, apptBody, tag1, renameTag1, tagID;
		TagItem tag;
		tz = ZTimeZone.TimeZoneEST.getID();
		tag1 = ZimbraSeleniumProperties.getUniqueString();
		renameTag1 = ZimbraSeleniumProperties.getUniqueString();
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptBody = ZimbraSeleniumProperties.getUniqueString();
		ZDate startDate = new ZDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		
		// Create new appointment
		AppointmentItem appt = AppointmentItem.createAppointmentSingleDay(app.zGetActiveAccount(), startDate, 120, null, apptSubject, apptBody, null, null);
        String apptId = appt.dApptID;

        // Create new tag and get tag ID
        app.zPageCalendar.zCreateTag(app, tag1, 8);
		tag = app.zPageCalendar.zGetTagItem(app.zGetActiveAccount(), tag1);
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		app.zGetActiveAccount().soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");;
		tagID = app.zGetActiveAccount().soapSelectValue("//mail:GetTagResponse//mail:tag[@name='"+ tag1 +"']", "id");
		
		// Apply tag to appointment
		app.zGetActiveAccount().soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>" + "<action id='" + apptId +"' op='tag' tn='"+ tag1 +"'/>" + "</ItemActionRequest>");
        
        // Delete the tag using the context menu
		DialogDeleteTag dialog = (DialogDeleteTag) app.zTreeCalendar.zTreeItem(
				Action.A_RIGHTCLICK, Button.B_DELETE, tag);
		dialog.zClickButton(Button.B_YES);
        app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
        
        // Verify appointment is not tagged
        app.zGetActiveAccount().soapSend("<GetAppointmentRequest xmlns='urn:zimbraMail' id='" + apptId + "'/>");
        ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:appt", "t"), null, "Verify appointment is not tagged");
		
	}
	
	@Test(description = "Apply tag to appointment and delete tagged appointment in day view",
			groups = { "functional" })
	public void DeleteTagAppointment_02() throws HarnessException {
		
		// Create objects
		String tz, apptSubject, apptBody, tag1, renameTag1, tagID;
		TagItem tag;
		tz = ZTimeZone.TimeZoneEST.getID();
		tag1 = ZimbraSeleniumProperties.getUniqueString();
		renameTag1 = ZimbraSeleniumProperties.getUniqueString();
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptBody = ZimbraSeleniumProperties.getUniqueString();
		ZDate startDate = new ZDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		
		// Create new appointment
		AppointmentItem appt = AppointmentItem.createAppointmentSingleDay(app.zGetActiveAccount(), startDate, 120, null, apptSubject, apptBody, null, null);
        String apptId = appt.dApptID;

        // Create new tag and get tag ID
        app.zPageCalendar.zCreateTag(app, tag1, 8);
		tag = app.zPageCalendar.zGetTagItem(app.zGetActiveAccount(), tag1);
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		app.zGetActiveAccount().soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");;
		tagID = app.zGetActiveAccount().soapSelectValue("//mail:GetTagResponse//mail:tag[@name='"+ tag1 +"']", "id");
		
		// Apply tag to appointment
		app.zGetActiveAccount().soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>" + "<action id='" + apptId +"' op='tag' tn='"+ tag1 +"'/>" + "</ItemActionRequest>");

		// Right click to appointment and delete it
        app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);
        DialogConfirmDeleteAppointment dlgConfirm = (DialogConfirmDeleteAppointment)app.zPageCalendar.zToolbarPressButton(Button.B_DELETE);
		dlgConfirm.zClickButton(Button.B_YES);
		SleepUtil.sleepMedium(); //testcase fails here due to timing issue so added sleep
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify appointment is deleted");
		
	}

	@Bugs(ids = "67035")
	@Test(description = "Apply tag to appointment, delete the tag and verify that the appointment is still visible",
			groups = { "functional" })
	public void DeleteTagAppointment_03() throws HarnessException {
		
		// Create objects
		String tz, apptSubject, apptBody, tag1, renameTag1, tagID;
		TagItem tag;
		tz = ZTimeZone.TimeZoneEST.getID();
		tag1 = ZimbraSeleniumProperties.getUniqueString();
		renameTag1 = ZimbraSeleniumProperties.getUniqueString();
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptBody = ZimbraSeleniumProperties.getUniqueString();
		ZDate startDate = new ZDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		
		// Create new appointment
		AppointmentItem appt = AppointmentItem.createAppointmentSingleDay(app.zGetActiveAccount(), startDate, 120, null, apptSubject, apptBody, null, null);
        String apptId = appt.dApptID;

        // Create new tag and get tag ID
        app.zPageCalendar.zCreateTag(app, tag1, 8);
		tag = app.zPageCalendar.zGetTagItem(app.zGetActiveAccount(), tag1);
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		app.zGetActiveAccount().soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");;
		tagID = app.zGetActiveAccount().soapSelectValue("//mail:GetTagResponse//mail:tag[@name='"+ tag1 +"']", "id");
		
		// Apply tag to appointment
		app.zGetActiveAccount().soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>" + "<action id='" + apptId +"' op='tag' tn='"+ tag1 +"'/>" + "</ItemActionRequest>");
        
        // Delete the tag using the context menu
		DialogDeleteTag dialog = (DialogDeleteTag) app.zTreeCalendar.zTreeItem(
				Action.A_RIGHTCLICK, Button.B_DELETE, tag);
		dialog.zClickButton(Button.B_YES);
        app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
        
        // Verify appointment is not tagged
        app.zGetActiveAccount().soapSend("<GetAppointmentRequest xmlns='urn:zimbraMail' id='" + apptId + "'/>");
        ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:appt", "t"), null, "Verify appointment is not tagged");
        
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Appointment not displayed in current view");
       
	}
	
}
