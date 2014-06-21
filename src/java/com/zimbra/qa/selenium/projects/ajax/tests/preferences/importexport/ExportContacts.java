/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.importexport;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;


public class ExportContacts extends AjaxCommonTest {

	public ExportContacts() {
		
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = null;
	}


	@Test(
			description = "Verify clicking on 'export contacts' radio button",
			groups = { "functional" }
			)
	public void ExportContacts_01() throws HarnessException {

		
		/**
		 * 
		 * TODO: Since selenium doesn't handle the
		 * 'download' system dialog, just execute
		 * as much as possible - click Contacts and
		 * verify the display.
		 * 
		 */
		
		
		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.ImportExport);

		// TODO: See https://bugzilla.zimbra.com/show_bug.cgi?id=63289
		String locator = "css=div[id$='_TYPE_CSV_control'] input[type='radio']";
		app.zPagePreferences.sFocus(locator);
		app.zPagePreferences.sClick(locator);
		app.zPagePreferences.zWaitForBusyOverlay();

		// Verify the help hint
		String hint = app.zPagePreferences.sGetText("css=div[id$='_TYPE_HINT']");
		ZAssert.assertStringContains(hint, "You can export your contacts in the standard", "Verify help hint text changed");
		
	}
}
