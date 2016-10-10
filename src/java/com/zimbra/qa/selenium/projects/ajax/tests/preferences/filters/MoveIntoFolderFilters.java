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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.filters;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.DialogEditFilter;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.DialogEditFilter.ConditionConstraint;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.DialogEditFilter.ConditionType;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.DialogEditFilter.FilterAction;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.PagePreferences;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;


public class MoveIntoFolderFilters extends AjaxCommonTest {

	public MoveIntoFolderFilters() {
		
		super.startingPage = app.zPagePreferences;
		
		
	}

	@Test(
			description = "Create an Incoming Message Filter having criterion 'From - Contains - X - Move into Folder Y'",
			groups = { "functional" }
			)
	public void MoveIntoFolderFilters_01() throws HarnessException {

		String filterName = "FromContains "+ ConfigProperties.getUniqueString();
		String conditionValue[] = ZimbraAccount.AccountC().EmailAddress.split("@");
		
		String subject1 = "subject1From "+ ConfigProperties.getUniqueString();
    	String subject2 = "subject2From "+ ConfigProperties.getUniqueString();
		String bodyText = "text" + ConfigProperties.getUniqueString();
		
		//Create a folder
		String folderName = "FromContains";  //		
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailFilters);
		
		// Click "Add New"
		DialogEditFilter dialog = (DialogEditFilter)app.zPagePreferences.zToolbarPressButton(Button.B_NEW_IN_FILTER);		
		
		// Give a name
		dialog.zSetFilterName(filterName);
		
		// Give a criteria
		dialog.zAddFilterCriteria(ConditionType.From, ConditionConstraint.Contains, conditionValue[0]);	
		
		// Give an action (if necessary)
		dialog.zAddFilterAction(FilterAction.MoveIntoFolder,folderName);

		// Save
		dialog.zClickButton(Button.B_OK);		
		
		// Verify the filter is created through SOAP
		app.zGetActiveAccount().soapSend(
						"<GetFilterRulesRequest xmlns='urn:zimbraMail'/>");
		
		Element[] rules = app.zGetActiveAccount().soapSelectNodes("//mail:GetFilterRulesResponse//mail:filterRule[@name='" + filterName +"']");
		ZAssert.assertEquals(rules.length, 1, "Verify the Incoming filter rule exists in the server");
		
