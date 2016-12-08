/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.DomainItem;
import com.zimbra.qa.selenium.projects.admin.ui.FormEditDomain;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.util.ZAssert;

public class EditLogoUrlOfTheTheme extends AdminCommonTest {
	public EditLogoUrlOfTheTheme() {
		logger.info("New" + EditLogoUrlOfTheTheme.class.getCanonicalName());

		//All tests starts from domain page
		this.startingPage=app.zPageManageDomains;
	}

	/**
	 * Testcase : Bug 50660 zimbra admin console theme customization should support the change of logoURL
	 * Steps :
	 * 1. Create a domain using SOAP.
	 * 2. Select a domain.
	 * 4. Edit an domain using edit button in Gear box menu.
	 * 5. Verify logoURL edited using SOAP.
	 * @throws HarnessException
	 */
	@Bugs( ids = "50660")
	@Test( description = "zimbra admin console theme customization should support the change of logoURL",
	groups = { "functional", "L2" })
	public void EditLogoUrlOfTheTheme_01() throws HarnessException {

		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();
		String domainName=domain.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + domainName + "</name>"
						+		"</CreateDomainRequest>");


		// Refresh the domain list
		app.zPageManageDomains.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be edited
		app.zPageManageDomains.zListItem(Action.A_LEFTCLICK, domain.getName());

		FormEditDomain form = (FormEditDomain) app.zPageManageDomains.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Edit the logo URL
		String URL = "aaa.com";
		form.setLogoURL(URL);

		//Submit the form.
		form.zSubmit();

		// Verify the domain with edited logo URL
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
						+	"<domain by='name'>" + domainName + "</domain>"
						+	"</GetDomainRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDomainResponse/admin:domain/admin:a[@n='zimbraSkinLogoURL']", 1);
		ZAssert.assertStringContains(response.toString(), URL, "Verify description is edited correctly");

	}

}


