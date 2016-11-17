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
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.smime;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field;
import com.zimbra.qa.selenium.projects.ajax.ui.zimlet.DialogViewCertificate;


public class SendEncryptedMail extends AjaxCommonTest {

	public SendEncryptedMail() {
		
		logger.info("New "+ SendEncryptedMail.class.getCanonicalName());		
	}

	@Test ( description = "Verify that Signed message can be sent from Web-client correctly and user can view it", priority=4, 
			groups = { "smime"})
	
	public void SendEncryptedMail_01() throws HarnessException  {
		ZimbraAccount user1 = new ZimbraAccount("user1"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user1.provision();
		user1.authenticate();

		ZimbraAccount user4 = new ZimbraAccount("user4"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user4.provision();
		user4.authenticate();

		String user4PublicCert = "MIICTTCCAbYCAxAAATANBgkqhkiG9w0BAQsFADBuMQ8wDQYDVQQKEwZaaW1icmExIzAhBgkqhkiG9w0BCQEWFGFkbWluQHRlc3Rkb21haW4uY29tMRIwEAYDVQQHEwlQYWxvIEFsdG8xCzAJBgNVBAgTAkNBMRUwEwYDVQQDEwxUZXN0IERlbW8gQ0EwHhcNMTYxMTAyMDQ0MTI0WhcNMTcxMTAyMDQ0MTI0WjBuMQswCQYDVQQGEwJJTjELMAkGA1UECAwCTVMxEDAOBgNVBAoMB1N5bmFjb3IxDjAMBgNVBAMMBXVzZXI0MSMwIQYJKoZIhvcNAQkBFhR1c2VyNEB0ZXN0ZG9tYWluLmNvbTELMAkGA1UECwwCUkQwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAOpPcN6qG2yHRMxiqxlGg1Bm+dnBFjbpDbNZ9YDgTub+QzpLTyrySiofJoYeX0t2ToknEcgF1Jmb7i/I4/m5kxfKZCZT4Jv8cqoIdA7E2TXVZv62rodp6ZU/qEaubEOPrh/UQy4xW29iV2eIxcoBCamNnOf28vW2miyHeHUexipZAgMBAAEwDQYJKoZIhvcNAQELBQADgYEAqwkOiMOkfvmhqG0Ec2Ezsk5HAkZQt/1+qPPV6BBgb/mMCEMSmM+j/VJZtacQJ1wGWx51zlGbXWnESAkcIDHYtAMnmbPTOSEaYn9LGjHlboelI7wHHluoU2DyQFdwAkNvNMkegGIsnaiUv5YbMfndGdpxcknbqAUbbETk9eP1Qi4=";
		
		// Modify the test account and change zimbraFeatureSMIMEEnabled to TRUE
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user1.ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");

		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user4.ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");
		
		// Create file item
		String filePath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user1_digitalid.p12";

		// Upload file to server through RestUtil
		String attachmentId = user1.uploadFile(filePath);

		user1.soapSend(
				"<SaveSmimeCertificateRequest xmlns='urn:zimbraAccount'>" +
				"<upload id='" + attachmentId + "'></upload>" +
                "<password>test123</password>" +
                "</SaveSmimeCertificateRequest>");

		// Create file item
		filePath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user4_digitalid.p12";

		// Upload file to server through RestUtil
		 attachmentId = user4.uploadFile(filePath);

		user4.soapSend(
				"<SaveSmimeCertificateRequest xmlns='urn:zimbraAccount'>" +
				"<upload id='" + attachmentId + "'></upload>" +
                "<password>test123</password>" +
                "</SaveSmimeCertificateRequest>");
		
		user1.soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
				"<cn>"+
				"<a n='firstName'>user4</a>" +
				"<a n='lastName'>user</a>" +
				"<a n='email'>" + user4.EmailAddress + "</a>" +
				"<a n='userCertificate'>" + user4PublicCert + "</a>" +
				"</cn>" +
				"</CreateContactRequest>");
	
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(user1);
		
		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(user4));
		mail.dSubject = "SignedMessage" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "SignedMessageBody" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFill(mail);

		//Choose sign only from the secure email drop-down
		mailform.zToolbarPressPulldown(Button.B_SECURE_EMAIL, Button.O_SIGN_AND_ENCRYPT);
		SleepUtil.sleepMedium();
		
		// Send the message
		mailform.zSubmit();
		
		//Search for the signed mail in recipients inbox
		MailItem received = MailItem.importFromSOAP(user4, "subject:("+ mail.dSubject +")");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, user1.EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, user4.EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(received.dSubject, mail.dSubject, "Verify the subject field is correct");
		//ZAssert.assertStringContains(received.dBodyText, mail.dBodyHtml, "Verify the body field is correct");
		ZAssert.assertEquals(received.dIsSigned, "true", "Verify that message is signed correctly");

		//TODO UI verification to be done once UI changes and decryption stories are completed.
		/*
		//Login as the recipient
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(user4);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);
		
		// Verify body content
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), mail.dBodyHtml, "Verify plain text content");
		
		//Verify the Mail security String
		ZAssert.assertTrue(actual.zMailSecurityPresent(user1.EmailAddress), "Signed by String present");

		//Data required for matching certificate details
		String issuedToOrganization = "Synacor";
		String issuedByOrganization = "Zimbra";
		String issuedByEmail = "admin@testdomain.com";
		String algorithm = "SHA256WITHRSA";
		
		//Click on View certificate.
		actual.zPressButton(Button.B_VIEW_CERTIFICATE);
		DialogViewCertificate dialog = (DialogViewCertificate) new DialogViewCertificate(app, app.zPageMail);

		//Verify certificate details
		ZAssert.assertEquals(user1.EmailAddress, dialog.zGetDisplayedText(user1.EmailAddress),"Issued to email address matched");
		ZAssert.assertEquals(issuedToOrganization, dialog.zGetDisplayedTextIssuedToOrganization(),"Issued to Organisation matched");
		ZAssert.assertEquals(issuedByOrganization, dialog.zGetDisplayedTextIssuedByOrganization(),"Issued by Organisation matched");
		ZAssert.assertEquals(issuedByEmail, dialog.zGetDisplayedTextIssuedByEmail(),"Issued by email address matched");
		ZAssert.assertEquals(algorithm, dialog.zGetDisplayedTextAlgorithm(),"Algorithm matched");
*/
		
        //Deleting the account created for the test-case
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<DeleteAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user1.ZimbraId + "</id>"
			+	"</DeleteAccountRequest>");
		
        //Deleting the account created for the test-case
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<DeleteAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user4.ZimbraId + "</id>"
			+	"</DeleteAccountRequest>");
		

	}
	
}
