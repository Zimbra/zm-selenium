package com.zimbra.qa.selenium.projects.zcs.tests.addressbook.contacts;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.ContactItem.GenerateItemType;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.projects.zcs.Locators;
import com.zimbra.qa.selenium.projects.zcs.PageObjects;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.ActionMethod;
import com.zimbra.qa.selenium.projects.zcs.ui.ABCompose.ABComposeActionMethod;


public class EditContact extends CommonTest {
	

	private FolderItem EmailedContacts;
	
	public EditContact() {
		
		EmailedContacts = new FolderItem();
		EmailedContacts.setName(localize(Locators.emailedContacts));
		
	}
	
	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------



	// --------------
	// section 2 BeforeClass
	// --------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.NAVIGATION_TAB="address book";
		super.zLogin();
	}
	
	/**
	 * Creates a contact with basic fields,edits the contacts using the ToolBar
	 * delete last name and verifies the change
	 */
	@Test(
			description = "Creates a contact with basic fields,edits the contacts using the ToolBar delete last name and verifies the change",
			groups = { "smoke", "full" }, 
			retryAnalyzer = RetryFailedTests.class)
	public void editNameAndVerify() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("na", "IE", "44288", "Java script entered data Or right click & copy paste data into edit fields are not recognized by new AB UI");
		
		ContactItem oldContact = ContactItem.generateContactItem(GenerateItemType.Basic);
		
		ContactItem newContact = ContactItem.generateContactItem(GenerateItemType.Basic);
		

		page.zABCompose.createItem(ActionMethod.DEFAULT, oldContact);
		page.zABCompose.modifyItem(ABComposeActionMethod.ToolbarEdit, oldContact, newContact);

		Assert.assertTrue(
				page.zABCompose.zVerifyEditContact(newContact),
				"Verify the contact fields match the correct values");
		
		SelNGBase.needReset.set(false);

	}

	/**
	 * Creates a contact with basic fields,edits the contacts using Right Click
	 * last name and verifies the change
	 */
	@Test(
			description = "Creates a contact with basic fields,edits the contacts using Right Click last name and verifies the change",
			groups = { "smoke", "full" }, 
			retryAnalyzer = RetryFailedTests.class)
	public void rghtClickEditNameAndVerify() throws Exception {
		
		checkForSkipException("na", "IE", "44288", "Java script entered data Or right click & copy paste data into edit fields are not recognized by new AB UI");

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();
		
		ContactItem contact = ContactItem.generateContactItem(GenerateItemType.Basic);

		ContactItem newContact = ContactItem.generateContactItem(GenerateItemType.Basic);
		
		page.zABCompose.createItem(ActionMethod.DEFAULT, contact);
		page.zABCompose.modifyItem(ABComposeActionMethod.RightClickEdit, contact, newContact);
		
		Assert.assertTrue(
				page.zABCompose.zVerifyEditContact(newContact),
				"Verify the contact fields match the correct values");

		SelNGBase.needReset.set(false);
	}
}