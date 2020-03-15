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
import com.zimbra.qa.selenium.projects.admin.pages.DialogForDeleteOperationDomain;

public class DeleteDomain extends AdminCore {

	public DeleteDomain() {
		logger.info("New" + DeleteDomain.class.getCanonicalName());
		this.startingPage=app.zPageManageDomains;
	}


	@Test (description = "Verify delete domain operation --  Manage Domain List View",
			groups = { "bhr" })

	public void DeleteDomain_01() throws HarnessException {
		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + domain.getName() + "</name>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateDomainRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on account to be deleted.
		app.zPageManageDomains.zListItem(Action.A_LEFTCLICK, domain.getName());

		// Click on Delete button
		DialogForDeleteOperationDomain dialog = (DialogForDeleteOperationDomain) app.zPageManageDomains.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zPressButton(Button.B_YES);

		// Verify the domain do not exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+	"<domain by='name'>" + domain.getName() + "</domain>"
				+	"</GetDomainRequest>");


		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDomainResponse/admin:domain", 1);
		ZAssert.assertNull(response, "Verify the domain is deleted successfully");
	}


	@Test (description = "Verify delete domain operation",
			groups = { "sanity" })

	public void DeleteDomain_02() throws HarnessException {
		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + domain.getName() + "</name>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateDomainRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on account to be deleted.
		app.zPageManageDomains.zListItem(Action.A_RIGHTCLICK, domain.getName());

		// Click on Delete button
		DialogForDeleteOperationDomain dialog = (DialogForDeleteOperationDomain) app.zPageManageDomains.zToolbarPressButton((Button.B_TREE_DELETE));

		// Click Yes in Confirmation dialog
		dialog.zPressButton(Button.B_YES);

		// Verify the domain do not exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+	"<domain by='name'>" + domain.getName() + "</domain>"
				+	"</GetDomainRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDomainResponse/admin:domain", 1);
		ZAssert.assertNull(response, "Verify the domain is deleted successfully");
	}


	@Test (description = "Verify delete domain alias operation - Manage Domain list view",
			groups = { "sanity" })

	public void DeleteDomain_03() throws HarnessException {
		String targetDomain = ConfigProperties.getStringProperty("testdomain");
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+	"<domain by='name'>" + targetDomain + "</domain>"
				+	"</GetDomainRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		String targetDomainID=ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:GetDomainResponse/admin:domain", "id").toString();

		// Create a new domain alias in the Admin Console using SOAP
		DomainItem alias = new DomainItem();
		String domainAliasName=alias.getName();

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

		// Click on account to be deleted.
		app.zPageManageDomains.zListItem(Action.A_LEFTCLICK, domainAliasName);


		// Click on Delete button
		DialogForDeleteOperationDomain dialog = (DialogForDeleteOperationDomain) app.zPageManageDomains.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zPressButton(Button.B_YES);

		// Verify the domain do not exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+	"<domain by='name'>" + domainAliasName + "</domain>"
				+	"</GetDomainRequest>");


		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDomainResponse/admin:domain", 1);
		ZAssert.assertNull(response, "Verify the domain is deleted successfully");
	}


	@Test (description = "Verify delete domain alias operation - Manage Domain list view",
			groups = { "sanity" })

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
				+ "<a n='zimbraMailCatchAllAddress'>@"+domainAliasName+"</a>"
				+ "<a n='zimbraMailCatchAllForwardingAddress'>@"+targetDomain+"</a>"
				+ "<a n='description'>Created by Selenium automation</a>"
				+ "</CreateDomainRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on account to be deleted.
		app.zPageManageDomains.zListItem(Action.A_RIGHTCLICK, domainAliasName);

		// Click on Delete button
		DialogForDeleteOperationDomain dialog = (DialogForDeleteOperationDomain) app.zPageManageDomains.zToolbarPressButton(Button.B_TREE_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zPressButton(Button.B_YES);

		// Verify the domain do not exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDomainRequest xmlns='urn:zimbraAdmin'>"
				+	"<domain by='name'>" + domainAliasName + "</domain>"
				+	"</GetDomainRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDomainResponse/admin:domain", 1);
		ZAssert.assertNull(response, "Verify the domain is deleted successfully");
	}
}