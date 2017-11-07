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
package com.zimbra.qa.selenium.projects.ajax.ui.social;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;

/**
 * Represents a "Rename Folder" dialog box
 * 
 * @author Matt Rhoades
 */

public class DialogSocialZimletWelcome extends AbsDialog {

	public static class Locators {
		public static final String zDialogLocatorCSS = "css=div[id='SocialZimlet_WelcomeDlg']";
	}

	public DialogSocialZimletWelcome(AbsApplication application, AbsTab tab) {
		super(application, tab);
	}

	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton(" + button + ")");

		String locator = null;

		if (button == Button.B_OK) {

			locator = Locators.zDialogLocatorCSS + " td[id^='OK_'] td[id$='_title']";

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		this.zClick(locator);

		zWaitForBusyOverlay();

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

		String locator = Locators.zDialogLocatorCSS;

		boolean present = this.sIsElementPresent(locator);
		if (!present) {
			return (false);
		}

		boolean visible = this.zIsVisiblePerPosition(locator, 0, 0);
		if (!visible) {
			return (false);
		}

		logger.info(myPageName() + " zIsVisible() = true");
		return (true);
	}
}