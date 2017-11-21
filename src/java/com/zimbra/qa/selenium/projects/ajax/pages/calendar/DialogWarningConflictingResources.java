/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.pages.*;

public class DialogWarningConflictingResources extends DialogWarning {

	public static class Locators {
		public static final String LocatorDivID = "RESC_CONFLICT_DLG";
		public static final String CancelInstanceLink = "css=div[class='ZmResourceConflictDialog'] div[class='ResourceConflictResolver'] span span";
	}

	public DialogWarningConflictingResources(AbsApplication application, AbsTab page) {
		super(new DialogWarningID(Locators.LocatorDivID), application, page);

		logger.info("new " + DialogWarningConflictingResources.class.getCanonicalName());
	}

	public String zGetResourceConflictWarningDialogText() throws HarnessException {
		String text = null;
		SleepUtil.sleepMedium();
		text = this.zGetDisplayedText("css=div[id='RESC_CONFLICT_DLG']");
		return text;
	}

	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");
		String locator = null;

		if (button == Button.B_CANCEL_INSTANCE_LINK) {
			locator = Locators.CancelInstanceLink;
		} else {
			return (super.zPressButton(button));
		}

		sClick(locator);
		this.zWaitForBusyOverlay();

		return null;
	}
}