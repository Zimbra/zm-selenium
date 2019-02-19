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

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class PageZextrasHSM extends AbsTab {

	public static class Locators {
		public static final String NETWORK_MODULE_NG_ICON = "css=td[id^='zti__AppAdmin__Home__Zimbra Network'] div[class='ImgZeXtras']";
		public static final String HSM_TAB = "css=td[id^='zti__AppAdmin__Home__Zimbra Network']:contains('HSM')";
		public static final String CLOSE_BUTTON = "css=td[id^='zb__ZaCurrentAppBar__CLOSE_']:contains('Close')";
		public static final String SAVE_BUTTON = "css=td[id^='zb__ZaCurrentAppBar__SAVE_']:contains('Save')";
		public static final String APPLY_HSM_POLICY_NOW_BUTTON = "css=td[class='ZWidgetTitle']:contains('Apply HSM Policy Now')";
		public static final String SECONDARY_VOLUME_ADD_BUTTON = "css=div[id='ztabv__ZxPowerstore_group_10'] td[class='ZWidgetTitle']:contains('Add')";
		public static final String SECONDARY_VOLUME_EDIT_BUTTON = "css=div[id='ztabv__ZxPowerstore_zawiz_top_grouper_2'] td[class='ZWidgetTitle']:contains('Edit')";
		public static final String SECONDARY_VOLUME_DELETE_BUTTON = "css=div[id='ztabv__ZxPowerstore_zawiz_top_grouper_2'] td[class='ZWidgetTitle']:contains('Delete')";
		public static final String HSM_POLICY_ADD_BUTTON = "css=div[id='ztabv__ZxPowerstore_group_16'] td[class='ZWidgetTitle']:contains('Add')";
		public static final String HSM_POLICY_EDIT_BUTTON = "css=div[id='ztabv__ZxPowerstore_group_16'] td[class='ZWidgetTitle']:contains('Edit')";
		public static final String HSM_POLICY_DELETE_BUTTON = "css=div[id='ztabv__ZxPowerstore_group_16'] td[class='ZWidgetTitle']:contains('Delete')";
		public static final String UpdatedPolicyInList = "css=div[id^='ztabv__ZxPowerstore_group_'] td[id='ztabv__ZxPowerstore_hsmList___container']";
		public static final String APPLY_HSM_POLICY_NOW_DIALOG = "css=div.DwtDialog[style*='display: block;'] table td:contains('Apply Storage Management Policy NOW!')";
		public static final String APPLY_HSM_POLICY_YES_BUTTON = "css=div.DwtDialog[style*='z-index: 7'][role='alertdialog'] td[class='ZWidgetTitle']:contains('Yes')";
		public static final String APPLY_HSM_POLICY_NO_BUTTON = "css=div.DwtDialog[style*='z-index: 7'][role='alertdialog'] td[class='ZWidgetTitle']:contains('No')";
		public static final String SECONDARY_VOLUME = "css=div[id='ztabv__ZxPowerstore_zawiz_top_grouper_2'] div.Row td";
		public static final String HSM_POLICY = "css=div[id^='ztabv__ZxPowerstore_group_'] td[id='ztabv__ZxPowerstore_hsmList___container'] div.Row td";
	}

	public PageZextrasHSM(AbsApplication application) {
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

		if (zIsActive()) {
			return;
		}

		//SleepUtil.sleepLong();
		if(zWaitForElementPresent(Locators.NETWORK_MODULE_NG_ICON)) {
		sClickAt(Locators.NETWORK_MODULE_NG_ICON, "");
		}
		zWaitForWorkInProgressDialogInVisible();
		sClickAt(Locators.HSM_TAB, "");
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

	public void zSave() throws HarnessException {
		SleepUtil.sleepSmall();
		sClickAt(Locators.SAVE_BUTTON, "");
		SleepUtil.sleepLong();
	}

	public AbsPage zAddSecondaryVolume() throws HarnessException {
		AbsPage page = null;
		SleepUtil.sleepVerySmall();
		page = new WizardAddNewVolume(this);
		this.sClickAt(Locators.SECONDARY_VOLUME_ADD_BUTTON, "");
		return (page);
	}

	public AbsPage zEditSecondaryVolume(String volumeName) throws HarnessException {
		AbsPage page = null;
		SleepUtil.sleepSmall();
		this.sClickAt(Locators.SECONDARY_VOLUME + ":contains('" + volumeName + "')", "");
		page = new WizardEditHSMVolume(this);
		this.sClickAt(Locators.SECONDARY_VOLUME_EDIT_BUTTON, "");
		return (page);
	}

	public AbsPage zAddHSMPolicy() throws HarnessException {
		AbsPage page = null;
		SleepUtil.sleepSmall();
		page = new WizardHSMPolicy(this);
		this.sClickAt(Locators.HSM_POLICY_ADD_BUTTON, "");
		SleepUtil.sleepVerySmall();
		return (page);
	}

	public AbsPage zEditHSMPolicy(String hsmPolicy) throws HarnessException {
		AbsPage page = null;
		SleepUtil.sleepSmall();
		this.sClickAt(Locators.HSM_POLICY + ":contains('" + hsmPolicy + "')", "");
		page = new WizardHSMPolicy(this);
		this.sClickAt(Locators.HSM_POLICY_EDIT_BUTTON, "");
		SleepUtil.sleepSmall();
		return (page);
	}

	public AbsPage zApplyHSMPolicy() throws HarnessException {
		DialogForZextrasOperationID operationIdDialog = new DialogForZextrasOperationID(MyApplication,null);
		this.sClickAt(Locators.APPLY_HSM_POLICY_NOW_BUTTON, "");
		if(zWaitForElementPresent(Locators.APPLY_HSM_POLICY_NOW_DIALOG)) {
			sClick(Locators.APPLY_HSM_POLICY_YES_BUTTON);
			if(operationIdDialog.zIsMonitorCommandPresent()){
				operationIdDialog.zPressButton(Button.B_OK);
			} else {
				throw new HarnessException("Operation id not appered");
			}
		} else {
			throw new HarnessException("Apply policy confirmation dialog is not appeared");
		}
		return null;
	}

	public Boolean zIsSecondaryVolumeExists(String volumeName) throws HarnessException {
		SleepUtil.sleepSmall();
		return (this.sIsElementPresent("css=div.secondaryVolumes div.DwtListView-Rows div:contains('"+ volumeName +"')"));
	}

	public Boolean zIsCurrentVolume(String volumeName) throws HarnessException {
		SleepUtil.sleepSmall();
		return (this.sIsElementPresent("xpath=//div[contains(text(),'Secondary Volumes')]/parent::div//td[contains(text(),'"
						+ volumeName + "')]/preceding-sibling::td[2]/div[@class='ImgCheck']"));
	}

	public String zGetUpdatedHSMPolicyInList() throws HarnessException {
		return (sGetText(Locators.UpdatedPolicyInList));
	}
}