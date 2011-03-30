/**
 * 
 */
package com.zimbra.qa.selenium.projects.ajax.ui.briefcase;

import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;
import com.zimbra.qa.selenium.framework.items.DocumentItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.GeneralUtility;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties.AppType;
import com.zimbra.qa.selenium.projects.ajax.ui.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;

/**
 * @author
 * 
 */
public class PageBriefcase extends AbsTab {

	public DocumentItem docItem;

	public static class Locators {
		public static final String zNewBriefcaseOverviewPaneIcon = "id=ztih__main_Briefcase__BRIEFCASE_textCell";
		public static final String zBriefcaseFolder = "id=zti__main_Briefcase__16_textCell";
		public static final String briefcaseListView = "css=div[id='zl__BDLV__rows'][class='DwtListView-Rows']";
		public static final String zBriefcaseFolderIcon = "id=zti__main_Briefcase__16";
		public static final String zBriefcaseFolderIcon_Desktop = "css=div[id*='Briefcase'][id$='16_div']";
		public static final String zTrashFolder = "id=zti__main_Briefcase__3_textCell";
		public static final String zBriefcaseAppIconBtn = "id=zb__App__Briefcase_left_icon";
		public static final String zNewMenuIconBtn = "id=zb__BCD__NEW_FILE_left_icon";
		public static final String zNewMenuLeftIconBtn = "id=zb__BDLV__NEW_MENU_left_icon";
		public static final String zNewMenuArrowBtn = "css=div[id=zb__BDLV__NEW_MENU] div[class^=ImgSelectPullDownArrow]";
		public static final String zUploadFileIconBtn = "id=zb__BDLV__NEW_FILE_left_icon";
		public static final String zEditFileIconBtn = "id=zb__BDLV__EDIT_FILE_left_icon";
		public static final String zOpenFileInSeparateWindowIconBtn = "id=zb__BDLV__NEW_BRIEFCASE_WIN_left_icon";
		public static final String zDeleteIconBtn = "id=zb__BDLV__DELETE_left_icon";
		public static final String zDeleteBtn = "id=zb__BDLV__DELETE";
		public static final String zMoveIconBtn = "id=zb__BDLV__MOVE_left_icon";
		public static final String zMoveBtn = "id=zb__BDLV__MOVE";
		public static final String zTagItemIconBtn = "id=zb__BCD__TAG_MENU_left_icon";
		public static final String zViewIconBtn = "id=zb__BCD__VIEW_MENU_left_icon";
		public static final String zSendBtnIconBtn = "id=zb__BCD__SEND_FILE_left_icon";
		public static final String zNewDocumentIconBtn = "id=zb__BCD__NEW_DOC_left_icon";
		public static final String zNewSpreadsheetIconBtn = "id=zb__BCD__NEW_SPREADSHEET_left_icon";
		public static final String zNewPresentationIconBtn = "id=zb__BCD__NEW_PRESENTATION_left_icon";
		public static final String zRenameInput = "css=div[class^=RenameInput]>input";
		public static final String zFileBodyField = "css=html>body";
	}

