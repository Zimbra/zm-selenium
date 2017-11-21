/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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

import org.apache.log4j.*;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.util.HarnessException;

public class TooltipFolder extends Tooltip {

	protected static Logger logger = LogManager.getLogger(TooltipFolder.class);

	public static class Locators {
	}

	public static class Field {

		public static final Field Foldername = new Field("Foldername");
		public static final Field TotalMessages = new Field("TotalMessages");
		public static final Field UnreadMessages = new Field("UnreadMessages");
		public static final Field Size = new Field("Size");

		private String field;

		private Field(String name) {
			field = name;
		}

		@Override
		public String toString() {
			return (field);
		}

	}

	public TooltipFolder(AbsApplication application) {
		super(application);

		logger.info("new " + this.getClass().getCanonicalName());

	}

	public String zGetField(Field field) throws HarnessException {

		String locator = null;

		if (field == Field.Foldername) {

			// https://bugzilla.zimbra.com/show_bug.cgi?id=78592
			locator = Tooltip.Locators.DwtToolTipCSS + " div#tooltipContents tbody tr:nth-of-type(1)";

		} else if (field == Field.UnreadMessages) {

			// https://bugzilla.zimbra.com/show_bug.cgi?id=78592
			locator = Tooltip.Locators.DwtToolTipCSS + " div#tooltipContents tbody tr:nth-of-type(2) td:nth-of-type(2)";

		} else if (field == Field.TotalMessages) {

			// https://bugzilla.zimbra.com/show_bug.cgi?id=78592
			locator = Tooltip.Locators.DwtToolTipCSS + " div#tooltipContents tbody tr:nth-of-type(3) td:nth-of-type(2)";

		} else if (field == Field.Size) {

			// https://bugzilla.zimbra.com/show_bug.cgi?id=78592
			locator = Tooltip.Locators.DwtToolTipCSS + " div#tooltipContents tbody tr:nth-of-type(4) td:nth-of-type(2)";

		} else {
			throw new HarnessException("implement me: " + field);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Field is not present field=" + field + " locator=" + locator);

		// Get the displayed text
		String value = this.sGetText(locator);

		return (value);
	}
}