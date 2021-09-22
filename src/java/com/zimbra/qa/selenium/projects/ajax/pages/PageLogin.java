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
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.pages;

import java.util.Date;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.ConfigProperties.*;

public class PageLogin extends AbsTab {

	public static class Locators {
		private static final String getLoginBtnLocator() {
			String btnLocator = "";
			try {
				if (ConfigProperties.zimbraGetVersionString().contains("8.8.")) {	
					btnLocator = "css=input.ZLoginButton";	
				} else {
					btnLocator = "css=button[class='loginButton']";
				}
			} catch (HarnessException e) {
				logger.error("Unable to get login button locator!", e);
			}
			return btnLocator;
		}

		// Buttons
		public static final String zBtnLogin = getLoginBtnLocator();
		public static final String zBtnVerify = "css=input[value='Verify']";

		// Text Input
		public static final String zInputUsername = "css=input[id='username']";
		public static final String zInputPassword = "css=input[id='password']";
		public static final String zInputCode = "css=input[id='totpcode']";
		public static final String zInputRemember = "css=input[id='remember']";
		public static final String zTrustThisComputer = "css=input[id='trustedDevice']";

		// Displayed text
		public static final String zDisplayedusername = "css=form[name='loginForm'] label[for='username']";
		public static final String zDisplayedcopyright = "css=div[class='copyright']";
		public static final String zLoginErrorMessage = "css=div[class='errorMessage']";
		public static final String zLoginErrorMessage8X = "css=div[id='ZLoginErrorPanel']";

		// Toolbar links
		public static final String zLogoutLink = "css=[id='skin_container_logoff']>a";
	}

	public PageLogin(AbsApplication application) {
		super(application);
		logger.info("new " + PageLogin.class.getCanonicalName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		AppType appType = ConfigProperties.getAppType();
		Boolean isAjax = false;
		String locator = null;

		switch (appType) {
		case AJAX:
			isAjax = true;
			break;
		default:
			throw new HarnessException("Please add a support for appType: " + appType);
		}

		if (isAjax) {
			locator = Locators.zBtnLogin;
		}

		// Look for the login button.
		boolean present = sIsElementPresent(locator);
		if (!present) {
			logger.debug("isActive() present = " + present);
			return (false);
		}

		boolean visible = zIsVisiblePerPosition(locator, 0, 0);
		if (!visible) {
			logger.debug("isActive() visible = " + visible);
			return (false);
		}

		logger.debug("isActive() = " + true);
		return (true);
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if (zIsActive()) {
			logger.info(myPageName() + " is already loaded");
			return;
		}

		// Logout
		if (((AjaxPages) MyApplication).zPageMain.zIsActive()) {
			sOpen(ConfigProperties.getLogoutURL());
			sOpen(ConfigProperties.getBaseURL());
		}

		zWaitForActive();

	}

	public void zLogin(ZimbraAccount account) throws HarnessException {
		logger.debug("login(ZimbraAccount account)" + account.EmailAddress);

		tracer.trace("Login to the " + MyApplication.myApplicationName() + " using user/password "
				+ account.EmailAddress + "/" + account.Password);

		zNavigateTo();

		Date start = new Date();

		try {
			((AjaxPages) MyApplication).zPageMain.zRefreshUITillElementPresent(Locators.zBtnLogin);

			for (int i=0; i<=2; i++) {
				zSetLoginName(account.EmailAddress);
				zSetLoginPassword(account.Password);

				if (!zGetLoginName().equals(account.EmailAddress)) {
					zSetLoginName(account.EmailAddress);
				}

				sClick(Locators.zBtnLogin);
				zWaitForBusyOverlay();
				SleepUtil.sleepLong();
				if (!sIsElementPresent(Locators.zBtnLogin)) {
					break;
				}
			}

			((AjaxPages) MyApplication).zPageMain.zWaitForActive(10000);
			((AjaxPages) MyApplication).zSetActiveAccount(account);

		} finally {
			SleepMetrics.RecordProcessing((new Throwable()).getStackTrace(), start, new Date());
		}
	}

	public AbsPage zSetupAfterLogin(ZimbraAccount account) throws HarnessException {
		logger.debug("login(ZimbraAccount account)" + account.EmailAddress);

		tracer.trace("Login to the " + MyApplication.myApplicationName() + " using user/password "
				+ account.EmailAddress + "/" + account.Password);

		zNavigateTo();

		for (int i=0; i<=2; i++) {
			zSetLoginName(account.EmailAddress);
			zSetLoginPassword(account.Password);

			if (!zGetLoginName().equals(account.EmailAddress)) {
				zSetLoginName(account.EmailAddress);
			}

			sClick(Locators.zBtnLogin);
			zWaitForBusyOverlay();
			SleepUtil.sleepLong();
			if (!sIsElementPresent(Locators.zBtnLogin)) {
				break;
			}
		}

		AbsPage page = null;
		page = new Dialog2FactorAuthEnable(MyApplication, ((AjaxPages) MyApplication).zPageLogin);
		if (page.zIsActive()) {
			return (page);
		}

		return (null);
	}

	public void zLogin(ZimbraAccount account, String totp, boolean trustThisComputer) throws HarnessException {
		logger.debug("login(ZimbraAccount account)" + account.EmailAddress);

		tracer.trace("Login to the " + MyApplication.myApplicationName() + " using user/password "
				+ account.EmailAddress + "/" + account.Password);

		zNavigateTo();

		Date start = new Date();

		try {

			for (int i=0; i<=2; i++) {
				zSetLoginName(account.EmailAddress);
				zSetLoginPassword(account.Password);

				if (!zGetLoginName().equals(account.EmailAddress)) {
					zSetLoginName(account.EmailAddress);
				}

				sClick(Locators.zBtnLogin);
				zWaitForBusyOverlay();
				SleepUtil.sleepLong();
				if (!sIsElementPresent(Locators.zBtnLogin)) {
					break;
				}
			}

			zSetLoginTOTPCode(totp);
			if (trustThisComputer == true) {
				zMarkTrustThisComputer();
			}

			sClickAt(Locators.zBtnVerify, "");
			((AjaxPages) MyApplication).zPageMain.zWaitForActive(10000);
			((AjaxPages) MyApplication).zSetActiveAccount(account);

		} finally {

			SleepMetrics.RecordProcessing((new Throwable()).getStackTrace(), start, new Date());

		}
	}

	public String zGetLoginName() throws HarnessException {
		String locator = Locators.zInputUsername;
		return sGetValue(locator);
	}

	public void zSetLoginName(String name) throws HarnessException {
		String locator = Locators.zInputUsername;
		if (name == null) {
			throw new HarnessException("Name is null");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Login field does not exist " + locator);
		}
		clearField(locator);
		sType(locator, name);
	}

	public void zSetLoginPassword(String password) throws HarnessException {
		String locator = Locators.zInputPassword;
		if (password == null) {
			throw new HarnessException("Password is null");
		}
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Password field does not exist " + locator);
		}
		clearField(locator);
		sType(locator, password);
	}

