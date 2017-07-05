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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.gui;

import java.util.HashMap;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.*;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;


public class NavigateAway extends AjaxCommonTest {

	public NavigateAway() {
		
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = new HashMap<String , String>() {
			private static final long serialVersionUID = 8043572657728539313L;
		{
		    put("zimbraFeatureMobileSyncEnabled", "TRUE");
		}};

	}

	/**
	 * Navigate to NavigateAwayDestinationTab, check for "Save Changes?" dialog
	 * @throws HarnessException
	 */
	protected boolean navigateAwayCheckWarning() throws HarnessException {
		logger.info("navigateAwayCheckWarning: start");
		
		boolean isVisible = true;
		
		// Navigate to mail
		//
		// This may cause problems if mail is not enabled for this account, but
		// let's assume that configuration is very rare.
		//
		app.zPagePreferences.zClick(PageMain.Locators.zMailApp);

		app.zPagePreferences.zWaitForBusyOverlay();
		
		// Check for the warning dialog
		DialogWarning warning = app.zPageMain.zGetWarningDialog(DialogWarning.DialogWarningID.PreferencesSaveChanges);
		ZAssert.assertNotNull(warning, "Verify the warning dialog object is created");
		isVisible = warning.zIsActive();
		
		if ( isVisible ) {
			
			// If the warning dialog is visible, discard
			// changes and throw exception
			warning.zClickButton(Button.B_NO);
		}
		
		logger.info("navigateAwayCheckWarning: finish");

		return (isVisible);
		
	}

	/**
	 * A table of preferences tree locators
	 * @return
	 */
	@DataProvider(name = "DataProviderPreferencePageToLocator")
	public Object[][] DataProviderPreferencePageToLocator() {
		return new Object[][] {
				new Object[] { TreeItem.General,				"css=td[id='CHANGE_PASSWORD_title']" },
				new Object[] { TreeItem.Mail,					null },
				new Object[] { TreeItem.MailAccounts,			null },
				new Object[] { TreeItem.MailFilters,			null },
				new Object[] { TreeItem.MailSignatures,			null },
				new Object[] { TreeItem.MailOutOfOffice,		null },
				new Object[] { TreeItem.MailTrustedAddresses,	null },
				new Object[] { TreeItem.AddressBook,			null },
				new Object[] { TreeItem.Calendar,				null },
				new Object[] { TreeItem.Sharing,				null },
				new Object[] { TreeItem.MobileDevices,			"css=td#zb__MD__MOBILE_REMOVE_title" },
				new Object[] { TreeItem.Notifications,			null },
				new Object[] { TreeItem.ImportExport,			null },
				new Object[] { TreeItem.Shortcuts,				null },
				
				// Bug: 71389 ... remove quick commands
				// new Object[] { TreeItem.QuickCommands,			null },
				
				new Object[] { TreeItem.Zimlets,				null },
		};
	}


	@Bugs(ids="98364,101596")
	@Test(
			description = "If no changes made, verify that navigating away from preferences pages do not prompt 'Save Changes?'",
			groups = { "functional", "L3" },
			dataProvider = "DataProviderPreferencePageToLocator")
	public void NavigateAway_01(TreeItem treeItemLocator, String verificationLocator) throws HarnessException {

		if ( 
				(treeItemLocator == TreeItem.MobileDevices)
				&&
				ConfigProperties.zimbraGetVersionString().contains("FOSS")
			) {
			
			// Mobile access is a NETWORK only feature
			return;
		}
		
		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, treeItemLocator);

		
		if ( verificationLocator == null ) {
			
			// No locator defined.  Just sleep a bit
			SleepUtil.sleep(1000);
			
		} else {
			
			// Locator specified.  Make sure it is present and visible
			ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(verificationLocator), "Verify the page is present");
			ZAssert.assertTrue(app.zPagePreferences.zIsVisiblePerPosition(verificationLocator, 0, 0), "Verify the page is visible");
			
		}
		
		// There seems to be a bug that once the preferences are dirty, the client doesn't
		// clean it up.  So, all subsequent pages checks also fail.  Throw a harness
		// exception so that Account is cleaned up and started new.
		//
		// Original Code that should work:
		// ZAssert.assertFalse(navigateAwayCheckWarning(), "Verify the Save Changes? dialog was not visible");

		
		// Navigate to "mail" and check for the "Save Changes?" dialog
		boolean isVisible = navigateAwayCheckWarning();
		if ( isVisible ) {
			throw new HarnessException("Dialog 'Save Changes?' was present!");
		}
		
	}

	@Bugs(ids = "103549")
	@Test( description = "Set calendar custom work week and navigate away without saving", 
			groups = { "functional", "L2" })
	
	public void NavigateAway_02() throws HarnessException {

		// Navigate to preferences -> calendar
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Calendar);
		SleepUtil.sleepMedium();

		// Select custom work hours for e.g. Tuesday to Friday
		app.zPagePreferences.zCheckboxSet(Button.C_MONDAY_WORK_WEEK, false);
		boolean isVisible = navigateAwayCheckWarning();
		if ( !isVisible ) {
			throw new HarnessException("Dialog 'Save Changes?' was present!");
		}
	}

	@Bugs(ids = "103549")
	@Test( description = "Change default appointment duration and navigate away without saving", 
			groups = { "functional", "L3" })
	
	public void NavigateAway_03() throws HarnessException {

		// Navigate to preferences -> calendar
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Calendar);
		SleepUtil.sleepMedium();

		// Select custom work hours for e.g. Tuesday to Friday
		// Change the default appointment duration
		app.zPagePreferences.zToolbarPressPulldown(Button.O_DEFAULT_APPOINTMENT_DURATION, Button.O_APPOINTMENT_DURATION_90);
		boolean isVisible = navigateAwayCheckWarning();
		if ( !isVisible ) {
			throw new HarnessException("Dialog 'Save Changes?' was present!");
		}
	}
	
}

		
