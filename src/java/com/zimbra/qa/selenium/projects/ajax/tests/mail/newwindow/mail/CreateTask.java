package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.mail;

/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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


import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.TaskItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowDisplayMail;


public class CreateTask extends PrefGroupMailByMessageTest {

	public int delaySeconds = 10;
	
	public CreateTask() {
		logger.info("New "+ CreateTask.class.getCanonicalName());
		
		super.startingAccountPreferences.put("zimbraPrefMarkMsgRead", "" + delaySeconds);


	}
	
	@Test( description = "Create Task from new windownby action menu -> Create Task",
			groups = { "functional" })
	public void CreateTask_01() throws HarnessException {
		
		// Create the message data to be sent
		String subject = "subject"+ ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		
		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);
				
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		
		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + subject;
		
		try {
			
			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);
			
			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
			
			window.zToolbarPressPulldown(Button.B_ACTIONS, Button.O_CREATE_TASK);
			app.zPageMail.zClickAt("css=div[id^='ztb__TKE']  tr[id^='ztb__TKE'] td[id$='_title']:contains('Save')", "");
			SleepUtil.sleepMedium();

		} finally {
			
			// Make sure to close the window
			if ( window != null ) {
				window.zCloseWindow(windowTitle);
				window = null;
			}
			app.zPageMail.zSelectWindow(null);
			
		}


		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(),subject);
		ZAssert.assertEquals(task.getName(), subject, "Verify task subject");

	}
		


}
