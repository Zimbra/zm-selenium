/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.main.attributes;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class ZimbraHelpAdvancedURL extends AjaxCommonTest {

	public ZimbraHelpAdvancedURL() {
		logger.info("New " + ZimbraHelpAdvancedURL.class.getCanonicalName());
	}


	@Bugs (ids = "101023")
	@Test (description = "Verify the product help URL", priority=5,
		groups = { "functional", "L3" })

	public void ZimbraHelpAdvancedURL_01() throws HarnessException {

		String url = "/zimbra/help/advanced/zimbra_user_help.htm";
		String domainID = null;
		String tempURL = null;
		boolean found = false;

		try {

			staf.execute("mkdir -p /opt/zimbra/jetty/webapps/zimbra/helpUrl/help/adv && echo '<html><head><title>Zimbra Temp Help</title></head><body><h1>Temp Help</h1><p> This is the new advanced help of zimbra!</p></body></html>' > /opt/zimbra/jetty/webapps/zimbra/helpUrl/help/adv/advhelp.html");

			// To get domain id
			String targetDomain = ConfigProperties.getStringProperty("testdomain");
			ZimbraAdminAccount.AdminConsoleAdmin().soapSend("<GetDomainRequest xmlns='urn:zimbraAdmin'>"
					+ "<domain by='name'>" + targetDomain + "</domain>" + "</GetDomainRequest>");

			domainID = ZimbraAdminAccount.AdminConsoleAdmin()
					.soapSelectValue("//admin:GetDomainResponse/admin:domain", "id").toString();

			// Modify the domain and change the help URL
			ZimbraAdminAccount.AdminConsoleAdmin()
					.soapSend("<ModifyDomainRequest xmlns='urn:zimbraAdmin'>" + "<id>" + domainID + "</id>"
							+ "<a n='zimbraHelpAdvancedURL'>/helpUrl/help/adv/advhelp.html</a>"
							+ "<a n='zimbraVirtualHostname'>" + ConfigProperties.getStringProperty("server.host")
							+ "</a>" + "</ModifyDomainRequest>");

			app.zPageMain.zRefreshMainUI();
			app.zPageMain.zToolbarPressPulldown(Button.B_ACCOUNT, Button.O_PRODUCT_HELP);

			// Zimbra advanced help page can open in separate window
			List<String> windowIds=app.zPageMain.sGetAllWindowIds();

			if (windowIds.size() > 1) {

				for(String id: windowIds) {

				app.zPageMain.sSelectWindow(id);
					if (app.zPageMain.sGetTitle().contains("Not Found") || app.zPageMain.sGetTitle().contains("Help")) {
						//Get the opened URL
						tempURL=app.zPageMain.sGetLocation();
						found = true;
						app.zPageMain.zSeparateWindowClose(app.zPageMain.sGetTitle());
						break;
					} else if (!(app.zPageMain.sGetTitle().contains("Zimbra: Inbox"))) {
								app.zPageMain.zSeparateWindowClose(app.zPageMain.sGetTitle());
					}
				}
				if (!found) {

					tempURL=app.zPageMain.sGetLocation();
				}

			} else {
				tempURL=app.zPageMain.sGetLocation();
			}

			// Check the URL
			ZAssert.assertTrue(tempURL.contains("/helpUrl/help/adv/advhelp.html"),	"Product Help URL is not as set in zimbraHelpAdvancedURL");

		} finally {

			// Revert the changes done in attribute 'zimbraHelpAdvancedURL'
			ZimbraAdminAccount.AdminConsoleAdmin()
					.soapSend("<ModifyDomainRequest xmlns='urn:zimbraAdmin'>" + "<id>" + domainID + "</id>"
							+ "<a n='zimbraHelpAdvancedURL'>" + url + "</a>" + "<a n='zimbraVirtualHostname'>" + ""
							+ "</a>" + "</ModifyDomainRequest>");

			// Restart zimbra services
			staf.execute("zmmailboxdctl restart");

			SleepUtil.sleepVeryLong();
			for (int i = 0; i <= 10; i++) {
				app.zPageLogin.zRefreshMainUI();
				if (app.zPageLogin.sIsElementPresent("css=input[class^='ZLoginButton']") == true ||
						app.zPageLogin.sIsElementPresent("css=div[id$='parent-ZIMLET'] td[id$='ZIMLET_textCell']") == true) {
					break;
				} else {
					SleepUtil.sleepLong();
					if (i == 5) {
						staf.execute("zmmailboxdctl restart");
						SleepUtil.sleepVeryLong();
					}
					continue;
				}
			}

		}
	}


	@Bugs (ids = "ZCS-3487")
	@Test (description = "Verify the product help URL as per the value set in attribute ZimbraHelpAdminURL at the global config", priority=5,
			groups = { "functional", "L3" })

	public void ZimbraHelpAdvancedURL_02() throws HarnessException {

		String url = "https://www.bbc.com";
		String tempURL = null;
		boolean found = false;

		try {
			// Modify the config and change the help URL
			ZimbraAdminAccount.AdminConsoleAdmin()
					.soapSend("<ModifyConfigRequest xmlns='urn:zimbraAdmin'>"
					+ "<a n='zimbraHelpAdvancedURL'>" + url + "</a>"
					+ "</ModifyConfigRequest>");

			app.zPageMain.zRefreshMainUI();
			app.zPageMain.zToolbarPressPulldown(Button.B_ACCOUNT, Button.O_PRODUCT_HELP);

			// Zimbra advanced help page can open in separate window
			List<String> windowIds = app.zPageMain.sGetAllWindowIds();

			if (windowIds.size() > 1) {

				for (String id: windowIds) {
					app.zPageMain.sSelectWindow(id);

					if (app.zPageMain.sGetTitle().contains("BBC")) {
						//Get the opened URL
						tempURL = app.zPageMain.sGetLocation();
						found = true;
						app.zPageMain.zSeparateWindowClose(app.zPageMain.sGetTitle());
						break;

					} else if (!(app.zPageMain.sGetTitle().contains("Zimbra: Inbox"))) {
						app.zPageMain.zSeparateWindowClose(app.zPageMain.sGetTitle());
					}
				}
				if (!found) {
					tempURL = app.zPageMain.sGetLocation();
				}

			} else {
				tempURL = app.zPageMain.sGetLocation();
			}

			// Check the URL
			ZAssert.assertTrue(tempURL.contains("www.bbc.com"),	"Product Help URL is not as set in zimbraHelpAdvancedURL");

		} finally {
			// Revert the changes done in attribute 'zimbraHelpAdvancedURL'
			ZimbraAdminAccount.AdminConsoleAdmin()
					.soapSend("<ModifyConfigRequest xmlns='urn:zimbraAdmin'>"
					+ "<a n='zimbraHelpAdvancedURL'>" + "" + "</a>"
					+ "</ModifyConfigRequest>");
		}
	}
}