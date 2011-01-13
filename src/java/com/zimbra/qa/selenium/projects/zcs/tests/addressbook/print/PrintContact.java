package com.zimbra.qa.selenium.projects.zcs.tests.addressbook.print;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.zcs.PageObjects;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.ActionMethod;


public class PrintContact extends CommonTest {

	public PrintContact() {
		
	}
	
	// --------------
	// section 2 BeforeClass
	// --------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.NAVIGATION_TAB="address book";
		super.zLogin();
	}
	
	@Test(
			description = "Print a contact",
			groups = { "smoke", "full" }
		)
	public void printContact01() throws HarnessException {
		throw new HarnessException("implement me!");
	}

	@Test(
			description = "Print two contacts by shift-select",
			groups = { "smoke", "full" }
		)
	public void printContact02() throws HarnessException {
		throw new HarnessException("implement me!");
	}
	
	@Test(
			description = "Print three contacts by shift select",
			groups = { "smoke", "full" }
		)
	public void printContact03() throws HarnessException {
		throw new HarnessException("implement me!");
	}

	@Test(
			description = "Print one contact and one contact group by shift select",
			groups = { "smoke", "full" }
		)
	public void printContact04() throws HarnessException {
		throw new HarnessException("implement me!");
	}

}
