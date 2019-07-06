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
package com.zimbra.qa.selenium.projects.admin.tests.adminextensions;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.DialogForUndeployAdminExtension;

public class UndeployAdminExtensions extends AdminCore {

	public UndeployAdminExtensions() {
		logger.info("New " + UndeployAdminExtensions.class.getCanonicalName());
		super.startingPage = app.zPageManageAdminExtensions;
	}


	/**
	 * Testcase : Undeploy admin extensions
	 * 1. Go to Configure > admin extensions
	 * 2. Select undeploy admin extensions from gear menu
	 * 3. Verify admin extension is undeployed and removed from Zimlet list
	 * @throws HarnessException
	 */

	@Test (description = "Deploy admin extension",
			groups = { "bhr" })

	public void UndeployAdminExtensions_01() throws HarnessException {

		String adminExtensionName = "de_dieploegers_admin_vacation";

		// Verify Admin extension is present in admin console
		boolean isAdminExtensionPresent = app.zPageManageAdminExtensions.zVerifyAdminExtensionName(adminExtensionName);

		if (!isAdminExtensionPresent) {
			throw new HarnessException("Admin Extension is not Present!");

		} else {
			// Click on admin extension
			app.zPageManageAdminExtensions.zListItem(Action.A_LEFTCLICK, adminExtensionName);

			// Click on undeploy from gear menu option
			DialogForUndeployAdminExtension dialog = (DialogForUndeployAdminExtension) app.zPageManageAdminExtensions
					.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_UNDEPLOY_ZIMLET);

			// Click Yes in Confirmation dialog
			dialog.zPressButton(Button.B_YES);

			// Verify the admin extension is not listed on zimlet page
			boolean isUndeploySuccessful = app.zPageManageAdminExtensions.zVerifyAdminExtensionName(adminExtensionName);
			ZAssert.assertFalse(isUndeploySuccessful, "Verify admin extension is undeployed!");
		}
	}
}