		//Verify that filter is created through UI
		
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(PagePreferences.Locators.zFilterRowCss +":contains("+ filterName +")"), "Incoming filter is not created successfully!");
		
		//Verify that mails are going in the correct folder
		
		// Send two messages from two different account
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

		ZimbraAccount.AccountC().soapSend(
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
		
		//Go to Mail tab to check that mails are in correct folder
		app.zPageMail.zNavigateTo();
		
		//Get the inbox folder
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		
		// Click on inbox folder
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);
		
		//Check that mail are filtered
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject1),"Mail is filtered out incorrectly and is not present in Inbox after filter run!");
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(subject2),"Mail is still present in inbox after filter run!");
		
		//Get the sent folder
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), folderName);
		
		// Click on trash folder
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, folder);

		//Check that mail are filtered
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(subject1),"Mail is filtered out incorrectly after filter run!");
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject2),"Mail is not present in "+folderName +" folder after filter run!");
		
	}
	
	@Test(
			description = "Create an Incoming Message Filter having criterion 'Subject - Contains - X - Move into Folder Y'",
			groups = { "functional" }
			)
	public void MoveIntoFolderFilters_02() throws HarnessException {

		String filterName = "SubjectContains "+ ConfigProperties.getUniqueString();
		
		String subject1 = "subject1Subject "+ ConfigProperties.getUniqueString();
    	String subject2 = "subject2Subject "+ ConfigProperties.getUniqueString();
		String bodyText = "text" + ConfigProperties.getUniqueString();
		
		String conditionValue[] = subject1.split(" ");
		
		//Create a folder
		String folderName = "SubjectContains"; 	
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailFilters);
		
		// Click "Add New"
		DialogEditFilter dialog = (DialogEditFilter)app.zPagePreferences.zToolbarPressButton(Button.B_NEW_IN_FILTER);		
		
		// Give a name
		dialog.zSetFilterName(filterName);
		
		// Give a criteria
		dialog.zAddFilterCriteria(ConditionType.Subject, ConditionConstraint.Contains, conditionValue[0]);	
		
		// Give an action (if necessary)
		dialog.zAddFilterAction(FilterAction.MoveIntoFolder,folderName);

		// Save
		dialog.zClickButton(Button.B_OK);		
		
		// Verify the filter is created through SOAP
		app.zGetActiveAccount().soapSend(
						"<GetFilterRulesRequest xmlns='urn:zimbraMail'/>");
		
		Element[] rules = app.zGetActiveAccount().soapSelectNodes("//mail:GetFilterRulesResponse//mail:filterRule[@name='" + filterName +"']");
		ZAssert.assertEquals(rules.length, 1, "Verify the Incoming filter rule exists in the server");
		
		//Verify that filter is created through UI
		
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(PagePreferences.Locators.zFilterRowCss +":contains("+ filterName +")"), "Incoming filter is not created successfully!");
		
		//Verify that mails are going in the correct folder
		
		// Send two messages from two different account
		ZimbraAccount.Account4().soapSend(
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

		ZimbraAccount.Account5().soapSend(
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
		
		//Go to Mail tab to check that mails are in correct folder
		app.zPageMail.zNavigateTo();
		
		//Get the inbox folder
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		
		// Click on inbox folder
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);
		
		//Check that mail are filtered
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(subject1),"Mail is still present in Inbox after filter run!");
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject2),"Mail is filtered out incorrectly after filter run!");
		
		//Get the sent folder
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), folderName);
		
		// Click on trash folder
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, folder);

		//Check that mail are filtered
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject1),"Mail is not present in "+folderName+" folder after filter run!");
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(subject2),"Mail is filtered out incorrectly after filter run!");
		
	}
	
	@Test(
			description = "Create an Incoming Message Filter having criterion 'Body - Contains - X - Move into Folder Y'",
			groups = { "functional" }
			)
	public void MoveIntoFolderFilters_03() throws HarnessException {

		String filterName = "BodyContains "+ ConfigProperties.getUniqueString();
		String conditionValue = "Body Condition";
		
		String subject1 = "subject1Body "+ ConfigProperties.getUniqueString();
    	String subject2 = "subject2Body "+ ConfigProperties.getUniqueString();
		String bodyText1 = "text Body Condition" + ConfigProperties.getUniqueString();
		String bodyText2 = "Body text" + ConfigProperties.getUniqueString();
		
		//Create a folder
		String folderName = "BodyContains";  //		
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailFilters);
		
		// Click "Add New"
		DialogEditFilter dialog = (DialogEditFilter)app.zPagePreferences.zToolbarPressButton(Button.B_NEW_IN_FILTER);		
		
		// Give a name
		dialog.zSetFilterName(filterName);
		
		// Give a criteria
		dialog.zAddFilterCriteria(ConditionType.Body, ConditionConstraint.Contains, conditionValue);	
		
		// Give an action (if necessary)
		dialog.zAddFilterAction(FilterAction.MoveIntoFolder,folderName);

		// Save
		dialog.zClickButton(Button.B_OK);		
		
		// Verify the filter is created through SOAP
		app.zGetActiveAccount().soapSend(
						"<GetFilterRulesRequest xmlns='urn:zimbraMail'/>");
		
		Element[] rules = app.zGetActiveAccount().soapSelectNodes("//mail:GetFilterRulesResponse//mail:filterRule[@name='" + filterName +"']");
		ZAssert.assertEquals(rules.length, 1, "Verify the Incoming filter rule exists in the server");
		
		//Verify that filter is created through UI
		
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(PagePreferences.Locators.zFilterRowCss +":contains("+ filterName +")"), "Incoming filter is not created successfully!");
		
		//Verify that mails are going in the correct folder
		
		// Send two messages from two different account
		ZimbraAccount.Account9().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject1 +"</su>" +
						"<mp ct='multipart/alternative'>" +
						"<mp ct='text/plain'>" +
						"<content>"+ bodyText1 +"</content>" +
						"</mp>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		ZimbraAccount.Account10().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject2 +"</su>" +
						"<mp ct='multipart/alternative'>" +
						"<mp ct='text/plain'>" +
						"<content>"+ bodyText2 +"</content>" +
						"</mp>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");
		
		//Go to Mail tab to check that mails are in correct folder
		app.zPageMail.zNavigateTo();
		
		//Get the inbox folder
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		
		// Click on inbox folder
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);
		
		//Check that mail are filtered
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(subject1),"Mail is sill present in Inbox after filter run!");
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject2),"Mail is not present in Inbox and is filtered out incorrectly after filter run!");
		
		//Get the sent folder
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), folderName);
		
		// Click on trash folder
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, folder);

		//Check that mail are filtered
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject1),"Mail is not present in "+folderName+"folder after filter run!");
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(subject2),"Mail is filtered out incorrectly after filter run!");		
	}
	
	@Test(
			description = "Create an outgoing Message Filter having criterion 'To - Contains - X - Move into Folder Y'",
			groups = { "functional" }
			)
	public void MoveIntoFolderFilters_04() throws HarnessException {

		String filterName = "ToContains "+ ConfigProperties.getUniqueString();
		String conditionValue[] = ZimbraAccount.AccountB().EmailAddress.split("@");
		
		String subject1 = "subject1To "+ ConfigProperties.getUniqueString();
    	String subject2 = "subject2To "+ ConfigProperties.getUniqueString();
		String bodyText = "text" + ConfigProperties.getUniqueString();
		
		//Create a folder
		String folderName = "ToContains";  //		
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailFilters);
		
		//Go to outgoing filter tab 
		app.zPagePreferences.sClick(PagePreferences.Locators.zOutGoingFilterTab);
		
		// Click "Add New"
		DialogEditFilter dialog = (DialogEditFilter)app.zPagePreferences.zToolbarPressButton(Button.B_NEW_OUT_FILTER);		
		
		// Give a name
		dialog.zSetFilterName(filterName);
		
		// Give a criteria
		dialog.zAddFilterCriteria(ConditionType.To, ConditionConstraint.Contains, conditionValue[0]);	
		
		// Give an action (if necessary)
		dialog.zAddFilterAction(FilterAction.MoveIntoFolder,folderName);

		// Save
		dialog.zClickButton(Button.B_OK);		
		
		// Verify the filter is created through SOAP
		app.zGetActiveAccount().soapSend(
				"<GetOutgoingFilterRulesRequest xmlns='urn:zimbraMail'/>");

		Element[] rules = app.zGetActiveAccount().soapSelectNodes("//mail:GetOutgoingFilterRulesResponse//mail:filterRule[@name='" + filterName +"']");
		ZAssert.assertEquals(rules.length, 1, "Verify the Outgoing filter rule exists in the server");
		
		//Verify that filter is created through UI
		
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(PagePreferences.Locators.zFilterRowCss +":contains("+ filterName +")"), "Incoming filter is not created successfully!");
		
		//Verify that mails are going in the correct folder
		
		// Send two messages from two different account
		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ ZimbraAccount.Account2().EmailAddress +"'/>" +
						"<su>"+ subject1 +"</su>" +
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
						"<e t='t' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
						"<su>"+ subject2 +"</su>" +
						"<mp ct='multipart/alternative'>" +
						"<mp ct='text/plain'>" +
						"<content>"+ bodyText +"</content>" +
						"</mp>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");
		
		//Go to Mail tab to check that mails are in correct folder
		app.zPageMail.zNavigateTo();
		
		//Get the inbox folder
		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Sent);
		
		// Click on inbox folder
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
		
		//Check that mail are filtered
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject1),"Mail is not present in Inbox and is filtered out incorrectly after filter run!");
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(subject2),"Mail is not filtered out after filter run and is still present in inbox!");
		
		//Get the sent folder
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), folderName);
		
		// Click on trash folder
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, folder);

		//Check that mail are filtered
		ZAssert.assertFalse(app.zPageMail.zVerifyMailExists(subject1),"Mail is filtered out incorrectly after filter run!");
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject2),"Mail is not present in "+folderName+" folder after filter run!");		
	} 	
}

