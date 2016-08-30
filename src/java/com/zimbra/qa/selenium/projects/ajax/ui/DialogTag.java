/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;

/**
 * Represents a "New Tag", "Rename Tag" dialog box
 * <p>
 * @author Matt Rhoades
 *
 */
public class DialogTag extends AbsDialog {

	public static class Locators {
	
		// TODO:  See https://bugzilla.zimbra.com/show_bug.cgi?id=54173
		public static final String zTagDialogId		= "CreateTagDialog";
		
		public static final String zTitleId	 		= "CreateTagDialog_title";

		public static final String zTagNameFieldId	= "CreateTagDialog_name";
		
		public static final String zTagNameFieldCss = "css=input[id='CreateTagDialog_name']";

		public static final String zTagColorPulldownId = "ZmTagColorMenu_dropdown";
		
		public static final String zButtonsId 		= "CreateTagDialog_buttons";
		
		public static final String zChooseNewTagButton = "css=div[id='ZmPickTagDialog_buttons'] td[id^='New_'] td[id$='_title']";
		public static final String zButtonOkCss	= "css=div[id='CreateTagDialog_buttons'] td[id^='OK'] td[id$='_title']";
		public static final String zButtonCancelId 	= "DWT179_title";


	}
	
	
	public DialogTag(AbsApplication application, AbsTab tab) {
		super(application, tab);
		
		logger.info("new " + DialogTag.class.getCanonicalName());
	}
	
	public void zSetTagName(String name) throws HarnessException {
		logger.info(myPageName() + " zSetTagName("+ name +")");

		String locator = "css=input#"+ Locators.zTagNameFieldId ;
		
		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Tag name locator "+ locator +" is not present");
		}
		
		this.sType(locator, name);
		
	}
	
	public void zSetTagColor(String color) throws HarnessException {
		logger.info(myPageName() + " zSetTagColor("+ color +")");

		throw new HarnessException("implement me!");
		
	}
	
	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton("+ button +")");

		String locator = null;
		
		if ( button == Button.B_OK ) {
			
			locator = "css=div#CreateTagDialog td[id^='OK'] td[id$='title']";
			
		} else if ( button == Button.B_CANCEL ) {
			
			locator = "css=div#CreateTagDialog td[id^='Cancel'] td[id$='title']";
			
		} else {
			
			throw new HarnessException("Button "+ button +" not implemented");
			
		}
		
		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Button "+ button +" locator "+ locator +" not present!");
		}
		
		zClickAt(locator,"0,0");
		
		zWaitForBusyOverlay();
		return (null);
	}

	public void zSubmit(String tagName) throws HarnessException {
	   zSetTagName(tagName);
	   zSubmit();
	}
	
	public void zSubmit() throws HarnessException {

		// There seem to be some issues with the busy overlay for the new
		// tag dialog.  I don't think the client is setting it correctly.
		// So, check how many tags are on in the mailbox.  Then click
		// the OK button.  Then, make sure the number of tags increases.
		//
		// NOTE: this may break a test case that doesn't actually create
		// a new tag.  For instance, if you click on a OK when creating
		// a tag that already exists.
		//
		
		// Determine how many tags are currently in the mailbox
		this.MyApplication.zGetActiveAccount().soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");
		int original = this.MyApplication.zGetActiveAccount().soapSelectNodes("//mail:tag").length;

		// Click OK
		zClickButton(Button.B_OK);

		// Now, make sure more tags are in the mailbox.
		boolean found = false;
		for(int i = 0; i < 30 && !found; i++) {

			this.MyApplication.zGetActiveAccount().soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");
			int now = this.MyApplication.zGetActiveAccount().soapSelectNodes("//mail:tag").length;

			found = (now > original);

			SleepUtil.sleep(1000);

		}

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

		logger.info(myPageName() + " zIsActive()");

		String locator = "css=div[id="+ Locators.zTagDialogId + "]";

		if ( !this.sIsElementPresent(locator) ) {
			return (false); // Not even present
		}

		if ( !this.zIsVisiblePerPosition(locator, 0, 0) ) {
			return (false);	// Not visible per position
		}

		// Yes, visible
		logger.info(myPageName() + " zIsActive() = true");
		return (true);

		//return ( this.sIsElementPresent(Locators.zTagDialogId) );
	}



}