	public PageBriefcase(AbsApplication application) {
		super(application);
		logger.info("new " + PageBriefcase.class.getCanonicalName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see projects.admin.ui.AbsPage#isActive()
	 */
	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the main page is active
		// if (!((AppAjaxClient) MyApplication).zPageMain.zIsActive())
		// ((AppAjaxClient) MyApplication).zPageMain.zNavigateTo();

		// If the "folders" tree is visible, then Briefcase tab is active

		String locator = null;
		if (ZimbraSeleniumProperties.getAppType() == AppType.DESKTOP) {
			String currentActiveEmailAddress = MyApplication
					.zGetActiveAccount() != null ? MyApplication
					.zGetActiveAccount().EmailAddress : ZimbraAccount
					.AccountZWC().EmailAddress;
			locator = Locators.zBriefcaseFolderIcon_Desktop + "[id*='"
					+ currentActiveEmailAddress + "']";
		} else {
			locator = Locators.zBriefcaseFolderIcon;
		}

		boolean loaded = this.sIsElementPresent(locator);

		if (!loaded)
			return (loaded);
		boolean active = this.zIsVisiblePerPosition(locator, 0, 0);
		return (active);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see projects.admin.ui.AbsPage#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see projects.admin.ui.AbsPage#navigateTo()
	 */
	@Override
	public void zNavigateTo() throws HarnessException {

		// Check if this page is already active.
		if (zIsActive()) {
			return;
		}

		tracer.trace("Navigate to " + this.myPageName());

		String locator = "css=[id='zov__main_Mail']";
		// Make sure we are logged into the Ajax app
		// if (!((AppAjaxClient) MyApplication).zPageMain.zIsActive())
		// ((AppAjaxClient) MyApplication).zPageMain.zNavigateTo();

		// make sure mail page is loaded
		if (ZimbraSeleniumProperties.getAppType() == AppType.DESKTOP) {
			((AppAjaxClient) MyApplication).zPageMail.zNavigateTo();
			GeneralUtility.waitForElementPresent(this,
					PageMain.Locators.zAppbarBriefcase, 20000);
		} else {
			zWaitForElementPresent(locator);
		}
		// Click on Briefcase icon
		zClick(PageMain.Locators.zAppbarBriefcase);

		zWaitForBusyOverlay();

		if (ZimbraSeleniumProperties.getAppType() == AppType.DESKTOP) {
			zWaitForActive();
		} else {
			zWaitForElementPresent(Locators.zBriefcaseFolderIcon);
		}
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		// Default behavior variables
		//
		String locator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		// Based on the button specified, take the appropriate action(s)
		//

		if (button == Button.B_NEW) {
			// Check if the button is visible
			String attrs = sGetAttribute("xpath=(//div[@id='zb__BDLV__NEW_MENU'])@style");
			if (!attrs.contains("visible")) {
				throw new HarnessException(button + " not visible " + attrs);
			}
			locator = Locators.zNewMenuLeftIconBtn;

			// Click on New Document icon
			this.zClick(locator);

			zWaitForBusyOverlay();

			// isEditDocLoaded("Zimbra Docs", "");

			page = new DocumentBriefcaseNew(this.MyApplication);

			page.zIsActive();

			return page;
		} else if (button == Button.B_UPLOAD_FILE) {
			// Check if the button is visible
			String attrs = sGetAttribute("xpath=(//div[@id='zb__BDLV__NEW_FILE'])@style");
			if (!attrs.contains("visible")) {
				throw new HarnessException(button + " not visible " + attrs);
			}
			locator = Locators.zUploadFileIconBtn;
			page = null;
		} else if (button == Button.B_EDIT_FILE) {
			// Check if the button is visible
			String attrs = sGetAttribute("xpath=(//div[@id='zb__BDLV__EDIT_FILE'])@style");
			if (!attrs.contains("visible")) {
				throw new HarnessException(button + " not visible " + attrs);
			}
			locator = Locators.zEditFileIconBtn;
			page = new DocumentBriefcaseEdit(this.MyApplication);
		} else if (button == Button.B_DELETE) {
			// Check if the button is visible
			String attrs = sGetAttribute("css=div[id='zb__BDLV__DELETE']@style");
			if (!attrs.contains("visible")) {
				throw new HarnessException(button + " not visible " + attrs);
			}
			locator = Locators.zDeleteIconBtn;

			page = new DialogDeleteConfirm(MyApplication, this);
		} else if (button == Button.B_OPEN_IN_SEPARATE_WINDOW) {
			// Check if the button is disabled
			String attrs = sGetAttribute("css=td["
					+ Locators.zOpenFileInSeparateWindowIconBtn + "]>div@class");
			if (attrs.contains("ZDisabledImage")) {
				throw new HarnessException(button + " is disabled " + attrs);
			}
			locator = Locators.zOpenFileInSeparateWindowIconBtn;
			page = new DocumentBriefcaseOpen(this.MyApplication);
		} else if (button == Button.B_MOVE) {
			// Check if the button is enabled
			String attrs = sGetAttribute("css=td[" + Locators.zMoveIconBtn
					+ "]>div@class");
			if (attrs.contains("ZDisabledImage")) {
				throw new HarnessException("Tried clicking on " + button
						+ " but it was disabled " + attrs);
			}
			// locator = "css=td[id='zb__BDLV__MOVE_left_icon']";
			locator = Locators.zMoveIconBtn;
			page = new DialogMove(MyApplication, this);
		} else if (button == Button.B_PRINT) {

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//td[@id='"
					+ "Locators.zPrintIconBtnID" + "']/div)@class");
			if (attrs.contains("ZDisabledImage")) {
				throw new HarnessException("Tried clicking on " + button
						+ " but it was disabled " + attrs);
			}

			locator = "id='" + "Locators.zPrintIconBtnID";
			page = null; // TODO
			throw new HarnessException("implement Print dialog");

		} else if (button == Button.B_TAG) {

			// For "TAG" without a specified pulldown option, just click on the
			// pulldown
			// To use "TAG" with a pulldown option, see
			// zToolbarPressPulldown(Button, Button)
			//

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//td[@id='"
					+ "Locators.zTagMenuDropdownBtnID" + "']/div)@class");
			if (attrs.contains("ZDisabledImage")) {
				throw new HarnessException("Tried clicking on " + button
						+ " but it was disabled " + attrs);
			}

			locator = "id='" + "Locators.zTagMenuDropdownBtnID" + "'";

		} else if (button == Button.B_LISTVIEW) {

			// For "TAG" without a specified pulldown option, just click on the
			// pulldown
			// To use "TAG" with a pulldown option, see
			// zToolbarPressPulldown(Button, Button)
			//

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//td[@id='"
					+ "Locators.zViewMenuDropdownBtnID" + "']/div)@class");
			if (attrs.contains("ZDisabledImage")) {
				throw new HarnessException("Tried clicking on " + button
						+ " but it was disabled " + attrs);
			}

			locator = "id='" + "Locators.zViewMenuDropdownBtnID" + "'";

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		// Default behavior, process the locator by clicking on it
		//

		// Make sure the button exists
		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Button is not present locator="
					+ locator + " button=" + button);

