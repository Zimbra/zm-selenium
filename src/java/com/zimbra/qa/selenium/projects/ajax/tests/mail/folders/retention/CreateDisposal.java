/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.folders.retention;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DialogEditFolder;

public class CreateDisposal extends SetGroupMailByMessagePreference {

	public CreateDisposal() {
		logger.info("New " + CreateDisposal.class.getCanonicalName());
	}


	@Test (description = "Save a new basic disposal on a folder (Context menu -> Edit -> Retention)",
			groups = { "smoke", "L1" })

	public void CreateDisposal_01() throws HarnessException {

		// Create the subfolder
		String foldername = "folder" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" +  FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox).getId() + "'/>"
				+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);
		ZAssert.assertNotNull(folder, "Verify the subfolder is available");

		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Rename the folder using context menu
		DialogEditFolder dialog = (DialogEditFolder) app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, folder);

		// Set to 2 years
		dialog.zDisposalEnable();
		dialog.zDisposalSetRange(
				DialogEditFolder.RetentionRangeType.Custom,
				DialogEditFolder.RetentionRangeUnits.Years,
				2);

		// Save
		dialog.zPressButton(Button.B_OK);

		// Verify the retention policy on the folder
		app.zGetActiveAccount().soapSend(
				"<GetFolderRequest xmlns='urn:zimbraMail'>"
			+		"<folder l='" + folder.getId() + "'/>"
			+	"</GetFolderRequest>");

		String lifetime = app.zGetActiveAccount().soapSelectValue("//mail:purge//mail:policy", "lifetime");
		String type = app.zGetActiveAccount().soapSelectValue("//mail:purge//mail:policy", "type");

		ZAssert.assertEquals(lifetime, "732d", "Verify the policy lifetime is set to 2 years");
		ZAssert.assertEquals(type, "user", "Verify the policy type is set to 'user'");
	}


	@DataProvider(name = "DataProviderRetentions")
	public Object[][] DataProviderRetentions() {
	  return new Object[][] {
			    new Object[] { DialogEditFolder.RetentionRangeUnits.Days, "2d" },
			    new Object[] { DialogEditFolder.RetentionRangeUnits.Weeks, "14d" },
			    new Object[] { DialogEditFolder.RetentionRangeUnits.Months, "62d" },
			    new Object[] { DialogEditFolder.RetentionRangeUnits.Years, "732d" },
	  };
	}

	@Test (description = "Create day, week, month, year disposals",
			groups = { "functional", "L2" },
			dataProvider = "DataProviderRetentions")

	public void CreateDisposal_02(DialogEditFolder.RetentionRangeUnits units, String expected) throws HarnessException {

		// Create the subfolder
		String foldername = "folder" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" +  FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox).getId() + "'/>"
				+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);
		ZAssert.assertNotNull(folder, "Verify the subfolder is available");

		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Rename the folder using context menu
		DialogEditFolder dialog = (DialogEditFolder) app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, folder);

		// Set to 2 years
		dialog.zDisposalEnable();
		dialog.zDisposalSetRange(
				DialogEditFolder.RetentionRangeType.Custom,
				units,
				2);

		// Save
		dialog.zPressButton(Button.B_OK);

		// Verify the retention policy on the folder
		app.zGetActiveAccount().soapSend(
				"<GetFolderRequest xmlns='urn:zimbraMail'>"
			+		"<folder l='" + folder.getId() + "'/>"
			+	"</GetFolderRequest>");

		String lifetime = app.zGetActiveAccount().soapSelectValue("//mail:purge//mail:policy", "lifetime");
		ZAssert.assertEquals(lifetime, expected, "Verify the policy lifetime is set correctly");
	}
}