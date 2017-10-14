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

import java.util.List;

import org.testng.SkipException;
import org.testng.annotations.Test;




import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.OperatingSystem;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.PagePreferences.Locators;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class ImportAccount extends AjaxCommonTest {

	public ImportAccount() {

		super.startingPage = app.zPagePreferences;
		
	}

	@Test( description = "Verify the presence of mail, contacts, calendar appointments, tasks and Briefcase documents after importing an exported compressed file",
			groups = { "sanity", "L0" })

	public void ImportAccount_01() throws HarnessException {

		if (OperatingSystem.isWindows() == true && !ConfigProperties.getStringProperty("browser").contains("edge")) {

			//-- File to import
			final String fileName = "account.tgz";
			final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\tgz\\" + fileName;

			//Data present in the file to be imported
			String mailSubject="test mail";
			String folderName="testfolder";
			String contactFileAs="test2, test1";
			String apptSubject="test invite";
			String taskSubject="test task";
			String docName="favicon.ico";

			//Getting system folders
			FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox);
			FolderItem contacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Contacts);
			FolderItem tasks = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Tasks);
			FolderItem briefcase = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Briefcase);

			// Navigate to preferences -> Import/Export
			app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.ImportExport);

			//Click on Browse/Choose File button
			app.zPagePreferences.sClickAt(Locators.zBrowseFileButton,"0,0");

			//Enter the path of the exported file and upload
			zUpload(filePath);

			//Click on Import button
			app.zPagePreferences.zPressButton(Button.B_IMPORT);
			SleepUtil.sleepMedium();

			//Check that file is imported successfully
			ZAssert.assertStringContains(app.zPagePreferences.sGetText(Locators.zImportDialogContent),"Import succeeded.","Import is unsuccessfull!");

			//Click OK on confirmation dialog after import
			app.zPagePreferences.zPressButton(Button.B_IMPORT_OK);

			//--Verification

			//Go to Mail tab
			startingPage= app.zPageMail;
			startingPage.zNavigateTo();

			//Check that mail is present in inbox
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK,inbox);
			ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(mailSubject),"Verify that mail is displayed");

			// Check that folder is displayed in the list
			List<FolderItem> folders = app.zTreeMail.zListGetFolders();
			boolean found = false;
			for (FolderItem f : folders) {
				if ( folderName.equals(f.getName()) ) {
					found = true;
					break;
				}
			}
			ZAssert.assertTrue(found, "Verify that folder is displayed in the tree");

			//Go to Contacts tab
			startingPage= app.zPageContacts;
			startingPage.zNavigateTo();
			app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, contacts);
			ZAssert.assertTrue(app.zPageContacts.zVerifyContactExists(contactFileAs), "Verify that contact is displayed");

			//Go to calendar tab and check the presence of appointment
			startingPage= app.zPageCalendar;
			startingPage.zNavigateTo();
			ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify that appointment is displayed");

			//Go to Tasks tab and check the presence of Task
			startingPage= app.zPageTasks;
			startingPage.zNavigateTo();
			app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK,tasks);
			ZAssert.assertTrue(app.zPageTasks.isPresent(taskSubject), "Verify that task is displayed");

			//Go to Briefcase tab and check the presence of document
			startingPage= app.zPageBriefcase;
			startingPage.zNavigateTo();
			app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK,briefcase);
			ZAssert.assertTrue(app.zPageBriefcase.isPresentInListView(docName), "Verify that document in briefcase is displayed");

		} else {
			throw new SkipException("File upload operation is allowed only for Windows OS (Skipping upload tests on MS Edge for now due to intermittancy and major control issue), skipping this test...");
		}

	}
}

