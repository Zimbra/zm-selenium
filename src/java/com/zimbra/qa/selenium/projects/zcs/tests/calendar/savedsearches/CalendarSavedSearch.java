package com.zimbra.qa.selenium.projects.zcs.tests.calendar.savedsearches;

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
public class CalendarSavedSearch extends CommonTest {
	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "dataProvider")
	public Object[][] createData(Method method) throws ServiceException {
		String test = method.getName();
		if (test.equals("appointmentSavedSearchTest")) {
			return new Object[][] { { getLocalizedData_NoSpecialChar() } };
		} else {
			return new Object[][] { { "" } };
		}
	}
	// --------------
	// section 2 BeforeClass
	// --------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.NAVIGATION_TAB="calendar";
		super.zLogin();
	}

	//--------------------------------------------------------------------------
	// SECTION 3: TEST-METHODS
	//--------------------------------------------------------------------------
	@Test(dataProvider = "dataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void appointmentSavedSearchTest(String subject) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		page.zCalCompose.zCreateSimpleAppt(subject);
		page.zCalCompose.zCreateSimpleAppt("123xyz");

		ClientSessionFactory.session().selenium().type("xpath=//input[@class='search_input']", subject);
		obj.zButton.zClick(page.zMailApp.zSearchIconBtn);
		obj.zAppointment.zExists(subject);
		obj.zAppointment.zNotExists("123xyz");
		obj.zButton.zClick("id=zb__Search__SAVE_left_icon");
		obj.zEditField.zTypeInDlgByName("id=*nameField", "Srch" + subject,
				localize(locator.saveSearch));
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.saveSearch));
		obj.zFolder.zClick("Srch" + subject);
		obj.zAppointment.zExists(subject);
		obj.zAppointment.zNotExists("123xyz");

		SelNGBase.needReset.set(false);
	}
}