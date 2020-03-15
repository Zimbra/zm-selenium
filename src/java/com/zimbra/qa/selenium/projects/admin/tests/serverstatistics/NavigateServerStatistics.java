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
package com.zimbra.qa.selenium.projects.admin.tests.serverstatistics;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.PageManageServerStatistics;

public class NavigateServerStatistics extends AdminCore {

	public NavigateServerStatistics() {
		logger.info("New "+ NavigateServerStatistics.class.getCanonicalName());
		super.startingPage = app.zPageManageServerStatistics;
	}


	@Test (description = "Navigate to Server Statistics",
			groups = { "smoke" })

	public void NavigateServerStatistics_01() throws HarnessException {
		ZAssert.assertTrue(app.zPageManageServerStatistics.zVerifyHeader(PageManageServerStatistics.Locators.HOME),
				"Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageServerStatistics.zVerifyHeader(PageManageServerStatistics.Locators.MONITOR),
				"Verfiy the 'Monitor' text exists in navigation path");
		ZAssert.assertTrue(
				app.zPageManageServerStatistics.zVerifyHeader(PageManageServerStatistics.Locators.SERVER_STATISTICS),
				"Verfiy the 'Server Statistics' text exists in navigation path");
	}
}