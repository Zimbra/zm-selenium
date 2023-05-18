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
package com.zimbra.qa.selenium.framework.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

/**
 * A <code>AbsDialog</code> object represents a "popup dialog", such as a new
 * folder, new tag, error message, etc.
 * <p>
 * 
 * @author Matt Rhoades
 *
 */
public abstract class AbsDialog extends AbsPage {
	protected static Logger logger = LogManager.getLogger(AbsDialog.class);
	protected AbsTab MyTab;

	/**
	 * Create this page object that exists in the specified application
	 * 
	 */
	public AbsDialog(AbsApplication application, AbsTab page) {

		super(application);

		MyTab = page;

		logger.info("new " + AbsDialog.class.getCanonicalName());

	}

	/**
	 * Get the dialog displayed text
	 */
	public abstract String zGetDisplayedText(String locator) throws HarnessException;

	/**
	 * Click on a button in the dialog
	 **/
	public abstract AbsPage zPressButton(Button button) throws HarnessException;

	/**
	 * Wait for this dialog to close
	 * 
	 */
	public void zWaitForClose() throws HarnessException {
		zWaitForClose(PageLoadDelay);
	}

	/**
	 * Wait for this dialog to close
	 * 
	 */
	public void zWaitForClose(long millis) throws HarnessException {

		if (!zIsActive()) {
			return;
		}

		do {
			SleepUtil.sleep(SleepUtil.SleepGranularity);
			millis = millis - SleepUtil.SleepGranularity;
			if (!zIsActive()) {
				return;
			}
		} while (millis > SleepUtil.SleepGranularity);

		SleepUtil.sleep(millis);
		if (!zIsActive()) {
			return;
		}

		throw new HarnessException("Dialog never closed");
	}

	/**
	 * Return the unique name for this page class
	 * 
	 */
	public abstract String myPageName();
}