/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.general.login;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class ZimbraPrefSkinSerenity extends AjaxCommonTest {

	public ZimbraPrefSkinSerenity() {
		logger.info("New "+ ZimbraPrefSkinSerenity.class.getCanonicalName());

		// All tests start at the login page
		super.startingPage = app.zPagePreferences;

		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -1303088148746653112L;
			{

				put("zimbraPrefSkin", "serenity");

			}
		};

	}
	
	@Test(
			description = "Verify the 'zimbraPrefSkin' option can be changed", 
			groups = { "functional" }
			)
	public void ZimbraPrefSkinSerenity_01() throws HarnessException {
				
		// Go to "General"
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.General);
				
		// Click on the "Bare" theme
		String locator = "css=td[id='bare_2_title']";
	
		ZAssert.assertTrue(
				app.zPagePreferences.sIsElementPresent(locator), 
				"Verify the 'Bare' theme selection is present");
	
		app.zPagePreferences.zClick(locator);
		app.zPagePreferences.zWaitForBusyOverlay();
		
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
		
		app.zGetActiveAccount().soapSend(
						"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+			"<pref name='zimbraPrefSkin'/>"
				+		"</GetPrefsRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefSkin']", null);
		ZAssert.assertEquals(value, "bare", "Verify the zimbraPrefSkin preference was saved");

	}
}