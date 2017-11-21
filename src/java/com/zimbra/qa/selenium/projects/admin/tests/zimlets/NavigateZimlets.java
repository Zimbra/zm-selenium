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
package com.zimbra.qa.selenium.projects.admin.tests.zimlets;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.PageManageZimlets;

public class NavigateZimlets extends AdminCore {

	public NavigateZimlets() {
		logger.info("New "+ NavigateZimlets.class.getCanonicalName());
		super.startingPage = app.zPageManageZimlets;
	}

	/**
	 * Testcase : Navigate to Zimlets page
	 * Steps :
	 * 1. Go to Accounts
	 * 2. Verify navigation path -- "Home --> Configure --> Zimlets"
	 * @throws HarnessException
	 */

	@Test (description = "Navigate to Zimlets",
			groups = { "sanity", "L0" })

	public void NavigateZimlets_01() throws HarnessException {

		/*
		 * Verify navigation path -- "Home --> Configure --> Zimlets"
		 */
		ZAssert.assertTrue(app.zPageManageZimlets.zVerifyHeader(PageManageZimlets.Locators.HOME),
				"Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageZimlets.zVerifyHeader(PageManageZimlets.Locators.CONFIGURE),
				"Verfiy the 'Configure' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageZimlets.zVerifyHeader(PageManageZimlets.Locators.ZIMLETS),
				"Verfiy the 'Zimlets' text exists in navigation path");
	}
}