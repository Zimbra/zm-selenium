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

package com.zimbra.qa.selenium.projects.touch.ui;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class PageCreateTag extends AbsPage {

	public static class Locators {
		
		public static final String PageDivLocatorCSS = "css=div[class='x-dock x-dock-vertical x-sized']";
		public static final String PageNameLocatorCSS = "css=div[class='x-dock x-dock-vertical x-sized'] input[class='x-input-el x-form-field x-input-text']";
		
		public static final String zNewTagButton = "css=div[class='x-dock x-dock-vertical x-sized'] span[class='x-button-label']:contains('New Tag')";
		public static final String zDoneButton = "css=div[class='x-dock x-dock-vertical x-sized'] span[class='x-button-label']:contains('Done')";
		public static final String zEditButton = "css=div[class='x-dock x-dock-vertical x-sized'] span[class='x-button-label']:contains('Edit')";
		
		public static final String zEditTagNameField = "css=div[id^='ext-organizeredit'] div[id='ext-input-3'] input";
		public static final String zTagNameField = "css=div[id^='ext-organizeredit'] input[id='ext-element-210']";
		public static final String zColorButton = "css=div[id^='ext-organizeredit'] div[id='ext-colorselector-1'] div[id^='ext-element-']:nth-child(2)";
		public static final String zYellowColorButton = "css=div[id^='ext-organizeredit'] div[id='ext-colorselector-1'] div[class='zcs-tag-6']";
		public static final String zSaveButton = "css=div[class='x-dock x-dock-vertical x-sized'] span[class='x-button-label']:contains('Save')";
		public static final String zCancelButton = "css=div['id=ext-button-20'] span[class='x-button-label']:contains('Cancel')";
		public static final String zDeleteButton = "css=div[id^='ext-organizeredit-1'] div[class^='x-container zcs-folder-edit']:nth-child(2) span[class='x-button-label']:contains('Delete')";
		
		public static final String zYesWarningDialog	= "css=div[id='ext-sheet-1'] div[id^='ext-button'] span:contains('Yes')";
		public static final String zNoWarningDialog		= "css=div[id^='ext-sheet-1'] div[id^='ext-button'] span:contains('No')";
		public static final String zOkerrorDialog	= "css=div[id='ext-toolbar-3'] div[id^='ext-element']";
		
	}
	
	public PageCreateTag(AbsApplication application, AbsTab tab) {
		super(application);
		
		logger.info("new "+ PageCreateTag.class.getCanonicalName());

	}
	
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.PageDivLocatorCSS;
		
		SleepUtil.sleepSmall();
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
		
		SleepUtil.sleepSmall();
		if ( button == Button.B_NEW_TAG ) {
			locator = Locators.zNewTagButton;
			
		} else if ( button == Button.B_DONE ) {
			locator = Locators.zDoneButton;
			
		} else if ( button == Button.B_EDIT ) {
			locator = Locators.zEditButton;
			
		} else if ( button == Button.B_SAVE ) {
			locator = Locators.zSaveButton;
			
		} else if ( button == Button.B_CANCEL ) {
			locator = Locators.zCancelButton;
			
		}else if ( button == Button.B_DELETE ) {
			locator = Locators.zDeleteButton;
		
		} else if ( button == Button.B_YES ) {
				
				locator = Locators.zYesWarningDialog;
				page = this;
				
		} else if ( button == Button.B_NO ) {
				
				locator = Locators.zNoWarningDialog;
				page = this;

		} else if ( button == Button.B_OK ) {
			
			locator = Locators.zOkerrorDialog;
			page = this;

		}else {
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

		this.sClickAt(locator, "");
		SleepUtil.sleepSmall();
		
		return (page);
	}

	public void zEnterTagName(String tagName) throws HarnessException {
		logger.info(myPageName() + " zEnterTagName("+ tagName +")");

		tracer.trace("Enter tag name in text box "+ tagName);

		if ( tagName == null ) 
			throw new HarnessException("tag must not be null");

		String locator = Locators.zEditTagNameField;

		SleepUtil.sleepSmall();
		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("unable to find tag name field "+ locator);

		sType(locator, tagName);      
	}
	
	public void zSelectTag(String tagName) throws HarnessException {
		logger.info(myPageName() + " zSelectTag("+ tagName +")");
		
		if ( tagName == null ) 
			throw new HarnessException("tag must not be null");
		
		String locator = "css=div[class='x-dock x-dock-vertical x-sized'] div[class='zcs-menu-label']:contains('" + tagName + "')";
		
		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("unable to find tag in tree "+ locator);
		
		// Click to tag
		this.sClickAt(locator, "");
		SleepUtil.sleepSmall();

	}
	
	public boolean zVerifyTagExists(String tagName) throws HarnessException {
		logger.info(myPageName() + " zVerifyTagExists("+ tagName +")");
		String locator = "css=div[class='x-dock x-dock-vertical x-sized'] div[class='zcs-menu-label']:contains('" + tagName + "')";
		if (this.sIsElementPresent(locator) ) {
			return true;
		} else {
			return false;
		}	
	}
	
	public boolean zVerifyDialogText(String errorMessage) throws HarnessException {
		logger.info(myPageName() + " zVerifyDialogText("+ errorMessage +")");
		String locator = "css=div[id^='ext-element'] div[class='x-innerhtml']:contains('" + errorMessage + "')";
		if (this.sIsElementPresent(locator) ) {
			return true;
		} else {
			return false;
		}	
	}
		
}
