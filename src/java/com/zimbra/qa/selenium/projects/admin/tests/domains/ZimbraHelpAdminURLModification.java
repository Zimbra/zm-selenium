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
package com.zimbra.qa.selenium.projects.admin.tests.domains;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;

public class ZimbraHelpAdminURLModification extends AdminCommonTest {

	public ZimbraHelpAdminURLModification() {
		logger.info("New "+ ZimbraHelpAdminURLModification.class.getCanonicalName());
	}

	@Bugs(ids="101023")
	@Test( description = "Verify that zimbra admin help page is opened as per the value set in attribute ZimbraHelpAdminURL",
			groups = { "functional", "L2" })

	public void ZimbraHelpAdminURLModification_01() throws HarnessException {

		staf.execute("mkdir -p /opt/zimbra/jetty/webapps/zimbraAdmin/helpUrl/help/admin && echo '<html><head><title>Zimbra Temp Admin Help</title></head><body><h1>Temp Admin Help</h1><p> This is the new admin help of zimbra!</p></body></html>' > /opt/zimbra/jetty/webapps/zimbraAdmin/helpUrl/help/admin/adminhelp.html");

		// To get domain id
		String targetDomain = ConfigProperties.getStringProperty("server.host");
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
						+	"<domain by='name'>" + targetDomain + "</domain>"
						+	"</GetDomainRequest>");

		String domainID=ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:GetDomainResponse/admin:domain", "id").toString();

		// Modify the domain and change the help URL
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<ModifyDomainRequest xmlns='urn:zimbraAdmin'>"
						+ "<id>" + domainID +"</id>"
						+  "<a n='zimbraHelpAdminURL'>/zimbraAdmin/helpUrl/help/admin/adminhelp.html</a>"
						+	"</ModifyDomainRequest>");

		String tempURL = null;
		boolean found = false;

		// Click on the Help drop down arrow
		app.zPageMain.sMouseMoveAt(PageMain.Locators.zSkinContainerHelpDropDownArrow,"0,0");
		app.zPageMain.sClickAt(PageMain.Locators.zSkinContainerHelpDropDownArrow,"0,0");
		SleepUtil.sleepSmall();

		// Select Help Center option
		app.zPageMain.sMouseMoveAt(PageMain.Locators.zHelpCenterOption,"0,0");
		app.zPageMain.sClickAt(PageMain.Locators.zHelpCenterOption,"0,0");
		SleepUtil.sleepSmall();

		// Zimbra admin help page opens in separate window
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
				} else if (!(app.zPageMain.sGetTitle().contains("Zimbra Administration"))) {
							app.zPageMain.zSeparateWindowClose(app.zPageMain.sGetTitle());					
				}
			}
			if (!found) {

				tempURL=app.zPageMain.sGetLocation();
			}

		} else {
			tempURL=app.zPageMain.sGetLocation();
		}

		// Revert the changes done in attribute 'zimbraHelpAdminURL'
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<ModifyDomainRequest xmlns='urn:zimbraAdmin'>"
						+ "<id>" + domainID +"</id>"
						+  "<a n='zimbraHelpAdminURL'>" + "" + "</a>"
						+	"</ModifyDomainRequest>");
		// Check the URL
		ZAssert.assertTrue(tempURL.contains("/zimbraAdmin/helpUrl/help/admin/adminhelp.html"),"Admin Help URL is not as set in zimbraHelpAdminURL");

	}

}