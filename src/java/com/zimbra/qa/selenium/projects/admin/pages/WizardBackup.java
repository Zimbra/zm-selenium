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
package com.zimbra.qa.selenium.projects.admin.pages;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.admin.items.BackupItem;

/**
 * @author Pallavi Khairnar
 *
 */
public class WizardBackup extends AbsWizard {

	public static class Locators {
		public static final String NEXT_BUTTON = "css=td[id$='_button12_title']:contains('Next')";
		public static final String BACKUP_BUTTON = "css=td[id$='_button12_title']:contains('Backup')";
		public static final String FINISH_BUTTON = "css=td[id$='_button13_title']:contains('Finish')";
		public static final String BACKUP_METHOD_FULL = "css=div[id='zdlgv__UNDEFINE_method_choice_0']";
		public static final String BACKUP_METHOD_INCREMENTAL = "css=div[id='zdlgv__UNDEFINE_method_choice_1']";
		public static final String EMAIL = "css=input[id$='zdlgv__UNDEFINE_query']";
		public static final String SEARCH_BUTTON = "css=td[id$='_dwt_button___container'] div:contains('Search')";
		public static final String ADD = "css=td[id$='dwt_button_3___container'] div:contains('Add')";
		public static final String BACKUP_ALL = "css=div[class='DwtDialog']:not([aria-hidden='true']) input[id$='_backupAll']";
		public static final String PULL_DOWN = "css=div[id='zdlgv__UNDEFINE_method_arrow_button']";
	}

	public WizardBackup(AbsTab page) {
		super(page);
	}

	public String backupMethod = "";

	public String getBackupMethod() {
		return backupMethod;
	}

	public void zSetBackupMethod(String backupMethod) throws HarnessException {

		if (backupMethod.equals(Locators.BACKUP_METHOD_FULL)) {
			backupMethod = Locators.BACKUP_METHOD_FULL;
			sClickAt(Locators.NEXT_BUTTON, "");
		} else if (backupMethod.equals("INCREMENTAL")) {
			backupMethod = Locators.BACKUP_METHOD_INCREMENTAL;
		}
		this.backupMethod = backupMethod;
	}

	public void zSearchInSelectedAccount(String email) throws HarnessException {

		sType(Locators.EMAIL, email);
		SleepUtil.sleepMedium();

		sClick(Locators.SEARCH_BUTTON);
		SleepUtil.sleepMedium();

		// How many items are in the table?
		String rowsLocator = "css=td[id$='_accountPool___container'] div[id^='zl'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zListGetPolicy: number of policys: " + count);

		for (int i = 1; i <= count; i++) {
			final String backupLocator = rowsLocator + ":nth-child(" + i + ")";
			String locator;

			// Email Address
			locator = backupLocator + " td:nth-child(2)";

			if (this.sIsElementPresent(locator)) {
				if (this.sGetText(locator).trim().equalsIgnoreCase(email)) {
					sClick(locator);
					SleepUtil.sleepMedium();
				} else {
					logger.info("Element is not present!");
				}
			}

			sClick(Locators.ADD);
			SleepUtil.sleepMedium();
		}
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {
		if (!(item instanceof BackupItem))
			throw new HarnessException("item must be an BackupItem, was " + item.getClass().getCanonicalName());

		BackupItem backup = (BackupItem) item;

		if (backupMethod.equals(Locators.BACKUP_METHOD_FULL)) {
			sClickAt(Locators.BACKUP_BUTTON, "");
			sClickAt(Locators.FINISH_BUTTON, "");

		} else {
			sClickAt(Locators.PULL_DOWN, "");
			sClickAt(Locators.BACKUP_METHOD_INCREMENTAL, "");
			sClickAt(Locators.NEXT_BUTTON, "");
			sClickAt(Locators.FINISH_BUTTON, "");
		}

		return (backup);
	}

	public void zSelectAccountsToBackup(String criteria, String email) throws HarnessException {
		if (criteria.equals("all")) {
			sClick(Locators.BACKUP_ALL);

		} else if (criteria.equals("selected")) {
			sType(Locators.EMAIL, email);
			SleepUtil.sleepMedium();

			sClick(Locators.SEARCH_BUTTON);
			SleepUtil.sleepMedium();

			// How many items are in the table?
			String rowsLocator = "css=td[id$='_accountPool___container'] div[id^='zl'] div[id^='zli__']";
			int count = this.sGetCssCount(rowsLocator);
			logger.debug(myPageName() + " zListGetPolicy: number of policys: " + count);

			for (int i = 1; i <= count; i++) {
				final String backupLocator = rowsLocator + ":nth-child(" + i + ")";
				String locator;
				locator = backupLocator + " td:nth-child(2)";
				if (this.sIsElementPresent(locator)) {
					if (this.sGetText(locator).trim().equalsIgnoreCase(email)) {
						sClick(locator);
						SleepUtil.sleepMedium();
					} else {
						logger.info("Element is not present!");
					}
				}
				sClick(Locators.ADD);
				SleepUtil.sleepMedium();
			}
		}
	}

	@Override
	public String myPageName() {
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		return false;
	}

	public boolean zCloseWizard() throws HarnessException {
		this.sClickAt("css=td[id$='zdlg__UNDEFINE_button1_title']", "");
		return true;
	}
}