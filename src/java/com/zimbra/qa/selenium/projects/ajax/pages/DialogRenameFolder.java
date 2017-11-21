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
package com.zimbra.qa.selenium.projects.ajax.pages;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;

/**
 * Represents a "Rename Folder" dialog box
 * 
 * @author Matt Rhoades
 */
public class DialogRenameFolder extends AbsDialog {

	public static class Locators {

		public static final String zDialogRenameId = "RenameFolderDialog";
		public static final String zNewFolderNameFieldId = "RenameFolderDialog_name";
		public static final String zButtonsId = "RenameFolderDialog_buttons";
	}

	public DialogRenameFolder(AbsApplication application, AbsTab tab) {
		super(application, tab);
	}

	public void zSetNewName(String name) throws HarnessException {
		logger.info(myPageName() + " zSetNewName(" + name + ")");

		String locator = "css=input[id='" + Locators.zNewFolderNameFieldId + "']";

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Rename locator " + locator + " is not present");
		}

		this.clearField(locator);
		this.sFocus(locator);
		this.sType(locator, name);
		SleepUtil.sleepSmall();
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		String locator = null;

		if (button == Button.B_OK) {

			locator = "css=div[id='RenameFolderDialog_buttons'] td[id^='OK_'] td[id$='_title']";

		} else if (button == Button.B_CANCEL) {

			locator = "css=div[id='RenameFolderDialog_buttons'] td[id^='Cancel_'] td[id$='_title']";

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
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

		String locator = "css=div[id='" + Locators.zDialogRenameId + "']";

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