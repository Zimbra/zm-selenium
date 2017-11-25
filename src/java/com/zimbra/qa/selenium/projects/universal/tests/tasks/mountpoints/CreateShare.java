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
import com.zimbra.qa.selenium.projects.universal.core.UniversalCore;
import com.zimbra.qa.selenium.projects.universal.pages.DialogShare;
import com.zimbra.qa.selenium.projects.universal.pages.DialogShare.ShareRole;

public class CreateShare extends UniversalCore {

	@SuppressWarnings("serial")
	public CreateShare() {
		logger.info("New " + CreateShare.class.getCanonicalName());

		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String, String>() {
			{
				put("zimbraPrefTasksReadingPaneLocation", "bottom");
				put("zimbraPrefShowSelectionCheckbox", "TRUE");
			}
		};
	}

	@Test (description = "Share a Tasklist  - Viewer", 
			groups = { "smoke", "L1" })
	public void CreateShare_01() throws HarnessException {

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
		dialog.zPressButton(Button.B_OK);

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

	}

	@Test (description = "Share a task folder - Manager rights", 
			groups = { "smoke", "L1" })
	public void CreateShare_02() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Create the subTaskList
		String name = "taskList" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
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

		dialog.zSetRole(ShareRole.Manager);

		// Send it
		dialog.zPressButton(Button.B_OK);

		// Make sure that AccountA now has the share
		ZimbraAccount.AccountA().soapSend(
				"<GetShareInfoRequest xmlns='urn:zimbraAccount'>"
				+ "<grantee type='usr'/>" + "<owner by='name'>"
				+ app.zGetActiveAccount().EmailAddress + "</owner>"
				+ "</GetShareInfoRequest>");
		
		String ownerEmail = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Tasks/"+ name + "']", "ownerEmail");
		ZAssert.assertEquals(ownerEmail, app.zGetActiveAccount().EmailAddress, "Verify the owner of the shared folder");

		String rights = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Tasks/"+ name + "']", "rights");
		
		// "Verify the rights are 'Manager'");
		ZAssert.assertStringContains(rights, "r","Verify the rights are 'Manager'");
		ZAssert.assertStringContains(rights, "w","Verify the rights are 'Manager'");
		ZAssert.assertStringContains(rights, "i","Verify the rights are 'Manager'");
		ZAssert.assertStringContains(rights, "d","Verify the rights are 'Manager'");
		ZAssert.assertStringContains(rights, "x","Verify the rights are 'Manager'");

		String granteeType = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Tasks/"+ name + "']", "granteeType");
		ZAssert.assertEquals(granteeType, "usr","Verify the grantee type is 'user'");

	}
	
	@Test (description = "Share a task folder - Admin rights", 
			groups = { "smoke", "L1" })
	public void CreateShare_03() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Create the subTaskList
		String name = "taskList" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
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

		dialog.zSetRole(ShareRole.Admin);

		// Send it
		dialog.zPressButton(Button.B_OK);

		// Make sure that AccountA now has the share
		ZimbraAccount.AccountA().soapSend(
				"<GetShareInfoRequest xmlns='urn:zimbraAccount'>"
				+ "<grantee type='usr'/>" + "<owner by='name'>"
				+ app.zGetActiveAccount().EmailAddress + "</owner>"
				+ "</GetShareInfoRequest>");
		
		String ownerEmail = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Tasks/"+ name + "']", "ownerEmail");
		ZAssert.assertEquals(ownerEmail, app.zGetActiveAccount().EmailAddress, "Verify the owner of the shared folder");

		String rights = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Tasks/"+ name + "']", "rights");
		
		// "Verify the rights are 'Manager'");
		ZAssert.assertStringContains(rights, "r","Verify the rights are 'Admin'");
		ZAssert.assertStringContains(rights, "w","Verify the rights are 'Admin'");
		ZAssert.assertStringContains(rights, "i","Verify the rights are 'Admin'");
		ZAssert.assertStringContains(rights, "d","Verify the rights are 'Admin'");
		ZAssert.assertStringContains(rights, "x","Verify the rights are 'Admin'");
		ZAssert.assertStringContains(rights, "a","Verify the rights are 'Admin'");

		String granteeType = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Tasks/"+ name + "']", "granteeType");
		ZAssert.assertEquals(granteeType, "usr","Verify the grantee type is 'user'");

	}

}
