/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.admin.tests.navigation;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain.Locators;
import com.zimbra.qa.selenium.projects.admin.ui.WizardConfigureAuthentication;
import com.zimbra.qa.selenium.projects.admin.ui.WizardConfigureGAL;
import com.zimbra.qa.selenium.projects.admin.ui.WizardCreateAccount;
import com.zimbra.qa.selenium.projects.admin.ui.WizardCreateDomain;
import com.zimbra.qa.selenium.projects.admin.ui.WizardInstallCertificate;
import com.zimbra.qa.selenium.projects.admin.ui.WizardMigrationCoexistance;

public class HomePageLinks extends AdminCommonTest {
	
	public HomePageLinks() {
		logger.info("New "+ HomePageLinks.class.getCanonicalName());

		// All tests start at the "HOME" page
		super.startingPage = app.zPageMain;
	}
	
	/**
	 * Testcase :Navigate to Install License
	 * Steps :
	 * 1. Verify navigation path -- "Home --> Install Licenses Link"
	 * @throws HarnessException
	 */
	@Test(	description = "Navigate to Install License",
			groups = { "obsolete" })
			public void NavigateHomePageLinks_01() throws HarnessException {
		
		// click on Install license Link on Home page and check if it redirects to proper page

		// Navigate back to Home page
		super.startingPage.zNavigateTo();
		
		app.zPageMain.sClickAt(Locators.HomeInstallLicense,"");
		boolean isNavigationSuccess = app.zPageMain.zVerifyHeader("License");
		ZAssert.assertTrue(isNavigationSuccess, "Verify the install license page is opened");

		
	}
	
	/**
	 * Testcase : Navigate to Search page
	 * Steps :
	 * 1. Verify navigation path -- "Home --> Configure Backup"
	 * @throws HarnessException
	 */
	@Test(	description = "Navigate to Configure Backup",
			groups = { "sanity" })
			public void NavigateHomePageLinks_02() throws HarnessException {
		
		// Navigate back to Home page
		super.startingPage.zNavigateTo();
		app.zPageMain.sClickAt(Locators.HomeConfigureBackups,"");
		boolean isNavigationSuccess = app.zPageMain.zVerifyHeader("Backup/Restore");
		ZAssert.assertTrue(isNavigationSuccess, "Verify the Backup/Restore page is opened");

	}
	
	
	/**
	 * Testcase : Navigate to Search page
	 * Steps :
	 * 1. Verify navigation path -- "Home --> Install License --> Install certificate Wizard"
	 * @throws HarnessException
	 */
	@Test(	description = "Navigate to Install certificate wizard",
			groups = { "sanity" })
			public void NavigateHomePageLinks_03() throws HarnessException {

		// Navigate back to Home page
		super.startingPage.zNavigateTo();

		WizardInstallCertificate wizard = 
			(WizardInstallCertificate)app.zPageManageCertificates.zToolbarPressButton(Button.B_INSTALL_CERTIFICATE);
		ZAssert.assertNotNull(wizard.sGetTitle(), "Verify the install certificate wizard is opened");
		
		// close the wizard to proceed to next test
		boolean isClosed = wizard.zCloseWizard();
		ZAssert.assertTrue(isClosed, "Verify the install certificate wizard is closed");

		
	}
	
	
	/**
	 * Testcase : Navigate to Configure default COS from home page link
	 * Steps :
	 * 1. Verify navigation path -- "Home --> Configure Default COS"
	 * @throws HarnessException
	 */
	@Test(	description = "Navigate to Configure default COS",
			groups = { "sanity" })
			public void NavigateHomePageLinks_04() throws HarnessException {
		
		/*
		 * Verify navigation path -- "Home --> Configure Default COS""
		 */
		super.startingPage.zNavigateTo();
		app.zPageMain.sClickAt(Locators.HomeConfigureDefaultCos,"");
		boolean isNavigationSuccess = app.zPageMain.zVerifyHeader("default");
		ZAssert.assertTrue(isNavigationSuccess, "Verify the configure default COS page is opened");

	}
	
	
	
	
	/**
	 * Testcase : Navigate to create Domain wizard from home page Link
	 * Steps :
	 * 1. Verify navigation path -- "Home --> create Domain wizard
	 * @throws HarnessException
	 */
	@Test(	description = "Navigate to create Domain wizard",
			groups = { "sanity" })
			public void NavigateHomePageLinks_05() throws HarnessException {
		
		// Navigate back to home page 
		super.startingPage.zNavigateTo();

		WizardCreateDomain wizard = 
			(WizardCreateDomain)app.zPageManageDomains.zToolbarPressButton(Button.B_HOME_DOMAIN);
		ZAssert.assertNotNull(wizard.sGetTitle(), "Verify the new domain wizard is opened");
		
		// Close the wizard to proceed to next test
		boolean isClosed = wizard.zCloseWizard();
		ZAssert.assertTrue(isClosed, "Verify the  new domain wizard is closed");

		
	}
	

