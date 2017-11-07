/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.main;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.*;

public class AboutDialog extends AjaxCommonTest {

	public AboutDialog() {
		logger.info("New "+ AboutDialog.class.getCanonicalName());
		super.startingPage = app.zPageMail;
	}


	@Test (description = "Open the 'About' dialog",
			groups = { "smoke", "L0" })

	public void AboutDialog_01() throws HarnessException {

		// Click the Account -> About menu
		DialogInformational dialog = (DialogInformational)app.zPageMain.zToolbarPressPulldown(Button.B_ACCOUNT, Button.O_ABOUT);

		//-- VERIFICATION
		ZAssert.assertTrue(dialog.zIsActive(), "Verify the About dialog opens");

		dialog.zClickButton(Button.B_OK);
	}
}