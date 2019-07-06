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
package com.zimbra.qa.selenium.projects.ajax.tests.briefcase.file;

import java.util.List;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FileItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.EnableBriefcaseFeature;
import com.zimbra.qa.selenium.projects.ajax.pages.briefcase.DocumentBriefcaseOpen;
import com.zimbra.qa.selenium.projects.ajax.pages.briefcase.PageBriefcase;

public class OpenFile extends EnableBriefcaseFeature {

	public OpenFile() throws HarnessException {
		logger.info("New " + OpenFile.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox","TRUE");
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
	}


	@Test (description = "Upload file through RestUtil - open & verify through GUI",
			groups = { "smoke" })

	public void OpenFile_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";
		FileItem fileItem = new FileItem(filePath);
		String fileName = fileItem.getName();
		final String fileText = "test";

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFileUsingRestUtil(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>" + "<doc l='" + briefcaseFolder.getId()
				+ "'><upload id='" + attachmentId + "'/></doc></SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created file
		app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);

		// Click on open in a separate window icon in toolbar

		DocumentBriefcaseOpen file;
		file = (DocumentBriefcaseOpen) app.zPageBriefcase.zToolbarPressPulldown(Button.B_ACTIONS,
					Button.B_LAUNCH_IN_SEPARATE_WINDOW, fileItem);
		app.zPageBriefcase.isOpenFileLoaded(fileName, fileText);

		String text = "";

		// Select document opened in a separate window
		try {
			app.zPageBriefcase.zSelectWindow(fileName);

			text = file.retriveFileText();

			// Close
			app.zPageBriefcase.zSelectWindow(fileName);

			app.zPageBriefcase.closeWindow();
		} finally {
			app.zPageBriefcase.zSelectWindow("Zimbra: Briefcase");
		}

		ZAssert.assertStringContains(text, fileText, "Verify document text through GUI");

		// Delete file upon test completion
		app.zPageBriefcase.deleteFileByName(fileItem.getName());
	}


	@AfterMethod(groups = { "always" })
	public void afterMethod() throws HarnessException {
		logger.info("Checking for the opened window ...");

		// Check if the window is still open
		List<String> windows = app.zPageBriefcase.sGetAllWindowNames();
		for (String window : windows) {
			if (!window.isEmpty() && !window.contains("null")
					&& !window.contains(PageBriefcase.pageTitle)
					&& !window.contains("main_app_window")
					&& !window.contains("undefined")) {
				logger.warn(window + " window was still active. Closing ...");
				app.zPageBriefcase.zSelectWindow(window);
				app.zPageBriefcase.closeWindow();
			}
		}
		app.zPageBriefcase.zSelectWindow(PageBriefcase.pageTitle);
	}
}