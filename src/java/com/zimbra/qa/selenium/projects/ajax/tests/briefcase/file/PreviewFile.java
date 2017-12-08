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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FileItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.EnableBriefcaseFeature;

public class PreviewFile extends EnableBriefcaseFeature {

	public PreviewFile() {
		logger.info("New " + PreviewFile.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
	}

	@BeforeMethod(groups = { "always" })
	public void MaximizeBrowser(){
		app.zPageBriefcase.maximizeBrowser();
	}


	@Test (description = "Verify JPEG fiew preview in reading pane",
			groups = { "smoke", "L1" })

	public void PreviewJPEGFile_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,	SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/samplejpg.jpg";
		FileItem file = new FileItem(filePath);
		String fileName = file.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>" + "<doc l='" + briefcaseFolder.getId()
				+ "'><upload id='" + attachmentId + "'/></doc></SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Verify document is created
		String name = app.zPageBriefcase.getItemNameFromListView(fileName);
		ZAssert.assertStringContains(name, fileName, "Verify file name through GUI");

		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, file);
		SleepUtil.sleepLong();

		ZAssert.assertTrue(app.zPageBriefcase.zVerifyImageFilePreviewContents("css=img[src*='" + fileName + "']"), "Verify that image is present in the preview pane");
	}


	@Test (description = "Verify text fiew preview in reading pane",
			groups = { "smoke", "L1" })

	public void PreviewTextFile_02() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";
		FileItem file = new FileItem(filePath);
		String fileName = file.getName();
		String fileContent = "test text file content";

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>" + "<doc l='" + briefcaseFolder.getId()
				+ "'><upload id='" + attachmentId + "'/></doc></SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Verify document is created
		String name = app.zPageBriefcase.getItemNameFromListView(fileName);
		ZAssert.assertStringContains(name, fileName, "Verify file name through GUI");

		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, file);
		SleepUtil.sleepLong();
		ZAssert.assertTrue(app.zPageBriefcase.zVerifyTextFilePreviewContents(fileContent), "Verify that text file can be viewed correctly in preview pane");
	}


	@Test (description = "Verify PDF fiew preview in reading pane",
			groups = { "smoke", "L1" })

	public void PreviewPDFFile_03() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testpdffile.pdf";
		FileItem file = new FileItem(filePath);
		String fileName = file.getName();
		String fileContent = "PDF Test File";

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>" + "<doc l='" + briefcaseFolder.getId()
				+ "'><upload id='" + attachmentId + "'/></doc></SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Verify document is created
		String name = app.zPageBriefcase.getItemNameFromListView(fileName);
		ZAssert.assertStringContains(name, fileName, "Verify file name through GUI");

		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, file);
		SleepUtil.sleepLong();
		ZAssert.assertTrue(app.zPageBriefcase.zVerifyPdfFilePreviewContents(fileContent), "Verify content using PDF file preview elements");
	}


	@Test (description = "Verify word fiew preview in reading pane",
			groups = { "smoke", "L1" })

	public void PreviewWordFile_04() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testwordfile.doc";
		FileItem file = new FileItem(filePath);
		String fileName = file.getName();
		String fileContent = "test word file content";

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>" + "<doc l='" + briefcaseFolder.getId()
				+ "'><upload id='" + attachmentId + "'/></doc></SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Verify document is created
		String name = app.zPageBriefcase.getItemNameFromListView(fileName);
		ZAssert.assertStringContains(name, fileName, "Verify file name through GUI");

		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, file);
		SleepUtil.sleepLong();
		ZAssert.assertTrue(app.zPageBriefcase.zVerifyPdfFilePreviewContents(fileContent), "Verify content using PDF file preview elements");
	}


	@Test (description = "Verify excel fiew preview in reading pane",
			groups = { "smoke", "L1" })

	public void PreviewXlsFile_05() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testexcelfile.xls";
		FileItem file = new FileItem(filePath);
		String fileName = file.getName();
		String fileContent = "test excel file content";

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>" + "<doc l='" + briefcaseFolder.getId()
				+ "'><upload id='" + attachmentId + "'/></doc></SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Verify document is created
		String name = app.zPageBriefcase.getItemNameFromListView(fileName);
		ZAssert.assertStringContains(name, fileName, "Verify file name through GUI");

		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, file);
		SleepUtil.sleepLong();
		ZAssert.assertTrue(app.zPageBriefcase.zVerifyPdfFilePreviewContents(fileContent), "Verify content using PDF file preview elements");
	}


	@Test (description = "Verify power point fiew preview in reading pane",
			groups = { "smoke", "L1" })

	public void PreviewPptFile_06() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testpptfile.ppt";
		FileItem file = new FileItem(filePath);
		String fileName = file.getName();
		String fileContent = "test power point file content";

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>" + "<doc l='" + briefcaseFolder.getId()
				+ "'><upload id='" + attachmentId + "'/></doc></SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Verify document is created
		String name = app.zPageBriefcase.getItemNameFromListView(fileName);
		ZAssert.assertStringContains(name, fileName, "Verify file name through GUI");

		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, file);
		SleepUtil.sleepLong();
		ZAssert.assertTrue(app.zPageBriefcase.zVerifyPdfFilePreviewContents(fileContent), "Verify content using PDF file preview elements");
	}
}