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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.attachments;

import org.openqa.selenium.Keys;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Locators;

public class ComposeReplyMailWithAttachmentAndVariousOptions extends SetGroupMailByMessagePreference {

	public ComposeReplyMailWithAttachmentAndVariousOptions() {
		logger.info("New "+ ComposeReplyMailWithAttachmentAndVariousOptions.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
	}


	@Bugs (ids = "103903")
	@Test (description = "Verify the presence of attachment while replying to mail and changing option from  'Include Original as an attachment' to 'Include Original message'",
			groups = { "functional", "L2", "upload" })

	public void ComposeReplyWithAttachmentAndVariousOptions_01() throws HarnessException {

		String subject = "subject" + ConfigProperties.getUniqueString();

		try {

			// Send a message to the account
			ZimbraAccount.AccountA().soapSend(
						"<SendMsgRequest xmlns='urn:zimbraMail'>" +
							"<m>" +
								"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
								"<su>"+ subject +"</su>" +
								"<mp ct='text/html'>" +
									"<content> Body content"+ ConfigProperties.getUniqueString() +"</content>" +
								"</mp>" +
							"</m>" +
						"</SendMsgRequest>");

			// Get the mail item for the new message
			MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
			ZAssert.assertNotNull(mail, "Verify the message is received correctly");

			// Refresh current view
			ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
			SleepUtil.sleepSmall();

			// Reply to the item
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
			SleepUtil.sleepLong();

			//Include original message as attachment
			mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_INCLUDE_ORIGINAL_AS_ATTACHMENT);

			// Check if a warning dialog is present. If Yes, Press Yes to continue
			if (mailform.sIsVisible(Locators.zOkCancelContinueComposeWarningDialog) && mailform.sIsElementPresent(Locators.zOkCancelContinueComposeWarningDialog)) {
				mailform.sClickAt(Locators.zOkBtnOnContinueComposeWarningDialog,"0,0");
			}
			SleepUtil.sleepSmall();

			// Verify that the message is included as attachment
			ZAssert.assertTrue(mailform.zHasAttachment(subject),"Original message is not present as attachment");

			//Attach a new file
			final String fileName = "inlineImage.jpg";
			final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;

			app.zPageMail.zPressButton(Button.O_ATTACH_DROPDOWN);
			app.zPageMail.zPressButton(Button.B_MY_COMPUTER);
			zUpload(filePath);

			//Include the original message in the body and not as attachment
			mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_INCLUDE_ORIGINAL_MESSAGE);
			SleepUtil.sleepSmall();

			// Verify that the new attachment is present and message as attachment is not
			ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present");
			ZAssert.assertFalse(mailform.zHasAttachment(subject),"Included original message attachment is still present");

			// Cancel the message
			AbsDialog warning = (AbsDialog)mailform.zToolbarPressButton(Button.B_CANCEL);
			ZAssert.assertNotNull(warning, "Verify the dialog is returned");

			// Dismiss the dialog
			warning.zPressButton(Button.B_NO);
			warning.zWaitForClose();

		} finally {
			app.zPageMain.zKeyboardKeyEvent(Keys.ESCAPE);
		}
	}


	@Bugs (ids = "103903")
	@Test (description = "Verify the presence of attachment while replying to mail and selecting 'Use Prefixes' option from Options'",
			groups = { "functional", "L3", "upload" })

	public void ComposeReplyWithAttachmentAndVariousOptions_02() throws HarnessException {

		String subject = "subject" + ConfigProperties.getUniqueString();

		try {

			// Send a message to the account
			ZimbraAccount.AccountA().soapSend(
						"<SendMsgRequest xmlns='urn:zimbraMail'>" +
							"<m>" +
								"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
								"<su>"+ subject +"</su>" +
								"<mp ct='text/html'>" +
									"<content> Body content"+ ConfigProperties.getUniqueString() +"</content>" +
								"</mp>" +
							"</m>" +
						"</SendMsgRequest>");

			// Get the mail item for the new message
			MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
			ZAssert.assertNotNull(mail, "Verify the message is received correctly");

			// Refresh current view
			ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

			// Reply to the item
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
			SleepUtil.sleepLong();

			//Attach a new file
			final String fileName = "inlineImage.jpg";
			final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;

			app.zPageMail.zPressButton(Button.O_ATTACH_DROPDOWN);
			app.zPageMail.zPressButton(Button.B_MY_COMPUTER);
			zUpload(filePath);

			// Select Use Prefix from Options drop down
			mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_USE_PRIFIX);

			// Check if a warning dialog is present. If Yes, Press Yes to continue
			if (mailform.sIsVisible(Locators.zOkCancelContinueComposeWarningDialog) && mailform.sIsElementPresent(Locators.zOkCancelContinueComposeWarningDialog)) {
				mailform.sClickAt(Locators.zOkBtnOnContinueComposeWarningDialog,"0,0");
			}

			// Verify that the new attachment is present and message as attachment is not
			ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present after selecting Use Prefix from Options!");

			// Cancel the message
			AbsDialog warning = (AbsDialog)mailform.zToolbarPressButton(Button.B_CANCEL);
			ZAssert.assertNotNull(warning, "Verify the dialog is returned");

			// Dismiss the dialog
			warning.zPressButton(Button.B_NO);
			warning.zWaitForClose();

		} finally {
			app.zPageMain.zKeyboardKeyEvent(Keys.ESCAPE);
		}
	}


	@Bugs (ids = "103903")
	@Test (description = "Verify the presence of attachment while replying to mail and selecting 'Include Headers' option from Options'",
			groups = { "functional", "L3", "upload" })

	public void ComposeReplyWithAttachmentAndVariousOptions_03() throws HarnessException {

		String subject = "subject" + ConfigProperties.getUniqueString();

		try {

			// Send a message to the account
			ZimbraAccount.AccountA().soapSend(
						"<SendMsgRequest xmlns='urn:zimbraMail'>" +
							"<m>" +
								"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
								"<su>"+ subject +"</su>" +
								"<mp ct='text/html'>" +
									"<content> Body content"+ ConfigProperties.getUniqueString() +"</content>" +
								"</mp>" +
							"</m>" +
						"</SendMsgRequest>");

			// Get the mail item for the new message
			MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
			ZAssert.assertNotNull(mail, "Verify the message is received correctly");

			// Refresh current view
			ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

			// Reply to the item
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
			SleepUtil.sleepLong();

			//Attach a new file
			final String fileName = "inlineImage.jpg";
			final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;

			app.zPageMail.zPressButton(Button.O_ATTACH_DROPDOWN);
			app.zPageMail.zPressButton(Button.B_MY_COMPUTER);
			zUpload(filePath);

			// Select Include Headers from Options drop down
			mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_INCLUDE_HEADERS);

			// Check if a warning dialog is present. If Yes, Press Yes to continue
			if (mailform.sIsVisible(Locators.zOkCancelContinueComposeWarningDialog) && mailform.sIsElementPresent(Locators.zOkCancelContinueComposeWarningDialog)) {
				mailform.sClickAt(Locators.zOkBtnOnContinueComposeWarningDialog,"0,0");
			}

			// Verify that the new attachment is present and message as attachment is not
			ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present after selecting Include Headers from Options!");

			// Select Include Headers from Options drop down again to Include Headers
			mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_INCLUDE_HEADERS);

			// Verify that the new attachment is present and message as attachment is not
			ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present after selecting Include Headers from Options!");

			// Cancel the message
			AbsDialog warning = (AbsDialog)mailform.zToolbarPressButton(Button.B_CANCEL);
			ZAssert.assertNotNull(warning, "Verify the dialog is returned");

			// Dismiss the dialog
			warning.zPressButton(Button.B_NO);
			warning.zWaitForClose();

		} finally {
			app.zPageMain.zKeyboardKeyEvent(Keys.ESCAPE);
		}
	}
}