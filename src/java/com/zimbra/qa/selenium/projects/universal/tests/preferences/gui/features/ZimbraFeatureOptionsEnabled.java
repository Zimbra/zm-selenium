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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.gui.features;

import java.util.HashMap;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCore;
import com.zimbra.qa.selenium.projects.universal.pages.preferences.TreePreferences.TreeItem;

public class ZimbraFeatureOptionsEnabled extends UniversalCore {

	@SuppressWarnings("serial")
	public ZimbraFeatureOptionsEnabled() {
		logger.info("New "+ ZimbraFeatureOptionsEnabled.class.getCanonicalName());

		// All tests start at the login page
		super.startingPage = app.zPagePreferences;

		super.startingAccountPreferences = new HashMap<String, String>() {
			{

				// Only options/Preferences is enabled
				put("zimbraFeatureOptionsEnabled", "TRUE");
				put("zimbraFeatureTasksEnabled", "FALSE");
				put("zimbraFeatureMailEnabled", "FALSE");
				put("zimbraFeatureContactsEnabled", "FALSE");
				put("zimbraFeatureCalendarEnabled", "FALSE");
				put("zimbraFeatureBriefcasesEnabled", "FALSE");
				put("zimbraZimletAvailableZimlets", "-com_zimbra_social");
				put("zimbraPrefAutocompleteAddressBubblesEnabled", "TRUE");

			}
		};

	}
	
	/**
	 * See http://bugzilla.zimbra.com/show_bug.cgi?id=62011 - WONTFIX
	 * @throws HarnessException
	 */
	@Bugs (ids = "62011")	
	@Test (description = "Load the Preferences tab with just Preferences enabled", groups = { "deprecated", "L4" })
	public void ZimbraFeatureOptionsEnabled_01() throws HarnessException {
		
		// Go to "General"
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.General);

		// Determine the status of the checkbox
		boolean checked = app.zPagePreferences
				.zGetCheckboxStatus("zimbraPrefAutocompleteAddressBubblesEnabled");

		// Since zimbraPrefAutocompleteAddressBubblesEnabled is set to TRUE, the
		// Bubble checkbox should be checked
		ZAssert.assertTrue(checked, "Verify if Address Bubbles check box is checked");

	}
}