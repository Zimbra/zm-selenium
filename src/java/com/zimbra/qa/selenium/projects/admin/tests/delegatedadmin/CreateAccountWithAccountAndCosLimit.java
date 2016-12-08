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
package com.zimbra.qa.selenium.projects.admin.tests.delegatedadmin;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.items.CosItem;
import com.zimbra.qa.selenium.projects.admin.items.DomainItem;
import com.zimbra.qa.selenium.projects.admin.ui.*;
import com.zimbra.qa.selenium.framework.core.Bugs;

public class CreateAccountWithAccountAndCosLimit extends AdminCommonTest {
	public CreateAccountWithAccountAndCosLimit() {
		logger.info("New "+ CreateAccountWithAccountAndCosLimit.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage = app.zPageManageDomains;
	}

	/**
	 * Testcase : Bug 58379 global admins not able to create new accounts when account limit per COS is set
	 * Steps :
	 * 1. Login to admin console
	 * 2. Edit any domain
	 * 3. GO to account limits >>  "set Maximum accounts per COS:" default say 1000
	 * Re-login/refresh admin console >> click on add account 
	 * @throws HarnessException
	 */

	@Bugs( ids = "58379")
	@Test( description = "Delegated admin should be able to create a new account when account limit is set",
	groups = { "smoke", "L1" })
	public void CreateAccountWithAccountAndCosLimit_01() throws HarnessException {

		String cos_name="default";
		String cos_limit="1000";

		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();
		String domainName=domain.getName();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + domainName + "</name>"
						+		"</CreateDomainRequest>");

		// Create admin account
		String adminaccount = "admin"+ ConfigProperties.getUniqueString() +"@"+domainName;
		ZimbraAdminAccount account = new ZimbraAdminAccount(adminaccount);
		account.provision();


		// Refresh the domain list
		app.zPageManageDomains.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be edited
		app.zPageManageDomains.zListItem(Action.A_LEFTCLICK, domain.getName());

		FormEditDomain form = (FormEditDomain) app.zPageManageDomains.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Edit the account limit
		app.zPageManageDomains.zSetAccountLimitPerCos(cos_name,cos_limit);

		//Submit the form.
		form.zSubmit();

		// logout
		app.zPageMain.logout();

		// Login as admin 
		app.zPageLogin.login(account);
		app.zPageManageAccounts.zNavigateTo();

		AccountItem account1 = new AccountItem("email" + ConfigProperties.getUniqueString(),domainName);

		// Click "New" -> "Account" at Delelgated Admin manage account page
		WizardCreateAccount wizard = 
				(WizardCreateAccount)app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW);

		// Fill out the wizard and click Finish
		wizard.zCompleteWizard(account1);

