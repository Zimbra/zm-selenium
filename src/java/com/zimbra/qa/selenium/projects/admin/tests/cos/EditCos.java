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
package com.zimbra.qa.selenium.projects.admin.tests.cos;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.CosItem;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditCos;
import com.zimbra.qa.selenium.projects.admin.pages.PageSearchResults;

public class EditCos extends AdminCore {

	public EditCos() {
		logger.info("New "+ EditCos.class.getCanonicalName());
		super.startingPage = app.zPageManageCOS;
	}


	@Test (description = "Edit Cos name  - Manage Cos View",
			groups = { "sanity" })

	public void EditCos_01() throws HarnessException {
		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + cosName + "</name>"
						+		"</CreateCosRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on account to be deleted.
		app.zPageManageCOS.zListItem(Action.A_LEFTCLICK, cosName);

		// Click on Edit button
		FormEditCos form = (FormEditCos) app.zPageManageCOS.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// Edit the name
		String editedName = "tceditedcos" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
		form.zSubmit();

		// Verify the cos exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
						"<cos by='name'>"+editedName+"</cos>"+
				"</GetCosRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos", 1);
		ZAssert.assertNotNull(response, "Verify the cos is edited successfully");
	}


	@Test (description = "Edit cos name -- right click",
			groups = { "sanity" })

	public void EditCos_02() throws HarnessException {
		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + cosName + "</name>"
						+		"</CreateCosRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on account to be deleted.
		app.zPageManageCOS.zListItem(Action.A_RIGHTCLICK, cosName);

		// Click on Edit button
		FormEditCos form = (FormEditCos) app.zPageManageCOS.zToolbarPressButton(Button.B_TREE_EDIT);

		// Click on General Information tab
		form.zSelectTreeItem(FormEditCos.TreeItem.GENERAL_INFORMATION);

		// Edit the name
		String editedName = "tceditedcos" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
		form.zSubmit();

		// Verify the cos exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
						"<cos by='name'>"+editedName+"</cos>"+
				"</GetCosRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos", 1);
		ZAssert.assertNotNull(response, "https://bugzilla.zimbra.com/show_bug.cgi?id=79304");
	}


	@Test (description = "Edit Cos name  - Search Cos View",
			groups = { "functional" })

	public void EditCos_03() throws HarnessException {
		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + cosName + "</name>"
						+		"</CreateCosRequest>");

		// Enter the search string to find the account
		app.zPageSearchResults.zSetType(PageSearchResults.TypeOfObject.COS);
		app.zPageSearchResults.zAddSearchQuery(cosName);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on cos
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, cos.getName());

		// Click on Edit button
		app.zPageSearchResults.zSetType(PageSearchResults.TypeOfObject.COS);
		FormEditCos form = (FormEditCos) app.zPageSearchResults.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// Click on General Information tab
		form.zSelectTreeItem(FormEditCos.TreeItem.GENERAL_INFORMATION);

		// Edit the name
		String editedName = "tceditedcos" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
		form.zSubmit();

		// Verify the cos exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
						"<cos by='name'>"+editedName+"</cos>"+
				"</GetCosRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos", 1);
		ZAssert.assertNotNull(response, "Verify the cos is edited successfully");
		app.zPageMain.zLogout();
	}


	@Test (description = "Edit cos name -- right click",
			groups = { "functional" })

	public void EditCos_04() throws HarnessException {
		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + cosName + "</name>"
						+		"</CreateCosRequest>");

		app.zPageSearchResults.zSetType(PageSearchResults.TypeOfObject.COS);

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(cosName);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on cos
		app.zPageSearchResults.zListItem(Action.A_RIGHTCLICK, cos.getName());

		// Click on Edit button
		FormEditCos form = (FormEditCos) app.zPageSearchResults.zToolbarPressButton(Button.B_TREE_EDIT);

		// Edit the name
		String editedName = "tceditedcos" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
		form.zSubmit();

		// Verify the cos exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
						"<cos by='name'>"+editedName+"</cos>"+
				"</GetCosRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos", 1);
		ZAssert.assertNotNull(response, "Verify the cos is edited successfully");
	}
}