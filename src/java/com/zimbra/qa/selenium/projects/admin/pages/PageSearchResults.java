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
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;

public class PageSearchResults extends AbsTab {

	public static class Locators {
		public static final String SEARCH_INPUT_TEXT_BOX = "_XForm_query_display";
		public static final String SEARCH_BUTTON = "css=td.xform_container div.ImgSearch";
		public static final String DELETE_BUTTON = "css=td[id='zmi__zb_currentApp__DELETE_title']";
		public static final String DELETE_BUTTON_DISABLED = "css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgDelete ZDisabledImage']";
		public static final String RIGHT_CLICK_MENU_DELETE_BUTTON = "css=div[id='zm__SCHLV__MENU_POP'] td[id='zmi__SCHLV__DELETE_title']";
		public static final String RIGHT_CLICK_MENU_DELETE_BUTTON_DISABLED = "css=div[id='zm__SCHLV__MENU_POP'] div[class='ImgDelete ZDisabledImage']";
		public static final String RIGHT_CLICK_MENU_EDIT_BUTTON = "css=div[id='zm__SCHLV__MENU_POP'] td[id='zmi__SCHLV__EDIT_title']";
		public static final String EDIT_BUTTON = "css=td[id='zmi__zb_currentApp__EDIT_title']";
		public static final String GEAR_ICON = "css=div.ImgConfigure";
		public static final String zArrowSelectSearchObject = "css=td[id*='dropdown'] div[class='ImgSelectPullDownArrow']";
		public static final String zCosSearchObject = "css=div[id='zmi__SEARCH_COSES'] td[id='zmi__SEARCH_COSES_title']";
		public static final String zDomainSearchObject = "css=div[id='zmi__SEARCH_DOMAINS'] td[id='zmi__SEARCH_DOMAINS_title']";
		public static final String zAccountsSearchObject = "css=div[id='zmi__SEARCH_ACCOUNTS'] td[id='zmi__SEARCH_ACCOUNTS_title']";
		public static final String zDLSearchObject = "css=div[id='zmi__SEARCH_DLS'] td[id='zmi__SEARCH_DLS_title']";
		public static final String zAliasesSearchObject = "css=div[id='zmi__SEARCH_ALIASES'] td[id='zmi__SEARCH_ALIASES_title']";
		public static final String zResourcesSearchObject = "css=div[id='zmi__SEARCH_RESOURCES'] td[id='zmi__SEARCH_RESOURCES_title']";
		public static final String zSearchTypeDropdown = "css=div[class^='ZaSearchFieldButton']";
	}

	public static class TypeOfObject {
		public static final String ACCOUNT = "Account";
		public static final String ALIAS = "ALIAS";
		public static final String RESOURCE = "RESOURCE";
		public static final String DISTRIBUTION_LIST = "Distribution List";
		public static final String COS = "Cos";
		public static final String DOMAIN = "Domain";
		public static final String DOMAIN_ALIAS = "Domain Alias";
	}

	public String typeOfObject = "";
	public String S_ACCOUNT = "Account";
	public String S_ALIAS = "ALIAS";
	public String S_RESOURCE = "RESOURCE";
	public String S_DISTRIBUTION_LIST = "Distribution List";
	public String S_COS = "Cos";
	public String S_DOMAIN = "Domain";
	public String S_ALL_OBJECTS = "All Objects";

	public String zGetType() {
		return typeOfObject;
	}

	public void zSetType(String type) {
		this.typeOfObject = type;
	}

