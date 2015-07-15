/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
/**
 * 
 */
package com.zimbra.qa.selenium.projects.touch.ui;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class PageCreateFolder extends AbsPage {

	public static class Locators {

		public static final String PageDivLocatorCSS = "css=div[class='x-dock x-dock-vertical x-sized']";
		public static final String PageNameLocatorCSS = "css=div[class='x-dock x-dock-vertical x-sized'] input[class='x-input-el x-form-field x-input-text']";

		public static final String zNewFolderButton = "css=div[class='x-dock x-dock-vertical x-sized'] span[class='x-button-label']:contains('New Folder')";
		public static final String zDoneButton = "css=div[class='x-dock x-dock-vertical x-sized'] span[class='x-button-label']:contains('Done')";
		public static final String zEditButton = "css=div[class='x-dock x-dock-vertical x-sized'] span[class='x-button-label']:contains('Edit')";

		public static final String zFolderNameField = "css=div[class='x-dock x-dock-vertical x-sized'] input[class='x-input-el x-form-field x-input-text']";
		public static final String zLocationButton = "css=span[class='x-button-icon x-shown forward']";
		public static final String zSaveButton = "css=div[class='x-dock x-dock-vertical x-sized'] span[class='x-button-label']:contains('Save')";
		public static final String zDeleteButton = "css=div[id^='ext-organizeredit-1'] div[class^='x-container zcs-folder-edit']:nth-child(1) span[class='x-button-label']:contains('Delete')";
		public static final String zSubFolderIcon = "css=div[class='x-unsized x-list-disclosure']";

	}

	public PageCreateFolder(AbsApplication application, AbsTab tab) {
		super(application);

		logger.info("new "+ PageCreateFolder.class.getCanonicalName());

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.PageDivLocatorCSS;

		if ( !this.sIsElementPresent(locator) ) {
			return (false); // Not even present
		}

		if ( !this.zIsVisiblePerPosition(locator, 0, 0) ) {
			return (false);	// Not visible per position
		}

		// Yes, visible
		logger.info(myPageName() + " zIsActive() = true");
		return (true);

	}

	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton("+ button +")");
		tracer.trace("Click page button "+ button);

		AbsPage page = null;
		String locator = null;

		if ( button == Button.B_NEW_FOLDER ) {
			locator = Locators.zNewFolderButton;

		} else if ( button == Button.B_DONE ) {
			locator = Locators.zDoneButton;

		} else if ( button == Button.B_EDIT ) {
			locator = Locators.zEditButton;
			this.sClickAt("css=span[class='x-button-icon x-shown organizer']", "0,0");


		} else if ( button == Button.B_LOCATION ) {
			locator = Locators.zLocationButton;

		} else if ( button == Button.B_SAVE ) {
			locator = Locators.zSaveButton;

		} else if ( button == Button.B_DELETE ) {
			locator = Locators.zDeleteButton;

		} else if ( button == Button.B_SUBFOLDER_ICON ) {
			locator = Locators.zSubFolderIcon;

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

		/*if ( button == Button.B_EDIT ) {
			SleepUtil.sleepMedium();
			locator = Locators.zEditButton;
			this.sClickAt("css=span[class='x-button-icon x-shown organizer']", "0,0");
		}*/
		this.sClickAt(locator, "");
		SleepUtil.sleepLong();

		return (page);
	}

	public void zSelectFolder(String folderName) throws HarnessException {

		logger.info(myPageName() + " zSelectFolder("+ folderName +")");

		if ( folderName == null ) 
			throw new HarnessException("folder must not be null");

		String locator = "css=div[class='x-dock x-dock-vertical x-sized'] div[class='zcs-menu-label']:contains('" + folderName + "')";

		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("unable to find folder in tree "+ locator);

		// Click to locations to select sub folder
		//this.sClickAt(Locators.zLocationButton, "");
		//SleepUtil.sleepSmall();

		this.sClickAt(locator, "");
		this.zWaitForBusyOverlay();
	}

	public AbsPage zSelectMountFolder(String mountpointname)  throws HarnessException {
		logger.info(myPageName() + " zSelectFolder("+ mountpointname +")");
		tracer.trace("Click page button "+ mountpointname);

		//public void zSelectMountFolder(String mountpointname) throws HarnessException {
		AbsPage page = null;


		if ( mountpointname == null ) 
			throw new HarnessException("folder must not be null");
		this.sClickAt("css=span[class='x-button-icon x-shown organizer']", "0,0");

		String locator = "css=div[class='x-dock x-dock-vertical x-sized'] div[class='zcs-menu-label']:contains('" + mountpointname + "')";

		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("unable to find folder in tree "+ locator);



		this.sClickAt(locator, "");
		this.zWaitForBusyOverlay();
		return (page);
	}
		public AbsPage zSelectOrganizer()  throws HarnessException {
			
			AbsPage page = null;
			this.sClickAt("css=span[class='x-button-icon x-shown organizer']", "0,0");
			this.zWaitForBusyOverlay();
			return (page);

		}

		public void zEnterFolderName(String folderName) throws HarnessException {
			logger.info(myPageName() + " zEnterFolderName("+ folderName +")");

			tracer.trace("Enter folder name in text box "+ folderName);

			if ( folderName == null ) 
				throw new HarnessException("folder must not be null");

			String locator = Locators.zFolderNameField;

			if ( !this.sIsElementPresent(locator) )
				throw new HarnessException("unable to find folder name field "+ locator);

			sType(locator, folderName);      
		}

		public boolean zVerifyFolderExists(String folderName) throws HarnessException {
			logger.info(myPageName() + " zVerifyFolderExists("+ folderName +")");
			String locator = "css=div[class='x-dock x-dock-vertical x-sized'] div[class='zcs-menu-label']:contains('" + folderName + "')";
			if (this.sIsElementPresent(locator) ) {
				return true;
			} else {
				return false;
			}	}

		public boolean zVerifyMountFolderExists(String mountpointname) throws HarnessException {
			logger.info(myPageName() + " zVerifyFolderExists("+ mountpointname +")");
			String locator = "css=div[class='x-dock x-dock-vertical x-sized'] div[class='zcs-menu-label']:contains('" + mountpointname + "')";
			if (this.sIsElementPresent(locator) ) {
				return true;
			} else {
				return false;
			}	
		}
	}
