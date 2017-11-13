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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.sharing;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;

public class AutoShareRemoval extends UniversalCommonTest {

	public AutoShareRemoval() {

		super.startingPage = app.zPagePreferences;
						
	}

	@Test (description = "check for autoshare removal prompt message and press 'ok' to revoke", groups = { "functional", "L3" })

	public void AutoShareRemoval_01() throws HarnessException {

		//*** Test Data
		ZimbraAccount delegate = new ZimbraAccount();
		delegate.provision();
		delegate.authenticate();

		// Create folder
		String ownerFoldername = "ownerfolder"+ ConfigProperties.getUniqueString();
		FolderItem ownerInbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox);
		ZAssert.assertNotNull(ownerInbox, "Verify the new owner folder exists");

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
						+		"<folder name='" + ownerFoldername +"' l='" + ownerInbox.getId() +"'/>"
						+	"</CreateFolderRequest>");

		FolderItem ownerFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), ownerFoldername);
		ZAssert.assertNotNull(ownerFolder, "Verify the new owner folder exists");

		// Share folder with delegate 
		app.zGetActiveAccount().soapSend(
				"<FolderActionRequest xmlns='urn:zimbraMail'>"
						+		"<action id='"+ ownerFolder.getId() +"' op='grant'>"
						+			"<grant d='" + delegate.EmailAddress + "' gt='usr' perm='r'/>"
						+		"</action>"
						+	"</FolderActionRequest>");

		// Delete delegete account using SOAP
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<DeleteAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ delegate.ZimbraId + "</id>"
			+	"</DeleteAccountRequest>");
		
		// Refresh
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Sharing);
	
		//UI verification
		
		// Verify that "Items have been share no longer exists" message is displayed
		ZAssert.assertStringContains(app.zPagePreferences.sGetText("css=td[id='MessageDialog_1_Msg'] p"), "no longer exists", "Verify 'ok/cancel' button is displayed in message prompt");
	
		// Verify share folder name is displayed in the message prompt
		ZAssert.assertStringContains(app.zPagePreferences.sGetText("css=td[id='MessageDialog_1_Msg'] li"), ownerFoldername, "Verify 'ok/cancel' button is displayed in message prompt");

		// Click 'OK' button at message prompt to revoke folder
		app.zPageMain.sClick("css=td[id='OkCancel_button2_title']"); //OK button locator
		SleepUtil.sleepSmall();
		
		// UI verification
		ZAssert.assertFalse(app.zPagePreferences.sIsElementPresent("css= div[id='Prefs_Pages_Sharing_sharesBy'] td[id$=_ac]:contains('Revoke')"), "Verify that 'Revoke' action not present");
		
	}

}
