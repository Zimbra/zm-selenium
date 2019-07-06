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
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.chat.BuddyItem;
import com.zimbra.qa.selenium.projects.ajax.pages.chat.WizardAddBuddy;
import com.zimbra.qa.selenium.projects.ajax.pages.chat.PageChatPanel.Userstatus;

public class AddBuddy extends AjaxCore {

	public AddBuddy() {
		logger.info("New "+ AddBuddy.class.getCanonicalName());
		super.startingPage = app.zPageChatPanel;
	}


	@Test (description = "Add new buddy",
			groups = { "smoke", "chat" })

	public void AddNewBuddy_01() throws HarnessException {

		BuddyItem item = new BuddyItem();
		item.setEmailAddress(ZimbraAccount.Account1().EmailAddress);
		app.zPageChatPanel.zNavigateTo();

		WizardAddBuddy wizard = (WizardAddBuddy) app.zPageChatPanel.zEllipsesOption(Button.B_ADD_NEW_BUDDY);
		wizard.zCompleteWizard(item);

		ZimbraAccount account = app.zGetActiveAccount();
		ZAssert.assertStringContains(app.zPageChatPanel.zUserStatus(ZimbraAccount.Account1().EmailAddress),
				Userstatus.invited.getStatus(), "Verify the user status");

		app.zPageLogin.zNavigateTo();
		app.zPageLogin.zLogin(ZimbraAccount.Account1());
		app.zPageChatPanel.zNavigateTo();

		ZAssert.assertStringContains(app.zPageChatPanel.zUserStatus(account.EmailAddress),
				Userstatus.need_response.getStatus(), "Verify the user status");
		ZAssert.assertStringContains(app.zPageChatPanel.zUserStatus(account.EmailAddress),
				Userstatus.need_response.getStatus(), "Verify the user status");
		app.zPageChatPanel.zSelectUser(account.EmailAddress, Userstatus.need_response);

	}
}