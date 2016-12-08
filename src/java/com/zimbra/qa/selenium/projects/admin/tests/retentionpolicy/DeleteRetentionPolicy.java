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
package com.zimbra.qa.selenium.projects.admin.tests.retentionpolicy;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;

public class DeleteRetentionPolicy extends AdminCommonTest {

	public DeleteRetentionPolicy() {
		logger.info("New "+ DeleteRetentionPolicy.class.getCanonicalName());
	}

	/**
	 * Testcase : Create retention policy
	 * Steps :
	 * 1. Go to configure >> global settings >> retention policy >> Add new retention policy 
	 * 2. Verify that new policy gets created successfully 
	 * @throws HarnessException
	 */

	@Test(	description = "Delete retention policy",
			groups = { "smoke", "L1" })
	public void DeleteRetentionPolicy_01() throws HarnessException {

		final String policyName = "test_policy" + ConfigProperties.getUniqueString();
		final String retentionRange = "3d";

		// Create retention policy
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateSystemRetentionPolicyRequest xmlns='urn:zimbraAdmin'>"
						+ "<keep>"
						+ "<policy name='"+policyName+"' lifetime='"+retentionRange+"' xmlns='urn:zimbraMail'> </policy>"
						+ "</keep>"
						+ "</CreateSystemRetentionPolicyRequest>");


		// Refresh the retention list
		app.zPageManageRetentionPolicy.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Verify navigation path - configure >> global settings >> retention policy
		app.zPageManageRetentionPolicy.zNavigateTo();

		// Click on account to be Edited.
		app.zPageManageRetentionPolicy.zListItem(Action.A_LEFTCLICK, policyName);

		// Click on delete
		app.zPageManageRetentionPolicy.zToolbarPressButton( Button.B_DELETE);

		// Click Yes in Confirmation dialog
		app.zPageManageRetentionPolicy.zClickButton(Button.B_YES);
		SleepUtil.sleepMedium();

		// Verify policy not exists on UI
		ZAssert.assertFalse(app.zPageManageRetentionPolicy.zVerifyPolicyName(policyName), "Policy not displayed on UI ");
	}
}

