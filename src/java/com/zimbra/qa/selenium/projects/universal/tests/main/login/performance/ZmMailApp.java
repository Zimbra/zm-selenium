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
package com.zimbra.qa.selenium.projects.universal.tests.main.login.performance;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.performance.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCore;
import com.zimbra.qa.selenium.projects.universal.pages.PageLogin.Locators;



public class ZmMailApp extends UniversalCore {
	
	public ZmMailApp() {
		logger.info("New "+ ZmMailApp.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageLogin;
		
		
	}
	
	@Test (description = "Measure the time to load the universal client",
			groups = { "performance", "L4" })
	public void ZmMailApp01() throws HarnessException {
		
		
		app.zPageLogin.zNavigateTo();

		app.zPageLogin.zSetLoginName(ZimbraAccount.AccountZCS().EmailAddress);
		app.zPageLogin.zSetLoginPassword(ZimbraAccount.AccountZCS().Password);

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmMailApp, "Login to the universal client (mail app)");

		// Click the Login button
		app.zPageLogin.sClick(Locators.zBtnLogin);

		PerfMetrics.waitTimestamp(token);
				
		// Wait for the app to load
		app.zPageMain.zWaitForActive();
		
		
	}


}
