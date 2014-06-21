/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui.addressbook;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;


public class DialogNewContactGroup extends AbsDialog {

	public static class Locators {
		public static final String WINDOW_DIALOGNAME = "css=div#CreateContactGroupDialog";
		public static final String INPUT_GROUPNAME = "css=input#CreateContactGroupDialog_name";
		public static final String BUTTON_SAVE     = "css=div#CreateContactGroupDialog_button2";
		public static final String BUTTON_CANCEL   = "css=div#CreateContactGroupDialog_button1";
	}

	public DialogNewContactGroup(AbsApplication application, AbsTab page) {
		super(application, page);

		logger.info("new " + DialogNewContactGroup.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}


	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		if ( !this.sIsElementPresent(Locators.WINDOW_DIALOGNAME) ) {
			return (false); // Not even present
		}

		if ( !this.zIsVisiblePerPosition(Locators.WINDOW_DIALOGNAME, 0, 0) ) {
			return (false);	// Not visible per position
		}

		// Yes, visible
		logger.info(myPageName() + " zIsVisible() = true");
		return (true);
	}


	public void zEnterGroupName(String name) throws HarnessException {
		logger.info(myPageName() + " zSetGroupName("+ name +")");

		String locator = Locators.INPUT_GROUPNAME;

		this.sType(locator, name);
		this.zWaitForBusyOverlay();

	}


	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton("+ button +")");

		AbsPage page = null;
		String locator = null;

		if ( button == Button.B_OK ) {

			locator = Locators.BUTTON_SAVE;
			page = null;

		} else if ( button == Button.B_CANCEL ) {

			locator = Locators.BUTTON_CANCEL;
			page = null;

		} else {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		// Default behavior, click the locator
		//

		// Make sure the locator was set
		if ( locator == null ) {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		this.zClick(locator);
		zWaitForBusyOverlay();

		if ( page != null ) {
			page.zWaitForActive();
		}

		return (null);
	}
}
