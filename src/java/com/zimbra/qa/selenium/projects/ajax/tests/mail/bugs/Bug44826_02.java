/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.bugs;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.AppAjaxClient;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;

public class Bug44826_02 extends  AjaxCommonTest {
	
	public Bug44826_02() {
		logger.info("New "+ Bug44826_02.class.getCanonicalName());
		
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox", "TRUE");
		

	}
	
	@Test(	description = "Verify bug 44826",
			groups = { "functional" })
	public void Bug44826() throws HarnessException {
		
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		// Create a message in trash to move
		String subject1 = "subject1" + ZimbraSeleniumProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='"+ trash.getId() +"' >"
        	+			"<content>From: foo@foo.com\n"
        	+				"To: foo@foo.com \n"
        	+				"Subject: "+ subject1 +"\n"
        	+				"MIME-Version: 1.0 \n"
        	+				"Content-Type: text/plain; charset=utf-8 \n"
        	+				"Content-Transfer-Encoding: 7bit\n"
        	+				"\n"
        	+				"simple text string in the body\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");
		
		// Create a message in trash to move
		String subject2 = "subject2" + ZimbraSeleniumProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='"+ trash.getId() +"' >"
        	+			"<content>From: foo@foo.com\n"
        	+				"To: foo@foo.com \n"
        	+				"Subject: "+ subject2 +"\n"
        	+				"MIME-Version: 1.0 \n"
        	+				"Content-Type: text/plain; charset=utf-8 \n"
        	+				"Content-Transfer-Encoding: 7bit\n"
        	+				"\n"
        	+				"simple text string in the body\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

		// Import each message into MailItem objects
		MailItem mail1 = MailItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:("+ subject1 +")");
		MailItem mail2 = MailItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:("+ subject2 +")");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		// Remember to close the search view
		try {
			
			// Search for the message
			app.zPageSearch.zAddSearchQuery("in:trash from:foo@foo.com");
			app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);
		
			// Select all
			app.zPageSearch.zToolbarPressButton(Button.B_SELECT_ALL);
		
			// Click delete
			app.zPageSearch.zToolbarPressButton(Button.B_DELETE);
			
			// Warning dialog will appear
			DialogWarning dialog = new DialogWarning(DialogWarning.DialogWarningID.PermanentlyDeleteTheItem,
											app,
											((AppAjaxClient) app).zPageSearch);
			ZAssert.assertTrue(dialog.zIsActive(), "Verify the warning dialog opens");
			dialog.zClickButton(Button.B_OK);
			
		} finally {
			// Remember to close the search view
			app.zPageSearch.zClose();
			// Select the trash
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Trash));
		}
	
		//-- VERIFICATION 
		//
		
		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem found1 = null;
		MailItem found2 = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking at: "+ m.gSubject);
			if ( mail1.dSubject.equals(m.gSubject) ) {
				found1 = m;
			}
			if ( mail2.dSubject.equals(m.gSubject) ) {
				found2 = m;
			}
		}
		ZAssert.assertNull(found1, "Verify the message "+ mail1.dSubject +" is no longer in the Trash");
		ZAssert.assertNull(found2, "Verify the message "+ mail2.dSubject +" is no longer in the Trash");
		
	}

}
