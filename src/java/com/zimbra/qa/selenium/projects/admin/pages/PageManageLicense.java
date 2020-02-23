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
import com.zimbra.qa.selenium.projects.ajax.pages.briefcase.DialogUploadFile;

/**
 * @author Matt Rhoades
 *
 */
public class PageManageLicense extends AbsTab {

	public static class Locators {
		public static final String CONFIGURE_ICON = "css=div.ImgAdministration";
		public static final String GLOBAL_SETTING = "css=div[id='zti__AppAdmin__CONFIGURATION__GSET_textCell']";
		public static final String GEAR_ICON = "css=div.ImgConfigure";
		public static final String HOME = "Home";
		public static final String CONFIGURE = "Configure";
		public static final String GLOBAL_SETTINGS = "Global Settings";
		public static final String GLOBAL_SETTINGS_LICENSE = "css=td[id^='zti__AppAdmin__CONFIGURATION__GSET__'] div[id$='_textCell']:contains('Licen')";
		public static final String GENERAL_INFORMATION = "css=div[id^='zti__AppAdmin__CONFIGURATION__GSET'] div[class='ZTreeItemTextCell']:contains('General Information')";
		public static final String UPDATE_LICENSE = "css=div[id^='zmi__zb_currentApp__']:contains('Update Licen')";
		public static final String ACTIVATE_LICENSE = "css=div[id^='zmi__zb_currentApp__']:contains('Activate Licen')";
		public static final String UPLOAD_LICENSE = "css=input[name='licenseFile']";
		public static final String NEXT_BUTTON = "css=td[id$='_button12_title']";
		public static final String INSTALL_COMMERCIAL_CERTIFICATE = "css=input[id='zdlgv__UNDEFINE_comm']";
	}

	public PageManageLicense(AbsApplication application) {
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

		sClick(Locators.CONFIGURE_ICON);
		zWaitForWorkInProgressDialogInVisible();
		SleepUtil.sleepMedium();
		sClick(Locators.GLOBAL_SETTING);
		zWaitForWorkInProgressDialogInVisible();
		sClick(Locators.GLOBAL_SETTINGS_LICENSE);
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

			} else if (option == Button.B_ACTIVATE_LICENSE) {

				optionLocator = Locators.ACTIVATE_LICENSE;
				page = new DialogForActivateLicense(this.MyApplication, null);

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
			SleepUtil.sleepSmall();

			if (optionLocator != null) {

				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}

				this.sClickAt(optionLocator, "");
			}
		}

		SleepUtil.sleepMedium();

		return (page);
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
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

		return (page);
	}

	public boolean zVerifyHeader(String header) throws HarnessException {
		if (this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}

	public boolean zVerifyCompanyName(String Value) throws HarnessException {
		if (this.sIsElementPresent(
				"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase_'] tr:nth-child(4) td:contains('Company name:')")
				&& this.sIsElementPresent(
						"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(4) td:nth-child(2) div:contains('"
								+ Value + "')"))
			return true;
		return false;
	}

	public boolean zVerifyLicenseType(String Value) throws HarnessException {
		if (this.sIsElementPresent(
				"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(5) td:contains('License type:')")
				&& this.sIsElementPresent(
						"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(5) td:nth-child(2) div:contains('"
								+ Value + "')"))
			return true;
		return false;
	}

	public boolean zVerifyLicenseID(String Value) throws HarnessException {
		if (this.sIsElementPresent(
				"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(6) td:contains('License ID:')")
				&& this.sIsElementPresent(
						"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(6) td:nth-child(2) div:contains('"
								+ Value + "')"))
			return true;
		return false;
	}

	public boolean zVerifyIssueDateLabel() throws HarnessException {
		if (this.sIsElementPresent(
				"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(7) td:contains('Issue date:')"))
			return true;
		return false;
	}

	public boolean zVerifyEffectiveDateLabel() throws HarnessException {
		if (this.sIsElementPresent(
				"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(8) td:contains('Effective date:')"))
			return true;
		return false;
	}

	public boolean zVerifyExpirationDateLabel() throws HarnessException {
		if (this.sIsElementPresent(
				"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(9) td:contains('Expiration date:')"))
			return true;
		return false;
	}

	public boolean zVerifyAccountLimit(String Value) throws HarnessException {

		if (this.sIsElementPresent(
				"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(11) td:contains('Accounts limit:')")
				&& this.sIsElementPresent(
						"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(11) td:nth-child(2) div:contains('"
								+ Value + "')"))
			return true;
		return false;
	}

	public boolean zVerifyMobileAccountLimit(String Value) throws HarnessException {

		if (this.sIsElementPresent(
				"css=div[id^='ztabv__GSET_EDIT_zatabcase] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(12) td:contains('Mobile accounts limit:')")
				&& this.sIsElementPresent(
						"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(12) td:nth-child(2) div:contains('"
								+ Value + "')"))
			return true;
		return false;
	}

	public boolean zVerifyMapiAccountLimit(String Value) throws HarnessException {

		if (this.sIsElementPresent(
				"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(13) td:contains('MAPI accounts limit:')")
				&& this.sIsElementPresent(
						"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(13) td:nth-child(2) div:contains('"
								+ Value + "')"))
			return true;
		return false;
	}

	public boolean zVerifyArchivingAccountLimit(String Value) throws HarnessException {

		if (this.sIsElementPresent(
				"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(14) td:contains('Archiving accounts limit:')")
				&& this.sIsElementPresent(
						"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(14) td:nth-child(2) div:contains('"
								+ Value + "')"))
			return true;
		return false;
	}

	public boolean zVerifyIndexingAccountLimit(String Value) throws HarnessException {

		if (this.sIsElementPresent(
				"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(15) td:contains('Attachment indexing accounts limit:')")
				&& this.sIsElementPresent(
						"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(15) td:nth-child(2) div:contains('"
								+ Value + "')"))
			return true;
		return false;
	}

	public boolean zVerifySMIMEAccountLimit(String Value) throws HarnessException {

		if (this.sIsElementPresent(
				"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(16) td:contains('SMIME accounts limit:')")
				&& this.sIsElementPresent(
						"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(16) td:nth-child(2) div:contains('"
								+ Value + "')"))
			return true;
		return false;
	}

	public boolean zVerifyVoiceAccountLimit(String Value) throws HarnessException {

		if (this.sIsElementPresent(
				"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(17) td:contains('Voice accounts limit:')")
				&& this.sIsElementPresent(
						"css=div[id^='ztabv__GSET_EDIT_zatabcase'] table[id^='ztabv__GSET_EDIT_zatabcase'] tr:nth-child(17) td:nth-child(2) div:contains('"
								+ Value + "')"))
			return true;
		return false;
	}
}