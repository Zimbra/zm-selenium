/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.dl.ui;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class CheckDLFolderUI extends AjaxCommonTest  {

	public CheckDLFolderUI() {
		logger.info("New "+ CheckDLFolderUI.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
		super.startingAccountPreferences = null;
	}

	@Test( description = "Right click to DL folder and check respected UI options", groups = { "functional" })

	public void CheckDLFolderRightClickOptions_01 () throws HarnessException {

		String firstContactEmail = ZimbraAccount.AccountA().EmailAddress;
		String secondContactEmail = ZimbraAccount.AccountB().EmailAddress;

		String dlFolder = "Distribution Lists";
		String dlName = "dl" + ConfigProperties.getUniqueString();
		String fullDLName = dlName + "@" + ConfigProperties.getStringProperty("testdomain");

		// Create DL
		app.zGetActiveAccount().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAccount'>"
			+		"<name>" + fullDLName + "</name>"
			+	"</CreateDistributionListRequest>");

		// Add DL members
		app.zGetActiveAccount().soapSend(
				"<DistributionListActionRequest xmlns='urn:zimbraAccount'>"
			+		"<dl by='name'>" + fullDLName + "</dl>"
			+		"<action op='addMembers'>"
         	+			"<dlm>" + firstContactEmail + "</dlm>"
         	+			"<dlm>" + secondContactEmail + "</dlm>"
         	+		"</action>"
			+	"</DistributionListActionRequest>");

		// Check DL folder right click options
		app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, dlFolder);
		ZAssert.assertTrue(app.zPageContacts.zVerifyDisabledControl(Button.O_NEW_CONTACTS_FOLDER), "Verify 'New Contacts' menu is enabled");
		ZAssert.assertTrue(app.zPageContacts.zVerifyDisabledControl(Button.O_SHARE_CONTACTS_FOLDER), "Verify 'Share Contacts' menu is enabled");
		ZAssert.assertTrue(app.zPageContacts.zVerifyDisabledControl(Button.O_DELETE), "Verify 'Delete' menu is enabled");
		ZAssert.assertTrue(app.zPageContacts.zVerifyDisabledControl(Button.O_RENAME_FOLDER), "Verify 'Rename Folder' menu is enabled");
		ZAssert.assertTrue(app.zPageContacts.zVerifyDisabledControl(Button.O_EDIT_PROPERTIES), "Verify 'Edit Properties' menu is enabled");
		ZAssert.assertTrue(app.zPageContacts.zVerifyDisabledControl(Button.O_EXPAND_ALL), "Verify 'Expand All' menu is enabled");
	}
}
