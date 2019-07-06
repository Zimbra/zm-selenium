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

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning.DialogWarningID;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;

public class CheckAttachmentSize extends SetGroupMailByMessagePreference {

	public CheckAttachmentSize() throws HarnessException {
		logger.info("New "+ CheckAttachmentSize.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");

		// Setting the maximum message size to 1.5MB
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyConfigRequest xmlns='urn:zimbraAdmin'>"
						+   "<a n='zimbraMtaMaxMessageSize'>1572864</a>"
						+ "</ModifyConfigRequest>");
	}

	@Bugs(ids = "76919")
	@Test (description = "Try to add an attachment of size greater than max size and check the display of warning message.",
			groups = { "functional", "upload", "non-msedge" })

	public void CheckAttachmentSize_01() throws HarnessException {

		try {

			// Create the message data to be sent
			MailItem mail = new MailItem();
			mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
			mail.dSubject = "subject" + ConfigProperties.getUniqueString();
			mail.dBodyHtml = "body" + ConfigProperties.getUniqueString();

			// Files to be uploaded
			final String fileName1 = "Basic24bitBitmap.bmp";
			final String filePath1 = ConfigProperties.getBaseDirectory() + "\\data\\public\\Files\\Basic01\\" + fileName1;
			final String fileName2 = "Basic16ColorBitmap.bmp";
			final String filePath2 = ConfigProperties.getBaseDirectory() + "\\data\\public\\Files\\Basic01\\" + fileName2;

			// Warning message on dialog
			final String warningMessage = "Attachment failed since the item would exceed the size limit of 1 MB.";

			FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);

			// Open the new mail form
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);

			// Fill out the form with the data
			mailform.zFill(mail);

			// Upload the file having size more than 1.1MB
			app.zPageMail.zPressButton(Button.B_ATTACH);
			zUpload(filePath1);

			// A dialog will appear stating that the attachment size can't exceed max limit
			DialogWarning warning = (DialogWarning) app.zPageMain.zGetWarningDialog(DialogWarningID.ZmMsgDialog);
			warning.zWaitForActive();

			// Verify the warning message displayed in the dialog
			ZAssert.assertStringContains(warning.zGetWarningContent(), warningMessage, 
					"Verify the warning message displayed while trying to attach a file of size more than 1 MB");

			// Confirm the deletion 
			warning.zPressButton(Button.B_OK);

			// Upload the file having size less than 1 MB
			app.zPageMail.zPressButton(Button.B_ATTACH);
			zUpload(filePath2);

			// Send the message
			mailform.zSubmit();

			// verify through soap that attachment within the file size limit are attached successfully
			ZimbraAccount.AccountA().soapSend(
					"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
							+			"<query>subject:("+ mail.dSubject +")</query>"
							+		"</SearchRequest>");
			String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");

			ZimbraAccount.AccountA().soapSend(
					"<GetMsgRequest xmlns='urn:zimbraMail'>"
							+			"<m id='"+ id +"' html='1'/>"
							+		"</GetMsgRequest>");

			Element[] nodes = ZimbraAccount.AccountA().soapSelectNodes("//mail:mp[@filename='" + fileName2 + "']");
			ZAssert.assertEquals(nodes.length, 1, "Verify attachment exist in the sent mail");

			// Verify UI for attachment
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
			app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);
			ZAssert.assertTrue(app.zPageMail.zVerifyAttachmentExistsInMail(fileName2), "Verify attachment exists in the email");

		} finally {
			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<ModifyConfigRequest xmlns='urn:zimbraAdmin'>"
							+   "<a n='zimbraMtaMaxMessageSize'>10240000</a>"
							+ "</ModifyConfigRequest>");
		}
	}
}