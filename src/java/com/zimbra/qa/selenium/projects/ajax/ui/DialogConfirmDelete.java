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
package com.zimbra.qa.selenium.projects.ajax.ui;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.PageCalendar;

@SuppressWarnings("unused")
public class DialogConfirmDelete extends AbsDialog {

	public static enum Confirmation {
		SENDCANCELLATION, EDITMESSAGE, CANCEL
	}

	private Confirmation confirmation = null;

	public static class Locators {
		public static final String zDialogClass = "DwtDialog";
		public static final String zDialogButtonsClass = "DwtDialogButtonBar";
		public static final String zDialogContentClassId = "DwtDialogBody";
	}

	public DialogConfirmDelete(Confirmation confirmation, AbsApplication application, AbsTab tab) {
		super(application, tab);

		this.confirmation = confirmation;

		logger.info("new " + DialogConfirmDelete.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Click dialog button " + button);
		if (button == null)
			throw new HarnessException("button cannot be null");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_YES) {
			locator = "css=div[class='" + Locators.zDialogClass + "'] " + "div[class='" + Locators.zDialogButtonsClass
					+ "'] td[class=ZWidgetTitle]:contains(Yes)";

		} else if (button == Button.B_NO) {
			locator = "css=div[class='" + Locators.zDialogClass + "'] " + "div[class='" + Locators.zDialogButtonsClass
					+ "'] td[class=ZWidgetTitle]:contains(No)";

		} else if (button == Button.B_SENDCANCELLATION) {
			locator = PageCalendar.Locators.SendCancellationButton;

		} else if (button == Button.B_EDITMESSAGE) {
			locator = PageCalendar.Locators.EditMessageButton;

		} else if (button == Button.B_CANCEL_CONFIRMDELETE) {
			locator = PageCalendar.Locators.CancelButton_ConfirmDelete;
		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		this.sClickAt(locator, "0,0");
		this.zWaitForBusyOverlay();

		if (page != null) {
			page.zWaitForActive();
		}

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

		String locator = "class=" + Locators.zDialogClass;

		if (!this.sIsElementPresent(locator)) {
			return (false);
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false);
		}

		logger.info(myPageName() + " zIsActive() = true");
		return (true);
	}
}