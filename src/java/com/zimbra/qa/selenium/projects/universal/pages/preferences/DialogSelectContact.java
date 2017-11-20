package com.zimbra.qa.selenium.projects.universal.pages.preferences;

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
public class DialogSelectContact extends AbsDialog {

	public static class Locators {
		public static final String zDialogClass = "DwtDialog";
		public static final String zDialogTitleClass = "css=td[class=DwtDialogTitle]";
		public static final String zDialogButtonsClass = "DwtDialogButtonBar";
		public static final String zDialogContactSearchInput = "ZmContactSearch";
		public static final String zDialogContactSearchButton = "searchButton";

		public static final String zListIcon = "css=td[class='ListViewIcon']";
	}

	public DialogSelectContact(AbsApplication application, AbsTab tab) {
		super(application, tab);
		logger.info("new " + DialogSelectContact.class.getCanonicalName());
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
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");
		tracer.trace("Click dialog button " + button);

		String locator = null;

		if (button == Button.B_OK) {
			locator = "css=div[class='" + Locators.zDialogClass + "'] "
					+ "div[class='" + Locators.zDialogButtonsClass
					+ "'] td[class=ZWidgetTitle]:contains(OK)";
		} else if (button == Button.B_CANCEL) {
			locator = "css=div[class='" + Locators.zDialogClass + "'] "
					+ "div[class='" + Locators.zDialogButtonsClass
					+ "'] td[class=ZWidgetTitle]:contains(Cancel)";
		} else if (button == Button.B_SEARCH) {
			locator = "css=div[class=" + Locators.zDialogContactSearchInput
					+ "] td[id$=" + Locators.zDialogContactSearchButton
					+ "] td[id$='title']:contains('Search')";
		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		// Default behavior, click the locator

		// Make sure the locator was set

		// Make sure the locator exists
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator "
					+ locator + " not present!");
		}

		// if (zIsActive())
		// zGetDisplayedText("css=div[class=" + Locators.zDialogContentClassId +
		// "]");

		this.sClickAt(locator, "0,0");

		return (null);
	}

	/**
	 * Enter text into the search contact input
	 * 
	 * @param notes
	 */
	// @SuppressWarnings("unused")
	public void zEnterContacts(String contact) throws HarnessException {

		if (contact == null)
			throw new HarnessException("contact must not be null");

		String locator = "css=div[class=" + Locators.zDialogContactSearchInput
				+ "] input[id$='searchField']";

		if (!this.zWaitForElementPresent(locator, "10000"))
			throw new HarnessException("unable to find body field " + locator);

		this.sFocus(locator);
		this.sClickAt(locator, "0,0");
		this.sType(locator, contact);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		// TODO Auto-generated method stub
		return null;
	}

}
