/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;

public class EditAsNewMessage extends SetGroupMailByMessagePreference {

	public EditAsNewMessage() {
		logger.info("New "+ EditAsNewMessage.class.getCanonicalName());
	}


	@Test (description = "'Edit as new' message, using 'Actions -> Edit as New' toolbar button",
			groups = { "bhr" })

	public void EditAsNewMessage_01() throws HarnessException {

		String subject = "subject"+ ConfigProperties.getUniqueString();

		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
							"<e t='c' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Get the mail item for the new message
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Click redirect
		FormMailNew form = (FormMailNew)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.O_EDIT_AS_NEW);
		form.zSubmit();

		// Verify the redirected message is received
		MailItem original = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+subject+") from:("+ZimbraAccount.AccountA().EmailAddress+")");
		ZAssert.assertNotNull(original, "Verify the original message from Account A is received by Account B");

		MailItem resent = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+subject+") from:("+app.zGetActiveAccount().EmailAddress+")");
		ZAssert.assertNotNull(resent, "Verify the 'edit as new' message from the test account is received by Account B");
	}


	@Test (description = "'Edit as new' message, using 'Right Click' -> 'Edit as new'",
			groups = { "sanity" })

	public void EditAsNewMessage_02() throws HarnessException {

		String subject = "subject"+ ConfigProperties.getUniqueString();

		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
							"<e t='c' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Get the mail item for the new message
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Click redirect
		FormMailNew form = (FormMailNew)app.zPageMail.zListItem(Action.A_RIGHTCLICK, Button.O_EDIT_AS_NEW, mail.dSubject);
		form.zSubmit();

		// Verify the redirected message is received
		MailItem original = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+subject+") from:("+ZimbraAccount.AccountA().EmailAddress+")");
		ZAssert.assertNotNull(original, "Verify the original message from Account A is received by Account B");

		MailItem resent = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+subject+") from:("+app.zGetActiveAccount().EmailAddress+")");
		ZAssert.assertNotNull(resent, "Verify the 'edit as new' message from the test account is received by Account B");
	}
}