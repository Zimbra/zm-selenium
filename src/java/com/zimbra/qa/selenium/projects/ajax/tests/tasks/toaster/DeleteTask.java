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
package com.zimbra.qa.selenium.projects.ajax.tests.tasks.toaster;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.Toaster;
import com.zimbra.qa.selenium.projects.ajax.ui.tasks.PageTasks;

public class DeleteTask extends AjaxCommonTest {

	@SuppressWarnings("serial")
	public DeleteTask() {
		logger.info("New "+ DeleteTask.class.getCanonicalName());

		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String , String>() {{
			put("zimbraPrefShowSelectionCheckbox", "TRUE");
			put("zimbraPrefTasksReadingPaneLocation", "bottom");
		}};
	}


	@Test (description = "Delete a task using toolbar delete button and Verify Toast message through GUI",
			groups = { "smoke", "L1" })

	public void DeleteTaskToastMsg_01() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Create a basic task to delete
		String subject = "task"+ ConfigProperties.getUniqueString();

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

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(task, "Verify the task is created");

		// Refresh the tasks view
		app.zPageTasks.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Select the item
		app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject);

		// Click delete
		app.zPageTasks.zToolbarPressButton(Button.B_DELETE);

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "1 task moved to Trash","Verify toast message: Task Moved to Trash");
	}


	@Test (description = "Delete a task using checkbox and toolbar delete button-Verify Toast message through GUI",
			groups = { "smoke", "L1" })

	public void DeleteTaskToastMsg_02() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Create a basic task to delete
		String subject = "task"+ ConfigProperties.getUniqueString();

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

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(task, "Verify the task is created");

		// Refresh the tasks view
		app.zPageTasks.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		if (!app.zPageTasks.sIsElementPresent(PageTasks.Locators.zCheckboxenable)) {
			app.zPageTasks.zListItem(Action.A_MAIL_CHECKBOX, subject);
		}

		// Click delete
		app.zPageTasks.zToolbarPressButton(Button.B_DELETE);

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "1 task moved to Trash","Verify toast message: Task Moved to Trash");
	}


	@DataProvider(name = "DataProviderDeleteKeys")
	public Object[][] DataProviderDeleteKeys() {
		return new Object[][] {
				new Object[] { "VK_DELETE", KeyEvent.VK_DELETE },
				new Object[] { "VK_BACK_SPACE", KeyEvent.VK_BACK_SPACE },
		};
	}

	@Test (description = "Delete a task by selecting and typing 'delete' keyboard-Verify Toast message through GUI",
			groups = { "functional", "L3"},
			dataProvider = "DataProviderDeleteKeys")

	public void DeleteTaskToastMsg_03(String name, int keyEvent) throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Create a basic task to delete
		String subject = "task"+ ConfigProperties.getUniqueString();

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

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(task, "Verify the task is created");

		// Refresh the tasks view
		app.zPageTasks.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		if (!app.zPageTasks.sIsElementPresent(PageTasks.Locators.zCheckboxenable)) {
			app.zPageTasks.zListItem(Action.A_MAIL_CHECKBOX, subject);
		}

		// Click delete keyboard
		logger.info("Typing shortcut key "+ name + " KeyEvent: "+ keyEvent);
		app.zPageMail.zKeyboardKeyEvent(keyEvent);

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "1 task moved to Trash","Verify toast message: Task Moved To trash");
	}


	@Bugs(ids="56467")
	@Test (description = "Delete a task by selecting and typing '.t' shortcut : Verify Toast message through GUI",
			groups = { "functional", "L3"} )

	public void DeleteTaskToastMsg_04() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Create a basic task to delete
		String subject = "task"+ ConfigProperties.getUniqueString();

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

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(task, "Verify the task is created");

		// Refresh the tasks view
		app.zPageTasks.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		if (!app.zPageTasks.sIsElementPresent(PageTasks.Locators.zCheckboxenable)) {
			app.zPageTasks.zListItem(Action.A_MAIL_CHECKBOX, subject);
		}

		// Use Delete Keyboard Shortcut
		app.zPageTasks.zKeyboardShortcut(Shortcut.S_MAIL_MOVETOTRASH);

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "1 task moved to Trash","Verify toast message: Task Moved to Trash");
	}


	@Test (description = "Delete multiple tasks (3) by select and toolbar delete : Verify Toast message through GUI",
			groups = { "functional", "L3" })

	public void DeleteTaskToastMsg_05() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Create the message data to be sent
		String subject1 = "task"+ ConfigProperties.getUniqueString();
		String subject2 = "task"+ ConfigProperties.getUniqueString();
		String subject3 = "task"+ ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateTaskRequest xmlns='urn:zimbraMail'>" +
				"<m >" +
				"<inv>" +
				"<comp name='"+ subject1 +"'>" +
				"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
				"</comp>" +
				"</inv>" +
				"<su>"+ subject1 +"</su>" +
				"<mp ct='text/plain'>" +
				"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
				"</mp>" +
				"</m>" +
		"</CreateTaskRequest>");

		app.zGetActiveAccount().soapSend(
				"<CreateTaskRequest xmlns='urn:zimbraMail'>" +
				"<m >" +
				"<inv>" +
				"<comp name='"+ subject2 +"'>" +
				"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
				"</comp>" +
				"</inv>" +
				"<su>"+ subject2 +"</su>" +
				"<mp ct='text/plain'>" +
				"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
				"</mp>" +
				"</m>" +
		"</CreateTaskRequest>");

		app.zGetActiveAccount().soapSend(
				"<CreateTaskRequest xmlns='urn:zimbraMail'>" +
				"<m >" +
				"<inv>" +
				"<comp name='"+ subject3 +"'>" +
				"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
				"</comp>" +
				"</inv>" +
				"<su>"+ subject3 +"</su>" +
				"<mp ct='text/plain'>" +
				"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
				"</mp>" +
				"</m>" +
		"</CreateTaskRequest>");

		// Import each message into MailItem objects
		TaskItem task1 = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject1);
		ZAssert.assertNotNull(task1, "Verify the task is created");

		TaskItem task2 = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject2);
		ZAssert.assertNotNull(task2, "Verify the task is created");

		TaskItem task3 = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject3);
		ZAssert.assertNotNull(task3, "Verify the task is created");

		// Refresh the tasks view
		app.zPageTasks.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Select the items
		app.zPageTasks.zListItem(Action.A_MAIL_CHECKBOX, subject1);
		app.zPageTasks.zListItem(Action.A_MAIL_CHECKBOX, subject2);
		app.zPageTasks.zListItem(Action.A_MAIL_CHECKBOX, subject3);

		// Click toolbar delete button
		app.zPageTasks.zToolbarPressButton(Button.B_DELETE);

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "3 tasks moved to Trash","Verify toast message: N Tasks Moved to Trash");
	}


	@Test (description = "Delete a task using context menu delete button:Verify Toast message through GUI",
			groups = { "functional", "L3" })

	public void DeleteTaskToastMsg_06() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Create a basic task to delete
		String subject = "task"+ ConfigProperties.getUniqueString();

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

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(task, "Verify the task is created");

		// Refresh the tasks view
		app.zPageTasks.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		if (!app.zPageTasks.sIsElementPresent(PageTasks.Locators.zCheckboxenable)) {
			app.zPageTasks.zListItem(Action.A_MAIL_CHECKBOX, subject);
		}

		// Right click the item, select delete
		app.zPageTasks.zListItem(Action.A_RIGHTCLICK, Button.B_DELETE, subject);

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "1 task moved to Trash","Verify toast message: Task Moved to Trash");
	}


	@Test (description = "Create task through SOAP - delete using Backspace Key & verify Toast message through GUI",
			groups = { "functional", "L3"} )

	public void DeleteTaskToastMsg_07() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Create a basic task to delete
		String subject = "task"+ ConfigProperties.getUniqueString();

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

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(task, "Verify the task is created");

		// Refresh the tasks view
		app.zPageTasks.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		if (!app.zPageTasks.sIsElementPresent(PageTasks.Locators.zCheckboxenable)) {
			app.zPageTasks.zListItem(Action.A_MAIL_CHECKBOX, subject);
		}

		// Use Backspace Keyboard Shortcut
		app.zPageTasks.zKeyboardShortcut(Shortcut.S_BACKSPACE);

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "1 task moved to Trash","Verify toast message: Task Moved to Trash");
	}
}