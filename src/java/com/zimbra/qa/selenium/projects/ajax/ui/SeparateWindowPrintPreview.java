/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui;

import java.awt.event.*;
import java.util.*;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;

public class SeparateWindowPrintPreview extends SeparateWindow {

	
	/**
	 * Create a new Print Preview Window
	 * 
	 * @param application
	 */
	public SeparateWindowPrintPreview(AbsApplication application) {
		super(application);
		
		// Initialize existing names to empty
		existingWindowNames = new ArrayList<String>();
		
		// Use zInitializeWindowNames() and zSetWindowName()
		// to set the new window name
		this.DialogWindowName = null;
	}

	protected boolean IsDismissed = false;
	
	/**
	 * Type "<ESC>" in the OS Print Dialog to close it
	 * 
	 * @throws HarnessException
	 */
	public void zDismissPrintDialog() throws HarnessException {
		
		if ( IsDismissed ) {
			return; // Already dismissed
		}
		
		// Wait for the print dialog
		SleepUtil.sleepMedium();
		zKeyboard.zTypeKeyEvent(KeyEvent.VK_ESCAPE);
		
		IsDismissed = true;
	}
	
	/**
	 * Determine if a new window opened
	 * Set DialogWindowName if found.
	 */
	public void zSetWindowName() throws HarnessException {
		zDismissPrintDialog(); // On the first attempt, dismiss the print dialog
		super.zSetWindowName();
	}
	

}
