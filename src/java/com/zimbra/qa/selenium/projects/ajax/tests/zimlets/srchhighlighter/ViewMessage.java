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
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.srchhighlighter;

import java.util.*;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.search.*;

public class ViewMessage extends AjaxCore {

	public ViewMessage() {
		logger.info("New "+ ViewMessage.class.getCanonicalName());

		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2077258660517383691L; {
				put("zimbraPrefGroupMailBy", "message");
			}
		};
	}


	@Test (description = "Search for mail content.  Verify search terms are highlighted.",
			groups = { "functional", "L2" })

	public void ViewMessage_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String term = "search" + ConfigProperties.getUniqueString();

		// Send the message from AccountA to the ZCS user
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

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

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

			// Verify the first term is located in a search highlight
			HtmlElement.evaluate(body, "//span/span[@class='ZmSearchResult']", null, term, 1);

		} finally {
			app.zPageSearch.zClose();
		}
	}
}