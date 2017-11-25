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

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.pages.briefcase.DialogUploadFile;

/**
 * @author Matt Rhoades
 *
 */
public class PageManageRetentionPolicy extends AbsTab {

	public static class Locators {
		public static final String CONFIGURE_ICON = "css=div.ImgAdministration";
		public static final String GLOBAL_SETTING = "zti__AppAdmin__CONFIGURATION__GSET_textCell";
		public static final String GEAR_ICON = "css=div.ImgConfigure";
		public static final String HOME = "Home";
		public static final String CONFIGURE = "Configure";
		public static final String GLOBAL_SETTINGS = "Global Settings";
		public static final String GLOBAL_SETTINGS_RETENTION_POLICY = "css=div[id^='zti__AppAdmin__CONFIGURATION__GSET'] div[class='ZTreeItemTextCell']:contains('Retention Policy')";
		public static final String ADD_BUTTON = "css=td[id$=_title]:contains('Add')";
		public static final String EDIT_BUTTON = "css= table [id='ztabv__GSET_EDIT_global_form_keep_p_group_table'] tbody tr:nth-child(2) td div table tbody table[class='grid_xform_table'] tbody tr td:nth-child(3) div";
		public static final String DELETE_BUTTON = "css= table [id='ztabv__GSET_EDIT_global_form_keep_p_group_table'] tbody tr:nth-child(2) td div table tbody table[class='grid_xform_table'] tbody tr td:nth-child(1) div";
		public static final String GENERAL_INFORMATION = "css=div[id^='zti__AppAdmin__CONFIGURATION__GSET'] div[class='ZTreeItemTextCell']:contains('General Information')";
		public static final String UPDATE_LICENSE = "css=div[id^='zmi__zb_currentApp__']:contains('Update License')";
		public static final String UPLOAD_LICENSE = "css=input[name='licenseFile']";
		public static final String NEXT_BUTTON = "css=td[id$='_button12_title']";
		public static final String INSTALL_COMMERCIAL_CERTIFICATE = "css=input[id='zdlgv__UNDEFINE_comm']";
		public static final String zRetentionRangeDropdown = "css=div[class^='oselect_display']";
		public static final String YES_BUTTON = "css=td[id='zdlg__MSG__CTR_DELETE_RETENTION_POLICY_ConfirmMessage_button5_title']";
		public static final String NO_BUTTON = "css=td[id='zdlg__MSG__CTR_DELETE_RETENTION_POLICY_ConfirmMessage_button4_title']";
	}

	public PageManageRetentionPolicy(AbsApplication application) {
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
		sIsElementPresent(Locators.GLOBAL_SETTING);
		sClickAt(Locators.GLOBAL_SETTING, "");
		zWaitForWorkInProgressDialogInVisible();
		sClickAt(Locators.GLOBAL_SETTINGS_RETENTION_POLICY, "");
		zWaitForWorkInProgressDialogInVisible();
		zWaitForActive();
	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		logger.info(myPageName() + " zListItem(" + action + ", " + item + ")");

		tracer.trace(action + " on subject = " + item);

		AbsPage page = null;
		SleepUtil.sleepMedium();

		// How many items are in the table?
		String rowsLocator = "css=td[id='ztabv__GSET_EDIT_retentionPolicyKeep___container'] div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zListGetPolicy: number of policys: " + count);

		for (int i = 1; i <= count; i++) {
			final String accountLocator = rowsLocator + ":nth-child(" + i + ")";
			String locator;

			// Email Address
			locator = accountLocator + " td";

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

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {

		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_ADD) {
			locator = Locators.ADD_BUTTON;
			page = new FormManageRetentionPolicy(this.MyApplication);

		} else if (button == Button.B_DELETE) {
			locator = Locators.DELETE_BUTTON;
			page = new DialogForDeleteOperation(this.MyApplication, null);

		} else if (button == Button.B_EDIT) {
			locator = Locators.EDIT_BUTTON;
			page = new FormManageRetentionPolicy(this.MyApplication);

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		this.sClickAt(locator, "");

		if (page != null) {
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
			pulldownLocator = Locators.GEAR_ICON;

			if (option == Button.B_UPDATE_LICENSE) {
				optionLocator = Locators.UPDATE_LICENSE;
				page = new WizardUpdateLicense(this);

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
			SleepUtil.sleepMedium();

			if (optionLocator != null) {

				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}

				this.sClickAt(optionLocator, "");
			}
		}
		return (page);
	}

	public AbsPage zToolbarPressButton(Button button, IItem item) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_UPLOAD_LICENSE) {
			locator = Locators.UPLOAD_LICENSE;
			page = new DialogUploadFile(MyApplication, this);

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		this.sClickAt(locator, "");

		if (page != null) {
			SleepUtil.sleepMedium();
		}

		sMouseOut(locator);
		return (page);
	}

	public boolean zVerifyHeader(String header) throws HarnessException {
		if (this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}

	public boolean zVerifyPolicyName(String item) throws HarnessException {

		logger.info(myPageName() + " zVerifyPolicyName(" + item + ")");
		boolean found = false;
		SleepUtil.sleepMedium();

		// How many items are in the table?
		String rowsLocator = "css=td[id='ztabv__GSET_EDIT_retentionPolicyKeep___container'] div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zVerifyPolicyName: number of rows: " + count);

		// Get each row data from the table list
		for (int i = 1; i <= count; i++) {
			final String accountLocator = rowsLocator + ":nth-child(" + i + ")";
			String locator;
			locator = accountLocator + " td";

			if (this.sIsElementPresent(locator)) {
				if (this.sGetText(locator).trim().equalsIgnoreCase(item)) {
					found = true;
					break;
				} else {
					logger.info("Policy not displayed in current view");
				}
			}

			if (found == true) {
				SleepUtil.sleepSmall();
				logger.info("Policy displayed in current view");
				ZAssert.assertTrue(found, "Policy displayed in current view");
				break;
			}
		}

		return found;
	}

	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Click dialog button " + button);
		if (button == null)
			throw new HarnessException("button cannot be null");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_YES) {
			locator = Locators.YES_BUTTON;

		} else if (button == Button.B_NO) {
			locator = Locators.NO_BUTTON;

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		this.sFocus(locator);
		this.sClickAt(locator, "");
		SleepUtil.sleepLong();

		return (page);
	}
}