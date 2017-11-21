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
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;

public class FormManageRetentionPolicy extends AbsForm {

	public static class TreeItem {
		public static final String GENERAL_INFORMATION = "General Information";
	}

	public static class Locators {
		public static final String NAME_TEXT_BOX = "css=input[id$='_name']";
		public static final String RETENTION_RANGE_TEXT_BOX = "css=input[id='zdlgv__UNDEFINE1_lifetime_2']";
		public static final String SAVE_BUTTON = "css=td[id^='zb__ZaCurrentAppBar__SAVE']";
		public static final String CLOSE_BUTTON = "css=td[id$='zb__ZaCurrentAppBar__CLOSE']";
		public static final String Pull_DOWN = "css=div[class='ImgSelectPullDownArrow']";
		public static final String PULL_DOWN_CLOSE = "css=td[id='zmi__zb_currentApp__CLOSE_title']";
		public static final String PASSWORD = "css=input[id$='ztabv__ACCT_EDIT_password']";
		public static final String CONFIRM_PASSWORD = "css=input[id$='ztabv__ACCT_EDIT_confirmPassword']";
		public static final String OK_BUTTON = "css=td[id^='zdlg__UNDEFINE']:contains('OK')";
	}

	public FormManageRetentionPolicy(AbsApplication application) {
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
		sClick(Locators.Pull_DOWN);
		sClick(Locators.PULL_DOWN_CLOSE);

	}

	public void zSelectTreeItem(String treeItem) throws HarnessException {
		sClickAt("css=td:contains('" + treeItem + "')", "");
	}

	public void setPolicyName(String name) throws HarnessException {
		sType(Locators.NAME_TEXT_BOX, name);
	}

	public void setRetentionRange(String name) throws HarnessException {
		sType(Locators.RETENTION_RANGE_TEXT_BOX, name);
	}

	public void setPassword(String password) throws HarnessException {
		sType(Locators.PASSWORD, password);
		zType(Locators.CONFIRM_PASSWORD, password);

	}

	public AbsPage selectRetentionRange(Button option) throws HarnessException {
		logger.info(myPageName() + " selectRetentionRange" + option);

		tracer.trace("Click pulldown " + option);

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		String optionLocator = null;
		String pulldownLocator = "zdlgv__UNDEFINE1_lifetime_3_display";
		AbsPage page = null;

		if (option == Button.O_DAYS) {
			optionLocator = "css=div[id='zdlgv__UNDEFINE1_lifetime_3_choice_0']";
		} else if (option == Button.O_WEEKS) {
			optionLocator = "css=div[id='zdlgv__UNDEFINE1_lifetime_3_choice_1']";
		} else if (option == Button.O_MONTHS) {
			optionLocator = "css=div[id='zdlgv__UNDEFINE1_lifetime_3_choice_2']";
		} else if (option == Button.O_YEARS) {
			optionLocator = "css=div[id='zdlgv__UNDEFINE1_lifetime_3_choice_3']";
		} else {
			throw new HarnessException("no logic defined for pulldown/option " + option);
		}
		// Make sure the locator exists
		if (!this.sIsElementPresent(pulldownLocator)) {
			throw new HarnessException(" option " + option + " optionLocator " + pulldownLocator + " not present!");
		}

		this.sClickAt(pulldownLocator, "");

		if (optionLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(optionLocator)) {
				throw new HarnessException(" option " + option + " optionLocator " + optionLocator + " not present!");
			}

			this.sClickAt(optionLocator, "");
		}

		return (page);
	}

	public void sClickOkButton() throws HarnessException {
		if (!this.sIsElementPresent(Locators.OK_BUTTON)) {
			throw new HarnessException("Locator" + Locators.OK_BUTTON + " not present!");
		}

		sClickAt(Locators.OK_BUTTON, "");
	}

}
