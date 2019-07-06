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
package com.zimbra.qa.selenium.projects.ajax.pages;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;

/**
 * Represents a "New Tag", "Rename Tag" dialog box
 * 
 * @author Matt Rhoades
 */

public class DialogTag extends AbsDialog {

	public static class Locators {

		// TODO: See https://bugzilla.zimbra.com/show_bug.cgi?id=54173
		public static final String zTagDialogId = "CreateTagDialog";

		public static final String zTitleId = "CreateTagDialog_title";

		public static final String zTagNameFieldId = "CreateTagDialog_name";

		public static final String zTagNameFieldCss = "css=input[id='CreateTagDialog_name']";

		public static final String zTagColorPulldownId = "ZmTagColorMenu_dropdown";

		public static final String zButtonsId = "CreateTagDialog_buttons";

		public static final String zChooseNewTagButton = "css=div[id='ZmPickTagDialog_buttons'] td[id^='New_'] td[id$='_title']";
		public static final String zButtonOkCss = "css=div[id='CreateTagDialog_buttons'] td[id^='OK'] td[id$='_title']";
		public static final String zButtonCancelId = "DWT179_title";

	}

	public DialogTag(AbsApplication application, AbsTab tab) {
		super(application, tab);

		logger.info("new " + DialogTag.class.getCanonicalName());
	}

	public void zSetTagName(String name) throws HarnessException {
		logger.info(myPageName() + " zSetTagName(" + name + ")");

		String locator = "css=input#" + Locators.zTagNameFieldId;

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Tag name locator " + locator + " is not present");
		}

		this.sType(locator, name);

	}

	public void zSetTagColor(String color) throws HarnessException {
		logger.info(myPageName() + " zSetTagColor(" + color + ")");

		throw new HarnessException("implement me!");

	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		String locator = null;

		if (button == Button.B_OK) {

			locator = "css=div#CreateTagDialog td[id^='OK'] td[id$='title']";

		} else if (button == Button.B_CANCEL) {

			locator = "css=div#CreateTagDialog td[id^='Cancel'] td[id$='title']";

		} else {

			throw new HarnessException("Button " + button + " not implemented");

		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		sClick(locator);
		zWaitForBusyOverlay();
		SleepUtil.sleepSmall();
		return (null);
	}

	public void zSubmit(String tagName) throws HarnessException {
		zSetTagName(tagName);
		zSubmit();
	}

	public void zSubmit() throws HarnessException {
		// Click OK
		zPressButton(Button.B_OK);
		SleepUtil.sleepSmall();
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

		String locator = "css=div[id=" + Locators.zTagDialogId + "]";

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