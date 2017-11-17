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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.inlineimage;

import org.testng.SkipException;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowDisplayMail;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field;

public class EditAsNewWithAnInlineAttachment extends PrefGroupMailByMessageTest {

	public EditAsNewWithAnInlineAttachment() {
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
		logger.info("New "+ EditAsNewWithAnInlineAttachment.class.getCanonicalName());
	}


	@Test (description = "Edit as New message >> add inline Attchment from new window ",
			groups = { "functional", "L2", "upload" })

	public void EditAsNewWithAnInlineAttachment_01() throws HarnessException {

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
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Create file item
		final String fileName = "samplejpg.jpg";
		final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;

		SeparateWindowDisplayMail window = null;

		MailItem mail = new MailItem();
		mail.dBodyHtml = "body"+ ConfigProperties.getUniqueString();

		String windowTitle = "Zimbra: " + subject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			window.zToolbarPressPulldown(Button.B_ACTIONS, Button.O_EDIT_AS_NEW);

			// Type in body
			window.zFillField(Field.Body, mail.dBodyHtml);

			SleepUtil.sleepSmall();

			// Click Attach>>inline image
			window.zPressButton(Button.O_ATTACH_DROPDOWN);
			window.zPressButton(Button.B_ATTACH_INLINE);
			zUploadInlineImageAttachment(filePath);

			ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageAttachmentExistsInComposeWindow(windowTitle, 1),"Verify inline image is present in  compose window");

			window.zToolbarPressButton(Button.B_SEND);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

		for (int i = 0; i < 30; i++) {
			app.zGetActiveAccount().soapSend(
					"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
							+ "<query>subject:(" + subject + ")</query>"
							+ "</SearchRequest>");
			com.zimbra.common.soap.Element node = ZimbraAccount.AccountA().soapSelectNode("//mail:m", 1);
			if (node != null) {
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
	}
}