	/**
	 * Testcase :Navigate to Configure GAL wizard from Home page link
	 * Steps :
	 * 1. Verify navigation path -- "Home --> Configure GAL wizard
	 * @throws HarnessException
	 */
	@Test(	description = "Navigate to Configure GAL wizard",
			groups = { "sanity" })
			public void NavigateHomePageLinks_06() throws HarnessException {

		// Navigate back to Home page 
		super.startingPage.zNavigateTo();

		WizardConfigureGAL wizard = 
			(WizardConfigureGAL)app.zPageManageCofigureGAL.zToolbarPressButton(Button.B_CONFIGURE_GAL);
		ZAssert.assertNotNull(wizard.sGetTitle(), "Verify the  Configure GAL wizard is opened");
		
		// Close the wizard to proceed to next test
		boolean isClosed = wizard.zCloseWizard();
		ZAssert.assertTrue(isClosed, "Verify the Configure GAL wizard is closed");

		
	}
	

	/**
	 * Testcase :Navigate to Configure Authentication wizard from Home page link
	 * Steps :
	 * 1. Verify navigation path -- "Home --> Configure Authentication wizard
	 * @throws HarnessException
	 */
	@Test(	description = "Navigate to Configure Authentication wizard",
			groups = { "sanity" })
			public void NavigateHomePageLinks_07() throws HarnessException {

		// Navigate back to Home page
		super.startingPage.zNavigateTo();

		WizardConfigureAuthentication wizard = 
			(WizardConfigureAuthentication)app.zPageManageCofigureAuthentication.zToolbarPressButton(Button.B_CONFIGURE_AUTHENTICATION);
		ZAssert.assertNotNull(wizard.sGetTitle(), "Verify the  Configure Authentication wizard is opened");
		
		// Close the wizard to proceed to next test
		boolean isClosed = wizard.zCloseWizard();
		ZAssert.assertTrue(isClosed, "Verify the Configure Authentication wizard is closed");
	}
	
	
	/**
	 * Testcase : Navigate to create account wizard from Home page Link
	 * 	 * Steps :
	 * 1. Verify navigation path -- "Home --> create account wizard from Home page Link
	 * @throws HarnessException
	 */
	@Test(	description = "Navigate to create account wizard",
			groups = { "sanity" })
			public void NavigateHomePageLinks_08() throws HarnessException {
	
		// Navigate back to Home page
		super.startingPage.zNavigateTo();

		WizardCreateAccount wizard = 
			(WizardCreateAccount)app.zPageManageAccounts.zToolbarPressButton(Button.B_HOME_ACCOUNT);
		ZAssert.assertNotNull(wizard.sGetTitle(), "Verify the new account wizard is opened");
		
		// Close the wizard to proceed to next test
		boolean isClosed = wizard.zCloseWizard();
		ZAssert.assertTrue(isClosed, "Verify the  new account wizard is closed");

		
	}

	
	/**
	 * Testcase : Navigate to Configure manage accounts from Home page links
	 * Steps :
	 * 1. Verify navigation path -- "Home --> Configure manage accounts from Home page links
	 * @throws HarnessException
	 */
	@Test(	description = "Navigate to Configure manage accounts",
			groups = { "sanity" })
			public void NavigateHomePageLinks_09() throws HarnessException {
		
		// Navigate back to Home page
		super.startingPage.zNavigateTo();
		
		app.zPageMain.sClickAt(Locators.HomeManageAccount,"");
		boolean isNavigationSuccess = app.zPageMain.zVerifyHeader("Manage");
		ZAssert.assertTrue(isNavigationSuccess, "Verify the manage accounts page is opened");

	}
	
	
	/**
	 * Testcase : Navigate to migration and co Existance wizard from Home page Links
	 * Steps :
	 * 1. Verify navigation path -- "Home --> to migration and co Existance wizard from Home page Links
	 * @throws HarnessException
	 */
	@Test(	description = "Navigate to migration and co Existance wizard",
			groups = { "sanity" })
			public void NavigateHomePageLinks_10() throws HarnessException {
		
		// Navigate Back to Home page
		super.startingPage.zNavigateTo();

		WizardMigrationCoexistance wizard = 
			(WizardMigrationCoexistance)app.zPageManageMigrationCoexistance.zToolbarPressButton(Button.B_HOME_MIGRATION);
		ZAssert.assertNotNull(wizard.sGetTitle(), "Verify the new account wizard is opened");
		
		// Close the wizard to proceed to next test
		boolean isClosed = wizard.zCloseWizard();
		ZAssert.assertTrue(isClosed, "Verify the migration and Coexistance wizard is closed");

		
	}
}

