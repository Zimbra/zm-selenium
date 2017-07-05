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
package com.zimbra.qa.selenium.projects.universal.tests.tasks.mountpoints;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogShare;
import com.zimbra.qa.selenium.projects.universal.ui.DialogShare.ShareRole;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DialogEditFolder;

public class EditShare extends UniversalCommonTest {
	
	@SuppressWarnings("serial")
	public EditShare() {
		logger.info("New "+ EditShare.class.getCanonicalName());
		logger.info("New " + CreateShare.class.getCanonicalName());

		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String, String>() {
			{
				put("zimbraPrefTasksReadingPaneLocation", "bottom");
				put("zimbraPrefShowSelectionCheckbox", "TRUE");
			}
		};

	}

	@Test( description = "Share and edit folder with admin rights",
			groups = { "smoke", "L1"})
	public void EditShare_01() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Create the subTaskList
		String name = "taskList" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount()
		.soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+ "<folder name='" + name + "' l='"
				+ taskFolder.getId() + "'/>"
				+ "</CreateFolderRequest>");

		FolderItem subTaskList = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(subTaskList, "Verify the subfolder is available");

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Right click on folder, select "Share"
		DialogShare dialog = (DialogShare) app.zTreeTasks.zTreeItem(Action.A_RIGHTCLICK, Button.B_SHARE, subTaskList);
		ZAssert.assertNotNull(dialog, "Verify the sharing dialog pops up");

		// Use defaults for all options
		dialog.zSetEmailAddress(ZimbraAccount.AccountA().EmailAddress);

		// Send it
		dialog.zClickButton(Button.B_OK);

		// Make sure that AccountA now has the share
		ZimbraAccount.AccountA().soapSend(
				"<GetShareInfoRequest xmlns='urn:zimbraAccount'>"
				+ "<grantee type='usr'/>" + "<owner by='name'>"
				+ app.zGetActiveAccount().EmailAddress + "</owner>"
				+ "</GetShareInfoRequest>");

		String ownerEmail = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Tasks/"	+ name + "']", "ownerEmail");
		ZAssert.assertEquals(ownerEmail, app.zGetActiveAccount().EmailAddress,
				"Verify the owner of the shared folder");

		String rights = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Tasks/"	+ name + "']", "rights");
		ZAssert.assertEquals(rights, "r", "Verify the rights are 'read only'");

		String granteeType = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Tasks/"+ name + "']", "granteeType");
		ZAssert.assertEquals(granteeType, "usr","Verify the grantee type is 'user'");		

		//Need to do Refresh to see folder in the list 
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		//Edit
		//Right click folder, click Edit Properties
		DialogEditFolder editdialog = (DialogEditFolder)app.zTreeTasks.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, subTaskList);
		ZAssert.assertNotNull(editdialog, "Verify the sharing dialog pops up");

		//Click Edit link on Edit properties dialog
		DialogShare sharedialog = (DialogShare)editdialog.zClickButton(Button.O_EDIT_LINK);
		ZAssert.assertTrue(sharedialog.zIsActive(), "Verify that the Share dialog is active ");

		//Select Admin radio button
		sharedialog.zSetRole(ShareRole.Admin);

		//click ok
		sharedialog.zClickButton(Button.B_OK);

		//Verify Edit properties  dialog is active
		ZAssert.assertTrue(editdialog.zIsActive(), "Verify that the Edit Folder Properties dialog is active ");

		//click ok button from edit Folder properties dialog
		editdialog.zClickButton(Button.B_OK);		
		
		ZimbraAccount.AccountA().soapSend(
				"<GetShareInfoRequest xmlns='urn:zimbraAccount'>"
				+		"<grantee type='usr'/>"
				+		"<owner by='name'>"+ app.zGetActiveAccount().EmailAddress +"</owner>"
				+	"</GetShareInfoRequest>");

		String adminrights = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Tasks/"+ name +"']", "rights");

		//verify admin rights 	
		ZAssert.assertEquals(adminrights, "rwidxa", "Verify the rights are admin");

	}

}
