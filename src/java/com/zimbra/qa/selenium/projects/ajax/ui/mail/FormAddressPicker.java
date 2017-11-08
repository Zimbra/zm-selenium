/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsForm;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

/**
 * The <code>FormAddressPicker<code> object defines an addresspicker dialog
 * <p>
 *
 * @author Matt Rhoades
 * @see http://wiki.zimbra.com/wiki/File:ZimbraSeleniumScreenshotAjaxMail6.JPG
 *
 */
public class FormAddressPicker extends AbsForm {

	public static class Locators {
		public static final String ZmContactPickerLocatorCSS = "css=div[id='ZmContactPicker']";
		public static final String SearchResultArea = "css=div[id$='ContactPicker_chooser'] div[class$='ListView']";
		public static final String ContactPickerFirstContact = "css=div[id='ZmContactPicker'] div[class='DwtChooserListView'] div[id^='zli__DWT']";
		public static final String contactRowsCSS = "css=div[id^='zl__'][id$='__rows'] > div";
	}

	public static class Field {

		public static final Field Search = new Field("Search = new");
		public static final Field EmailAddress = new Field("Search = Email address");
		public static final Field Department = new Field("Search = department");

		private String field;

		private Field(String name) {
			field = name;
		}

		@Override
		public String toString() {
			return (field);
		}

	}

	public FormAddressPicker(AbsApplication application) {
		super(application);

		logger.info("new " + FormAddressPicker.class.getCanonicalName());

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zSubmit() throws HarnessException {
		logger.info("FormMailNew.submit()");

		zToolbarPressButton(Button.B_OK);

		this.zWaitForBusyOverlay();

	}

	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Click button " + button);

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		AbsPage page = null;
		String locator = null;

		if (button == Button.B_OK) {

			locator = "css=div[id='ZmContactPicker_buttons'] td[id^='OK_'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_CANCEL) {

			locator = "css=div[id='ZmContactPicker_buttons'] td[id^='Cancel_'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_TO) {

			locator = "css=div[id='ZmContactPicker'] div[id='DwtChooserButtonDiv_1'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_CC) {

			locator = "css=div[id='ZmContactPicker'] div[id='DwtChooserButtonDiv_2'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_BCC) {

			locator = "css=div[id='ZmContactPicker'] div[id='DwtChooserButtonDiv_3'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_REMOVE) {

			locator = "css=div[id='DwtChooserRemoveButton_1'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_SEARCH) {

			locator = "css=td[id='ZmContactPicker_searchButton'] td[id$='_title']";
			page = null;

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("locator was not present for button " + button);

		this.sClickAt(locator, "");

		SleepUtil.sleepMedium();

		this.zWaitForBusyOverlay();

		return (page);

	}

	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressPulldown(" + pulldown + ", " + option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		//
		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		//

		if (pulldown == Button.B_SHOW_NAMES_FROM) {

			pulldownLocator = "css=td[id='ZmContactPicker_listSelect'] div[class='ImgSelectPullDownArrow']";

			if (option == Button.O_CONTACTS) {

				optionLocator = "css=TODO#TODO";
				page = null;

			} else if (option == Button.O_PERSONAL_AND_SHARED_CONTACTS) {

				optionLocator = "css=TODO#TODO";
				page = null;

			} else if (option == Button.O_GLOBAL_ADDRESS_LIST) {

				optionLocator = "css=TODO#TODO";
				page = null;

			} else {
				throw new HarnessException("unsupported priority option " + option);
			}

		} else {
			throw new HarnessException("no logic defined for pulldown " + pulldown);
		}

		if (pulldownLocator != null) {

			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator "
						+ pulldownLocator + " not present!");
			}

			this.sClick(pulldownLocator);

			this.zWaitForBusyOverlay();

			if (optionLocator != null) {

				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}

				this.sClick(optionLocator);

				this.zWaitForBusyOverlay();

			}

		}

		return (page);
	}

	public void zFillField(Field field, String value) throws HarnessException {

		tracer.trace("Set " + field + " to " + value);

		String locator = null;

		if (field == Field.Search) {

			locator = "css=td>input[id*=ZmContactPicker_searchField]";

		} else if (field == Field.Department) {

			locator = "css=input[id$='_searchDepartmentField']";

		} else if (field == Field.EmailAddress) {

			locator = "css=input[id$='_searchEmailField']";

		} else {
			throw new HarnessException("not implemented for field " + field);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Field is not present field=" + field + " locator=" + locator);

		// Click at the field
		this.sClickAt(locator, "");

		// Type the value
		this.sType(locator, value);

		this.zWaitForBusyOverlay();

	}

	@Override
	public void zFill(IItem item) throws HarnessException {

		throw new HarnessException(
				"No item associated with this dialog - use zFillField(Field.Search, 'value') instead");

	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		// https://bugzilla.zimbra.com/show_bug.cgi?id=62021
		String locator = Locators.ZmContactPickerLocatorCSS;

		if (!this.sIsElementPresent(locator))
			return (false);

		if (!this.zIsVisiblePerPosition(locator, 0, 0))
			return (false);

		// Check the existing contacts are loaded in the dialog
		int count = this.sGetCssCount(Locators.contactRowsCSS);
		if (count < 1) {
			logger.info(myPageName() + " Contacts are not displayed in address picker dialog");
			return (false);
		}

		return (true);
	}
}