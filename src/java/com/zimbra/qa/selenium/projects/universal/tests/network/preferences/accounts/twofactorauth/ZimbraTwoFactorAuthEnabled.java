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
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.PagePreferences.Locators;
import com.zimbra.qa.selenium.projects.universal.ui.Dialog2FactorAuthEnable;

public class ZimbraTwoFactorAuthEnabled extends UniversalCommonTest {

	public ZimbraTwoFactorAuthEnabled() {
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 2485388299568483622L;
			{				
		 		put("zimbraFeatureTwoFactorAuthAvailable", "TRUE");
			}
		};
	}

	@Test( description = "Enable two factor auth from preferences", priority=4, 
			groups = { "sanity", "L0", "network" } )
	
	public void ZimbraTwoFactorAuthEnabled_01() throws HarnessException {
		
		// Navigate to preferences -> Accounts
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);

		// Click on setup link
		logger.info("Click on 'Setup two-step authentication'");
		app.zPagePreferences.sClick(Locators.z2FAEnableLink);
		SleepUtil.sleepMedium();
		
		Dialog2FactorAuthEnable dialog = (Dialog2FactorAuthEnable) new Dialog2FactorAuthEnable(app, app.zPagePreferences);
		dialog.zClickButton(Button.B_BEGIN_SETUP);
		SleepUtil.sleepMedium();
		
		dialog.zSetUserPassword(app.zGetActiveAccount().Password);
		SleepUtil.sleepSmall();
		dialog.zClickButton(Button.B_NEXT);
		SleepUtil.sleepMedium();
		
		dialog.zClickButton(Button.B_NEXT);
		String secretKey = dialog.zGetSecretKey();
		String totp = CommandLine.cmdExecOnServer(app.zGetActiveAccount().EmailAddress, secretKey);
		
		dialog.zClickButton(Button.B_NEXT);
		SleepUtil.sleepVeryVeryLong();
		dialog.zSetTotpCode(totp);
		SleepUtil.sleepMedium();
		
		dialog.zClickButton(Button.B_NEXT);
		SleepUtil.sleepLong();
		
		dialog.zClickButton(Button.B_FINISH);
		SleepUtil.sleepMedium();

		//-- VERIFICATION
        ZAssert.assertTrue(app.zPagePreferences.zVerifyDisable2FALink(), "Verify Disable link is present");

	}
	
	@AfterMethod(groups={"always"})
	public void afterMethod() throws HarnessException {
		zFreshLogin();
		logger.info(app.zGetActiveAccount().EmailAddress);
	}

}
