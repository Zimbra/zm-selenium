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
package com.zimbra.qa.selenium.projects.admin.ui;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class DialogForDeleteOperationDomain extends AbsDialog {

	public static class Locators {
		public static final String YES_BUTTON="zdlg__MSG__CTR_DMLV_removeConfirm_button5_title";
		public static final String NO_BUTTON="zdlg__MSG__CTR_DMLV_removeConfirm_button4_title";
	}

	public DialogForDeleteOperationDomain(AbsApplication application, AbsTab page) {
		super(application, page);
	}

	@Override
	public String myPageName() {
		return null;
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Click dialog button " + button);
		if ( button == null )
			throw new HarnessException("button cannot be null");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_YES) {
			locator = Locators.YES_BUTTON;
		} else if (button == Button.B_NO) {
			locator = Locators.NO_BUTTON;
		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		// Make sure the locator was set
		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		// Make sure the locator exists
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator "
					+ locator + " not present!");
		}

		// if (zIsActive())
		// zGetDisplayedText("css=div[class=" + Locators.zDialogContentClassId +
		// "]");

		this.sClickAt(locator,"0,0");
		SleepUtil.sleepLong();

		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		return false;
	}

}
