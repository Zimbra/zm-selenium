package com.zimbra.qa.selenium.projects.ajax.ui.zimlet;

/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;

/**
 * Represents a "Add Document Version Notes" dialog box
 * <p>
 */
public class DialogViewCertificate extends AbsDialog {

	public static class Locators {
		public static final String zDialogClass = "DwtDialog";
		public static final String zDialogTitleClass = "css=td[class=DwtDialogTitle]";
		public static final String zDialogButtonsClass = "DwtDialogButtonBar";
	}

	public DialogViewCertificate(AbsApplication application, AbsTab tab) {
		super(application, tab);
		logger.info("new " + DialogViewCertificate.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.zDialogTitleClass;

		if (!this.sIsElementPresent(locator)) {
			return (false); // Not even present
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false); // Not visible per position
		}

		// Yes, visible
		logger.info(myPageName() + " zIsActive() = true");
		return (true);
	}

	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton(" + button + ")");
		tracer.trace("Click dialog button " + button);

		String locator = null;

		if (button == Button.B_OK) {
			locator = "css=div[class='" + Locators.zDialogClass + "'] "
					+ "div[class='" + Locators.zDialogButtonsClass
					+ "'] td[class=ZWidgetTitle]:contains(OK)";
		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator "
					+ locator + " not present!");
		}
		this.zClickAt(locator, "0,0");

		return (null);
	}
	
	@Override
	public String zGetDisplayedText(String issuedToEmail) throws HarnessException {
		logger.info(myPageName() + " Issued to email");

		String locator = "css=div[id='ZmMsgDialog_content'] table tbody tr:nth-child(5) td[class='Value']" ;
		
		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Issued to email "+ locator +" is not present");
		}
		
		return(this.sGetText(locator));
	}

	public String zGetDisplayedTextIssuedToOrganization() throws HarnessException {
		logger.info(myPageName() + " Issued to Organisation");

		String locator = "css=div[id='ZmMsgDialog_content'] table tbody tr:nth-child(3) td[class='Value']" ;
		
		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Issued to email "+ locator +" is not present");
		}
		
		return(this.sGetText(locator));
	}
	public String zGetDisplayedTextIssuedByOrganization() throws HarnessException {
		logger.info(myPageName() + " Issued by Organisation");

		String locator = "css=div[id='ZmMsgDialog_content'] table tbody tr:nth-child(10) td[class='Value']" ;
		
		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Issued to email "+ locator +" is not present");
		}
		
		return(this.sGetText(locator));
	}
	public String zGetDisplayedTextIssuedByEmail() throws HarnessException {
		logger.info(myPageName() + " Issued by email");

		String locator = "css=div[id='ZmMsgDialog_content'] table tbody tr:nth-child(11) td[class='Value']" ;
		
		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Issued to email "+ locator +" is not present");
		}
		
		return(this.sGetText(locator));
	}
	public String zGetDisplayedTextAlgorithm() throws HarnessException {
		logger.info(myPageName() + " Algorithm");

		String locator = "css=div[id='ZmMsgDialog_content'] table tbody tr:nth-child(19) td[class='Value']" ;
		
		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Issued to email "+ locator +" is not present");
		}
		
		return(this.sGetText(locator));
	}

}
