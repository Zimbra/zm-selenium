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

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;


public class DownloadsTab extends AdminCommonTest {

	public static String[] NetworkOnlyLocators = {
		
		// These links only appear on NETWORK

		// Zimbra Connector for Outlook MSI Customizer
		"//a[contains(text(),'Zimbra Connector for Outlook MSI Customizer')]", // TODO: need to add unique ID
		
		// Zimbra Connector for Outlook Branding MSI
		"//a[contains(text(),'Zimbra Connector for Outlook Branding MSI')]", // TODO: need to add unique ID
		
		// Zimbra Connector(32bits) for Outlook
		"//a[contains(text(),'Zimbra Connector for Outlook (32bit)')]", // TODO: need to add unique ID
		
		// Zimbra Connector(64bits) for Outlook
		"//a[contains(text(),'Zimbra Connector for Outlook (64bit)')]", // TODO: need to add unique ID
		
		// (User Instructions )
		//"//a[contains(text(),'User Instructions')]", // TODO: need to add unique ID

		// Zimbra Connector for Apple iSync
		"//a[contains(text(),'Legacy ZCS Migration Wizard for Exchange')]", // TODO: need to add unique ID
		
	};
	
	public static String[] FossOnlyLocators = {
		
		// These links only appear on FOSS

		
		// There are currently no FOSS specific downloads
		
	};

	public static String[] CommonLocators = {
		
		// These links appear on both NETWORK and FOSS
		
		// ZCS Migration Wizard for Exchange
		"//a[contains(text(),'ZCS Migration Wizard for Exchange/PST (32bit)')]", // TODO: need to add unique ID
		
		// ZCS Migration Wizard for Exchange
		"//a[contains(text(),'ZCS Migration Wizard for Exchange/PST (64bit)')]", // TODO: need to add unique ID


		// ZCS Migration Wizard for Domino
		"//a[contains(text(),'ZCS Migration Wizard for Domino')]", // TODO: need to add unique ID

		// PST Import Wizard
		"//a[contains(text(),'Legacy PST Import Wizard')]", // TODO: need to add unique ID

		// PST Import Wizard (User Instructions)
		//"//a[contains(text(),'User Instructions')]", // TODO: need to add unique ID

	};

	
	
	public DownloadsTab() {
		logger.info("New "+ DownloadsTab.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage = app.zPageDownloads;
	}



	@Test( description = "Verify the Downloads Tab contains the correct FOSS vs NETWORK links",
			groups = { "functional", "L2" })
	public void DownloadsTab_01() throws HarnessException {
		
		// Make sure common links are present
		for ( String locator : CommonLocators ) {
			ZAssert.assertTrue(app.zPageDownloads.sIsElementPresent(locator), "Verify the common locator exists: "+ locator);
		}
		
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
	
	@Bugs( ids = "100755")
	@Test( description = "Verify the downloads links return 200 rather than 404",
			groups = { "functional", "L2" })
	public void DownloadsTab_02() throws HarnessException {

		// Determine which links should be present
		List<String> locators = new ArrayList<String>();
		
		if ( ConfigProperties.zimbraGetVersionString().contains("NETWORK") ) {
			
			locators.addAll(Arrays.asList(NetworkOnlyLocators));
			locators.addAll(Arrays.asList(CommonLocators));
			
		} else if ( ConfigProperties.zimbraGetVersionString().contains("FOSS") ) {
			
			locators.addAll(Arrays.asList(FossOnlyLocators));
			locators.addAll(Arrays.asList(CommonLocators));

		} else {
			throw new HarnessException("Unable to find NETWORK or FOSS in version string: "+ ConfigProperties.zimbraGetVersionString());
		}

		for (String locator : locators ) {
			String href = app.zPageDownloads.sGetAttribute("xpath="+ locator +"@href");
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



}
