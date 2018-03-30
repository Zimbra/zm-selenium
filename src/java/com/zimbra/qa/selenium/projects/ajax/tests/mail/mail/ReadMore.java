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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mail;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;

public class ReadMore extends SetGroupMailByMessagePreference {

	public ReadMore() throws HarnessException {
		logger.info("New "+ ReadMore.class.getCanonicalName());
	}


	@Test (description = "Use the 'Read More' button to scroll through the message content",
			groups = { "functional", "L2" })

	public void ReadMore_01() throws HarnessException {

		final String subject = "ReadMore13674340693102";
		final String mimeFolder = ConfigProperties.getBaseDirectory() + "/data/public/mime/email11";

		// Inject sample mimes
		for (int i=0; i<=6; i++) {
			injectMessage(app.zGetActiveAccount(), mimeFolder + "/mime0" + i + ".txt");
		}

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Click on the "Read More" button
		app.zPageMail.zToolbarPressButton(Button.B_READMORE);

		// TODO: not sure how to verify that the scrollbar has moved?
	}
}