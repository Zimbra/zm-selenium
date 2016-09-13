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
/**
 * 
 */
package com.zimbra.qa.selenium.projects.ajax.ui.mail;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;


/**
 * Represents a "New Tag", "Rename Tag" dialog box
 * <p>
 * @author Matt Rhoades
 *
 */
public class DialogTag extends AbsDialog {

	public static class Locators {
	
		// TODO:  See https://bugzilla.zimbra.com/show_bug.cgi?id=54173
		public static final String zTagDialogId			= "CreateTagDialog";
		
		public static final String zTitleId	 			= "CreateTagDialog_title";

		public static final String zTagNameFieldId		= "CreateTagDialog_name";

		public static final String zTagColorPulldownId 	= "ZmTagColorMenu_dropdown";
		
		public static final String zButtonsId 			= "CreateTagDialog_buttons";
		public static final String zButtonOkId 			= "DWT178_title";
		public static final String zButtonCancelId 		= "DWT179_title";


	}
	
	
	public DialogTag(AbsApplication application, AbsTab tab) {
		super(application, tab);
		
		logger.info("new "+ DialogTag.class.getCanonicalName());
	}
	
	public void zSetTagName(String name) throws HarnessException {
		logger.info(myPageName() + " zSetTagName("+ name +")");

		tracer.trace("Set tag name "+ name);

		String locator = "id="+ Locators.zTagNameFieldId;
		
		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Tag name locator "+ locator +" is not present");
		}
		
		this.sType(locator, name);
		
	}
	
	public void zSetTagColor(String color) throws HarnessException {
		logger.info(myPageName() + " zSetTagColor("+ color +")");

		tracer.trace("Set tag color "+ color);

		throw new HarnessException("implement me!");
		
	}
	
	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton("+ button +")");

		tracer.trace("Click dialog button "+ button);

		AbsPage page = null;
		String locator = null;
		
		if ( button == Button.B_OK ) {
			
			// TODO: L10N this
			locator =  "//div[@id='"+ Locators.zTagDialogId +"']//div[@id='"+ Locators.zButtonsId +"']//td[text()='OK']";
			
		} else if ( button == Button.B_CANCEL ) {
			
			// TODO: L10N this
			locator =  "//div[@id='"+ Locators.zTagDialogId +"']//div[@id='"+ Locators.zButtonsId +"']//td[text()='Cancel']";

		} else {
			throw new HarnessException("Button "+ button +" not implemented");
		}
		
		this.zClick(locator);
		
		this.zWaitForBusyOverlay();
	
		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		
		// Need to implement for:
		
		// "Create New Tag"
		// "Tag name:"
		// "Blue", "Cyan", ..., "Orange", "More colors ..." (Tag color pulldown)
		// OK
		// Cancel
		
		throw new HarnessException("implement me");
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
		return ( this.sIsElementPresent(Locators.zTagDialogId) );
	}



}
