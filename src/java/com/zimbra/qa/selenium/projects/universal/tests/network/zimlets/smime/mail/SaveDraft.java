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
package com.zimbra.qa.selenium.projects.universal.tests.network.zimlets.smime.mail;


import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew;

public class SaveDraft extends AjaxCommonTest {

	public SaveDraft() {
		
		logger.info("New "+ SaveDraft.class.getCanonicalName());		
	}

	@Test ( description = "Verify that Signed email can be saved as draft and can be sent later", priority=4, 
			groups = {"smoke", "L1", "network"})
	
	public void SaveDraft_01() throws HarnessException  {
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
		mailform.zToolbarPressPulldown(Button.B_SECURE_EMAIL, Button.O_SIGN);
		SleepUtil.sleepSmall();

		// Save the message
		mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
		SleepUtil.sleepMedium();
		mailform.zToolbarPressButton(Button.B_CANCEL);

		// Go to draft		
		FolderItem drafts = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Drafts);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, drafts);

		// Select the mail,Click Edit
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);
		FormMailNew mailformEdit = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_EDIT);
		SleepUtil.sleepSmall();

		// Send the message
		mailformEdit.zSubmit();
		SleepUtil.sleepMedium();
		
		//Search for the signed mail in recipients inbox
		MailItem received = MailItem.importFromSOAP(user5, "subject:("+ mail.dSubject +")");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, user3.EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, user5.EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(received.dSubject, mail.dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(received.dBodyText, mail.dBodyHtml, "Verify the body field is correct");
		ZAssert.assertEquals(received.dIsSigned, "true", "Verify that message is signed correctly");
		
	}

	@Test ( description = "Verify that draft can be saved after replying an encrypted email", priority=4, 
			groups = {"functional", "L1", "network"})
	
	public void SaveDraftAFterReplyingEncryptedMail_02() throws HarnessException  {
		ZimbraAccount user5 = new ZimbraAccount("user5"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user5.provision();
		user5.authenticate();

		ZimbraAccount user6 = new ZimbraAccount("user6"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user6.provision();
		user6.authenticate();
		
		// Modify the test account and change zimbraFeatureSMIMEEnabled to TRUE
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user5.ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");

		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user6.ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");
		
		// Create file item
		String filePath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user5_digitalid.p12";

		// Upload file to server through RestUtil
		String attachmentId = user5.uploadFile(filePath);

		user5.soapSend(
				"<SaveSmimeCertificateRequest xmlns='urn:zimbraAccount'>" +
				"<upload id='" + attachmentId + "'></upload>" +
                "<password>zimbra</password>" +
                "</SaveSmimeCertificateRequest>");

		// Create file item
		filePath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user6_digitalid.p12";

		// Upload file to server through RestUtil
		 attachmentId = user6.uploadFile(filePath);

		user6.soapSend(
				"<SaveSmimeCertificateRequest xmlns='urn:zimbraAccount'>" +
				"<upload id='" + attachmentId + "'></upload>" +
                "<password>zimbra</password>" +
                "</SaveSmimeCertificateRequest>");
	
		// Create file item
		String certPath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user6.cer";

		// Upload file to server through RestUtil
		String certId = user5.uploadFile(certPath);
		
		user5.soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
				"<cn>"+
				"<a n='firstName'>user6</a>" +
				"<a n='lastName'>user</a>" +
				"<a n='email'>" + user6.EmailAddress + "</a>" +
				"<a n='userCertificate' aid='" + certId + "'></a>" +
				"</cn>" +
				"</CreateContactRequest>");

		// Create file item
		String certPath2 = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user5.cer";

		// Upload file to server through RestUtil
		String certId2 = user5.uploadFile(certPath2);
		
		user5.soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
				"<cn>"+
				"<a n='firstName'>user5</a>" +
				"<a n='lastName'>user</a>" +
				"<a n='email'>" + user5.EmailAddress + "</a>" +
				"<a n='userCertificate' aid='" + certId2 + "'></a>" +
				"</cn>" +
				"</CreateContactRequest>");
		
		// Create file item
		String certPath1 = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user5.cer";

		// Upload file to server through RestUtil
		String certId1 = user6.uploadFile(certPath1);
		
		user6.soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
				"<cn>"+
				"<a n='firstName'>user5</a>" +
				"<a n='lastName'>user</a>" +
				"<a n='email'>" + user5.EmailAddress + "</a>" +
				"<a n='userCertificate' aid='" + certId1 + "'></a>" +
				"</cn>" +
				"</CreateContactRequest>");
		
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(user5);
		
		String subject = "subject"+ ConfigProperties.getUniqueString();
		user6.soapSend(
				"<SendSecureMsgRequest sign='true' encrypt='true' xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ user5.EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendSecureMsgRequest>");
	
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");
	
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
	
		// Reply the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);

		// Fill out the form with the data
		String bodyContent = "Signed Content "+ ConfigProperties.getUniqueString();
		mailform.zFillField(com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew.Field.Body, bodyContent);
		
		//Choose sign only from the secure email drop-down
		mailform.zToolbarPressPulldown(Button.B_SECURE_EMAIL, Button.O_SIGN_AND_ENCRYPT);
		SleepUtil.sleepSmall();

		// Save the message
		mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
		SleepUtil.sleepMedium();
		mailform.zToolbarPressButton(Button.B_CANCEL);

		// Go to draft		
		FolderItem drafts = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Drafts);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, drafts);

		// Select the mail,Click Edit
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		FormMailNew mailformEdit = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_EDIT);
		SleepUtil.sleepSmall();

		// Send the message
		mailformEdit.zSubmit();
		SleepUtil.sleepMedium();
		
		//Search for the signed mail in recipients inbox
		MailItem received = MailItem.importFromSOAP(user6, "in:inbox subject:("+ subject +")");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, user5.EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, user6.EmailAddress, "Verify the to field is correct");
		ZAssert.assertStringContains(received.dBodyText, bodyContent, "Verify the body field is correct");
		ZAssert.assertEquals(received.dIsSigned, "true", "Verify that message is signed correctly");
		
		user6.soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m id='"+ received.getId() +"'/>"
				+	"</GetMsgRequest>");

		Element[] nodes = user6.soapSelectNodes("//mail:mp[@cd='attachment']");
		ZAssert.assertEquals(nodes.length, 1, "Verify the attachment does not exist in the replied mail");
		
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
				"<account by='name'>user5@testdomain.com</account>" + 
				"</GetAccountRequest>");
        String user5Id = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//admin:GetAccountResponse/admin:account", "id");
		
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<DeleteAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user5Id + "</id>"
			+	"</DeleteAccountRequest>");
		
	}

		
}
