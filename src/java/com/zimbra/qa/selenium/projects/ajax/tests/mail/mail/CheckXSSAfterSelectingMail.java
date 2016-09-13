package com.zimbra.qa.selenium.projects.ajax.tests.mail.mail;

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

import java.io.File;



import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;

public class CheckXSSAfterSelectingMail extends PrefGroupMailByMessageTest {

	boolean injected = false;
		
	public CheckXSSAfterSelectingMail() throws HarnessException {
		logger.info("New "+ CheckXSSAfterSelectingMail.class.getCanonicalName());
	}
	
	@Bugs( ids = "102637" )	
	@Test( description = "Persistent XSS: unsafe content not filtered by defange", groups = { "functional" })
	
	public void CheckXSSAfterSelectingMail_01() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory()	+ "/data/public/mime/XSS.txt";
		final String subject = "test xss";

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);
		
		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		SleepUtil.sleepMedium();
		String Window = "Problem loading page";
		
		try {
			//click on test link
			
			app.zPageMail.sClick("css=div>a[target='_blank']:contains('test')");	
			SleepUtil.sleepMedium();

			String[] windowtitle = app.zPageMail.sGetAllWindowTitles().toString().split(",");
			
			//Veriy there is not xss alert dialog.
			ZAssert.assertStringContains(windowtitle[1].toLowerCase(), Window.toLowerCase(), "it shows window javascript-blocked:alert'XSS");
			
			//Close Show Original window
			app.zPageMail.zSeparateWindowClose(Window);
			app.zPageMail.sSelectWindow(null);
			
		} finally  {
			app.zPageMail.zSeparateWindowClose(Window);				
			app.zPageMail.sSelectWindow(null);
			//window.sSelectWindow(null);
		}
	
	}
}
