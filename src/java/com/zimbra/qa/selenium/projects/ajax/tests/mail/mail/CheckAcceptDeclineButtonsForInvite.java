/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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

import java.io.File;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.LmtpInject;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;

public class CheckAcceptDeclineButtonsForInvite extends PrefGroupMailByMessageTest {

	public CheckAcceptDeclineButtonsForInvite() {
		logger.info("New "+ CheckAcceptDeclineButtonsForInvite.class.getCanonicalName());
	}
	
	@Bugs( ids = "21013")
	@Test( description = "Mail with ics attachment doesn't open appt (view-appt window breaks)", groups = { "functional" })
	
	public void CheckAcceptDeclineButtonsForInvite_01 () throws HarnessException {

		String subject = "all-hands";

		String MimeFolder = ConfigProperties.getBaseDirectory() + "/data/private/mime/Bugs/Bug21013";
		LmtpInject.injectFile(ZimbraAccount.AccountZWC().EmailAddress, new File(MimeFolder));

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		ZAssert.assertTrue(display.zHasADTButtons(), "Verify A/D/T buttons");
	}
}