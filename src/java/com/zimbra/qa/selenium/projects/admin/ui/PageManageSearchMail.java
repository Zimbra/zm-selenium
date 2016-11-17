/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageAccounts.Locators;


/**
 * @author Matt Rhoades
 *
 */
public class PageManageSearchMail extends AbsTab {

	public static class Locators {
		public static final String TOOLS_AND_MIGRATION_ICON="css=div[id='zti__AppAdmin__Home__magHV_textCell']";
		public static final String SEARCHMAIL="css=div[id='zti__AppAdmin__magHV__xmbSearch_textCell']";
		public static final String HOME="Home";
		public static final String TOOLS_AND_MIGRATION="Tools and Migration";
		public static final String SEARCH_MAIL="Search Mail";
		public static final String GEAR_ICON="css=div.ImgConfigure";
		public static final String NEW_MENU="css=div[id='zm__zb_currentApp__MENU_POP'] div[id='zmi__zb_currentApp__NEW']";
		public static final String DELETE="css=div[id='zm__zb_currentApp__MENU_POP'] div[id='zmi__zb_currentApp__DELETE']";
		public static final String VIEW="css=div[id='zm__zb_currentApp__MENU_POP'] div[id='zmi__zb_currentApp__EDIT']";
		public static final String VIEW_RESULTS="css=div[id='zm__zb_currentApp__MENU_POP'] div[id^='zmi__zb_currentApp__']:contains('View results')";
		public static final String STATUS="css=td#_XForm_";
	}

	public PageManageSearchMail(AbsApplication application) {
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


		boolean present = sIsElementPresent("css=td[id='zti__AppAdmin__Home__magHV_textTDCell']:contains('" + Locators.TOOLS_AND_MIGRATION + "')");
		if ( !present ) {
			return (false);
		}

		boolean visible = zIsVisiblePerPosition("css=td[id='zti__AppAdmin__Home__magHV_textTDCell']:contains('" + Locators.TOOLS_AND_MIGRATION + "')", 0, 0);
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
			// This page is already active.
			return;
		}

		// Click on Tools and Migration -> Downloads
		zClickAt(Locators.TOOLS_AND_MIGRATION_ICON,"");
		SleepUtil.sleepMedium();
		if (sIsElementPresent(Locators.SEARCHMAIL));
		sClickAt(Locators.SEARCHMAIL, "");

		zWaitForActive();
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

	public boolean zVerifyHeader (String header) throws HarnessException {
		if (this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}

	public boolean zVerifyToolsAndMigrationHeader (String header) throws HarnessException {
		if (this.sIsElementPresent("css=div[id='zti__AppAdmin__Home__magHV_textCell']:contains('" + header + "')"))
			return true;
		return false;
	}

	public boolean zVerifyViewSearchQuery() throws HarnessException {

		for(int i=12;i>=0;i--) {
			if (sIsElementPresent(Locators.STATUS+i+"_status___label")) {
				return true;
			}
		}
		return false;
	}


	@Override
	public AbsPage zListItem(Action action, String item)
			throws HarnessException {
		logger.info(myPageName() + " zListItem("+ action +", "+ item +")");

		tracer.trace(action +" on subject = "+ item);

		AbsPage page = null;
		SleepUtil.sleepMedium();

		// How many items are in the table?
		String rowsLocator = "css=td[id='ztabv__UNDEFINE_searchesList___container'] div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zListGetSearchResults: number of results: "+ count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {
			final String resultLocator = rowsLocator + ":nth-child("+i+")";
			String locator;

			// Email Address
			locator = resultLocator + " td:nth-child(3)";


			if(this.sIsElementPresent(locator))
			{
				if(this.sGetText(locator).trim().equalsIgnoreCase(item))
				{
					if(action == Action.A_LEFTCLICK) {
						zClick(locator);
						break;
					} else if(action == Action.A_RIGHTCLICK) {
						zRightClick(locator);
						break;
					}

				}
			}
		}
		return page;
	}


	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ option +")");

		tracer.trace("Click pulldown "+ pulldown +" then "+ option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");


		String pulldownLocator = null; 
		String optionLocator = null; 
		AbsPage page = null; 
		if (pulldown == Button.B_GEAR_BOX) {
			pulldownLocator = Locators.GEAR_ICON;

			if (option == Button.O_NEW) {

				optionLocator = Locators.NEW_MENU;
				page = new FormSearchMail(this.MyApplication);

			}else if (option == Button.O_DELETE) {

				optionLocator = Locators.DELETE;
			}else if (option == Button.B_VIEW) {

				optionLocator = Locators.VIEW;
			}else if (option == Button.O_VIEW_RESULTS) {

				optionLocator = Locators.VIEW_RESULTS;
			}
			else {
				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
			}

		} else {
			throw new HarnessException("no logic defined for pulldown/option "
					+ pulldown + "/" + option);
		}

		// Default behavior
		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator " + pulldownLocator + " not present!");
			}

			this.zClickAt(pulldownLocator,"");
			SleepUtil.sleepMedium();

			// If the app is busy, wait for it to become active
			//zWaitForBusyOverlay();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator " + optionLocator + " not present!");
				}

				this.zClickAt(optionLocator,"");
				SleepUtil.sleepMedium();

				// If the app is busy, wait for it to become active
				//zWaitForBusyOverlay();
			}

		}

		// Return the specified page, or null if not set
		return (page);

	}

	public boolean zVerifySearchResult(String item) throws HarnessException {

		logger.info(myPageName() + " zVerifyPolicyName("+ item +")");
		boolean found = false;
		SleepUtil.sleepMedium();

		// How many items are in the table?
		String rowsLocator = "css=td[id='ztabv__UNDEFINE_searchesList___container'] div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zVerifyPolicyName: number of policys: "+ count);

		// Get each row data from the table list
		for (int i = 1; i <= count; i++) {
			final String accountLocator = rowsLocator + ":nth-child("+i+")";
			String locator;
			locator = accountLocator + " td" + ":nth-child(3)";

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
