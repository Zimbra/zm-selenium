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

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogShareRevoke;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DialogEditFolder;


public class RevokeShare extends PrefGroupMailByMessageTest {

	public RevokeShare() {
		logger.info("New "+ RevokeShare.class.getCanonicalName());
		
		
	}
	
	@Test( description = "Revoke a folder share - External",
			groups = { "smoke", "L1" })
	public void RevokeShare_01() throws HarnessException {
		
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox);
		String foldername = "folder" + ConfigProperties.getUniqueString();
		String externalEmail = ConfigProperties.getStringProperty("external.yahoo.account");

		// Create a subfolder in Inbox
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername +"' l='" + inbox.getId() +"'/>"
				+	"</CreateFolderRequest>");
		String folderid = app.zGetActiveAccount().soapSelectValue("//mail:folder", "id");

		// Create a subfolder in Inbox
		app.zGetActiveAccount().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folderid +"' op='grant'>"
				+			"<grant d='"+ externalEmail +"' inh='1' gt='guest' pw='' perm='r'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		//Need to do Refresh to see folder in the list 
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		
		// Make sure the folder was created on the server
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);
		ZAssert.assertNotNull(subfolder, "Verify the folder exists on the server");


		// Right click on folder, select "Edit"
		DialogEditFolder editdialog = (DialogEditFolder)app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, subfolder);
		ZAssert.assertNotNull(editdialog, "Verify the edit dialog pops up");

		//Click Edit link on Edit properties dialog
		DialogShareRevoke sharedialog = (DialogShareRevoke)editdialog.zClickButton(Button.O_REVOKE_LINK);
		ZAssert.assertTrue(sharedialog.zIsActive(), "Verify that the share dialog pops up");

		//click Yes
		sharedialog.zClickButton(Button.B_YES);
		
		
		// Verify the account has shared the folder
		app.zGetActiveAccount().soapSend(
					"<GetFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder l='" + folderid + "'/>"
				+	"</GetFolderRequest>");
		
		/** Example response:
		 *     <GetFolderResponse xmlns="urn:zimbraMail">
		 *           <folder f="i" rev="2" i4next="258" i4ms="2" ms="4" n="0" activesyncdisabled="0" l="2" id="257" s="0" name="folder13379798458754" uuid="a4d8c530-d8f5-46e2-9798-c87c86968c82" luuid="9dce7c49-ec67-4315-868f-bbf090605034">
		 *             <acl guestGrantExpiry="1345755322480">
		 *               <grant zid="zimbraexternal@yahoo.com" gt="guest" pw="" perm="r"/>
		 *             </acl>
 		 *          </folder>
		 *         </GetFolderResponse>
		 * 
		 **/

		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:grant");
		ZAssert.assertEquals(nodes.length, 0, "Verify the folder is not granted");

	}

	

	

}
