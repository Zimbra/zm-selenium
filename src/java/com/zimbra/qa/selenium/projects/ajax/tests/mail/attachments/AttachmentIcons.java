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

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.AttachmentItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;

public class AttachmentIcons extends SetGroupMailByMessagePreference {

	public AttachmentIcons() throws HarnessException {
		logger.info("New "+ AttachmentIcons.class.getCanonicalName());
	}


	@Test (description = "Verify icon: ImgGenericDoc",
			groups = { "sanity" })

	public void ImgGenericDoc_01() throws HarnessException {

		// This mime contains an attachment that should map to ImgGenericDoc
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email05/mime01.txt";
		final String subject = "subject151615738";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

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


	@Test (description = "Verify icon: ImgImageDoc",
			groups = { "sanity" })

	public void ImgImageDoc_01() throws HarnessException {

		// This mime contains an attachment that should map to ImgGenericDoc
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email05/mime03.txt";
		final String subject = "subject13330659993903";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

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