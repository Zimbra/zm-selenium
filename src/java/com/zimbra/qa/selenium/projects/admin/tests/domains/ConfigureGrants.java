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
import com.zimbra.qa.selenium.projects.admin.ui.WizardConfigureGrants;
import com.zimbra.qa.selenium.projects.admin.ui.WizardConfigureGrants.Locators;

public class ConfigureGrants extends AdminCommonTest {

	public ConfigureGrants() {
		logger.info("New " + ConfigureGrants.class.getCanonicalName());

		//All tests starts from domain page
		this.startingPage=app.zPageManageDomains;
	}

	/**
	 * Testcase : Add ACE to Domain 
	 * 1. Go to Manage domain View
	 * 2. Select domain
	 * 3. Right click on domain and select Configure grants option
	 * 4. Click on gear icon and select Add > Enter details > Click on 'Add and Finish'  
	 * 5. Verify ACE is added successfully
	 * @throws HarnessException
	 */
	@Test( description = "Verify Configure Grants option by selecting it from right click menu option",
			groups = { "smoke", "L0" })
	public void AddACE_01() throws HarnessException {

		// Create grantee account
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(account);

		// Create target account
		AccountItem target_account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(target_account);

		String rightName="sendAs";
		AclItem acl = new AclItem();
		acl.setRightName(rightName);

		// Set target account email address
		acl.setTargetAccountEmail(target_account.getEmailAddress());
		
		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + domain.getName() + "</name>"
				+		"</CreateDomainRequest>");
	
		
		// Refresh the list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on domain to be edited
		app.zPageManageDomains.zListItem(Action.A_RIGHTCLICK, domain.getName());

		// Click on Configure grants
		app.zPageManageDomains.zToolbarPressButton(Button.B_CONFIGURE_GRANTS);
		SleepUtil.sleepMedium();

		// Click on Add
		WizardConfigureGrants wizard= (WizardConfigureGrants) app.zPageManageConfigureGrants.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_ADD);

		// Select target type
		wizard.selectTargetType(Locators.ACCOUNT);

		// Fill out the wizard	
		wizard.zCompleteWizard(acl);

		// Click on "Add and Finish"
		wizard.zCloseWizard();
		SleepUtil.sleepMedium();
		
		// Verify ACE is displayed on UI
		ZAssert.assertTrue(app.zPageManageDomains.zVerifyACE(domain.getName()), "Verify ACE is displayed on UI");

		// Verify grants using soap
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetGrantsRequest  xmlns='urn:zimbraAdmin'>"
						+ "<grantee type='dom' by='name' all='1'>"+ domain.getName() +"</grantee>"
						+ "</GetGrantsRequest >");
		
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetGrantsResponse/admin:grant/admin:right", 1); 
		ZAssert.assertStringContains(response.getText(), rightName, "Verify right is granted!");
		app.zPageMain.logout();

	}
}
