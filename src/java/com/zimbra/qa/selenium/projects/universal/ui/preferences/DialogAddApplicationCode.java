/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.ui.preferences;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class DialogAddApplicationCode extends AbsDialog {

	public static class Locators {
		public static final String zDialogClass = "DwtDialog";
		public static final String zDialogTitleClass = "css=td[class=DwtDialogTitle]";
		public static final String zDialogButtonsClass = "DwtDialogButtonBar";
		public static final String zApplicationName = "css=input[id$='_app_name_input']";
	}

	public DialogAddApplicationCode(AbsApplication application, AbsTab tab) {
		super(application, tab);
		logger.info("new " + DialogAddApplicationCode.class.getCanonicalName());
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
			return (false); // Not even present
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false); // Not visible per position
		}

		// Yes, visible
		logger.info(myPageName() + " zIsActive() = true");
		return (true);
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");
		tracer.trace("Click dialog button " + button);

		String locator = null;

		if (button == Button.B_NEXT) {
			locator = "css=div[class='" + Locators.zDialogClass + "'] "
					+ "div[class='" + Locators.zDialogButtonsClass
					+ "'] td[class=ZWidgetTitle]:contains(Next)";
		} else if (button == Button.B_CLOSE) {
			locator = "css=div[class='" + Locators.zDialogClass + "'] "
					+ "div[class='" + Locators.zDialogButtonsClass
					+ "'] td[class=ZWidgetTitle]:contains(Close)";
		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator "
					+ locator + " not present!");
		}
		this.sClickAt(locator, "0,0");
		SleepUtil.sleepLong();

		return (null);
	}

	public void zEnterApplicationName(String application) throws HarnessException {

		if (application == null)
			throw new HarnessException("application must not be null");

		String locator = Locators.zApplicationName;

		if (!this.zWaitForElementPresent(locator, "10000"))
			throw new HarnessException("unable to find body field " + locator);

		this.sFocus(locator);
		this.sClickAt(locator, "0,0");
		this.sFocus(locator);
		this.zKeyboard.zTypeCharacters(application);
		SleepUtil.sleepSmall();
	}


	@Override
	public String zGetDisplayedText(String passcode) throws HarnessException {
		logger.info(myPageName() + " zGetSecretKey");

		String locator = "css=td[class='WindowInnerContainer'] div[id$='_app_passcode'] span[id$='_app_passcode_value']" ;

		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Secret key "+ locator +" is not present");
		}

		return(this.sGetText(locator));
	}

}
