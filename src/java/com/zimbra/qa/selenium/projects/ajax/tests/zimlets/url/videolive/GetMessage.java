/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.url.videolive;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;

public class GetMessage extends AjaxCore {

	public GetMessage() {
		logger.info("New "+ GetMessage.class.getCanonicalName());
	}


	@Test (description = "Receive a mail with embedded video",
			groups = { "functional", "L2" })

	public void GetMail_01() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email07/mime.txt";
		final String subject = "subject135232705018411";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify the thumbnail appears
		String locator = "css=div[id^='YOUTUBE'] img";
		boolean present = app.zPageMail.sIsElementPresent(locator);

		ZAssert.assertTrue(present, "Verify the youtube thumbnail is present");

		// Click on the image
		app.zPageMail.sClick(locator);
		app.zPageMail.zWaitForBusyOverlay();

		SleepUtil.sleep(5000);

		// Verify the iframe appears with the you tube player
		locator = "css=iframe[id^='YOUTUBE-IFRAME']";
		present = app.zPageMail.sIsElementPresent(locator);
		ZAssert.assertTrue(present, "Verify the youtube iframe is present");
	}
}