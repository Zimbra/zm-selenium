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
package com.zimbra.qa.selenium.projects.admin.tests.messagevolume;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.PageManageMessageVolume;

public class NavigateMessageVolume extends AdminCore {

	public NavigateMessageVolume() {
		logger.info("New "+ NavigateMessageVolume.class.getCanonicalName());
		super.startingPage = app.zPageManageMessageVolume;
	}


	@Test (description = "Navigate to Message Volume",
			groups = { "smoke", "testcafe" })

	public void NavigateMessageVolume_01() throws HarnessException {
		ZAssert.assertTrue(app.zPageManageMessageVolume.zVerifyHeader(PageManageMessageVolume.Locators.HOME),
				"Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageMessageVolume.zVerifyHeader(PageManageMessageVolume.Locators.MONITOR),
				"Verfiy the 'Monitor' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageMessageVolume.zVerifyHeader(PageManageMessageVolume.Locators.MESSAGE_VOLUME),
				"Verfiy the 'Message Volume' text exists in navigation path");
	}
}