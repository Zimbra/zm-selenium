package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.inlineimage;

/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowFormMailNew;

public class CreateMailWithAnInlineImg extends PrefGroupMailByMessageTest {

	public CreateMailWithAnInlineImg() {
		logger.info("New " + CreateMailWithAnInlineImg.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
		super.startingAccountPreferences.put("zimbraPrefComposeInNewWindow","TRUE");

	}

	@Test(description = "Create and send email with an inline attachment - in a separate window", groups = {"windows", "smoke" })
	public void CreateMailWithAnInlineImg_01() throws HarnessException {

		try {

			// Create the message data to be sent
			MailItem mail = new MailItem();
			mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
			mail.dSubject = "subject"+ ZimbraSeleniumProperties.getUniqueString();

			FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);

			// Create file item
			final String fileName = "samplejpg.jpg";
			final String filePath = ZimbraSeleniumProperties.getBaseDirectory()	+ "\\data\\public\\other\\" + fileName;
			SeparateWindowFormMailNew window = null;

			// Open the new mail form
			try {

				window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW_IN_NEW_WINDOW);

				window.zSetWindowTitle("Compose");
				window.zWaitForActive(); // Make sure the window is there

				window.waitForComposeWindow();

				ZAssert.assertTrue(window.zIsActive(),"Verify the window is active");

				// Fill out the form with the data
				window.zFill(mail);

				// TODO: ... debugging to be removed
				mail.dBodyHtml = "body"+ ZimbraSeleniumProperties.getUniqueString();
				window.sSelectWindow("Zimbra: Compose");
				String locator = "css=iframe[id*=ifr]";
				window.zWaitForElementPresent(locator, "5000");
				window.sClickAt(locator, "");
				window.zTypeFormattedText(locator, mail.dBodyHtml);

				// Click Attach>>inline image
				window.zPressButton(Button.O_ATTACH_DROPDOWN);
				window.zPressButton(Button.B_ATTACH_INLINE);
				zUploadInlineImageAttachment(filePath);

				// Send the message
				window.zToolbarPressButton(Button.B_SEND);

				if (window.zWaitForWindowClosed("Zimbra: Compose")) {
					// Window closes automatically
					window = null;
				}
			} finally {

				// Make sure to close the window
				if (window != null) {
					window.zCloseWindow();
					window = null;
				}

			}

			// Sometimes, the harness is too fast for the client.
			// Since we are composing in a new window, there is no
			// busy overlay to block.
			//
			// Add a loop, while waiting for the message
			//
			for (int i = 0; i < 30; i++) {

				ZimbraAccount.AccountA().soapSend(
						"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
								+ "<query>subject:(" + mail.dSubject
								+ ")</query>" + "</SearchRequest>");
				com.zimbra.common.soap.Element node = ZimbraAccount.AccountA()
						.soapSelectNode("//mail:m", 1);
				if (node != null) {
					// found the message
					break;
				}

				SleepUtil.sleep(1000);

			}

			ZimbraAccount.AccountA().soapSend(
					"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
							+ "<query>subject:(" + mail.dSubject + ")</query>"
							+ "</SearchRequest>");
			String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m",
					"id");

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
			// Verify inline image in reading pane
			ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageAttachmentExistsInMail(),"Verify inline image is present in reading pane");

		} finally {

			Robot robot;

			try {
				robot = new Robot();
				robot.delay(250);
				robot.keyPress(KeyEvent.VK_ESCAPE);
				robot.keyRelease(KeyEvent.VK_ESCAPE);
				robot.delay(50);

			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
