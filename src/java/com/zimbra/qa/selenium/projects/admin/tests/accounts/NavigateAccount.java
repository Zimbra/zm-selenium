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
package com.zimbra.qa.selenium.projects.admin.tests.accounts;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.PageManageAccounts;

public class NavigateAccount extends AdminCore {

	public NavigateAccount() {
		logger.info("New "+ NavigateAccount.class.getCanonicalName());
		super.startingPage = app.zPageManageAccounts;
	}


	/**
	 * Testcase : Navigate to Accounts page
	 * Steps :
	 * 1. Go to Accounts
	 * 2. Verify navigation path -- "Home --> Manage --> Accounts"
	 * @throws HarnessException
	 */

	@Test (description = "Navigate to Accounts",
			groups = { "smoke" })

	public void NavigateAccount_01() throws HarnessException {

		/*
		 * Verify navigation path -- "Home --> Manage Accounts --> Accounts"
		 */
		ZAssert.assertTrue(app.zPageManageAccounts.zVerifyHeader(PageManageAccounts.Locators.HOME),
				"Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageAccounts.zVerifyHeader(PageManageAccounts.Locators.MANAGE),
				"Verfiy the 'Manage Accounts' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageAccounts.zVerifyHeader(PageManageAccounts.Locators.ACCOUNT),
				"Verfiy the 'Accounts' text exists in navigation path");
	}
}