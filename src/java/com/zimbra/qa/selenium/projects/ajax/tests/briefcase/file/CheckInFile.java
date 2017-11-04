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
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.FeatureBriefcaseTest;
import com.zimbra.qa.selenium.projects.ajax.ui.briefcase.DialogCheckInFile;

public class CheckInFile extends FeatureBriefcaseTest {

	public CheckInFile() {
		logger.info("New " + CheckInFile.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
	}


	@Test( description = "Check Out File through SOAP - right click 'Check In' - click 'Cancel'",
			groups = { "functional", "L2" })

	public void CheckInFile_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";

		FileItem file = new FileItem(filePath);

		String fileName = file.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>"
				+ "<doc l='" + briefcaseFolder.getId() + "'>" + "<upload id='"
				+ attachmentId + "'/>" + "</doc>" + "</SaveDocumentRequest>");

		// Search the uploaded file
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>" + "<query>" + fileName + "</query>"
				+ "</SearchRequest>");

		// Verify file name through SOAP
		String name = account.soapSelectValue("//mail:doc", "name");
		ZAssert.assertEquals(name, fileName, "Verify file name through SOAP");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Verify Lock icon is not present for the uploaded file
		ZAssert.assertFalse(app.zPageBriefcase.isLockIconPresent(file),"Verify Lock icon is not present for the uploaded file");

		// Check Out file through SOAP
		String id = account.soapSelectValue("//mail:doc", "id");
		account.soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>"
				+ "<action id='" + id + "' op='lock'/>"
				+ "</ItemActionRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Verify Lock icon is present for the checked out file
		ZAssert.assertTrue(app.zPageBriefcase.isLockIconPresent(file),"Verify Lock icon is present for the checked out file");

		// Right click on the locked file and select Check In File context menu
		DialogCheckInFile dlg = (DialogCheckInFile) app.zPageBriefcase.zListItem(Action.A_RIGHTCLICK,
				Button.O_CHECK_IN_FILE, file);

		// Verify the 'Check In File to Briefcase' dialog is displayed
		ZAssert.assertTrue(dlg.zIsActive(), "Verify the 'Check In File to Briefcase' dialog is displayed");

		// Dismiss dialog by clicking on Cancel button
		dlg.zClickButton(Button.B_CANCEL);

		// Delete file upon test completion
		app.zPageBriefcase.deleteFileById(id);
	}


	@Test( description = "Check Out File through SOAP - right click 'Discard Check Out'",
			groups = { "functional", "L2" })

	public void CheckInFile_02() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";
		FileItem file = new FileItem(filePath);
		String fileName = file.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>"
				+ "<doc l='" + briefcaseFolder.getId() + "'>" + "<upload id='"
				+ attachmentId + "'/>" + "</doc>" + "</SaveDocumentRequest>");

		// Search the uploaded file
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>" + "<query>" + fileName + "</query>"
				+ "</SearchRequest>");

		// Verify file name through SOAP
		String name = account.soapSelectValue("//mail:doc", "name");
		ZAssert.assertEquals(name, fileName, "Verify file name through SOAP");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Verify Lock icon is not present for the uploaded file
		ZAssert.assertFalse(app.zPageBriefcase.isLockIconPresent(file),"Verify Lock icon is not present for the uploaded file");

		// Check Out file through SOAP
		String id = account.soapSelectValue("//mail:doc", "id");
		account.soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>"
				+ "<action id='" + id + "' op='lock'/>"
				+ "</ItemActionRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Verify Lock icon is present for the checked out file
		ZAssert.assertTrue(app.zPageBriefcase.isLockIconPresent(file),"Verify Lock icon is present for the checked out file");

		// Right click on the locked file and select Discard Check Out context menu
		app.zPageBriefcase
				.zListItem(Action.A_RIGHTCLICK, Button.O_DISCARD_CHECK_OUT, file);

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Verify Lock icon is removed for the corresponding file
		ZAssert.assertFalse(app.zPageBriefcase.isLockIconPresent(file),"Verify Lock icon is not present for the uploaded file");

		// Delete file upon test completion
		app.zPageBriefcase.deleteFileById(id);
	}
}
