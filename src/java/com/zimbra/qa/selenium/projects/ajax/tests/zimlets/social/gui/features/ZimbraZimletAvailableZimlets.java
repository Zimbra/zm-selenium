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
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.social.gui.features;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class ZimbraZimletAvailableZimlets extends AjaxCommonTest {

	@SuppressWarnings("serial")
	public ZimbraZimletAvailableZimlets() {
		logger.info("New "+ ZimbraZimletAvailableZimlets.class.getCanonicalName());

		super.startingPage = app.zPageSocial;
		super.startingAccountPreferences = new HashMap<String, String>() {{
			put("zimbraFeatureMailEnabled", "FALSE");
			put("zimbraFeatureContactsEnabled", "FALSE");
			put("zimbraFeatureCalendarEnabled", "FALSE");
			put("zimbraFeatureTasksEnabled", "FALSE");
			put("zimbraFeatureBriefcasesEnabled", "FALSE");
			put("zimbraZimletAvailableZimlets", "+com_zimbra_social");
		}};
	}


	@Bugs (ids = "50123")
	@Test (description = "Load the client with just Social enabled",
			groups = { "deprecated" })

	public void ZimbraZimletAvailableZimlets_01() throws HarnessException {
		ZAssert.assertTrue(app.zPageSocial.zIsActive(), "Verify the social page is active");
	}
}