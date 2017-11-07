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
package com.zimbra.qa.selenium.projects.admin.tests.globalsettings;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageGlobalSettings;

public class VerifyMaximumSizeOfaFileUpload extends AdminCommonTest {
	public VerifyMaximumSizeOfaFileUpload() {
		logger.info("New "+ VerifyMaximumSizeOfaFileUpload.class.getCanonicalName());

		// All tests start at the "Cos" page
		super.startingPage = app.zPageManageGlobalSettings;
	}
	
	/**
	 * Testcase :  zimbraFileUploadMaxSize does not restrict to upload attachment exceeding the value
	 * Steps :
	 * 1. Go to Accounts
	 * 2. Verify navigation path -- "Home --> Configure --> Global Settings" --> Verify fileUploadMaxSize field
	 * @throws HarnessException
	 */
	@Bugs( ids = "81323")
	@Test (description = "Verify field text in Global Settings > General Information = Maximum size of a file uploaded from the desktop (KB):",
			groups = { "functional", "L2" })
			public void VerifyMaximumSizeOfaFileUpload_01() throws HarnessException {
		
		String fileUploadMaxSizeLabel = "Maximum size of a file uploaded from the desktop (KB):";
		String default_value = "10240";
		
		// Verify label "Maximum size of a file uploaded from the desktop (KB):"
		String check_label_for_fileUploadMaxSizeLabel= app.zPageManageGlobalSettings.sGetText(PageManageGlobalSettings.Locators.ZIMBRA_FILE_UPLOAD_MAX_SIZE);	
		ZAssert.assertStringContains(check_label_for_fileUploadMaxSizeLabel, fileUploadMaxSizeLabel,"Verify label is correct!");
		
		// Verify default value for fileUploadMaxSize field
		String check_default_value= app.zPageManageGlobalSettings.sGetValue(PageManageGlobalSettings.Locators.ZIMBRA_FILE_UPLOAD_MAX_SIZE_TEXT_FIELD);
		ZAssert.assertStringContains(check_default_value, default_value,"Verify default value is correct!");
		
		// Verify that system accepts updated value for fileUploadMaxSize field
	    app.zPageManageGlobalSettings.sType(PageManageGlobalSettings.Locators.ZIMBRA_FILE_UPLOAD_MAX_SIZE_TEXT_FIELD, "10241");
		String check_updated_value= app.zPageManageGlobalSettings.sGetValue(PageManageGlobalSettings.Locators.ZIMBRA_FILE_UPLOAD_MAX_SIZE_TEXT_FIELD);
		ZAssert.assertStringContains(check_updated_value, "10241" ,"Verify updated value is correct!");
			
	}

}
