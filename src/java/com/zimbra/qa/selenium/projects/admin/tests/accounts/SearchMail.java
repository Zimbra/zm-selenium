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
package com.zimbra.qa.selenium.projects.admin.tests.accounts;

import java.util.List;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;

public class SearchMail extends AdminCommonTest {
	public SearchMail() {
		logger.info("New "+ SearchMail.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage = app.zPageManageAccounts;
	}

	/**
	 * Testcase : Search mail  -- manage account >> right click >> Search mail
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Edit the account name using UI Right Click.
	 * 3. Verify Search mail functionality
	 * @throws HarnessException
	 */
	@Test( description = "Search mail  -- manage account > right click > Search mail",
			groups = { "smoke", "L1" })
	public void SearchMail_01() throws HarnessException {

		// Create a new account in the admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+		"</CreateAccountRequest>");

		// Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Right click on account 
		app.zPageManageAccounts.zListItem(Action.A_RIGHTCLICK, account.getEmailAddress());

		// Right click account >>  Search mail
		app.zPageManageAccounts.zToolbarPressButton(Button.B_SEARCH_MAIL);

		// Verify Search Mail page is opened up
		boolean isPageDisplayed = app.zPageManageSearchMail.zVerifySearchPage();
		ZAssert.assertTrue(isPageDisplayed, "Verify Search Mail page is opened up!!");

	}

	/**
	 * Testcase : Search mail  -- manage account >> Gearbox >> Search mail
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Manage account >> Gearbox >> edit account >>  Search mail
	 * 3. Verify account mailbox is opened up
	 * @throws HarnessException
	 */
	@Test( description = " Search mail  -- manage account >> Gearbox >> edit account >>  Search mail",
			groups = { "smoke", "L1" })
	public void SearchMail_02() throws HarnessException {

		// Create a new account in the admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+		"</CreateAccountRequest>");

		// Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());

		// Gearbox >> Search mail
		app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_SEARCH_MAIL);

		// Verify Search Mail page is opened up
		boolean isPageDisplayed = app.zPageManageSearchMail.zVerifySearchPage();
		ZAssert.assertTrue(isPageDisplayed, "Verify Search Mail page is opened up!!");	
	}

	/**
	 * Testcase : Search mail in Search List view
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Search account. 
	 * 3. Select an Account.
	 * 4. Search mail
	 * 5. Verify search page is opened up
	 * 
	 * @throws HarnessException
	 */
	@Test( description = "Edit a basic account - Search List Search",
			groups = { "functional", "L2" })
	public void SearchMail_03() throws HarnessException {

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

		// Click on account 
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());

		// Gearbox >> Search mail
		app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_SEARCH_MAIL);

		// Verify Search Mail page is opened up
		boolean isPageDisplayed = app.zPageManageSearchMail.zVerifySearchPage();
		ZAssert.assertTrue(isPageDisplayed, "Verify Search Mail page is opened up!!");			
	}	

	/**
	 * Testcase : Search mail in Search List view
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Search account. 
	 * 3. Select an Account.
	 * 4. Search mail
	 * 5. Verify search mail page is opened up
	 * 
	 * @throws HarnessException
	 */
	@Test( description = "Edit a basic account - Search List",
			groups = { "functional", "L3" })
	public void SearchMail_04() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+			"<a xmlns='' n='zimbraAccountStatus'>lockout</a>"
						+		"</CreateAccountRequest>");

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(account.getEmailAddress());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on account 
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());

		// Gearbox >> Search mail
		app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_SEARCH_MAIL);

		// Verify Search Mail page is opened up
		boolean isPageDisplayed = app.zPageManageSearchMail.zVerifySearchPage();
		ZAssert.assertTrue(isPageDisplayed, "Verify Search Mail page is opened up!!");		
	}

	@AfterMethod( groups = { "always" } )
	public void afterMethod() throws HarnessException {
		//Mail opens in new window. Close all the windows except Zimbra administration page
		List<String> windowIds=app.zPageMain.sGetAllWindowIds();

		if (windowIds.size() > 1) {

			for(String id: windowIds) {

				app.zPageMain.sSelectWindow(id);
				if (!(app.zPageMain.sGetTitle().contains("Zimbra Administration"))) {
					logger.info("Closing window" +app.zPageMain.sGetTitle());
					app.zPageMain.zSeparateWindowClose(app.zPageMain.sGetTitle());					
				}
			}
		}
	}

}
