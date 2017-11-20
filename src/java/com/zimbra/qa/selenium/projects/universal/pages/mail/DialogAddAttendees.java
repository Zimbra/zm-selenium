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
package com.zimbra.qa.selenium.projects.universal.pages.mail;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;

public class DialogAddAttendees extends AbsDialog {

	public static class Locators {
		public static final String DialogDivLocatorCSS = "css=div[id='YesNoMsgDialog']";

		// Buttons
		public static final String zYesButton = "css=div[id='YesNoMsgDialog_buttons'] td[id^='Yes_'] td[id$='_title']";
		public static final String zNoButton = "css=div[id='YesNoMsgDialog_buttons'] td[id^='No_'] td[id$='_title']";
	}
	
	
	public DialogAddAttendees(AbsApplication application, AbsTab tab) {
		super(application, tab);
		logger.info("new "+ DialogAddAttendees.class.getCanonicalName());
	}
	
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.DialogDivLocatorCSS;
		
		if ( !this.sIsElementPresent(locator) ) {
			return (false); // Not even present
		}
		
		if ( !this.zIsVisiblePerPosition(locator, 0, 0) ) {
			return (false);
		}
	
		logger.info(myPageName() + " zIsActive() = true");
		return (true);
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton("+ button +")");

		tracer.trace("Click dialog button "+ button);

		AbsPage page = null;
		String locator = null;
		
		if ( button == Button.B_YES ) {

			locator = Locators.zYesButton;

		} else if ( button == Button.B_NO ) {

			locator = Locators.zNoButton;

		} else {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		// Make sure the locator was set
		if ( locator == null ) {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Button "+ button +" locator "+ locator +" not present!");
		}

		this.sClick(locator);
		this.zWaitForBusyOverlay();
		SleepUtil.sleepMedium();
		
		if ( button == Button.B_YES ) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}

		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		logger.info(myPageName() + " zGetDisplayedText("+ locator +")");
		
		if ( locator == null )
			throw new HarnessException("locator was null");
		
		return (this.sGetText(locator));
	}

}
