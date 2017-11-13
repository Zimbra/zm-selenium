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
package com.zimbra.qa.selenium.projects.universal.tests.mail.mail;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DialogRedirect;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DialogRedirect.Field;


public class RedirectMessage extends PrefGroupMailByMessageTest {

	
	public RedirectMessage() {
		logger.info("New "+ RedirectMessage.class.getCanonicalName());
	}
	
	@Bugs (ids = "14110")
	@Test (description = "Redirect message, using 'Redirect' toolbar button",
			groups = { "smoke", "L1" })
	public void RedirectMessage_01() throws HarnessException {
		
		String subject = "subject"+ ConfigProperties.getUniqueString();
		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
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
		DialogRedirect dialog = (DialogRedirect)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_REDIRECT);
		dialog.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);
		dialog.zPressButton(Button.B_OK);
		
		// Verify the redirected message is received
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the redirected message is received");
		ZAssert.assertEquals(received.dRedirectedFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress, "Verify the message shows as redirected from the test account");
	}

	//TODO: Remove x from groups to enable when feature is implemented  
	@Bugs (ids = "62170")
	@Test (description = "Redirect message, using 'Redirect' shortcut key",
			groups = { "functional", "L5" })
	public void RedirectMessage_02() throws HarnessException {
		throw new HarnessException("See bug https://bugzilla.zimbra.com/show_bug.cgi?id=62170");
	}
	
	@Test (description = "Redirect message, using 'Right Click' -> 'Redirect'",
			groups = { "smoke", "L1" })
	public void RedirectMessage_03() throws HarnessException {
		
		String subject = "subject"+ ConfigProperties.getUniqueString();
	
		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
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
		DialogRedirect dialog = (DialogRedirect)app.zPageMail.zListItem(Action.A_RIGHTCLICK, Button.B_REDIRECT, mail.dSubject);
		dialog.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);
		dialog.zPressButton(Button.B_OK);
		
		// Verify the redirected message is received
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the redirected message is received");
		ZAssert.assertEquals(received.dRedirectedFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress, "Verify the message shows as redirected from the test account");
	}
}
