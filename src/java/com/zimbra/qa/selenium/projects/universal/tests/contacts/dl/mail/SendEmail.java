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
package com.zimbra.qa.selenium.projects.universal.tests.contacts.dl.mail;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCore;
import com.zimbra.qa.selenium.projects.universal.pages.mail.FormMailNew;

public class SendEmail extends UniversalCore  {

	public SendEmail() {
		logger.info("New "+ SendEmail.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
		
	}

	@Test (description = "Right click to DL and send email", 
			groups = { "smoke", "L0" })

	public void SendEmail_01 () throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "body" + ConfigProperties.getUniqueString();

		String firstContactEmail = ZimbraAccount.AccountA().EmailAddress;
		String secondContactEmail = ZimbraAccount.AccountB().EmailAddress;

		String dlFolder = "Distribution Lists";
		String dlName = "dl" + ConfigProperties.getUniqueString();
		String fullDLName = dlName + "@" + ConfigProperties.getStringProperty("testdomain");

		// Create DL
		app.zGetActiveAccount().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAccount'>"
			+		"<name>" + fullDLName + "</name>"
			+	"</CreateDistributionListRequest>");

		// Add DL members
		app.zGetActiveAccount().soapSend(
				"<DistributionListActionRequest xmlns='urn:zimbraAccount'>"
			+		"<dl by='name'>" + fullDLName + "</dl>"
			+		"<action op='addMembers'>"
         	+			"<dlm>" + firstContactEmail + "</dlm>"
         	+			"<dlm>" + secondContactEmail + "</dlm>"
         	+		"</action>"
			+	"</DistributionListActionRequest>");

		// Send email
		app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, dlFolder);

		FormMailNew FormMailNew = (FormMailNew) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.O_NEW_EMAIL, fullDLName);
		FormMailNew.zIsVisiblePerPosition("css=css=div[class='ImgBubbleExpand'][onclick^='ZmAddressBubble.expandBubble']", 0, 0);
		FormMailNew.zIsVisiblePerPosition("css=span[class='addrBubble']:contains('" + fullDLName + "')", 0, 0);
		FormMailNew.zFill(mail);
		FormMailNew.zSubmit();

		// Member 1 verifcation
		ZimbraAccount.AccountA().soapSend(
			"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
			+ "<query>subject:("+ mail.dSubject +")</query>"
			+ "</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");

		ZimbraAccount.AccountA().soapSend(
			"<GetMsgRequest xmlns='urn:zimbraMail'>"
			+ "<m id='"+ id +"' html='1'/>"
			+ "</GetMsgRequest>");

		String from = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='f']", "a");
		String to = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='t']", "a");
		String subject = ZimbraAccount.AccountA().soapSelectValue("//mail:su", null);
		String html = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@ct='text/html']//mail:content", null);

		ZAssert.assertEquals(from, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(to, fullDLName, "Verify the to field is correct");
		ZAssert.assertEquals(subject, mail.dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(html, mail.dBodyHtml, "Verify the html content");

		// Member 2 verifcation
		ZimbraAccount.AccountB().soapSend(
			"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
			+ "<query>subject:("+ mail.dSubject +")</query>"
			+ "</SearchRequest>");
		id = ZimbraAccount.AccountB().soapSelectValue("//mail:m", "id");

		ZimbraAccount.AccountB().soapSend(
			"<GetMsgRequest xmlns='urn:zimbraMail'>"
			+ "<m id='"+ id +"' html='1'/>"
			+ "</GetMsgRequest>");

		from = ZimbraAccount.AccountB().soapSelectValue("//mail:e[@t='f']", "a");
		to = ZimbraAccount.AccountB().soapSelectValue("//mail:e[@t='t']", "a");
		subject = ZimbraAccount.AccountB().soapSelectValue("//mail:su", null);
		html = ZimbraAccount.AccountB().soapSelectValue("//mail:mp[@ct='text/html']//mail:content", null);

		ZAssert.assertEquals(from, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(to, fullDLName, "Verify the to field is correct");
		ZAssert.assertEquals(subject, mail.dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(html, mail.dBodyHtml, "Verify the html content");
	}
}
