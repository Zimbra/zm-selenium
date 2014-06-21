/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013 Zimbra, Inc.
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
package com.zimbra.qa.selenium.framework.ui;

import com.zimbra.qa.selenium.framework.util.HarnessException;



/**
 * A <code>AbsDialogSeparateWindow</code> object represents a "popup dialog", 
 * such as a new folder, new tag, error message, etc. - in a separate window
 * <p>
 * 
 * @author Matt Rhoades
 *
 */
public abstract class AbsDialogSeparateWindow extends AbsSeparateWindow {
	
	protected AbsSeparateWindow MyWindow = null;
	
	public AbsDialogSeparateWindow(AbsApplication application, AbsSeparateWindow window) {
		super(application);
		
		MyWindow = window;
		
		logger.info("new "+ AbsDialogSeparateWindow.class.getCanonicalName());

	}
	
	public AbsPage zClickButton(Button button) throws HarnessException {
		throw new HarnessException("implement me!");
	}

	/**
	 * Check if this dialog is active.
	 * First, check that the separate window is active.
	 * Second, check if the dialog is visible.
	 * 
	 */
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		if ( !MyWindow.zIsActive() ) {
			logger.debug("separate window is not active");
			return (false);
		}
		
		// Define whether this dialog is active in the extending class
		
		return (true);
	}
	
}
