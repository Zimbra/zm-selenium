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
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.attachcontacts;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.AppAjaxClient;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogAttach;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.PageMail.Locators;

public class AttachContactAndSendMail extends PrefGroupMailByMessageTest {

	public AttachContactAndSendMail() {
		logger.info("New " + AttachContactAndSendMail.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}


	@Test (description = "Attach a contact to a mail",
			groups = { "functional", "L2" })

	public void AttachContactAndSendMail_01() throws HarnessException {

		// Create a contact item
		ContactItem contact = new ContactItem();
		contact.firstName = "First" + ConfigProperties.getUniqueString();
		contact.lastName = "Last" + ConfigProperties.getUniqueString();
		contact.email = "email" + ConfigProperties.getUniqueString() + "@domain.com";

		app.zGetActiveAccount()
				.soapSend("<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >" + "<a n='firstName'>"
						+ contact.firstName + "</a>" + "<a n='lastName'>" + contact.lastName + "</a>" + "<a n='email'>"
						+ contact.email + "</a>" + "</cn>" + "</CreateContactRequest>");

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyText = "body" + ConfigProperties.getUniqueString();

		// Click Get Mail button to get the new contact
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		mailform.zFill(mail);

		// Click attach drop down and select Contact
		app.zPageMail.zToolbarPressPulldown(Button.B_ATTACH, Button.O_CONTACTATTACH);

		DialogAttach dialog = new DialogAttach(app, ((AppAjaxClient) app).zPageMail);
		ZAssert.assertTrue(dialog.zIsActive(), "Attach Contact dialog gets open and active");

		// Click on Contact folder
		dialog.zClick(Locators.zAttachContactFolder);

		// Select first contact
		dialog.sClick("css=div[id^='attachContactsZimlet_row'] tr td>span:contains('" + contact.firstName + "')");
		dialog.zClickButton(Button.B_ATTACH);
		SleepUtil.sleepMedium();
		mailform.zSubmit();

		// From the receiving end, verify the message details
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:(" + mail.dSubject + ")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
		ZimbraAccount.AccountA().soapSend("<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + received.getId() + "'/>" + "</GetMsgRequest>");

		String filename = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@cd='attachment']", "filename");
		ZAssert.assertStringContains(filename, contact.firstName, "Verify the attached contacts exist");
	}
}