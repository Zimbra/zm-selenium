/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.feeds;

import java.io.File;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.tests.mail.mail.CheckMailContentForSpecificMimes;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field;

public class CheckFeedGeneratedMessage extends PrefGroupMailByMessageTest {

	public CheckFeedGeneratedMessage() {
		logger.info("New "+ CheckMailContentForSpecificMimes.class.getCanonicalName());
	}

	@Bugs(	ids = "52121")
	@Test(	description = "Verify bug 52121:  feed-generated messages do not render in AJAX client ", groups = { "functional" })
	
	public void CheckFeedGeneratedMessage_01() throws HarnessException {
		
		String subject = "\"Wear-with-all\"";
		String bodytext = "Barbara's suggestion:";
	
		String MimeFolder = ZimbraSeleniumProperties.getBaseDirectory() + "/data/private/mime/Bugs/Bug52121";
		LmtpInject.injectFile(ZimbraAccount.AccountZWC().EmailAddress, new File(MimeFolder));
				
		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		String body = display.zGetMailProperty(Field.Body);
		
		//Verify message is rendered correctly
		ZAssert.assertStringContains(body, bodytext, "Verify the ending text appears");

	}
}
