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
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.PageMain.Locators;
import com.zimbra.qa.selenium.projects.admin.pages.WizardConfigureAuthentication;
import com.zimbra.qa.selenium.projects.admin.pages.WizardConfigureGAL;
import com.zimbra.qa.selenium.projects.admin.pages.WizardCreateAccount;
import com.zimbra.qa.selenium.projects.admin.pages.WizardCreateDomain;
import com.zimbra.qa.selenium.projects.admin.pages.WizardInstallCertificate;
import com.zimbra.qa.selenium.projects.admin.pages.WizardMigrationCoexistance;

public class HomePageLinks extends AdminCore {

	public HomePageLinks() {
		logger.info("New " + HomePageLinks.class.getCanonicalName());
		super.startingPage = app.zPageMain;
	}


	/**
	 * Testcase : Navigate to Search page Steps : 1. Verify navigation path -- "Home
	 * --> Install License --> Install certificate Wizard"
	 *
	 * @throws HarnessException
	 */

	@Test (description = "Navigate to Install certificate wizard",
			groups = { "smoke" })

	public void NavigateHomePageLinks_01() throws HarnessException {

		WizardInstallCertificate wizard = (WizardInstallCertificate) app.zPageManageCertificates
				.zToolbarPressButton(Button.B_INSTALL_CERTIFICATE);
		ZAssert.assertNotNull(wizard.sGetTitle(), "Verify the install certificate wizard is opened");

		// close the wizard to proceed to next test
		boolean isClosed = wizard.zCloseWizard();
		ZAssert.assertTrue(isClosed, "Verify the install certificate wizard is closed");
		app.zPageMain.zLogout();
	}


	/**
	 * Testcase : Navigate to Configure default COS from home page link Steps : 1.
	 * Verify navigation path -- "Home --> Configure Default COS"
	 *
	 * @throws HarnessException
	 */

	@Test (description = "Navigate to Configure default COS",
			groups = { "smoke" })

	public void NavigateHomePageLinks_02() throws HarnessException {

		/*
		 * Verify navigation path -- "Home --> Configure Default COS""
		 */
		app.zPageMain.sClickAt(Locators.HomeConfigureDefaultCos, "");
		boolean isNavigationSuccess = app.zPageMain.zVerifyHeader("default");
		ZAssert.assertTrue(isNavigationSuccess, "Verify the configure default COS page is opened");
		app.zPageMain.zLogout();
	}


	/**
	 * Testcase : Navigate to create Domain wizard from home page Link Steps : 1.
	 * Verify navigation path -- "Home --> create Domain wizard
	 *
	 * @throws HarnessException
	 */

	@Test (description = "Navigate to create Domain wizard",
			groups = { "smoke" })

	public void NavigateHomePageLinks_03() throws HarnessException {

		// Navigate back to home page
		WizardCreateDomain wizard = (WizardCreateDomain) app.zPageManageDomains
				.zToolbarPressButton(Button.B_HOME_DOMAIN);
		ZAssert.assertNotNull(wizard.sGetTitle(), "Verify the new domain wizard is opened");

		// Close the wizard to proceed to next test
		boolean isClosed = wizard.zCloseWizard();
		ZAssert.assertTrue(isClosed, "Verify the  new domain wizard is closed");
		app.zPageMain.zLogout();
	}


	/**
	 * Testcase :Navigate to Configure GAL wizard from Home page link Steps : 1.
	 * Verify navigation path -- "Home --> Configure GAL wizard
	 *
	 * @throws HarnessException
	 */

	@Test (description = "Navigate to Configure GAL wizard",
			groups = { "smoke" })

