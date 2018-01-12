/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.zextras.chat;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.chat.BuddyItem;
import com.zimbra.qa.selenium.projects.ajax.pages.chat.WizardAddBuddy;
import com.zimbra.qa.selenium.projects.ajax.pages.chat.PageChatPanel.Userstatus;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;

public class ChatWithUser extends AjaxCore {

	public ChatWithUser() {
		logger.info("New "+ ChatWithUser.class.getCanonicalName());
		super.startingPage = app.zPageChatPanel;
	}


	@Test (description = "Chat with online user after adding buddy",
			groups = { "sanity", "L0", "chat" })

	public void ChatWithOnlineUser_01() throws HarnessException {

		BuddyItem item = new BuddyItem();
		item.setEmailAddress(ZimbraAccount.Account2().EmailAddress);
		app.zPageChatPanel.zNavigateTo();

		WizardAddBuddy wizard = (WizardAddBuddy)app.zPageChatPanel.zEllipsesOption(Button.B_ADD_NEW_BUDDY);
		wizard.zCompleteWizard(item);

		ZimbraAccount account = app.zGetActiveAccount();
		app.zPageLogin.zNavigateTo();
		app.zPageLogin.zLogin(ZimbraAccount.Account2());
		app.zPageChatPanel.zNavigateTo();
		app.zPageChatPanel.zSelectUser(account.EmailAddress, Userstatus.need_response);

		account.soapSend(
				"<ZxChatRequest xmlns='urn:zimbraAccount'>"
						+ "<type>1</type>"
						+ "<from>"+account.EmailAddress+"</from>"
						+ "<to>"+ZimbraAccount.Account2().EmailAddress+"</to>"
						+ "<message>Test</message>"
						+ "<message_type>chat</message_type>"
						+ "<action>send_message</action>"
						+ "</ZxChatRequest>");

		app.zPageChatPanel.zSelectUser(account.EmailAddress, Userstatus.offline);
		ZAssert.assertStringContains(app.zPageChatPanel.zGetMsg(), "Test", "Verify the chat message");
	}


	@Test (description = "Chat with offline user after adding buddy",
			groups = { "smoke", "L1", "chat" })

	public void ChatWithOfflineUser_02() throws HarnessException {

		String message = "Chat" + ConfigProperties.getUniqueString();
		BuddyItem item = new BuddyItem();
		item.setEmailAddress(ZimbraAccount.Account3().EmailAddress);
		app.zPageChatPanel.zNavigateTo();

		WizardAddBuddy wizard = (WizardAddBuddy)app.zPageChatPanel.zEllipsesOption(Button.B_ADD_NEW_BUDDY);
		wizard.zCompleteWizard(item);

		ZimbraAccount account = app.zGetActiveAccount();

		app.zPageLogin.zNavigateTo();
		app.zPageLogin.zLogin(ZimbraAccount.Account3());
		app.zPageChatPanel.zNavigateTo();

		app.zPageChatPanel.zSelectUser(account.EmailAddress, Userstatus.need_response);
		app.zPageChatPanel.zSelectUser(account.EmailAddress, Userstatus.offline);
		app.zPageChatPanel.zSendMsg(message);

		app.zPageLogin.zNavigateTo();
		app.zPageLogin.zLogin(account);
		app.zPageChatPanel.zNavigateTo();
		ZAssert.assertTrue(app.zPageChatPanel.zVerifyChatFolder(),"Verify the chat folder");

		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK,
				"OpenChat - " + ZimbraAccount.Account3().EmailAddress);
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body),message,"Verify the Open Mail existence");
	}
}