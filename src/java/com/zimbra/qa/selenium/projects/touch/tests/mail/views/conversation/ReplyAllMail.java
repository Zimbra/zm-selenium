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

public class ReplyAllMail extends SetGroupMailByConversationPreference {

	public ReplyAllMail() {
		logger.info("New "+ ReplyAllMail.class.getCanonicalName());		
	}
	
	@Bugs (ids = "85534")
	@Test (description = "Reply all to message and verify it",
			groups = { "sanity" })
			
	public void ReplyAllMail_01() throws HarnessException {
		
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "text <strong>bold"+ ConfigProperties.getUniqueString() +"</strong> text";
		String modifiedContent = "modified body" + ConfigProperties.getUniqueString();
		String htmlBody = XmlStringUtil.escapeXml(
				"<html>" +
					"<head></head>" +
					"<body>"+ body +"</body>" +
				"</html>");

		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<e t='c' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
						"<e t='c' a='"+ ZimbraAccount.AccountC().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='multipart/alternative'>" +
						"<mp ct='text/plain'>" +
							"<content>"+ body +"</content>" +
						"</mp>" +
						"<mp ct='text/html'>" +
							"<content>"+ htmlBody +"</content>" +
						"</mp>" +
					"</mp>" +
					"</m>" +
				"</SendMsgRequest>");
		
		app.zPageMail.zToolbarPressButton(Button.B_FOLDER_TREE);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, "Inbox");
		
		// Select the mail
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Reply to mail
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressPulldown(Button.B_REPLY, Button.O_REPLY_TO_ALL);
		mailform.zFillField(Field.Body, modifiedContent);
		mailform.zSubmit();

		// To user verification
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>subject:(" + subject + ")</query>"
						+ "</SearchRequest>");
		String toid = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		
		ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + toid
						+ "' html='1'/>" + "</GetMsgRequest>");

		String tofrom = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='f']", "a");
		String toto = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='t']", "a");
		String tocc1 = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='c']", "a");
		String tocc2 = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='c'][2]", "a");
		String tosubject = ZimbraAccount.AccountA().soapSelectValue("//mail:su", null);
		String tobody = ZimbraAccount.AccountA().soapSelectValue("//mail:content", null);
		
		ZAssert.assertEquals(tofrom, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(toto, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(tocc1, ZimbraAccount.AccountB().EmailAddress, "Verify the cc user1 field is correct");
		ZAssert.assertEquals(tocc2, ZimbraAccount.AccountC().EmailAddress, "Verify the cc user2 field is correct");
		ZAssert.assertEquals(tosubject, "Re: " + subject, "Verify the subject field is correct");
		ZAssert.assertStringContains(tobody, body, "Verify the body content");
		ZAssert.assertStringContains(tobody.trim(), modifiedContent.trim(), "Verify the modified content");
		
		// Cc user1 verification
		ZimbraAccount.AccountB().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>subject:(" + subject + ")</query>"
						+ "</SearchRequest>");
		toid = ZimbraAccount.AccountB().soapSelectValue("//mail:m", "id");
		
		ZimbraAccount.AccountB().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + toid
						+ "' html='1'/>" + "</GetMsgRequest>");

		tofrom = ZimbraAccount.AccountB().soapSelectValue("//mail:e[@t='f']", "a");
		toto = ZimbraAccount.AccountB().soapSelectValue("//mail:e[@t='t']", "a");
		tocc1 = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='c']", "a");
		tocc2 = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='c'][2]", "a");
		tosubject = ZimbraAccount.AccountB().soapSelectValue("//mail:su", null);
		tobody = ZimbraAccount.AccountB().soapSelectValue("//mail:content", null);
		
		ZAssert.assertEquals(tofrom, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(toto, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(tocc1, ZimbraAccount.AccountB().EmailAddress, "Verify the cc user1 field is correct");
		ZAssert.assertEquals(tocc2, ZimbraAccount.AccountC().EmailAddress, "Verify the cc user2 field is correct");
		ZAssert.assertEquals(tosubject, "Re: " + subject, "Verify the subject field is correct");
		ZAssert.assertStringContains(tobody, body, "Verify the body content");
		ZAssert.assertStringContains(tobody.trim(), modifiedContent.trim(), "Verify the modified content");
		
		// Cc user2 verification
		ZimbraAccount.AccountC().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>subject:(" + subject + ")</query>"
						+ "</SearchRequest>");
		toid = ZimbraAccount.AccountC().soapSelectValue("//mail:m", "id");
		
		ZimbraAccount.AccountC().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + toid
						+ "' html='1'/>" + "</GetMsgRequest>");

		tofrom = ZimbraAccount.AccountC().soapSelectValue("//mail:e[@t='f']", "a");
		toto = ZimbraAccount.AccountC().soapSelectValue("//mail:e[@t='t']", "a");
		tocc1 = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='c']", "a");
		tocc2 = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='c'][2]", "a");
		tosubject = ZimbraAccount.AccountC().soapSelectValue("//mail:su", null);
		tobody = ZimbraAccount.AccountC().soapSelectValue("//mail:content", null);
		
		ZAssert.assertEquals(tofrom, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(toto, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(tocc1, ZimbraAccount.AccountB().EmailAddress, "Verify the cc user1 field is correct");
		ZAssert.assertEquals(tocc2, ZimbraAccount.AccountC().EmailAddress, "Verify the cc user2 field is correct");
		ZAssert.assertEquals(tosubject, "Re: " + subject, "Verify the subject field is correct");
		ZAssert.assertStringContains(tobody, body, "Verify the body content");
		ZAssert.assertStringContains(tobody.trim(), modifiedContent.trim(), "Verify the modified content");

	}
	
}
