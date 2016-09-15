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

import java.awt.AWTException;
import java.awt.Robot;
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
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
	}

	@Bugs(ids = "103903")
	@Test( description = "Verify the presence of attachment while replying to a mail and changing option from  'Include Original as an attachment' to 'Include Original message' in new window", 
			groups = { "functional" })

	public void ComposeReplyWithAttachmentAndVariousOptions_01() throws HarnessException {

		if (OperatingSystem.isWindows() == true) {

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

				//-- GUI

				// Refresh current view
				app.zPageMail.zVerifyMailExists(subject);

				// Select the item
				app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
				SleepUtil.sleepSmall();

				// Reply to the item
				FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
				SleepUtil.sleepLong();

				//Include original message as attachment
				mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_INCLUDE_ORIGINAL_AS_ATTACHMENT);

				//Check if a warning dialog is present. If Yes, Press Yes to continue
				if(mailform.sIsVisible(Locators.zOkCancelContinueComposeWarningDialog) && mailform.sIsElementPresent(Locators.zOkCancelContinueComposeWarningDialog)) {

					mailform.sClickAt(Locators.zOkBtnOnContinueComposeWarningDialog,"0,0");
				}
				SleepUtil.sleepSmall();

				//Verify that the message is included as attachment
				ZAssert.assertTrue(mailform.zHasAttachment(subject),"Original message is not present as attachment");

				SeparateWindowFormMailNew window = null;
				String windowTitle = "Zimbra: Reply";
				
				// Open it in new window
				try {

					window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_DETACH_COMPOSE);

					window.zSetWindowTitle(windowTitle);
					window.zWaitForWindow(windowTitle);
					ZAssert.assertTrue(window.zIsActive(),"Verify the window is active");

					// Select the window
					window.sSelectWindow(windowTitle);

					//Verify that the message is included as attachment in new window
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

					//verify that the new attachment is present and message as attachment is not
					ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present");
					ZAssert.assertFalse(mailform.zHasAttachment(subject),"Included original message attachment is still present");

					//Close the new window
					window.zCloseWindow(windowTitle);
					window = null;

				} finally {

					// Make sure to close the window
					if (window != null) {
						window.zCloseWindow(windowTitle);
						window = null;
					}
				}

			} finally {

				Robot robot;

				try {
					robot = new Robot();
					robot.delay(250);
					robot.keyPress(KeyEvent.VK_ESCAPE);
					robot.keyRelease(KeyEvent.VK_ESCAPE);
					robot.delay(50);

				} catch (AWTException e) {
					e.printStackTrace();
				}

			}

		} else {
			throw new SkipException("File upload operation is allowed only for Windows OS, skipping this test...");
		}
	}


	@Bugs(ids = "103903")
	@Test( description = "Verify the presence of attachment in new window while replying to a mail and selecting 'Use Prefixes' option from Options.",	
			groups = { "functional" })

	public void ComposeReplyWithAttachmentAndVariousOptions_02() throws HarnessException {

		if (OperatingSystem.isWindows() == true) {

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

				//-- GUI

				// Refresh current view
				app.zPageMail.zVerifyMailExists(subject);

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

				//Select use prefix option from Options drop down
				mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_USE_PRIFIX);

				//Check if a warning dialog is present. If Yes, Press Yes to continue
				if(mailform.sIsVisible(Locators.zOkCancelContinueComposeWarningDialog) && mailform.sIsElementPresent(Locators.zOkCancelContinueComposeWarningDialog)) {

					mailform.sClickAt(Locators.zOkBtnOnContinueComposeWarningDialog,"0,0");
				}

				//verify that the attachment is still present
				ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present after selecting Use Prefix from Options!");

				SeparateWindowFormMailNew window = null;
				String windowTitle = "Zimbra: Reply";
				
				//Open it in new window
				try {

					window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_DETACH_COMPOSE);

					window.zSetWindowTitle(windowTitle);
					window.zWaitForWindow(windowTitle);
					ZAssert.assertTrue(window.zIsActive(),"Verify the window is active");

					// Select the window
					window.sSelectWindow(windowTitle);

					//Verify that the attachment is present in new window as well.
					ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present in new window!");

					//Close the new window
					window.zCloseWindow(windowTitle);
					window = null;

				} finally {

					// Make sure to close the window
					if (window != null) {
						window.zCloseWindow(windowTitle);
						window = null;
					}
				}

			} finally {

				Robot robot;

				try {
					robot = new Robot();
					robot.delay(250);
					robot.keyPress(KeyEvent.VK_ESCAPE);
					robot.keyRelease(KeyEvent.VK_ESCAPE);
					robot.delay(50);

				} catch (AWTException e) {
					e.printStackTrace();
				}

			}

		} else {
			throw new SkipException("File upload operation is allowed only for Windows OS, skipping this test...");
		}
	}

	@Bugs(ids = "103903")
	@Test( description = "Verify the presence of attachment in new window while replying to a mail and selecting 'Include Headers' option from Options'", 
		groups = { "functional" })

	public void ComposeReplyWithAttachmentAndVariousOptions_03() throws HarnessException {

		if (OperatingSystem.isWindows() == true) {

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

				//-- GUI

				// Refresh current view
				app.zPageMail.zVerifyMailExists(subject);

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

				//Select Include Headers from Options drop down
				mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_INCLUDE_HEADERS);

				//Check if a warning dialog is present. If Yes, Press Yes to continue
				if(mailform.sIsVisible(Locators.zOkCancelContinueComposeWarningDialog) && mailform.sIsElementPresent(Locators.zOkCancelContinueComposeWarningDialog)) {
					mailform.sClickAt(Locators.zOkBtnOnContinueComposeWarningDialog,"0,0");
				}

				//verify that the attachment is present after selecting Include Headers option
				ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present after selecting Include Headers from Options!");

				//Select Include Headers from Options drop down  again as it was set by default
				mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_INCLUDE_HEADERS);

				//verify that the attachment is present after selecting Include Headers option
				ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present after selecting Include Headers from Options!");

				SeparateWindowFormMailNew window = null;
				String windowTitle = "Zimbra: Reply";
				
				// Open it in new window
				try {

					window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_DETACH_COMPOSE);

					window.zSetWindowTitle(windowTitle);
					window.zWaitForWindow(windowTitle);
					ZAssert.assertTrue(window.zIsActive(),"Verify the window is active");

					// Select the window
					window.sSelectWindow(windowTitle);

					//Verify that the attachment is present in new window as well.
					ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present in new window!");

					//Close the new window
					window.zCloseWindow(windowTitle);
					window = null;

				} finally {

					// Make sure to close the window
					if (window != null) {
						window.zCloseWindow(windowTitle);
						window = null;
					}
				}

			} finally {

				Robot robot;

				try {
					robot = new Robot();
					robot.delay(250);
					robot.keyPress(KeyEvent.VK_ESCAPE);
					robot.keyRelease(KeyEvent.VK_ESCAPE);
					robot.delay(50);

				} catch (AWTException e) {
					e.printStackTrace();
				}

			}
			
		} else {
			throw new SkipException("File upload operation is allowed only for Windows OS, skipping this test...");
		}
	}
}