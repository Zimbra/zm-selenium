/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mail;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.*;

public class SendMailWithContactsDisabled extends SetGroupMailByMessagePreference {

	public SendMailWithContactsDisabled() {
		logger.info("New " + SendMailWithContactsDisabled.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraFeatureContactsEnabled", "FALSE");
	}


	@Bugs (ids = "48923")
	@Test (description = "Send a mail from an account having Contacts disabled",
			groups = { "sanity" } )

	public void SendMailWithContactsDisabled_01() throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		mailform.zFill(mail);

		// Send the message
		mailform.zSubmit();

		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
				+	"<query>subject:("+ mail.dSubject +")</query>"
				+	"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");

		ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>"
				+	"<m id='"+ id +"' html='1'/>"
				+	"</GetMsgRequest>");

		String from = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='f']", "a");
		String to = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='t']", "a");
		String subject = ZimbraAccount.AccountA().soapSelectValue("//mail:su", null);
		String html = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@ct='text/html']//mail:content", null);

		ZAssert.assertEquals(from, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(to, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(subject, mail.dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(html, mail.dBodyHtml, "Verify the html content");

		// Go to Sent
		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);

		// Verify that mail is present in Sent folder
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(mail.dSubject),"Verify that mail is present in sent folder");
	}
}