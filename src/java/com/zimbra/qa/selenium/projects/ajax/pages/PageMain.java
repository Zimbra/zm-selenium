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

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogError.*;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning.*;
import com.zimbra.qa.selenium.projects.ajax.pages.briefcase.PageBriefcase;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.PageCalendar;
import com.zimbra.qa.selenium.projects.ajax.pages.contacts.PageContacts;
import com.zimbra.qa.selenium.projects.ajax.pages.drive.PageDrive;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.*;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.PagePreferences;
import com.zimbra.qa.selenium.projects.ajax.pages.tasks.PageTasks;

public class PageMain extends AbsTab {

	public static class Locators {

		public static final String zLogoffPulldown = "css=td[id='skin_dropMenu'] div[class='DwtLinkButtonDropDownArrow']";
		public static final String zLogoffOption = "css=tr[id='POPUP_logOff'] td[id$='_title']";

		public static final String zMailApp = "id=zb__App__Mail_title";
		public static final String zContactsApp = "id=zb__App__Contacts_title";
		public static final String zCalendarApp = "id=zb__App__Calendar_title";
		public static final String zTasksApp = "id=zb__App__Tasks_title";
		public static final String zBriefcaseApp = "css=td[id=zb__App__Briefcase_title]";
		public static final String zPreferencesTab = "id=zb__App__Options_title";
		public static final String zDriveApp = "css=td[id=zb__App__ZIMBRA_DRIVE_title]";
		
		public static final String zSocialTab = "css=div[id^='zb__App__com_zimbra_social_'] td[id$='_title']";
		public static final String zRefreshButton = "css=div[id='CHECK_MAIL'] td[id='CHECK_MAIL_left_icon']>div";
	}

	public PageMain(AbsApplication application) {
		super(application);
		logger.info("new " + PageMain.class.getCanonicalName());
	}

	public Toaster zGetToaster() throws HarnessException {
		SleepUtil.sleepMedium();
		Toaster toaster = new Toaster(this.MyApplication);
		logger.info("toaster is active: " + toaster.zIsActive());
		return (toaster);
	}

	public DialogWarning zGetWarningDialog(DialogWarningID zimbra) {
		return (new DialogWarning(zimbra, this.MyApplication, this));
	}

	public DialogError zGetErrorDialog(DialogErrorID zimbra) {
		return (new DialogError(zimbra, this.MyApplication, this));
	}

	public boolean zIsZimletsPanelLoaded() throws HarnessException {
		for (int i = 0; i <= 15; i++) {
			boolean present = sIsElementPresent("css=div[id$='parent-ZIMLET'] td[id$='ZIMLET_textCell']");
			if (present == true) {
				SleepUtil.sleepSmall();
				return true;
			} else {
				SleepUtil.sleepMedium();
			}
		}
		return false;
	}

	public boolean zIsTagsPanelLoaded() throws HarnessException {
		for (int i = 0; i <= 15; i++) {
			boolean present = sIsElementPresent("css=div[id$='parent-TAG'] td[id$='TAG_textCell']");
			if (present == true) {
				SleepUtil.sleepSmall();
				return true;
			} else {
				SleepUtil.sleepMedium();
			}
		}
		return false;
	}

	public boolean zIsMinicalLoaded() throws HarnessException {
		return ("true".equals(sGetEval(
				"this.browserbot.getUserWindow().top.appCtxt.getAppViewMgr().getCurrentViewComponent(this.browserbot.getUserWindow().top.ZmAppViewMgr.C_TREE_FOOTER) != null")));
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		boolean present = sIsElementPresent(Locators.zLogoffPulldown);
		if (!present) {
			logger.info("Logoff button present = " + present);
			return (false);
		}

		if (ConfigProperties.getStringProperty("server.host").contains("zimbra.com")) {
			boolean loaded = zIsZimletsPanelLoaded();
			if (!loaded) {
				logger.info("zIsZimletsPanelLoaded() = " + loaded);
				return (false);
			}
		} else {
			boolean loaded = zIsTagsPanelLoaded();
			if (!loaded) {
				logger.info("zIsTagsPanelLoaded() = " + loaded);
				return (false);
			}
		}

		logger.info("isActive() = " + true);
		return (true);

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if (zIsActive()) {
			logger.info(myPageName() + " is already loaded");
			return;
		}

		if (!((AjaxPages) MyApplication).zPageLogin.zIsActive()) {
			((AjaxPages) MyApplication).zPageLogin.zNavigateTo();
		}

		((AjaxPages) MyApplication).zPageLogin.zLogin(ZimbraAccount.AccountZCS());
	}

