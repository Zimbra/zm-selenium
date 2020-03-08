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
import com.zimbra.qa.selenium.projects.admin.items.GALItem;
import com.zimbra.qa.selenium.projects.admin.items.GALItem.GALMode;

/**
 * @author Jitesh Singh
 *
 */
public class WizardConfigureGAL extends AbsWizard {

	public static class Locators {
		public static final String GAL_CONFIGRATION_WIZARD = "css=div[id^='zdlg__UNDEFINE__DWT'][style*='z-index: 7'] td[id$='title']:contains('GAL Configuration Wizard')";
		public static final String GAL_MODE_DROPDOWN = "css=div[id='zdlgv__UNDEFINE_zimbraGalMode_arrow_button'] div.ImgSelectPullDownArrow";
		public static final String GAL_MODE_OPTION_INTERNAL = "css=div[id='zdlgv__UNDEFINE_zimbraGalMode_choice_0']";
		public static final String GAL_MODE_OPTION_EXTERNAL = "css=div[id='zdlgv__UNDEFINE_zimbraGalMode_choice_1']";
		public static final String GAL_MODE_OPTION_BOTH = "css=div[id='zdlgv__UNDEFINE_zimbraGalMode_choice_2']";
		public static final String WIZARD_MINIMIZE = "div[style*='z-index: 7'] div.ImgCollapseRight";
		public static final String MAIL_SERVER_DROPDOWN = "css=div[id^='zdlgv__UNDEFINE_'][id$='zimbraMailHost_arrow_button'] div.ImgSelectPullDownArrow";
		public static final String MAIL_SERVER = "css=div[id^='zdlgv__UNDEFINE_'][id$='zimbraMailHost_choice_0']";
		public static final String SERVER_TYPE_DROPDOWN = "css=div[id^='zdlgv__UNDEFINE_'][id$='galservertype_arrow_button'] div.ImgSelectPullDownArrow";
		public static final String SERVER_TYPE_ACTIVE_DIRECTORY = "css=div[id^='zdlgv__UNDEFINE_'][id$='galservertype_choice_1']";
		public static final String EXT_DATA_SRC_NAME = "css=td[id$='new_external_gal_ds_name___container'][style*='visible'] input";
		public static final String INT_DATA_SRC_NAME = "css=td[id$='new_internal_gal_ds_name___container'][style*='visible'] input";
		public static final String LDAP_URL = "css=input[id^='zdlgv__UNDEFINE_'][id$='_zimbraGalLdapURL[0]_4']";
		public static final String LDAP_FILTER = "css=textarea[id^='zdlgv__UNDEFINE_'][id$='zimbraGalLdapFilter']";
		public static final String LDAP_SEARCH_BASE = "css=textarea[id^='zdlgv__UNDEFINE_'][id$='zimbraGalLdapSearchBase']";
		public static final String CONFIRM_BIND_PASSWORD = "css=td[id$='_zimbraGalLdapBindPasswordConfirm___container'][style*='visible'] input";
		public static final String POLLING_INTERVAL = "css=input[id='zdlgv__UNDEFINE_gal_sync_accounts_set[0]/new_internal_gal_polling_interval_2']";
		public static final String NEXT_BUTTON = "css=td[id^='Next'] td[id$='_button12_title']";
		public static final String FINISH_BUTTON = "css=td[id^='Finish'] td[id$='_button13_title']";
	}

	public WizardConfigureGAL(AbsTab page) {
		super(page);
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {
		if (!(item instanceof GALItem))
			throw new HarnessException("item must be a GALItem, was " + item.getClass().getCanonicalName());

		GALItem gal = (GALItem) item;

		if (gal.getCurrentGALMode().equals(GALMode.Internal) && gal.getNewGALMode().equals(GALMode.Both)) {
			// Select GAL mode as 'both' and fill the required fields
			sClick(Locators.GAL_MODE_DROPDOWN);
			sClick(Locators.GAL_MODE_OPTION_BOTH);

			// Select the Mail Server
			sClick(Locators.MAIL_SERVER_DROPDOWN);
			sClick(Locators.MAIL_SERVER);

			clearField(Locators.EXT_DATA_SRC_NAME);
			sType(Locators.EXT_DATA_SRC_NAME, gal.getDataSourceName());
			sType(Locators.LDAP_URL, gal.getLDAPUrl());
			clearField(Locators.LDAP_FILTER);
			sType(Locators.LDAP_FILTER, gal.getLDAPFilter());
			clearField(Locators.LDAP_SEARCH_BASE);
			sType(Locators.LDAP_SEARCH_BASE, gal.getLDAPSearchBase());

			// Select the server type
			sClick(Locators.SERVER_TYPE_DROPDOWN);
			sClick(Locators.SERVER_TYPE_ACTIVE_DIRECTORY);

			// Click next 5 times
			for (int i = 0; i < 5; i++) {
				sClick(Locators.NEXT_BUTTON);
				SleepUtil.sleepSmall();
			}
			sClick(Locators.FINISH_BUTTON);

		} else if (gal.getCurrentGALMode().equals(GALMode.External) && gal.getNewGALMode().equals(GALMode.Both)) {
			// Select GAL mode as 'both' and fill the required fields
			sClick(Locators.GAL_MODE_DROPDOWN);
			sClick(Locators.GAL_MODE_OPTION_BOTH);
			zWaitForElementPresent(Locators.INT_DATA_SRC_NAME);

			// Enter the internal data source name
			sType(Locators.INT_DATA_SRC_NAME, gal.getDataSourceName());

			// Next
			sClick(Locators.NEXT_BUTTON);

			// Enter bind password
			sType(Locators.CONFIRM_BIND_PASSWORD, "password");

			// Click next 5 times
			for (int i = 0; i < 5; i++) {
				sClick(Locators.NEXT_BUTTON);
				SleepUtil.sleepSmall();
			}
			sClick(Locators.FINISH_BUTTON);

		} else if (gal.getCurrentGALMode().equals(GALMode.Internal)) {
			sClick(Locators.MAIL_SERVER_DROPDOWN);
			sClick(Locators.MAIL_SERVER);
			sType(Locators.POLLING_INTERVAL, "2");
			sClick(Locators.NEXT_BUTTON);
			sClick(Locators.FINISH_BUTTON);

		} else {
			throw new HarnessException("This change of GALMode is not implemented!");
		}

		return gal;
	}

	@Override
	public String myPageName() {
		return "GAL Configuration wizard";
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		boolean present = sIsElementPresent(Locators.GAL_CONFIGRATION_WIZARD);
		return present;
	}

	public boolean zCloseWizard() throws HarnessException {
		this.sClick(Locators.WIZARD_MINIMIZE);
		return true;
	}
}