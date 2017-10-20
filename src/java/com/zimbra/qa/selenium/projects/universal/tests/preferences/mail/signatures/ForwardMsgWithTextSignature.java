/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.mail.signatures;

import java.util.HashMap;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.SignatureItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew.Field;

public class ForwardMsgWithTextSignature extends UniversalCommonTest {
	String sigName = "signame" + ConfigProperties.getUniqueString();
	String sigBody = "sigbody" + ConfigProperties.getUniqueString();

	@SuppressWarnings("serial")
	public ForwardMsgWithTextSignature() {
		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {
			{
				put("zimbraPrefComposeFormat", "text");
				put("zimbraPrefGroupMailBy", "message");
			}
		};
	}

	@BeforeMethod(groups = { "always" })
	public void CreateSignature() throws HarnessException {
		ZimbraAccount.AccountZCS().authenticate();
		ZimbraAccount.AccountZCS()
				.soapSend("<CreateSignatureRequest xmlns='urn:zimbraAccount'>" + "<signature name='" + this.sigName
						+ "' >" + "<content type='text/plain'>" + this.sigBody + "</content>" + "</signature>"
						+ "</CreateSignatureRequest>");

		// Refresh UI
		app.zPageMain.sRefresh();

		logger.info("CreateSignature: finish");
	}

	/**
	 * Test case : Forward Msg with text signature and Verify signature through
	 * soap Create signature through soap Send message with text signature
	 * through soap Fwd same message to another account say (accountB()) Verify
	 * signature in forwarded msg through soap
	 * 
	 * @throws HarnessException
	 */
	@Test(description = " Forward Msg with text signature and Verify signature through soap", groups = { "functional", "L2" })
	public void ForwardMsgWithTextSignature_01() throws HarnessException {

		// Signature is created
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		ZAssert.assertEquals(signature.getName(), this.sigName, "verified Text Signature is created");

		String subject = "subject" + ConfigProperties.getUniqueString();

		// Send a message to the account(self)
		ZimbraAccount.AccountZCS()
				.soapSend("<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>" + "<e t='t' a='"
						+ app.zGetActiveAccount().EmailAddress + "'/>" + "<su>" + subject + "</su>"
						+ "<mp ct='text/plain'>" + "<content>content" + ConfigProperties.getUniqueString() + "\n\n"
						+ signature.dBodyText + "\n</content>" + "</mp>" + "</m>" + "</SendMsgRequest>");

		// Get the mail item for the new message
		MailItem mail = MailItem.importFromSOAP(ZimbraAccount.AccountZCS(), "in:inbox subject:(" + subject + ")");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Forward the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);

		// Send the message
		mailform.zSubmit();

		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:(" + mail.dSubject + ")");

		// Verify TO, Subject, Body,Signature
		ZAssert.assertStringContains(received.dSubject, "Fwd", "Verify the subject field contains the 'Fwd' prefix");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress,
				"Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountB().EmailAddress,
				"Verify the to field is correct");
		ZAssert.assertStringContains(received.dBodyText, mail.dBodyText, "Verify the body content is correct");
		ZAssert.assertStringContains(received.dBodyText, this.sigBody, "Verify the signature is correct");

	}
}
