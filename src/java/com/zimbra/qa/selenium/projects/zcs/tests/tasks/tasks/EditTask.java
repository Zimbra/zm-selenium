package com.zimbra.qa.selenium.projects.zcs.tests.tasks.tasks;

import java.lang.reflect.Method;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;


@SuppressWarnings( { "static-access" })
public class EditTask extends CommonTest {
	@DataProvider(name = "taskCreateDataProvider")
	protected Object[][] createData(Method method) {
		String test = method.getName();
		if ( test.equals("editTask")) {
			return new Object[][] { { getLocalizedData(1), getLocalizedData(1),
					localize(locator.low), getLocalizedData(3), "", } };
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
	 * Creates a task with all details Edits majority of the details Verifies
	 * task is edited successfully
	 * 
	 */
	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void editTask() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("da,ar", "na", "na", "Percentage special character different in da, ar locale");

		String subject = getLocalizedData_NoSpecialChar();
		String newSubject = getLocalizedData_NoSpecialChar();
		String location = getLocalizedData(2);
		String priority = localize(locator.high);
		String body = getLocalizedData(3);
		String progress = localize(locator.deferred);
		String progressPercent = "60";
		String newLocation = getLocalizedData(1);
		String newBody = getLocalizedData(3);
		page.zTaskApp.zNavigateToTasks();
		page.zTaskApp.zTaskCreate(subject, location, priority, body, "",
				progress, progressPercent, "", "");
		obj.zTaskItem.zExists(subject);
		page.zTaskApp.zTaskEdit(subject, newSubject, newLocation, "", newBody,
				"", "", "", "", "");
		obj.zTaskItem.zExists(newSubject);

		SelNGBase.needReset.set(false);
	}

}
