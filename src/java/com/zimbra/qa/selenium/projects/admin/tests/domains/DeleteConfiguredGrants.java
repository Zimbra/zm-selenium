/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
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
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.*;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;

public class DeleteConfiguredGrants extends AdminCommonTest {

	public DeleteConfiguredGrants() {
		logger.info("New " + DeleteConfiguredGrants.class.getCanonicalName());

		//All tests starts from domain page
		this.startingPage=app.zPageManageDomains;
	}

	/**
	 * Testcase : Delete ACE
	 * 1. Go to Manage domain View
	 * 2. Select an domain
	 * 3. Right click on domain and select Configure grants option
	 * 4. Delete grants
	 * 5. Verify ACE is deleted successfully
	 * @throws HarnessException
	 */
	@Test( description = "Verify delete Configure Grants option",
			groups = { "smoke", "L1" })
	public void DeleteACE_01() throws HarnessException {

		// Create grantee account
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(account);

		// Create target account
		AccountItem target_account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(target_account);

		// Add right
		String rightName="sendAs";
		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + domain.getName() + "</name>"
						+		"</CreateDomainRequest>");

		// Add right
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GrantRightRequest xmlns='urn:zimbraAdmin'>"
						+ "<target type='account' by='name'>"+ target_account.getEmailAddress() +"</target>"
						+ "<grantee type='dom' by='name'>"+domain.getName()+"</grantee>"
						+ "<right>"+rightName+"</right>"
						+ "</GrantRightRequest>");

		// Refresh the domain list
		app.zPageManageDomains.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on domain 
		app.zPageManageDomains.zListItem(Action.A_LEFTCLICK, domain.getName());

		// Click on Configure grants option
		app.zPageManageDomains.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_CONFIGURE_GRANTS);
		SleepUtil.sleepMedium();

		// Click on ACE to be deleted
		app.zPageManageConfigureGrants.zListItem(Action.A_LEFTCLICK, domain.getName());
		
		// Click on Delete button 
		app.zPageManageConfigureGrants.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);	
		
		// Click Yes in Confirmation dialog
		app.zPageManageConfigureGrants.zClickButton(Button.B_YES);
		
		// Verify ACE is deleted from UI
		ZAssert.assertFalse(app.zPageManageDomains.zVerifyACE(domain.getName()), "Verify ACE is deleted from UI");

		// Verify ACE is deleted using soap
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetGrantsRequest  xmlns='urn:zimbraAdmin'>"
						+ "<grantee type='dom' by='name' all='1'>"+ domain.getName() +"</grantee>"
						+ "</GetGrantsRequest >");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetGrantsResponse/admin:grant/admin:right", 1); 
		ZAssert.assertEquals(response, null, "Verify right is deleted without any error!");

	}

}
