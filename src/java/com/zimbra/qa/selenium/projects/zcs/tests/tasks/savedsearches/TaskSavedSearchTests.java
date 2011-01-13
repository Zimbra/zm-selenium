package com.zimbra.qa.selenium.projects.zcs.tests.tasks.savedsearches;

import java.lang.reflect.Method;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.zimbra.common.service.ServiceException;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;


/**
 * @author Jitesh Sojitra
 */

@SuppressWarnings("static-access")
public class TaskSavedSearchTests extends CommonTest {
	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "dataProvider")
	public Object[][] createData(Method method) throws ServiceException {
		String test = method.getName();
		if (test.equals("taskSavedSearchTest")) {
			return new Object[][] { { getLocalizedData_NoSpecialChar() } };
		} else {
			return new Object[][] { { "" } };
		}
	}

	//--------------------------------------------------------------------------
	// SECTION 2: SETUP
	//--------------------------------------------------------------------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.NAVIGATION_TAB="tasks";
		super.zLogin();
	}


	//--------------------------------------------------------------------------
	// SECTION 3: TEST-METHODS
	//--------------------------------------------------------------------------
	@Test(dataProvider = "dataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void taskSavedSearchTest(String subject) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		page.zTaskApp.zTaskCreateSimple(subject, "", "", "");
		page.zTaskApp.zTaskListCreateNewBtn("newTaskFolder");
		ClientSessionFactory.session().selenium().type("xpath=//input[@class='search_input']", subject);
		obj.zButton.zClick(page.zMailApp.zSearchIconBtn);
		obj.zTaskItem.zExists(subject);
		obj.zButton.zClick("id=zb__Search__SAVE_left_icon");
		obj.zEditField.zTypeInDlgByName("id=*nameField", "Srch" + subject,
				localize(locator.saveSearch));
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.saveSearch));
		obj.zFolder.zClick("newTaskFolder");
		obj.zTaskItem.zNotExists(subject);
		obj.zFolder.zClick("Srch" + subject);
		obj.zTaskItem.zExists(subject);

		SelNGBase.needReset.set(false);
	}

}