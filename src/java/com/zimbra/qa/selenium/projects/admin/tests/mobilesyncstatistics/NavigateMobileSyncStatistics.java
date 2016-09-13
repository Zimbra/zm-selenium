/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.mobilesyncstatistics;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageMobileSyncStatistics;

public class NavigateMobileSyncStatistics extends AdminCommonTest {
	
	public NavigateMobileSyncStatistics() {
		logger.info("New "+ NavigateMobileSyncStatistics.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage = app.zPageManageMobileSyncStatistics;
	}
	
	/**
	 * Testcase : Navigate to Client Upload page
	 * Steps :
	 * 1. Verify navigation path -- "Home --> Monitor --> MobileSync Statistics"
	 * @throws HarnessException
	 */
	@Test( description = "Navigate to MobileSync Statistics",
			groups = { "sanity","network" })
			public void NavigateMobileSync_01() throws HarnessException {
		
		/*
		 * Verify navigation path -- "Home --> Tools and Migraton --> Client Upload"
		 */
		ZAssert.assertTrue(app.zPageManageMobileSyncStatistics.zVerifyHeader(PageManageMobileSyncStatistics.Locators.HOME), "Verfiy the \"Home\" text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageMobileSyncStatistics.zVerifyHeader(PageManageMobileSyncStatistics.Locators.MONITOR), "Verfiy the \"Monitor\" text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageMobileSyncStatistics.zVerifyHeader(PageManageMobileSyncStatistics.Locators.MOBILESYNC_STATISTICS), "Verfiy the \"MobileSync Statistics\" text exists in navigation path");
		
	}

}
