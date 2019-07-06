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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.attachments;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;

public class CheckErrorAfterRemovingAttachement extends SetGroupMailByMessagePreference {

	public CheckErrorAfterRemovingAttachement() throws HarnessException {
		logger.info("New "+ CheckErrorAfterRemovingAttachement.class.getCanonicalName());
	}


	@Bugs (ids = "88160")
	@Test (description = "Bug 88160 - Remove an attachment from a mail and then delete mail",
			groups = { "sanity" })

	public void CheckErrorAfterRemovingAttachement_01() throws HarnessException {

		// Data Setup
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email05/mime01.txt";
		final String subject = "subject151615738";
		final String attachmentname = "file.txt";
		ZimbraAccount account = app.zGetActiveAccount();

		// Inject the message
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Verification for attachment
		account.soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
						+		"<query>subject:("+ subject +")</query>"
						+	"</SearchRequest>");
		String id = account.soapSelectValue("//mail:m", "id");

		account.soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail' >"
						+		"<m id='"+ id +"'/>"
						+	"</GetMsgRequest>");
		Element[] nodes = account.soapSelectNodes("//mail:mp[@cd='attachment']");
		ZAssert.assertGreaterThan(nodes.length, 0, "Verify the message has the attachment");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		AttachmentItem item = null;
		List<AttachmentItem> items = display.zListGetAttachments();
		for (AttachmentItem i : items) {
			if ( i.getAttachmentName().equals(attachmentname)) {
				item = i;
				break;
			}
		}

		ZAssert.assertNotNull(item, "Verify one attachment is in the message");

		// Double click the message to open in full reading pane
		app.zPageMail.zListItem(Action.A_DOUBLECLICK, subject);

		// Click remove attachment
		DialogWarning dialog = (DialogWarning)display.zListAttachmentItem(Button.B_REMOVE, item);
		dialog.zPressButton(Button.B_YES);

		// Verify the message no longer has an attachment
		account.soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
						+		"<query>subject:("+ subject +")</query>"
						+	"</SearchRequest>");
		id = account.soapSelectValue("//mail:m", "id");

		try {

			int i = 0;
			do {
				SleepUtil.sleepSmall();

				account.soapSend(
						"<GetMsgRequest xmlns='urn:zimbraMail' >"
								+   "<m id='"+ id +"'/>"
								+ "</GetMsgRequest>");
				nodes = account.soapSelectNodes("//mail:mp[@cd='attachment']");

			} while ( (i++ < 10) && (nodes.length > 0) );

		} catch(Exception ex) {
			logger.error(ex);
		}

		ZAssert.assertEquals(nodes.length, 0, "Verify the message no longer has the attachment");

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		display.zPressButton(Button.B_DELETE);

		// Verify the message no longer exist in the list
		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem found = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for "+ mail.dSubject +"m  found: "+ m.gSubject);
			if ( mail.dSubject.equals(m.gSubject) ) {
				found = m;
				break;
			}
		}
		ZAssert.assertNull(found, "Verify the message is no longer in the inbox");
	}
}