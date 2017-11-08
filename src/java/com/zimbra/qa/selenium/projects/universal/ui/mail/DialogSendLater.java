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
/**
 * 
 */
package com.zimbra.qa.selenium.projects.universal.ui.mail;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;

/**
 * Represents a "Specify Message Send Time" dialog box (Send Later)
 * 
 * See https://bugzilla.zimbra.com/show_bug.cgi?id=7524
 * See https://bugzilla.zimbra.com/show_bug.cgi?id=61935
 * 
 * <p>
 * @author Matt Rhoades
 *
 */
public class DialogSendLater extends AbsDialog {

	
	
	public static class Locators {
		
		// Main dialog locator
		// TODO: need to update this locator https://bugzilla.zimbra.com/show_bug.cgi?id=61935
		public static final String SendLaterDialogLocatorCSS	= "css=div[id^='ZmTimeDialog']";

		// Fields
		public static final String FieldDateLocator				= SendLaterDialogLocatorCSS + " input[id$='_date']";
		public static final String FieldTimeLocator				= SendLaterDialogLocatorCSS + " input[id$='_startTimeInput']";

		// Pulldowns
		public static final String PulldownTimezoneLocator		= SendLaterDialogLocatorCSS + " div[id$='_buttons'] td[id$='_dropdown'] div[class='ImgSelectPullDownArrow']";

		// Buttons
		public static final String ButtonOkButtonLocator		= SendLaterDialogLocatorCSS + " div[id$='_buttons'] td[id^='OK_'] td[id$='_title']";
		public static final String ButtonCancelButtonLocator	= SendLaterDialogLocatorCSS + " div[id$='_buttons'] td[id^='Cancel'] td[id$='_title']";
	}
	
	
	public DialogSendLater(AbsApplication application, AbsTab tab) {
		super(application, tab);
		
		logger.info("new "+ DialogSendLater.class.getCanonicalName());

	}
	

	/* (non-Javadoc)
	 * @see framework.ui.AbsDialog#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsVisible()");

		String locator = Locators.SendLaterDialogLocatorCSS;
		
		if ( !this.sIsElementPresent(locator) ) {
			return (false); // Not even present
		}
		
		if ( !this.zIsVisiblePerPosition(locator, 0, 0) ) {
			return (false);	// Not visible per position
		}
	
		// Yes, visible
		logger.info(myPageName() + " zIsVisible() = true");
		return (true);
		
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton("+ button +")");

		tracer.trace("Click dialog button "+ button);

		AbsPage page = null;
		String locator = null;
		
		
		if ( button == Button.B_OK ) {

			locator = Locators.ButtonOkButtonLocator;

			this.sClick(locator);

			this.zWaitForBusyOverlay();

			// Check the message queue
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();

			return (page);

		} else if ( button == Button.B_CANCEL ) {

			locator = Locators.ButtonCancelButtonLocator;

		} else {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		// Default behavior, click the locator
		//

		// Make sure the locator was set
		if ( locator == null ) {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Button "+ button +" locator "+ locator +" not present!");
		}

		this.sClick(locator);

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

	public static class Field {
		
		public static final Field Date = new Field("Date");
		public static final Field Time = new Field("Time");
		public static final Field Timezone = new Field("Timezone");
		
		
		private String field;
		private Field(String name) {
			field = name;
		}
		
		@Override
		public String toString() {
			return (field);
		}

	}
	

	public void zFill(Calendar calendar) throws HarnessException {
		logger.info(myPageName() + ".zFill("+ calendar +")");

		if ( calendar == null )
			throw new HarnessException("calendar cannot be null!");
		
		zFillField(Field.Date, calendar);
		zFillField(Field.Time, calendar);
		// TODO: zFillField(Field.Timezone, calendar);
		
	}


	public void zFillField(Field field, Calendar calendar) throws HarnessException {
		String value = null;
		
		if ( field == Field.Date ) {

			// TODO: need to INTL
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			value = format.format(calendar.getTime());
			
		} else if (field == Field.Time ) {
			
			// TODO: need to INTL
			SimpleDateFormat format = new SimpleDateFormat("HH:mm a");
			value = format.format(calendar.getTime());

		} else if ( field == Field.Timezone ) {
			
			throw new HarnessException("TODO: need to handle timezone as a pulldown");

		} else {
			throw new HarnessException("Unsupported field: "+ field);
		}
		
		if ( value == null || value.trim().length() == 0 ) {
			throw new HarnessException("value cannot be null or empty");
		}
	
		zFillField(field, value);
	}

	public void zFillField(Field field, String value) throws HarnessException {
		tracer.trace("Set "+ field +" to "+ value);

		String locator = null;
		
		if ( field == Field.Date ) {
			
			locator = Locators.FieldDateLocator;
			
			
			
		} else if ( field == Field.Time ) {
			
			locator = Locators.FieldTimeLocator;
			
			
			
		} else if ( field == Field.Timezone ) {
					
			throw new HarnessException("TODO: need to handle timezone as a pulldown");
			
		} else {
			throw new HarnessException("Unsupported field: "+ field);
		}
		
		if ( locator == null ) {
			throw new HarnessException("locator was null for field "+ field);
		}
		
		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("Field is not present field="+ field +" locator="+ locator);
		
		this.clearField(locator);
		this.sType(locator, value);
		
		this.zWaitForBusyOverlay();
		
	}


}
