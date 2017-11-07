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
package com.zimbra.qa.selenium.projects.ajax.ui;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

/**
 * Represents a "Rename Tag" dialog box
 * 
 * @author Matt Rhoades
 */
public class DialogRenameTag extends AbsDialog {

	public static class Locators {
		public static final String zRenameTagDialogId = "RenameTagDialog";
		public static final String zNewTagNameFieldId = "RenameTagDialog_name";
		public static final String zButtonsId = "RenameTagDialog_buttons";
	}

	public DialogRenameTag(AbsApplication application, AbsTab tab) {
		super(application, tab);
		logger.info("new " + DialogRenameTag.class.getCanonicalName());

	}

	public void zSetNewName(String name) throws HarnessException {
		logger.info(myPageName() + " zSetNewName(" + name + ")");

		String locator = "css=input#" + Locators.zNewTagNameFieldId + "";

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Rename locator " + locator + " is not present");
		}
		SleepUtil.sleepSmall();
		clearField(locator);
		sType(locator, name);
		SleepUtil.sleepSmall();
	}

	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton(" + button + ")");

		String locator = null;

		if (button == Button.B_OK) {
			locator = "css=div#RenameTagDialog_button2";

		} else if (button == Button.B_CANCEL) {
			locator = "css=div#RenameTagDialog_button1";

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		zClickAt(locator, "0,0");
		this.zWaitForBusyOverlay();
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

		String locator = "id=" + Locators.zRenameTagDialogId;

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