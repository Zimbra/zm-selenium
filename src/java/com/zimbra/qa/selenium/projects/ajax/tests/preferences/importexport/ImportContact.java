/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.Toaster;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.PagePreferences.Locators;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class ImportContact extends AjaxCore {

	public ImportContact() {
		super.startingPage = app.zPagePreferences;
	}

	@Bugs( ids = "78607" )	
	@Test (description = "Verify that while importing, ZCS accepts only csv format address book",
			groups = { "smoke", "L1", "upload" })

	public void ImportContact_01() throws HarnessException {

		//-- Files to import
		final String fileName1 = "samplejpg.jpg"; 
		final String filePath1 = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName1;
		
		final String fileName2 = "importContact.csv"; 
		final String filePath2 = ConfigProperties.getBaseDirectory() + "\\data\\public\\csv\\" + fileName2;
		
		final String contactLastname = "suite";
		
		FolderItem contacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Contacts);

		// Navigate to preferences -> Import/Export
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.ImportExport);

		// Click on Browse/Choose File button
		app.zPagePreferences.sClickAt(Locators.zBrowseFileButton,"0,0");

		// Enter the path of the exported file and upload
		zUpload(filePath1);

		// Click on Import button
		app.zPagePreferences.zPressButton(Button.B_IMPORT);
		
		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMessage = toast.zGetToastMessage();
		System.out.println("The Toast message:"+ toastMessage);
		ZAssert.assertStringContains(toastMessage, "File extension png is not supported by Import.", "Verify toast message: Contact Created");
		
		// Click on Browse/Choose File button
		app.zPagePreferences.sClickAt(Locators.zBrowseFileButton,"0,0");

		// Enter the path of the exported file and upload
		zUpload(filePath2);

		// Click on Import button
		app.zPagePreferences.zPressButton(Button.B_IMPORT);
		SleepUtil.sleepMedium();
		
		// Check that file is imported successfully
		ZAssert.assertStringContains(app.zPagePreferences.sGetText(Locators.zImportDialogContent),"Import succeeded.","Import is unsuccessfull!");

		// Click OK on confirmation dialog after import
		app.zPagePreferences.zPressButton(Button.B_IMPORT_OK);
		
		// Go to Contacts tab and verify that the contact is displayed
		startingPage= app.zPageContacts;
		startingPage.zNavigateTo();
		app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, contacts);
		ZAssert.assertTrue(app.zPageContacts.zVerifyContactExists(contactLastname), "Verify that contact is displayed");
	}
}