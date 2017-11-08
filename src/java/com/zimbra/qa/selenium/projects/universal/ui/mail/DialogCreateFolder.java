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
/**
 * 
 */
package com.zimbra.qa.selenium.projects.universal.ui.mail;

import java.net.URL;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

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
public class DialogCreateFolder extends AbsDialog {

	public static class Locators {
		public static final String DialogDivLocatorCSS = "css=div[id='CreateNewFolderDialog']";
		public static final String DialogNameLocatorCSS = "css=input[id='CreateNewFolderDialog_name']";
		public static final String zChooseNewFolderButton = "css=div[id='ChooseFolderDialog_buttons'] td[id^='New_'] td[id$='_title']";
		public static final String zOkButton = "css=div[id='CreateNewFolderDialog_buttons'] td[id^='OK_'] td[id$='_title']";
		public static final String zCancelButton = "css=div[id='CreateNewFolderDialog_buttons'] td[id^='Cancel_'] td[id$='_title']";
	}

	public DialogCreateFolder(AbsApplication application, AbsTab tab) {
		super(application, tab);
		logger.info("new " + DialogCreateFolder.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.DialogDivLocatorCSS;

		if (!this.sIsElementPresent(locator)) {
			return (false); // Not even present
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false); // Not visible per position
		}

		// Yes, visible
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

		if (locator == null) {
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

	/**
	 * Click on the folder in the dialog tree
	 * 
	 * @param folder
	 * @throws HarnessException
	 */
	public void sClickTreeFolder(FolderItem folder) throws HarnessException {
		logger.info(myPageName() + " sClickTreeFolder(" + folder + ")");

		if (folder == null)
			throw new HarnessException("folder must not be null");

		tracer.trace("Click on tree folder with name " + folder.getName());

		String locator = Locators.DialogDivLocatorCSS + " td[id='zti__ZmChooseFolderDialog_Mail__" + folder.getId()
				+ "_textCell']";

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("unable to find folder in tree " + locator);

		this.sClick(locator);

		this.zWaitForBusyOverlay();

	}

	/**
	 * Enter text into the move message dialog folder name field
	 * 
	 * @param folder
	 */
	public void zEnterFolderName(String folder) throws HarnessException {
		logger.info(myPageName() + " zEnterFolderName(" + folder + ")");

		tracer.trace("Enter folder name in text box " + folder);

		if (folder == null)
			throw new HarnessException("folder must not be null");

		String locator = Locators.DialogNameLocatorCSS;

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("unable to find folder name field " + locator);

		SleepUtil.sleepSmall();
		sClick(locator);
		sType(locator, folder);
	}

	public enum FolderColor {
		None, Blue, Cyan, Green, Purple, Red, Yellow, Pink, Grey, Orange, MoreColors
	}

	/**
	 * Set the color pulldown
	 * 
	 * @param folder
	 */
	public void zEnterFolderColor(FolderColor color) throws HarnessException {
		logger.info(myPageName() + " zEnterFolderColor(" + color + ")");

		tracer.trace("Enter color " + color);

		if (color == null)
			throw new HarnessException("folder must not be null");

		if (color == FolderColor.MoreColors)
			throw new HarnessException("'more colors' - implement me!");

		throw new HarnessException("implement me!");

	}

	public void sClickSubscribeFeed(boolean b) throws HarnessException {
		String locator = "css=input[id='CreateNewFolderDialog_remote']";

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException(locator + " no present!");
		}

		if (this.sIsChecked(locator) == b) {
			logger.debug("checkbox status matched.  not doing anything");
			return;
		}

		this.sCheck(locator);

	}

	public void zEnterFeedURL(URL feed) throws HarnessException {
		String locator = "CreateNewFolderDialog_remoteURLfield";

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException(locator + " no present!");
		}

		this.sType(locator, feed.toString());

	}

}
