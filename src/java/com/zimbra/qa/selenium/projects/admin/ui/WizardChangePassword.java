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
/**
 *
 */
package com.zimbra.qa.selenium.projects.admin.ui;

import java.awt.event.KeyEvent;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.items.AliasItem;


/**
 * @author Matt Rhoades
 *
 */
public class WizardChangePassword extends AbsWizard {
	public static class Locators {
		public static final String zNewPassword = "css=input[id='zdlgv__UNDEFINE1_password']";
		public static final String zConfirmPassword = "css=input[id='zdlgv__UNDEFINE1_confirmPassword']";
		public static final String zdlg_OK="css=td[id='zdlg__UNDEFINE1_button2_title']:contains('OK')";
		public static final String PASSWORD = "css=input[id='ztabv__ACCT_EDIT_password]";
		public static final String CONFIRM_PASSWORD= "css=input[id='ztabv__ACCT_EDIT_confirmPassword']";
	}

	public WizardChangePassword(AbsTab myApplication) {
		super(myApplication);
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {

		if ( !(item instanceof AccountItem) )
			throw new HarnessException("item must be an AccountItem, was "+ item.getClass().getCanonicalName());

		sType(Locators.zNewPassword, "test1234");

		/**
		 * If you use normal type method domain is taken as default domain name.
		 * Below line of code is not grid friendly but this is only solution working currently.
		 */
		if(ZimbraSeleniumProperties.isWebDriver()) {
			SleepUtil.sleepSmall();
			this.clearField(Locators.zConfirmPassword);

		}
		sType(Locators.zConfirmPassword,"");
		sType(Locators.zConfirmPassword,"test1234");

		//System.out.println(domain);
		//this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);

		zClickAt(Locators.zdlg_OK,"");
		return item;

	}
	
	

	@Override
	public String myPageName() {
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		boolean present = sIsElementPresent(Locators.zNewPassword);
		if ( !present ) {
			return (false);
		}

		boolean visible = this.zIsVisiblePerPosition(Locators.zConfirmPassword, 0, 0);
		if ( !visible ) {
			return (false);
		}

		return (true);
	}

}
