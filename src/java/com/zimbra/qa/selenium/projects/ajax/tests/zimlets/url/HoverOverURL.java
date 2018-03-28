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
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.url;

import java.util.*;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.pages.*;

public class HoverOverURL extends AjaxCore {

	public HoverOverURL() {
		logger.info("New "+ HoverOverURL.class.getCanonicalName());

		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -3888082425397157889L; {
				put("zimbraPrefGroupMailBy", "message");
			}
		};
	}


	@Test (description = "Hover over a URL",
			groups = { "functional", "L2" })

	public void HoverOverURL_01() throws HarnessException {

		String subject = "subject" + ConfigProperties.getUniqueString();
		String server = "synacor";
		String body = "http://www." + server + ".com";

		// Send the message from AccountA to the ZCS user
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ body +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Get all the messages in the inbox
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Hover over the URL
		String locator = "css=span[id$='_com_zimbra_url']:contains(" + server + ")";
		app.zPageMail.zDisplayMailHoverOver(locator);

		// Verify the contact tool tip opens
		TooltipContact tooltip = new TooltipContact(app);
		tooltip.zWaitForActive();
		ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");
	}


	@Test (description = "Hover over a URL - verify tooltip content",
			groups = { "functional", "L2" })

	public void HoverOverURL_02() throws HarnessException {

		String subject = "subject" + ConfigProperties.getUniqueString();
		String server = "synacor";
		String body = "http://www." + server + ".com";

		// Send the message from AccountA to the ZCS user
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ body +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Get all the messages in the inbox
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Hover over the URL
		String locator = "css=span[id$='_com_zimbra_url']:contains(" + server + ")";
		app.zPageMail.zDisplayMailHoverOver(locator);

		// Verify the contact tool tip opens
		TooltipContact tooltip = new TooltipContact(app);
		tooltip.zWaitForActive();

		// Get the tooltip contents
		String content = tooltip.zGetContents();
		ZAssert.assertStringContains(content, server, "Verify basic tooltip content");
	}


	@Bugs (ids = "82303")
	@Test (description = "Hover over a URL with 'javascript' in the path.  Verify no change to JAVASCRIPT-BLOCKED",
			groups = { "functional", "L2" })

	public void HoverOverURL_03() throws HarnessException {

		// Create the message data to be sent
		String subject = "bug82303";
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug82303/mime.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Get all the messages in the inbox
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Hover over each URL, verify no 'blocked'
		for (String link : "Link1,Link2,Link3".split(",") ) {

			// Link1: http://www.zimbra.com/foo/javascript/foo
			// Link2: http://www.zimbra.com/foo/Javascript/foo
			// Link3: http://www.zimbra.com/foo/JAVASCRIPT/foo

			// Hover over the URL
			String locator = "css=span[id$='_com_zimbra_url']:contains("+ link +")";
			app.zPageMail.zDisplayMailHoverOver(locator);

			//-- VERIFICATION

			// Verify the contact tool tip opens
			TooltipContact tooltip = new TooltipContact(app);
			tooltip.zWaitForActive();

			// Get the tooltip contents
			String content = tooltip.zGetContents();
			ZAssert.assertStringDoesNotContain(content.toLowerCase(), "blocked", "Verify 'javascript' not changed to 'javascript-BLOCKED'");
		}
	}
}
