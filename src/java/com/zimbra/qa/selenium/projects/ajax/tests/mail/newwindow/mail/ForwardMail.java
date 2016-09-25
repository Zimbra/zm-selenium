package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.mail;

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


import java.awt.event.KeyEvent;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowDisplayMail;


public class ForwardMail extends PrefGroupMailByMessageTest {


	public ForwardMail() {
		logger.info("New "+ ForwardMail.class.getCanonicalName());


	}

	@Test( description = "Fwd to a mail by pressing Fwd button - in separate window",
			groups = { "smoke" })
	public void FwdMailFromNewWindow_01() throws HarnessException {

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
		app.zPageMail.zVerifyMailExists(subject);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + subject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail) app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS,
					Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();

			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

			window.zToolbarPressButton(Button.B_FORWARD);
			
			windowTitle = "Zimbra: Forward";
			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

			window.sSelectWindow(windowTitle);
			String locator = FormMailNew.Locators.zToField;
			window.sClick(locator);
			window.sType(locator, ZimbraAccount.AccountB().EmailAddress);
			window.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
			SleepUtil.sleepSmall();
			window.zKeyboard.zTypeKeyEvent(KeyEvent.VK_TAB);
			SleepUtil.sleepSmall();			
			window.zToolbarPressButton(Button.B_SEND);
			
			windowTitle = "Zimbra: " + subject;
			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			window.zToolbarPressButton(Button.B_CLOSE);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

		// From the receiving end, verify the message details
		// Need 'in:inbox' to seprate the message from the sent message
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "in:inbox subject:("+subject +")");

		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountB().EmailAddress, "Verify the to field is correct");
		ZAssert.assertStringContains(received.dSubject, subject, "Verify the subject field is correct");
		ZAssert.assertStringContains(received.dSubject, "Fwd", "Verify the subject field contains the 'fwd' prefix");

	}


	@Test( description = "Forward a  message , using keyboard shortcut (keyboard='f') - in a separate window",
			groups = { "smoke" })
	public void FwdMailFromNewWindow_02() throws HarnessException {

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
		app.zPageMail.zVerifyMailExists(subject);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + subject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail) app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS,
					Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();

			ZAssert.assertTrue(window.zIsActive(),
					"Verify the window is active");

			window.zKeyboardShortcut(Shortcut.S_MAIL_FOWARD);
			
			windowTitle = "Zimbra: Forward";
			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

			window.sSelectWindow(windowTitle);
			String locator = FormMailNew.Locators.zToField;
			window.sClick(locator);
			window.sType(locator, ZimbraAccount.AccountB().EmailAddress);
			window.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
			SleepUtil.sleepSmall();
			window.zKeyboard.zTypeKeyEvent(KeyEvent.VK_TAB);
			SleepUtil.sleepSmall();			
			window.zToolbarPressButton(Button.B_SEND);		
			
			windowTitle = "Zimbra: " + subject;
			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			window.zToolbarPressButton(Button.B_CLOSE);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

		// From the receiving end, verify the message details
		// Need 'in:inbox' to seprate the message from the sent message
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "in:inbox subject:("+subject +")");

		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountB().EmailAddress, "Verify the to field is correct");
		ZAssert.assertStringContains(received.dSubject, subject, "Verify the subject field is correct");
		ZAssert.assertStringContains(received.dSubject, "Fwd", "Verify the subject field contains the 'Fwd' prefix");

	}


}

