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

import org.openqa.selenium.WebElement;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsForm;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class FormEditAccount extends AbsForm {

	public static class TreeItem {
		public static final String GENERAL_INFORMATION = "General Information";
	}

	public static class Locators {
		public static final String NAME_TEXT_BOX = "css=input#ztabv__ACCT_EDIT_";
		public static final String DA_NAME_TEXT_BOX = "css=input[id='ztabv__ACCT_EDIT_name_2']";
		public static final String SAVE_BUTTON = "css=td[id^='zb__ZaCurrentAppBar__SAVE']";
		public static final String CLOSE_BUTTON = "css=td[id$='zb__ZaCurrentAppBar__CLOSE']";
		public static final String Pull_DOWN = "css=div[aria-controls='zm__zb_currentApp__MENU_POP'] div[class='ImgSelectPullDownArrow']";
		public static final String PULL_DOWN_CLOSE = "css=td[id='zmi__zb_currentApp__CLOSE_title']";
		public static final String PASSWORD = "css=input[id$='ztabv__ACCT_EDIT_password']";
		public static final String CONFIRM_PASSWORD = "css=input[id$='ztabv__ACCT_EDIT_confirmPassword']";
		public static final String zdlg_OK = "css=td[id$='_button2_title']:contains('OK')";
		public static final String MAIL = "css=input[id^='ztabv__ACCT_EDIT_zimbraFeatureMailEnabled']";
		public static final String CALENDAR = "css=input[id^='ztabv__ACCT_EDIT_zimbraFeatureCalendarEnabled']";
		public static final String SHOW_SEARCH_STRINGS = "css=input[id$='zimbraPrefShowSearchString_2']";
		public static final String SHOW_IMAP_SEARCH_FOLDERS = "css=input[id$='zimbraPrefImapSearchFoldersEnabled_2']";
		public static final String ALIAS_NAME = "css=input[id^='zdlgv__EDIT_ALIAS']";
		public static final String ALIAS_DOMAIN_NAME = "css=input[id$='_name_3_display']";
		public static final String ENABLE_MOBILE_SYNC = "css=input[id$='zimbraFeatureMobileSyncEnabled_2']";
		public static final String ENABLE_MOBILE_POLICY = "css=input[id$='zimbraFeatureMobilePolicyEnabled_2']";
		public static final String ENABLE_ARCHIVING = "css=td[id^='ztabv__ACCT_EDIT_archiving_enable_disable_button']:contains('Enable archiving')";
		public static final String DISABLE_ARCHIVING = "css=td[id^='ztabv__ACCT_EDIT_archiving_enable_disable_button']:contains('Disable archiving')";
		public static final String YES_LABEL = "css=td[id$='zimbraArchiveEnabled___container'] div[id$='zimbraArchiveEnabled']:contains('Yes')";
		public static final String NO_LABEL = "css=td[id$='zimbraArchiveEnabled___container'] div[id$='zimbraArchiveEnabled']:contains('No')";
		public static final String LIMIT_ZIMLETS_RADIO = "css=td[id$='zimbraZimletAvailableZimlets_3___container'] input";
		public static final String LIMIT_THEME_RADIO = "css=td[id$='_zimbraAvailableSkin_3___container'] input";
		public static final String THEME_Pull_DOWN = "css=div[id^='zdlgv__EDIT_ACL'][id*='zimbraPrefSkin_2_arrow_button'] div[class='ImgSelectPullDownArrow']";
		public static final String Account_Quota = "css=div[class='DwtTabView']:not([aria-hidden=true]) input[id*='zimbraMailQuota']";
		public static final String ENABLE_MOBILE_SYNC_ZEXTRAS = "css=input[id*='zimbraFeatureMobileSyncEnabled']";
		public static final String B_ENABLE_MOBILE_DEVICE_MANAGEMENT = "css=input[id$='zimbraFeatureMobilePolicyEnabled_2']";
		public static final String B_ALLOW_NON_PROVISIONABLE_DEVICES = "css=input[id$='zimbraMobilePolicyAllowNonProvisionableDevices_2']";
		public static final String B_ALLOW_PARTIAL_POLICY_ENFORCEMENT = "css=input[id$='zimbraMobilePolicyAllowPartialProvisioning_2']";

	}

	public FormEditAccount(AbsApplication application) {
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
		SleepUtil.sleepSmall();
		sClickAt(Locators.SAVE_BUTTON, "");
		SleepUtil.sleepLong();
		sClick(Locators.Pull_DOWN);
		SleepUtil.sleepSmall();
		sClick(Locators.PULL_DOWN_CLOSE);
		SleepUtil.sleepSmall();
	}

	public void zSave() throws HarnessException {
		SleepUtil.sleepSmall();
		sClickAt(Locators.SAVE_BUTTON, "");
		SleepUtil.sleepLong();
	}

	public void zSelectTreeItem(String treeItem) throws HarnessException {
		sClickAt("css=td:contains('" + treeItem + "')", "");
	}

	public void zSetName(String name) throws HarnessException {
		for (int i = 12; i >= 0; i--) {
			if (sIsElementPresent(Locators.NAME_TEXT_BOX + i + "_name_3")) {
				sType(Locators.NAME_TEXT_BOX + i + "_name_3", name);
				SleepUtil.sleepSmall();
				return;
			}
		}
		sType(Locators.NAME_TEXT_BOX + "name_3", name);
		SleepUtil.sleepSmall();
	}

	public void zSetNameAsDelegatedAdmin(String name) throws HarnessException {
		sType(Locators.DA_NAME_TEXT_BOX, name);
	}

	public void zSetPassword(String password) throws HarnessException {
		sType(Locators.PASSWORD, password);
		zType(Locators.CONFIRM_PASSWORD, password);
		SleepUtil.sleepSmall();
	}

	public void zSubmitChangePassword() throws HarnessException {
		sClick(Locators.zdlg_OK);
	}

	public void zEnableArchieving() throws HarnessException {
		sClick(Locators.ENABLE_ARCHIVING);
		SleepUtil.sleepMedium();
	}

	public void zDisableArchieving() throws HarnessException {
		sClick(Locators.ENABLE_ARCHIVING);
	}

	public AbsPage zFeatureCheckboxSet(Button button, boolean status) throws HarnessException {
		logger.info(myPageName() + " zFeatureCheckboxSet(" + button + ")");
		tracer.trace("Click page button " + button);

		AbsPage page = null;
		String locator = null;

		SleepUtil.sleepSmall();

		if (button == Button.B_MAIL) {
			locator = Locators.MAIL;

		} else if (button == Button.B_CALENDAR) {
			locator = Locators.CALENDAR;

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		if (this.sIsChecked(locator) == status) {
			logger.debug("checkbox status matched. not doing anything");
			return (page);
		}

		if (status == true) {
			this.sClickAt(locator, "");

		} else {
			this.sUncheck(locator);
		}

		SleepUtil.sleepSmall();
		return (page);
	}


	public Boolean zFeatureEnable() throws HarnessException {
		return !zIsElementDisabled(Locators.MAIL);
	}

	public void zAddAccountQuota(String accountquota) throws HarnessException{
		if(sIsElementPresent(Locators.Account_Quota)) {
			sType(Locators.Account_Quota, accountquota);
		} else {
			throw new HarnessException("Account Quota input is not present");
		}
	}

	public AbsPage zPreferencesCheckboxSet(Button button, boolean status) throws HarnessException {
		logger.info(myPageName() + " zFeatureCheckboxSet(" + button + ")");
		tracer.trace("Click page button " + button);

		AbsPage page = null;
		String locator = null;

		SleepUtil.sleepSmall();

		if (button == Button.B_SHOW_SEARCH_STRINGS) {
			locator = Locators.SHOW_SEARCH_STRINGS;

		} else if (button == Button.B_SHOW_IMAP_SEARCH_FOLDERS) {
			locator = Locators.SHOW_IMAP_SEARCH_FOLDERS;

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		if (this.sIsChecked(locator) == status) {
			logger.debug("checkbox status matched. not doing anything");
			return (page);
		}

		if (status == true) {
			this.sClickAt(locator, "");

		} else {
			this.sUncheck(locator);
		}

		SleepUtil.sleepSmall();
		return (page);
	}

	public void zAddAccountAliases(String cn, String domain) throws HarnessException {
		logger.info(myPageName() + " zAddAccountAliases(" + cn + ")");
		tracer.trace("Click page button ");

		this.clearField(Locators.ALIAS_NAME);
		sType(Locators.ALIAS_NAME, cn);

		SleepUtil.sleepSmall();

		this.clearField(Locators.ALIAS_DOMAIN_NAME);
		sType(Locators.ALIAS_DOMAIN_NAME, "");
		SleepUtil.sleepSmall();
		zType(Locators.ALIAS_DOMAIN_NAME, domain);
		SleepUtil.sleepSmall();
		sClick(Locators.zdlg_OK);
		SleepUtil.sleepSmall();
	}

	public AbsPage zSetMobileAccess(Button button, boolean status) throws HarnessException {
		logger.info(myPageName() + " zSetMobileAccess(" + button + ")");
		tracer.trace("Click page button " + button);

		AbsPage page = null;
		String locator = null;

		SleepUtil.sleepSmall();

		if (button == Button.B_ENABLE_MOBILE_SYNC) {
			locator = Locators.ENABLE_MOBILE_SYNC;

		} else if (button == Button.B_ENABLE_MOBILE_POLICY) {
			locator = Locators.ENABLE_MOBILE_POLICY;

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		if (this.sIsChecked(locator) == status) {
			logger.debug("checkbox status matched. not doing anything");
			return (page);
		}

		if (status == true) {
			this.sClickAt(locator, "");

		} else {
			this.sUncheck(locator);
		}

		SleepUtil.sleepSmall();
		return (page);
	}

	public void zLimitZimlets(String zimlet) throws HarnessException {
		logger.info(myPageName() + " zLimitZimlets(" + zimlet + ")");

		SleepUtil.sleepSmall();
		sClick(Locators.LIMIT_ZIMLETS_RADIO);
		SleepUtil.sleepSmall();
		sUncheck("css=td[id$='zimbraZimletAvailableZimlets_4___container'] div table tbody tr:contains('" + zimlet
				+ "') td input");
		SleepUtil.sleepSmall();
	}

	public void zLimitThemes(String theme) throws HarnessException {
		logger.info(myPageName() + " zLimitThemes(" + theme + ")");

		SleepUtil.sleepSmall();
		sClick(Locators.LIMIT_THEME_RADIO);
		SleepUtil.sleepSmall();
		sCheck("css=td[id$='zimbraAvailableSkin_4___container'] div table tbody tr:contains('" + theme + "') input");
		SleepUtil.sleepSmall();
	}

	public void zSetCurrentUITheme(String theme) throws HarnessException {
		logger.info(myPageName() + " zSetCurrentUITheme(" + theme + ")");

		SleepUtil.sleepSmall();
		sCheck("css=td[id$='zimbraAvailableSkin_4___container'] div table tbody tr:contains('" + theme + "') input");
		SleepUtil.sleepSmall();
	}

	public AbsPage zSetMobilePreferences(Button button, boolean status) throws HarnessException {
		logger.info(myPageName() + " zSetMobileAccess(" + button + ")");
		tracer.trace("Click page button " + button);

		AbsPage page = null;
		String locator = null;

		SleepUtil.sleepSmall();

		if (button == Button.B_ENABLE_MOBILE_SYNC_ZEXTRAS) {
			locator = Locators.ENABLE_MOBILE_SYNC_ZEXTRAS;
		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		if (this.sIsChecked(locator) == status) {
			logger.debug("checkbox status matched. not doing anything");
			return (page);
		}

		if (status == true) {
			this.sClickAt(locator, "");

		} else {
			this.sUncheck(locator);
		}

		SleepUtil.sleepSmall();
		return (page);
	}

	public boolean zVerifyMobileProvisioning(Button button, String status) throws HarnessException {
		logger.info(myPageName() + " zSetMobileAccess(" + button + ")");
		tracer.trace("Click page button " + button);

		String locator = null;
		boolean result;

		if (button == Button.B_ENABLE_MOBILE_DEVICE_MANAGEMENT) {
			locator = Locators.B_ENABLE_MOBILE_DEVICE_MANAGEMENT;
		} else if (button == Button.B_ALLOW_NON_PROVISIONABLE_DEVICES) {
			locator = Locators.B_ALLOW_NON_PROVISIONABLE_DEVICES;
		} else if (button == Button.B_ALLOW_PARTIAL_POLICY_ENFORCEMENT) {
			locator = Locators.B_ALLOW_PARTIAL_POLICY_ENFORCEMENT;
		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		WebElement we = getElement(locator);
		SleepUtil.sleepSmall();

		if (!status.equals("Enabled")) {
			result = we.getAttribute("disabled").equals("");
		} else {
			result = we.isEnabled();
		}

		return result;
	}
}