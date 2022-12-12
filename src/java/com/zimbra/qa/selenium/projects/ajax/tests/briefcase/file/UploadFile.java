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

import org.openqa.selenium.Keys;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FileItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.ajax.core.EnableBriefcaseFeature;
import com.zimbra.qa.selenium.projects.ajax.pages.briefcase.DialogUploadFile;
import com.zimbra.qa.selenium.projects.ajax.pages.briefcase.DialogUploadFile.Locators;

public class UploadFile extends EnableBriefcaseFeature {

	public UploadFile() throws HarnessException {
		logger.info("New " + UploadFile.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox","TRUE");
	}


	@Test (description = "Upload file through RestUtil - verify through GUI",
			groups = { "smoke" })

	public void UploadFile_01() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();
		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,	SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";
		FileItem fileItem = new FileItem(filePath);
		String fileName = fileItem.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFileUsingRestUtil(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>"
				+ "<doc l='" + briefcaseFolder.getId() + "'><upload id='"
				+ attachmentId + "'/></doc></SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Verify document is created
		String name = app.zPageBriefcase.getItemNameFromListView(fileName);
		ZAssert.assertStringContains(name, fileName, "Verify file name through GUI");

		// Delete file upon test completion
		app.zPageBriefcase.deleteFileByName(fileItem.getName());
	}


	@Test (description = "Upload file through GUI - verify through GUI",
			groups = { "smoke", "upload", "testcafe" })

	public void UploadFile_02() throws HarnessException {

		try {
			ZimbraAccount account = app.zGetActiveAccount();
			FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

			// Create file item
			final String fileName = "testtextfile.txt";
			final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;
			FileItem fileItem = new FileItem(filePath);

			// Select briefcase folder
			app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

			// Click on Upload File button in the Toolbar
			DialogUploadFile dlg = (DialogUploadFile) app.zPageBriefcase.zToolbarPressButton(Button.B_UPLOAD_FILE, fileItem);

			app.zPageBriefcase.sClickAt("css=div[class='ZmUploadDialog'] input[name='uploadFile']", "10,10");

			zUpload(filePath);

			dlg.zPressButton(Button.B_OK);

			// Click on created File
			app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);

			// Verify file is uploaded
			String name = app.zPageBriefcase.getItemNameFromListView(fileName);
			ZAssert.assertStringContains(name, fileName, "Verify file name through GUI");

		} finally {
			app.zPageMain.zKeyboardKeyEvent(Keys.ESCAPE);
		}
	}


	@Test (description = "Upload file through RestUtil - verify through SOAP",
			groups = { "bhr", "testcafe" })

	public void UploadFile_03() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();
		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";
		FileItem file = new FileItem(filePath);
		String fileName = file.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFileUsingRestUtil(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend(
				"<SaveDocumentRequest xmlns='urn:zimbraMail'>" +
					"<doc l='" + briefcaseFolder.getId() + "'>" +
					"<upload id='" + attachmentId + "'/></doc>" +
				"</SaveDocumentRequest>");

		account.soapSelectNode("//mail:SaveDocumentResponse", 1);

		// Search the uploaded file
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='document'>"
						+ "<query>" + fileName + "</query>"
						+ "</SearchRequest>");

		// Verify file name through SOAP
		String name = account.soapSelectValue("//mail:doc", "name");
		ZAssert.assertEquals(name, fileName, "Verify file name through SOAP");

		// Delete file upon test completion
		String id = account.soapSelectValue("//mail:doc", "id");
		app.zPageBriefcase.deleteFileById(id);
	}
	
	
	@Test (description = "Upload file of size greater than set zimbraFileUploadMaxSize through GUI - verify the upload failure message",
		      groups = { "functional", "upload", "non-msedge" })

	public void UploadFile_04() throws HarnessException {

		try {
			// Set the max file upload limit to 1 MB so that user can upload file of size upto 764KB
			ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
					" <ModifyConfigRequest  xmlns='urn:zimbraAdmin'>"
							+     "<a n='zimbraFileUploadMaxSize'>1048576</a>"
							+ "</ModifyConfigRequest >");

			// Refresh the UI so that the configuration change is loaded
			app.zPageBriefcase.zRefreshUI();
			startingPage.zNavigateTo();

			ZimbraAccount account = app.zGetActiveAccount();

			FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

			// Create file item
			final String fileName1 = "BasicPng.PNG";
			final String filePath1 = ConfigProperties.getBaseDirectory() + "\\data\\public\\Files\\Basic01\\" + fileName1; // Size of the file is 530 KB
			final String fileName2 = "BasicTiff.TIF";
			final String filePath2 = ConfigProperties.getBaseDirectory() + "\\data\\public\\Files\\Basic01\\" + fileName2; // Size of the file is 794 KB

			// Select briefcase folder
			app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder);

			// Click on Upload File button in the tool bar
			DialogUploadFile dlg = (DialogUploadFile) app.zPageBriefcase.zToolbarPressButton(Button.B_UPLOAD_FILE);
			
			app.zPageBriefcase.sClickAt("css=div[class='ZmUploadDialog'] input[name='uploadFile']", "10,10");

			// Upload file
			zUpload(filePath2);
			dlg.zPressButton(Button.B_OK);
			
			// Verify the upload failure message
			ZAssert.assertTrue(dlg.zGetDisplayedText(Locators.zUploadStatusMessage).equals("Upload Failed:\n* The file " + fileName2 + " exceeds the size limit of 764 KB."), 
					"Verify the upload failure message");
			
			// Upload the file of smaller size
			app.zPageBriefcase.sClickAt("css=div[class='ZmUploadDialog'] input[name='uploadFile']", "10,10");
			zUpload(filePath1);
			dlg.zPressButton(Button.B_OK);

			// Verify file is uploaded and displayed in the list view
			ZAssert.assertTrue(app.zPageBriefcase.isPresentInListView(fileName1), "Verify the file is present in the list");
			
			// Verify file is not present
			ZAssert.assertFalse(app.zPageBriefcase.isPresentInListView(fileName2), "Verify the file is not present in the list");
			
		} finally {
			app.zPageMain.zKeyboardKeyEvent(Keys.ESCAPE);
			// Reset the zimbraFileUploadMaxSize attribute
			ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
					" <ModifyConfigRequest  xmlns='urn:zimbraAdmin'>"
							+     "<a n='zimbraFileUploadMaxSize'>10485760</a>"
							+ "</ModifyConfigRequest >");
		}
	}
}