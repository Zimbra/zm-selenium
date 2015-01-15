/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.touch.tests.mail.mail.conversation;

import java.io.File;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByConversationTest;
import com.zimbra.qa.selenium.projects.touch.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.touch.ui.mail.FormMailNew.Field;

public class ForwardImageMail extends PrefGroupMailByConversationTest {

	public ForwardImageMail() {
		logger.info("New "+ ForwardImageMail.class.getCanonicalName());
	}
	
	@Bugs( ids = "81331")
	@Test( description = "Verify inline image present after hitting Forward from the mail",
			groups = { "sanity" })
			
	public void ForwardInlineImageMail_01() throws HarnessException {
		
		String subject = "inline image testing";		
		String MimeFolder = ZimbraSeleniumProperties.getBaseDirectory() + "/data/public/mime/email13/inline image.txt";
		LmtpInject.injectFile(ZimbraAccount.AccountZWC().EmailAddress, new File(MimeFolder));
		
		// Select the mail from inbox
		app.zPageMail.zToolbarPressButton(Button.B_FOLDER_TREE);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, "Inbox");
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressPulldown(Button.B_REPLY, Button.O_FORWARD);
		ZAssert.assertTrue(app.zPageMail.zVerifyBodyContent(), "Verify the content of the mail");
		ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageInReadingPane(), "Verify inline image showing in the reading pane");
		mailform.zToolbarPressButton(Button.B_CANCEL);
		
	}
	
	@Bugs( ids = "81331")
	@Test( description = "Forward a mail which contains inline image and verify it at the receipient side",
			groups = { "smoke" })
			
	public void ForwardInlineImageMail_02() throws HarnessException {
		
		String subject = "inline image testing";
		String startTextOfBody = "body of the inline image starts..";
		String endTextOfBody = "body of the inline image ends..";;
		String imgSrc = "cid:c44b200d9264f34d048f41c1280beee5b1e7dd38@zimbra";
		String modifiedContent = "modified body" + ZimbraSeleniumProperties.getUniqueString();
		
		String MimeFolder = ZimbraSeleniumProperties.getBaseDirectory() + "/data/public/mime/email13/inline image.txt";
		LmtpInject.injectFile(ZimbraAccount.AccountZWC().EmailAddress, new File(MimeFolder));
		
		// Select the mail from inbox
		app.zPageMail.zToolbarPressButton(Button.B_FOLDER_TREE);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, "Inbox");
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressPulldown(Button.B_REPLY, Button.O_FORWARD);
		ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageInComposedMessage(), "Verify image tag in the composed mail");
		
		mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);
		mailform.zFillField(Field.Body, modifiedContent);
		mailform.zSubmit();

		// Verify received mail
		ZimbraAccount.AccountB().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>" + "subject:(" + subject + ")</query>"
						+ "</SearchRequest>");
		String toid = ZimbraAccount.AccountB().soapSelectValue("//mail:m", "id");
		
		ZimbraAccount.AccountB().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + toid
						+ "' html='1'/>" + "</GetMsgRequest>");

		String tobody = ZimbraAccount.AccountB().soapSelectValue("//mail:content", null);
		ZAssert.assertStringContains(tobody, startTextOfBody, "Verify the start text of the body");
		ZAssert.assertStringContains(tobody, endTextOfBody, "Verify the end text of the body");
		ZAssert.assertStringContains(tobody, modifiedContent, "Verify the modified content");
		ZAssert.assertStringContains(tobody, imgSrc, "Verify the image tag");
		ZAssert.assertTrue(app.zPageMail.zVerifyBodyContent(), "Verify image tag in the composed mail");
		
	}
	
	@Bugs( ids = "81069")
	@Test( description = "Verify external image present after hitting Forward from the mail",
			groups = { "functional" })
			
	public void ForwardExternalImageMail_03() throws HarnessException {
		
		String subject = "external image testing";		
		String MimeFolder = ZimbraSeleniumProperties.getBaseDirectory() + "/data/public/mime/email13/external image.txt";
		LmtpInject.injectFile(ZimbraAccount.AccountZWC().EmailAddress, new File(MimeFolder));
		
		// Select the mail from inbox
		app.zPageMail.zToolbarPressButton(Button.B_FOLDER_TREE);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, "Inbox");
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		app.zPageMail.zToolbarPressButton(Button.B_LOAD_IMAGES);
		
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressPulldown(Button.B_REPLY, Button.O_FORWARD);
		//ZAssert.assertTrue(app.zPageMail.zVerifyBodyContent(), "Verify the content of the mail");
		ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageInReadingPane(), "Verify inline image in the reading pane");
		mailform.zToolbarPressButton(Button.B_CANCEL);
		
	}
	
	@Bugs( ids = "81069")
	@Test( description = "Forward a mail which contains external image and verify it at the receipient side",
			groups = { "functional" })
			
	public void ForwardExternalImageMail_04() throws HarnessException {
		
		String subject = "external image testing";
		String startTextOfBody = "body of the image starts..";
		String endTextOfBody = "body of the image ends..";
		String imgSrc = "http://fileswwwzimbracom.s3.amazonaws.com/_res/images/try/Try-Page-Collab-8-5.png";
		String modifiedContent = "modified body" + ZimbraSeleniumProperties.getUniqueString();
		
		String MimeFolder = ZimbraSeleniumProperties.getBaseDirectory() + "/data/public/mime/email13/external image.txt";
		LmtpInject.injectFile(ZimbraAccount.AccountZWC().EmailAddress, new File(MimeFolder));
		
		// Select the mail from inbox
		app.zPageMail.zToolbarPressButton(Button.B_FOLDER_TREE);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, "Inbox");
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		app.zPageMail.zToolbarPressButton(Button.B_LOAD_IMAGES);
		
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressPulldown(Button.B_REPLY, Button.O_FORWARD);
		ZAssert.assertTrue(app.zPageMail.zVerifyInlineImageInComposedMessage(), "Verify image tag in the composed mail");
		
		mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);
		mailform.zFillField(Field.Body, modifiedContent);
		mailform.zSubmit();

		// Verify received mail
		ZimbraAccount.AccountB().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>subject:(" + subject + ")</query>"
						+ "</SearchRequest>");
		String toid = ZimbraAccount.AccountB().soapSelectValue("//mail:m", "id");
		
		ZimbraAccount.AccountB().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + toid
						+ "' html='1'/>" + "</GetMsgRequest>");

		String tobody = ZimbraAccount.AccountB().soapSelectValue("//mail:content", null);
		ZAssert.assertStringContains(tobody, startTextOfBody, "Verify the start text of the body");
		ZAssert.assertStringContains(tobody, endTextOfBody, "Verify the end text of the body");
		ZAssert.assertStringContains(tobody, modifiedContent, "Verify the modified content");
		ZAssert.assertStringContains(tobody, imgSrc, "Verify the image tag");
		//ZAssert.assertTrue(app.zPageMail.zVerifyBodyContent(), "Verify image tag in the composed mail");
		
	}
	
}