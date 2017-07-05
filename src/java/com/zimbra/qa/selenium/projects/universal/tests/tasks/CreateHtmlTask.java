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
package com.zimbra.qa.selenium.projects.universal.tests.tasks;

import java.util.HashMap;
import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FileItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.TaskItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.universal.ui.tasks.FormTaskNew;
import com.zimbra.qa.selenium.projects.universal.ui.tasks.FormTaskNew.Field;
import com.zimbra.qa.selenium.projects.universal.ui.tasks.PageTasks.Locators;

public class CreateHtmlTask extends AjaxCommonTest {

	@SuppressWarnings("serial")
	public CreateHtmlTask() {
		logger.info("New " + CreateHtmlTask.class.getCanonicalName());
		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String , String>() {{
			put("zimbraPrefGroupMailBy", "message");
			put("zimbraPrefComposeFormat", "html");
			put("zimbraPrefTasksReadingPaneLocation", "bottom");
			put("zimbraPrefShowSelectionCheckbox", "TRUE");
		}};
	}
	
	@Test( description = "Create Simple Html task through GUI - verify through soap", 
			groups = { "sanity", "L0"})
	
	public void CreateHtmlTask_01() throws HarnessException {

		String subject = "task" + ConfigProperties.getUniqueString();
		//String taskHtmlbody = "task<b>bold"+ ConfigProperties.getUniqueString() + "</b>task";
		String taskHtmlbody = "body" + ConfigProperties.getUniqueString();

		// Click NEW button
		FormTaskNew taskNew = (FormTaskNew) app.zPageTasks.zToolbarPressButton(Button.B_NEW);

		// Fill out the resulting form
		taskNew.zFillField(Field.Subject, subject);
		taskNew.zFillField(Field.HtmlBody, taskHtmlbody);
		taskNew.zSubmit();

		//Verify the html content of the task body		
		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertEquals(task.getName(), subject, "Verify task subject");
		ZAssert.assertStringContains(task.getHtmlTaskBody().trim().toLowerCase(), taskHtmlbody.trim(), "Verify the html content of task body");

	}
	
	@Test( description = "Create new Html task using keyboard shortcut Esc- Verify through Soap", 
			groups = { "smoke", "L0"})
	
	public void CreateHtmlTask_02() throws HarnessException {

		Shortcut shortcut = Shortcut.S_ESCAPE;
		String subject = "task" + ConfigProperties.getUniqueString();
		//String taskHtmlbody = "task<b>bold"+ ConfigProperties.getUniqueString() + "</b>task";
		String taskHtmlbody = "body" + ConfigProperties.getUniqueString();

		//Click NEW button
		FormTaskNew taskNew = (FormTaskNew) app.zPageTasks.zToolbarPressButton(Button.B_NEW);

		//Fill out resulting form		
		taskNew.zFillField(Field.Subject, subject);
		taskNew.zFillField(Field.HtmlBody, taskHtmlbody);

		//Click Escape shortcut 'Esc'	
		DialogWarning warning =(DialogWarning)app.zPageTasks.zKeyboardShortcut(shortcut);
		ZAssert.assertNotNull(warning, "Verify the dialog is opened");

		//Click Yes button of warning dialog
		warning.zClickButton(Button.B_YES);

		//Verify the html content of the task body		
		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertEquals(task.getName(), subject, "Verify task subject");
		ZAssert.assertStringContains(task.getHtmlTaskBody().trim().toLowerCase(), taskHtmlbody.trim(), "Verify the html content of task body");

	}
	
	@Test( description = "Create Html task using New menu pulldown  - verify through SOAP",	
			groups = { "smoke", "L0"})
	
	public void CreateHtmlTask_03() throws HarnessException {

		String subject = "task" + ConfigProperties.getUniqueString();
		//String taskHtmlbody = "task<b>bold"+ ConfigProperties.getUniqueString() + "</b>task";
		String taskHtmlbody = "body" + ConfigProperties.getUniqueString();

		// Click NEW drop down and click Task
		FormTaskNew taskNew = (FormTaskNew) app.zPageTasks.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_TASK);

		// Fill out the resulting form
		taskNew.zFillField(Field.Subject, subject);
		taskNew.zFillField(Field.HtmlBody, taskHtmlbody);
		taskNew.zSubmit();

