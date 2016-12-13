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
package com.zimbra.qa.selenium.projects.admin.tests.rights;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;

public class ViewRights extends AdminCommonTest {
	public ViewRights() {
		logger.info("New "+ ViewRights.class.getCanonicalName());

		// All tests start at the "Rights" page
		super.startingPage = app.zPageManageRights;
	}

	/**
	 * Testcase : View Rights
	 * Steps :
	 * 1. Go to "Home > Configure > Rights"
	 * 2. Click on Right > select View from gear icon
	 * 3. View description 
	 * @throws HarnessException
	 */
	@Test( description = "View Right",
			groups = { "smoke","L1" })
	public void ViewRights_01() throws HarnessException {
	
		String rightName = "accessGAL";
		String rightDesc = "access GAL(global address list)";
		String rightType = "preset";
		String targetType = "domain";

		// Click on Right
		app.zPageManageRights.zListItem(Action.A_LEFTCLICK, rightName);

		// Click on View
		app.zPageManageRights.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.B_VIEW);
		
		// Verify information related to right is displayed correctly
		ZAssert.assertTrue(app.zPageManageRights.zVerifyRightName(rightName), "Verify right name!");
		ZAssert.assertTrue(app.zPageManageRights.zVerifyRightrightDescription(rightDesc), "Verify right description!");
		ZAssert.assertTrue(app.zPageManageRights.zVerifyRightrightType(rightType), "Verify right type!");
		ZAssert.assertTrue(app.zPageManageRights.zVerifyRightTargetType(targetType), "Verify target type!");
	}
}
