/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.folders.external;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.AjaxPages;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.*;

public class CreateExternalCalendarOther extends AjaxCore {

	public CreateExternalCalendarOther() {
		logger.info("New " + CreateExternalCalendarOther.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs (ids = "66576")
	@Test (description = "Create a new external calendar (type=other) by clicking 'Gear' -> 'new folder' on folder tree",
			groups = { "sanity" })

	public void CreateExternalCalendarOther_01() throws HarnessException {

		ZimbraAccount icalAccount = new ZimbraAccount();
		icalAccount.provision();
		icalAccount.authenticate();

		// Set the new calendar name
		String calendarname = "calendar" + ConfigProperties.getUniqueString();

		// Click on the "New Calendar" button in the calendar tree
		DialogAddExternalCalendar dialog = (DialogAddExternalCalendar) app.zTreeCalendar.zPressPulldown(Button.B_TREE_FOLDERS_OPTIONS, Button.B_TREE_NEW_EXTERNAL_CALENDAR);

		// Fill out the dialog
		dialog.zSetSourceType(DialogAddExternalCalendar.SourceType.Other);
		dialog.zPressButton(Button.B_NEXT);

		// Fill out the external calendar
		dialog.zSetSourceEmailAddress(icalAccount.EmailAddress);
		dialog.zSetSourcePassword(icalAccount.Password);
		dialog.zSetSourceServer(icalAccount.zGetAccountStoreHost());
		dialog.zPressButton(Button.B_NEXT);

		DialogCreateCalendarFolder dailog2 = new DialogCreateCalendarFolder(app, ((AjaxPages) app).zPageCalendar);
		dailog2.zWaitForActive();
		dailog2.zEnterFolderName(calendarname);
		dailog2.zPressButton(Button.B_OK);


		// Make sure the folder was created on the ZCS server
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), calendarname);
		ZAssert.assertNotNull(folder, "Verify the new folder is found");
		ZAssert.assertEquals(folder.getName(), calendarname, "Verify the server and client folder names match");
	}

}