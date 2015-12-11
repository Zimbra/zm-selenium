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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.accounts.twofactorauth;

import java.util.HashMap;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
public class OneTimeCode extends AjaxCommonTest {

	public OneTimeCode() {
		
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 2485388299568483622L;
			{				
		 		put("zimbraFeatureTwoFactorAuthAvailable", "TRUE");
			}
		};

	}

	@Test(	description = "Login using One time Code instead of TOTP code",
			groups = { "smoke", "network" })
	public void OneTimeCode_01() throws HarnessException {
		String totp, secret, tempToken, oneTimeCode;
		
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
		
		oneTimeCode = ZimbraAccount.AccountZWC().soapSelectValue("//acct:EnableTwoFactorAuthResponse//acct:scratchCodes", "scratchCode");
		
		// Login
		app.zPageLogin.zNavigateTo();
		app.zPageLogin.zLogin(ZimbraAccount.AccountZWC(), oneTimeCode, false);
		
		// Verify main page becomes active
		ZAssert.assertTrue(app.zPageMain.zIsActive(), "Verify that the account is logged in");

	}
	
	@AfterMethod(groups={"always"})
	public void afterMethod() throws HarnessException {
		zKillBrowserAndRelogin();
		logger.info(app.zGetActiveAccount().EmailAddress);
	}

}
