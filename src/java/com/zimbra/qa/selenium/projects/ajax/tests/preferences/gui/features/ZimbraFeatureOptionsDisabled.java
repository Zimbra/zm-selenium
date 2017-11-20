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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.gui.features;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogError;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogError.DialogErrorID;

public class ZimbraFeatureOptionsDisabled extends AjaxCore {

	@SuppressWarnings("serial")
	public ZimbraFeatureOptionsDisabled() {
		logger.info("New "+ ZimbraFeatureOptionsDisabled.class.getCanonicalName());

		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {{
			put("zimbraFeatureOptionsEnabled", "FALSE");
		}};
	}


	@Bugs (ids = "63652")
	@Test(	description = "Load the app with Preferences tab disabled",
			groups = { "functional-skip", "L3-skip" })

	public void ZimbraFeatureOptionsDisabled_01() throws HarnessException {

		// Verify that the main app is loaded
		ZAssert.assertTrue(app.zPageMain.zIsActive(), "Verify that the main app is loaded");
		ZAssert.assertTrue(app.zPageMail.zIsActive(), "Verify that the mail app is loaded");

		// Verify bug 63652
		DialogError dialog = app.zPageMain.zGetErrorDialog(DialogErrorID.Zimbra);
		ZAssert.assertFalse(dialog.zIsActive(), "Verify that the Permission Denied error dialog is not present");
	}
}