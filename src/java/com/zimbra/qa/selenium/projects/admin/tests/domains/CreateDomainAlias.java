/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.domains;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.DomainItem;
import com.zimbra.qa.selenium.projects.admin.ui.WizardCreateDomainAlias;


public class CreateDomainAlias extends AdminCommonTest {
	
	public CreateDomainAlias() {
		logger.info("New " + CreateDomainAlias.class.getCanonicalName());
		
		// All tests start at the "Alias" page
		super.startingPage=app.zPageManageDomains;
	}
	
	
	/**
	 * Testcase : Create a basic domain alias
	 * Steps :
	 * 1. Create a domain alias from GUI i.e. New -> Alias.
	 * 2. Verify domain alias is created using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Create a domain alias",
			groups = { "sanity" })
			public void CreateAlias_01() throws HarnessException {

		// Create a new account in the Admin Console
		DomainItem domainalias = new DomainItem();		

		// Click "New"
		WizardCreateDomainAlias wizard = 
			(WizardCreateDomainAlias)app.zPageManageDomains.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.O_ADD_DOMAIN_ALIAS);
		
		// Fill out the wizard	
		wizard.zCompleteWizard(domainalias);

		// Verify the domain exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
			+	"<domain by='name'>" + domainalias.getName() + "</domain>"
			+	"</GetDomainRequest>");


		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDomainResponse/admin:domain", 1);
		ZAssert.assertNotNull(response, "Verify the domain is created successfully");
	}

}
