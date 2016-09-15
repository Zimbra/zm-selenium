package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.inlineimage;

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

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowDisplayMail;

public class EditAsNewWithAnInlineAttachment extends PrefGroupMailByMessageTest {

	public EditAsNewWithAnInlineAttachment() {
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
		logger.info("New "+ EditAsNewWithAnInlineAttachment.class.getCanonicalName());
	}

	@Test( description = "Edit as New message >> add inline Attchment from new window ",
			groups = { "functional" })
	
	public void EditAsNewWithAnInlineAttachment_01() throws HarnessException {

		if (OperatingSystem.isWindows() == true) {

			String subject = "subject"+ ConfigProperties.getUniqueString();
			FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);

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


			// Create file item
			final String fileName = "samplejpg.jpg";
			final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;

			SeparateWindowDisplayMail window = null;

			MailItem mail = new MailItem();
			mail.dBodyHtml = "body"+ ConfigProperties.getUniqueString();
			
			String windowTitle = "Zimbra: Compose";

			try {

				// Choose Actions -> Launch in Window
				window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);
				window.zSetWindowTitle(windowTitle);
				window.zWaitForActive();	// Make sure the window is there
				ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

				window.zToolbarPressPulldown(Button.B_ACTIONS, Button.O_EDIT_AS_NEW);
				window.zSetWindowTitle(windowTitle);
				window.zWaitForActive();
				ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
				window.sSelectWindow(windowTitle);


				//Type in body
				String	locator = "css=div[id^='zv__COMPOSE'] iframe[id$='_body_ifr']";

				window.zWaitForElementPresent(locator, "5000");
				window.sSelectFrame(locator);
				window.sClick(locator);

				//Note: Explicitly we have used both command to type in body area.
				window.sTypeNewWindow(locator, mail.dBodyHtml);
				window.zTypeFormattedText(locator, mail.dBodyHtml);

				SleepUtil.sleepSmall();

				// Click Attach>>inline image
				window.zPressButton(Button.O_ATTACH_DROPDOWN);
				window.zPressButton(Button.B_ATTACH_INLINE);
				zUploadInlineImageAttachment(filePath);

				ZAssert.assertTrue(window.zVerifyInlineImageAttachmentExistsInComposeWindow(),"Verify inline image is present in  compose window");

				window.zToolbarPressButton(Button.B_SEND);
				window.zSetWindowTitle(windowTitle);
				window.zWaitForActive();
				window.zToolbarPressButton(Button.B_CLOSE);
				SleepUtil.sleepSmall();

				// Window is closed automatically by the client
				window = null;

			} finally {

				// Make sure to close the window
				if ( window != null ) {
					window.zCloseWindow(windowTitle);
					window = null;
				}

			}

			for (int i = 0; i < 30; i++) {

				app.zGetActiveAccount().soapSend(
						"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
								+ "<query>subject:(" + subject + ")</query>"
								+ "</SearchRequest>");
				com.zimbra.common.soap.Element node = ZimbraAccount.AccountA()
						.soapSelectNode("//mail:m", 1);
				if (node != null) {
					// found the message
					break;
				}

				SleepUtil.sleep(1000);

			}

			app.zGetActiveAccount().soapSend(
					"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
							+ "<query>subject:(" + subject + ")</query>"
							+ "</SearchRequest>");
			String id = app.zGetActiveAccount().soapSelectValue("//mail:m", "id");

			app.zGetActiveAccount().soapSend(
					"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + id
					+ "' html='1'/>" + "</GetMsgRequest>");

			String html = app.zGetActiveAccount().soapSelectValue("//mail:mp[@ct='text/html']//mail:content", null);
			ZAssert.assertStringContains(html, mail.dBodyHtml, "Verify the html content");

			// Verify UI for attachment
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
			ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageAttachmentExistsInMail(),"Verify inline attachment exists in the email");

		} else {
			throw new SkipException("File upload operation is allowed only for Windows OS, skipping this test...");
		}

	}
}