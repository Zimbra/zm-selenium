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

import java.awt.AWTException;
import java.util.Calendar;
import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.TagItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

@SuppressWarnings("unused")
public class UnTagAppointment extends AjaxCommonTest {

	public UnTagAppointment() {
		logger.info("New "+ UnTagAppointment.class.getCanonicalName());

		// All tests start at the Calendar page
		super.startingPage = app.zPageCalendar;

		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L;
		{
		    put("zimbraPrefCalendarInitialView", "day");
		}};
	}

	
	@Test(description = "Untag an appointment using toolbar button in day view",
			groups = { "functional" })
	public void UnTagAppointment_01() throws HarnessException {
		
		// Create objects
		String apptSubject, apptBody, tag1, tagID;
		TagItem tag;
		tag1 = ZimbraSeleniumProperties.getUniqueString();
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptBody = ZimbraSeleniumProperties.getUniqueString();
		ZDate startDate = new ZDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 0, 0);
		
		// Create new appointment
		AppointmentItem appt = AppointmentItem.createAppointmentSingleDay(app.zGetActiveAccount(), startDate, 120, null, apptSubject, apptBody, null, null);
        String apptId = appt.dApptID;

        // Create new tag and get tag ID   
		app.zPageCalendar.zCreateTag(app, tag1, 2);
		tag = app.zPageCalendar.zGetTagItem(app.zGetActiveAccount(), tag1);
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		app.zGetActiveAccount().soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");;
		tagID = app.zGetActiveAccount().soapSelectValue("//mail:GetTagResponse//mail:tag[@name='"+ tag1 +"']", "id");
		
		// Remove tag from appointment using toolbar button		
		app.zGetActiveAccount().soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>" + "<action id='" + apptId +"' op='tag' tn='"+ tag1 +"'/>" + "</ItemActionRequest>");
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);
        app.zPageCalendar.zToolbarPressButton(Button.O_LISTVIEW_TAG);
        app.zPageCalendar.zToolbarPressButton(Button.O_LISTVIEW_REMOVETAG);
        SleepUtil.sleepSmall();
        
        // Verify appointment is not tagged
        app.zGetActiveAccount().soapSend("<GetAppointmentRequest xmlns='urn:zimbraMail' id='" + apptId + "'/>");
        ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:appt", "t"), null, "Verify appointment is not tagged");
		
		// Verify search result from UI
		app.zTreeCalendar.zTreeItem(Action.A_LEFTCLICK, tag1);
		SleepUtil.sleepMedium();
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify search result after clicking tag");
		app.zGetActiveAccount().soapSend("<TagActionRequest xmlns='urn:zimbraMail'>" + "<action op='delete' id='" + tagID +"'/>" + "</TagActionRequest>");
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startDate.addDays(-10).toMillis() +"' calExpandInstEnd='"+ startDate.addDays(10).toMillis() +"'>"
			+		"<query>"+ apptSubject +"</query>"
			+	"</SearchRequest>");
	
	String invId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		
		app.zGetActiveAccount().soapSend("<CancelAppointmentRequest xmlns='urn:zimbraMail' id='" + invId + "' comp='0'>"
               + "<m>"
               +   "<su>Cancelled" + apptSubject + "</su>"
               +    "<mp content-type='text/plain'>"
               +        "<content> Action: Cancelled" + apptSubject + "</content>"
               +    "</mp>"
               + "</m>"
           + "</CancelAppointmentRequest>");
	    this.app.zPageLogin.zNavigateTo();
	    this.startingPage.zNavigateTo();
		
		
	}
	
	@Test(description = "Untag tagged appointment using context menu in day view",
			groups = { "functional" })
	public void UnTagAppointment_02() throws HarnessException, AWTException {
		
		// Create objects
		String apptSubject, apptBody, tag1, tagID;
		TagItem tag;
		tag1 = ZimbraSeleniumProperties.getUniqueString();
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptBody = ZimbraSeleniumProperties.getUniqueString();
		ZDate startDate = new ZDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 0, 0);
		
		// Create new appointment
		AppointmentItem appt = AppointmentItem.createAppointmentSingleDay(app.zGetActiveAccount(), startDate, 120, null, apptSubject, apptBody, null, null);
        String apptId = appt.dApptID;
        
        // Create new tag and get tag ID      
		app.zPageCalendar.zCreateTag(app, tag1, 4);
		tag = app.zPageCalendar.zGetTagItem(app.zGetActiveAccount(), tag1);
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		app.zGetActiveAccount().soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");;
		tagID = app.zGetActiveAccount().soapSelectValue("//mail:GetTagResponse//mail:tag[@name='"+ tag1 +"']", "id");
		app.zGetActiveAccount().soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>" + "<action id='" + apptId +"' op='tag' tn='"+ tag1 +"'/>" + "</ItemActionRequest>");
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		SleepUtil.sleepMedium();
		
		// Remove tag from appointment using context menu
        app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, apptSubject);
        app.zPageCalendar.zMouseOver(Button.B_TAGAPPOINTMENTMENU);
        SleepUtil.sleepSmall(); //reqires to select sub menu otherwise test fails here
        app.zPageCalendar.zToolbarPressButton(Button.O_TAG_APPOINTMENT_REMOVE_TAG_SUB_MENU);
        SleepUtil.sleepSmall(); // give time to soap verification because it runs fast and test fails
        
        // Verify appointment is not tagged
        app.zGetActiveAccount().soapSend("<GetAppointmentRequest xmlns='urn:zimbraMail' id='" + apptId + "'/>");
        ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:appt", "t"), null, "Verify appointment is not tagged");
        
	}
}
