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
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FileItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.WizardDeployZimlet;
import com.zimbra.qa.selenium.projects.admin.pages.WizardDeployZimlet.Locators;

public class DeployAdminExtensions extends AdminCore {

	public DeployAdminExtensions() {
		logger.info("New " + DeployAdminExtensions.class.getCanonicalName());
		super.startingPage = app.zPageManageAdminExtensions;
	}


	/**
	 * Testcase : Deploy admin extension
	 * 1. Go to configure > admin extensions
	 * 2. Select deploy option from gear menu > deploy extension
	 * 3. Verify extension is listed under admin extensions
	 * @throws HarnessException
	 */

	@Bugs (ids = "ZCS-1059")
	@Test (description = "Deploy admin extension",
			groups = { "bhr" })

	public void DeployAdminExtension_01() throws HarnessException {

		// Create file item
		final String fileName = "de_dieploegers_admin_vacation.zip";
		String zimletName ="de_dieploegers_admin_vacation";
		final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\adminextension\\" + fileName;
		FileItem fileItem = new FileItem(filePath);

		// Click on deploy admin extension
		WizardDeployZimlet wizard= (WizardDeployZimlet)app.zPageManageZimlets.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.B_DEPLOY_ZIMLET);

		// Click on Upload File button
		app.zPageManageZimlets.zToolbarPressButton(Button.B_UPLOAD_ZIMLET, fileItem);

		// Upload admin extension
		zUpload(filePath);

		// Click on flush cache
		wizard.zCheckboxSet(Locators.FLUSH_CHACHE,true);
		SleepUtil.sleepMedium();

		// Click on Deploy
		wizard.sClickAt(Locators.DEPLOY_BUTTON,"");
		SleepUtil.sleepMedium();

		// Verify extension is uploaded successfully
		boolean isUploadSuccessful = app.zPageManageZimlets.zVerifyUploadSuccessMessage();
		ZAssert.assertTrue(isUploadSuccessful, "Verify extension is uploaded successfully!");

		// Verify extension is deployed successfully
		boolean isPresent = app.zPageManageZimlets.zVerifyDeploySuccessMessage();
		ZAssert.assertTrue(isPresent, "Verify extension is delployed successfully!");

		// Click on Finish
		wizard.sClickAt(Locators.FINISH_BUTTON,"");
		SleepUtil.sleepMedium();

		// Verify extension is deployed
		boolean isDeploySuccessful = app.zPageManageAdminExtensions.zVerifyAdminExtensionName(zimletName);
		ZAssert.assertTrue(isDeploySuccessful, "Verify extension is deployed!");
	}
}