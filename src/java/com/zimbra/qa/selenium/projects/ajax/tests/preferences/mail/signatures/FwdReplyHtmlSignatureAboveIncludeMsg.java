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
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;

public class FwdReplyHtmlSignatureAboveIncludeMsg extends AjaxCommonTest {
	String sigName = "signame" + ConfigProperties.getUniqueString();
	String sigBody = "signature<b>bold" + ConfigProperties.getUniqueString() + "</b>signature";
	String contentHTMLSig = XmlStringUtil
			.escapeXml("<html>" + "<head></head>" + "<body>" + sigBody + "</body>" + "</html>");

	@SuppressWarnings("serial")
	public FwdReplyHtmlSignatureAboveIncludeMsg() {
		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {
			{
				put("zimbraPrefGroupMailBy", "message");
			}
		};
	}

	@BeforeMethod(groups = { "always" })
	public void CreateSignature() throws HarnessException {
		ZimbraAccount.AccountZWC()
				.soapSend("<CreateSignatureRequest xmlns='urn:zimbraAccount'>" + "<signature name='" + this.sigName
						+ "' >" + "<content type='text/html'>'" + this.contentHTMLSig + "'</content>" + "</signature>"
						+ "</CreateSignatureRequest>");

		// Refresh UI
		app.zPageMain.sRefresh();

		logger.info("CreateSignature: finish");
	}

	/**
	 * Test case : Verify Html Signature AboveIncludedMsg While Fwd'ing Create
	 * HTML signature through soap Send message through soap Select Same Msg and
	 * click Fwd Click Options dropdown and select Signature Verify signature
	 * should place above included message while fwd'ing msg
	 * 
	 * @throws HarnessException
	 */
	@Test(description = "Verify Html Signature place AboveIncludedMsg While Fwd'ing- Verify through GUI ", groups = {
			"functional", "L2" })
	public void FwdMsgWithTextSignatureAboveIncludeMsg_01() throws HarnessException {

		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		ZAssert.assertEquals(signature.getName(), this.sigName, "verified Text Signature is created");

		String subject = "subject" + ConfigProperties.getUniqueString();
		String bodyText = "text" + ConfigProperties.getUniqueString();
		String bodyHTML = "text <b>bold" + ConfigProperties.getUniqueString() + "</b> text";
		String contentHTML = XmlStringUtil
				.escapeXml("<html>" + "<head></head>" + "<body>" + bodyHTML + "<br></br>" + "</body>" + "</html>");

		// Send a message to the account
		ZimbraAccount.AccountZWC()
				.soapSend("<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>" + "<e t='t' a='"
						+ app.zGetActiveAccount().EmailAddress + "'/>" + "<su>" + subject + "</su>"
						+ "<mp ct='multipart/alternative'>" + "<mp ct='text/plain'>" + "<content>" + bodyText
						+ "</content>" + "</mp>" + "<mp ct='text/html'>" + "<content>" + contentHTML + "\n</content>"
						+ "</mp>" + "</mp>" + "</m>" + "</SendMsgRequest>");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);

		// Select message
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Forward the item
		actual.zPressButton(Button.B_FORWARD);
		ZAssert.assertTrue(
				actual.zGetMailPropertyAsText(DisplayMail.Field.Subject)
						.contains("Fwd"),
				"Verify Fwd Window");

