package com.zimbra.qa.selenium.projects.ajax.tests.calendar.bugs;

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
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogConfirmationDeclineAppointment;
import java.util.Calendar;
import org.testng.annotations.Test;

public class Bug27661 extends CalendarWorkWeekTest {

  public Bug27661()
  {
    logger.info("New " + Bug27661.class.getCanonicalName());
    this.startingPage = this.app.zPageCalendar;
    this.startingAccountPreferences.put("zimbraFeatureGroupCalendarEnabled", "FALSE");
  }
  
  @Bugs(ids="27661")
  @Test(description="Allow declined meetings to be hidden/shown based on preference",
  groups={"functional"}
  )
  public void Bug27661_01()
    throws HarnessException
  {
    String apptSubject = ZimbraSeleniumProperties.getUniqueString();
    
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

    this.app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);
    this.app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_DECLINE_MENU, apptSubject);
    

    DialogConfirmationDeclineAppointment declineAppt = new DialogConfirmationDeclineAppointment(this.app, this.app.zPageCalendar);
    declineAppt.zClickButton(Button.B_DONT_NOTIFY_ORGANIZER);
    declineAppt.zClickButton(Button.B_YES);
    SleepUtil.sleepMedium();
    

    this.app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
    

    ZAssert.assertEquals(Boolean.valueOf(this.app.zPageCalendar.zIsAppointmentExists(apptSubject)), Boolean.valueOf(false), "Verify decline meeting is hidden in current view");
  }
}
