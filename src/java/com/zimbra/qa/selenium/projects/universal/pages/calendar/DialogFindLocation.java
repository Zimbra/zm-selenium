/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.pages.calendar;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.universal.pages.DialogWarning;

public class DialogFindLocation extends DialogWarning {

	// The ID for the main Dialog DIV
	public static final String LocatorDivID = "SEND_UPDATES_DIALOG";
		
	public DialogFindLocation(AbsApplication application, AbsTab page) {
		super(new DialogWarningID(LocatorDivID), application, page);
				
		logger.info("new " + DialogFindLocation.class.getCanonicalName());
	}
	public static class Locators {

		public static final String LocationPickerSerach="css=div[class='DwtDialog'] td[id$='_title']:contains('Search')";
		public static final String SelectLocationFromPicker="css=div[class='DwtDialog'] td[id$='_title']:contains('Select')";
		public static final String AddLocationFromPicker="css=div[class='DwtDialog']  td[id='ZmAttendeePicker_LOCATION_button2_title']";
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Click dialog button " + button);
		if ( button == null )
			throw new HarnessException("button cannot be null");
	
		String locator = null;
		AbsPage page = null;
		boolean waitForPostfix = false;

		if (button == Button.B_SEARCH_LOCATION) {

			locator = Locators.LocationPickerSerach;
			page = null;

		} else if (button == Button.B_SELECT_LOCATION) {

			locator = Locators.SelectLocationFromPicker;
			page = null;
		
		} else if (button == Button.B_OK) {

			locator= Locators.AddLocationFromPicker;
			page = null;
		
		} else if (button == Button.B_CANCEL) {

			locator = "css=div[class='DwtDialog'] td[id$='_button1_title']";
			page = null;
			                              
		} else {
			
			return ( super.zPressButton(button) );

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
		this.sFocus(locator);
		this.sGetCssCount(locator);
		this.sClickAt(locator, "10,10");
		this.zWaitForBusyOverlay();
		SleepUtil.sleepMedium();
		
		// This dialog could send messages, so wait for the queue
		if ( waitForPostfix ) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}

		return (page);
	}

}

