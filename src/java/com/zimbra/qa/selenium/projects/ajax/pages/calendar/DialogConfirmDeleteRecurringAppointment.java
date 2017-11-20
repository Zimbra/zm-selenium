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
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.pages.*;

public class DialogConfirmDeleteRecurringAppointment extends DialogWarning {

	public static class Locators {

		public static final String LocatorDivID = "CAL_ITEM_TYPE_DIALOG";
		public static final String LocatorDivCSS = "css=div#CAL_ITEM_TYPE_DIALOG";

		public static final String DeleteThisInstanceRadioButton = LocatorDivCSS
				+ " label:contains('Delete this instance')";
		public static final String DeleteTheSeriesRadioButton = LocatorDivCSS + " label:contains('Delete the series')";

	}

	public DialogConfirmDeleteRecurringAppointment(AbsApplication application, AbsTab page) {
		super(new DialogWarningID(Locators.LocatorDivID), application, page);

		logger.info("new " + DialogConfirmDeleteRecurringAppointment.class.getCanonicalName());
	}

	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		AbsPage page = null;
		String locator = null;

		if (button == Button.B_DELETE_THIS_INSTANCE) {

			locator = Locators.DeleteThisInstanceRadioButton;
			page = null;

			sClick(locator);
			this.zWaitForBusyOverlay();

			return (page);

		} else if (button == Button.B_DELETE_THE_SERIES) {

			locator = Locators.DeleteTheSeriesRadioButton;
			page = null;

			sClick(locator);
			this.zWaitForBusyOverlay();

			return (page);

		} else if (button == Button.B_OK) {

			// Execute the super steps
			super.zPressButton(button);
			SleepUtil.sleepMedium();

			page = new DialogConfirmDeleteSeries(MyApplication, ((AjaxPages) MyApplication).zPageCalendar);
			if (page.zIsActive()) {
				return (page);
			}

			page = new DialogConfirmDeleteAppointment(MyApplication, ((AjaxPages) MyApplication).zPageCalendar);
			if (page.zIsActive()) {
				return (page);
			}

			page = new DialogConfirmDeleteRecurringAppointment(MyApplication,
					((AjaxPages) MyApplication).zPageCalendar);
			if (page.zIsActive()) {
				return (page);
			}
			return (null);

		}
		return (super.zPressButton(button));
	}
}