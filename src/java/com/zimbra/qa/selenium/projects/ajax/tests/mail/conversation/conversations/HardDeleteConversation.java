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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.conversation.conversations;

import org.testng.annotations.*;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByConversationPreference;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning;

public class HardDeleteConversation extends SetGroupMailByConversationPreference {

	public HardDeleteConversation() {
		logger.info("New "+ HardDeleteConversation.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox", "TRUE");
	}


	@Bugs (ids = "53564")
	@Test (description = "Hard-delete a mail by selecting and typing 'shift-del' shortcut",
			groups = { "sanity" } )

	public void HardDeleteConversation_01() throws HarnessException {

		// Create the message data to be sent
		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(c.getSubject()), "Verify message displayed in current view");

		// Check the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, c.getSubject());

		// Click shift-delete
		DialogWarning dialog = (DialogWarning)app.zPageMail.zKeyboardShortcut(Shortcut.S_MAIL_HARDELETE);
		dialog.zPressButton(Button.B_OK);

		// Verify the message is no longer in the mailbox
		app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='conversation'>"
				+		"<query>subject:("+ c.getSubject() +") is:anywhere</query>"
				+	"</SearchRequest>");

		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:c");
		ZAssert.assertEquals(nodes.length, 0, "Verify the converastion is no longer in the mailbox");
	}


	@Bugs (ids = "ZCS-3672")
	@Test (description = "Hard-delete multiple messages (3) by selecting and typing 'shift-del' shortcut",
			groups = { "sanity-application-bug" })

	public void HardDeleteConversation_02() throws HarnessException {

		// Create the message data to be sent
		ConversationItem c1 = ConversationItem.createConversationItem(app.zGetActiveAccount());
		ConversationItem c2 = ConversationItem.createConversationItem(app.zGetActiveAccount());
		ConversationItem c3 = ConversationItem.createConversationItem(app.zGetActiveAccount());

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select all three items
		app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, c1.getSubject());
		app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, c2.getSubject());
		app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, c3.getSubject());

		DialogWarning dialog = (DialogWarning)app.zPageMail.zKeyboardShortcut(Shortcut.S_MAIL_HARDELETE);
		dialog.zPressButton(Button.B_OK);

		// Verify the message is no longer in the mailbox
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='conversation'>"
			+		"<query>subject:("+ c1.getSubject() +") is:anywhere</query>"
			+	"</SearchRequest>");

		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:c");
		ZAssert.assertEquals(nodes.length, 0, "Verify the conversation (subject1) is no longer in the mailbox");

		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='conversation'>"
				+		"<query>subject:("+ c2.getSubject() +") is:anywhere</query>"
				+	"</SearchRequest>");

		nodes = app.zGetActiveAccount().soapSelectNodes("//mail:c");
		ZAssert.assertEquals(nodes.length, 0, "Verify the conversation (subject2) is no longer in the mailbox");

		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='conversation'>"
				+		"<query>subject:("+ c3.getSubject() +") is:anywhere</query>"
				+	"</SearchRequest>");

		nodes = app.zGetActiveAccount().soapSelectNodes("//mail:c");
		ZAssert.assertEquals(nodes.length, 0, "Verify the conversation (subject2) is no longer in the mailbox");
	}
}