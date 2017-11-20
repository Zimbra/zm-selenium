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
/**
 * 
 */
package com.zimbra.qa.selenium.projects.universal.pages;

import java.awt.event.KeyEvent;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

/**
 * Represents a "Add Delegate" dialog box (Preferences -> Accounts -> Delegates)
 * <p>
 * 
 * @author Matt Rhoades
 *
 */
public class DialogDelegate extends AbsDialog {

	public static class Locators {
		public static final String zDialogLocator = "css=div[id='GrantRightsDialog']";
	}

	protected String MyDialogLocator = null;

	public enum Rights {
		SendAs, SendOnBehalfOf,
	}

	public DialogDelegate(AbsApplication application, AbsTab tab) {
		super(application, tab);
		MyDialogLocator = Locators.zDialogLocator;
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		String locator = null;

		if (button == Button.B_OK) {
			locator = MyDialogLocator + " td[id^='OK_'] td[id$='_title']";

		} else if (button == Button.B_CANCEL) {
			locator = MyDialogLocator + " td[id^='Cancel_'] td[id$='_title']";

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		this.sClick(locator);
		zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		return (null);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		throw new HarnessException("implement me");
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		logger.info(myPageName() + " zIsActive()");

		String locator = MyDialogLocator;

		if (!this.sIsElementPresent(locator)) {
			return (false);
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false);
		}

		logger.info(myPageName() + " zIsActive() = true");
		return (true);
	}

	public void zSetEmailAddress(String email) throws HarnessException {
		logger.info(myPageName() + " zSetEmailAddress(" + email + ")");

		String locator = "css=input#ZmGrantRightsDialog_name";

		// Make sure the locator exists
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("zSetEmailAddress " + locator + " is not present");
		}

		this.sFocus(locator);
		this.sClick(locator);
		this.zWaitForBusyOverlay();
		this.sType(locator, email);
		SleepUtil.sleepSmall();
		this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		SleepUtil.sleepSmall();
	}

	public void zCheckRight(Rights right) throws HarnessException {
		logger.info(myPageName() + " zCheckRight(" + right + ")");

		String locator = null;

		if (right == Rights.SendAs) {
			locator = "css=input#ZmGrantRightsDialog_sendAs";

		} else if (right == Rights.SendOnBehalfOf) {
			locator = "css=input#ZmGrantRightsDialog_sendObo";

		} else {
			throw new HarnessException("zCheckRight: " + right + " is not defined");
		}

		this.sFocus(locator);
		this.sClick(locator);
	}

	public void zUnCheckRight(Rights right) throws HarnessException {
		logger.info(myPageName() + " zUnCheckRight(" + right + ")");
		zCheckRight(right);
	}

}
