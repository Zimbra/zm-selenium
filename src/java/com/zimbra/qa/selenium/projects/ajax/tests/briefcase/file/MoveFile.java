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

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FileItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.EnableBriefcaseFeature;
import com.zimbra.qa.selenium.projects.ajax.pages.*;
import org.testng.annotations.AfterMethod;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;

public class MoveFile extends EnableBriefcaseFeature {

	public MoveFile() throws HarnessException {
		logger.info("New " + MoveFile.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox","TRUE");
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
	}


	@Test (description = "Upload file through RestUtil - move & verify through GUI",
			groups = { "smoke", "L1" })

	public void MoveFile_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem folderItem = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		String briefcaseFolderId = folderItem.getId();

		String name = "subFolder" + ConfigProperties.getUniqueString();

		// Create a subfolder to move the file into i.e. Briefcase/subfolder
		account.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name + "' l='"
				+ briefcaseFolderId + "'/>" + "</CreateFolderRequest>");

		FolderItem subFolderItem = FolderItem.importFromSOAP(account, name);

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, folderItem, true);

		// Click on created subfolder
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, subFolderItem);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";

		FileItem fileItem = new FileItem(filePath);

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>" + "<doc l='" + folderItem.getId() + "'>"
				+ "<upload id='" + attachmentId + "'/>" + "</doc>" + "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, folderItem, true);

		app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);

		// Click move -> subfolder
		app.zPageBriefcase.zToolbarPressPulldown(Button.B_MOVE, subFolderItem);

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, folderItem, false);

		// Verify document was moved from the folder
		ZAssert.assertFalse(app.zPageBriefcase.isPresentInListView(fileItem.getName()),
				"Verify document was moved from the folder");

		// Click on subfolder in tree view
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, subFolderItem, true);

		// Verify document was moved to the selected folder
		boolean present = app.zPageBriefcase.isPresentInListView(fileItem.getName());

		ZAssert.assertTrue(present, "Verify document was moved to the selected folder");
	}


	@Test (description = "Move File using 'm' keyboard shortcut",
			groups = { "functional", "L3" })

	public void MoveFile_02() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseRootFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		String briefcaseRootFolderId = briefcaseRootFolder.getId();

		Shortcut shortcut = Shortcut.S_MOVE;

		String[] subFolderNames = { "subFolderName1" + ConfigProperties.getUniqueString(),
				"subFolderName2" + ConfigProperties.getUniqueString() };

		FolderItem[] subFolders = new FolderItem[subFolderNames.length];

		// Create sub-folders to move the message from/to: Briefcase/sub-folder
		for (int i = 0; i < subFolderNames.length; i++) {
			account.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + subFolderNames[i]
					+ "' l='" + briefcaseRootFolderId + "'/>" + "</CreateFolderRequest>");

			subFolders[i] = FolderItem.importFromSOAP(account, subFolderNames[i]);
		}

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseRootFolder, true);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";

		FileItem fileItem = new FileItem(filePath);

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>" + "<doc l='" + subFolders[0].getId() + "'>"
				+ "<upload id='" + attachmentId + "'/>" + "</doc>" + "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseRootFolder,
				true);

		// Double click on sub-folder1 in list view
		app.zPageBriefcase.zListItem(Action.A_DOUBLECLICK, subFolders[0]);

		app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);

		// Click the Move keyboard shortcut
		DialogMove chooseFolder = (DialogMove) app.zPageBriefcase.zKeyboardShortcut(shortcut);

		// Choose destination folder and Click OK on Confirmation dialog
		chooseFolder.sClickTreeFolder(subFolders[1]);
		app.zPageBriefcase.sClickAt("css=div[id=ChooseFolderDialog_button2]", "0,0");

		// Click on sub-folder1 in tree view
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, subFolders[0], false);

		// Verify document is no longer in the sub-folder1
		ZAssert.assertFalse(app.zPageBriefcase.isPresentInListView(fileItem.getName()),
				"Verify document is no longer in the folder: " + subFolders[0].getName());

		// Click on sub-folder2 in tree view
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, subFolders[1], true);

		// Verify document was moved to sub-folder2
		ZAssert.assertTrue(app.zPageBriefcase.isPresentInListView(fileItem.getName()),
				"Verify document was moved to the folder: " + subFolders[1].getName());
	}


	@Test (description = "Upload file through RestUtil - move using Right Click Context Menu & verify through GUI",
			groups = { "functional", "L2" })

	public void MoveFile_03() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem folderItem = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		String briefcaseFolderId = folderItem.getId();

		String name = "subFolder" + ConfigProperties.getUniqueString();

		// Create a subfolder to move the message into i.e. Briefcase/subfolder
		account.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name + "' l='"
				+ briefcaseFolderId + "'/>" + "</CreateFolderRequest>");

		FolderItem subFolderItem = FolderItem.importFromSOAP(account, name);

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, folderItem, true);

		// Click on created subfolder
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, subFolderItem);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";
		FileItem fileItem = new FileItem(filePath);

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend(
			"<SaveDocumentRequest xmlns='urn:zimbraMail'>" +
				"<doc l='" + folderItem.getId() + "'>" +
				"<upload id='" + attachmentId + "'/></doc>" +
			"</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, folderItem, true);

		app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);

		// Move using Right Click Context Menu
		DialogMove chooseFolder = (DialogMove) app.zPageBriefcase.zListItem(Action.A_RIGHTCLICK, Button.O_MOVE,
				fileItem);

		// Click OK on Confirmation dialog
		chooseFolder.sClickTreeFolder(subFolderItem);
		chooseFolder.zPressButton(Button.B_OK);

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, folderItem, false);

		// Verify document was moved from the folder
		ZAssert.assertFalse(app.zPageBriefcase.isPresentInListView(fileItem.getName()),
				"Verify document was moved from the folder");

		// Click on subfolder in tree view
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, subFolderItem, true);

		// Verify document was moved to the selected folder
		boolean present = app.zPageBriefcase.isPresentInListView(fileItem.getName());

		ZAssert.assertTrue(present, "Verify document was moved to the selected folder");
	}


	@AfterMethod(groups = { "always" })
	public void afterMethod() throws HarnessException {
		logger.info("Checking for the Move Dialog ...");

		// Check if the "Move Dialog is still open
		DialogMove dialog = new DialogMove(app, ((AjaxPages) app).zPageBriefcase);
		if (dialog.zIsActive()) {
			logger.warn(dialog.myPageName() + " was still active.  Cancelling ...");
			dialog.zPressButton(Button.B_CANCEL);
		}

	}
}