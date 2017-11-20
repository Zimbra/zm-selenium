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
package com.zimbra.qa.selenium.projects.admin.tests.distributionlists;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.PageManageDistributionLists;

public class NavigateDistributionList extends AdminCore {

	public NavigateDistributionList() {
		logger.info("New "+ NavigateDistributionList.class.getCanonicalName());
		super.startingPage = app.zPageManageDistributionList;
	}


	/**
	 * Testcase : Navigate to DL page
	 * Steps :
	 * 1. Go to DL
	 * 2. Verify navigation path -- "Home --> Manage --> Distribution Lists"
	 * @throws HarnessException
	 */

	@Test (description = "Navigate to DL",
			groups = { "sanity", "L0" })

	public void NavigateDistributionList_01() throws HarnessException {

		/*
		 * Verify navigation path -- "Home --> Manage Accounts --> Distribution Lists"
		 */
		ZAssert.assertTrue(app.zPageManageDistributionList.zVerifyHeader(PageManageDistributionLists.Locators.HOME),
				"Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageDistributionList.zVerifyHeader(PageManageDistributionLists.Locators.MANAGE),
				"Verfiy the 'Manage Accounts' text exists in navigation path");
		ZAssert.assertTrue(
				app.zPageManageDistributionList.zVerifyHeader(PageManageDistributionLists.Locators.DISTRIBUTION_LIST),
				"Verfiy the 'Distribution Lists' text exists in navigation path");
	}
}