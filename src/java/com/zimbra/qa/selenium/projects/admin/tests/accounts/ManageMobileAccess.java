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

public class ManageMobileAccess extends AdminCommonTest {

	public ManageMobileAccess() {
		logger.info("New "+ ManageMobileAccess.class.getCanonicalName());
		super.startingPage = app.zPageManageAccounts;
	}


	/**
	 * Testcase : Edit account - Two Factor Authentication
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Edit the two factor authentication attributes using UI
	 * 3. Verify two factor authentication attributes are changed using SOAP.
	 * @throws HarnessException
	 */
	@Test( description = "Edit account - Two Factor Authentication",
			groups = { "smoke", "network" })

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

		// CLick on Features
		//form.ClickOnFeatures();
		app.zPageEditAccount.zToolbarPressButton(Button.B_MOBILE_ACCESS);
		
		SleepUtil.sleepMedium();

		Boolean isMobileAccessEnabled = app.zPageEditAccount.sIsChecked(FormEditAccount.Locators.ENABLE_MOBILE_SYNC);	
		ZAssert.assertFalse(isMobileAccessEnabled, "Verify right is deleted without any error!!");
		
		// Uncheck Mail
		form.zSetMobileAccess(Button.B_ENABLE_MOBILE_SYNC,true);

		// Uncheck Calendar
		form.zSetMobileAccess(Button.B_ENABLE_MOBILE_POLICY,true);

		// Submit the form
		form.zSubmit();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ account.getEmailAddress() +"</account>"
						+		"</GetAccountRequest>");

		// Verify calendar feature is disabled
		Element response1 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraFeatureMobileSyncEnabled']", 1);
		ZAssert.assertNotNull(response1, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response1.toString(),"TRUE", "Verify calendar feature is disabled");
		
		Boolean status = app.zPageEditAccount.sIsChecked(FormEditAccount.Locators.ENABLE_MOBILE_SYNC);	
		ZAssert.assertTrue(status, "Verify right is deleted without any error!!");

		// Verify mail feature is disabled
		Element response2 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraFeatureMobilePolicyEnabled']", 1);
		ZAssert.assertNotNull(response2, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response2.toString(),"TRUE", "Verify mail feature is disabled");

	}
	
	/**
	 * Testcase : Edit account - Two Factor Authentication
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Edit the two factor authentication attributes using UI
	 * 3. Verify two factor authentication attributes are changed using SOAP.
	 * @throws HarnessException
	 */
	@Test( description = "Edit account - Two Factor Authentication",
			groups = { "smoke", "network" })

	public void EnableMobileAccess_02() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(account);

		// Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be Edited.
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());

		// Click on Edit button
		FormEditAccount form = (FormEditAccount) app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// CLick on Features
		//form.ClickOnFeatures();
		app.zPageEditAccount.zToolbarPressButton(Button.B_MOBILE_ACCESS);
		
		SleepUtil.sleepMedium();

		// Uncheck Mail
		form.zSetMobileAccess(Button.B_ENABLE_MOBILE_SYNC,false);

		// Uncheck Calendar
		form.zSetMobileAccess(Button.B_ENABLE_MOBILE_POLICY,false);

		// Submit the form
		form.zSubmit();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ account.getEmailAddress() +"</account>"
						+		"</GetAccountRequest>");

		// Verify calendar feature is disabled
		Element response1 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraFeatureMobileSyncEnabled']", 1);
		ZAssert.assertNotNull(response1, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response1.toString(),"FALSE", "Verify calendar feature is disabled");

		// Verify mail feature is disabled
		Element response2 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraFeatureMobilePolicyEnabled']", 1);
		ZAssert.assertNotNull(response2, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response2.toString(),"FALSE", "Verify mail feature is disabled");

	}

}
