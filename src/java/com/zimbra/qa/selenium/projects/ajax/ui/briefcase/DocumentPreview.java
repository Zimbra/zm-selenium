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
package com.zimbra.qa.selenium.projects.ajax.ui.briefcase;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;

public class DocumentPreview extends AbsDisplay {

	public static class Locators {
	}

	public static enum Field {
		Name, Time, Date, Body
	}

	public final String pageTitle = "Zimbra: Briefcase";

	protected DocumentPreview(AbsApplication application) {
		super(application);
		logger.info("new " + DocumentPreview.class.getCanonicalName());
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

	public String zGetDocumentProperty(Field field) throws HarnessException {
		logger.info("DocumentPreview.zGetDocumentProperty(" + field + ")");
		String locator = null;

		if (field == Field.Name) {
			throw new HarnessException("implement me!");

		} else if (field == Field.Body) {

			try {
				this.sSelectFrame("//iframe[contains(@class, 'PreviewFrame')]");
				String bodyLocator = "css=body";

				if (!this.sIsElementPresent(bodyLocator))
					throw new HarnessException("Unable to preview body!");

				String html = this.zGetHtml(bodyLocator);

				logger.info("DocumentPreview GetBody(" + bodyLocator + ") = " + html);
				return (html);

			} finally {
				this.sSelectFrame("relative=top");
			}
		} else if (field == Field.Date) {
			locator = "css=";
			this.sGetText(locator);
			throw new HarnessException("implement me!");
		} else if (field == Field.Time) {
			locator = "css=";
			this.sGetText(locator);
			throw new HarnessException("implement me!");
		}

		return "";
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		zSelectWindow(pageTitle);

		zWaitForElementPresent("css=div[class=ZmPreviewView]");

		return true;
	}
}
