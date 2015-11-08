package com.zimbra.qa.selenium.projects.ajax.tests.calendar.addresscontextmenu;

//package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.contextmenu;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.PageCalendar.Locators;


public class EditAttendeeContextMenu extends PrefGroupMailByMessageTest {

	public EditAttendeeContextMenu() {
		logger.info("New " + EditAttendeeContextMenu.class.getCanonicalName());

		super.startingPage = app.zPageCalendar;

	}

	@Test(description = "Right click To attendee bubble address>>Verify Edit menus", groups = { "smoke-temp-skipped" })
	public void EditAttendeesContextMenu() throws HarnessException {

		String apptAttendee1,apptContent;
		AppointmentItem appt = new AppointmentItem();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptContent = ZimbraSeleniumProperties.getUniqueString();
		appt.setAttendees(apptAttendee1);
		appt.setContent(apptContent);

		FormApptNew apptForm = (FormApptNew) app.zPageCalendar
				.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);

		app.zPageCalendar.zRightClickAddressBubble();
		app.zPageMail.EditAddressContextMenu();

		app.zPageCalendar.sFocus(FormApptNew.Locators.AttendeeField);
		app.zPageCalendar.zClick(FormApptNew.Locators.AttendeeField);
		app.zPageCalendar.zType(FormApptNew.Locators.AttendeeField,"test@test.com");
		app.zPageCalendar.sKeyDown(FormApptNew.Locators.AttendeeField, "13");
		SleepUtil.sleepMedium();
		ZAssert.assertEquals(app.zPageCalendar.sGetText(Locators.AttendeeBubbleAddr), "test@test.com", "Edited address should present");	

	}

}
