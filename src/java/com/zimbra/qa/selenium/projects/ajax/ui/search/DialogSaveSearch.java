/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui.search;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class DialogSaveSearch extends AbsDialog {

	public static class Locators {

		public static final String zDialogLocator = "css=div#CreateNewFolderDialog";
		public static final String zTitleId = "css=td#CreateNewFolderDialog_title";
		public static final String zDialogInputLocator = "css=input#CreateNewFolderDialog_name";
		public static final String zDialogButtonsId = "CreateNewFolderDialog_buttons";

	}

	public DialogSaveSearch(AbsApplication application, AbsTab tab) {
		super(application, tab);

		logger.info("new " + DialogSaveSearch.class.getCanonicalName());

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.zDialogLocator;

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
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton(" + button + ")");

		tracer.trace("Click dialog button " + button);

		String locator = null;

		if (button == Button.B_OK) {

			locator = "css=div[id='" + Locators.zDialogButtonsId + "'] div[id='CreateNewFolderDialog_button2']";

		} else if (button == Button.B_CANCEL) {

			locator = "css=div[id='" + Locators.zDialogButtonsId + "'] div[id='CreateNewFolderDialog_button1']";

		} else {

			throw new HarnessException("Button " + button + " not implemented");

		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		this.zClick(locator);

		this.zWaitForBusyOverlay();

		return (null);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		logger.info(myPageName() + " zGetDisplayedText(" + locator + ")");

		if (locator == null)
			throw new HarnessException("locator was null");

		return (this.sGetText(locator));
	}

	public void zClickTreeFolder(FolderItem folder) throws HarnessException {
		logger.info(myPageName() + " zClickTreeFolder(" + folder + ")");

		tracer.trace("Click on tree folder with name " + folder.getName());

		String locator = Locators.zDialogLocator + " td[id='zti__ZmChooseFolderDialog_Mail__" + folder.getId()
				+ "_textCell']";

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("unable to find folder in tree " + locator);

		// For some reason, the text doesn't get entered on the first try
		this.zClick(locator);

		SleepUtil.sleepSmall();

	}

	public void zEnterFolderName(String folder) throws HarnessException {
		logger.info(myPageName() + " zEnterFolderName(" + folder + ")");

		tracer.trace("Enter folder name in text box " + folder);

		if (folder == null)
			throw new HarnessException("folder must not be null");

		String locator = Locators.zDialogInputLocator;

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("unable to find folder name field " + locator);

		this.sFocus(locator);
		this.zClick(locator);
		this.sType(locator, folder);
		// zKeyboard.zTypeCharacters(folder);

		SleepUtil.sleepSmall();
	}
}