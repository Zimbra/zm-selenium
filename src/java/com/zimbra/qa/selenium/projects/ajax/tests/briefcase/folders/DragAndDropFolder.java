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
package com.zimbra.qa.selenium.projects.ajax.tests.briefcase.folders;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.EnableBriefcaseFeature;

public class DragAndDropFolder extends EnableBriefcaseFeature {

	public DragAndDropFolder() {
		logger.info("New " + DragAndDropFolder.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
	}


	@Test (description = "Drag one briefcase sub-folder and Drop into other",
			groups = { "sanity" })

	public void DragAndDropFolder_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseRootFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		ZAssert.assertNotNull(briefcaseRootFolder, "Verify the Briefcase root folder is available");

		// Create two briefcase sub-folders:One folder to Drag & Another folder to drop into
		String briefcaseSubFolderName1 = "folder1" + ConfigProperties.getUniqueString();
		String briefcaseSubFolderName2 = "folder2" + ConfigProperties.getUniqueString();

		account.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + briefcaseSubFolderName1
				+ "' l='" + briefcaseRootFolder.getId() + "'/>" + "</CreateFolderRequest>");

		FolderItem briefcaseSubFolder1 = FolderItem.importFromSOAP(account, briefcaseSubFolderName1);
		ZAssert.assertNotNull(briefcaseSubFolder1, "Verify the first subfolder is available");

		account.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + briefcaseSubFolderName2
				+ "' l='" + briefcaseRootFolder.getId() + "'/>" + "</CreateFolderRequest>");

		FolderItem briefcaseSubFolder2 = FolderItem.importFromSOAP(account, briefcaseSubFolderName2);
		ZAssert.assertNotNull(briefcaseSubFolder2, "Verify the second subfolder is available");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseRootFolder, false);

		// Perform DND action
		app.zPageBriefcase.zDragAndDrop(
				"css=td#zti__main_Briefcase__" + briefcaseSubFolder1.getId() + "_textCell:contains("
						+ briefcaseSubFolder1.getName() + ")",
				"css=td#zti__main_Briefcase__" + briefcaseSubFolder2.getId() + "_textCell:contains("
						+ briefcaseSubFolder2.getName() + ")");

		// Verify the folder is now in the other sub-folder
		briefcaseSubFolder1 = FolderItem.importFromSOAP(account, briefcaseSubFolderName1);
		ZAssert.assertNotNull(briefcaseSubFolder1, "Verify the subfolder is still available");
		ZAssert.assertEquals(briefcaseSubFolder2.getId(), briefcaseSubFolder1.getParentId(),
				"Verify the subfolder's parent is now the other subfolder");
	}
}