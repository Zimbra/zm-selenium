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
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.TaskItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.pages.tasks.DisplayTask;
import com.zimbra.qa.selenium.projects.ajax.pages.tasks.FormTaskNew;
import com.zimbra.qa.selenium.projects.ajax.pages.tasks.FormTaskNew.Field;

public class EditHtmlTask extends AjaxCore{

	@SuppressWarnings("serial")
	public EditHtmlTask() {
		logger.info("Edit " + EditHtmlTask.class.getCanonicalName());

		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String , String>() {{
			put("zimbraPrefShowSelectionCheckbox", "TRUE");
			put("zimbraPrefTasksReadingPaneLocation", "bottom");
		}};
	}


	@Test (description = "Create Html task through SOAP - edit subject and verify through Soap",
			groups = { "smoke", "L0" })

	public void EditHtmlTask_01() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		String subject = "task"+ ConfigProperties.getUniqueString();
		String editSubject = "Edittask"+ ConfigProperties.getUniqueString();

		String taskHtmlbody = "task<b>bold"+ ConfigProperties.getUniqueString()+"</b>task";
		String editTaskHtmlbody = "Edittask"+ ConfigProperties.getUniqueString();
		String contentHTML = XmlStringUtil.escapeXml("<html>"+"<body>"+"<div>"+taskHtmlbody+"</div>"+"</body>"+"</html>");

