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
package com.zimbra.qa.selenium.projects.admin.tests.advancedstatistics;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageAdvancedStatistics;

public class NavigateAdvancedStatistics extends AdminCommonTest {

	public NavigateAdvancedStatistics() {
		logger.info("New "+ NavigateAdvancedStatistics.class.getCanonicalName());
		super.startingPage = app.zPageManageAdvancedStatistics;
	}

	
	/**
	 * Testcase : Navigate to Advanced Statistics page
	 * Steps :
	 * 1. Verify navigation path -- "Home --> Monitor --> Advanced Statistics"
	 * @throws HarnessException
	 */
	
	@Test (description = "Navigate to Advanced Statistics",
			groups = { "sanity", "L0" })
	
	public void NavigateAdvancedStatistics_01() throws HarnessException {

		/*
		 * Verify navigation path -- "Home --> Monitor --> Advanced Statistics"
		 */
		ZAssert.assertTrue(app.zPageManageAdvancedStatistics.zVerifyHeader(PageManageAdvancedStatistics.Locators.HOME),
				"Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(
				app.zPageManageAdvancedStatistics.zVerifyHeader(PageManageAdvancedStatistics.Locators.MONITOR),
				"Verfiy the 'Monitor' text exists in navigation path");
		ZAssert.assertTrue(
				app.zPageManageAdvancedStatistics
						.zVerifyHeader(PageManageAdvancedStatistics.Locators.ADVANCED_STATISTICS),
				"Verfiy the 'Advanced Statistics' text exists in navigation path");
	}
}