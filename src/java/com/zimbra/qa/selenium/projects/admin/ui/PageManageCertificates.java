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
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.ajax.ui.briefcase.DialogUploadFile;

/**
 * @author Matt Rhoades
 *
 */
public class PageManageCertificates extends AbsTab {

	public static class Locators {
		public static final String CONFIGURE_ICON = "css=div.ImgAdministration";
		public static final String CERTIFICATE = "zti__AppAdmin__CONFIGURATION__CertHV_textCell";
		public static final String GEAR_ICON = "css=div.ImgConfigure";
		public static final String HOME = "Home";
		public static final String CONFIGURE = "Configure";
		public static final String CERTIFICATES = "Certificates";
		public static final String SERVER_HOST_NAME = "css=div[id^='zl__SERVER_MANAGE'] table tbody tr:nth-child(2) td div[id^='zli__DWT']";
		public static final String VIEW_CERTIFICATE = "css=div[id='zmi__zb_currentApp__VIEW']";
		public static final String LDAP_CERTIFICATE_LABEL = "css=div.ZaApp:not([aria-hidden='true']) > div.DwtComposite > div > table:nth-child(3) tr:nth-child(1) td:nth-child(2):contains('zimbra')";
		public static final String INSTALL_MESSAGE = "css=td[class='DwtAlertContent']:contains('Your certificate was installed successfully')";
		public static final String UPLOAD_CERTIFICATE = "css=input[name='certFile']";
		public static final String UPLOAD_ROOT_CERTIFICATE = "css=input[name='rootCA']";

		public static final String INSTALL_CERTIFICATE = "css=div[id='zmi__zb_currentApp__NEW']";
	}

	public PageManageCertificates(AbsApplication application) {
		super(application);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see projects.admin.ui.AbsTab#isActive()
	 */
	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if (!MyApplication.zIsLoaded())
			throw new HarnessException("Admin Console application is not active!");

		boolean present = sIsElementPresent(Locators.GEAR_ICON);
		if (!present) {
			return (false);
		}

		boolean visible = zIsVisiblePerPosition(Locators.GEAR_ICON, 0, 0);
		if (!visible) {
			logger.debug("isActive() visible = " + visible);
			return (false);
		}

		return (true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see projects.admin.ui.AbsTab#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see projects.admin.ui.AbsTab#navigateTo()
	 */
	@Override
	public void zNavigateTo() throws HarnessException {

		if (zIsActive()) {
			// This page is already active.
			return;
		}

		// Click on Addresses -> Accounts
		zClickAt(Locators.CONFIGURE_ICON, "");
		zWaitForWorkInProgressDialogInVisible();
		sIsElementPresent(Locators.CERTIFICATE);
		zClickAt(Locators.CERTIFICATE, "");
		zWaitForWorkInProgressDialogInVisible();
		zWaitForActive();
		SleepUtil.sleepMedium();

	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		return null;
	}

	public AbsPage zToolbarPressButton(Button button, IItem item) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		// Default behavior variables
		//
		String locator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		// Based on the button specified, take the appropriate action(s)
		//

		if (button == Button.B_INSTALL_CERTIFICATE) {

			// Install vertificate link on home page
			locator = PageMain.Locators.HomeInstallCertificate;
			// Create the page
			page = new WizardInstallCertificate(this);
			// FALL THROUGH

		} else if (button == Button.B_UPLOAD_CERTIFICATE) {

			locator = Locators.UPLOAD_CERTIFICATE;

			page = new DialogUploadFile(MyApplication, this);
		} else if (button == Button.B_UPLOAD_ROOT_CERTIFICATE) {

			locator = Locators.UPLOAD_ROOT_CERTIFICATE;

			page = new DialogUploadFile(MyApplication, this);
		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		// Default behavior, process the locator by clicking on it
		//
		this.sClickAt(locator, "");

		// If page was specified, make sure it is active
		if (page != null) {
			SleepUtil.sleepMedium();
		}

		sMouseOut(locator);
		return (page);
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown(" + pulldown + ", " + option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		// Default behavior variables
		String pulldownLocator = null; // If set, this will be expanded
		String optionLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		if (pulldown == Button.B_GEAR_BOX) {
			pulldownLocator = Locators.GEAR_ICON;

			if (option == Button.B_VIEW_CERTIFICATE) {

				optionLocator = Locators.VIEW_CERTIFICATE;

				// FALL THROUGH
			} else if (option == Button.B_INSTALL_CERTIFICATE) {

				optionLocator = Locators.INSTALL_CERTIFICATE;

				page = new WizardInstallCertificate(this);
			}

			else {
				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
			}

		} else {
			throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
		}

		// Default behavior
		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator "
						+ pulldownLocator + " not present!");
			}

			this.zClickAt(pulldownLocator, "");
			SleepUtil.sleepMedium();

			// If the app is busy, wait for it to become active
			// zWaitForBusyOverlay();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}

				this.zClickAt(optionLocator, "");

				// If the app is busy, wait for it to become active
				// zWaitForBusyOverlay();
			}

		}

		// Return the specified page, or null if not set
		return (page);

	}

	public boolean zVerifyHeader(String header) throws HarnessException {
		if (this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {

		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		// Based on the button specified, take the appropriate action(s)

		if (button == Button.B_INSTALL_CERTIFICATE) {

			locator = PageMain.Locators.HomeInstallCertificate;
			page = new WizardInstallCertificate(this);

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}
		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		this.sClickAt(locator, "");

		if (page != null) {
			SleepUtil.sleepMedium();
		}

		return page;

	}

	public AbsPage zSelectServer() throws HarnessException {
		AbsPage page = null;
		SleepUtil.sleepMedium();
		if (this.sIsElementPresent(Locators.SERVER_HOST_NAME))
			this.zClickAt(PageManageCertificates.Locators.SERVER_HOST_NAME, "");
		return (page);
	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		logger.info(myPageName() + " zListItem(" + action + ", " + item + ")");

		tracer.trace(action + " on subject = " + item);

		AbsPage page = null;
		SleepUtil.sleepMedium();

		// How many items are in the table?
		String rowsLocator = "css=div[id='zl__SERVER_MANAGE'] div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zListGetPolicy: number of policys: " + count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {
			final String serverLocator = rowsLocator + ":nth-child(" + i + ")";
			String locator;

			// Email Address
			locator = serverLocator + " tr";

			if (this.sIsElementPresent(locator)) {
				if (this.sGetText(locator).trim().contains(item)) {
					if (action == Action.A_LEFTCLICK) {
						zClick(locator);
						break;
					} else if (action == Action.A_RIGHTCLICK) {
						zRightClick(locator);
						break;
					}

				}
			}
		}
		return page;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException {
		// TODO Auto-generated method stub
		return null;
	}

}
