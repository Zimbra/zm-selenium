/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013 Zimbra Software, LLC.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.4 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.date;

import java.util.*;

import org.openqa.selenium.*;
import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.*;


public class HoverOver extends PrefGroupMailByMessageTest {

	
	public HoverOver() throws HarnessException {
		logger.info("New "+ HoverOver.class.getCanonicalName());
		
	}
	
	@Test(	description = "Hover over a date in a message body",
			groups = { "functional" })
	public void HoverOver_01() throws HarnessException {

		//-- DATA Setup
		final String date = "12/25/2016";
		final String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();

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


		
		
		//-- GUI Actions
		
		
		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Hover over the email address
		String locator = "css=span[id$='_com_zimbra_date']:contains("+ date + ")";
		app.zPageMail.sMouseOver(locator, (WebElement[]) null);
		
		
		
		//-- VERIFICATION
		
		
		// Verify the contact tool tip opens
		TooltipContact tooltip = new TooltipContact(app);
		tooltip.zWaitForActive();
		
		ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");
		
		
	}
	
	@Test(	description = "Hovor over a date string in the body, such as today, tomorrow, last night, etc.",
			groups = { "functional" })
	public void HoverOver_11() throws HarnessException {
		String newline = String.format("%n");
		

		List<String> values = Arrays.asList("today,tonight,this morning,tomorrow night,tomorrow morning,tomorrow,last night,yesterday morning,yesterday,this Monday,next Monday,Last Monday,first Monday in April,third Monday".split(","));
		
		// Create the message content, with one term on each line
		StringBuffer content = new StringBuffer(ZimbraSeleniumProperties.getUniqueString()).append(newline);
		for (String s : values) {
			content.append(s).append(newline);
		}
		String subject = "subject " + ZimbraSeleniumProperties.getUniqueString();

		// Send the message from AccountA to the ZWC user
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
		
		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);


		// VERIFICATION
		//
		for (String value : values) {

			// Verify the data is present
			HtmlElement.evaluate(bodyElement, "//span//span", null, value, 1);

			// Hover over the email address
			String locator = "css=span[id$='_com_zimbra_date']:contains("+ value + ")";
			app.zPageMail.sMouseOver(locator, (WebElement[]) null);
			
			// Verify the contact tool tip opens
			TooltipContact tooltip = new TooltipContact(app);
			tooltip.zWaitForActive();
			
			ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");
		
			// Clear the tooltip
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
			
		}
		

	}



}
