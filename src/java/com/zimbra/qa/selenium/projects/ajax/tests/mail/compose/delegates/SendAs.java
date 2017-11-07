/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.delegates;

import java.io.File;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;

public class SendAs extends PrefGroupMailByMessageTest {

	public SendAs() {
		logger.info("New "+ SendAs.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}


	@Test (description = "Send As another user",
			groups = { "smoke", "L1" })

	public void SendAs_01() throws HarnessException {

		// Mail data
		String subject = "subject"+ ConfigProperties.getUniqueString();

		// The grantor
		ZimbraAccount grantor = null;
		grantor = new ZimbraAccount();
		grantor.provision();
		grantor.authenticate();

		grantor.soapSend(
				"<GrantRightsRequest xmlns='urn:zimbraAccount'>"
			+		"<ace gt='usr' d='"+ app.zGetActiveAccount().EmailAddress +"' right='sendAs'/>"
			+	"</GrantRightsRequest>");

		// Refresh UI
		app.zPageMain.zRefreshMainUI();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, "body" + ConfigProperties.getUniqueString());
		mailform.zFillField(Field.From, grantor.EmailAddress);
		mailform.zSubmit();

		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ subject +")</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");

		ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail' >"
			+		"<m id='"+ id +"'/>"
			+	"</GetMsgRequest>");

		// Verify From: grantor
		String from = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='f']", "a");
		ZAssert.assertEquals(from, grantor.EmailAddress, "Verify From: grantor");

		// Verify no headers contain active account
		Element[] nodes = ZimbraAccount.AccountA().soapSelectNodes("//mail:e");
		for (Element e : nodes) {
			String attr = e.getAttribute("a", null);
			if ( attr != null ) {
				ZAssert.assertStringDoesNotContain(
						attr,
						app.zGetActiveAccount().EmailAddress,
						"Verify no headers contain the active account email address");
			}
		}
	}


	@Bugs(ids="106931,102475")
	@Test (description = "Forward a mail with attachment as a delegate - Verify attachment sent",
			groups = { "functional", "L2" })

	public void SendAs_02() throws HarnessException {

		ZimbraAccount grantor = null;
		grantor = new ZimbraAccount();
		grantor.provision();
		grantor.authenticate();

		grantor.soapSend(
				"<GrantRightsRequest xmlns='urn:zimbraAccount'>"
			+		"<ace gt='usr' d='"+ app.zGetActiveAccount().EmailAddress +"' right='sendAs'/>"
			+	"</GrantRightsRequest>");

		// Refresh UI
		app.zPageMain.zRefreshMainUI();

		//-- DATA
		final String subject = "subject13977785775182543";
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email14/mime.txt";
		final String mimeAttachmentName = "screenshot.JPG";

		// Send the message to the test account
		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFile));

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Forward the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);
		mailform.zFillField(Field.From, grantor.EmailAddress);

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

		// Verify From: grantor
		String from = ZimbraAccount.AccountB().soapSelectValue("//mail:e[@t='f']", "a");
		ZAssert.assertEquals(from, grantor.EmailAddress, "Verify From: grantor");
	}
}