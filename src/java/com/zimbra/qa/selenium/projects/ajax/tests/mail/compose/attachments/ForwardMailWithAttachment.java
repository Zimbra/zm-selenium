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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.attachments;

import java.io.File;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;

public class ForwardMailWithAttachment extends PrefGroupMailByMessageTest {

	public ForwardMailWithAttachment() {
		logger.info("New "+ ForwardMailWithAttachment.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}
	
	@Test(	description = "Forward a mail by adding attachment - Verify both attachment sent",
			groups = { "windows" })
	
	public void ForwardMailWithAttachment_01() throws HarnessException {
		
		//-- DATA
		final String subject = "subject03431362517016470";
		final String mimeFile = ZimbraSeleniumProperties.getBaseDirectory() + "/data/public/mime/email09/mime.txt";
		final String mimeAttachmentName = "screenshot.JPG";
		
		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);

		// Send the message to the test account
		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Forward the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);
		
		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);
		
		final String fileName = "testtextfile.txt";
		final String filePath = ZimbraSeleniumProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;
		
		app.zPageMail.zPressButton(Button.B_ATTACH);
		zUpload(filePath);
		
		// Send the message
		mailform.zSubmit();

		//-- Verification
		
		// From the receiving end, verify the message details
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
		
		ZimbraAccount.AccountB().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m id='"+ received.getId() +"'/>"
				+	"</GetMsgRequest>");

		String filename = ZimbraAccount.AccountB().soapSelectValue("//mail:mp[@cd='attachment']", "filename");
		ZAssert.assertEquals(filename, fileName, "Verify existing attachment exists in the forwarded mail");
		
		filename = ZimbraAccount.AccountB().soapSelectValue("//mail:mp[@cd='attachment'][2]", "filename");
		ZAssert.assertEquals(filename, mimeAttachmentName, "Verify newly added attachment exists in the forwarded mail");
		
		Element[] nodes = ZimbraAccount.AccountB().soapSelectNodes("//mail:mp[@filename='" + fileName + "']");
		ZAssert.assertEquals(nodes.length, 1, "Verify attachment exist in the forwarded mail");
		
		nodes = ZimbraAccount.AccountB().soapSelectNodes("//mail:mp[@filename='" + mimeAttachmentName + "']");
		ZAssert.assertEquals(nodes.length, 1, "Verify attachment exist in the forwarded mail");
		
		// Verify UI for attachment
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		app.zPageMail.zVerifyAttachmentExistsInMail(mimeAttachmentName);
		app.zPageMail.zVerifyAttachmentExistsInMail(fileName);
	}

}