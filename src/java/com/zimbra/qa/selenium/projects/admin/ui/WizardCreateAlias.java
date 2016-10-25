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
/**
 *
 */
package com.zimbra.qa.selenium.projects.admin.ui;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.admin.items.AliasItem;

public class WizardCreateAlias extends AbsWizard {
	public static class Locators {
		public static final String zdlg_NEW_ALIAS = "zdlg__NEW_ALIAS";
		public static final String zdlg_ALIAS_NAME = "css=input[id='zdlgv__NEW_ALIAS_name_2']";
		public static final String zdlg_ALIAS_DOMAIN_NAME = "css=input[id='zdlgv__NEW_ALIAS_name_3_display']";
		public static final String zdlg_TARGET_ACCOUNT_NAME = "css=input[id='zdlgv__NEW_ALIAS_targetName_display']";
		public static final String zdlg_OK = "css=td[id^='zdlg__NEW_ALIAS_button']:contains('OK')";
	}

	public WizardCreateAlias(AbsTab page) {
		super(page);
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {

		if ( !(item instanceof AliasItem) )
			throw new HarnessException("item must be an AliasItem, was "+ item.getClass().getCanonicalName());

		AliasItem alias = (AliasItem)item;

		String CN = alias.getLocalName();
		String domain = alias.getDomainName();
		String targetAccount = alias.getTargetAccountEmail();		
		sType(Locators.zdlg_ALIAS_NAME, CN);
		SleepUtil.sleepSmall();
		this.clearField(Locators.zdlg_ALIAS_DOMAIN_NAME);		
		sType(Locators.zdlg_ALIAS_DOMAIN_NAME,domain);	
		sType(Locators.zdlg_TARGET_ACCOUNT_NAME, targetAccount);
		zClick(Locators.zdlg_OK);
		return alias;

	}

	@Override
	public String myPageName() {
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		boolean present = sIsElementPresent(Locators.zdlg_NEW_ALIAS);
		if ( !present ) {
			return (false);
		}

		boolean visible = this.zIsVisiblePerPosition(Locators.zdlg_NEW_ALIAS, 0, 0);
		if ( !visible ) {
			return (false);
		}

		return (true);
	}

}
