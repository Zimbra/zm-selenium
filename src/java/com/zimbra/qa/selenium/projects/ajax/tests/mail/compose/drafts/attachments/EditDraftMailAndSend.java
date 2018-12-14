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

import org.openqa.selenium.Keys;
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
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;

public class EditDraftMailAndSend extends SetGroupMailByMessagePreference {

	public EditDraftMailAndSend() {
		logger.info("New "+ EditDraftMailAndSend.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
	}


	@Bugs(ids = "ZCS-4696")
	@Test (description = "Save draft a mail, edit, add an attachment, send the mail and verify the presence of the mail in Draft",
			groups = { "functional", "L2", "upload", "non-msedge" })

	public void EditDraftMailAndSend_01() throws HarnessException {

		try {

			// Create the message data to be sent
			MailItem mail = new MailItem();
			mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
			mail.dSubject = "subject" + ConfigProperties.getUniqueString();
			mail.dBodyHtml = "body" + ConfigProperties.getUniqueString();
			
			FolderItem drafts = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Drafts);
			FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);

			// Create file item
			final String fileName = "testtextfile.txt";
			final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;

			// Open the new mail form
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);

			// Fill out the form with the data
			mailform.zFill(mail);
			
			// Save the message as draft
			mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
			mailform.zToolbarPressButton(Button.B_CLOSE);
			
			// Go to draft folder and edit the message
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, drafts);
			app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);
			mailform.zToolbarPressButton(Button.B_EDIT);
			
			// Add an attachment
			app.zPageMail.zPressButton(Button.B_ATTACH);
			zUpload(filePath);

			// Send the mail
			mailform.zSubmit();
			
			// Check that mail is removed from draft folder
			ZAssert.assertTrue(app.zPageMail.zListGetMessages().size() == 0, "Verify that the mail is removed from draft folder");
			
			// Verify using soap that mail is sent to the recipients
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
			app.zPageMain.zKeyboardKeyEvent(Keys.ESCAPE);
		}
	}
}