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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.general.searches;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;


public class ZimbraPrefIncludeSpamInSearchTrue extends UniversalCommonTest {

	public ZimbraPrefIncludeSpamInSearchTrue() {
		logger.info("New "+ ZimbraPrefIncludeSpamInSearchTrue.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPagePreferences;

		// Make sure we are using an account with conversation view
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 2146617175771551998L;
		{
				    put("zimbraPrefIncludeSpamInSearch", "FALSE");
				}};
			
		
	}
	
	@Test( description = "Change zimbraPrefIncludeSpamInSearch setting TRUE",
			groups = { "functional", "L2" })
	public void PreferencesGeneralSearches_zimbraPrefIncludeSpamInSearch_01() throws HarnessException {
		
		//-- SETUP
		
		
		//-- GUI
		
		// Go to "General"
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.General);
		
		// Check the box
		String locator = "css=input[id$=_SEARCH_INCLUDES_SPAM]";
		app.zPagePreferences.zCheckboxSet(locator, true);
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
		
		
		//-- Verification
		
		// Verify the account preference has been modified
		
		app.zGetActiveAccount().soapSend(
                "<GetPrefsRequest xmlns='urn:zimbraAccount'>"
              +     "<pref name='zimbraPrefIncludeSpamInSearch'/>"
              + "</GetPrefsRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefIncludeSpamInSearch']", null);
		ZAssert.assertEquals(value, "TRUE", "Verify the zimbraPrefIncludeSpamInSearch preference was changed to 'TRUE'");
		
		
	}



}
