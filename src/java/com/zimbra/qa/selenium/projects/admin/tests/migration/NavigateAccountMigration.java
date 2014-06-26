/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.migration;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageAccountMigration;

public class NavigateAccountMigration extends AdminCommonTest {
	
	public NavigateAccountMigration() {
		logger.info("New "+ NavigateAccountMigration.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage = app.zPageManageAccountMigration;
	}
	
	/**
	 * Testcase : Navigate to Accounts Migration page
	 * Steps :
	 * 1. Verify navigation path -- "Home --> Tools and Migraton --> Account Migration"
	 * @throws HarnessException
	 */
	@Test(	description = "Navigate to Account Migration",
			groups = { "sanity" })
			public void NavigateAccountMigration_01() throws HarnessException {
		
		/*
		 * Verify navigation path -- "Home --> Tools and Migraton --> Account Migration"
		 */
		ZAssert.assertTrue(app.zPageManageAccountMigration.zVerifyHeader(PageManageAccountMigration.Locators.HOME), "Verfiy the \"Home\" text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageAccountMigration.zVerifyHeader(PageManageAccountMigration.Locators.TOOLS_AND_MIGRATION), "Verfiy the \"Tools and Migration\" text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageAccountMigration.zVerifyHeader(PageManageAccountMigration.Locators.ACCOUNT_MIGRATION), "Verfiy the \"Account Migration\" text exists in navigation path");
		
	}

}
