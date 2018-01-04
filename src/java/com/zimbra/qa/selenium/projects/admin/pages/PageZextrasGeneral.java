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

import org.openqa.selenium.WebElement;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

/**
 * Admin Console -> Network Modules NG -> General
 *
 * @author Swapnil Pingle
 *
 */
public class PageZextrasGeneral extends AbsTab {

	public static class Locators {
		public static final String NETWORK_MODULE_NG_ICON = "css=td[id^='zti__AppAdmin__Home__ZeXtras_'] div[class='ImgZeXtras']";
		public static final String CLOSE_BUTTON = "css=td[id^='zb__ZaCurrentAppBar__CLOSE_']:contains('Close')";
		public static final String HOME = "Home";
		public static final String NETWORK_MODULE_NG = "Network Modules NG";
		public static final String GENERAL = "General";
		public static final String GENERAL_TAB = "css=td[id^='zti__AppAdmin__ZeXtras__ZxCore_']:contains('General')";
		public static final String BACKUP_TAB = "css=td[id^='zti__AppAdmin__ZeXtras__ZxBackup_']:contains('Backup')";
		public static final String HSM_TAB = "css=td[id^='zti__AppAdmin__ZeXtras__ZxPowerstore_']:contains('HSM')";
		public static final String MOBILE_TAB = "css=td[id^='zti__AppAdmin__ZeXtras__ZxMobile_']:contains('Mobile')";
		public static final String YES_BUTTON = "css=div[class='DwtDialog']:not([aria-hidden='true']) td[class='ZWidgetTitle']:contains('Yes')";
		public static final String BACKUP_ALERT = "css=div#ztabv__ZxBackup_zx_alert_2";
		public static final String MOBILE_ALERT = "css=div#ztabv__ZxMobile_zx_alert_2";
		public static final String HSM_ALERT = "css=div#ztabv__ZxPowerstore_zx_alert_2";

	}

	public PageZextrasGeneral(AbsApplication application) {
		super(application);

		logger.info("new " + myPageName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if (!MyApplication.zIsLoaded())
			throw new HarnessException("Admin Console application is not active!");

		boolean present = sIsElementPresent(Locators.CLOSE_BUTTON);
		if (!present) {
			return (false);
		}

		boolean visible = zIsVisiblePerPosition(Locators.CLOSE_BUTTON, 0, 0);
		if (!visible) {
			logger.debug("isActive() visible = " + visible);
			return (false);
		}
		return (true);
	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if (zIsActive() && zVerifyHeader(PageZextrasGeneral.Locators.GENERAL)) {
			return;
		}

		SleepUtil.sleepLong();
		sClickAt(Locators.NETWORK_MODULE_NG_ICON, "");
		zWaitForWorkInProgressDialogInVisible();
		sClickAt(Locators.GENERAL_TAB, "");
		zWaitForWorkInProgressDialogInVisible();
	}

	@Override
	public AbsPage zListItem(Action action, String accountEmailAddress) throws HarnessException {

		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String accountEmailAddress) throws HarnessException {
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

	public boolean zVerifyModuleStatus(String module) throws HarnessException {
		WebElement we = this.getElement(
				"xpath=//table[@id='ztabv__ZxCore_top_grouper_table']//td[@class='xform_label'][contains(text(),'"
						+ module + "')]/following-sibling::td[1]");
		if (we.getText().contains("Running")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean zVerifyModuleLicenseStatus(String module) throws HarnessException {
		WebElement we = this.getElement(
				"xpath=//table[@id='ztabv__ZxCore_grouper_table']//td[@class='xform_label'][contains(text(),'" + module
						+ "')]/following-sibling::td[1]");
		if (we.getText().contains("Licensed")) {
			return true;
		} else {
			return false;
		}
	}

	public void zEnableDisableModule(String module, String action) throws HarnessException {
		WebElement we = this.getElement(
				"//table[@id='ztabv__ZxCore_top_grouper_table']//td[@class='xform_label'][contains(text(),'" + module
						+ "')]/following-sibling::td[2]//td[@class='ZWidgetTitle'][contains(text(),'" + action + "')]");
		we.click();
		SleepUtil.sleepLong();
		if (action.equals("Stop")) {
			sClick(Locators.YES_BUTTON);
		}
	}

	public boolean zVerifyModuleRunningStatus(String module) throws HarnessException {
		String locator = "";
		String tab = "";
		if (module.equals("Backup")) {
			locator = Locators.BACKUP_ALERT;
			tab = Locators.BACKUP_TAB;
		} else if (module.equals("Mobile")) {
			locator = Locators.MOBILE_ALERT;
			tab = Locators.MOBILE_TAB;
		} else {
			locator = Locators.HSM_ALERT;
			tab = Locators.HSM_TAB;
		}

		sClick(tab);
		SleepUtil.sleepLong();
		WebElement we = getElement(locator);
		return we.getText().contains("The " + module + " module is not running.");
	}

}