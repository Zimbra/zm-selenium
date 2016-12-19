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
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.DialogForUndeployZimlet;

public class UndeployZimlet extends AdminCommonTest {

	public UndeployZimlet() {
		logger.info("New " + UndeployZimlet.class.getCanonicalName());

		// All tests start at the "zimlet" page
		super.startingPage = app.zPageManageZimlets;
	}

	/**
	 * Testcase : Undeploy Zimlet
	 * 1. Go to Configure > Zimlet
	 * 2. Select undeploy zimlet option from gear menu 
	 * 3. Verify zimlet is undeployed and removed from Zimlet list
	 * @throws HarnessException
	 */
	@Test(	description = "Deploy Zimlet",
			groups = { "functional", "L1" })

	public void UndeployZimlet_01() throws HarnessException {

		String zimletName ="com_zimbra_dnd";
		
		// Verify the zimlet is present on zimlet page
		boolean isZimletPresent = app.zPageManageZimlets.zVerifyZimletName(zimletName);
		if ( !isZimletPresent ) {
			throw new HarnessException("Zimlet is not Present!");
		}
		else
		{
		// Click on Zimlets
		app.zPageManageZimlets.zListItem(Action.A_LEFTCLICK, zimletName);
		
		// Click on undeploy from gear menu option
		DialogForUndeployZimlet dialog = (DialogForUndeployZimlet) app.zPageManageZimlets.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.B_UNDEPLOY_ZIMLET);
		
		// Click Yes in Confirmation dialog
		dialog.zClickButton(Button.B_YES);
		
		// Verify the zimlet is not listed on zimlet page
		boolean isUndeploySuccessful = app.zPageManageZimlets.zVerifyZimletName(zimletName);
		ZAssert.assertFalse(isUndeploySuccessful, "Verify zimlet is undeployed!");
		}
	}

}
