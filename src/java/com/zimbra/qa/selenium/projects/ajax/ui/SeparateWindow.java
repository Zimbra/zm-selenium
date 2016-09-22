/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui;

import java.util.*;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;

public class SeparateWindow extends AbsSeparateWindow {

	// Windows that exist before this Show Original is opened
	protected List<String> existingWindowNames = null;
	
	// The selenium window name for this Window
	protected String DialogWindowName = null;
	
	/**
	 * Create a new Show Original Window
	 * 
	 * The Show Original will not have a window title.  So, we
	 * must use the window ID's from Selenium
	 * 
	 * The SeparateWindow object must be created before
	 * the window is opened, so that the harness will know
	 * which new window is opened.
	 *  
	 * @param application
	 */
	public SeparateWindow(AbsApplication application) {
		super(application);
		
		// Initialize existing names to empty
		existingWindowNames = new ArrayList<String>();
		
		// Use zInitializeWindowNames() and zSetWindowName()
		// to set the new window name
		this.DialogWindowName = null;
	}
	
		public static class Locators {
		
			public static final String openApptOnLaunchedWindow = "css= td[class='ZhAppContent']  table[class='ZhCalMonthTable'] div[class^='ZhCalMonthAppt'] >a";
			public static final String apptHeaderSubject = "css= td[class$='MsgHdrName']:contains('Subject')";
			public static final String apptHeaderOrganizer = "css= td[class$='MsgHdrName']:contains('Organizer')";
			public static final String apptValueSubject = "css= td[class$='MsgHdrValue']:contains('Test')";
		     
		}
	/**
	 * Call this before the Window is opened
	 */
	public void zInitializeWindowNames() throws HarnessException {
		logger.info(myPageName() + " zInitializeWindowNames()");
		
		// Get a list of existing window names before the Show Original is opened
		existingWindowNames = super.sGetAllWindowNames();
		
		// For logging
		for (String name : existingWindowNames) {
			logger.info("Existing name: "+ name);
		}

	}
	
	/**
	 * Determine if a new window opened
	 * Set DialogWindowName if found.
	 */
	public void zSetWindowName() throws HarnessException {		
		Set <String> windows = webDriver().getWindowHandles();
		String mainwindow = webDriver().getWindowHandle();

		for (String handle: windows) {
			webDriver().switchTo().window(handle);
		    if (!handle.equals(mainwindow)) {
		    	if (webDriver().switchTo().window(handle).getTitle().equals("")) {
			    	this.DialogWindowName = webDriver().switchTo().window(handle).getTitle();
					this.DialogWindowID = this.DialogWindowName;
		    	} else {
		    		this.DialogWindowName = "selenium_blank";
		    		this.DialogWindowID = this.DialogWindowName;
		    	}
				logger.info("Found window: " + this.DialogWindowName);
				return;
		    }
		}
	}
	

	/**
	 * Wait for the page to open.
	 * 
	 * Since the show original window doesn't have a title,
	 * this method waits for a new window to open, then assumes
	 * that new window is the Show Original.
	 * 
	 */
	public void zWaitForActive() throws HarnessException {
		logger.info(myPageName() + " zWaitForActive()");

		if ( DialogWindowName == null ) {

			for (int i = 0; i < 5; i++) {

				zSetWindowName();
				SleepUtil.sleep(5000);
				if (DialogWindowName != null ) {
					// Found it
					return;
				}

				logger.info("Waiting a second ...");
				SleepUtil.sleep(1000);
			}
			
		} else if ( DialogWindowName.contains("selenium_blank")) {
			if (DialogWindowName != null ) {
				this.sSelectWindow(DialogWindowName);
				this.sWindowFocus();
				// Found it
				return;
			}

		} else {
			throw new HarnessException("Window never became active!");
		}
	}
	
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		if ( this.DialogWindowName == null ) {
			//throw new HarnessException("Window Title is null.  Use zSetWindowName() first.");
		}
		
		for (String name : super.sGetAllWindowNames()) {
			logger.info("Window name: "+ name);
			if ( name.toLowerCase().contains(DialogWindowName.toLowerCase()) ) {
				logger.info("zIsActive() = true ... title = "+ DialogWindowName);
				return (true);
			}
		}
		
		logger.info("zIsActive() = false");
		return (false);
		
	}

	@Override
	public String myPageName() {
		return (this.getClass().getCanonicalName());
	}

}
