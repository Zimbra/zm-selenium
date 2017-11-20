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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.contacts;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class SendMailToContact extends AjaxCore {

	public SendMailToContact() {
		logger.info("New " + SendMailToContact.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test (description = "Right click then click New Email",
			groups = { "smoke", "L0" })

	public void SendMailToContact_01() throws HarnessException {

		// The message subject
		String subject = "subject" + ConfigProperties.getUniqueString();

		// Create a contact
		String firstName = "First" + ConfigProperties.getUniqueString();
		String lastName = "Last" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount()
				.soapSend("<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >" + "<a n='firstName'>" + firstName
						+ "</a>" + "<a n='lastName'>" + lastName + "</a>" + "<a n='email'>"
						+ ZimbraAccount.AccountA().EmailAddress + "</a>" + "</cn>" + "</CreateContactRequest>");

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Right Click -> New Email
		FormMailNew formMailNew = (FormMailNew) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_NEW, firstName);

		formMailNew.zFillField(Field.Subject, subject);
		formMailNew.zFillField(Field.Body, "body" + ConfigProperties.getUniqueString());
		formMailNew.zSubmit();

		// Verification
		MailItem message1 = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:(" + subject + ")");
		ZAssert.assertNotNull(message1, "Verify the message is received by Account A");
	}
}