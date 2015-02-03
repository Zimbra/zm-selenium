package com.zimbra.qa.selenium.projects.ajax.tests.calendar.bugs;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraResource;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew.Field;

import java.util.Calendar;

import org.testng.annotations.Test;

public class Bug71542 extends CalendarWorkWeekTest {

  public Bug71542()
  {
    logger.info("New " + Bug71542.class.getCanonicalName());
  }
  
  @Bugs(ids="71542")
  @Test(description="Unable to create appointment in shared calendars if mail feature is disabled",
  groups={"functional"}
  )
  public void Bug71542_01()
    throws HarnessException
  {
		ZimbraResource location = new ZimbraResource(ZimbraResource.Type.LOCATION);
		
		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		
		String apptSubject, apptLocation, apptAttendee1, apptContent, mountPointName;
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptLocation = location.EmailAddress;
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptContent = ZimbraSeleniumProperties.getUniqueString();
		mountPointName = ZimbraSeleniumProperties.getUniqueString();
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 01, 0, 0);
		ZDate endUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 02, 0, 0);

		appt.setSubject(apptSubject);
		appt.setLocation(apptLocation);
		appt.setAttendees(apptAttendee1);
		appt.setStartTime(startUTC);
		appt.setEndTime(endUTC);
		appt.setContent(apptContent);
		
		// Use system calendar folder
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.AccountB(),	FolderItem.SystemFolder.Calendar);

		// Share it
		ZimbraAccount.AccountB().soapSend("<FolderActionRequest xmlns='urn:zimbraMail'>" + "<action id='"	+ folder.getId() + "' op='grant'>" + "<grant d='"
						+ app.zGetActiveAccount().EmailAddress	+ "' gt='usr' perm='rwidx' view='appointment'/>"
						+ "</action>" + "</FolderActionRequest>");

		// Mount it
		app.zGetActiveAccount().soapSend("<CreateMountpointRequest xmlns='urn:zimbraMail'>" + "<link l='1' name='" + mountPointName + "'  rid='"
						+ folder.getId() + "' zid='" + ZimbraAccount.AccountB().ZimbraId + "' view='appointment' color='4'/>" + "</CreateMountpointRequest>");

	    ZimbraAdminAccount.GlobalAdmin().soapSend(
	    	      "<ModifyAccountRequest xmlns='urn:zimbraAdmin'><id>" + 
	    	      this.app.zGetActiveAccount().ZimbraId + "</id>" + 
	    	      "<a n='zimbraFeatureMailEnabled'>FALSE</a>" + 
	    	      "</ModifyAccountRequest>");
	    
	    this.app.zPageLogin.zNavigateTo();
	    this.startingPage.zNavigateTo();
	    
		// Compose appointment on shared mailbox
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zFillField(Field.CalendarFolder, mountPointName);
		apptForm.zSubmit();

		// Verify appointment is created on shared mailbox
		AppointmentItem actual = AppointmentItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:(" + apptSubject + ")");
		ZAssert.assertEquals(actual.getFolder(), folder.getId(), "Verify appointment is created on shared mailbox");

		// Verify sent invite not present in current account
		app.zGetActiveAccount().soapSend("<SearchRequest xmlns='urn:zimbraMail' types='message'>" + "<query>" + "in:sent " + "subject:(" + apptSubject + ")</query>" + "</SearchRequest>");
		String messageId = app.zGetActiveAccount().soapSelectValue("//mail:m", "id");
		ZAssert.assertNull(messageId, "Verify sent invite not present in current account");

		ZimbraAccount.AccountA().soapSend("<SearchRequest xmlns='urn:zimbraMail' types='message'>" + "<query>" + "in:inbox " + "subject:(" + apptSubject + ")</query>" + "</SearchRequest>");
		messageId = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify attendee gets email notification");

		// Verify from and sender address in received invite
		ZimbraAccount.AccountA().soapSend("<GetMsgRequest  xmlns='urn:zimbraMail'>" + "<m id='"	+ messageId + "'/>" + "</GetMsgRequest>");
		ZAssert.assertEquals(ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='f']", "a"), ZimbraAccount.AccountB().EmailAddress,	"Verify From address in received invite");
		ZAssert.assertEquals(ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='s']","a"), app.zGetActiveAccount().EmailAddress, "Verify Sender address in received invite");

		// Verify appointment exists on the server
		actual = AppointmentItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:(" + appt.getSubject() + ")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(),"Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getLocation(), appt.getLocation(),"Location: Verify the appointment data");
		ZAssert.assertEquals(actual.getAttendees(), apptAttendee1,"Attendees: Verify the appointment data");
		ZAssert.assertEquals(actual.getContent(), appt.getContent(),"Content: Verify the appointment data");
		
		actual = AppointmentItem.importFromSOAP(location, "subject:(" + appt.getSubject() + ")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify location is booked");
		
		// Verify appointment exists in UI
		app.zTreeCalendar.zMarkOnOffCalendarFolder("Calendar");
		app.zTreeCalendar.zMarkOnOffMountedFolder(mountPointName);
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), true,"Verify appointment is visible in UI");
  }
}
