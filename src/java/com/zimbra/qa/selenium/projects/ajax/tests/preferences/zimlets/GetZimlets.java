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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.zimlets;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class GetZimlets extends AjaxCore {

	public GetZimlets() {
		super.startingPage = app.zPagePreferences;
	}


	@Test (description = "Verify the Phone table, Search Highlighter, WebEx and Y-Emoticons zimlet description",
			groups = { "functional" })

	public void GetZimlets_01() throws HarnessException {

		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Zimlets);

		// The locator to the table
		String locator = "css=div[id='ZmPrefZimletListView'] div[id$='__rows']";

		String phoneName = app.zPagePreferences.sGetText(locator + " td[id$='__com_zimbra_phone__na']");
		String phoneDescription = app.zPagePreferences.sGetText(locator + " td[id$='__com_zimbra_phone__ds']");
		ZAssert.assertEquals(phoneName, "Phone", "Verify the Phone entry exists");
		ZAssert.assertEquals(phoneDescription, "Highlights phone numbers to enable Skype calls.", "Verify the Phone description");

		String searchName = app.zPagePreferences.sGetText(locator + " td[id$='__com_zimbra_srchhighlighter__na']");
		String searchDescription = app.zPagePreferences.sGetText(locator + " td[id$='__com_zimbra_srchhighlighter__ds']");
		ZAssert.assertEquals(searchName, "Search Highlighter", "Verify the Search Highlighter entry exists");
		ZAssert.assertEquals(searchDescription, "After a mail search, this Zimlet highlights Search terms with yellow color.", "Verify the Search Highlighter description");

		String webexName = app.zPagePreferences.sGetText(locator + " td[id$='__com_zimbra_webex__na']");
		String webexDescription = app.zPagePreferences.sGetText(locator + " td[id$='__com_zimbra_webex__ds']");
		ZAssert.assertEquals(webexName, "WebEx", "Verify the WebEx entry exists");
		ZAssert.assertEquals(webexDescription, "Easily schedule, start or join WebEx meetings", "Verify the WebEx description");

		String yemoticonName = app.zPagePreferences.sGetText(locator + " td[id$='__com_zimbra_ymemoticons__na']");
		String yemoticonDescription = app.zPagePreferences.sGetText(locator + " td[id$='__com_zimbra_ymemoticons__ds']");
		ZAssert.assertEquals(yemoticonName, "Yahoo! Emoticons", "Verify the Y Emoticons entry exists");
		ZAssert.assertEquals(yemoticonDescription, "Displays Yahoo! Emoticons images in email messages.", "Verify the Y Emoticons description");
	}
}