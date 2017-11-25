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

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.admin.items.DomainItem;

/**
 * @author Matt Rhoades
 *
 */
public class WizardCreateDomain extends AbsWizard {

	public static class Locators {
		public static final String zdlg_DOMAIN_NAME = "zdlgv__NEW_DOMAIN_zimbraDomainName";
		public static final String MAIL_SERVER_DROPDOWN_old = "css=div[id$='zimbraMailHost_arrow_button']/div";
		public static final String MAIL_SERVER_DROPDOWN = "css=div[id^='zdlgv__NEW_DOMAIN_gal_sync_accounts_set'][id$='zimbraMailHost_arrow_button']>div";
		public static final String MAIL_SERVER_DROPDOWN_TABLE = "css=div#___OSELECT_MENU___";
		public static final String ADD_A_GAL_ACCOUNT_BUTTON = "css=button.xform_button:contains('Add a GAL account')";
	}

	public WizardCreateDomain(AbsTab page) {
		super(page);
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {

		if (!(item instanceof DomainItem))
			throw new HarnessException("item must be an DomainItem, was " + item.getClass().getCanonicalName());

		DomainItem domain = (DomainItem) item;

		String domainName = domain.getName();

		zType(Locators.zdlg_DOMAIN_NAME, domainName);
		clickNext(AbsWizard.Locators.DOMAIN_DIALOG);
		sClick(Locators.MAIL_SERVER_DROPDOWN);
		sClickAt(Locators.MAIL_SERVER_DROPDOWN_TABLE + " div:contains('" + "')", ""); // FIX-ME
		clickFinish(AbsWizard.Locators.DOMAIN_DIALOG);

		return (domain);
	}

	@Override
	public String myPageName() {
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		return false;
	}

	public boolean zCloseWizard() throws HarnessException {
		this.sClickAt("css=td[id$='zdlg__NEW_DOMAIN_button1_title']", "");
		return true;
	}
}