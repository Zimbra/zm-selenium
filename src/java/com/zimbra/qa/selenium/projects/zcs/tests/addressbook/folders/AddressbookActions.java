package com.zimbra.qa.selenium.projects.zcs.tests.addressbook.folders;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.ActionMethod;


/**
 * @written by Prashant Jaiswal & updated by Jitesh
 * 
 */
@SuppressWarnings("static-access")
public class AddressbookActions extends CommonTest {
	
	
	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------

	// --------------
	// section 2 BeforeClass
	// --------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.NAVIGATION_TAB="address book";
		super.zLogin();
	}
	
	/**
	 * Rename an addressbook
	 * 
	 * Steps:
	 * 1. Create a new addressbook
	 * 2. Rename the addressbook
	 */
	@Test(
			description = "Rename an addressbook",
			groups = { "smoke", "full" }, 
			retryAnalyzer = RetryFailedTests.class)
	public void renameAddressbook01() throws Exception {
		
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();


		SelNGBase.needReset.set(false);
		
		throw new HarnessException("implement me!");

	}
}