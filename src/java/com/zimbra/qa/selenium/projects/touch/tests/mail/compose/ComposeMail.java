/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.touch.tests.mail.compose;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.RecipientItem.RecipientType;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.TouchCommonTest;
import com.zimbra.qa.selenium.projects.touch.ui.mail.FormMailNew;

public class ComposeMail extends TouchCommonTest {

	public ComposeMail() {
		logger.info("New "+ ComposeMail.class.getCanonicalName());
	}
	
	@Test( description = "Send a mail with To, Cc, Bcc user and verify it for all recipients",
			groups = { "sanity" })
			
	public void ComposeMail_01() throws HarnessException {
		
		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA(), RecipientType.To));
		mail.dCcRecipients.add(new RecipientItem(ZimbraAccount.AccountB(), RecipientType.Cc));
		mail.dBccRecipients.add(new RecipientItem(ZimbraAccount.AccountC(), RecipientType.Bcc));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyText = "body" + ConfigProperties.getUniqueString();
		
		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		mailform.zFill(mail);
		mailform.zSubmit();

		// Verify received mail to To: user
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>subject:(" + mail.dSubject + ")</query>"
						+ "</SearchRequest>");
		String toid = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		
		ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + toid
						+ "' html='1'/>" + "</GetMsgRequest>");

		String tofrom = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='f']", "a");
		String toto = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='t']", "a");
		String tocc = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='c']", "a");
		String tobcc = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='b']", "a");
		String tosubject = ZimbraAccount.AccountA().soapSelectValue("//mail:su", null);
		String tobody = ZimbraAccount.AccountA().soapSelectValue("//mail:content", null);
		
		ZAssert.assertEquals(tofrom, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(toto, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(tocc, ZimbraAccount.AccountB().EmailAddress, "Verify the cc field is correct");
		ZAssert.assertNull(tobcc, "Verify the bcc field is null");
		ZAssert.assertEquals(tosubject, mail.dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(tobody, mail.dBodyText, "Verify the body content");
		
		// Verify received mail to Cc: user
		ZimbraAccount.AccountB().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>subject:(" + mail.dSubject + ")</query>"
						+ "</SearchRequest>");
		String ccid = ZimbraAccount.AccountB().soapSelectValue("//mail:m", "id");
		
		ZimbraAccount.AccountB().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + ccid
						+ "' html='1'/>" + "</GetMsgRequest>");

		String ccfrom = ZimbraAccount.AccountB().soapSelectValue("//mail:e[@t='f']", "a");
		String ccto = ZimbraAccount.AccountB().soapSelectValue("//mail:e[@t='t']", "a");
		String cccc = ZimbraAccount.AccountB().soapSelectValue("//mail:e[@t='c']", "a");
		String ccbcc = ZimbraAccount.AccountB().soapSelectValue("//mail:e[@t='b']", "a");
		String ccsubject = ZimbraAccount.AccountB().soapSelectValue("//mail:su", null);
		String ccbody = ZimbraAccount.AccountB().soapSelectValue("//mail:content", null);
		
		ZAssert.assertEquals(ccfrom, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(ccto, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(cccc, ZimbraAccount.AccountB().EmailAddress, "Verify the cc field is correct");
		ZAssert.assertNull(ccbcc, "Verify the bcc field is null");
		ZAssert.assertEquals(ccsubject, mail.dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(ccbody, mail.dBodyText, "Verify the body content");
		
		// Verify received mail to Bcc: user
		ZimbraAccount.AccountC().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>subject:(" + mail.dSubject + ")</query>"
						+ "</SearchRequest>");
		String bccid = ZimbraAccount.AccountC().soapSelectValue("//mail:m", "id");
		
		ZimbraAccount.AccountC().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + bccid
						+ "' html='1'/>" + "</GetMsgRequest>");

		String bccfrom = ZimbraAccount.AccountC().soapSelectValue("//mail:e[@t='f']", "a");
		String bccto = ZimbraAccount.AccountC().soapSelectValue("//mail:e[@t='t']", "a");
		String bcccc = ZimbraAccount.AccountC().soapSelectValue("//mail:e[@t='c']", "a");
		String bccbcc = ZimbraAccount.AccountC().soapSelectValue("//mail:e[@t='b']", "a");
		String bccsubject = ZimbraAccount.AccountC().soapSelectValue("//mail:su", null);
		String bccbody = ZimbraAccount.AccountC().soapSelectValue("//mail:content", null);
		
		ZAssert.assertEquals(bccfrom, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(bccto, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(bcccc, ZimbraAccount.AccountB().EmailAddress, "Verify the cc field is correct");
		ZAssert.assertNull(bccbcc, "Verify the bcc field is null");
		ZAssert.assertEquals(bccsubject, mail.dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(bccbody, mail.dBodyText, "Verify the body content");

	}

}
