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
package com.zimbra.qa.selenium.projects.ajax.pages;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsForm;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;

/**
 * The <code>FormRecoverDeletedItems<code> object defines an "Recover Deleted
 * Items" dialog
 * <p>
 *
 * @author Matt Rhoades
 * @see http://wiki.zimbra.com/wiki/File:ZimbraSeleniumScreenshotAjaxMail7.JPG
 *
 */
public class FormRecoverDeletedItems extends AbsForm {

	public static class Locators {

	}

	public static class Field {

		public static final Field Search = new Field("Search = new");

		private String field;

		private Field(String name) {
			field = name;
		}

		@Override
		public String toString() {
			return (field);
		}

	}

	public FormRecoverDeletedItems(AbsApplication application) {
		super(application);

		logger.info("new " + FormRecoverDeletedItems.class.getCanonicalName());

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

		if (button == Button.B_CLOSE) {

			locator = "css=div[class='ZmDumpsterDialog'] td[id$='_button1_title']";
			page = null;

		} else if (button == Button.B_SEARCH) {

			locator = "css=td#searchDumpsterButton_title";
			page = null;

		} else if (button == Button.B_RECOVER_TO) {

			locator = "css=td#zb__dumpsterMail__MOVE_left_icon";
			page = new DialogMove(this.MyApplication, ((AjaxPages) this.MyApplication).zPageMail);

		} else if (button == Button.B_DELETE) {

			locator = "css=td#zb__dumpsterMail__DELETE_title";
			page = null;

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("locator was not present for button " + button);

		this.sClick(locator);

		this.zWaitForBusyOverlay();

		if (page != null) {

			page.zWaitForActive();

		}

		return (page);

	}

	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressPulldown(" + pulldown + ", " + option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		throw new HarnessException("Dumpster recovery dialog does not have this functionality");
	}

	@Override
	public void zFill(IItem item) throws HarnessException {

		throw new HarnessException(
				"No item associated with this dialog - use zFillField(Field.Search, 'value') instead");

	}

	public void zFillField(Field field, String value) throws HarnessException {

		tracer.trace("Set " + field + " to " + value);

		String locator = null;

		if (field == Field.Search) {

			locator = "css=td[class='DumpsterSearchInput'] input";

		} else {
			throw new HarnessException("not implemented for field " + field);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Field is not present field=" + field + " locator=" + locator);

		this.sFocus(locator);
		this.sClick(locator);
		this.zWaitForBusyOverlay();

		// Enter text
		this.sType(locator, value);
		this.zWaitForBusyOverlay();
	}

	public AbsPage zListItem(Action action, String subject) throws HarnessException {
		logger.info(myPageName() + " zListItem(" + action + ", " + subject + ")");

		tracer.trace(action + " on subject = " + subject);

		AbsPage page = null;
		String listLocator = "css=div[id=zl__dumpsterMail__rows]";
		String rowLocator = listLocator + " div[id^=zli__dumpsterMail__]";
		String itemLocator = null;

		if (!this.sIsElementPresent(rowLocator))
			throw new HarnessException("List View Rows is not present " + rowLocator);

		int count = this.sGetCssCount(rowLocator);
		logger.debug(myPageName() + " zListSelectItem: number of list items: " + count);

		for (int i = 1; i <= count; i++) {

			String s = "";

			itemLocator = rowLocator + ":nth-child(" + i + ") td[id*='__su']";
			if (sIsElementPresent(itemLocator)) {
				s = this.sGetText(itemLocator).trim();
			}

			if (s.contains(subject)) {
				break;
			}

			itemLocator = null;
		}

		if (itemLocator == null) {
			throw new HarnessException("subject locator is not present " + itemLocator);
		}

		if (action == Action.A_LEFTCLICK) {

			this.sClick(itemLocator);

			this.zWaitForBusyOverlay();

			page = null;

		} else {
			throw new HarnessException("implement me!  action = " + action);
		}

		return (page);

	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = "css=div[class='ZmDumpsterDialog']";

		if (!this.sIsElementPresent(locator))
			return (false);

		if (!this.zIsVisiblePerPosition(locator, 0, 0))
			return (false);

		return (true);

	}
}