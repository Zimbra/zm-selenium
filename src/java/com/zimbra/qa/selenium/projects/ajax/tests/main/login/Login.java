/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.main.login;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderMountpointItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.util.CommandLineUtility;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.PageLogin.Locators;

public class Login extends AjaxCore {

	public Login() {
		logger.info("New "+ Login.class.getCanonicalName());
		super.startingPage = app.zPageLogin;
	}


	@Test (description = "Login to the Ajax Client",
			groups = { "smoke" })

	public void Login_01() throws HarnessException {

		// Login
		app.zPageLogin.zLogin(ZimbraAccount.AccountZCS());

		// Verify main page becomes active
		ZAssert.assertTrue(app.zPageMain.zIsActive(), "Verify that the account is logged in");
	}


	@Test (description = "Login to the Ajax Client using a locked account",
			groups = { "bhr" })

	public void Login_02() throws HarnessException {

		//Create a Zimbra account
		ZimbraAccount locked = new ZimbraAccount();
		locked.provision();

		// Lock the account
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
						+		"<id>" + locked.ZimbraId + "</id>"
						+		"<a n='zimbraAccountStatus'>locked</a>"
						+	"</ModifyAccountRequest>");
		// Login
		app.zPageLogin.zSetLoginName(locked.EmailAddress);
		app.zPageLogin.zSetLoginPassword(locked.Password);
		app.zPageLogin.sClickAt(Locators.zBtnLogin, "");

		// Verify the error message displayed
		ZAssert.assertTrue(app.zPageLogin.zVerifyLoginErrorMessage(), "Verify login error message");