	public void NavigateHomePageLinks_04() throws HarnessException {

		// Navigate back to Home page
		WizardConfigureGAL wizard = (WizardConfigureGAL) app.zPageManageCofigureGAL
				.zToolbarPressButton(Button.B_CONFIGURE_GAL);
		ZAssert.assertNotNull(wizard.sGetTitle(), "Verify the  Configure GAL wizard is opened");

		// Close the wizard to proceed to next test
		boolean isClosed = wizard.zCloseWizard();
		ZAssert.assertTrue(isClosed, "Verify the Configure GAL wizard is closed");

		app.zPageMain.zLogout();
	}


	/**
	 * Testcase :Navigate to Configure Authentication wizard from Home page link
	 * Steps : 1. Verify navigation path -- "Home --> Configure Authentication
	 * wizard
	 *
	 * @throws HarnessException
	 */

	@Test (description = "Navigate to Configure Authentication wizard",
			groups = { "smoke" })

	public void NavigateHomePageLinks_05() throws HarnessException {

		// Navigate back to Home page
		WizardConfigureAuthentication wizard = (WizardConfigureAuthentication) app.zPageManageCofigureAuthentication
				.zToolbarPressButton(Button.B_CONFIGURE_AUTHENTICATION);
		ZAssert.assertNotNull(wizard.sGetTitle(), "Verify the  Configure Authentication wizard is opened");

		// Close the wizard to proceed to next test
		boolean isClosed = wizard.zCloseWizard();
		ZAssert.assertTrue(isClosed, "Verify the Configure Authentication wizard is closed");
		app.zPageMain.zLogout();
	}


	/**
	 * Testcase : Navigate to create account wizard from Home page Link * Steps : 1.
	 * Verify navigation path -- "Home --> create account wizard from Home page Link
	 *
	 * @throws HarnessException
	 */

	@Test (description = "Navigate to create account wizard",
			groups = { "smoke" })

	public void NavigateHomePageLinks_06() throws HarnessException {

		// Navigate back to Home page
		WizardCreateAccount wizard = (WizardCreateAccount) app.zPageManageAccounts
				.zToolbarPressButton(Button.B_HOME_ACCOUNT);
		ZAssert.assertNotNull(wizard.sGetTitle(), "Verify the new account wizard is opened");

		// Close the wizard to proceed to next test
		boolean isClosed = wizard.zCloseWizard();
		ZAssert.assertTrue(isClosed, "Verify the  new account wizard is closed");
		app.zPageMain.zLogout();
	}


	/**
	 * Testcase : Navigate to Configure manage accounts from Home page links Steps :
	 * 1. Verify navigation path -- "Home --> Configure manage accounts from Home
	 * page links
	 *
	 * @throws HarnessException
	 */

	@Test (description = "Navigate to Configure manage accounts",
			groups = { "smoke" })

	public void NavigateHomePageLinks_07() throws HarnessException {

		// Navigate back to Home page
		app.zPageMain.sClickAt(Locators.HomeManageAccount, "");
		boolean isNavigationSuccess = app.zPageMain.zVerifyHeader("Manage");
		ZAssert.assertTrue(isNavigationSuccess, "Verify the manage accounts page is opened");
		app.zPageMain.zLogout();
	}


	/**
	 * Testcase : Navigate to migration and co Existance wizard from Home page Links
	 * Steps : 1. Verify navigation path -- "Home --> to migration and co Existance
	 * wizard from Home page Links
	 *
	 * @throws HarnessException
	 */

	@Test (description = "Navigate to migration and co Existance wizard",
			groups = { "smoke" })

	public void NavigateHomePageLinks_08() throws HarnessException {

		// Navigate Back to Home page
		WizardMigrationCoexistance wizard = (WizardMigrationCoexistance) app.zPageManageMigrationCoexistance
				.zToolbarPressButton(Button.B_HOME_MIGRATION);
		ZAssert.assertNotNull(wizard.sGetTitle(), "Verify the new account wizard is opened");

		// Close the wizard to proceed to next test
		boolean isClosed = wizard.zCloseWizard();
		ZAssert.assertTrue(isClosed, "Verify the migration and Coexistance wizard is closed");
		app.zPageMain.zLogout();
	}
}