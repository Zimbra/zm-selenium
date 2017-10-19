/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.zimlets.date;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.*;
import com.zimbra.qa.selenium.projects.universal.ui.*;

public class HoverOverDate extends PrefGroupMailByMessageTest {
	
	public HoverOverDate() throws HarnessException {
		logger.info("New "+ HoverOverDate.class.getCanonicalName());
	}
	
	@Test( description = "Hover over a date in a message body",
			groups = { "functional","L2" })
	
	public void HoverOverDate_01() throws HarnessException {

		//-- DATA Setup
		final String date = "12/25/2016";
		final String subject = "subject" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='" + FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox).getId() + "' >"
        	+			"<content>From: foo@foo.com\n"
        	+				"To: foo@foo.com \n"
        	+				"Subject: "+ subject +"\n"
        	+				"MIME-Version: 1.0 \n"
        	+				"Content-Type: text/plain; charset=utf-8 \n"
        	+				"Content-Transfer-Encoding: 7bit\n"
        	+				"\n"
        	+				"Line 1\n"
        	+				"abc "+ date +" def\n"
        	+				"Line 2\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Hover over the email address
		String locator = "css=span[id$='_com_zimbra_date']:contains("+ date + ")";
		app.zPageMail.zDisplayMailHoverOver(locator);
		
		//-- VERIFICATION
		TooltipContact tooltip = new TooltipContact(app);
		tooltip.zWaitForActive();
		
		ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");
	}
	
	@Test( description = "Hovor over a date string in the body, such as today, tomorrow, last night, etc.",
			groups = { "functional", "L2" })
	
	public void HoverOverDate_02() throws HarnessException {
		
		String newline = String.format("%n");

		String[] values = { "today", "tonight", "this morning", "tomorrow night", "tomorrow morning", "tomorrow", "last night", 
							"yesterday morning", "yesterday", "this Monday", "next Monday", "Last Monday", "first Monday in April", "third Monday" };
		
		// Create the message content, with one term on each line
		StringBuffer content = new StringBuffer(ConfigProperties.getUniqueString()).append(newline);
		for (String s : values) {
			content.append(s).append(newline);
		}
		String subject = "subject " + ConfigProperties.getUniqueString();

		// Send the message from AccountA to the ZCS user
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ content.toString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");
		
		// GUI Actions
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// VERIFICATION
		for (int i=0; i<=values.length-1; i++) {
			
			System.out.println("Verify " + values[i] + " hover tooltip shows");

			// Hover over the email address
			String locator = "body div span:nth-of-type(" + (i+1) + ")";
			app.zPageMail.zDisplayMailHoverOver(locator);
			
			// Verify the contact tool tip opens
			TooltipContact tooltip = new TooltipContact(app);
			tooltip.zWaitForActive();
			
			ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");
		
			// Clear the tooltip
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
			
		}
	}
}