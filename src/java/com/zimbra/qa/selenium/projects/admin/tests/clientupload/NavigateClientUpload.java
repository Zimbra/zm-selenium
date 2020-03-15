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
package com.zimbra.qa.selenium.projects.admin.tests.clientupload;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.PageManageClientUpload;

public class NavigateClientUpload extends AdminCore {

	public NavigateClientUpload() {
		logger.info("New "+ NavigateClientUpload.class.getCanonicalName());
		super.startingPage = app.zPageManageClientUpload;
	}


	@Test (description = "Navigate to Client Upload",
			groups = { "smoke", "non-zimbrax" })

	public void NavigateClientUpload_01() throws HarnessException {
		ZAssert.assertTrue(app.zPageManageClientUpload.zVerifyHeader(PageManageClientUpload.Locators.HOME),
				"Verfiy the 'Home' text exists in navigation path");
		ZAssert.assertTrue(
				app.zPageManageClientUpload.zVerifyHeader(PageManageClientUpload.Locators.TOOLS_AND_MIGRATION),
				"Verfiy the 'Tools and Migration' text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageClientUpload.zVerifyHeader(PageManageClientUpload.Locators.CLIENT_UPLOAD),
				"Verfiy the 'Client Upload' text exists in navigation path");
	}
}