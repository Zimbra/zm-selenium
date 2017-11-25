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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.importexport;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class ExportAccount extends AjaxCore {

	public ExportAccount() {
		super.startingPage = app.zPagePreferences;
	}


	@Test (description = "Verify clicking on 'export account' radio button",
			groups = { "functional", "L2" })

	public void ExportAccount_01() throws HarnessException {

		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.ImportExport);

		String locator = "css=div[id$='_TYPE_TGZ_control'] input[type='radio']";
		app.zPagePreferences.sFocus(locator);
		app.zPagePreferences.sClick(locator);
		app.zPagePreferences.zWaitForBusyOverlay();

		// Verify the help hint
		String hint = app.zPagePreferences.sGetText("css=div[id$='_TYPE_HINT']");
		ZAssert.assertStringContains(hint, "All account data can be exported", "Verify help hint text changed");
	}


	@Test (description = "Verify clicking on 'Advanced Settings' checkbox",
			groups = { "functional", "L3" })

	public void ExportAccount_02() throws HarnessException {

		String locator;

		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.ImportExport);

		// Make sure the fields are invisible first
		locator = "css=tr[id='ZmExportView_DATA_TYPES_row'][style='display: table-row;']";
		ZAssert.assertFalse(app.zPagePreferences.sIsElementPresent(locator), "Verify the advanced data is invisible");

		locator = "css=tr[id='ZmExportView_DATE_row'][style='display: table-row;']";
		ZAssert.assertFalse(app.zPagePreferences.sIsElementPresent(locator), "Verify the advanced data is invisible");

		locator = "css=tr[id='ZmExportView_SEARCH_FILTER_row'][style='display: table-row;']";
		ZAssert.assertFalse(app.zPagePreferences.sIsElementPresent(locator), "Verify the advanced data is invisible");

		locator = "css=tr[id='ZmExportView_SKIP_META_row'][style='display: table-row;']";
		ZAssert.assertFalse(app.zPagePreferences.sIsElementPresent(locator), "Verify the advanced data is invisible");

		// See https://bugzilla.zimbra.com/show_bug.cgi?id=63289
		locator = "css=input[id='ZmExportView_ADVANCED']";
		app.zPagePreferences.sClick(locator);
		app.zPagePreferences.zWaitForBusyOverlay();

		// Verify the extra options now appear
		locator = "css=tr[id='ZmExportView_DATA_TYPES_row'][style='display: table-row;']";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(locator), "Verify the advanced data is visible");

		locator = "css=tr[id='ZmExportView_DATE_row'][style='display: table-row;']";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(locator), "Verify the advanced data is visible");

		locator = "css=tr[id='ZmExportView_SEARCH_FILTER_row'][style='display: table-row;']";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(locator), "Verify the advanced data is visible");

		locator = "css=tr[id='ZmExportView_SKIP_META_row'][style='display: table-row;']";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(locator), "Verify the advanced data is visible");
	}
}