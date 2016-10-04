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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.drafts;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.OperatingSystem;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;

public class SaveDraftMailWithIncludeOriginalAsAttachment extends PrefGroupMailByMessageTest {

	public SaveDraftMailWithIncludeOriginalAsAttachment() {
		logger.info("New "+ SaveDraftMailWithIncludeOriginalAsAttachment.class.getCanonicalName());
	}

	@Test( description = "Reply to a mail with include original as attachment, format as HTML and save draft",
			groups = { "functional" })

	public void SaveDraftMailWithIncludeOriginalAsAttachment_01() throws HarnessException {

		if (OperatingSystem.isWindows() == true) {

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
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
			mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_INCLUDE_ORIGINAL_AS_ATTACHMENT);
			mailform.zFillField(Field.Body, body);
			mailform.zToolbarPressPulldown(Button.B_OPTIONS,Button.O_FORMAT_AS_HTML);

			// Save the message
			mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
			SleepUtil.sleepSmall();
			ZAssert.assertEquals(app.zPageMail.sGetXpathCount("//div[@id='zv__COMPOSE-1_attachments_div']/table/tbody/tr/td/div/div/span"), 1, "Attachemnt not duplicated");
			mailform.zToolbarPressButton(Button.B_CLOSE);

			// Get the message from the server
			MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:drafts subject:("+ subject +")");

			// Verify the draft data matches
			ZAssert.assertStringContains(draft.dSubject, subject, "Verify the subject field is correct");
			ZAssert.assertStringContains(draft.dBodyText, body, "Verify the subject field is correct");

		} else {
			throw new SkipException("File upload operation is allowed only for Windows OS, skipping this test...");
		}

	}

	@Test( description = "Reply to a mail with include original as attachment, format as text and save draft",
			groups = { "functional" })

	public void SaveDraftMailWithIncludeOriginalAsAttachment_02() throws HarnessException {

		if (OperatingSystem.isWindows() == true) {

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
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
			mailform.zToolbarPressPulldown(Button.B_OPTIONS,Button.O_FORMAT_AS_HTML);
			mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_INCLUDE_ORIGINAL_AS_ATTACHMENT);
			SleepUtil.sleepSmall();
			mailform.zFillField(Field.Body, body);
			DialogWarning dialog = (DialogWarning) mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_FORMAT_AS_PLAIN_TEXT);
			dialog.zClickButton(Button.B_OK);
			SleepUtil.sleepSmall();

			// Save the message
			mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
			SleepUtil.sleepSmall();
			ZAssert.assertEquals(app.zPageMail.sGetXpathCount("//div[@id='zv__COMPOSE-1_attachments_div']/table/tbody/tr/td/div/div/span"), 1, "Attachemnt not duplicated");
			mailform.zToolbarPressButton(Button.B_CLOSE);
			SleepUtil.sleepSmall();

			// Get the message from the server
			MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:drafts subject:("+ subject +")");

			// Verify the draft data matches
			ZAssert.assertStringContains(draft.dSubject, subject, "Verify the subject field is correct");
			ZAssert.assertStringContains(draft.dBodyText, body, "Verify the subject field is correct");

		} else {
			throw new SkipException("File upload operation is allowed only for Windows OS, skipping this test...");
		}
	}

}
