/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui.preferences;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;

/**
 * Represents an "Out Of Office Alert" dialog box
 * <p>
 */
public class DialogOOOAlert extends AbsDialog {
	public static class Locators {
		public static final String zDialogClass = "DwtDialog";
		public static final String zDialogButtonsClass = "DwtDialogButtonBar";
		public static final String zDialogContentClass = "DwtDialogBody";
	}

	public DialogOOOAlert(AbsApplication application, AbsTab tab) {
		super(application, tab);

		logger.info("new " + DialogOOOAlert.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton(" + button + ")");

		tracer.trace("Click dialog button " + button);
		if (button == null)
			throw new HarnessException("button cannot be null");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_YES) {
			locator = "css=div[class='" + Locators.zDialogClass + "'] "
					+ "div[class='" + Locators.zDialogButtonsClass
					+ "'] td[class=ZWidgetTitle]:contains(Yes)";
		} else if (button == Button.B_NO) {
			locator = "css=div[class='" + Locators.zDialogClass + "'] "
					+ "div[class='" + Locators.zDialogButtonsClass
					+ "'] td[class=ZWidgetTitle]:contains(No)";
		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		// Make sure the locator exists
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator "
					+ locator + " not present!");
		}

		// if(zIsActive())
		// zGetDisplayedText("css=div." + Locators.zDialogClass +
		// ":contains(Out Of Office)");

		this.zClickAt(locator, "0,0");

		// If the app is busy, wait for it to become active
		this.zWaitForBusyOverlay();

		return (page);
	}

	public boolean zGetCheckboxStatus() throws HarnessException {
		logger.info("zGetCheckboxStatus");

		String locator = "css=input[type=checkbox][id$=dontRemind]";

		if (!sIsElementPresent(locator)) {
			throw new HarnessException(
					"DialogOOOAlert checkbox locator not present " + locator);
		}

		boolean checked = sIsChecked(locator);
		logger.info("zGetCheckboxStatus: " + checked);

		return (checked);
	}

	public void zCheckboxSet(boolean status) throws HarnessException {

		String locator = "css=input[type=checkbox][id$=dontRemind]";

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException(locator + " no present!");
		}

		if (this.sIsChecked(locator) == status) {
			logger.debug("checkbox status matched.  not doing anything");
			return;
		}

		if (status == true) {
			this.sCheck(locator);
		} else {
			this.sUncheck(locator);
		}

		this.zWaitForBusyOverlay();
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

		String locator = "css=div." + Locators.zDialogClass
				+ ":contains(Out Of Office Alert)";

		if (!this.sIsElementPresent(locator)) {
			return (false); // Not even present
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false); // Not visible per position
		}

		logger.info(myPageName() + " zIsActive() = true");
		return (true);
	}
}
