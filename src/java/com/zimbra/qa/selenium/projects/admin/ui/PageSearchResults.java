/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
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
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;



public class PageSearchResults extends AbsTab {
	public static class Locators {
		public static final String SEARCH_INPUT_TEXT_BOX="_XForm_query_display";
		public static final String SEARCH_BUTTON="css=td.xform_container div.ImgSearch";
		public static final String DELETE_BUTTON="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgDelete']";
		public static final String DELETE_BUTTON_DISABLED="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgDelete ZDisabledImage']";
		public static final String RIGHT_CLICK_MENU_DELETE_BUTTON="css=div[id='zm__SCHLV__MENU_POP'] div[class='ImgDelete']";
		public static final String RIGHT_CLICK_MENU_DELETE_BUTTON_DISABLED="css=div[id='zm__SCHLV__MENU_POP'] div[class='ImgDelete ZDisabledImage']";
		public static final String RIGHT_CLICK_MENU_EDIT_BUTTON="css=div[id='zm__SCHLV__MENU_POP'] div[class='ImgEdit']";
		public static final String EDIT_BUTTON="css=div[id='zm__zb_currentApp__MENU_POP']  div[class='ImgEdit']";
		public static final String GEAR_ICON="css=div.ImgConfigure";
		public static final String zArrowSelectSearchObject		="css=td[id*='dropdown'] div[class='ImgSelectPullDownArrow']";
		public static final String zCosSearchObject = "css=div[id='zmi__SEARCH_COSES'] td[id='zmi__SEARCH_COSES_title']";
	}

	public static class TypeOfObject {
		public static final String ACCOUNT = "Account";
		public static final String ALIAS = "ALIAS";
		public static final String RESOURCE = "RESOURCE";
		public static final String DISTRIBUTION_LIST = "Distribution List";
		public static final String COS = "Cos";
		public static final String DOMAIN = "Domain";
		public static final String DOMAIN_ALIAS="Domain Alias";
	}

	public String typeOfObject = "";
	
	//  Search Object from the dropdown in search panel
	
	public String S_ACCOUNT = "Account";
	public String S_ALIAS = "ALIAS";
	public String S_RESOURCE = "RESOURCE";
	public String S_DISTRIBUTION_LIST = "Distribution List";
	public String S_COS = "Cos";
	public String S_DOMAIN = "Domain";
	public String S_ALL_OBJECTS="All Objects";

	public String getType() {
		return typeOfObject;
	}

	public void setType(String type) {
		this.typeOfObject = type;
	}

