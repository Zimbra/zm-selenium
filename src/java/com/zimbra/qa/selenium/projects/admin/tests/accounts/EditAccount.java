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
import com.zimbra.qa.selenium.projects.admin.ui.PageManageAccounts;
import com.zimbra.qa.selenium.projects.admin.ui.PageSearchResults;

public class EditAccount extends AdminCommonTest {
	public EditAccount() {
		logger.info("New "+ EditAccount.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage = app.zPageManageAccounts;
	}

	/**
	 * Testcase : Edit account name  - Manage Account View
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Go to Manage Account View
	 * 3. Select an Account.
	 * 4. Edit an account using edit button in Gear box menu.
	 * 5. Verify account is edited using SOAP.
	 * @throws HarnessException
	 */
	@Test( description = "Edit Account name  - Manage Account View",
			groups = { "smoke" })
	public void EditAccount_01() throws HarnessException {

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

		//Edit the name.
		String editedName = "editedAccount_" + ConfigProperties.getUniqueString();
		form.setName(editedName);

		//Submit the form.
		form.zSubmit();

		app.zPageMain.zRefresh();
		// Verify the account exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ editedName+"@"+account.getDomainName() +"</account>"
						+		"</GetAccountRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account", 1);
		ZAssert.assertNotNull(response, "Verify the account is edited successfully");
		app.zPageMain.logout();
	}

