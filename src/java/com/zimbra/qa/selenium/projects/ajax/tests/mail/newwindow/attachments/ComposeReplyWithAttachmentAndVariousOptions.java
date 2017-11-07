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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.attachments;

import java.awt.event.KeyEvent;
import org.testng.SkipException;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
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
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowFormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Locators;

public class ComposeReplyWithAttachmentAndVariousOptions extends PrefGroupMailByMessageTest {

	public ComposeReplyWithAttachmentAndVariousOptions() {
		logger.info("New "+ ComposeReplyWithAttachmentAndVariousOptions.class.getCanonicalName());
	}


	@Bugs(ids = "103903, 106583")
	@Test (description = "Verify the presence of attachment while replying to a mail and changing option from  'Include Original as an attachment' to 'Include Original message' in new window",
			groups = { "functional", "L3" })

	public void ComposeReplyWithAttachmentAndVariousOptions_01() throws HarnessException {

		if (OperatingSystem.isWindows() == true && !ConfigProperties.getStringProperty("browser").contains("edge")) {

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

				// Include original message as attachment
				mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_INCLUDE_ORIGINAL_AS_ATTACHMENT);
				zCloseRandomDialogs(mailform);

				// Verify that the message is included as attachment
				ZAssert.assertTrue(mailform.zHasAttachment(subject),"Original message is not present as attachment");

				// Open it in new window
				SeparateWindowFormMailNew window = null;
				String windowTitle = "Zimbra: Reply";

				final String fileName = "inlineImage.jpg";
				final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;

				try {

					window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_DETACH_COMPOSE);

					window.zSetWindowTitle(windowTitle);
					ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

					// Verify that the message is included as attachment in new window
					ZAssert.assertTrue(mailform.zHasAttachment(subject),"Original message is not present as attachment");

					app.zPageMail.zPressButton(Button.O_ATTACH_DROPDOWN);
					app.zPageMail.zPressButton(Button.B_MY_COMPUTER);
					zUpload(filePath);

					// Include the original message in the body and not as attachment
					mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_INCLUDE_ORIGINAL_MESSAGE);
					SleepUtil.sleepSmall();

					// Verify that the new attachment is present and message as attachment is not
					ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present");
					ZAssert.assertFalse(mailform.zHasAttachment(subject),"Included original message attachment is still present");

				} finally {
					app.zPageMain.zCloseWindow(window, windowTitle, app);

					// This is just for bug 106583
					if (window != null) {
						window.zCloseWindow(fileName);
						window = null;
					}
					app.zPageMail.zSelectWindow(null);
				}

			} finally {
				app.zPageMain.zKeyboardKeyEvent(KeyEvent.VK_ESCAPE);
			}

		} else {
			throw new SkipException("File upload operation is allowed only for Windows OS (Skipping upload tests on MS Edge for now due to intermittancy and major control issue), skipping this test...");
		}
	}


	@Bugs(ids = "103903")
	@Test (description = "Verify the presence of attachment in new window while replying to a mail and selecting 'Use Prefixes' option from Options.",
			groups = { "functional", "L3" })

	public void ComposeReplyWithAttachmentAndVariousOptions_02() throws HarnessException {

		if (OperatingSystem.isWindows() == true && !ConfigProperties.getStringProperty("browser").contains("edge")) {

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

				// Attach a new file
				final String fileName = "inlineImage.jpg";
				final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;

				app.zPageMail.zPressButton(Button.O_ATTACH_DROPDOWN);
				app.zPageMail.zPressButton(Button.B_MY_COMPUTER);
				zUpload(filePath);

				// Select use prefix option from Options drop down
				mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_USE_PRIFIX);
				zCloseRandomDialogs(mailform);

				// Verify that the attachment is still present
				ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present after selecting Use Prefix from Options!");

				// Open it in new window
				SeparateWindowFormMailNew window = null;
				String windowTitle = "Zimbra: Reply";

				try {

					window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_DETACH_COMPOSE);

					window.zSetWindowTitle(windowTitle);
					ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

					// Verify that the attachment is present in new window as well.
					ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present in new window!");

				} finally {
					app.zPageMain.zCloseWindow(window, windowTitle, app);
				}

			} finally {
				app.zPageMain.zKeyboardKeyEvent(KeyEvent.VK_ESCAPE);
			}

		} else {
			throw new SkipException("File upload operation is allowed only for Windows OS (Skipping upload tests on MS Edge for now due to intermittancy and major control issue), skipping this test...");
		}
	}

	@Bugs(ids = "103903")
	@Test (description = "Verify the presence of attachment in new window while replying to a mail and selecting 'Include Headers' option from Options'",
			groups = { "functional", "L3" })

	public void ComposeReplyWithAttachmentAndVariousOptions_03() throws HarnessException {

		if (OperatingSystem.isWindows() == true && !ConfigProperties.getStringProperty("browser").contains("edge")) {

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

				// Attach a new file
				final String fileName = "inlineImage.jpg";
				final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;

				app.zPageMail.zPressButton(Button.O_ATTACH_DROPDOWN);
				app.zPageMail.zPressButton(Button.B_MY_COMPUTER);
				zUpload(filePath);

				// Select Include Headers from Options drop down
				mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_INCLUDE_HEADERS);
				zCloseRandomDialogs(mailform);

				// Verify that the attachment is present after selecting Include Headers option
				ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present after selecting Include Headers from Options!");

				// Select Include Headers from Options drop down again as it was set by default
				mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_INCLUDE_HEADERS);
				SleepUtil.sleepMedium();
				zCloseRandomDialogs(mailform);

				// Verify that the attachment is present after selecting Include Headers option
				ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present after selecting Include Headers from Options!");

				// Open message in a separate window
				SeparateWindowFormMailNew window = null;
				String windowTitle = "Zimbra: Reply";

				try {

					window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_DETACH_COMPOSE);

					window.zSetWindowTitle(windowTitle);
					ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

					// Verify that the attachment is present in new window as well.
					ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present in new window!");

				} finally {
					app.zPageMain.zCloseWindow(window, windowTitle, app);
				}

			} finally {
				app.zPageMain.zKeyboardKeyEvent(KeyEvent.VK_ESCAPE);
			}

		} else {
			throw new SkipException("File upload operation is allowed only for Windows OS (Skipping upload tests on MS Edge for now due to intermittancy and major control issue), skipping this test...");
		}
	}


	public void zCloseRandomDialogs(FormMailNew mailform) throws HarnessException {

		// Check if a warning dialog is present. If Yes, Press Yes to continue
		if (mailform.sIsVisible(Locators.zOkCancelContinueComposeWarningDialog) && mailform.sIsElementPresent(Locators.zOkCancelContinueComposeWarningDialog)) {
			mailform.sClickAt(Locators.zOkBtnOnContinueComposeWarningDialog,"0,0");
			SleepUtil.sleepMedium();
		}

		// Check if a warning dialog is present. If Yes, Press Yes to continue
		if (mailform.sIsVisible("css=div#OkCancel.DwtDialog") && mailform.sIsElementPresent("css=div#OkCancel.DwtDialog")) {
			mailform.sClickAt("css=div#OkCancel.DwtDialog td[id^='OK']  td[id$='_title']","0,0");
			SleepUtil.sleepMedium();
		}
	}
}