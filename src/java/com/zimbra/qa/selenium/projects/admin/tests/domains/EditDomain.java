/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.DomainItem;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditDomain;
import com.zimbra.qa.selenium.projects.admin.pages.PageMain;
import com.zimbra.qa.selenium.projects.admin.pages.PageSearchResults;

public class EditDomain extends AdminCore {

	public EditDomain() {
		logger.info("New "+ EditDomain.class.getCanonicalName());
		super.startingPage = app.zPageManageDomains;
	}


	/**
	 * Testcase : Verify delete domain operation --  Manage Domain List View
	 * Steps :
	 * 1. Create a domain using SOAP.
	 * 2. Select a domain.
	 * 4. Edit an domain using edit button in Gear box menu.
	 * 5. Verify domain is edited using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Verify edit domain operation --  Manage Domain List View",
			groups = { "smoke", "L1" })

	public void EditDomain_01() throws HarnessException {

		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();
		String domainName = domain.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend("<CreateDomainRequest xmlns='urn:zimbraAdmin'>" + "<name>"
				+ domainName + "</name>" + "</CreateDomainRequest>");

		// Refresh the domain list
		app.zPageManageDomains.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be deleted.
		app.zPageManageDomains.zListItem(Action.A_LEFTCLICK, domain.getName());

		FormEditDomain form = (FormEditDomain) app.zPageManageDomains.zToolbarPressPulldown(Button.B_GEAR_BOX,
				Button.O_EDIT);

		// Edit the description.
		String description = "editeddomain_" + ConfigProperties.getUniqueString();
		form.zSetName(description);

		// Submit
		form.zSubmit();

		// Verify the domain exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend("<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+ "<domain by='name'>" + domainName + "</domain>" + "</GetDomainRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin()
				.soapSelectNode("//admin:GetDomainResponse/admin:domain/admin:a[@n='description']", 1);
		ZAssert.assertStringContains(response.toString(), description, "Verify description is edited correctly");
	}


	/**
	 * Testcase : Verify delete domain operation  -- Manage Domain List View/Right Click Menu
	 * Steps :
	 * 1. Create a domain using SOAP.
	 * 2. Right click on domain.
	 * 3. Delete a domain using delete button in right click menu.
	 * 4. Verify domain is deleted using SOAP..
	 * @throws HarnessException
	 */

	@Test (description = "Verify edit domain operation",
			groups = { "functional", "L3" })

	public void EditDomain_02() throws HarnessException {

		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();
		String domainName = domain.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend("<CreateDomainRequest xmlns='urn:zimbraAdmin'>" + "<name>"
				+ domainName + "</name>" + "</CreateDomainRequest>");

		// Refresh the domain list
		app.zPageManageDomains.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be deleted.
		app.zPageManageDomains.zListItem(Action.A_RIGHTCLICK, domain.getName());

		FormEditDomain form = (FormEditDomain) app.zPageManageDomains.zToolbarPressButton(Button.B_TREE_EDIT);

		// Edit the description.
		String description = "editeddomain_" + ConfigProperties.getUniqueString();
		form.zSetName(description);

		// Submit
		form.zSubmit();

		// Verify the domain exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend("<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+ "<domain by='name'>" + domainName + "</domain>" + "</GetDomainRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin()
				.soapSelectNode("//admin:GetDomainResponse/admin:domain/admin:a[@n='description']", 1);
		ZAssert.assertStringContains(response.toString(), description, "Verify description is edited correctly");
	}

	/**
	 * Testcase : Edit domain name  - Search list view
	 * Steps :
	 * 1. Create an domain using SOAP.
	 * 2. Go to search domain View
	 * 3. Select a domain.
	 * 4. Edit an domain using edit button in Gear box menu.
	 * 5. Verify domain is edited using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Edit domain name  - Search list View",
			groups = { "functional", "L2" })

	public void Editdomain_03() throws HarnessException {

		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();
		String domainName = domain.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend("<CreateDomainRequest xmlns='urn:zimbraAdmin'>" + "<name>"
				+ domainName + "</name>" + "</CreateDomainRequest>");

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(domainName);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on domain to be deleted.
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, domain.getName());

		// Click on Edit button
		app.zPageSearchResults.zSetType(PageSearchResults.TypeOfObject.DOMAIN);
		FormEditDomain form = (FormEditDomain) app.zPageSearchResults.zToolbarPressPulldown(Button.B_GEAR_BOX,
				Button.O_EDIT);

		// Edit the description.
		String description = "editeddomain_" + ConfigProperties.getUniqueString();
		form.zSetName(description);

		// Submit
		form.zSubmit();

		// Verify the domain exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend("<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+ "<domain by='name'>" + domainName + "</domain>" + "</GetDomainRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin()
				.soapSelectNode("//admin:GetDomainResponse/admin:domain/admin:a[@n='description']", 1);
		ZAssert.assertStringContains(response.toString(), description, "Verify description is edited correctly");
		app.zPageMain.zLogout();
	}


	/**
	 * Testcase : Edit domain name -- right click
	 * Steps :
	 * 1. Create an domain using SOAP.
	 * 2. Edit the domain name using UI Right Click.
	 * 3. Verify domain name is changed using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Edit domain name -- right click",
			groups = { "functional", "L3" })

	public void Editdomain_04() throws HarnessException {

		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();
		String domainName = domain.getName();
		this.startingPage = app.zPageManageDomains;
		this.startingPage.zNavigateTo();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend("<CreateDomainRequest xmlns='urn:zimbraAdmin'>" + "<name>"
				+ domainName + "</name>" + "</CreateDomainRequest>");

		// Refresh the account list
		app.zPageSearchResults.zSelectSearchObject(app.zPageSearchResults.S_DOMAIN);

		// Enter the search string to find the account
		app.zPageSearchResults.zSetType(PageSearchResults.TypeOfObject.DOMAIN);
		app.zPageSearchResults.zAddSearchQuery(domainName);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on domain to be deleted.
		app.zPageSearchResults.zListItem(Action.A_RIGHTCLICK, domain.getName());

		// Click on Edit button
		app.zPageSearchResults.zSetType(PageSearchResults.TypeOfObject.DOMAIN);
		FormEditDomain form = (FormEditDomain) app.zPageSearchResults.zToolbarPressButton(Button.B_TREE_EDIT);

		// Edit the description.
		String description = "editeddomain_" + ConfigProperties.getUniqueString();
		form.zSetName(description);

		// Submit
		form.zSubmit();

		// Verify the domain exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend("<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+ "<domain by='name'>" + domainName + "</domain>" + "</GetDomainRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin()
				.soapSelectNode("//admin:GetDomainResponse/admin:domain/admin:a[@n='description']", 1);
		ZAssert.assertStringContains(response.toString(), description, "Verify description is edited correctly");
	}
}