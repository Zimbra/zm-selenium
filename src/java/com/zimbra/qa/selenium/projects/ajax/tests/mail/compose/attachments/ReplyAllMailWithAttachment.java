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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.attachments;

import org.openqa.selenium.Keys;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class ReplyAllMailWithAttachment extends SetGroupMailByMessagePreference {

	public ReplyAllMailWithAttachment() {
		logger.info("New "+ ReplyAllMailWithAttachment.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
	}


	@Test (description = "Reply to a mail with attachment - Verify both attachment sent",
			groups = { "sanity", "L0", "upload" })

	public void ReplyAllMailWithAttachment_01() throws HarnessException {

		try {

			final String mimeSubject = "subjectAttachment";
			final String mimeFile = ConfigProperties.getBaseDirectory() + "\\data\\public\\mime\\email17\\mime.txt";
			FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);
			final String mimeAttachmentName = "samplejpg.jpg";

			// Inject the sample mime
			injectMessage(app.zGetActiveAccount(), mimeFile);

			MailItem original = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ mimeSubject +")");
			ZAssert.assertNotNull(original, "Verify the message is received correctly");

			// Refresh current view
			ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(mimeSubject), "Verify message displayed in current view");

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, mimeSubject);

			// Reply to all the item
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLYALL);

			mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
			mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);

			final String fileName = "structure.jpg";
			final String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/" + fileName;

			app.zPageMail.zPressButton(Button.B_ATTACH);
			zUpload(filePath);

			app.zPageMail.sClickAt("css=div[id^='zv__COMPOSE'] td a:contains('Add attachments from original message')", "0,0");
			SleepUtil.sleepMedium();

			// Send the message
			mailform.zSubmit();

			// From the receiving end, verify the message details
			MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "from:("+ app.zGetActiveAccount().EmailAddress +") subject:("+ mimeSubject +")");
			ZAssert.assertNotNull(received, "Verify the message is received correctly");

			ZimbraAccount.AccountA().soapSend(
					"<GetMsgRequest xmlns='urn:zimbraMail'>"
					+		"<m id='"+ received.getId() +"'/>"
					+	"</GetMsgRequest>");

			String filename = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@cd='attachment']", "filename");
			ZAssert.assertEquals(filename, fileName, "Verify existing attachment exists in the replied mail");

			filename = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@cd='attachment'][2]", "filename");
			ZAssert.assertEquals(filename, mimeAttachmentName, "Verify newly added attachment exists in the replied mail");

			Element[] nodes = ZimbraAccount.AccountA().soapSelectNodes("//mail:mp[@filename='" + fileName + "']");
			ZAssert.assertEquals(nodes.length, 1, "Verify attachment exist in the replied mail");

			nodes = ZimbraAccount.AccountA().soapSelectNodes("//mail:mp[@filename='" + mimeAttachmentName + "']");
			ZAssert.assertEquals(nodes.length, 1, "Verify newly added attachment exist in the replied mail");

			// From the receiving end, verify the message details
			MailItem receivedB = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "from:("+ app.zGetActiveAccount().EmailAddress +") subject:("+ mimeSubject +")");
			ZAssert.assertNotNull(receivedB, "Verify the message is received correctly");

			ZimbraAccount.AccountB().soapSend(
					"<GetMsgRequest xmlns='urn:zimbraMail'>"
					+		"<m id='"+ receivedB.getId() +"'/>"
					+	"</GetMsgRequest>");

			String getFilename = ZimbraAccount.AccountB().soapSelectValue("//mail:mp[@cd='attachment']", "filename");
			ZAssert.assertEquals(getFilename, fileName, "Verify existing attachment exists in the replied mail");

			getFilename = ZimbraAccount.AccountB().soapSelectValue("//mail:mp[@cd='attachment'][2]", "filename");
			ZAssert.assertEquals(getFilename, mimeAttachmentName, "Verify newly added attachment exists in the replied mail");

			nodes = ZimbraAccount.AccountB().soapSelectNodes("//mail:mp[@filename='" + fileName + "']");
			ZAssert.assertEquals(nodes.length, 1, "Verify attachment exist in the replied mail");

			nodes = ZimbraAccount.AccountB().soapSelectNodes("//mail:mp[@filename='" + mimeAttachmentName + "']");
			ZAssert.assertEquals(nodes.length, 1, "Verify newly added attachment exist in the replied mail");

			// Verify UI for attachment
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
			app.zPageMail.zListItem(Action.A_LEFTCLICK, mimeSubject);
			ZAssert.assertTrue(app.zPageMail.zVerifyAttachmentExistsInMail(fileName), "Verify attachment exists in the email");
			ZAssert.assertTrue(app.zPageMail.zVerifyAttachmentExistsInMail(mimeAttachmentName), "Verify attachment exists in the email");

		} finally {
			app.zPageMain.zKeyboardKeyEvent(Keys.ESCAPE);
		}
	}
}