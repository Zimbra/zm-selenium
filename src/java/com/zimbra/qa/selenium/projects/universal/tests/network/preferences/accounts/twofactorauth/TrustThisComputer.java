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
package com.zimbra.qa.selenium.projects.universal.tests.network.preferences.accounts.twofactorauth;

import java.util.HashMap;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;

public class TrustThisComputer extends AjaxCommonTest {

	public TrustThisComputer() {
		
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 2485388299568483622L;
			{				
		 		put("zimbraFeatureTwoFactorAuthAvailable", "TRUE");
			}
		};
	}

	@Test( description = "Trust the computer and verify that after relogin totp code is not required", priority=4, 
			groups = { "sanity", "L0" , "network" })
	
	public void TrustThisComputer_01() throws HarnessException {
		
		String totp, secret, tempToken;
		
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
		app.zPageLogin.zLogin(ZimbraAccount.AccountZWC(), totp, true);
		
		// Verify main page becomes active
		ZAssert.assertTrue(app.zPageMain.zIsActive(), "Verify that the account is logged in");

		app.zPagePreferences.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);
		
		//Verification
		ZAssert.assertTrue(app.zPagePreferences.zVerifyTrustedDeviceCount(1), "Verify trusted device count is increased");
		ZAssert.assertTrue(app.zPagePreferences.zVerifyRevokeThisDevice(), "Verify revoke this device link is present");
		app.zPageMain.zLogout();
	    this.app.zPageLogin.zNavigateTo();
	    this.startingPage.zNavigateTo();
	    logger.info("Logged in successfully without totp");
	    
	}
	
	@AfterMethod(groups={"always"})
	public void beforeMethod() throws HarnessException {
		zFreshLogin();
		logger.info(app.zGetActiveAccount().EmailAddress);
	}
	
	@AfterMethod(groups={"always"})
	public void afterMethod() throws HarnessException {
		zFreshLogin();
		logger.info(app.zGetActiveAccount().EmailAddress);
	}
	
}
