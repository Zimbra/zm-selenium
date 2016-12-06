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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.accounts.twofactorauth;

import java.util.HashMap;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.DialogAddApplicationCode;

public class AddApplicationCode extends AjaxCommonTest {

	public AddApplicationCode() {
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 2485388299568483622L;
			{				
		 		put("zimbraFeatureTwoFactorAuthAvailable", "TRUE");
			}
		};
	}

	@Test ( description = "Add application code for IMAP application from preferences and verify the same", priority=4, 
			groups = { "sanity", "L0", "network" })
	
	public void AddApplicationCode_01() throws HarnessException  {
		
		String totp, secret, tempToken;
		String applicationName = "appName" + ConfigProperties.getUniqueString();
		String host = ConfigProperties.getStringProperty("store.host");
		
		app.zPageMain.sOpen(ConfigProperties.getLogoutURL());
		ZimbraAccount.AccountZWC().soapSend(
        		"<EnableTwoFactorAuthRequest xmlns='urn:zimbraAccount'>" +
	        		"<name>" + ZimbraAccount.AccountZWC().EmailAddress + "</name>" +
	        		"<password>" + ZimbraAccount.AccountZWC().Password + "</password>" +
        		"</EnableTwoFactorAuthRequest>");
        secret = ZimbraAccount.AccountZWC().soapSelectValue("//acct:EnableTwoFactorAuthResponse", "secret");
        tempToken = ZimbraAccount.AccountZWC().soapSelectValue("//acct:EnableTwoFactorAuthResponse", "authToken");

		totp = CommandLine.cmdExecOnServer(ZimbraAccount.AccountZWC().EmailAddress, secret);
		ZimbraAccount.AccountZWC().soapSend(
        		"<EnableTwoFactorAuthRequest xmlns='urn:zimbraAccount'>" +
	        		"<name>" + ZimbraAccount.AccountZWC().EmailAddress + "</name>" +
	                "<authToken verifyAccount='0'>" + tempToken + "</authToken>" +
	                "<twoFactorCode>" + totp + "</twoFactorCode>" +
        		"</EnableTwoFactorAuthRequest>");
		// Login
		totp = CommandLine.cmdExecOnServer(ZimbraAccount.AccountZWC().EmailAddress, secret);
		app.zPageLogin.zLogin(ZimbraAccount.AccountZWC(), totp, false);
		
		// Verify main page becomes active
		ZAssert.assertTrue(app.zPageMain.zIsActive(), "Verify that the account is logged in");

		app.zPagePreferences.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);
		DialogAddApplicationCode dialog =(DialogAddApplicationCode) app.zPagePreferences.zPressButton(Button.B_ADD_APPLICATION_CODE);
		dialog.zEnterApplicationName(applicationName);
		SleepUtil.sleepSmall();
		
		dialog.zClickButton(Button.B_NEXT);
		SleepUtil.sleepSmall();
		String passcode = dialog.zGetDisplayedText("passcode");
	    System.out.println(passcode);
		SleepUtil.sleepSmall();
		dialog.zClickButton(Button.B_CLOSE);
		
		//Verification
		ZimbraAccount.Account10().soapSend(
        		"<TestDataSourceRequest xmlns='urn:zimbraMail'>" +
				"<imap host='" + host + "' port='993' username='" + ZimbraAccount.AccountZWC().EmailAddress + "' password='" + passcode + "' connectionType='ssl' />" +
			    "</TestDataSourceRequest>");
		String value = ZimbraAccount.Account10().soapSelectValue("//mail:TestDataSourceResponse/mail:imap","success");
		ZAssert.assertEquals(value, "1", "Verify that TestDataSource is tested correctly");
	}
	
	@AfterMethod(groups={"always"})
	public void afterMethod() throws HarnessException {
		zFreshLogin();
		logger.info(app.zGetActiveAccount().EmailAddress);
	}
}
