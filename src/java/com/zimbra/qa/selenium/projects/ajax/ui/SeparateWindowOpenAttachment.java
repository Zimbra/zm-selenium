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
package com.zimbra.qa.selenium.projects.ajax.ui;

import java.util.*;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;

public class SeparateWindowOpenAttachment extends AbsSeparateWindow {

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
	public SeparateWindowOpenAttachment(AbsApplication application) {
		super(application);
		
		// Initialize existing names to empty
		existingWindowNames = new ArrayList<String>();
		
		// Use zInitializeWindowNames() and zSetWindowName()
		// to set the new window name
		this.DialogWindowName = null;
	}

	/**
	 * Call this before the Show Original is opened
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
		logger.info(myPageName() + " zSetWindowName()");
		
		for (String name : super.sGetAllWindowNames()) {
			if ( existingWindowNames.contains(name) ) {
				logger.info("Already existing Name: "+ name);
			} else {
				logger.info("Found my Name: "+ name);
				this.DialogWindowName = name;
				this.DialogWindowID = name;
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

			for (int i = 0; i < 15; i++) {

				zSetWindowName();
				if (DialogWindowName != null ) {
					// Found it
					return;
				}

				logger.info("Waiting a second ...");
				SleepUtil.sleep(1000);
			}
		}

		throw new HarnessException("Window never became active!");

	}
	
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		if ( this.DialogWindowName == null )
			throw new HarnessException("Window Title is null.  Use zSetWindowName() first.");
		
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
