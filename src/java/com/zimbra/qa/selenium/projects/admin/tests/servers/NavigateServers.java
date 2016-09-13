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
package com.zimbra.qa.selenium.projects.admin.tests.servers;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageServers;

public class NavigateServers extends AdminCommonTest {
	public NavigateServers() {
		logger.info("New "+ NavigateServers.class.getCanonicalName());

		// All tests start at the "Cos" page
		super.startingPage = app.zPageManageServers;
	}
	
	/**
	 * Testcase : Navigate to Servers page
	 * Steps :
	 * 1. Go to Accounts
	 * 2. Verify navigation path -- "Home --> Configure --> Servers"
	 * @throws HarnessException
	 */
	@Test( description = "Navigate to Servers",
			groups = { "sanity" })
			public void NavigateServers_01() throws HarnessException {
		
		/*
		 * Verify navigation path -- "Home --> Configure --> Servers"
		 */
		ZAssert.assertTrue(app.zPageManageServers.zVerifyHeader(PageManageServers.Locators.HOME), "Verfiy the \"Home\" text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageServers.zVerifyHeader(PageManageServers.Locators.CONFIGURE), "Verfiy the \"Configure\" text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageServers.zVerifyHeader(PageManageServers.Locators.SERVERS), "Verfiy the \"Servers\" text exists in navigation path");
		
	}

}
