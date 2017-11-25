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
package com.zimbra.qa.selenium.projects.admin.tests.search.domains;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.DomainItem;
import com.zimbra.qa.selenium.projects.admin.pages.DelegatedAdminPageManageAccounts.Locators;

public class DeleteDomain extends AdminCore {

	public DeleteDomain() {
		logger.info("New" + DeleteDomain.class.getCanonicalName());
		this.startingPage=app.zPageManageDomains;
	}


	/**
	 * Testcase : Verify delete domain operation --  Search List View
	 * Steps :
	 * 1. Create a domain using SOAP.
	 * 2. Search domain.
	 * 3. Select a domain.
	 * 4. check delete button in Gear box menu.
	 * 5. Verify delete domain option is disabled at search results.
	 * @throws HarnessException
	 */

	@Test (description = "Verify delete domain operation is absent from leftclick > gear icon --  Search List View",
			groups = { "functional", "L2" })

	public void DeleteDomain_01() throws HarnessException {

		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + domain.getName() + "</name>"
				+		"</CreateDomainRequest>");

		// Enter the search string to find the domain
		app.zPageSearchResults.zAddSearchQuery(domain.getName());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on domain to be deleted.
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, domain.getName());

		// Click on gear icon
		app.zPageSearchResults.sClickAt(Locators.GEAR_ICON,"");
		boolean isPresent = app.zPageSearchResults.zVerifyDisabled("DeleteTreeMenu");
		ZAssert.assertTrue(isPresent, "Verify the delete domain button is disabled at Search results");
	}


	/**
	 * Testcase : Verify delete domain operation is disabled -- Search List View/Right Click Menu
	 * Steps :
	 * 1. Create a domain using SOAP.
	 * 2. Search domain.
	 * 3. Right click on domain.
	 * 4. check option Delete using delete button in right click menu.
	 * 5. Verify option is disabled
	 * @throws HarnessException
	 */

	@Test (description = " Verify delete domain operation is disabled -- Search List View/Right Click Menu",
			groups = { "functional", "L3" })

	public void DeleteDomain_02() throws HarnessException {

		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + domain.getName() + "</name>"
				+		"</CreateDomainRequest>");

		// Enter the search string to find the domain
		app.zPageSearchResults.zAddSearchQuery(domain.getName());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Right Click on domain to be deleted.
		app.zPageSearchResults.zListItem(Action.A_RIGHTCLICK, domain.getName());

		boolean isPresent = app.zPageSearchResults.zVerifyDisabled("DeleteTreeMenu");
		ZAssert.assertTrue(isPresent, "Verify the delete domain button is disabled at Search results");
	}


	/**
	 * Testcase : Verify delete domain alias operation is disabled- Search list view.
	 * Steps :
	 * 1. Create domain alias using SOAP.
	 * 2. Search created domain alias.
	 * 3. Select the domain alias from gear box menu and select delete.
	 * 4. Verify domain alias option is disabled
	 * @throws HarnessException
	 */

	@Test (description = "Verify delete domain alias operation is disabled- Search list view.",
			groups = { "functional", "L2" })

	public void DeleteDomain_03() throws HarnessException {

		String targetDomain = ConfigProperties.getStringProperty("testdomain");
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+	"<domain by='name'>" + targetDomain + "</domain>"
				+	"</GetDomainRequest>");

		String targetDomainID=ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:GetDomainResponse/admin:domain", "id").toString();

		// Create a new domain alias in the Admin Console using SOAP
		DomainItem alias = new DomainItem();
		String domainAliasName=alias.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
				+ "<name>"+domainAliasName+"</name>"
				+ "<a n='zimbraDomainType'>alias</a>"
				+ "<a n='zimbraDomainAliasTargetId'>"+targetDomainID+"</a>"
				+ "<a n='description'>"+"domain alias"+"</a>"
				+ "<a n='zimbraMailCatchAllAddress'>@"+domainAliasName+"</a>"
				+ "<a  n='zimbraMailCatchAllForwardingAddress'>@"+targetDomain+"</a>"
				+ "</CreateDomainRequest>");

		// Enter the search string to find the alias
		app.zPageSearchResults.zAddSearchQuery(domainAliasName);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on alias to be deleted.
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, domainAliasName);

		// Click on Delete button
		boolean isPresent = app.zPageSearchResults.zVerifyDisabled("DeleteTreeMenu");
		ZAssert.assertTrue(isPresent, "Verify the delete domain button is disabled at Search results");
	}


	/**
	 * Testcase : Verify delete domain alias operation is disabled- Search list view.
	 * Steps :
	 * 1. Create domain alias using SOAP.
	 * 2. Search created domain alias.
	 * 3. Select the domain alias from gear box menu and select delete.
	 * 4. Verify domain alias option is disabled
	 * @throws HarnessException
	 */

	@Test (description = "Verify delete domain alias operation is disabled- Search list view.",
			groups = { "functional", "L3" })

	public void DeleteDomain_04() throws HarnessException {

		String targetDomain = ConfigProperties.getStringProperty("testdomain");
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+	"<domain by='name'>" + targetDomain + "</domain>"
				+	"</GetDomainRequest>");

		String targetDomainID=ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:GetDomainResponse/admin:domain", "id").toString();

		// Create a new domain alias in the Admin Console using SOAP
		DomainItem alias = new DomainItem();
		String domainAliasName=alias.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
				+ "<name>"+domainAliasName+"</name>"
				+ "<a n='zimbraDomainType'>alias</a>"
				+ "<a n='zimbraDomainAliasTargetId'>"+targetDomainID+"</a>"
				+ "<a n='description'>"+"domain alias"+"</a>"
				+ "<a n='zimbraMailCatchAllAddress'>@"+domainAliasName+"</a>"
				+ "<a  n='zimbraMailCatchAllForwardingAddress'>@"+targetDomain+"</a>"
				+ "</CreateDomainRequest>");

		// Enter the search string to find the alias
		app.zPageSearchResults.zAddSearchQuery(domainAliasName);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on alias to be deleted.
		app.zPageSearchResults.zListItem(Action.A_RIGHTCLICK, domainAliasName);

		// Click on Delete button
		boolean isPresent = app.zPageSearchResults.zVerifyDisabled("DeleteTreeMenu");
		ZAssert.assertTrue(isPresent, "Verify the delete domain button is disabled at Search results");
	}
}