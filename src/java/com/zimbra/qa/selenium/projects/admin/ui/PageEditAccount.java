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


public class PageEditAccount extends AbsTab {

	public static final String ztab__DOAMIN_EDIT__DWT192 = "xpath=//*[@id='ztab__DOAMIN_EDIT__DWT192']";
	public static final String ztab__DOAMIN_EDIT__DWT192_classAttr = "xpath=(//*[@id='ztab__DOAMIN_EDIT__DWT192'])@class";
	public static final String ztab_ACCOUNT_EDIT_ACL = "css=div[id='zti__AppAdmin__Home__actLstHV__1__14_textCell']";
	public static final String ztab_ACCOUNT_EDIT_GENERAL_INFORMATION = "css=div[id='zti__AppAdmin__Home__actLstHV__1__1_textCell']:contains('General Information')";
	public static final String ACCOUNT_EDIT_ACL_ADD = "css=div[id=^'ztabv__ACCT_EDIT_dwt_button'] td[class='ZWidgetTitle']:contains('Add')";
	public static final String ACCOUNT_EDIT_ACL_GRANTEE_NAME = "css=div[class='DwtDialog WindowOuterContainer'] table[class='dynselect_table'] input";

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
		return null;
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


}
