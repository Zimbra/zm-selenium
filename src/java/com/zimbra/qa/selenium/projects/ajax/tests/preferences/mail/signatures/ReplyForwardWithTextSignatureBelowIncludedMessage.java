/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.signatures;

import java.util.HashMap;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.SignatureItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;

public class ReplyForwardWithTextSignatureBelowIncludedMessage extends AjaxCore {
	String sigName = "signame" + ConfigProperties.getUniqueString();
	String sigBody = "sigbody" + ConfigProperties.getUniqueString();

	@SuppressWarnings("serial")
	public ReplyForwardWithTextSignatureBelowIncludedMessage() {
		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {{
			put("zimbraPrefComposeFormat", "text");
			put("zimbraPrefGroupMailBy", "message");
			put("zimbraPrefMailSignatureStyle", "internet");
		}};
	}

	@BeforeMethod(groups = { "always" })
	public void CreateSignature() throws HarnessException {
		ZimbraAccount.AccountZCS()
				.soapSend("<CreateSignatureRequest xmlns='urn:zimbraAccount'>" + "<signature name='" + this.sigName
						+ "' >" + "<content type='text/plain'>" + this.sigBody + "</content>" + "</signature>"
						+ "</CreateSignatureRequest>");

		// Refresh UI
		app.zPageMain.zRefreshMainUI();

		logger.info("CreateSignature: finish");
	}


	/**
	 * Test case : Verify text signature below included message While Reply'ing Create
	 * signature through soap Send message with text signature through soap
	 * Select Same message and click Reply from toolbar Click Options dropdown and
	 * select Signature Verify signature should place Below included message
	 * when replying message
	 */

	@Test (description = "Verify text signature below included message when replying",
			groups = { "functional", "L3" })

	public void ReplyTextSignatureBelowIncludeMessage_01() throws HarnessException {

		// Signature is created
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);

		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		ZAssert.assertEquals(signature.getName(), this.sigName, "Verify text signature is created");

		String subject = "subject" + ConfigProperties.getUniqueString();

		// Send a message to the account(self)
		ZimbraAccount.AccountZCS()
				.soapSend("<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>" + "<e t='t' a='"
						+ app.zGetActiveAccount().EmailAddress + "'/>" + "<su>" + subject + "</su>"
						+ "<mp ct='text/plain'>" + "<content>content" + ConfigProperties.getUniqueString()
						+ "</content>" + "</mp>" + "</m>" + "</SendMsgRequest>");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);

		// Select message
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Reply the item
		actual.zPressButton(Button.B_REPLY);
		ZAssert.assertTrue(actual.zGetMailPropertyAsText(DisplayMail.Field.Subject).contains("Re"), "Verify Reply Window");

		// Click Options Drop Down and select Signature
		app.zPageMail.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_ADD_Reply_SIGNATURE, this.sigName);

		// Verify Signature is place Below included message.
		actual.zVerifySignaturePlaceInText("BelowIncludedMessage", this.sigBody, "Reply");
	}


	/**
	 * Test case : Verify text signature below included message when replying all
	 * Create signature through soap Send message with text signature through
	 * soap Select Same message and click ReplyAll from toolbar Click Options
	 * dropdown and select Signature Verify signature should place Below
	 * included message when replying message
	 */

	@Test (description = "Verify text signature below included message when replying all",
			groups = { "functional", "L3" })

	public void ReplyAllTextSignatureBelowIncludeMessage_02() throws HarnessException {

		// Signature is created
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		ZAssert.assertEquals(signature.getName(), this.sigName, "Verify text signature is created");

		String subject = "subject" + ConfigProperties.getUniqueString();

		// Send a message to the account(self)
		ZimbraAccount.AccountZCS()
				.soapSend("<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>" + "<e t='t' a='"
						+ app.zGetActiveAccount().EmailAddress + "'/>" + "<su>" + subject + "</su>"
						+ "<mp ct='text/plain'>" + "<content>content" + ConfigProperties.getUniqueString()
						+ "</content>" + "</mp>" + "</m>" + "</SendMsgRequest>");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);

		// Select message
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Reply the item
		actual.zPressButton(Button.B_REPLYALL);
		ZAssert.assertTrue(actual.zGetMailPropertyAsText(DisplayMail.Field.Subject).contains("Re"), "Verify ReplyAll Window");

		// Click Options Drop Down and select Signature
		app.zPageMail.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_ADD_ReplyAll_SIGNATURE, this.sigName);

		// Verify Signature is place Below included message in ReplyAll window.
		actual.zVerifySignaturePlaceInText("BelowIncludedMessage", this.sigBody, "ReplyAll");
	}


	/**
	 * Test case : Verify text signature below included message when forwarding Create
	 * signature through soap Send message with text signature through soap
	 * Select Same message and click Fwd Click Options dropdown and select Signature
	 * Verify signature should place Below included message when forwarding message
	 */

	@Test (description = "Verify text signature below included message when forwarding",
			groups = { "functional", "L2" })

	public void ForwardTextSignatureBelowIncludeMessage_03() throws HarnessException {

		// Signature is created
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		ZAssert.assertEquals(signature.getName(), this.sigName, "Verify text signature is created");

		String subject = "subject" + ConfigProperties.getUniqueString();

		// Send a message to the account(self)
		ZimbraAccount.AccountZCS()
				.soapSend("<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>" + "<e t='t' a='"
						+ app.zGetActiveAccount().EmailAddress + "'/>" + "<su>" + subject + "</su>"
						+ "<mp ct='text/plain'>" + "<content>content" + ConfigProperties.getUniqueString()
						+ "</content>" + "</mp>" + "</m>" + "</SendMsgRequest>");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);

		// Select message
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Forward the item
		actual.zPressButton(Button.B_FORWARD);
		ZAssert.assertTrue(actual.zGetMailPropertyAsText(DisplayMail.Field.Subject).contains("Fwd"),
				"Verify Fwd Window");

		// Click Options Drop Down and select Signature
		app.zPageMail.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_ADD_FWD_SIGNATURE, this.sigName);

		// Verify Signature is place Below included message.
		actual.zVerifySignaturePlaceInText("BelowIncludedMessage", this.sigBody, "Forward");
	}
}