/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.srchhighlighter;

import java.util.*;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field;
import com.zimbra.qa.selenium.projects.ajax.ui.search.*;


public class ViewMessage extends AjaxCommonTest {

	
	public ViewMessage() {
		logger.info("New "+ ViewMessage.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageMail;

		// Basic settings
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2077258660517383691L;
		{
		    put("zimbraPrefGroupMailBy", "message");
		}};



	}
	
	@Test(	description = "Search for mail content.  Verify search terms are highlighted.",
			groups = { "functional" })
	public void ViewMessage_01() throws HarnessException {
		
		
		//-- DATA
		
		
		// Create the message data to be sent
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		String term = "search" + ZimbraSeleniumProperties.getUniqueString();
		
		// Send the message from AccountA to the ZWC user
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ term +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		
		
		//-- GUI
		
		
		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		// Remember to close the search view
		try {
			
			// Search for the message
			app.zPageSearch.zAddSearchQuery("content:("+ term +")");
			app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);
			
			// Select the result
			app.zPageSearch.zListItem(Action.A_LEFTCLICK, subject);
			
			// Wait for a bit so the zimlet can take affect
			SleepUtil.sleep(5000);

			// Select the result
			DisplayMail display = (DisplayMail)app.zPageSearch.zListItem(Action.A_LEFTCLICK, subject);
			ZAssert.assertNotNull(display, "Verify the message contents are shown");

			// Wait for a bit so the zimlet can take affect
			SleepUtil.sleep(5000);

			// Verify the term is highlighted
			
			HtmlElement body = display.zGetMailPropertyAsHtml(Field.Body);
			
			// See http://bugzilla.zimbra.com/show_bug.cgi?id=82607 ... the DOM
			// has a bug in it.  Once that bug is fixed, the xpath may need rework.
			// 

			// Verify the first term is located in a search highlight
			HtmlElement.evaluate(body, "//span/span[@class='ZmSearchResult']", null, term, 1);
			
		} finally {
			// Remember to close the search view
			app.zPageSearch.zClose();
		}

	}

	@Test(	description = "Search for mail content (two terms).  Verify search terms are highlighted.",
			groups = { "functional" })
	public void ViewMessage_02() throws HarnessException {
		
		
		//-- DATA
		
		
		// Create the message data to be sent
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		String term1 = "search" + ZimbraSeleniumProperties.getUniqueString();
		String term2 = "search" + ZimbraSeleniumProperties.getUniqueString();
		
		// Send the message from AccountA to the ZWC user
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ term1 +" "+ term2 +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		
		
		//-- GUI
		
		
		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		// Remember to close the search view
		try {
			
			// Search for the message
			app.zPageSearch.zAddSearchQuery("content:("+ term1 +" "+ term2 +")");
			app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);
			
			// Select the result
			DisplayMail display = (DisplayMail)app.zPageSearch.zListItem(Action.A_LEFTCLICK, subject);
			ZAssert.assertNotNull(display, "Verify the message contents are shown");

			// Wait for a bit so the zimlet can take affect
			SleepUtil.sleep(5000);

			// Verify the term is highlighted
			
			HtmlElement body = display.zGetMailPropertyAsHtml(Field.Body);
			
			// See http://bugzilla.zimbra.com/show_bug.cgi?id=82607 ... the DOM
			// has a bug in it.  Once that bug is fixed, the xpath may need rework.
			// 
			// Verify the first term is located in a search highlight
			HtmlElement.evaluate(body, "//span/span[@class='ZmSearchResult']", null, term1, 1);
			HtmlElement.evaluate(body, "//span/span[@class='ZmSearchResult']", null, term2, 1);

		} finally {
			// Remember to close the search view
			app.zPageSearch.zClose();
		}

	}



}
