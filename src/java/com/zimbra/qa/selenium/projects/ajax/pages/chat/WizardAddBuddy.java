/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.pages.chat;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

/**
 * @author Mitesh Savani
 *
 */
public class WizardAddBuddy extends AbsWizard {

	public static class Locators {
		public static final String BUDDY_EMAIL_Address = "css=div[class='DwtDialog']:not([aria-hidden='true']) input[name='add_buddy_dialog_email_prop']";
		public static final String BUDDY_NICKNAME = "css=div[class='DwtDialog']:not([aria-hidden='true']) input[name='add_buddy_dialog_nickname_prop']";
		public static final String Dialog_OKbutton = "css=div[class='DwtDialog']:not([aria-hidden='true']) td[class='ZWidgetTitle']:contains('OK')";
		public static final String BUDDY_Dialog = "css=div[class='DwtDialog']:not([aria-hidden='true']) td[class='DwtDialogTitle']:contains('Add Buddy')";
	}

	public WizardAddBuddy(AbsTab page) {
		super(page);
	}

	public IItem zCompleteWizard(IItem item) throws HarnessException {

		if (!(item instanceof BuddyItem))
			throw new HarnessException("item must be a BuddyItem, was " + item.getClass().getCanonicalName());

		BuddyItem buddy = (BuddyItem) item;

		if (buddy.getEmailAddress() != null) {
			SleepUtil.sleepVerySmall();
			sType(Locators.BUDDY_EMAIL_Address, buddy.getEmailAddress());
			SleepUtil.sleepMedium();
			sClick("css=div[class='ZmAutocompleteListView']:not([aria-hidden='true']) table tbody tr");
		}

		if (buddy.getNickName() != null) {
			sType(Locators.BUDDY_NICKNAME, buddy.getNickName());
		}

		sClick(Locators.Dialog_OKbutton);
		SleepUtil.sleepMedium();

		return item;
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.BUDDY_Dialog;

		if (!this.sIsElementPresent(locator)) {
			return (false);
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false);
		}

		logger.info(myPageName() + " zIsActive() = true");
		return (true);
	}
}