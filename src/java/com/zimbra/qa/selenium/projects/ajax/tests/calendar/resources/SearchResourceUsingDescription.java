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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.resources;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.DialogFindEquipment;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew.Locators;

public class SearchResourceUsingDescription extends AjaxCore {

	public SearchResourceUsingDescription() {
		logger.info("New "+ ChangeLocationOfOneInstance.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs (ids = "60134")
	@Test (description = "Cannot search resource using 'description'",
			groups = { "functional", "L2" } )

	public void SearchResourceUsingDescription_01() throws HarnessException {

		ZimbraResource equipment = new ZimbraResource(ZimbraResource.Type.EQUIPMENT);
		String equipmentDescription = ConfigProperties.getUniqueString();
		String apptSubject = ConfigProperties.getUniqueString();
		String apptEquipment = equipment.EmailAddress;

	    ZimbraAdminAccount.GlobalAdmin().soapSend(
	    	      "<ModifyCalendarResourceRequest xmlns='urn:zimbraAdmin'><id>" +
	    	      equipment.ZimbraId + "</id>" +
	    	      "<a n='description'>" + equipmentDescription + "</a>" +
	    	      "</ModifyCalendarResourceRequest>");

		ZimbraDomain domain = new ZimbraDomain(equipment.EmailAddress.split("@")[1]);
		domain.provision();
		domain.syncGalAccount();

		// Absolute dates in UTC zone
		String tz = ZTimeZone.getLocalTimeZone().getID();
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);

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
	                 "<su>"+ apptSubject +"</su>" +
	                 "</m>" +
	           "</CreateAppointmentRequest>");

		// Verify appointment exists in current view
		ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

	    // Add equipment from 'Search Equipment' dialog and send the meeting
	    FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
	    apptForm.zToolbarPressButton(Button.B_SHOW_EQUIPMENT);
	    SleepUtil.sleepSmall();
	    apptForm.zToolbarPressButton(Button.B_EQUIPMENT);
	    SleepUtil.sleepMedium();

	    DialogFindEquipment dialogFindEquipment = (DialogFindEquipment) new DialogFindEquipment(app, app.zPageCalendar);
	    dialogFindEquipment.zType(Locators.EquipmentName, apptEquipment);
	    dialogFindEquipment.zType(Locators.EquipmentDescription, equipmentDescription);
	    SleepUtil.sleepSmall();
	    dialogFindEquipment.zPressButton(Button.B_SEARCH_EQUIPMENT);
	    SleepUtil.sleepMedium();
	    dialogFindEquipment.zPressButton(Button.B_SELECT_EQUIPMENT);
	    SleepUtil.sleepSmall();
	    dialogFindEquipment.zPressButton(Button.B_OK);
	    SleepUtil.sleepMedium();
	    apptForm.zSubmitWithResources();

	    // Verify equipment present in the appointment
	    AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertStringContains(actual.getEquipment(), apptEquipment, "Equipment: Verify the appointment data");

		// Verify equipment free/busy status
		String equipmentStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptEquipment +"']", "ptst");
		ZAssert.assertEquals(equipmentStatus, "AC", "Verify equipment free/busy status");
	}
}