	public PageSearchResults(AbsApplication application) {
		super(application);
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		throw new HarnessException("implement me");
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zNavigateTo() throws HarnessException {
		throw new HarnessException("implement me");
	}

	/**
	 * Enter text into the query string field
	 *
	 * @param query
	 * @throws HarnessException
	 */
	public void zAddSearchQuery(String query) throws HarnessException {
		logger.info(myPageName() + " zAddSearchQuery(" + query + ")");
		this.sType(Locators.SEARCH_INPUT_TEXT_BOX, query);
	}

	@Override
	public AbsPage zListItem(Action action, String entity) throws HarnessException {
		logger.info(myPageName() + " zListItem(" + action + ", " + entity + ")");

		tracer.trace(action + " on subject = " + entity);

		AbsPage page = null;

		// How many items are in the table?
		String rowsLocator = "css=div#zl__SEARCH_MANAGE div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zListGetAccounts: number of accounts: " + count);

		for (int i = 1; i <= count; i++) {
			final String accountLocator = rowsLocator + ":nth-child(" + i + ")";
			String locator;

			// Email Address
			locator = accountLocator + " td[id^='SEARCH_MANAGE_data_emailaddress']";

			if (this.sIsElementPresent(locator)) {
				if (this.sGetText(locator).trim().equalsIgnoreCase(entity)) {
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

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {

		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Click button " + button);

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		SleepUtil.sleepSmall();
		if (button == Button.B_SEARCH) {

			locator = Locators.SEARCH_BUTTON;
			page = new PageSearchResults(MyApplication);

			if (!this.sIsElementPresent(locator))
				throw new HarnessException("Button is not present locator=" + locator + " button=" + button);

		} else if (button == Button.B_TREE_DELETE) {
			locator = Locators.RIGHT_CLICK_MENU_DELETE_BUTTON;
			page = new DialogForDeleteOperation(this.MyApplication, null);

			if (!this.sIsElementPresent(locator))
				throw new HarnessException("Button is not present locator=" + locator + " button=" + button);

		} else if (button == Button.B_TREE_EDIT) {
			locator = Locators.RIGHT_CLICK_MENU_EDIT_BUTTON;

			if (typeOfObject.equals(TypeOfObject.DISTRIBUTION_LIST))
				page = new FormEditDistributionList(this.MyApplication);
			else if (typeOfObject.equals(TypeOfObject.ACCOUNT))
				page = new FormEditAccount(this.MyApplication);
			else if (typeOfObject.equals(TypeOfObject.RESOURCE))
				page = new FormEditResource(this.MyApplication);
			else if (typeOfObject.equals(TypeOfObject.ALIAS))
				page = new FormEditAccount(this.MyApplication);
			else if (typeOfObject.equals(TypeOfObject.COS))
				page = new FormEditCos(this.MyApplication);
			else if (typeOfObject.equals(TypeOfObject.DOMAIN))
				page = new FormEditDomain(this.MyApplication);
			else if (typeOfObject.equals(TypeOfObject.DOMAIN_ALIAS))
				page = new WizardCreateDomainAlias(this);

			if (!this.sIsElementPresent(locator))
				throw new HarnessException("Button is not present locator=" + locator + " button=" + button);

		} else if (button == Button.B_DELETE) {
			locator = Locators.DELETE_BUTTON;
			page = new DialogForDeleteOperation(this.MyApplication, null);

		} else if (button == Button.B_ADVANCED) {
			locator = "css=div[id^='zti__AppAdmin__Home__actLstHV__2__8_textCell']";
			if (zIsVisiblePerPosition(locator, 10, 10)) {
				locator = "css=div[id^='zti__AppAdmin__Home__actLstHV__2__8_textCell']";
			} else {
				locator = "css=div[id^='zti__AppAdmin__Home__actLstHV__1__8_textCell']";
			}

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		// Click it
		this.sClick(locator);

		if (page != null) {
			SleepUtil.sleepMedium();
		}

		if (button == Button.B_SEARCH) {
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

		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		if (pulldown == Button.B_GEAR_BOX) {

			if (option == Button.O_DELETE) {

				pulldownLocator = Locators.GEAR_ICON;
				optionLocator = Locators.DELETE_BUTTON;

				page = new DialogForDeleteOperation(this.MyApplication, null);

			} else if (option == Button.O_EDIT) {

				pulldownLocator = Locators.GEAR_ICON;
				optionLocator = Locators.EDIT_BUTTON;

				if (typeOfObject.equals(TypeOfObject.DISTRIBUTION_LIST))
					page = new FormEditDistributionList(this.MyApplication);
				else if (typeOfObject.equals(TypeOfObject.ACCOUNT))
					page = new FormEditAccount(this.MyApplication);
				else if (typeOfObject.equals(TypeOfObject.RESOURCE))
					page = new FormEditResource(this.MyApplication);
				else if (typeOfObject.equals(TypeOfObject.ALIAS))
					page = new FormEditAccount(this.MyApplication);
				else if (typeOfObject.equals(TypeOfObject.COS))
					page = new FormEditCos(this.MyApplication);
				else if (typeOfObject.equals(TypeOfObject.DOMAIN))
					page = new FormEditDomain(this.MyApplication);
				else if (typeOfObject.equals(TypeOfObject.DOMAIN_ALIAS))
					page = new WizardCreateDomainAlias(this);
			} else {
				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
			}

		} else if (pulldown == Button.B_SEARCH_TYPE) {

			pulldownLocator = Locators.zSearchTypeDropdown;

			if (option == Button.O_ACCOUNTS) {
				optionLocator = "css=td[id='zmi__SEARCH_ACCOUNTS_left_icon']";
			} else if (option == Button.O_DISTRIBUTION_LISTS) {
				optionLocator = "css=td[id='zmi__SEARCH_DLS_left_icon']";
			} else if (option == Button.O_ALIASES) {
				optionLocator = "css=td[id='zmi__SEARCH_ALIASES_left_icon']";
			} else if (option == Button.O_RESOURCES) {
				optionLocator = "css=td[id='zmi__SEARCH_RESOURCES_left_icon']";
			} else if (option == Button.O_DOMAINS) {
				optionLocator = "css=td[id='zmi__SEARCH_DOMAINS_left_icon']";
			} else if (option == Button.O_CLASS_OF_SERVICE) {
				optionLocator = "css=td[id='zmi__SEARCH_COSES_left_icon']";
			} else if (option == Button.O_DISTRIBUTION_LISTS) {
				optionLocator = "css=td[id='zmi__SEARCH_ALL_left_icon']";
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

			if (optionLocator != null) {

				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}
				this.sClick(optionLocator);
			}
			SleepUtil.sleepMedium();
		}

		return (page);
	}

	/**
	 * Return a list of all accounts in the current view
	 */
	public List<AccountItem> zListGetAccounts() throws HarnessException {

		List<AccountItem> items = new ArrayList<AccountItem>();

		SleepUtil.sleepMedium();

		// How many items are in the table?
		String rowsLocator = "css=div#zl__SEARCH_MANAGE div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		int scrollCounter = 50;

		logger.debug(myPageName() + " zListGetAccounts: number of accounts: " + count);

		if (count >= 50) {

			for (int accountPaging = 1; accountPaging <= 50; accountPaging++) {

				String pageCounter = rowsLocator + ":nth-child(" + scrollCounter + ")";

				if (this.sIsElementPresent(pageCounter)) {
					sClickAt(pageCounter, "");
					SleepUtil.sleepVerySmall();
					this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_DOWN);
					SleepUtil.sleepVerySmall();
					scrollCounter = scrollCounter + 20;
				} else {
					break;
				}
			}
		}

		count = this.sGetCssCount(rowsLocator);

		for (int i = 1; i <= count; i++) {
			final String accountLocator = rowsLocator + ":nth-child(" + i + ")";
			String locator;

			AccountItem item = new AccountItem("email" + ConfigProperties.getUniqueString(),
					ConfigProperties.getStringProperty("testdomain"));

			// Type (image)
			locator = accountLocator + " td[id^='SEARCH_MANAGE_data_type'] div";
			if (this.sIsElementPresent(locator)) {
				item.setGAccountType(this.sGetAttribute(locator + "@class"));
			}

			// Email Address
			locator = accountLocator + " td[id^='SEARCH_MANAGE_data_emailaddress']";
			if (this.sIsElementPresent(locator)) {
				item.setGEmailAddress(this.sGetText(locator).trim());
			}

			// Add the new item to the list
			items.add(item);
			logger.info(item.prettyPrint());
		}

		return (items);
	}

	public boolean zIsPresentInSearchResult(String email) throws HarnessException {
		logger.info(myPageName() + " zIsPresentInSearchResult(" + email + ")");
		String rowlocator = "css=div#zl__SEARCH_MANAGE div[id$='__rows'] div[id^='zli__'] td[id^='SEARCH_MANAGE_data_emailaddress_']";
		return sIsElementPresent(rowlocator + ":contains(" + email + ")");
	}

	public boolean zVerifyDisabled(String buttonID) throws HarnessException {
		if (buttonID == "DeleteContext") {
			boolean test = this.sIsElementPresent(Locators.DELETE_BUTTON_DISABLED);
			return test;
		} else if (buttonID == "DeleteTreeMenu") {
			return this.sIsElementPresent(Locators.RIGHT_CLICK_MENU_DELETE_BUTTON_DISABLED);
		} else {
			return false;
		}
	}

	public void zSelectSearchObject(String object) throws HarnessException {

		if (object == S_COS) {
			this.sClickAt(Locators.zArrowSelectSearchObject, "");
			this.sClickAt(Locators.zCosSearchObject, "");

		} else if (object == S_DOMAIN) {
			this.sClickAt(Locators.zDomainSearchObject, "");

		} else if (object == S_ACCOUNT) {
			this.sClickAt(Locators.zArrowSelectSearchObject, "");
			this.sClickAt(Locators.zAccountsSearchObject, "");

		} else if (object == S_RESOURCE) {
			this.sClickAt(Locators.zResourcesSearchObject, "");

		} else if (object == S_DISTRIBUTION_LIST) {
			SleepUtil.sleepMedium();
			this.sClickAt(Locators.zDLSearchObject, "");

		} else if (object == S_ALIAS) {
			this.sClickAt(Locators.zAliasesSearchObject, "");

		} else {
			throw new HarnessException("Not imeplemented for " + object + "Object");
		}
	}
}
