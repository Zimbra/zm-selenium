/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.sharing;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.PagePreferences.ShareItem;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class FindSharesUnaccepted extends AjaxCommonTest {

	public FindSharesUnaccepted() {
		super.startingPage = app.zPagePreferences;
	}


	@Test(	description = "View the sharing preference page - show unaccepted shares",
			groups = { "functional", "L2" })

	public void FindSharesUnaccepted_01() throws HarnessException {

		ZimbraAccount Owner = (new ZimbraAccount()).provision().authenticate();

		String ownerFoldername = "ownerfolder"+ ConfigProperties.getUniqueString();

		FolderItem ownerInbox = FolderItem.importFromSOAP(Owner, FolderItem.SystemFolder.Inbox);
		ZAssert.assertNotNull(ownerInbox, "Verify the new owner folder exists");

		Owner.soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + ownerFoldername +"' l='" + ownerInbox.getId() +"'/>"
				+	"</CreateFolderRequest>");

		FolderItem ownerFolder = FolderItem.importFromSOAP(Owner, ownerFoldername);
		ZAssert.assertNotNull(ownerFolder, "Verify the new owner folder exists");

		Owner.soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ ownerFolder.getId() +"' op='grant'>"
				+			"<grant d='" + app.zGetActiveAccount().EmailAddress + "' gt='usr' perm='r'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Refresh
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Sharing);

		// Add username to the search box
		app.zPagePreferences.sType("css=div#Prefs_Pages_Sharing_shareForm div[id$='_owner'] input", Owner.EmailAddress);

		// Click "Find Shares"
		app.zPagePreferences.zClick("css=div[id$='_findButton'] td[id$='_title']");

		// Wait for the list to be populated
		app.zPagePreferences.zWaitForBusyOverlay();

		// Verification
		ShareItem found = null;
		List<ShareItem> items = app.zPagePreferences.zSharesGetUnaccepted();
		for (ShareItem item : items) {
			if (Owner.EmailAddress.contains(item.name)) {
				found = item;
				break;
			}
		}

		ZAssert.assertNotNull(found, "verify the shared item appears in the list");
		ZAssert.assertStringContains(found.item, ownerFoldername, "Verify the owner foldername");
		ZAssert.assertEquals(found.type, "Folder", "Verify the owner item type");
		ZAssert.assertEquals(found.email, app.zGetActiveAccount().EmailAddress, "Verify the share email destination");
	}
}