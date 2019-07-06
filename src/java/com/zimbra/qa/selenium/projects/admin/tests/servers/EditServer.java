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
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.admin.tests.servers;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.ExecuteHarnessMain;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditServer;

public class EditServer extends AdminCore {

	public EditServer() {
		logger.info("New "+ EditServer.class.getCanonicalName());
		super.startingPage = app.zPageManageServers;
	}

	/**
	 * Testcase : Edit Server
	 * Steps :
	 * 1. Go to "Home > Configure > Servers"
	 * 2. Click on Server > select Edit from gear icon
	 * 3. Edit description and notes > Save
	 * 4. Verify details are edited successfully
	 * @throws HarnessException
	 */

	@Test (description = "Edit Server from gear menu option",
			groups = { "bhr" })

	public void EditServer_01() throws HarnessException {

		String storeServer = ExecuteHarnessMain.storeServers.get(0);

		// Select server
		app.zPageManageServers.zListItem(Action.A_LEFTCLICK, storeServer);

		// Click on Edit server option
		FormEditServer form = (FormEditServer) app.zPageManageServers.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.B_EDIT);

		String description = "description_" + ConfigProperties.getUniqueString();
		String notes = "notes" + ConfigProperties.getUniqueString();

		// Edit description
		form.zSetDescription(description);

		// Edit notes
		form.zSetNotes(notes);

		// Submit the form
		form.zSubmit();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetServerRequest xmlns='urn:zimbraAdmin'>"
						+	"<server by='name'>" + storeServer + "</server>"
						+		"</GetServerRequest>");

		// Verify description is edited correctly
		Element edited_description = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:a[@n='description']", 1);
		ZAssert.assertEquals(edited_description.getText(),description, "Verify description is edited correctly!");

		// Verify notes are edited correctly
		Element edited_notes = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:a[@n='zimbraNotes']", 1);
		ZAssert.assertEquals(edited_notes.getText(),notes, "Verify notes are edited correctly!");
	}
}