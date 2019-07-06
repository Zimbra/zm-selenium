/*

 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.readingpane;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByConversationPreference;
import com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.drafts.OpenDraftMail;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.PageMail.Locators;

public class VerifyReadingPaneOptions extends SetGroupMailByConversationPreference {

	public VerifyReadingPaneOptions() {
		logger.info("New " + OpenDraftMail.class.getCanonicalName());
	}


	@Bugs (ids = "91533")
	@Test (description = "Verify reading pane options in message view (bottom, right, off)",
			groups = { "sanity" })

	public void VerifyReadingPaneOptionsInMessageView_01() throws HarnessException {

		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_BY_MESSAGE);
		SleepUtil.sleepVerySmall();
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_READING_PANE_RIGHT);
		SleepUtil.sleepVerySmall();
		ZAssert.assertTrue(app.zPageMail.sIsVisible(Locators.zRightReadingPaneSeparatorBar), "Verify horizontal seperator is visible");
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zBottomReadingPaneSeparatorBar), "Verify vertical seperator is not visible");

		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_READING_PANE_BOTTOM);
		SleepUtil.sleepVerySmall();
		ZAssert.assertTrue(app.zPageMail.sIsVisible(Locators.zBottomReadingPaneSeparatorBar), "Verify vertical seperator is visible");
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zRightReadingPaneSeparatorBar), "Verify horizontal seperator is not visible");

		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_READING_PANE_OFF);
		SleepUtil.sleepVerySmall();
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zBottomReadingPaneSeparatorBar), "Verify vertical seperator is not visible");
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zRightReadingPaneSeparatorBar), "Verify horizontal seperator is not visible");
	}


	@Bugs (ids = "91533")
	@Test (description = "Verify reading pane options in conversation view (bottom, right, off)",
			groups = { "sanity" })

	public void VerifyReadingPaneOptionsInConversationView_02() throws HarnessException {

		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(),SystemFolder.Inbox);
		String subject = "subject"+ ConfigProperties.getUniqueString();

		// Send a message to the account(self)
		ZimbraAccount.AccountZCS().soapSend(
						"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ConfigProperties.getUniqueString() + "</content>" +
						"</mp>" +
						"</m>" +
						"</SendMsgRequest>");

		// Get the mail item for the new message
		MailItem mail = MailItem.importFromSOAP(ZimbraAccount.AccountZCS(),"in:inbox subject:(" + subject + ")");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);
		SleepUtil.sleepSmall();

		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_READING_PANE_RIGHT);
		SleepUtil.sleepVerySmall();
		ZAssert.assertTrue(app.zPageMail.sIsVisible(Locators.zRightReadingPaneSeparatorBar), "Verify horizontal seperator is visible");
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zBottomReadingPaneSeparatorBar), "Verify vertical appsash bar is not visible");

		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_READING_PANE_BOTTOM);
		SleepUtil.sleepVerySmall();
		ZAssert.assertTrue(app.zPageMail.sIsVisible(Locators.zBottomReadingPaneSeparatorBar), "Verify horizontal appSash bar is visible");
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zRightReadingPaneSeparatorBar), "Issues in Bottom reading pane");

		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_READING_PANE_OFF);
		SleepUtil.sleepVerySmall();
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zBottomReadingPaneSeparatorBar), "Issues in reading pane off option");
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zRightReadingPaneSeparatorBar), "Issues in reading pane off option");
	}
}