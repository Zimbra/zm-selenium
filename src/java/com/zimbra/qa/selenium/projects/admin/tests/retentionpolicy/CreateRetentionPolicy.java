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
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.FormManageRetentionPolicy;

public class CreateRetentionPolicy extends AdminCore {

	public CreateRetentionPolicy() {
		logger.info("New "+ CreateRetentionPolicy.class.getCanonicalName());
	}


	/**
	 * Testcase : Create retention policy
	 * Steps :
	 * 1. Go to configure >> global settings >> retention policy >> Add new retention policy
	 * 2. Verify that new policy gets created successfully
	 * @throws HarnessException
	 */

	@Test (description = "Create retention policy",
			groups = { "bhr" })

	public void CreateRetentionPolicy_01() throws HarnessException {

		final String policyName = "test_policy" + ConfigProperties.getUniqueString();
		final String retentionRange = "3";

		// Verify navigation path - configure >> global settings >> retention policy
		app.zPageManageRetentionPolicy.zNavigateTo();

		SleepUtil.sleepMedium();
		FormManageRetentionPolicy form = (FormManageRetentionPolicy) app.zPageManageRetentionPolicy.zToolbarPressButton( Button.B_ADD);

		// Enter policy name
		form.zSetPolicyName(policyName);

		// Enter retention range
		form.zSetRetentionRange(retentionRange);

		// Select days from range drop-down
		form.selectRetentionRange(Button.O_DAYS);

		// Click on OK button
		form.sClickOkButton();

		// Get retention policy information
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetSystemRetentionPolicyRequest xmlns='urn:zimbraAdmin'>"
						+		"</GetSystemRetentionPolicyRequest>");

		// Verify the retention policy exists in the ZCS
		String response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:GetSystemRetentionPolicyResponse/mail:retentionPolicy/mail:keep/mail:policy", "name");
		ZAssert.assertNotNull(response, "Verify the retention policy is created successfully");

		// Verify policy exists on UI
        ZAssert.assertTrue(app.zPageManageRetentionPolicy.zVerifyPolicyName(policyName), "Policy displayed in current view");
	}
}
