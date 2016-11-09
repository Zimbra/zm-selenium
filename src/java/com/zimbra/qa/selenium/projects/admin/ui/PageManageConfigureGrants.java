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
public class PageManageConfigureGrants extends AbsTab {

	public static class Locators {

		// ** OverviewTreePanel -> Manage -> Aliases

		public static final String GEAR_ICON="css=div.ImgConfigure";
		public static final String ACL_ADD ="css=td[id^='ztabv__ACCT_EDIT_dwt_button_'] td[id$='title']:contains('Add')";
		public static final String GRANTED_ACL = "css=div[id='zl'] table tr:nth-child(2) td div div table";
		public static final String NO_BUTTON="zdlg__MSG__GLOBAL__confirm2btn_button4_title";
		public static final String OK_BUTTON="css=td#zdlg__UNDEFINE";
		public static final String ADD_GRANTS="css=div[id='zmi__zb_currentApp__NEW']";
		public static final String EDIT_GRANTS="css=div[id='zmi__zb_currentApp__EDIT']";
		public static final String DELETE_GRANTS="css=div[id='zmi__zb_currentApp__DELETE']";
		public static final String YES_BUTTON="css=div[id^='zdlg__MSG__']:contains('Info') td[id^='Yes'] td[id$='_button5_title']";
			
	}

	public PageManageConfigureGrants(AbsApplication application) {
		super(application);
		logger.info("new " + myPageName());
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

		String locator = null;			// If set, this will be clicked
		AbsPage page = null;	// If set, this page will be returned

		// Based on the button specified, take the appropriate action(s)
		//

		if ( button == Button.B_ADD ) {

			// Add button
			locator = Locators.ADD_GRANTS;

			// Create the page
			page = new WizardAddACL(this);
				
		}
		else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		if ( locator == null ) {
			throw new HarnessException("locator was null for button "+ button);
		}

		// Default behavior, process the locator by clicking on it
		this.zClickAt(locator,"");

		// If page was specified, make sure it is active
		if ( page != null ) {
			SleepUtil.sleepLong();
		}

		sMouseOut(locator);
		return (page);
	}
	

	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton(" + button + ")");

		tracer.trace("Click dialog button " + button);
		if ( button == null )
			throw new HarnessException("button cannot be null");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_YES) {
			locator = Locators.YES_BUTTON;
			
		} else if (button == Button.B_NO) {
			locator = Locators.NO_BUTTON;
			
		} else if (button == Button.B_OK) {
			for(int i=0;i<=15;i++) {
				if (sIsElementPresent(Locators.OK_BUTTON+i+"_button2_title")) {
					locator=Locators.OK_BUTTON+i+"_button2_title";
					break;
				}
			}
		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		// Make sure the locator was set
		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		// Make sure the locator exists
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator "
					+ locator + " not present!");
		}

		// if (zIsActive())
		// zGetDisplayedText("css=div[class=" + Locators.zDialogContentClassId +
		// "]");
		
		this.sClickAt(locator,"");
		SleepUtil.sleepLong();

		return (page);
	}

	public boolean zVerifyHeader (String header) throws HarnessException {
		if(this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}

	@Override
	public void zNavigateTo() throws HarnessException {
		// TODO Auto-generated method stub	
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

			if (option == Button.B_ADD) {

				optionLocator = Locators.ADD_GRANTS;
				page = new WizardConfigureGrants(this);
			} 
			else if (option == Button.O_EDIT) {
				optionLocator = Locators.EDIT_GRANTS;
				page = new WizardConfigureGrants(this);
				
			}else if (option == Button.O_DELETE) {
				optionLocator = Locators.DELETE_GRANTS;
				page = new DialogForDeleteOperation(this.MyApplication,null);
			
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
			
			this.sFocus(pulldownLocator);
			this.zClickAt(pulldownLocator,"");
			SleepUtil.sleepMedium();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator " + optionLocator + " not present!");
				}

				this.zClickAt(optionLocator,"");

			}

		}

		// Return the specified page, or null if not set
		return (page);
	}	
	
	@Override
	public AbsPage zListItem(Action action, String item)
			throws HarnessException {
		logger.info(myPageName() + " zListItem("+ action +", "+ item +")");

		tracer.trace(action +" on subject = "+ item);

		AbsPage page = null;
		SleepUtil.sleepMedium();

		// How many items are in the table?
		String rowsLocator = "css=div[id='zl'] div[id$='__rows'] div[id^='zli__']";
		int count = this.sGetCssCount(rowsLocator);
		logger.debug(myPageName() + " zListGetPolicy: number of policys: "+ count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {
			final String aceLocator = rowsLocator + ":nth-child("+i+")";
			String locator;

			// Email Address
			locator = aceLocator + " td";


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
	
	public boolean zVerifyACE (String item) throws HarnessException {
		if(this.sIsElementPresent("css=div[id='zl'] div[id$='__rows'] div[id^='zli__']:contains('" + item + "')"))
			return true;
		return false;
	}
}
