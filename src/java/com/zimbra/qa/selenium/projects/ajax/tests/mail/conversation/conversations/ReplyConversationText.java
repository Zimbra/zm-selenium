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
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.*;

public class ReplyConversationText extends PrefGroupMailByConversationTest {

	public ReplyConversationText() {
		logger.info("New "+ ReplyConversationText.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}

	@Test( description = "Reply to a conversation",
			groups = { "smoke", "L1" })

	public void ReplyConversationText_01() throws HarnessException {

		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(c.getSubject()), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, c.getSubject());

		// Click reply
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Send the message
		mailform.zSubmit();

		// From the test account, check the sent folder for the reply
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ c.getSubject() +")");
		ZAssert.assertNotNull(sent, "Verify the sent message in the sent folder");
	}
}