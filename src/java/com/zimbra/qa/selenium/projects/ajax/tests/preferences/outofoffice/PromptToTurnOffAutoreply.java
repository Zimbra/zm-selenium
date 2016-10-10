/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.outofoffice;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class PromptToTurnOffAutoreply extends AjaxCommonTest {
	
	public static final String autoReplyMessage = "OOO" + ConfigProperties.getUniqueString();

	public PromptToTurnOffAutoreply() {
		logger.info("New " + PromptToTurnOffAutoreply.class.getCanonicalName());

		// test starts in the Mail tab
		super.startingPage = app.zPageMail;

		// use an account with OOO auto-reply enabled
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("zimbraPrefOutOfOfficeReplyEnabled", "TRUE");
				put("zimbraPrefOutOfOfficeReply", autoReplyMessage);
				put("zimbraPrefOutOfOfficeStatusAlertOnLogin", "FALSE");
			}
		};
	}

	
	@Bugs(ids = "51990")
	@Test(description = "Enable auto-reply message - Verify after login  alert dialog promts to turn off auto-reply", 
		groups = {"functional" })
	
	public void PromptToTurnOffAutoreply_01() throws HarnessException {
		
		ZimbraAccount account = app.zGetActiveAccount();

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();

		// Send message to self
		account.soapSend("<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>" + "<e t='t' a='" + account.EmailAddress
				+ "'/>" + "<su>" + subject + "</su>" + "<mp ct='text/plain'>" + "<content>content"
				+ ConfigProperties.getUniqueString() + "</content>" + "</mp>" + "</m>" + "</SendMsgRequest>");
		
		MailItem mailItem = MailItem.importFromSOAP(account, "in:inbox " + autoReplyMessage);
		ZAssert.assertTrue(mailItem.dBodyText.contains(autoReplyMessage), "Verify auto-reply message is received");
	}
}
