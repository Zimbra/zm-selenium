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
package com.zimbra.qa.selenium.projects.admin.tests.cos;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.PageManageCOS;

public class NavigateCos extends AdminCore {

	public NavigateCos() {
		logger.info("New "+ NavigateCos.class.getCanonicalName());
		super.startingPage = app.zPageManageCOS;
	}


	@Test (description = "Navigate to Cos",
			groups = { "smoke" })

	public void NavigateCos_01() throws HarnessException {
		ZAssert.assertTrue(app.zPageManageCOS.zVerifyHeader(PageManageCOS.Locators.HOME),
				"Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageCOS.zVerifyHeader(PageManageCOS.Locators.CONFIGURE),
				"Verfiy the 'Configure' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageCOS.zVerifyHeader(PageManageCOS.Locators.CLASS_OS_SERVICE),
				"Verfiy the 'Class Of Service' text exists in navigation path");
	}
}