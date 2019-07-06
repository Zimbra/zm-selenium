/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.domains;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.DomainItem;
import com.zimbra.qa.selenium.projects.admin.pages.WizardCreateDomain;

public class CreateDomain extends AdminCore {

	public CreateDomain() {
		logger.info("New " + CreateDomain.class.getName());
		super.startingPage=app.zPageManageDomains;
	}


	/**
	 * Testcase : Create a simple domain
	 * Steps :
	 * 1. Create a domain from GUI
	 * 2. Verify domain is created using SOAP.
	 * @throws HarnessException
	 */

	@Bugs (ids = "58795")
	@Test (description = "Create a simple domain",
			groups = { "smoke" })

	public void CreateDomain_01() throws HarnessException {

		// Create a new domain in the Admin Console
		DomainItem domain = new DomainItem();

		// Click "New" -> "Domain"
		WizardCreateDomain wizard = (WizardCreateDomain) app.zPageManageDomains.zToolbarPressPulldown(Button.B_GEAR_BOX,
				Button.O_NEW);

		// Fill out the wizard and click Finish
		wizard.zCompleteWizard(domain);

		// Verify the domain exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
			+	"<domain by='name'>" + domain.getName() + "</domain>"
			+	"</GetDomainRequest>");


		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDomainResponse/admin:domain", 1);
		ZAssert.assertNotNull(response, "Verify the domain is created successfully");
	}
}