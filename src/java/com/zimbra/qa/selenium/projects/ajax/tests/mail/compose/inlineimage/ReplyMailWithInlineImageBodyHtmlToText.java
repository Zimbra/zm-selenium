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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.inlineimage;

import org.openqa.selenium.Keys;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Locators;

public class ReplyMailWithInlineImageBodyHtmlToText extends SetGroupMailByMessagePreference {

	public ReplyMailWithInlineImageBodyHtmlToText() {
		logger.info("New "+ ReplyMailWithInlineImageBodyHtmlToText.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
	}


	@Bugs (ids = "104342,106133")
	@Test (description = "Verify that in reply compose screen, inline attachment shows as an attachment when changing format to from HTML to Plain Text",
			groups = { "functional-skip", "application-bug", "upload" })

	public void ReplyMailWithInlineImageBodyHtmlToText_01() throws HarnessException {

		String subject = "subject" + ConfigProperties.getUniqueString();

		try {

			// Send a message to the account
			ZimbraAccount.AccountA().soapSend(
						"<SendMsgRequest xmlns='urn:zimbraMail'>" +
							"<m>" +
								"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
								"<su>"+ subject +"</su>" +
								"<mp ct='text/html'>" +
									"<content> Body content"+ ConfigProperties.getUniqueString() +"</content>" +
								"</mp>" +
							"</m>" +
						"</SendMsgRequest>");

			// Get the mail item for the new message
			MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
			ZAssert.assertNotNull(mail, "Verify the message is received correctly");

			// Refresh current view
			ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

			// Reply to the item
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
			SleepUtil.sleepLong();

			final String fileName = "inlineImage.jpg";
			final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;

			app.zPageMail.zPressButton(Button.O_ATTACH_DROPDOWN);
			app.zPageMail.zPressButton(Button.B_ATTACH_INLINE);
			zUploadInlineImageAttachment(filePath);

			app.zPageMail.zVerifyInlineImageAttachmentExistsInComposeWindow();

			DialogWarning dialog = (DialogWarning) mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_FORMAT_AS_PLAIN_TEXT);
			dialog.zPressButton(Button.B_OK);
			SleepUtil.sleepMedium();

			mailform.sClickAt(Locators.zAddAttachmentFromOriginalMsgLink, "0,0");

			ZAssert.assertTrue(mailform.zHasAttachment(fileName),"Attachment is not present");

			// Send the mail
			mailform.zSubmit();

			// Click on sent folder to verify the presence of attachment
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Sent));

			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
			ZAssert.assertTrue(app.zPageMail.zVerifyAttachmentExistsInMail(fileName), "Attachment is not present in the sent mail.");

		} finally {
			app.zPageMain.zKeyboardKeyEvent(Keys.ESCAPE);
		}
	}
}