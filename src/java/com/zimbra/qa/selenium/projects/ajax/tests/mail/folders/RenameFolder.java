/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.ZimbraCharsets.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogError;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogRenameFolder;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogError.DialogErrorID;

public class RenameFolder extends SetGroupMailByMessagePreference {

	public RenameFolder() {
		logger.info("New "+ RenameFolder.class.getCanonicalName());
	}


	@Test (description = "Rename a folder - Context menu -> Rename",
			groups = { "smoke", "L1" })

	public void RenameFolder_01() throws HarnessException {

		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		ZAssert.assertNotNull(inbox, "Verify the inbox is available");

		// Create the subfolder
		String name1 = "folder" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ name1 +"' l='"+ inbox.getId() +"'/>" +
                "</CreateFolderRequest>");

		FolderItem subfolder1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name1);
		ZAssert.assertNotNull(subfolder1, "Verify the subfolder is available");

		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Rename the folder using context menu
		DialogRenameFolder dialog = (DialogRenameFolder)app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_RENAME, subfolder1);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		// Set the name, click OK
		String name2 = "folder" + ConfigProperties.getUniqueString();
		dialog.zSetNewName(name2);
		dialog.zPressButton(Button.B_OK);

		// Get all the folders and verify the new name appears and the old name disappears
		app.zGetActiveAccount().soapSend("<GetFolderRequest xmlns = 'urn:zimbraMail'/>");

		Element[] eFolder1 = app.zGetActiveAccount().soapSelectNodes("//mail:folder[@name='"+ name1 +"']");
		ZAssert.assertEquals(eFolder1.length, 0, "Verify the old folder name no longer exists");

		Element[] eFolder2 = app.zGetActiveAccount().soapSelectNodes("//mail:folder[@name='"+ name2 +"']");
		ZAssert.assertEquals(eFolder2.length, 1, "Verify the new folder name exists");
	}


	@Test (description = "Rename a folder - set to an invalid name with ':'",
			groups = { "functional", "L2" })

	public void RenameFolder_02() throws HarnessException {

		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		ZAssert.assertNotNull(inbox, "Verify the inbox is available");

		// Create the subfolder
		String name1 = "folder" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ name1 +"' l='"+ inbox.getId() +"'/>" +
                "</CreateFolderRequest>");

		FolderItem subfolder1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name1);
		ZAssert.assertNotNull(subfolder1, "Verify the subfolder is available");

		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Rename the folder using context menu
		DialogRenameFolder dialog = (DialogRenameFolder)app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_RENAME, subfolder1);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		// Set the name, click OK
		String name2 = "folder:folder" + ConfigProperties.getUniqueString();
		dialog.zSetNewName(name2);
		dialog.zPressButton(Button.B_OK);

		DialogError error = app.zPageMain.zGetErrorDialog(DialogErrorID.InvalidFolderName);
		ZAssert.assertTrue(error.zIsActive(), "Verify the error dialog appears");

		error.zPressButton(Button.B_OK);
		if ( dialog.zIsActive() ) {
			dialog.zPressButton(Button.B_CANCEL);
		}
	}


	@Test (description = "Get a folder with non-ASCII special characters",
			groups = { "functional", "L2" }, dataProvider = "DataProviderSupportedCharsets")

	public void RenameFolder_03(ZCharset charset, String foldername) throws HarnessException {

		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		ZAssert.assertNotNull(inbox, "Verify the inbox is available");

		// Create the subfolder
		String name1 = "folder" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ name1 +"' l='"+ inbox.getId() +"'/>" +
                "</CreateFolderRequest>");

		FolderItem subfolder1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name1);
		ZAssert.assertNotNull(subfolder1, "Verify the subfolder is available");

		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Rename the folder using context menu
		DialogRenameFolder dialog = (DialogRenameFolder)app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_RENAME, subfolder1);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		// Set the name, click OK
		dialog.zSetNewName(foldername);
		dialog.zPressButton(Button.B_OK);

		// Get all the folders and verify the new name appears and the old name disappears
		app.zGetActiveAccount().soapSend("<GetFolderRequest xmlns = 'urn:zimbraMail'/>");

		Element[] eFolder1 = app.zGetActiveAccount().soapSelectNodes("//mail:folder[@name='"+ name1 +"']");
		ZAssert.assertEquals(eFolder1.length, 0, "Verify the old folder name no longer exists");

		Element[] eFolder2 = app.zGetActiveAccount().soapSelectNodes("//mail:folder[@name='"+ foldername +"']");
		ZAssert.assertEquals(eFolder2.length, 1, "Verify the new folder name exists");
	}
}