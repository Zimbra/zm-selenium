package com.zimbra.qa.selenium.projects.universal.tests.network.zimlets.smime.mail;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DisplayMail.Field;
import com.zimbra.qa.selenium.projects.universal.ui.zimlet.DialogViewCertificate;

public class ViewEncryptedEmail extends UniversalCommonTest {

	public ViewEncryptedEmail() {
		
		logger.info("New "+ ViewEncryptedEmail.class.getCanonicalName());		
	}
	
	@Test ( description = "Verify attached certificate for a signed and ecnrypted email with attachment", priority=4, 
			groups = {"functional", "L2", "network"})
	
	public void ViewEncryptedEmailWithAttachment_01() throws HarnessException  {
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
		String content = "content"+ ConfigProperties.getUniqueString();
		String filename = "testpdffile.pdf";
		String filePath1 = ConfigProperties.getBaseDirectory() + "/data/public/other/"+ filename;
		String dAttachmentId  = user6.uploadFile(filePath1);
		
		user6.soapSend(
				"<SendSecureMsgRequest sign='true' encrypt='true' xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ user5.EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ content +"</content>" +
						"</mp>" +
						"<attach aid='" + dAttachmentId + "'/>"+	
					"</m>" +
				"</SendSecureMsgRequest>");

		SleepUtil.sleepMedium();
					
		//Search for the signed mail in recipients inbox
		MailItem received = MailItem.importFromSOAP(user5, "in:inbox subject:("+ subject +")");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, user6.EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, user5.EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(received.dIsSigned, "true", "Verify that message is signed correctly");
		ZAssert.assertEquals(received.dIsEncrypted, "true", "Verify that message is encrypted correctly");

		user5.soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m id='"+ received.getId() +"'/>"
				+	"</GetMsgRequest>");

		String filename1 = user5.soapSelectValue("//mail:mp[@cd='attachment']", "filename");
		ZAssert.assertEquals(filename1, filename, "Verify the attachment exists in the forwarded mail");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");
		
		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Verify body content
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), content, "Verify plain text content");
		
		//Verify the Mail security String
		ZAssert.assertTrue(actual.zMailSecurityPresent(user6.EmailAddress), "Signed and Encrypted by String present");

		//Data required for matching certificate details
		String issuedToOrganization = "Internet Widgits Pty Ltd";
		String issuedByOrganization = "ZImbra";
		String issuedByEmail = "admin@zqa-061.eng.zimbra.com";
		String algorithm = "SHA256WITHRSA";
		
		//Click on View certificate.
		actual.zPressButton(Button.B_VIEW_CERTIFICATE);
		DialogViewCertificate dialog = (DialogViewCertificate) new DialogViewCertificate(app, app.zPageMail);

		//Verify certificate details
		ZAssert.assertEquals(user6.EmailAddress, dialog.zGetDisplayedText(user6.EmailAddress),"Issued to email address matched");
		ZAssert.assertEquals(issuedToOrganization, dialog.zGetDisplayedTextIssuedToOrganization(),"Issued to Organisation matched");
		ZAssert.assertEquals(issuedByOrganization, dialog.zGetDisplayedTextIssuedByOrganization(),"Issued by Organisation matched");
		ZAssert.assertEquals(issuedByEmail, dialog.zGetDisplayedTextIssuedByEmail(),"Issued by email address matched");
		ZAssert.assertEquals(algorithm, dialog.zGetDisplayedTextAlgorithm(),"Algorithm matched");
		
	}

	@AfterMethod(groups={"always"})
	public void afterMethod() throws HarnessException {
	
	ZimbraAdminAccount.GlobalAdmin().soapSend(
			"<GetAccountRequest xmlns='urn:zimbraAdmin'> "+
			"<account by='name'>user6@testdomain.com</account>" + 
			"</GetAccountRequest>");
    String user6Id = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//admin:GetAccountResponse/admin:account", "id");
	
	ZimbraAdminAccount.GlobalAdmin().soapSend(
			"<DeleteAccountRequest xmlns='urn:zimbraAdmin'>"
		+		"<id>"+ user6Id + "</id>"
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
