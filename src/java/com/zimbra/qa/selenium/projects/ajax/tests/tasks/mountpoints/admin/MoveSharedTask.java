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
package com.zimbra.qa.selenium.projects.ajax.tests.tasks.mountpoints.admin;

import java.util.HashMap;
import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderMountpointItem;
import com.zimbra.qa.selenium.framework.items.TaskItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;

public class MoveSharedTask extends AjaxCore {

	@SuppressWarnings("serial")
	public MoveSharedTask() {
		logger.info("New "+ MoveSharedTask.class.getCanonicalName());
		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String, String>() {
			{
				put("zimbraPrefReadingPaneLocation", "bottom");
				put("zimbraPrefTasksReadingPaneLocation", "bottom");
				put("zimbraPrefGroupMailBy", "message");
				put("zimbraPrefShowSelectionCheckbox", "TRUE");
			}
		};
	}


	@Test (description = "Move task from shared folder to local task folder (admin rights)",
			groups = { "functional", "L2" })

	public void MoveTaskFromSharedToLocalFolder_01() throws HarnessException {

		String foldername = "tasklist" + ConfigProperties.getUniqueString();
		String subject = "subject" + ConfigProperties.getUniqueString();
		String mountpointname = "mountpoint" + ConfigProperties.getUniqueString();

		FolderItem task = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Tasks );
		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Create a folder to share
		ZimbraAccount.AccountA().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" + task.getId() + "'/>"
				+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), foldername);

		// Share it
		ZimbraAccount.AccountA().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidxa'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Add a task to it
		ZimbraAccount.AccountA().soapSend(
				"<CreateTaskRequest xmlns='urn:zimbraMail'>" +
				"<m l='"+ folder.getId() +"' >" +
			        	"<inv>" +
			        		"<comp name='"+ subject +"'>" +
			        			"<or a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>" +
			        		"</comp>" +
			        	"</inv>" +
			        	"<su>"+ subject +"</su>" +
			        	"<mp ct='text/plain'>" +
			        		"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
			        	"</mp>" +
					"</m>" +
				"</CreateTaskRequest>");

		app.zPageTasks.zToolbarPressButton(Button.B_REFRESH);
		TaskItem task1 = TaskItem.importFromSOAP(ZimbraAccount.AccountA(), subject);
		ZAssert.assertNotNull(task1, "Verify the task added");

		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointname +"' view='task' rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.AccountA().ZimbraId +"'/>"
				+	"</CreateMountpointRequest>");

		FolderMountpointItem mountpoint = FolderMountpointItem.importFromSOAP(app.zGetActiveAccount(), mountpointname);

		// Refresh the tasks view
		app.zPageTasks.zToolbarPressButton(Button.B_REFRESH);
		SleepUtil.sleepMedium();
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, task);

		// Click on the mountpoint
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, mountpoint);

		// Select the item
		app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject);

		app.zPageTasks.zToolbarPressPulldown(Button.B_MOVE, taskFolder);

		// Refresh tasks page
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK,mountpoint);

		List<TaskItem> tasks = app.zPageTasks.zGetTasks();
		TaskItem found = null;
		for (TaskItem t : tasks) {
			logger.info("Task: looking for " + subject + " found: "
					+ t.gSubject);
			if (subject.equals(t.gSubject)) {
				// Found it!
				found = t;
				break;
			}
		}

		ZAssert.assertNull(found,"Verify the  task no longer  present in the mounted folder");

		// Click on subfolder in tree view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);
		List<TaskItem> tasks1 = app.zPageTasks.zGetTasks();

		TaskItem movetask = null;
		for (TaskItem t : tasks1) {
			logger.info("Task: looking for " + subject + " found: "
					+ t.gSubject);
			if (subject.equals(t.gSubject)) {
				// Found it!
				movetask = t;
				break;
			}
		}
		ZAssert.assertNotNull(movetask,	"Verify the task is moved to the local folder");
	}


	@Test (description = "Move task from local task folder to shared folder(admin rights)",
			groups = { "functional", "L2" })

	public void MoveTaskFromLocalToSharedFolder_02() throws HarnessException {

		String foldername = "tasklist" + ConfigProperties.getUniqueString();
		String subject = "subject" + ConfigProperties.getUniqueString();
		String mountpointname = "mountpoint" + ConfigProperties.getUniqueString();

		FolderItem task = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Tasks );
		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Create a folder to share
		ZimbraAccount.AccountA().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" + task.getId() + "'/>"
				+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), foldername);

		// Share it
		ZimbraAccount.AccountA().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidxa'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		app.zGetActiveAccount().soapSend(
				"<CreateTaskRequest xmlns='urn:zimbraMail'>" +
					"<m >" +
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

		app.zPageTasks.zToolbarPressButton(Button.B_REFRESH);

		TaskItem task1 = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(task1, "Verify the task added");

		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointname +"' view='task' rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.AccountA().ZimbraId +"'/>"
				+	"</CreateMountpointRequest>");

		FolderMountpointItem mountpoint = FolderMountpointItem.importFromSOAP(app.zGetActiveAccount(), mountpointname);

		// Refresh the tasks view
		app.zPageTasks.zToolbarPressButton(Button.B_REFRESH);
		SleepUtil.sleepMedium();
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, task);

		// Select the item
		app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject);

		app.zPageTasks.zToolbarPressPulldown(Button.B_MOVE, mountpoint);

		// Refresh tasks page
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK,taskFolder);

		List<TaskItem> tasks = app.zPageTasks.zGetTasks();
		TaskItem found = null;
		for (TaskItem t : tasks) {
			logger.info("Task: looking for " + subject + " found: "
					+ t.gSubject);
			if (subject.equals(t.gSubject)) {
				// Found it!
				found = t;
				break;
			}
		}

		ZAssert.assertNull(found,"Verify the  task is no longer  present in the task list");

		// Click on subfolder in tree view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, mountpoint);
		List<TaskItem> tasks1 = app.zPageTasks.zGetTasks();

		TaskItem movetask = null;
		for (TaskItem t : tasks1) {
			logger.info("Task: looking for " + subject + " found: "
					+ t.gSubject);
			if (subject.equals(t.gSubject)) {
				// Found it!
				movetask = t;
				break;
			}
		}
		ZAssert.assertNotNull(movetask,	"Verify the task is moved to the mounted/shared folder");
	}
}