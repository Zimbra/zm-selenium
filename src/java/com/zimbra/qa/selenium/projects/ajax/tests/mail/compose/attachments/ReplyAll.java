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

import java.io.File;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;

public class ReplyAll extends PrefGroupMailByMessageTest {

	public ReplyAll() {
		logger.info("New "+ ReplyAll.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}


	@Test (description = "Reply to a mail with attachment - Verify no attachment sent",
			groups = { "functional", "L2" })

	public void Reply_01() throws HarnessException {

		final String mimeSubject = "subject1397778577518254677";
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email15/mime.txt";

		final String subject = "subject13625192398933";

		LmtpInject.injectFile(ZimbraAccount.AccountA(), new File(mimeFile));

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
							"<content>"+ "body" + ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"<attach>" +
							"<mp mid='"+ original.getId() +"' part='"+ partID +"'/>" +
						"</attach>" +
					"</m>" +
				"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Forward the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLYALL);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Send the message
		mailform.zSubmit();

		// From the receiving end, verify the message details
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "from:("+ app.zGetActiveAccount().EmailAddress +") subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");

		// Verify the attachment exists in the forwarded mail
		ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m id='"+ received.getId() +"'/>"
				+	"</GetMsgRequest>");

		Element[] nodes = ZimbraAccount.AccountA().soapSelectNodes("//mail:mp[@cd='attachment']");
		ZAssert.assertEquals(nodes.length, 0, "Verify the attachment does not exist in the replied mail");
	}
}