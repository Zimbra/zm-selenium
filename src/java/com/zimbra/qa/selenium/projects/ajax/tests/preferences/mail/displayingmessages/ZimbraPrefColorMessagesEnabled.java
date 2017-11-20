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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.displayingmessages;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.items.TagItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogTag;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.PagePreferences;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class ZimbraPrefColorMessagesEnabled extends SetGroupMailByMessagePreference {

	public ZimbraPrefColorMessagesEnabled() {
		super.startingPage = app.zPageMail;
		super.startingAccountPreferences.put("zimbraPrefColorMessagesEnabled", "FALSE");
	}

	@Test(	description = "Verify if the background color of messages is according to the tag color when 'zimbraPrefColorMessagesEnabled' is Set TRUE",
			groups = { "functional", "L2" })

	public void ZimbraPrefColorMessagesEnabled_01() throws HarnessException {

		// Data for messages
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		String subject1 = "subjectX"+ ConfigProperties.getUniqueString();
		String subject2 = "subjectY"+ ConfigProperties.getUniqueString();
		String tagName1 = "tagOrange";
		String tagName2 = "tagBlue";

		// Create a message
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
						+		"<m l='" + inbox.getId() + "'>"
						+			"<content>"
						+				"From: foo@foo.com\n"
						+ 				"To: foo@foo.com \n"
						+				"Subject: " + subject1 + "\n"
						+ 				"MIME-Version: 1.0 \n"
						+				"Content-Type: text/plain; charset=utf-8 \n"
						+				"Content-Transfer-Encoding: 7bit\n"
						+				"\n"
						+				"content \n"
						+				"\n"
						+				"\n"
						+			"</content>"
						+		"</m>"
						+	"</AddMsgRequest>");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject1);

		// Create new tag
		DialogTag dialog = (DialogTag)app.zPageMail.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_TAG);
		dialog.zSetTagName(tagName1);
		dialog.zSubmit();

		TagItem tag = TagItem.importFromSOAP(app.zGetActiveAccount(), tagName1);

		// Tag it
		app.zPageMail.zToolbarPressPulldown(Button.B_TAG, tag);

		// Verify that color of the message is not displayed yet
		ZAssert.assertFalse(app.zPageMail.sIsElementPresent("css=li[style^='background:'][style*='#ffeed5']"),
				"Verify that message is not colored as per tag color when 'zimbraPrefColorMessagesEnabled' is FALSE");

		// Changing the preference zimbraPrefColorMessagesEnabled to TRUE

		// Navigate to Preferences -> General
		startingPage=app.zPagePreferences;
		startingPage.zNavigateTo();

		// Navigate to preferences -> mail
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);

		// Select the check box for Automatic display of External images
		app.zPagePreferences.sClick(PagePreferences.Locators.zDisplayMessageColor);

		// Click save
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		// Verification through SOAP
		app.zGetActiveAccount().soapSend(
				"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
						+			"<pref name='zimbraPrefColorMessagesEnabled'/>"
						+		"</GetPrefsRequest>");

		// Verifying that the 'zimbraPrefColorMessagesEnabled' is set to TRUE
		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefColorMessagesEnabled']", null);
		ZAssert.assertEquals(value, "TRUE", "Verify that show message color is enabled");

		// Verifying the background color of message in inbox

		// Refresh current view
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);

		// Verifying the background color of message now
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=li[style^='background:'][style*='#ffeed5'][aria-label*='" + subject1 + "']"),
				"Verify that message is colored as per tag color when 'zimbraPrefColorMessagesEnabled' is TRUE");

		// Create another message and tag it with another color
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
						+		"<m l='" + inbox.getId() + "'>"
						+			"<content>"
						+				"From: foo@foo.com\n"
						+ 				"To: foo@foo.com \n"
						+				"Subject: " + subject2 + "\n"
						+ 				"MIME-Version: 1.0 \n"
						+				"Content-Type: text/plain; charset=utf-8 \n"
						+				"Content-Transfer-Encoding: 7bit\n"
						+				"\n"
						+				"content \n"
						+				"\n"
						+				"\n"
						+			"</content>"
						+		"</m>"
						+	"</AddMsgRequest>");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject2);

		// Create another tag with different color
		dialog = (DialogTag)app.zPageMail.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_TAG);
		dialog.zSetTagName(tagName2);
		dialog.zSubmit();

		tag = TagItem.importFromSOAP(app.zGetActiveAccount(), tagName2);

		// Tag it
		app.zPageMail.zToolbarPressPulldown(Button.B_TAG, tag);

		// Verifying the background color of message
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=li[style^='background:'][style*='#84b4f5'][aria-label*='" + subject2 + "']"),
				"Verify that message is colored as per tag color when 'zimbraPrefColorMessagesEnabled' is TRUE");

		// Changing the starting page for test below
		super.startingPage = app.zPageMail;
	}


	@Test(	description = "Verify the color of message when it is tagged with two tags and 'zimbraPrefColorMessagesEnabled' is Set TRUE",
			groups = { "functional", "L3" })

	public void ZimbraPrefColorMessagesEnabled_02() throws HarnessException {

		// Data for messages
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		String subject = "subjectZ"+ ConfigProperties.getUniqueString();
		String tagName1 = "tagA";
		String tagName2 = "tagB";

		// Create a message
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
						+		"<m l='" + inbox.getId() + "'>"
						+			"<content>"
						+				"From: foo@foo.com\n"
						+ 				"To: foo@foo.com \n"
						+				"Subject: " + subject + "\n"
						+ 				"MIME-Version: 1.0 \n"
						+				"Content-Type: text/plain; charset=utf-8 \n"
						+				"Content-Transfer-Encoding: 7bit\n"
						+				"\n"
						+				"content \n"
						+				"\n"
						+				"\n"
						+			"</content>"
						+		"</m>"
						+	"</AddMsgRequest>");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Create new tag
		DialogTag dialog = (DialogTag)app.zPageMail.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_TAG);
		dialog.zSetTagName(tagName1);
		dialog.zSubmit();

		TagItem tag = TagItem.importFromSOAP(app.zGetActiveAccount(), tagName1);

		// Tag it
		app.zPageMail.zToolbarPressPulldown(Button.B_TAG, tag);

		// Create another tag with different color
		dialog = (DialogTag)app.zPageMail.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_TAG);
		dialog.zSetTagName(tagName2);
		dialog.zSubmit();

		tag = TagItem.importFromSOAP(app.zGetActiveAccount(), tagName2);

		// Tag it
		app.zPageMail.zToolbarPressPulldown(Button.B_TAG, tag);

		// Verifying the background color of message
		ZAssert.assertFalse(app.zPageMail.sIsElementPresent("css=li[style^='background:'][aria-label*='" + subject + "']"),
				"Verify that message is not colored when it is tagged with two tags");
	}
}
