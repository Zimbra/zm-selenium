package com.zimbra.qa.selenium.projects.touch.tests.search.mail.message;

import java.awt.AWTException;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByMessageTest;

public class DeleteMail extends PrefGroupMailByMessageTest {

	public DeleteMail() {
		logger.info("New "+ DeleteMail.class.getCanonicalName());
	}
	@Test(description = "Search message by subject and delete it in message view",
			groups = { "functional" })
	
	public void DeleteMail_01() throws HarnessException, AWTException {
	
	// Create the message data to be sent
	String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
	
	// Send the message from AccountA to the ZWC user
	ZimbraAccount.AccountA().soapSend(
		"<SendMsgRequest xmlns='urn:zimbraMail'>" +
			"<m>" +
				"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
				"<su>"+ subject +"</su>" +
				"<mp ct='text/plain'>" +
					"<content>"+ "body" + ZimbraSeleniumProperties.getUniqueString() +"</content>" +
				"</mp>" +
			"</m>" +
		"</SendMsgRequest>");
	app.zPageMail.zRefresh();	

	// Switch to message view
	app.zPageMail.zListItem(Action.A_LEFTCLICK, Button.B_SWITCH_TO_CONVERSATION_VIEW, subject);
	

	// Search email		
	app.zTreeMail.zFillField(Button.B_SEARCH, subject);

	// Select mail
	app.zPageMail.zListItem(Action.A_LEFTCLICK, Button.B_DELETE, subject);

	
	// UI Verification
	ZAssert.assertFalse(app.zPageMail.zVerifyMessageExists(subject), "Verify message is removed from list view");
	
	// SOAP Verification
	MailItem actual= MailItem.importFromSOAP(app.zGetActiveAccount(), "in:trash "+ subject);
	ZAssert.assertNotNull(actual, "Verify the mail is in the trash");
	
	}

	
}