/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.outofoffice;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class ZimbraPrefOutOfOfficeReplyEnabledTrue extends AjaxCommonTest {

	public static final String autoReplyMessage = "OOO" + ConfigProperties.getUniqueString();
	public ZimbraPrefOutOfOfficeReplyEnabledTrue() throws HarnessException {

		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -3101848474022410670L; {
				put("zimbraPrefOutOfOfficeReplyEnabled", "FALSE");
				put("zimbraPrefOutOfOfficeStatusAlertOnLogin", "FALSE");
			}
		};
	}


	@Bugs(ids = "101356")
	@Test(description = "Enable out of office", priority = 4,
			groups = { "smoke", "L1"  })

	public void ZimbraPrefOutOfOfficeReplyEnabledTrue_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();
		String subject = "subject" + ConfigProperties.getUniqueString();

		// Navigate to preferences -> mail -> Out of Office
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailOutOfOffice);

		// Enable the preferences
		app.zPagePreferences.sClick("css=input[id$='VACATION_MSG_ENABLED_input']");

		// Add a message
		app.zPagePreferences.sType("css=textarea[id$='_VACATION_MSG']", autoReplyMessage);

		// Click "Save"
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		app.zGetActiveAccount().soapSend("<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+ "<pref name='zimbraPrefOutOfOfficeReplyEnabled'/>" + "<pref name='zimbraPrefOutOfOfficeReply'/>"
				+ "<pref name='zimbraPrefOutOfOfficeStatusAlertOnLogin'/>" + "</GetPrefsRequest>");

		String zimbraPrefOutOfOfficeReplyEnabled = app.zGetActiveAccount()
				.soapSelectValue("//acct:pref[@name='zimbraPrefOutOfOfficeReplyEnabled']", null);
		String zimbraPrefOutOfOfficeReply = app.zGetActiveAccount()
				.soapSelectValue("//acct:pref[@name='zimbraPrefOutOfOfficeReply']", null);
		String zimbraPrefOutOfOfficeStatusAlertOnLogin = app.zGetActiveAccount()
				.soapSelectValue("//acct:pref[@name='zimbraPrefOutOfOfficeStatusAlertOnLogin']", null);

		ZAssert.assertEquals(zimbraPrefOutOfOfficeReplyEnabled, "TRUE",
				"Verify zimbraPrefOutOfOfficeReplyEnabled is TRUE");
		ZAssert.assertEquals(zimbraPrefOutOfOfficeReply, autoReplyMessage,
				"Verify zimbraPrefOutOfOfficeReply contains the message");
		ZAssert.assertEquals(zimbraPrefOutOfOfficeStatusAlertOnLogin, "TRUE",
				"Verify zimbraPrefOutOfOfficeStatusAlertOnLogin is TRUE");

		// Send message to self
		account.soapSend("<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>" + "<e t='t' a='" + account.EmailAddress
				+ "'/>" + "<su>" + subject + "</su>" + "<mp ct='text/plain'>" + "<content>content"
				+ ConfigProperties.getUniqueString() + "</content>" + "</mp>" + "</m>" + "</SendMsgRequest>");

		MailItem mailItem = MailItem.importFromSOAP(account, "in:inbox " + autoReplyMessage);
		ZAssert.assertTrue(mailItem.dBodyText.contains(autoReplyMessage), "Verify auto-reply message is received");
	}

}