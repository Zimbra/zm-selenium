/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.conversation.conversations;

import java.io.*;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;


public class ReadMore extends PrefGroupMailByConversationTest {

	
	public ReadMore() throws HarnessException {
		logger.info("New "+ ReadMore.class.getCanonicalName());
		

	}
	
	
	@Test(	description = "Use the 'Read More' button to scroll through the conversation content",
			groups = { "functional" })
	public void ViewMail_01() throws HarnessException {
		
		
		//-- DATA
		
		final String subject = "ReadMore13674340693103";

		final String mimeFolder = ZimbraSeleniumProperties.getBaseDirectory() + "/data/public/mime/email11";
		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFolder));


		
		//-- GUI
		
		
		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Click on the "Read More" button
		app.zPageMail.zToolbarPressButton(Button.B_READMORE);
		
		//-- VERIFICATION
		
		// TODO: not sure how to verify that the scrollbar has moved?
		

	}


}
