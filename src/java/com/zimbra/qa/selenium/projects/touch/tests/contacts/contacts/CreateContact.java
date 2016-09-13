/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014, 2015, 2016 Synacor, Inc.
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
import org.testng.annotations.Test;import com.zimbra.qa.selenium.framework.core.Bugs;import com.zimbra.qa.selenium.framework.items.ContactItem;import com.zimbra.qa.selenium.framework.items.FolderItem;import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.ui.contacts.FormContactNew;
import com.zimbra.qa.selenium.projects.touch.ui.contacts.FormContactNew.Field;
import com.zimbra.qa.selenium.projects.touch.core.TouchCommonTest;import com.zimbra.qa.selenium.projects.touch.ui.contacts.PageAddressbook.Locators;
public class CreateContact extends TouchCommonTest  {	
	public CreateContact() {
		logger.info("New "+ CreateContact.class.getCanonicalName());
		super.startingPage = app.zPageAddressbook;
	}
	
	@Test( description = "Create a contact with basic fields",
			groups = { "sanity" })
	
	public void CreateContact_01() throws HarnessException {
		//-- DATA		
		// Generate basic attribute values for new account
		String contactFirst = "First" + ConfigProperties.getUniqueString();
		String contactLast = "Last"+ ConfigProperties.getUniqueString();
		String contactCompany = "Company"+ ConfigProperties.getUniqueString();
		//-- GUI Action		
		// Click +(Add) button
		FormContactNew formContactNew = (FormContactNew)app.zPageAddressbook.zToolbarPressButton(Button.B_NEW);
        // Fill in the form
		formContactNew.zFillField(Field.FirstName, contactFirst);
		formContactNew.zFillField(Field.LastName, contactLast);
		formContactNew.zFillField(Field.Company, contactCompany);
		// Click Save button
		formContactNew.zSubmit();
		//-- Data Verification
		
		// Search the created contact 
		app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='contact'>"
				+		"<query>#firstname:"+ contactFirst +"</query>"
				+	"</SearchRequest>");
		String contactId = app.zGetActiveAccount().soapSelectValue("//mail:cn", "id");
		// Make sure if the data is found by search request
		ZAssert.assertNotNull(contactId, "Verify the contact is returned in the search");
		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail'>"
			+		"<cn id='"+ contactId +"'/>"
			+	"</GetContactsRequest>");
		// Get all the contact data stored in Zimbra server
		String lastname = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='lastName']", null);
		String firstname = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='firstName']", null);
		String company = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='company']", null);
		// Make sure those are equal to one you created from GUI
		ZAssert.assertEquals(lastname, contactLast, "Verify the last name was saved correctly");
		ZAssert.assertEquals(firstname, contactFirst, "Verify the first name was saved correctly");
		ZAssert.assertEquals(company, contactCompany, "Verify the company was saved correctly");
	}
	
		
	@Test( description = "Create a contact with all fields",
			groups = { "smoke" })	

	public void CreateContact_02() throws HarnessException {				
		//-- DATA
		// Generate all attributes value for new contact
		String contactFirst = "First" + ConfigProperties.getUniqueString();
		String contactLast = "Last"+ ConfigProperties.getUniqueString();
		String contactCompany = "Company"+ ConfigProperties.getUniqueString();
		String contactPrefix = "Mr";
		String contactMiddleName = "MiddleName" + ConfigProperties.getUniqueString();
		String contactMaidenName = "MadenName" + ConfigProperties.getUniqueString();
		String contactSuffix = "Sr" ;
		String contactNickname = "Nickname" + ConfigProperties.getUniqueString();
		String contactJobTitle = "JobTitle" + ConfigProperties.getUniqueString();
		String contactDepartment = "Department" + ConfigProperties.getUniqueString();
		String contactEmail = "Email" + ConfigProperties.getUniqueString() + "@testdomain.com";
		String contactMobilePhone = "1-408-555-1212";
		String contactOtherStreet = "123 Main St.";
		String contactOtherCity = "City" + ConfigProperties.getUniqueString();
		String contactOtherState = "State" + ConfigProperties.getUniqueString();
		String contactOtherZipcode = "94402";
		String contactOtherCountry = "Country" + ConfigProperties.getUniqueString();
		String contactWorkUrl = "http://"+ ConfigProperties.getUniqueString()+".com";
		//-- GUI Action
		// Click +(Add) button
		FormContactNew formContactNew = (FormContactNew)app.zPageAddressbook.zToolbarPressButton(Button.B_NEW);
		// Fill in the form		
		// For basic attributes
		formContactNew.zFillField(Field.FirstName, contactFirst);
		formContactNew.zFillField(Field.LastName, contactLast);
		formContactNew.zFillField(Field.Company, contactCompany);
		// Show all hidden field:
		formContactNew.zDisplayHiddenName();
		// For extended attributes
		formContactNew.zFillField(Field.NamePrefix, contactPrefix);
		formContactNew.zFillField(Field.MiddleName, contactMiddleName);
		formContactNew.zFillField(Field.MaidenName, contactMaidenName);
		formContactNew.zFillField(Field.NameSuffix, contactSuffix);
		formContactNew.zFillField(Field.Nickname, contactNickname);
		formContactNew.zFillField(Field.Department, contactDepartment);
		formContactNew.zFillField(Field.JobTitle, contactJobTitle);
		formContactNew.zFillField(Field.Email, contactEmail);
		formContactNew.zToolbarPressPulldown(Button.B_PHONE_TYPE, Button.O_MOBILE);		
		formContactNew.zFillField(Field.MobilePhone, contactMobilePhone);
		formContactNew.zToolbarPressPulldown(Button.B_ADDRESS_TYPE, Button.O_OTHER);
		formContactNew.zFillField(Field.OtherStreet, contactOtherStreet);
		formContactNew.zFillField(Field.OtherCity, contactOtherCity);
		formContactNew.zFillField(Field.OtherState, contactOtherState);
		formContactNew.zFillField(Field.OtherCountry, contactOtherCountry);
		formContactNew.zFillField(Field.OtherZipcode, contactOtherZipcode);
		formContactNew.zToolbarPressPulldown(Button.B_URL_TYPE, Button.O_WORK);
		formContactNew.zFillField(Field.WorkURL, contactWorkUrl);
		// Click Save button
		formContactNew.zSubmit();
		//-- Verification
		// Search the created contact
		app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='contact'>"
				+		"<query>#firstname:"+ contactFirst +"</query>"
				+	"</SearchRequest>");
		String contactId = app.zGetActiveAccount().soapSelectValue("//mail:cn", "id");
		// Make sure if the data is found by search request
		ZAssert.assertNotNull(contactId, "Verify the contact is returned in the search");
		app.zGetActiveAccount().soapSend(
					"<GetContactsRequest xmlns='urn:zimbraMail'>"
				+		"<cn id='"+ contactId +"'/>"
				+	"</GetContactsRequest>");
		// Get all the contact data stored in Zimbra server
		String lastname = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='lastName']", null);
		String firstname = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='firstName']", null);
		String company = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='company']", null);
		String prefix = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='namePrefix']", null);
		String middlename = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='middleName']", null);
		String maidenname = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='maidenName']", null);
		String suffix = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='nameSuffix']", null);
		String nickname = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='nickname']", null);
		String jobtitle = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='jobTitle']", null);
		String department = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='department']", null);
		String email = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='email']", null);
		String mobile = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='mobilePhone']", null);
		String otherstreet = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='otherStreet']", null);
		String othercity = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='otherCity']", null);
		String otherstate = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='otherState']", null);
		String othercountry = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='otherCountry']", null);
		String otherzipcode = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='otherPostalCode']", null);
		String workurl = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='"+ contactId +"']//mail:a[@n='workURL']", null);
		//-- Data Verification
		// Make sure those are equal to one you created from GUI
		ZAssert.assertEquals(lastname, contactLast, "Verify the last name was saved correctly");
		ZAssert.assertEquals(firstname, contactFirst, "Verify the first name was saved correctly");
		ZAssert.assertEquals(email, contactEmail, "Verify the email was saved correctly");
		ZAssert.assertEquals(company, contactCompany, "Verify the company was saved correctly");
		ZAssert.assertEquals(prefix, contactPrefix, "Verify the prefix was saved correctly");
		ZAssert.assertEquals(middlename, contactMiddleName, "Verify the middle name was saved correctly");
		ZAssert.assertEquals(maidenname, contactMaidenName, "Verify the maiden was saved correctly");
		ZAssert.assertEquals(suffix, contactSuffix, "Verify the suffix was saved correctly");
		ZAssert.assertEquals(nickname, contactNickname, "Verify the nickname was saved correctly");
		ZAssert.assertEquals(jobtitle, contactJobTitle, "Verify the jobtitle was saved correctly");
		ZAssert.assertEquals(department, contactDepartment, "Verify the department was saved correctly");
		ZAssert.assertEquals(mobile, contactMobilePhone, "Verify the mobile phone was saved correctly");
		ZAssert.assertEquals(otherstreet, contactOtherStreet, "Verify the other street was saved correctly");
		ZAssert.assertEquals(othercity, contactOtherCity, "Verify the other city was saved correctly");
		ZAssert.assertEquals(otherstate, contactOtherState, "Verify the other state was saved correctly");
		ZAssert.assertEquals(othercountry, contactOtherCountry, "Verify the other country was saved correctly");
		ZAssert.assertEquals(otherzipcode, contactOtherZipcode, "Verify the other zipcode was saved correctly");
		ZAssert.assertEquals(workurl, contactWorkUrl, "Verify the work url was saved correctly");
	}		@Bugs( ids = "82461")	@Test( description = "Create a contact in emailed contacts folder",			groups = { "functional" })	public void CreateContact_03() throws HarnessException {		//-- DATA				// Generate basic attribute values for new account		String contactFirst = "First" + ConfigProperties.getUniqueString();		String contactLast = "Last"+ ConfigProperties.getUniqueString();		String contactCompany = "Company"+ ConfigProperties.getUniqueString();		String locator = "css=div[class='zcs-menu-label']:contains('Emailed Contacts')";		//-- GUI Action				// Click on emailed contacts folder		app.zPageAddressbook.zClickAt(Locators.zNavigationButton, "");		app.zPageAddressbook.zClickAt(locator, "");				// Click +(Add) button		FormContactNew formContactNew = (FormContactNew)app.zPageAddressbook.zToolbarPressButton(Button.B_NEW);		        // Fill in the form		formContactNew.zFillField(Field.FirstName, contactFirst);		formContactNew.zFillField(Field.LastName, contactLast);		formContactNew.zFillField(Field.Company, contactCompany);		// Click Save button		formContactNew.zSubmit();		//-- Data Verification		// The Emailed Contacts folder		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.EmailedContacts);				//-- Verification                //verify contact deleted        ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #firstname:"+ contactFirst);        ZAssert.assertNotNull(actual, "Verify the contact exists in the emailed contacts folder");        ZAssert.assertEquals(actual.getFolderId(), folder.getId(), "Verify the contact is in the Emailed Contacts");	}
}
