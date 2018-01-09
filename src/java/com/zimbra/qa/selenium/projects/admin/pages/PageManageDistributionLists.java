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
package com.zimbra.qa.selenium.projects.admin.pages;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;

public class PageManageDistributionLists extends AbsTab {

	public static class Locators {
		public static final String MANAGE_ACCOUNTS_ICON = "css=div[class=ImgManageAccounts]";
		public static final String DISTRIBUTION_LISTS = "css=div[id='zti__AppAdmin__Home__dlLstHV_textCell']";
		public static final String GEAR_ICON = "css=div[class=ImgConfigure]";
		public static final String NEW_MENU = "css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgDistributionList']";
		public static final String HOME = "Home";
		public static final String MANAGE = "Manage";
		public static final String DISTRIBUTION_LIST = "Distribution Lists";
		public static final String DELETE_BUTTON = "css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgDelete']";
		public static final String EDIT_BUTTON = "css=td[id='zmi__zb_currentApp__EDIT_title']:contains('Edit')";
		public static final String RIGHT_CLICK_MENU_DELETE_BUTTON = "css=div[id^='zm__ACLV__MENU_POP__'] td[id^='zmi__ACLV__DELETE'][id$='title']";
		public static final String RIGHT_CLICK_MENU_EDIT_BUTTON = "css=div[id^='zm__ACLV__MENU_POP__'] td[id^='zmi__ACLV__EDIT'][id$='title']";
		public static final String REFRESH_BUTTON = "css=div.ImgSearchRefreshWhite";
		public static final String VIEW_RIGHTS_BUTTON = "css=td[id^='zmi__zb_currentApp__UNKNOWN_']:contains('View Rights')";
		public static final String COS_TAB = "css=td[id='ztabv__UNDEFINE_xform_tabbar___container']:contains('Class of Service')";
		public static final String RESOURCES_TAB = "css=td[id='ztabv__UNDEFINE_xform_tabbar___container']:contains('Class of Service')";
		public static final String ACL_TAB = "css=div[id^='zti__AppAdmin__Home__dlLstHV'] div[class='ZTreeItemTextCell']:contains('ACL')";
	}

	public PageManageDistributionLists(AbsApplication application) {
		super(application);
	}

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

		boolean onPage = zIsVisiblePerPosition(Locators.DISTRIBUTION_LISTS, 0, 0);
		if (!onPage) {
			logger.debug("isActive() onPage = " + onPage);
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

		sClickAt(Locators.MANAGE_ACCOUNTS_ICON, "");
		zWaitForWorkInProgressDialogInVisible();
		sIsElementPresent(Locators.DISTRIBUTION_LISTS);
		sClickAt(Locators.DISTRIBUTION_LISTS, "");
		zWaitForWorkInProgressDialogInVisible();
		zWaitForActive();
	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		logger.info(myPageName() + " zListItem(" + action + ", " + item + ")");

		tracer.trace(action + " on subject = " + item);

		AbsPage page = null;
		SleepUtil.sleepSmall();

		// How many items are in the table?
		String rowsLocator = "css=div#zl__DL_MANAGE div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zListGetAccounts: number of accounts: " + count);

