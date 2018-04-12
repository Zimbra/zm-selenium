/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.pages;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.I18N;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.briefcase.PageBriefcase;
import com.zimbra.qa.selenium.projects.ajax.pages.briefcase.TreeBriefcase;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.PageCalendar;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.TreeCalendar;
import com.zimbra.qa.selenium.projects.ajax.pages.chat.PageChatPanel;
import com.zimbra.qa.selenium.projects.ajax.pages.contacts.PageContacts;
import com.zimbra.qa.selenium.projects.ajax.pages.contacts.TreeContacts;
import com.zimbra.qa.selenium.projects.ajax.pages.drive.PageDrive;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.PageMail;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.TreeMail;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.PagePreferences;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.signature.PageSignature;
import com.zimbra.qa.selenium.projects.ajax.pages.search.PageAdvancedSearch;
import com.zimbra.qa.selenium.projects.ajax.pages.search.PageSearch;
import com.zimbra.qa.selenium.projects.ajax.pages.social.PageSocial;
import com.zimbra.qa.selenium.projects.ajax.pages.tasks.PageTasks;
import com.zimbra.qa.selenium.projects.ajax.pages.tasks.TreeTasks;

/**
 * The <code>AppAjaxClient</code> class defines the Zimbra Ajax client.
 * <p>
 * The <code>AppAjaxClient</code> contains all pages, folder trees, dialog
 * boxes, forms, menus for the Ajax client.
 * <p>
 * In {@link AjaxCore}, there is one AppAjaxClient object created per test
 * case class (ensuring class-level concurrency). The test case methods can
 * access different application pages and trees, using the object properties.
 * <p>
 *
 * <pre>
 * {@code
 *
 * // Navigate to the contacts
 * app.zPageContacts.navigateTo();
 *
 * // Click "New" button to create a new contact
 * app.zPageContacts.zToolbarPressButton(Button.B_NEW);
 *
 * }
 * </pre>
 * <p>
 *
 * @author Matt Rhoades
 *
 */

public class AjaxPages extends AbsApplication {

	// Pages
	public PageLogin zPageLogin = null;
	public PageExternalRegistration zPageExternalRegistration = null;
	public PageMain zPageMain = null;
	public PageExternalMain zPageExternalMain = null;
	public PageSearch zPageSearch = null;
	public PageAdvancedSearch zPageAdvancedSearch = null;
	public PageMail zPageMail = null;
	public PageBriefcase zPageBriefcase = null;
	public PageContacts zPageContacts = null;
	public PageCalendar zPageCalendar = null;
	public PageTasks zPageTasks = null;
	public PagePreferences zPagePreferences = null;
	public PageSignature zPageSignature = null;
	public PageChatPanel zPageChatPanel = null;
	public PageDrive zPageDrive = null;

	// Trees
	public TreeMail zTreeMail = null;
	public TreeContacts zTreeContacts = null;
	public TreeCalendar zTreeCalendar = null;
	public TreeTasks zTreeTasks = null;
	public TreeBriefcase zTreeBriefcase = null;
	public TreePreferences zTreePreferences = null;

	// Zimlets
	public PageSocial zPageSocial = null;

	private static AjaxPages AjaxPages;
	public static AjaxPages getInstance() {
		if (AjaxPages == null) {
			AjaxPages = new AjaxPages();
		}
		return AjaxPages;
	}
	private AjaxPages() {
		super();

		logger.info("new " + AjaxPages.class.getCanonicalName());

		// Login page
		zPageLogin = new PageLogin(this);
		pages.put(zPageLogin.myPageName(), zPageLogin);

		zPageExternalRegistration = new PageExternalRegistration(this);
		pages.put(zPageExternalRegistration.myPageName(), zPageExternalRegistration);

		// Main page
		zPageMain = new PageMain(this);
		pages.put(zPageMain.myPageName(), zPageMain);

		zPageExternalMain = new PageExternalMain(this);
		pages.put(zPageExternalMain.myPageName(), zPageExternalMain);

		zPageSearch = new PageSearch(this);
		pages.put(zPageSearch.myPageName(), zPageSearch);

		zPageAdvancedSearch = new PageAdvancedSearch(this);
		pages.put(zPageAdvancedSearch.myPageName(), zPageAdvancedSearch);

		// Mail page
		zPageMail = new PageMail(this);
		pages.put(zPageMail.myPageName(), zPageMail);

		zTreeMail = new TreeMail(this);
		trees.put(zTreeMail.myPageName(), zTreeMail);

		// Addressbook page
		zPageContacts = new PageContacts(this);
		pages.put(zPageContacts.myPageName(), zPageContacts);

		zTreeContacts = new TreeContacts(this);
		trees.put(zTreeContacts.myPageName(), zTreeContacts);

		// Calendar page
		zPageCalendar = new PageCalendar(this);
		pages.put(zPageCalendar.myPageName(), zPageCalendar);

		zTreeCalendar = new TreeCalendar(this);
		trees.put(zTreeCalendar.myPageName(), zTreeCalendar);

		// PageBriefcase page
		zPageBriefcase = new PageBriefcase(this);
		pages.put(zPageBriefcase.myPageName(), zPageBriefcase);

		zTreeBriefcase = new TreeBriefcase(this);
		trees.put(zTreeBriefcase.myPageName(), zTreeBriefcase);

		// PageTasks page
		zPageTasks = new PageTasks(this);
		pages.put(zPageTasks.myPageName(), zPageTasks);

		zTreeTasks = new TreeTasks(this);
		trees.put(zTreeTasks.myPageName(), zTreeTasks);

		// Preferences page
		zPagePreferences = new PagePreferences(this);
		pages.put(zPagePreferences.myPageName(), zPagePreferences);

		zTreePreferences = new TreePreferences(this);
		trees.put(zTreePreferences.myPageName(), zTreePreferences);
		// signature Preferences page
		zPageSignature = new PageSignature(this);
		pages.put(zPageSignature.myPageName(), zPageSignature);

		// Zimlets
		zPageSocial = new PageSocial(this);
		pages.put(zPageSocial.myPageName(), zPageSocial);

		zPageChatPanel = new PageChatPanel(this);
		pages.put(zPageChatPanel.myPageName(), zPageChatPanel);

		// Drive
		zPageDrive = new PageDrive(this);
		pages.put(zPageDrive.myPageName(), zPageDrive);

		// Configure the localization strings
		getL10N().zAddBundlename(I18N.Catalog.I18nMsg);
		getL10N().zAddBundlename(I18N.Catalog.AjxMsg);
		getL10N().zAddBundlename(I18N.Catalog.ZMsg);
		getL10N().zAddBundlename(I18N.Catalog.ZsMsg);
		getL10N().zAddBundlename(I18N.Catalog.ZmMsg);

	}

	@Override
	public boolean zIsLoaded() throws HarnessException {
		if (this.zPageMain.zIsActive() || this.zPageLogin.zIsActive()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String myApplicationName() {
		return ("Ajax Client");
	}

	@Override
	public ZimbraAccount zSetActiveAccount(ZimbraAccount account) throws HarnessException {
		return (super.zSetActiveAccount(account));
	}
}