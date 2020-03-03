/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.downloads;

import java.io.IOException;
import java.net.*;
import java.util.*;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.PageMain;

public class DownloadsTab extends AdminCore {

	public DownloadsTab() {
		logger.info("New "+ DownloadsTab.class.getCanonicalName());
		super.startingPage = app.zPageDownloads;
	}

	public static String[] downloadLinkLocators = {
		"//a[contains(text(),'ZCS Migration Wizard for Exchange/PST (32bit)')]",
		"//a[contains(text(),'ZCS Migration Wizard for Exchange/PST (64bit)')]",
		"//a[contains(text(),'ZCS Migration Wizard for Domino')]",
		"//a[contains(text(),'Legacy PST Import Wizard')]",
	};

	public static String[] networkDownloadLinkLocators = {
		"//a[contains(text(),'Zimbra Connector for Outlook MSI Customizer')]",
		"//a[contains(text(),'Zimbra Connector for Outlook Branding MSI')]",
		"//a[contains(text(),'Zimbra Connector for Outlook (32bit)')]",
		"//a[contains(text(),'Zimbra Connector for Outlook (64bit)')]",
		"//a[contains(text(),'Legacy ZCS Migration Wizard for Exchange')]",
	};

	@Test (description = "Verify the Downloads Tab contains the correct download links",
			groups = { "smoke" })

	public void DownloadsTab_01() throws HarnessException {

		// Make sure common links are present
		for ( String locator : downloadLinkLocators ) {
			ZAssert.assertTrue(app.zPageDownloads.sIsElementPresent(locator), "Verify the common locator exists: " + locator);
		}

		if ( ConfigProperties.zimbraGetVersionString().contains("NETWORK") ) {
			for ( String locator : networkDownloadLinkLocators ) {
				ZAssert.assertTrue(app.zPageDownloads.sIsElementPresent(locator), "Verify the network-only locator exists: "+ locator);
			}

		} else if ( ConfigProperties.zimbraGetVersionString().contains("FOSS") ) {
			for ( String locator : networkDownloadLinkLocators ) {
				ZAssert.assertFalse(app.zPageDownloads.sIsElementPresent(locator), "Verify the network-only locator does not exists: "+ locator);
			}

		} else {
			throw new HarnessException("Unable to find NETWORK or FOSS in version string: "+ ConfigProperties.zimbraGetVersionString());
		}
	}


	@Bugs (ids = "100755")
	@Test (description = "Verify the downloads links are accessible",
			groups = { "smoke" })

	public void DownloadsTab_02() throws HarnessException {

		// Determine which links should be present
		List<String> locators = new ArrayList<String>();
		locators.addAll(Arrays.asList(downloadLinkLocators));

		for (String locator : locators ) {
			String href = app.zPageDownloads.sGetAttribute("xpath=" + locator + "@href");

			HttpURLConnection  connection = null;
			try {

				URL url = new URL(href);
				int authResponse = app.zPageDownloads.zGetAuthResponse(url);

		        // 200 and 400 are acceptable
		        ZAssert.assertStringContains("200 400", ""+authResponse, "Verify the download URL is valid: " + url.toString());

		  } catch (MalformedURLException e) {
				throw new HarnessException(e);
			} catch (IOException e) {
				throw new HarnessException(e);
			} finally {
				if ( connection != null ) {
					connection.disconnect();
					connection = null;
				}
			}
		}
	}


	@AfterMethod(groups = { "always" })
	public void afterMethod() throws HarnessException {
		logger.info("Opening base URL...");
		app.zPageMain.sOpen(ConfigProperties.getBaseURL());
		app.zPageMain.zWaitTillElementPresent(PageMain.Locators.zHelpButton);
	}
}