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
package com.zimbra.qa.selenium.projects.zcs.tests.addressbook.contactgroups;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;


public class EditContactGroup extends CommonTest {
	
	public EditContactGroup() {
	}

	@Test(
			description = "Rename a contact group",
			groups = { "smoke", "full" }
		)
	public void editContactGroup01() throws HarnessException {
		throw new HarnessException("implement me!");
	}

	@Test(
			description = "Add another contact to an existing contact group",
			groups = { "smoke", "full" }
		)
	public void editContactGroup02() throws HarnessException {
		throw new HarnessException("implement me!");
	}

	@Test(
			description = "Remove a contact from an existing contact group",
			groups = { "smoke", "full" }
		)
	public void editContactGroup03() throws HarnessException {
		throw new HarnessException("implement me!");
	}


}
