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
package com.zimbra.qa.selenium.projects.ajax.tests.search.tags;

import java.util.*;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;

public class SelectTag extends SetGroupMailByMessagePreference {

	public SelectTag() {
		logger.info("New "+ SelectTag.class.getCanonicalName());

		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 3685575017990609879L; {
				put("zimbraPrefGroupMailBy", "message");
			}
		};
	}


	@Test (description = "Left click on tag - verify tagged messages are shown",
			groups = { "functional","L2" })

	public void SelectTag_01() throws HarnessException {

		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		String subject1 = "tagged" + ConfigProperties.getUniqueString();
		String subject2 = "untagged" + ConfigProperties.getUniqueString();

		// Create a tag
		TagItem tag = TagItem.CreateUsingSoap(app.zGetActiveAccount());
		ZAssert.assertNotNull(tag, "Verify the tag was created");

		// Create a message (tagged)
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>" +
					"<m t='"+ tag.getId() +"' l='"+ inbox.getId() + "'>" +
						"<content>MIME-Version: 1.0 \n" +
							"From: foo@foo.com\n" +
							"To: foo@foo.com \n" +
							"Subject: " + subject1 + "\n" +
							"Content-Type: text/plain; charset=utf-8 \n" +
							"Content-Transfer-Encoding: 7bit\n" + "\n" +
							"simple text string in the body\n" +
						"</content>" +
					"</m>" + "</AddMsgRequest>");

		// Tag it

		// Create another message (un-tagged)
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>" +
					"<m l='"+ inbox.getId() + "'>" +
						"<content>MIME-Version: 1.0 \n" +
							"From: foo@foo.com\n" +
							"To: foo@foo.com \n" +
							"Subject: " + subject2 + "\n" +
							"Content-Type: text/plain; charset=utf-8 \n" +
							"Content-Transfer-Encoding: 7bit\n" + "\n" +
							"simple text string in the body\n" +
						"</content>" +
					"</m>" + "</AddMsgRequest>");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Click on the tag from the tree
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, tag);

		boolean found = false;
		List<MailItem> messages = app.zPageMail.zListGetMessages();

		// Verify the tagged message shows
		for (MailItem m : messages) {
			if ( subject1.equals(m.gSubject) ) {
				found = true;
				break;
			}
		}
		ZAssert.assertTrue(found, "Verify the tagged message appears");

		// Verify the un-tagged message does not show
		found = false;
		for (MailItem m : messages) {
			if ( subject2.equals(m.gSubject) ) {
				found = true;
				break;
			}
		}
		ZAssert.assertFalse(found, "Verify the un-tagged message does not appear");
	}
}