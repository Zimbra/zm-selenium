/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.zimlets;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;

public class ToggleStatus extends AdminCommonTest {
	public ToggleStatus() {
		logger.info("New "+ ToggleStatus.class.getCanonicalName());

		// All tests start at the "Rights" page
		super.startingPage = app.zPageManageZimlets;
	}

	/**
	 * Testcase : Verify administrator should be able to toggle zimlet status
	 * Steps :
	 * 1. Go to "Home > Configure > Zimlets"
	 * 2. Click on Zimlet > select toggle status from gear icon for zimlet - com_zimbra_webex
	 * 3. Verify status
	 * @throws HarnessException
	 */
	@Test( description = "Verify administrator should be able to toggle zimlet status",
			groups = { "smoke","L1" })
	public void EnableZimlet_01() throws HarnessException {
	
		String zimletName ="com_zimbra_webex";
		
		// Click on Zimlets
		app.zPageManageZimlets.zListItem(Action.A_LEFTCLICK, zimletName);
		
		// Toggle zimlet status
		app.zPageManageZimlets.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.B_TOGGLE_STATUS);
		
		// Verify zimlet is disabled
		boolean isPresent = app.zPageManageZimlets.zVerifyZimletIsDisabled(zimletName);
		ZAssert.assertTrue(isPresent, "Verify zimlet is disabled!");
	}

}
