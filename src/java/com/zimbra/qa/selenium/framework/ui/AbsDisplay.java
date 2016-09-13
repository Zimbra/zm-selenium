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
package com.zimbra.qa.selenium.framework.ui;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.zimbra.qa.selenium.framework.util.HarnessException;

/**
 * A <code>AbsDisplay</code> object represents a read-only "displayed object", 
 * such as a message, contact, appointment (as attendee), document, etc.
 * <p>
 * Displayed objects are usually returned after opening or selecting an
 * object from a list, such as double-clicking on a message from the mail
 * message list view.
 * <p>
 * The Display object allows the test method to access displayed information,
 * such as To addresses, subject, message body, contact field, appointment
 * start time, etc.
 * <p>
 * 
 * @author Matt Rhoades
 *
 */
public abstract class AbsDisplay extends AbsPage {
	protected static Logger logger = LogManager.getLogger(AbsDisplay.class);

	/**
	 * Create this page object that exists in the specified application
	 * @param application
	 */
	public AbsDisplay(AbsApplication application) {
		super(application);
		
		logger.info("new AbsDisplayPage");
	}
		
	
	/**
	 * Return the unique name for this page class
	 * @return
	 */
	public abstract String myPageName();
	
	/**
	 * Click on a Button in the display
	 */
	public abstract AbsPage zPressButton(Button button) throws HarnessException;
	
}
