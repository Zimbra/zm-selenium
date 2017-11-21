/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.attributes;

import java.util.Calendar;
import org.testng.annotations.*;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.DialogFindLocation;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.PageCalendar.Locators;

public class ZimbraCalResLocationDisplayName extends AjaxCore {

	public ZimbraCalResLocationDisplayName() {
		logger.info("New "+ ZimbraCalResLocationDisplayName.class.getCanonicalName());
	}


	@Bugs (ids = "57039")
	@Test (description = "Verify the serach location dialog shows location display name for location",
			groups = { "functional", "L2" })

	public void ZimbraCalResLocationDisplayName_01() throws HarnessException {

		ZimbraResource location = new ZimbraResource(ZimbraResource.Type.LOCATION);
		String resourceDisplayName = "DisplayName" +ConfigProperties.getUniqueString();

		// Modify the Location resource account and change zimbraCalResLocationDisplayName
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyCalendarResourceRequest xmlns='urn:zimbraAdmin'>" +
					"<name>" + location.EmailAddress + "</name>" +
					"<id> " + location.ZimbraId + "</id> " +
					"<a n='zimbraCalResLocationDisplayName'>"+ resourceDisplayName +"</a>" +
				"</ModifyCalendarResourceRequest>");

		ZimbraDomain domain = new ZimbraDomain(location.EmailAddress.split("@")[1]);
		domain.provision();
		domain.syncGalAccount();

		Element[] ModifyCalendarResourceResponse = ZimbraAdminAccount.GlobalAdmin().soapSelectNodes("//admin:ModifyCalendarResourceRequest");
		logger.info("ModifyCalendarResourceResponse is')" + ModifyCalendarResourceResponse);

		if ( (ModifyCalendarResourceResponse == null) || (ModifyCalendarResourceResponse.length == 0)) {
			Element[] soapFault = ZimbraAdminAccount.GlobalAdmin().soapSelectNodes("//soap:Fault");
			if ( soapFault != null && soapFault.length > 0 ) {
				String error = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//zimbra:Code", null);
				throw new HarnessException("Unable to modify resource : "+ error);
			}
			SleepUtil.sleepMedium();
		}

		// Refresh UI
		app.zPageMain.zRefreshMainUI();
		app.zPageCalendar.zNavigateTo();

		String tz = ZTimeZone.getLocalTimeZone().getID();
		String apptSubject = ConfigProperties.getUniqueString();
		String apptAttendee = ZimbraAccount.AccountA().EmailAddress;

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);

		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     		"<at role='REQ' ptst='AC' rsvp='1' a='" + apptAttendee + "' d='2'/>" +
                     	"</inv>" +
                     	"<e a='"+ ZimbraAccount.AccountA().EmailAddress +"' t='t'/>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ ConfigProperties.getUniqueString() +"</content>" +
                     	"</mp>" +
                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

        FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_OPEN, apptSubject);
        SleepUtil.sleepMedium();
        apptForm.zToolbarPressButton(Button.B_LOCATION);
        SleepUtil.sleepMedium();

        DialogFindLocation dialogFindLocation = (DialogFindLocation) new DialogFindLocation(app, app.zPageCalendar);
        dialogFindLocation.zType(Locators.LocationName, location.EmailAddress);
        dialogFindLocation.zPressButton(Button.B_SEARCH_LOCATION);
        SleepUtil.sleepMedium();

        // Verify the search dialog show name as email address and Location as Display name set above
        String searchResult = dialogFindLocation.zGetDisplayedText(Locators.LocationFirstSearchResult);
		ZAssert.assertStringContains(searchResult, resourceDisplayName, "verify if the Location dispaly name is being displayed in the results");
		ZAssert.assertStringContains(searchResult, location.EmailAddress, "verify if the Location  name is being displayed as email address in the results");

		// Close the search location dialog
		dialogFindLocation.zPressButton(Button.B_OK);

		// Close Edit appt form
        apptForm.zToolbarPressButton(Button.B_CLOSE);

	}


}
