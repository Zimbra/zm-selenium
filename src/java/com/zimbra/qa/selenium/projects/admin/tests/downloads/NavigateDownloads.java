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
package com.zimbra.qa.selenium.projects.admin.tests.downloads;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.PageDownloads;
import com.zimbra.qa.selenium.projects.admin.pages.PageMain;

public class NavigateDownloads extends AdminCore {

	public NavigateDownloads() {
		logger.info("New "+ NavigateDownloads.class.getCanonicalName());
		super.startingPage = app.zPageDownloads;
	}


	/**
	 * Testcase : Navigate to Downloads page
	 * Steps :
	 * 1. Go to Downloads
	 * 2. Verify navigation path -- "Home --> Tools and Migration --> Downloads"
	 * @throws HarnessException
	 */

	@Test (description = "Navigate to Downloads",
			groups = { "smoke" })

	public void NavigateDownloads_01() throws HarnessException {

		/*
		 * Verify navigation path -- "Home --> Tools and Migration --> Downloads"
		 */
		ZAssert.assertTrue(app.zPageDownloads.zVerifyHeader(PageDownloads.Locators.HOME),
				"Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageDownloads.zVerifyHeader(PageDownloads.Locators.TOOLS_AND_MIGRATION),
				"Verfiy the 'Tools and Migration' text exists in navigation path");
		ZAssert.assertTrue(app.zPageDownloads.zVerifyHeader(PageDownloads.Locators.DOWNLOAD),
				"Verfiy the 'Downloads' text exists in navigation path");
	}


	@AfterMethod(groups = { "always" })
	public void afterMethod() throws HarnessException {
		logger.info("Opening base URL...");
		app.zPageMain.sOpen(ConfigProperties.getBaseURL());
		app.zPageMain.zWaitTillElementPresent(PageMain.Locators.zHelpButton);
	}
}