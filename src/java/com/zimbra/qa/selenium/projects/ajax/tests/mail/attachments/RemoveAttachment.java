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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.attachments;

import java.io.File;
import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;

public class RemoveAttachment extends PrefGroupMailByMessageTest {

	public RemoveAttachment() throws HarnessException {
		logger.info("New "+ RemoveAttachment.class.getCanonicalName());
	}


	@Test (description = "Remove an attachment from a mail",
			groups = { "smoke", "L1" })

	public void RemoveAttachment_01() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email05/mime01.txt";
		final String subject = "subject151615738";
		final String attachmentname = "file.txt";
		ZimbraAccount account = app.zGetActiveAccount();

		// Inject the message
		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFile));

		// Double check that there is an attachment
		account.soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ subject +")</query>"
			+	"</SearchRequest>");
		String id = account.soapSelectValue("//mail:m", "id");

		account.soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail' >"
			+		"<m id='"+ id +"'/>"
			+	"</GetMsgRequest>");

		Element[] nodes = account.soapSelectNodes("//mail:mp[@cd='attachment']");
		ZAssert.assertGreaterThan(nodes.length, 0, "Verify the message has the attachment");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		AttachmentItem item = null;
		List<AttachmentItem> items = display.zListGetAttachments();
		for (AttachmentItem i : items) {
			if ( i.getAttachmentName().equals(attachmentname)) {
				item = i;
				break;
			}
		}
		ZAssert.assertNotNull(item, "Verify one attachment is in the message");

		// Click remove
		DialogWarning dialog = (DialogWarning)display.zListAttachmentItem(Button.B_REMOVE, item);
		dialog.zPressButton(Button.B_YES);

		// Verify the message no longer has an attachment
		account.soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ subject +")</query>"
			+	"</SearchRequest>");
		id = account.soapSelectValue("//mail:m", "id");

		try {

			int i = 0;
			do {
				SleepUtil.sleepSmall();

		    	account.soapSend(
		    			  "<GetMsgRequest xmlns='urn:zimbraMail' >"
		    			+   "<m id='"+ id +"'/>"
		    			+ "</GetMsgRequest>");
		    	nodes = account.soapSelectNodes("//mail:mp[@cd='attachment']");

			} while ( (i++ < 10) && (nodes.length > 0) );

		} catch(Exception ex) {
		    logger.error(ex);
		}

		ZAssert.assertEquals(nodes.length, 0, "Verify the message no longer has the attachment");
	}


	@Test (description = "Remove all attachments (2 attachments) from a mail",
			groups = { "functional", "L2" })

	public void RemoveAttachment_02() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email06/mime.txt";
		final String subject = "subject135219672356274";
		ZimbraAccount account = app.zGetActiveAccount();

		// Inject the message
		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFile));

		// Double check that there is an attachment
		account.soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ subject +")</query>"
			+	"</SearchRequest>");
		String id = account.soapSelectValue("//mail:m", "id");

		account.soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail' >"
			+		"<m id='"+ id +"'/>"
			+	"</GetMsgRequest>");
		Element[] nodes = account.soapSelectNodes("//mail:mp[@cd='attachment']");
		ZAssert.assertGreaterThan(nodes.length, 0, "Verify the message has the attachment");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		ZAssert.assertNotNull(display, "Verify the message shows");

		// Click remove
		DialogWarning dialog = (DialogWarning)display.zPressButton(Button.B_REMOVE_ALL);
		dialog.zPressButton(Button.B_YES);

		// Verify the message no longer has an attachment
		account.soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ subject +")</query>"
			+	"</SearchRequest>");
		id = account.soapSelectValue("//mail:m", "id");

		try {

			int i = 0;
			do {
				SleepUtil.sleepSmall();

		    	account.soapSend(
		    			  "<GetMsgRequest xmlns='urn:zimbraMail' >"
		    			+   "<m id='"+ id +"'/>"
		    			+ "</GetMsgRequest>");
		    	nodes = account.soapSelectNodes("//mail:mp[@cd='attachment']");

			} while ( (i++ < 10) && (nodes.length > 0) );


		} catch(Exception ex) {
		    logger.error(ex);
		}

		ZAssert.assertEquals(nodes.length, 0, "Verify the message no longer has the attachment");
	}


	@Bugs( ids = "81565")
	@Test (description = "Remove an attachment from a meeting invite",
			groups = { "functional", "L2" })

	public void RemoveAttachment_03() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug81565/mime1.txt";
		final String subject = "Bug81565";
		final String attachmentname = "Capture.PNG";
		ZimbraAccount account = app.zGetActiveAccount();

		// Inject the message
		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFile));

		// Double check that there is an attachment
		account.soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ subject +")</query>"
			+	"</SearchRequest>");
		String id = account.soapSelectValue("//mail:m", "id");

		account.soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail' >"
			+		"<m id='"+ id +"'/>"
			+	"</GetMsgRequest>");
		Element[] nodes = account.soapSelectNodes("//mail:mp[@cd='attachment']");
		ZAssert.assertGreaterThan(nodes.length, 0, "Verify the message has the attachment");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		AttachmentItem item = null;
		List<AttachmentItem> items = display.zListGetAttachments();
		for (AttachmentItem i : items) {
			if ( i.getAttachmentName().equals(attachmentname)) {
				item = i;
				break;
			}
		}
		ZAssert.assertNotNull(item, "Verify one attachment is in the message");

		// Click remove
		DialogWarning dialog = (DialogWarning)display.zListAttachmentItem(Button.B_REMOVE, item);
		dialog.zPressButton(Button.B_YES);

		// Verify the message no longer has an attachment
		account.soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ subject +")</query>"
			+	"</SearchRequest>");
		id = account.soapSelectValue("//mail:m", "id");

		try {

			int i = 0;
			do {
				SleepUtil.sleepSmall();

		    	account.soapSend(
		    			  "<GetMsgRequest xmlns='urn:zimbraMail' >"
		    			+   "<m id='"+ id +"'/>"
		    			+ "</GetMsgRequest>");
		    	nodes = account.soapSelectNodes("//mail:mp[@cd='attachment']");

			} while ( (i++ < 10) && (nodes.length > 0) );


		} catch(Exception ex) {
		    logger.error(ex);
		}

		ZAssert.assertEquals(nodes.length, 0, "Verify the message no longer has the attachment");
	}


	@Bugs( ids = "81565")
	@Test (description = "Remove all attachments (2 attachments) from a meeting invite",
			groups = { "functional", "L3" })

	public void RemoveAttachment_04() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug81565/mime2.txt";
		final String subject = "Bug81565B";
		ZimbraAccount account = app.zGetActiveAccount();

		// Inject the message
		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFile));

		// Double check that there is an attachment
		account.soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ subject +")</query>"
			+	"</SearchRequest>");
		String id = account.soapSelectValue("//mail:m", "id");

		account.soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail' >"
			+		"<m id='"+ id +"'/>"
			+	"</GetMsgRequest>");
		Element[] nodes = account.soapSelectNodes("//mail:mp[@cd='attachment']");
		ZAssert.assertGreaterThan(nodes.length, 0, "Verify the message has the attachment");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		ZAssert.assertNotNull(display, "Verify the message shows");

		// Click remove
		DialogWarning dialog = (DialogWarning)display.zPressButton(Button.B_REMOVE_ALL);
		dialog.zPressButton(Button.B_YES);

		// Verify the message no longer has an attachment
		account.soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ subject +")</query>"
			+	"</SearchRequest>");
		id = account.soapSelectValue("//mail:m", "id");

		try {

			int i = 0;
			do {
				SleepUtil.sleepSmall();

		    	account.soapSend(
		    			  "<GetMsgRequest xmlns='urn:zimbraMail' >"
		    			+   "<m id='"+ id +"'/>"
		    			+ "</GetMsgRequest>");
		    	nodes = account.soapSelectNodes("//mail:mp[@cd='attachment']");

			} while ( (i++ < 10) && (nodes.length > 0) );

		} catch(Exception ex) {
		    logger.error(ex);
		}

		ZAssert.assertEquals(nodes.length, 0, "Verify the message no longer has the attachment");
	}


	@Bugs(ids = "83243")
	@Test (description = "Remove an attachment from a mail from Show Conversation view",
			groups = { "functional", "L3" })

	public void RemoveAttachment_05() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email16/mime02.txt";
		final String subject = "remove attachment from conversation view";
		final String attachmentname = "remove.txt";
		ZimbraAccount account = app.zGetActiveAccount();

		// Inject the message
		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFile));

		// Double check that there is an attachment
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='message'>" + "<query>subject:(" + subject
				+ ")</query>" + "</SearchRequest>");
		String id = account.soapSelectValue("//mail:m", "id");

		account.soapSend("<GetMsgRequest xmlns='urn:zimbraMail' >" + "<m id='" + id + "'/>" + "</GetMsgRequest>");
		Element[] nodes = account.soapSelectNodes("//mail:mp[@cd='attachment']");
		ZAssert.assertGreaterThan(nodes.length, 0, "Verify the message has the attachment");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Go to Actions drop down and select Show Conversation.
		app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.O_SHOW_CONVERSATION);

		// Check the presence of attachment
		AttachmentItem item = null;
		List<AttachmentItem> items = display.zListGetAttachments();
		for (AttachmentItem i : items) {
			if (i.getAttachmentName().equals(attachmentname)) {
				item = i;
				break;
			}
		}
		ZAssert.assertNotNull(item, "No attachment is in the message");

		// Click remove
		DialogWarning dialog = (DialogWarning) display.zListAttachmentItem(Button.B_REMOVE, item);
		dialog.zPressButton(Button.B_YES);

		// Verification through UI

		// Close the show conversation tab
		app.zPageMail.zToolbarPressButton(Button.B_CLOSE_CONVERSATION);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		//Get attachments
		items = display.zListGetAttachments();

		// Check the count of attachments
		ZAssert.assertTrue(items.size()==0, "Attachment is still present in the message");

		// Verification through SOAP

		// Verify the message no longer has an attachment through SOAP request
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='message'>" + "<query>subject:(" + subject
				+ ")</query>" + "</SearchRequest>");
		id = account.soapSelectValue("//mail:m", "id");

		try {

			int i = 0;
			do {
				SleepUtil.sleepSmall();

				account.soapSend(
						"<GetMsgRequest xmlns='urn:zimbraMail' >" + "<m id='" + id + "'/>" + "</GetMsgRequest>");
				nodes = account.soapSelectNodes("//mail:mp[@cd='attachment']");

			} while ((i++ < 10) && (nodes.length > 0));

		} catch (Exception ex) {
			logger.error(ex);
		}

		ZAssert.assertEquals(nodes.length, 0, "The message still has the attachment");
	}
}