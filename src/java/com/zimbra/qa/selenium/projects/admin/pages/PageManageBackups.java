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
import com.zimbra.qa.selenium.framework.util.ZAssert;

/**
 * @author Matt Rhoades
 *
 */
public class PageManageBackups extends AbsTab {

	public static class Locators {
		public static final String GEAR_ICON = "css=div.ImgConfigure";
		public static final String VIEW = "css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgProperties']";
		public static final String BACKUP = "css=div[id='zti__AppAdmin__magHV__BackUpHV_textCell']";
		public static final String RESTORE = "css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgRestoreMailbox']";
		public static final String CONFIGURE = "css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgAdvancedTools']";
		public static final String REFRESH = "css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgRefresh']";
		public static final String DELETE_BUTTON = "css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgDelete']";
		public static final String EDIT_BUTTON = "css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgEdit']";
		public static final String RIGHT_CLICK_MENU_DELETE_BUTTON = "css=div[id^='zm__ACLV__MENU_POP'] div[class='ImgDelete']";
		public static final String RIGHT_CLICK_MENU_EDIT_BUTTON = "css=div[id^='zm__ACLV__MENU_POP'] div[class='ImgEdit']";
		public static final String TOOLS_AND_MIGRATION_ICON = "css=div[id='zti__AppAdmin__Home__magHV_textCell']";
		public static final String HOME = "Home";
		public static final String TOOLS_AND_MIGRATION = "Tools and Migration";
		public static final String BACKUPS = "Backups";
		public static final String BACKUP_OPTION = "css=div[id^='zmi__zb_currentApp__UNKNOWN'] td[id^=zmi__zb_currentApp__UNKNOWN]:contains('Backup')";
		public static final String TOOLS_AND_MIGRATION_HEADER = "css=span:contains('Tools and Migration')";
		public static final String VIEW_OPTION = "css=div[id='zmi__zb_currentApp__VIEW']";

	}

	public PageManageBackups(AbsApplication application) {
		super(application);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see projects.admin.pages.AbsTab#isActive()
	 */
	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if (!MyApplication.zIsLoaded())
			throw new HarnessException("Admin Console application is not active!");

		boolean present = sIsElementPresent(Locators.TOOLS_AND_MIGRATION_HEADER);
		if (!present) {
			return (false);
		}

		boolean visible = zIsVisiblePerPosition(Locators.TOOLS_AND_MIGRATION_HEADER, 0, 0);
		if (!visible) {
			logger.debug("isActive() visible = " + visible);
			return (false);
		}

		return (true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see projects.admin.pages.AbsTab#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see projects.admin.pages.AbsTab#navigateTo()
	 */
	@Override
	public void zNavigateTo() throws HarnessException {

		if (zIsActive()) {
			return;
		}

		// Click on Tools and Migration -> Downloads
		sClick(Locators.TOOLS_AND_MIGRATION_ICON);
		zWaitForWorkInProgressDialogInVisible();
		SleepUtil.sleep(5000);
		zWaitForElementPresent(Locators.BACKUP);
		sClickAt(Locators.BACKUP, "");
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

		// Default behavior variables
		String pulldownLocator = null; // If set, this will be expanded
		String optionLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		if (pulldown == Button.B_GEAR_BOX) {
			pulldownLocator = Locators.GEAR_ICON;

			if (option == Button.B_BACKUP) {

				optionLocator = Locators.BACKUP_OPTION;
				page = new WizardBackup(this);

			} else if (option == Button.B_VIEW) {

				optionLocator = Locators.VIEW_OPTION;

			} else {
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

			this.sClickAt(pulldownLocator, "");
			SleepUtil.sleepLong();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}

				this.sClickAt(optionLocator, "");
				SleepUtil.sleepLong();

			}

		}

		// Return the specified page, or null if not set
		return (page);
	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		logger.info(myPageName() + " zListItem(" + action + ", " + item + ")");

		tracer.trace(action + " on subject = " + item);

		AbsPage page = null;
		SleepUtil.sleepSmall();

		// How many items are in the table?
		String rowsLocator = "css=div[id='zl'] div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " Number of backups: " + count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {
			final String backupLocator = rowsLocator + ":nth-child(" + i + ")";
			String locator;

			// Email Address
			locator = backupLocator + " td[id^='backup_server_list_data_host']";

			if (this.sIsElementPresent(locator)) {
				if (this.sGetText(locator).trim().equalsIgnoreCase(item)) {
					if (action == Action.A_LEFTCLICK) {
						sClick(locator);
						break;
					} else if (action == Action.A_DOUBLECLICK) {
						sDoubleClick(locator);
						SleepUtil.sleepSmall();
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

	public boolean zVerifyBackupStatus(String item) throws HarnessException {

		logger.info(myPageName() + " zVerifyBackupStatus(" + item + ")");
		boolean found = false;
		SleepUtil.sleepSmall();

		// How many items are in the table?
		String rowsLocator = "css=div[id='zl__ZaBackupListView'] div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zVerifyBackupStatus: number of backups: " + count);

		// Get each row data from the table list

		final String backupLocator = rowsLocator + ":nth-child(1)";
		String locator;
		locator = backupLocator + " td" + ":nth-child(4)";
		zWaitForElementPresent(locator);
		if (this.sIsElementPresent(locator)) {
			if (this.sGetText(locator).trim().equalsIgnoreCase(item)
					|| this.sGetText(locator).trim().equalsIgnoreCase("Running")) {
				found = true;
			} else {
				logger.info("Backup is Failed!");
			}
		}

		if (found == true) {
			SleepUtil.sleepSmall();
			logger.info("Backup is Running/completed");
			ZAssert.assertTrue(found, "Backup is Running/completed!");
		}
		return found;
	}

	public boolean zVerifyHeader(String header) throws HarnessException {
		if (this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}

	public boolean zVerifyToolsAndMigrationHeader(String header) throws HarnessException {
		if (this.sIsElementPresent("css=div[id='zti__AppAdmin__Home__magHV_textCell']:contains('" + header + "')"))
			return true;
		return false;
	}
}
