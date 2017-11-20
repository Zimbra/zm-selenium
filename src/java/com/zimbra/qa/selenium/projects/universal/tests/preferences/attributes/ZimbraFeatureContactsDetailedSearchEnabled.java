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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.attributes;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.*;
import com.zimbra.qa.selenium.projects.universal.pages.calendar.DialogFindAttendees;
import com.zimbra.qa.selenium.projects.universal.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.universal.pages.mail.*;
import com.zimbra.qa.selenium.projects.universal.pages.mail.FormAddressPicker.Field;
import com.zimbra.qa.selenium.projects.universal.pages.mail.FormAddressPicker.Locators;

public class ZimbraFeatureContactsDetailedSearchEnabled extends SetGroupMailByMessagePreference {

	public ZimbraFeatureContactsDetailedSearchEnabled() {
		logger.info("New "+ ZimbraFeatureContactsDetailedSearchEnabled.class.getCanonicalName());
		super.startingAccountPreferences.put("ZimbraFeatureContactsDetailedSearchEnabled", "TRUE");		
	}

	
	@Bugs (ids = "70708")
	@Test (description = "Filter addresses using department name after selecting To: while composing mail", 
		groups = { "functional", "L2" })
	
	public void ZimbraFeatureContactsDetailedSearchEnabled_01() throws HarnessException {
		
		try {

			String department=ConfigProperties.getUniqueString();
	
			//-- Data
	
			// Add departments to three accounts	
			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
							+		"<id>"+ZimbraAccount.Account5().ZimbraId +"</id>"
							+		"<a n='ou'>"+department+"</a>"
							+	"</ModifyAccountRequest>");
	
			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
							+		"<id>"+ZimbraAccount.Account6().ZimbraId +"</id>"
							+		"<a n='ou'>"+department+"</a>"
							+	"</ModifyAccountRequest>");
	
			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
							+		"<id>"+ ZimbraAccount.Account7().ZimbraId +"</id>"
							+		"<a n='ou'>HR</a>"
							+	"</ModifyAccountRequest>");
	
			ZimbraDomain domain = new ZimbraDomain(ZimbraAccount.Account5().EmailAddress.split("@")[1]);
			domain.provision();
			domain.syncGalAccount();
			
			// Open the new mail form
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
			ZAssert.assertNotNull(mailform, "Verify the new form opened");
	
			//Click on To button
			FormAddressPicker selectAddress =(FormAddressPicker)mailform.zToolbarPressButton(Button.B_TO);
	
			//Enter department to search
			selectAddress.zFillField(Field.Department,department );
			selectAddress.zToolbarPressButton(Button.B_SEARCH);
	
			//Check that correct addresses are filtered out		
			ZAssert.assertTrue(selectAddress.sIsElementPresent(Locators.SearchResultArea+":contains('" + ZimbraAccount.Account5().EmailAddress + "')"),"Verify that correct addresses are filtered out");
			ZAssert.assertTrue(selectAddress.sIsElementPresent(Locators.SearchResultArea+":contains('" + ZimbraAccount.Account6().EmailAddress + "')"),"Verify that correct addresses are filtered out");
			
			//Check that department names are also displayed
			ZAssert.assertTrue(selectAddress.sIsElementPresent(Locators.SearchResultArea+":contains('" + department + "')"),"Verify that search result containing the department name is displayed ");
	
			//Selecting the first addresses from the search result and moving to recipient box		
			selectAddress.sClick(Locators.ContactPickerFirstContact);
			selectAddress.zToolbarPressButton(Button.B_TO);
	
			//Verify that  the selected address has been moved to recipient box and department name is displayed in recipient box as well
			ZAssert.assertTrue(selectAddress.sIsElementPresent(Locators.SearchResultArea+":contains('" + department + "'):contains('To:')"), "Verify that department is displayed in recipient box as well");
	    
		} finally {	
			// Refresh due to skipped issue
			app.zPageMain.zRefreshMainUI();
		}
	}		

	
	@Bugs (ids = "70708")
	@Test (description = "Filter addresses using department name after selecting To: while composing appointments", 
		groups = { "functional", "L2" })
	
	public void ZimbraFeatureContactsDetailedSearchEnabled_02() throws HarnessException {

		try {
			
			String department=ConfigProperties.getUniqueString();
			
			ZimbraAccount account1 = new ZimbraAccount();
			account1.provision();
			account1.authenticate();

			ZimbraAccount account2 = new ZimbraAccount();
			account2.provision();
			account2.authenticate();

			ZimbraAccount account3 = new ZimbraAccount();
			account3.provision();
			account3.authenticate();
			
			// Add departments to three accounts	
			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
							+		"<id>"+account1.ZimbraId +"</id>"
							+		"<a n='ou'>"+department+"</a>"
							+	"</ModifyAccountRequest>");

			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
							+		"<id>"+account2.ZimbraId +"</id>"
							+		"<a n='ou'>"+department+"</a>"
							+	"</ModifyAccountRequest>");

			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
							+		"<id>"+ account3.ZimbraId +"</id>"
							+		"<a n='ou'>ProductManagement</a>"
							+	"</ModifyAccountRequest>");

			ZimbraDomain domain = new ZimbraDomain(account1.EmailAddress.split("@")[1]);
			domain.provision();
			domain.syncGalAccount();

			// Go to Calendar app
			app.zPageCalendar.zNavigateTo();
			
			// Open the new mail form
			FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
			ZAssert.assertNotNull(apptForm, "Verify the new form opened");
			
	        apptForm.zToolbarPressButton(Button.B_TO);
	        DialogFindAttendees dialogFindAttendees = (DialogFindAttendees) new DialogFindAttendees(app, app.zPageCalendar);
	        
	        //Enter department to search
	        dialogFindAttendees.zFillField(com.zimbra.qa.selenium.projects.universal.pages.calendar.DialogFindAttendees.Field.Department, department);
			dialogFindAttendees.zPressButton(Button.B_SEARCH);

			//Check that correct addresses are filtered out		
			ZAssert.assertTrue(dialogFindAttendees.sIsElementPresent(Locators.SearchResultArea+":contains('" + account1.EmailAddress + "')"),"Verify that correct addresses are filtered out");
			ZAssert.assertTrue(dialogFindAttendees.sIsElementPresent(Locators.SearchResultArea+":contains('" + account2.EmailAddress + "')"),"Verify that correct addresses are filtered out");
			
			//Check that department names are also displayed
			ZAssert.assertTrue(dialogFindAttendees.sIsElementPresent(Locators.SearchResultArea+":contains('" + department + "')"),"Verify that search result containing the department name is displayed ");

			//Selecting the first addresses from the search result and moving to recipient box		
	        dialogFindAttendees.zPressButton(Button.B_SELECT_FIRST_CONTACT);
	        dialogFindAttendees.zPressButton(Button.B_CHOOSE_CONTACT_FROM_PICKER);

			//Verify that  the selected address has been moved to recipient box and department name is displayed in recipient box as well
			ZAssert.assertTrue(dialogFindAttendees.sIsElementPresent("css=div[id='ZmContactPicker'] div[class='DwtChooserListView'] div[id^='zli__DWT'] td:contains('" + department + "')"), "Verify that department is visible in recipient box as well");
		
		} finally {
			
			// Refresh due to skipped issue
			app.zPageMain.zRefreshMainUI();
		}
		
	}		
}

