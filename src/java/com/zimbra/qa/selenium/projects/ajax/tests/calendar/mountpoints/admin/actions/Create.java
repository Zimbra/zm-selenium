/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.mountpoints.admin.actions;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew.Field;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class Create extends AjaxCommonTest {

	public Create() {
		logger.info("New " + Create.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs(ids = "75771")
	@Test (description = "Verify sending invite using OBO although user2 granted OBO rights to user1",
			groups = { "functional","L2" })

	public void Create_01() throws HarnessException {

		String mountPointName = "mountpoint" + ConfigProperties.getUniqueString();
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.Account3(), SystemFolder.Calendar);

		// Share it
		ZimbraAccount.Account3().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx' view='appointment'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountPointName +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.Account3().ZimbraId +"' view='appointment' color='3'/>"
				+	"</CreateMountpointRequest>");


		ZimbraAccount.Account3().soapSend(
	      "<GrantRightsRequest xmlns='urn:zimbraAccount'>"
	      + " <ace d='" + app.zGetActiveAccount().EmailAddress + "' right='sendAs' gt='usr'/>"
	      + " <ace d='" + app.zGetActiveAccount().EmailAddress + "' right='sendOnBehalfOf' gt='usr'/>"
	      + "</GrantRightsRequest>");

		String persona = ConfigProperties.getUniqueString();
		AppointmentItem appt = new AppointmentItem();
		String apptSubject, apptAttendee1, apptContent;
		Calendar now = Calendar.getInstance();
		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.Account2().EmailAddress;
		apptContent = ConfigProperties.getUniqueString();

		// Create persona
		app.zGetActiveAccount().soapSend(
				" <CreateIdentityRequest xmlns='urn:zimbraAccount'>"
			+		"<identity name='"+ persona +"'>"
			+			"<a name='zimbraPrefIdentityName'>"+ persona +"</a>"
			+			"<a name='zimbraPrefFromDisplay'>"+ ZimbraAccount.Account3().DisplayName +"</a>"
			+			"<a name='zimbraPrefFromAddress'>"+ ZimbraAccount.Account3().EmailAddress +"</a>"
			+			"<a name='zimbraPrefFromAddressType'>sendOnBehalfOf</a>"
			+			"<a name='zimbraPrefReplyToEnabled'>FALSE</a>"
			+			"<a name='zimbraPrefReplyToDisplay'/>"
			+			"<a name='zimbraPrefDefaultSignatureId'/>"
			+			"<a name='zimbraPrefForwardReplySignatureId'/>"
			+			"<a name='zimbraPrefWhenSentToEnabled'>FALSE</a>"
			+			"<a name='zimbraPrefWhenInFoldersEnabled'>FALSE</a>"
			+		"</identity>"
			+	"</CreateIdentityRequest>");

		// Refresh UI
		app.zPageMain.zRefreshMainUI();
		app.zPageCalendar.zNavigateTo();

		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 8, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0));
		appt.setContent(apptContent);

		// Compose appointment and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zFillField(Field.From, persona);
        String locator = "css=td[id$='_folderSelect'] td[id$='_select_container']";
        apptForm.sClickAt(locator, "");

        locator = "//div[@id='z_shell']/div[contains(@id,'_Menu_') and contains(@class, 'DwtMenu')]";
        int count = apptForm.sGetXpathCount(locator);
        for  (int  i = 1; i <= count; i++) {
        	String calPullDown = locator + "[position()=" + i + "]//tr//*[contains(text(),'" + mountPointName + "')]";
        	if (apptForm.zIsVisiblePerPosition(calPullDown, 0, 0)) {
        	    apptForm.sClickAt(calPullDown, "");
        	    break;
        	}
        }
		apptForm.zSubmit();

		// Verify appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(ZimbraAccount.Account3(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getAttendees(), apptAttendee1, "Attendees: Verify the appointment data");
		ZAssert.assertEquals(actual.getContent(), appt.getContent(), "Content: Verify the appointment data");

		// Verify the attendee receives the meeting
		AppointmentItem received = AppointmentItem.importFromSOAP(ZimbraAccount.Account2(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(received, "Verify the new appointment is created");
		ZAssert.assertEquals(received.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(received.getAttendees(), apptAttendee1, "Attendees: Verify the appointment data");
		ZAssert.assertEquals(received.getContent(), appt.getContent(), "Content: Verify the appointment data");


	}
}
