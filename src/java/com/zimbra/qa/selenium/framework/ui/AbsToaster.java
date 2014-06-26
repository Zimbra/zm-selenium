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
package com.zimbra.qa.selenium.framework.ui;

import java.awt.event.KeyEvent;

import com.zimbra.qa.selenium.framework.util.*;


/**
 * An abstraction of the toaster message that occurs in the Ajax client
 * @author Matt Rhoades, Dieu Nguyen
 *
 */
public abstract class AbsToaster extends AbsSeleniumObject {
	
	public static class Locators {
		
		public static final String ToastDivContainerCSS = "css=div[id='z_toast']";
		public static final String idVisibleLocator = "z_toast";
		
		public static final String ToastTextLocatorCSS   = ToastDivContainerCSS + " div[id='z_toast_text']";

		public static final String ToastUndoLocatorCSS = ToastDivContainerCSS + " a.undo";
		

	}
    
    
    
	/**
	 * A pointer to the application that created this object
	 */
	protected AbsApplication MyApplication = null;

	/**
	 * Create this page object that exists in the specified application
	 * @param application
	 */
	public AbsToaster(AbsApplication application) {
		MyApplication = application;
		
		logger.info("new "+ AbsToaster.class.getCanonicalName());
	}

	/**
	 * Returns the displayed text in the toaster
	 * @return
	 * @throws HarnessException
	 */
	public String zGetToastMessage() throws HarnessException {
		String text=null;		
		
		zWaitForElementVisible(Locators.ToastTextLocatorCSS);
    	text=sGetText(Locators.ToastTextLocatorCSS);
    	    
    	//make the toasted message invisible if it contains "Undo" link
    	/* Reason for adding If condition:
    	 * For some cases like Signature Toaster(Bug_78058)
    	 * unnecessary warning dialog gets appeared every time after pressing Escape key
    	 */
    	if (sIsElementPresent(Locators.ToastUndoLocatorCSS)) {
			sKeyPressNative(String.valueOf(KeyEvent.VK_ESCAPE));
		}    	    	
    	zWaitForElementInvisible(Locators.ToastTextLocatorCSS);
		return text;					
	}
	
	/**
	 * Click Undo in the toaster
	 * @param text
	 * @return
	 * @throws HarnessException
	 */
	public void zClickUndo() throws HarnessException {
		sClick(Locators.ToastUndoLocatorCSS);
		zWaitForBusyOverlay();
	}
	
    public boolean zIsActive() throws HarnessException {        
    	return zIsVisiblePerPosition(Locators.ToastDivContainerCSS,0,0);
    	
    }

	public void zWaitForActive() throws HarnessException {
		zWaitForActive(AbsPage.PageLoadDelay);
	}
    
	public void zWaitForActive(long millis) throws HarnessException {
		if ( zIsActive() ) {
			return; // Toaster is already active
		}
		
		do {
			SleepUtil.sleep(SleepUtil.SleepGranularity);
			millis = millis - SleepUtil.SleepGranularity;
			if ( zIsActive() ) {
				return; // Toaster became active
			}
		} while (millis > SleepUtil.SleepGranularity);
		
		SleepUtil.sleep(millis);
		if ( zIsActive() ) {
			return;	// Toaster became active
		}

		throw new HarnessException("Toaster never became active");
	}

	public void zWaitForClose() throws HarnessException {
		zWaitForClose(AbsPage.PageLoadDelay);
	}
    
	public void zWaitForClose(long millis) throws HarnessException {
		if ( !zIsActive() ) {
			return; // Toaster is already closed
		}
		
		do {
			SleepUtil.sleep(SleepUtil.SleepGranularity);
			millis = millis - SleepUtil.SleepGranularity;
			if ( !zIsActive() ) {
				return; // Toaster closed
			}
		} while (millis > SleepUtil.SleepGranularity);
		
		SleepUtil.sleep(millis);
		if ( !zIsActive() ) {
			return;	// Toaster closed
		}

		throw new HarnessException("Toaster never closed");
	}
	
    


}
