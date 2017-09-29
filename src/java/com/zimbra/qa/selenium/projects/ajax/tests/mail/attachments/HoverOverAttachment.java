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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.attachments;

import java.io.*;
import java.util.*;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.*;
import com.zimbra.qa.selenium.projects.ajax.ui.TooltipImage.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.*;


public class HoverOverAttachment extends PrefGroupMailByMessageTest {



	public HoverOverAttachment() {
		logger.info("New "+ HoverOverAttachment.class.getCanonicalName());

	}

	@DataProvider(name = "DataProviderMimeWithImageAttachments")
	public Object[][] DataProviderDeleteKeys() {
		return new Object[][] {
				new Object[] { "subject13715117780534", "/data/public/mime/email12/mime01.txt" },
				new Object[] { "subject13715024766995", "/data/public/mime/email12/mime02.txt" },
				new Object[] { "subject13715024846237", "/data/public/mime/email12/mime03.txt" },
				//new Object[] { "subject13715020881915", "/data/public/mime/email12/mime04.txt" }, /As per bug fix 82807 tool-tips for tiff will not be shown
		};
	}

	@Bugs( ids = "82807")
	@Test( description = "Hover over an image attachment",
			dataProvider = "DataProviderMimeWithImageAttachments",
			groups = { "functional", "L2" })
	public void HoverOverAttachment_01(String subject, String path) throws HarnessException {

		//-- DATA


		final String mimeFile = ConfigProperties.getBaseDirectory() + path;
		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFile));




		//-- GUI

		// Click on Get Mail to refresh the folder list
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message
		DisplayMail display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		List<AttachmentItem> attachments = display.zListGetAttachments();
		ZAssert.assertEquals(attachments.size(), 1, "Verify the attachment appears");

		TooltipImage tooltip = (TooltipImage)display.zListAttachmentItem(Action.A_HOVEROVER, attachments.get(0));


		//-- VERIFICATION

		ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");
		ZAssert.assertNotNull(tooltip.zGetField(Field.URL), "Verify the image URL");

	}	



}
