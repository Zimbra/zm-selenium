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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.gui.hover;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.TooltipContact;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;

public class Address extends PrefGroupMailByMessageTest {

	public Address() {
		logger.info("New "+ Address.class.getCanonicalName());
	}


	@Test (description = "Hover over GAL address",
			groups = { "functional", "L2" })

	public void Address_01() throws HarnessException {

		String subject = "subject"+ ConfigProperties.getUniqueString();

		// AccountA (in the GAL) sends a message to the test account
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>body" + ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Hover over the From field
		display.zHoverOver(DisplayMail.Field.From);

		TooltipContact tooltip = new TooltipContact(app);
		tooltip.zWaitForActive();

		ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");
	}


	@Test (description = "Hover over External (non-GAL) address",
			groups = { "functional", "L2" })

	public void Address_02() throws HarnessException {

		String subject = "subject"+ ConfigProperties.getUniqueString();
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox);

		// AccountA (in the GAL) sends a message to the test account
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
		        		+		"<m l='"+ folder.getId() +"' >"
		            	+			"<content>From: foo@foo.com\n"
		            	+				"To: foo@foo.com \n"
		            	+				"Subject: "+ subject +"\n"
		            	+				"MIME-Version: 1.0 \n"
		            	+				"Content-Type: text/plain; charset=utf-8 \n"
		            	+				"Content-Transfer-Encoding: 7bit\n"
		            	+				"\n"
		            	+				"simple text string in the body\n"
		            	+			"</content>"
		            	+		"</m>"
						+	"</AddMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Hover over the From field
		display.zHoverOver(DisplayMail.Field.From);

		TooltipContact tooltip = new TooltipContact(app);
		tooltip.zWaitForActive();

		ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");
	}
}