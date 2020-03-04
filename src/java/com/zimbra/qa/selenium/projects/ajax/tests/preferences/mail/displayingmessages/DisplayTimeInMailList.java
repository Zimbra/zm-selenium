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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.displayingmessages;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Checkbox;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.PagePreferences.Locators;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class DisplayTimeInMailList extends AjaxCore {

	public DisplayTimeInMailList() {
		logger.info("New "+ DisplayTimeInMailList.class.getCanonicalName());

		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences.put("zimbraPrefDisplayTimeInMailList", "FALSE");
	}


	@Bugs( ids = "77129" )
	@Test (description = "Verify the display of email received time in email list",
			groups = { "sanity", "upload", "non-msedge" })

	public void DisplayTimeInMailList_01() throws HarnessException {
		// File to import: contains mail delivered in past
		final String fileName = "inbox.tgz";
		final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\tgz\\" + fileName;

		// Data present in the file to be imported
		String subject = "RE: [Bug 108711] TIFF image preview is not showing in message body or as attachment";
		String mailReceivedTime = "12/12/2017 7:42 PM";

		// Navigate to preferences -> Import/Export
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.ImportExport);

		// Click on Browse/Choose File button
		app.zPagePreferences.sClickAt(Locators.zBrowseFileButton,"0,0");

		// Enter the path of the exported file and upload
		zUpload(filePath);

		// Click on Import button
		app.zPagePreferences.zPressButton(Button.B_IMPORT);
		SleepUtil.sleepMedium();

		// Check that file is imported successfully
		ZAssert.assertStringContains(app.zPagePreferences.sGetText(Locators.zImportDialogContent),"Import succeeded.", "Import is unsuccessfull!");

		// Click OK on confirmation dialog after import
		app.zPagePreferences.zPressButton(Button.B_IMPORT_OK);

		// Navigate to preferences -> mail -> displaying messages
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);

		// Check the check box: Always display received time in email list
		app.zPagePreferences.zCheckboxSet(Checkbox.C_DISPLAY_RECEIVED_TIME, true);

		// Click save
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		// Select yes to reload the client
		app.zPagePreferences.zPressButton(Button.B_YES);

		// Verification using soap
		app.zGetActiveAccount().soapSend(
				"<GetPrefsRequest xmlns='urn:zimbraAccount'>" +
						"<pref name='zimbraPrefDisplayTimeInMailList'/>" +
				"</GetPrefsRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefDisplayTimeInMailList']", null);
		ZAssert.assertEquals(value, "TRUE", "Verify the preference was changed to TRUE");

		// Change the reading pane to bottom
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_READING_PANE_BOTTOM);

		// Verify the mail received time displayed in mail list
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "time"), mailReceivedTime, "Verify that message delivery date and time for past mail are displayed in the mail list.");
	}
}