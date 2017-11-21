/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.pages;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class DialogAddToCalendar extends AbsDialog {

	public static class Locators {
		public static final String DialogDivLocatorCSS = "css=div[id='ChooseFolderDialog']";

		// Textfields
		public static final String DialogNameLocatorCSS = "css=input[id='CreateNewFolderDialog_name']";
		
		// Calendar
		public static final String CalendarFolder = "css=div[id='ChooseFolderDialog'] td[id='zti__ZmChooseFolderDialog_Calendar__10_textCell']";
		
		// Buttons
		public static final String zOkButton = "css=div[id='ChooseFolderDialog_buttons'] td[id^='OK_'] td[id$='_title']";
		public static final String zCancelButton = "css=div[id='ChooseFolderDialog_buttons'] td[id^='Cancel_'] td[id$='_title']";
		public static final String zNewButton = "css=div[id='ChooseFolderDialog_buttons'] td[id^='New_'] td[id$='_title']";
	}
	
	
	public DialogAddToCalendar(AbsApplication application, AbsTab tab) {
		super(application, tab);
		logger.info("new "+ DialogAddToCalendar.class.getCanonicalName());
	}
	
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.DialogDivLocatorCSS;
		
		if ( !this.sIsElementPresent(locator) ) {
			return (false); // Not even present
		}
		
		if ( !this.zIsVisiblePerPosition(locator, 0, 0) ) {
			return (false);
		}
	
		logger.info(myPageName() + " zIsActive() = true");
		return (true);
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton("+ button +")");

		tracer.trace("Click dialog button "+ button);

		AbsPage page = null;
		String locator = null;
		
		if ( button == Button.B_OK ) {

			locator = Locators.zOkButton;

			this.sClick(locator);

			this.zWaitForBusyOverlay();

			return (page);

		} else if ( button == Button.B_CANCEL ) {

			locator = Locators.zCancelButton;
		
		} else if ( button == Button.B_NEW) {

			locator = Locators.zNewButton;

		} else {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		// Make sure the locator was set
		if ( locator == null ) {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Button "+ button +" locator "+ locator +" not present!");
		}

		this.sClickAt(locator, "");
		SleepUtil.sleepMedium();
		
		this.zWaitForBusyOverlay();

		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		logger.info(myPageName() + " zGetDisplayedText("+ locator +")");
		
		if ( locator == null )
			throw new HarnessException("locator was null");
		
		return (this.sGetText(locator));
	}

	public void sClickTreeFolder(FolderItem folder) throws HarnessException {
		logger.info(myPageName() + " sClickTreeFolder("+ folder +")");
		
		if ( folder == null ) 
			throw new HarnessException("folder must not be null");
		
		tracer.trace("Click on tree folder with name "+ folder.getName());

		String locator = Locators.DialogDivLocatorCSS + " td[id='zti__ZmChooseFolderDialog_Mail__"+ folder.getId() +"_textCell']";
		
		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("unable to find folder in tree "+ locator);
		
		this.sClick(locator);
		
		this.zWaitForBusyOverlay();

	}
	
	public void zChooseCalendarFolder(String folderID) throws HarnessException {
		
		if ( folderID == null ) 
			throw new HarnessException("folder must not be null");
		
		String locator = Locators.DialogDivLocatorCSS + " td[id='zti__ZmChooseFolderDialog_Calendar__" + folderID + "_textCell']";
		
		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("unable to find folder in tree "+ locator);
		
		this.sClick(locator);
		
		this.zWaitForBusyOverlay();

	}

	public void zEnterFolderName(String folder) throws HarnessException {
		logger.info(myPageName() + " zEnterFolderName("+ folder +")");

		tracer.trace("Enter folder name in text box "+ folder);

		if ( folder == null ) 
			throw new HarnessException("folder must not be null");

		String locator = Locators.DialogNameLocatorCSS;

		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("unable to find folder name field "+ locator);

		sType(locator, folder);      
	}

}
