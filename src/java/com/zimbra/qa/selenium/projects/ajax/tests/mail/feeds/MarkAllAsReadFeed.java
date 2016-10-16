/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.feeds;

import java.net.*;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;

public class MarkAllAsReadFeed extends PrefGroupMailByMessageTest {

	public MarkAllAsReadFeed() {
		logger.info("New "+ MarkAllAsReadFeed.class.getCanonicalName());
	}

	@Test( description = "Mark all messages as read in folder (context menu)",	groups = { "smoke" })

	public void MarkAllAsReadFolder_01() throws HarnessException, MalformedURLException {

		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox);

		// Create a subfolder in Inbox
		String feedname = "feed" + ConfigProperties.getUniqueString();
		// feed.rss=http://server/files/Service/RSS/Basic/basic.xml
		URL feedurl = new URL(ConfigProperties.getStringProperty("rss.sample"));

		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='"+ feedname +"' l='"+ inbox.getId() +"' url='"+ feedurl.toString() +"'/>"
				+	"</CreateFolderRequest>");

		FolderItem feed = FolderItem.importFromSOAP(app.zGetActiveAccount(), feedname);
		ZAssert.assertNotNull(feed, "Verify the subfolder is available");


		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);


		// Right click on folder, select "Mark all as read"
		app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_FOLDER_MARKASREAD, feed);
	}

}
