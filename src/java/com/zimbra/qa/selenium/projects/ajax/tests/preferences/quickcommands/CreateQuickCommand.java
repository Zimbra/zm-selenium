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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.quickcommands;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.QuickCommand;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.DialogEditQuickCommand;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.DialogEditQuickCommand.*;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class CreateQuickCommand extends AjaxCore {

	public CreateQuickCommand() {
		super.startingPage = app.zPagePreferences;
	}

	@Bugs (ids = "71389")
	@Test (description = "Create a basic Quick Command",
			groups = { "deprecated" })

	public void CreateQuickCommand_01() throws HarnessException {

		String name = "name"+ ConfigProperties.getUniqueString();
		String description = "description"+ ConfigProperties.getUniqueString();

		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.QuickCommands);

		// Click "New"
		DialogEditQuickCommand dialog = (DialogEditQuickCommand)app.zPagePreferences.zToolbarPressButton(Button.B_NEW_QUICK_COMMAND);
		ZAssert.assertTrue(dialog.zIsActive(), "Verify the dialog opened sucessfully, See bug 63932");

		// Fill out the dialog.  Click OK.
		dialog.zSetQuickCommandName(name);
		dialog.zSetQuickCommandDescription(description);
		dialog.zSetQuickCommandType(QuickCommandType.Message);

		dialog.zSetQuickCommandActionActive(1, true);
		dialog.zSetQuickCommandActionOperation(1, QuickCommandOperation.MarkAs);
		dialog.zSetQuickCommandActionTarget(1, QuickCommandTarget.MarkAsFlagged);
		dialog.zPressButton(Button.B_OK);

		app.zGetActiveAccount().soapSend(
					"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+		"<pref name='zimbraPrefQuickCommand'/>"
				+	"</GetPrefsRequest>");

		String found = null;
		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//acct:pref[@name='zimbraPrefQuickCommand']");
		for (Element e : nodes) {
			if ( e.getText().contains(name) ) {
				found = e.getText();
				break;
			}
		}

		ZAssert.assertNotNull(found, "Verify the quick command was saved to the server");

		// Convert the Prefs value to a harness QuickCommand
		QuickCommand command = QuickCommand.fromJSON(found);

		ZAssert.assertEquals(command.getName(), name, "Verify the name matches");
		ZAssert.assertEquals(command.getDescription(), description, "Verify the name matches");
		ZAssert.assertEquals(command.isActive(), true, "Verify the command is active");
	}
}
