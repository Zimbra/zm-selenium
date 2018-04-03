/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.util.CommandLineUtility;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;

public class LoginWithCsrfTokenCheckDisabled extends AjaxCore {

	public LoginWithCsrfTokenCheckDisabled() {
		logger.info("New "+ LoginWithCsrfTokenCheckDisabled.class.getCanonicalName());
		super.startingPage = app.zPageLogin;
	}


	@Test (description = "Login to the webclient after disabling csrf check", priority=5,
			groups = { "smoke", "L0", "non-aws" })

	public void LoginWithCsrfTokenCheckDisabled_01() throws HarnessException {
		try {

			String zimbraCsrfTokenCheckEnabledValue = "FALSE";

			// Change zimbraCsrfTokenCheckEnabled value to false
			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<ModifyConfigRequest xmlns='urn:zimbraAdmin'>"
							+		"<a n='zimbraCsrfTokenCheckEnabled'>"+ zimbraCsrfTokenCheckEnabledValue + "</a>"
							+	"</ModifyConfigRequest>");

			// Restart zimbra services
			CommandLineUtility.runCommandOnZimbraServer(ZimbraAccount.AccountZCS().zGetAccountStoreHost(),
					"zmmailboxdctl restart");
			SleepUtil.sleepVeryLong();

			for (int i=0; i<=5; i++) {
				app.zPageMain.zRefreshUI();
				if (app.zPageLogin.sIsElementPresent("css=input[class^='ZLoginButton']") == true ||
						app.zPageLogin.sIsElementPresent("css=div[id$='parent-ZIMLET'] td[id$='ZIMLET_textCell']") == true) {
					break;
				} else {
					SleepUtil.sleepLong();
					if (i == 3) {
						CommandLineUtility.runCommandOnZimbraServer(ZimbraAccount.AccountZCS().zGetAccountStoreHost(),
								"zmmailboxdctl restart");
						SleepUtil.sleepLongMedium();
					}
					continue;
				}
			}

			// Login
			app.zPageLogin.zLogin(ZimbraAccount.AccountZCS());

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

			CommandLineUtility.runCommandOnZimbraServer(ZimbraAccount.AccountZCS().zGetAccountStoreHost(),
					"zmmailboxdctl restart");
			SleepUtil.sleepVeryLong();

			for (int i=0; i<=5; i++) {
				app.zPageMain.zRefreshUI();
				if (app.zPageLogin.sIsElementPresent("css=input[class^='ZLoginButton']") == true ||
						app.zPageLogin.sIsElementPresent("css=div[id$='parent-ZIMLET'] td[id$='ZIMLET_textCell']") == true) {
					break;
				} else {
					SleepUtil.sleepLong();
					if (i == 3) {
						CommandLineUtility.runCommandOnZimbraServer(ZimbraAccount.AccountZCS().zGetAccountStoreHost(),
								"zmmailboxdctl restart");
						SleepUtil.sleepLongMedium();
					}
					continue;
				}
			}
		}
	}
}
