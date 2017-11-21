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
package com.zimbra.qa.selenium.projects.mobile.tests.main;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.mobile.core.MobileCore;
import com.zimbra.qa.selenium.projects.mobile.pages.PageMain;


public class MainScreen extends MobileCore {
	
	public MainScreen() {
		logger.info("New "+ MainScreen.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageMain;
		super.startingAccount = null;
		
	}
	
	@Test (description = "Verify basic elements on the Main Screen",
			groups = { "sanity" })
	public void MainScreen_01() throws HarnessException {
				
		ZAssert.assertTrue(app.zPageMain.sIsElementPresent(PageMain.Locators.zAppbarMail),		"Verify that the appbar Mail icon is present");
		ZAssert.assertTrue(app.zPageMain.sIsElementPresent(PageMain.Locators.zAppbarContact),	"Verify that the appbar Contact icon is present");
		ZAssert.assertTrue(app.zPageMain.sIsElementPresent(PageMain.Locators.zAppbarCal),		"Verify that the appbar Cal icon is present");
		ZAssert.assertTrue(app.zPageMain.sIsElementPresent(PageMain.Locators.zAppbarDocs),		"Verify that the appbar Docs icon is present");
		ZAssert.assertTrue(app.zPageMain.sIsElementPresent(PageMain.Locators.zAppbarSearch),	"Verify that the appbar Search icon is present");
		
		ZAssert.assertTrue(app.zPageMain.sIsElementPresent(PageMain.Locators.zBtnCompose),		"Verify that the New Compose link is present");

		ZAssert.assertTrue(app.zPageMain.sIsElementPresent(PageMain.Locators.zPreferences),	"Verify that the Preferences link is present");

	}

	@Test (description = "Verify Copyright on the Main Screen",
			groups = { "smoke" })
	public void MainScreen_02() throws HarnessException {
				
		// The copyright doesn't seem to be translated
		//use "\u00a9" (char)169 for Copyright
		
		String copyright = "Copyright " + "\u00a9" + " 2005-2014 Telligent Systems, Inc. " + (char)34 + "Telligent" + (char)34 + " and " + (char)34 + "Zimbra" + (char)34 + " are registered trademarks or trademarks of Telligent Systems, Inc.";
		
				
		ZAssert.assertTrue(app.zPageMain.sIsElementPresent(PageMain.Locators.zMainCopyright),	"Verify that the copyright notice is present");
		
		String text = app.zPageMain.sGetText(PageMain.Locators.zMainCopyright);
		ZAssert.assertEquals(text, copyright, "Verify the copyright text is correct");
			
		Calendar calendar = new GregorianCalendar();
		String thisYear = "" + calendar.get(Calendar.YEAR);

		ZAssert.assertStringContains(text, thisYear, "Verify the copyright text is correct");

	}


}
