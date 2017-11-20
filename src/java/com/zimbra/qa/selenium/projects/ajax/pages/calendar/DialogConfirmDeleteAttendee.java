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
package com.zimbra.qa.selenium.projects.ajax.pages.calendar;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.DialogConfirmDeleteRecurringAppointment.Locators;

public class DialogConfirmDeleteAttendee extends DialogWarning {

	public static final String LocatorDivID = "CONFIRM_DELETE_APPT_DIALOG";

	public DialogConfirmDeleteAttendee(AbsApplication application, AbsTab page) {
		super(new DialogWarningID(LocatorDivID), application, page);

		logger.info("new " + DialogConfirmDeleteAttendee.class.getCanonicalName());
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Click dialog button " + button);
		if (button == null)
			throw new HarnessException("button cannot be null");

		String locator = null;
		AbsPage page = null;
		boolean waitForPostfix = false;

		if (button == Button.B_NOTIFY_ORGANIZER) {

			locator = "css=div[id='" + this.MyDivId + "'] td[id^='TODO'] td[id$='_title']";
			page = null;
			waitForPostfix = true;

		} else if (button == Button.B_DONT_NOTIFY_ORGANIZER) {

			locator = "css=div[id='" + this.MyDivId + "'] td[id^='TODO'] td[id$='_title']";
			page = null;
			waitForPostfix = false;

		} else if (button == Button.B_DELETE_THE_SERIES) {

			locator = Locators.DeleteTheSeriesRadioButton;
			page = null;

			sClick(locator);
			this.zWaitForBusyOverlay();

			return (page);

		} else if (button == Button.B_DELETE_THIS_INSTANCE) {

			locator = Locators.DeleteThisInstanceRadioButton;
			page = null;

			sClick(locator);
			this.zWaitForBusyOverlay();

			return (page);

		} else {

			return (super.zPressButton(button));

		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		this.sClickAt(locator, "0,0");

		this.zWaitForBusyOverlay();

		if (waitForPostfix) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}

		return (page);
	}
}