/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.attachments;

import java.io.File;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.AttachmentItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;


public class AttachmentIcons extends PrefGroupMailByMessageTest {

	
	public AttachmentIcons() throws HarnessException {
		logger.info("New "+ AttachmentIcons.class.getCanonicalName());
		
	}
	
	
	@Test(	description = "Verify icon: ImgGenericDoc",
			groups = { "functional" })
	public void ImgGenericDoc_01() throws HarnessException {
		
		// This mime contains an attachment that should map to ImgGenericDoc
		final String mimeFile = ZimbraSeleniumProperties.getBaseDirectory() + "/data/public/mime/email05/mime01.txt";
		final String subject = "subject151615738";
		
		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));


		
		
		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		
		
		// Verify the icon appears
		boolean found = false;
		for ( AttachmentItem i : display.zListGetAttachments() ) {
			if ( i.getAttachmentIcon().equals(AttachmentItem.AttachmentIcon.ImgDoc) ) {
				found = true;
				break;
			}
		}
		ZAssert.assertTrue(found, "Verify the attachment icon appears");
		
		
	}


	@Test(	description = "Verify icon: ImgImageDoc",
			groups = { "functional" })
	public void ImgImageDoc_01() throws HarnessException {
		
		// This mime contains an attachment that should map to ImgGenericDoc
		final String mimeFile = ZimbraSeleniumProperties.getBaseDirectory() + "/data/public/mime/email05/mime03.txt";
		final String subject = "subject13330659993903";
		
		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));


		
		
		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		
		
		// Verify the icon appears
		boolean found = false;
		for ( AttachmentItem i : display.zListGetAttachments() ) {
			if ( i.getAttachmentIcon().equals(AttachmentItem.AttachmentIcon.ImgImageDoc) ) {
				found = true;
				break;
			}
		}
		ZAssert.assertTrue(found, "Verify the attachment icon appears");
		
		
	}



}
