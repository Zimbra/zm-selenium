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
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class DialogCreateCalendarFolder extends AbsDialog {

	public static class Locators {

		public static final String zDialogId = "CreateNewFolderDialog";
		public static final String zDialogCSS = "css=div[id^='" + zDialogId + "']";

		public static final String zNameField = zDialogCSS + " div[id$='_content'] input[id$='_name']";

		public static final String zOkButton = zDialogCSS + " div[id$='_buttons'] td[id^='OK_'] td[id$='_title']";
		public static final String zCancelButton = zDialogCSS
				+ " div[id$='_buttons'] td[id^='Cancel_'] td[id$='_title']";
		public static final String zBackButton = zDialogCSS + " div[id$='_buttons'] td[id^='Back_'] td[id$='_title']";

	}

	public DialogCreateCalendarFolder(AbsApplication application, AbsTab tab) {
		super(application, tab);

		logger.info("new " + DialogCreateCalendarFolder.class.getCanonicalName());

	}

	@Override
	public String myPageName() {
		return null;
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Click dialog button " + button);

		AbsPage page = null;
		String locator = null;

		if (button == Button.B_OK) {

			locator = Locators.zOkButton;

		} else if (button == Button.B_CANCEL) {

			locator = Locators.zCancelButton;

		} else if (button == Button.B_BACK) {

			locator = Locators.zBackButton;

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		this.sClickAt(locator, "0,0");
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.zDialogCSS;

		if (!this.sIsElementPresent(locator)) {
			return (false);
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false);
		}

		logger.info(myPageName() + " zIsActive() = true");
		return (true);
	}

	public void zEnterFolderName(String calendarname) throws HarnessException {
		logger.info(myPageName() + " zEnterFolderName(" + calendarname + ")");

		String locator = Locators.zNameField;

		this.sType(locator, calendarname);
		this.zWaitForBusyOverlay();
	}
}