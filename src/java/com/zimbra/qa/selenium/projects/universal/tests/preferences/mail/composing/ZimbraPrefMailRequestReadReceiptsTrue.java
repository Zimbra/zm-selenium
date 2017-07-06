/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.mail.composing;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;


public class ZimbraPrefMailRequestReadReceiptsTrue extends UniversalCommonTest {

	@SuppressWarnings("serial")
	public ZimbraPrefMailRequestReadReceiptsTrue() {

		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = new HashMap<String, String>() {
			{
				put("zimbraPrefMailRequestReadReceipts", "FALSE");
			}
		};
	}

	@Test(
			description = "Set zimbraPrefMailRequestReadReceipts to 'TRUE'",
			groups = { "functional", "L3" }
	)
	public void ZimbraPrefMailRequestReadReceiptsTrue_01() throws HarnessException {

		
		//-- GUI steps
		//
		
		
		// Navigate to preferences -> mail -> composing
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);

		// Check the box
		app.zPagePreferences.sClick("css=input[id$='_AUTO_READ_RECEIPT_ENABLED'][type='checkbox']");

		// Click save
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		
		//-- VERIFICATION
		//
		
		// Verify the pref
		app.zGetActiveAccount().soapSend(
						"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+			"<pref name='zimbraPrefMailRequestReadReceipts'/>"
				+		"</GetPrefsRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefMailRequestReadReceipts']", null);
		ZAssert.assertEquals(value, "TRUE", "Verify the preference was changed to 'TRUE'");

	}
}
