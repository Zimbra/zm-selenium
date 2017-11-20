/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.pages.contacts;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;

/**
 * Represents a "Create New Folder" dialog box
 *
 * Lots of methods not yet implemented. See
 * https://bugzilla.zimbra.com/show_bug.cgi?id=55923
 * <p>
 *
 * @author Matt Rhoades
 *
 */

public class DialogEditFolder extends com.zimbra.qa.selenium.projects.ajax.pages.mail.DialogEditFolder {

	public static final class Locators {

		public static final String zNoneColorId = "css=div.ZmColorMenu div#COLOR_0 td[id$='_title']";
		public static final String zBlueColorId = "css=div.ZmColorMenu div#COLOR_1 td[id$='_title']";
		public static final String zCyanColorId = "css=div.ZmColorMenu div#COLOR_2 td[id$='_title']";
		public static final String zGreenColorId = "css=div.ZmColorMenu div#COLOR_3 td[id$='_title']";
		public static final String zPurpleColorId = "css=div.ZmColorMenu div#COLOR_4 td[id$='_title']";
		public static final String zRedColorId = "css=div.ZmColorMenu div#COLOR_5 td[id$='_title']";
		public static final String zYellowColorId = "css=div.ZmColorMenu div#COLOR_6 td[id$='_title']";
		public static final String zPinkColorId = "css=div.ZmColorMenu div#COLOR_7 td[id$='_title']";
		public static final String zGrayColorId = "css=div.ZmColorMenu div#COLOR_8 td[id$='_title']";
		public static final String zOrangeColorId = "css=div.ZmColorMenu div#COLOR_9 td[id$='_title']";

	}

	public DialogEditFolder(AbsApplication application, AbsTab tab) {
		super(application, tab);
		logger.info("new " + DialogEditFolder.class.getCanonicalName());

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	public void zSetNewColor(FolderColor color) throws HarnessException {
		logger.info(myPageName() + " zEnterFolderColor(" + color + ")");

		String actionLocator = "css=div[id^='FolderProperties'] td[id$='_dropdown'].ZDropDown>div";
		String optionLocator = null;
		tracer.trace("Enter folder color " + color);

		if (color == null)
			throw new HarnessException("folder must not be null");

		if (color == FolderColor.MoreColors) {
			throw new HarnessException("'more colors' - implement me!");
		}

		if (color == FolderColor.Gray) {

			optionLocator = Locators.zGrayColorId;

		} else if (color == FolderColor.Blue) {

			optionLocator = Locators.zBlueColorId;

		} else if (color == FolderColor.Cyan) {

			optionLocator = Locators.zCyanColorId;

		} else if (color == FolderColor.Green) {

			optionLocator = Locators.zGreenColorId;

		} else if (color == FolderColor.Red) {

			optionLocator = Locators.zRedColorId;

		} else if (color == FolderColor.Orange) {

			optionLocator = Locators.zOrangeColorId;

		} else if (color == FolderColor.Yellow) {

			optionLocator = Locators.zYellowColorId;

		} else if (color == FolderColor.Purple) {

			optionLocator = Locators.zPurpleColorId;

		} else {
			throw new HarnessException("color " + color + " not yet implemented");
		}

		if (actionLocator != null) {

			if (!this.sIsElementPresent(actionLocator)) {
				throw new HarnessException("actionLocator is not present! " + this.sGetHtmlSource());
			}

			sClick(actionLocator);
			this.zWaitForBusyOverlay();

		}

		if (optionLocator != null) {

			if (!this.sIsElementPresent(optionLocator)) {
				throw new HarnessException("optionLocator is not present! " + this.sGetHtmlSource());
			}

			sClick(optionLocator);
			this.zWaitForBusyOverlay();

		}
	}

	public void zSetNewName(String folder) throws HarnessException {
		logger.info(myPageName() + " zEnterFolderName(" + folder + ")");

		tracer.trace("Enter new folder name " + folder);

		if (folder == null)
			throw new HarnessException("folder must not be null");

		String locator = "css=div[id='FolderProperties'] div[id$='_content'] td.Field input";

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("unable to find folder name field " + locator);

		this.sType(locator, folder);
		this.zWaitForBusyOverlay();
	}
}