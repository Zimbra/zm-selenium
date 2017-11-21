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

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class DialogCreateBriefcaseFolder extends AbsDialog {

	public static class Locators {

		public static final String zDialogId = "ChooseFolderDialog";
		public static final String zTitleId = "ChooseFolderDialog_title";
		public static final String zDialogContentId = "ChooseFolderDialog_content";
		public static final String zTitle = "css=td[id^=CreateNewFolderDialog__]:contains(Create New Briefcase Folder)";
		public static final String zNameField = "css=div[id^=CreateNewFolderDialog]:contains(Create New Briefcase Folder) td>input.Field";
		public static final String zDialogButtonsId = "ChooseFolderDialog_buttons";
		public static final String zOkButton = "css=td[id^=OK] td[id^=CreateNewFolderDialog]:contains(OK)";
		public static final String zCancelButton = "css=td[id^=Cancel] td[id^=CreateNewFolderDialog]:contains(Cancel)";
	}

	public DialogCreateBriefcaseFolder(AbsApplication application, AbsTab tab) {
		super(application, tab);
		logger.info("new " + DialogCreateBriefcaseFolder.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.zNameField;

		if (!this.zWaitForElementPresent(locator, "3000")) {
			throw new HarnessException("locator not even present");
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

		} else if (button == Button.B_CANCEL) {

			locator = Locators.zCancelButton;

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		this.sClick(locator);
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		logger.info(myPageName() + " zGetDisplayedText(" + locator + ")");

		if (locator == null)
			throw new HarnessException("locator was null");

		return (this.sGetText(locator));
	}

	public void sClickTreeFolder(FolderItem folder) throws HarnessException {
		if (folder == null) {
			throw new HarnessException("sClickTreeFolder(FolderItem): folder must not be null");
		}

		logger.info(myPageName() + " sClickTreeFolder(" + folder + ")");

		tracer.trace("Click on tree briefcase with name " + folder.getName());

		String locator = "css=div[id='" + Locators.zDialogId + "'] td[id='zti__ZmChooseFolderDialog_Briefcase__"
				+ folder.getId() + "_textCell']";

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("unable to find folder in tree " + locator);

		this.sClickAt(locator, "0,0");

		this.zWaitForBusyOverlay();

	}

	public void zEnterFolderName(String folder) throws HarnessException {
		logger.info(myPageName() + " zEnterFolderName(" + folder + ")");

		tracer.trace("Enter briefcase name in text box " + folder);

		if (folder == null)
			throw new HarnessException("folder must not be null");

		String locator = Locators.zNameField;

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("unable to find folder name field " + locator);

		this.sFocus(locator);
		this.sClickAt(locator, "0,0");
		this.clearField(locator);
		this.sType(locator, folder);

		this.zWaitForBusyOverlay();
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