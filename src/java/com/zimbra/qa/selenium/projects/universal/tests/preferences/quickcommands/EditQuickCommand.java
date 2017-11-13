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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.quickcommands;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.universal.core.UniversalQuickCommandTest;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.DialogEditQuickCommand;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;


public class EditQuickCommand extends UniversalQuickCommandTest {

	public EditQuickCommand() {
		
		super.startingPage = app.zPagePreferences;
		
	}
	


	@Bugs (ids = "71389")	// Hold off on GUI implementation of Quick Commands in 8.X
	@Test(
			description = "Edit a basic Quick Command",
			groups = { "deprecated", "L4" }
			)
	public void EditQuickCommand_01() throws HarnessException {
		
		
		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.QuickCommands);

		// Select the quick command
		String locator = "css=div[id='zl__QCV__rows'] div[id^='zli__QCV__'] td[id$='_na']:contains('"+ this.getQuickCommand01().getName() +"')";
		ZAssert.assertTrue(app.zTreePreferences.sIsElementPresent(locator), "Verify quick command 1 is in the list");
		app.zTreePreferences.sClickAt(locator, "");
		app.zTreePreferences.zWaitForBusyOverlay();

		// Click "Edit"
		DialogEditQuickCommand dialog = (DialogEditQuickCommand)app.zPagePreferences.zToolbarPressButton(Button.B_EDIT_QUICK_COMMAND);

		// TODO: modify the quick command
		dialog.zPressButton(Button.B_OK);
		
		// Get the quick commands from the server.  Verify the edited quick command is there.
		throw new HarnessException("implement me!");
		
	}
}
