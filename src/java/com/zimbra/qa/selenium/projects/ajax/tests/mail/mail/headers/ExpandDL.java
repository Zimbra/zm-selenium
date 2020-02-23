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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mail.headers;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field;

public class ExpandDL extends SetGroupMailByMessagePreference {

	public ExpandDL() throws HarnessException {
		logger.info("New "+ ExpandDL.class.getCanonicalName());
	}


	@Test (description = "Verify the Expand icon a DL in the To field",
			groups = { "bhr" })

	public void ExpandDL_01() throws HarnessException {

		// Create a DL with a couple of members
		ZimbraAccount account1 = (new ZimbraAccount()).provision().authenticate();
		ZimbraAccount account2 = (new ZimbraAccount()).provision().authenticate();
		ZimbraDistributionList distribution = (new ZimbraDistributionList()).provision();
		distribution.addMember(account1);
		distribution.addMember(account2);

		// Send a message to the DL with the test account in the CC
		String subject = "subject"+ ConfigProperties.getUniqueString();
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ distribution.EmailAddress +"'/>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ "body" + ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		// Refresh to get the new message
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		List<AbsBubble> bubbles = actual.zListGetBubbles(Field.To);

		// Verify the DL is in the To field and has a expand/+ icon
		AbsBubble found = null;
		for (AbsBubble b : bubbles) {
			if ( distribution.EmailAddress.equalsIgnoreCase(b.getMyDisplayText()) ) {
				found = b;
				break;
			}
		}

		ZAssert.assertNotNull(found, "Verify the DL bubble is found");
		ZAssert.assertNotNull(found instanceof BubbleEmailAddress, "Verify the bubble is a DL bubble");
		ZAssert.assertTrue(((BubbleEmailAddress)found).zHasExpandIcon(), "Verify the DL buble has a expand/+ icon");
	}


	@Test (description = "Expand a DL in the To field",
			groups = { "bhr" })

	public void ExpandDL_02() throws HarnessException {

		// Create a DL with a couple of members
		ZimbraAccount account1 = (new ZimbraAccount()).provision().authenticate();
		ZimbraAccount account2 = (new ZimbraAccount()).provision().authenticate();
		ZimbraDistributionList distribution = (new ZimbraDistributionList()).provision();
		distribution.addMember(account1);
		distribution.addMember(account2);

		// Send a message to the DL with the test account in the CC
		String subject = "subject"+ ConfigProperties.getUniqueString();
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ distribution.EmailAddress +"'/>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ "body" + ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		// Refresh to get the new message
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Find the DL bubble
		List<AbsBubble> bubbles = actual.zListGetBubbles(Field.To);
		AbsBubble found = null;
		for (AbsBubble b : bubbles) {
			if ( distribution.EmailAddress.equalsIgnoreCase(b.getMyDisplayText()) ) {
				found = b;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the DL bubble is found");
		ZAssert.assertNotNull(found instanceof BubbleEmailAddress, "Verify buble is a DL bubble");

		BubbleEmailAddress bubble = (BubbleEmailAddress)found;

		// Expand the bubble
		bubble.zItem(Action.A_EXPAND);

		// Get the list of members that are displayed
		List<AutocompleteEntry> members = bubble.zAutocompleteListGetEntries();

		// Verify that the two members show up
		boolean found1 = false;
		boolean found2 = false;
		for ( AutocompleteEntry entry : members ) {
			if ( entry.getAddress().equals(account1.EmailAddress) ) {
				found1 = true;
				continue;
			}
			if ( entry.getAddress().equals(account2.EmailAddress) ) {
				found2 = true;
				continue;
			}
		}

		ZAssert.assertTrue(found1, "Verify the DL expanded list contains account1's email "+ account1.EmailAddress);
		ZAssert.assertTrue(found2, "Verify the DL expanded list contains account2's email "+ account2.EmailAddress);
	}
}