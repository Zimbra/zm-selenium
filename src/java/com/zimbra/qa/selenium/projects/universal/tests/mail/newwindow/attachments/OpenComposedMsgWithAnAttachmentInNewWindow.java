package com.zimbra.qa.selenium.projects.universal.tests.mail.newwindow.attachments;

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

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.universal.ui.mail.SeparateWindowFormMailNew;

public class OpenComposedMsgWithAnAttachmentInNewWindow extends PrefGroupMailByMessageTest {

	public OpenComposedMsgWithAnAttachmentInNewWindow() {
		logger.info("New " + OpenComposedMsgWithAnAttachmentInNewWindow.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
	}

	@Test( description = "Verify attachment in Normal Compose window as well as in New compose window", groups = { "functional", "L2" })
	
	public void OpenComposedMsgWithAnAttachmentInNewWindow_01() throws HarnessException {

		if (OperatingSystem.isWindows() == true && !ConfigProperties.getStringProperty("browser").contains("msedge")) {

			// Create file item
			final String fileName = "testtextfile.txt";
			final String filePath = ConfigProperties.getBaseDirectory()+ "\\data\\public\\other\\" + fileName;

			// Open the new mail form
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
			ZAssert.assertNotNull(mailform, "Verify the new form opened");

			// Upload the file
			app.zPageMail.zPressButton(Button.B_ATTACH);
			zUpload(filePath);
			
			SeparateWindowFormMailNew window = null;
			String windowTitle = "Zimbra: Compose";
			
			try {

				window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_DETACH_COMPOSE);

				window.zSetWindowTitle(windowTitle);
				window.waitForComposeWindow();
				ZAssert.assertTrue(window.zIsActive(),"Verify the window is active");

				// Select the window
				window.sSelectWindow(windowTitle);
				
				// Verify Attachment should not disappeared  New compose window
				Assert.assertTrue(window.zIsVisiblePerPosition("css=a[id^='COMPOSE']:contains(" + fileName + ")", 0, 0),"vcf attachment link present");

			} finally {
				app.zPageMain.zCloseWindow(window, windowTitle, app);
			}

		} else {
			throw new SkipException("File upload operation is allowed only for Windows OS (Skipping upload tests on MS Edge for now due to intermittancy and major control issue), skipping this test...");
		}
	}
}
