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
import com.zimbra.qa.selenium.framework.items.AttachmentItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.SeparateWindowOpenAttachment;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;

public class OpenAttachment extends SetGroupMailByMessagePreference {

	public OpenAttachment() throws HarnessException {
		logger.info("New "+ OpenAttachment.class.getCanonicalName());
	}


	@Test (description = "Open a text attachment",
			groups = { "functional", "L2" })

	public void OpenTextAttachment_01() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email05/mime01.txt";
		final String subject = "subject151615738";
		final String attachmentname = "file.txt";
		final String attachmentcontent = "Text Attachment Content";

		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFile));

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

		SeparateWindowOpenAttachment window = null;

		try {

			// Left click on the attachment
			window = (SeparateWindowOpenAttachment)display.zListAttachmentItem(Action.A_LEFTCLICK, item);

			for (String winHandle : webDriver.getWindowHandles()) {
				webDriver.switchTo().window(winHandle);
			}

			// Verify show original window with proper content
			String content = window.sGetBodyText();
			ZAssert.assertStringContains(content, attachmentcontent, "Verify the content in the attachment");

		} finally {
			app.zPageMain.zCloseWindow(window, app);
		}
	}
}