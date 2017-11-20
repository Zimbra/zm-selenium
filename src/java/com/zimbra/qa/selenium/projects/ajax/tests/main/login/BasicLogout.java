/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.main.login;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;

public class BasicLogout extends AjaxCore {

	public BasicLogout() {
		logger.info("New "+ BasicLogout.class.getCanonicalName());
	}


	@Test (description = "Logout of the Ajax Client",
			groups = { "sanity", "L0" })

	public void BasicLogout_01() throws HarnessException {

		// Login
		app.zPageMain.zLogout();

		// Verify main page becomes active
		ZAssert.assertTrue(app.zPageLogin.zIsActive(), "Verify that the account is logged out");
	}
}