/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.ui.calendar;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;

public class DialogCustomRepeat extends AbsDialog {

	public static class DialogWarningID {
		
		public static final DialogWarningID DialogCustomRepeat = new DialogWarningID("DwtDialog");

		protected String Id;
		public DialogWarningID(String id) {
			Id = id;
		}
	}
	
	protected String MyDivId = null;
	
	
	public DialogCustomRepeat(DialogWarningID dialogId, AbsApplication application, AbsTab tab) {
		super(application, tab);
	
		MyDivId = dialogId.Id;
		
		logger.info("new " + DialogCustomRepeat.class.getCanonicalName());

	}
	
	public String zGetWarningTitle() throws HarnessException {
		String locator = "css=div[id='"+ MyDivId +"'] td[id='"+ MyDivId +"_title']";
		return (zGetDisplayedText(locator));
	}
	
	public String zGetWarningContent() throws HarnessException {	
		String locator = "css=td[id^='MessageDialog'][class='DwtMsgArea']";
		return (zGetDisplayedText(locator));
	}
	
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		if ( button == null )
			throw new HarnessException("button cannot be null");

		String locator = null;
		AbsPage page = null; 
		
		String buttonsTableLocator = "css=div[class='"+ MyDivId +"']";
		
		if (button == Button.B_OK) {
			
			locator = buttonsTableLocator + " td[id$='_button2_title']:contains('OK')";
			
		} else if (button == Button.B_CANCEL) {

			locator = buttonsTableLocator + " td[id$='_button1_title']:contains('Cancel')";

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		// Click it
		sClickAt(locator, "");

		// If the app is busy, wait for it to become active
		zWaitForBusyOverlay();

		// This dialog might send message(s), so wait for the queue
		Stafpostqueue sp = new Stafpostqueue();
		sp.waitForPostqueue();
		
		SleepUtil.sleepMedium();

		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		if ( locator == null )
			throw new HarnessException("locator cannot be null");
		
		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("locator cannot be found");
		
		return (this.sGetText(locator));
		
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		
		if ( !this.sIsElementPresent(MyDivId) )
			return (false);
	
		if ( !this.zIsVisiblePerPosition(MyDivId, 0, 0) )
			return (false);
		
		return (true);
	}

}
