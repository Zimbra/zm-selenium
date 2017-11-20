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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.attendee.singleday.viewappt;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;

public class VerifyAttachment extends AjaxCore {

	public VerifyAttachment() {
		logger.info("New "+ VerifyAttachment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test (description = "View invite which has attachment present as an organizer",
			groups = { "functional", "L2" } )

	public void VerifyAttachment_01() throws HarnessException {

		// Create appointment & subject
		ZimbraAccount account = ZimbraAccount.Account1();
		String apptSubject = ConfigProperties.getUniqueString();

		//upload file to server
		String filename = "BasicExcel2007.xlsx";
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/Files/Basic01/"+ filename;
		String dAttachmentId  = account.uploadFile(filePath);
		// Create date object
		String tz = ZTimeZone.getLocalTimeZone().getID();
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 17, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 18, 0, 0);

		ZimbraAccount.Account1().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
                     	"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                        "<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     	"<or a='"+ ZimbraAccount.Account1().EmailAddress +"'/>" +

                     	"</inv>" +
                     	"<e a='"+ app.zGetActiveAccount().EmailAddress +"' t='t'/>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ ConfigProperties.getUniqueString() +"</content>" +
                     	"</mp>" +
                     	"<attach aid='"+dAttachmentId +"'/>"+
                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");

        app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
        app.zPageMail.zNavigateTo();
		app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		ZAssert.assertTrue( app.zPageMail.sIsElementPresent("css=td a[id*='_attLinks']:contains('" + filename + "')"), "Verify attachment exists in the email");
	}

}