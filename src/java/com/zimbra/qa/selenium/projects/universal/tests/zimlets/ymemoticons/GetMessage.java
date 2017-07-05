/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.zimlets.ymemoticons;

import java.util.*;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.*;
import com.zimbra.qa.selenium.projects.universal.ui.mail.*;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DisplayMail.*;


public class GetMessage extends AjaxCommonTest {

	public static final class Emoticons {
		public static final String HAPPY = ":)";
		public static final String SAD = ":(";
		// TODO: add all the emoticons
	}
	
	public GetMessage() {
		logger.info("New "+ GetMessage.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageMail;

		// Basic settings
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 2239204417855001627L;
		{
		    put("zimbraPrefGroupMailBy", "message");
		}};
	}
	
	@Test( description = "Receive a mail with a basic emoticon",
			groups = { "functional", "L2" })
	public void GetMessage_01() throws HarnessException {
		
		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "text " + Emoticons.HAPPY + " text";
		
		// Send the message from AccountA to the ZWC user
		ZimbraAccount.AccountA().soapSend(
			"<SendMsgRequest xmlns='urn:zimbraMail'>" +
				"<m>" +
					"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
					"<su>"+ subject +"</su>" +
					"<mp ct='text/plain'>" +
						"<content>"+ body +"</content>" +
					"</mp>" +
				"</m>" +
			"</SendMsgRequest>");
		
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);
		
		
		//-- VERIFICATION
		
		
		// Get the HTML of the body
		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);
		
		ZAssert.assertStringContains(bodyElement.prettyPrint(), "com_zimbra_ymemoticons", "Verify the ymemoticons zimlet is applied to the body");
		ZAssert.assertStringContains(bodyElement.prettyPrint(), "1.gif", "Verify the 'happy' emoticon is displayed");
		
	}
}
