package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.inlineimage;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.OperatingSystem;
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowDisplayMail;

public class FwdMailWithAnInlineAttachment extends PrefGroupMailByMessageTest {

	public FwdMailWithAnInlineAttachment() {
		logger.info("New "+ FwdMailWithAnInlineAttachment.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
	}

	@Test( description = "Fwd a mail  with an inline attachment by pressing Forward button>>attach>>Inline - in separate window",
			groups = { "smoke", "L1" })
	
	public void FwdMailWithAnInlineAttachment_01() throws HarnessException {

		if (OperatingSystem.isWindows() == true && !ConfigProperties.getStringProperty("browser").contains("msedge")) {

			String subject = "subject"+ ConfigProperties.getUniqueString();
			String bodyText = "text" + ConfigProperties.getUniqueString();
			String bodyHTML = "text <strong>bold"+ ConfigProperties.getUniqueString() +"</strong> text";
			String contentHTML = XmlStringUtil.escapeXml(
					"<html>" +
							"<head></head>" +
							"<body>"+ bodyHTML +"</body>" +
					"</html>");


			// Send a message to the account
			ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
							"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='multipart/alternative'>" +
							"<mp ct='text/plain'>" +
							"<content>"+ bodyText +"</content>" +
							"</mp>" +
							"<mp ct='text/html'>" +
							"<content>"+ contentHTML +"</content>" +
							"</mp>" +
							"</mp>" +
							"</m>" +
					"</SendMsgRequest>");



			// Refresh current view
			ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

			FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);

			// Create file item
			final String fileName = "samplejpg.jpg";
			final String filePath = ConfigProperties.getBaseDirectory()+ "\\data\\public\\other\\" + fileName;

			SeparateWindowDisplayMail window = null;
			String windowTitle = "Zimbra: " + subject;

			try {

				// Choose Actions -> Launch in Window
				window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

				window.zSetWindowTitle(windowTitle);
				ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");
				
				//Forward Mail
				window.zToolbarPressButton(Button.B_FORWARD);
				String locator = FormMailNew.Locators.zToField;
				window.sType(locator, ZimbraAccount.AccountB().EmailAddress + ",");
				
				// Click Attach>>inline image
				window.zPressButton(Button.O_ATTACH_DROPDOWN);
				window.zPressButton(Button.B_ATTACH_INLINE);
				zUploadInlineImageAttachment(filePath);

				ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageAttachmentExistsInComposeWindow(windowTitle, 1),"Verify inline image is present in Fwd compose window");

				//click Send
				window.zToolbarPressButton(Button.B_SEND);
				
			} finally {
				app.zPageMain.zCloseWindow(window, windowTitle, app);
			}

			// Verify UI for attachment
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
			ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageAttachmentExistsInMail(),"Verify inline attachment exists in the email");

			// From the receiving end, verify the message details
			// Need 'in:inbox' to seprate the message from the sent message
			MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "in:inbox subject:("+subject +")");

			ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
			ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountB().EmailAddress, "Verify the to field is correct");
			ZAssert.assertStringContains(received.dSubject, "Fwd: " + subject, "Verify forward subject field is correct");

		} else {
			throw new SkipException("File upload operation is allowed only for Windows OS (Skipping upload tests on MS Edge for now due to intermittancy and major control issue), skipping this test...");
		}
	}

}