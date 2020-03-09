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

import java.awt.event.KeyEvent;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsForm;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class FormEditDistributionList extends AbsForm {

	public static class TreeItem {
		public static final String MEMBERS = "Members";
		public static final String Notes = "Notes";
		public static final String MEMBER_OF = "Member Of";
		public static final String ALIASES = "Aliases";
		public static final String OWNERS = "Owners";
		public static final String PREFERENCES = "Preferences";
		public static final String ACL = "ACL";
	}

	public static class Locators {
		public static final String NAME_TEXT_BOX = "css=input#ztabv__UNDEFINE_";
		public static final String SAVE_BUTTON = "css=td[id^='zb__ZaCurrentAppBar__SAVE']";
		public static final String CLOSE_BUTTON = "css=td[id^='zb__ZaCurrentAppBar__CLOSE']";
		public static final String DYNAMIC_GROUP = "css=input[id$='_dlType']";
		public static final String ADMIN_GROUP = "css=input[id$='zimbraIsAdminGroup']";
		public static final String HIDE_IN_GAL = "css=input[id$='zimbraHideInGal']";
		public static final String SET_REPLY_TO = "css=input[id$='zimbraPrefReplyToEnabled']";
		public static final String REPLY_TO_DISPLAY_NAME = "css=input[id$='zimbraPrefReplyToDisplay']";
		public static final String REPLY_TO_ADDRESS = "css=input[id$='zimbraPrefReplyToAddress']";
		public static final String ALIAS_NAME = "css=input[id^='zdlgv__EDIT_ALIAS']";
		public static final String ALIAS_DOMAIN_NAME = "css=input[id$='_name_3_display']";
		public static final String zdlg_OK = "css=td[id$='_button2_title']:contains('OK')";
		public static final String DirectMemberOf = "css=td[id$='_directMemberList___container'] div[id$='__rows']";
		public static final String IndirectMemberOf = "css=td[id$='_indirectMemberList___container'] div[id$='__rows']";
		public static final String FIND_DL_TEXTBOX = "css=div[id^='ztab__UNDEFINE']:not([aria-hidden=true]) input[id$='query_2']";
		public static final String SEARCH_DL_BUTTON = "css=div[id^='ztabv__UNDEFINE_zatabcase'][style*='visibility: visible'] div table tbody tr td:nth-child(2):contains('Search')";
		public static final String SelectDLRowCSS = "css=div[id^='zl__'][id$='rows'] div[id^='zli_'] td[width='auto']";
		public static final String ADD_DL = "css=div[id^='ztabv__UNDEFINE_zatabcase'][style*='visibility: visible'] div table tbody tr td:nth-child(2):contains('Add')";
	}

	public FormEditDistributionList(AbsApplication application) {
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
		sClickAt(Locators.SAVE_BUTTON, "");
		SleepUtil.sleepMedium();
	}

	public void zSelectTreeItem(String treeItem) throws HarnessException {
		sClickAt("css=div[id^='zti__AppAdmin__Home__dlLstHV'] div[class='ZTreeItemTextCell']:contains('" + treeItem
				+ "')", "");
	}

	public void zSetName(String name) throws HarnessException {
		for (int i = 12; i >= 0; i--) {
			if (sIsElementPresent(Locators.NAME_TEXT_BOX + i + "_name_2")) {
				sType(Locators.NAME_TEXT_BOX + i + "_name_2", name);
				return;
			}
		}
		sType(Locators.NAME_TEXT_BOX + "name_2", name);
		SleepUtil.sleepVerySmall();
	}

	public AbsPage zPropertiesCheckboxSet(Button button, boolean status) throws HarnessException {
		logger.info(myPageName() + " zFeatureCheckboxSet(" + button + ")");
		tracer.trace("Click page button " + button);

		AbsPage page = null;
		String locator = null;

		SleepUtil.sleepSmall();

		if (button == Button.B_DYNAMIC_GROUP) {
			locator = Locators.DYNAMIC_GROUP;

		} else if (button == Button.B_HIDE_IN_GAL) {
			locator = Locators.HIDE_IN_GAL;

		} else if (button == Button.B_ADMIN_GROUP) {
			locator = Locators.ADMIN_GROUP;

		} else {
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

	public AbsPage zPreferencesCheckboxSet(Button button, boolean status) throws HarnessException {
		logger.info(myPageName() + " zFeatureCheckboxSet(" + button + ")");
		tracer.trace("Click page button " + button);

		AbsPage page = null;
		String locator = null;

		SleepUtil.sleepSmall();

		if (button == Button.B_SET_REPLY_TO) {
			locator = Locators.SET_REPLY_TO;

		} else {
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

	public void zSetDisplayNameInReplyToMessage(String name) throws HarnessException {

		if (sIsElementPresent(Locators.REPLY_TO_DISPLAY_NAME)) {
			sType(Locators.REPLY_TO_DISPLAY_NAME, name);
			return;
		}
	}

	public void zSetReplyToAddress(String email) throws HarnessException {
		logger.info(myPageName() + " zSetReplyToAddress(" + email + ")");

		if (!this.sIsElementPresent(Locators.REPLY_TO_ADDRESS)) {
			throw new HarnessException("zSetReplyToAddress " + Locators.REPLY_TO_ADDRESS + " is not present");
		}

		this.sClick(Locators.REPLY_TO_ADDRESS);
		this.sType(Locators.REPLY_TO_ADDRESS, email);
		SleepUtil.sleepSmall();
		this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		SleepUtil.sleepSmall();
	}

	public void zAddDLAliases(String cn, String domain) throws HarnessException {
		logger.info(myPageName() + " zAddAccountAliases(" + cn + ")");
		tracer.trace("Click page button ");
		this.clearField(Locators.ALIAS_NAME);
		sType(Locators.ALIAS_NAME, cn);
		SleepUtil.sleepSmall();
		sClick(Locators.zdlg_OK);
		SleepUtil.sleepSmall();
	}

	public void zAddDLOwner(String cn, String domain) throws HarnessException {
		logger.info(myPageName() + " zAddAccountAliases(" + cn + ")");
		tracer.trace("Click page button ");

		this.clearField(Locators.ALIAS_NAME);
		sType(Locators.ALIAS_NAME, cn);

		SleepUtil.sleepSmall();

		for (int i = 12; i >= 0; i--) {
			if (sIsElementPresent("css=input[id='zdlgv__EDIT_ALIAS_" + i + "_name_3_display']")) {
				sType("css=input[id='zdlgv__EDIT_ALIAS_" + i + "_name_3_display']", "");
				sType("css=input[id='zdlgv__EDIT_ALIAS_" + i + "_name_3_display']", domain);
			} else if (sIsElementPresent("css=input[id='zdlgv__EDIT_ALIAS_name_3_display']")) {
				sType("css=input[id='zdlgv__EDIT_ALIAS_name_3_display']", "");
				sType("css=input[id='zdlgv__EDIT_ALIAS_name_3_display']", domain);
			}
		}
		SleepUtil.sleepSmall();
		sClick(Locators.zdlg_OK);
		SleepUtil.sleepSmall();
	}

	public void zAddDistributionList(com.zimbra.qa.selenium.projects.admin.items.DistributionListItem d)
			throws HarnessException {
		logger.info(myPageName() + " zAddDistributionList(" + d.getEmailAddress() + ")");

		// Enter Dl to search
		this.sType(Locators.FIND_DL_TEXTBOX, d.getEmailAddress());
		this.sClick(Locators.SEARCH_DL_BUTTON);
		SleepUtil.sleepSmall();

		// Select DL and Add it.
		this.sClick(Locators.SelectDLRowCSS + ":contains('" + d.getEmailAddress() + "')");
		SleepUtil.sleepSmall();
		this.sClickAt(Locators.ADD_DL,"");
	}
}