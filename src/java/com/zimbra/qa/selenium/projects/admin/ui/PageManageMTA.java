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

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Checkbox;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;


/**
 * @author Jitesh Singh
 *
 */
public class PageManageMTA extends AbsTab {

	public static class Locators {
		public static final String CONFIGURE_ICON="css=div.ImgAdministration";
		public static final String GLOBAL_SETTING="zti__AppAdmin__CONFIGURATION__GSET_textCell";
		public static final String GEAR_ICON="css=div.ImgConfigure";
		public static final String UPDATE_LICENSE="css=div[id^='zmi__zb_currentApp__']:contains('Update License')";
		public static final String MTA_Page = "css=td.ZWidgetTitle span:contains('MTA')";
		public static final String HOME="css=div#ztih__AppAdmin__Home_textCell";
		public static final String GLOBAL_SETTINGS_MTA="css=td[id^='zti__AppAdmin__CONFIGURATION__GSET__'] div[id$='_textCell']:contains('MTA')";
		public static final String GENERAL_INFORMATION="css=div[id^='zti__AppAdmin__CONFIGURATION__GSET'] div[class='ZTreeItemTextCell']:contains('General Information')";
		public static final String TLS_AUTHENTICATION_ONLY = "css=input[id$='_zimbraMtaTlsAuthOnly']";
		public static final String ENABLE_MILTER_SERVER = "css=input[id='ztabv__GSET_EDIT_zimbraMilterServerEnabled']";
		public static final String CLIENTS_IP_ADDRESS = "css=input[id$='_zimbraMtaRestriction_reject_unknown_client_hostname']";		
		public static final String SAVE = "css=td[id='zb__ZaCurrentAppBar__SAVE_title']";
		public static final String CLOSE = "css=td[id='zb__ZaCurrentAppBar__CLOSE_title']";
		public static final String SAVE_CSS = "div[id$='__SAVE']";

	}


	public PageManageMTA(AbsApplication application) {
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


		boolean present = sIsElementPresent(Locators.MTA_Page);
		if ( !present ) {
			return (false);
		}

		boolean visible = zIsVisiblePerPosition(Locators.MTA_Page, 0, 0);
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
			// This page is already active.
			return;
		}

		zClickAt(Locators.GLOBAL_SETTINGS_MTA,"");
		zWaitForActive();
	}
	
	public void zCheckboxSet(Checkbox checkbox, boolean status) throws HarnessException {
		logger.info("zCheckboxSet(" + checkbox + ") = " + status);
		
		if(checkbox == null) {
			throw new HarnessException("Checkbox cannot be null!");
		}
		
		String locator = null;
		
		switch(checkbox.toString()) {
		
		case "C_MTA_CLIENTS_IP_ADDRESS" :locator = Locators.CLIENTS_IP_ADDRESS;
										 break;
		case "C_MTA_TLS_AUTHENTICATION_ONLY":locator = Locators.TLS_AUTHENTICATION_ONLY;
										 break;
		case "C_MTA_ENABLE_MILTER_SERVER":locator=Locators.ENABLE_MILTER_SERVER;
										 break;
		default: new  HarnessException("implement the "+ checkbox);
		}
				
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException(locator + " not present!");
		}

		if ( this.sIsChecked(locator) == status ) {
			logger.debug("checkbox status matched.  not doing anything");
			return;
		}
		if ( status == true ) {
			this.sCheck(locator);
		} else {
			this.sUncheck(locator);
		}

		this.zWaitForBusyOverlay();
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
		logger.info(myPageName() + " zToolbarPressButton("+ button +")");

		tracer.trace("Press the "+ button +" button");

		if ( button == null )
			throw new HarnessException("Button cannot be null!");


		// Default behavior variables

		String locator = null;			// If set, this will be clicked
		AbsPage page = null;			// If set, this page will be returned

		// Based on the button specified, take the appropriate action(s)

		switch(button.toString()) {

		case "B_SAVE" : locator = Locators.SAVE;
						this.sClick(locator);
						SleepUtil.sleepSmall();
						page = null;
						break;

		case "B_CLOSE": locator = Locators.CLOSE;
						this.sClick(locator);
						SleepUtil.sleepSmall();
						page = new PageManageCOS(MyApplication);
						break;

		default : throw new HarnessException("no logic defined for button "+ button);
		}
		return page;
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ option +")");

		tracer.trace("Click pulldown "+ pulldown +" then "+ option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");


		// Default behavior variables
		String pulldownLocator = null; // If set, this will be expanded
		//String optionLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		if (pulldown == Button.B_GEAR_BOX) {
			pulldownLocator = Locators.GEAR_ICON;

			if (option == Button.B_UPDATE_LICENSE) {

				//optionLocator = Locators.UPDATE_LICENSE;
				page = new WizardUpdateLicense(this);

				// FALL THROUGH
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
		}
		// Return the specified page, or null if not set
		return (page);

	}

	public AbsPage zToolbarPressButton(Button button, IItem item) throws HarnessException {	
		return null;
	}

	public boolean zVerifyHeader(String header) throws HarnessException {
		if(this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}

}
