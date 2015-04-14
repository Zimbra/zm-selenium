package com.zimbra.qa.selenium.projects.touch.tests.mail.mail.message;

import java.awt.AWTException;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
//import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByConversationTest;
import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.touch.ui.PageCreateFolder;

public class MarkNotSpamMessage extends PrefGroupMailByMessageTest{

	public MarkNotSpamMessage() {
		logger.info("New "+ MarkNotSpamMessage.class.getCanonicalName());
	}
	@Test(	description = "Mark message as not spam, from option menu",
			groups = { "smoke" })

	public void MarkNotSpamMessage_01() throws HarnessException, AWTException {

		String subject = "subject"+ ZimbraSeleniumProperties.getUniqueString();
		//FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
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

		// Switch to message view
		app.zPageMail.zListItem(Action.A_LEFTCLICK, Button.B_SPAM_MESSAGE, subject);
		PageCreateFolder createFolderPage = new PageCreateFolder(app, startingPage);
		createFolderPage.zSelectFolder("Junk");
		app.zPageMail.zListItem(Action.A_LEFTCLICK, Button.B_NOT_SPAM_MESSAGE, subject);

		// Verification
		mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:("+ subject +")");
		ZAssert.assertNotNull(mail, "Make sure the mail is found");
		ZAssert.assertEquals(mail.dFolderId, inbox.getId(), "Verify the message is in the inbox folder");
	}	
}