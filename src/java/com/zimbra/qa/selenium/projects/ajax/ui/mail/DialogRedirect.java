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
package com.zimbra.qa.selenium.projects.ajax.ui.mail;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;

public class DialogRedirect extends AbsDialog {

	public static class Locators {

		public static final String RedirectDialogLocator = "css=div#RedirectDialog";

		// Fields
		public static final String FieldEmailLocator = "css=input#RedirectDialog_to_control";

		// Buttons
		public static final String ButtonToButtonLocator = "css=div[id='zb__RedirectDialog__TO'] td[id$='__TO_title]";
		public static final String ButtonOkButtonLocator = "css=div[id='RedirectDialog_buttons'] td[id^='OK'] td[id$='_title']";
		public static final String ButtonCancelButtonLocator = "css=div[id='RedirectDialog_buttons'] td[id^='Cancel'] td[id$='_title']";
	}

	public DialogRedirect(AbsApplication application, AbsTab tab) {
		super(application, tab);

		logger.info("new " + DialogRedirect.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsVisible()");

		String locator = Locators.RedirectDialogLocator;

		if (!this.sIsElementPresent(locator)) {
			return (false);
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false);
		}

		logger.info(myPageName() + " zIsVisible() = true");
		return (true);

	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Click dialog button " + button);

		AbsPage page = null;
		String locator = null;

		if (button == Button.B_OK) {

			locator = Locators.ButtonOkButtonLocator;

			this.sClick(locator);

			this.zWaitForBusyOverlay();

			// Check the message queue
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();

			return (page);

		} else if (button == Button.B_CANCEL) {

			locator = Locators.ButtonCancelButtonLocator;

		} else if (button == Button.B_TO) {

			locator = Locators.ButtonToButtonLocator;

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		this.sClick(locator);

		this.zWaitForBusyOverlay();

		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		logger.info(myPageName() + " zGetDisplayedText(" + locator + ")");

		if (locator == null)
			throw new HarnessException("locator was null");

		return (this.sGetText(locator));
	}

	public static class Field {

		public static final Field To = new Field("To");

		private String field;

		private Field(String name) {
			field = name;
		}

		@Override
		public String toString() {
			return (field);
		}

	}

	public void zFillField(Field field, String email) throws HarnessException {
		tracer.trace("Set " + field + " to " + email);

		String locator = null;

		if (field == Field.To) {

			locator = Locators.FieldEmailLocator;

		} else {
			throw new HarnessException("Unsupported field: " + field);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for field " + field);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Field is not present field=" + field + " locator=" + locator);

		this.sFocus(locator);
		this.sClick(locator);
		this.zWaitForBusyOverlay();

		// Enter text
		this.sType(locator, email);

		this.zWaitForBusyOverlay();
	}
}