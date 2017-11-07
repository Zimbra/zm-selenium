/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.admin.tests.adminextensions;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageAdminExtensions;

public class NavigateAdminExtensions extends AdminCommonTest {
	public NavigateAdminExtensions() {
		logger.info("New "+ NavigateAdminExtensions.class.getCanonicalName());

		// All tests start at the "Cos" page
		super.startingPage = app.zPageManageAdminExtensions;
	}
	
	/**
	 * Testcase : Navigate to Admin Extensions page
	 * Steps :
	 * 1. Go to Accounts
	 * 2. Verify navigation path -- "Home --> Configure --> Admin Extensions"
	 * @throws HarnessException
	 */
	@Test (description = "Navigate to Admin Extensions",
			groups = { "sanity", "L0" })
			public void NavigateAdminExtensions_01() throws HarnessException {
		
		/*
		 * Verify navigation path -- "Home --> Configure --> Admin Extensions"
		 */
		ZAssert.assertTrue(app.zPageManageAdminExtensions.zVerifyHeader(PageManageAdminExtensions.Locators.HOME), "Verfiy the \"Home\" text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageAdminExtensions.zVerifyHeader(PageManageAdminExtensions.Locators.CONFIGURE), "Verfiy the \"Configure\" text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageAdminExtensions.zVerifyHeader(PageManageAdminExtensions.Locators.ADMIN_EXTENSIONS), "Verfiy the \"Admin Extensions\" text exists in navigation path");
		
	}

}
