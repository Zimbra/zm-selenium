package com.zimbra.qa.selenium.projects.ajax.tests.addressbook.bugs;


import java.util.*;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;

import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.addressbook.*;

public class Bug44132_ManipulateContactGroupFromContactContextMenu extends AjaxCommonTest  {

	public Bug44132_ManipulateContactGroupFromContactContextMenu() {
		logger.info("New "+ Bug44132_ManipulateContactGroupFromContactContextMenu.class.getCanonicalName());
		
		// All tests start at the Address page
		super.startingPage = app.zPageAddressbook;

		// Make sure we are using an account with conversation view
		super.startingAccountPreferences = null;		
		
	}
	
	private void Verification(ContactGroupItem group) throws HarnessException {
		//verify group name is displayed on the list		        
		List<ContactItem> contacts = app.zPageAddressbook.zListGetContacts();
		boolean isFileAsEqual=false;
		for (ContactItem ci : contacts) {
			if (ci.fileAs.equals(group.fileAs)) {
	            isFileAsEqual = true;	
				break;
			}
		}
	
		ZAssert.assertTrue(isFileAsEqual, "Verify contact fileAs (" + group.fileAs + ") existed ");

	    //verify location is System folder "Contacts"
		ZAssert.assertEquals(app.zPageAddressbook.sGetText("css=td.companyFolder"), SystemFolder.Contacts.getName(), "Verify location (folder) is " + SystemFolder.Contacts.getName());
		
	    // Select the contact group
		DisplayContactGroup groupView = (DisplayContactGroup) app.zPageAddressbook.zListItem(Action.A_LEFTCLICK, group.fileAs);
	
		//verify group name displayed
	    ZAssert.assertStringContains(groupView.zGetContactProperty(DisplayContactGroup.Field.Company), group.fileAs  , "Verify contact group email (" + group.fileAs + ") displayed");	
		
	    //verify members
		for (int i=0; i<group.dlist.size(); i++) {
	       ZAssert.assertStringContains(groupView.zGetContactProperty(DisplayContactGroup.Field.Email), group.dlist.get(i), "Verify contact group email (" + group.dlist.get(i) + ") displayed");	
		}            

	}
	
	private void CreateGroupVerification(SimpleFormContactGroupNew simpleFormGroup, ContactGroupItem group) throws HarnessException {
	
		//fill in group name 
		simpleFormGroup.zFill(group);
	   
		//click Save
		simpleFormGroup.zSubmit(); 
	
		//verify toasted message 'group created'  
        String expectedMsg ="Group Created";
        ZAssert.assertStringContains(app.zPageMain.zGetToaster().zGetToastMessage(),
        		        expectedMsg , "Verify toast message '" + expectedMsg + "'");
    
	    Verification(group);
           
	}
	
		
	@Test(	description = "D1 Enhancement : Create a contact group with only one contact",
			groups = { "smoke" })
	public void CreateContactGroupWith1Contact() throws HarnessException {			
		
		 // Create a contact via Soap then select
		ContactItem contactItem = app.zPageAddressbook.createUsingSOAPSelectContact(app, Action.A_LEFTCLICK);
		  			
		//open contact group form
		SimpleFormContactGroupNew simpleFormGroup = (SimpleFormContactGroupNew) app.zPageAddressbook.zListItem(Action.A_RIGHTCLICK, Button.B_CONTACTGROUP, Button.O_NEW_CONTACTGROUP , contactItem.fileAs);     
		
		  //Create contact group 
		ContactGroupItem group = new ContactGroupItem("group_" + ZimbraSeleniumProperties.getUniqueString().substring(8));
		group.addDListMember(contactItem.email);
	
		//verification
		CreateGroupVerification(simpleFormGroup, group);
	}

