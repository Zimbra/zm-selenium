/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.conversation.conversationview;

import java.awt.event.KeyEvent;
import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.ConversationItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByConversationPreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;

public class ExpandConversation extends SetGroupMailByConversationPreference {

	public ExpandConversation() {
		logger.info("New "+ ExpandConversation.class.getCanonicalName());
	}


	@Test (description = "Select conversation having html content message",
			groups = { "smoke" })

	public void ExpandConversation_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String fragment = "fragment" + ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='" + app.zGetActiveAccount().EmailAddress + "'/>" +
						"<su>" + subject + "</su>" +
						"<mp ct='text/html'>" +
							"<content>" + fragment + "</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Reply to the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
		mailform.zKeyboard.zTypeCharacters("Line 1");
		mailform.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		mailform.zKeyboard.zTypeCharacters("Line 2");
		mailform.zSubmit();

		// Select conversation
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		ZAssert.assertGreaterThanEqualTo(Integer.parseInt(actual.zGetMailProperty(Field.ReadingPaneHeight)), 50, "Verify height of iframe from reading pane");

		// Verify the list shows: 1 conversation with 2 messages
		List<MailItem> items = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(items, "Verify the conversation list exists");

		boolean found = false;
		for (MailItem c : items) {
			logger.info("Subject: looking for " + subject + " found: " + c.gSubject);
			if ( subject.equals(c.gSubject) ) {
				found = true;
				break;
			}
		}
		ZAssert.assertTrue(found, "Verify the conversation is in the inbox");
	}


	@Test (description = "Expand conversation having plain text messages",
			groups = { "smoke" })

	public void ExpandConversation_02() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String fragment1 = "fragment1" + ConfigProperties.getUniqueString();
		String fragment2 = "fragment2" + ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='" + app.zGetActiveAccount().EmailAddress + "'/>" +
						"<su>" + subject + "</su>" +
						"<mp ct='text/plain'>" +
							"<content>" + fragment1 + "</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='" + app.zGetActiveAccount().EmailAddress + "'/>" +
						"<su>RE: " + subject + "</su>" +
						"<mp ct='text/plain'>" +
							"<content>" + fragment2 + "</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Expand the item
		app.zPageMail.zListItem(Action.A_MAIL_EXPANDCONVERSATION, subject);

		// Verify the list shows: 1 conversation with 2 messages
		List<MailItem> items = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(items, "Verify the conversation list exists");

		boolean found = false;
		for (MailItem c : items) {
			logger.info("Subject: looking for " + subject + " found: " + c.gSubject);
			if ( subject.equals(c.gSubject) ) {
				found = true;
				break;
			}
		}
		ZAssert.assertTrue(found, "Verify the conversation is in the inbox");

		int count = 0;
		for (MailItem m : items) {
			logger.info("Subject: looking for " + fragment1 + " or " + fragment2 + " found: " + m.gFragment);

			if ( m instanceof ConversationItem ) {
				ConversationItem c = (ConversationItem)m;

				if ( !c.gIsConvExpanded ) {
					// Not a conversation member
					continue;
				}

				if ( fragment1.equals(c.gFragment) ) {
					logger.info("Subject: Found " + fragment1);
					count++;
				}
				if ( fragment2.equals(c.gFragment) ) {
					logger.info("Subject: Found " + fragment2);
					count++;
				}
			}
		}
		ZAssert.assertEquals(count, 2, "Verify two messages in the conversation");
	}
}