		int m = 50;
		if (count >= 50) {
			for (int a1 = 1; a1 <= 5; a1++) {
				String p0 = rowsLocator + ":nth-child(" + m + ")";
				if (this.sIsElementPresent(p0)) {
					sClickAt(p0,"");
					this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_DOWN);
					m = m + 20;
				} else
					break;
			}

		}
		count = this.sGetCssCount(rowsLocator);

		for (int i = 1; i <= count; i++) {
			final String accountLocator = rowsLocator + ":nth-child(" + i + ")";
			String locator;

			// Email Address
			locator = accountLocator + " td[id^='dl_data_emailaddress']";

			if (this.sIsElementPresent(locator)) {
				if (this.sGetText(locator).trim().equalsIgnoreCase(item)) {
					if (action == Action.A_LEFTCLICK) {
						sClick(locator);
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

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException {
		return null;
	}

	public AbsPage zToolbarPressButton(Button button) throws HarnessException {

		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_NEW) {
			locator = Locators.DISTRIBUTION_LISTS;
			page = new WizardCreateDL(this);

		} else if (button == Button.B_TREE_DELETE) {
			locator = Locators.RIGHT_CLICK_MENU_DELETE_BUTTON;
			page = new DialogForDeleteOperation(this.MyApplication, null);

		} else if (button == Button.B_TREE_EDIT) {
			locator = Locators.RIGHT_CLICK_MENU_EDIT_BUTTON;
			page = new FormEditDistributionList(this.MyApplication);

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		this.sClick(locator);

		if (page != null) {
			SleepUtil.sleepMedium();
		}

		return (page);
	}

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

			if (option == Button.O_NEW) {
				optionLocator = Locators.NEW_MENU;
				page = new WizardCreateDL(this);

			} else if (option == Button.O_EDIT) {
				optionLocator = Locators.EDIT_BUTTON;
				page = new FormEditDistributionList(this.MyApplication);

			} else if (option == Button.O_DELETE) {
				optionLocator = Locators.DELETE_BUTTON;
				page = new DialogForDeleteOperation(this.MyApplication, null);

			} else if (option == Button.O_VIEW_RIGHTS) {
				optionLocator = Locators.VIEW_RIGHTS_BUTTON;
				page = new DialogForDeleteOperation(this.MyApplication, null);

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

			SleepUtil.sleepLong();
			this.sClickAt(pulldownLocator, "");
			SleepUtil.sleepLong();

			if (optionLocator != null) {

				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}

				this.sClickAt(optionLocator, "0,0");
				SleepUtil.sleepMedium();
			}
		}

		return (page);
	}

	/**
	 * Return a list of all accounts in the current view
	 */
	public List<AccountItem> zListGetAccounts() throws HarnessException {

		List<AccountItem> items = new ArrayList<AccountItem>();

		if (!this.sIsElementPresent("css=div[id='zl__DL_MANAGE'] div[id$='__rows']"))
			throw new HarnessException("Account Rows is not present");

		// How many items are in the table?
		String rowsLocator = "//div[@id='zl__DL_MANAGE']//div[contains(@id, '__rows')]//div[contains(@id,'zli__')]";
		int count = this.sGetXpathCount(rowsLocator);
		logger.debug(myPageName() + " zListGetAccounts: number of accounts: " + count);

		int m = 50;
		if (count >= 50) {
			for (int a1 = 1; a1 <= 5; a1++) {
				String p0 = rowsLocator + "[" + m + "]";
				if (this.sIsElementPresent(p0)) {
					sClick(p0);
					this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_DOWN);
					m = m + 20;
				} else {
					break;
				}
			}
		}
		count = this.sGetXpathCount(rowsLocator);

		for (int i = 1; i <= count; i++) {
			final String accountLocator = rowsLocator + "[" + i + "]";
			String locator;

			AccountItem item = new AccountItem("email" + ConfigProperties.getUniqueString(),
					ConfigProperties.getStringProperty("testdomain"));

			// Type (image)
			locator = accountLocator + "//td[contains(@id, 'dl_data_type_')]//div";
			if (this.sIsElementPresent(locator)) {
				item.setGAccountType(this.sGetAttribute("xpath=(" + locator + ")@class"));
			}

			// Email Address
			locator = accountLocator + "//td[contains(@id, 'dl_data_emailaddress_')]";
			if (this.sIsElementPresent(locator)) {
				item.setGEmailAddress(this.sGetText(locator).trim());
			}

			// Add the new item to the list
			items.add(item);
			logger.info(item.prettyPrint());
		}

		return (items);
	}

	public boolean zVerifyMailTab(String header) throws HarnessException {
		if (this.sIsElementPresent(
				"css=td[id='ztabv__UNDEFINE_xform_tabbar___container'] div[id$='_tabbar'] div[id$='_items'] td:nth-child(2):contains('"
						+ header + "')"))
			return true;
		return false;
	}

	public boolean zVerifyResourceTab() throws HarnessException {
		if (this.sIsElementPresent(
				"css=td[id='ztabv__UNDEFINE_xform_tabbar___container'] div[id$='_tabbar'] div[id$='_items'] td:nth-child(3):contains('Resources')"))
			return true;
		return false;
	}

	public boolean zVerifyTab(String header) throws HarnessException {
		for (int i = 0; i <= 10; i++) {
			if (this.sIsElementPresent(
					"css=td[id='ztabv__UNDEFINE_xform_tabbar___container'] div[id$='_tabbar'] div[id$='_items'] td:nth-child("
							+ i + "):contains('" + header + "')"))
				return true;
		}
		return false;
	}

	public boolean zVerifyHeader(String header) throws HarnessException {
		if (this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}
}