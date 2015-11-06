package com.zimbra.qa.selenium.projects.ajax.tests.calendar.addresscontextmenu;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.FormContactNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.PageCalendar.Locators;

public class AddToContactsAttendeeContextMenu extends PrefGroupMailByMessageTest {

	public AddToContactsAttendeeContextMenu() {
		logger.info("New " + AddToContactsAttendeeContextMenu.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Bugs(ids = "102204")
	@Test(description = "Right click To attendee bubble address>>Verify AddToContact", groups = { "smoke-temp-skipped-bug102204" })
	public void AddToContactAttendeeContextMenu() throws HarnessException {

		String apptAttendee1,apptContent;
		String contactFirst = "First"+ ZimbraSeleniumProperties.getUniqueString();

		AppointmentItem appt = new AppointmentItem();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptContent = ZimbraSeleniumProperties.getUniqueString();
		appt.setAttendees(apptAttendee1);
		appt.setContent(apptContent);

		FormApptNew apptForm = (FormApptNew) app.zPageCalendar
				.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);

		String OriginalEmailAddr = app.zPageCalendar.sGetText(Locators.AttendeeBubbleAddr);

		app.zPageCalendar.zRightClickAddressBubble();
		app.zPageMail.AddToContactAddressContextMenu();

		SleepUtil.sleepMedium();

		app.zPageMail.sFocus("css=input[id$='_FIRST_input']");
		app.zPageMail.zClick("css=input[id$='_FIRST_input']");
		app.zPageMail.zKeyboard.zTypeCharacters(contactFirst);
		SleepUtil.sleepMedium();
	//	app.zPageMail.sType("css=input[id$='_FIRST_input']", contactFirst);
		app.zPageMail.sClickAt(FormContactNew.Toolbar.SAVE, "");
		SleepUtil.sleepMedium();

		// -- Data Verification

		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='contact'>"
						+ "<query>#firstname:" + contactFirst + "</query>"
						+ "</SearchRequest>");
		String contactId = app.zGetActiveAccount().soapSelectValue("//mail:cn",
				"id");

		ZAssert.assertNotNull(contactId,"Verify the contact is returned in the search");

		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail'>" + "<cn id='"
						+ contactId + "'/>" + "</GetContactsRequest>");

		String firstname = app.zGetActiveAccount().soapSelectValue(
				"//mail:cn[@id='" + contactId + "']//mail:a[@n='firstName']",
				null);
		String email = app.zGetActiveAccount().soapSelectValue(
				"//mail:cn[@id='" + contactId + "']//mail:a[@n='email']", null);

		ZAssert.assertEquals(firstname, contactFirst,"Verify the first name was saved correctly");
		ZAssert.assertEquals(email, OriginalEmailAddr,"Verify the email was saved correctly");


	}

}
