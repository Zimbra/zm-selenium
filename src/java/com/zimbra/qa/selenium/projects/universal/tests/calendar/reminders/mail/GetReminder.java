/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.reminders.mail;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;

public class GetReminder extends UniversalCommonTest {

	public GetReminder() {
		logger.info("New "+ GetReminder.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageMail;

		// Make sure we are using an account with message view
		

	}
	
	@Bugs(ids = "69132")
	@Test( description = "Verify reminder popup when in the mail app",
			groups = { "smoke-skip", "L4" })
	public void GetReminder_01() throws HarnessException {
		
		// Create the appointment on the server
		// Create the message data to be sent
		String apptSubject = ConfigProperties.getUniqueString();
		
		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startLocal = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.HOUR_OF_DAY) + 1, 0, 0);
		ZDate finishLocal   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.HOUR_OF_DAY) + 2, 0, 0);
		
		// Create a meeting request from AccountA to the test account
		app.zGetActiveAccount().soapSend(
					"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv>"
				+				"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+					"<s d='"+ startLocal.toYYYYMMDDTHHMMSS() +"' tz='"+ now.getTimeZone().getID() +"'/>"
				+					"<e d='"+ finishLocal.toYYYYMMDDTHHMMSS() +"' tz='"+ now.getTimeZone().getID() +"'/>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+					"<alarm action='DISPLAY'>"
				+						"<trigger>"
				+							"<rel neg='1' m='60' related='START'/>"
				+						"</trigger>"
				+					"</alarm>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>"+ apptSubject + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");
		
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");		

		// ReminderDialog dialog = (ReminderDialog) app.zPageMain.zGetReminderDialog();
		throw new HarnessException("Implement me: check that the Reminder Dialog Shows Up");
		
	}


}
