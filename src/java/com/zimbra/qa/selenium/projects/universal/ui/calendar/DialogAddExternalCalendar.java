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
package com.zimbra.qa.selenium.projects.universal.ui.calendar;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;

/**
 * Represents a "Confirmation" dialog box
 * <p>
 */
public class DialogAddExternalCalendar extends AbsDialog {

	public static class Locators {
		public static final String DialogDivID = "ADD_EXTERNAL_CAL_DIALOG";
		public static final String DialogDivCss = "css=div[id='"+ DialogDivID +"']";
		
		public static final String RadioGoogleCss = "css=input[id$='_shareRadioGoogle']";
		public static final String RadioYahooCss = "css=input[id$='_shareRadioYahoo']";
		public static final String RadioOtherCss = "css=input[id$='_shareRadioOther']";
		
	}

	public DialogAddExternalCalendar(AbsApplication application, AbsTab page) {
		super(application, page);
				
		logger.info("new " + DialogAddExternalCalendar.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton(" + button + ")");

		tracer.trace("Click dialog button " + button);
		if ( button == null )
			throw new HarnessException("button cannot be null");
	
		String locator = null;
		AbsPage page = null; 

		if (button == Button.B_NEXT) {
			
			locator = Locators.DialogDivCss + " div[id$='_buttons'] td[id^='Next_'] td[id$='_title']";
			page = null;
			
		} else if (button == Button.B_CANCEL) {
			
			locator = Locators.DialogDivCss + " div[id$='_buttons'] td[id^='Cancel_'] td[id$='_title']";
			page = null; // Probably need to have a FormMailNew
			
		} else if (button == Button.B_BACK) {
			
			locator = Locators.DialogDivCss + " div[id$='_buttons'] td[id^='Back_'] td[id$='_title']";
			page = null; // Probably need to have a FormMailNew
			
		} else {
			
			throw new HarnessException("no logic defined for button "+ button);

		}

		// Make sure the locator exists
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator "
					+ locator + " not present!");
		}

		this.zClickAt(locator,"0,0");

		// If the app is busy, wait for it to become active
		this.zWaitForBusyOverlay();
		
		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		logger.info(myPageName() + " zGetDisplayedText(" + locator + ")");

		if (locator == null)
			throw new HarnessException("locator was null");

		return (this.sGetText(locator));
	}
	
	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		// See https://bugzilla.zimbra.com/show_bug.cgi?id=66576 
		String locator = Locators.DialogDivCss;

		if (!this.sIsElementPresent(locator)) {
			return (false); // Not even present
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false); // Not even present
		}

		logger.info(myPageName() + " zIsActive() = true");
		return (true);

	}
	
	public static enum SourceType {
		Google, Yahoo, Other
	}
	
	public void zSetSourceType(SourceType type) throws HarnessException {
		logger.info(myPageName() + " zSetSourceType("+ type +")");
		
		String locator = null;
		if ( type == SourceType.Google ) {
			
			locator = Locators.RadioGoogleCss;
			
		} else if ( type == SourceType.Yahoo ) {
			
			locator = Locators.RadioYahooCss;
			
		} else if ( type == SourceType.Other ) {
			
			locator = Locators.RadioOtherCss;
			
		} else {
			throw new HarnessException("unknown calendar type: "+ type);
		}
		
		if ( !sIsElementPresent(locator) ) {
			throw new HarnessException("locator not present: "+ locator);
		}
		
		this.sClick(locator);
		this.zWaitForBusyOverlay();
		
	}
	
	public static enum CalendarType {
		CalDav, iCal, Other
	}
	
	public void zSetCalendarType(CalendarType type) throws HarnessException {
		logger.info(myPageName() + " zSetCalendarType("+ type +")");
	}
	
	public void zSetSourceEmailAddress(String address) throws HarnessException {
		logger.info(myPageName() + " zSetSourceEmailAddress("+ address +")");

		String locator = "css=input#ADD_EXTERNAL_CAL_DIALOG_syncUserNameInput";
		this.sType(locator, address);
		this.zWaitForBusyOverlay();
		
	}
	
	public void zSetSourcePassword(String password) throws HarnessException {
		logger.info(myPageName() + " zSetSourceEmailAddress("+ password +")");

		String locator = "css=input#ADD_EXTERNAL_CAL_DIALOG_syncPasswordInput";
		this.sType(locator, password);
		this.zWaitForBusyOverlay();
		
	}
	
	public void zSetSourceServer(String server) throws HarnessException {
		logger.info(myPageName() + " zSetSourceEmailAddress("+ server +")");

		String locator = "css=input#ADD_EXTERNAL_CAL_DIALOGsyncUrlInput";
		this.sType(locator, server);
		this.zWaitForBusyOverlay();
	
		
		SleepUtil.sleepMedium();

	}
	


}

