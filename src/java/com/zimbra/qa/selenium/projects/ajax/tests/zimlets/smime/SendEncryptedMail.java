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

import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogError;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogError.DialogErrorID;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowDisplayMail;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowFormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Locators;
import com.zimbra.qa.selenium.projects.ajax.ui.zimlet.DialogViewCertificate;
import org.openqa.selenium.NoSuchWindowException; 

public class SendEncryptedMail extends AjaxCommonTest {

	public SendEncryptedMail() {
		
		logger.info("New "+ SendEncryptedMail.class.getCanonicalName());		
	}

	@Test ( description = "Verify that Signed and encrypted message can be sent from Web-client correctly and user can view it", priority=4, 
			groups = { "smime", "L4"})
	
	public void SendEncryptedMail_01() throws HarnessException  {
		ZimbraAccount user3 = new ZimbraAccount("user3"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user3.provision();
		user3.authenticate();

		ZimbraAccount user5 = new ZimbraAccount("user5"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user5.provision();
		user5.authenticate();
		
		// Modify the test account and change zimbraFeatureSMIMEEnabled to TRUE
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user3.ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");

		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user5.ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");
		
		// Create file item
		String filePath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user3_digitalid.p12";

		// Upload file to server through RestUtil
		String attachmentId = user3.uploadFile(filePath);

		user3.soapSend(
				"<SaveSmimeCertificateRequest xmlns='urn:zimbraAccount'>" +
				"<upload id='" + attachmentId + "'></upload>" +
                "<password>test123</password>" +
                "</SaveSmimeCertificateRequest>");

		// Create file item
		filePath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user5_digitalid.p12";

		// Upload file to server through RestUtil
		 attachmentId = user5.uploadFile(filePath);

		user5.soapSend(
				"<SaveSmimeCertificateRequest xmlns='urn:zimbraAccount'>" +
				"<upload id='" + attachmentId + "'></upload>" +
                "<password>zimbra</password>" +
                "</SaveSmimeCertificateRequest>");
	
		// Create file item
		String certPath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user5.cer";

		// Upload file to server through RestUtil
		String certId = user3.uploadFile(certPath);

		
		user3.soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
				"<cn>"+
				"<a n='firstName'>user5</a>" +
				"<a n='lastName'>user</a>" +
				"<a n='email'>" + user5.EmailAddress + "</a>" +
				"<a n='userCertificate' aid='" + certId + "'></a>" +
				"</cn>" +
				"</CreateContactRequest>");
	
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(user3);
		
		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(user5));
		mail.dSubject = "Signed and Encrypted Message" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "Signed and Encrypted Message Body" + ConfigProperties.getUniqueString();

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
		MailItem received = MailItem.importFromSOAP(user5, "subject:("+ mail.dSubject +")");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, user3.EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, user5.EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(received.dSubject, mail.dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(received.dBodyText, mail.dBodyHtml, "Verify the body field is correct");
		ZAssert.assertEquals(received.dIsSigned, "true", "Verify that message is signed correctly");
		ZAssert.assertEquals(received.dIsEncrypted, "true", "Verify that message is encrypted correctly");

		//Login as the recipient
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(user5);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);
		
		// Verify body content
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), mail.dBodyHtml, "Verify plain text content");
		
		//Verify the Mail security String
		ZAssert.assertTrue(actual.zMailSecurityPresent(user3.EmailAddress), "Signed and Encrypted by String present");

		//Data required for matching certificate details
		String issuedToOrganization = "Synacor";
		String issuedByOrganization = "Zimbra";
		String issuedByEmail = "admin@testdomain.com";
		String algorithm = "SHA256WITHRSA";
		
		//Click on View certificate.
		actual.zPressButton(Button.B_VIEW_CERTIFICATE);
		DialogViewCertificate dialog = (DialogViewCertificate) new DialogViewCertificate(app, app.zPageMail);

		//Verify certificate details
		ZAssert.assertEquals(user3.EmailAddress, dialog.zGetDisplayedText(user3.EmailAddress),"Issued to email address matched");
		ZAssert.assertEquals(issuedToOrganization, dialog.zGetDisplayedTextIssuedToOrganization(),"Issued to Organisation matched");
		ZAssert.assertEquals(issuedByOrganization, dialog.zGetDisplayedTextIssuedByOrganization(),"Issued by Organisation matched");
		ZAssert.assertEquals(issuedByEmail, dialog.zGetDisplayedTextIssuedByEmail(),"Issued by email address matched");
		ZAssert.assertEquals(algorithm, dialog.zGetDisplayedTextAlgorithm(),"Algorithm matched");
		
	}
	
	@Test ( description = "Verify that Signed and encrypted message can be sent from Web-client correctly and user can view it in new window", priority=4, 
			groups = { "smime","L4"})
	
	public void SendEncryptedMail_02() throws HarnessException  {
		ZimbraAccount user3 = new ZimbraAccount("user3"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user3.provision();
		user3.authenticate();

		ZimbraAccount user5 = new ZimbraAccount("user5"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user5.provision();
		user5.authenticate();
		
		// Modify the test account and change zimbraFeatureSMIMEEnabled to TRUE
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user3.ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");

		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user5.ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");
		
		// Create file item
		String filePath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user3_digitalid.p12";

		// Upload file to server through RestUtil
		String attachmentId = user3.uploadFile(filePath);

		user3.soapSend(
				"<SaveSmimeCertificateRequest xmlns='urn:zimbraAccount'>" +
				"<upload id='" + attachmentId + "'></upload>" +
                "<password>test123</password>" +
                "</SaveSmimeCertificateRequest>");

		// Create file item
		filePath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user5_digitalid.p12";

		// Upload file to server through RestUtil
		 attachmentId = user5.uploadFile(filePath);

		user5.soapSend(
				"<SaveSmimeCertificateRequest xmlns='urn:zimbraAccount'>" +
				"<upload id='" + attachmentId + "'></upload>" +
                "<password>zimbra</password>" +
                "</SaveSmimeCertificateRequest>");
	
		// Create file item
		String certPath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user5.cer";

		// Upload file to server through RestUtil
		String certId = user3.uploadFile(certPath);

		
		user3.soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
				"<cn>"+
				"<a n='firstName'>user5</a>" +
				"<a n='lastName'>user</a>" +
				"<a n='email'>" + user5.EmailAddress + "</a>" +
				"<a n='userCertificate' aid='" + certId + "'></a>" +
				"</cn>" +
				"</CreateContactRequest>");
	
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(user3);
		
		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(user5));
		mail.dSubject = "Signed and Encrypted Message" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "Signed and Encrypted Message Body" + ConfigProperties.getUniqueString();

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
		MailItem received = MailItem.importFromSOAP(user5, "subject:("+ mail.dSubject +")");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, user3.EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, user5.EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(received.dSubject, mail.dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(received.dBodyText, mail.dBodyHtml, "Verify the body field is correct");
		ZAssert.assertEquals(received.dIsSigned, "true", "Verify that message is signed correctly");
		ZAssert.assertEquals(received.dIsEncrypted, "true", "Verify that message is encrypted correctly");

		//Login as the recipient
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(user5);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);
		
		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + mail.dSubject;
		
		try {
			
			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);
			
			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
			
			// Verify body content
			ZAssert.assertStringContains(window.zGetMailProperty(Field.Body), mail.dBodyHtml, "Verify plain text content");
			
			//Verify the Mail security String
			ZAssert.assertTrue(window.zMailSecurityPresent(user3.EmailAddress), "Signed and Encrypted by String present");
			
			//Data required for matching certificate details
			String issuedToOrganization = "Synacor";
			String issuedByOrganization = "Zimbra";
			String issuedByEmail = "admin@testdomain.com";
			String algorithm = "SHA256WITHRSA";
			
			//Click on View certificate.
			window.zPressButton(Button.B_VIEW_CERTIFICATE);
			List<String> windowIds=app.zPageMain.sGetAllWindowIds();
			if (windowIds.size() > 1) {

				for(String id: windowIds) {

				app.zPageMain.sSelectWindow(id);
					if (app.zPageMain.sGetTitle().contains("Zimbra:Inbox") ){
						app.zPageMain.zSelectWindow(id);;
						}
					}
				}
			DialogViewCertificate dialog = (DialogViewCertificate) new DialogViewCertificate(app, app.zPageMail);

			//Verify certificate details
			ZAssert.assertEquals(user3.EmailAddress, dialog.zGetDisplayedText(user3.EmailAddress),"Issued to email address matched");
			ZAssert.assertEquals(issuedToOrganization, dialog.zGetDisplayedTextIssuedToOrganization(),"Issued to Organisation matched");
			ZAssert.assertEquals(issuedByOrganization, dialog.zGetDisplayedTextIssuedByOrganization(),"Issued by Organisation matched");
			ZAssert.assertEquals(issuedByEmail, dialog.zGetDisplayedTextIssuedByEmail(),"Issued by email address matched");
			ZAssert.assertEquals(algorithm, dialog.zGetDisplayedTextAlgorithm(),"Algorithm matched");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);			
		}		
	}

	@Test ( description = "Verify that proper error is displayed when trying to sendSigned and encrypted message without public key of the receiver", priority=4, 
			groups = { "smime", "L4"})
	
	public void SendEncryptedMail_03() throws HarnessException  {

		ZimbraAccount user4 = new ZimbraAccount("user4"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user4.provision();
		user4.authenticate();
		
		// Modify the test account and change zimbraFeatureSMIMEEnabled to TRUE
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user4.ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");
		
		// Create file item
		String filePath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user4_digitalid.p12";

		// Upload file to server through RestUtil
		String attachmentId = user4.uploadFile(filePath);

		user4.soapSend(
				"<SaveSmimeCertificateRequest xmlns='urn:zimbraAccount'>" +
				"<upload id='" + attachmentId + "'></upload>" +
                "<password>test123</password>" +
                "</SaveSmimeCertificateRequest>");
		
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(user4);
		
		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
		mail.dSubject = "Signed and Encrypted Message" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "Signed and Encrypted Message Body" + ConfigProperties.getUniqueString();

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
		
		// Verification
		DialogError error = new DialogError(DialogErrorID.Zimbra, app, app.zPageContacts);
		ZAssert.assertEquals(error.zGetWarningContent(), "Message encryption failed. No valid public certificate found for " + ZimbraAccount.AccountA().EmailAddress +"", "Verify error message sending email");
		error.zClickButton(Button.B_OK);
        
		
	}

	@Test ( description = "Verify that proper error is displayed when trying to sendSigned and encrypted message without private key of the sender", priority=4, 
			groups = { "smime", "L2"})
	
	public void SendEncryptedMail_04() throws HarnessException  {
		
		// Modify the test account and change zimbraFeatureSMIMEEnabled to TRUE
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ app.zGetActiveAccount().ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");
		
		app.zPageMain.sRefreshPage();
				
		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.Account10()));
		mail.dSubject = "Signed and Encrypted Message" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "Signed and Encrypted Message Body" + ConfigProperties.getUniqueString();

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
		
		// Verification
		DialogError error = new DialogError(DialogErrorID.Zimbra, app, app.zPageMail);
		ZAssert.assertEquals(error.zGetWarningContent(), "Message encryption failed. No certificate found.", "Verify error message sending email");
		error.zClickButton(Button.B_OK);
		
	}

	@Test ( description = "Verify that Signed and encrypted messged cannot be viewed if user has not uploaded the private key", priority=4, 
			groups = { "smime", "L4"})
	
	public void SendEncryptedMail_05() throws HarnessException  {
		ZimbraAccount user3 = new ZimbraAccount("user3"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user3.provision();
		user3.authenticate();

		ZimbraAccount user5 = new ZimbraAccount("user5"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user5.provision();
		user5.authenticate();
		
		// Modify the test account and change zimbraFeatureSMIMEEnabled to TRUE
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user3.ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");

		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user5.ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");
		
		// Create file item
		String filePath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user3_digitalid.p12";

		// Upload file to server through RestUtil
		String attachmentId = user3.uploadFile(filePath);

		user3.soapSend(
				"<SaveSmimeCertificateRequest xmlns='urn:zimbraAccount'>" +
				"<upload id='" + attachmentId + "'></upload>" +
                "<password>test123</password>" +
                "</SaveSmimeCertificateRequest>");
	
		// Create file item
		String certPath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user5.cer";

		// Upload file to server through RestUtil
		String certId = user3.uploadFile(certPath);

		//Create contact
		user3.soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
				"<cn>"+
				"<a n='firstName'>user5</a>" +
				"<a n='lastName'>user</a>" +
				"<a n='email'>" + user5.EmailAddress + "</a>" +
				"<a n='userCertificate' aid='" + certId + "'></a>" +
				"</cn>" +
				"</CreateContactRequest>");
		
		// Create the message data to be sent
		String subject = "Signed and Encrypted Message" + ConfigProperties.getUniqueString();
		String bodyContent = "Signed and Encrypted Message Body" + ConfigProperties.getUniqueString();

		user3.soapSend(
				"<SendSecureMsgRequest sign='true' encrypt='true' xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ user5.EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>"+ "body" + bodyContent +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendSecureMsgRequest>");
		

		//Login as the recipient
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(user5);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		//Verify the Mail security String
		ZAssert.assertTrue(actual.zMessageCannotBeDecrypted(), "Message cannot be decrypted string present");

	}

	@Test ( description = "Verify that proper error is displayed when trying to send Signed and encrypted message in new window without public key of the receiver", priority=4, 
			groups = { "smime", "L3"})
	
	public void SendEncryptedMail_06() throws HarnessException  {

		ZimbraAccount user4 = new ZimbraAccount("user4"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user4.provision();
		user4.authenticate();
		
		// Modify the test account and change zimbraFeatureSMIMEEnabled to TRUE
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user4.ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");
		
		// Create file item
		String filePath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user4_digitalid.p12";

		// Upload file to server through RestUtil
		String attachmentId = user4.uploadFile(filePath);

		user4.soapSend(
				"<SaveSmimeCertificateRequest xmlns='urn:zimbraAccount'>" +
				"<upload id='" + attachmentId + "'></upload>" +
                "<password>test123</password>" +
                "</SaveSmimeCertificateRequest>");
		
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(user4);

		String subject = "Signed and Encrypted Message" + ConfigProperties.getUniqueString();
		String body = "Signed and Encrypted Message Body" + ConfigProperties.getUniqueString();
		
		// Create the message data to be sent
		FormMailNew mail = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mail, "Verify the new form opened");
		mail.zFillField(com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field.To, ZimbraAccount.AccountB().EmailAddress);
		mail.zFillField(com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field.Subject, subject);
		mail.zFillField(com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field.Body, body);	

		SeparateWindowFormMailNew window = null;
		String windowTitle = "Zimbra: Compose";

		try {

			window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_DETACH_COMPOSE);

			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			window.waitForComposeWindow();			
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

			// Select the window
			window.sSelectWindow(windowTitle);					

			// Verify the data appearing in fields in New window
			ZAssert.assertStringContains(
					mail.sGetText(Locators.zBubbleToField) + "@" + ConfigProperties.getStringProperty("testdomain"),
					ZimbraAccount.AccountB().EmailAddress, "Verify To field value");
			ZAssert.assertEquals(mail.sGetValue(Locators.zSubjectField),subject, "Verify Subject field value");
			ZAssert.assertStringContains(mail.zGetHtmltBodyText(),body, "Verify Body field value");
			mail.zToolbarPressPulldown(Button.B_SECURE_EMAIL, Button.O_SIGN_AND_ENCRYPT);
			// Send the message
			mail.zSubmit();
			
			// Verification
			DialogError error = new DialogError(DialogErrorID.Zimbra, app, app.zPageContacts);
			ZAssert.assertEquals(error.zGetWarningContent(), "Message encryption failed. No valid public certificate found for " + ZimbraAccount.AccountB().EmailAddress +"", "Verify error message sending email");
			error.zClickButton(Button.B_OK);
			
		} catch (NoSuchWindowException e) {
			
		}
		
		 finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}
        		
	}
	
		@AfterMethod(groups={"always"})
	public void afterMethod() throws HarnessException {
		
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'> "+
				"<account by='name'>user3@testdomain.com</account>" + 
				"</GetAccountRequest>");
        String user3Id = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//admin:GetAccountResponse/admin:account", "id");
		
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<DeleteAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user3Id + "</id>"
			+	"</DeleteAccountRequest>");

		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'> "+
				"<account by='name'>user4@testdomain.com</account>" + 
				"</GetAccountRequest>");
        String user4Id = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//admin:GetAccountResponse/admin:account", "id");
		
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<DeleteAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user4Id + "</id>"
			+	"</DeleteAccountRequest>");
		
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'> "+
				"<account by='name'>user5@testdomain.com</account>" + 
				"</GetAccountRequest>");
        String user5Id = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//admin:GetAccountResponse/admin:account", "id");
		
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<DeleteAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user5Id + "</id>"
			+	"</DeleteAccountRequest>");
		
	}

		
}