	public void zLogout() throws HarnessException {
		logger.info("Logout of the " + MyApplication.myApplicationName());

		zNavigateTo();
		SleepUtil.sleepSmall();
		getElement("css=td[class='DwtLinkButtonDropDownArrowTd']").click();
		this.zWaitForBusyOverlay();

		getElement("css=tr[id=POPUP_logOff]>td[id=logOff_title]").click();
		this.zWaitForBusyOverlay();

		((AjaxPages) MyApplication).zPageLogin.zWaitForActive();
		((AjaxPages) MyApplication).zSetActiveAccount(null);
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
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown(" + pulldown + ", " + option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		SleepUtil.sleepLong();

		if (pulldown == Button.B_ACCOUNT) {

			if (option == Button.O_PRODUCT_HELP) {

				pulldownLocator = "css=div#skin_outer td#skin_dropMenu div.DwtLinkButtonDropDownArrow";
				optionLocator = "css=div[id^='POPUP'] div[id='documentation'] td[id$='_title']";

				SeparateWindow window = new SeparateWindow(this.MyApplication);

				this.sClickAt(pulldownLocator, "0,0");
				this.zWaitForBusyOverlay();
				SleepUtil.sleepSmall();

				this.sClickAt(optionLocator, "0,0");
				this.zWaitForBusyOverlay();
				SleepUtil.sleepMedium();

				return (window);

			} else if (option == Button.O_ABOUT) {

				pulldownLocator = "css=div#skin_outer td#skin_dropMenu div.DwtLinkButtonDropDownArrow";
				optionLocator = "css=div[id^='POPUP'] div[id='about'] td[id$='_title']";
				page = new DialogInformational(DialogInformational.DialogWarningID.InformationalDialog,
						this.MyApplication, this);

			} else if (option == Button.O_SHORTCUT) {

				pulldownLocator = "css=div#skin_outer td#skin_dropMenu div.DwtLinkButtonDropDownArrow";
				optionLocator = "css=div[id^='POPUP'] div[id='showCurrentShortcuts'] td[id$='_title']";
				page = new DialogInformational(DialogInformational.DialogWarningID.ShortcutDialog, this.MyApplication,
						this);

			} else {

				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
			}

		} else {
			throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
		}

		if (pulldownLocator != null) {

			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator "
						+ pulldownLocator + " not present!");
			}

			this.sClickAt(pulldownLocator, "0,0");
			zWaitForBusyOverlay();
			SleepUtil.sleepSmall();

			if (optionLocator != null) {
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator "
							+ optionLocator + " not present!");
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
	public AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException {
		throw new HarnessException("Main page does not have lists");
	}

	/**
	 * Throw an exception on error dialog found and refresh page on any un-closed
	 * dialogs found
	 */
	public void zHandleDialogs(AbsTab appTab) throws HarnessException {

		// Opened dialogs
		String zIndex;
		List<WebElement> dialogLocators = webDriver()
				.findElements(By.cssSelector("div[class^='Dwt'][class$='Dialog']"));

		int totalDialogs = dialogLocators.size();
		logger.info("Total dialogs found " + totalDialogs);

		for (int i = totalDialogs - 1; i >= 0; i--) {
			zIndex = dialogLocators.get(i).getCssValue("z-index");
			if (!zIndex.equals("auto") && !zIndex.equals("") && !zIndex.equals(null)
					&& Integer.parseInt(zIndex) >= 700) {
				logger.info("##### Found active dialog #####");
				zRefreshMainUI();
				dialogLocators = webDriver().findElements(By.cssSelector("div[class^='Dwt'][class$='Dialog']"));
				totalDialogs = dialogLocators.size();
				for (int j = totalDialogs - 1; j >= 0; j--) {
					zIndex = dialogLocators.get(j).getCssValue("z-index");
					if (!zIndex.equals("auto") && !zIndex.equals("") && !zIndex.equals(null)
							&& Integer.parseInt(zIndex) >= 700) {
						throw new HarnessException("##### Active dialog found after reloading page #####");
					}
				}
				logger.info("Navigate to " + appTab.myPageName());
				appTab.zNavigateTo();
				return;
			}
		}

		logger.info("No active dialogs found");
	}

	/**
	 * Close any extra compose tabs
	 */
	public void zHandleComposeTabs() throws HarnessException {

		String locator = "css=div[id^='zb__App__tab']";
		if (sIsElementPresent(locator)) {
			int count = this.sGetCssCount(locator);
			logger.info("Found " + count + " opened compose tabs");
			for (int i = 1; i <= count; i++) {
				final String composeLocator = locator + ":nth-of-type(1) td[id$='_right_icon']";
				if (!sIsElementPresent(composeLocator))
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
	 *
	 * @param uri
	 *            The URL to access (e.g.
	 *            ?to=foo@foo.com&body=MsgContent&subject=MsgSubject&view=compose)
	 *            the page that opens
	 *
	 */
	public AbsPage zOpenDeeplink(ZimbraURI uri) throws HarnessException {
		logger.info("PageMain.zOpenDeeplink(" + uri.toString() + ")");

		AbsPage page = null;

		if (!uri.getQuery().containsKey("view")) {
			throw new HarnessException("query attribute 'view' must be specified");
		}

		if (uri.getQuery().get("view").equals("compose")) {
			page = new FormMailNew(this.MyApplication);

		} else if (uri.getQuery().get("view").equals("msg")) {
			throw new HarnessException("implement me!");

		} else {
			throw new HarnessException("query attribute 'view' must be specified");
		}

		// Re-open the URL
		this.sOpen(uri.getURL().toString());

		if (page != null) {
			page.zWaitForActive();
		}

		return (page);
	}

	// Various kind of close window (with title) methods

	public void zCloseWindow(SeparateWindow window, String windowTitle, AjaxPages app) throws HarnessException {
		zCoreCloseWindow(window, windowTitle, app);
	}

	public void zCloseWindow(SeparateWindowFormMailNew window, String windowTitle, AjaxPages app)
			throws HarnessException {
		zCoreCloseWindow(window, windowTitle, app);
	}

	public void zCloseWindow(SeparateWindowDisplayMail window, String windowTitle, AjaxPages app)
			throws HarnessException {
		zCoreCloseWindow(window, windowTitle, app);
	}

	public void zCloseWindow(SeparateWindowOpenAttachment window, String windowTitle, AjaxPages app)
			throws HarnessException {
		zCoreCloseWindow(window, windowTitle, app);
	}

	private void zCoreCloseWindow(AbsSeparateWindow window, String windowTitle, AjaxPages app)
			throws HarnessException {
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

	public void zCloseWindow(SeparateWindow window, AjaxPages app) throws HarnessException {
		zCoreCloseWindow(window, app);
	}

	public void zCloseWindow(SeparateWindowOpenAttachment window, AjaxPages app) throws HarnessException {
		zCoreCloseWindow(window, app);
	}

	private void zCoreCloseWindow(AbsSeparateWindow window, AjaxPages app) throws HarnessException {
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

	public AbsTab zGetCurrentApp() throws HarnessException {

		String mailZimletsPane = null, contactsZimletsPane = null, calendarZimletsPane = null;
		String tasksZimletsPane = null, briefcaseZimletsPane = null, generalPreferencesOverviewPane = null;
		String drivePane = null;

		mailZimletsPane = PageMail.Locators.zMailZimletsPane;
		contactsZimletsPane = PageContacts.Locators.zContactsZimletsPane;
		calendarZimletsPane = PageCalendar.Locators.zCalendarZimletsPane;
		tasksZimletsPane = PageTasks.Locators.zTasksZimletsPane;
		briefcaseZimletsPane = PageBriefcase.Locators.zBriefcaseZimletsPane;
		generalPreferencesOverviewPane = PagePreferences.Locators.zGeneralPreferencesOverviewPane;
		drivePane = PageDrive.Locators.zDriveFolderPane.toString();
		

		if (sIsVisible(mailZimletsPane)) {
			return ((AjaxPages) MyApplication).zPageMail;
		} else if (sIsVisible(contactsZimletsPane)) {
			return ((AjaxPages) MyApplication).zPageContacts;
		} else if (sIsVisible(calendarZimletsPane)) {
			return ((AjaxPages) MyApplication).zPageCalendar;
		} else if (sIsVisible(tasksZimletsPane)) {
			return ((AjaxPages) MyApplication).zPageTasks;
		} else if (sIsVisible(briefcaseZimletsPane)) {
			return ((AjaxPages) MyApplication).zPageBriefcase;
		} else if (sIsVisible(generalPreferencesOverviewPane)) {
			return ((AjaxPages) MyApplication).zPagePreferences;
		} else if (sIsVisible(drivePane)) {
			return ((AjaxPages) MyApplication).zPageDrive;
		} else {
			logger.info("Unable to find current app");
			return ((AjaxPages) MyApplication).zPageMail;
		}
	}

	public void zCheckAppLoaded(String appIdentifier) throws HarnessException {

		AbsTab appTab;
		String appLocator = null;
		String commonAppLocator = "css=div[id$='__ZIMLET']";

		if (appIdentifier.contains("Mail")) {
			appTab = ((AjaxPages) MyApplication).zPageMail;
			appLocator = PageMain.Locators.zMailApp;

		} else if (appIdentifier.contains("Contacts")) {
			appTab = ((AjaxPages) MyApplication).zPageContacts;
			appLocator = PageMain.Locators.zContactsApp;

		} else if (appIdentifier.contains("Calendar")) {
			appTab = ((AjaxPages) MyApplication).zPageCalendar;
			appLocator = PageMain.Locators.zCalendarApp;

		} else if (appIdentifier.contains("Tasks")) {
			appTab = ((AjaxPages) MyApplication).zPageTasks;
			appLocator = PageMain.Locators.zTasksApp;

		} else if (appIdentifier.contains("Briefcase")) {
			appTab = ((AjaxPages) MyApplication).zPageBriefcase;
			appLocator = PageMain.Locators.zBriefcaseApp;

		} else if (appIdentifier.contains("DRIVE")) {
			appTab = ((AjaxPages) MyApplication).zPageDrive;
			appLocator = PageMain.Locators.zDriveApp;

		} else if (appIdentifier.contains("Options")) {
			appTab = ((AjaxPages) MyApplication).zPagePreferences;
			appLocator = PageMain.Locators.zPreferencesTab;

		} else {
			appTab = ((AjaxPages) MyApplication).zPageMail;
			appLocator = PageMain.Locators.zMailApp;
			logger.info("Unable to find application tab identifier " + appIdentifier);
		}

		if (!((AjaxPages) MyApplication).zPageMain.zIsActive()) {
			zHandleDialogs(appTab);
			((AjaxPages) MyApplication).zPageMain.zNavigateTo();
		}

		// Navigate to app
		logger.info("Navigate to " + appTab.myPageName());

		for (int i = 0; i <= 2; i++) {

			if (!appTab.zIsActive()) {
				zHandleDialogs(appTab);

				if (appTab.equals(((AjaxPages) MyApplication).zPageCalendar)) {
					SleepUtil.sleepMedium();
				} else {
					SleepUtil.sleepLong();
				}

				sClickAt(appLocator, "");
				this.zWaitForBusyOverlay();
				SleepUtil.sleepMedium();
				if (!appTab.equals(((AjaxPages) MyApplication).zPagePreferences)
						&& !appTab.equals(((AjaxPages) MyApplication).zPageDrive)) {
					zWaitForElementPresent(commonAppLocator);
				}

				if (zGetCurrentApp().equals(appTab)) {
					logger.info("Navigated to " + appTab + " page");
					break;
				}
			}
		}
	}
}