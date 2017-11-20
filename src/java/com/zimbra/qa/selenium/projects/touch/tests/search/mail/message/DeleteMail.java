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
package com.zimbra.qa.selenium.projects.touch.tests.search.mail.message;

import java.awt.AWTException;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.SetGroupMailByMessagePreference;

public class DeleteMail extends SetGroupMailByMessagePreference {

	public DeleteMail() {
		logger.info("New "+ DeleteMail.class.getCanonicalName());
	}
	@Test (description = "Search message by subject and delete it in message view",
			groups = { "functional" })
	
	public void DeleteMail_01() throws HarnessException, AWTException {
	
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
	

	// Search email		
	app.zTreeMail.zFillField(Button.B_SEARCH, subject);

	// Select mail
	app.zPageMail.zListItem(Action.A_LEFTCLICK, Button.B_DELETE, subject);

	
	// UI Verification
	ZAssert.assertFalse(app.zPageMail.zVerifyMessageExists(subject), "Verify message is removed from list view");
	
	// SOAP Verification
	MailItem actual= MailItem.importFromSOAP(app.zGetActiveAccount(), "in:trash "+ subject);
	ZAssert.assertNotNull(actual, "Verify the mail is in the trash");
	
	app.zPageMail.zPressButton(Button.B_BACK);
	
	}

	
}