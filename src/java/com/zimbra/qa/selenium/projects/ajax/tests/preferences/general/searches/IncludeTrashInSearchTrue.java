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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.general.searches;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.PagePreferences.Locators;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class IncludeTrashInSearchTrue extends AjaxCore {

	public IncludeTrashInSearchTrue() {
		logger.info("New "+ IncludeTrashInSearchTrue.class.getCanonicalName());

		super.startingPage = app.zPageMail;
		super.startingAccountPreferences.put("zimbraPrefIncludeTrashInSearch", "FALSE");
	}


	@Test (description = "Verify that mails in trash are also included in search when 'zimbraPrefIncludeTrashInSearch' is TRUE",
			groups = { "sanity" })

	public void IncludeTrashInSearchTrue_01() throws HarnessException {

		// Data required to create a mail in Trash
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		String subject = "subject" + ConfigProperties.getUniqueString();

		// Create a message in trash
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
						+		"<m l='"+ trash.getId() +"' >"
						+			"<content>From: foo@foo.com\n"
						+				"To: foo@foo.com \n"
						+				"Subject: "+ subject +"\n"
						+				"MIME-Version: 1.0 \n"
						+				"Content-Type: text/plain; charset=utf-8 \n"
						+				"Content-Transfer-Encoding: 7bit\n"
						+				"\n"
						+				"simple text string in the body\n"
						+			"</content>"
						+		"</m>"
						+	"</AddMsgRequest>");

		// Check the presence of mail in Trash folder
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, trash);
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Search the mail when 'zimbraPrefIncludeTrashInSearch' is FALSE
		app.zPageSearch.zAddSearchQuery(subject );
		app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);

		// Verify that the mail is not displayed in search result
		ZAssert.assertFalse(app.zPageSearch.zVerifyMailExists(subject), "Verify that message from trash is not displayed");

		// Go to "Preferences -> General -> Search
		app.zPagePreferences.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.General);

		// Select the check box to search in trash folder
		app.zPagePreferences.zCheckboxSet(Locators.zSearchTrashFolder, true);
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		// Verify the account preference has been modified
		app.zGetActiveAccount().soapSend(
				"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
						+		"<pref name='zimbraPrefIncludeTrashInSearch'/>"
						+	"</GetPrefsRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefIncludeTrashInSearch']", null);
		ZAssert.assertEquals(value, "TRUE", "Verify the zimbraPrefIncludeTrashInSearch preference was changed to 'TRUE'");

		// Search the mail when 'zimbraPrefIncludeTrashInSearch' is TRUE
		app.zPageSearch.zAddSearchQuery(subject );
		app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);

		// Verify that the mail is displayed in search result
		ZAssert.assertTrue(app.zPageSearch.zVerifyMailExists(subject), "Verify that message from trash is not displayed");

		// Close the search view
		app.zPageSearch.zClose();
	}
}