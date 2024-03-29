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
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;

public class ViewMail extends AdminCore {

	public ViewMail() {
		logger.info("New "+ ViewMail.class.getCanonicalName());
		super.startingPage = app.zPageManageAccounts;
	}


	@Test (description = "View mail - manage account >> Gearbox >> edit account >>  View mail",
			groups = { "smoke", "non-zimbrax", "testcafe" })

	public void ViewMail_01() throws HarnessException {
		// Create a new account in the admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
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

		// Search account
		app.zPageSearchResults.zAddSearchQuery(account.getEmailAddress());
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// View mail
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());
		app.zPageSearchResults.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_VIEW_MAIL);

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


	@Bugs (ids = "69155")
	@Test (description = "View mail - manage account > right click > view mail",
			groups = { "functional", "non-zimbrax", "testcafe" })

	public void ViewMail_02() throws HarnessException {
		// Create a new account in the admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
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

		// Search account
		app.zPageSearchResults.zAddSearchQuery(account.getEmailAddress());
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// View mail
		app.zPageSearchResults.zListItem(Action.A_RIGHTCLICK, account.getEmailAddress());
		app.zPageSearchResults.zToolbarPressButton(Button.B_VIEW_MAIL);

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


	@Test (description = "View mail - Edit a basic account - Search List View",
			groups = { "functional", "non-zimbrax", "testcafe" })

	public void ViewMail_03() throws HarnessException {
		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
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

		// Refresh the list
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