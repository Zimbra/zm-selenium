/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.ui;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;

/**
 * Represents a 2fa enable dialog box
 * <p>
 * @author Nidhi Vyas
 *
 */
public class Dialog2FactorAuthEnable extends AbsDialog {

	public static class Locators {
	
		public static final String z2FASetUpDialogId = "DwtDialogTitle";
		
	}
	
	public Dialog2FactorAuthEnable(AbsApplication application, AbsTab tab) {
		super(application, tab);
		
		logger.info("new " + Dialog2FactorAuthEnable.class.getCanonicalName());
	}
		
	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton("+ button +")");

		String locator = null;
		
		if ( button == Button.B_CANCEL ) {
			
			locator = "css=div[class='DwtDialog'] div[class='DwtDialogButtonBar'] td[id$='_button10_title']:contains('Cancel')";
			
		} else if ( button == Button.B_BEGIN_SETUP ) {
			
			locator = "css=div[class='DwtDialog'] div[class='DwtDialogButtonBar'] td[id$='_button17_title']:contains('Begin Setup')";
			
			if ( !this.sIsElementPresent(locator) ) {
			
				locator = "css=div[class='DwtDialog'] div[class='DwtDialogButtonBar'] td[id$='_button7_title']:contains('Begin Setup')";
				
			}
			
		} else if ( button == Button.B_NEXT ) {
			
			locator = "css=div[class='DwtDialog'] div[class='DwtDialogButtonBar'] td[id$='_button18_title']:contains('Next')";
			
			if ( !this.sIsElementPresent(locator) ) {
				
				locator = "css=div[class='DwtDialog'] div[class='DwtDialogButtonBar'] td[id$='_button8_title']:contains('Next')";
				
			}
			
		} else if ( button == Button.B_FINISH ) {
			
			locator = "css=div[class='DwtDialog'] div[class='DwtDialogButtonBar'] td[id$='_button19_title']:contains('Finish')";
			
			if ( !this.sIsElementPresent(locator) ) {
				
				locator = "css=div[class='DwtDialog'] div[class='DwtDialogButtonBar'] td[id$='_button9_title']:contains('Finish')";
				
			}
			
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

	public void zSetTotpCode(String totpCode) throws HarnessException {
		logger.info(myPageName() + " zSetTotpCode("+ totpCode +")");

		String locator = "css=td[class='WindowInnerContainer'] div[class='ZmTwoFactorSetupContainer'] input[id$='_code_input']";
		
		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Secret key locator "+ locator +" is not present");
		}
		
		this.zClickAt(locator, "");
		this.zKeyboard.zTypeCharacters(totpCode);
		
	}

	public String zGetSecretKey() throws HarnessException {
		logger.info(myPageName() + " zGetSecretKey");

		String locator = "css=td[class='WindowInnerContainer'] div[class='ZmTwoFactorSetupContainer'] span[id$='_email_key']" ;
		
		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Secret key "+ locator +" is not present");
		}
		
		return(this.sGetText(locator));
		
	}
	
	
	public void zSetUserPassword(String password) throws HarnessException {
		logger.info(myPageName() + " zSetUserPassword("+ password +")");

		String locator = "css=div[class='DwtDialog'] td[class='WindowInnerContainer'] input[id$='_password_input']" ;
		
		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Password "+ locator +" is not present");
		}
		this.zClickAt(locator, "");
		this.zKeyboard.zTypeCharacters(password);
	}
	
	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		
		throw new HarnessException("implement me");
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		logger.info(myPageName() + " zIsActive()");

		String locator = "css=div[id="+ Locators.z2FASetUpDialogId + "]";

		if ( !this.sIsElementPresent(locator) ) {
			return (false); // Not even present
		}

		if ( !this.zIsVisiblePerPosition(locator, 0, 0) ) {
			return (false);	// Not visible per position
		}

		logger.info(myPageName() + " zIsActive() = true");
		return (true);

	}

}
