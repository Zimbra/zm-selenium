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
package com.zimbra.qa.selenium.projects.ajax.ui.calendar;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew.Locators;

public class DialogSendUpdatetoAttendees extends DialogWarning {

	// The ID for the main Dialog DIV
	public static final String LocatorDivID = "SEND_UPDATES_DIALOG";
		
	public DialogSendUpdatetoAttendees(AbsApplication application, AbsTab page) {
		super(new DialogWarningID(LocatorDivID), application, page);
				
		logger.info("new " + DialogSendUpdatetoAttendees.class.getCanonicalName());
	}

	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton(" + button + ")");
		
		tracer.trace("Click dialog button " + button);
		if ( button == null )
			throw new HarnessException("button cannot be null");
	
		String locator = null;
		AbsPage page = null;

		if (button == Button.B_SEND_UPDATES_ONLY_TO_ADDED_OR_REMOVED_ATTENDEES) {

			locator = Locators.SendUpdatesToAddedRemovedRadioButton;
			page = null;

		} else if (button == Button.B_SEND_UPDATES_TO_ALL_ATTENDEES) {

			locator = Locators.SendUpdatesToAllRadioButton;
			page = null;
		
		} else if (button == Button.B_OK) {

			locator = "css=div[id='SEND_NOTIFY_DIALOG'][class='DwtDialog'] div[id='SEND_NOTIFY_DIALOG_buttons'] td[id='SEND_NOTIFY_DIALOG_button2_title']";
			page = null;
		
		} else if (button == Button.B_CANCEL) {

			locator = "css=div[id='SEND_NOTIFY_DIALOG'][class='DwtDialog'] div[id='SEND_NOTIFY_DIALOG_buttons'] td[id='SEND_NOTIFY_DIALOG_button1_title']";
			page = null;
			                              
		} else {
			
			return ( super.zClickButton(button) );

		}
		
		// Make sure the locator was set
		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator "	+ locator + " not present!");
		}
		
		this.sClickAt(locator, "");
		this.zWaitForBusyOverlay();

		if (button == Button.B_OK) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}

		return (page);
	}

}

