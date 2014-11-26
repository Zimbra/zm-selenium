/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.topmenu.links;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;


public class LoggedInUsername extends AdminCommonTest {
	
	public LoggedInUsername() {
		logger.info("New "+ LoggedInUsername.class.getCanonicalName());
		
		// Use default starting page and starting account
	}
	
	@Test(	description = "Verify the Top Menu displays the correct Admin username",
			groups = { "smoke" })
	public void TopMenu_LoggedInUsername_01() throws HarnessException {
		
		// The displayed name is part of the full email address.  
		// For Example: 
		// Displayed:    globaladmin1284592683
		// Actual Email: globaladmin12845926837811@qa62.lab.zimbra.com
		//

		// Check that the displayed name is contained in the email
		String displayed = app.zPageMain.sGetText(PageMain.Locators.zSkinContainerUsername);
		ZimbraAccount actual = app.zGetActiveAccount();
		ZAssert.assertStringContains(actual.EmailAddress, displayed.split("@")[0], "Verify the correct account display name is shown");
		
	}


}
