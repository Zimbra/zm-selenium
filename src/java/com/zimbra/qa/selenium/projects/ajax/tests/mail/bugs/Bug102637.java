package com.zimbra.qa.selenium.projects.ajax.tests.mail.bugs;

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


import java.io.File;


import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;



public class Bug102637 extends PrefGroupMailByMessageTest {

	boolean injected = false;
		
	public Bug102637() throws HarnessException {
		logger.info("New "+ Bug102637.class.getCanonicalName());
	}
	
	
	@Bugs(	ids = "102637" )	

	@Test(description = "Persistent XSS: unsafe content not filtered by defange", groups = { "functional" })
	public void Bug_102637() throws HarnessException {

		final String mimeFile = ZimbraSeleniumProperties.getBaseDirectory()
				+ "/data/public/mime/XSS.txt";
		final String subject = "test xss";

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(
				mimeFile));

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		SleepUtil.sleepMedium();

		//click on test link
		
		app.zPageMail.sClick("css=div>a[target='_blank']:contains('test')");

		String Window = "Problem loading page";

		String[] windowtitle = app.zPageMail.sGetAllWindowTitles().toString()
				.split(",");
		
		//Veriy there is not xss alert dialog.
		ZAssert.assertStringContains(windowtitle[1].toLowerCase(),
				Window.toLowerCase(), "it shows window javascript-blocked:alert'XSS");

	}
	

}
