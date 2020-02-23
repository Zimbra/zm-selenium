/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.openqa.selenium.JavascriptExecutor;
import com.zimbra.qa.selenium.framework.core.ExecuteHarnessMain;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;

public class WizardCreateAccount extends AbsWizard {

	public static class Locators {
		public static final String zdlg_NEW_ACCT = "zdlg__NEW_ACCT";
		public static final String zdlg_ACCT_NAME = "zdlgv__NEW_ACCT_name_2";
		public static final String zdlg_DOMAIN_NAME = "zdlgv__NEW_ACCT_name_3_display";
		public static final String zdlg_LAST_NAME = "zdlgv__NEW_ACCT_sn";
		public static final String zdlg_DESCRIPTION = "zdlgv__NEW_ACCT_description";
		public static final String zdlg_OK = "zdlg__MSG_button2_title";
		public static final String server_AUTO_CHECKBOX = "css=input[id='zdlgv__NEW_ACCT_automailserver']";
		public static final String server_PULL_DOWN = "div[id^='zdlgv__NEW_ACCT'][id*='_zimbraMailHost'] div[class='ImgSelectPullDownArrow']";
	}

	public WizardCreateAccount(AbsTab page) {
		super(page);
		logger.info("New " + WizardCreateAccount.class.getName());
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {

		if (!(item instanceof AccountItem))
			throw new HarnessException("item must be an AccountItem, was " + item.getClass().getCanonicalName());

		AccountItem account = (AccountItem) item;

		String CN = account.getLocalName();
		String domain = account.getDomainName();

		zType(Locators.zdlg_ACCT_NAME, CN);
		this.clearField(Locators.zdlg_DOMAIN_NAME);
		zType(Locators.zdlg_DOMAIN_NAME, "");
		zType(Locators.zdlg_DOMAIN_NAME, domain);
		SleepUtil.sleepLong();

		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);

		for (String key : account.getAccountAttrs().keySet()) {
			if (key.equals("sn")) {
				zType(Locators.zdlg_LAST_NAME, account.getAccountAttrs().get(key));
				continue;
			} else if (key.equals("description")) {
				zType(Locators.zdlg_DESCRIPTION, account.getAccountAttrs().get(key));
				continue;
			} 
			throw new HarnessException("Unknown account attribute key " + key);
		}

		if (ExecuteHarnessMain.totalZimbraServers > 1) {
			if (sIsVisible(Locators.server_AUTO_CHECKBOX)) {

				// Scroll down to select server from dropdown
				((JavascriptExecutor) webDriver())
						.executeScript("var objdiv=document.getElementsByClassName('ZaXWizardDialogPageDiv')[0];"
								+ "var obj = document.getElementById('zdlgv__NEW_ACCT_automailserver');"
								+ "objdiv.scrollTo(0,obj.getBoundingClientRect().y - objdiv.getBoundingClientRect().y);");

				sUncheck(Locators.server_AUTO_CHECKBOX);
				sClick(Locators.server_PULL_DOWN);
				SleepUtil.sleepSmall();
				String storeServer = ExecuteHarnessMain.storeServers.get(0);
				ZAssert.assertEquals(sGetCssCount("div[id^='zdlgv__NEW_ACCT_zimbraMailHost_choice_']"),
						ExecuteHarnessMain.storeServers.size(), "Verify number of store server listed");
				sClick("css=div[id^='zdlgv__NEW_ACCT_zimbraMailHost_choice_']:contains('" + storeServer + "')");
			}
		}
		clickFinish(AbsWizard.Locators.ACCOUNT_DIALOG);
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

	public boolean zCloseWizard() throws HarnessException {
		this.sClickAt("css=td[id$='zdlg__NEW_ACCT_button1_title']", "");
		return true;
	}
}