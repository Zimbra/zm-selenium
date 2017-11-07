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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.drafts.attachments;

import java.awt.event.KeyEvent;
import java.io.File;
import org.testng.SkipException;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;

public class SaveDraftMailWithInlineImageAttachment extends PrefGroupMailByMessageTest {

	public SaveDraftMailWithInlineImageAttachment() {
		logger.info("New "+ SaveDraftMailWithInlineImageAttachment.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
		super.startingAccountPreferences.put("zimbraPrefForwardReplyInOriginalFormat", "FALSE");
	}


	@Test (description = "Save draft a mail with inline attachment and send a mail",
			groups = { "smoke", "L1" })

	public void SaveDraftAndSendMailWithInlineImageAttachment_01() throws HarnessException {

		if (OperatingSystem.isWindows() == true && !ConfigProperties.getStringProperty("browser").contains("edge")) {

			try {

				// Create the message data to be sent
				MailItem mail = new MailItem();
				mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
				mail.dSubject = "subject" + ConfigProperties.getUniqueString();
				mail.dBodyHtml = "body" + ConfigProperties.getUniqueString();

				FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);

				// Create file item
				final String fileName = "structure.jpg";
				final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;

				// Open the new mail form
				FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
				ZAssert.assertNotNull(mailform, "Verify the new form opened");

				// Fill out the form with the data
				mailform.zFill(mail);

				app.zPageMail.zPressButton(Button.O_ATTACH_DROPDOWN);
				app.zPageMail.zPressButton(Button.B_ATTACH_INLINE);
				zUploadInlineImageAttachment(filePath);

				ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageAttachmentExistsInComposeWindow(), "Verify inline attachment exists in the Compose window");

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
				ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageAttachmentExistsInMail(), "Verify attachment exists in the email");

			} finally {
				app.zPageMain.zKeyboardKeyEvent(KeyEvent.VK_ESCAPE);
			}

		} else {
			throw new SkipException("File upload operation is allowed only for Windows OS (Skipping upload tests on MS Edge for now due to intermittancy and major control issue), skipping this test...");
		}
	}


	@Test (description = "Open existing saved draft with attachment and send a mail with inline image attachment",
			groups = { "functional", "L2" })

	public void OpenExistingSavedDraftAndSendMailWithInlineImageAttachment_02() throws HarnessException {

		if (OperatingSystem.isWindows() == true && !ConfigProperties.getStringProperty("browser").contains("edge")) {

			try {

				// Create file item
				final String mimeSubject = "subjectAttachment";
				final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email17/mime.txt";
				final String mimeAttachmentName = "samplejpg.jpg";

				FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);
				FolderItem drafts = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Drafts);

				LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFile));

				// Refresh current view
				ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(mimeSubject), "Verify message displayed in current view");

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

				final String anotherFileName = "structure.jpg";
				final String anotherFilePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + anotherFileName;

				app.zPageMail.zPressButton(Button.O_ATTACH_DROPDOWN);
				app.zPageMail.zPressButton(Button.B_ATTACH_INLINE);
				zUploadInlineImageAttachment(anotherFilePath);

				ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageAttachmentExistsInComposeWindow(), "Verify inline attachment exists in the compose window");

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
				String html = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@ct='text/html']//mail:content", null);

				ZAssert.assertEquals(from, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
				ZAssert.assertEquals(to, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
				ZAssert.assertEquals(subject, "Re: " + mimeSubject, "Verify the subject field is correct");
				ZAssert.assertStringContains(html, mimeSubject, "Verify the html content");

				String getFilename = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@cd='inline']", "filename");
				ZAssert.assertEquals(getFilename, anotherFileName, "Verify existing attachment exists in the sent mail");

				getFilename = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@cd='attachment']", "filename");
				ZAssert.assertEquals(getFilename, mimeAttachmentName, "Verify newly added attachment exists in the sent mail");

				Element[] nodes = ZimbraAccount.AccountA().soapSelectNodes("//mail:mp[@filename='" + anotherFileName + "']");
				ZAssert.assertEquals(nodes.length, 1, "Verify attachment exist in the replied mail");

				nodes = ZimbraAccount.AccountA().soapSelectNodes("//mail:mp[@filename='" + mimeAttachmentName + "']");
				ZAssert.assertEquals(nodes.length, 1, "Verify newly added attachment exist in the sent mail");

				// Verify UI for attachment
				app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
				app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
				ZAssert.assertTrue(app.zPageMail.zVerifyAttachmentExistsInMail(mimeAttachmentName), "Verify attachment exists in the email");
				ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageAttachmentExistsInMail(), "Verify inline attachment exists in the email");

			} finally {
				app.zPageMain.zKeyboardKeyEvent(KeyEvent.VK_ESCAPE);
			}

		} else {
			throw new SkipException("File upload operation is allowed only for Windows OS (Skipping upload tests on MS Edge for now due to intermittancy and major control issue), skipping this test...");
		}
	}
}