package com.zimbra.qa.selenium.projects.touch.tests.mail.mail.message;

import java.awt.AWTException;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
//import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByConversationTest;
import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByMessageTest;

public class MarkSpamMessage extends PrefGroupMailByMessageTest{

	public MarkSpamMessage() {
		logger.info("New "+ MarkSpamMessage.class.getCanonicalName());
	}
	@Test(	description = "Mark message as spam, from option menu",
			groups = { "sanity" })


	public void MarkSpamMessage_01() throws HarnessException, AWTException {

		String subject = "subject"+ ZimbraSeleniumProperties.getUniqueString();
		FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		String folderName = "folder" + ZimbraSeleniumProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='"+ folderName +"' l='"+ userRoot.getId() +"'/>" +
				"</CreateFolderRequest>");

		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ZimbraSeleniumProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		// Get the mail item for the new message
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		app.zPageMail.zRefresh();	

		// Mark the message as spam
		app.zPageMail.zListItem(Action.A_LEFTCLICK, Button.B_SPAM_MESSAGE, subject);

		// Verification
		FolderItem junk = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Junk);
		mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:("+ subject +")");
		ZAssert.assertNotNull(mail, "Make sure the mail is found");
		ZAssert.assertEquals(mail.dFolderId, junk.getId(), "Verify the message is in the spam folder");
	}	
}