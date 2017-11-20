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
package com.zimbra.qa.selenium.projects.ajax.pages;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;

public class DialogAssistant extends AbsDialog {

	public static class Locators {

		public static final String Locator_Assistant_DIV_css = "css=div[class='ZmAssistantDialog']";

	}

	public DialogAssistant(AbsApplication application, AbsTab tab) {
		super(application, tab);
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {

		if (button == null)
			throw new HarnessException("button cannot be null");

		String locator = null;
		AbsPage page = null;
		boolean postqueue = false;

		String buttonsTableLocator = Locators.Locator_Assistant_DIV_css + " div[id$='_buttons']";

		if (button == Button.B_HELP) {

			locator = buttonsTableLocator + " td[id^='Help_'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_OK) {

			locator = buttonsTableLocator + " td[id^='OK_'] td[id$='_title']";
			page = null;
			postqueue = true;

		} else if (button == Button.B_CANCEL) {

			locator = buttonsTableLocator + " td[id^='Cancel_'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_MORE_DETAILS) {

			locator = buttonsTableLocator + " td[id^='More Details'] td[id$='_title']";
			page = null;

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		sClickAt(locator, "0,0");

		zWaitForBusyOverlay();

		if (postqueue) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}

		return (page);

	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		throw new HarnessException("impelment me!");
	}

	public void zEnterCommand(String command) throws HarnessException {

		if ((command == null) || (command.trim().length() == 0)) {
			throw new HarnessException("command cannot be null or empty");
		}

		String locator = Locators.Locator_Assistant_DIV_css + " div[id$='_content'] textarea";

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Unable to locate command area");

		this.sFocus(locator);
		this.sClick(locator);
		this.zKeyboard.zTypeCharacters(command);
		this.zWaitForBusyOverlay();

		return;
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		boolean present = this.sIsElementPresent(Locators.Locator_Assistant_DIV_css);
		if (!present) {
			logger.info("Zimbra Assistant is not present");
			return (false);
		}

		boolean visible = this.zIsVisiblePerPosition(Locators.Locator_Assistant_DIV_css, 0, 0);
		if (!visible) {
			logger.info("Zimbra Assistant is not visible");
			return (false);
		}
		if (this.sIsElementPresent(Locators.Locator_Assistant_DIV_css)) {

		}
		return (true);
	}
}