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
package com.zimbra.qa.selenium.projects.admin.tests.softwareupdates;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.PageManageSoftwareUpdates;

public class NavigateSoftwareUpdates extends AdminCore {

	public NavigateSoftwareUpdates() {
		logger.info("New "+ NavigateSoftwareUpdates.class.getCanonicalName());
		super.startingPage = app.zPageManageSoftwareUpdates;
	}


	/**
	 * Testcase : Navigate to Software Updates page
	 * Steps :
	 * 1. Verify navigation path -- "Home --> Tools and Migraton --> Software Updates"
	 * @throws HarnessException
	 */

	@Test (description = "Navigate to Software Updates",
			groups = { "sanity", "L0" })

	public void NavigateAccountMigration_01() throws HarnessException {

		/*
		 * Verify navigation path -- "Home --> Tools and Migraton --> Software Updates"
		 */
		ZAssert.assertTrue(app.zPageManageSoftwareUpdates.zVerifyHeader(PageManageSoftwareUpdates.Locators.HOME),
				"Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(
				app.zPageManageSoftwareUpdates.zVerifyHeader(PageManageSoftwareUpdates.Locators.TOOLS_AND_MIGRATION),
				"Verfiy the 'Tools and Migration' text exists in navigation path");
		ZAssert.assertTrue(
				app.zPageManageSoftwareUpdates.zVerifyHeader(PageManageSoftwareUpdates.Locators.SOFTWARE_UPDATES),
				"Verfiy the 'Software Updates' text exists in navigation path");
	}
}