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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.attributes;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;

public class ZimbraPrefAutoAddAddressEnabledTrue extends AjaxCore {

	@SuppressWarnings("serial")
	public ZimbraPrefAutoAddAddressEnabledTrue() {
		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {
			{
		 		put("zimbraPrefAutoAddAddressEnabled", "TRUE");
			}
		};
	}


	/**
	 * Test case : Opt-in Add New Contacts To emailed contact
	 * Verify receivers' addresses of out-going mails automatically added to "Emailed Contacts" folder
	 */

	@Test (description = "send message to 1 receiver, the address should be added into Emailed Contact",
			groups = { "smoke", "L1" })

	public void SendEmailTo1Receiver_01() throws HarnessException {

		FolderItem emailedContacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.EmailedContacts);

		ZimbraAccount receiver = new ZimbraAccount();
		receiver.provision();
		receiver.authenticate();

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(receiver));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFill(mail);

		// Send the message
		mailform.zSubmit();

		app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='contact'>"
				+		"<query>"+ receiver.EmailAddress +"</query>"
				+	"</SearchRequest>");

		String email = app.zGetActiveAccount().soapSelectValue("//mail:cn//mail:a[@n='email']", null);
		ZAssert.assertEquals(email, receiver.EmailAddress, "Verify the recipient is in the contacts folder");

		String folder = app.zGetActiveAccount().soapSelectValue("//mail:cn", "l");
		ZAssert.assertEquals(folder, emailedContacts.getId(), "Verify the recipient is in the Emailed Contacts folder");
	}


	/**
	 * Test case : Opt-in Add New Contacts To emailed contact
	 * Verify receivers' addresses of out-going mails automatically added to "Emailed Contacts" folder
	 */

	@Test (description = "Send message to 2 receiver, the addresses should be added into Emailed Contact",
			groups = { "functional", "L3" })

	public void SendEmailTo2Receivers_02() throws HarnessException {

		FolderItem emailedContacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.EmailedContacts);

		ZimbraAccount receiver1 = new ZimbraAccount();
		receiver1.provision();
		receiver1.authenticate();

		ZimbraAccount receiver2 = new ZimbraAccount();
		receiver2.provision();
		receiver2.authenticate();

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(receiver1));
		mail.dToRecipients.add(new RecipientItem(receiver2));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFill(mail);

		// Send the message
		mailform.zSubmit();

		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='contact'>"
			+		"<query>"+ receiver1.EmailAddress +"</query>"
			+	"</SearchRequest>");

		String email1 = app.zGetActiveAccount().soapSelectValue("//mail:cn//mail:a[@n='email']", null);
		ZAssert.assertEquals(email1, receiver1.EmailAddress, "Verify the recipient is in the contacts folder");

		String folder1 = app.zGetActiveAccount().soapSelectValue("//mail:cn", "l");
		ZAssert.assertEquals(folder1, emailedContacts.getId(), "Verify the recipient is in the Emailed Contacts folder");

		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='contact'>"
			+		"<query>"+ receiver2.EmailAddress +"</query>"
			+	"</SearchRequest>");

		String email2 = app.zGetActiveAccount().soapSelectValue("//mail:cn//mail:a[@n='email']", null);
		ZAssert.assertEquals(email2, receiver2.EmailAddress, "Verify the recipient is in the contacts folder");

		String folder2 = app.zGetActiveAccount().soapSelectValue("//mail:cn", "l");
		ZAssert.assertEquals(folder2, emailedContacts.getId(), "Verify the recipient is in the Emailed Contacts folder");
	}
}