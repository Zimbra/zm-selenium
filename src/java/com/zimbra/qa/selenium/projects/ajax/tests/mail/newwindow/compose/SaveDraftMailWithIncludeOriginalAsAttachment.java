/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.compose;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowDisplayMail;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;

public class SaveDraftMailWithIncludeOriginalAsAttachment extends PrefGroupMailByMessageTest {

	public SaveDraftMailWithIncludeOriginalAsAttachment() {
		logger.info("New "+ SaveDraftMailWithIncludeOriginalAsAttachment.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
		super.startingAccountPreferences.put("zimbraPrefComposeInNewWindow", "TRUE");
	}


	@Bugs (ids = "104334")
	@Test (description = "Reply to a mail with include original as attachment, format as HTML and save draft",
			groups = { "functional", "L3" })

	public void SaveDraftMailWithIncludeOriginalAsAttachment_01() throws HarnessException {

		// Send a message to the account
		String subject = "subject"+ ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();
		ZimbraAccount.Account1().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<e t='c' a='"+ ZimbraAccount.Account2().EmailAddress +"'/>" +
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

		// Reply the item
		SeparateWindowDisplayMail window = null;
		String windowTitle1 = "Zimbra: " + subject;
		String windowTitle2 = "Zimbra: Reply";

		try {

			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle1);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle1),"Verify the window is opened and switch to it");

			window.zToolbarPressButton(Button.B_REPLY);

			window.zSetWindowTitle(windowTitle2);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle2),"Verify the window is opened and switch to it");

			window.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_INCLUDE_ORIGINAL_AS_ATTACHMENT);
			window.zFillField(DisplayMail.Field.Body, body);

			window.zToolbarPressPulldown(Button.B_OPTIONS,Button.O_FORMAT_AS_HTML_MULTI_WINDOW);

			// Save the message
			window.zToolbarPressButton(Button.B_SAVE_DRAFT);
			ZAssert.assertEquals(window.sGetCssCountNewWindow("css=div[id='zv__COMPOSE-1_attachments_div'] table tbody tr td div[class='attBubbleContainer'] div span[id^='zv__COMPOSE-1_']"), 1, "Attachemnt not duplicated");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle1, app);
			app.zPageMain.zCloseWindow(window, windowTitle2, app);
		}

		// Get the message from the server
		MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:drafts subject:(" + subject + ")");

		// Verify the draft data matches
		ZAssert.assertStringContains(draft.dSubject, subject, "Verify the subject field is correct");
	}


	@Bugs (ids = "104334")
	@Test (description = "Reply to a mail with include original as attachment, format as text and save draft",
			groups = { "functional", "L3" })

	public void SaveDraftMailWithIncludeOriginalAsAttachment_02() throws HarnessException {

		// Send a message to the account
		String subject = "subject"+ ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();
		ZimbraAccount.Account1().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<e t='c' a='"+ ZimbraAccount.Account2().EmailAddress +"'/>" +
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
		String windowTitle1 = "Zimbra: " + subject;
		String windowTitle2 = "Zimbra: Reply";

		try {

			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle1);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle1),"Verify the window is opened and switch to it");

			window.zToolbarPressButton(Button.B_REPLY);

			window.zSetWindowTitle(windowTitle2);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle2),"Verify the window is opened and switch to it");

			window.zToolbarPressPulldown(Button.B_OPTIONS,Button.O_FORMAT_AS_HTML_MULTI_WINDOW);
			window.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_INCLUDE_ORIGINAL_AS_ATTACHMENT);
			window.zFillField(DisplayMail.Field.Body, body);
			window.zToolbarPressPulldown(Button.B_OPTIONS,Button.O_FORMAT_AS_PLAIN_TEXT_MULTI_WINDOW);

			window.zToolbarPressButton(Button.B_SAVE_DRAFT);
			ZAssert.assertEquals(window.sGetCssCountNewWindow("css=div[id='zv__COMPOSE-1_attachments_div'] table tbody tr td div[class='attBubbleContainer'] div span[id^='zv__COMPOSE-1_']"), 1, "Attachemnt not duplicated");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle1, app);
			app.zPageMain.zCloseWindow(window, windowTitle2, app);
		}

		// Get the message from the server
		MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:drafts subject:("+ subject +")");
		ZAssert.assertNotNull(draft, "not null present");

		// Verify the draft data matches
		ZAssert.assertStringContains(draft.dSubject, subject, "Verify the subject field is correct");
	}
}