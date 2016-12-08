/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.domains;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.DomainItem;
import com.zimbra.qa.selenium.projects.admin.ui.*;

public class SetAccountLimits extends AdminCommonTest {
	public SetAccountLimits() {
		logger.info("New "+ SetAccountLimits.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage = app.zPageManageDomains;
	}

	/*
	 * Testcase : Admin should be able modify domain limit
	 * Steps :
	 * 1. Login to admin console
	 * 2. Edit any domain
	 * 3. GO to account limits >> set Maximum accounts for this domain:  say 1000
	 * @throws HarnessException
	 */
	@Test( description = "Admin should be able modify domain limit",
			groups = { "functional", "L2" })
	public void SetAccountLimits_01() throws HarnessException {

		String domain_limit="1000";

		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();
		String domainName=domain.getName();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + domainName + "</name>"
						+		"</CreateDomainRequest>");

		// Refresh the domain list
		app.zPageManageDomains.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on domain to be edited
		app.zPageManageDomains.zListItem(Action.A_LEFTCLICK, domain.getName());

		// Edit domain 
		FormEditDomain form = (FormEditDomain) app.zPageManageDomains.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// Set Maximum accounts for the domain
		app.zPageManageDomains.zSetAccountLimitOnDomain(domain_limit);

		//Submit the form.
		form.zSubmit();

		// Verify domain limit is set 
		String check_domain_limit= app.zPageManageGlobalSettings.sGetValue(PageManageDomains.Locators.MAXIMUM_ACCOUNTS_FOR_DOMAIN);
		ZAssert.assertStringContains(check_domain_limit, domain_limit ,"Verify domain limit is set!");
	}

}
