/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.pages;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;

public class DialogAttach extends AbsDialog {

	public static class Locators {

		public static final String zDialogId = "css=div[class='ZmAttachDialog']";
		public static final String zTitleId = "css=td[class='DwtDialogTitle']";
	}

	public DialogAttach(AbsApplication application, AbsTab page) {
		super(application, page);

		logger.info("new " + DialogAttach.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		AbsPage page = null;
		String locator = null;

		if (button == Button.B_ATTACH) {

			locator = "css=div[class='ZmAttachDialog'] div[id$='_buttons'] td[id^='OK'] td[id$='_title']:contains('Attach')";

		} else if (button == Button.B_CANCEL) {

			locator = "css=div[class='ZmAttachDialog'] td[id^='Cancel_'] td[id$='_title']";

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		this.sClick(locator);
		this.zWaitForBusyOverlay();

		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.zDialogId;

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