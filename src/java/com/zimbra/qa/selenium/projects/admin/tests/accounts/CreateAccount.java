/*
 * ***** BEGIN LICENSE BLOCK *****
 * 
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013 VMware, Inc.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * 
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.admin.tests.accounts;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.ui.WizardCreateAccount;
import com.zimbra.qa.selenium.projects.admin.ui.WizardCreateAdminAccount;


public class CreateAccount extends AdminCommonTest {

	public CreateAccount() {
		logger.info("New "+ CreateAccount.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage = app.zPageManageAccounts;

	}

	/**
	 * Testcase : Create a basic account
	 * Steps :
	 * 1. Create an account from GUI i.e. Gear Box -> New.
	 * 2. Verify account is created using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Create a basic account using New->Account",
			groups = { "sanity" })
			public void CreateAccount_01() throws HarnessException {

		// Create a new account in the Admin Console
		AccountItem account = new AccountItem("email" + ZimbraSeleniumProperties.getUniqueString(),ZimbraSeleniumProperties.getStringProperty("testdomain"));



		// Click "New" -> "Account"
		WizardCreateAccount wizard = 
			(WizardCreateAccount)app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW);

		// Fill out the wizard and click Finish
		wizard.zCompleteWizard(account);



		// Verify the account exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<account by='name'>"+ account.getEmailAddress() +"</account>"
				+		"</GetAccountRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account", 1); 
		ZAssert.assertNotNull(response, "Verify the account is created successfully");


	}
	
	

	/**
	 * Testcase : Create a global admin account.
	 * Steps :
	 * 1. Create an global admin account from GUI.
	 * 2. Verify account is created using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Create a global admin account",
			groups = { "sanity" })
			public void CreateAccount_02() throws HarnessException {

		// Create a new account in the Admin Console
		AccountItem account = new AccountItem("global_admin" + ZimbraSeleniumProperties.getUniqueString(),ZimbraSeleniumProperties.getStringProperty("testdomain"));

		// Click "New" -> "Admin Account"
		WizardCreateAdminAccount wizard = 
			(WizardCreateAdminAccount)app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW_ADMIN);

		// Fill out the wizard and click Finish
		wizard.setAdminType(WizardCreateAdminAccount.Locators.ADMIN_USER);
		wizard.setGlobalAdmin(true);
		wizard.zCompleteWizard(account);


		// Verify the account exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<account by='name'>"+ account.getEmailAddress() +"</account>"
				+		"</GetAccountRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account", 1); 
		ZAssert.assertNotNull(response, "Verify the admin account is created successfully");

	}

}
