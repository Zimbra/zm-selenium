/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.tasks.attachments;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FileItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.TaskItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class OpenTaskContainsAttachmentMultipleTimes extends AjaxCommonTest {

	@SuppressWarnings("serial")
	public OpenTaskContainsAttachmentMultipleTimes() {
		logger.info("New " + OpenTaskContainsAttachmentMultipleTimes.class.getCanonicalName());
		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String , String>() {{
			put("zimbraPrefShowSelectionCheckbox", "TRUE");
			put("zimbraPrefComposeFormat", "text");
			put("zimbraPrefGroupMailBy", "message");
			put("zimbraPrefTasksReadingPaneLocation", "bottom");
		}};
	}


	@Bugs (ids = "75283")
	@Test (description = "Same attachment keeps on adding while opening same task multiple times",
			groups = { "smoke", "L1" })

	public void OpenTaskContainsAttachmentMultipleTimes_01() throws HarnessException {

		String subject = "task" + ConfigProperties.getUniqueString();

		ZimbraAccount account = app.zGetActiveAccount();
		FolderItem taskFolder = FolderItem.importFromSOAP(account,SystemFolder.Tasks);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/Files/Basic01/BasicExcel2007.xlsx";
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
				"<mp ct='text/plain'>" +
				"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
				"</mp>" +
				"<attach aid='"+attachmentId+"'>"+
				"</attach>"+
				"</m>" +
		"</CreateTaskRequest>");

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(task, "Verify the task is created");

		// Verify attachment through soap
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='task' >"
				+ "<query>" + subject + "</query>" + "</SearchRequest>");

		String invId = account.soapSelectValue("//mail:SearchResponse/mail:task", "invId");

		account.soapSend("<GetMsgRequest xmlns='urn:zimbraMail'>"
				+ "<m id='" + invId + "' />" + "</GetMsgRequest>");

		Element getMsgResponse = account.soapSelectNode("//mail:GetMsgResponse", 1);
		Element m = ZimbraAccount.SoapClient.selectNode(getMsgResponse,"//mail:mp[@s='9055']");
		ZAssert.assertEquals(m.getAttribute("filename", null), fileName, "Verify file name through SOAP");

		//1st attempt
		app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject);
		app.zPageTasks.zListItem(Action.A_DOUBLECLICK, subject);

		SleepUtil.sleepMedium();
		ZAssert.assertTrue(app.zPageTasks.sIsElementPresent(com.zimbra.qa.selenium.projects.ajax.ui.tasks.FormTaskNew.Locators.zCloseButton), "Verify Close button is there");

		ZAssert.assertTrue(app.zPageTasks.sIsElementPresent("css=div[class='ZmTaskEditView'] tr[id$='_attachment_container'] div td a[class='AttLink']:contains('"+fileName+"')")," Verify only one Attachment present");
		ZAssert.assertFalse(app.zPageTasks.sIsElementPresent("xpath=//div[contains(@class,'ZmTaskEditView')]//tr[contains(@id,'_attachment_container')]/td/fieldset/form/div/div[2]"),"Verify Duplicate attachment is not present");

		app.zPageTasks.sClickAt(com.zimbra.qa.selenium.projects.ajax.ui.tasks.FormTaskNew.Locators.zCloseButton, "0,0");
		ZAssert.assertTrue(app.zPageTasks.sGetEval("window.appCtxt.getCurrentViewType()").equalsIgnoreCase("TKL"),"Verify List view is open");

		//2nd Attempt
		app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject);
		app.zPageTasks.zListItem(Action.A_DOUBLECLICK, subject);

		SleepUtil.sleepMedium();
		ZAssert.assertTrue(app.zPageTasks.sIsElementPresent(com.zimbra.qa.selenium.projects.ajax.ui.tasks.FormTaskNew.Locators.zCloseButton), "Verify Close button is there");

		ZAssert.assertTrue(app.zPageTasks.sIsElementPresent("css=div[class='ZmTaskEditView'] tr[id$='_attachment_container'] div td a[class='AttLink']:contains('"+fileName+"')")," Verify only one Attachment present");
		ZAssert.assertFalse(app.zPageTasks.sIsElementPresent("xpath=//div[contains(@class,'ZmTaskEditView')]//tr[contains(@id,'_attachment_container')]/td/fieldset/form/div/div[2]"),"Verify Duplicate attachment is not present");

		// Close Edit window
		app.zPageTasks.sClickAt(com.zimbra.qa.selenium.projects.ajax.ui.tasks.FormTaskNew.Locators.zCloseButton, "0,0");

		//After closing Task list view should show.
		ZAssert.assertTrue(app.zPageTasks.sGetEval("window.appCtxt.getCurrentViewType()").equalsIgnoreCase("TKL"),"Verify List view is open");

		//3rd Attempt
		app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject);
		app.zPageTasks.zListItem(Action.A_DOUBLECLICK, subject);

		SleepUtil.sleepMedium();
		ZAssert.assertTrue(app.zPageTasks.sIsElementPresent(com.zimbra.qa.selenium.projects.ajax.ui.tasks.FormTaskNew.Locators.zCloseButton), "Verify Close button is there");

		ZAssert.assertTrue(app.zPageTasks.sIsElementPresent("css=div[class='ZmTaskEditView'] tr[id$='_attachment_container'] div td a[class='AttLink']:contains('"+fileName+"')")," Verify only one Attachment present");
		ZAssert.assertFalse(app.zPageTasks.sIsElementPresent("xpath=//div[contains(@class,'ZmTaskEditView')]//tr[contains(@id,'_attachment_container')]/td/fieldset/form/div/div[2]"),"Verify Duplicate attachment is not present");
	}
}