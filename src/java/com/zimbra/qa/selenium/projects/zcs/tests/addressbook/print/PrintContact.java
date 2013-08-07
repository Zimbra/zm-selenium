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
