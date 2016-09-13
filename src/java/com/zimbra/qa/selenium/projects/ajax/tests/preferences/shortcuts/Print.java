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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.shortcuts;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.SeparateWindow;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;


public class Print extends AjaxCommonTest {

	public Print() {
		
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = null;
	}


	@Test(
			description = "Print the shortcuts preference page",
			groups = { "functional" }
			)
	public void Print_01() throws HarnessException {

		
		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Shortcuts);

		
		// Verify the page is showing
		String locator = "css=div[id$='_SHORTCUT_PRINT'] div.ZButton td[id$='_title']";
		if ( !app.zPagePreferences.sIsElementPresent(locator) ) {
			throw new HarnessException("Print button does not exist");
		}
		
		SeparateWindow window = null;
		
		try {
				
			// Click Print, which opens a separate window
			window = new SeparateWindow(app);
			window.zInitializeWindowNames();
			app.zTreePreferences.zClickAt(locator, "");
			app.zTreePreferences.zWaitForBusyOverlay();

			
			// Make sure the window is there			
			window.zWaitForActive();
			ZAssert.assertTrue(window.zIsActive(), "Verify the print window is active");

		} finally {
			
			// Close the print window, if applicable
			
			if ( window != null ) {
				window.zCloseWindow();
				window = null;
			}
		}


	}
}
