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
/**
 *
 */
package com.zimbra.qa.selenium.projects.ajax.ui;

import java.awt.event.KeyEvent;

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

		public static final String zLogoffPulldown = "css=td[id='skin_dropMenu'] div[class='DwtLinkButtonDropDownArrow']";
		public static final String zLogoffOption = "css=tr[id='POPUP_logOff'] td[id$='_title']";

		public static final String zMailApp	= "id=zb__App__Mail_title";
		public static final String zContactsApp = "id=zb__App__Contacts_title";
		public static final String zCalendarApp = "id=zb__App__Calendar_title";
		public static final String zTasksApp = "id=zb__App__Tasks_title";
		public static final String zBriefcaseApp = "css=td[id=zb__App__Briefcase_title]";
		public static final String zPreferencesTab	= "id=zb__App__Options_title";

		public static final String zSocialTab	= "css=div[id^='zb__App__com_zimbra_social_'] td[id$='_title']";
		public static final String zRefreshButton = "css=div[id='CHECK_MAIL'] td[id='CHECK_MAIL_left_icon']>div";
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

		if (ConfigProperties.getStringProperty("server.host").contains("local") == true) {

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
			return;
		}

		if ( !((AppAjaxClient)MyApplication).zPageLogin.zIsActive() ) {
			((AppAjaxClient)MyApplication).zPageLogin.zNavigateTo();
		}
		((AppAjaxClient)MyApplication).zPageLogin.zLogin(ZimbraAccount.AccountZWC());

		zWaitForActive(100000);

	}

	/**
	 * Click the logout button
	 * @throws HarnessException
	 */
	public void zLogout() throws HarnessException {
		logger.debug("logout()");

		tracer.trace("Logout of the "+ MyApplication.myApplicationName());

		zNavigateTo();

		SleepUtil.sleepMedium();
		getElement("css=td[class='DwtLinkButtonDropDownArrowTd']").click();
		this.zWaitForBusyOverlay();
		
		getElement("css=tr[id=POPUP_logOff]>td[id=logOff_title]").click();
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
			locator = Locators.zRefreshButton;
			page = null;

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		this.sClickAt(locator, "0,0");
		this.zWaitForBusyOverlay();
		SleepUtil.sleepMedium();

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


		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		SleepUtil.sleepLong(); // Intentional sleep due to failure and for reliability

		if (pulldown == Button.B_ACCOUNT) {

			if (option == Button.O_PRODUCT_HELP) {

				pulldownLocator = "css=div#skin_outer td#skin_dropMenu div.DwtLinkButtonDropDownArrow";
				optionLocator = "css=div[id^='POPUP'] div[id='documentation'] td[id$='_title']";

				SeparateWindow window = new SeparateWindow(this.MyApplication);

				this.zClickAt(pulldownLocator, "0,0");
				this.zWaitForBusyOverlay();
				SleepUtil.sleepSmall();

				this.zClickAt(optionLocator, "0,0");
				this.zWaitForBusyOverlay();
				SleepUtil.sleepSmall();

				return (window);

			} else if (option == Button.O_ABOUT) {

				pulldownLocator = "css=div#skin_outer td#skin_dropMenu div.DwtLinkButtonDropDownArrow";
				optionLocator = "css=div[id^='POPUP'] div[id='about'] td[id$='_title']";
				page = new DialogInformational(DialogInformational.DialogWarningID.InformationalDialog, this.MyApplication, this);

			} else if (option == Button.O_SHORTCUT) {

				pulldownLocator = "css=div#skin_outer td#skin_dropMenu div.DwtLinkButtonDropDownArrow";
				optionLocator = "css=div[id^='POPUP'] div[id='showCurrentShortcuts'] td[id$='_title']";
				page = new DialogInformational(DialogInformational.DialogWarningID.ShortcutDialog, this.MyApplication, this);

		} else {

				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
			}

		} else {
			throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
		}

		if (pulldownLocator != null) {

			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator " + pulldownLocator + " not present!");
			}

			this.sClickAt(pulldownLocator, "0,0");
			zWaitForBusyOverlay();
			SleepUtil.sleepSmall();

			if (optionLocator != null) {

				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator " + optionLocator + " not present!");
				}

				this.sClickAt(optionLocator, "0,0");
				zWaitForBusyOverlay();

			}

		}

		if (page != null) {
			page.zWaitForActive();
		}

		SleepUtil.sleepSmall();

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
	 * Close any extra dialogs
	 */
	public void zCloseOpenDialogs() throws HarnessException {

		String okButtonLocator = "css=div[class='DwtDialog'] td[id$='_title']:contains('OK')";
		String noButtonLocator = "css=div[class='DwtDialog'] td[id$='_title']:contains('No')";
		String cancelButtonLocator = "css=div[class='DwtDialog'] td[id$='_title']:contains('Cancel')";
		String closeButtonLocator = "css=div[class='DwtDialog'] td[id$='_title']:contains('Close')";

		for (int i=0; i<=1; i++) {
			if ( this.sIsVisible(okButtonLocator) || this.sIsVisible(noButtonLocator) || this.sIsVisible(cancelButtonLocator) || this.sIsVisible(closeButtonLocator) ) {
				logger.debug("Found open dialogs");
				
				// Need to handle multiple dialogs if opened (safer side)
				this.zKeyboardKeyEvent(KeyEvent.VK_ESCAPE);
				SleepUtil.sleepSmall();

				if (sIsVisible(cancelButtonLocator)) {
					this.sClick(cancelButtonLocator);
					SleepUtil.sleepSmall();
					if (sIsElementPresent("css=td[id^='YesNoCancel'][id$='_title']:contains('No')")) {
						this.sClick("css=td[id^='YesNoCancel'][id$='_title']:contains('No')");
						SleepUtil.sleepSmall();
					}

				} else if (sIsVisible(okButtonLocator)) {
					this.sClick(okButtonLocator);
					SleepUtil.sleepSmall();
					if (sIsElementPresent("css=td[id^='YesNoCancel'][id$='_title']:contains('No')")) {
						this.sClick("css=td[id^='YesNoCancel'][id$='_title']:contains('No')");
						SleepUtil.sleepSmall();
					}
				}

			} else {
				break;
			}
		}
	}

	/**
	 * Close any extra compose tabs
	 */
	public void zCloseComposeTabs() throws HarnessException {

		String locator = "css=div[id^='zb__App__tab']";
		if ( sIsElementPresent(locator) ) {
			int count = this.sGetCssCount(locator);
			logger.info("Found " + count + " opened compose tabs");
			for (int i = 1; i <= count; i++) {
				final String composeLocator = locator + ":nth-of-type(1) td[id$='_right_icon']";
				if ( !sIsElementPresent(composeLocator) )
					throw new HarnessException("Unable to find compose tab close icon " + composeLocator);
				this.sClick(composeLocator);
				if (sIsElementPresent("css=td[id^='YesNoCancel'][id$='_title']:contains('No')")) {
					SleepUtil.sleepSmall();
					this.sClick("css=td[id^='YesNoCancel'][id$='_title']:contains('No')");
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



		} else if ( uri.getQuery().get("view").equals("msg") ) {

			// page = new DisplayMail(this.MyApplication);
			throw new HarnessException("implement me!");



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
	
	
	// Various kind of close window (with title) methods 
	
	public void closeWindow (SeparateWindow window, String windowTitle, AppAjaxClient app) throws HarnessException {
	    coreCloseWindow(window, windowTitle, app);
	}	
	
	public void closeWindow (SeparateWindowFormMailNew window, String windowTitle, AppAjaxClient app) throws HarnessException {
		coreCloseWindow(window, windowTitle, app);
	}
	
	public void closeWindow (SeparateWindowDisplayMail window, String windowTitle, AppAjaxClient app) throws HarnessException {
		coreCloseWindow(window, windowTitle, app);
	}
	
	public void closeWindow (SeparateWindowOpenAttachment window, String windowTitle, AppAjaxClient app) throws HarnessException {
		coreCloseWindow(window, windowTitle, app);
	}
	
	private void coreCloseWindow (AbsSeparateWindow window, String windowTitle, AppAjaxClient app) throws HarnessException {
		if (window != null) {
			window.zCloseWindow(windowTitle);
			window = null;
		}
		try {
			app.zPageMain.zSelectWindow(null);
		} catch (HarnessException e) {
			e.printStackTrace();
		}
	}
	
	
	// Various kind of close window (without title) methods
	
	public void closeWindow (SeparateWindowOpenAttachment window, AppAjaxClient app) throws HarnessException {
		coreCloseWindow(window, app);
	}
	
	private void coreCloseWindow (AbsSeparateWindow window, AppAjaxClient app) throws HarnessException {
		if (window != null) {
			window.zCloseWindow();
			window = null;
		}
		try {
			app.zPageMain.zSelectWindow(null);
		} catch (HarnessException e) {
			e.printStackTrace();
		}
	}
	
	
}
