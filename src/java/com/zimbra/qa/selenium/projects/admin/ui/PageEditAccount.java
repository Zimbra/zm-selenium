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
package com.zimbra.qa.selenium.projects.admin.ui;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;

public class PageEditAccount extends AbsTab {

	public static final String ztab__DOAMIN_EDIT__DWT192 = "xpath=//*[@id='ztab__DOAMIN_EDIT__DWT192']";
	public static final String ztab__DOAMIN_EDIT__DWT192_classAttr = "xpath=(//*[@id='ztab__DOAMIN_EDIT__DWT192'])@class";
	public static final String ztab_ACCOUNT_EDIT_ACL = "css=div[id^='zti__AppAdmin__Home__actLstHV__'][class='ZTreeItemTextCell']:contains(ACL)";
	public static final String ztab_ACCOUNT_EDIT_GENERAL_INFORMATION = "css=div[id='zti__AppAdmin__Home__actLstHV__1__1_textCell']:contains('General Information')";
	public static final String ACCOUNT_EDIT_ACL_ADD = "css=td[id^='ztabv__ACCT_EDIT_dwt_button_'] td[id$='title']:contains('Add')";
	public static final String ACCOUNT_EDIT_ACL_GRANTEE_NAME = "css=div[class='DwtDialog WindowOuterContainer'] table[class='dynselect_table'] input";

	public static class Locators {
		public static final String ADD_BUTTON = "css=td[id$='dwt_button_5___container']:contains('Add')";
		public static final String FEATURES="css=div[id^='zti__AppAdmin__Home__actLstHV'][id$='_textCell']:contains('Features')";
		public static final String PREFERENCES="css=div[id^='zti__AppAdmin__Home__actLstHV'][id$='_textCell']:contains('Preferences')";
		public static final String ALIASES="css=div[id^='zti__AppAdmin__Home__actLstHV'][id$='_textCell']:contains('Aliases')";
		public static final String MOBILE_ACCESS="css=div[id^='zti__AppAdmin__Home__actLstHV'][id$='_textCell']:contains('Mobile Access')";
		public static final String ARCHIVING="css=div[id^='zti__AppAdmin__Home__actLstHV'][id$='_textCell']:contains('Archiving')";

	}

	public PageEditAccount(AbsApplication application) {
		super(application);
		logger.info("new " + myPageName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if ( !MyApplication.zIsLoaded() )
			throw new HarnessException("Admin Console application is not active!");

		boolean present = sIsElementPresent(ztab__DOAMIN_EDIT__DWT192);
		if ( !present ) {
			return (false);
		}

		String attrs = sGetAttribute(ztab__DOAMIN_EDIT__DWT192_classAttr);
		if ( !attrs.contains("ZSelected") ) {
			return (false);
		}

		return (true);

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

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

		logger.info(myPageName() + " zToolbarPressButton("+ button +")");

		tracer.trace("Press the "+ button +" button");

		if ( button == null )
			throw new HarnessException("Button cannot be null!");


		//
		String locator = null;			// If set, this will be clicked
		AbsPage page = null;	// If set, this page will be returned

		// Based on the button specified, take the appropriate action(s)
		//

		if (button == Button.B_ADD) {

			locator=Locators.ADD_BUTTON;

			page = new FormEditAccount(this.MyApplication);
		} else if (button == Button.B_FEATURES) {

			locator=Locators.FEATURES;

			page = new FormEditAccount(this.MyApplication);
		} else if (button == Button.B_PREFERENCES) {

			locator=Locators.PREFERENCES;

			page = new FormEditAccount(this.MyApplication);
		} else if (button == Button.B_ALIASES) {

			locator=Locators.ALIASES;

			page = new FormEditAccount(this.MyApplication);
		}  else if (button == Button.B_MOBILE_ACCESS) {

			locator=Locators.MOBILE_ACCESS;

			page = new FormEditAccount(this.MyApplication);
		} else if (button == Button.B_ARCHIVING) {

			locator=Locators.ARCHIVING;

			page = new FormEditAccount(this.MyApplication);
		} 
		else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		if ( locator == null ) {
			throw new HarnessException("locator was null for button "+ button);
		}

		// Default behavior, process the locator by clicking on it
		//
		this.zClickAt(locator,"");
		SleepUtil.sleepMedium();


		// If page was specified, make sure it is active
		if ( page != null ) {
			SleepUtil.sleepMedium();
		}

		//sMouseOut(locator);
		return (page);


	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option)
			throws HarnessException {
		return null;
	}

	public AbsPage zAddACL(String granteeName, Button pulldown, String rightName)

			throws HarnessException {


		return null;
	}

	public boolean zVerifyACL(String item) throws HarnessException {

		logger.info(myPageName() + " zVerifyACL("+ item +")");
		boolean found = false;
		SleepUtil.sleepMedium();

		// How many items are in the table?
		String rowsLocator = "css=td[id='ztabv__ACCT_EDIT_grantsList___container'] div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zVerifyPolicyName: number of policys: "+ count);

		// Get each row data from the table list
		for (int i = 1; i <= count; i++) {
			final String aclLocator = rowsLocator + ":nth-child("+i+")";
			String locator;
			locator = aclLocator + " td" + ":nth-child(3)";

			if(this.sIsElementPresent(locator))
			{
				if(this.sGetText(locator).trim().equalsIgnoreCase(item))
				{
					found = true;
					break;
				} else 
				{
					logger.info("search result not displayed in current view");
				}
			} 

			if (found == true) {
				SleepUtil.sleepSmall();
				logger.info("Search result displayed in current view");
				ZAssert.assertTrue(found, "Search result displayed in current view");
				break;
			}

		}
		return found;
	}


}
