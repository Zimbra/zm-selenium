/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.touch.tests.contacts.contacts;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.TouchCommonTest;

public class ViewContact extends TouchCommonTest  {
	
	public ViewContact() {
		logger.info("New "+ ViewContact.class.getCanonicalName());
		super.startingPage = app.zPageAddressbook;
	}

	@Test (description = "View a contact",
			groups = { "sanity" })
	
	public void ViewContact_01() throws HarnessException {		         		

		//-- Data
		
		// Create a contact item
		String firstname = "first"+ ConfigProperties.getUniqueString();
		String lastname = "last"+ ConfigProperties.getUniqueString();
		String email = "email"+ ConfigProperties.getUniqueString() + "@example.com";
		String company = "company"+ ConfigProperties.getUniqueString();
		
		String prefix = "prefix" + ConfigProperties.getUniqueString();
		String middleName = "middleName" + ConfigProperties.getUniqueString();
		String maidenName = "maidenName" + ConfigProperties.getUniqueString();
		String suffix = "suffix" + ConfigProperties.getUniqueString();
		String nickname = "nickname" + ConfigProperties.getUniqueString();
		
		String jobTitle = "jobTitle" + ConfigProperties.getUniqueString();
		String department = "department" + ConfigProperties.getUniqueString();
		
		String homePhone = "0123456789";
		
		String workAddressCity = "city" + ConfigProperties.getUniqueString();
		String workAddressState = "state" + ConfigProperties.getUniqueString();
		String workAddressZipcode = "27513";
		String workAddressCountry = "country" + ConfigProperties.getUniqueString();
				
		String otherUrl = "http://"+ ConfigProperties.getUniqueString()+".org";
		
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

		// Refresh to get the contact into the client
		app.zPageAddressbook.zRefresh();
				
		// Select the contact from contact list
		String locator = lastname + ", " + firstname;
		app.zPageAddressbook.zSelectContact(locator);
		
		//-- Verification
		
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='contact'>"
			+		"<query>#firstname:"+ firstname +"</query>"
			+	"</SearchRequest>");
		String contactId = app.zGetActiveAccount().soapSelectValue("//mail:cn", "id");
	
		ZAssert.assertNotNull(contactId, "Verify the contact is returned in the search");
			
		String fullContact = prefix+" "+firstname+" "+middleName+" ("+maidenName+") "+lastname+", "+suffix+" \""+nickname+"\"";
		
		Boolean foundFullContactname = app.zPageAddressbook.sIsElementPresent("css=div[class='zcs-contactview-personalInfo'] span[name='contactname']:contains('"+fullContact+"')");
		Boolean foundCompany = app.zPageAddressbook.sIsElementPresent("css=div[class='zcs-contactview-personalInfo'] span:contains('"+company+"')");
		Boolean foundJobtitle = app.zPageAddressbook.sIsElementPresent("css=div[class='zcs-contactview-personalInfo'] span:contains('"+jobTitle+"')");
				
		// TODO check only some basic attributes for now. maybe implement below to check other fields as well
		//ContactItem ci = ContactItem.importFromSOAP(GetCreateResponse);
		//Boolean otherAttributes = app.zPageAddressbook.zIsContactDisplayed(ci);

		ZAssert.assertTrue(foundFullContactname, "Verify contact (" + fullContact + ") displayed ");
		ZAssert.assertTrue(foundCompany, "Verify contact (" + company + ") displayed ");
		ZAssert.assertTrue(foundJobtitle, "Verify contact (" + jobTitle + ") displayed ");
	}

}