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
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.admin.items.DomainItem;

public class PageManageDomains extends AbsTab {

	public static class Locators {
		public static final String CONFIGURE_ICON="css=div.ImgAdministration";
		public static final String DOMAINS="zti__AppAdmin__CONFIGURATION__DOMAINS_textCell";
		public static final String GEAR_ICON="css=div.ImgConfigure";
		public static final String NEW_MENU="css=td[id='zmi__zb_currentApp__NEW_title']:contains('New')";
		public static final String ADD_DOMAIN_ALIAS="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgDomainAlias']";
		public static final String HOME="Home";
		public static final String CONFIGURE="Configure";
		public static final String DOMAIN="Domains";
		public static final String DELETE_BUTTON="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgDelete']";
		public static final String EDIT_BUTTON="css=td[id='zmi__zb_currentApp__EDIT_title']:contains('Edit')";
		public static final String RIGHT_CLICK_MENU_DELETE_BUTTON="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgDelete']";
		public static final String RIGHT_CLICK_MENU_EDIT_BUTTON="css=td[id='zmi__DMLV__EDIT_title']:contains('Edit')";
		public static final String DOMAIN_EDIT_ACL="css=div[id^='zti__AppAdmin__CONFIGURATION__DOMAINS'] div[class='ZTreeItemTextCell']:contains('ACL')";
		public static final String DOMAIN_ACCOUNTS_LIMITS="css=div[id^='zti__AppAdmin__CONFIGURATION__DOMAINS'] div[class='ZTreeItemTextCell']:contains('Account Limits')";
		public static final String DOMAIN_ACCOUNTS_LIMITS_ADD = "css=div[id$='account_form_account_limits_tab'] td[class='ZWidgetTitle']:contains('Add')";
		public static final String DOMAIN_ACCOUNTS_LIMITS_COS_NAME = "css=table[id='zdlgv__UNDEFINE1_group_table']  tbody tr td[id='zdlgv__UNDEFINE1_cos___container'] input";
		public static final String DOMAIN_ACCOUNTS_LIMITS_COS_LIMIT = "css=table[id='zdlgv__UNDEFINE1_group_table'] tbody tr td[id='zdlgv__UNDEFINE1_limits___container'] input";
		public static final String MAXIMUM_ACCOUNTS_FOR_DOMAIN = "css=input[id='ztabv__DOAMIN_EDIT_zimbraDomainMaxAccounts']";
		public static final String DOMAIN_ACCOUNTS_LIMITS_AT_COS_OK = "css=td[id^='zdlg__UNDEFINE']:contains('OK')";
		
		
	}

	public static class TypeOfObject {
		public static final String DOMAIN = "Domain";
		public static final String DOMAIN_ALIAS = "Domain Alias";

	}

	public String typeOfObject = "Domain";


	public String getType() {
		return typeOfObject;
	}

	public void setType(String type) {
		this.typeOfObject = type;
	}


	public PageManageDomains(AbsApplication application) {
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
		SleepUtil.sleepLong();

		zClickAt(Locators.CONFIGURE_ICON,"");
		sIsElementPresent(Locators.DOMAINS);
		SleepUtil.sleepLong();
		zClickAt(Locators.DOMAINS, "");
		SleepUtil.sleepMedium();
		zWaitForActive();
	}

