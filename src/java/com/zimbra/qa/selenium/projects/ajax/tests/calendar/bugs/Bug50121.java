package com.zimbra.qa.selenium.projects.ajax.tests.calendar.bugs;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;

import java.util.Calendar;

import org.testng.annotations.Test;

public class Bug50121 extends CalendarWorkWeekTest {

  public Bug50121()
  {
    logger.info("New " + Bug50121.class.getCanonicalName());
  }
  
  @Bugs(ids="50121")
  @Test(description="unable to save when cancelling conflicts",
  groups={"functional"}
  )
  public void Bug50121_01() throws HarnessException  {
	  
	    ZimbraAdminAccount.GlobalAdmin().soapSend(
	  	      "<ModifyAccountRequest xmlns='urn:zimbraAdmin'><id>" + 
	  	      this.app.zGetActiveAccount().ZimbraId + "</id>" + 
	  	      "<a n='zimbraPrefCalendarDefaultApptDuration'>30m</a>" + 
	  	      "</ModifyAccountRequest>");

	  	    this.app.zPageLogin.zNavigateTo();
	  	    this.startingPage.zNavigateTo();

			String apptContent = ZimbraSeleniumProperties.getUniqueString();
		String apptSubject = ZimbraSeleniumProperties.getUniqueString();
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);

	  // Create appointment
		AppointmentItem appt = new AppointmentItem();
		appt.setSubject(apptSubject);
		appt.setStartTime(startUTC);
		appt.setContent(apptContent);
		
		// Open the new mail form
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(apptForm, "Verify the new form opened");

		// Fill out the form with the data
		apptForm.zFill(appt);

		// Send the message
		apptForm.zSubmit();
			
		// Verify the new appointment exists on the server
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ startUTC.addDays(7).toMillis() +"'>"
			+	"<query>subject:("+ apptSubject +")</query>"
			+	"</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(id, "verify that appointment is synced to the server");
        app.zGetActiveAccount().soapSend("<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ id +"'/>");
		String endtime = app.zGetActiveAccount().soapSelectValue("//mail:e", "d");
        ZAssert.assertEquals(startUTC.addMinutes(30).toyyyyMMddTHHmmss(), endtime, "verify that the duartion is 30 minutes");

	}
	
}
