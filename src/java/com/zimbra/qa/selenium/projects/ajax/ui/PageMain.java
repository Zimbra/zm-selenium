/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogError.*;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.*;

/**
 * @author Matt Rhoades
 *
 */
public class PageMain extends AbsTab {

	public static class Locators {
				
		public static final String zLogoffPulldown		= "css=td[id='skin_dropMenu'] div[class='DwtLinkButtonDropDownArrow']";
		public static final String zLogoffOption		= "css=tr[id='POPUP_logOff'] td[id$='_title']";
		
		public static final String zAppbarMail			= "id=zb__App__Mail_title";
		public static final String zAppbarContact		= "id=zb__App__Contacts_title";
		public static final String zAppbarCal			= "id=zb__App__Calendar_title";
		public static final String zAppbarTasks			= "id=zb__App__Tasks_title";
		public static final String zAppbarBriefcase		= "css=td[id=zb__App__Briefcase_title]";
		public static final String zAppbarPreferences	= "id=zb__App__Options_title";

		public static final String zAppbarSocialLocator	= 		"css=div[id^='zb__App__com_zimbra_social_'] td[id$='_title']";
		public static final String ButtonRefreshLocatorCSS = "css=div[id='CHECK_MAIL'] td[id='CHECK_MAIL_left_icon']>div";
	}
	
	public PageMain(AbsApplication application) {
		super(application);
		logger.info("new " + PageMain.class.getCanonicalName());
	}
	
	public Toaster zGetToaster() throws HarnessException {
		SleepUtil.sleepMedium();
		Toaster toaster = new Toaster(this.MyApplication);
		logger.info("toaster is active: "+ toaster.zIsActive());
		return (toaster);
	}
	
	public DialogWarning zGetWarningDialog(DialogWarningID zimbra) {
		return (new DialogWarning(zimbra, this.MyApplication, this));
	}
	
	public DialogError zGetErrorDialog(DialogErrorID zimbra) {
		return (new DialogError(zimbra, this.MyApplication, this));
	}

	public boolean zIsZimletsPanelLoaded() throws HarnessException {
        for (int i=0; i<=60; i++) {
            boolean present = sIsElementPresent("css=div[id$='parent-ZIMLET'] td[id$='ZIMLET_textCell']");
            if (present == true) {
                SleepUtil.sleepSmall();
                return true;
            } else {
                SleepUtil.sleepMedium();
                if (i == 60) {
                    return false;
                }
            }
        }
        return false;
    }
	
	public boolean zIsTagsPanelLoaded() throws HarnessException {
        for (int i=0; i<=60; i++) {
            boolean present = sIsElementPresent("css=div[id$='parent-TAG'] td[id$='TAG_textCell']");
            if (present == true) {
                SleepUtil.sleepSmall();
                return true;
            } else {
                SleepUtil.sleepMedium();
                if (i == 60) {
                    return false;
                }
            }
        }
        return false;
    }
	
	public boolean zIsMinicalLoaded() throws HarnessException {
		return ("true".equals(sGetEval("this.browserbot.getUserWindow().top.appCtxt.getAppViewMgr().getCurrentViewComponent(this.browserbot.getUserWindow().top.ZmAppViewMgr.C_TREE_FOOTER) != null")));
	}
	
	@Override
	public boolean zIsActive() throws HarnessException {

		boolean present = sIsElementPresent(Locators.zLogoffPulldown);
		if ( !present ) {
			logger.info("Logoff button present = "+ present);
			return (false);
		}
				
		if (ZimbraSeleniumProperties.getStringProperty("server.host").contains("local") == true) {
			
			boolean loaded = zIsTagsPanelLoaded();
			if ( !loaded) {
				logger.info("zIsTagsPanelLoaded() = "+ loaded);
				return (false);
			}
			
		} else {
			
			boolean loaded = zIsZimletsPanelLoaded();
			if ( !loaded) {
				logger.info("zIsZimletsPanelLoaded() = "+ loaded);
				return (false);
			}
		}
		
		logger.info("isActive() = "+ true);
		return (true);

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if ( zIsActive() ) {
			// This page is already active
			return;
		}

		if ( !((AppAjaxClient)MyApplication).zPageLogin.zIsActive() ) {
			((AppAjaxClient)MyApplication).zPageLogin.zNavigateTo();
		}
		((AppAjaxClient)MyApplication).zPageLogin.zLogin(ZimbraAccount.AccountZWC());
		
		zWaitForActive(120000);
		
	}

	/**
	 * Click the logout button
	 * @throws HarnessException
	 */
	public void zLogout() throws HarnessException {
		logger.debug("logout()");

		tracer.trace("Logout of the "+ MyApplication.myApplicationName());

		zNavigateTo();

		if (ZimbraSeleniumProperties.isWebDriver()) {
			getElement("css=div[id=DwtLinkButtonDropDownArrowTd").click();
			
		} else {
		
			if ( !sIsElementPresent(Locators.zLogoffPulldown) ) {
				throw new HarnessException("The logoff button is not present " + Locators.zLogoffPulldown);
			}
			zClickAt(Locators.zLogoffPulldown, "0,0");
		}
		
		this.zWaitForBusyOverlay();
		
		if (ZimbraSeleniumProperties.isWebDriver()) {
			getElement("css=tr[id=POPUP_logOff]>td[id=logOff_title]").click();
			
		} else {
			if ( !sIsElementPresent(Locators.zLogoffOption) ) {
				throw new HarnessException("The logoff button is not present " + Locators.zLogoffOption);
			}

			zClick(Locators.zLogoffOption);
		}
		
		this.zWaitForBusyOverlay();

		((AppAjaxClient)MyApplication).zPageLogin.zWaitForActive();

		((AppAjaxClient)MyApplication).zSetActiveAcount(null);

	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_REFRESH) {
			
			locator = Locators.ButtonRefreshLocatorCSS;
			page = null;
			
		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		this.sClickAt(locator, "0,0");
		this.zWaitForBusyOverlay();
		
		SleepUtil.sleepSmall();

		return (page);
		
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ option +")");
		
