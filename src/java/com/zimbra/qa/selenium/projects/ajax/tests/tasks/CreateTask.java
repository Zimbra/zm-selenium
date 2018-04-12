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

import java.util.*;
import org.testng.annotations.*;
import com.zimbra.common.soap.*;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.pages.*;
import com.zimbra.qa.selenium.projects.ajax.pages.tasks.*;
import com.zimbra.qa.selenium.projects.ajax.pages.tasks.FormTaskNew.*;
import com.zimbra.qa.selenium.projects.ajax.pages.tasks.PageTasks.Locators;

public class CreateTask extends AjaxCore {

	@SuppressWarnings("serial")
	public CreateTask() {
		logger.info("New " + CreateTask.class.getCanonicalName());
		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String , String>() {{
			put("zimbraPrefShowSelectionCheckbox", "TRUE");
			put("zimbraPrefComposeFormat", "text");
			put("zimbraPrefGroupMailBy", "message");
			put("zimbraPrefTasksReadingPaneLocation", "bottom");
		}};
	}


	@Test (description = "Create Simple task through GUI - verify through soap",
			groups = { "sanity", "L0" })

	public void CreateTask_01() throws HarnessException {

		String subject = "task" + ConfigProperties.getUniqueString();
		String body = "taskbody"+ ConfigProperties.getUniqueString();

		// Click NEW button
		FormTaskNew taskNew = (FormTaskNew) app.zPageTasks.zToolbarPressButton(Button.B_NEW);

		// Fill out the resulting form
		taskNew.zFillField(Field.Subject, subject);
		taskNew.zFillField(Field.Body, body);
		taskNew.zSubmit();

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertEquals(task.getName(), subject, "Verify task subject");
		ZAssert.assertEquals(task.gettaskBody().trim(), body.trim(), "Verify the task body");
	}


	@Test (description = "Create new task using keyboard shortcut Esc- Verify through Soap",
			groups = { "smoke", "L1" })

	public void CreateTask_02() throws HarnessException {

		Shortcut shortcut = Shortcut.S_ESCAPE;
		String subject = "task" + ConfigProperties.getUniqueString();
		String body = "taskbody" + ConfigProperties.getUniqueString();
		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Click NEW button
		FormTaskNew taskNew = (FormTaskNew) app.zPageTasks.zToolbarPressButton(Button.B_NEW);

		// Fill out resulting form
		taskNew.zFillField(Field.Subject, subject);

		//Work around by typing using keyboard otherwise warning dialog won't come
		taskNew.sFocus(Locators.zTaskBodyField);
		taskNew.sClick(Locators.zTaskBodyField);
		taskNew.zKeyboard.zTypeCharacters(body);

		// Click Escape shortcut 'Esc'
		DialogWarning warning =(DialogWarning)app.zPageTasks.zKeyboardShortcut(shortcut);
		ZAssert.assertNotNull(warning, "Verify the dialog is opened");

		// Click Yes button of warning dialog
		warning.zPressButton(Button.B_YES);

		SleepUtil.sleepMedium();
		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertEquals(task.getName(), subject, "Verify task subject");
		ZAssert.assertEquals(task.gettaskBody().trim(), body.trim(), "Verify the task body");
	}


	@Test (description = "Create task using New menu pulldown  - verify through SOAP",
			groups = { "smoke", "L1" })

	public void CreateTask_03() throws HarnessException {

		String subject = "task" + ConfigProperties.getUniqueString();
		String body = "taskbody" + ConfigProperties.getUniqueString();
		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Click NEW drop down and click Task
		FormTaskNew taskNew = (FormTaskNew) app.zPageTasks.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_TASK);
		SleepUtil.sleepMedium();

