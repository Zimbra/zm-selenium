/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsSeparateWindow;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning.DialogWarningID;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;

public class SeparateWindowDialog extends AbsSeparateWindow {

	protected String MyDivId = null;

	public SeparateWindowDialog(DialogWarningID dialogId, AbsApplication application, AbsSeparateWindow window) {
		super(application);

		// Remember which div this object is pointing at
		/*
		 * Example:
		 * <div id="YesNoCancel" style="position: absolute; overflow: visible; left: 229px; top: 208px; z-index: 700;" class="DwtDialog" parentid="z_shell">
		 *   <div class="DwtDialog WindowOuterContainer">
		 *   ...
		 *   </div>
		 * </div>
		 */
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
		AbsPage page = null; 		// Does this ever result in a page being returned?

		// See http://bugzilla.zimbra.com/show_bug.cgi?id=54560
		// Need unique id's for the buttons
		String buttonsTableLocator = "css=div[id='"+ MyDivId +"'] div[id$='_buttons']";

		if ( button == Button.B_YES ) {

			locator = buttonsTableLocator + " td[id^='Yes_'] td[id$='_title']";

			if(MyDivId.contains("css=div[class=DwtConfirmDialog]")){
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
		zClickAt(locator,"0,0");

		return (page);
	}


}
