package com.zimbra.qa.selenium.projects.universal.tests.mail.conversation.conversationview;

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

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByConversationTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.PageMail.Locators;

public class CheckScriptErrorWhenChangingView extends PrefGroupMailByConversationTest {

	public CheckScriptErrorWhenChangingView() {
		logger.info("New "+ CheckScriptErrorWhenChangingView.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox", "TRUE");
	}
	
	@Bugs( ids = "102767")
	@Test( description = "Script Error: this._listView[this._currentViewId].getLimit is not a function while clicking in View >> Message View",
	groups = { "functional", "L2" })
	
	public void CheckScriptErrorWhenChangingView_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject"+ ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		app.zPageMail.sDoubleClick("css=ul[id='zl__CLV-main__rows'] li:nth-of-type(1)");

		app.zPageMail.sClickAt(Locators.zViewMenuDropdownBtnConvID, "");

		ZAssert.assertFalse(app.zPageMail.sIsElementPresent(Locators.zByConvViewConvID), "By Conversation view should not present");
		ZAssert.assertFalse(app.zPageMail.sIsElementPresent(Locators.zByMsgViewConvID), "By Message view should not present");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zByCONVORDERwConvID), "By Conv order should be present");

	}

}

