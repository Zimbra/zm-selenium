/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.core.ExecuteHarnessMain;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.DomainItem;
import com.zimbra.qa.selenium.projects.admin.pages.PageManageDomains;
import com.zimbra.qa.selenium.projects.admin.pages.PageSearchResults;
import com.zimbra.qa.selenium.projects.admin.pages.WizardCreateDomainAlias;

public class EditDomainAlias extends AdminCore {

	public EditDomainAlias() {
		logger.info("New "+ EditDomainAlias.class.getCanonicalName());
		super.startingPage = app.zPageManageDomains;
	}


	@Test (description = "Verify edit domain operation --  Manage Domain List View",
			groups = { "sanity-skip" })

	public void EditDomainAlias_01() throws HarnessException {
		String targetDomain = ConfigProperties.getStringProperty("testdomain");
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+	"<domain by='name'>" + targetDomain + "</domain>"
				+	"</GetDomainRequest>");

		String targetDomainID = ZimbraAdminAccount.AdminConsoleAdmin()
				.soapSelectValue("//admin:GetDomainResponse/admin:domain", "id").toString();

		// Create a new domain alias in the Admin Console using SOAP
		DomainItem alias = new DomainItem();
		String domainAliasName = alias.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
				+ "<name>"+domainAliasName+"</name>"
				+ "<a n='zimbraDomainType'>alias</a>"
				+ "<a n='zimbraDomainAliasTargetId'>"+targetDomainID+"</a>"
				+ "<a n='zimbraMailCatchAllAddress'>@"+domainAliasName+"</a>"
				+ "<a n='zimbraMailCatchAllForwardingAddress'>@"+targetDomain+"</a>"
				+ "<a n='description'>Created by Selenium automation</a>"
				+ "</CreateDomainRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on account to be edited.
		app.zPageManageDomains.zListItem(Action.A_LEFTCLICK, domainAliasName);

