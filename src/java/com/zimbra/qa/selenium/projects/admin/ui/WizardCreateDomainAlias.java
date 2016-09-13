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
/**
 *
 */
package com.zimbra.qa.selenium.projects.admin.ui;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.items.DomainItem;

/**
 * @author Matt Rhoades
 *
 */
public class WizardCreateDomainAlias extends AbsWizard {
	public static class Locators {
		public static final String DOMAIN_ALIAS_DLG = "zdlgv__UNDEFINE1_zimbraDomainName";
		public static final String DOMAIN_ALIAS_NAME = "_zimbraDomainName";
		public static final String TARGET_DOMAIN_NAME="_zimbraDomainAliasTargetName";
		public static final String zdlg_OK="_button2_title";
	}

	public WizardCreateDomainAlias(AbsTab page) {
		super(page);
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {

		if ( !(item instanceof DomainItem) )
			throw new HarnessException("item must be an AliasItem, was "+ item.getClass().getCanonicalName());

		DomainItem alias = (DomainItem)item;

		String domainAlias = alias.getName();
		String targetDomain = ConfigProperties.getStringProperty("testdomain");

		for(int i=10;i>=1;i--) {
			if(sIsElementPresent("zdlgv__UNDEFINE"+i+Locators.TARGET_DOMAIN_NAME)) {
				sType("zdlgv__UNDEFINE"+i+Locators.DOMAIN_ALIAS_NAME, domainAlias);
				sClick("zdlgv__UNDEFINE"+i+Locators.TARGET_DOMAIN_NAME);
				sType("zdlgv__UNDEFINE"+i+Locators.TARGET_DOMAIN_NAME, targetDomain);
				zClick("zdlg__UNDEFINE"+i+Locators.zdlg_OK);
				break;
			}
		}
		return alias;
	}

	public IItem zSetTargetDomain(IItem item) throws HarnessException {

		if ( !(item instanceof DomainItem) )
			throw new HarnessException("item must be an AliasItem, was "+ item.getClass().getCanonicalName());

		DomainItem alias = (DomainItem)item;

		String targetDomain = ConfigProperties.getStringProperty("server.host");

		for(int i=10;i>=1;i--) {
			if(sIsElementPresent("zdlgv__UNDEFINE"+i+Locators.TARGET_DOMAIN_NAME)) {
				sType("zdlgv__UNDEFINE"+i+Locators.TARGET_DOMAIN_NAME, targetDomain);
				zClick("zdlg__UNDEFINE"+i+Locators.zdlg_OK);
				break;
			}
		}


		return alias;
	}


	@Override
	public String myPageName() {
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		boolean present = sIsElementPresent(Locators.DOMAIN_ALIAS_DLG);
		if ( !present ) {
			return (false);
		}

		boolean visible = this.zIsVisiblePerPosition(Locators.DOMAIN_ALIAS_DLG, 0, 0);
		if ( !visible ) {
			return (false);
		}

		return (true);
	}

}
