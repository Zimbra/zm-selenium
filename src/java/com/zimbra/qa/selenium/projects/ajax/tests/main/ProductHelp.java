/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.main;


import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.*;


public class ProductHelp extends AjaxCommonTest {

	public ProductHelp() {
		logger.info("New "+ ProductHelp.class.getCanonicalName());
		
		
		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = null;

		
	}
	
	@Test(	description = "Open 'Product Help'",
			groups = { "functional" })
	public void ProductHelp_01() throws HarnessException {
		
		//-- DATA


		//-- GUI
		
		SeparateWindow window = null;
		
		try {
			
			// Click the Account -> Product Help
			window = (SeparateWindow)app.zPageMain.zToolbarPressPulldown(Button.B_ACCOUNT, Button.O_PRODUCT_HELP);
			window.zWaitForActive(); // Make sure the window is there
			ZAssert.assertTrue(window.zIsActive(), "Verify the Product Help dialog opens");

		} finally {
			window.zCloseWindow();
			window = null;
		}

		//-- VERIFICATION
		

	}


}
