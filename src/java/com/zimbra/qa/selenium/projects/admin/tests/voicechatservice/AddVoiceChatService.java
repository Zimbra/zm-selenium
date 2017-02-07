/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.VoiceChatServiceItem;
import com.zimbra.qa.selenium.projects.admin.ui.WizardAddVoiceChatService;

public class AddVoiceChatService extends AdminCommonTest {
	public AddVoiceChatService() {
		logger.info("New "+ AddVoiceChatService.class.getCanonicalName());


		super.startingPage = app.zPageManageVoiceChatService;
	}

	/**
	 * Testcase : Add Voice/Chat Services 
	 * Steps :
	 * 1. Go to "Home --> Configure --> Voice/Chat Services"
	 * 2. Add cisco Voice/Chat Service > Save
	 * 3. Verify service is added successfully
	 * @throws HarnessException
	 */
	@Test( description = "Navigate to Voice/Chat Services",
			groups = { "smoke", "L1" })
	public void AddVoiceChatService_01() throws HarnessException {

		String DisplayName = "service"+ ConfigProperties.getUniqueString();
		VoiceChatServiceItem item = new VoiceChatServiceItem();
		item.setDisplayName(DisplayName);

		// Click "New"
		WizardAddVoiceChatService wizard = 
				(WizardAddVoiceChatService)app.zPageManageVoiceChatService.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.O_NEW);

		// Fill out the wizard	
		wizard.zCompleteWizard(item);

		// Verify voice/chat service is created successfully
		ZAssert.assertTrue(app.zPageManageVoiceChatService.zVerifySearchResult(DisplayName), "Verfiy display name is returned in search result");


	}

}
