/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013 Zimbra Software, LLC.
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
public class MoveTaskFolder extends CommonTest {
	@DataProvider(name = "taskCreateDataProvider")
	protected Object[][] createData(Method method) {
		String test = method.getName();
		if (test.equals("moveTaskFolder")) {
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

	@Test(dataProvider = "taskCreateDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void moveTaskFolder() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String orgTaskList = getLocalizedData_NoSpecialChar();
		page.zTaskApp.zTaskListCreateNewBtn(orgTaskList);
		zDragAndDrop(
				"//td[contains(@id, 'zti__main_Tasks') and contains(text(), '"
						+ orgTaskList + "')]", page.zTaskApp.zTasksFolder);
		Assert
				.assertTrue(ClientSessionFactory.session().selenium()
						.isElementPresent("//div[@id='zti__main_Tasks__15']/div[@class='DwtTreeItemChildDiv']//td[contains(text(), '"
								+ orgTaskList + "')]"));

		SelNGBase.needReset.set(false);
	}

}
