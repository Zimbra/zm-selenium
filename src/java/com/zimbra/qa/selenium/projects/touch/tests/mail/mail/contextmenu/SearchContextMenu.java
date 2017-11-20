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
package com.zimbra.qa.selenium.projects.touch.tests.mail.mail.contextmenu;

import java.util.*;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.*;
import com.zimbra.qa.selenium.projects.touch.pages.mail.DisplayMail.*;

public class SearchContextMenu extends CalendarWorkWeekPreference {

	public SearchContextMenu() {
		logger.info("New "+ SearchContextMenu.class.getCanonicalName());
		super.startingPage = app.zPageMail;
	}

	@Test (description = "Click on To msg Header and verify context menu>> Search", 
			groups = { "smoke" })

	public void SearchContextMenu_01() throws HarnessException {

		// ------------------------ Test data ------------------------------------
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "text <strong>bold"+ ConfigProperties.getUniqueString() +"</strong> text";
		String locator = "css= div[class='x-innerhtml']:contains('Search Results')";
		String htmlBody = XmlStringUtil.escapeXml(
				"<html>" +
						"<head></head>" +
						"<body>"+ body +"</body>" +
				"</html>");

		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<e t='c' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='multipart/alternative'>" +
						"<mp ct='text/plain'>" +
						"<content>"+ body +"</content>" +
						"</mp>" +
						"<mp ct='text/html'>" +
						"<content>"+ htmlBody +"</content>" +
						"</mp>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");
		app.zPageMail.zRefresh();

		//----------------------------- Verification ------------------------------

		// Select the mail
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Click on To address bubble
		SleepUtil.sleepMedium();
		app.zPageMail.sClickAddressBubble(Field.From);

		//Click on search option
		app.zPageMail.zMsgHdrContextListItem(Button.B_SEARCH);
		
		//Verify search functionality
		SleepUtil.sleepSmall();
		ZAssert.assertTrue(app.zPageMail.zVerifyMessageExists(subject), "Verify message exists after search");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(locator), "Verify the search results page opened up");
		
		app.zPageMail.zPressButton(Button.B_BACK);
		
	}

	@Test (description = "Click on organizer msg Header and verify context menu>> Search", 
			groups = { "functional" })

	public void NewMessageContextMenu_02() throws HarnessException {

		// ------------------------ Test data ------------------------------------

		String apptSubject = "appointment" + ConfigProperties.getUniqueString();		
		String locator = "css= div[class='x-innerhtml']:contains('Search Results')";
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);

		// --------------- Creating invitation (organizer) ----------------------------

		ZimbraAccount.AccountA().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
						+		"<m>"
						+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
						+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
						+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
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
		app.zPageMail.zRefresh();

		// Select the invitation
		app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);

		// Click on organizer bubble
		SleepUtil.sleepMedium();
		app.zPageMail.sClickAddressBubble(Field.Organizer);

		//Click on search option
		app.zPageMail.zMsgHdrContextListItem(Button.B_SEARCH);
		
		//Verify search functionality
		SleepUtil.sleepSmall();
		ZAssert.assertTrue(app.zPageMail.zVerifyMessageExists(apptSubject), "Verify message exists after search");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(locator), "Verify the search results page opened up");
		
		app.zPageMail.zPressButton(Button.B_BACK);

	}
}
