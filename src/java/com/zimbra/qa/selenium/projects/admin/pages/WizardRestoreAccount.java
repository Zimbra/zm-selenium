/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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

public class WizardRestoreAccount extends AbsWizard {
	
	public static class Locators {
		public static final String USE_RECENT_DATE_TIME_CHECK_BOX = "css=div[style*='z-index: 7'][class='DwtDialog'] input[id$='_attrs/useLast']";
		public static final String FILTER_TEXT_BOX = "css=div[style*='z-index: 7'] input[id^='zdlgv__UNDEFINE_'][id$='_textfield']";
		public static final String NEXT_BUTTON = "css=div[style*='z-index: 7'] td[id$='_button12_title']";
		public static final String FINISH_BUTTON = "css=div[style*='z-index: 7'] td[id$='_button13_title']";
	}

	public WizardRestoreAccount(AbsTab page) {
		super(page);
		logger.info("New " + WizardRestoreAccount.class.getName());
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {
		if (!(item instanceof AccountItem))	throw new HarnessException("item must be an AccountItem, was " + item.getClass().getCanonicalName());

		AccountItem account = (AccountItem) item;
		String email_id = account.getEmailAddress();
		
		sClickAt(Locators.NEXT_BUTTON, "");
		sCheck(Locators.USE_RECENT_DATE_TIME_CHECK_BOX);
		sClickAt(Locators.NEXT_BUTTON, "");
		zType(Locators.FILTER_TEXT_BOX, email_id);
		sClickAt("div[style*='z-index: 7'] td[id$='_zx_list_component___container'] td:contains('"+email_id+"')", "");
		sClickAt(Locators.NEXT_BUTTON, "");
		SleepUtil.sleepSmall();
		sClickAt(Locators.NEXT_BUTTON, "");
		sClickAt(Locators.FINISH_BUTTON, "");

		return (account);
	}

	@Override
	public String myPageName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		// TODO Auto-generated method stub
		return false;
	}
}
