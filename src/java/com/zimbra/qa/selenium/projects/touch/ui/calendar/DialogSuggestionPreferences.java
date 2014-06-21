/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.touch.ui.calendar;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew.Locators;

public class DialogSuggestionPreferences extends DialogWarning {

	// The ID for the main Dialog DIV
	public static final String LocatorDivID = "SUGGESTION_PREFERENCES";
		
	public DialogSuggestionPreferences(AbsApplication application, AbsTab page) {
		super(new DialogWarningID(LocatorDivID), application, page);
				
		logger.info("new " + DialogSuggestionPreferences.class.getCanonicalName());
	}

	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton(" + button + ")");
		
		tracer.trace("Click dialog button " + button);
		if ( button == null )
			throw new HarnessException("button cannot be null");
	
		String locator = null;
		AbsPage page = null;

		if (button == Button.B_ONLY_INCLUDE_MY_WORKING_HOURS) {

			locator = Locators.OnlyIncludeMyWorkingHoursCheckBox;
			page = null;

		} else if (button == Button.B_ONLY_INCLUDE_OTHER_ATTENDEES_WORKING_HOURS) {

			locator = Locators.OnlyIncludeOtherAttendeeCheckBox;
			page = null;
		
		} else if (button == Button.B_OK) {

			locator = Locators.OKButtonSuggestionPreferencesDialog;
			page = null;
		
		} else if (button == Button.B_CANCEL) {

			locator = Locators.CancelButtonSuggestionPreferencesDialog;
			page = null;
			                              
		} else {
			
			return ( super.zClickButton(button) );

		}
		
		// Make sure the locator was set
		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		SleepUtil.sleepMedium();
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator "
					+ locator + " not present!");
		}
		
		this.sClickAt(locator, "");
		this.zWaitForBusyOverlay();
		
		// If page was specified, make sure it is active
		if ( page != null ) {
			// This function (default) throws an exception if never active
			page.zWaitForActive();
			
		}

		// This dialog could send messages, so wait for the queue
		Stafpostqueue sp = new Stafpostqueue();
		sp.waitForPostqueue();

		return (page);
	}
	
	public AbsPage zType(Button editField, String editFieldValue) throws HarnessException {
		
		logger.info(myPageName() + " zType(" + editField + ")");

		tracer.trace("Type value in " + editField);
		
		if ( editField == null )
			throw new HarnessException("Edit field cannot be null");
	
		String locator = null;
		AbsPage page = null;

		if (editField == Button.F_NAME_EDIT_FIELD) {

			locator = Locators.NameLocationPreferencesField;
			page = null;
               
		} else {
			
			throw new HarnessException("Edit field " + editField + " not implemented");

		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Edit field " + editField + " locator "
					+ locator + " not present!");
		}
		
		this.sType(locator, editFieldValue);
		this.zWaitForBusyOverlay();
		
		// If page was specified, make sure it is active
		if ( page != null ) {
			// This function (default) throws an exception if never active
			page.zWaitForActive();
			
		}

		return (page);
	}

}

