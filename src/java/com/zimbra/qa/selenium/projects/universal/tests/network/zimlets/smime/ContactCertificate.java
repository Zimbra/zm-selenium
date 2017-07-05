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
package com.zimbra.qa.selenium.projects.universal.tests.network.zimlets.smime;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.FormContactNew;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.FormContactNew.Field;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.FormContactNew.Locators;
import com.zimbra.qa.selenium.projects.universal.ui.zimlet.DialogViewCertificate;

public class ContactCertificate extends AjaxCommonTest {

	public ContactCertificate() {
		
		super.startingPage = app.zPageContacts;
		logger.info("New "+ ContactCertificate.class.getCanonicalName());		
	}

	@Test ( description = "Verify that user's public certificate can be uploaded from contacts", priority=4, 
			groups = { "sanity", "L0", "network"})
	
	public void ContactUploadCertificate_01() throws HarnessException  {
		
		// -- DATA

		String contactFirst = "First" + ConfigProperties.getUniqueString();
		String contactLast = "Last" + ConfigProperties.getUniqueString();
		String contactEmail = "user4@testdomain.com";
		String contactCertificate = "MIICTTCCAbYCAxAAATANBgkqhkiG9w0BAQsFADBuMQ8wDQYDVQQKEwZaaW1icmExIzAhBgkqhkiG9w0BCQEWFGFkbWluQHRlc3Rkb21haW4uY29tMRIwEAYDVQQHEwlQYWxvIEFsdG8xCzAJBgNVBAgTAkNBMRUwEwYDVQQDEwxUZXN0IERlbW8gQ0EwHhcNMTYxMTAyMDQ0MTI0WhcNMTcxMTAyMDQ0MTI0WjBuMQswCQYDVQQGEwJJTjELMAkGA1UECAwCTVMxEDAOBgNVBAoMB1N5bmFjb3IxDjAMBgNVBAMMBXVzZXI0MSMwIQYJKoZIhvcNAQkBFhR1c2VyNEB0ZXN0ZG9tYWluLmNvbTELMAkGA1UECwwCUkQwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAOpPcN6qG2yHRMxiqxlGg1Bm+dnBFjbpDbNZ9YDgTub+QzpLTyrySiofJoYeX0t2ToknEcgF1Jmb7i/I4/m5kxfKZCZT4Jv8cqoIdA7E2TXVZv62rodp6ZU/qEaubEOPrh/UQy4xW29iV2eIxcoBCamNnOf28vW2miyHeHUexipZAgMBAAEwDQYJKoZIhvcNAQELBQADgYEAqwkOiMOkfvmhqG0Ec2Ezsk5HAkZQt/1+qPPV6BBgb/mMCEMSmM+j/VJZtacQJ1wGWx51zlGbXWnESAkcIDHYtAMnmbPTOSEaYn9LGjHlboelI7wHHluoU2DyQFdwAkNvNMkegGIsnaiUv5YbMfndGdpxcknbqAUbbETk9eP1Qi4=";
		// Create file item
		final String fileName = "user4.cer";
		final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\private\\certs\\" + fileName;
		
		// Modify the test account and change zimbraFeatureSMIMEEnabled to TRUE
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ app.zGetActiveAccount().ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");

		  //Refresh for changes
        app.zPageMain.sRefresh();		
        app.zPageContacts.zNavigateTo();
 		// -- GUI Action

		FormContactNew formContactNew = (FormContactNew) app.zPageContacts.zToolbarPressButton(Button.B_NEW);

		// Fill in the form
		formContactNew.zFillField(Field.FirstName, contactFirst);
		formContactNew.zFillField(Field.LastName, contactLast);
		formContactNew.zFillField(Field.Email, contactEmail);
		formContactNew.zToolbarPressButton(Button.B_BROWSE_TO_CERTIFICATE);
		
		//Browse and Upload file
		zUploadFile(filePath);
		SleepUtil.sleepMedium();
		formContactNew.zSubmit();

		//Verify that contact was created
		ZAssert.assertTrue(app.zPageContacts.zVerifyContactExists(contactLast), "Verify that contact is displayed");
		
		// Select the contact and verify that certificate is visible
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, contactLast);
		app.zPageContacts.zVerifyCertificatePresent(contactEmail);
		
		//Data required for matching certificate details
		String issuedToOrganization = "Synacor";
		String issuedByOrganization = "Zimbra";
		String issuedByEmail = "admin@testdomain.com";
		String algorithm = "SHA256with RSA";
		
