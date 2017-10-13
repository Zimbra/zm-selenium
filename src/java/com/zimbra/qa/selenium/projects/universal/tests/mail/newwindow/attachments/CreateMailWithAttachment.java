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

package com.zimbra.qa.selenium.projects.universal.tests.mail.newwindow.attachments;

import java.awt.event.KeyEvent;
import org.testng.SkipException;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.SeparateWindowFormMailNew;

public class CreateMailWithAttachment extends PrefGroupMailByMessageTest {

	public CreateMailWithAttachment() {
		logger.info("New "+ CreateMailWithAttachment.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeInNewWindow", "TRUE");
	}

	@Test( description = "Send a mail with an attachment - in a separate window",
			groups = {"sanity", "L0" })
	
	public void CreateMailWithAttachment_01() throws HarnessException {

		if (OperatingSystem.isWindows() == true && !ConfigProperties.getStringProperty("browser").contains("msedge")) {

			try {

				// Create the message data to be sent
				MailItem mail = new MailItem();
				mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
				mail.dSubject = "subject" + ConfigProperties.getUniqueString();
				mail.dBodyHtml = "body"	+ ConfigProperties.getUniqueString();
				FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);

				// Create file item
				final String fileName = "testtextfile.txt";
				final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;
				SeparateWindowFormMailNew window = null;
				String windowTitle = "Zimbra: Compose";

				// Open the new mail form
				try {

					window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW_IN_NEW_WINDOW);

					window.zSetWindowTitle(windowTitle);
					window.zWaitForActive();

					window.waitForComposeWindow();

					ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

					// Fill out the form with the data
					window.zFill(mail);
					
					// Click Attach
					window.zPressButton(Button.B_ATTACH);
					zUpload(filePath, window);

					// Send the message
					window.zToolbarPressButton(Button.B_SEND);

				} finally {
					app.zPageMain.zCloseWindow(window, windowTitle, app);
				}

				for (int i = 0; i < 30; i++) {

					ZimbraAccount.AccountA().soapSend("<SearchRequest types='message' xmlns='urn:zimbraMail'>"
							+ "<query>subject:(" + mail.dSubject + ")</query>" + "</SearchRequest>");
					com.zimbra.common.soap.Element node = ZimbraAccount.AccountA().soapSelectNode("//mail:m", 1);
					if (node != null) {
						break;
					}

					SleepUtil.sleep(1000);

				}

				ZimbraAccount.AccountA().soapSend("<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>subject:(" + mail.dSubject + ")</query>" + "</SearchRequest>");
				String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m","id");

				ZimbraAccount.AccountA().soapSend(
						"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + id
								+ "' html='1'/>" + "</GetMsgRequest>");

				String from = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='f']", "a");
				String to = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='t']", "a");
				String subject = ZimbraAccount.AccountA().soapSelectValue("//mail:su", null);
				String html = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@ct='text/html']//mail:content", null);

				ZAssert.assertEquals(from, app.zGetActiveAccount().EmailAddress,"Verify the from field is correct");
				ZAssert.assertEquals(to, ZimbraAccount.AccountA().EmailAddress,"Verify the to field is correct");
				ZAssert.assertEquals(subject, mail.dSubject,"Verify the subject field is correct");
				ZAssert.assertStringContains(html, mail.dBodyHtml,"Verify the html content");

				Element[] nodes = ZimbraAccount.AccountA().soapSelectNodes("//mail:mp[@filename='" + fileName + "']");
				ZAssert.assertEquals(nodes.length, 1,"Verify attachment exist in the sent mail");

				// Verify UI for attachment
				app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
				app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
				ZAssert.assertTrue(app.zPageMail.zVerifyAttachmentExistsInMail(fileName),"Verify attachment exists in the email");

			} finally {

				app.zPageMain.zKeyboardKeyEvent(KeyEvent.VK_ESCAPE);

			}

		} else {
			throw new SkipException("File upload operation is allowed only for Windows OS (Skipping upload tests on MS Edge for now due to intermittancy and major control issue), skipping this test...");
		}

	}
}