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
public class PageManageGlobalSettings extends AbsTab {

	public static class Locators {
		public static final String CONFIGURE_ICON = "css=div.ImgAdministration";
		public static final String GLOBAL_SETTING = "css=div[id='zti__AppAdmin__CONFIGURATION__GSET_textCell']";
		public static final String GEAR_ICON = "css=div.ImgConfigure";
		public static final String HOME = "Home";
		public static final String CONFIGURE = "Configure";
		public static final String GLOBAL_SETTINGS = "Global Settings";
		public static final String GLOBAL_SETTINGS_ACL = "css=div[id^='zti__AppAdmin__CONFIGURATION__GSET__'][class='ZTreeItemTextCell']:contains('ACL')";
		public static final String GLOBALS_SETTINGS_ACL_ADD = "css=td[id^='ztabv__GSET_EDIT_dwt_button'] td[id$='title']:contains('Add')";
		public static final String GLOBALS_SETTINGS_ACL_GRANTEE_NAME = "css=div[class='DwtDialog WindowOuterContainer'] table[class='dynselect_table'] input[id^='zdlgv__EDIT_ACLundefined_'][id$='_grantee_email_display']";
		public static final String GENERAL_INFORMATION = "css=div[id^='zti__AppAdmin__CONFIGURATION__GSET'] div[class='ZTreeItemTextCell']:contains('General Information')";
		public static final String ZIMBRA_FILE_UPLOAD_MAX_SIZE = "css=td[id='ztabv__GSET_EDIT_zimbraFileUploadMaxSize___label']";
		public static final String ZIMBRA_FILE_UPLOAD_MAX_SIZE_TEXT_FIELD = "css=input[id='ztabv__GSET_EDIT_zimbraFileUploadMaxSize']";
		public static final String WORK_IN_PROGRESS_DIALOG = "css=div[id^='ztabv__TabContent_output_']:contains('Work')";
	}

	public PageManageGlobalSettings(AbsApplication application) {
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
			// This page is already active.
			return;
		}

		// Click on Addresses -> Accounts
		sClick(Locators.CONFIGURE_ICON);
		zWaitForWorkInProgressDialogInVisible();
		SleepUtil.sleepMedium();
		zWaitForElementPresent(Locators.GLOBAL_SETTING);
		sClick(Locators.GLOBAL_SETTING);
		zWaitForWorkInProgressDialogInVisible();
		zWaitForActive();
	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		return null;
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
		return null;
	}

	public boolean zVerifyHeader(String header) throws HarnessException {
		if (this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}

}
