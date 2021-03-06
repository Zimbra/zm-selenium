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
import com.zimbra.qa.selenium.framework.util.CommandLineUtility;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;

public class HelpAdvancedURL extends AjaxCore {

	public HelpAdvancedURL() {
		logger.info("New " + HelpAdvancedURL.class.getCanonicalName());
	}


	@Bugs (ids = "101023")
	@Test (description = "Verify the product help URL", priority=5,
			groups = { "functional" })

	public void HelpAdvancedURL_01() throws HarnessException {

		String helpURLTitle = "Zimbra Temp Help";
		String url = "/zimbra/help/advanced/zimbra_user_help.htm";
		String domainID = null;
		String tempURL = null;
		boolean found = false;

		try {

			CommandLineUtility.runCommandOnZimbraServer(ZimbraAccount.AccountZCS().zGetAccountStoreHost(),
					"mkdir -p /opt/zimbra/jetty/webapps/zimbra/helpUrl/help/adv");

			CommandLineUtility.runCommandOnZimbraServer(ZimbraAccount.AccountZCS().zGetAccountStoreHost(),
					"sh -c 'echo \"<html><head><title>" + helpURLTitle + "</title></head><body><h1>Temp Help</h1><p> This is the new advanced help of zimbra!</p></body></html>\" > /opt/zimbra/jetty/webapps/zimbra/helpUrl/help/adv/advhelp.html'");

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
						tempURL = app.zPageMain.sGetLocation();
						found = true;
						app.zPageMain.zSeparateWindowClose(helpURLTitle);
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
			ZAssert.assertTrue(tempURL.contains("/helpUrl/help/adv/advhelp.html"), "Product Help URL is not as set in zimbraHelpAdvancedURL");

		} finally {

			// Revert the changes done in attribute 'zimbraHelpAdvancedURL'
			ZimbraAdminAccount.AdminConsoleAdmin()
					.soapSend("<ModifyDomainRequest xmlns='urn:zimbraAdmin'>" + "<id>" + domainID + "</id>"
							+ "<a n='zimbraHelpAdvancedURL'>" + url + "</a>" + "<a n='zimbraVirtualHostname'>" + ""
							+ "</a>" + "</ModifyDomainRequest>");
		}
	}


	@Test (description = "Verify the product help URL as per the value set in attribute ZimbraHelpAdminURL at the global config", priority=5,
			groups = { "functional-known-failure" })

	public void HelpAdvancedURL_02() throws HarnessException {

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