		// Verify main page never becomes active
		ZAssert.assertFalse(app.zPageMain.zIsActive(), "Verify that login was not successfull for a locked account");
	}


	@Test (description = "Login to the Ajax Client, with a mounted folder",
			groups = { "functional" })

	public void Login_03() throws HarnessException {

		String foldername = "folder" + ConfigProperties.getUniqueString();
		String subject = "subject" + ConfigProperties.getUniqueString();
		String mountpointname = "mountpoint" + ConfigProperties.getUniqueString();

		FolderItem inbox = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox);
		ZAssert.assertNotNull(inbox, "Verify other account's inbox exists");

		// Create a folder to share
		ZimbraAccount.AccountA().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" + inbox.getId() + "'/>"
				+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), foldername);
		ZAssert.assertNotNull(folder, "Verify other account's folder is created");

		// Share it
		ZimbraAccount.AccountA().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ ZimbraAccount.AccountZCS().EmailAddress +"' gt='usr' perm='r'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Add a message to it
		ZimbraAccount.AccountB().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<e t='t' a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>"
				+			"<su>"+ subject +"</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>"+ "body" + ConfigProperties.getUniqueString() +"</content>"
				+			"</mp>"
				+		"</m>"
				+	"</SendMsgRequest>");

		MailItem mail = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ subject +")");
		ZAssert.assertNotNull(mail, "Verify other account's mail is created");

		ZimbraAccount.AccountA().soapSend(
						"<MsgActionRequest xmlns='urn:zimbraMail'>"
					+		"<action id='"+ mail.getId() +"' op='move' l='"+ folder.getId() +"'/>"
					+	"</MsgActionRequest>");

		// Mount it
		ZimbraAccount.AccountZCS().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointname +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.AccountA().ZimbraId +"'/>"
				+	"</CreateMountpointRequest>");

		FolderMountpointItem mountpoint = FolderMountpointItem.importFromSOAP(ZimbraAccount.AccountZCS(), mountpointname);
		ZAssert.assertNotNull(mountpoint, "Verify active account's mountpoint is created");

		// Login
		app.zPageLogin.zLogin(ZimbraAccount.AccountZCS());

		// Verify main page becomes active
		ZAssert.assertTrue(app.zPageMain.zIsActive(), "Verify that the account is logged in");
	}


	@Bugs (ids = "59847")
	@Test (description = "Login to the Ajax Client, with a mounted folder of a deleted account",
			groups = { "functional" })

	public void Login_04() throws HarnessException {

		// Create Account2
		ZimbraAccount account = new ZimbraAccount();
		account.provision();
		account.authenticate();


		String foldername = "folder" + ConfigProperties.getUniqueString();
		String subject = "subject" + ConfigProperties.getUniqueString();
		String mountpointname = "mountpoint" + ConfigProperties.getUniqueString();

		FolderItem inbox = FolderItem.importFromSOAP(account, FolderItem.SystemFolder.Inbox);
		ZAssert.assertNotNull(inbox, "Verify other account's inbox exists");

		// Create a folder to share
		account.soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" + inbox.getId() + "'/>"
				+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(account, foldername);
		ZAssert.assertNotNull(folder, "Verify other account's folder is created");

		// Share it
		account.soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ ZimbraAccount.AccountZCS().EmailAddress +"' gt='usr' perm='r'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Add a message to it
		account.soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
        		+		"<m l='"+ folder.getId() +"' >"
            	+			"<content>From: foo@foo.com\n"
            	+				"To: foo@foo.com \n"
            	+				"Subject: "+ subject +"\n"
            	+				"MIME-Version: 1.0 \n"
            	+				"Content-Type: text/plain; charset=utf-8 \n"
            	+				"Content-Transfer-Encoding: 7bit\n"
            	+				"\n"
            	+				"simple text string in the body\n"
            	+			"</content>"
            	+		"</m>"
				+	"</AddMsgRequest>");

		MailItem mail = MailItem.importFromSOAP(account, "subject:("+ subject +")");
		ZAssert.assertNotNull(mail, "Verify other account's mail is created");

		// Mount it
		ZimbraAccount.AccountZCS().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointname +"'  rid='"+ folder.getId() +"' zid='"+ account.ZimbraId +"'/>"
				+	"</CreateMountpointRequest>");

		FolderMountpointItem mountpoint = FolderMountpointItem.importFromSOAP(ZimbraAccount.AccountZCS(), mountpointname);
		ZAssert.assertNotNull(mountpoint, "Verify active account's mountpoint is created");

		// Delete other account
		ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<DeleteAccountRequest xmlns='urn:zimbraAdmin'>"
				+		"<id>"+ account.ZimbraId +"</id>"
				+	"</DeleteAccountRequest>");

		// Login
		app.zPageLogin.zLogin(ZimbraAccount.AccountZCS());

		// Verify main page becomes active
		ZAssert.assertTrue(app.zPageMain.zIsActive(), "Verify that the account is logged in");
	}


	@DataProvider(name = "ZimbraMailURLDataProvider")
	public Object[][] ZimbraMailURLDataProvider() {
		  return new Object[][] {
			  new Object[] { "/foobar", null },
		  };
		}

	@Bugs (ids = "66788")
	@Test (description = "Change the zimbraMailURL and login", priority=5,
			groups = { "functional", "non-aws" },
			dataProvider = "ZimbraMailURLDataProvider")

	public void Login_05(String zimbraMailURLtemp) throws HarnessException {

		String zimbraMailURL = null;

		try {

			// Get the original zimbraMailURL value
			ZimbraAdminAccount.GlobalAdmin().soapSend(
						"<GetConfigRequest xmlns='urn:zimbraAdmin'>"
					+		"<a n='zimbraMailURL'/>"
					+	"</GetConfigRequest>");
			zimbraMailURL = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//admin:a[@n='zimbraMailURL']", null);

			// Change to the new zimbraMailURL temp value
			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<ModifyConfigRequest xmlns='urn:zimbraAdmin'>"
				+		"<a n='zimbraMailURL'>" + zimbraMailURLtemp + "</a>"
				+	"</ModifyConfigRequest>");

			CommandLineUtility.runCommandOnZimbraServer(ZimbraAccount.AccountZCS().zGetAccountStoreHost(),
					"zmmailboxdctl restart");

			SleepUtil.sleepVeryLong();
			for (int i = 0; i <= 10; i++) {
				app.zPageMain.sOpen(ConfigProperties.getBaseURL() + zimbraMailURLtemp);
				if (app.zPageLogin.sIsElementPresent("css=input[class^='ZLoginButton']") == true ||
						app.zPageLogin.sIsElementPresent("css=div[id$='parent-ZIMLET'] td[id$='ZIMLET_textCell']") == true) {
					break;
				} else {
					SleepUtil.sleepLong();
					continue;
				}
			}

			// Login
			app.zPageLogin.zLogin(ZimbraAccount.AccountZCS());

			// Verify main page becomes active
			ZAssert.assertTrue(app.zPageMain.zIsActive(), "Verify that the account is logged in");

		} finally {

			if ( zimbraMailURL != null ) {

				// Delete any authToken/SessionID
				app.zPageLogin.sDeleteAllVisibleCookies();

				// Change the URL back to the original
				ZimbraAdminAccount.GlobalAdmin().soapSend(
						"<ModifyConfigRequest xmlns='urn:zimbraAdmin'>"
					+		"<a n='zimbraMailURL'>" + zimbraMailURL + "</a>"
					+	"</ModifyConfigRequest>");

				CommandLineUtility.runCommandOnZimbraServer(ZimbraAccount.AccountZCS().zGetAccountStoreHost(),
						"zmmailboxdctl restart");

				SleepUtil.sleepVeryLong();
				for (int i = 0; i <= 10; i++) {
					app.zPageLogin.zRefreshUI();
					if (app.zPageLogin.sIsElementPresent("css=input[class^='ZLoginButton']") == true ||
							app.zPageLogin.sIsElementPresent("css=div[id$='parent-ZIMLET'] td[id$='ZIMLET_textCell']") == true) {
						break;
					} else {
						SleepUtil.sleepLong();
						continue;
					}
				}
			}
		}
	}
}
