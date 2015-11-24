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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.contacts;


import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.items.ContextMenuItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.ContextMenu;
import com.zimbra.qa.selenium.projects.ajax.ui.PagePrint;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.PageContacts;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.search.PageAdvancedSearch;

public class ContactContextMenu extends AjaxCommonTest  {
	public ContactContextMenu() {
		logger.info("New "+ ContactContextMenu.class.getCanonicalName());
		
		// All tests start at the Address page
		super.startingPage = app.zPageContacts;

		super.startingAccountPreferences = null;		
		
	}
	
	private ContactItem createSelectAContactItem(String firstName, String lastName, String email, String ... tagIdArray ) throws HarnessException {
		String tagParam ="";
		//default value for file as is last, first
		String fileAs = lastName + ", " + firstName;
	
		if (tagIdArray.length == 1) {
			tagParam = " t='" + tagIdArray[0] + "'";
		}
        app.zGetActiveAccount().soapSend(
                "<CreateContactRequest xmlns='urn:zimbraMail'>" +
                "<cn " + tagParam + " fileAsStr='" + fileAs + "' >" +
                "<a n='firstName'>" + firstName +"</a>" +
                "<a n='lastName'>" + lastName +"</a>" +
                "<a n='email'>" + email + "</a>" +               
                "</cn>" +            
                "</CreateContactRequest>");

        ContactItem contactItem = ContactItem.importFromSOAP(app.zGetActiveAccount(), "FIELD[lastname]:" + lastName + "");
        
        // Refresh the view, to pick up the new contact
        FolderItem contactFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), "Contacts");
        app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, contactFolder);
        
        app.zPageContacts.zRefresh();
                 
        return contactItem;		
	
		
	}

	private ContactItem createSelectARandomContactItem(String ... tagIdArray) throws HarnessException {

		String firstName = "first" + ZimbraSeleniumProperties.getUniqueString();		
		String lastName = "last" + ZimbraSeleniumProperties.getUniqueString();
	    String email = "email" +  ZimbraSeleniumProperties.getUniqueString() + "@zimbra.com";
	
	    return createSelectAContactItem(firstName, lastName, email, tagIdArray );
	}
	
	
	@Test(	description = "Right click a contact to show a menu",
			groups = { "smoke" })
	public void ShowContextMenu() throws HarnessException {
		
		ContactItem contactItem = createSelectARandomContactItem();
		

		// Select the item
        // Right click to show the menu
        ContextMenu contextMenu= (ContextMenu) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, contactItem.fileAs); // contactItem.fileAs);
      
        
        ArrayList <ContextMenuItem> list = contextMenu.zListGetContextMenuItems(PageContacts.CONTEXT_MENU.class);
        
        //verify all items in the context menu list
        ZAssert.assertTrue(list.contains(PageContacts.CONTEXT_MENU.CONTACT_SEARCH),"Verify contact search in context menu");
         ZAssert.assertTrue(list.contains(PageContacts.CONTEXT_MENU.CONTACT_NEW_EMAIL),"Verify new email in context menu");
        ZAssert.assertTrue(list.contains(PageContacts.CONTEXT_MENU.CONTACT_EDIT),"Verify edit contact  in context menu");
        ZAssert.assertTrue(list.contains(PageContacts.CONTEXT_MENU.CONTACT_FORWARD),"Verify forward email in context menu");
        ZAssert.assertTrue(list.contains(PageContacts.CONTEXT_MENU.CONTACT_TAG),"Verify tag option in context menu");
        ZAssert.assertTrue(list.contains(PageContacts.CONTEXT_MENU.CONTACT_DELETE),"Verify delete option in context menu");
        ZAssert.assertTrue(list.contains(PageContacts.CONTEXT_MENU.CONTACT_MOVE),"Verify move option in context menu");
        ZAssert.assertTrue(list.contains(PageContacts.CONTEXT_MENU.CONTACT_PRINT),"Verify print option in context menu");

        //Verify all items enabled
        ZAssert.assertTrue(contextMenu.isEnable(PageContacts.CONTEXT_MENU.CONTACT_SEARCH),"Verify contact search is enabled");
        ZAssert.assertTrue(contextMenu.isEnable(PageContacts.CONTEXT_MENU.CONTACT_NEW_EMAIL),"Verify new email is enabled");
        ZAssert.assertTrue(contextMenu.isEnable(PageContacts.CONTEXT_MENU.CONTACT_EDIT),"Verify edit contact is enabled");

        
        ZAssert.assertTrue(contextMenu.isEnable(PageContacts.CONTEXT_MENU.CONTACT_FORWARD),"Verify forward email is disabled");
        
        ZAssert.assertTrue(contextMenu.isEnable(PageContacts.CONTEXT_MENU.CONTACT_TAG),"Verify tag option is enabled");
        ZAssert.assertTrue(contextMenu.isEnable(PageContacts.CONTEXT_MENU.CONTACT_DELETE),"Verify delete option is enabled");
        ZAssert.assertTrue(contextMenu.isEnable(PageContacts.CONTEXT_MENU.CONTACT_MOVE),"Verify move option is enabled");
        ZAssert.assertTrue(contextMenu.isEnable(PageContacts.CONTEXT_MENU.CONTACT_PRINT),"Verify print option is enabled");
   	}
	


	@Test(	description = "Right click then click New Email",
			groups = { "smoke" })
	public void NewEmail() throws HarnessException {
	
		ContactItem contactItem = createSelectARandomContactItem();
		

		//Click New Email
        FormMailNew formMailNew = (FormMailNew) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_NEW, contactItem.fileAs);        
        
        //Verify Form New mail is active
        ZAssert.assertTrue(formMailNew.zIsActive(),"Verify Form New Mail is active");
        
        //Verify contactItem.first contactItem.last displayed in the "To" field
        ZAssert.assertTrue(app.zPageContacts.sGetText(FormMailNew.Locators.zBubbleToField).contains(contactItem.firstName + " "  + contactItem.lastName),
        		     "Verify contact email displayed in field To - expected " + contactItem.firstName + " " +  contactItem.lastName +" - was " + app.zPageContacts.sGetText(FormMailNew.Locators.zBubbleToField));
        
        //TODO: Verify send email
	}
	

	@Test(	description = "Right click then click Advanced Search",
			groups = { "deprecated" })
	public void AdvancedSearch() throws HarnessException {
	
		ContactItem contactItem = createSelectARandomContactItem();
		

		//Click Advanced Search
        PageAdvancedSearch pageAdvancedSearch = (PageAdvancedSearch) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_SEARCHADVANCED, contactItem.fileAs);        
        
        //Verify Advanced Search page is active
        ZAssert.assertTrue(pageAdvancedSearch.zIsActive(),"Verify Advanced Search page is active");
                
        //close pageAdvancedSearch panel
        pageAdvancedSearch.zToolbarPressButton(Button.B_CLOSE);
	}

	@Test(	description = "Right click then click Print",
			groups = { "smoke-skip" })	
	public void Print() throws HarnessException {
		ContactItem contactItem = createSelectARandomContactItem();
		

        PagePrint pagePrint = (PagePrint) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_PRINT, contactItem.fileAs);        
                
        //close Print Dialog 
        pagePrint.cancelPrintDialog();
        
        //verify first,last,email displayed in Print View
	    Assert.assertTrue(pagePrint.isContained("css=td[class='contactHeader']", contactItem.lastName + ", " + contactItem.firstName )," expected: " + contactItem.lastName + "," + contactItem.firstName + " not displayed in Print Page" + " was:"  );

	    Assert.assertTrue(pagePrint.isContained("css=td[class='contactOutput']", contactItem.email ), contactItem.firstName + " not displayed in Print Page");
	    
	}

	@Test(	description = "Right click then  click Find Emails->Sent To contact",
			groups = { "smoke" })
	public void FindEmailsSentToContact() throws HarnessException {

			
	    //Create  email sent to this contacts	
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		String lastName = "lastname " + ZimbraSeleniumProperties.getUniqueString();
		
		// Send the message from AccountA to the ZWC user
		ZimbraAccount.AccountA().soapSend(
			"<SendMsgRequest xmlns='urn:zimbraMail'>" +
				"<m>" +
					"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
					"<su>"+ subject +"</su>" +
					"<mp ct='text/plain'>" +
						"<content>"+ "body" + ZimbraSeleniumProperties.getUniqueString() +"</content>" +
					"</mp>" +
				"</m>" +
			"</SendMsgRequest>");
		MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		ContactItem contactItem = createSelectAContactItem(app.zGetActiveAccount().DisplayName, lastName, app.zGetActiveAccount().EmailAddress);
		
		//Click Find Emails->Sent To Contact
        app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_SEARCH, Button.O_SEARCH_MAIL_SENT_TO_CONTACT , contactItem.fileAs);
		
		SleepUtil.sleepSmall();
        
        // Get the bubleText
		String bubleText = app.zPageSearch.sGetText("css=[class='addrBubble']");
		ZAssert.assertEquals(bubleText, "tocc:"+ app.zGetActiveAccount().EmailAddress ,"Verify the message list exists");
                
	}
	
	@Test(	description = "Right click then  click Find Emails->Received From contact",
				groups = { "smoke" })
	public void FindEmailsReceivedFromContact() throws HarnessException {
		
	    //Create  email sent to this contacts	
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		String lastName = "lastname " + ZimbraSeleniumProperties.getUniqueString();
		
		// Send the message from AccountB to the ZWC user
		ZimbraAccount.AccountB().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ "body" + ZimbraSeleniumProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ subject +")");

		ContactItem contactItem = createSelectAContactItem(app.zGetActiveAccount().DisplayName,lastName, ZimbraAccount.AccountB().EmailAddress);
		
		
		//Click Find Emails->Received From Contact
        app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_SEARCH, Button.O_SEARCH_MAIL_RECEIVED_FROM_CONTACT, contactItem.fileAs);

        
        // Get the bubleText
		String bubleText = app.zPageSearch.sGetText("css=[class='addrBubble']");
		ZAssert.assertEquals(bubleText, "from:"+ ZimbraAccount.AccountB().EmailAddress ,"Verify the message list exists");
		
                
	}
}

