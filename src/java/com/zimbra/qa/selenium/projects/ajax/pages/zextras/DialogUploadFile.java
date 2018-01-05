/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.pages.zextras;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;


public class DialogUploadFile extends AbsDialog {

	public static class Locators {
		public static final String zDialogClass = "css=div.ZmUploadDialog";
		public static final String zTitleCLass = "DwtDialogTitle";
		public static final String zDialogButtonsClass = "DwtDialogButtonBar";
	}

	public DialogUploadFile(AbsApplication application, AbsTab page) {
		super(application, page);
		logger.info("new " + DialogCheckInFile.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.zDialogClass;

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
			locator = "//div[@class='ZmUploadDialog']"
					+ "//*[contains(@class,'ZWidgetTitle') and contains(text(),'Cancel')]";
		} else if (button == Button.B_OK) {
			locator = "css=div[class='ZmUploadDialog'] div[class='DwtDialogButtonBar'] td[class='ZWidgetTitle']:contains('OK')";
		} else if (button == Button.B_BROWSE) {
			locator = "css=div[class='ZmUploadDialog'] input[name='uploadFile']";
			WebElement el = this.getElement(locator);
			if (webDriver() instanceof InternetExplorerDriver) {
				Actions action = new Actions(webDriver());
				action.moveToElement(el, 1, 1).doubleClick(el).build().perform();
			} else {
				//executeScript("arguments[0].click()", el);
				sClick(locator);
			}
			return null;
		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsVisible(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not visible!");
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
}