		// Click Options Drop Down and select Signature
		app.zPageMail.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_ADD_FWD_SIGNATURE, this.sigName);

		// Verify Signature is place above included message.
		actual.zVerifySignaturePlaceInHTML("AboveIncludedMsg", this.sigBody, "Forward");
	}

	/**
	 * Test case : Verify Html Signature AboveIncludedMsg While Reply'ing Create
	 * Html signature through soap Send message through soap Select Same Msg and
	 * click Reply from toolbar Click Options dropdown and select Signature
	 * Verify signature should place above included message while Replying msg
	 * 
	 * @throws HarnessException
	 */
	@Test(description = "Verify Html Signature placed AboveIncludedMsg While Replying Msg", groups = { "functional", "L2" })
	public void ReplyMsgWithTextSignatureAboveIncludeMsg_02() throws HarnessException {

		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		ZAssert.assertEquals(signature.getName(), this.sigName, "verified Text Signature is created");

		String subject = "subject" + ConfigProperties.getUniqueString();
		String bodyText = "text" + ConfigProperties.getUniqueString();
		String bodyHTML = "text <b>bold" + ConfigProperties.getUniqueString() + "</b> text";
		String contentHTML = XmlStringUtil
				.escapeXml("<html>" + "<head></head>" + "<body>" + bodyHTML + "<br></br>" + "</body>" + "</html>");

		// Send a message to the account
		ZimbraAccount.AccountZWC()
				.soapSend("<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>" + "<e t='t' a='"
						+ app.zGetActiveAccount().EmailAddress + "'/>" + "<su>" + subject + "</su>"
						+ "<mp ct='multipart/alternative'>" + "<mp ct='text/plain'>" + "<content>" + bodyText
						+ "</content>" + "</mp>" + "<mp ct='text/html'>" + "<content>" + contentHTML + "\n</content>"
						+ "</mp>" + "</mp>" + "</m>" + "</SendMsgRequest>");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);

		// Select message
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Forward the item
		actual.zPressButton(Button.B_REPLY);
		ZAssert.assertTrue(
				actual.zGetMailPropertyAsText(DisplayMail.Field.Subject)
						.contains("Re"),
				"Verify Reply Window");

		// Click Options Drop Down and select Signature
		app.zPageMail.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_ADD_Reply_SIGNATURE, this.sigName);

		// Verify Signature is place above included message.
		actual.zVerifySignaturePlaceInHTML("AboveIncludedMsg", this.sigBody, "Reply");
	}

	/**
	 * Test case : Verify Html Signature placed AboveIncludedMsg While
	 * ReplyingAll Create Html signature through soap Send message through soap
	 * Select Same Msg and click ReplyAll from toolbar Click Options dropdown
	 * and select Signature Verify signature should place above included message
	 * while Replying msg
	 * 
	 * @throws HarnessException
	 */
	@Test(description = "Verify Html Signature placed AboveIncludedMsg While ReplyingAll", groups = { "functional", "L2" })
	public void ReplyAllMsgWithTextSignatureAboveIncludeMsg_03() throws HarnessException {

		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		ZAssert.assertEquals(signature.getName(), this.sigName, "verified Text Signature is created");

		String subject = "subject" + ConfigProperties.getUniqueString();
		String bodyText = "text" + ConfigProperties.getUniqueString();
		String bodyHTML = "text <b>bold" + ConfigProperties.getUniqueString() + "</b> text";
		String contentHTML = XmlStringUtil
				.escapeXml("<html>" + "<head></head>" + "<body>" + bodyHTML + "<br></br>" + "</body>" + "</html>");

		// Send a message to the account
		ZimbraAccount.AccountZWC()
				.soapSend("<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>" + "<e t='t' a='"
						+ app.zGetActiveAccount().EmailAddress + "'/>" + "<su>" + subject + "</su>"
						+ "<mp ct='multipart/alternative'>" + "<mp ct='text/plain'>" + "<content>" + bodyText
						+ "</content>" + "</mp>" + "<mp ct='text/html'>" + "<content>" + contentHTML + "\n</content>"
						+ "</mp>" + "</mp>" + "</m>" + "</SendMsgRequest>");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);

		// Select message
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Forward the item
		actual.zPressButton(Button.B_REPLYALL);
		ZAssert.assertTrue(
				actual.zGetMailPropertyAsText(DisplayMail.Field.Subject)
						.contains("Re"),
				"Verify Reply All Window");

		// Click Options Drop Down and select Signature
		app.zPageMail.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_ADD_ReplyAll_SIGNATURE, this.sigName);

		// Verify Signature is place above included message.
		actual.zVerifySignaturePlaceInHTML("AboveIncludedMsg", this.sigBody, "ReplyAll");
	}
}
