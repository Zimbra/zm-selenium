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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.meetings.organizer.singleday.viewinvite;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.universal.ui.calendar.PageCalendar.Locators;

public class ViewInviteWhichContainsAttachment extends CalendarWorkWeekTest {

	public ViewInviteWhichContainsAttachment() {
		logger.info("New "+ ViewInviteWhichContainsAttachment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Test (description = "View invite which has attachment present as an organizer",
			groups = { "smoke", "L1"}
	)
	public void ViewInviteWhichContainsAttachment_01() throws HarnessException {
		
		// Create appointment & subject
		ZimbraAccount account = app.zGetActiveAccount();
		String apptSubject = ConfigProperties.getUniqueString();
		
		//upload file to server
		String filename = "BasicExcel2007.xlsx";
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/Files/Basic01/"+ filename;
		String dAttachmentId  = account.uploadFile(filePath);
		//create date object
		String tz = ZTimeZone.getLocalTimeZone().getID();
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		
		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
                     	"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                        "<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     	"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                    
                     	"</inv>" +
                     	"<e a='"+ ZimbraAccount.AccountA().EmailAddress +"' t='t'/>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ ConfigProperties.getUniqueString() +"</content>" +
                     	"</mp>" +
                     	"<attach aid='"+dAttachmentId +"'/>"+
                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");
        
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
			
		// open the appt
    	app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
    	
		// Verify the new appointment has an attachment 
		ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent(Locators.zAttachmentsLabel),"Verify Attachments: label");
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertStringContains(actual.getGMultipart().toString(), filename , "check if multipart has above created file name" );	
		
	}

}