	public void zSetLoginTOTPCode(String totpCode) throws HarnessException {
		String locator = Locators.zInputCode;
		if (totpCode == null) {
			throw new HarnessException("totp code is null");
		}
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("totp code field does not exist " + locator);
		}
		clearField(locator);
		sType(locator, totpCode);
		SleepUtil.sleepSmall();
	}

	public void zMarkTrustThisComputer() throws HarnessException {
		tracer.trace("Click on Trust this computer");
		SleepUtil.sleepSmall();
		this.sClick(Locators.zTrustThisComputer);
		SleepUtil.sleepSmall();
	}

	public boolean zVerifyTrustThisComputer() throws HarnessException {
		return sIsElementPresent(Locators.zTrustThisComputer);
	}

	public boolean zVerifyLoginErrorMessage() throws HarnessException {
		String loginErrorMessage = "The username or password is incorrect. Verify that CAPS LOCK is not on, and then retype the current username and password.";
		if (ConfigProperties.isZimbra9XEnvironment()) {
			return sGetText(Locators.zLoginErrorMessage).contains(loginErrorMessage);
		}
		return sGetText(Locators.zLoginErrorMessage8X).contains(loginErrorMessage);
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		throw new HarnessException("Login page does not have a Toolbar");
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		throw new HarnessException("Login page does not have a Toolbar");
	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		throw new HarnessException("Login page does not have lists");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		throw new HarnessException("Login page does not have lists");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException {
		throw new HarnessException("Login page does not have lists");
	}

	@Override
	public AbsPage zKeyboardShortcut(Shortcut shortcut) throws HarnessException {
		throw new HarnessException("No shortcuts supported in the login page");
	}
}