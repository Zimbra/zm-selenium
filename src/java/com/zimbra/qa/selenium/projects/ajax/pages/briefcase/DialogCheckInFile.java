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
package com.zimbra.qa.selenium.projects.ajax.pages.briefcase;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;

public class DialogCheckInFile extends AbsDialog {

	public static class Locators {
		public static final String zDialogClass = "css=div.DwtDialog:contains(Check In File to Briefcase)";
		public static final String zTitleCLass = "DwtDialogTitle";
		public static final String zTitle = "Check In File to Briefcase";
		public static final String zDialogButtonsClass = "DwtDialogButtonBar";
		public static final String zDialogContentClassId = "DwtDialogBody";
	}

	public DialogCheckInFile(AbsApplication application, AbsTab page) {
		super(application, page);
		logger.info("new " + DialogCheckInFile.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.zDialogClass;

		if (!this.sIsElementPresent(locator)) {
			return (false);
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false);
		}

		logger.info(myPageName() + " zIsActive() = true");
		return (true);
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");
		tracer.trace("Click dialog button " + button);

		String locator = null;

		if (button == Button.B_CANCEL) {
			locator = "//div[@class='DwtDialog']"
					+ "//*[contains(@class,'ZWidgetTitle') and contains(text(),'Cancel')]";

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsVisible(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not visible!");
		}

		this.sClickAt(locator, "0,0");
		this.zWaitForBusyOverlay();

		return (null);
	}

	public void zEnterNotes(String notes) throws HarnessException {
		logger.info(myPageName() + " zEnterNotes(" + notes + ")");

		tracer.trace("Enter notes in text field " + notes);

		if (notes == null)
			throw new HarnessException("notes must not be null");

		String locator = "css=div[class=" + Locators.zDialogContentClassId + "] textarea[id$='notes']";

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("unable to find body field " + locator);

		this.sFocus(locator);
		this.sClickAt(locator, "0,0");
		this.sType(locator, notes);

		this.zWaitForBusyOverlay();
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		logger.info(myPageName() + " zGetDisplayedText(" + locator + ")");

		if (locator == null)
			throw new HarnessException("locator was null");

		return (this.sGetText(locator));
	}
}