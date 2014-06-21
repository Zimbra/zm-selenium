/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.touch.tests.addressbook.contacts;



import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.TouchCommonTest;
import com.zimbra.qa.selenium.projects.touch.ui.AppTouchClient;
import com.zimbra.qa.selenium.projects.touch.ui.PageMain;
import com.zimbra.qa.selenium.projects.touch.ui.PageMain.Locators;


public class ShowContact extends TouchCommonTest  {
	public ShowContact() {
		logger.info("New "+ ShowContact.class.getCanonicalName());

		// All tests start at the Address page
		super.startingPage = app.zPageAddressbook;

	}


	//First Last 
	@Test(	description = "View a contact",
			groups = { "sanity" })
	public void ShowContact() throws HarnessException {		         		

		//-- Data
		
		// Create a contact item
		String firstname = "first"+ ZimbraSeleniumProperties.getUniqueString();
		String lastname = "last"+ ZimbraSeleniumProperties.getUniqueString();
		String email = "email"+ ZimbraSeleniumProperties.getUniqueString() + "@example.com";
		String company = "company"+ ZimbraSeleniumProperties.getUniqueString();
		
		String prefix = "prefix" + ZimbraSeleniumProperties.getUniqueString();
		String middleName = "middleName" + ZimbraSeleniumProperties.getUniqueString();
		String maidenName = "madenName" + ZimbraSeleniumProperties.getUniqueString();
		String suffix = "suffix" + ZimbraSeleniumProperties.getUniqueString();
		String nickname = "nickname" + ZimbraSeleniumProperties.getUniqueString();
		
		String jobTitle = "jobTitle" + ZimbraSeleniumProperties.getUniqueString();
		String department = "department" + ZimbraSeleniumProperties.getUniqueString();
		
		String homePhone = "0123456789";
		
		String workAddressCity = "city" + ZimbraSeleniumProperties.getUniqueString();
		String workAddressState = "state" + ZimbraSeleniumProperties.getUniqueString();
		String workAddressZipcode = "27513";
		String workAddressCountry = "country" + ZimbraSeleniumProperties.getUniqueString();
				
		String otherUrl = "http://"+ ZimbraSeleniumProperties.getUniqueString()+".org";
		
		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
							"<a n='firstName'>"+ firstname +"</a>" +
							"<a n='lastName'>"+ lastname +"</a>" +
							"<a n='email'>"+ email +"</a>" +
							"<a n='company'>"+ company +"</a>" +
							"<a n='namePrefix'>"+ prefix +"</a>" +
							"<a n='middleName'>"+ middleName +"</a>" +
							"<a n='maidenName'>"+ maidenName +"</a>" +							
							"<a n='nameSuffix'>"+ suffix +"</a>" +							
							"<a n='nickname'>"+ nickname +"</a>" +
							"<a n='department'>"+ department +"</a>" +
							"<a n='jobTitle'>"+ jobTitle +"</a>" +
							"<a n='homePhone'>"+ homePhone +"</a>" +
							"<a n='workCity'>"+ workAddressCity +"</a>" +
							"<a n='workState'>"+ workAddressState +"</a>" +
							"<a n='workPostalCode'>"+ workAddressZipcode +"</a>" +
							"<a n='workCountry'>"+ workAddressCountry +"</a>" +	
							"<a n='otherUrl'>"+ otherUrl +"</a>" +
						"</cn>" +
				"</CreateContactRequest>" );

		//-- GUI

		// refresh to get the contact into the client
		app.zPageAddressbook.zRefresh();
		SleepUtil.sleepVeryLong();
		
		// select the contact
		String nameInList = lastname + ", " + firstname;
		String locator = "css=div[id^='ext-contactslistview'] div[class='zcs-contactList-name']:contains('"+nameInList+"')";
		app.zPageAddressbook.zClick(locator);
		
		
		
		//-- Verification
		
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='contact'>"
			+		"<query>#firstname:"+ firstname +"</query>"
			+	"</SearchRequest>");
		String contactId = app.zGetActiveAccount().soapSelectValue("//mail:cn", "id");
	
		ZAssert.assertNotNull(contactId, "Verify the contact is returned in the search");
	
		Element GetCreateResponse = app.zGetActiveAccount().soapSend(
			"<GetContactsRequest xmlns='urn:zimbraMail'>"
		+		"<cn id='"+ contactId +"'/>"
		+	"</GetContactsRequest>");
		
		
		String expectedContactname = prefix+" "+firstname+" "+middleName+" ("+maidenName+") "+lastname+", "+suffix+" \""+nickname+"\"";
		Boolean foundContactname = app.zPageAddressbook.sIsElementPresent("css=div[id^='ext-contactsitemview'] span[name='contactname']:contains('"+expectedContactname+"'");
		Boolean foundCompany = app.zPageAddressbook.sIsElementPresent("css=div[id^='ext-contactsitemview'] span:contains('"+company+"'");
		Boolean foundJobtitle = app.zPageAddressbook.sIsElementPresent("css=div[id^='ext-contactsitemview'] span:contains('"+jobTitle+"'");
		// TODO check only some basic attributes for now. maybe implment below to check other fields as well
		//ContactItem ci = ContactItem.importFromSOAP(GetCreateResponse);
		//Boolean otherAttributes = app.zPageAddressbook.zIsContactDisplayed(ci);

		ZAssert.assertTrue(foundContactname, "Verify contact (" + expectedContactname + ") displayed");
		ZAssert.assertTrue(foundCompany, "Verify contact (" + company + ") displayed ");
		ZAssert.assertTrue(foundJobtitle, "Verify contact (" + jobTitle + ") displayed ");

	}

}

