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
package com.zimbra.qa.selenium.projects.admin.ui;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.ajax.ui.Toaster;

/**
 * This class defines the top menu bar of the admin console application
 * @author Matt Rhoades
 *
 */
public class PageMain extends AbsTab {

	public static class Locators {
		public static final String zSkinContainerLogo		= "xpath=//*[@id='skin_container_logo']";		
		public static final String zSkinContainerUsername	= "css=div[id='skin_container_username']";

		public static final String zSkinContainerLogoff		= "css=table[class='skin_table'] span[onclick='ZaZimbraAdmin.logOff();']";
		public static final String zLogoffDropDownArrow		="css=td[id$='_dropdown'] div[class='ImgNodeExpandedWhite']";
		public static final String zLogOff = "zmi__ZA_LOGOFF__LOGOFF_title";

		public static final String zSkinContainerDW			= "xpath=//*[@id='skin_container_dw']";

		//Help Drop down
		public static final String zSkinContainerHelpDropDownArrow	= "css=div[id='skin_container_help'] div[class='ImgNodeExpandedWhite']";
		public static final String zHelpCenterOption = "css=div[id='zm__ZA_HELP'] div[id='zaHelpCenter'] td[id$='_title']";

		public static final String REFRESH_BUTTON = "css=div.ImgSearchRefreshWhite";

		public static final String HomeInstallLicense="css=div[id^='ztabv__HOMEV_output'] div:contains('Install Licenses')";

		public static final String HomeConfigureBackups = "css=div[id^='ztabv__HOMEV_output'] div:contains('Configure Back-ups')"; 

		public static final String HomeInstallCertificate = "css=div[id^='ztabv__HOMEV_output'] div:contains('Install Certificates')";   

		public static final String HomeConfigureDefaultCos = "css=div[id^='ztabv__HOMEV_output'] div:contains('Configure Default COS')";

		public static final String HomeCreateDomain = "css=div[id^='ztabv__HOMEV_output'] div:contains('Create Domain')";

		public static final String HomeConfigureGal = "css=div[id^='ztabv__HOMEV_output'] div:contains('Configure GAL')";

		public static final String HomeConfigureAuthentication = "css=div[id^='ztabv__HOMEV_output'] div:contains('Configure Authentication')"; 

		public static final String HomeAddAcoount = "css=div[id^='ztabv__HOMEV_output'] div:contains('Add Account')";

		public static final String HomeManageAccount = "css=div[id^='ztabv__HOMEV_output'] div:contains('Manage Accounts')";

		public static final String HomeMigrationCoexistance = "css=div[id^='ztabv__HOMEV_output'] div:contains('Migration and Co-existence')";

		public static final String zHelpButton = "css=div[id='zb__ZaCurrentAppBar__HELP'] td[id$='_title']";
	}

	public PageMain(AbsApplication application) {
		super(application);

		logger.info("new " + myPageName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	/**
	 * If the "Logout" button is visible, assume the MainPage is active
	 */
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if ( !MyApplication.zIsLoaded() )
			throw new HarnessException("Admin Console application is not active!");


		// Look for the Refresh Button
		boolean present = sIsElementPresent(Locators.zLogoffDropDownArrow);
		if ( !present ) {
			logger.debug("isActive() present = "+ present);
			return (false);
		}


		// Look for the Refresh Button.
		boolean visible = zIsVisiblePerPosition(Locators.zLogoffDropDownArrow, 0, 0);
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
			// This page is already active
			return;
		}


		// 1. Logout
		// 2. Login as the default account
		if ( !((AppAdminConsole)MyApplication).zPageLogin.zIsActive() ) {
			((AppAdminConsole)MyApplication).zPageLogin.zNavigateTo();
		}
		((AppAdminConsole)MyApplication).zPageLogin.login();
		SleepUtil.sleepLong();
	}

	/**
	 * Click the logout button
	 * @throws HarnessException
	 */
	public void logout() throws HarnessException {
		logger.debug("logout()");
		
		zNavigateTo();
		if ( !sIsElementPresent(Locators.zLogoffDropDownArrow) ) {
			throw new HarnessException("The refresh button is not present " + Locators.zLogoffDropDownArrow);
		}
		
		// Click on logout
		sClickAt(Locators.zLogoffDropDownArrow,"");
		sClickAt(Locators.zLogOff,"");
		SleepUtil.sleepLong();
		

		((AppAdminConsole)MyApplication).zPageLogin.zWaitForActive();

		((AppAdminConsole)MyApplication).zSetActiveAccount(null);

	}

	public String getContainerUsername() throws HarnessException {
		logger.debug("getLoggedInAccount()");

		if ( !zIsActive() )
			throw new HarnessException("MainPage is not active");

		String username = sGetText(Locators.zSkinContainerUsername);
		return (username);

	}

	public void zHandleDialogs() throws HarnessException {
		zRefreshMainUI();
	}

		/*// Opened dialogs
		String zIndex;
		List<WebElement> dialogLocators = webDriver().findElements(By.cssSelector("div[class^='Dwt'][class$='Dialog']"));

		int totalDialogs = dialogLocators.size();
		logger.info("Total dialogs found " + totalDialogs);

		for (int i=totalDialogs-1; i>=0; i--) {
			zIndex = dialogLocators.get(i).getCssValue("z-index");
			if (!zIndex.equals("auto") && !zIndex.equals("") && !zIndex.equals(null) && Integer.parseInt(zIndex)>=700) {
				logger.info("Found active dialog");
				sRefresh();				
				return;
			}
		}

		logger.info("No active dialogs found");
	}*/

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

	public AbsPage zToolbarPressButton(Button button) throws HarnessException {

		logger.info(myPageName() + " zToolbarPressButton("+ button +")");

		tracer.trace("Press the "+ button +" button");

		if ( button == null )
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_REFRESH) {
			locator = PageMain.Locators.REFRESH_BUTTON;
			page = null;

		} else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		if ( locator == null ) {
			throw new HarnessException("locator was null for button "+ button);
		}

		this.sClickAt(locator,"");
		SleepUtil.sleepMedium();

		return (page);
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option)
			throws HarnessException {
		return null;
	}


	public boolean zVerifyHeader (String header) throws HarnessException {
		if (this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}
	
	public Toaster zGetToaster() throws HarnessException {
		SleepUtil.sleepMedium();
		Toaster toaster = new Toaster(this.MyApplication);
		logger.info("toaster is active: " + toaster.zIsActive());
		return (toaster);
	}
}
