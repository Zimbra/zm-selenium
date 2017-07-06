
package com.zimbra.qa.selenium.projects.universal.tests.network.zimlets.smime.mail;

import java.util.List;
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
import com.zimbra.qa.selenium.projects.universal.ui.mail.DisplayConversation;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DisplayConversationMessage;
import com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DisplayMail.Field;

public class QuickReplySignedEncryptedMail extends UniversalCommonTest {

	public QuickReplySignedEncryptedMail() {
		
		logger.info("New "+ QuickReplySignedEncryptedMail.class.getCanonicalName());		
	}

	@Test ( description = "Verify sending quick reply to a signed email", priority=4, 
			groups = {"smoke", "L1", "network"})
	
	public void QuickReplySignedMail_01() throws HarnessException  {
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
			
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(user5);
		
		String subject = "subject"+ ConfigProperties.getUniqueString();
		String reply = "quickreply" + ConfigProperties.getUniqueString();
		user6.soapSend(
				"<SendSecureMsgRequest sign='true' xmlns='urn:zimbraMail'>" +
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
	
		// Select the conversation
		DisplayConversation display = (DisplayConversation)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Get the first mesage
		List<DisplayConversationMessage> messages = display.zListGetMessages();
				
		// Quick Reply
		messages.get(0).zPressButton(Button.B_QUICK_REPLY_REPLY);
		messages.get(0).zFillField(Field.Body, reply);
		messages.get(0).zPressButton(Button.B_QUICK_REPLY_SEND);	

		// Verify message in Sent
		MailItem sent = MailItem.importFromSOAP(user5, "subject:("+ subject +") from:("+ user5.EmailAddress +")");
		ZAssert.assertNotNull(sent, "Verify the message is in the sent folder");

		// Verify message is Received by sender
		MailItem received = MailItem.importFromSOAP(user6, "subject:("+ subject +") from:("+ user5.EmailAddress +")");
		ZAssert.assertNotNull(received, "Verify the message is received by the original sender");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, user5.EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, user6.EmailAddress, "Verify the to field is correct");
		ZAssert.assertStringContains(received.dBodyText, reply, "Verify the body field is correct");
		ZAssert.assertEquals(received.dIsSigned, "false", "Verify that message is signed correctly");
		
	}
	
	@Test ( description = "Verify sending quick reply(Forward) to an encrypted email", priority=4, 
			groups = {"smoke", "L1", "network"})
	
	public void QuickForwardSignedAndEncryptedMail_02() throws HarnessException  {
		ZimbraAccount user5 = new ZimbraAccount("user5"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user5.provision();
		user5.authenticate();

		ZimbraAccount user6 = new ZimbraAccount("user6"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user6.provision();
		user6.authenticate();
		
		ZimbraAccount user7 = new ZimbraAccount("user7"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user7.provision();
		user7.authenticate();
		
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
				+ "/data/private/certs/user5.cer";

		// Upload file to server through RestUtil
		String certId = user6.uploadFile(certPath);
		
		user6.soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
				"<cn>"+
				"<a n='firstName'>user5</a>" +
				"<a n='lastName'>user</a>" +
				"<a n='email'>" + user5.EmailAddress + "</a>" +
				"<a n='userCertificate' aid='" + certId + "'></a>" +
				"</cn>" +
				"</CreateContactRequest>");
		
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(user5);
		
		String subject = "subject"+ ConfigProperties.getUniqueString();
		String forward = "quickforward" + ConfigProperties.getUniqueString();
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
	
		// Select the conversation
		DisplayConversation display = (DisplayConversation)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Get the first mesage
		List<DisplayConversationMessage> messages = display.zListGetMessages();
				
		// Quick Reply
		FormMailNew form = (FormMailNew)messages.get(0).zPressButton(Button.B_QUICK_REPLY_FORWARD);
		form.zFillField(FormMailNew.Field.To, user7.EmailAddress);
		form.zFillField(FormMailNew.Field.Body, forward);
		form.zToolbarPressButton(Button.B_SEND);
		SleepUtil.sleepMedium();
		
		// Verify message in Sent
		MailItem sent = MailItem.importFromSOAP(user5, "subject:("+ subject +") from:("+ user5.EmailAddress +")");
		ZAssert.assertNotNull(sent, "Verify the message is in the sent folder");

		// Verify message is Received by sender
		MailItem received = MailItem.importFromSOAP(user7, "subject:("+ subject +") from:("+ user5.EmailAddress +")");
		ZAssert.assertNotNull(received, "Verify the message is received by the original sender");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, user5.EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress,  user7.EmailAddress, "Verify the to field is correct");
		ZAssert.assertStringContains(received.dBodyText, forward, "Verify the body field is correct");
		ZAssert.assertNull(received.dIsSigned,"Verify that message is signed correctly");
		ZAssert.assertNull(received.dIsEncrypted, "Verify that message is encrypted correctly");
		
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