/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011 VMware, Inc.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.zcs.tests.tasks.folders;

import java.lang.reflect.Method;
import junit.framework.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;


@SuppressWarnings( { "static-access" })
public class CreateTaskFolder extends CommonTest {
	@DataProvider(name = "taskCreateDataProvider")
	protected Object[][] createData(Method method) {
		String test = method.getName();
		if (test.equals("createTaskFolder")) {
			return new Object[][] { {} };
		} else {
			return new Object[][] { { "" } };
		}
	}

	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.NAVIGATION_TAB="tasks";
		super.zLogin();
	}

	/**
	 * Creates a task list folder Verifies that the folder is created
	 * successfully
	 * 
	 */
	@Test(
			dataProvider = "taskCreateDataProvider", 
			groups = { "sanity", "smoke", "full" }, 
			retryAnalyzer = RetryFailedTests.class)
	public void createTaskFolder() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String taskListBtn = getLocalizedData_NoSpecialChar();
		String taskListRtClick = getLocalizedData_NoSpecialChar();
		page.zTaskApp.zTaskListCreateNewBtn(taskListBtn);
		SleepUtil.sleep(1000);
		page.zTaskApp.zTaskListCreateRtClick(taskListRtClick);
		obj.zTaskFolder.zExists(taskListBtn);
		obj.zTaskFolder.zExists(taskListRtClick);

		SelNGBase.needReset.set(false);
	}

}
