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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.attributes;

import java.util.*;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.RecipientItem.RecipientType;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;

public class IncludeTrashInSearchTrue extends SetGroupMailByMessagePreference {

	public IncludeTrashInSearchTrue() {
		logger.info("New "+ IncludeTrashInSearchTrue.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefIncludeTrashInSearch", "TRUE");
	}


	@Test (description = "Verify when zimbraPrefIncludeTrashInSearch=TRUE, that trash is included in search",
			groups = { "functional", "L2" })

	public void IncludeTrashInSearchTrue_01() throws HarnessException {

		String query = "query" + ConfigProperties.getUniqueString();
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox);
		FolderItem trashFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Trash);

		MailItem message1 = new MailItem();
		message1.dSubject = "subject" + ConfigProperties.getUniqueString();
		message1.dFromRecipient = new RecipientItem(
				"globaladmin" + "@" + ConfigProperties.getStringProperty("testdomain"), RecipientType.From);
		message1.dToRecipients.add(new RecipientItem(
				"globaladmin" + "@" + ConfigProperties.getStringProperty("testdomain"), RecipientType.To));
		message1.dBodyText = query;

		MailItem message2 = new MailItem();
		message2.dSubject = "subject" + ConfigProperties.getUniqueString();
		message2.dFromRecipient = new RecipientItem(
				"globaladmin" + "@" + ConfigProperties.getStringProperty("testdomain"), RecipientType.From);
		message2.dToRecipients.add(new RecipientItem(
				"globaladmin" + "@" + ConfigProperties.getStringProperty("testdomain"), RecipientType.To));
		message2.dBodyText = query;

		// Add a message to the inbox
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>" +
                	"<m l='"+ inboxFolder.getId() +"'>" +
                    	"<content>" + message1.generateMimeString() + "</content>" +
                    "</m>" +
                "</AddMsgRequest>");

		// Add a message to the trash
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>" +
                	"<m l='"+ trashFolder.getId() +"'>" +
                    	"<content>" + message2.generateMimeString() + "</content>" +
                    "</m>" +
                "</AddMsgRequest>");

		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Search for the query
		app.zPageSearch.zAddSearchQuery(query);
		app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);

		// Verify that both messages are in the list
		List<MailItem> items = app.zPageSearch.zListGetMessages();

		boolean found1 = false;
		boolean found2 = false;
		for (MailItem c : items) {
			if ( message1.dSubject.equals(c.gSubject) ) {
				found1 = true;
				break;
			}
		}
		for (MailItem c : items) {
			if ( message2.dSubject.equals(c.gSubject) ) {
				found2 = true;
				break;
			}
		}

		ZAssert.assertTrue(found1, "Verify the message in the inbox is found");
		ZAssert.assertTrue(found2, "Verify the message in the trash is found");
	}
}