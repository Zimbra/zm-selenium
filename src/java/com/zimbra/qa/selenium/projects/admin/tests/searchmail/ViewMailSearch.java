/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.searchmail;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageSearchMail;

public class ViewMailSearch extends AdminCommonTest {

	public ViewMailSearch() {
		logger.info("New "+ ViewMailSearch.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage = app.zPageManageSearchMail;
	}

	/**
	 * Testcase : View mail search
	 * Steps :
	 * 1. Go to Home > Tools and Migraton > Search Mail
	 * 2. click on view option from gear icon
	 * 3. Verify view mail search operation
	 * @throws HarnessException
	 */

	@Test( description = "View mail search",
			groups = { "functional","network" })
	public void ViewMailSearch_01() throws HarnessException {

		String hostname = ConfigProperties.getStringProperty("server.host");

		// Send a message to the account
		String subject = "crossmailboxsearch"+ ConfigProperties.getUniqueString();
		String searchArchives = "1";

		String emailAddress =ZimbraAccount.AccountA().EmailAddress;
		// Get server ID
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetServerRequest xmlns='urn:zimbraAdmin'>"
						+	"<server by='name'>" + hostname + "</server>"
						+		"</GetServerRequest>");
		String id = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:server", "id");

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateXMbxSearchRequest xmlns='urn:zimbraAdmin'>"
						+ "<a n=\"query\">" + subject + "</a>"
						+ "<a n=\"searchArchives\">" + searchArchives + "</a>"
						+ "<a n=\"searchLive\">" + "1" + "</a>"
						+ "<a n=\"inDumpster\">" + "0" + "</a>"
						+ "<a n=\"targetFolder\">" + "xmbx search results" + "</a>"
						+ "<a n=\"accounts\">" + "*" + "</a>"
						+ "<a n=\"limit\">" + "0" + "</a>"
						+ "<a n=\"permailbox\">" + "1000" + "</a>"
						+ "<a n=\"serverId\">" + id + "</a>"
						+ "<a n=\"targetMbx\">" + emailAddress + "</a>"
						+ "</CreateXMbxSearchRequest>");

		app.zPageManageSearchMail.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");
		
		// Navigate to Home > Tools and Migraton > Search Mail
		app.zPageManageSearchMail.zClickAt(PageManageSearchMail.Locators.TOOLS_AND_MIGRATION_ICON, "");
		SleepUtil.sleepMedium();

		// Click on search mail
		app.zPageManageSearchMail.sClickAt(PageManageSearchMail.Locators.SEARCHMAIL, "");
		
		app.zPageManageSearchMail.zListItem(Action.A_LEFTCLICK, emailAddress);

		// Click on New
		app.zPageManageSearchMail.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_VIEW);
		
		// Verify search is created successfully
		ZAssert.assertTrue(app.zPageManageSearchMail.zVerifyViewSearchQuery(), "Verfiy view search result operation");

	}
}
