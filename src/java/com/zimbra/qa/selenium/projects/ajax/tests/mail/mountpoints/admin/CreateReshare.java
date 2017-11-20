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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mountpoints.admin;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.pages.*;

public class CreateReshare extends SetGroupMailByMessagePreference {

	public CreateReshare() {
		logger.info("New "+ CreateReshare.class.getCanonicalName());
	}


	/**
	 * 1. Account1 shares a folder with admin rights to Account2
	 * 2. Account2 re-shares the folder with Account3
	 * 3. Verify Account3 can view the folder contents
	 */

	@Bugs (ids = "68760")
	@Test (description = "Reshare a folder that is shared as admin",
			groups = { "functional", "L3" })

	public void CreateReshare_01() throws HarnessException {

		// Create the owner and destination accounts
		ZimbraAccount Owner = new ZimbraAccount();
		Owner.provision();
		Owner.authenticate();

		ZimbraAccount Destination = new ZimbraAccount();
		Destination.provision();
		Destination.authenticate();

		FolderItem ownerInbox = FolderItem.importFromSOAP(Owner, FolderItem.SystemFolder.Inbox);
		String ownerFoldername = "folder" + ConfigProperties.getUniqueString();

		// Owner shares a folder to the test account with admin rights
		Owner.soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + ownerFoldername +"' l='" + ownerInbox.getId() +"'/>"
				+	"</CreateFolderRequest>");
		String ownerFolderid = Owner.soapSelectValue("//mail:folder", "id");

		Owner.soapSend(
				"<FolderActionRequest xmlns='urn:zimbraMail'>"
			+		"<action id='"+ ownerFolderid +"' op='grant'>"
			+			"<grant d='" + app.zGetActiveAccount().EmailAddress + "' gt='usr' perm='rwidxa'/>"
			+		"</action>"
			+	"</FolderActionRequest>");

		// Test account creates a mountpoint
		String mountpointFoldername = "mountpoint"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointFoldername +"' view='message' rid='"+ ownerFolderid +"' zid='"+ Owner.ZimbraId +"'/>"
				+	"</CreateMountpointRequest>");

		FolderMountpointItem mountpoint = FolderMountpointItem.importFromSOAP(app.zGetActiveAccount(), mountpointFoldername);
		ZAssert.assertNotNull(mountpoint, "Verify the subfolder is available");

		// Refresh current view to see the new mountpoint
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		DialogShare dialog = (DialogShare)app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_SHARE, mountpoint);
		ZAssert.assertNotNull(dialog, "Verify the sharing dialog pops up");

		// Use defaults for all options
		dialog.zSetEmailAddress(Destination.EmailAddress);

		// Send it
		dialog.zPressButton(Button.B_OK);

		// Make sure that AccountA now has the share
		Destination.soapSend(
					"<GetShareInfoRequest xmlns='urn:zimbraAccount'>"
				+		"<grantee type='usr'/>"
				+		"<owner by='name'>"+ Owner.EmailAddress +"</owner>"
				+	"</GetShareInfoRequest>");

		String destinationPath = Destination.soapSelectValue("//acct:GetShareInfoResponse//acct:share[@ownerEmail='"+ Owner.EmailAddress +"']", "folderPath");
		ZAssert.assertNotNull(destinationPath, "Verify the share exists");
		ZAssert.assertStringContains(destinationPath, ownerFoldername, "Verify the test account has shared the folder");
	}
}