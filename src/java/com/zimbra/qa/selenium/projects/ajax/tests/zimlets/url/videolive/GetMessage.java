/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.url.videolive;

import java.io.File;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;


public class GetMessage extends AjaxCommonTest {

	
	public GetMessage() {
		logger.info("New "+ GetMessage.class.getCanonicalName());


	}
	
	@Test(	description = "Receive a mail with embedded video",
			groups = { "functional" })
	public void GetMail_01() throws HarnessException {
		
		//-- Data
		
		// Inject the sample message
		final String mimeFile = ZimbraSeleniumProperties.getBaseDirectory() + "/data/public/mime/email07/mime.txt";
		final String subject = "subject135232705018411";
		
		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));


		
		//-- GUI
		
		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		
		//-- Verification
		
		// Verify the thumbnail appears
		
		String locator = "css=div[id^='YOUTUBE_'] img";
		boolean present = app.zPageMail.sIsElementPresent(locator);
		
		ZAssert.assertTrue(present, "Verify the youtube thumbnail is present");
		
		// Click on the image
		app.zPageMail.zClickAt(locator, "");
		app.zPageMail.zWaitForBusyOverlay();
		
		SleepUtil.sleep(5000);
		
		// Verify the iframe appears with the you tube player
		locator = "css=iframe[id^='youtube-iframe_']";
		present = app.zPageMail.sIsElementPresent(locator);

		ZAssert.assertTrue(present, "Verify the youtube iframe is present");

	}


}
