package com.zimbra.qa.selenium.projects.ajax.tests.tasks;

import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import java.util.List;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.TaskItem;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.GeneralUtility;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;

import com.zimbra.qa.selenium.projects.ajax.ui.tasks.FormTaskNew;
import com.zimbra.qa.selenium.projects.ajax.ui.tasks.PageTasks;
import com.zimbra.qa.selenium.projects.ajax.ui.tasks.FormTaskNew.Field;

public class CancelTask extends AjaxCommonTest {
	public CancelTask() {
		logger.info("New " + CancelTask.class.getCanonicalName());

		super.startingPage = app.zPageTasks;

		super.startingAccountPreferences = null;
	}

	@Test(description = "Create Simple task through GUI - verify through GUI", groups = { "sanity" })
	public void CancelTask_01() throws HarnessException {

		String subject = "task" + ZimbraSeleniumProperties.getUniqueString();
		String body = "taskbody" + ZimbraSeleniumProperties.getUniqueString();

		GeneralUtility.waitForElementPresent(app.zPageTasks,
				PageTasks.Locators.zTasks, 2000);
		app.zPageTasks.zClick("zb__App__Tasks");
		GeneralUtility.waitForElementPresent(app.zPageTasks,
				PageTasks.Locators.zNewTask, 20000);
		FormTaskNew taskNew = (FormTaskNew) app.zPageTasks
				.zToolbarPressButton(Button.B_NEW);
		taskNew.zFillField(Field.Subject, subject);
		taskNew.zFillField(Field.Body, body);

		AbsDialog warning = (AbsDialog) taskNew
				.zToolbarPressButton(Button.B_CANCEL);
		logger.info(warning);

		ZAssert.assertNotNull(warning, "Verify the dialog is returned");
		warning.zClickButton(Button.B_NO);

		// Get the list of tasks in the view
		List<TaskItem> tasks = app.zPageTasks.zGetTasks();
		ZAssert.assertNull(tasks, "Verify the list of tasks doesn't exists");

	}
}
