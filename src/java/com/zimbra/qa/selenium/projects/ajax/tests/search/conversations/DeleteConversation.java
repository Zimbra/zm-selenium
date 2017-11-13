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
package com.zimbra.qa.selenium.projects.ajax.tests.search.conversations;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByConversationTest;

public class DeleteConversation extends PrefGroupMailByConversationTest {

	public DeleteConversation() {
		logger.info("New "+ DeleteConversation.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox", "TRUE");
	}


	@Bugs (ids = "81074")
	@Test (description = "From search: Delete a conversation using the Delete Toolbar button",
			groups = { "functional","L2" })

	public void DeleteConversation_01() throws HarnessException {

		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);

		// Create the message data to be sent
		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		try {

			// Search for the message
			app.zPageSearch.zAddSearchQuery("subject:("+ c.getSubject() +")");
			app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);

			// Select the item
			app.zPageSearch.zListItem(Action.A_LEFTCLICK, c.getSubject());

			// Click delete
			app.zPageSearch.zToolbarPressButton(Button.B_DELETE);

		} finally {
			app.zPageSearch.zClose();
		}

		MailItem message = MailItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere from:("+ ZimbraAccount.AccountA().EmailAddress + ") subject:("+ c.getSubject() +")");
		ZAssert.assertNotNull(message, "Verify the message still exists in the mailbox");
		ZAssert.assertEquals(message.dFolderId, trash.getId(), "Verify the message exists in the trash");
	}
}