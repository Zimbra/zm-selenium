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
package com.zimbra.qa.selenium.projects.admin.tests.voicechatservice;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.DialogForDeleteOperationACL;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;

public class DeleteVoiceChatServices extends AdminCommonTest {

	public DeleteVoiceChatServices() {
		logger.info("New " + DeleteVoiceChatServices.class.getCanonicalName());
		super.startingPage = app.zPageManageVoiceChatService;
	}

	/**
	 * Testcase : Delete Voice/Chat Services
	 * Steps :
	 * 1. Go to "Home --> Configure --> Voice/Chat Services"
	 * 2. Delete cisco Voice/Chat Service 
	 * 3. Verify service is deleted successfully
	 * @throws HarnessException
	 */
	@Test(	description = "Delete ACL",
			groups = { "smoke", "L1" })
	public void DelDeleteVoiceChatService_01() throws HarnessException {


		// Create a new account in the Admin Console using SOAP
		String DisplayName = "service"+ ConfigProperties.getUniqueString();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateUCServiceRequest xmlns='urn:zimbraAdmin'>"
						+ "<name>" + DisplayName + "</name>"
						+ "<a n='zimbraUCProvider'>cisco</a>" 
						+ "<a n='cn'>" + DisplayName +"</a>" 
						+ "</CreateUCServiceRequest>");

		// Refresh the service list
		app.zPageManageVoiceChatService.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on service to be deleted
		app.zPageManageVoiceChatService.zListItem(Action.A_LEFTCLICK, DisplayName);

		// Click on Delete button
		DialogForDeleteOperationACL dialog = (DialogForDeleteOperationACL) app.zPageManageVoiceChatService.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);
		// Click Yes in Confirmation dialog
		dialog.zClickButton(Button.B_YES);

		// Verify voice/chat service is deleted successfully
		ZAssert.assertFalse(app.zPageManageVoiceChatService.zVerifySearchResult(DisplayName), "Verfiy display name is not returned in search result");
	}
}
