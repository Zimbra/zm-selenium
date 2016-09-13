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
package com.zimbra.qa.selenium.projects.admin.tests.resources;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageResources;

public class NavigateResource extends AdminCommonTest {

	public NavigateResource() {
		logger.info("New "+ NavigateResource.class.getCanonicalName());

		// All tests start at the "Resource" page
		super.startingPage = app.zPageManageResources;
	}
	
	/**
	 * Testcase : Navigate to Resource page
	 * Steps :
	 * 1. Go to Resource
	 * 2. Verify navigation path -- "Home --> Manage --> Resources"
	 * @throws HarnessException
	 */
	@Test( description = "Navigate to Resource",
			groups = { "sanity" })
			public void NavigateResource_01() throws HarnessException {
		
		/*
		 * Verify navigation path -- "Home --> Manage Accounts --> Resources"
		 */
		ZAssert.assertTrue(app.zPageManageResources.zVerifyHeader(PageManageResources.Locators.HOME), "Verfiy the \"Home\" text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageResources.zVerifyHeader(PageManageResources.Locators.MANAGE), "Verfiy the \"Manage Accounts\" text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageResources.zVerifyHeader(PageManageResources.Locators.RESOURCE), "Verfiy the \"Resources\" text exists in navigation path");
	}
}
