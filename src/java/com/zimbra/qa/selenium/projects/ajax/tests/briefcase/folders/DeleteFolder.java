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
package com.zimbra.qa.selenium.projects.ajax.tests.briefcase.folders;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.FeatureBriefcaseTest;
import com.zimbra.qa.selenium.projects.ajax.ui.briefcase.DialogConfirm;

public class DeleteFolder extends FeatureBriefcaseTest {

	public DeleteFolder() {
		logger.info("New " + DeleteFolder.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
	}


	@Test (description = "Delete a briefcase sub-folder - Right click, Delete",
			groups = { "smoke", "L0" })

	public void DeleteFolder_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseRootFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		ZAssert.assertNotNull(briefcaseRootFolder, "Verify the Briefcase root folder is available");

		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		ZAssert.assertNotNull(trash, "Verify the trash is available");

		// Create the sub-folder
		String briefcaseSubFolderName = "folder" + ConfigProperties.getUniqueString();

		account.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + briefcaseSubFolderName
				+ "' l='" + briefcaseRootFolder.getId() + "'/>" + "</CreateFolderRequest>");

		FolderItem briefcaseSubFolder = FolderItem.importFromSOAP(account, briefcaseSubFolderName);
		ZAssert.assertNotNull(briefcaseSubFolder, "Verify the subfolder is available");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseRootFolder, false);

		// Delete the folder using context menu
		app.zTreeBriefcase.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_DELETE, briefcaseSubFolder);

		// Verify the folder is now in the trash
		briefcaseSubFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), briefcaseSubFolderName);
		ZAssert.assertNotNull(briefcaseSubFolder, "Verify the subfolder is again available");
		ZAssert.assertEquals(trash.getId(), briefcaseSubFolder.getParentId(),
				"Verify the subfolder's parent is now the trash folder ID");
	}


	@Test (description = "Delete a a top level briefcase folder - Right click, Delete",
			groups = { "smoke", "L1" })

	public void DeleteFolder_02() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseRootFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		FolderItem userRootFolder = FolderItem.importFromSOAP(account, SystemFolder.UserRoot);

		ZAssert.assertNotNull(userRootFolder, "Verify the user root folder is available");

		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		ZAssert.assertNotNull(trash, "Verify the trash is available");

		// Create a top level briefcase folder
		String briefcaseTopLevelFolderName = "folder" + ConfigProperties.getUniqueString();

		account.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + briefcaseTopLevelFolderName
				+ "' l='" + userRootFolder.getId() + "' view='document'/>" + "</CreateFolderRequest>");

		FolderItem briefcaseTopLevelFolder = FolderItem.importFromSOAP(account, briefcaseTopLevelFolderName);
		ZAssert.assertNotNull(briefcaseTopLevelFolder, "Verify the briefcase top level folder is available");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseRootFolder, false);

		// Delete the folder using context menu
		app.zTreeBriefcase.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_DELETE, briefcaseTopLevelFolder);

		// Verify the folder is now in the trash
		briefcaseTopLevelFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), briefcaseTopLevelFolderName);
		ZAssert.assertNotNull(briefcaseTopLevelFolder, "Verify the briefcase top level folder is again available");
		ZAssert.assertEquals(trash.getId(), briefcaseTopLevelFolder.getParentId(),
				"Verify the deleted briefcase top level folder's parent is now the trash folder ID");
	}


	@Bugs(ids = "80600")
	@Test (description = "Delete a briefcase sub-folder from list view and hitting toolbar delete button",
			groups = { "functional", "L2" })

	public void DeleteFolder_03() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseRootFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		ZAssert.assertNotNull(briefcaseRootFolder, "Verify the Briefcase root folder is available");

		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		ZAssert.assertNotNull(trash, "Verify the trash is available");

		// Create the sub-folder
		String briefcaseSubFolderName = "folder" + ConfigProperties.getUniqueString();

		account.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + briefcaseSubFolderName
				+ "' l='" + briefcaseRootFolder.getId() + "'/>" + "</CreateFolderRequest>");

		FolderItem briefcaseSubFolder = FolderItem.importFromSOAP(account, briefcaseSubFolderName);
		ZAssert.assertNotNull(briefcaseSubFolder, "Verify the subfolder is available");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseRootFolder, false);

		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, briefcaseSubFolder);

		// Click on Delete document icon in toolbar
		DialogConfirm deleteConfirm = (DialogConfirm) app.zPageBriefcase.zToolbarPressButton(Button.B_DELETE,
				briefcaseSubFolder);

		// Click OK on Confirmation dialog
		deleteConfirm.zPressButton(Button.B_YES);

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseRootFolder, false);

		// Verify the folder is now in the trash
		briefcaseSubFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), briefcaseSubFolderName);
		ZAssert.assertNotNull(briefcaseSubFolder, "Verify the subfolder is again available");
		ZAssert.assertEquals(trash.getId(), briefcaseSubFolder.getParentId(),
				"Verify the subfolder's parent is now the trash folder ID");
	}
}