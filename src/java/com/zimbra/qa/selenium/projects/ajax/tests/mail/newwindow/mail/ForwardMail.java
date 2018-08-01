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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.mail;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.SeparateWindowDisplayMail;

public class ForwardMail extends SetGroupMailByMessagePreference {

	public ForwardMail() {
		logger.info("New "+ ForwardMail.class.getCanonicalName());
	}


	@Test (description = "Forward mail from new window",
			groups = { "smoke", "L1" })

	public void ForwardMailFromNewWindow_01() throws HarnessException {

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

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Email: " + subject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail) app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS,
					Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			window.zToolbarPressButton(Button.B_FORWARD);
			window.sType(FormMailNew.Locators.zToField, ZimbraAccount.AccountB().EmailAddress + ",");
			window.zToolbarPressButton(Button.B_SEND);
			window.zToolbarPressButton(Button.B_CLOSE);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "in:inbox subject:("+subject +")");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountB().EmailAddress, "Verify the to field is correct");
		ZAssert.assertStringContains(received.dSubject, subject, "Verify the subject field is correct");
		ZAssert.assertStringContains(received.dSubject, "Fwd", "Verify the subject field contains the 'fwd' prefix");
	}


	@Test (description = "Forward a  message , using keyboard shortcut (keyboard='f') - in a separate window",
			groups = { "smoke", "L1" })

	public void ForwardMailFromNewWindow_02() throws HarnessException {

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

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Email: " + subject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail) app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS,
					Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			window.zKeyboardShortcut(Shortcut.S_MAIL_FOWARD);
			window.sType(FormMailNew.Locators.zToField, ZimbraAccount.AccountB().EmailAddress + ",");
			window.zToolbarPressButton(Button.B_SEND);
			window.zToolbarPressButton(Button.B_CLOSE);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "in:inbox subject:("+subject +")");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountB().EmailAddress, "Verify the to field is correct");
		ZAssert.assertStringContains(received.dSubject, subject, "Verify the subject field is correct");
		ZAssert.assertStringContains(received.dSubject, "Fwd", "Verify the subject field contains the 'Fwd' prefix");
	}
}