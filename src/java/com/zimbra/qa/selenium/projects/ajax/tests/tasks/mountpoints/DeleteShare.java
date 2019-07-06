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
package com.zimbra.qa.selenium.projects.ajax.tests.tasks.mountpoints;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.items.FolderMountpointItem;
import com.zimbra.qa.selenium.framework.items.TaskItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogShare;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogShareAccept;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;

public class DeleteShare extends AjaxCore {
	
	public DeleteShare() {
		logger.info("New " + DeleteShare.class.getCanonicalName());
		super.startingPage = app.zPageTasks;
	}
	
	@Bugs(ids = "60990")
	@Test (description = "Delete a shared task folder and verify its presence in trash after deletion",
			groups = { "bhr" })

	public void DeleteShare_01() throws HarnessException {

		// Create the subTaskList
		String name = "taskList" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
						+ "<folder name='" + name + "' l='" + 1 + "' view='task' />"						
						+ "</CreateFolderRequest>");

		FolderItem taskList = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(taskList, "Verify the subfolder is available");
		
		//Create a task in the task list
		String subject = "task"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateTaskRequest xmlns='urn:zimbraMail'>" +
					"<m >" +
						"<l>"+ taskList.getId() +"</l>" +
			        	"<inv>" +
			        		"<comp name='"+ subject +"'>" +
			        			"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
			        		"</comp>" +
			        	"</inv>" +
			        	"<su>"+ subject +"</su>" +
			        	"<mp ct='text/plain'>" +
			        		"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
			        	"</mp>" +
			        	
					"</m>" +
				"</CreateTaskRequest>");
		
		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(task, "Verify the task is created");
		
		// Click tool bar refresh button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		
		// Right click on folder, select "Share"
		DialogShare dialog = (DialogShare) app.zTreeTasks.zTreeItem(Action.A_RIGHTCLICK, Button.B_SHARE, taskList);
		ZAssert.assertNotNull(dialog, "Verify the sharing dialog pops up");

		// Enter recipient email
		dialog.zSetEmailAddress(ZimbraAccount.AccountA().EmailAddress);

		// Send it
		dialog.zPressButton(Button.B_OK);

		// Make sure that AccountA now has the share
		ZimbraAccount.AccountA().soapSend(
				"<GetShareInfoRequest xmlns='urn:zimbraAccount'>"
						+ "<grantee type='usr'/>" + "<owner by='name'>"
						+ app.zGetActiveAccount().EmailAddress + "</owner>"
						+ "</GetShareInfoRequest>");

		String ownerEmail = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/"	+ name + "']", "ownerEmail");
		ZAssert.assertEquals(ownerEmail, app.zGetActiveAccount().EmailAddress,"Verify the owner of the shared folder");
		
		// Login with the account to which task is shared
		app.zPageLogin.zNavigateTo();
		app.zPageLogin.zLogin(ZimbraAccount.AccountA());
		
		// Switch to message view
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_BY_MESSAGE);
		// Select the item
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, name);

		// Verify that the A/D buttons are displayed
		ZAssert.assertTrue(display.zHasShareADButtons(), "Verify that the Accept/Decline share buttons are present");

		// Accept the share, which opens a dialog
		DialogShareAccept dialogShare = (DialogShareAccept)display.zPressButton(Button.B_ACCEPT_SHARE);
		ZAssert.assertNotNull(dialogShare, "Verify that the accept share dialog opens");

		// Click OK on the dialog
		dialogShare.zPressButton(Button.B_YES);
		
		app.zPageTasks.zNavigateTo();
		
		FolderItem found = null;

		// Verify that the new mount point is present
		logger.info("Looking for mountpoint containing text: "+ name);

		List<FolderItem> folders = app.zTreeTasks.zListGetFolders();
		for (FolderItem f : folders) {
			if ( f.getName().contains(name) ) {
				logger.info("Found folder item: "+ f.getName());
				found = f;
				break;
			}
		}

		ZAssert.assertNotNull(found, "Verify the mountpoint is in the folder list");
		
		// select the shared task list
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, found);
		ZAssert.assertTrue(app.zPageTasks.isPresent(subject),"Verify task from the shared folder is displayed");
		
		// Right click the task list, click Delete
		app.zTreeTasks.zTreeItem(Action.A_RIGHTCLICK, Button.B_DELETE, found);
		
		// Verify the task list is now in the trash
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		FolderMountpointItem sharedFolder = FolderMountpointItem.importFromSOAP(app.zGetActiveAccount(), found.getName());
		ZAssert.assertNotNull(sharedFolder, "Verify the subfolder is again available");
		ZAssert.assertEquals(trash.getId(), sharedFolder.getParentId(), "Verify the subfolder's parent is now the trash folder ID");
	}
}