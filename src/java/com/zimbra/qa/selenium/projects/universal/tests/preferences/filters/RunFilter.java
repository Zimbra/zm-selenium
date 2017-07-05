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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.filters;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;

public class RunFilter extends AjaxCommonTest {

	public RunFilter() {
		super.startingPage = app.zPageMail;
		
	}

   	@Test( description = "Create an incoming filter, run it and check the results", groups = { "sanity", "L0"  } )
   	
	public void RunFilter_01() throws HarnessException {
   		
    	String subject1 = "subject1 "+ ConfigProperties.getUniqueString();
    	String subject2 = "subject2 "+ ConfigProperties.getUniqueString();
		String bodyText = "text" + ConfigProperties.getUniqueString();
		
		// Send two messages with different subjects to the account
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject1 +"</su>" +
						"<mp ct='multipart/alternative'>" +
						"<mp ct='text/plain'>" +
						"<content>"+ bodyText +"</content>" +
						"</mp>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");
		
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject2 +"</su>" +
						"<mp ct='multipart/alternative'>" +
						"<mp ct='text/plain'>" +
						"<content>"+ bodyText +"</content>" +
						"</mp>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");
		
		//Check that mails are present in Inbox
		//ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject1),"Mail is not present in Inbox!");
		//ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject2),"Mail is not present in Inbox!");	
		
		//Create a filter which checks the subject and Move it to Trash if subject matches
		
		String filterName = "MoveToTrash"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<ModifyFilterRulesRequest xmlns='urn:zimbraMail'>" 
						+	"<filterRules>" 
						+		"<filterRule active='1' name='" + filterName + "'>"				
						+			"<filterTests condition='anyof' >"
						+				"<headerTest header='subject' stringComparison='contains' value='"+subject1+"' />"    
						+			"</filterTests>"
						+			"<filterActions>"
						+				"<actionFileInto folderPath='Trash' />"
						+				"<actionStop />"
						+			"</filterActions>"
						+		"</filterRule>"
						+	"</filterRules>"
						+"</ModifyFilterRulesRequest>");
		
		
		//Go to preferences tab
		app.zPagePreferences.zNavigateTo();
		
		//Go Filters
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailFilters);
		
		//Get the inbox folder
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		//Get the sent folder
		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Sent);
		
		//Run the filter on Inbox  and sent folder
		app.zPagePreferences.zRunIncomingFilter(filterName, inbox,sent);
		
		//Go to mail tab to verify that mails are discarded and present in Trash		
		app.zPageMail.zNavigateTo();
		
		// Click on inbox folder
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);
		
		//Check that mail are filtered
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(subject1),"Mail is present in Inbox after filter run!");
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject2),"Mail is filtered out incorrectly after filter run!");		
		
		//Get the Trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		
		// Click on trash folder
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, trash);
		
		//Check that mail are filtered
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject1),"Mail is not present in Trash after filter run!");
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(subject2),"Mail is filtered out incorrectly after filter run!");
		
	}
    
    
   	@Test( description = "Create an Outgoinging filter, run it and check the results", groups = { "functional", "L3" } )
   	
	public void RunFilter_02() throws HarnessException {   	
    	
    	String subject3 = "subject3 "+ ConfigProperties.getUniqueString();
    	String subject4 = "subject4 "+ ConfigProperties.getUniqueString();
		String bodyText = "text" + ConfigProperties.getUniqueString();
		
		// Send two messages from active account to two different accounts
		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ ZimbraAccount.Account7().EmailAddress +"'/>" +
						"<su>"+ subject3 +"</su>" +
						"<mp ct='multipart/alternative'>" +
						"<mp ct='text/plain'>" +
						"<content>"+ bodyText +"</content>" +
						"</mp>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");
		
		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ ZimbraAccount.Account8().EmailAddress +"'/>" +
						"<su>"+ subject4 +"</su>" +
						"<mp ct='multipart/alternative'>" +
						"<mp ct='text/plain'>" +
						"<content>"+ bodyText +"</content>" +
						"</mp>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");
		
		//Check that the mails are sent successfully and present in sent folder		
		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Sent);
		
		// Click on inbox folder
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
		
		//Check that mails are present in Sent folder
		//ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject3),"Mail is not present in Inbox!");
		//ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject4),"Mail is not present in Inbox!");	
		
		//Create a filter which checks the To field and Move it to Trash if To field matches
		
		String filterName = "MoveToTrashOut"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<ModifyOutgoingFilterRulesRequest xmlns='urn:zimbraMail'>" 
						+	"<filterRules>" 
						+		"<filterRule active='1' name='" + filterName + "'>"				
						+			"<filterTests condition='anyof' >"
						+				"<headerTest header='To' stringComparison='contains' value='" + ZimbraAccount.Account7().EmailAddress + "' />"    
						+			"</filterTests>"
						+			"<filterActions>"
						+				"<actionFileInto folderPath='Trash' />"
						+				"<actionStop />"
						+			"</filterActions>"
						+		"</filterRule>"
						+	"</filterRules>"
						+"</ModifyOutgoingFilterRulesRequest>");
		
		
		//Go to preferences tab
		app.zPagePreferences.zNavigateTo();
		
		//Go Filters
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailFilters);
		
		//Run the filter on sent folder
		app.zPagePreferences.zRunOutgoingFilter(filterName, sent);
		
		//Go to mail tab to verify that mails are moved to Trash		
		app.zPageMail.zNavigateTo();		
		
		// Click on inbox folder
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
		
		//Check that mail are filtered
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(subject3),"Mail is still present in Sent after filter run!");
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject4),"Mail is filtered out incorrectly after filter run!");		
		
		//Get the Trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		
		// Click on trash folder
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, trash);
		
		//Check that mail are filtered
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject3),"Mail is not present in Trash after filter run!");
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(subject4),"Mail is filtered out incorrectly after filter run!");
		
	}
    
}
