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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.drafts;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;

public class SaveDraftMailWithAttachment extends PrefGroupMailByMessageTest {

	public SaveDraftMailWithAttachment() {
		logger.info("New "+ SaveDraftMailWithAttachment.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
	}
	
	@Test(	description = "Save draft a mail with attachment and send a mail",
			groups = { "sanity","windows" })
	
	public void SaveDraftMailWithAttachment_01() throws HarnessException {
		
		try {
		
			// Create the message data to be sent
			MailItem mail = new MailItem();
			mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
			mail.dSubject = "subject" + ZimbraSeleniumProperties.getUniqueString();
			mail.dBodyHtml = "body" + ZimbraSeleniumProperties.getUniqueString();
			
			FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);
			
			// Create file item
			final String fileName = "testtextfile.txt";
			final String filePath = ZimbraSeleniumProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;
			
			// Open the new mail form
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
			ZAssert.assertNotNull(mailform, "Verify the new form opened");
			
			// Fill out the form with the data
			mailform.zFill(mail);
			
			// Upload the file
			app.zPageMail.zPressButton(Button.B_ATTACH);
			zUpload(filePath);
			
			// Send the message after saving as draft
			mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
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
			
			Element[] nodes = ZimbraAccount.AccountA().soapSelectNodes("//mail:mp[@filename='" + fileName + "']");
			ZAssert.assertEquals(nodes.length, 1, "Verify attachment exist in the sent mail");
			
			// Verify UI for attachment
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);			
			ZAssert.assertTrue(app.zPageMail.zVerifyAttachmentExistsInMail(fileName), "Verify attachment exists in the email");
			
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
	
	
	@Test(	description = "Open existing saved draft with attachment and send a mail",
			groups = { "functional","windows" })
	
	public void OpenExistingSavedDraftMailWithAttachment_02() throws HarnessException {
		
		try {
			
			// Create file item
			final String mimeSubject = "subjectAttachment";
			final String mimeFile = ZimbraSeleniumProperties.getBaseDirectory() + "/data/public/mime/email17/mime.txt";
			final String mimeAttachmentName = "samplejpg.jpg";
			
			FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);
			FolderItem drafts = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Drafts);

			LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));

			// Refresh current view
			app.zPageMail.zVerifyMailExists(mimeSubject);
							
			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, mimeSubject);
			
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
			
			app.zPageMail.sClickAt("css=div[id^='zv__COMPOSE'] td a:contains('Add attachments from original message')", "0,0");
			SleepUtil.sleepMedium();
			
			// Save draft
			mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
			mailform.zToolbarPressButton(Button.B_CLOSE);
			
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, drafts);
			app.zPageMail.zListItem(Action.A_LEFTCLICK, mimeSubject);
			mailform.zToolbarPressButton(Button.B_EDIT);
			
			mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
			
			// Upload another file and send mail
			app.zPageMail.zPressButton(Button.B_ATTACH);
			
			final String anotherFileName = "putty.log";
			final String anotherFilePath = ZimbraSeleniumProperties.getBaseDirectory() + "\\data\\public\\other\\" + anotherFileName;
			
			zUpload(anotherFilePath);
			mailform.zSubmit();
			
			ZimbraAccount.AccountA().soapSend(
							"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
					+			"<query>subject:("+ mimeSubject +")</query>"
					+		"</SearchRequest>");
			String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
			
			ZimbraAccount.AccountA().soapSend(
							"<GetMsgRequest xmlns='urn:zimbraMail'>"
					+			"<m id='"+ id +"' html='1'/>"
					+		"</GetMsgRequest>");
	
			String from = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='f']", "a");
			String to = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='t'][2]", "a");
			String subject = ZimbraAccount.AccountA().soapSelectValue("//mail:su", null);
			String html = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@ct='text/plain']//mail:content", null);
			
			ZAssert.assertEquals(from, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
			ZAssert.assertEquals(to, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
			ZAssert.assertEquals(subject, "Re: " + mimeSubject, "Verify the subject field is correct");
			ZAssert.assertStringContains(html, mimeSubject, "Verify the html content");
			
			String getFilename = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@cd='attachment']", "filename");
			ZAssert.assertEquals(getFilename, anotherFileName, "Verify existing attachment exists in the sent mail");
			
			getFilename = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@cd='attachment'][2]", "filename");
			ZAssert.assertEquals(getFilename, mimeAttachmentName, "Verify newly added attachment exists in the sent mail");
			
			Element[] nodes = ZimbraAccount.AccountA().soapSelectNodes("//mail:mp[@filename='" + anotherFileName + "']");
			ZAssert.assertEquals(nodes.length, 1, "Verify attachment exist in the replied mail");
			
			nodes = ZimbraAccount.AccountA().soapSelectNodes("//mail:mp[@filename='" + mimeAttachmentName + "']");
			ZAssert.assertEquals(nodes.length, 1, "Verify newly added attachment exist in the sent mail");
			
			// Verify UI for attachment
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
			ZAssert.assertTrue(app.zPageMail.zVerifyAttachmentExistsInMail(mimeAttachmentName), "Verify attachment exists in the email");
			ZAssert.assertTrue(app.zPageMail.zVerifyAttachmentExistsInMail(anotherFileName), "Verify attachment exists in the email");

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
