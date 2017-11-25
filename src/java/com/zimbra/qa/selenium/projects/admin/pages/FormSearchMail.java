/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsForm;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class FormSearchMail extends AbsForm {

	public static class TreeItem {
		public static final String GENERAL_INFORMATION = "General Information";
	}

	public static class Locators {
		public static final String QUERY_TEXT_BOX = "css=input#_XForm_";
		public static final String SAVE_BUTTON = "css=td[id^='zb__ZaCurrentAppBar__SAVE']";
		public static final String CLOSE_BUTTON = "css=td[id^='zb__ZaCurrentAppBar__CLOSE']";
		public static final String SERVER_NAME_ARROW = "css=div[id$='_serverId_arrow_button']";
		public static final String SERVER_NAME = "css=div#_XForm_";
		public static final String TARGET_MAILBOX = "css=input#_XForm_";
		public static final String ALL_ACCOUNTS = "css=input#_XForm_";
		public static final String SELECTED_ACCOUNTS = "css=input#_XForm_";
		public static final String RUN_SEARCH = "css=div[id^='zb__ZaCurrentAppBar']:contains('Run Search')";
		public static final String ACCOUNT_NAME = "css=input[id='_XForm_2_accountQuery']";
		public static final String FIND_ACCOUNTS = "css=td[id$='_dwt_button_3___container'] div:contains('Find accounts')";
		public static final String ADD = "css=td[id$='dwt_button_5___container'] div:contains('Add')";
		public static final String SELECTED_ACCOUNTS1 = "css=input[id='_XForm_2_searchAll_2']";
	}

	public FormSearchMail(AbsApplication application) {
		super(application);
		logger.info("new " + myPageName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if (!MyApplication.zIsLoaded())
			throw new HarnessException("Admin Console application is not active!");

		boolean present = sIsElementPresent("");
		if (!present) {
			return (false);
		}

		String attrs = sGetAttribute("");
		if (!attrs.contains("ZSelected")) {
			return (false);
		}

		return (true);
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zFill(IItem item) throws HarnessException {
	}

	@Override
	public void zSubmit() throws HarnessException {
		sClickAt(Locators.RUN_SEARCH, "");
		SleepUtil.sleepSmall();
	}

	public void zSelectTreeItem(String treeItem) throws HarnessException {
		sClickAt("css=td:contains('" + treeItem + "')", "");
	}

	public void zSetSearchQuery(String Query) throws HarnessException {
		for (int i = 12; i >= 0; i--) {
			if (sIsElementPresent(Locators.QUERY_TEXT_BOX + i + "_query_2")) {
				sType(Locators.QUERY_TEXT_BOX + i + "_query_2", Query);
				return;
			}
		}
		sType(Locators.QUERY_TEXT_BOX + "_query_2", Query);
	}

	public void zSelectServerName(String serverName) throws HarnessException {
		this.sClickAt(Locators.SERVER_NAME_ARROW, "");
		for (int i = 12; i >= 0; i--) {
			if (sIsElementPresent(Locators.SERVER_NAME + i + "_serverId_choice_0")) {
				sClick(Locators.SERVER_NAME + i + "_serverId_choice_0");
				SleepUtil.sleepMedium();
				return;
			}
		}
		sClick(Locators.SERVER_NAME + "_serverId_choice_0");
	}

	public void zSetTargetMailbox(String emailAddress) throws HarnessException {
		for (int i = 12; i >= 0; i--) {
			if (sIsElementPresent(Locators.TARGET_MAILBOX + i + "_targetMbx_2_display")) {
				sType(Locators.TARGET_MAILBOX + i + "_targetMbx_2_display", emailAddress);
				SleepUtil.sleepMedium();
				return;
			}
		}

		sType(Locators.TARGET_MAILBOX + "_targetMbx_2_display", emailAddress);
		SleepUtil.sleepMedium();
	}

	public void zSelectAccountsToSearch(String criteria) throws HarnessException {
		if (criteria.equals("all")) {
			for (int i = 12; i >= 0; i--) {
				if (sIsElementPresent(Locators.ALL_ACCOUNTS + i + "_searchAll")) {
					sClick(Locators.ALL_ACCOUNTS + i + "_searchAll");
					return;
				}
			}
			sClick(Locators.ALL_ACCOUNTS + "_searchAll");
		} else if (criteria.equals("selected")) {
			sClick(Locators.SELECTED_ACCOUNTS1);
		}
	}

	public void zSearchInSelectedAccount(String email) throws HarnessException {

		sType(Locators.ACCOUNT_NAME, email);
		SleepUtil.sleepMedium();

		sClick(Locators.FIND_ACCOUNTS);
		SleepUtil.sleepMedium();

		// How many items are in the table?
		String rowsLocator = "css=td[id$='_accountPool___container'] div[id^='zl'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zListGetPolicy: number of policys: " + count);

		for (int i = 1; i <= count; i++) {
			final String accountLocator = rowsLocator + ":nth-child(" + i + ")";
			String locator;

			// Email Address
			locator = accountLocator + " td:nth-child(2)";

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