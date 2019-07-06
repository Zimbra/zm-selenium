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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.formatting;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;

public class ReplyMail extends SetGroupMailByMessagePreference {

	public ReplyMail() {
		logger.info("New "+ ReplyMail.class.getCanonicalName());
	}


	@Test (description = "Reply an Excel formatting data message  and verify its formatting",
			groups = { "bhr" })

	public void ReplyHtmlMail_01() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Excel_Data_Formatting_Mime.txt";
		final String subject = "Test Excel Data Formatting";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		ZAssert.assertTrue(app.zPageMail.zVerifyDisplayMailElement("div[id='zimbraEditorContainer'] div table[border='1']"), "Verify Excel Table border ");

		// Reply the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		mailform.zSubmit();

		// From the receiving end, verify the message details
		MailItem received = MailItem.importFromSOAP(app.zGetActiveAccount(),"in:sent subject:("+subject +")");
		ZAssert.assertStringContains(received.dSubject, "Re", "Verify the subject field contains the 'Re' prefix");

		// Click to sent
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Sent));

		// Verify Excel Table border
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		ZAssert.assertTrue(app.zPageMail.zVerifyDisplayMailElement("div[id='zimbraEditorContainer'] div table[border='1']"), "Verify Excel Table border ");
		ZAssert.assertStringContains(actual.zGetMailProperty(com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field.Body), "ID", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field.Body), "Fname", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field.Body), "Lname", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field.Body), "Test1", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field.Body), "Test2", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field.Body), "Test3", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field.Body), "Test4", "Verify the body content matches");
	}
}