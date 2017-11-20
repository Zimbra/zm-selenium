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

public class DialogFindShares extends AbsDialog {

	public static class Locators {

		public static final String zDialogLocator = "div[class='ZmShareSearchDialog DwtDialog']";
		public static final String zTitleId = "div[id$=_title]";
		public static final String zDialogButtonsId = "div[id$=_buttons]";

	}

	public DialogFindShares(AbsApplication application, AbsTab tab) {
		super(application, tab);

		logger.info("new " + DialogFindShares.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = "css=" + Locators.zDialogLocator;

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

		String locator = null;

		if (button == Button.B_CANCEL) {

			locator = "css=div[id=ZmShareSearchDialog_buttons] td[class=ZWidgetTitle]:contains(Cancel)";

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		this.sClickAt(locator, "0,0");

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

	public void sClickTreeFolder(FolderItem folder) throws HarnessException {
		if (folder == null) {
			throw new HarnessException("sClickTreeFolder(FolderItem): folder must not be null");
		}

		logger.info(myPageName() + " sClickTreeFolder(" + folder + ")");

		tracer.trace("Click on tree folder with name " + folder.getName());

		String locator = Locators.zDialogLocator + " td[id$=" + folder.getId() + "_textCell']";

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("unable to find folder in tree " + locator);

		this.sClick(locator);

		SleepUtil.sleepSmall();
	}
}