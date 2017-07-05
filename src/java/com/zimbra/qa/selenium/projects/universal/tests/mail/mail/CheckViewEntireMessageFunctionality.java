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
package com.zimbra.qa.selenium.projects.universal.tests.mail.mail;

import java.io.File;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DisplayMail.Field;

public class CheckViewEntireMessageFunctionality extends PrefGroupMailByMessageTest {

	public CheckViewEntireMessageFunctionality() {
		logger.info("New "+ CheckViewEntireMessageFunctionality.class.getCanonicalName());
	}
	
	@Bugs( ids = "102052")
	@Test( description = "Can't read long message due to missing of 'View entire message' link", groups = { "smoke", "L1" })
	
	public void CheckViewEntireMessageFunctionality_01() throws HarnessException  {	
			
		final String subject = "Bug39246";
		final String mime = ConfigProperties.getBaseDirectory() + "/data/public/mime/viewEntireMessage_Bug39246.txt";
		
		// Inject the example message
		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mime));

		// Get Mail
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");
		
		// Select the message
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// In the preview pane, click "View Entire Message"
		display.zPressButton(Button.B_VIEW_ENTIRE_MESSAGE);
		
		// Wait 30 seconds for the end of the message to display
		String body = "";
		for (int i = 0; i < 30; i++) {
			
			// Get the body
			body = display.zGetMailProperty(Field.Body);
			
			// If the body contains the last few words from the MIME message, assume the full body loaded
			logger.info("Waiting for 'CLICK ON THE TEST SCRIPT NAMES' to appear ...");
			if ( body.contains("CLICK ON THE TEST SCRIPT NAMES") )
				break;
			
			SleepUtil.sleep(1000);
		}

		ZAssert.assertStringContains(body, "CLICK ON THE TEST SCRIPT NAMES", "Verify the last words from the mime are displayed in the body");
		
	}

}
