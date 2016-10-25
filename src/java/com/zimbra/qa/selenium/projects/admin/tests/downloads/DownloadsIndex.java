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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;


public class DownloadsIndex extends AdminCommonTest {

	public DownloadsIndex() {
		logger.info("New "+ DownloadsIndex.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage = app.zPageDownloads;
	}

	public static String[] NetworkOnlyLocators = {

			// These links only appear on NETWORK

			// Zimbra Connector for Microsoft Outlook 32 bit
			"//a[@id='zco_32bit']", 

			// Zimbra Connector for Microsoft Outlook 64 bit
			"//a[@id='zco_64bit']", 

			// Microsoft Outlook PST Import Tools
			"//a[@id='pst_import']",

			// Migration Wizard for Microsoft Exchange
			"//a[@id='exchange_migration']",

			// General Migration Wizard 32 bit
			"//a[@id='general_migration_32bit']",

			// General Migration Wizard 64 bit
			"//a[@id='general_migration_64bit']",		
	};

	public static String[] FossOnlyLocators = {

			// These links only appear on FOSS


			// There are currently no FOSS specific downloads

	};



	@Test( description = "Verify the Downloads Index opens",
			groups = { "functional" })
	public void DownloadsIndex_01() throws HarnessException {	

		String windowTitle = "Zimbra Collaboration Suite :: Downloads";	
		ZAssert.assertTrue(app.zPageDownloads.zOpenIndexHTML(windowTitle), "Download Page is not opened successfully");		
	}

	@Test( description = "Verify the Downloads Tab contains the correct FOSS vs NETWORK links",
			groups = { "functional"  })
	public void DownloadsIndex_02() throws HarnessException {		
		String windowTitle = "Zimbra Collaboration Suite :: Downloads";		

		ZAssert.assertTrue(app.zPageDownloads.zOpenIndexHTML(windowTitle), "Download Page is not opened successfully");	
		
		// If NETWORK, make sure NETWORK-only links appear and FOSS-only links do not appear
		// If FOSS, make sure FOSS-only links appear and NETWORK-only links do not appear
		if ( ConfigProperties.zimbraGetVersionString().contains("NETWORK") ) {

			for ( String locator : NetworkOnlyLocators ) {
				ZAssert.assertTrue(app.zPageDownloads.sIsElementPresent(locator), "Verify the network-only locator exists: "+ locator);
			}

			for ( String locator : FossOnlyLocators ) {
				ZAssert.assertFalse(app.zPageDownloads.sIsElementPresent(locator), "Verify the foss-only locator does not exists: "+ locator);
			}

		} else if ( ConfigProperties.zimbraGetVersionString().contains("FOSS") ) {

			for ( String locator : NetworkOnlyLocators ) {
				ZAssert.assertFalse(app.zPageDownloads.sIsElementPresent(locator), "Verify the network-only locator does not exists: "+ locator);
			}

			for ( String locator : FossOnlyLocators ) {
				ZAssert.assertTrue(app.zPageDownloads.sIsElementPresent(locator), "Verify the foss-only locator exists: "+ locator);
			}				

		} else {
			throw new HarnessException("Unable to find NETWORK or FOSS in version string: "+ ConfigProperties.zimbraGetVersionString());
		}

	}

	@Test( description = "Verify the downloads links return 200 rather than 404",
			groups = { "functional"  })
	public void DownloadsIndex_03() throws HarnessException {

		String windowTitle = "Zimbra Collaboration Suite :: Downloads";		

		ZAssert.assertTrue(app.zPageDownloads.zOpenIndexHTML(windowTitle), "Download Page is not opened successfully");		



		// Determine which links should be present
		List<String> locators = new ArrayList<String>();

		if ( ConfigProperties.zimbraGetVersionString().contains("NETWORK") ) {

			locators.addAll(Arrays.asList(NetworkOnlyLocators));			

		} else if ( ConfigProperties.zimbraGetVersionString().contains("FOSS") ) {

			locators.addAll(Arrays.asList(FossOnlyLocators));			

		} else {
			throw new HarnessException("Unable to find NETWORK or FOSS in version string: "+ ConfigProperties.zimbraGetVersionString());
		}

		for (String locator : locators ) {
			String href = app.zPageDownloads.sGetAttribute(locator +"@href");
			String page = ConfigProperties.getBaseURL() + href;

			HttpURLConnection  connection = null;
			try {

				URL url = new URL(page);
				int authResponse = app.zPageDownloads.getAuthResponse(url);

				// 200 and 400 are acceptable
				ZAssert.assertStringContains("200 400", ""+authResponse, "Verify the download URL is valid: "+ url.toString());

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

	@AfterMethod(groups={"always"})
	public void afterMethod() throws HarnessException {
		logger.info("Opening the base URL and loggin out for the next test to execute");
		app.zPageMain.sOpen(ConfigProperties.getBaseURL());
		app.zPageMain.logout();
	}

}
