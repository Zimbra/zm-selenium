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
package com.zimbra.qa.selenium.projects.admin.tests.searchmail;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageBackups.Locators;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageSearchMail;

public class NavigateSearchMail extends AdminCommonTest {
	
	public NavigateSearchMail() {
		logger.info("New "+ NavigateSearchMail.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage = app.zPageManageSearchMail;
	}
	
	/**
	 * Testcase : Navigate to Search Mail page
	 * Steps :
	 * 1. Verify navigation path -- "Home --> Tools and Migraton --> Search Mail"
	 * @throws HarnessException
	 */
	@Test( description = "Navigate to Search Mail",
			groups = { "sanity", "L0","network" })
			public void NavigateAccountSearchMail_01() throws HarnessException {
		
		/*
		 * Verify navigation path -- "Home --> Tools and Migraton --> Search Mail"
		 */
		app.zPageManageSearchMail.zClickAt(Locators.TOOLS_AND_MIGRATION_ICON, "");
		SleepUtil.sleepLong();
		app.zPageManageSearchMail.sClickAt(PageManageSearchMail.Locators.SEARCHMAIL, "");
		ZAssert.assertTrue(app.zPageManageSearchMail.zVerifyHeader(PageManageSearchMail.Locators.HOME), "Verfiy the \"Home\" text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearchMail.zVerifyHeader(PageManageSearchMail.Locators.TOOLS_AND_MIGRATION), "Verfiy the \"Tools and Migration\" text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearchMail.zVerifyHeader(PageManageSearchMail.Locators.SEARCH_MAIL), "Verfiy the \"Search Mail\" text exists in navigation path");
		
	}

}
