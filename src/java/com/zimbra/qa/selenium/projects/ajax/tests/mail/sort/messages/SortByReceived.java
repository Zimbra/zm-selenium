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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.sort.messages;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;

public class SortByReceived extends SetGroupMailByMessagePreference {

	public SortByReceived() {
		logger.info("New "+ SortByReceived.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefReadingPaneLocation", "bottom");
	}


	@Test (description = "Sort a list of messages by received (oldest -> newest)",
			groups = { "functional", "L2" })

	public void SortByReceived_01() throws HarnessException {

		// Create the message data
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		String subjectA = "subjectA" + ConfigProperties.getUniqueString();
		String subjectB = "subjectB" + ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>"
				+			"<su>"+ subjectA +"</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>simple text</content>"
				+			"</mp>"
				+		"</m>"
				+	"</SendMsgRequest>");

		SleepUtil.sleep(5000);

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>"
			+		"<m>"
			+			"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>"
			+			"<su>"+ subjectB +"</su>"
			+			"<mp ct='text/plain'>"
			+				"<content>simple text</content>"
			+			"</mp>"
			+		"</m>"
			+	"</SendMsgRequest>");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Click on Inbox
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);

		// First, sort by subject to clear the order
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_LIST_SORTBY_FLAGGED);

		// Now, click on "subject"
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_LIST_SORTBY_RECEIVED);

		// Get all the messages in the inbox
		app.zGetActiveAccount().soapSend(
				"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
    		+		"<pref name='zimbraPrefSortOrder'/>"
			+	"</GetPrefsRequest>");

		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem itemB = null;
		for (MailItem m : messages) {
			if ( subjectB.equals(m.gSubject) ) {
				itemB = m;
			}
			if ( subjectA.equals(m.gSubject) ) {
				ZAssert.assertNotNull(itemB, "Item A is in the list.  Verify Item B has already been found.");
			}
		}

		ZAssert.assertNotNull(itemB, "Verify Item B was found.");
	}


	@Test (description = "Sort a list of messages by received (newest -> oldest)",
			groups = { "functional", "L2" })

	public void SortByReceived_02() throws HarnessException {

		// Create the message data
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		String subjectA = "subjectA" + ConfigProperties.getUniqueString();
		String subjectB = "subjectB" + ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>"
			+		"<m>"
			+			"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>"
			+			"<su>"+ subjectA +"</su>"
			+			"<mp ct='text/plain'>"
			+				"<content>simple text</content>"
			+			"</mp>"
			+		"</m>"
			+	"</SendMsgRequest>");

	SleepUtil.sleep(5000);

	ZimbraAccount.AccountA().soapSend(
			"<SendMsgRequest xmlns='urn:zimbraMail'>"
		+		"<m>"
		+			"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>"
		+			"<su>"+ subjectB +"</su>"
		+			"<mp ct='text/plain'>"
		+				"<content>simple text</content>"
		+			"</mp>"
		+		"</m>"
		+	"</SendMsgRequest>");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Click on Inbox
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);

		// First, sort by subject to clear the order
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_LIST_SORTBY_FLAGGED);

		// Now, click on "subject"
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_LIST_SORTBY_RECEIVED);

		// Now, click on "subject" to reverse the order
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_LIST_SORTBY_RECEIVED);

		// Get all the messages in the inbox
		app.zGetActiveAccount().soapSend(
				"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
    		+		"<pref name='zimbraPrefSortOrder'/>"
			+	"</GetPrefsRequest>");

		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem itemA = null;
		for (MailItem m : messages) {
			if ( subjectA.equals(m.gSubject) ) {
				itemA = m;
			}
			if ( subjectB.equals(m.gSubject) ) {
				ZAssert.assertNotNull(itemA, "Item B is in the list.  Verify Item A has already been found.");
			}
		}

		ZAssert.assertNotNull(itemA, "Verify Item A was found.");
	}
}