/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.admin.tests.domains;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageDomains;

public class NavigateDomain extends AdminCommonTest {
	public NavigateDomain() {
		logger.info("New "+ NavigateDomain.class.getCanonicalName());

		// All tests start at the "Domain" page
		super.startingPage = app.zPageManageDomains;
	}
	
	/**
	 * Testcase : Navigate to Domain page
	 * Steps :
	 * 1. Go to Domains
	 * 2. Verify navigation path -- "Home --> Configure --> Domains"
	 * @throws HarnessException
	 */
	@Test(	description = "Navigate to Domain",
			groups = { "sanity" })
			public void NavigateDomain_01() throws HarnessException {
		
		/*
		 * Verify navigation path -- "Home --> Configure --> Domains"
		 */
		ZAssert.assertTrue(app.zPageManageDomains.zVerifyHeader(PageManageDomains.Locators.HOME), "Verfiy the \"Home\" text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageDomains.zVerifyHeader(PageManageDomains.Locators.CONFIGURE), "Verfiy the \"Configure\" text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageDomains.zVerifyHeader(PageManageDomains.Locators.DOMAIN), "Verfiy the \"Domain\" text exists in navigation path");
		
	}
}
