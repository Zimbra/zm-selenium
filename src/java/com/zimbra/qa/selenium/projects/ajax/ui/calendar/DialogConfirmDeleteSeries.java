/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;

/**
 * Represents a "Delete Recurring Item(s)" dialog box,
 * for an appointment without attendees.
 *
 * Two new options
 * - Delete This Instance
 * - Delete The Series
 *
 * No new buttons on this dialog, just OK and Cancel
 * <p>
 */
public class DialogConfirmDeleteSeries extends DialogWarning {

	// The ID for the main Dialog DIV
	public static final String LocatorDivID = "CONFIRM_DELETE_APPT_DIALOG";

	public static class Locators {

	}

	public DialogConfirmDeleteSeries(AbsApplication application, AbsTab page) {
		super(new DialogWarningID(LocatorDivID), application, page);

		logger.info("new " + DialogConfirmDeleteSeries.class.getCanonicalName());
	}



	public AbsPage zCheckRadioButton(Button button) throws HarnessException {

		if (button == null)
			throw new HarnessException("Radio button cannot be null!");

		logger.info(myPageName() + " zCheckRadioButton(" + button + ")");

		tracer.trace("Check the radio " + button + " button");


		String locator = null;
		AbsPage page = null;

		// Based on the button specified, take the appropriate action(s)

		if (button == Button.B_DELETE_THIS_INSTANCE) {

			locator = "implement me!";
			page = null;

		} else if (button == Button.B_DELETE_THE_SERIES) {

			locator = "implement me!";
			page = null;

		} else {
			throw new HarnessException("no logic defined for radio button " + button);
		}

		// Default behavior, process the locator by clicking on it
		sClick(locator);

		// If the app is busy, wait for it to become active
		this.zWaitForBusyOverlay();

		return (page);
	}

}

