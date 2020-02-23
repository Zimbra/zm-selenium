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
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;

public class ViewMail extends AdminCore {

	public ViewMail() {
		logger.info("New "+ ViewMail.class.getCanonicalName());
		super.startingPage = app.zPageManageAccounts;
	}


	/**
	 * Testcase : View mail  -- manage account >> Gearbox >> View mail
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Manage account >> Gearbox >> edit account >>  View mail
	 * 3. Verify account mailbox is opened up
	 * @throws HarnessException
	 */

	@Test (description = " View mail  -- manage account >> Gearbox >> edit account >>  View mail",
			groups = { "smoke" })

	public void ViewMail_01() throws HarnessException {

		// Create a new account in the admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+			"<a n='description'>Created by Selenium automation</a>"
						+		"</CreateAccountRequest>");

		// Send a mail to the account
		String subject = "subject"+ ConfigProperties.getUniqueString();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ account.getEmailAddress() +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		// Refresh the account list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on account
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());

		// Gearbox >> View mail
		app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_VIEW_MAIL);

		// Wait for page to load
		SleepUtil.sleepLong();

		// Mail page opens in separate window
		List<String> windowIds = app.zPageMain.sGetAllWindowIds();
		ZAssert.assertTrue(windowIds.size() > 1, "User's mailbox is not opened in the separate window");

		boolean mailFound = false;
		for(String id: windowIds) {
			app.zPageMain.sSelectWindow(id);

			String pageTitle = "Zimbra: Inbox";
			if (ConfigProperties.isZimbra9XEnvironment()) {
				pageTitle = "Zimbra";
			}

			if (app.zPageMain.sGetTitle().contains(pageTitle) && !(app.zPageMain.sGetTitle().contains("Zimbra Administration"))) {
				mailFound = app.zPageMain.zVerifyMailExists(subject);
				app.zPageMain.sClose();
				app.zPageMain.sSelectWindow("null");
				break;
			} else if (!(app.zPageMain.sGetTitle().contains("Zimbra Administration"))) {
				app.zPageMain.zSeparateWindowClose(app.zPageMain.sGetTitle());
			}
		}

		// Verify that the mail is present
		ZAssert.assertTrue(mailFound, "Verify that user's mail is displayed to admin");
	}


	/**
	 * Testcase : View mail  -- manage account >> right click >> View mail
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Edit the account name using UI Right Click.
	 * 3. Verify view mail functionality
	 * @throws HarnessException
	 */

	@Bugs (ids = "69155")
	@Test (description = "View mail  -- manage account > right click > view mail",
			groups = { "smoke" })

	public void ViewMail_02() throws HarnessException {

		// Create a new account in the admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+			"<a n='description'>Created by Selenium automation</a>"
						+		"</CreateAccountRequest>");

		// Send a mail to the account
		String subject = "subject"+ ConfigProperties.getUniqueString();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ account.getEmailAddress() +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		// Refresh the account list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Right click on account
		app.zPageManageAccounts.zListItem(Action.A_RIGHTCLICK, account.getEmailAddress());

		// Right click account >>  view mail
		app.zPageManageAccounts.zToolbarPressButton(Button.B_VIEW_MAIL);

		// Wait for page to load
		SleepUtil.sleepLong();

		// Mail page opens in separate window
		List<String> windowIds = app.zPageMain.sGetAllWindowIds();
		ZAssert.assertTrue(windowIds.size() > 1, "User's mailbox is not opened in the separate window");

		boolean mailFound = false;
		for(String id: windowIds) {
			app.zPageMain.sSelectWindow(id);

			String pageTitle = "Zimbra: Inbox";
			if (ConfigProperties.isZimbra9XEnvironment()) {
				pageTitle = "Zimbra";
			}

			if (app.zPageMain.sGetTitle().contains(pageTitle) && !(app.zPageMain.sGetTitle().contains("Zimbra Administration"))) {
				mailFound = app.zPageMain.zVerifyMailExists(subject);
				app.zPageMain.sClose();
				app.zPageMain.sSelectWindow("null");
				break;
			} else if (!(app.zPageMain.sGetTitle().contains("Zimbra Administration"))) {
				app.zPageMain.zSeparateWindowClose(app.zPageMain.sGetTitle());
			}
		}

		// Verify that the mail is present
		ZAssert.assertTrue(mailFound, "Verify that user's mail is displayed to admin");
	}


	/**
	 * Testcase : Edit a basic account -- Search List View
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Search account.
	 * 3. Select an Account.
	 * 4. View mail
	 * 5. Verify account mailbox is opened up
	 *
	 * @throws HarnessException
	 */

	@Test (description = "Edit a basic account - Search List View",
			groups = { "bhr" })

	public void ViewMail_03() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+			"<a n='zimbraAccountStatus'>lockout</a>"
						+			"<a n='description'>Created by Selenium automation</a>"
						+		"</CreateAccountRequest>");

		// Send a mail to the account
		String subject = "subject"+ ConfigProperties.getUniqueString();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ account.getEmailAddress() +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(account.getEmailAddress());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on account
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());

		// Gearbox >> View mail
		app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_VIEW_MAIL);

		// Wait for page to load
		SleepUtil.sleepLong();

		// Mail page opens in separate window
		List<String> windowIds = app.zPageMain.sGetAllWindowIds();
		ZAssert.assertTrue(windowIds.size() > 1, "User's mailbox is not opened in the separate window");

		boolean mailFound = false;
		for(String id: windowIds) {
			app.zPageMain.sSelectWindow(id);

			String pageTitle = "Zimbra: Inbox";
			if (ConfigProperties.isZimbra9XEnvironment()) {
				pageTitle = "Zimbra";
			}

			if (app.zPageMain.sGetTitle().contains(pageTitle) && !(app.zPageMain.sGetTitle().contains("Zimbra Administration"))) {
				mailFound = app.zPageMain.zVerifyMailExists(subject);
				app.zPageMain.sClose();
				app.zPageMain.sSelectWindow("null");
				break;
			} else if (!(app.zPageMain.sGetTitle().contains("Zimbra Administration"))) {
				app.zPageMain.zSeparateWindowClose(app.zPageMain.sGetTitle());
			}
		}

		// Verify that the mail is present
		ZAssert.assertTrue(mailFound, "Verify that user's mail is displayed to admin");
	}


	/**
	 * Testcase : Edit a basic account -- Search List View
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Search account.
	 * 3. Select an Account.
	 * 4. View mail
	 * 5. Verify account mailbox is opened up
	 *
	 * @throws HarnessException
	 */
	@Test (description = "Edit a basic account - Search List View",
			groups = { "bhr" })

	public void ViewMail_04() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+			"<a n='description'>Created by Selenium automation</a>"
						+		"</CreateAccountRequest>");

		// Send a mail to the account
		String subject = "subject"+ ConfigProperties.getUniqueString();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ account.getEmailAddress() +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		// Refresh the account list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on view mail
		app.zPageManageAccounts.zListItem(Action.A_RIGHTCLICK, Button.O_VIEW_MAIL, account.getEmailAddress());

		// Wait for page to load
		SleepUtil.sleepLong();

		// Mail page opens in separate window
		List<String> windowIds = app.zPageMain.sGetAllWindowIds();
		ZAssert.assertTrue(windowIds.size() > 1, "User's mailbox is not opened in the separate window");

		boolean mailFound = false;
		for(String id: windowIds) {
			app.zPageMain.sSelectWindow(id);

			String pageTitle = "Zimbra: Inbox";
			if (ConfigProperties.isZimbra9XEnvironment()) {
				pageTitle = "Zimbra";
			}

			if (app.zPageMain.sGetTitle().contains(pageTitle) && !(app.zPageMain.sGetTitle().contains("Zimbra Administration"))) {
				mailFound = app.zPageMain.zVerifyMailExists(subject);
				app.zPageMain.sClose();
				app.zPageMain.sSelectWindow("null");
				break;
			} else if (!(app.zPageMain.sGetTitle().contains("Zimbra Administration"))) {
				app.zPageMain.zSeparateWindowClose(app.zPageMain.sGetTitle());
			}
		}

		// Verify that the mail is present
		ZAssert.assertTrue(mailFound, "Verify that user's mail is displayed to admin");
	}


	@Test (description = "Verify that admin is able to view mails of a locked account",
			groups = { "sanity" })

	public void ViewMail_05() throws HarnessException {

		//Create a Zimbra account
		ZimbraAccount locked = new ZimbraAccount();
		locked.provision();

		// Lock the account
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
						+		"<id>" + locked.ZimbraId + "</id>"
						+		"<a n='zimbraAccountStatus'>locked</a>"
						+	"</ModifyAccountRequest>");

		// Send a mail to the account
		String subject = "subject"+ ConfigProperties.getUniqueString();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ locked.EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(locked.EmailAddress);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on account
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, locked.EmailAddress);

		// Gearbox >> View mail
		app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_VIEW_MAIL);

		// Wait for page to load
		SleepUtil.sleepLong();

		// Mail page opens in separate window
		List<String> windowIds = app.zPageMain.sGetAllWindowIds();
		ZAssert.assertTrue(windowIds.size() > 1, "User's mailbox is not opened in the separate window");

		boolean mailFound = false;
		for(String id: windowIds) {
			app.zPageMain.sSelectWindow(id);

			String pageTitle = "Zimbra: Inbox";
			if (ConfigProperties.isZimbra9XEnvironment()) {
				pageTitle = "Zimbra";
			}

			if (app.zPageMain.sGetTitle().contains(pageTitle) && !(app.zPageMain.sGetTitle().contains("Zimbra Administration"))) {
				mailFound = app.zPageMain.zVerifyMailExists(subject);
				app.zPageMain.sClose();
				app.zPageMain.sSelectWindow("null");
				break;
			} else if (!(app.zPageMain.sGetTitle().contains("Zimbra Administration"))) {
				app.zPageMain.zSeparateWindowClose(app.zPageMain.sGetTitle());
			}
		}

		// Verify that the mail is present
		ZAssert.assertTrue(mailFound, "Verify that user's mail is displayed to admin");
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