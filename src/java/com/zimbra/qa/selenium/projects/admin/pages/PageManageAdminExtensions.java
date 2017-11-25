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
package com.zimbra.qa.selenium.projects.admin.pages;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

/**
 * @author Matt Rhoades
 *
 */
public class PageManageAdminExtensions extends AbsTab {

	public static class Locators {
		public static final String CONFIGURE_ICON = "css=div.ImgAdministration";
		public static final String ADMIN_EXTENSION = "zti__AppAdmin__CONFIGURATION__AEMINEXT_textCell";
		public static final String GEAR_ICON = "css=div.ImgConfigure";
		public static final String HOME = "Home";
		public static final String CONFIGURE = "Configure";
		public static final String ADMIN_EXTENSIONS = "Admin Extensions";
		public static final String ADMIN_EXTENSIONS_LIST = "css=div[id='zl__ADMEXT_MANAGE']";
		public static final String UNDEPLOY_ZIMLET = "css=div[id='zmi__zb_currentApp__DELETE']";
	}

	public PageManageAdminExtensions(AbsApplication application) {
		super(application);
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if (!MyApplication.zIsLoaded())
			throw new HarnessException("Admin Console application is not active!");

		boolean present = sIsElementPresent(Locators.ADMIN_EXTENSIONS_LIST);
		if (!present) {
			return (false);
		}
		return (true);

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if (zIsActive()) {
			return;
		}

		sClickAt(Locators.CONFIGURE_ICON, "");
		zWaitForWorkInProgressDialogInVisible();
		zWaitForElementPresent(Locators.ADMIN_EXTENSION);
		sClickAt(Locators.ADMIN_EXTENSION, "");
		SleepUtil.sleepMedium();
		zWaitForWorkInProgressDialogInVisible();
		zWaitForActive();
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown(" + pulldown + ", " + option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		if (pulldown == Button.B_GEAR_BOX) {
			pulldownLocator = Locators.GEAR_ICON;

			if (option == Button.B_UNDEPLOY_ZIMLET) {
				optionLocator = Locators.UNDEPLOY_ZIMLET;
				page = new DialogForUndeployAdminExtension(this.MyApplication, null);

			} else {
				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
			}

		} else {
			throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
		}

		if (pulldownLocator != null) {

			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator "
						+ pulldownLocator + " not present!");
			}

			this.sClickAt(pulldownLocator, "");
			SleepUtil.sleepLong();

			if (optionLocator != null) {

				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}

				this.sClickAt(optionLocator, "");
				SleepUtil.sleepLong();
			}
		}
		return (page);
	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		logger.info(myPageName() + " zListItem(" + action + ", " + item + ")");

		tracer.trace(action + " on subject = " + item);

		AbsPage page = null;
		SleepUtil.sleepMedium();

		// How many items are in the table?
		String rowsLocator = "css=div#zl__ADMEXT_MANAGE div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zListItem: number of rows: " + count);

		count = this.sGetCssCount(rowsLocator);

		for (int i = 1; i <= count; i++) {
			final String accountLocator = rowsLocator;
			String locator;

			// Email Address
			locator = accountLocator + ":nth-child(" + i + ")";
			SleepUtil.sleepSmall();

			if (this.sIsElementPresent(locator)) {
				SleepUtil.sleepSmall();
				if (this.sGetText(locator).trim().contains(item)) {
					if (action == Action.A_LEFTCLICK) {
						sClick(locator);
						SleepUtil.sleepLong();
						break;
					} else if (action == Action.A_RIGHTCLICK) {
						sRightClick(locator);
						break;
					}
				}
			}
		}
		return page;
	}

	public boolean zVerifyAdminExtensionName(String item) throws HarnessException {
		if (this.sIsElementPresent("css=div#zl__ADMEXT_MANAGE div[id$='__rows'] div[id$='__" + item + "']"))
			return true;
		return false;
	}

	public boolean zVerifyHeader(String header) throws HarnessException {
		if (this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}
}