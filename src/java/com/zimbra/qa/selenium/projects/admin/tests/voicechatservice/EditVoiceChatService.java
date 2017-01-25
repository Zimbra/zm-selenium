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
package com.zimbra.qa.selenium.projects.admin.tests.voicechatservice;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.FormEditVoiceChatService;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;

public class EditVoiceChatService extends AdminCommonTest {

	public EditVoiceChatService() {
		logger.info("New "+ EditVoiceChatService.class.getCanonicalName());

		super.startingPage = app.zPageManageVoiceChatService;
	}


	/**
	 * Testcase :  Edit Voice/Chat Services 
	 * Steps :
	 * 1. Go to "Home --> Configure --> Voice/Chat Services"
	 * 2. Edit cisco Voice/Chat Service > Save
	 * 3. Verify service is edited successfully
	 * @throws HarnessException
	 */
	@Test( description = "Edit Voice/Chat Service",
			groups = { "smoke", "L1" })

	public void EditVoiceChatService_01() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		String DisplayName = "service"+ ConfigProperties.getUniqueString();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateUCServiceRequest xmlns='urn:zimbraAdmin'>"
						+ "<name>" + DisplayName + "</name>"
						+ "<a n='zimbraUCProvider'>cisco</a>" 
						+ "<a n='cn'>" + DisplayName +"</a>" 
						+ "</CreateUCServiceRequest>");

		// Refresh the account list
		app.zPageManageVoiceChatService.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on service to be Edited.
		app.zPageManageVoiceChatService.zListItem(Action.A_LEFTCLICK, DisplayName);

		// Click on Edit button
		FormEditVoiceChatService form = (FormEditVoiceChatService) app.zPageManageVoiceChatService.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Edit the name.
		String editedName = "editedService_" + ConfigProperties.getUniqueString();
		form.setName(editedName);

		//Submit the form.
		form.zSubmit();

		// Verify voice/chat service is edited successfully
		ZAssert.assertTrue(app.zPageManageVoiceChatService.zVerifySearchResult(editedName), "Verfiy edited display name is returned in search result");

	}

}
