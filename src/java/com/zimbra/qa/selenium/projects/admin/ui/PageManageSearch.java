/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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

public class PageManageSearch extends AbsTab {

	public static class Locators {
		public static final String SEARCH_MENU="zti__AppAdmin__Home__searchHV_table";
		public static final String HOME="Home";
		public static final String SEARCH="Search";
		public static final String SEARCH_OPTIONS="Search Options";
		public static final String SAVED_SEARCHES="Saved Searches";

		public static final String ALL_RESULT="zti__AppAdmin__currentSearch__allResult_textCell";
		public static final String ACCOUNTS="zti__AppAdmin__currentSearch__accountResult_textCell";
		public static final String DOMAINS="zti__AppAdmin__currentSearch__domainResult_textCell";
		public static final String SEARCH_OPTION_DOMAINS="zti__AppAdmin__searchOption__7_textCell";
		public static final String DISTRIBUTION_LISTS="zti__AppAdmin__currentSearch__dlResult_textCell";
	}

	public static class TreeItem {
		public static final String ALL_RESULT="All Result";
		public static final String ACCOUNTS="Accounts";
		public static final String DOMAINS="Domains";
		public static final String DISTRIBUTION_LISTS="Distribution Lists";

		//Search Options
		public static final String SEARCH_OPTIONS="Search Options";
		public static final String BASIC_ATTRIBUTES="Basic Attributes";
		public static final String STATUS="Status";
		public static final String LAST_LOGIN_TIME="Last Login Time";
		public static final String EXTERNAL_EMAIL_ADDRESS="External Email Address";
		public static final String COS="COS";
		public static final String SERVER="Server";

		//Saved Searches
		public static final String SAVED_SEARCHES="Saved Searches";
		public static final String INACTIVE_ACCOUNTS_90="Inactive Accounts (90 days)";
		public static final String LOCKED_OUT_ACCOUNTS="Locked Out Accounts";
		public static final String NON_ACTIVE_ACCOUNTS="Non-Active Accounts";
		public static final String INACTIVE_ACCOUNTS_30="Inactive Accounts (30 days)";
		public static final String ADMIN_ACCOUNTS="Admin Accounts";
		public static final String EXTERNAL_ACCOUNTS="External Accounts";
		public static final String CLOSED_ACCOUNTS="Closed Accounts";
		public static final String MAINTENANCE_ACCOUNTS="Maintenance Accounts";
	}


	public PageManageSearch(AbsApplication application) {
		super(application);
	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsTab#isActive()
	 */
	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if ( !MyApplication.zIsLoaded() )
			throw new HarnessException("Admin Console application is not active!");


		boolean present = sIsElementPresent("css=td:contains('" + TreeItem.MAINTENANCE_ACCOUNTS + "')");
		if ( !present ) {
			return (false);
		}

		boolean visible = zIsVisiblePerPosition("css=td:contains('" + TreeItem.MAINTENANCE_ACCOUNTS + "')", 0, 0);
		if ( !visible ) {
			logger.debug("isActive() visible = "+ visible);
			return (false);
		}

		return (true);

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


		if ( zIsActive() ) {
			

			return;
		}

		// Click on Search
		sClickAt(Locators.SEARCH_MENU,"");
		zWaitForWorkInProgressDialogInVisible();
		//SleepUtil.sleepLong();
		zWaitForActive();
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

	public void zSelectTreeItemOfSearch(String treeItem) throws HarnessException {
		sClickAt(treeItem, "");
		SleepUtil.sleepMedium();
	}

	public void zSelectTreeItem(String treeItem) throws HarnessException {
		sClickAt("css=td:contains('" + treeItem + "')", "");
		SleepUtil.sleepMedium();
	}

	public boolean zVerifyHeader (String header) throws HarnessException {
		if (this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}

}
