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
package com.zimbra.qa.selenium.projects.universal.tests.mail.mountpoints.external;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogShare;
import com.zimbra.qa.selenium.projects.universal.ui.DialogShare.ShareWith;

public class CreateShare extends PrefGroupMailByMessageTest {

	public CreateShare() {
		logger.info("New "+ CreateShare.class.getCanonicalName());
	}
	
	@Test (description = "Share a folder - External", groups = { "smoke", "L1" })
	
	public void CreateShare_01() throws HarnessException {
		
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox);
		String foldername = "folder" + ConfigProperties.getUniqueString();
		String externalEmail = ConfigProperties.getStringProperty("external.yahoo.account");

		// Create a subfolder in Inbox
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername +"' l='" + inbox.getId() +"'/>"
				+	"</CreateFolderRequest>");
		String folderid = app.zGetActiveAccount().soapSelectValue("//mail:folder", "id");

		//Need to do Refresh to see folder in the list 
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		
		// Make sure the folder was created on the server
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);
		ZAssert.assertNotNull(subfolder, "Verify the folder exists on the server");

		// Right click on folder, select "Share"
		DialogShare dialog = (DialogShare)app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_SHARE, subfolder);
		ZAssert.assertNotNull(dialog, "Verify the sharing dialog pops up");

		// Use external and set the email
		dialog.zSetShareWith(ShareWith.ExternalGuests);
		dialog.zSetEmailAddress(externalEmail);
		
		// Send it
		dialog.zPressButton(Button.B_OK);
		
		// Verify the account has shared the folder
		app.zGetActiveAccount().soapSend(
				"<GetFolderRequest xmlns='urn:zimbraMail'>"
			+		"<folder l='" + folderid + "'/>"
			+	"</GetFolderRequest>");

		String zid = app.zGetActiveAccount().soapSelectValue("//mail:grant", "zid");
		ZAssert.assertEquals(zid, externalEmail, "Verify the zid of the shared folder is set to the external address");
		
		String gt = app.zGetActiveAccount().soapSelectValue("//mail:grant", "gt");
		ZAssert.assertEquals(gt, "guest", "Verify the gt of the shared folder is guest");

		String pw = app.zGetActiveAccount().soapSelectValue("//mail:grant", "pw");
		ZAssert.assertEquals(pw, "", "Verify the default pw is blank");

		String perm = app.zGetActiveAccount().soapSelectValue("//mail:grant", "perm");
		ZAssert.assertEquals(perm, "r", "Verify the perm of the shared folder is 'r'");

	}

	

	

}
