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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.inlineimage;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.SeparateWindowFormMailNew;

public class OpenComposedMsgWithAnInlineAttachmentInNewWindow extends SetGroupMailByMessagePreference {

	public OpenComposedMsgWithAnInlineAttachmentInNewWindow() {
		logger.info("New "+ OpenComposedMsgWithAnInlineAttachmentInNewWindow.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
	}


	@Test (description = "Verify inline attachment in Normal Compose window as well as in New compose window",
			groups = { "functional", "L2", "upload" })

	public void OpenComposedMsgWithAnAttachmentInNewWindow_01() throws HarnessException {

		// Create file item
		final String fileName = "samplejpg.jpg";
		final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Upload the file
		app.zPageMail.zPressButton(Button.O_ATTACH_DROPDOWN);
		app.zPageMail.zPressButton(Button.B_ATTACH_INLINE);
		zUploadInlineImageAttachment(filePath);

		SeparateWindowFormMailNew window = null;
		String windowTitle = "Zimbra: Compose";

		try {

			window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_DETACH_COMPOSE);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			// Verify Inline Attachment should not disappeared  New compose window
			ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageAttachmentExistsInComposeWindow(windowTitle, 0),"Verify inline image is present in New compose window");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}
	}
}