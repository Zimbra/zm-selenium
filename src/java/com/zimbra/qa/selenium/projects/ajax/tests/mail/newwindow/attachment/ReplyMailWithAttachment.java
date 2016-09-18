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

package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.attachment;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowDisplayMail;

public class ReplyMailWithAttachment extends PrefGroupMailByMessageTest {

	public ReplyMailWithAttachment() {
		logger.info("New "+ ReplyMailWithAttachment.class.getCanonicalName());
	}

	@Test( description = "Reply to a mail  with an attachment by pressing Reply button>>attach - in separate window",
			groups = { "smoke" })
	
	public void ReplyMailWithAttachment_01() throws HarnessException {

		if (OperatingSystem.isWindows() == true) {

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

			FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);

			// Create file item
			final String fileName = "testtextfile.txt";
			final String filePath = ConfigProperties.getBaseDirectory()	+ "\\data\\public\\other\\" + fileName;

			SeparateWindowDisplayMail window = null;
			String windowTitle = "Zimbra: " + subject;

			try {

				// Choose Actions -> Launch in Window
				window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

				window.zSetWindowTitle(windowTitle);
				window.zWaitForActive();

				ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

				window.zToolbarPressButton(Button.B_REPLY);
				SleepUtil.sleepMedium();

				window.zSetWindowTitle(windowTitle);
				SleepUtil.sleepMedium();
				ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

				//Add an attachment
				window.zPressButton(Button.B_ATTACH);
				zUpload(filePath, window);

				//click Send
				window.zToolbarPressButton(Button.B_SEND);
				
			} finally {

				// Make sure to close the window
				if ( window != null ) {
					window.zCloseWindow(windowTitle);
					window = null;
				}
				app.zPageMail.zSelectWindow(null);
			}

			// Verify UI for attachment
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
			ZAssert.assertTrue(app.zPageMail.zVerifyAttachmentExistsInMail(fileName),"Verify attachment exists in the email");

			MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "in:inbox subject:("+subject +")");
			ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
			ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
			ZAssert.assertStringContains(received.dSubject, "Re: " + subject, "Verify Reply subject field is correct");

		} else {
			throw new SkipException("File upload operation is allowed only for Windows OS, skipping this test...");
		}

	}

}