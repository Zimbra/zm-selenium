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
public class TaskFolderActions extends CommonTest {
	@DataProvider(name = "taskCreateDataProvider")
	protected Object[][] createData(Method method) {
		String test = method.getName();
		if ( test.equals("renameTaskFolder")
				|| test.equals("tryToCreateDuplicateTaskFolder")) {
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
	 * Creates a task list folder Renames the task list folder Verifies that the
	 * task list folder is renamed successfully
	 * 
	 */
	@Test(dataProvider = "taskCreateDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void renameTaskFolder() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String orgTaskList = getLocalizedData_NoSpecialChar();
		String renamedTaskList = getLocalizedData_NoSpecialChar();
		page.zTaskApp.zTaskListCreateNewBtn(orgTaskList);
		obj.zTaskFolder.zExists(orgTaskList);
		page.zTaskApp.zTaskListRename(orgTaskList, renamedTaskList);
		obj.zTaskFolder.zNotExists(orgTaskList);
		obj.zTaskFolder.zExists(renamedTaskList);

		SelNGBase.needReset.set(false);
	}



	@Test(dataProvider = "taskCreateDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void tryToCreateDuplicateTaskFolder() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String orgTaskList = getLocalizedData_NoSpecialChar();
		page.zTaskApp.zTaskListCreateNewBtn(orgTaskList);
		SleepUtil.sleep(1000);
		obj.zButton
				.zRtClick(replaceUserNameInStaticId(page.zTaskApp.zNewTasksOverviewPaneIcon));
		obj.zMenuItem.zClick(localize(locator.newTaskFolder));
		obj.zEditField.zTypeInDlgByName(localize(locator.name), orgTaskList,
				localize(locator.createNewTaskFolder));
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.createNewTaskFolder));
		assertReport(localize(locator.errorAlreadyExists, orgTaskList, ""),
				obj.zDialog.zGetMessage(localize(locator.criticalMsg)),
				"Verifying dialog message");
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.criticalMsg));
		obj.zButton.zClickInDlgByName(localize(locator.cancel),
				localize(locator.createNewTaskFolder));

		SelNGBase.needReset.set(false);
	}
}
