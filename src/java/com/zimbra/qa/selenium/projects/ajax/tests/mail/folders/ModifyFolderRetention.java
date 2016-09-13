/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.folders;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DialogEditFolder;

public class ModifyFolderRetention extends PrefGroupMailByMessageTest {

	public ModifyFolderRetention() {
		logger.info("New " + ModifyFolderRetention.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraFeatureSharingEnabled", "FALSE");
	}

	@Bugs( ids = "97126")
	@Test( description = "Modify a basic retention (Context menu -> Edit -> Retention) (zimbraFeatureSharingEnabled=FALSE)", 
			groups = { "functional" } )
	
	public void ModifyFolderRetention_01() throws HarnessException {

		//-- Data
		
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
			+				"<keep>"
			+					"<policy lifetime='5d' type='user'/>"
			+				"</keep>"
			+			"</retentionPolicy>"
			+		"</action>"
			+	"</FolderActionRequest>");

		//-- GUI
		
		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Rename the folder using context menu
		DialogEditFolder dialog = (DialogEditFolder) app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, folder);

		// Set to 4 years
		dialog.zNavigateToTab(DialogEditFolder.DialogTab.Retention);
		dialog.zRetentionSetRangeValue(6);

		// Save
		dialog.zClickButton(Button.B_OK);
		
		//-- Verification
		
		// Verify the retention policy on the folder
		app.zGetActiveAccount().soapSend(
				"<GetFolderRequest xmlns='urn:zimbraMail'>"
			+		"<folder l='" + folder.getId() + "'/>"
			+	"</GetFolderRequest>");
		String lifetime = app.zGetActiveAccount().soapSelectValue("//mail:keep//mail:policy", "lifetime");
		
		ZAssert.assertEquals(lifetime, "6d", "Verify the policy lifetime is set to 6 days");
		
	}
}
