/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.touch.pages;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.touch.pages.search.PageSearch;
import com.zimbra.qa.selenium.projects.touch.core.TouchCore;
import com.zimbra.qa.selenium.projects.touch.pages.contacts.PageAddressbook;
import com.zimbra.qa.selenium.projects.touch.pages.contacts.TreeContacts;
import com.zimbra.qa.selenium.projects.touch.pages.calendar.PageCalendar;
import com.zimbra.qa.selenium.projects.touch.pages.calendar.TreeCalendar;
import com.zimbra.qa.selenium.projects.touch.pages.mail.*;

/**
 * The <code>AppTouchClient</code> class defines the Zimbra Ajax client.
 * <p>
 * The <code>AppTouchClient</code> contains all pages, folder trees,
 * dialog boxes, forms, menus for the Ajax client.
 * <p>
 * In {@link TouchCore}, there is one
 * AppTouchClient object created per test case class (ensuring
 * class-level concurrency).  The test case methods can access
 * different application pages and trees, using the object
 * properties.
 * <p>
 * <pre>
 * {@code
 *
 * // Navigate to the addresbook
 * app.zPageAddressbook.navigateTo();
 *
 * // Click "New" button to create a new contact
 * app.zPageAddressbook.zToolbarPressButton(Button.B_NEW);
 *
 * }
 * </pre>
 * <p>
 *
 * @author Matt Rhoades
 *
 */
public class TouchPages extends AbsApplication {

	// Pages
	public PageLogin					zPageLogin = null;
	public PageMain						zPageMain = null;
	public PageMail						zPageMail = null;
	public PageAddressbook              zPageAddressbook = null;
	public PageCalendar					zPageCalendar = null;
	public PageSearch					zPageSearch = null;
	public FormMailNew					zFormMailNew=null;


	// Trees
	public TreeMail						zTreeMail = null;
	public TreeContacts					zTreeContacts = null;
	public TreeCalendar					zTreeCalendar = null;

	private static TouchPages TouchPages;
	public static TouchPages getInstance() {
		if (TouchPages == null) {
			TouchPages = new TouchPages();
		}
		return TouchPages;
	}
	public TouchPages() {
		super();

		logger.info("new " + TouchPages.class.getCanonicalName());


		// Login page
		zPageLogin = new PageLogin(this);
		pages.put(zPageLogin.myPageName(), zPageLogin);

		// Main page
		zPageMain = new PageMain(this);
		pages.put(zPageMain.myPageName(), zPageMain);

		// Mail page
		zPageMail = new PageMail(this);
		pages.put(zPageMail.myPageName(), zPageMail);

		// Mail form
		zFormMailNew = new FormMailNew(this);
		forms.put(zFormMailNew.myPageName(), zFormMailNew);

		zTreeMail = new TreeMail(this);
		trees.put(zTreeMail.myPageName(), zTreeMail);

		// Addressbook page
		zPageAddressbook = new PageAddressbook(this);
		pages.put(zPageAddressbook.myPageName(), zPageAddressbook);

		zTreeContacts = new TreeContacts(this);
		trees.put(zTreeContacts.myPageName(), zTreeContacts);

		// Calendar page
		zPageCalendar = new PageCalendar(this);
		pages.put(zPageCalendar.myPageName(), zPageCalendar);

		zTreeCalendar = new TreeCalendar(this);
		trees.put(zTreeCalendar.myPageName(), zTreeCalendar);

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
	   if (this.zPageMain.zIsActive() ||
            this.zPageLogin.zIsActive()) {
         return true;
      } else {
         return false;
      }
	}

	/* (non-Javadoc)
	 * @see projects.admin.pages.AbsApplication#myApplicationName()
	 */
	@Override
	public String myApplicationName() {
		return ("Ajax Client");
	}

	/* (non-Javadoc)
	 * @see projects.admin.pages.AbsApplication#myApplicationName()
	 */
	@Override
	public ZimbraAccount zSetActiveAccount(ZimbraAccount account) throws HarnessException {
		return (super.zSetActiveAccount(account));
	}

}
