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
package com.zimbra.qa.selenium.projects.ajax.tests.search.savedsearch;

import java.util.*;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;

public class RunSavedSearch extends AjaxCore  {

	@SuppressWarnings("serial")
	public RunSavedSearch() {
		logger.info("New "+ RunSavedSearch.class.getCanonicalName());

		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {{
			put("zimbraPrefGroupMailBy", "message");
		}};
	}


	@Test (description = "Run a saved search",
			groups = { "bhr" })

	public void RunSavedSearch_01() throws HarnessException {

		// Create the message data to be sent
		String name = "search" + ConfigProperties.getUniqueString();
		String subject1 = "subject" + ConfigProperties.getUniqueString();
		String subject2 = "subject" + ConfigProperties.getUniqueString();
		String query = "subject:(" + subject1 + ")";

		// Send two messages with different subjects to the account
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject1 +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content1"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject2 +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content1"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Create the saved search
		app.zGetActiveAccount().soapSend(
				"<CreateSearchFolderRequest xmlns='urn:zimbraMail'>" +
					"<search name='"+ name +"' query='"+ query +"' l='1' types='message'/>" +
				"</CreateSearchFolderRequest>");

		// Get the item
		SavedSearchFolderItem item = SavedSearchFolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Left click on the search
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, item);

		// Verify the correct messages appear
		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem found1 = null;
		MailItem found2 = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for "+ subject1 +" found: "+ m.gSubject);
			if ( subject1.equals(m.gSubject) ) {
				found1 = m;
				break;
			}
			logger.info("Subject: looking for "+ subject2 +" found: "+ m.gSubject);
			if ( subject2.equals(m.gSubject) ) {
				found2 = m;
				break;
			}
		}
		ZAssert.assertNotNull(found1, "Verify the matched message exists in the inbox");
		ZAssert.assertNull(found2, "Verify the un-match message does not exist in the inbox");
	}
}