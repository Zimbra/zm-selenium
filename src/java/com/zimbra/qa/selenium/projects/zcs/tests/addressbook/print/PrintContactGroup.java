/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011 VMware, Inc.
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


public class PrintContactGroup extends CommonTest {

	public PrintContactGroup() {
		
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
			description = "Print a contact group",
			groups = { "smoke", "full" }
		)
	public void printContactGroup01() throws HarnessException {
		throw new HarnessException("implement me!");
	}

	@Test(
			description = "Print two contact groups by shift-select",
			groups = { "smoke", "full" }
		)
	public void printContactGroup02() throws HarnessException {
		throw new HarnessException("implement me!");
	}
	
	@Test(
			description = "Print three contact groupss by shift select",
			groups = { "smoke", "full" }
		)
	public void printContactGroup03() throws HarnessException {
		throw new HarnessException("implement me!");
	}

}