		//View certificate of the contact
		app.zPageContacts.zToolbarPressButton(Button.B_VIEW_CERTIFICATE);
		DialogViewCertificate dialog = (DialogViewCertificate) new DialogViewCertificate(app, app.zPageMail);

		//Verify certificate details
		ZAssert.assertEquals(contactEmail, dialog.zGetDisplayedText(contactEmail),"Issued to email address matched");
		ZAssert.assertEquals(issuedToOrganization, dialog.zGetDisplayedTextIssuedToOrganization(),"Issued to Organisation matched");
		ZAssert.assertEquals(issuedByOrganization, dialog.zGetDisplayedTextIssuedByOrganization(),"Issued by Organisation matched");
		ZAssert.assertEquals(issuedByEmail, dialog.zGetDisplayedTextIssuedByEmail(),"Issued by email address matched");
		ZAssert.assertEquals(algorithm, dialog.zGetDisplayedTextAlgorithm(),"Algorithm matched");

		
		// -- Data Verification
		app.zGetActiveAccount().soapSend("<SearchRequest xmlns='urn:zimbraMail' types='contact'>" + "<query>#firstname:"
				+ contactFirst + "</query>" + "</SearchRequest>");
		String contactId = app.zGetActiveAccount().soapSelectValue("//mail:cn", "id");

		ZAssert.assertNotNull(contactId, "Verify the contact is returned in the search");

		app.zGetActiveAccount().soapSend("<GetContactsRequest xmlns='urn:zimbraMail'>" + "<cn id='" + contactId + "'/>"
				+ "</GetContactsRequest>");