		//Set object type and initialize create domain alias wizard
		app.zPageManageDomains.zSetType(PageManageDomains.TypeOfObject.DOMAIN_ALIAS);
		WizardCreateDomainAlias wizard = (WizardCreateDomainAlias) app.zPageManageDomains
				.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);
		wizard.zSetTargetDomain(alias);

		// Verify the domain exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+	"<domain by='name'>" + domainAliasName + "</domain>"
				+	"</GetDomainRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode(
				"//admin:GetDomainResponse/admin:domain/admin:a[@n='zimbraMailCatchAllForwardingAddress']", 1);
		ZAssert.assertStringContains(response.toString(), ExecuteHarnessMain.zimbraServer,
				"Verify description is edited correctly");
	}


	@Test (description = "Verify delete domain operation",
			groups = { "functional", "testcafe" })

	public void EditDomainAlias_02() throws HarnessException {
		String targetDomain = ConfigProperties.getStringProperty("testdomain");
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+	"<domain by='name'>" + targetDomain + "</domain>"
				+	"</GetDomainRequest>");

		String targetDomainID = ZimbraAdminAccount.AdminConsoleAdmin()
				.soapSelectValue("//admin:GetDomainResponse/admin:domain", "id").toString();

		// Create a new domain alias in the Admin Console using SOAP
		DomainItem alias = new DomainItem();
		String domainAliasName = alias.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
				+ "<name>"+domainAliasName+"</name>"
				+ "<a n='zimbraDomainType'>alias</a>"
				+ "<a n='zimbraDomainAliasTargetId'>"+targetDomainID+"</a>"
				+ "<a n='zimbraMailCatchAllAddress'>@"+domainAliasName+"</a>"
				+ "<a n='zimbraMailCatchAllForwardingAddress'>@"+targetDomain+"</a>"
				+ "<a n='description'>Created by Selenium automation</a>"
				+ "</CreateDomainRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on account to be edited.
		app.zPageManageDomains.zListItem(Action.A_RIGHTCLICK, domainAliasName);

		// Set object type and initialize create domain alias wizard
		app.zPageManageDomains.zSetType(PageManageDomains.TypeOfObject.DOMAIN_ALIAS);
		WizardCreateDomainAlias wizard = (WizardCreateDomainAlias) app.zPageManageDomains
				.zToolbarPressButton(Button.B_TREE_EDIT);
		wizard.zSetTargetDomain(alias);

		// Verify the domain exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+	"<domain by='name'>" + domainAliasName + "</domain>"
				+	"</GetDomainRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode(
				"//admin:GetDomainResponse/admin:domain/admin:a[@n='zimbraMailCatchAllForwardingAddress']", 1);
		ZAssert.assertStringContains(response.toString(), ExecuteHarnessMain.zimbraServer,
				"Verify description is edited correctly");
	}


	@Test (description = "Edit domain name  - Search list View",
			groups = { "sanity", "testcafe" })

	public void EditdomainAlias_03() throws HarnessException {
		String targetDomain = ConfigProperties.getStringProperty("testdomain");
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+	"<domain by='name'>" + targetDomain + "</domain>"
				+	"</GetDomainRequest>");

		String targetDomainID = ZimbraAdminAccount.AdminConsoleAdmin()
				.soapSelectValue("//admin:GetDomainResponse/admin:domain", "id").toString();

		// Create a new domain alias in the Admin Console using SOAP
		DomainItem alias = new DomainItem();
		String domainAliasName = alias.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
				+ "<name>"+domainAliasName+"</name>"
				+ "<a n='zimbraDomainType'>alias</a>"
				+ "<a n='zimbraDomainAliasTargetId'>"+targetDomainID+"</a>"
				+ "<a n='zimbraMailCatchAllAddress'>@"+domainAliasName+"</a>"
				+ "<a n='zimbraMailCatchAllForwardingAddress'>@"+targetDomain+"</a>"
				+ "<a n='description'>Created by Selenium automation</a>"
				+ "</CreateDomainRequest>");

		// Enter the search string to find the alias
		app.zPageSearchResults.zAddSearchQuery(domainAliasName);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on alias to be edited.
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, domainAliasName);

		// Click on Edit button
		app.zPageSearchResults.zSetType(PageSearchResults.TypeOfObject.DOMAIN_ALIAS);
		WizardCreateDomainAlias wizard = (WizardCreateDomainAlias) app.zPageSearchResults
				.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);
		wizard.zSetTargetDomain(alias);

		// Verify the domain exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+	"<domain by='name'>" + domainAliasName + "</domain>"
				+	"</GetDomainRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode(
				"//admin:GetDomainResponse/admin:domain/admin:a[@n='zimbraMailCatchAllForwardingAddress']", 1);
		ZAssert.assertStringContains(response.toString(), ExecuteHarnessMain.zimbraServer,
				"Verify description is edited correctly");
	}


	@Test (description = "Edit domain name -- right click",
			groups = { "functional", "testcafe" })

	public void EditdomainAlias_04() throws HarnessException {
		String targetDomain = ConfigProperties.getStringProperty("testdomain");
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+	"<domain by='name'>" + targetDomain + "</domain>"
				+	"</GetDomainRequest>");

		String targetDomainID = ZimbraAdminAccount.AdminConsoleAdmin()
				.soapSelectValue("//admin:GetDomainResponse/admin:domain", "id").toString();

		// Create a new domain alias in the Admin Console using SOAP
		DomainItem alias = new DomainItem();
		String domainAliasName = alias.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
				+ "<name>"+domainAliasName+"</name>"
				+ "<a n='zimbraDomainType'>alias</a>"
				+ "<a n='zimbraDomainAliasTargetId'>"+targetDomainID+"</a>"
				+ "<a n='zimbraMailCatchAllAddress'>@"+domainAliasName+"</a>"
				+ "<a n='zimbraMailCatchAllForwardingAddress'>@"+targetDomain+"</a>"
				+ "<a n='description'>Created by Selenium automation</a>"
				+ "</CreateDomainRequest>");

		// Enter the search string to find the alias
		app.zPageSearchResults.zAddSearchQuery(domainAliasName);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on alias to be edited.
		app.zPageSearchResults.zListItem(Action.A_RIGHTCLICK, domainAliasName);

		// Click on Edit button
		app.zPageSearchResults.zSetType(PageSearchResults.TypeOfObject.DOMAIN_ALIAS);
		WizardCreateDomainAlias wizard = (WizardCreateDomainAlias) app.zPageSearchResults
				.zToolbarPressButton(Button.B_TREE_EDIT);
		wizard.zSetTargetDomain(alias);

		// Verify the domain exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+	"<domain by='name'>" + domainAliasName + "</domain>"
				+	"</GetDomainRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode(
				"//admin:GetDomainResponse/admin:domain/admin:a[@n='zimbraMailCatchAllForwardingAddress']", 1);
		ZAssert.assertStringContains(response.toString(), ExecuteHarnessMain.zimbraServer,
				"Verify description is edited correctly");
	}
}