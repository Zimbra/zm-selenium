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
import com.zimbra.qa.selenium.framework.util.ConfigProperties;

public class DialogAddVersionNotes extends AbsDialog {

	public static class Locators {
		public static final String zDialogClass = "DwtDialog";
		public static final String zDialogTitleClass = "css=td[class=DwtDialogTitle]";
		public static final String zDialogButtonsClass = "DwtDialogButtonBar";
		public static final String zDialogContentClassId = "DwtDialogBody";
	}

	public DialogAddVersionNotes(AbsApplication application, AbsTab tab) {
		super(application, tab);
		logger.info("new " + DialogAddVersionNotes.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.zDialogTitleClass;

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

		if (button == Button.B_OK) {
			locator = "css=div[class='" + Locators.zDialogClass + "'] " + "div[class='" + Locators.zDialogButtonsClass
					+ "'] td[class=ZWidgetTitle]:contains(OK)";
		} else if (button == Button.B_CANCEL) {
			locator = "css=div[class='" + Locators.zDialogClass + "'] " + "div[class='" + Locators.zDialogButtonsClass
					+ "'] td[class=ZWidgetTitle]:contains(Cancel)";
		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		this.sClickAt(locator, "0,0");

		return (null);
	}

	private void zEnterVersionNotes(String notes) throws HarnessException {
		logger.info(myPageName() + " zEnterVersionNotes(" + notes + ")");

		tracer.trace("Enter version notes in text field " + notes);

		if (notes == null)
			throw new HarnessException("notes must not be null");

		String locator = "css=div[class=" + Locators.zDialogContentClassId + "] textarea[id$='notes']";

		if (!this.zWaitForElementPresent(locator, "10000"))
			throw new HarnessException("unable to find body field " + locator);

		this.sFocus(locator);
		this.sClickAt(locator, "0,0");
		this.sType(locator, notes);
	}

	public void zDismissAddVersionNotesDlg(String parentWindow) throws HarnessException {
		sSelectWindow(PageBriefcase.pageTitle);
		if (zIsWindowOpen(parentWindow)) {
			zSelectWindow(parentWindow);

			if (zIsActive()) {
				zEnterVersionNotes("notes" + ConfigProperties.getUniqueString());

				zPressButton(Button.B_OK);
			}
		}
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		logger.info(myPageName() + " zGetDisplayedText(" + locator + ")");

		if (locator == null)
			throw new HarnessException("locator was null");

		return (this.sGetText(locator));
	}
}