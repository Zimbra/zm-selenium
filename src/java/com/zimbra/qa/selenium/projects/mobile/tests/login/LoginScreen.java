/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.mobile.tests.login;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.mobile.core.MobileCore;
import com.zimbra.qa.selenium.projects.mobile.pages.PageLogin;


public class LoginScreen extends MobileCore {

	public LoginScreen() {
		logger.info("New "+ LoginScreen.class.getCanonicalName());

		// All tests start at the login page
		super.startingPage = app.zPageLogin;
		super.startingAccount = null;

	}

	@Test (description = "Verify the label text on the mobile client login screen",
			groups = { "smoke" })
	public void LoginScreen01() throws HarnessException {
		
		String username = app.zPageLogin.sGetText(PageLogin.Locators.zDisplayedusername);
		ZAssert.assertEquals(username, app.zGetLocaleString("usernameLabel"), "Verify the displayed label 'username'");
		

	}
	
	@Test (description = "Verify the copyright on the login screen contains the current year",
			groups = { "smoke" })
	public void LoginScreen02() throws HarnessException {
		
		Calendar calendar = new GregorianCalendar();
		String thisYear = "" + calendar.get(Calendar.YEAR);
		
		String copyright = app.zPageLogin.sGetText(PageLogin.Locators.zDisplayedcopyright);
		
		String message = String.format("Verify the copyright (%s) on the login screen contains the current year (%s)", copyright, thisYear);
		ZAssert.assertStringContains(copyright, thisYear, message);
		

	}

}