	@Test(	description = "D1 Enhancement : Add a contact to an existing group",
			groups = { "smoke" })
	public void Add1ContactToGroup() throws HarnessException {			
		// Create a contact group via Soap
		ContactGroupItem group = ContactGroupItem.createUsingSOAP(app);			             
					
		//refresh the browser
		app.zPageAddressbook.zRefresh();
				
		
		 // Create a contact via Soap then select
		ContactItem contactItem = app.zPageAddressbook.createUsingSOAPSelectContact(app, Action.A_LEFTCLICK);
	
		//select the contact 
		app.zPageAddressbook.zListItem(Action.A_RIGHTCLICK, Button.B_CONTACTGROUP, group, contactItem.fileAs);     
		
	
		//Add contact to existing group 
		group.addDListMember(contactItem.email);
	
		//verify toasted message 'group saved'  
        String expectedMsg ="Group Saved";
        ZAssert.assertStringContains(app.zPageMain.zGetToaster().zGetToastMessage(),
        		        expectedMsg , "Verify toast message '" + expectedMsg + "'");
    
	    Verification(group);
	}

	@Test(	description = "D1 Enhancement : Add 3 contacts to an existing group",
			groups = { "functional" })
	public void Add3ContactsToGroup() throws HarnessException {			
		// Create a contact group via Soap
		ContactGroupItem group = ContactGroupItem.createUsingSOAP(app);			             
		
		//refresh the browser
		app.zPageAddressbook.zRefresh();

		// Create a contact via Soap
		ContactItem contactItem1 = ContactItem.createUsingSOAP(app);			             
		  		  
		// Create a contact via Soap
		ContactItem contactItem2 = ContactItem.createUsingSOAP(app);			             
		
		// Create a contact via Soap
		ContactItem contactItem3 = ContactItem.createUsingSOAP(app);			             
		
		 // Refresh the view, to pick up the new contact
	    FolderItem contactFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);	  
	    app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, contactFolder);
	    	  
	
	    // Select the item
	    app.zPageAddressbook.zListItem(Action.A_CHECKBOX, contactItem1.fileAs);
	    app.zPageAddressbook.zListItem(Action.A_CHECKBOX, contactItem2.fileAs);
	    app.zPageAddressbook.zListItem(Action.A_CHECKBOX, contactItem3.fileAs);
	   				  							
		
		//select the contact group 
		app.zPageAddressbook.zListItem(Action.A_RIGHTCLICK, Button.B_CONTACTGROUP, group, contactItem1.fileAs);     


		//verify toasted message 'group saved'  
        String expectedMsg ="Group Saved";
        ZAssert.assertStringContains(app.zPageMain.zGetToaster().zGetToastMessage(),
        		        expectedMsg , "Verify toast message '" + expectedMsg + "'");
    
    	group.addDListMember(contactItem1.email);
		group.addDListMember(contactItem2.email);
		group.addDListMember(contactItem3.email);

	    Verification(group);
	}
	
	@Test(	description = "D1 Enhancement : Create a contact group with 3 contacts",
			groups = { "functional" })
	public void CreateContactGroupWith3Contacts() throws HarnessException {			
		  // Create a contact via Soap
		ContactItem contactItem1 = ContactItem.createUsingSOAP(app);			             
		  		  
		// Create a contact via Soap
		ContactItem contactItem2 = ContactItem.createUsingSOAP(app);			             
		
		// Create a contact via Soap
		ContactItem contactItem3 = ContactItem.createUsingSOAP(app);			             
		
		  // Refresh the view, to pick up the new contact
	    FolderItem contactFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);	  
	    app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, contactFolder);
	    
	    // Select the item
	    app.zPageAddressbook.zListItem(Action.A_CHECKBOX, contactItem1.fileAs);
	    app.zPageAddressbook.zListItem(Action.A_CHECKBOX, contactItem2.fileAs);
	    app.zPageAddressbook.zListItem(Action.A_CHECKBOX, contactItem3.fileAs);
	   				  			
		//open contact group form
		SimpleFormContactGroupNew simpleFormGroup = (SimpleFormContactGroupNew) app.zPageAddressbook.zListItem(Action.A_RIGHTCLICK, Button.B_CONTACTGROUP, Button.O_NEW_CONTACTGROUP , contactItem2.fileAs);     
		
		  //Create contact group 
		ContactGroupItem group = new ContactGroupItem("group_" + ZimbraSeleniumProperties.getUniqueString().substring(8));
		group.addDListMember(contactItem1.email);
		group.addDListMember(contactItem2.email);
		group.addDListMember(contactItem3.email);
	
		//verification
		CreateGroupVerification(simpleFormGroup, group);
	}

	@Test(	description = "D1 Enhancement : Create a contact group with one contact + one group",
			groups = { "functionaly" })
	public void CreateContactGroupWith1ContactAnd1Group() throws HarnessException {			
		  // Create a contact via Soap
		ContactItem contactItem = ContactItem.createUsingSOAP(app);			             
		  			
		// Create a contact group via Soap
		ContactGroupItem group = ContactGroupItem.createUsingSOAP(app);
			             		

		  // Refresh the view, to pick up the new contact + group
	    FolderItem contactFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);	  
	    app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, contactFolder);
	    
	    // Select the item
	    app.zPageAddressbook.zListItem(Action.A_CHECKBOX, contactItem.fileAs);
	    app.zPageAddressbook.zListItem(Action.A_CHECKBOX, group.fileAs);
	    
	   				  			
		//open contact group form
		SimpleFormContactGroupNew simpleFormGroup = (SimpleFormContactGroupNew) app.zPageAddressbook.zListItem(Action.A_RIGHTCLICK, Button.B_CONTACTGROUP, Button.O_NEW_CONTACTGROUP , contactItem.fileAs);     
		
		  //Create contact group 
		ContactGroupItem newGroup = new ContactGroupItem("group_" + ZimbraSeleniumProperties.getUniqueString().substring(8));
		newGroup.addDListMember(contactItem.email);
		for (int i=0; i<group.dlist.size(); i++) {
			  newGroup.addDListMember(group.dlist.get(i));
		}
	
		//verification
		CreateGroupVerification(simpleFormGroup, newGroup);

	}		
	

	@Test(	description = "D1 Enhancement : Add 1 contact + 1 group to an existing group",
			groups = { "functionaly" })
	public void Add1ContactAnd1GroupToExistingGroup() throws HarnessException {			
		// Create a contact group via Soap
		ContactGroupItem group = ContactGroupItem.createUsingSOAP(app);			             
	
		// Create a contact group via Soap
		ContactGroupItem group1 = ContactGroupItem.createUsingSOAP(app);			             		
	
		//refresh the browser
		app.zPageAddressbook.zRefresh();

		// Create a contact via Soap
		ContactItem contactItem1 = ContactItem.createUsingSOAP(app);			             
		  		  
		 
		// Refresh the view, to pick up the new contact + group
	    FolderItem contactFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);	  
	    app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, contactFolder);
	    	  
	
	    // Select the items
	    app.zPageAddressbook.zListItem(Action.A_CHECKBOX, contactItem1.fileAs);
	    app.zPageAddressbook.zListItem(Action.A_CHECKBOX, group1.fileAs);
	   				  							
	   
		//select the contact group 
		app.zPageAddressbook.zListItem(Action.A_RIGHTCLICK, Button.B_CONTACTGROUP, group, group1.fileAs);     
		

		app.zPageAddressbook.zListItem(Action.A_LEFTCLICK, group1.fileAs);
	   	
		//verify toasted message 'group saved'  
        String expectedMsg ="Group Saved";
        ZAssert.assertStringContains(app.zPageMain.zGetToaster().zGetToastMessage(),
        		        expectedMsg , "Verify toast message '" + expectedMsg + "'");
    
    	group.addDListMember(contactItem1.email);
    	
    	for (int i=0; i<group1.dlist.size(); i++) {
			  group.addDListMember(group1.dlist.get(i));
		}
	
	    Verification(group);
	}
}
