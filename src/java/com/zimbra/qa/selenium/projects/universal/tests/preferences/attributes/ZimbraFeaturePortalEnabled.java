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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.attributes;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;



public class ZimbraFeaturePortalEnabled extends UniversalCommonTest {
	
	public ZimbraFeaturePortalEnabled() {
		logger.info("New "+ ZimbraFeaturePortalEnabled.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageLogin;
		super.startingAccountPreferences = new HashMap<String , String>() {
			private static final long serialVersionUID = -3123410183252792255L;
		{
		    put("zimbraFeaturePortalEnabled", "TRUE");
		    put("zimbraPortalName", "example");
		}};
		
	}
	
	@Bugs(ids = "67462")
	@Test( description = "Login to the Ajax Client with the 'example' portal enabled",
			groups = { "functional", "L2" })
	public void BasicLogin01() throws HarnessException {
		
		// Login
		app.zPageLogin.zLogin(ZimbraAccount.AccountZWC());
		
		// Verify main page becomes active
		ZAssert.assertTrue(app.zPageMain.zIsActive(), "Verify that the account is logged in");
		
		
		ZAssert.assertTrue(app.zPageMain.sIsElementPresent("css=td#zb__App__Portal_title"), "Verify the 'Home' tab is present");
		ZAssert.assertTrue(app.zPageMain.zIsVisiblePerPosition("css=td#zb__App__Portal_title", 0, 0), "Verify the 'Home' tab is present");
		
	}


}
