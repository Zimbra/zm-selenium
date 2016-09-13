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
/**
 *
 */
package com.zimbra.qa.selenium.projects.admin.ui;

import java.util.ArrayList;
import java.util.List;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.admin.items.CosItem;

/**
 * @author Matt Rhoades
 *
 */
public class PageManageCOS extends AbsTab {

	public static class Locators {
		public static final String CONFIGURE_ICON="css=div.ImgAdministration";
		public static final String COS="zti__AppAdmin__CONFIGURATION__COS_textCell";
		public static final String GEAR_ICON="css=div[class='ImgConfigure']";
		public static final String NEW_MENU="css=td[id='zmi__zb_currentApp__NEW_title']:contains('New')";
		public static final String HOME="Home";
		public static final String CONFIGURE="Configure";
		public static final String CLASS_OS_SERVICE="Class of Service";
		public static final String DELETE_BUTTON="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgDelete']";
		public static final String EDIT_BUTTON="css=td[id='zmi__zb_currentApp__EDIT_title']:contains('Edit')";
		public static final String RIGHT_CLICK_MENU_DELETE_BUTTON="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgDelete']";
		public static final String RIGHT_CLICK_MENU_EDIT_BUTTON="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgProperties']";
		public static final String DUPLICATE_COS= "css= div[id='zm__zb_currentApp__MENU_POP'] td[id='zmi__zb_currentApp__DUPLICATE_title']";
		public static final String RIGHT_CLICK_DUPLICATE_COS= "css= td[id='zmi__COSLV__DUPLICATE_title']";
	}

	public PageManageCOS(AbsApplication application) {
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


		boolean present = sIsElementPresent(Locators.GEAR_ICON);
		if ( !present ) {
			return (false);
		}

		boolean visible = zIsVisiblePerPosition(Locators.GEAR_ICON, 0, 0);
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

		// Click on Addresses -> Accounts
		SleepUtil.sleepMedium();
		zClickAt(Locators.CONFIGURE_ICON,"");
		SleepUtil.sleepMedium();
		zClickAt(Locators.COS, "");


	}

	@Override
	public AbsPage zListItem(Action action, String item)
	throws HarnessException {
		logger.info(myPageName() + " zListItem("+ action +", "+ item +")");

		tracer.trace(action +" on subject = "+ item);

		AbsPage page = null;
		SleepUtil.sleepSmall();

		// How many items are in the table?
		String rowsLocator = "css=div#zl__COS_MANAGE div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zListGetAccounts: number of accounts: "+ count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {
			final String accountLocator = rowsLocator + ":nth-child("+i+")";
			String locator;

			// Email Address
			locator = accountLocator + " td[id^='cos_data_name']";


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
	public AbsPage zListItem(Action action, Button option, String item)
	throws HarnessException {
		return null;
	}
	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption ,String item)
	throws HarnessException {
		return null;
	}


	public AbsPage zToolbarPressButton(Button button) throws HarnessException {

		logger.info(myPageName() + " zToolbarPressButton("+ button +")");

		tracer.trace("Press the "+ button +" button");

		if ( button == null )
			throw new HarnessException("Button cannot be null!");


		// Default behavior variables
		//
		String locator = null;			// If set, this will be clicked
		AbsPage page = null;	// If set, this page will be returned

		// Based on the button specified, take the appropriate action(s)
		//

		if ( button == Button.B_NEW ) {

			// New button
			locator = Locators.COS;


			// Create the page
			page = new WizardCreateCos(this);
			// FALL THROUGH

		} else if(button == Button.B_TREE_DELETE) {

			locator=Locators.RIGHT_CLICK_MENU_DELETE_BUTTON;

			page = new DialogForDeleteOperationCos(this.MyApplication, null);

		} else if(button == Button.B_TREE_EDIT) {

			locator=Locators.RIGHT_CLICK_MENU_EDIT_BUTTON;

			page=new FormEditCos(this.MyApplication);
		}else if(button == Button.O_DUPLICATE_COS) {

			locator = Locators.RIGHT_CLICK_DUPLICATE_COS;

			page = new WizardCreateCos(this);

		} else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		if ( locator == null ) {
			throw new HarnessException("locator was null for button "+ button);
		}

		// Default behavior, process the locator by clicking on it
		//
		SleepUtil.sleepLong();
		this.zClickAt(locator,"");

		// If page was specified, make sure it is active
		if ( page != null ) {
			SleepUtil.sleepMedium();
		}

		return (page);

	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ option +")");

		tracer.trace("Click pulldown "+ pulldown +" then "+ option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");


		// Default behavior variables
		String pulldownLocator = null; // If set, this will be expanded
		String optionLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		if (pulldown == Button.B_GEAR_BOX) {
			pulldownLocator = Locators.GEAR_ICON;

			if (option == Button.O_NEW) {

				optionLocator = Locators.NEW_MENU;

				page = new WizardCreateCos(this);

				// FALL THROUGH

			}   else if(option == Button.O_DELETE) {

				optionLocator = Locators.DELETE_BUTTON;

				page = new DialogForDeleteOperationCos(this.MyApplication,null);

			}  else if(option == Button.O_EDIT) {

				optionLocator = Locators.EDIT_BUTTON;

				page=new FormEditCos(this.MyApplication);

			} else if(option == Button.O_DUPLICATE_COS) {

				optionLocator = Locators.DUPLICATE_COS;

				page = new WizardCreateCos(this);

			}  else {
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

			this.sClickAt(pulldownLocator,"");

			// If the app is busy, wait for it to become active
			//zWaitForBusyOverlay();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator " + optionLocator + " not present!");
				}
				SleepUtil.sleepLong();
				this.sClickAt(optionLocator,"");

				// If the app is busy, wait for it to become active
				//zWaitForBusyOverlay();
			}

		}

		// Return the specified page, or null if not set
		return (page);

	}

	/**
	 * Return a list of all cos entries in the current view
	 * @return
	 * @throws HarnessException
	 * @throws HarnessException
	 */
	public List<CosItem> zListGetCos() throws HarnessException {

		List<CosItem> items = new ArrayList<CosItem>();

		// Make sure the button exists
		if ( !this.sIsElementPresent("css=div[id='zl__COS_MANAGE'] div[id$='__rows']") )
			throw new HarnessException("Account Rows is not present");

		// How many items are in the table?
		String rowsLocator = "css=div#zl__COS_MANAGE div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zListGetAccounts: number of accounts: "+ count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {
			final String cosLocator = rowsLocator + ":nth-child("+i+")";
			String locator;

			CosItem item = new CosItem();

			// Email Address
			locator = cosLocator + "//td[contains(@id,'cos_data_name')]";
			if ( this.sIsElementPresent(locator) ) {
				item.setCosName(this.sGetText(locator).trim());
			}

			// Display Name
			// Status
			// Lost Login Time
			// Description
			// Add the new item to the list
			
			items.add(item);
			logger.info(item.prettyPrint());
		}

		// Return the list of items
		return (items);
	}


	public boolean zVerifyHeader (String header) throws HarnessException {
		if(this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}


}


