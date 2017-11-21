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
package com.zimbra.qa.selenium.projects.admin.tests.zimlets;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FileItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.WizardDeployZimlet;
import com.zimbra.qa.selenium.projects.admin.pages.WizardDeployZimlet.Locators;

public class DeployZimlet extends AdminCore {

	public DeployZimlet() {
		logger.info("New " + DeployZimlet.class.getCanonicalName());
		super.startingPage = app.zPageManageZimlets;
	}


	/**
	 * Testcase : Deploy Zimlet
	 * 1. Go to Configure > Zimlet
	 * 2. Select deploy zimlet from gear menu
	 * 3. Verify zimlet is deployed and listed correctly
	 * @throws HarnessException
	 */

	@Test (description = "Deploy Zimlet",
			groups = { "smoke", "L1" })

	public void DeployZimlet_01() throws HarnessException {

		// Create file item
		final String fileName = "com_zimbra_dnd.zip";
		String zimletName ="com_zimbra_dnd";
		final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\zimlet\\" + fileName;
		FileItem fileItem = new FileItem(filePath);

		// Click on deploy zimlet
		WizardDeployZimlet wizard= (WizardDeployZimlet)app.zPageManageZimlets.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.B_DEPLOY_ZIMLET);

		// Click on Upload File button
		app.zPageManageZimlets.zToolbarPressButton(Button.B_UPLOAD_ZIMLET, fileItem);

		// Upload zimlet
		zUpload(filePath);

		// Check flush chache
		wizard.zCheckboxSet(Locators.FLUSH_CHACHE,true);
		SleepUtil.sleepMedium();

		// Click on Deploy
		wizard.sClickAt(Locators.DEPLOY_BUTTON,"");
		SleepUtil.sleepMedium();

		// Verify zimlet is uploaded successfully
		boolean isUploadSuccessful = app.zPageManageZimlets.zVerifyUploadSuccessMessage();
		ZAssert.assertTrue(isUploadSuccessful, "Verify zimlet is uploaded successfully!");

		// Verify zimlet is deployed successfully
		boolean isPresent = app.zPageManageZimlets.zVerifyDeploySuccessMessage();
		ZAssert.assertTrue(isPresent, "Verify zimlet is deployed successfully!");

		// Click on Finish
		wizard.sClickAt(Locators.FINISH_BUTTON,"");
		SleepUtil.sleepMedium();

		// Verify the zimlet is listed on zimlet page
		boolean isDeploySuccessful = app.zPageManageZimlets.zVerifyZimletName(zimletName);
		ZAssert.assertTrue(isDeploySuccessful, "Verify zimlet is deployed!");
	}
}