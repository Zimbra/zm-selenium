/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.mountpoints;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogShareFind;

public class FindShares extends AjaxCommonTest {
	
	public FindShares() {
		logger.info("New "+ FindShares.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
		
	}
	
	@Bugs(ids = "82125,84339")
	@Test (description = "Search for a share using 'Find Shares' and add it",
			groups = { "functional", "L2" })
			
	public void FindShares_01() throws HarnessException {
		
		ZimbraAccount Owner = new ZimbraAccount();
		Owner.provision();
		Owner.authenticate();
		
		// Owner creates a folder, shares it with current user
		String ownerFoldername = ConfigProperties.getUniqueString();
		
		FolderItem ownerCalendar = FolderItem.importFromSOAP(Owner, FolderItem.SystemFolder.Calendar);
		ZAssert.assertNotNull(ownerCalendar, "Verify the new owner folder exists");

		Owner.soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + ownerFoldername +"' l='" + ownerCalendar.getId() +"' view='appointment'/>"
				+	"</CreateFolderRequest>");
		
		FolderItem ownerFolder = FolderItem.importFromSOAP(Owner, ownerFoldername);
		ZAssert.assertNotNull(ownerFolder, "Verify the new owner folder exists");
		
		Owner.soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ ownerFolder.getId() +"' op='grant'>"
				+			"<grant d='" + app.zGetActiveAccount().EmailAddress + "' gt='usr' perm='r'/>"
				+		"</action>"
				+	"</FolderActionRequest>");
		
		// Click Refresh button
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		
		// Click Find Shares
		DialogShareFind dialog = (DialogShareFind)app.zTreeCalendar.zPressPulldown(Button.B_TREE_FOLDERS_OPTIONS, Button.B_TREE_FIND_SHARES);
		
		// Find shares from the user
		dialog.zSetFindEmail(Owner.EmailAddress);
		dialog.zClickButton(Button.B_SEARCH);

		// Check the folder item
		List<String> items = dialog.zListGetFolders();
		String found = null;
		for ( String f : items) {
			if ( f.contains(ownerFoldername) ) { 
				found = f;
				break;
			}
		}
		
		ZAssert.assertNotNull(found, "Verify the shared folder shows up in the tree");
		
		// Check the box and add the share
		dialog.zTreeItem(Action.A_TREE_CHECKBOX, found);

		dialog.zClickButton(Button.B_ADD);

		// Verify the mountpoint exists
		app.zGetActiveAccount().soapSend(
				"<GetFolderRequest xmlns='urn:zimbraMail'/>");
		
		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:link[@owner='"+ Owner.EmailAddress +"']");
		ZAssert.assertGreaterThan(nodes.length, 0, "Verify the mountpoint is listed in the folder tree");

		String rid = app.zGetActiveAccount().soapSelectValue("//mail:link[@owner='"+ Owner.EmailAddress +"']", "rid");
		ZAssert.assertEquals(rid, ownerFolder.getId(), "Verify the mountpoint is listed in the folder tree");
	}
}
