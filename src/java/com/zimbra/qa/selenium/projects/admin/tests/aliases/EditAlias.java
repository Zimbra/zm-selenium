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
package com.zimbra.qa.selenium.projects.admin.tests.aliases;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.*;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;

public class EditAlias extends AdminCommonTest {

	public EditAlias() {
		logger.info("New " + EditAlias.class.getCanonicalName());
		super.startingPage=app.zPageManageAliases;
	}


	/**
	 * Testcase : Create a basic alias.
	 * 1. Create a alias with GUI.
	 * 2. Verify alias is created using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Edit a basic alias",
			groups = { "smoke", "L1" })

	public void EditAlias_01() throws HarnessException {

		AccountItem target = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(target);

		// Create a new account in the Admin Console using SOAP
		AliasItem alias = new AliasItem();
		String aliasEmailAddress=alias.getEmailAddress();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<AddAccountAliasRequest xmlns='urn:zimbraAdmin'>"
				+			"<id>" + target.getID() + "</id>"
				+			"<alias>" + aliasEmailAddress + "</alias>"
				+		"</AddAccountAliasRequest>");

		// Refresh the account list
		app.zPageManageAliases.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on alias to be edited.
		app.zPageManageAliases.zListItem(Action.A_LEFTCLICK, alias.getEmailAddress());

		// Verify the alias exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<account by='name'>"+ alias.getEmailAddress() +"</account>"
				+		"</GetAccountRequest>");
		String email = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:account", "name");
		ZAssert.assertEquals(email, target.getEmailAddress(), "Verify the alias is associated with the correct account");
	}


	/**
	 * Testcase : Bug 58191 - JavaScript error while clicking on alias of resource
	 * 1. Create a resource
	 * 2. Create an alias to resource
	 * 3. Click on newly created alias
	 * @throws HarnessException
	 */

	@Bugs (ids = "58191")
	@Test (description = "Bug 58191 - JavaScript error while clicking on alias of resource",
			groups = { "functional", "L2" })

	public void EditAlias_02() throws HarnessException {

		//Create calendar resource
		ResourceItem resource = new ResourceItem();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				 		+ "<name>" + resource.getEmailAddress() + "</name>"
				 		+ "<a n=\"displayName\">" + resource.getName() + "</a>"
				 		+ "<a n=\"zimbraCalResType\">" + "Location" + "</a>"
				 		+ "<password>test123</password>"
				 		+ "</CreateCalendarResourceRequest>");
		String resourceId = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:CreateCalendarResourceResponse/admin:calresource", "id").toString();

		// Create a new alias for resource
		AliasItem alias = new AliasItem();
		String aliasEmailAddress=alias.getEmailAddress();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<AddAccountAliasRequest xmlns='urn:zimbraAdmin'>"
				+			"<id>" + resourceId  + "</id>"
				+			"<alias>" + aliasEmailAddress + "</alias>"
				+		"</AddAccountAliasRequest>");

		// Refresh the account list
		app.zPageManageAliases.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on alias
		app.zPageManageAliases.zListItem(Action.A_LEFTCLICK, alias.getEmailAddress());

    	// Verify the alias is associated with the correct resource
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				+ 		"<calresource by='name'>" +  alias.getEmailAddress()  + "</calresource>"
				+		"</GetCalendarResourceRequest>");

		Element email = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource/admin:a[@n='zimbraMailAlias']", 1);
	    ZAssert.assertEquals(email.getText(), aliasEmailAddress, "Verify the alias is associated with the correct resource");
	}
}