		// Fill out the resulting form
		taskNew.zFillField(Field.Subject, subject);
		taskNew.zFillField(Field.Body, body);
		taskNew.zSubmit();
		SleepUtil.sleepMedium();

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);
		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(),subject);
		ZAssert.assertEquals(task.getName(), subject, "Verify task subject");
		ZAssert.assertEquals(task.gettaskBody().trim(), body.trim(),"Verify the task body");
	}


	@Test (description = "Create new task using NK keyboard shortcut key",
			groups = { "smoke", "L1" })

	public void CreateTask_04() throws HarnessException {

		Shortcut shortcut = Shortcut.S_NEWTASK;
		String subject = "task" + ConfigProperties.getUniqueString();
		String body = "taskbody" + ConfigProperties.getUniqueString();

		//Explicitly cliking of Task folder to refresh view
		ZimbraAccount account = app.zGetActiveAccount();
		FolderItem taskFolder = FolderItem.importFromSOAP(account,SystemFolder.Tasks);
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Click NEW Task shortcut "NK"
		FormTaskNew taskNew = (FormTaskNew) app.zPageTasks.zKeyboardShortcut(shortcut);
		SleepUtil.sleepMedium();

		// Fill out resulting form
		taskNew.zFillField(Field.Subject, subject);
		taskNew.zFillField(Field.Body, body);
		taskNew.zSubmit();
		SleepUtil.sleepMedium();

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertEquals(task.getName(), subject, "Verify task subject");
		ZAssert.assertEquals(task.gettaskBody().trim(), body.trim(), "Verify the task body");
	}


	@Test (description = "Create Tasks, using 'Right Click' Mail subject -> 'Create Task'-Verify through Soap",
			groups = { "smoke", "L1" })

	public void CreateTask_05() throws HarnessException {

		app.zPageMail.zNavigateTo();
		String subject = "subject"+ ConfigProperties.getUniqueString();

		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Get the mail item for the new message
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		SleepUtil.sleepMedium();

		// Click on subject
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Right click subject >> select Create Task menu item
		app.zPageMail.zListItem(Action.A_RIGHTCLICK, Button.O_CREATE_TASK, mail.dSubject);

		// Click save
		app.zPageTasks.zToolbarPressButton(Button.B_SAVE);

		// Verify task created.
		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertEquals(task.getName(), subject, "Verify task subject");
	}


	@Test (description = "Create Simple task with attachment through RestUtil - verify through GUI",
			groups = { "smoke", "L1" })

	public void CreateTask_06() throws HarnessException {

		String subject = "task" + ConfigProperties.getUniqueString();

		ZimbraAccount account = app.zGetActiveAccount();
		FolderItem taskFolder = FolderItem.importFromSOAP(account,SystemFolder.Tasks);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/Files/Basic01/BasicExcel2007.xlsx";
		// Upload file to server through RestUtil
		String attachmentId = account.uploadFileUsingRestUtil(filePath);

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
				"<attach aid='"+attachmentId+"'>"+
				"</attach>"+
				"</m>" +
		"</CreateTaskRequest>");

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(task, "Verify the task is created");

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Select the item
		app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject);
		ZAssert.assertTrue(app.zPageTasks.sIsElementPresent(Locators.zAttachmentsLabel),"Verify Attachments: label");
	}


	@Test (description = "Create Simple task  with attachment through RestUtil - verify through soap",
			groups = { "smoke", "L1" })

	public void CreateTask_07() throws HarnessException {

		String subject = "task" + ConfigProperties.getUniqueString();

		ZimbraAccount account = app.zGetActiveAccount();
		FolderItem taskFolder = FolderItem.importFromSOAP(account,SystemFolder.Tasks);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/Files/Basic01/BasicExcel2007.xlsx";
		FileItem file = new FileItem(filePath);
		String fileName = file.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFileUsingRestUtil(filePath);

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
				"<attach aid='"+attachmentId+"'>"+
				"</attach>"+
				"</m>" +
		"</CreateTaskRequest>");

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(task, "Verify the task is created");

		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='task' >"
				+ "<query>" + subject + "</query>" + "</SearchRequest>");

		String invId = account.soapSelectValue("//mail:SearchResponse/mail:task", "invId");

		account.soapSend("<GetMsgRequest xmlns='urn:zimbraMail'>"
				+ "<m id='" + invId + "' />" + "</GetMsgRequest>");

		Element getMsgResponse = account.soapSelectNode("//mail:GetMsgResponse", 1);
		Element m = ZimbraAccount.SoapClient.selectNode(getMsgResponse,"//mail:mp[@s='9055']");
		ZAssert.assertEquals(m.getAttribute("filename", null), fileName, "Verify file name through SOAP");
	}


	@DataProvider(name = "DataProvidePriorities")
	public Object[][] DataProvidePriorities() {
	  return new Object[][] {
			  new Object[] { Button.O_PRIORITY_HIGH, "1" },
			  new Object[] { Button.O_PRIORITY_LOW, "9" },
			  new Object[] { Button.O_PRIORITY_NORMAL, "5" }
	  };
	}

	@Test (description = "Create a task with different priorities high/normal/low",
			groups = { "smoke", "L1"},	dataProvider = "DataProvidePriorities")

	public void CreateTask_08(Button option, String verify) throws HarnessException {

		String subject = "task" + ConfigProperties.getUniqueString();
		String body = "taskbody"+ ConfigProperties.getUniqueString();

		// Click NEW button
		FormTaskNew form = (FormTaskNew) app.zPageTasks.zToolbarPressButton(Button.B_NEW);

		SleepUtil.sleepMedium();

		// Fill out the resulting form
		form.zFillField(Field.Subject, subject);
		form.sFocus(Locators.zTaskBodyField);
		form.sClick(Locators.zTaskBodyField);
		form.zKeyboard.zTypeCharacters(body);

		// Change the priority
		form.zToolbarPressPulldown(Button.B_PRIORITY, option);
		SleepUtil.sleepMedium();
		form.zSubmit();

		// Verification
		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertStringContains(task.gPriority, verify, "Verify the correct priority was sent");
	}
	
	@Bugs (ids = "81749")
	@Test (description = "Create a task with start and end date through UI and verify through soap/UI",
			groups = { "sanity", "L1" })

	public void CreateTask_09() throws HarnessException {
		
		Calendar now = Calendar.getInstance();
		FolderItem tasks = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);
		String subject = "task" + ConfigProperties.getUniqueString();
		String body = "taskbody"+ ConfigProperties.getUniqueString();
		String startDay = String.valueOf(now.get(Calendar.DAY_OF_MONTH) + 1);
		String endDay = String.valueOf(now.get(Calendar.DAY_OF_MONTH) + 3);

		// Click New button
		FormTaskNew taskNew = (FormTaskNew) app.zPageTasks.zToolbarPressButton(Button.B_NEW);

		// Fill out the resulting form
		taskNew.zFillField(Field.Subject, subject);
		taskNew.zFillField(Field.Body, body);
		
		// Select start date and end date from date picker
		taskNew.zSelectStartDateFromDatePicker(startDay);
		taskNew.zSelectEndDateFromDatePicker(endDay);
		taskNew.zSubmit();
		
		// verify through SOAP
		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertEquals(task.getName(), subject, "Verify task subject");
		ZAssert.assertEquals(task.gettaskBody().trim(), body.trim(), "Verify the task body");
		
		// Verify through GUI
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, tasks);

		// Select the item
		app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject);
		
		ZAssert.assertStringContains(app.zPageTasks.sGetText(Locators.zTaskSubject), subject, "Verify that subject is displayed correctly.");
		ZAssert.assertStringContains(app.zPageTasks.sGetText(Locators.zTaskStartDate), startDay, "Verify that start day is displayed correctly.");
		ZAssert.assertStringContains(app.zPageTasks.sGetText(Locators.zTaskDueDate), endDay, "Verify that end day is displayed correctly.");
		ZAssert.assertStringContains(app.zPageTasks.zGetHtmlBodyText(), body, "Verify that body is displayed correctly.");
	}
}