	@Override
	public AbsPage zListItem(Action action, String item)
			throws HarnessException {
		logger.info(myPageName() + " zListItem("+ action +", "+ item +")");

		tracer.trace(action +" on subject = "+ item);

		AbsPage page = null;
		SleepUtil.sleepSmall();

		// How many items are in the table?
		String rowsLocator = "css=div#zl__DOMAIN_MANAGE div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zListGetAccounts: number of accounts: "+ count);
		int m= 50;
		if(count >= 50){
			for (int a1 = 1; a1 <= 8; a1++) { 
				String p0  = rowsLocator + ":nth-child("+m+")";
				if(this.sIsElementPresent(p0)){
				zClick(p0);
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
			locator = accountLocator +  " table tr td:nth-child(2)" ;


			if(this.sIsElementPresent(locator))
			{
				if(this.sGetText(locator).trim().equalsIgnoreCase(item))
				{
					if(action == Action.A_LEFTCLICK) {
						SleepUtil.sleepMedium();
						zClickAt(locator,"");
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


		// Default behavior variables
		//
		String locator = null;			// If set, this will be clicked
		AbsPage page = null;	// If set, this page will be returned

		// Based on the button specified, take the appropriate action(s)
		//

		if ( button == Button.B_NEW ) {

			// New button
			locator = Locators.DOMAINS;

			// Create the page
			page = new WizardCreateDomain(this);

			// FALL THROUGH

		} else if(button == Button.B_TREE_DELETE) {
			locator = Locators.RIGHT_CLICK_MENU_DELETE_BUTTON;

			page = new DialogForDeleteOperationDomain(this.MyApplication,null);

		} else if(button == Button.B_TREE_EDIT) {
			locator = Locators.RIGHT_CLICK_MENU_EDIT_BUTTON;

			if (typeOfObject.equals(TypeOfObject.DOMAIN))
				page=new FormEditDomain(this.MyApplication);
			else if (typeOfObject.equals(TypeOfObject.DOMAIN_ALIAS))
				page=new WizardCreateDomainAlias(this);

		}else if ( button == Button.B_HOME_DOMAIN ) {

			// New button
			locator = PageMain.Locators.HomeCreateDomain;
			// Create the page
			page = new WizardCreateDomain(this);
			this.sClickAt(locator,"");
			return page;
			// FALL THROUGH

		} else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		if ( locator == null ) {
			throw new HarnessException("locator was null for button "+ button);
		}

		// Default behavior, process the locator by clicking on it
		//
		
	
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

				page = new WizardCreateDomain(this);

				// FALL THROUGH

			} else if(option == Button.O_ADD_DOMAIN_ALIAS) {
				optionLocator = Locators.ADD_DOMAIN_ALIAS;

				page = new WizardCreateDomainAlias(this);

			} else if(option == Button.O_DELETE) {
				optionLocator = Locators.DELETE_BUTTON;

				page = new DialogForDeleteOperationDomain(this.MyApplication,null);

			} else if(option == Button.O_EDIT) {
				optionLocator = Locators.EDIT_BUTTON;

				if (typeOfObject.equals(TypeOfObject.DOMAIN))
					page=new FormEditDomain(this.MyApplication);
				else if (typeOfObject.equals(TypeOfObject.DOMAIN_ALIAS))
					page=new WizardCreateDomainAlias(this);

			} else {
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

			this.sClickAt(pulldownLocator, "");

			// If the app is busy, wait for it to become active
			//zWaitForBusyOverlay();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator " + optionLocator + " not present!");
				}

				SleepUtil.sleepMedium();
				this.sClickAt(optionLocator,"");

				// If the app is busy, wait for it to become active
				//zWaitForBusyOverlay();
			}

		}

		// Return the specified page, or null if not set
		return (page);

	}

	public boolean zVerifyHeader (String header) throws HarnessException {
		if(this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}

	/**
	 * Return a list of all domain entries in the current view
	 * @return
	 * @throws HarnessException
	 * @throws HarnessException
	 */
	public List<DomainItem> zListGetDomainList() throws HarnessException {

		List<DomainItem> items = new ArrayList<DomainItem>();

		// Make sure the button exists
		if ( !this.sIsElementPresent("css=div[id='zl__DOMAIN_MANAGE'] div[id$='__rows']") )
			throw new HarnessException("Account Rows is not present");

		// How many items are in the table?
		String rowsLocator = "//div[@id='zl__DOMAIN_MANAGE']//div[contains(@id, '__rows')]//div[contains(@id,'zli__')]";
		int count = this.sGetXpathCount(rowsLocator);
		logger.debug(myPageName() + " zListGetdomain: number of domain: "+ count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {
			final String domainLocator = rowsLocator + "["+ i +"]";
			String locator;

			DomainItem item = new DomainItem();

			// Type (image)
			// ImgAdminUser ImgAccount ImgSystemResource (others?)
			locator = domainLocator + "//td[contains(@id,'domain_data_name')]";
			if ( this.sIsElementPresent(locator) ) {
				item.setName(this.sGetText(locator).trim());
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
	
	
	public AbsPage zSetAccountLimitPerCos(String  cos_name, String limit) throws HarnessException {
		logger.info(myPageName() + " zSetAccountLimitPerCos("+ cos_name +", "+ limit +")");

		tracer.trace("Enter cos name and limit "+ cos_name +" then "+ limit);


		// Default behavior variables
		AbsPage page = null; // If set, this page will be returned

		//Click on accounts limit tab
		this.zClickAt(Locators.DOMAIN_ACCOUNTS_LIMITS,"");
		
		//Click on Add button
		this.zClickAt(Locators.DOMAIN_ACCOUNTS_LIMITS_ADD,"");
		
		SleepUtil.sleepLong();
		
		//Enter COS name
		if ( this.sIsElementPresent(Locators.DOMAIN_ACCOUNTS_LIMITS_COS_NAME) ) {
			this.sFocus(Locators.DOMAIN_ACCOUNTS_LIMITS_COS_NAME);
			this.sType(Locators.DOMAIN_ACCOUNTS_LIMITS_COS_NAME, cos_name);	
		}	
		
		//Enter Limit
		if ( this.sIsElementPresent(Locators.DOMAIN_ACCOUNTS_LIMITS_COS_LIMIT) ) {
			this.sFocus(Locators.DOMAIN_ACCOUNTS_LIMITS_COS_LIMIT);
			this.sType(Locators.DOMAIN_ACCOUNTS_LIMITS_COS_LIMIT, limit);
		
		}
		
		//Click on OK button
		this.zClickAt(Locators.DOMAIN_ACCOUNTS_LIMITS_AT_COS_OK,"");
		
		// Return the specified page, or null if not set
		return (page);

	}
	
	public AbsPage zSetAccountLimitOnDomain(String limit) throws HarnessException {
		logger.info(myPageName() + " zSetAccountLimitPerCos("+ limit +")");

		tracer.trace("Enter cos name and limit "+ limit);


		// Default behavior variables
		AbsPage page = null; // If set, this page will be returned
		

		//Click on accounts limit tab
		this.zClickAt(Locators.DOMAIN_ACCOUNTS_LIMITS,"");
		
		SleepUtil.sleepMedium();
			
		//Enter Limit
		if ( this.sIsElementPresent(Locators.MAXIMUM_ACCOUNTS_FOR_DOMAIN) ) {
			this.sFocus(Locators.MAXIMUM_ACCOUNTS_FOR_DOMAIN);
			this.sType(Locators.MAXIMUM_ACCOUNTS_FOR_DOMAIN, limit);
		
		}
		
		// Return the specified page, or null if not set
		return (page);

	}
	
	public AbsPage zSetAccountLimit(String limit) throws HarnessException {
		logger.info(myPageName() + " zSetAccountLimitPerCos("+ limit +")");

		tracer.trace("Enter cos name and limit "+ limit);


		// Default behavior variables
		AbsPage page = null; // If set, this page will be returned

		//Click on accounts limit tab
		this.zClickAt(Locators.DOMAIN_ACCOUNTS_LIMITS,"");
		
		SleepUtil.sleepMedium();
			
		//Enter Limit
		if ( this.sIsElementPresent(Locators.MAXIMUM_ACCOUNTS_FOR_DOMAIN) ) {
			this.sFocus(Locators.MAXIMUM_ACCOUNTS_FOR_DOMAIN);
			this.sType(Locators.MAXIMUM_ACCOUNTS_FOR_DOMAIN, limit);
		
		}
		
		// Return the specified page, or null if not set
		return (page);

	}
	
	
	/**
	 * Press the toolbar button
	 * @param button
	 * @return
	 * @throws HarnessException
	 */
	public AbsPage zToolbarPressButton(Button button, String cos_name,String limit) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton("+ button +")");
		
		tracer.trace("Click button "+ button);

		// Fallthrough objects
		AbsPage page = null;
		
		//Click on accounts limit tab
		this.zClickAt(Locators.DOMAIN_ACCOUNTS_LIMITS,"");
		
		if ( button == Button.B_ACCOUNTS_LIMIT_PER_DOMAIN ) {
			
			this.sFocus(Locators.MAXIMUM_ACCOUNTS_FOR_DOMAIN);
			this.sType(Locators.MAXIMUM_ACCOUNTS_FOR_DOMAIN, limit);
			page = this;
		
		} else if ( button == Button.B_ACCOUNTS_LIMIT_PER_COS ) {

			//Click on Add button
			this.zClickAt(Locators.DOMAIN_ACCOUNTS_LIMITS_ADD,"");
			
			SleepUtil.sleepLong();
			
			//Enter COS name
			if ( this.sIsElementPresent(Locators.DOMAIN_ACCOUNTS_LIMITS_COS_NAME) ) {
				this.sFocus(Locators.DOMAIN_ACCOUNTS_LIMITS_COS_NAME);
				this.sType(Locators.DOMAIN_ACCOUNTS_LIMITS_COS_NAME, cos_name);	
			}	
			
			//Enter Limit
			if ( this.sIsElementPresent(Locators.DOMAIN_ACCOUNTS_LIMITS_COS_LIMIT) ) {
				this.sFocus(Locators.DOMAIN_ACCOUNTS_LIMITS_COS_LIMIT);
				this.sType(Locators.DOMAIN_ACCOUNTS_LIMITS_COS_LIMIT, limit);
				
				//Click on OK button
				this.zClickAt(Locators.DOMAIN_ACCOUNTS_LIMITS_AT_COS_OK,"");
		} 
			page = this;
		}
		else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		return page;
	}

}

