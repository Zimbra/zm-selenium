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
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditAccount.Locators;

/**
 * Admin Console -> Network Modules NG -> Backup
 *
 * @author Shubham Gupta
 *
 */
public class PageZextrasBackup extends AbsTab {

	public static class Locators {
		public static final String NETWORK_MODULE_NG_ICON = "css=td[id^='zti__AppAdmin__Home__ZeXtras_'] div[class='ImgZeXtras']";
		public static final String BACKUP_TAB = "css=td[id^='zti__AppAdmin__ZeXtras__ZxBackup_']:contains('Backup')";
		public static final String UNINITIALIZED_ALERT_MSG = "css=td.DwtAlertContent";
		public static final String CLOSE_BUTTON = "css=td[id^='zb__ZaCurrentAppBar__CLOSE_']:contains('Close')";
		public static final String SAVE_BUTTON = "css=td[id^='zb__ZaCurrentAppBar__SAVE_']:contains('Save')";
		public static final String BACKUP_PATH = "css=input[id='ztabv__ZxBackup_attrs/properties/ZxBackup_DestPath']";
		public static final String INITIALIZE_NOW_BUTTON = "css=td[class='ZWidgetTitle']:contains('Initialize NOW!')";
		public static final String RESTORE_DELETED_ACCOUNT_BUTTON = "td[class='ZWidgetTitle']:contains('Restore Deleted Account')";
	}

	public PageZextrasBackup(AbsApplication application) {
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

		SleepUtil.sleepLong();
		sClickAt(Locators.NETWORK_MODULE_NG_ICON, "");
		zWaitForWorkInProgressDialogInVisible();
		sClickAt(Locators.BACKUP_TAB, "");
		zWaitForWorkInProgressDialogInVisible();
	}

	public void zNavigateTo(String treeItem) {

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
	
	/*public List<AccountItem> zListGetAccounts() throws HarnessException {

		return null;
	}*/

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
	
	public AbsPage initiallizeBackup() throws HarnessException{
		AbsPage page = null;
		SleepUtil.sleepVerySmall();
		page = new DialogRunSmartScanConfirmation(MyApplication, null);
		this.sClickAt(Locators.INITIALIZE_NOW_BUTTON, "");
		return (page);
	}
	
	public AbsPage RestoreDeletedAccount() throws HarnessException{
		AbsPage page = null;
		SleepUtil.sleepVerySmall();
		page = new WizardRestoreAccount(this);
		this.sClickAt(Locators.RESTORE_DELETED_ACCOUNT_BUTTON, "");
		return (page);
	}

	/*public AbsPage zPreferenceCheckboxSet(Button button, boolean status) throws HarnessException {
		return null;
	}*/
}