		// Verify the account exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ account1.getEmailAddress() +"</account>"
						+		"</GetAccountRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account", 1); 
		ZAssert.assertNotNull(response, "Verify the account is created successfully at DA");

	}

	/**
	 * Testcase : Bug 103122 Delegated Admins not able to create new accounts when account limit per COS is set
	 * Steps :
	 * 1. Login to admin console
	 * 2. Edit any domain
	 * 3. GO to account limits >>  "set Maximum accounts per COS:" default cos say 1000
	 * 4. Login as delegated admin >> click on add account 
	 * @throws HarnessException
	 */

	@Bugs( ids = "103122")
	@Test( description = "Delegated admin should be able to create new accounts when account limit per COS is set",
	groups = { "smoke", "L1" })
	public void CreateAccountWithAccountAndCosLimit_02() throws HarnessException {

		String cos_name="default";
		String cos_limit="10";

		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();
		String domainName=domain.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + domainName + "</name>"
						+		"</CreateDomainRequest>");


		// Refresh the domain list
		app.zPageManageDomains.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be edited
		app.zPageManageDomains.zListItem(Action.A_LEFTCLICK, domain.getName());

		FormEditDomain form = (FormEditDomain) app.zPageManageDomains.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Edit the account limit
		app.zPageManageDomains.zSetAccountLimitPerCos(cos_name,cos_limit);

		//Submit the form.
		form.zSubmit();

		// Create a new AdminAccount
		ZimbraAdminAccount accounta = new ZimbraAdminAccount("admin"+ ConfigProperties.getUniqueString() + "@" + domainName);

		accounta.provisionDA(accounta.EmailAddress);

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ accounta.EmailAddress +"</account>"
						+		"</GetAccountRequest>");
		String ZimbraId= ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:GetAccountResponse/admin:account", "id").toString();

		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
						+   " <id>"+ ZimbraId +"</id>"
						+   "<a n='zimbraAdminConsoleUIComponents'>accountListView</a>"
						+    "<a n='zimbraAdminConsoleUIComponents'>downloadsView</a>"
						+    "<a n='zimbraAdminConsoleUIComponents'>DLListView</a>"
						+    "<a n='zimbraAdminConsoleUIComponents'>aliasListView</a>"
						+    "<a n='zimbraAdminConsoleUIComponents'>resourceListView</a>"
						+    "<a n='zimbraAdminConsoleUIComponents'>saveSearch</a>"
						+    "<a n='zimbraAdminConsoleUIComponents'>domainListView</a>"
						+  "</ModifyAccountRequest>");

		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<GrantRightRequest xmlns='urn:zimbraAdmin'>"
						+		"<target by='name' type='global'>globalacltarget</target>"
						+		"<grantee by='name' type='usr'>"+ accounta.EmailAddress +"</grantee>"
						+		"<right>domainAdminCosRights</right>"
						+	"</GrantRightRequest>");

		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<GrantRightRequest xmlns='urn:zimbraAdmin'>"
						+		"<target by='name' type='domain'>" + domainName + "</target>"
						+		"<grantee by='name' type='usr'>"+ accounta.EmailAddress +"</grantee>"
						+		"<right>domainAdminRights</right>"
						+	"</GrantRightRequest>");


		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<FlushCacheRequest  xmlns='urn:zimbraAdmin'>" +
						"<cache type='galgroup'/>" +
				"</FlushCacheRequest>");

		app.zPageMain.logout();		
		accounta.authenticate();

		// Login as a delagated admin
		app.zPageLogin.login(accounta);
		app.zPageManageAccounts.zNavigateTo();

		AccountItem account1 = new AccountItem("email"+ ConfigProperties.getUniqueString() + "@" + domainName,"lastname");

		// Click "New" -> "Account" at Delelgated Admin manage account page
		WizardCreateAccount wizard = 
				(WizardCreateAccount)app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW);

		// Fill out the wizard and click Finish
		wizard.zCompleteWizard(account1);

		// Verify the account exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ account1.getEmailAddress() +"</account>"
						+		"</GetAccountRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account", 1); 
		ZAssert.assertNotNull(response, "Verify the account is created successfully at DA");

	}

	/**
	 * Testcase : Bug 103122 Delegated Admins not able to create new accounts when account limit per COS and domain is set
	 * Steps :
	 * 1. Login to admin console
	 * 2. Edit any domain
	 * 3. GO to account limits >>  "set Maximum accounts per COS:" default and non-default" 
	 * 4. Login as delegated admin >> click on add account 
	 * @throws HarnessException
	 */
	@Test( description = " Delegated Admins should be able to create new accounts when account limit per COS and domain is set",
			groups = { "functional", "L2" })
	public void CreateAccountWithAccountAndCosLimit_03() throws HarnessException {

		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();
		String domainName=domain.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + domainName + "</name>"
						+		"</CreateDomainRequest>");
		// Create COS
		CosItem cos = new CosItem();
		String cosName=cos.getName();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + cosName + "</name>"
						+		"</CreateCosRequest>");

		// Refresh the domain list
		app.zPageManageDomains.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be edited
		app.zPageManageDomains.zListItem(Action.A_LEFTCLICK, domain.getName());

		FormEditDomain form = (FormEditDomain) app.zPageManageDomains.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// Edit the account limit for default and non default cos			
		app.zPageManageDomains.zToolbarPressButton(Button.B_ACCOUNTS_LIMIT_PER_DOMAIN,"","10");
		app.zPageManageDomains.zToolbarPressButton(Button.B_ACCOUNTS_LIMIT_PER_COS,"default","10");
		app.zPageManageDomains.zToolbarPressButton(Button.B_ACCOUNTS_LIMIT_PER_COS,cosName,"10");

		//Submit the form.
		form.zSubmit();

		// Create a new AdminAccount
		ZimbraAdminAccount accounta = new ZimbraAdminAccount("admin"+ ConfigProperties.getUniqueString() + "@" + domainName);

		accounta.provisionDA(accounta.EmailAddress);

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ accounta.EmailAddress +"</account>"
						+		"</GetAccountRequest>");
		String ZimbraId= ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:GetAccountResponse/admin:account", "id").toString();

		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
						+   " <id>"+ ZimbraId +"</id>"
						+   "<a n='zimbraAdminConsoleUIComponents'>accountListView</a>"
						+    "<a n='zimbraAdminConsoleUIComponents'>downloadsView</a>"
						+    "<a n='zimbraAdminConsoleUIComponents'>DLListView</a>"
						+    "<a n='zimbraAdminConsoleUIComponents'>aliasListView</a>"
						+    "<a n='zimbraAdminConsoleUIComponents'>resourceListView</a>"
						+    "<a n='zimbraAdminConsoleUIComponents'>saveSearch</a>"
						+    "<a n='zimbraAdminConsoleUIComponents'>domainListView</a>"
						+  "</ModifyAccountRequest>");

		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<GrantRightRequest xmlns='urn:zimbraAdmin'>"
						+		"<target by='name' type='global'>globalacltarget</target>"
						+		"<grantee by='name' type='usr'>"+ accounta.EmailAddress +"</grantee>"
						+		"<right>domainAdminCosRights</right>"
						+	"</GrantRightRequest>");

		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<GrantRightRequest xmlns='urn:zimbraAdmin'>"
						+		"<target by='name' type='domain'>" + domainName + "</target>"
						+		"<grantee by='name' type='usr'>"+ accounta.EmailAddress +"</grantee>"
						+		"<right>domainAdminRights</right>"
						+	"</GrantRightRequest>");


		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<FlushCacheRequest  xmlns='urn:zimbraAdmin'>" +
						"<cache type='galgroup'/>" +
				"</FlushCacheRequest>");

		app.zPageMain.logout();		
		accounta.authenticate();

		// Login as delegated admin
		app.zPageLogin.login(accounta);

		app.zPageManageAccounts.zNavigateTo();

		AccountItem account1 = new AccountItem("email"+ ConfigProperties.getUniqueString() + "@" + domainName,"lastname");
		
		// Click "New" -> "Account" at Delelgated Admin manage account page
		WizardCreateAccount wizard = 
				(WizardCreateAccount)app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW);

		// Fill out the wizard and click Finish
		wizard.zCompleteWizard(account1);

		// Verify the account exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ account1.getEmailAddress() +"</account>"
						+		"</GetAccountRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account", 1); 
		ZAssert.assertNotNull(response, "Verify the account is created successfully at DA");

	}

}
