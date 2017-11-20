/*
 * ***** BEGIN LICENSE BLOCK *****
 *
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
 *
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.universal.tests.preferences.sharing;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCore;
import com.zimbra.qa.selenium.projects.universal.pages.preferences.TreePreferences.TreeItem;

public class AcceptShareFromPreferences extends UniversalCore {

	public AcceptShareFromPreferences() {

		super.startingPage = app.zPagePreferences;
			
	}

	@Test (description = "Accept shared folder from preferences/sharing", groups = { "functional", "L2" })

	public void AcceptShare_01() throws HarnessException {

		FolderItem inbox = FolderItem.importFromSOAP(ZimbraAccount.AccountB(), FolderItem.SystemFolder.Inbox);
		String foldername = "folder" + ConfigProperties.getUniqueString();
		

		// Create a subfolder in Inbox
		ZimbraAccount.AccountB().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
						+		"<folder name='" + foldername +"' l='" + inbox.getId() +"'/>"
						+	"</CreateFolderRequest>");

		FolderItem subfolder = FolderItem.importFromSOAP(ZimbraAccount.AccountB(), foldername);
		ZAssert.assertNotNull(subfolder, "Verify the new owner folder exists");

		// Share with user
		ZimbraAccount.AccountB().soapSend(
				"<FolderActionRequest xmlns='urn:zimbraMail'>"
						+		"<action id='"+ subfolder.getId() +"' op='grant'>"
						+			"<grant d='" + app.zGetActiveAccount().EmailAddress + "' gt='usr' perm='r'/>"
						+		"</action>"
						+	"</FolderActionRequest>");

		// Navigate to preferences -> sharing
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Sharing);
		
		// Add Share name to the search box
		app.zPagePreferences.sType("css=div#Prefs_Pages_Sharing_shareForm div[id$='_owner'] input", ZimbraAccount.AccountB().EmailAddress);

		// Click "Find Shares"
		app.zPagePreferences.sClick("css=div[id$='_findButton'] td[id$='_title']");

		// Select Accept and Yes to accept the share
		app.zPagePreferences.zWaitForBusyOverlay();
		app.zPagePreferences.sClick("css=div[id='zl__SVP__rows'] a[id$='_accept']"); //Accept locator
		SleepUtil.sleepSmall(); 
		app.zPagePreferences.sClick("css=td[id='ZmAcceptShare_button5_title']"); // 'Yes' button locator
		SleepUtil.sleepMedium(); 

		//Soap verification
		// Make sure that Active Account now has the share
		app.zGetActiveAccount().soapSend(
				"<GetShareInfoRequest xmlns='urn:zimbraAccount'>"
						+		"<grantee type='usr'/>"
						+		"<owner by='name'>"+ ZimbraAccount.AccountB().EmailAddress +"</owner>"
						+	"</GetShareInfoRequest>");

		String ownerEmail = app.zGetActiveAccount().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Inbox/"+ foldername +"']", "ownerEmail");
		ZAssert.assertEquals(ownerEmail, ZimbraAccount.AccountB().EmailAddress, "Verify the owner of the shared folder");

		//UI Verification
		//Make sure Active user name is present under 'Folder shares with me that I have accepted'

		
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent("css=div[id='Prefs_Pages_Sharing_mountedShares'] td[id$='_wi']:contains('" + app.zGetActiveAccount().EmailAddress + "')"), "Verify active user email id exists");
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent("css=div[id='Prefs_Pages_Sharing_mountedShares'] td[id$='_it']:contains('" + foldername  + "')"), "Verify shared folder name exists");

	}
}