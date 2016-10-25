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
package com.zimbra.qa.selenium.projects.ajax.ui;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsSeparateWindow;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning.DialogWarningID;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;

public class SeparateWindowDialog extends AbsSeparateWindow {

	protected String MyDivId = null;

	public SeparateWindowDialog(DialogWarningID dialogId, AbsApplication application, AbsSeparateWindow window) {
		super(application);
		MyDivId = dialogId.Id;
		logger.info("new " + SeparateWindowDialog.class.getCanonicalName());
	}

	public SeparateWindowDialog(AbsApplication application) {
		super(application);
	}

	@Override
	public String myPageName() {
		return null;
	}

	public void zSetWindowTitle(String title) throws HarnessException {
		DialogWindowTitle = title;
	}

	public void zSetWindowID(String id) throws HarnessException {
		this.DialogWindowID = id;
	}

	public AbsPage zClickButton(Button button) throws HarnessException {
		if ( button == null )
			throw new HarnessException("button cannot be null");

		String locator = null;
		AbsPage page = null;

		String buttonsTableLocator = "css=div[id^=" + MyDivId + "] div[id$='_buttons']";

		if ( button == Button.B_YES ) {

			locator = buttonsTableLocator + " td[id^='Yes_'] td[id$='_title']";
			if (!sIsElementPresent(locator)) {
				locator = "css=div[class^='DwtDialog'] div[id$='_buttons'] td[id$='_title']:contains('Yes')";
				sIsElementPresent(locator);
			}
			if (MyDivId.contains("css=div[class=DwtConfirmDialog]")) {
				page = 	new FormMailNew(this.MyApplication);
			}

		} else if ( button == Button.B_NO ) {

			locator = buttonsTableLocator + " td[id^='No_'] td[id$='_title']";

		} else if ( button == Button.B_CANCEL ) {

			locator = buttonsTableLocator + " td[id^='Cancel_'] td[id$='_title']";

		} else if (button == Button.B_OK) {

			locator = buttonsTableLocator + " td[id^='OK_'] td[id$='_title']";

		} else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		// Click it
		sClick(locator);
		
		if ( button == Button.B_YES ) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}
		
		return (page);
	}
}