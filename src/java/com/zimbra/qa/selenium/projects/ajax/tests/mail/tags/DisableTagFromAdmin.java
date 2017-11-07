/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.tags;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;

public class DisableTagFromAdmin extends PrefGroupMailByMessageTest {

	public DisableTagFromAdmin() {
		logger.info("New "+ CreateTag.class.getCanonicalName());
	}


	@Test (description = "Bug # 80892 - Disable tag from admin and check in AJAX client",
			groups = { "functional", "L2" })

	public void DisableTagFromAdmin_01() throws HarnessException {

		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
						+		"<id>"+ZimbraAccount.Account10().ZimbraId +"</id>"
						+		"<a n='zimbraFeatureTaggingEnabled'>FALSE</a>"
						+	"</ModifyAccountRequest>");
		// Login to active account
		app.zPageLogin.zLogin(ZimbraAccount.Account10());

		// Verify that Tag is not present under tree
		ZAssert.assertFalse(app.zTreeMail.sIsElementPresent("css=td[id='ztih__main_Mail__TAG_textCell']") , "Tag is not present");

		// Open context menu
		app.zTreeMail.sClick("css=td[id='zb__NEW_MENU_dropdown'] ");
		SleepUtil.sleepSmall();

		// Verify tag not present under context menu
		ZAssert.assertFalse(app.zTreeMail.sIsElementPresent("css=td[id='zb__NEW_MENU_NEW_TAG_title']"), "Tag is not present under context menu");
	}
}