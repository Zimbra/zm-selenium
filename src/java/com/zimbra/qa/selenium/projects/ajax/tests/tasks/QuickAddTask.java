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

import java.awt.event.KeyEvent;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.items.TaskItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.tasks.FormTaskNew;
import com.zimbra.qa.selenium.projects.ajax.pages.tasks.FormTaskNew.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.tasks.PageTasks.Locators;

public class QuickAddTask extends AjaxCore {

	public QuickAddTask() {
		logger.info("New " + QuickAddTask.class.getCanonicalName());
		super.startingPage = app.zPageTasks;
	}


	@Test (description = "Create a new task via quick add",
			groups = { "sanity", "L0" })

	public void QuickAddTask_01() throws HarnessException {
		
		// Task data
		String subject = "task" + ConfigProperties.getUniqueString();
		
		// Create a task using add new task banner
		app.zPageTasks.sClick(Locators.zNewTaskBanner);
		app.zPageTasks.zKeyboard.zTypeCharacters(subject);
		app.zPageTasks.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		
		// Verify using soap
		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertEquals(task.getName(), subject, "Verify task subject");
		
		// Verify using UI
		ZAssert.assertTrue(app.zPageTasks.zVerifyTaskExists(subject), "Verify that the task is present in the task list");
	}
	
	
	@Test (description = "Create a new task via quick add, edit it to add body",
			groups = { "smoke", "L1" })

	public void QuickAddTask_02() throws HarnessException {
		
		// Task data
		String subject = "task" + ConfigProperties.getUniqueString();
		String body = "taskBody" + ConfigProperties.getUniqueString();
		FolderItem tasks = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);
		
		// Create a task using add new task banner
		app.zPageTasks.sClick(Locators.zNewTaskBanner);
		app.zPageTasks.zKeyboard.zTypeCharacters(subject);
		app.zPageTasks.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		
		// Verify using soap
		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertEquals(task.getName(), subject, "Verify task subject");
		
		// Open the task to edit
		FormTaskNew taskForm = (FormTaskNew)app.zPageTasks.zListItem(Action.A_DOUBLECLICK, subject);
		
		// Enter body of task and save
		taskForm.zFillField(Field.Body, body);
		taskForm.zSubmit();
		
		// Verify using soap
		task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertEquals(task.getName(), subject, "Verify task subject");
		ZAssert.assertEquals(task.gettaskBody(), body, "Verify task body");
		
		// Verify using UI
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, tasks);
		app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject);
		ZAssert.assertTrue(app.zPageTasks.zVerifyTaskBody(body), "Verify task body");
	}
	
	
	@Bugs( ids = "ZCS-4023" )
	@Test (description = "Create a new task via quick add, edit it to add body and verify the editor displayed",
			groups = { "functional", "L3" })

	public void QuickAddTask_03() throws HarnessException {
		
		// Task data
		String subject1 = "task1 " + ConfigProperties.getUniqueString();
		String body1 = "taskBody1 " + ConfigProperties.getUniqueString();
		String subject2 = "task2 " + ConfigProperties.getUniqueString();
		String body2 = "taskBody2 " + ConfigProperties.getUniqueString();
		FolderItem tasks = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);
		
		// Create a task using add new task banner
		app.zPageTasks.sClick(Locators.zNewTaskBanner);
		app.zPageTasks.zKeyboard.zTypeCharacters(subject1);
		app.zPageTasks.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		
		// Open the task to edit
		FormTaskNew taskForm = (FormTaskNew)app.zPageTasks.zListItem(Action.A_RIGHTCLICK, Button.O_EDIT, subject1);
		
		// Enter body of task and save
		taskForm.zFillField(Field.Body, body1);
		taskForm.zSubmit();
		
		// Create a new task again using add new task banner
		app.zPageTasks.sClick(Locators.zNewTaskBanner);
		app.zPageTasks.zKeyboard.zTypeCharacters(subject2);
		app.zPageTasks.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		
		// Verify using soap
		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject2);
		ZAssert.assertEquals(task.getName(), subject2, "Verify task subject");
		
		// Open the task to edit
		taskForm = (FormTaskNew)app.zPageTasks.zListItem(Action.A_RIGHTCLICK, Button.O_EDIT, subject2);
		
		// Verify the presence of editor
		ZAssert.assertTrue(taskForm.zIsTextEditorDisplayed(), "Verify plain text editor is displayed.");
		
		// Enter body of task and save
		taskForm.zFillField(Field.Body, body2);
		taskForm.zSubmit();
		
		// Verify using UI
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, tasks);
		app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject2);
		ZAssert.assertTrue(app.zPageTasks.zVerifyTaskBody(body2), "Verify task body");
	}
}