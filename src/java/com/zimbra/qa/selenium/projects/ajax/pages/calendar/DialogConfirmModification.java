/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.pages.calendar;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning;

public class DialogConfirmModification extends DialogWarning {

	public static final String LocatorDivID = "SEND_UPDATES_DIALOG";

	public static class Locators {
		public static final String SaveAndSendUpdates = "css= div[class='DwtDialog WindowOuterContainer'] label:contains('Save changes and send updates.')";
		public static final String DontSaveAndKeepOpen = "css= div[class='DwtDialog WindowOuterContainer'] label:contains('Don’t save but keep the meeting open.')";
		public static final String DiscardAndClose = "css= div[class='DwtDialog WindowOuterContainer'] label:contains('Discard changes and close.')";
		public static final String Ok_changes = "css=td[id='YesNoCancel_button5_title']";
		public static final String Cancel_changes = "css=td[id='YesNoCancel_button4_title']";
		public static final String Save_modifications = "css=td[id='CHNG_DLG_ORG_1_button2_title']";
		public static final String Cancel_modifications = "css=td[id='CHNG_DLG_ORG_1_button1_title']";
	}

	public DialogConfirmModification(AbsApplication application, AbsTab page) {
		super(new DialogWarningID(LocatorDivID), application, page);

		logger.info("new " + DialogConfirmModification.class.getCanonicalName());
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Click dialog button " + button);
		if (button == null)
			throw new HarnessException("button cannot be null");

		String locator = null;
		AbsPage page = null;
		boolean waitForPostfix = false;

		if (button == Button.B_SAVE_SEND_UPDATES) {

			locator = Locators.SaveAndSendUpdates;
			page = null;

		} else if (button == Button.B_DONTSAVE_KEEP_OPEN) {

			locator = Locators.DontSaveAndKeepOpen;
			page = null;

		} else if (button == Button.B_DISCARD_CLOSE) {

			locator = Locators.DiscardAndClose;
			page = null;

		} else if (button == Button.B_OK) {
			locator = Locators.Ok_changes;
			page = null;

		} else if (button == Button.B_CANCEL) {

			locator = Locators.Cancel_changes;
			page = null;

		} else if (button == Button.B_SAVE_MODIFICATION) {
			locator = Locators.Save_modifications;
			page = null;

		} else if (button == Button.B_CANCEL_MODIFICATION) {

			locator = Locators.Cancel_modifications;
			page = null;

		} else {
			return (super.zPressButton(button));
		}

		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}
		this.sFocus(locator);
		this.sClickAt(locator, "");
		this.zWaitForBusyOverlay();

		if (waitForPostfix) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}

		return (page);
	}
}