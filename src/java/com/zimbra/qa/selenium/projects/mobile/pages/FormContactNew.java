/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.mobile.pages;

import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsForm;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;



public class FormContactNew extends AbsForm {

	public static class Locators {
		
		public static final String lSubmit = "//input[@name='actionSave']";
		
		public static final String lLastName		= "css=input#lastName";
		public static final String lFirstName		= "css=input#firstName";
		public static final String lJobTitle		= "css=input#jobTitle";
		public static final String lCompany			= "css=input#company";
		public static final String lEmail			= "css=input#email";

	}
	
	public FormContactNew(AbsApplication application) {
		super(application);
		
		logger.info("new " + FormContactNew.class.getCanonicalName());

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zSubmit() throws HarnessException {
		if ( !(this.sIsElementPresent(Locators.lSubmit)) ) {
			throw new HarnessException("Submit button is not present "+ Locators.lSubmit);
		}
		this.sClick(Locators.lSubmit);
		SleepUtil.sleepMedium();
	}

	@Override
	public void zFill(IItem item) throws HarnessException {
		logger.debug(myPageName() + " fill()");
		logger.info(item.prettyPrint());

		if ( !(item instanceof ContactItem) ) {
			throw new HarnessException("Invalid item type - must be ContactItem");
		}
		
		ContactItem contact = (ContactItem)item;
		
		if ( contact.firstName != null ) {
			if ( !this.sIsElementPresent(Locators.lFirstName) )
				throw new HarnessException("Unable to find locator "+ Locators.lFirstName);
			this.sType(Locators.lFirstName, contact.firstName);
		}
		
		if ( contact.lastName != null ) {
			if ( !this.sIsElementPresent(Locators.lLastName) )
				throw new HarnessException("Unable to find locator "+ Locators.lLastName);
			this.sType(Locators.lLastName, contact.lastName);
		}

		if ( contact.email != null ) {
			if ( !this.sIsElementPresent(Locators.lEmail) )
				throw new HarnessException("Unable to find locator "+ Locators.lEmail);
			this.sType(Locators.lEmail, contact.email);
		}

	}

	@Override
	public boolean zIsActive() throws HarnessException {
		throw new HarnessException("implement me!");
	}

}
