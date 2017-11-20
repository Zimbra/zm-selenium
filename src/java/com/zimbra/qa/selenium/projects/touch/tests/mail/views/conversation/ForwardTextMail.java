/*
 * ***** BEGIN LICENSE BLOCK *****
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
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.touch.tests.mail.views.conversation;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.SetGroupMailByConversationPreference;
import com.zimbra.qa.selenium.projects.touch.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.touch.pages.mail.FormMailNew.Field;

public class ForwardTextMail extends SetGroupMailByConversationPreference {

	public ForwardTextMail() {
		logger.info("New "+ ForwardTextMail.class.getCanonicalName());
	}
	
	@Bugs (ids = "85534")
	@Test (description = "Forward a text mail and verify subject, from and to fields",
			groups = { "sanity" })
			
	public void ForwardTextMail_01() throws HarnessException {
		
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();
		String modifiedContent = "modified body" + ConfigProperties.getUniqueString();
		
		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>" + body + "</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");
		
		// Select the mail from inbox
		app.zPageMail.zToolbarPressButton(Button.B_FOLDER_TREE);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, "Inbox");
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Forward the mail
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressPulldown(Button.B_REPLY, Button.O_FORWARD);
		mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);
		mailform.zFillField(Field.Body, modifiedContent);
		mailform.zSubmit();

		// Verify received mail
		ZimbraAccount.AccountB().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>subject:(" + subject + ")</query>"
						+ "</SearchRequest>");
		String toid = ZimbraAccount.AccountB().soapSelectValue("//mail:m", "id");
		
		ZimbraAccount.AccountB().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + toid
						+ "' html='1'/>" + "</GetMsgRequest>");

		String tofrom = ZimbraAccount.AccountB().soapSelectValue("//mail:e[@t='f']", "a");
		String toto = ZimbraAccount.AccountB().soapSelectValue("//mail:e[@t='t']", "a");
		String tosubject = ZimbraAccount.AccountB().soapSelectValue("//mail:su", null);
		
		ZAssert.assertEquals(tofrom, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(toto, ZimbraAccount.AccountB().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(tosubject, "Fwd: " + subject, "Verify the subject field is correct");
		
	}
	
	@Test (description = "Forward a text mail and verify body content",
			groups = { "sanity" })
			
	public void ForwardTextMail_02() throws HarnessException {
		
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();
		String modifiedContent = "modified body" + ConfigProperties.getUniqueString();
		
		// Send a message to the account
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
		
		// Select the mail from inbox
		app.zPageMail.zToolbarPressButton(Button.B_FOLDER_TREE);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, "Inbox");
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Forward the mail
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressPulldown(Button.B_REPLY, Button.O_FORWARD);
		mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);
		mailform.zFillField(Field.Body, modifiedContent);
		mailform.zSubmit();

		// Verify received mail
		ZimbraAccount.AccountB().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>subject:(" + subject + ")</query>"
						+ "</SearchRequest>");
		String toid = ZimbraAccount.AccountB().soapSelectValue("//mail:m", "id");
		
		ZimbraAccount.AccountB().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + toid
						+ "' html='1'/>" + "</GetMsgRequest>");

		String tobody = ZimbraAccount.AccountB().soapSelectValue("//mail:content", null);
		ZAssert.assertStringContains(tobody, body, "Verify the body content");
		ZAssert.assertStringContains(tobody, modifiedContent, "Verify the modified content");
		
	}
}
