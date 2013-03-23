/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011 VMware, Inc.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.zcs.tests.addressbook.folders;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;



public class AddressbookContextMenu extends CommonTest {

	public AddressbookContextMenu() {
	}
	
	/**
	 * verify context menu items match expected values from right-click on system addressbook
	 * 
	 * Steps:
	 * 1. Right click on "Contacts" addressbook folder
	 * 2. Verify context menu contains:
	 *  "New Address Book"
	 *  "Share Address Book"
	 *  "Delete" (disabled)
	 *  "Rename Folder" (disabled)
	 *  "Edit Properties"
	 *  "Expand All" (disabled)
	 * @throws HarnessException 
	 * 
	 */
	@Test(
			description="verify context menu items match expected values from right-click on system Contacts addressbook",
			groups = { "smoke", "full" }
				)
	public void addressbookContextMenu01() throws HarnessException {
		throw new HarnessException("implement me!");
	}
	
	/**
	 * verify context menu items match expected values from right-click on user-defined addressbook
	 * 
	 * Steps:
	 * 1. Create new addressbook folder
	 * 2. Right click on addressbook folder
	 * 3. Verify context menu contains:
	 *  "New Address Book"
	 *  "Share Address Book"
	 *  "Delete"
	 *  "Rename Folder"
	 *  "Edit Properties"
	 *  "Expand All" (disabled)
	 * @throws HarnessException 
	 * 
	 */
	@Test(
			description="verify context menu items match expected values from right-click on user-defined addressbook",
			groups = { "smoke", "full" }
				)
	public void addressbookContextMenu02() throws HarnessException {
		throw new HarnessException("implement me!");
	}
	
	
}
