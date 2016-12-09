/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
/**
 *
 */
package com.zimbra.qa.selenium.projects.admin.ui;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.ajax.ui.briefcase.DialogUploadFile;


/**
 * @author Matt Rhoades
 *
 */
public class PageManageLicense extends AbsTab {

	public static class Locators {
		public static final String CONFIGURE_ICON="css=div.ImgAdministration";
		public static final String GLOBAL_SETTING="zti__AppAdmin__CONFIGURATION__GSET_textCell";
		public static final String GEAR_ICON="css=div.ImgConfigure";
		public static final String HOME="Home";
		public static final String CONFIGURE="Configure";
		public static final String GLOBAL_SETTINGS="Global Settings";
		public static final String GLOBAL_SETTINGS_LICENSE="css=div[id^='zti__AppAdmin__CONFIGURATION__GSET'] div[class='ZTreeItemTextCell']:contains('License')";
		public static final String GENERAL_INFORMATION="css=div[id^='zti__AppAdmin__CONFIGURATION__GSET'] div[class='ZTreeItemTextCell']:contains('General Information')";
		public static final String UPDATE_LICENSE="css=div[id^='zmi__zb_currentApp__']:contains('Update License')";
		public static final String ACTIVATE_LICENSE="css=div[id^='zmi__zb_currentApp__']:contains('Activate License')";
		public static final String UPLOAD_LICENSE ="css=input[name='licenseFile']";
		public static final String NEXT_BUTTON="css=td[id$='_button12_title']";
		public static final String INSTALL_COMMERCIAL_CERTIFICATE="css=input[id='zdlgv__UNDEFINE_comm']";
	}


	public PageManageLicense(AbsApplication application) {
		super(application);
	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsTab#isActive()
	 */
	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if ( !MyApplication.zIsLoaded() )
			throw new HarnessException("Admin Console application is not active!");


		boolean present = sIsElementPresent(Locators.GEAR_ICON);
		if ( !present ) {
			return (false);
		}

		boolean visible = zIsVisiblePerPosition(Locators.GEAR_ICON, 0, 0);
		if ( !visible ) {
			logger.debug("isActive() visible = "+ visible);
			return (false);
		}

		return (true);

	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsTab#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsTab#navigateTo()
	 */
	@Override
	public void zNavigateTo() throws HarnessException {

		if ( zIsActive() ) {

			return;
		}

		// Click on Addresses -> Accounts
		zClickAt(Locators.CONFIGURE_ICON,"");
		zWaitForWorkInProgressDialogInVisible();
		SleepUtil.sleepMedium();		
		zClickAt(Locators.GLOBAL_SETTING, "");
		zWaitForWorkInProgressDialogInVisible();
		zClickAt(Locators.GLOBAL_SETTINGS_LICENSE, "");
		zWaitForWorkInProgressDialogInVisible();
		zWaitForActive();
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
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ option +")");

		tracer.trace("Click pulldown "+ pulldown +" then "+ option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");



		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		if (pulldown == Button.B_GEAR_BOX) {
			pulldownLocator = Locators.GEAR_ICON;

			if (option == Button.B_UPDATE_LICENSE) {

				optionLocator = Locators.UPDATE_LICENSE;
				page = new WizardUpdateLicense(this);


			}else if (option == Button.B_ACTIVATE_LICENSE) {

				optionLocator = Locators.ACTIVATE_LICENSE; 
				page = new DialogForActivateLicense(this.MyApplication,null);

			}	

			else {
				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
			}

		} else {
			throw new HarnessException("no logic defined for pulldown/option "
					+ pulldown + "/" + option);
		}

		// Default behavior
		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator " + pulldownLocator + " not present!");
			}

			this.zClickAt(pulldownLocator,"");
			SleepUtil.sleepMedium();

			// If the app is busy, wait for it to become active
			//zWaitForBusyOverlay();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator " + optionLocator + " not present!");
				}

				this.zClickAt(optionLocator,"");
				SleepUtil.sleepSmall();

				// If the app is busy, wait for it to become active
				//zWaitForBusyOverlay();
			}

		}

		// Return the specified page, or null if not set
		return (page);

	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton("+ button +")");

		tracer.trace("Press the "+ button +" button");

		if ( button == null )
			throw new HarnessException("Button cannot be null!");



		//
		String locator = null;			// If set, this will be clicked
		AbsPage page = null;	// If set, this page will be returned

		// Based on the button specified, take the appropriate action(s)
		//

		if (button == Button.B_UPLOAD_LICENSE) {

			locator = Locators.UPLOAD_LICENSE; 

			page = new DialogUploadFile(MyApplication, this);

		}
		else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		if ( locator == null ) {
			throw new HarnessException("locator was null for button "+ button);
		}

		// Default behavior, process the locator by clicking on it
		//
		this.sClickAt(locator,"");



		// If page was specified, make sure it is active
		if ( page != null ) {
			SleepUtil.sleepMedium();
		}

		sMouseOut(locator);
		return (page);
	}

	public boolean zVerifyHeader (String header) throws HarnessException {
		if (this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}

}
