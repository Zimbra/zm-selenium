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

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.ajax.ui.Toaster;
/**
 * Admin Console -> Manage Accounts -> Accounts
 * @author Matt Rhoades
 *
 */
public class PageManageAccounts extends AbsTab {

	public static class Locators {
		public static final String MANAGE_ACCOUNTS_ICON="css=div.ImgManageAccounts";
		public static final String ACCOUNTS="css=td[id^='zti__AppAdmin__Home__actLstHV']";
		public static final String GEAR_ICON="css=div.ImgConfigure";
		public static final String HOME="Home";
		public static final String MANAGE="Manage";
		public static final String ACCOUNT="Accounts";
		public static final String NEW_MENU="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgNewAccount']";
		public static final String NEW_ADMIN_USER="css=td[id='zmi__zb_currentApp__NEW_ADMIN_title']:contains('New Administrator')";
		public static final String DELETE_BUTTON="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgDelete']";
		//public static final String EDIT_BUTTON="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgEdit']";
		public static final String EDIT_BUTTON="css=td[id='zmi__zb_currentApp__EDIT_title']:contains('Edit')";
		public static final String RIGHT_CLICK_MENU_DELETE_BUTTON="css=div[id^='zm__ACLV__MENU_POP'] div[class='ImgDelete']";
		public static final String RIGHT_CLICK_MENU_EDIT_BUTTON="css=td[id='zmi__ACLV__EDIT_title']:contains('Edit')";
		public static final String RIGHT_CLICK_MENU_CHANGE_PASSWORD_BUTTON = "css=td[id='zmi__ACLV__CHNG_PWD_title']";
		public static final String CHANGE_PASSWORD_BUTTON="css=td[id='zmi__zb_currentApp__CHNG_PWD_title']";
		public static final String ADVANCED="css=div[id^='zti__AppAdmin__Home__actLstHV'][id$='_textCell']:contains('Advanced')";
		public static final String RIGHT_CLICK_MENU_VIEW_MAIL_BUTTON="css=td[id='zmi__ACLV__VIEW_MAIL_title']";
		public static final String RIGHT_CLICK_MENU_SEARCH_MAIL_BUTTON="css=td[id^='zmi__ACLV__UNKNOWN']:contains('Search Mail')";
		public static final String VIEW_MAIL="css=td[id='zmi__zb_currentApp__VIEW_MAIL_title']";	
		public static final String SEARCH_MAIL="css=td[id^='zmi__zb_currentApp__UNKNOWN']:contains('Search Mail')";
		public static final String RIGHT_CLICK_INVALIDATE_SESSIONS="css=td[id='zmi__ACLV__EXPIRE_SESSION_title']";
		public static final String INVALIDATE_SESSIONS="css=td[id='zmi__zb_currentApp__EXPIRE_SESSION_title']";
		public static final String INVALIDATE_SESSIONS_YES="css=td[id^='zdlg__MSG__GLOBAL__confirm']:contains('Yes')";
		public static final String MUST_CHANGE_PASSWORD ="css=input[id='ztabv__ACCT_EDIT_zimbraPasswordMustChange']";
		public static final String CONFIGURE_GRANTS="css=td[id^='zmi__zb_currentApp__UNKNOWN']:contains('Configure Grants')";
		public static final String RIGHT_CLICK_CONFIGURE_GRANTS="css=td[id^='zmi__ACLV__UNKNOWN']:contains('Configure Grants')";
		public static final String SEARCH_QUERY_LABEL="div[id^='_XForm_4_output']:contains('Search query')";
			
	}

	public PageManageAccounts(AbsApplication application) {
		super(application);

		logger.info("new " + myPageName());

	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsTab#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
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
	 * @see projects.admin.ui.AbsTab#navigateTo()
	 */
	@Override
	public void zNavigateTo() throws HarnessException {

		if ( zIsActive() ) {
			// This page is already active.
			return;
		}

		// Click on Manage Accounts -> Accounts
		SleepUtil.sleepLong();
		sClickAt(Locators.MANAGE_ACCOUNTS_ICON,"");
		zWaitForProgressDialogInvisibble();
		//SleepUtil.sleepLong();
		sClickAt(Locators.ACCOUNTS, "");
		zWaitForProgressDialogInvisibble();

	}

	public void zNavigateTo(String treeItem) {


	}

	@Override
	public AbsPage zListItem(Action action, String item)
	throws HarnessException {
		logger.info(myPageName() + " zListItem("+ action +", "+ item +")");

		tracer.trace(action +" on subject = "+ item);

		AbsPage page = null;
		SleepUtil.sleepSmall();

		// How many items are in the table?
		String rowsLocator = "css=div#zl__ACCT_MANAGE div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zListGetAccounts: number of accounts: "+ count);
		
		int m= 50;
		if (count >= 50) {
			for (int a1 = 1; a1 <= 10; a1++) { 
				String p0  = rowsLocator + ":nth-child("+m+")";				
				if (this.sIsElementPresent(p0)) {					
					sClickAt(p0,"");
					this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_DOWN);
					m=m+20;
				}
				else
					break;
				
			
		}
			
		}
		
