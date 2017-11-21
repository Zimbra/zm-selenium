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
/**
 * 
 */
package com.zimbra.qa.selenium.projects.mobile.pages;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;


/**
 * @author Matt Rhoades
 *
 */
public class MobilePages extends AbsApplication {
	
	public PageLogin					zPageLogin = null;
	public PageMain						zPageMain = null;
	public PageMail						zPageMail = null;
	public PageContacts					zPageContacts = null;
	
	
	public MobilePages() {
		super();
		
		logger.info("new " + MobilePages.class.getCanonicalName());
		
		// Login page
		
		zPageLogin = new PageLogin(this);
		pages.put(zPageLogin.myPageName(), zPageLogin);
		
		// Main page
		zPageMain = new PageMain(this);
		pages.put(zPageMain.myPageName(), zPageMain);
		
		zPageMail = new PageMail(this);
		pages.put(zPageMail.myPageName(), zPageMail);
		
		zPageContacts = new PageContacts(this);
		pages.put(zPageContacts.myPageName(), zPageContacts);
		
		// Configure the localization strings
		getL10N().zAddBundlename(I18N.Catalog.I18nMsg);
		getL10N().zAddBundlename(I18N.Catalog.AjxMsg);
		getL10N().zAddBundlename(I18N.Catalog.ZMsg);
		getL10N().zAddBundlename(I18N.Catalog.ZsMsg);
		getL10N().zAddBundlename(I18N.Catalog.ZmMsg);

	}
	
	/* (non-Javadoc)
	 * @see projects.admin.pages.AbsApplication#isLoaded()
	 */
	@Override
	public boolean zIsLoaded() throws HarnessException {
		// TODO: Need to define this method
		return (true);
	}

	/* (non-Javadoc)
	 * @see projects.admin.pages.AbsApplication#myApplicationName()
	 */
	@Override
	public String myApplicationName() {
		return ("Mobile Client");
	}

	protected ZimbraAccount zSetActiveAccount(ZimbraAccount account) throws HarnessException {
		return (super.zSetActiveAccount(account));
	}

}
