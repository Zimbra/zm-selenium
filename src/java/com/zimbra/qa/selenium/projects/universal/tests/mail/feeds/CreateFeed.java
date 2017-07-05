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
package com.zimbra.qa.selenium.projects.universal.tests.mail.feeds;

import java.net.*;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DialogCreateFolder;

public class CreateFeed extends PrefGroupMailByMessageTest {

	public CreateFeed() {
		logger.info("New "+ CreateFeed.class.getCanonicalName());
	}

	@Test( description = "Create a new feed by clicking 'new folder' on folder tree", groups = { "smoke", "L1" })

	public void CreateFeed_01() throws HarnessException, MalformedURLException {

		String foldername = "folder" + ConfigProperties.getUniqueString();

		// feed.rss=http://server/files/Service/RSS/Basic/basic.xml
		String feed = ConfigProperties.getStringProperty("rss.sample");

		// Click on the "new folder" button
		DialogCreateFolder createFolderDialog = (DialogCreateFolder)app.zTreeMail.zPressButton(Button.B_TREE_NEWFOLDER);

		createFolderDialog.zEnterFolderName(foldername);
		createFolderDialog.zClickSubscribeFeed(true);
		createFolderDialog.zEnterFeedURL(new URL(feed));

		createFolderDialog.zClickButton(Button.B_OK);

		// Make sure the folder was created on the ZCS server
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);
		ZAssert.assertNotNull(folder, "Verify the feed exists on the server");

		ZAssert.assertEquals(folder.getName(), foldername, "Verify the server and client feed names match");

	}
}