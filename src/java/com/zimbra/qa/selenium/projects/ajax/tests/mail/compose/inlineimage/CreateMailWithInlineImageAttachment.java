/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.inlineimage;

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
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;

public class CreateMailWithInlineImageAttachment extends PrefGroupMailByMessageTest {

	public CreateMailWithInlineImageAttachment() {
		logger.info("New "+ CreateMailWithInlineImageAttachment.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
		super.startingAccountPreferences.put("zimbraPrefForwardReplyInOriginalFormat", "FALSE");
	}
	
	@Test(	description = "Send a mail by adding inline image attachment",
			groups = { "windows" })
	
	public void CreateMailWithInlineImageAttachment_01() throws HarnessException {
		
		try {
		
			// Create the message data to be sent
			MailItem mail = new MailItem();
			mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
			mail.dSubject = "subject" + ZimbraSeleniumProperties.getUniqueString();
			mail.dBodyHtml = "body" + ZimbraSeleniumProperties.getUniqueString();
			
			FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);
			
			// Create file item
			final String fileName = "samplejpg.jpg";
			final String filePath = ZimbraSeleniumProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;
			
			// Open the new mail form
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
			ZAssert.assertNotNull(mailform, "Verify the new form opened");
			
			// Fill out the form with the data
			mailform.zFill(mail);
			
			// Upload the file
			app.zPageMail.zPressButton(Button.O_ATTACH_DROPDOWN);
			app.zPageMail.zPressButton(Button.B_ATTACH_INLINE);
			zUploadInlineImageAttachment(filePath);
			
			//Verify inline image in compose window
			ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageAttachmentExistsInMail(), "Verify inline image is present in compose window");

			
			//app.zPageMail.zVerifyInlineImageAttachmentExistsInMail(fileName);
			
			// Send the message
			mailform.zSubmit();
			
			ZimbraAccount.AccountA().soapSend(
						"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
				+			"<query>subject:("+ mail.dSubject +")</query>"
				+		"</SearchRequest>");
			String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
			
			ZimbraAccount.AccountA().soapSend(
						"<GetMsgRequest xmlns='urn:zimbraMail'>"
				+			"<m id='"+ id +"' html='1'/>"
				+		"</GetMsgRequest>");
	
			String from = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='f']", "a");
			String to = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='t']", "a");
			String subject = ZimbraAccount.AccountA().soapSelectValue("//mail:su", null);
			String html = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@ct='text/html']//mail:content", null);
			
			ZAssert.assertEquals(from, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
			ZAssert.assertEquals(to, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
			ZAssert.assertEquals(subject, mail.dSubject, "Verify the subject field is correct");
			ZAssert.assertStringContains(html, mail.dBodyHtml, "Verify the html content");
			
			String getFilename = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@cd='inline']", "filename");
			ZAssert.assertEquals(getFilename, fileName, "Verify existing attachment exists in the  mail");
			
			Element[] nodes = ZimbraAccount.AccountA().soapSelectNodes("//mail:mp[@filename='" + fileName + "']");		
			ZAssert.assertEquals(nodes.length, 1, "Verify attachment exist in the sent mail");
			
			// Verify UI for attachment
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);			
			//app.zPageMail.zVerifyInlineImageAttachmentExistsInMail(fileName);
			
			// Verify inline image in reading pane
			ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageAttachmentExistsInMail(), "Verify inline image is present in reading pane");

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
