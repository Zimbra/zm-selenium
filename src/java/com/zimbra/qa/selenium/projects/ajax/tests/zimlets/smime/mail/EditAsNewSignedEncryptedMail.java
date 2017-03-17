
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.smime.mail;

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
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowDisplayMail;

public class EditAsNewSignedEncryptedMail extends AjaxCommonTest {

	public EditAsNewSignedEncryptedMail() {
		
		logger.info("New "+ EditAsNewSignedEncryptedMail.class.getCanonicalName());		
	}

	@Test ( description = "Verify that signed and encrypted email can be edited as new from new window", priority=4, 
			groups = {"functional", "L2", "network"})
	
	public void EditAsNewSignedEncryptedMail_01() throws HarnessException  {
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
		
		SeparateWindowDisplayMail window = null;
		MailItem mail = new MailItem();
		mail.dBodyHtml = " body"+ ConfigProperties.getUniqueString();
		
		String windowTitle = "Zimbra: " + subject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);
			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");			

			windowTitle = "Zimbra: Compose";
			window.zToolbarPressPulldown(Button.B_ACTIONS, Button.O_EDIT_AS_NEW);
			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
			
			// Type in body
			String locator = "css=div[id^='zv__COMPOSE'] iframe[id$='_body_ifr']";

			window.sSelectFrame(locator);
			window.sClick(locator);
			window.zTypeCharacters(mail.dBodyHtml);

			SleepUtil.sleepSmall();	
			//Choose sign only from the secure email drop-down
			window.zToolbarPressPulldown(Button.B_SECURE_EMAIL, Button.O_SIGN_AND_ENCRYPT);
			SleepUtil.sleepSmall();
			window.zToolbarPressButton(Button.B_SEND);
			
			windowTitle = "Zimbra: " + subject;
			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			window.zToolbarPressButton(Button.B_CLOSE);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}
		
		SleepUtil.sleepMedium();
		
		//Search for the signed mail in recipients inbox
		MailItem received = MailItem.importFromSOAP(user5, "in:sent subject:("+ subject +")");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, user5.EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, user5.EmailAddress, "Verify the to field is correct");
		ZAssert.assertStringContains(received.dBodyText, mail.dBodyHtml, "Verify the body field is correct");
		ZAssert.assertEquals(received.dIsSigned, "true", "Verify that message is signed correctly");
		ZAssert.assertEquals(received.dIsEncrypted, "true", "Verify that message is encrypted correctly");
		
	}

	@Test ( description = "Verify that signed email can be edited as new", priority=4, 
			groups = {"functional", "L2", "network"})
	
	public void EditAsNewSignedMail_02() throws HarnessException  {
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
		String content = "content"+ ConfigProperties.getUniqueString();
		user6.soapSend(
				"<SendSecureMsgRequest sign='true' xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ user5.EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ content +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendSecureMsgRequest>");
	
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");
	
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		FormMailNew form = (FormMailNew)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.O_EDIT_AS_NEW);
		form.zToolbarPressPulldown(Button.B_SECURE_EMAIL, Button.O_SIGN);
		form.zSubmit();
		
		SleepUtil.sleepMedium();
		
		//Search for the signed mail in recipients inbox
		MailItem received = MailItem.importFromSOAP(user5, "in:sent subject:("+ subject +")");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, user5.EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, user5.EmailAddress, "Verify the to field is correct");
		ZAssert.assertStringContains(received.dBodyText, content, "Verify the body field is correct");
		ZAssert.assertEquals(received.dIsSigned, "true", "Verify that message is signed correctly");
		
	}

	@Test ( description = "Verify that signed and encrypted email can be edited as new", priority=4, 
			groups = {"functional", "L2", "network"})
	
	public void EditAsNewSignedEncryptedMail_03() throws HarnessException  {
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
		
		FormMailNew form = (FormMailNew)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.O_EDIT_AS_NEW);
		form.zToolbarPressPulldown(Button.B_SECURE_EMAIL, Button.O_SIGN_AND_ENCRYPT);
		SleepUtil.sleepSmall();

		form.zSubmit();
		SleepUtil.sleepMedium();
		
		//Search for the signed mail in recipients inbox
		MailItem received = MailItem.importFromSOAP(user5, "in:sent subject:("+ subject +")");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, user5.EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, user5.EmailAddress, "Verify the to field is correct");
		ZAssert.assertStringContains(received.dBodyText, subject, "Verify the body field is correct");
		ZAssert.assertEquals(received.dIsSigned, "true", "Verify that message is signed correctly");
		ZAssert.assertEquals(received.dIsEncrypted, "true", "Verify that message is encrypted correctly");
		
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