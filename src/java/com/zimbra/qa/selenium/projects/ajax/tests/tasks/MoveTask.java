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
package com.zimbra.qa.selenium.projects.ajax.tests.tasks;

import java.util.HashMap;
import java.util.List;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.AppAjaxClient;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogMove;
import com.zimbra.qa.selenium.projects.ajax.ui.tasks.DialogCreateTaskFolder;

public class MoveTask extends AjaxCommonTest {

	@SuppressWarnings("serial")
	public MoveTask() {
		logger.info("Move " + EditTask.class.getCanonicalName());

		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String , String>() {{
			put("zimbraPrefShowSelectionCheckbox", "TRUE");
			put("zimbraPrefTasksReadingPaneLocation", "bottom");
		}};
	}


	@Test( description = "Create task through SOAP - move & verify through GUI",
			groups = { "smoke", "L0"})

	public void MoveTask_01() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);
		String name = "TaskFolder" + ConfigProperties.getUniqueString();

		// Create a subfolder to move the message into i.e. tasks/subfolder
		String taskFolderID = taskFolder.getId();

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + name + "' l='" + taskFolderID + "'/>"
				+	"</CreateFolderRequest>");

		FolderItem subFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Refresh task page
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		String subject = "task" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateTaskRequest xmlns='urn:zimbraMail'>"
				+		"<m >"
				+			"<inv>"
				+				"<comp name='" + subject + "'>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>" + subject + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content" + ConfigProperties.getUniqueString() + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateTaskRequest>");

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(),subject);
		ZAssert.assertNotNull(task, "Verify the task is created");

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		app.zPageTasks.zToolbarPressPulldown(Button.B_MOVE, subFolder);

		// Refresh tasks page
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

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

		ZAssert.assertNull(found,"Verify the  task no longer  present in the task list http://bugzilla.zimbra.com/show_bug.cgi?id=61471");

		// Click on subfolder in tree view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, subFolder);
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
		ZAssert.assertNotNull(movetask,	"Verify the task is moved to the selected folder");
	}


	@Test( description = "Move a task by selecting task, then click 'm' shortcut",
			groups = { "functional", "L2"})

	public void MoveTask_02() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);
		String name = "TaskFolder" + ConfigProperties.getUniqueString();

		// Create a subfolder to move the message into i.e. tasks/subfolder
		String taskFolderID = taskFolder.getId();

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + name + "' l='" + taskFolderID + "'/>"
				+	"</CreateFolderRequest>");

		FolderItem subFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Refresh task page
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		String subject = "task" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateTaskRequest xmlns='urn:zimbraMail'>"
				+		"<m >"
				+			"<inv>"
				+				"<comp name='" + subject + "'>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>" + subject + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content" + ConfigProperties.getUniqueString() + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateTaskRequest>");

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(),subject);
		ZAssert.assertNotNull(task, "Verify the task is created");

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Click the Move keyboard shortcut
		DialogMove chooseFolder = (DialogMove) app.zPageTasks.zKeyboardShortcut(Shortcut.S_MOVE);
		chooseFolder.zClickTreeFolder(subFolder);
		chooseFolder.zClickButton(Button.B_OK);

		// Refresh tasks page
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

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

		ZAssert.assertNull(found,"Verify the  task no longer  present in the task list");

		// Click on subfolder in tree view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, subFolder);
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
		ZAssert.assertNotNull(movetask,	"Verify the task is moved to the selected folder");
	}


	@Test( description = "Create task through SOAP - move using Right Click Context Menu & verify through GUI",
			groups = { "smoke", "L1"})

	public void MoveTask_03() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		String name = "TaskFolder" + ConfigProperties.getUniqueString();

		// Create a subfolder to move the message into i.e. tasks/subfolder
		String taskFolderID = taskFolder.getId();

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + name + "' l='" + taskFolderID + "'/>"
				+	"</CreateFolderRequest>");

		FolderItem subFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Refresh task page
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		String subject = "task" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateTaskRequest xmlns='urn:zimbraMail'>"
				+		"<m >"
				+			"<inv>"
				+				"<comp name='" + subject + "'>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>" + subject + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content" + ConfigProperties.getUniqueString() + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateTaskRequest>");

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(),subject);
		ZAssert.assertNotNull(task, "Verify the task is created");

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Click the Move keyboard shortcut
		DialogMove chooseFolder = (DialogMove) app.zPageTasks.zListItem(Action.A_RIGHTCLICK, Button.O_MOVE_MENU, subject);
		chooseFolder.zClickTreeFolder(subFolder);
		chooseFolder.zClickButton(Button.B_OK);

		// Refresh tasks page
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

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

		ZAssert.assertNull(found,"Verify the  task no longer  present in the task list");

		// Click on subfolder in tree view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, subFolder);
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
		ZAssert.assertNotNull(movetask,	"Verify the task is moved to the selected folder");
	}


	@Test( description = "Move a task by using Move -> New folder & verify through GUI",
			groups = { "functional", "L2"})

	public void MoveTask_04() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);
		String name = "TaskFolder" + ConfigProperties.getUniqueString();
		String subject = "task" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateTaskRequest xmlns='urn:zimbraMail'>"
				+		"<m >"
				+			"<inv>"
				+				"<comp name='" + subject + "'>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>" + subject + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content" + ConfigProperties.getUniqueString() + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateTaskRequest>");

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(),subject);
		ZAssert.assertNotNull(task, "Verify the task is created");

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Click the Move dropdown and enter new task list name
		DialogCreateTaskFolder createTaskFolderDialog =(DialogCreateTaskFolder)app.zPageTasks.zToolbarPressPulldown(Button.B_MOVE, Button.O_NEW_TASK_FOLDER);

		createTaskFolderDialog.zEnterFolderName(name);
		createTaskFolderDialog.zClickButton(Button.B_OK);

		// Refresh tasks page
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Verify the  task no longer  present in the task list
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

		ZAssert.assertNull(found,"Verify the  task no longer  present in the task list");

		FolderItem newTasklist = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Click on subfolder in tree view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, newTasklist);
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
		ZAssert.assertNotNull(movetask,	"Verify the task is moved to the newTasklist");
	}


	@AfterMethod(groups = { "always" })
	public void afterMethod() throws HarnessException {
		logger.info("Checking for the Move Dialog ...");

		// Check if the "Move Dialog is still open
		DialogMove dialog = new DialogMove(app, ((AppAjaxClient)app).zPageTasks);
		if (dialog.zIsActive()) {
			logger.warn(dialog.myPageName()
					+ " was still active.  Cancelling ...");
			dialog.zClickButton(Button.B_CANCEL);
		}

	}
}