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
import com.zimbra.qa.selenium.projects.ajax.ui.PageLogin.Locators;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
public class ZimbraFeatureTrustedDevicesEnabled extends AjaxCommonTest {

	public ZimbraFeatureTrustedDevicesEnabled() {
		
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 2485388299568483622L;
			{				
		 		put("zimbraFeatureTwoFactorAuthAvailable", "TRUE");
		 		
			}
		};

	}

	@Test(	description = "Trust this computer should not be present if ZimbraFeatureTrustedDevicesEnabled is set to false",
			groups = { "smoke", "network" })
	public void ZimbraFeatureTrustedDevicesEnabled_01() throws HarnessException {
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
		
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ app.zGetActiveAccount().ZimbraId +"</id>"
			+		"<a n='zimbraFeatureTrustedDevicesEnabled'>FALSE</a>"
			+	"</ModifyAccountRequest>");

		app.zPageMain.zLogout();
		app.zPageLogin.zSetLoginName(ZimbraAccount.AccountZWC().EmailAddress);
		app.zPageLogin.zSetLoginPassword(ZimbraAccount.AccountZWC().Password);
		app.zPageLogin.sClickAt(Locators.zBtnLogin, "");
        ZAssert.assertFalse(app.zPageLogin.zVerifyTrustThisComputer(), "Verify 'trust this computer' is not present");

	}
	
	@AfterMethod(groups={"always"})
	public void beforeMethod() throws HarnessException {
		zKillBrowserAndRelogin();
		logger.info(app.zGetActiveAccount().EmailAddress);
	}
	
	@AfterMethod(groups={"always"})
	public void afterMethod() throws HarnessException {
		zKillBrowserAndRelogin();
		logger.info(app.zGetActiveAccount().EmailAddress);
	}
}
