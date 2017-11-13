/*
 * ***** BEGIN LICENSE BLOCK *****
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
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday.create;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;

public class CreateMeetingWithGroupCalendarFeatureDisabled extends AjaxCommonTest {

	public CreateMeetingWithGroupCalendarFeatureDisabled() {
		logger.info("New "+ CreateMeetingWithGroupCalendarFeatureDisabled.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
		this.startingAccountPreferences.put("zimbraFeatureGroupCalendarEnabled", "FALSE");
	}


	@Bugs (ids = "68726,71103")
	@Test (description = "Appointment creation broken if 'Group Calendar' feature is disabled for calendar (zimbraFeatureGroupCalendarEnabled)",
			groups = { "functional", "L2" } )

	public void CreateMeetingWithGroupCalendarFeatureDisabled_01() throws HarnessException {

		// Data
		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = "content" + ConfigProperties.getUniqueString();

		// Modify the test account
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ app.zGetActiveAccount().ZimbraId +"</id>"
			+		"<a n='zimbraFeatureGroupCalendarEnabled'>FALSE</a>"
			+	"</ModifyAccountRequest>");

		// Refresh UI
		app.zPageMain.zRefreshMainUI();
		app.zPageCalendar.zNavigateTo();

		// Create appointment
		AppointmentItem appt = new AppointmentItem();
		Calendar now = Calendar.getInstance();
		appt.setSubject(apptSubject);
		appt.setContent(apptBody);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 16, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 17, 0, 0));

		// Open the new mail form
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zSubmit();

		// Verify the new appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");

		// Verify 'Forward' menu remains disabled (bug http://bugzilla.zimbra.com/show_bug.cgi?id=71103)
		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, apptSubject);
		ZAssert.assertTrue(app.zPageCalendar.zVerifyDisabledControl(Button.O_FORWARD_DISABLED), "Verify 'Forward' menu is disabled");
	}

}
