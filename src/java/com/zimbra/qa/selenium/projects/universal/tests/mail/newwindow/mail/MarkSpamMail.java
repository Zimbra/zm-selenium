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
package com.zimbra.qa.selenium.projects.universal.tests.mail.newwindow.mail;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.universal.pages.mail.SeparateWindowDisplayMail;


public class MarkSpamMail extends SetGroupMailByMessagePreference {

	
	public MarkSpamMail() {
		logger.info("New "+ MarkSpamMail.class.getCanonicalName());
		
		
	}
	@Bugs (ids = "103950")
	@Test (description = "Mark a message as spam, using 'Spam' toolbar button - in separate window",
			groups = { "smoke", "L1" })
	public void MarkSpamMail_01() throws HarnessException {
		
		String subject = "subject"+ ConfigProperties.getUniqueString();
		
		// Get the junk folder
		FolderItem junk = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Junk);


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
		String windowTitle = "Zimbra: " + subject;
		
		try {
			
			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);
			
			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
		
			window.zToolbarPressButton(Button.B_RESPORTSPAM);
			
		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

		
		// Get the mail item for the new message
		// Need 'is:anywhere' to include the spam folder
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:("+ subject +")");
		ZAssert.assertNotNull(mail, "Make sure the mail is found");
		
		ZAssert.assertEquals(mail.dFolderId, junk.getId(), "Verify the message is in the spam folder");
				
	}

	@Bugs (ids = "103950")
	@Test (description = "Mark a message as spam, using keyboard shortcut (keyboard='ms') - in a separate window",
			groups = { "functional", "L2" })
	public void MarkSpamMail_02() throws HarnessException {
		
		String subject = "subject"+ ConfigProperties.getUniqueString();
		
		// Get the junk folder
		FolderItem junk = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Junk);


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
		String windowTitle = "Zimbra: " + subject;
		
		try {
			
			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);
			
			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
		
			window.zKeyboardShortcut(Shortcut.S_MAIL_MARKSPAM);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);			
		}

		
		// Get the mail item for the new message
		// Need 'is:anywhere' to include the spam folder
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:("+ subject +")");
		ZAssert.assertNotNull(mail, "Make sure the mail is found");
		
		ZAssert.assertEquals(mail.dFolderId, junk.getId(), "Verify the message is in the spam folder");
				

	}


}
