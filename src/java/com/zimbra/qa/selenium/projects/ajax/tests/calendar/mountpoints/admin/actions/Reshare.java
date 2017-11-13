/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.mountpoints.admin.actions;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogShare;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;

public class Reshare extends AjaxCommonTest {

	public Reshare() {
		logger.info("New "+ Reshare.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs (ids = "36754")
	@Test (description = "Unwanted 'Accept Share' and 'Decline Share' buttons appeared in sent mail ",
			groups = { "smoke","L1" })

	public void Reshare_01() throws HarnessException {

		String mountPointName = "mountpoint" + ConfigProperties.getUniqueString();
		String mailSubject = "Share Created: Calendar shared by " + app.zGetActiveAccount().DisplayName;
		// Use system calendar folder
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Calendar);

		// Share it
		app.zGetActiveAccount().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ ZimbraAccount.Account7().EmailAddress +"' gt='usr' perm='rwidx' view='appointment'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Mount it
		ZimbraAccount.Account7().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountPointName +"'  rid='"+ folder.getId() +"' zid='"+ app.zGetActiveAccount().ZimbraId +"' view='appointment' color='4'/>"
				+	"</CreateMountpointRequest>");

		// Share it again
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Right click on folder, select "Share"
		DialogShare dialog = (DialogShare)app.zTreeCalendar.zTreeItem(Action.A_RIGHTCLICK, Button.B_SHARE, folder);
		ZAssert.assertNotNull(dialog, "Verify the sharing dialog pops up");

		// Use defaults for all options
		dialog.zSetEmailAddress(ZimbraAccount.Account8().EmailAddress);

		// Send it
		dialog.zPressButton(Button.B_OK);

        app.zPageMail.zNavigateTo();
		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Go to draft
		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);

		// Select the invitation and verify Accept/decline/Tentative buttons are not present
		DisplayMail display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, mailSubject);
		ZAssert.assertFalse(display.zHasShareADButtons(), "Verify A/D buttons");
	}
}