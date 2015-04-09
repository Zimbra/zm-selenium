package com.zimbra.qa.selenium.projects.touch.tests.mail.mail.message;

import java.awt.AWTException;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByConversationTest;


public class SwitchToMessageView extends PrefGroupMailByConversationTest{

	public SwitchToMessageView() {
		logger.info("New "+ SwitchToMessageView.class.getCanonicalName());
	}
	@Test(description = "Switch to message view",
			groups = {"smoke"})

	public void SwitchToMessageView_01() throws HarnessException, AWTException {

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
		app.zPageMail.zListItem(Action.A_LEFTCLICK, Button.B_SWITCH_TO_MESSAGE_VIEW, subject);
		app.zPageMail.zVerifyMessageDetailsNotExist();

	}


}