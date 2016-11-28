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
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.*;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageACL;
import com.zimbra.qa.selenium.projects.admin.ui.WizardAddACL;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageDomains;

public class AddACL extends AdminCommonTest {

	public AddACL() {
		logger.info("New " + AddACL.class.getCanonicalName());

		// All tests start at the "Domains" page
		this.startingPage=app.zPageManageDomains;
	}

	/**
	 * Testcase : Add ACL to domain
	 * 1. Go to Manage domain View
	 * 2. Select domain
	 * 3. Edit domain using edit button in Gear box menu.
	 * 4. Verify ACL is added 
	 * @throws HarnessException
	 */
	@Test( description = "Add ACL to domain",
			groups = { "smoke" })
	public void AddACL_01() throws HarnessException {

		// Create grantee account
		AccountItem grantee = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(grantee);

		String rightName="createDistList";
		AclItem acl = new AclItem();
		acl.setRightName(rightName);

		// Set grantee account email address
		acl.setGranteeAccountEmail(grantee.getEmailAddress());

		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();
		String domainName=domain.getName();
	
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + domainName + "</name>"
				+		"</CreateDomainRequest>");
	
	
		// Refresh the domain list
		app.zPageManageDomains.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");
	
		// Click on account to be edited.
		app.zPageManageDomains.zListItem(Action.A_LEFTCLICK, domain.getName());
		app.zPageManageDomains.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Click on ACL tab
		app.zPageManageDomains.zClickAt(PageManageDomains.Locators.DOMAIN_EDIT_ACL,"");

		// Click on Add ACL
		WizardAddACL wizard = 
				(WizardAddACL)app.zPageManageACL.zToolbarPressButton(Button.B_ADD_ACL_AT_DOMAIN);
		
		
		// Fill out the wizard	
		wizard.zCompleteWizard(acl);

		// Verify added ACL is displayed on UI 
		ZAssert.assertTrue(app.zPageManageACL.sIsElementPresent(PageManageACL.Locators.GRANTED_ACL), "Verify general tab is displayed");

		// Verify right is granted
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CheckRightRequest xmlns='urn:zimbraAdmin'>"
						+ "<target type='domain' by='name'>"+ domainName+"</target>"
						+ "<grantee type='usr' by='name'>"+ grantee.getEmailAddress()+"</grantee>"
						+ "<right>"+rightName+"</right>"
						+ "</CheckRightRequest>");
		String response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:CheckRightResponse", "allow").toString();
		ZAssert.assertEquals(response, "1", "Verify right is granted!");

	}

}