		tracer.trace("Click pulldown "+ pulldown +" then "+ option);
		
		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		// Default behavior variables
		String pulldownLocator = null; // If set, this will be expanded
		String optionLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned
		
		if (pulldown == Button.B_ACCOUNT) {
			
			if (option == Button.O_PRODUCT_HELP) {

				pulldownLocator = "css=div#skin_outer td#skin_dropMenu div.DwtLinkButtonDropDownArrow";
				optionLocator = "css=div[id^='POPUP'] div[id='documentation'] td[id$='_title']";
				
				SeparateWindow window = new SeparateWindow(this.MyApplication);
				window.zInitializeWindowNames();
				
				this.zClickAt(pulldownLocator, "0,0");
				this.zWaitForBusyOverlay();

				this.zClickAt(optionLocator, "0,0");
				this.zWaitForBusyOverlay();

				return (window);
				
			} else if (option == Button.O_ABOUT) {

					pulldownLocator = "css=div#skin_outer td#skin_dropMenu div.DwtLinkButtonDropDownArrow";
					optionLocator = "css=div[id^='POPUP'] div[id='about'] td[id$='_title']";
					page = new DialogInformational(DialogInformational.DialogWarningID.InformationalDialog, this.MyApplication, this);

					// FALL THROUGH
					
			}else if (option == Button.O_SHORTCUT) {

				pulldownLocator = "css=div#skin_outer td#skin_dropMenu div.DwtLinkButtonDropDownArrow";
				optionLocator = "css=div[id^='POPUP'] div[id='showCurrentShortcuts'] td[id$='_title']";
				page = new DialogInformational(DialogInformational.DialogWarningID.ShortcutDialog, this.MyApplication, this);

				// FALL THROUGH
				
		} else {
				
				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
			}
			

		} else {
			throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
		}

		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator " + pulldownLocator + " not present!");
			}

			this.zClickAt(pulldownLocator, "0,0");

			// If the app is busy, wait for it to become active
			zWaitForBusyOverlay();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator " + optionLocator + " not present!");
				}

				this.zClickAt(optionLocator, "0,0");

				// If the app is busy, wait for it to become active
				zWaitForBusyOverlay();
				
			}

		}
		
		// If we click on pulldown/option and the page is specified, then
		// wait for the page to go active
		if (page != null) {
			
			page.zWaitForActive();
			
		}

		// Return the specified page, or null if not set
		return (page);

	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		throw new HarnessException("Main page does not have lists");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		throw new HarnessException("Main page does not have lists");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption ,String item)
			throws HarnessException {
		throw new HarnessException("Main page does not have lists");
	}	
	/**
	 * Close any extra compose tabs
	 */
	public void zCloseComposeTabs() throws HarnessException {
		
		String locator = "css=div[id^='zb__App__tab']";
		if ( sIsElementPresent(locator) ) {
			logger.debug("Found compose tabs");
			int count = this.sGetCssCount(locator);
			for (int i = 1; i <= count; i++) {
				final String composeLocator = locator + ":nth-child("+i+") td[id$='_right_icon']";
				if ( !sIsElementPresent(composeLocator) ) 
					throw new HarnessException("Unable to find compose tab close icon "+ composeLocator);
				this.zClick(composeLocator);
				if (sIsElementPresent("css=td[id^='YesNoCancel'][id$='_title']:contains('No')")) {
					SleepUtil.sleepSmall();
					this.zClickAt("css=td[id^='YesNoCancel'][id$='_title']:contains('No')", "0,0");
				}
			}
		}
	}

	/**
	 * Change the URL (and reload) to access deep-link pages
	 * @param uri The URL to access (e.g. ?to=foo@foo.com&body=MsgContent&subject=MsgSubject&view=compose)
	 * @return the page that opens
	 * @throws HarnessException 
	 */
	public AbsPage zOpenDeeplink(ZimbraURI uri) throws HarnessException {
		logger.info("PageMain.zOpenDeeplink("+ uri.toString() + ")");
		
		AbsPage page = null;
		
		
		if ( !uri.getQuery().containsKey("view") ) {
			throw new HarnessException("query attribute 'view' must be specified");
		}
		
		if ( uri.getQuery().get("view").equals("compose") ) {
			
			page = new FormMailNew(this.MyApplication);
			
			// FALL THROUGH
			
		} else if ( uri.getQuery().get("view").equals("msg") ) {
			
			// page = new DisplayMail(this.MyApplication);
			throw new HarnessException("implement me!");
			
			// FALL THROUGH
			
		} else {
			
			throw new HarnessException("query attribute 'view' must be specified");
			
		}
		
		// Re-open the URL
		this.sOpen(uri.getURL().toString());

		if ( page != null ) {
			page.zWaitForActive();
		}
		
		return (page);

	}

	

}
