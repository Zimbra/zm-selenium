/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
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

import com.zimbra.qa.selenium.framework.items.TagItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.PageMail;

/**
 * Represents a "Tag Picker" dialog
 * 
 * @author Matt Rhoades
 */

public class DialogTagPicker extends AbsDialog {

	public static class Locators {

		// TODO: See https://bugzilla.zimbra.com/show_bug.cgi?id=54173
		public static final String TagPicker_Div = "css=div#ZmPickTagDialog";

		public static final String TagPicker_TagName_Locator = TagPicker_Div + " div[id$='_inputDivId'] input";

		public static final String TagPickerButton_NEW_Locator = TagPicker_Div
				+ " div[id$='_buttons'] td[id^='New'] td[id$='_title']";
		public static final String TagPickerButton_OK_Locator = TagPicker_Div
				+ " div[id$='_buttons'] td[id^='OK'] td[id$='_title']";
		public static final String TagPickerButton_CANCEL_Locator = TagPicker_Div
				+ " div[id$='_buttons'] td[id^='Cancel'] td[id$='_title']";

	}

	public DialogTagPicker(AbsApplication application, AbsTab tab) {
		super(application, tab);

		logger.info("new " + DialogTag.class.getCanonicalName());
	}

	public void zSetTagName(String name) throws HarnessException {
		logger.info(myPageName() + " zSetTagName(" + name + ")");

		String locator = Locators.TagPicker_TagName_Locator;

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Tag name locator " + locator + " is not present");
		}

		this.sType(locator, name);

	}

	public void zSetTagColor(String color) throws HarnessException {
		logger.info(myPageName() + " zSetTagColor(" + color + ")");

		throw new HarnessException("implement me!");

	}

	public void sClickTreeTag(TagItem tag) throws HarnessException {

		logger.info(myPageName() + " sClickTreeTag(" + tag + ")");

		if (tag == null) {
			throw new HarnessException("tag must not be null");
		}

		String locator = null;

		if (MyTab instanceof PageMail) {

			locator = Locators.TagPicker_Div + " div[id='zti__ZmPickTagDialog__" + tag.getId()
					+ "'] td[id$='_textCell']";

		} else {
			throw new HarnessException("Unknown app type!");
		}

		// Click the tag
		this.sClick(locator);
		this.zWaitForBusyOverlay();

	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		String locator = null;

		if (button == Button.B_OK) {

			locator = Locators.TagPickerButton_OK_Locator;

		} else if (button == Button.B_CANCEL) {

			locator = Locators.TagPickerButton_CANCEL_Locator;

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		sClickAt(locator, "");

		zWaitForBusyOverlay();
		return (null);
	}

	public void zSubmit() throws HarnessException {
		zPressButton(Button.B_OK);
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

		String locator = Locators.TagPicker_Div;

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