/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.readreceipt;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning.DialogWarningID;

public class SendReadReceiptsPrompt extends PrefGroupMailByMessageTest {

	public SendReadReceiptsPrompt() {
		logger.info("New "+ SendReadReceiptsPrompt.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefMailSendReadReceipts", "prompt");
	}


	@Test (description = "zimbraPrefMailSendReadReceipts=prompt - verify prompt, verify receipt is sent",
			groups = { "functional", "L2" })

	public void SendReadReceiptsPrompt_01() throws HarnessException {

		// Create a source account
		ZimbraAccount sender = new ZimbraAccount();
		sender.provision().authenticate();

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();

		// Send the message from AccountA to the ZCS user
		sender.soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>"
				+			"<e t='n' a='"+ sender.EmailAddress +"'/>"
				+			"<su>"+ subject +"</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>"+ "body" + ConfigProperties.getUniqueString() +"</content>"
				+			"</mp>"
				+		"</m>"
				+	"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		DialogWarning dialog = (DialogWarning)app.zPageMain.zGetWarningDialog(DialogWarningID.SendReadReceipt);
		ZAssert.assertNotNull(dialog, "Verify the read receipt dialog is popped up");

		// Click YES to send
		dialog.zClickButton(Button.B_YES);

		// Make sure all read-receipts are delivered
		Stafpostqueue q = new Stafpostqueue();
		q.waitForPostqueue();

		// Verify the sender receives the read receipt
		sender.soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>subject:(Read-Receipt) subject:("+ subject +")</query>"
				+	"</SearchRequest>");
		Element[] nodes = sender.soapSelectNodes("//mail:m");
		ZAssert.assertEquals(nodes.length, 1, "Verify the read receipt is received by the sender");
	}


	@Test (description = "zimbraPrefMailSendReadReceipts=prompt - verify prompt, verify receipt is not sent",
			groups = { "functional", "L2" })

	public void SendReadReceiptsPrompt_02() throws HarnessException {

		// Create a source account
		ZimbraAccount sender = new ZimbraAccount();
		sender.provision().authenticate();

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();

		// Send the message from AccountA to the ZCS user
		sender.soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>"
				+			"<e t='n' a='"+ sender.EmailAddress +"'/>"
				+			"<su>"+ subject +"</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>"+ "body" + ConfigProperties.getUniqueString() +"</content>"
				+			"</mp>"
				+		"</m>"
				+	"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		DialogWarning dialog = (DialogWarning)app.zPageMain.zGetWarningDialog(DialogWarningID.SendReadReceipt);
		ZAssert.assertNotNull(dialog, "Verify the read receipt dialog is popped up");

		// Click NO to not send
		dialog.zClickButton(Button.B_NO);

		// Make sure all read-receipts are delivered
		Stafpostqueue q = new Stafpostqueue();
		q.waitForPostqueue();

		// Verify the sender receives the read receipt
		sender.soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>subject:(Read-Receipt) subject:("+ subject +")</query>"
				+	"</SearchRequest>");
		Element[] nodes = sender.soapSelectNodes("//mail:m");
		ZAssert.assertEquals(nodes.length, 0, "Verify the read receipt is not received by the sender");
	}
}