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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.mail;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.SeparateWindowDisplayMail;

public class MarkUnSpamMail extends SetGroupMailByMessagePreference {

	public MarkUnSpamMail() {
		logger.info("New "+ MarkUnSpamMail.class.getCanonicalName());
	}


	@Bugs (ids = "103950")
	@Test (description = "Mark a message as not spam, using 'Not Spam' toolbar button - in a separate window",
			groups = { "smoke", "L1" })

	public void MarkUnSpamMail_01() throws HarnessException {

		String subject = "subject"+ ConfigProperties.getUniqueString();

		// Get the junk and inbox folder
		FolderItem junk = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Junk);
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);

		// Add a message to the account's junk folder
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>" +
            		"<m l='"+ junk.getId() +"'>" +
                		"<content>From: foo@foo.com\n" +
							"To: foo@foo.com \n" +
							"Subject: "+ subject +"\n" +
							"MIME-Version: 1.0 \n" +
							"Content-Type: text/plain; charset=utf-8 \n" +
							"Content-Transfer-Encoding: 7bit\n" +
							"\n" +
							"simple text string in the body\n" +
							"</content>" +
                	"</m>" +
            	"</AddMsgRequest>");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Go to the Junk folder
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, junk);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Email: " + subject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			window.zToolbarPressButton(Button.B_RESPORTNOTSPAM);

			SleepUtil.sleepLong();

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:("+ subject +")");
		ZAssert.assertNotNull(mail, "Make sure the mail is found");
		ZAssert.assertEquals(mail.dFolderId, inbox.getId(), "Verify the message is in the inbox folder");
	}
}