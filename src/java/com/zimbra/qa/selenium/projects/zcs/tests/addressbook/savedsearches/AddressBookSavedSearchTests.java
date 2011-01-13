package com.zimbra.qa.selenium.projects.zcs.tests.addressbook.savedsearches;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import com.zimbra.common.service.ServiceException;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.ActionMethod;


/**
 * @author Jitesh Sojitra
 */

@SuppressWarnings("static-access")
public class AddressBookSavedSearchTests extends CommonTest {
	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "dataProvider")
	public Object[][] createData(Method method) throws ServiceException {
		String test = method.getName();
		if (test.equals("contactSavedSearchTest")) {
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
		super.NAVIGATION_TAB="address book";
		super.zLogin();
	}
	
	//--------------------------------------------------------------------------
	// SECTION 3: TEST-METHODS
	//--------------------------------------------------------------------------
	@Test(dataProvider = "dataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void contactSavedSearchTest(String lastName) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		ContactItem contact = new ContactItem();
		contact.lastName = lastName;

		page.zABCompose.createItem(ActionMethod.DEFAULT, contact);

		ClientSessionFactory.session().selenium().type("xpath=//input[@class='search_input']", lastName);
		obj.zButton.zClick(page.zMailApp.zSearchIconBtn);
		obj.zContactListItem.zExists(lastName);
		obj.zButton.zClick("id=zb__Search__SAVE_left_icon");
		obj.zEditField.zTypeInDlgByName("id=*nameField", "Srch" + lastName,
				localize(locator.saveSearch));
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.saveSearch));
		obj.zFolder.zClick(page.zABCompose.zEmailedContactsFolder);
		obj.zContactListItem.zNotExists(lastName);
		obj.zFolder.zClick("Srch" + lastName);
		obj.zContactListItem.zExists(lastName);

		SelNGBase.needReset.set(false);
	}
}