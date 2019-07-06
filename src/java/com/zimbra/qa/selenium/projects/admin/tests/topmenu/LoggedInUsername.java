/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.topmenu;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.PageMain;

public class LoggedInUsername extends AdminCore {

	public LoggedInUsername() {
		logger.info("New "+ LoggedInUsername.class.getCanonicalName());
	}


	@Test (description = "Verify the Top Menu displays the correct Admin username",
			groups = { "bhr" })

	public void LoggedInUsername_01() throws HarnessException {

		// Check that the displayed name is contained in the email
		String displayed = app.zPageMain.sGetText(PageMain.Locators.zSkinContainerUsername);
		ZimbraAccount actual = app.zGetActiveAccount();
		ZAssert.assertStringContains(actual.EmailAddress, displayed.split("@")[0], "Verify the correct account display name is shown");
	}
}