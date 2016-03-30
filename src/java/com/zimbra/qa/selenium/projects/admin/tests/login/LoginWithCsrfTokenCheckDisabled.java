/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.admin.tests.login;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.framework.util.staf.StafServicePROCESS;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;

public class LoginWithCsrfTokenCheckDisabled extends AdminCommonTest {

	public LoginWithCsrfTokenCheckDisabled() {
		logger.info("New "+ LoginWithCsrfTokenCheckDisabled.class.getCanonicalName());

		// All tests start at the login page
		super.startingPage = app.zPageLogin;
		super.startingAccount = null;

	}

	@Test(	description = "Login to the admin console after disabling csrf check",
			groups = { "smoke" })
	public void LoginWithCsrfTokenCheckDisabled_01() throws HarnessException {
		try {

			String zimbraCsrfTokenCheckEnabledValue = "FALSE";

			// Change zimbraCsrfTokenCheckEnabled value to false
			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<ModifyConfigRequest xmlns='urn:zimbraAdmin'>"
							+		"<a n='zimbraCsrfTokenCheckEnabled'>"+ zimbraCsrfTokenCheckEnabledValue + "</a>"
							+	"</ModifyConfigRequest>");

			// Restart zimbra services
			StafServicePROCESS staf = new StafServicePROCESS();
			staf.execute("zmmailboxdctl restart");

			// Wait for the service to come up
			SleepUtil.sleep(60000);

			staf.execute("zmcontrol status");

			SleepUtil.sleepMedium();

			// Login
			app.zPageLogin.login(gAdmin);

			// Verify main page becomes active
			ZAssert.assertTrue(app.zPageMain.zIsActive(), "Verify that the account is logged in");
		}

		finally {
		
			String zimbraCsrfTokenCheckEnabledValue = "TRUE";
			
			// Change zimbraCsrfTokenCheckEnabled value to false
						ZimbraAdminAccount.GlobalAdmin().soapSend(
								"<ModifyConfigRequest xmlns='urn:zimbraAdmin'>"
										+		"<a n='zimbraCsrfTokenCheckEnabled'>"+ zimbraCsrfTokenCheckEnabledValue + "</a>"
										+	"</ModifyConfigRequest>");

						StafServicePROCESS staf = new StafServicePROCESS();
						staf.execute("zmmailboxdctl restart");
						
			// Open the base URL
			app.zPageLogin.sOpen(ZimbraSeleniumProperties.getBaseURL());
		}

	}
}
