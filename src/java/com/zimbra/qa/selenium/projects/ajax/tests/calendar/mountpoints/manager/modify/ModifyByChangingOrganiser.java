/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.mountpoints.manager.modify;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew.Field;

public class ModifyByChangingOrganiser extends CalendarWorkWeekTest {	
	
	public ModifyByChangingOrganiser() {
		logger.info("New "+ ModifyByChangingOrganiser.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;

		{
		    put("zimbraPrefCalendarInitialView", "month");
		}};

	}
	
	@Bugs(ids = "77105")
	@Test(description = " Changing organizer of an imported appointment is not allowed",
			groups = { "functional" })
			
	public void ModifyByChangingOrganiser_01() throws HarnessException {
		String foldername = "folder" + ZimbraSeleniumProperties.getUniqueString();
		String mountPointName = "mountpoint" + ZimbraSeleniumProperties.getUniqueString();
		String subject = "Meeting scheduled: The Performance Hour";
		FolderItem calendarFolder = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Calendar);
		
		// Create a folder to share
		ZimbraAccount.AccountA().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" + calendarFolder.getId() + "' view='appointment'/>"
				+	"</CreateFolderRequest>");
		
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), foldername);
		
		// Share it
		ZimbraAccount.AccountA().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx' view='appointment'/>"
				+		"</action>"
				+	"</FolderActionRequest>");
		
		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountPointName +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.AccountA().ZimbraId +"' view='appointment' color='3'/>"
				+	"</CreateMountpointRequest>");

		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		SleepUtil.sleepMedium();
		Calendar today = Calendar.getInstance();

		// Import Calendar.ics 
		String filename = ZimbraSeleniumProperties.getBaseDirectory() + "/data/public/ics/calendar06/Calendar.ics";
		File file = null;

		// Modify the ICS such that the dates equal to this month
		file = RestUtil.FileUtils.replaceInFile("201208", (new SimpleDateFormat("yyyyMM")).format(today.getTime()), new File(filename));

		RestUtil rest = new RestUtil();
		rest.setAuthentication(app.zGetActiveAccount());
		rest.setPath("/service/home/~/Calendar");
		rest.setQueryParameter("fmt", "ics");
		rest.setUploadFile(file);
		rest.doPost();
		
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		SleepUtil.sleepMedium();

		DialogWarning dialog = (DialogWarning)app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, Button.B_OPEN_THE_SERIES, Button.O_EDIT, subject);
		dialog.zClickButton(Button.B_OK);
		
		FormApptNew apptForm = new FormApptNew(app);
        if(ZimbraSeleniumProperties.isWebDriver()){
            String locator = "css=td[id$='_folderSelect'] td[id$='_select_container']";
            apptForm.sClickAt(locator, "");            

            locator = "//div[@id='z_shell']/div[contains(@id,'_Menu_') and contains(@class, 'DwtMenu')]";   
            int count = apptForm.sGetXpathCount(locator);           
            for  (int  i = 1; i <= count; i++) {
        	String calPullDown = locator + "[position()=" + i + "]//tr//*[contains(text(),'" + mountPointName + "')]";
        	if(apptForm.zIsVisiblePerPosition(calPullDown, 0, 0)){
        	    apptForm.sClickAt(calPullDown, "");
        	    break;
        	}        	
            }            
        }else{
            apptForm.zFillField(Field.CalendarFolder, mountPointName);
        }
        apptForm.zToolbarPressButton(Button.B_SEND);
        
		Calendar now = this.calendarWeekDayUTC;
		ZDate start = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, 24, 8, 0, 0);
        SleepUtil.sleepLong();
      
        String search = "Meeting Scheduled";
        // Verify calendar value
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ start.addDays(-10).toMillis() +"' calExpandInstEnd='"+ start.addDays(10).toMillis() +"'>"
						+		"<query>inid:"+ folder.getId() +" subject:("+ search +")</query>"
					+	"</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:appt", "id");
		ZAssert.assertNotNull(id, "Verify that the appointment was moved to the mountpoint");
		
		
		
	}
}