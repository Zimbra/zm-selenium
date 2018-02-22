/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.pages.tasks;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsDisplay;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;

public class DisplayTask extends AbsDisplay {

	WebElement we = null;

	public static class Locators {
		public static final String IsActive = "css=[parentid='zv__TKL-main']";
	}

	public static enum Field {
		Subject, Location, StartDate, DueDate, Priority, Status, Percentage, Reminder, Body
	}

	protected DisplayTask(AbsApplication application) {
		super(application);

		logger.info("new " + DisplayTask.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zDisplayPressButton(" + button + ")");

		tracer.trace("Click " + button);

		throw new HarnessException("no logic defined for button: " + button);

	}

	@Override
	public boolean zIsActive() throws HarnessException {
		String locator = Locators.IsActive;
		return (sIsElementPresent(locator));
	}

	public String zGetTaskProperty(Field field) throws HarnessException {
		logger.info(myPageName() + ".zGetTaskProperty(" + field + ")");

		String locator = "css=div[id='zv__TKL-main'] div[class='ZmMailMsgView']";

		if (field == Field.Subject) {

			locator += " div[id$='__su']";

		} else if (field == Field.Location) {

			locator += " tr[id$='__lo'] td[class='LabelColValue']";

		} else if (field == Field.Priority) {

			locator += " tr[id$='__pr'] td[class='LabelColValue']";

		} else if (field == Field.Status) {

			locator += " tr[id$='__st'] td[class='LabelColValue']";

		} else if (field == Field.Percentage) {

			locator += " tr[id$='__pc'] td[class='LabelColValue']";

		} else if (field == Field.StartDate) {

			locator += " tr[id$='__sd'] td[class='LabelColValue']";

		} else if (field == Field.DueDate) {

			locator += " tr[id$='__ed'] td[class='LabelColValue']";

		} else if (field == Field.Reminder) {

			locator += " tr[id$='__al'] td[class='LabelColValue']";

		} else if (field == Field.Body) {

			try {

				String bodyLocator = "body";
				webDriver().switchTo().defaultContent();
				webDriver().switchTo().frame(0);
				we = webDriver().findElement(By.cssSelector(bodyLocator));

				String html = this.sGetHtmlSource();
				logger.info("zGetTaskProperty.zGetBody(" + bodyLocator + ") = " + html);
				return (html);

			} finally {
				this.sSelectFrame("relative=top");
				webDriver().switchTo().defaultContent();
			}

		} else {
			throw new HarnessException("no logic defined for field " + field);
		}

		// Get the subject value
		String value = this.sGetText(locator).trim();

		logger.info(myPageName() + ".zGetTaskProperty(" + field + ") = " + value);
		return (value);

	}

	public String zGetTaskListViewProperty(Field field) throws HarnessException {
		String locator = "css=div[id='zl__TKL-main__rows'] div[id^='zli__TKL'] tr[id^='zlif__TKL']";

		if (field == Field.Subject) {

			locator += " div[id$='__su']";

		} else if (field == Field.Status) {

			locator += " td[id$='__st']";

		} else if (field == Field.Percentage) {

			locator += " td[id$='__pc']";

		} else if (field == Field.DueDate) {

			locator += " td[id$='__dt']";

		} else {
			throw new HarnessException("no logic defined for field " + field);
		}

		// Get the subject value
		String value = this.sGetText(locator).trim();

		logger.info(myPageName() + ".zGetTaskListViewProperty(" + field + ") = " + value);
		return (value);
	}
}