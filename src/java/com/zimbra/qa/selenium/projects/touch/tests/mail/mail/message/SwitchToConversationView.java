/*
 * ***** BEGIN LICENSE BLOCK *****
 *
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
 *
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.touch.tests.mail.mail.message;

import java.awt.AWTException;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByMessageTest;

public class SwitchToConversationView extends PrefGroupMailByMessageTest{

	public SwitchToConversationView() {
		logger.info("New "+ SwitchToConversationView.class.getCanonicalName());
	}
	@Test( description = "Switch to conversation view",
			groups = {"smoke"})

	public void SwitchToConversationView_01() throws HarnessException, AWTException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();

		// Send the message from AccountA to the ZCS user
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>"+ "body" + ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");
		app.zPageMail.zRefresh();	
 
		// Switch to message view
		app.zPageMail.zListItem(Action.A_LEFTCLICK, Button.B_SWITCH_TO_CONVERSATION_VIEW, subject);
		app.zPageMail.zVerifyMessageDetailsExist();

	}


}