package com.zimbra.qa.selenium.projects.ajax.tests.calendar.addresscontextmenu;

//package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.contextmenu;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;


public class AddressContextMenuOptions extends PrefGroupMailByMessageTest {

	public AddressContextMenuOptions() {
		logger.info("New " + AddressContextMenuOptions.class.getCanonicalName());

		super.startingPage = app.zPageCalendar;

	}

	@Test(description = "Right click To attendee bubble address>>Verify Delete/Copy/Edit/Expand/AddtoContacts menus", groups = { "sanity-skip" })
	public void VerifyAttendeesContextMenuOptions() throws HarnessException {

		String apptAttendee1,apptContent;
		AppointmentItem appt = new AppointmentItem();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptContent = ZimbraSeleniumProperties.getUniqueString();
		appt.setAttendees(apptAttendee1);
		appt.setContent(apptContent);
		
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar
				.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		
		SleepUtil.sleepMedium();
		app.zPageCalendar.zRightClickAddressBubble();
		SleepUtil.sleepMedium();
		logger.info(app.zPageMail.zVerifyAllAddressContextMenu("calendar"));
		ZAssert.assertTrue(
				app.zPageMail.zVerifyAllAddressContextMenu("calendar"),
				"Delete/Copy/Edit/Expand/AddtoContact menu should be exist");

	}

}
