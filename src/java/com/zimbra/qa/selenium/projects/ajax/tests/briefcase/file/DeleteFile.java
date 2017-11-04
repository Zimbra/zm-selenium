/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FileItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.FeatureBriefcaseTest;
import com.zimbra.qa.selenium.projects.ajax.ui.briefcase.DialogConfirm;

public class DeleteFile extends FeatureBriefcaseTest {

	public DeleteFile() throws HarnessException {
		logger.info("New " + DeleteFile.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox","TRUE");
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
	}


	@Test( description = "Upload file through RestUtil - delete & verify through GUI",
			groups = { "smoke", "L1" })

	public void DeleteFile_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";

		FileItem fileItem = new FileItem(filePath);

		String fileName = fileItem.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>" + "<doc l='" + briefcaseFolder.getId() + "'>"
				+ "<upload id='" + attachmentId + "'/>" + "</doc>" + "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);

		// Click on Delete document icon in toolbar
		DialogConfirm deleteConfirm = (DialogConfirm) app.zPageBriefcase.zToolbarPressButton(Button.B_DELETE, fileItem);

		// Click OK on Confirmation dialog
		deleteConfirm.zClickButton(Button.B_YES);

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Verify document was deleted
		ZAssert.assertFalse(app.zPageBriefcase.isPresentInListView(fileName),
				"Verify document was deleted through GUI");
	}


	@Test( description = "Upload file through RestUtil - delete using Delete Key & check trash",
			groups = { "functional", "L2" })

	public void DeleteFile_02() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		FolderItem trashFolder = FolderItem.importFromSOAP(account, SystemFolder.Trash);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";

		FileItem fileItem = new FileItem(filePath);

		String fileName = fileItem.getName();

		Shortcut shortcut = Shortcut.S_DELETE;

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'><doc l='"
				+ briefcaseFolder.getId() + "'>" + "<upload id='"
				+ attachmentId + "'/></doc></SaveDocumentRequest>");

		String docId = account.soapSelectValue("//mail:SaveDocumentResponse//mail:doc", "id");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);

		// Click the Delete keyboard shortcut
		DialogConfirm deleteConfirm = (DialogConfirm) app.zPageBriefcase.zKeyboardShortcut(shortcut);

		// Click OK on Confirmation dialog
		deleteConfirm.zClickButton(Button.B_YES);

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Verify file was deleted from the list
		ZAssert.assertFalse(app.zPageBriefcase.isPresentInListView(fileName), "Verify file was deleted through GUI");

		// Verify document moved to Trash
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>" + "<query>in:"
				+ trashFolder.getName() + " " + fileName + "</query>" + "</SearchRequest>");

		String id = account.soapSelectValue("//mail:SearchResponse//mail:doc", "id");
		ZAssert.assertEquals(id, docId, "Verify the file was moved to the trash folder: " + fileName + " id: " + id);
	}


	@Test( description = "Upload file through RestUtil - delete using <Backspace> Key & check trash",
			groups = { "functional", "L2" })

	public void DeleteFile_03() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		FolderItem trashFolder = FolderItem.importFromSOAP(account, SystemFolder.Trash);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";

		FileItem fileItem = new FileItem(filePath);

		String fileName = fileItem.getName();

		Shortcut shortcut = Shortcut.S_BACKSPACE;

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'><doc l='"
				+ briefcaseFolder.getId() + "'>" + "<upload id='"
				+ attachmentId + "'/></doc></SaveDocumentRequest>");

		String docId = account.soapSelectValue("//mail:SaveDocumentResponse//mail:doc", "id");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);

		// Click the Backspace keyboard shortcut
		DialogConfirm deleteConfirm = (DialogConfirm) app.zPageBriefcase.zKeyboardShortcut(shortcut);

		// Click OK on Confirmation dialog
		deleteConfirm.zClickButton(Button.B_YES);

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Verify file was deleted from the list
		ZAssert.assertFalse(app.zPageBriefcase.isPresentInListView(fileName), "Verify file was deleted through GUI");

		// Verify document moved to Trash
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>" + "<query>in:"
				+ trashFolder.getName() + " " + fileName + "</query>" + "</SearchRequest>");

		String id = account.soapSelectValue("//mail:SearchResponse//mail:doc", "id");
		ZAssert.assertEquals(id, docId, "Verify the file was moved to the trash folder: " + fileName + " id: " + id);
	}


	@Test( description = "Upload file through RestUtil - delete using Right Click context menu",
			groups = { "functional", "L2" })

	public void DeleteFile_04() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";

		FileItem fileItem = new FileItem(filePath);

		String fileName = fileItem.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'><doc l='"
				+ briefcaseFolder.getId() + "'>" + "<upload id='"
				+ attachmentId + "'/></doc></SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created file
		app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);

		// Delete File using Right Click Context Menu
		DialogConfirm deleteConfirm = (DialogConfirm) app.zPageBriefcase.zListItem(Action.A_RIGHTCLICK, Button.O_DELETE,
				fileItem);

		// Click OK on Confirmation dialog
		deleteConfirm.zClickButton(Button.B_YES);

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Verify file was deleted from the list
		ZAssert.assertFalse(app.zPageBriefcase.isPresentInListView(fileName), "Verify file was deleted through GUI");
	}


	@Bugs(ids = "46889")
	@Test( description = "Cannot delete uploaded file if it was already deleted once before",
			groups = { "functional", "L2" })

	public void DeleteFile_05() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";
		FileItem fileItem = new FileItem(filePath);
		String fileName = fileItem.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>" + "<doc l='" + briefcaseFolder.getId() + "'>"
				+ "<upload id='" + attachmentId + "'/>" + "</doc>" + "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);

		DialogConfirm deleteConfirm = (DialogConfirm) app.zPageBriefcase
				.zToolbarPressButton(Button.B_DELETE, fileItem);

		deleteConfirm.zClickButton(Button.B_YES);

		app.zTreeBriefcase
				.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		ZAssert.assertFalse(app.zPageBriefcase.isPresentInListView(fileName),
				"Verify document was deleted through GUI");

		// Re-Upload file to server through RestUtil
		String attachId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>" + "<doc l='" + briefcaseFolder.getId() + "'>"
				+ "<upload id='" + attachId + "'/>" + "</doc>" + "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);

		DialogConfirm deleteConfirm2 = (DialogConfirm) app.zPageBriefcase.zToolbarPressButton(Button.B_DELETE,
				fileItem);

		deleteConfirm2.zClickButton(Button.B_YES);

		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		ZAssert.assertFalse(app.zPageBriefcase.isPresentInListView(fileName),
				"Verify document was deleted through GUI");
	}
}