		app.zGetActiveAccount().soapSend(
				"<CreateTaskRequest xmlns='urn:zimbraMail'>" +
				"<m >" +
				"<inv>" +
				"<comp name='"+ subject +"'>" +
				"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
				"</comp>" +
				"</inv>" +
				"<su>"+ subject +"</su>" +
				"<mp ct='multipart/alternative'>" +
				"<mp ct='text/plain'>" +
				"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
				"</mp>" +
				"<mp ct='text/html'>" +
				"<content>"+contentHTML+"</content>" +
				"</mp>" +
				"</mp>" +
				"</m>" +
		"</CreateTaskRequest>");

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertStringContains(task.getHtmlTaskBody().trim().toLowerCase(), taskHtmlbody.trim(), "Verify the html content of task body");

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Select the item
		app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject);

		// Edit task
		FormTaskNew taskedit = (FormTaskNew) app.zPageTasks.zToolbarPressButton(Button.B_EDIT);
		taskedit.zFillField(Field.Subject, editSubject);
		taskedit.zFillField(Field.HtmlBody, editTaskHtmlbody);
		taskedit.zSubmit();

		// Verify the Edited task present in the task list
		TaskItem task1 = TaskItem.importFromSOAP(app.zGetActiveAccount(), editSubject);
		ZAssert.assertEquals(task1.getName(), editSubject, "Verify edited task subject");
		ZAssert.assertStringContains(task1.getHtmlTaskBody().trim(), editTaskHtmlbody.trim(), "Verify the Edited task present in the task list");
	}


	/**
	 * 	1. Go to Tasks
	 * 	2. Create a new html task with no due date
	 * 	3. Refresh list view to see new task
	 * 	4. Edit html task and add due date
	 * 	5. Refresh list view again
	 * 	   Expected result:Task should show due date
	 */

	@Bugs (ids = "64647")
	@Test (description = "Create Html task through SOAP - edit due date >> Refresh task >>verify Due Date in list view through GUI",
			groups = { "smoke", "L1" })

	public void EditHtmlTask_02() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);
		FolderItem trashFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);

		String subject = "task"+ ConfigProperties.getUniqueString();
		ZDate dueDate      = new ZDate(2015, 11, 17, 12, 0, 0);

		// Create task
		String taskHtmlbody = "task<b>bold"+ ConfigProperties.getUniqueString()+"</b>task";
		String contentHTML = XmlStringUtil.escapeXml("<html>"+"<body>"+"<div>"+taskHtmlbody+"</div>"+"</body>"+"</html>");

		app.zGetActiveAccount().soapSend(
				"<CreateTaskRequest xmlns='urn:zimbraMail'>" +
				"<m >" +
				"<inv>" +
				"<comp name='"+ subject +"'>" +
				"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
				"</comp>" +
				"</inv>" +
				"<su>"+ subject +"</su>" +
				"<mp ct='multipart/alternative'>" +
				"<mp ct='text/plain'>" +
				"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
				"</mp>" +
				"<mp ct='text/html'>" +
				"<content>"+contentHTML+"</content>" +
				"</mp>" +
				"</mp>" +
				"</m>" +
		"</CreateTaskRequest>");

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertStringContains(task.getHtmlTaskBody().trim().toLowerCase(), taskHtmlbody.trim(), "Verify the html content of task body");

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Select the item
		app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject);

		// Click edit
		FormTaskNew taskedit = (FormTaskNew) app.zPageTasks.zToolbarPressButton(Button.B_EDIT);

		// Fill due date field
		taskedit.zFillField(Field.DueDate, dueDate.toMM_DD_YYYY());
		taskedit.zSubmit();

		DisplayTask actual = (DisplayTask) app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject);

		// Verify Due Date before refresh
		ZAssert.assertEquals(actual.zGetTaskListViewProperty(com.zimbra.qa.selenium.projects.ajax.pages.tasks.DisplayTask.Field.DueDate), dueDate.toMM_DD_YYYY(), "Verify the due date matches");

		// Click on Trash folder
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, trashFolder);

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Verify the due date matches after refresh
		ZAssert.assertEquals(actual.zGetTaskListViewProperty(com.zimbra.qa.selenium.projects.ajax.pages.tasks.DisplayTask.Field.DueDate), dueDate.toMM_DD_YYYY(), "Verify the due date matches after refresh");
	}


	@Test (description = "Create Html task through SOAP - Edit html task using Right Click Context Menu & verify through GUI",
			groups = { "smoke", "L1" })

	public void EditHtmlTask_03() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);
		String subject = "task"+ ConfigProperties.getUniqueString();
		String editSubject = "Edittask"+ ConfigProperties.getUniqueString();
		String taskHtmlbody = "task<b>bold"+ ConfigProperties.getUniqueString()+"</b>task";
		String editTaskHtmlbody = "task<b>bold"+ ConfigProperties.getUniqueString()+"</b>task";
		String contentHTML = XmlStringUtil.escapeXml("<html>"+"<body>"+"<div>"+taskHtmlbody+"</div>"+"</body>"+"</html>");

		app.zGetActiveAccount().soapSend(
				"<CreateTaskRequest xmlns='urn:zimbraMail'>" +
				"<m >" +
				"<inv>" +
				"<comp name='"+ subject +"'>" +
				"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
				"</comp>" +
				"</inv>" +
				"<su>"+ subject +"</su>" +
				"<mp ct='multipart/alternative'>" +
				"<mp ct='text/plain'>" +
				"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
				"</mp>" +
				"<mp ct='text/html'>" +
				"<content>"+contentHTML+"</content>" +
				"</mp>" +
				"</mp>" +
				"</m>" +
		"</CreateTaskRequest>");

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertStringContains(task.getHtmlTaskBody().trim().toLowerCase(), taskHtmlbody.trim(), "Verify the html content of task body");

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Select the item
		app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject);

		// Right click subject and select edit context menu
		FormTaskNew taskedit = (FormTaskNew) app.zPageTasks.zListItem(Action.A_RIGHTCLICK, Button.O_EDIT, subject);

		// Fill new subject in subject field
		taskedit.zFillField(Field.Subject, editSubject);
		taskedit.zFillField(Field.HtmlBody, editTaskHtmlbody);
		taskedit.zSubmit();

		// Get the list of tasks in the view
		List<TaskItem> tasks = app.zPageTasks.zGetTasks();
		ZAssert.assertNotNull(tasks, "Verify the list of edited tasks exists");

		// Iterate over the task list, looking for the new task
		TaskItem found = null;
		for (TaskItem t : tasks ) {
			logger.info("Task: looking for "+ editSubject +" found: "+ t.gSubject);
			if ( editSubject.equals(t.gSubject) ) {
				// Found it!
				found = t;
			}
		}
		ZAssert.assertNotNull(found, "Verify the Edited html task present in the task list");

		// Iterate over the task list, looking for the old task
		TaskItem foundoldtask = null;
		for (TaskItem t : tasks ) {
			logger.info("Task: looking for "+ subject +" foundeditedtask: "+ t.gSubject);
			if ( subject.equals(t.gSubject) ) {
				// Found it!
				foundoldtask = t;
				break;
			}
		}
		ZAssert.assertNull(foundoldtask, "Verify the old html task no longer  present in the task list");
	}


	@Test (description = "Create Html task through SOAP - Edit> convert Html to Plain Text and veirfy Warning dialog and its content",
			groups = { "functional", "L3" })

	public void EditHtmlTask_04() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		String subject = "task"+ ConfigProperties.getUniqueString();
		String taskHtmlbody = "task<b>bold"+ ConfigProperties.getUniqueString()+"</b>task";
		String contentHTML = XmlStringUtil.escapeXml("<html>"+"<body>"+"<div>"+taskHtmlbody+"</div>"+"</body>"+"</html>");

		app.zGetActiveAccount().soapSend(
				"<CreateTaskRequest xmlns='urn:zimbraMail'>" +
				"<m >" +
				"<inv>" +
				"<comp name='"+ subject +"'>" +
				"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
				"</comp>" +
				"</inv>" +
				"<su>"+ subject +"</su>" +
				"<mp ct='multipart/alternative'>" +
				"<mp ct='text/plain'>" +
				"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
				"</mp>" +
				"<mp ct='text/html'>" +
				"<content>"+contentHTML+"</content>" +
				"</mp>" +
				"</mp>" +
				"</m>" +
		"</CreateTaskRequest>");

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertStringContains(task.getHtmlTaskBody().trim().toLowerCase(), taskHtmlbody.trim(), "Verify the html content of task body");

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Select the item
		app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject);

		// Click edit
		FormTaskNew taskedit = (FormTaskNew) app.zPageTasks.zToolbarPressButton(Button.B_EDIT);

		DialogWarning dialogWarning = (DialogWarning)  taskedit.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_OPTION_FORMAT_AS_TEXT);
		ZAssert.assertNotNull(dialogWarning, "Verify the dialog is returned");
		String text = "Switching to text will discard all HTML formatting. Continue?";
		ZAssert.assertEquals(text,dialogWarning.zGetWarningContent()," Verify content is " + text);
	}
}