/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.search;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.PageManageSearch;

public class NavigateSearch extends AdminCore {

	public NavigateSearch() {
		logger.info("New "+ NavigateSearch.class.getCanonicalName());
		super.startingPage = app.zPageManageSearch;
	}


	/**
	 * Testcase : Navigate to Search page
	 * Steps :
	 * 1. Verify navigation path -- "Home --> Search --> Search --> Options"
	 * @throws HarnessException
	 */

	@Test (description = "Navigate to Search",
			groups = { "sanity", "L0" })

	public void NavigateSearch_01() throws HarnessException {

		/*
		 * Verify navigation path -- "Home --> Search --> Search --> All Results"
		 */
		app.zPageManageSearch.zSelectTreeItemOfSearch(PageManageSearch.Locators.ALL_RESULT);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.ALL_RESULT), "Verfiy the 'All Result' text exists in navigation path");

		/*
		 * Verify navigation path -- "Home --> Search --> Search --> Accounts"
		 */

		app.zPageManageSearch.zSelectTreeItemOfSearch(PageManageSearch.Locators.ACCOUNTS);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.ACCOUNTS), "Verfiy the 'Accounts' text exists in navigation path");

		/*
		 * Verify navigation path -- "Home --> Search --> Search --> Domains"
		 */
		app.zPageManageSearch.zSelectTreeItemOfSearch(PageManageSearch.Locators.DOMAINS);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.DOMAINS), "Verfiy the 'Domains' text exists in navigation path");

		/*
		 * Verify navigation path -- "Home --> Search --> Search --> Distribution Lists"
		 */
		app.zPageManageSearch.zSelectTreeItemOfSearch(PageManageSearch.Locators.DISTRIBUTION_LISTS);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.DISTRIBUTION_LISTS), "Verfiy the 'Distribution Lists' text exists in navigation path");
	}


	/**
	 * Testcase : Navigate to Search page -- Search
	 * Steps :
	 * 1. Verify navigation path -- "Home --> Search --> Search --> Options"
	 * @throws HarnessException
	 */

	@Test (description = "Navigate to Search",
			groups = { "sanity", "L0" })

	public void NavigateSearch_02() throws HarnessException {

		/*
		 * Verify navigation path -- "Home --> Search --> Search Options --> Basci Attributes"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.BASIC_ATTRIBUTES);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH_OPTIONS), "Verfiy the 'Search Options' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.BASIC_ATTRIBUTES), "Verfiy the 'Basic Attributes' text exists in navigation path");

		/*
		 * Verify navigation path -- "Home --> Search --> Search Options --> Status"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.STATUS);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH_OPTIONS), "Verfiy the 'Search Options' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.STATUS), "Verfiy the 'Status' text exists in navigation path");

		/*
		 * Verify navigation path -- "Home --> Search --> Search Options --> Last Login Time"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.LAST_LOGIN_TIME);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH_OPTIONS), "Verfiy the 'Search Options' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.LAST_LOGIN_TIME), "Verfiy the 'Last Login Time' text exists in navigation path");

		/*
		 * Verify navigation path -- "Home --> Search --> Search Options --> External Email Address"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.EXTERNAL_EMAIL_ADDRESS);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH_OPTIONS), "Verfiy the 'Search Options' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.EXTERNAL_EMAIL_ADDRESS), "Verfiy the 'External Email Address' text exists in navigation path");

		/*
		 * Verify navigation path -- "Home --> Search --> Search Options --> COS"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.COS);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH_OPTIONS), "Verfiy the 'Search Options' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.COS), "Verfiy the 'COS' text exists in navigation path");

		/*
		 * Verify navigation path -- "Home --> Search --> Search Options --> Server"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.SERVER);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH_OPTIONS), "Verfiy the 'Search Options' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.SERVER), "Verfiy the 'Server' text exists in navigation path");

		/*
		 * Verify navigation path -- "Home --> Search --> Search Options --> Domains"
		 */
		app.zPageManageSearch.zSelectTreeItemOfSearch(PageManageSearch.Locators.SEARCH_OPTION_DOMAINS);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH_OPTIONS), "Verfiy the 'Search Options' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.DOMAINS), "Verfiy the 'Domains' text exists in navigation path");
	}


	/**
	 * Testcase : Navigate to Search page -- Search
	 * Steps :
	 * 1. Verify navigation path -- "Home --> Search --> Search --> Options"
	 * @throws HarnessException
	 */

	@Test (description = "Navigate to Search",
			groups = { "sanity", "L0" })

	public void NavigateSearch_03() throws HarnessException {

		/*
		 * Verify navigation path -- "Home --> Search --> Saved Searches --> Inactive Accounts (90 days)"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.INACTIVE_ACCOUNTS_90);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SAVED_SEARCHES), "Verfiy the 'Saved Searches' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.INACTIVE_ACCOUNTS_90), "Verfiy the 'Inactive Accounts (90 days)' text exists in navigation path");

		/*
		 * Verify navigation path -- "Home --> Search --> Saved Searches --> Locked Out Accounts"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.LOCKED_OUT_ACCOUNTS);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SAVED_SEARCHES), "Verfiy the 'Saved Searches' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.LOCKED_OUT_ACCOUNTS), "Verfiy the 'Locked Out Accounts' text exists in navigation path");

		/*
		 * Verify navigation path -- "Home --> Search --> Saved Searches --> Non-Active Accounts"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.NON_ACTIVE_ACCOUNTS);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SAVED_SEARCHES), "Verfiy the 'Saved Searches' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.NON_ACTIVE_ACCOUNTS), "Verfiy the 'Non-Active Accounts' text exists in navigation path");

		/*
		 * Verify navigation path -- "Home --> Search --> Saved Searches --> Inactive Accounts (30 days)"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.INACTIVE_ACCOUNTS_30);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SAVED_SEARCHES), "Verfiy the 'Saved Searches' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.INACTIVE_ACCOUNTS_30), "Verfiy the 'Inactive Accounts (30 days)' text exists in navigation path");

		/*
		 * Verify navigation path -- "Home --> Search --> Saved Searches --> Admin Accounts"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.ADMIN_ACCOUNTS);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SAVED_SEARCHES), "Verfiy the 'Saved Searches' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.ADMIN_ACCOUNTS), "Verfiy the 'Admin Accounts' text exists in navigation path");

		/*
		 * Verify navigation path -- "Home --> Search --> Saved Searches --> External Accounts"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.EXTERNAL_ACCOUNTS);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SAVED_SEARCHES), "Verfiy the 'Saved Searches' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.EXTERNAL_ACCOUNTS), "Verfiy the 'External Accounts' text exists in navigation path");

		/*
		 * Verify navigation path -- "Home --> Search --> Saved Searches --> Closed Accounts"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.CLOSED_ACCOUNTS);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SAVED_SEARCHES), "Verfiy the 'Saved Searches' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.CLOSED_ACCOUNTS), "Verfiy the 'Closed Accounts' text exists in navigation path");

		/*
		 * Verify navigation path -- "Home --> Search --> Saved Searches --> Maintenance Accounts"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.MAINTENANCE_ACCOUNTS);

		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.HOME), "Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SEARCH), "Verfiy the 'Search' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.Locators.SAVED_SEARCHES), "Verfiy the 'Saved Searches' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageSearch.zVerifyHeader(PageManageSearch.TreeItem.MAINTENANCE_ACCOUNTS), "Verfiy the 'Maintenance Accounts' text exists in navigation path");
	}
}