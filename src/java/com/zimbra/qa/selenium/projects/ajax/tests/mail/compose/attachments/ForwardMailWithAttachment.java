/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class ForwardMailWithAttachment extends SetGroupMailByMessagePreference {

	public ForwardMailWithAttachment() {
		logger.info("New "+ ForwardMailWithAttachment.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}


	@Test (description = "Forward a mail by adding attachment - Verify both attachment sent",
			groups = { "smoke", "upload" })

	public void ForwardMailWithAttachment_01() throws HarnessException {

		try {

			final String subject = "subjectAttachment";
			final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email17/mime.txt";
			final String mimeAttachmentName = "samplejpg.jpg";

			FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);

			// Send the message to the test account
			injectMessage(app.zGetActiveAccount(), mimeFile);

			// Refresh current view
			ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

			// Forward the item
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);

			// Fill out the form with the data
			mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);

			final String fileName = "testtextfile.txt";
			final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;

			app.zPageMail.zPressButton(Button.B_ATTACH);
			zUpload(filePath);

			// Send the message
			mailform.zSubmit();

			// From the receiving end, verify the message details
			MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ subject +")");
			ZAssert.assertNotNull(received, "Verify the message is received correctly");

			ZimbraAccount.AccountA().soapSend(
					"<GetMsgRequest xmlns='urn:zimbraMail'>"
					+		"<m id='"+ received.getId() +"'/>"
					+	"</GetMsgRequest>");

			String getFilename = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@cd='attachment']", "filename");
			ZAssert.assertEquals(getFilename, fileName, "Verify existing attachment exists in the forwarded mail");

			Element[] nodes = ZimbraAccount.AccountA().soapSelectNodes("//mail:mp[@filename='" + mimeAttachmentName + "']");
			ZAssert.assertEquals(nodes.length, 1, "Verify attachment exists in the forwarded mail");

			// Verify UI for attachment
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
			ZAssert.assertTrue(app.zPageMail.zVerifyAttachmentExistsInMail(mimeAttachmentName), "Verify attachment exists in the email");

		} finally {
			app.zPageMain.zKeyboardKeyEvent(Keys.ESCAPE);
		}
	}


	@Test (description = "Forward a mail with attachment - Verify attachment sent",
			groups = { "sanity" })

	public void ForwardMailWithAttachment_02() throws HarnessException {

		final String subject = "subject03431362517016470";
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email09/mime.txt";
		final String mimeAttachmentName = "screenshot.JPG";

		// Send the message to the test account
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Forward the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);

		// Send the message
		mailform.zSubmit();

		// From the receiving end, verify the message details
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");

		// Verify the attachment exists in the forwarded mail
		ZimbraAccount.AccountB().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m id='"+ received.getId() +"'/>"
				+	"</GetMsgRequest>");

		String filename = ZimbraAccount.AccountB().soapSelectValue("//mail:mp[@cd='attachment']", "filename");
		ZAssert.assertEquals(filename, mimeAttachmentName, "Verify the attachment exists in the forwarded mail");
	}


	@Test (description = "Forward a mail after removing the attachemnt",
			groups = { "bhr" })

	public void ForwardMailWithAttachment_03() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email16/mime02.txt";
		final String subject = "remove attachment from conversation view";
		final String attachmentName = "remove.txt";

		// Send the message to the test account
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Forward the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);

		// Remove the attachment
		SleepUtil.sleepLong();
		mailform.zRemoveAttachment(attachmentName);

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.Account1().EmailAddress);

		// Send the message
		mailform.zSubmit();

		// From the receiving end, verify the message details
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.Account1(), "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");

		// Verify the attachment does not exist in the forwarded mail
		ZimbraAccount.Account1().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m id='"+ received.getId() +"'/>"
				+	"</GetMsgRequest>");

		String fileName = ZimbraAccount.Account1().soapSelectValue("//mail:mp[@cd='attachment']", "filename");
		ZAssert.assertNull(fileName, "Verify the attachment is not present in the forwarded mail");
	}


	@Bugs(ids = "76776")
	@Test (description = "Forward a mail having two attachments, remove one attachement and Cancel, again forward and verify number of attachments",
			groups = { "functional" })

	public void ForwardMailWithAttachment_04() throws HarnessException {

		final String subject = "TwoAttachmentsForward";
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/TwoAttachments.txt";
		final String attachmentName1 = "1.png";
		final String attachmentName2 = "2.png";

		// Send the message to the test account
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Forward the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);

		// Remove one of the attachments
		SleepUtil.sleepLong();
		mailform.zRemoveAttachment(attachmentName1);

		// Cancel the forward compose
		mailform.zToolbarPressButton(Button.B_CANCEL);

		mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);

		// Check the presence of attachments
		ZAssert.assertTrue(mailform.zHasAttachment(attachmentName1),"Verify attachment '" + attachmentName1 + "' is present");
		ZAssert.assertTrue(mailform.zHasAttachment(attachmentName2),"Verify attachment '" + attachmentName2 + "' is present");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);

		// Send the message
		mailform.zSubmit();

		// From the receiving end, verify the message details
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");

		// Verify the attachments exist in the forwarded mail
		ZimbraAccount.AccountB().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m id='"+ received.getId() +"'/>"
				+	"</GetMsgRequest>");

		String filename1 = ZimbraAccount.AccountB().soapSelectValue("//mail:mp[@cd='attachment'][1]", "filename");
		String filename2 = ZimbraAccount.AccountB().soapSelectValue("//mail:mp[@cd='attachment'][2]", "filename");
		ZAssert.assertEquals(filename1, attachmentName1, "Verify the attachment1 exists in the forwarded mail");
		ZAssert.assertEquals(filename2, attachmentName2, "Verify the attachment2 exists in the forwarded mail");
	}
}