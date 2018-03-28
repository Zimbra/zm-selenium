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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.attachments;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AttachmentItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;

public class GetAttachment extends SetGroupMailByMessagePreference {

	public GetAttachment() throws HarnessException {
		logger.info("New "+ GetAttachment.class.getCanonicalName());
	}


	@Test (description = "Receive a message with one attachment",
			groups = { "smoke", "L1" })

	public void GetAttachment_01() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email05/mime01.txt";
		final String subject = "subject151615738";
		final String attachmentname = "file.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		List<AttachmentItem> items = display.zListGetAttachments();
		ZAssert.assertEquals(items.size(), 1, "Verify one attachment is in the message");

		boolean found = false;
		for ( AttachmentItem item : items ) {
			if ( item.getAttachmentName().equals(attachmentname)) {
				found = true;
				break;
			}
		}
		ZAssert.assertTrue(found, "Verify the attachment appears in the list (by file name)");
	}


	@Test (description = "Receive a message with three attachments",
			groups = { "functional", "L2" })

	public void GetAttachment_02() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email05/mime02.txt";
		final String subject = "subject151111738";
		final String attachmentname1 = "file01.txt";
		final String attachmentname2 = "file02.txt";
		final String attachmentname3 = "file03.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		List<AttachmentItem> items = display.zListGetAttachments();
		ZAssert.assertEquals(items.size(), 3, "Verify three attachment in the message");

		// Verify each attachment by file name
		boolean found1 = false;
		boolean found2 = false;
		boolean found3 = false;
		for ( AttachmentItem item : items ) {
			if ( item.getAttachmentName().equals(attachmentname1)) {
				found1 = true;
				continue;
			}
			if ( item.getAttachmentName().equals(attachmentname2)) {
				found2 = true;
				continue;
			}
			if ( item.getAttachmentName().equals(attachmentname3)) {
				found3 = true;
				continue;
			}
		}
		ZAssert.assertTrue(found1, "Verify the attachments appear in the list (by file name)");
		ZAssert.assertTrue(found2, "Verify the attachments appear in the list (by file name)");
		ZAssert.assertTrue(found3, "Verify the attachments appear in the list (by file name)");
	}


	@Bugs (ids = "60769")
	@Test (description = "Receive a message with an inline attachment",
			groups = { "smoke", "L1" })

	public void GetAttachment_03() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug60769/Bug60769.txt";
		final String subject = "FW: Christian cartoons [SEC=UNCLASSIFIED]";
		final String fileName = "image001.gif";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Verify UI for attachment
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		ZAssert.assertFalse(app.zPageMail.zVerifyExternalImageInfoBarExists(subject), "Verify External Image information bar is not displayed");
		ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageAttachmentExistsInMail(), "Verify inline attachment exists in the email");

		// From the receiving end, verify the message details
		MailItem received = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:\""+subject+"\"");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");

		app.zGetActiveAccount().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m id='"+ received.getId() +"'/>"
				+	"</GetMsgRequest>");

		String getFilename = app.zGetActiveAccount().soapSelectValue("//mail:mp[@ct='image/gif']", "filename");
		ZAssert.assertEquals(getFilename, fileName, "Verify existing attachment exists in the forwarded mail");

		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:mp[@filename='" + fileName + "']");
		ZAssert.assertEquals(nodes.length, 1, "Verify attachment exist in the forwarded mail");
	}


	@Bugs (ids = "83052")
	@Test (description = "Verify the presence of attachment in mail on the second attempt of Show Conversation",
			groups = { "functional", "L3" })

	public void GetAttachment_04() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email16/mime02.txt";
		final String subject = "remove attachment from conversation view";
		final String attachmentName = "remove.txt";
		ZimbraAccount account = app.zGetActiveAccount();

		// Inject a message with an attachment
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Double check that there is an attachment
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='message'>" + "<query>subject:(" + subject
				+ ")</query>" + "</SearchRequest>");
		String id = account.soapSelectValue("//mail:m", "id");

		account.soapSend("<GetMsgRequest xmlns='urn:zimbraMail' >" + "<m id='" + id + "'/>" + "</GetMsgRequest>");
		Element[] nodes = account.soapSelectNodes("//mail:mp[@cd='attachment']");
		ZAssert.assertGreaterThan(nodes.length, 0, "Verify the message has the attachment");

		// Refresh current view and check the presence of mail
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Switch to message view
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_BY_MESSAGE);

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Go to Actions drop down and select Show Conversation.
		app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.O_SHOW_CONVERSATION);

		// Check the presence of attachment
		AttachmentItem item = null;
		List<AttachmentItem> items = display.zListGetAttachments();
		for (AttachmentItem i : items) {
			if (i.getAttachmentName().equals(attachmentName)) {
				item = i;
				break;
			}
		}
		ZAssert.assertNotNull(item, "No attachment is in the message");

		// Close the show conversation tab
		app.zPageMail.zToolbarPressButton(Button.B_CLOSE_CONVERSATION);

		// Open the Show conversation view again and check the presence of attachment: Bug:83052
		app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.O_SHOW_CONVERSATION);

		// Check the presence of attachment
		items = display.zListGetAttachments();
		for (AttachmentItem i : items) {
			if (i.getAttachmentName().equals(attachmentName)) {
				item = i;
				break;
			}
		}
		ZAssert.assertNotNull(item, "No attachment is in the message");

		// Close the show conversation tab
		app.zPageMail.zToolbarPressButton(Button.B_CLOSE_CONVERSATION);
	}
}