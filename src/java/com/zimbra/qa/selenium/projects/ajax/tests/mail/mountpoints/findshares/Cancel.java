/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mountpoints.findshares;


import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogShareFind;


public class Cancel extends PrefGroupMailByMessageTest {

	
	public Cancel() {
		logger.info("New "+ Cancel.class.getCanonicalName());
		
		
		
		
		
	}
	
	@Test(	description = "Open the find shares dialog.  Cancel the dialog.",
			groups = { "functional" })
	public void CancelFindShares_01() throws HarnessException {
		

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);
		
		// Click the inbox
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);
				
		// Click Find Shares
		DialogShareFind dialog = (DialogShareFind)app.zTreeMail.zPressPulldown(Button.B_TREE_FOLDERS_OPTIONS, Button.B_TREE_FIND_SHARES);
		
		
		// Close the dialog box
		dialog.zClickButton(Button.B_CANCEL);

	}

	
	

	

}
