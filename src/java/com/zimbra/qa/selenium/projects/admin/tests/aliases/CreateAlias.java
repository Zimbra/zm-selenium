/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.aliases;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.*;
import com.zimbra.qa.selenium.projects.admin.ui.WizardCreateAlias;


public class CreateAlias extends AdminCommonTest {
	
	public CreateAlias() {
		logger.info("New " + CreateAlias.class.getCanonicalName());
		
		// All tests start at the "Alias" page
		super.startingPage=app.zPageManageAliases;
	}
	
	/**
	 * Testcase : Create a basic alias.
	 * 1. Create a alias with GUI.
	 * 2. Verify alias is created using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Create a basic alias",
			groups = { "obsolete" })
	public void CreateAlias_01() throws HarnessException {

		AccountItem target = new AccountItem("email" + ZimbraSeleniumProperties.getUniqueString(),ZimbraSeleniumProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(target);
		
		AliasItem alias = new AliasItem();		// Create a new account in the Admin Console using SOAP
		alias.setTargetAccountEmail(target.getEmailAddress());


		// Click "New"
		WizardCreateAlias wizard = 
			(WizardCreateAlias)app.zPageManageAliases.zToolbarPressButton(Button.B_NEW);
		
		// Fill out the wizard	
		wizard.zCompleteWizard(alias);

		// Verify the alias exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<account by='name'>"+ alias.getEmailAddress() +"</account>"
				+		"</GetAccountRequest>");
		String email = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:account", "name");
		ZAssert.assertEquals(email, target.getEmailAddress(), "Verify the alias is associated with the correct account");
	}
	
	/**
	 * Testcase : Create a basic alias
	 * Steps :
	 * 1. Create an alias from GUI i.e. New -> Alias.
	 * 2. Verify account is created using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Create a basic account using New->Account",
			groups = { "obsolete" })
			public void CreateAlias_02() throws HarnessException {

		AccountItem target = new AccountItem("email" + ZimbraSeleniumProperties.getUniqueString(),ZimbraSeleniumProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(target);
		

		// Create a new account in the Admin Console
		AliasItem alias = new AliasItem();		// Create a new account in the Admin Console using SOAP
		alias.setTargetAccountEmail(target.getEmailAddress());


	
		// Click "New"
		WizardCreateAlias wizard = 
			(WizardCreateAlias)app.zPageManageAliases.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.O_NEW);
		
		// Fill out the wizard	
		
		wizard.zCompleteWizard(alias);

		// Verify the alias exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<account by='name'>"+ alias.getTargetAccountEmail() +"</account>"
				+		"</GetAccountRequest>");
		String email = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:account", "name");
		ZAssert.assertEquals(email, target.getEmailAddress(), "Verify the alias is associated with the correct account");
	}

}
