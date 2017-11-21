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

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.ajax.pages.AjaxPages;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogShare;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogShareRevoke;

public class DialogEditProperties extends AbsDialog {

	public static class Locators {

		public static final String zEditPropertiesDialogId = "ZmFolderPropsDialog";
		public static final String zEditPropertiesDialogDropDown = "css=div.DwtDialogBody div.ImgSelectPullDownArrow";
		public static final String zGrayColorId = "//td[contains(@id,'_title') and contains(text(),'Gray')]";
		public static final String zBlueColorId = "//td[contains(@id,'_title') and contains(text(),'Blue')]";
		public static final String zCyanColorId = "//td[contains(@id,'_title') and contains(text(),'Cyan')]";
		public static final String zGreenColorId = "//td[contains(@id,'_title') and contains(text(),'Green')]";

		public static final String zPurpleColorId = "//td[contains(@id,'_title') and contains(text(),'Purple')]";
		public static final String zRedColorId = "//td[contains(@id,'_title') and contains(text(),'Red')]";
		public static final String zYellowColorId = "//td[contains(@id,'_title') and contains(text(),'Yellow')]";
		public static final String zPinkColorId = "//td[contains(@id,'_title') and contains(text(),'Pink')]";
		public static final String zOrangeColorId = "//td[contains(@id,'_title') and contains(text(),'Orange')]";

	}

	public DialogEditProperties(AbsApplication application, AbsTab tab) {
		super(application, tab);
		logger.info("new " + DialogEditProperties.class.getCanonicalName());

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		logger.info(myPageName() + " zIsActive()");

		String locator = "css=div[class=" + Locators.zEditPropertiesDialogId + "]";

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

			locator = "//div[@class='" + Locators.zEditPropertiesDialogId
					+ "']//div[contains(@id,'_buttons')]//td[text()='OK']";

		} else if (button == Button.B_CANCEL) {

			locator = "//div[@class='" + Locators.zEditPropertiesDialogId
					+ "']//div[contains(@id,'_buttons')]//td[text()='Cancel']";

		} else if (button == Button.B_SHARE) {

			locator = "//div[@class='" + Locators.zEditPropertiesDialogId
					+ "']//div[contains(@id,'_buttons')]//td[text()=''Add Share...']";

		} else if (button == Button.O_RESEND_LINK) {

			throw new HarnessException("implement me");

		} else if (button == Button.O_REVOKE_LINK) {

			locator = "//div[@class='" + Locators.zEditPropertiesDialogId
					+ "']//div[contains(@id,'_content')]//div/fieldset/div/table/tbody/tr/td/a[contains(text(),'Revoke')]";
			page = new DialogShareRevoke(MyApplication, ((AjaxPages) MyApplication).zPageMail);

			// Click the link
			this.sClick(locator);
			this.zWaitForBusyOverlay();

			// Wait for the Edit dialog to appear
			page.zWaitForActive();
			this.zWaitForBusyOverlay();

			// Done
			return (page);

		} else if (button == Button.O_EDIT_LINK) {

			locator = "//div[@class='" + Locators.zEditPropertiesDialogId
					+ "']//div[contains(@id,'_content')]//div/fieldset/div/table/tbody/tr/td/a[contains(text(),'Edit')]";
			page = new DialogShare(MyApplication, ((AjaxPages) MyApplication).zPageMail);

			// Click the link
			this.sClick(locator);
			this.zWaitForBusyOverlay();

			// Wait for the Edit dialog to appear
			page.zWaitForActive();
			this.zWaitForBusyOverlay();

			// Done
			return (page);

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		this.sClick(locator);

		this.zWaitForBusyOverlay();

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
	 * Set the new folder name
	 */
	public void zSetNewName(String folder) throws HarnessException {
		logger.info(myPageName() + " zEnterFolderName(" + folder + ")");

		tracer.trace("Enter new folder name " + folder);

		if (folder == null)
			throw new HarnessException("folder must not be null");

		String locator = "css=div.DwtDialogBody div input";

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("unable to find folder name field " + locator);

		// For some reason, the text doesn't get entered on the first try
		this.sFocus(locator);
		this.sClick(locator);
		this.clearField(locator);
		this.sType(locator, folder);
		this.zWaitForBusyOverlay();
	}

	public enum FolderColor {
		None, Blue, Cyan, Green, Purple, Red, Yellow, Pink, Gray, Orange, MoreColors
	}

	/**
	 * Set the color pulldown
	 */
	public void zSetNewColor(FolderColor color) throws HarnessException {
		logger.info(myPageName() + " zEnterFolderColor(" + color + ")");
		String actionLocator = null;
		String optionLocator = null;
		tracer.trace("Enter folder color " + color);

		if (color == null)
			throw new HarnessException("folder must not be null");

		if (color == FolderColor.MoreColors)
			throw new HarnessException("'more colors' - implement me!");
		if (color == FolderColor.Gray) {

			actionLocator = Locators.zEditPropertiesDialogDropDown;
			optionLocator = Locators.zGrayColorId;

			sClick(actionLocator);
			sClick(optionLocator);

		} else if (color == FolderColor.Blue) {

			actionLocator = Locators.zEditPropertiesDialogDropDown;
			optionLocator = Locators.zBlueColorId;

			sClick(actionLocator);
			sClick(optionLocator);

		} else if (color == FolderColor.Cyan) {

			actionLocator = Locators.zEditPropertiesDialogDropDown;
			optionLocator = Locators.zCyanColorId;

			sClick(actionLocator);
			sClick(optionLocator);

		} else if (color == FolderColor.Green) {

			actionLocator = Locators.zEditPropertiesDialogDropDown;
			optionLocator = Locators.zGreenColorId;

			sClick(actionLocator);
			sClick(optionLocator);

		} else if (color == FolderColor.Purple) {

			actionLocator = Locators.zEditPropertiesDialogDropDown;
			optionLocator = Locators.zPurpleColorId;

			sClick(actionLocator);
			sClick(optionLocator);

		} else {
			throw new HarnessException("color " + color + " not yet implemented");
		}

	}

}
