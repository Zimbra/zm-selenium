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
import com.zimbra.qa.selenium.projects.admin.ui.DialogForDeleteOperationACL;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageACLAtDL;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageDomains;

public class DeleteACL extends AdminCommonTest {

	public DeleteACL() {
		logger.info("New " + DeleteACL.class.getCanonicalName());

		// All tests start at the "Domains" page
		this.startingPage=app.zPageManageDomains;
	}

	/**
	 * Testcase : Delete ACL
	 * 1. Go to Manage domain View
	 * 2. Select DL
	 * 3. Edit an domain using edit button in Gear box menu > Delete ACL
	 * 4. Verify ACL is Deleted 
	 * @throws HarnessException
	 */
	@Test(	description = "Delete ACL",
			groups = { "smoke", "L1" })
	public void DeleteACL_01() throws HarnessException {

		// Create grantee account
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(account);

		String rightName="sendToDistList";
		String granteeType="usr";
		AclItem acl = new AclItem();
		acl.setGranteeType(granteeType);

		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();
		String domainName=domain.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + domainName + "</name>"
						+		"</CreateDomainRequest>");

		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<GrantRightRequest xmlns='urn:zimbraAdmin'>" 
						+	"<target  by='name' type='domain'>" + domainName + "</target>"
						+	"<grantee  by='name' type='usr'>" + account.getEmailAddress() + "</grantee>" 
						+	"<right>"+ rightName + "</right>" 
						+ "</GrantRightRequest>");

		// Set grantee account email address
		acl.setGranteeAccountEmail(account.getEmailAddress());

		// Refresh the domain list
		app.zPageManageDomains.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on domain to be edited.
		app.zPageManageDomains.zListItem(Action.A_LEFTCLICK, domain.getName());
		
		app.zPageManageDomains.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Click on ACL tab
		app.zPageManageDomains.zClickAt(PageManageDomains.Locators.DOMAIN_EDIT_ACL,"");

		//Click on granted ACL
		app.zPageManageACLAtDL.zClickAt(PageManageACLAtDL.Locators.GRANTED_ACL,"");

		// Click on Delete button
		DialogForDeleteOperationACL dialog = (DialogForDeleteOperationACL) app.zPageManageDomains.zToolbarPressButton(Button.B_DELETE_ACL);

		// Click Yes in Confirmation dialog
		dialog.zClickButton(Button.B_YES);

		// Verify ACL is deleted
		ZAssert.assertFalse(app.zPageManageACLAtDL.zVerifyACL(rightName), "Verfiy right name is returned in search result");
	}
}