		//Verify the html content of the task body		
		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertEquals(task.getName(), subject, "Verify task subject");
		ZAssert.assertStringContains(task.getHtmlTaskBody().trim().toLowerCase(), taskHtmlbody.trim(), "Verify the html content of task body");

	}
	
	@Test( description = "Create new Html task using keyboard shortcut 'NK'- Verify through Soap",
			groups = { "smoke", "L0"})
	
	public void CreateHtmlTask_04() throws HarnessException {

		Shortcut shortcut = Shortcut.S_NEWTASK;
		String subject = "task" + ConfigProperties.getUniqueString();
		//String taskHtmlbody = "task<b>bold"+ ConfigProperties.getUniqueString() + "</b>task";
		String taskHtmlbody = "body" + ConfigProperties.getUniqueString();
		
		//Explicitly clicking of Task folder to refresh view
		ZimbraAccount account = app.zGetActiveAccount();
		FolderItem taskFolder = FolderItem.importFromSOAP(account,SystemFolder.Tasks);
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		//Click NEW Task shortcut "NK"
		FormTaskNew taskNew = (FormTaskNew) app.zPageTasks.zKeyboardShortcut(shortcut);

		// Fill out the resulting form
		taskNew.zFillField(Field.Subject, subject);
		taskNew.zFillField(Field.HtmlBody, taskHtmlbody);
		taskNew.zSubmit();

		//Verify the html content of the task body		
		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertEquals(task.getName(), subject, "Verify task subject");
		ZAssert.assertStringContains(task.getHtmlTaskBody().trim().toLowerCase(), taskHtmlbody.trim(), "Verify the html content of task body");

	}
	
	@Test( description = "Create Html Tasks, using 'Right Click' Html Mail subject -> 'Create Task'-Verify through Soap", 
			groups = { "smoke", "L0"})
	
	public void CreateHtmlTask_05() throws HarnessException {

		app.zPageMail.zNavigateTo();
		
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(),SystemFolder.Inbox);
		String subject = "task" + ConfigProperties.getUniqueString();
		String bodyText = "bodyText" + ConfigProperties.getUniqueString();
		//String taskHtmlbody = "task<b>bold"+ ConfigProperties.getUniqueString() + "</b>task";
		String taskHtmlbody = "body" + ConfigProperties.getUniqueString();
		String contentHTML = XmlStringUtil.escapeXml("<html>"+"<body>"+"<div>"+"<div>"+taskHtmlbody+"</div>"+"</div>"+"</body>"+"</html>");


		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
				"<m>" +
				"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
				"<su>"+ subject +"</su>" +
				"<mp ct='multipart/alternative'>" +
				"<mp ct='text/plain'>" +
				"<content>"+bodyText+"</content>" +
				"</mp>" +
				"<mp ct='text/html'>" +
				"<content>"+contentHTML+"</content>" +
				"</mp>" +
				"</mp>" +
				"</m>" +
		"</SendMsgRequest>");

		// Get the mail item for the new message
		MailItem mail = MailItem.importFromSOAP(ZimbraAccount.AccountA(),"subject:(" + subject + ")");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);

		SleepUtil.sleepMedium();

		//Click on subject
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);


		//Right click subject >> select Create Task menu item
		app.zPageMail.zListItem(Action.A_RIGHTCLICK, Button.O_CREATE_TASK, mail.dSubject);

		//click save
		app.zPageTasks.zToolbarPressButton(Button.B_SAVE);


		//Verify the html content of the task body		
		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);

		ZAssert.assertEquals(task.getName(), subject, "Verify task subject");
		ZAssert.assertStringContains(task.getHtmlTaskBody().trim().toLowerCase(), taskHtmlbody.trim(), "Verify the html content of task body");
		
	}
	
	@Test( description = "Create Html task with attachment through RestUtil - verify through GUI", 
			groups = { "smoke", "L0"})
	
	public void CreateHtmlTask_06() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();
		FolderItem taskFolder = FolderItem.importFromSOAP(account, SystemFolder.Tasks);

		String subject = "task"+ ConfigProperties.getUniqueString();
		String taskHtmlbody = "task<b>bold"+ ConfigProperties.getUniqueString()+"</b>task";
		String contentHTML = XmlStringUtil.escapeXml("<html>"+"<body>"+"<div>"+taskHtmlbody+"</div>"+"</body>"+"</html>");		
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/Files/Basic01/BasicExcel2007.xlsx";
		
		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);	
		
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
				"<attach aid='"+attachmentId+"'>"+
				"</attach>" +
				"</m>" +
		"</CreateTaskRequest>");

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertStringContains(task.getHtmlTaskBody().trim().toLowerCase(), taskHtmlbody.trim(), "Verify the html content of task body");

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Select the item
		app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject);
		ZAssert.assertTrue(app.zPageTasks.sIsElementPresent(Locators.zAttachmentsLabel),"Verify Attachments: label");
		
	}
	
	@Test( description = "Create Html task with attachment through RestUtil - verify through Soap", 
			groups = { "smoke", "L0"})
	
	public void CreateHtmlTask_07() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();
		FolderItem taskFolder = FolderItem.importFromSOAP(account, SystemFolder.Tasks);

		String subject = "task" + ConfigProperties.getUniqueString();
		String taskHtmlbody = "task<b>bold"	+ ConfigProperties.getUniqueString() + "</b>task";
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<body>"
				+ "<div>" + taskHtmlbody + "</div>" + "</body>" + "</html>");
		String filePath = ConfigProperties.getBaseDirectory()
		+ "/data/public/Files/Basic01/BasicExcel2007.xlsx";
		FileItem file = new FileItem(filePath);
		String fileName = file.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);	

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
				"<attach aid='"+attachmentId+"'>"+
				"</attach>" +
				"</m>" +
		"</CreateTaskRequest>");

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertStringContains(task.getHtmlTaskBody().trim().toLowerCase(), taskHtmlbody.trim(), "Verify the html content of task body");

		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='task' >"
				+ "<query>" + subject + "</query>" + "</SearchRequest>");

		String invId = account.soapSelectValue("//mail:SearchResponse/mail:task", "invId");
		account.soapSend("<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='"
				+ invId + "' />" + "</GetMsgRequest>");

		Element getMsgResponse = account.soapSelectNode("//mail:GetMsgResponse", 1);
		Element m = ZimbraAccount.SoapClient.selectNode(getMsgResponse,"//mail:mp[@s='9055']");
		
		ZAssert.assertEquals(m.getAttribute("filename", null), fileName, "Verify file name through SOAP");

	}

}