	/**
	 * Testcase : Edit account name -- right click 
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Edit the account name using UI Right Click.
	 * 3. Verify account name is changed using SOAP.
	 * @throws HarnessException
	 */
	@Test( description = "Edit account name -- right click",
			groups = { "functional" })
	public void EditAccount_02() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+		"</CreateAccountRequest>");

		// Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Right Click on account to be Edited.
		app.zPageManageAccounts.zListItem(Action.A_RIGHTCLICK, account.getEmailAddress());

		// Click on Edit button
		FormEditAccount form = (FormEditAccount) app.zPageManageAccounts.zToolbarPressButton(Button.B_TREE_EDIT);

		//Edit the name.
		String editedName = "editedAccount_" + ConfigProperties.getUniqueString();
		form.setName(editedName);

		//Submit the form.
		form.zSubmit();
		app.zPageMain.zRefresh();

		// Verify the account exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ editedName+"@"+account.getDomainName() +"</account>"
						+		"</GetAccountRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account", 1);
		ZAssert.assertNotNull(response, "https://bugzilla.zimbra.com/show_bug.cgi?id=74487");
		app.zPageMain.logout();
	}

	/**
	 * Testcase : Edit delegated admin account name  - Manage Account View
	 * Steps :
	 * 1. Create an delegated admin account using SOAP.
	 * 2. Go to Manage Account View
	 * 3. Select an Account.
	 * 4. Edit an account using edit button in Gear box menu.
	 * 5. Verify account is edited using SOAP.
	 * @throws HarnessException
	 */
	@Test( description = "Edit delegated admin account name  - Manage Account View",
			groups = { "functional" })
	public void EditAccount_03() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("delegated_admin" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+			"<a xmlns='' n='zimbraIsDelegatedAdminAccount'>TRUE</a>"
						+		"</CreateAccountRequest>");

		// Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be Edited.
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());

		// Click on Edit button
		FormEditAccount form = (FormEditAccount) app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Edit the name.
		String editedName = "editedAccount_" + ConfigProperties.getUniqueString();
		form.setName(editedName);

		//Submit the form.
		form.zSubmit();
		app.zPageMain.zRefresh();

		// Verify the account exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ editedName+"@"+account.getDomainName() +"</account>"
						+		"</GetAccountRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account", 1);
		ZAssert.assertNotNull(response, "Verify the account is edited successfully");
		app.zPageMain.logout();
	}

	/**
	 * Testcase : Edit global admin account name  - Manage Account View
	 * Steps :
	 * 1. Create an global admin account using SOAP.
	 * 2. Go to Manage Account View
	 * 3. Select an Account.
	 * 4. Edit an account using edit button in Gear box menu.
	 * 5. Verify account is edited using SOAP.
	 * @throws HarnessException
	 */
	@Test( description = "Edit global admin Account name  - Manage Account View",
			groups = { "smoke" })
	public void EditAccount_04() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("global_admin" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+			"<a xmlns='' n='zimbraIsAdminAccount'>TRUE</a>"
						+		"</CreateAccountRequest>");

		// Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be Edited.
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());

		// Click on Edit button
		FormEditAccount form = (FormEditAccount) app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);


		//Edit the name.
		String editedName = "editedAccount_" + ConfigProperties.getUniqueString();
		form.setName(editedName);

		//Submit the form.
		form.zSubmit();
		app.zPageMain.zRefresh();

		// Verify the account exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ editedName+"@"+account.getDomainName() +"</account>"
						+		"</GetAccountRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account", 1);
		ZAssert.assertNotNull(response, "Verify the account is edited successfully");
		app.zPageMain.logout();
	}


	/**
	 * Testcase : Edit a basic account -- Search List View
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Search account.
	 * 3. Select an Account.
	 * 4. Edit an account using edit button in Gear box menu.
	 * 5. Verify account is edited using SOAP.
	 * 
	 * @throws HarnessException
	 */
	@Test( description = "Edit a basic account - Search List View",
			groups = { "functional" })
	public void EditAccount_05() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+		"</CreateAccountRequest>");



		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(account.getEmailAddress());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on account to be deleted.
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());


		// Click on Delete button
		app.zPageSearchResults.setType(PageSearchResults.TypeOfObject.ACCOUNT);
		FormEditAccount form = (FormEditAccount) app.zPageSearchResults.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Edit the name.
		String editedName = "editedAccount_" + ConfigProperties.getUniqueString();
		form.setName(editedName);

		//Submit the form.
		form.zSubmit();
		app.zPageMain.zRefresh();

		// Verify the account exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ editedName+"@"+account.getDomainName() +"</account>"
						+		"</GetAccountRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account", 1);
		ZAssert.assertNotNull(response, "https://bugzilla.zimbra.com/show_bug.cgi?id=74487");
		app.zPageMain.logout();

	}

	/**
	 * Testcase : Edit a basic account -- Search List View
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Search account.
	 * 3. Select an Account.
	 * 4. Edit an account using edit button in Gear box menu.
	 * 5. Verify account is edited using SOAP.
	 * 
	 * @throws HarnessException
	 */
	@Test( description = "Edit a basic account - Search List View",
			groups = { "functional" })
	public void EditAccount_06() throws HarnessException {


		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+		"</CreateAccountRequest>");



		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(account.getEmailAddress());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on account to be deleted.
		app.zPageSearchResults.zListItem(Action.A_RIGHTCLICK, account.getEmailAddress());


		// Click on Delete button
		app.zPageSearchResults.setType(PageSearchResults.TypeOfObject.ACCOUNT);
		FormEditAccount form = (FormEditAccount) app.zPageSearchResults.zToolbarPressButton(Button.B_TREE_EDIT);


		//Edit the name.
		String editedName = "editedAccount_" + ConfigProperties.getUniqueString();
		form.setName(editedName);

		//Submit the form.
		form.zSubmit();
		app.zPageMain.zRefresh();

		// Verify the account exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ editedName+"@"+account.getDomainName() +"</account>"
						+		"</GetAccountRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account", 1);
		ZAssert.assertNotNull(response, "https://bugzilla.zimbra.com/show_bug.cgi?id=74487");
		app.zPageMain.logout();

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
			groups = { "sanity", "network" })
	public void EditAccount_07() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+		"</CreateAccountRequest>");

		// Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");
		SleepUtil.sleepMedium();

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(account.getEmailAddress());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on account to be edited
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());

		// Click on Edit button
		app.zPageSearchResults.setType(PageSearchResults.TypeOfObject.ACCOUNT);
		FormEditAccount form = (FormEditAccount) app.zPageSearchResults.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);
		SleepUtil.sleepMedium();

		// Click on Advanced section
		form.sClick(PageManageAccounts.Locators.ADVANCED);
		SleepUtil.sleepMedium();

		// Check "Enable two-factor authentication"
		app.zPageEditCOS.zPreferenceCheckboxSet(Button.B_ENABLE_TWO_FACTOR_AUTH,true);

		// Check "Require two-step authentication"
		app.zPageEditCOS.zPreferenceCheckboxSet(Button.B_REQUIRED_TWO_FACTOR_AUTH,true);

		// Check "Enable application passcodes"
		app.zPageEditCOS.zPreferenceCheckboxSet(Button.B_ENABLE_APPLICATION_PASSCODES,true);

		// Enter "Number of one-time codes to generate"
		app.zPageEditCOS.zPreferenceTextSet(Button.B_TWO_FACTOR_AUTH_NUM_SCRATCH_CODES, "5");

		// Submit the form
		form.zSubmit();

		// Verify the enable two-factor authentication is set to true
		app.zPageMain.zRefresh();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ account.getEmailAddress() +"</account>"
						+		"</GetAccountRequest>");

		Element response1 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraFeatureTwoFactorAuthAvailable']", 1);
		ZAssert.assertNotNull(response1, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response1.toString(),"TRUE", "Verify the Enable two-factor authentication is set to true");

		// Verify the require two-step authentication is set to true
		Element response2 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraFeatureTwoFactorAuthRequired']", 1);
		ZAssert.assertNotNull(response2, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response2.toString(),"TRUE", " Verify the Require two-step authentication is set to true");

		// Verify the number of one-time codes to generate is set to 5
		Element response3 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraTwoFactorAuthNumScratchCodes']", 1); 
		ZAssert.assertNotNull(response3, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response3.toString(),"5", "Verify the Number of one-time codes to generate is set to 5");

		// Verify the enable application passcodes is set to true
		Element response4 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraFeatureAppSpecificPasswordsEnabled']", 1);
		ZAssert.assertNotNull(response4, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response4.toString(),"TRUE", "Verify the Enable application passcodes is set to true");

		app.zPageMain.logout();
	}

}