		String lastname = app.zGetActiveAccount()
				.soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='lastName']", null);
		String firstname = app.zGetActiveAccount()
				.soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='firstName']", null);
		String email = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='email']",
				null);

		String certificate = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='userCertificate']",
				null);
		
		//To test the working of "returnCertInfo='1'" flag
		Element issuedTo = app.zGetActiveAccount().soapSelectNode("//mail:GetContactsResponse//mail:cn[@id='" + contactId + "']/acct:certificate//acct:issuedTo/acct:emailAddress", 1);
		ZAssert.assertNull(issuedTo, "Cerificate info is getting displayed in the responce even if the returnCertInfo='1' is not set!");
		
		ZAssert.assertEquals(lastname, contactLast, "Verify the last name was saved correctly");
		ZAssert.assertEquals(firstname, contactFirst, "Verify the first name was saved correctly");
		ZAssert.assertEquals(email, contactEmail, "Verify the email was saved correctly");
		ZAssert.assertEquals(certificate, contactCertificate, "Verify the certificate string was saved correctly");

	}

	@Test ( description = "Verify that user's public certificate can be updated from contacts", priority=4, 
			groups = { "functional", "L2", "network"})
	
	public void ContactEditCertificate_01() throws HarnessException  {
		
		// -- DATA

		String contactFirst = "First" + ConfigProperties.getUniqueString();
		String contactLast = "Last" + ConfigProperties.getUniqueString();
		String contactEmail = "user5@tesdomain.com";
		String contactCertificate = "MIICXjCCAccCAxAABTANBgkqhkiG9w0BAQsFADBzMQ8wDQYDVQQKEwZaSW1icmExKzApBgkqhkiG9w0BCQEWHGFkbWluQHpxYS0wNjEuZW5nLnppbWJyYS5jb20xEjAQBgNVBAcTCVBhbG8gQWx0bzELMAkGA1UECBMCQ0ExEjAQBgNVBAMTCVBJeXVzaCBDQTAeFw0xNjExMjQwNjQ3MTJaFw0xNzExMjQwNjQ3MTJaMHoxCzAJBgNVBAYTAkFVMRMwEQYDVQQIEwpTb21lLVN0YXRlMSEwHwYDVQQKExhJbnRlcm5ldCBXaWRnaXRzIFB0eSBMdGQxDjAMBgNVBAMTBXVzZXI1MSMwIQYJKoZIhvcNAQkBFhR1c2VyNUB0ZXN0ZG9tYWluLmNvbTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEApKxE7ryFY9XaFGsYN0QuJ6pTAxw+Ibs+FtFeJfhARrFdxcQ+D685oL56L4DSYowCCAuAc1DQq1Q0CV1ENZsjLm4iXEkiXQyMqpiDAI4A9yyVazIM1LICoB5K3bLF3feNYbCxgX6sMir6rZVEIBBMgkzCCxl6lrZ5/AoBMLIZnbsCAwEAATANBgkqhkiG9w0BAQsFAAOBgQA4lPrHIzH6P4CQLVohRZfz6P1ZbpGFAk7uHjmp0yhMPo50ePsn1U1qjIDgqRjjsfeUe7R1WieCX8bOSZkCiW9zsGAnV+Fi+B9+RWa52fe9iRDXFxmGKsxhArTxAOVuQHxWfPr+rCRPSrFfQPYihmh4go0M2Tf4XVJJV9Uqd6plUw==";
				
		// Modify the test account and change zimbraFeatureSMIMEEnabled to TRUE
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ app.zGetActiveAccount().ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");
		
		// Create file item
		String certPath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user5_old.cer";

		// Upload file to server through RestUtil
		String certId = app.zGetActiveAccount().uploadFile(certPath);
		
		//Create contact from backend
		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
				"<cn>"+
				"<a n='firstName'>" + contactFirst + "</a>" +
				"<a n='lastName'>" + contactLast + "</a>" +
				"<a n='email'>" + contactEmail + "</a>" +
				"<a n='userCertificate' aid='" + certId + "'></a>" +
				"</cn>" +
				"</CreateContactRequest>");		

		  //Refresh for changes
        app.zPageMain.sRefresh();		
        app.zPageContacts.zNavigateTo();
        
		// Select the contact
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, contactLast);
		
		// Click "Edit" from the toolbar
        FormContactNew form = (FormContactNew) app.zPageContacts.zToolbarPressButton(Button.B_EDIT);
        
        //Click on remove certificate link
		form.sClick(Locators.zRemoveCertificateLink);
		
		//Click on yes in the confirmation dialog
		DialogWarning dialog = (DialogWarning) new DialogWarning(DialogWarning.DialogWarningID.RemoveCertificate, app, app.zPagePreferences);
		dialog.zClickButton(Button.B_YES);
		
		// Create file item
		final String fileName = "user5.cer";
		final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\private\\certs\\" + fileName;
		
		form.zToolbarPressButton(Button.B_BROWSE_TO_CERTIFICATE);
		
		//Browse and Upload file
		zUploadFile(filePath);
		SleepUtil.sleepMedium();

		//Click on save
		form.zSubmit();
		
		// Select the contact and verify that certificate is visible
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, contactLast);
		app.zPageContacts.zVerifyCertificatePresent(contactEmail);

		//Data required for matching certificate details
		String issuedToOrganization = "Internet Widgits Pty Ltd";
		String issuedByOrganization = "ZImbra";
		
		//View certificate of the contact
		app.zPageContacts.zToolbarPressButton(Button.B_VIEW_CERTIFICATE);
		DialogViewCertificate dialog1 = (DialogViewCertificate) new DialogViewCertificate(app, app.zPageMail);

		//Verify certificate details
		ZAssert.assertEquals(issuedToOrganization, dialog1.zGetDisplayedTextIssuedToOrganization(),"Issued to Organisation matched");
		ZAssert.assertEquals(issuedByOrganization, dialog1.zGetDisplayedTextIssuedByOrganization(),"Issued by Organisation matched");
		
		// -- Data Verification

		app.zGetActiveAccount().soapSend("<SearchRequest xmlns='urn:zimbraMail' types='contact'>" + "<query>#firstname:"
				+ contactFirst + "</query>" + "</SearchRequest>");
		String contactId = app.zGetActiveAccount().soapSelectValue("//mail:cn", "id");

		ZAssert.assertNotNull(contactId, "Verify the contact is returned in the search");

		app.zGetActiveAccount().soapSend("<GetContactsRequest xmlns='urn:zimbraMail' returnCertInfo='1'>" + "<cn id='" + contactId + "'/>"
				+ "</GetContactsRequest>");

		String lastname = app.zGetActiveAccount()
				.soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='lastName']", null);
		String firstname = app.zGetActiveAccount()
				.soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='firstName']", null);
		String email = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='email']",
				null);

		String certificate = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='userCertificate']",
				null);
		
		//To test the working of "returnCertInfo='1'" flag
		Element issuedTo = app.zGetActiveAccount().soapSelectNode("//mail:GetContactsResponse//mail:cn[@id='" + contactId + "']/acct:certificate//acct:issuedTo/acct:emailAddress", 1);
		ZAssert.assertEquals(issuedTo.getText(),contactEmail, "Cerificate info is getting displayed in the responce when returnCertInfo='1' is set!");
				
		ZAssert.assertEquals(lastname, contactLast, "Verify the last name was saved correctly");
		ZAssert.assertEquals(firstname, contactFirst, "Verify the first name was saved correctly");
		ZAssert.assertEquals(email, contactEmail, "Verify the email was saved correctly");
		ZAssert.assertEquals(certificate, contactCertificate, "Verify the certificate string was saved correctly");

	}

	@Test ( description = "Verify that certificate can be deleted from a contact", priority=4, 
			groups = { "functional", "L2", "network"})
	
	public void ContactDeleteCertificate_01() throws HarnessException  {
		
		String contactFirst = "First" + ConfigProperties.getUniqueString();
		String contactLast = "Last" + ConfigProperties.getUniqueString();
		String contactEmail = "user6@testdomain.com";
		
		// Modify the test account and change zimbraFeatureSMIMEEnabled to TRUE
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ app.zGetActiveAccount().ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");
		
		// Create file item
		String certPath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user6.cer";

		// Upload file to server through RestUtil
		String certId = app.zGetActiveAccount().uploadFile(certPath);
		
		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
				"<cn>"+
				"<a n='firstName'>" + contactFirst + "</a>" +
				"<a n='lastName'>" + contactLast + "</a>" +
				"<a n='email'>" + contactEmail + "</a>" +
				"<a n='userCertificate' aid='" + certId + "'></a>" +
				"</cn>" +
				"</CreateContactRequest>");		

		  //Refresh for changes
        app.zPageMain.sRefresh();		
        app.zPageContacts.zNavigateTo();
        
		// Select the contact
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, contactLast);
		
		// Click "Edit" from the toolbar
        FormContactNew form = (FormContactNew) app.zPageContacts.zToolbarPressButton(Button.B_EDIT);
        
        //Click on remove certificate link
		form.sClick(Locators.zRemoveCertificateLink);
		
		//Click on yes in the confirmation dialog
		DialogWarning dialog = (DialogWarning) new DialogWarning(DialogWarning.DialogWarningID.RemoveCertificate, app, app.zPagePreferences);
		dialog.zClickButton(Button.B_YES);
		
		//Save contact after removing certificate
        form.zToolbarPressButton(Button.B_SAVE);
 		
        //Verify that contact is not deleted
		ZAssert.assertTrue(app.zPageContacts.zVerifyContactExists(contactLast), "Verify that contact is displayed");
		
       //Verify that contact's certificate row is not present
		ZAssert.assertFalse(app.zPageContacts.zVerifyCertificatePresent(contactEmail), "Verify that certificate is not visible after removing");
		ZAssert.assertFalse(app.zPageContacts.sIsElementPresent(com.zimbra.qa.selenium.projects.universal.ui.contacts.PageContacts.Locators.zViewCertificate), "Verify that view certificate link is not present");
		
		// -- Data Verification
		app.zGetActiveAccount().soapSend("<SearchRequest xmlns='urn:zimbraMail' types='contact'>" + "<query>#firstname:"
				+ contactFirst + "</query>" + "</SearchRequest>");
		String contactId = app.zGetActiveAccount().soapSelectValue("//mail:cn", "id");

		ZAssert.assertNotNull(contactId, "Verify the contact is returned in the search");

		app.zGetActiveAccount().soapSend("<GetContactsRequest xmlns='urn:zimbraMail'>" + "<cn id='" + contactId + "'/>"
				+ "</GetContactsRequest>");

		String lastname = app.zGetActiveAccount()
				.soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='lastName']", null);
		String firstname = app.zGetActiveAccount()
				.soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='firstName']", null);
		String email = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='email']",
				null);		
		String certificate = app.zGetActiveAccount().soapSelectValue("//mail:cn[@id='" + contactId + "']//mail:a[@n='userCertificate']",
				null);
		
		//To test the working of "returnCertInfo='1'" flag
		Element issuedTo = app.zGetActiveAccount().soapSelectNode("//mail:GetContactsResponse//mail:cn[@id='" + contactId + "']/acct:certificate//acct:issuedTo/acct:emailAddress", 1);
		ZAssert.assertNull(issuedTo, "Cerificate info is getting displayed in the responce even if the returnCertInfo='1' is not set!");

		ZAssert.assertEquals(lastname, contactLast, "Verify the last name was saved correctly");
		ZAssert.assertEquals(firstname, contactFirst, "Verify the first name was saved correctly");
		ZAssert.assertEquals(email, contactEmail, "Verify the email was saved correctly");
		ZAssert.assertNull(certificate, "Verify certificate was deleted");
	}

	}
