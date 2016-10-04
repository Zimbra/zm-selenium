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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.calendar;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ZTimeZone;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogConfirmationDeclineAppointment;
import java.util.Calendar;
import org.testng.annotations.Test;

public class ShowHideDeclinedMeetings extends CalendarWorkWeekTest {

  public ShowHideDeclinedMeetings()
  {
    logger.info("New " + ShowHideDeclinedMeetings.class.getCanonicalName());
    this.startingPage = this.app.zPageCalendar;
    this.startingAccountPreferences.put("zimbraFeatureGroupCalendarEnabled", "FALSE");
  }
  
  @Bugs(ids="27661")
  @Test( description="Allow declined meetings to be hidden/shown based on preference", groups={"functional"} )
  
  public void ShowHideDeclinedMeetings_01() throws HarnessException {
	  
    String apptSubject = ConfigProperties.getUniqueString();
    
    Calendar now = this.calendarWeekDayUTC;
    ZDate startUTC = new ZDate(now.get(1), now.get(2) + 1, now.get(5), 12, 0, 0);
    ZDate endUTC = new ZDate(now.get(1), now.get(2) + 1, now.get(5), 14, 0, 0);

    ZimbraAdminAccount.GlobalAdmin().soapSend(
	  "<ModifyAccountRequest xmlns='urn:zimbraAdmin'><id>" + 
	  this.app.zGetActiveAccount().ZimbraId + "</id>" + 
	  "<a n='zimbraPrefCalendarShowDeclinedMeetings'>FALSE</a>" + 
	  "</ModifyAccountRequest>");

    this.app.zPageLogin.zNavigateTo();
    this.startingPage.zNavigateTo();

	ZimbraAccount.AccountA().soapSend(
			"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
			+		"<m>"
			+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
			+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
			+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
			+				"<or a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>"
			+				"<at role='REQ' ptst='NE' rsvp='1' a='" + app.zGetActiveAccount().EmailAddress + "'/>"
			+			"</inv>"
			+			"<e a='"+ app.zGetActiveAccount().EmailAddress +"' t='t'/>"
			+			"<su>"+ apptSubject +"</su>"
			+			"<mp content-type='text/plain'>"
			+				"<content>content</content>"
			+			"</mp>"
			+		"</m>"
			+	"</CreateAppointmentRequest>");

	// Verify appointment exists in current view
	ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

    this.app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_DECLINE_MENU, apptSubject);
    
    DialogConfirmationDeclineAppointment declineAppt = new DialogConfirmationDeclineAppointment(this.app, this.app.zPageCalendar);
    declineAppt.zClickButton(Button.B_DONT_NOTIFY_ORGANIZER);
    declineAppt.zClickButton(Button.B_YES);
    SleepUtil.sleepMedium();
    
    this.app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
    ZAssert.assertEquals(Boolean.valueOf(this.app.zPageCalendar.zIsAppointmentExists(apptSubject)), Boolean.valueOf(false), "Verify decline meeting is hidden in current view");
  }
}
