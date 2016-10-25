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
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.admin.tests.certificates;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageCertificates;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageCertificates.Locators;


public class ViewCertificate extends AdminCommonTest {
	public ViewCertificate() {
		logger.info("New "+ ViewCertificate.class.getCanonicalName());

		// All tests start at the "Cos" page
		super.startingPage = app.zPageManageCertificates;
	}
	
	/**
	 * Testcase : View Installed certificate
	 * Steps :
	 * 1. Go to "Home --> Configure --> Certificates"
	 * 2. Click on view certificate
	 * 3. Verify certificate details displayed correctly
	 * @throws HarnessException
	 */
	@Test( description = "Navigate to Certificates",
			groups = { "smoke" })
			public void ViewCertificate_01() throws HarnessException {
		
		// Go to "Home --> Configure --> Certificates"
		
		app.zPageManageCertificates.sClick(PageManageCertificates.Locators.SERVER_HOST_NAME);
		app.zPageManageCertificates.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.B_VIEW_CERTIFICATE);
		
		//Wait for certificate details to be displayed.
		app.zPageManageCertificates.zWaitForElementPresent(Locators.LDAP_CERTIFICATE_LABEL);
		
		// Verify certificate details displayed correctly
		ZAssert.assertTrue(app.zPageManageCertificates.sIsElementPresent(Locators.LDAP_CERTIFICATE_LABEL), "Verify certificate is installed!");
		
	}

}
