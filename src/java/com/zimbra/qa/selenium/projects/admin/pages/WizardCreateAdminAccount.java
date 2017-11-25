/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;

public class WizardCreateAdminAccount extends AbsWizard {

	public static class Locators {
		public static final String zdlg_NEW_ACCT = "zdlg__NEW_ACCT";
		public static final String zdlg_ACCT_NAME = "zdlgv__NEW_ADMIN_name_2";
		public static final String zdlg_DOMAIN_NAME = "zdlgv__NEW_ADMIN_name_3_display";
		public static final String zdlg_DL_NAME = "zdlgv__NEW_ADMIN_name_5";
		public static final String zdlg_DL_DOMAIN_NAME = "zdlgv__NEW_ADMIN_name_6_display";
		public static final String zdlg_LAST_NAME = "zdlgv__NEW_ACCT_sn";
		public static final String zdlg_OK = "zdlg__MSG_button2_title";
		public static final String ADMIN_TYPE = "css=div[id$='zdlgv__NEW_ADMIN'] div.ImgSelectPullDownArrow";
		public static final String ADMIN_USER = "css=div[id$='new_admin_type_choice_1']";
		public static final String ADMIN_GROUP = "css=div[id$='new_admin_type_choice_0']";
		public static final String GLOBAL_ADMIN_CHECK_BOX = "zdlgv__NEW_ADMIN_";
		public static final String zdlg_PASSWORD = "css=input[id='zdlgv__NEW_ADMIN_password']";
		public static final String zdlg_CONFIRM_PASSWORD = "css=input[id='zdlgv__NEW_ADMIN_confirmPassword']";
		public static final String InformationDialog = "css=tr[id^='zdlg__MSG__'] td[id$='_title']:contains('Informational')";
		public static final String InformationDialogOkBtn = "css=div[id^='zdlg__MSG__'][id$='_buttons'] td[id^='OK'] td[id$='_title']";
	}

	public WizardCreateAdminAccount(AbsTab page) {
		super(page);
		logger.info("New " + WizardCreateAdminAccount.class.getName());
	}

	public String adminType = "";
	public static boolean IsGlobalAdmin = false;

	public static boolean isGlobalAdmin() {
		return IsGlobalAdmin;
	}

	public void setGlobalAdmin(boolean isGlobalAdmin) throws HarnessException {
		IsGlobalAdmin = isGlobalAdmin;
		if (IsGlobalAdmin) {
			for (int i = 10; i >= 1; i--) {
				if (sIsElementPresent(Locators.GLOBAL_ADMIN_CHECK_BOX + i + "_zimbraIsAdminAccount")) {
					sCheck(Locators.GLOBAL_ADMIN_CHECK_BOX + i + "_zimbraIsAdminAccount");
					return;
				}
			}
			sCheck(Locators.GLOBAL_ADMIN_CHECK_BOX + "zimbraIsAdminAccount");
		}
	}

	public String getAdminType() {
		return adminType;
	}

	public void setAdminType(String adminType) throws HarnessException {
		if (adminType != "") {
			sClick(Locators.ADMIN_TYPE);
			if (adminType.equals(Locators.ADMIN_USER)) {
				adminType = Locators.ADMIN_USER;
			} else if (adminType.equals(Locators.ADMIN_GROUP)) {
				adminType = Locators.ADMIN_GROUP;
			}
			sClick(adminType);
			clickNext(AbsWizard.Locators.ADMIN_DIALOG);
		}
		this.adminType = adminType;
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {

		if (!(item instanceof AccountItem))
			throw new HarnessException("item must be an AccountItem, was " + item.getClass().getCanonicalName());

		AccountItem account = (AccountItem) item;

		String CN = account.getLocalName();
		String domain = account.getDomainName();

		if (adminType.equals(Locators.ADMIN_USER)) {

			zType(Locators.zdlg_ACCT_NAME, CN);
			this.clearField(Locators.zdlg_DOMAIN_NAME);
			zType(Locators.zdlg_DOMAIN_NAME, domain);
			zType(Locators.zdlg_PASSWORD, "test123");
			zType(Locators.zdlg_CONFIRM_PASSWORD, "test123");
			SleepUtil.sleepSmall();
			clickNext(AbsWizard.Locators.ADMIN_DIALOG);
			clickFinish(AbsWizard.Locators.ADMIN_DIALOG);
			SleepUtil.sleepSmall();

		} else {
			zType(Locators.zdlg_DL_NAME, CN);
			SleepUtil.sleepSmall();
			this.clearField(Locators.zdlg_DL_DOMAIN_NAME);
			zType(Locators.zdlg_DL_DOMAIN_NAME, domain);
			clickNext(AbsWizard.Locators.ADMIN_DIALOG);
			clickFinish(AbsWizard.Locators.ADMIN_DIALOG);
		}
		return (account);
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		boolean present = sIsElementPresent(Locators.zdlg_NEW_ACCT);
		if (!present) {
			return (false);
		}

		boolean visible = this.zIsVisiblePerPosition(Locators.zdlg_NEW_ACCT, 0, 0);
		if (!visible) {
			return (false);
		}

		return (true);
	}

	@Override
	public String myPageName() {
		return null;
	}
}