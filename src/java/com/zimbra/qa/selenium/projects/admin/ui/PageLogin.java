/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.ui;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;


/**
 * This class defines the login page
 * @author Matt Rhoades
 *
 */
public class PageLogin extends AbsTab {

	public static class Locators {

		public static final String zLoginDialog = "css=div[class='ZaLoginDialog']";
		public static final String zLoginUserName = "ZLoginUserName";
		public static final String zLoginPassword = "ZLoginPassword";
		public static final String zLoginButtonContainer = "ZLoginButton";
		public static final String zLoginLicenseContainer = "ZLoginLicenseContainer";
		public static final String zLoginNewPassword = "css=input[id^='newpass1']";
		public static final String zConfirmNewPassword ="css=input[id^='newpass2']";

	}

	/**
	 * An object that controls the Admin Console Login Page
	 */
	public PageLogin(AbsApplication application) {
		super(application);

		logger.info("new " + myPageName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	/**
	 * If the "Login" button is visible, assume the LoginPage is active
	 */
	public boolean zIsActive() throws HarnessException {

		// Make sure the application is loaded first
		if ( !MyApplication.zIsLoaded() )
			throw new HarnessException("Admin Console application is not active!");


		// Look for the login button.
		boolean present = sIsElementPresent(Locators.zLoginButtonContainer);
		if ( !present ) {
			logger.debug("isActive() present = "+ present);
			return (false);
		}

		boolean visible = zIsVisiblePerPosition(Locators.zLoginButtonContainer, 0 , 0);
		if ( !visible ) {
			logger.debug("isActive() visible = "+ visible);
			return (false);
		}

		logger.debug("isActive() = "+ true);
		return (true);
	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if ( zIsActive() ) {
			
			return;
		}


		// Logout
		if ( ((AppAdminConsole)MyApplication).zPageMain.zIsActive() ) {
			((AppAdminConsole)MyApplication).zPageMain.logout();
		}

		zWaitForActive();
	}


	/**
	 * Login as the GlobalAdmin
	 * @throws HarnessException
	 */
	public void login() throws HarnessException {
		logger.debug("login()");

		login(ZimbraAdminAccount.AdminConsoleAdmin());
	}

	/**
	 * Login as the specified account
	 * @param account
	 * @throws HarnessException
	 */
	public void login(ZimbraAccount account) throws HarnessException {
		logger.debug("login(ZimbraAccount account)" + account.EmailAddress);

		zNavigateTo();

		// Fill out the form
		fillLoginFormFields(account);

		// Click the Login button
		sClick(Locators.zLoginButtonContainer);

		// Wait for the app to load
		// sWaitForPageToLoad();
		((AppAdminConsole)MyApplication).zPageMain.zWaitForActive();

		((AppAdminConsole)MyApplication).zSetActiveAcount(account);

		SleepUtil.sleep(10000);
	}

	/**
	 * Fill the form with the specified user
	 * @throws HarnessException
	 */
	public void fillLoginFormFields(ZimbraAccount account) throws HarnessException {
		logger.debug("fillFields(ZimbraAccount account)" + account.EmailAddress);

		if ( !zIsActive() )
			throw new HarnessException("LoginPage is not active");

		sType(Locators.zLoginUserName, account.EmailAddress);
		SleepUtil.sleepMedium();
		sType(Locators.zLoginPassword, account.Password);
	}
	
	/**
	 * Fill the reset password form with the specified user
	 * @throws HarnessException
	 */
	public void fillResetLoginPasswordFormFields(String NewPassword , String ConfirmPassword ) throws HarnessException {
		logger.debug("fillFields(ZimbraAccount account)" + NewPassword);

		if ( !zIsActive() )
			throw new HarnessException("LoginPage is not active");
		
		sType(Locators.zLoginNewPassword, NewPassword);
		SleepUtil.sleepMedium();
		sType(Locators.zConfirmNewPassword, ConfirmPassword);
		
		// Click the Login button
		sClick(Locators.zLoginButtonContainer);

		// Wait for the app to load
		// sWaitForPageToLoad();
		((AppAdminConsole)MyApplication).zPageMain.zWaitForActive();
		SleepUtil.sleep(10000);
	}
	
	@Override
	public AbsPage zListItem(Action action, String item)
			throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item)
			throws HarnessException {
		return null;
	}
	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption ,String item)
			throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option)
			throws HarnessException {
		return null;
	}



}
