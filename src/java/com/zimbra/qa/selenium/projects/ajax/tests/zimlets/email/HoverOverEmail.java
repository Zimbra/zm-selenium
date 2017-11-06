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
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.email;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.TooltipContact;

public class HoverOverEmail extends PrefGroupMailByMessageTest {

	public HoverOverEmail() throws HarnessException {
		logger.info("New "+ HoverOverEmail.class.getCanonicalName());
	}


	@Test( description = "Hover over a contact in a message body",
			groups = { "functional", "L2" })

	public void HoverOverEmail_01() throws HarnessException {

		final String email = "email" + ConfigProperties.getUniqueString() + "@foo.com";
		final String subject = "subject" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
							"<a n='firstName'>first"+ ConfigProperties.getUniqueString() +"</a>" +
							"<a n='lastName'>last"+ ConfigProperties.getUniqueString() +"</a>" +
							"<a n='email'>"+ email +"</a>" +
						"</cn>" +
				"</CreateContactRequest>" );

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
        	+				"abc "+ email +" def\n"
        	+				"Line 2\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Hover over the email address
		String locator = "css=span[id$='_ZmEmailObjectHandler']:contains("+ email +")";
		app.zPageMail.zDisplayMailHoverOver(locator);

		// Verify the contact tool tip opens
		TooltipContact tooltip = new TooltipContact(app);
		tooltip.zWaitForActive();

		ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");
	}


	@Test( description = "Hover over a GAL contact in a message body",
			groups = { "functional", "L2" })

	public void HoverOverEmail_02() throws HarnessException {

		// Create a contact in the GAL
		ZimbraAccount contactGAL = (new ZimbraAccount()).provision().authenticate();

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
        	+				"abc "+ contactGAL.EmailAddress +" def\n"
        	+				"Line 2\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Hover over the email address
		String locator = "css=span[id$='_ZmEmailObjectHandler']:contains("+ contactGAL.EmailAddress +")";
		app.zPageMail.zDisplayMailHoverOver(locator);

		// Verify the contact tool tip opens
		TooltipContact tooltip = new TooltipContact(app);
		tooltip.zWaitForActive();

		ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");
	}


	@Test( description = "Hover over a contact group in a message body",
			groups = { "functional", "L2" })

	public void HoverOverEmail_03() throws HarnessException {

		String groupName = "group" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
					"<cn >" +
						"<a n='type'>group</a>" +
						"<a n='nickname'>" + groupName +"</a>" +
						"<a n='fileAs'>8:" +  groupName +"</a>" +
				        "<m type='I' value='" + ZimbraAccount.AccountA().EmailAddress + "' />" +
				        "<m type='I' value='" + ZimbraAccount.AccountB().EmailAddress + "' />" +
					"</cn>" +
				"</CreateContactRequest>");

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
        	+				"abc "+ groupName +" def\n"
        	+				"Line 2\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Hover over the email address
		String locator = "css=span[id$='_com_zimbra_email']:contains("+ groupName +")";

		// Verify the contact group is not converted to the email zimlet link
		boolean present = app.zPageMail.sIsElementPresent(locator);
		ZAssert.assertFalse(present, "Verify the contact group name is not highlighted");
	}


	@Test( description = "Hover over an unknown email address",
			groups = { "functional", "L2" })

	public void HoverOverEmail_04() throws HarnessException {

		final String email = "email" + ConfigProperties.getUniqueString() + "@foo.com";
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
        	+				"abc "+ email +" def\n"
        	+				"Line 2\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Hover over the email address
		String locator = "css=span[id$='_ZmEmailObjectHandler']:contains("+ email +")";
		app.zPageMail.zDisplayMailHoverOver(locator);

		// Verify the contact tool tip opens
		TooltipContact tooltip = new TooltipContact(app);
		tooltip.zWaitForActive();

		ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");
	}


	@Test( description = "Hover over a contact in a message body (zimbraFeatureGalEnabled=FALSE)",
			groups = { "functional", "L2" })

	public void HoverOverEmail_05() throws HarnessException {

		final String email = "email" + ConfigProperties.getUniqueString() + "@foo.com";
		final String subject = "subject" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn >" +
							"<a n='firstName'>first"+ ConfigProperties.getUniqueString() +"</a>" +
							"<a n='lastName'>last"+ ConfigProperties.getUniqueString() +"</a>" +
							"<a n='email'>"+ email +"</a>" +
						"</cn>" +
				"</CreateContactRequest>" );

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
        	+				"abc "+ email +" def\n"
        	+				"Line 2\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Hover over the email address
		String locator = "css=span[id$='_ZmEmailObjectHandler']:contains("+ email +")";
		app.zPageMail.zDisplayMailHoverOver(locator);

		// Verify the contact tool tip opens
		TooltipContact tooltip = new TooltipContact(app);
		tooltip.zWaitForActive();

		ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");
	}


	@Bugs( ids = "80286" )
	@Test( description = "Hover over a GAL contact in a message body (zimbraFeatureGalEnabled=FALSE)",
			groups = { "functional", "L2" })

	public void HoverOverEmail_06() throws HarnessException {

		// Create a contact in the GAL
		ZimbraAccount contactGAL = (new ZimbraAccount()).provision().authenticate();

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
        	+				"abc "+ contactGAL.EmailAddress +" def\n"
        	+				"Line 2\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Hover over the email address
		String locator = "css=span[id$='_ZmEmailObjectHandler']:contains("+ contactGAL.EmailAddress +")";
		app.zPageMail.zDisplayMailHoverOver(locator);

		// Verify the contact tool tip opens
		TooltipContact tooltip = new TooltipContact(app);
		tooltip.zWaitForActive();

		ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");
	}


	@Test( description = "Hover over a contact group in a message body (zimbraFeatureGalEnabled=FALSE)",
			groups = { "functional", "L2" })

	public void HoverOverEmail_07() throws HarnessException {

		String groupName = "group" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
			"<CreateContactRequest xmlns='urn:zimbraMail'>" +
				"<cn >" +
					"<a n='type'>group</a>" +
					"<a n='nickname'>" + groupName +"</a>" +
					"<a n='fileAs'>8:" +  groupName +"</a>" +
			        "<m type='I' value='" + ZimbraAccount.AccountA().EmailAddress + "' />" +
			        "<m type='I' value='" + ZimbraAccount.AccountB().EmailAddress + "' />" +
				"</cn>" +
			"</CreateContactRequest>");

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
        	+				"abc "+ groupName +" def\n"
        	+				"Line 2\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Hover over the email address
		String locator = "css=span[id$='_com_zimbra_email']:contains("+ groupName +")";

		// Verify the contact group is not converted to the email zimlet link
		boolean present = app.zPageMail.sIsElementPresent(locator);
		ZAssert.assertFalse(present, "Verify the contact group name is not highlighted");
	}


	@Bugs( ids = "80286" )
	@Test( description = "Hover over an unknown email address (zimbraFeatureGalEnabled=FALSE)",
			groups = { "functional", "L2" })

	public void HoverOverEmail_08() throws HarnessException {

		final String email = "email" + ConfigProperties.getUniqueString() + "@foo.com";
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
        	+				"abc "+ email +" def\n"
        	+				"Line 2\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Hover over the email address
		String locator = "css=span[id$='_ZmEmailObjectHandler']:contains("+ email +")";
		app.zPageMail.zDisplayMailHoverOver(locator);

		// Verify the contact tool tip opens
		TooltipContact tooltip = new TooltipContact(app);
		tooltip.zWaitForActive();

		ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");
	}
}