		// Click it
		this.zClick(locator);

		// If the app is busy, wait for it to become active
		zWaitForBusyOverlay();

		return (page);
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option)
			throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("
				+ pulldown + ", " + option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		// Default behavior variables
		//
		String pulldownLocator = null; // If set, this will be expanded
		String optionLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		// Based on the button specified, take the appropriate action(s)
		//

		if (pulldown == Button.B_NEW) {
			if (option == Button.O_NEW_BRIEFCASE) {
				throw new HarnessException("implement me!");
			} else if (option == Button.O_NEW_DOCUMENT) {
				pulldownLocator = Locators.zNewMenuArrowBtn;

				optionLocator = "css=td[id$='_title']:contains('Document')";

				page = new DocumentBriefcaseNew(this.MyApplication);

				// FALL THROUGH
			} else if (option == Button.O_NEW_FOLDER) {
				throw new HarnessException("implement me!");
			} else if (option == Button.O_NEW_TAG) {
				pulldownLocator = Locators.zNewMenuArrowBtn;

				optionLocator = "css=td[id$='_title'][class=ZWidgetTitle]:contains('Tag')";

				page = new DialogTag(this.MyApplication, this);

				// FALL THROUGH
			} else {
				throw new HarnessException(
						"no logic defined for pulldown/option " + pulldown
								+ "/" + option);
			}
		} else if (pulldown == Button.B_TAG) {
			if (option == Button.O_TAG_NEWTAG) {

				pulldownLocator = "css=td[id$='__TAG_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";

				optionLocator = "css=td[id$='__TAG_MENU|MENU|NEWTAG_title']";

				page = new DialogTag(this.MyApplication, this);

				// FALL THROUGH
			} else if (option == Button.O_TAG_REMOVETAG) {
				// Using General shortcuts: Type "u" shortcut
				// zKeyboard.zTypeCharacters(Shortcut.S_MAIL_REMOVETAG.getKeys());

				pulldownLocator = "css=td[id$='__TAG_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";

				optionLocator = "css=td[id$='__TAG_MENU|MENU|REMOVETAG_title']";

				page = null;

				// FALL THROUGH
			} else {
				throw new HarnessException(
						"no logic defined for pulldown/option " + pulldown
								+ "/" + option);
			}
		} else if (pulldown == Button.B_SEND) {
			if (option == Button.O_SEND_AS_ATTACHMENT) {

				pulldownLocator = "css=td[id$='__SEND_FILE_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";

				optionLocator = "css=td[id$='_title']:contains('Send as attachment')";

				page = new FormMailNew(this.MyApplication);

				// FALL THROUGH
			} else if (option == Button.O_SEND_LINK) {

				pulldownLocator = "css=td[id$='__SEND_FILE_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";

				optionLocator = "css=td[id$='_title']:contains('Send link')";

				page = new DialogWarning(
						DialogWarning.DialogWarningID.SendLink,
						this.MyApplication, this);

				// FALL THROUGH
			} else {
				throw new HarnessException(
						"no logic defined for pulldown/option " + pulldown
								+ "/" + option);
			}
		} else {
			throw new HarnessException("no logic defined for pulldown "
					+ pulldown);
		}

		// Default behavior
		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option "
						+ option + " pulldownLocator " + pulldownLocator
						+ " not present!");
			}

			// If the app is busy, wait for it to become active
			zWaitForBusyOverlay();

			this.zClick(pulldownLocator);

			// If the app is busy, wait for it to become active
			zWaitForBusyOverlay();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown
							+ " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}

				this.zClick(optionLocator);

				// If the app is busy, wait for it to become active
				zWaitForBusyOverlay();
			}

			// If we click on pulldown/option and the page is specified, then
			// wait for the page to go active
			if (page != null) {
				page.zWaitForActive();
				if (option == Button.O_SEND_AS_ATTACHMENT)
					zWaitForElementPresent("css=div[id$=_attachments_div] a[class='AttLink']");
			}
		}
		// Return the specified page, or null if not set
		return (page);
	}

	public AbsPage zToolbarPressPulldown(Button pulldown, String option)
			throws HarnessException {

		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("
				+ pulldown + ", " + option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		// Default behavior variables
		//
		String pulldownLocator = null; // If set, this will be expanded
		String optionLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		// Based on the button specified, take the appropriate action(s)
		if (pulldown == Button.B_TAG) {
			if (option.length() > 0) {

				pulldownLocator = "css=td[id$='__TAG_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";

				optionLocator = "css=td[class=ZWidgetTitle]:contains(" + option
						+ ")";

				page = null;

				// FALL THROUGH
			} else {
				throw new HarnessException(
						"no logic defined for pulldown/option " + pulldown
								+ "/" + option);
			}
		} else {
			throw new HarnessException("no logic defined for pulldown "
					+ pulldown);
		}

		// Default behavior
		if (pulldownLocator != null) {
			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option "
						+ option + " pulldownLocator " + pulldownLocator
						+ " not present!");
			}

			this.zClick(pulldownLocator);

			// If the app is busy, wait for it to become active
			zWaitForBusyOverlay();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown
							+ " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}

				this.zClick(optionLocator);

				// If the app is busy, wait for it to become active
				zWaitForBusyOverlay();
			}

			// If we click on pulldown/option and the page is specified, then
			// wait for the page to go active
			if (page != null) {
				page.zWaitForActive();
			}
		}
		// Return the specified page, or null if not set
		return (page);
	}

	public AbsPage zListItem(Action action, DocumentItem document)
			throws HarnessException {

		docItem = document;

		return zListItem(action, docItem.getDocName());
	}

	@Override
	public AbsPage zListItem(Action action, String docName)
			throws HarnessException {
		logger.info(myPageName() + " zListItem(" + action + ", " + docName
				+ ")");

		tracer.trace(action + " on briefcase = " + docName);

		AbsPage page = null;
		String listLocator = Locators.briefcaseListView;
		String itemlocator;

		// listLocator = "div[id='zl__BDLV__rows'][class='DwtListView-Rows']";
		// String rowLocator = rowLocator = "div[id^='zli__BDLV__']";
		// rowLocator = "css=div:contains[id^='zli__BDLV__']";
		// rowLocator = "css=div:contains[id:contains('zli__BDLV__')]";
		if (!this.sIsElementPresent(listLocator))
			throw new HarnessException("List View Rows is not present "
					+ listLocator);
		/*
		 * // How many items are in the table? int count =this.sGetXpathCount(
		 * "//div[@id='zl__BDLV__rows']//div[contains(@id, 'zli__BDLV__')]");
		 * logger.debug(myPageName() +
		 * " zListSelectItem: number of list items: "+ count);
		 * 
		 * for (int i = 1; i <= count; i++) { itemlocator = "css=" + listLocator
		 * + ">div:nth-child(" + i + ")"; String namelocator; namelocator =
		 * itemlocator + ">table>tbody>tr>td>div[id*='__na']"; String s =
		 * this.sGetText(namelocator).trim(); s =
		 * this.sGetText("css=div[id='zl__BDLV__rows']>div:nth-child(" + i +
		 * ")").trim();
		 * 
		 * if ( s.contains(name) ) { break; // found it } itemlocator = null; }
		 * if ( itemlocator == null ) { throw new
		 * HarnessException("Unable to locate item with name("+ name +")"); }
		 */
		itemlocator = listLocator + " td[width*='auto'] div:contains("
				+ docName + ")";

		if (!GeneralUtility.waitForElementPresent(this, itemlocator))
			throw new HarnessException("Unable to locate item with name("
					+ docName + ")");
		if (action == Action.A_LEFTCLICK) {

			zWaitForElementPresent(itemlocator);

			// Left-Click on the item
			this.zClick(itemlocator);

			// page = new DocumentPreview(MyApplication);

		} else if (action == Action.A_DOUBLECLICK) {
			zWaitForElementPresent(itemlocator);

			// double-click on the item
			this.sDoubleClick(itemlocator);

			page = new DocumentBriefcaseOpen(MyApplication, docItem);
		}

		zWaitForBusyOverlay();

		if (page != null) {
			page.zWaitForActive();
		}
		return page;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption,
			String item) throws HarnessException {
		tracer.trace(action + " then " + option + "," + subOption
				+ " on item = " + item);

		throw new HarnessException("implement me!");
	}

	public AbsPage zListItem(Action action, Button option, DocumentItem document)
			throws HarnessException {

		docItem = document;

		return zListItem(action, option, docItem.getDocName());
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String subject)
			throws HarnessException {
		tracer.trace(action + " then " + option + " on briefcase = " + subject);

		logger.info(myPageName() + " zListItem(" + action + ", " + option
				+ ", " + subject + ")");

		if (action == null)
			throw new HarnessException("action cannot be null");
		if (option == null)
			throw new HarnessException("button cannot be null");
		if (subject == null || subject.trim().length() == 0)
			throw new HarnessException("docName cannot be null or blank");

		AbsPage page = null;
		String listLocator = Locators.briefcaseListView;
		// String rowLocator;
		String itemlocator = null;

		if (!this.sIsElementPresent(listLocator))
			throw new HarnessException("List View Rows is not present "
					+ listLocator);

		itemlocator = listLocator + " td[width*='auto'] div:contains("
				+ subject + ")";

		if (action == Action.A_RIGHTCLICK) {

			zWaitForElementPresent(itemlocator);

			// Right-Click on the item
			this.zRightClick(itemlocator);

			// Now the ContextMenu is opened
			// Click on the specified option

			String optionLocator = null;

			if (option == Button.B_RENAME) {

				optionLocator = "css=td#zmi__Briefcase__RENAME_FILE_title:contains(Rename)";

				page = null;

			} else if (option == Button.O_EDIT) {

				optionLocator = "css=td#zmi__Briefcase__EDIT_FILE_title:contains(Edit)";

				page = new DocumentBriefcaseEdit(MyApplication, docItem);

			} else if (option == Button.O_OPEN) {

				optionLocator = "css=td#zmi__Briefcase__OPEN_FILE_title:contains(Open)";

				page = new DocumentBriefcaseOpen(MyApplication, docItem);

			} else if (option == Button.O_DELETE) {

				optionLocator = "css=td#zmi__Briefcase__DELETE_title:contains(Delete)";

				page = new DialogDeleteConfirm(MyApplication, this);

			} else {
				throw new HarnessException("implement action:" + action
						+ " option:" + option);
			}

			// click on the option
			this.zClick(optionLocator);

			this.zWaitForBusyOverlay();

			// FALL THROUGH

		} else {
			throw new HarnessException("implement me!  action = " + action);
		}

		if (page != null) {
			page.zWaitForActive();
		}

		// Default behavior
		return (page);
	}

	@Override
	public AbsPage zKeyboardShortcut(Shortcut shortcut) throws HarnessException {

		if (shortcut == null)
			throw new HarnessException("Shortcut cannot be null");

		tracer.trace("Using the keyboard, press the " + shortcut.getKeys()
				+ " keyboard shortcut");

		AbsPage page = null;

		String keyCode = "";

		if ((shortcut == Shortcut.S_NEWITEM)
				|| (shortcut == Shortcut.S_NEWDOCUMENT)) {

			// "New Document" shortcuts result in a new document page opening
			page = new DocumentBriefcaseNew(this.MyApplication);

			keyCode = "78";
		} else if (shortcut == Shortcut.S_DELETE) {

			// "Delete Document" shortcut leads to Confirmation Dialog opening
			page = new DialogDeleteConfirm(MyApplication, this);

			keyCode = "46";
		} else if (shortcut == Shortcut.S_BACKSPACE) {

			// "Delete Document" shortcut leads to Confirmation Dialog opening
			page = new DialogDeleteConfirm(MyApplication, this);

			keyCode = "8";
		} else if (shortcut == Shortcut.S_NEWTAG) {

			// "NEW TAG" shortcut opens "Create New Tag" dialog
			page = new DialogTag(MyApplication, this);

			keyCode = "78,84";
		} else {
			throw new HarnessException("implement shortcut: " + shortcut);
		}

		// zKeyboard.zTypeCharacters(shortcut.getKeys());

		for (String kc : keyCode.split(",")) {
			/*
			 * vare=document.createEvent('KeyboardEvent');
			 * if(typeof(e.initKeyboardEvent)!='undefined'){e.initEvent()}
			 * else{e.initKeyEvent()}
			 */
			sGetEval("if(window.KeyEvent)"
					+ "{var evObj = document.createEvent('KeyEvents');"
					+ "evObj.initKeyEvent( 'keydown', true, true, window, false, false, false, false,"
					+ kc + ", 0 );} "
					+ "else {var evObj = document.createEvent('HTMLEvents');"
					+ "evObj.initEvent( 'keydown', true, true, window, 1 );"
					+ "evObj.keyCode = " + kc + ";}"
					+ "var x = selenium.browserbot.findElementOrNull('"
					+ "css=html>body" + "'); "
					+ "x.focus(); x.dispatchEvent(evObj);");
		}

		// If the app is busy, wait for it to become active
		this.zWaitForBusyOverlay();

		// If a page is specified, wait for it to become active
		if (page != null) {
			page.zWaitForActive(); // throws a HarnessException if never active
		}
		return (page);
	}

	public void rename(String text) throws HarnessException {
		// ClientSessionFactory.session().selenium().getEval("var x = selenium.browserbot.findElementOrNull(\""+Locators.zFrame+"\");if(x!=null)x=x.contentWindow.document.body;if(browserVersion.isChrome){x.textContent='"+text+"';}else if(browserVersion.isIE){x.innerText='"+text+"';}");
		logger.info("renaming to: " + text);
		zSelectWindow("Zimbra: Briefcase");
		// sSelectFrame("relative=top");
		sType(Locators.zRenameInput, text);
		sFocus(Locators.zRenameInput);
		// hit <Enter> key
		// sKeyPressNative(Integer.toString(KeyEvent.VK_ENTER));

		sGetEval("if(window.KeyEvent)"
				+ "{var evObj = document.createEvent('KeyEvents');"
				+ "evObj.initKeyEvent( 'keyup', true, true, window, false, false, false, false, 13, 0 );} "
				+ "else {var evObj = document.createEvent('HTMLEvents');"
				+ "evObj.initEvent( 'keyup', true, true, window, 1 );"
				+ "evObj.keyCode = 13;}"
				+ "var x = selenium.browserbot.findElementOrNull('"
				+ Locators.zRenameInput + "'); "
				+ "x.blur(); x.focus(); x.dispatchEvent(evObj);");
	}

	public void isOpenDocLoaded(String windowName, String text)
			throws HarnessException {
		zWaitForWindow(windowName);

		zSelectWindow(windowName);

		zWaitForElementPresent("css=td[class='ZhAppContent'] div:contains('"
				+ text + "')");
	}

	public void isOpenFileLoaded(String windowName, String text)
			throws HarnessException {
		zWaitForWindow(windowName);

		zSelectWindow(windowName);

		zWaitForElementPresent(Locators.zFileBodyField + ":contains('" + text
				+ "')");
	}

	public boolean isPresent(String itemName) throws HarnessException {
		String itemLocator = Locators.briefcaseListView
				+ " td[width*='auto'] div:contains(" + itemName + ")";

		zWaitForElementPresent(itemLocator);
		return true;
	}

	public boolean isDeleted(String itemName) throws HarnessException {
		String itemLocator = Locators.briefcaseListView
				+ " td[width*='auto'] div:contains(" + itemName + ")";

		zWaitForElementDeleted(itemLocator);
		return true;
	}

	public String getText(String itemName) throws HarnessException {
		String itemLocator = Locators.briefcaseListView
				+ " td[width*='auto'] div:contains(" + itemName + ")";

		return sGetText(itemLocator);
	}

	public boolean isEditDocLoaded(String windowName, String text)
			throws HarnessException {
		zWaitForWindow(windowName);

		zSelectWindow(windowName);

		zWaitForElementPresent("css=div[class='ZDToolBar ZWidget']");

		zWaitForElementPresent("css=iframe[id*='DWT'][class='ZDEditor']");

		zWaitForIframeText("css=iframe[id*='DWT'][class='ZDEditor']", text);

		return true;
	}

	public void deleteFileByName(String docName) throws HarnessException {
		ZimbraAccount account = MyApplication.zGetActiveAccount();
		account
				.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>"
						+ "<query>" + docName + "</query>" + "</SearchRequest>");
		String id = account.soapSelectValue("//mail:doc", "id");
		deleteFileById(id);
	}

	public void deleteFileById(String docId) throws HarnessException {
		ZimbraAccount account = MyApplication.zGetActiveAccount();
		account.soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>"
				+ "<action id='" + docId + "' op='trash'/>"
				+ "</ItemActionRequest>");
	}

	public void closeWindow() {
		tracer.trace("Close the separate window");

		ClientSessionFactory.session().selenium().close();
	}
}
