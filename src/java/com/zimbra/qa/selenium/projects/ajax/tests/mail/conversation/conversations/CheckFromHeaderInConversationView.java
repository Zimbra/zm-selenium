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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.conversation.conversations;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.LmtpInject;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class CheckFromHeaderInConversationView extends AjaxCommonTest {

	@SuppressWarnings("serial")
	public CheckFromHeaderInConversationView() {
		logger.info("New " + CheckFromHeaderInConversationView.class.getCanonicalName());

		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {{
				put("zimbraPrefGroupMailBy", "conversation");
				put("zimbraPrefMessageViewHtmlPreferred", "TRUE");
		}};
	}


	@Bugs(ids = "67986,64067,47288,16213")
	@Test (description = "Conversation list should show From=<blank>",
			groups = { "functional", "L2" })

	public void CheckFromHeaderInConversationView_01() throws HarnessException {

		String subject = "Encoding test";
		String to = "ljk20k00k1je";

		String MimeFolder = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug16213";
		LmtpInject.injectFile(ZimbraAccount.AccountZCS(), new File(MimeFolder));

		// Click on folder in the tree
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);

		List<MailItem> items = app.zPageMail.zListGetMessages();
		MailItem found = null;
		for (MailItem item : items) {
			if (item.gSubject.contains(subject)) {
				found = item;
				break;
			}
		}

		ZAssert.assertNotNull(found, "Verify the message exists in the list");
		ZAssert.assertStringDoesNotContain(found.gFrom, to, "Verify the To is not contained in the From");
	}
}