/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.navigation;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;

public class HelpLinks extends AdminCore {

	public HelpLinks() {
		logger.info("New "+ HelpLinks.class.getCanonicalName());
		super.startingPage = app.zPageMain;
	}


	@Test (description = "Verify Help Center links",
			groups = { "smoke" })

	public void HelpLinks_01() throws HarnessException {
		// Navigate to Help > Help links
		app.zPageManageHelp.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_HELP_CENTRAL_ONLINE);
		SleepUtil.sleepMedium();

		// Get help page content
		String helpContent = app.zPageManageHelp.sGetHtmlBody();

		// Verify help and doc links
		ZAssert.assertStringContains(helpContent, "/zimbraAdmin/help/admin/html/administration_console_help.htm?locid=en_US",
				"Verify admin console help link is present");

		ZAssert.assertStringContains(helpContent, "https://www.zimbra.com/support/documentation/zcs-ne-documentation.html",
				"Verify zimbra documentatione html help link is present");

		ZAssert.assertStringContains(helpContent, "/zimbraAdmin/adminhelp/pdf/admin.pdf?locid=en_US",
				"Verify administrator guide pdf link is present");

		ZAssert.assertStringContains(helpContent, "/zimbraAdmin/adminhelp/pdf/ZCS%20Connector%20for%20Outlook.pdf?locid=en_US",
				"Verify administrator guide ZCS connector outlook pdf link is present");

		ZAssert.assertStringContains(helpContent, "/zimbraAdmin/help/admin/pdf/zimbra_user_guide.pdf?locid=en_US",
				"Verify end users guide pdf link is present");

		ZAssert.assertStringContains(helpContent, "/zimbraAdmin/adminhelp/pdf/User%20Instructions%20Connector%20for%20Outlook.pdf?locid=en_US",
				"Verify end user guide ZCS connector outlook pdf link is present");

		ZAssert.assertStringContains(helpContent, "https://www.zimbra.com/forums",
				"Verify forums link is present");

		ZAssert.assertStringContains(helpContent, "https://wiki.zimbra.com",
				"Verify wiki link is present");

		app.zPageMain.zLogout();
	}
}