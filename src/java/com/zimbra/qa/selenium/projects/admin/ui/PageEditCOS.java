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
/**
 *
 */
package com.zimbra.qa.selenium.projects.admin.ui;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
/**
 * @author Matt Rhoades
 *
 */
public class PageEditCOS extends AbsTab {

	public static class Locators {
		public static final String zArrowSelectSearchObject	="css=td[id*='dropdown'] div[class='ImgSelectPullDownArrow']";
		public static final String zEnableTwoFactorAuth="css=td[id$='_zimbraFeatureTwoFactorAuthAvailable___container'] input";	
		public static final String zRequiredTwoFactorAuth="css=td[id$='_zimbraFeatureTwoFactorAuthRequired___container'] input";
		public static final String zTwoFactorAuthNumScratchCodes="css=td[id$='_zimbraTwoFactorAuthNumScratchCodes___container'] input";
		public static final String zEnableApplicationPasscodes="css=td[id$='_zimbraFeatureAppSpecificPasswordsEnabled___container'] input";
		public static final String COS_EDIT_ACL="css=div[id^='zti__AppAdmin__Home__actLstHV'] div[class='ZTreeItemTextCell']:contains('ACL')";
		
	}

	public PageEditCOS(AbsApplication application) {
		super(application);
	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsTab#isActive()
	 */
	@Override
	public boolean zIsActive() throws HarnessException {
		throw new HarnessException("implement me");
	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsTab#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsTab#navigateTo()
	 */
	@Override
	public void zNavigateTo() throws HarnessException {
		throw new HarnessException("implement me");
	}

	@Override
	public AbsPage zListItem(Action action, String item)
			throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item)
			throws HarnessException {
		return null;
	}
	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption ,String item)
			throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option)
			throws HarnessException {
		return null;

	}

	public AbsPage zPreferenceTextSet(Button button, String numscratchcodes) throws HarnessException {
		logger.info(myPageName() + " zPreferenceSet("+ button +")");
		tracer.trace("Click page button "+ button);

		AbsPage page = null;
		String locator = null;

		if ( button == Button.B_TWO_FACTOR_AUTH_NUM_SCRATCH_CODES ) {

			locator = Locators.zTwoFactorAuthNumScratchCodes;

		} else {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		// Make sure the locator was set
		if ( locator == null ) {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException(locator + " no present!");
		}

		else
		{
			this.sType(locator, numscratchcodes);
		}

		SleepUtil.sleepSmall();
		return (page);
	}


	public AbsPage zPreferenceCheckboxSet(Button button, boolean status) throws HarnessException {
		logger.info(myPageName() + " zPreferenceSet("+ button +")");
		tracer.trace("Click page button "+ button);

		AbsPage page = null;
		String locator = null;

		SleepUtil.sleepSmall();

		if ( button == Button.B_ENABLE_TWO_FACTOR_AUTH ) {

			locator = Locators.zEnableTwoFactorAuth;

		} else if ( button == Button.B_REQUIRED_TWO_FACTOR_AUTH ) {

			locator = Locators.zRequiredTwoFactorAuth;

		} else if ( button == Button.B_ENABLE_APPLICATION_PASSCODES ) {

			locator = Locators.zEnableApplicationPasscodes;

		} else {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		// Make sure the locator was set
		if ( locator == null ) {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Button "+ button +" locator "+ locator +" not present!");
		}

		if ( this.sIsChecked(locator) == status ) {
			logger.debug("checkbox status matched. not doing anything");
			return (page);
		}

		if ( status == true ) {
			this.sClickAt(locator,"");

		} else {
			this.sUncheck(locator);
		}

		SleepUtil.sleepSmall();
		return (page);
	}

}
