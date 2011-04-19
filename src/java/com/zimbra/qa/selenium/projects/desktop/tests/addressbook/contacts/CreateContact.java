package com.zimbra.qa.selenium.projects.desktop.tests.addressbook.contacts;

import java.util.List;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.ContactItem.GenerateItemType;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.desktop.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.desktop.ui.*;
import com.zimbra.qa.selenium.projects.desktop.ui.addressbook.*;


//TODO: add more in ContactItem.java

public class CreateContact extends AjaxCommonTest  {

	public CreateContact() {
		logger.info("New "+ CreateContact.class.getCanonicalName());
		
		// All tests start at the Address page
		super.startingPage = app.zPageAddressbook;

		// Make sure we are using an account with conversation view
		super.startingAccountPreferences = null;		
		
	}
	
	


	//can be used for other classes such as DeleteContact, MoveContact
	public static ContactItem createBasicContact(AppAjaxClient app, FormContactNew formContactNew)throws HarnessException {
							
		// Create a contact Item
		ContactItem contactItem = ContactItem.generateContactItem(GenerateItemType.Basic);
	
		//verify form contact new page is displayed
		ZAssert.assertTrue(formContactNew.zIsActive(),"Verify new contact form is displayed");
		
        // Fill in the form
	    formContactNew.zFill(contactItem);
	    
		// Save the contact
        formContactNew.zSubmit();
		
        //verify toasted message 'contact created'  
        Toaster toast = app.zPageMain.zGetToaster();
        String toastMsg = toast.zGetToastMessage();
        ZAssert.assertStringContains(toastMsg, "Contact Created", "Verify toast message 'Contact Created'");

        //verify contact "file as" is displayed
		List<ContactItem> contacts = app.zPageAddressbook.zListGetContacts();
		boolean isFileAsEqual=false;
		for (ContactItem ci : contacts) {
			if (ci.fileAs.equals(contactItem.fileAs)) {
	            isFileAsEqual = true;	
				break;
			}
		}
		
        ZAssert.assertTrue(isFileAsEqual, "Verify contact fileAs (" + contactItem.fileAs + ") existed ");

		return contactItem;
	}
	
	@Test(	description = "Create a basic contact item by click New in page Addressbook ",
			groups = { "sanity" })
	public void CreateContact_01() throws HarnessException {				
		FormContactNew formContactNew = (FormContactNew)app.zPageAddressbook.zToolbarPressButton(Button.B_NEW);

		createBasicContact(app, formContactNew);		
	}
	
	@Test(	description = "Create a basic contact item by use PullDown Menu->Contacts",
			groups = { "functional" })
	public void CreateContactFromPulldownMenu() throws HarnessException {				
		FormContactNew formContactNew = (FormContactNew)app.zPageAddressbook.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_CONTACT);
		
		createBasicContact(app, formContactNew);		
	}
}
