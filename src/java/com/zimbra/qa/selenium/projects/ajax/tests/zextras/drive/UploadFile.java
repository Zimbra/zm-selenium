/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.zextras.drive;

import org.openqa.selenium.Keys;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FileItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.drive.DialogUploadFile;;

public class UploadFile extends AjaxCore{

	public UploadFile() throws HarnessException {
		logger.info("New " + UploadFile.class.getCanonicalName());
		super.startingPage = app.zPageDrive;
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox","TRUE");
	}

	@Test (description = "Upload file through UI and verify it",
			groups = { "sanity", "L0", "upload", "drive","test" })

	public void UploadFile_01() throws HarnessException {

		try {
			// Create file item
			final String fileName = "testtextfile.txt";
			final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;
			FileItem fileItem = new FileItem(filePath);

			// Click on Upload File button in the Toolbar
			DialogUploadFile dlg = (DialogUploadFile) app.zPageDrive.zToolbarPressButton(Button.B_UPLOAD_FILE, fileItem);
			dlg.zPressButton(Button.B_BROWSE);
			zUpload(filePath);
			dlg.zPressButton(Button.B_OK);

			// Verify file is uploaded
			String name = app.zPageDrive.getItemNameFromListView(fileName);
			ZAssert.assertStringContains(name, fileName, "Verify file name through GUI");

		} finally {
			app.zPageMain.zKeyboardKeyEvent(Keys.ESCAPE);
		}
	}
}