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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.accounts.twofactorauth;

import java.util.HashMap;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.PagePreferences.Locators;
import com.zimbra.qa.selenium.projects.ajax.ui.Dialog2FactorAuthEnable;
public class ZimbraTwoFactorAuthEnabled extends AjaxCommonTest {

	public ZimbraTwoFactorAuthEnabled() {
		
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 2485388299568483622L;
			{				
		 		put("zimbraFeatureTwoFactorAuthAvailable", "TRUE");
			}
		};

	}

	@Test(
			description = "Enable two factor auth from preferences",
			groups = { "sanity" }
			)
	public void zimbraTwoFactorAuthEnabled_01() throws HarnessException {
		
		// Navigate to preferences -> Accounts
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);

		// Click on setup link
		logger.info("Click on 'Setup two-step authentication'");
		app.zPagePreferences.sClick(Locators.z2FAEnableLink);
		
		Dialog2FactorAuthEnable dialog = (Dialog2FactorAuthEnable) new Dialog2FactorAuthEnable(app, app.zPagePreferences);
		dialog.zClickButton(Button.B_BEGIN_SETUP);
		SleepUtil.sleepSmall();
		dialog.zSetUserPassword(app.zGetActiveAccount().Password);
		dialog.zClickButton(Button.B_NEXT);
		SleepUtil.sleepSmall(); //was not clicking on Next button twice
		dialog.zClickButton(Button.B_NEXT);
		String secretKey = dialog.zGetSecretKey();
		String totp = CommandLine.cmdExecOnServer(app.zGetActiveAccount().EmailAddress, secretKey);
		dialog.zClickButton(Button.B_NEXT);
		SleepUtil.sleepVeryLong();
		dialog.zSetTotpCode(totp);
		dialog.zClickButton(Button.B_NEXT);
		SleepUtil.sleepSmall();
		dialog.zClickButton(Button.B_FINISH);
		SleepUtil.sleepVeryLong();

		//-- VERIFICATION
        ZAssert.assertTrue(app.zPagePreferences.zVerifyDisable2FALink(), "Verify Disable link is present");

	}
	
	@AfterMethod(groups={"always"})
	public void afterMethod() throws HarnessException {
		ZimbraAccount.ResetAccountZWC();
		if (app.zPageMail.sIsVisible("css=td[id='skin_dropMenu'] td[id$='_dropdown']") == false) { 
			app.zPageLogin.zLogin(ZimbraAccount.Account10());
			logger.info(app.zGetActiveAccount());
		}
	}

}
