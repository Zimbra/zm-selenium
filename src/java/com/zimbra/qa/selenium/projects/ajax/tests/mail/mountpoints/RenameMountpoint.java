/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mountpoints;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderMountpointItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogRenameFolder;

public class RenameMountpoint extends PrefGroupMailByMessageTest {

	public RenameMountpoint() {
		logger.info("New "+ RenameMountpoint.class.getCanonicalName());
	}


	@Test (description = "Rename a mountpoint (Right Click -> Rename)",
			groups = { "smoke", "L1" })

	public void RenameMountpoint_01() throws HarnessException {

		ZimbraAccount Owner = (new ZimbraAccount()).provision().authenticate();

		// Owner creates a folder, shares it with current user
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

		// Current user creates the mountpoint that points to the share
		String mountpointFoldername = "mountpoint"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointFoldername +"' view='message' rid='"+ ownerFolder.getId() +"' zid='"+ Owner.ZimbraId +"'/>"
				+	"</CreateMountpointRequest>");

		FolderMountpointItem mountpoint = FolderMountpointItem.importFromSOAP(app.zGetActiveAccount(), mountpointFoldername);
		ZAssert.assertNotNull(mountpoint, "Verify the subfolder is available");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		DialogRenameFolder dialog = (DialogRenameFolder)app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_RENAME, mountpoint);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		// Set the name, click OK
		String mountpointFoldername2 = "mountpoint" + ConfigProperties.getUniqueString();
		dialog.zSetNewName(mountpointFoldername2);
		dialog.zPressButton(Button.B_OK);

		// Get all the folders and verify the new name appears and the old name disappears
		app.zGetActiveAccount().soapSend("<GetFolderRequest xmlns = 'urn:zimbraMail'/>");

		Element[] eFolder1 = app.zGetActiveAccount().soapSelectNodes("//mail:link[@name='"+ mountpointFoldername +"']");
		ZAssert.assertEquals(eFolder1.length, 0, "Verify the old folder name no longer exists");

		Element[] eFolder2 = app.zGetActiveAccount().soapSelectNodes("//mail:link[@name='"+ mountpointFoldername2 +"']");
		ZAssert.assertEquals(eFolder2.length, 1, "Verify the new folder name exists");
	}
}