/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
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

import java.io.File;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;

public class ReplyMailWithInlineImageAttachment extends PrefGroupMailByMessageTest {

	public ReplyMailWithInlineImageAttachment() {
		logger.info("New "+ ReplyMailWithInlineImageAttachment.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
		super.startingAccountPreferences.put("zimbraPrefForwardReplyInOriginalFormat", "FALSE");
	}
	
	@Test(	description = "Reply to a mail with attachment - Verify inline image sent",
			groups = { "windows" })
	
	public void ReplyMailWithInlineImageAttachment_01() throws HarnessException {
		
		//-- DATA
		final String mimeSubject = "subject03431362517016470";
		final String mimeFile = ZimbraSeleniumProperties.getBaseDirectory() + "/data/public/mime/email09/mime.txt";
		final String subject = "subject13625192398933";
		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);

		LmtpInject.injectFile(ZimbraAccount.AccountA().EmailAddress, new File(mimeFile));

		MailItem original = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ mimeSubject +")");
		ZAssert.assertNotNull(original, "Verify the message is received correctly");

		// Get the part ID
		ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>"
			+		"<m id='"+ original.getId() +"'/>"
			+	"</GetMsgRequest>");

		String partID = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@cd='attachment']", "part");

		ZimbraAccount.AccountA().soapSend(
			"<SendMsgRequest xmlns='urn:zimbraMail'>" +
				"<m>" +
					"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
					"<su>"+ subject +"</su>" +
					"<mp ct='text/plain'>" +
						"<content>"+ "body" + ZimbraSeleniumProperties.getUniqueString() +"</content>" +
					"</mp>" +
					"<attach>" +
						"<mp mid='"+ original.getId() +"' part='"+ partID +"'/>" +
					"</attach>" +
				"</m>" +
			"</SendMsgRequest>");

		//-- GUI

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);
						
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Reply to the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
		
		final String fileName = "samplejpg.jpg";
		final String filePath = ZimbraSeleniumProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;
		
		app.zPageMail.zPressButton(Button.O_ATTACH_DROPDOWN);
		app.zPageMail.zPressButton(Button.B_ATTACH_INLINE);
		zUploadInlineImageAttachment(filePath);
		
		app.zPageMail.zVerifyInlineImageAttachmentExistsInMail(fileName);
		
		// Send the message
		mailform.zSubmit();

		//-- Verification
		
		// From the receiving end, verify the message details
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "from:("+ app.zGetActiveAccount().EmailAddress +") subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
		
		ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m id='"+ received.getId() +"'/>"
				+	"</GetMsgRequest>");

		String getFilename = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@cd='attachment']", "filename");
		ZAssert.assertEquals(getFilename, fileName, "Verify existing attachment exists in the replied mail");
		
		Element[] nodes = ZimbraAccount.AccountA().soapSelectNodes("//mail:mp[@filename='" + fileName + "']");
		ZAssert.assertEquals(nodes.length, 1, "Verify attachment exist in the replied mail");
		
		// Verify UI for attachment
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		ZAssert.assertTrue(app.zPageMail.zVerifyAttachmentExistsInMail(fileName), "Verify attachment exists in the email");
		
		app.zPageMail.zVerifyInlineImageAttachmentExistsInMail(fileName);
	}
}