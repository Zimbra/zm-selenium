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
package com.zimbra.qa.selenium.projects.admin.tests.accounts;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.ui.FormEditAccount;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.PageSearchResults;

public class ManageMobileAccess extends AdminCommonTest {

	public ManageMobileAccess() {
		logger.info("New "+ ManageMobileAccess.class.getCanonicalName());
		super.startingPage = app.zPageManageAccounts;
	}

	/**
	 * Testcase : Edit account - Enable mobile access
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Edit account > navigate to mobile access
	 * 3. Enable mobile access
	 * @throws HarnessException
	 */
	@Test( description = "Edit account - Enable mobile access",
			groups = { "smoke", "L1", "network" })

	public void EnableMobileAccess_01() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+		"</CreateAccountRequest>");

		// Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be Edited.
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());

		// Click on Edit button
		FormEditAccount form = (FormEditAccount) app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// CLick on mobile access
		app.zPageEditAccount.zToolbarPressButton(Button.B_MOBILE_ACCESS);
		SleepUtil.sleepMedium();

		// Enable mobile sync
		form.zSetMobileAccess(Button.B_ENABLE_MOBILE_SYNC,true);

		// Enable mobile policy
		form.zSetMobileAccess(Button.B_ENABLE_MOBILE_POLICY,true);

		// Submit the form
		form.zSubmit();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ account.getEmailAddress() +"</account>"
						+		"</GetAccountRequest>");

		// Verify mobile sync is enabled
		Element response1 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraFeatureMobileSyncEnabled']", 1);
		ZAssert.assertNotNull(response1, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response1.toString(),"TRUE", "Verify Enable mobile sync is checked");

		Boolean status = app.zPageEditAccount.sIsChecked(FormEditAccount.Locators.ENABLE_MOBILE_SYNC);	
		ZAssert.assertTrue(status, "Verify Enable mobile sync is checked!!");

		Element response2 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraFeatureMobilePolicyEnabled']", 1);
		ZAssert.assertNotNull(response2, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response2.toString(),"TRUE", "Verify Enable mobile policy is checked");

	}

	/**
	 * Testcase : Edit account - Disable mobile access
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Edit account > navigate to mobile access
	 * 3. Disable mobile access
	 * @throws HarnessException
	 */
	@Test( description = "Edit account - Disable mobile access",
			groups = { "smoke", "L1", "network" })

	public void DisableMobileAccess_02() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(account);

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(account.getEmailAddress());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on account to be edited.
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());


		// Click on edit button
		app.zPageSearchResults.setType(PageSearchResults.TypeOfObject.ACCOUNT);
		FormEditAccount form = (FormEditAccount) app.zPageSearchResults.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// Click on mobile access
		app.zPageEditAccount.zToolbarPressButton(Button.B_MOBILE_ACCESS);
		SleepUtil.sleepMedium();


		// Uncheck 'enable mobile sync'
		form.zSetMobileAccess(Button.B_ENABLE_MOBILE_SYNC,false);

		// Submit the form
		form.zSubmit();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ account.getEmailAddress() +"</account>"
						+		"</GetAccountRequest>");

		// Verify mobile sync feature is disabled
		Element response1 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraFeatureMobileSyncEnabled']", 1);
		ZAssert.assertNotNull(response1, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response1.toString(),"FALSE", "Verify calendar feature is disabled");

	}

}
