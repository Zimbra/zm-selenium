/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.tasks.toaster;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.Toaster;
import com.zimbra.qa.selenium.projects.universal.ui.tasks.FormTaskNew;
import com.zimbra.qa.selenium.projects.universal.ui.tasks.FormTaskNew.Field;

public class CreateTask extends UniversalCommonTest {

	@SuppressWarnings("serial")
	public CreateTask() {

		logger.info("New " + CreateTask.class.getCanonicalName());
		super.startingPage = app.zPageTasks;

		super.startingAccountPreferences = new HashMap<String, String>() {
			{
				put("zimbraPrefTasksReadingPaneLocation", "bottom");
			}
		};
	}

	@Test( description = "Verify Toaster message on Create Task", 
			groups = { "smoke", "L1"})
	public void CreateTask_01() throws HarnessException {

		String subject = "task" + ConfigProperties.getUniqueString();

		// Click NEW button
		FormTaskNew taskNew = (FormTaskNew) app.zPageTasks.zToolbarPressButton(Button.B_NEW);

		// Fill out the resulting form
		taskNew.zFillField(Field.Subject, subject);
		app.zPageTasks.sClick("css=div[id^='ztb__TKE']  tr[id^='ztb__TKE'] td[id$='_title']:contains('Save')");
		
		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "Task Saved","Verify toast message: Task Saved");

	}

}