	public PageSearchResults(AbsApplication application) {
		super(application);
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		throw new HarnessException("implement me");
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zNavigateTo() throws HarnessException {
		throw new HarnessException("implement me");
	}

	/**
	 * Enter text into the query string field
	 * @param query
	 * @throws HarnessException
	 */
	public void zAddSearchQuery(String query) throws HarnessException {
		logger.info(myPageName() + " zAddSearchQuery("+ query +")");

		tracer.trace("Search for the query "+ query);

		this.sType(Locators.SEARCH_INPUT_TEXT_BOX, query);

	}

	@Override
	public AbsPage zListItem(Action action, String entity) throws HarnessException {
		logger.info(myPageName() + " zListItem("+ action +", "+ entity +")");

		tracer.trace(action +" on subject = "+ entity);

		AbsPage page = null;

		// How many items are in the table?
		String rowsLocator = "css=div#zl__SEARCH_MANAGE div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zListGetAccounts: number of accounts: "+ count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {
			final String accountLocator = rowsLocator + ":nth-child("+i+")";
			String locator;

			// Email Address
			locator = accountLocator + " td[id^='SEARCH_MANAGE_data_emailaddress']";


			if(this.sIsElementPresent(locator))
			{
				if(this.sGetText(locator).trim().equalsIgnoreCase(entity))
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

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {

		logger.info(myPageName() + " zToolbarPressButton("+ button +")");

		tracer.trace("Click button "+ button);

		if ( button == null )
			throw new HarnessException("Button cannot be null!");

		// Default behavior variables
		//
		String locator = null;	// If set, this will be clicked
		AbsPage page = null;	// If set, this page will be returned

		// Based on the button specified, take the appropriate action(s)
		//

		if ( button == Button.B_SEARCH ) {

			locator = Locators.SEARCH_BUTTON;
			page = new PageSearchResults(MyApplication);

			// Make sure the button exists
			if ( !this.sIsElementPresent(locator) )
				throw new HarnessException("Button is not present locator="+ locator +" button="+ button);

			// FALL THROUGH

		} else if(button == Button.B_TREE_DELETE) {
			locator = Locators.RIGHT_CLICK_MENU_DELETE_BUTTON;
			page = new DialogForDeleteOperation(this.MyApplication, null);

			// Make sure the button exists
			if ( !this.sIsElementPresent(locator) )
				throw new HarnessException("Button is not present locator="+ locator +" button="+ button);

			// FALL THROUGH
		} else if(button == Button.B_TREE_EDIT) {
			locator = Locators.RIGHT_CLICK_MENU_EDIT_BUTTON;

			if(typeOfObject.equals(TypeOfObject.DISTRIBUTION_LIST))
				page=new FormEditDistributionList(this.MyApplication);
			else if(typeOfObject.equals(TypeOfObject.ACCOUNT))
				page=new FormEditAccount(this.MyApplication);
			else if(typeOfObject.equals(TypeOfObject.RESOURCE))
				page=new FormEditResource(this.MyApplication);
			else if(typeOfObject.equals(TypeOfObject.ALIAS))
				page=new FormEditAccount(this.MyApplication);
			else if(typeOfObject.equals(TypeOfObject.COS))
				page=new FormEditCos(this.MyApplication);
			else if(typeOfObject.equals(TypeOfObject.DOMAIN))
				page=new FormEditDomain(this.MyApplication);
			else if (typeOfObject.equals(TypeOfObject.DOMAIN_ALIAS))
				page=new WizardCreateDomainAlias(this);

			// Make sure the button exists
			if ( !this.sIsElementPresent(locator) )
				throw new HarnessException("Button is not present locator="+ locator +" button="+ button);

			// FALL THROUGH
		}else if(button == Button.B_DELETE) {
				locator = Locators.DELETE_BUTTON;
				page = new DialogForDeleteOperation(this.MyApplication,null);

			// FALL THROUGH
		}else{
			throw new HarnessException("no logic defined for button "+ button);
		}

		if ( locator == null ) {
			throw new HarnessException("locator was null for button "+ button);
		}

		// Default behavior, process the locator by clicking on it
		//

		// Click it
		this.zClick(locator);


		// If page was specified, make sure it is active
		if ( page != null ) {

			// This function (default) throws an exception if never active
			//page.zWaitForActive();
			SleepUtil.sleepMedium();

		}

		sMouseOut(locator);
		return (page);


	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option)
	throws HarnessException {
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

			if (option == Button.O_DELETE) {

				pulldownLocator = Locators.GEAR_ICON;
				optionLocator = Locators.DELETE_BUTTON;

				page = new DialogForDeleteOperation(this.MyApplication,null);

				// FALL THROUGH

			} else if (option == Button.O_EDIT) {

				pulldownLocator = Locators.GEAR_ICON;
				optionLocator = Locators.EDIT_BUTTON;

				if(typeOfObject.equals(TypeOfObject.DISTRIBUTION_LIST))
					page=new FormEditDistributionList(this.MyApplication);
				else if(typeOfObject.equals(TypeOfObject.ACCOUNT))
					page=new FormEditAccount(this.MyApplication);
				else if(typeOfObject.equals(TypeOfObject.RESOURCE))
					page=new FormEditResource(this.MyApplication);
				else if(typeOfObject.equals(TypeOfObject.ALIAS))
					page=new FormEditAccount(this.MyApplication);
				else if(typeOfObject.equals(TypeOfObject.COS))
					page=new FormEditCos(this.MyApplication);
				else if(typeOfObject.equals(TypeOfObject.DOMAIN))
					page=new FormEditDomain(this.MyApplication);
				else if (typeOfObject.equals(TypeOfObject.DOMAIN_ALIAS))
					page=new WizardCreateDomainAlias(this);
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

			// If the app is busy, wait for it to become active
			//zWaitForBusyOverlay();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator " + optionLocator + " not present!");
				}

				this.zClickAt(optionLocator,"");

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
		if ( !this.sIsElementPresent("css=div[id='zl__SEARCH_MANAGE'] div[id$='__rows']") )
			throw new HarnessException("Account Rows is not present");

		// How many items are in the table?
		String rowsLocator = "css=div#zl__SEARCH_MANAGE div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zListGetAccounts: number of accounts: "+ count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {
			final String accountLocator = rowsLocator + ":nth-child("+i+")";
			String locator;

			AccountItem item = new AccountItem("email" + ZimbraSeleniumProperties.getUniqueString(),ZimbraSeleniumProperties.getStringProperty("testdomain"));

			// Type (image)
			// ImgAdminUser ImgAccount ImgSystemResource (others?)
			locator = accountLocator + " td[id^='SEARCH_MANAGE_data_type'] div";
			if ( this.sIsElementPresent(locator) ) {
				item.setGAccountType(this.sGetAttribute(locator + "@class"));
			}


			// Email Address
			locator = accountLocator + " td[id^='SEARCH_MANAGE_data_emailaddress']";
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

	public boolean zVerifyDisabled(String  buttonID)throws HarnessException{
		if(buttonID=="DeleteContext"){
			boolean test= this.sIsElementPresent(Locators.DELETE_BUTTON_DISABLED);
			return test;
		}else if(buttonID=="DeleteTreeMenu"){
			return this.sIsElementPresent(Locators.RIGHT_CLICK_MENU_DELETE_BUTTON_DISABLED);
		}else{
			return false;
		}

	}
	
	public void zSelectSearchObject(String object)throws HarnessException{
		
		if(object==S_COS){
			
			this.sClickAt(Locators.zArrowSelectSearchObject, "");
			this.sClickAt(Locators.zCosSearchObject, "");
		
		
		}else{
			throw new HarnessException("Not imeplemented for "+object+"Object");
		}
	}
		
}