		count = this.sGetCssCount(rowsLocator);
		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {
			final String accountLocator = rowsLocator + ":nth-child("+i+")";
			String locator;

			// Email Address
			locator = accountLocator + " td[id^='account_data_emailaddress']";
			
		
			if (this.sIsElementPresent(locator))
			{
				if (this.sGetText(locator).trim().equalsIgnoreCase(item))
				{
					if (action == Action.A_LEFTCLICK) {
						sClick(locator);
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

		if ( button == Button.B_NEW ) {

			// New button
			//			locator = Locators.zb__ACLV__NEW_MENU_title;
			locator ="";
			// Create the page
			page = new WizardCreateAccount(this);
			
		} else if (button == Button.B_TREE_DELETE) {

			locator=Locators.RIGHT_CLICK_MENU_DELETE_BUTTON;

			page = new DialogForDeleteOperation(this.MyApplication, null);
		} else if (button == Button.B_EDIT) {

			locator=Locators.EDIT_BUTTON;

			page = new FormEditAccount(this.MyApplication);
		} else if (button == Button.B_TREE_EDIT) {

			locator=Locators.RIGHT_CLICK_MENU_EDIT_BUTTON;

			page = new FormEditAccount(this.MyApplication);
		}else if (button == Button.B_CHANGE_PASSWORD) {

			locator=Locators.RIGHT_CLICK_MENU_CHANGE_PASSWORD_BUTTON;
			page = new WizardChangePassword(this);
			
		}else if (button == Button.B_VIEW_MAIL) {

			locator=Locators.RIGHT_CLICK_MENU_VIEW_MAIL_BUTTON;
			
		}else if (button == Button.B_SEARCH_MAIL) {

			locator=Locators.RIGHT_CLICK_MENU_SEARCH_MAIL_BUTTON;
			
		}else if (button == Button.B_CONFIGURE_GRANTS) {
			
			locator = Locators.RIGHT_CLICK_CONFIGURE_GRANTS;			
		
		}else if (button == Button.B_INVALIDATE_SESSIONS) {

			locator=Locators.RIGHT_CLICK_INVALIDATE_SESSIONS;
			
		}else if ( button == Button.B_HOME_ACCOUNT) {

			// New button
			//			locator = Locators.zb__ACLV__NEW_MENU_title;
			locator = PageMain.Locators.HomeAddAcoount;
			// Create the page
			page = new WizardCreateAccount(this);
			this.sClickAt(locator,"");
			return page;
			
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

				page = new WizardCreateAccount(this);

				
			}else if (option == Button.O_NEW_ADMIN) {
				optionLocator = Locators.NEW_ADMIN_USER;

				page = new WizardCreateAdminAccount(this);

			} else if (option == Button.O_EDIT) {
				optionLocator = Locators.EDIT_BUTTON;

				page = new FormEditAccount(this.MyApplication);

			} else if (option == Button.O_DELETE) {
				optionLocator = Locators.DELETE_BUTTON;

				page = new DialogForDeleteOperation(this.MyApplication,null);

			} else if (option == Button.O_CHANGE_PASSWORD) {
				optionLocator = Locators.CHANGE_PASSWORD_BUTTON;

				page = new FormEditAccount(this.MyApplication);

			}  else if (option == Button.B_VIEW_MAIL) {
				optionLocator = Locators.VIEW_MAIL;
				
			}  else if (option == Button.B_SEARCH_MAIL) {
				optionLocator = Locators.SEARCH_MAIL;
				
			}
			else if (option == Button.B_INVALIDATE_SESSIONS) {
				optionLocator = Locators.INVALIDATE_SESSIONS;
				
			}  else if (option == Button.B_CONFIGURE_GRANTS) {
				optionLocator = Locators.CONFIGURE_GRANTS;
				
			}else {
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

	/**
	 * Return a list of all accounts in the current view
	 * @return
	 * @throws HarnessException
	 * @throws HarnessException
	 */
	public List<AccountItem> zListGetAccounts() throws HarnessException {

		List<AccountItem> items = new ArrayList<AccountItem>();

		// Make sure the button exists
		if ( !this.sIsElementPresent("css=div[id='zl__ACCT_MANAGE'] div[id$='__rows']") )
			throw new HarnessException("Account Rows is not present");

		// How many items are in the table?
		String rowsLocator = "//div[@id='zl__ACCT_MANAGE']//div[contains(@id, '__rows')]//div[contains(@id,'zli__')]";
		int count = this.sGetXpathCount(rowsLocator);
		logger.debug(myPageName() + " zListGetAccounts: number of accounts: "+ count);

		
		

		int m= 50;
		if (count >= 50){
			for (int a1 = 1; a1 <= 5; a1++) { 
				String p0  = rowsLocator + "["+ m +"]";
				if (this.sIsElementPresent(p0)){
				zClick(p0);
				this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_DOWN);
				m=m+20;
				}
				else
					break;
				
			
		}
			
		}
		
		count = this.sGetXpathCount(rowsLocator);
		
		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {
			final String accountLocator = rowsLocator + "["+ i +"]";
			String locator;

			AccountItem item = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));

			// Type (image)
			// ImgAdminUser ImgAccount ImgSystemResource (others?)
			locator = accountLocator + "//td[contains(@id, 'account_data_type_')]//div";
			if ( this.sIsElementPresent(locator) ) {
				item.setGAccountType(this.sGetAttribute("xpath=("+ locator + ")@class"));
			}


			// Email Address
			locator = accountLocator + "//td[contains(@id, 'account_data_emailaddress_')]";
			if ( this.sIsElementPresent(locator) ) {
				item.setGEmailAddress(this.sGetText(locator).trim());
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
		if (this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}
	
	public AbsPage zPreferenceCheckboxSet(Button button, boolean status) throws HarnessException {
		logger.info(myPageName() + " zPreferenceSet("+ button +")");
		tracer.trace("Click page button "+ button);

		AbsPage page = null;
		String locator = null;

		SleepUtil.sleepSmall();

		if ( button == Button.B_MUST_CHANGE_PASSWORD ) {

			locator = Locators.MUST_CHANGE_PASSWORD;

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
	
	public Toaster zGetToaster() throws HarnessException {
		SleepUtil.sleepMedium();
		Toaster toaster = new Toaster(this.MyApplication);
		logger.info("toaster is active: "+ toaster.zIsActive());
		return (toaster);
	}
	
	
	
	

}
