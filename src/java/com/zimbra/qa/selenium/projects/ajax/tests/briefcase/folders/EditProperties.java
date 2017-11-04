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
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.FeatureBriefcaseTest;
import com.zimbra.qa.selenium.projects.ajax.ui.briefcase.DialogEditProperties;

public class EditProperties extends FeatureBriefcaseTest {

	public EditProperties() {
		logger.info("New " + EditProperties.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
	}


	@Test( description = "Edit Properties - Rename folder using context menu",
			groups = { "functional", "L2" })

	public void EditProperties_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseRootFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		ZAssert.assertNotNull(briefcaseRootFolder, "Verify the Briefcase root folder is available");

		// Create the sub-folder
		String subFolderName = "folder" + ConfigProperties.getUniqueString();

		account.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + subFolderName + "' l='"
				+ briefcaseRootFolder.getId() + "'/>" + "</CreateFolderRequest>");

		// Verify the sub-folder exists on the server
		FolderItem subFolder = FolderItem.importFromSOAP(account, subFolderName);
		ZAssert.assertNotNull(subFolder, "Verify the subfolder is available");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseRootFolder, false);

		// Rename the folder using context menu
		DialogEditProperties dialog = (DialogEditProperties) app.zTreeBriefcase.zTreeItem(Action.A_RIGHTCLICK,
				Button.B_TREE_EDIT_PROPERTIES, subFolder);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		// Set the name, click OK
		String subFolderName2 = "renamedfolder" + ConfigProperties.getUniqueString();

		dialog.zSetNewName(subFolderName2);

		dialog.zClickButton(Button.B_OK);

		// Get all the folders and verify the new name appears and the old name disappears
		account.soapSend("<GetFolderRequest xmlns = 'urn:zimbraMail'/>");

		Element[] eFolder1 = account.soapSelectNodes("//mail:folder[@name='" + subFolderName + "']");
		ZAssert.assertEquals(eFolder1.length, 0, "Verify the old folder name no longer exists");

		Element[] eFolder2 = app.zGetActiveAccount().soapSelectNodes("//mail:folder[@name='" + subFolderName2 + "']");
		ZAssert.assertEquals(eFolder2.length, 1, "Verify the new folder name exists");

	}
}