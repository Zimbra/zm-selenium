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

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.TaskItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.AppAjaxClient;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogTag;

public class TagTask extends AjaxCommonTest{

	@SuppressWarnings("serial")
	public TagTask() {
		logger.info("Tag " + TagTask.class.getCanonicalName());

		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String , String>() {{
			put("zimbraPrefTasksReadingPaneLocation", "bottom");
			put("zimbraPrefShowSelectionCheckbox", "TRUE");
		}};
	}


	@Bugs( ids = "96832")
	@Test (description = "Tag a Task using Toolbar -> Tag -> New Tag",
			groups = { "smoke", "L1" })

	public void TagTask_01() throws HarnessException {
		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		String subject = "task"+ ConfigProperties.getUniqueString();
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

		// Select the item
		app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject);

		// Create a tag using GUI
		String tagName = "tag" + ConfigProperties.getUniqueString();

		// Click on New Tag and check for active
		DialogTag dialogtag = (DialogTag)app.zPageTasks.zToolbarPressPulldown(Button.B_TAG, Button.O_TAG_NEWTAG);
		ZAssert.assertTrue(dialogtag.zIsActive(), "Verify that the Create New Tag dialog is active or not");

		// Fill Name  and Press OK button
		dialogtag.zSetTagName(tagName);
		dialogtag.zPressButton(Button.B_OK);

		// Make sure the tag was created on the server (get the tag ID)
		app.zGetActiveAccount().soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");;
		String tagID = app.zGetActiveAccount().soapSelectValue("//mail:GetTagResponse//mail:tag[@name='"+ tagName +"']", "id");

		// Verify tagged task name
		app.zGetActiveAccount()
		.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='task'>"
				+ "<query>tag:"
				+ tagName
				+ "</query>"
				+ "</SearchRequest>");

		String name = app.zGetActiveAccount().soapSelectValue("//mail:SearchResponse//mail:task", "name");

		ZAssert.assertEquals(name, subject,	"Verify tagged task name");

		// Make sure the tag was applied to the task
		app.zGetActiveAccount().soapSend("<SearchRequest xmlns='urn:zimbraMail' types='task'>"
				+ "<query>" + subject + "</query>" + "</SearchRequest>");

		String id = app.zGetActiveAccount().soapSelectValue("//mail:SearchResponse//mail:task", "t");
		ZAssert.assertEquals(id, tagID,"Verify the tag was attached to the task see bug 96832 ");
	}


	@AfterMethod(groups = { "always" })
	public void afterMethod() throws HarnessException {
		logger.info("Checking for the Create New Tag Dialog ...");

		// Check if the "Create New Tag Dialog is still open
		DialogTag dialogtag = new DialogTag(app, ((AppAjaxClient)app).zPageTasks);
		if (dialogtag.zIsActive()) {
			logger.warn(dialogtag.myPageName() + " was still active.  Cancelling ...");
			dialogtag.zPressButton(Button.B_CANCEL);
		}
	}
}