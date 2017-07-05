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
package com.zimbra.qa.selenium.projects.universal.tests.main;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.*;
import com.zimbra.qa.selenium.projects.universal.ui.*;

public class ProductHelp extends UniversalCommonTest {

	public ProductHelp() {
		logger.info("New " + ProductHelp.class.getCanonicalName());
		super.startingPage = app.zPageMail;
	}

	
	@Test( description = "Open 'Product Help'",
			groups = { "smoke", "L0" })
	
	public void ProductHelp_01() throws HarnessException {

		SeparateWindow window = null;
		String windowTitle = "ZWC Help";

		try {

			// Click the Account -> Product Help
			window = (SeparateWindow)app.zPageMain.zToolbarPressPulldown(Button.B_ACCOUNT, Button.O_PRODUCT_HELP);

			SleepUtil.sleepLong();

			for (String title : app.zPageMain.sGetAllWindowTitles()) {
				logger.info("Window title: " + title);
				if (title.contains("Zimbra: Inbox")) {
					logger.info("Already existing Name: " + title);

				} else if (title.contains(windowTitle)) {
					logger.info("zIsActive() = true ... title = " + title);
					break;
					
				} else {
					throw new HarnessException("Window never became active!");
				}
			}
			app.zPageMain.sSelectWindow(windowTitle);
			app.zPageMain.zSeparateWindowClose(windowTitle);
			app.zPageMain.sSelectWindow(null);

		} finally {

			app.zPageMain.zSeparateWindowClose(windowTitle);
			app.zPageMain.sSelectWindow(null);
			window.sSelectWindow(null);

		}
	}
}