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
package com.zimbra.qa.selenium.projects.ajax.pages.tasks;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DialogCreateFolder;

public class DialogCreateTaskFolder extends AbsDialog {

	public static class Locators {
		public static final String zDialogId = "css=div[class*='DwtDialog WindowOuterContainer']";
		public static final String zNameField = "css=input[id$='_name'][class*='Field']";
		public static final String zOkButton = "css=div[id='CreateNewFolderDialog'] td[id^='OK_DWT']> div[id^='CreateNewFolderDialog_button']";
		public static final String zCancelButton = "css=div[id='CreateNewFolderDialog'] td[id^='Cancel_DWT']> div[id^='CreateNewFolderDialog_button']";
	}

	public DialogCreateTaskFolder(AbsApplication application, AbsTab page) {
		super(application, page);
		logger.info("new " + DialogCreateFolder.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return null;
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

		String locator = Locators.zNameField;

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

		AbsPage page = null;
		String locator = null;
		if (button == Button.B_OK) {

			locator = Locators.zOkButton;

			this.sClickAt(locator, "0,0");
			this.zWaitForBusyOverlay();

			return (page);

		} else if (button == Button.B_CANCEL) {

			locator = Locators.zCancelButton;

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
		SleepUtil.sleepSmall();

		return (page);
	}

	public void zEnterFolderName(String folder) throws HarnessException {
		logger.info(myPageName() + " zEnterFolderName(" + folder + ")");

		tracer.trace("Enter folder name in text box " + folder);

		if (folder == null)
			throw new HarnessException("folder must not be null");

		String locator = Locators.zNameField;

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("unable to find folder name field " + locator);

		sClickAt(locator, "0,0");
		sType(locator, folder);
	}

	public enum FolderColor {
		None, Blue, Cyan, Green, Purple, Red, Yellow, Pink, Grey, Orange, MoreColors
	}

	public void zEnterFolderColor(FolderColor color) throws HarnessException {
		logger.info(myPageName() + " zEnterFolderColor(" + color + ")");

		tracer.trace("Enter color " + color);

		if (color == null)
			throw new HarnessException("folder must not be null");

		if (color == FolderColor.MoreColors)
			throw new HarnessException("'more colors' - implement me!");

		throw new HarnessException("implement me!");
	}
}