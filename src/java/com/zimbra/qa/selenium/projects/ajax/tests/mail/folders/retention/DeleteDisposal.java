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
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DialogEditFolder;

public class DeleteDisposal extends SetGroupMailByMessagePreference {

	public DeleteDisposal() {
		logger.info("New " + DeleteDisposal.class.getCanonicalName());
	}

	@Test (description = "Delete a basic disposal (Context menu -> Edit -> Retention)",
			groups = { "functional", "L2" })

	public void DeleteDisposal_01() throws HarnessException {

		// Create the subfolder
		String foldername = "folder" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" +  FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox).getId() + "'/>"
				+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);
		ZAssert.assertNotNull(folder, "Verify the subfolder is available");

		// Add a retention policy
		app.zGetActiveAccount().soapSend(
				"<FolderActionRequest xmlns='urn:zimbraMail'>"
			+		"<action id='" + folder.getId() + "' op='retentionpolicy'>"
			+			"<retentionPolicy>"
			+				"<purge>"
			+					"<policy lifetime='5d' type='user'/>"
			+				"</purge>"
			+			"</retentionPolicy>"
			+		"</action>"
			+	"</FolderActionRequest>");

		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Rename the folder using context menu
		DialogEditFolder dialog = (DialogEditFolder) app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, folder);

		// Set to 4 years
		dialog.zNavigateToTab(DialogEditFolder.DialogTab.Disposal);
		dialog.zDisposalDisable();

		// Save
		dialog.zPressButton(Button.B_OK);

		// Verify the retention policy on the folder
		app.zGetActiveAccount().soapSend(
				"<GetFolderRequest xmlns='urn:zimbraMail'>"
			+		"<folder l='" + folder.getId() + "'/>"
			+	"</GetFolderRequest>");

		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:retentionPolicy");
		ZAssert.assertEquals(nodes.length, 0, "Verify no retention policies are set");
	}
}