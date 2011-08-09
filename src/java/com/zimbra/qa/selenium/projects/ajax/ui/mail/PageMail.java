/**
 *
 */
package com.zimbra.qa.selenium.projects.ajax.ui.mail;

import java.util.ArrayList;
import java.util.List;

import com.zimbra.qa.selenium.framework.items.ContextMenuItem;
import com.zimbra.qa.selenium.framework.items.ConversationItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.ContextMenuItem.CONTEXT_MENU_ITEM_NAME;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.I18N;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.GeneralUtility;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.framework.util.GeneralUtility.WAIT_FOR_OPERAND;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties.AppType;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.AppAjaxClient;
import com.zimbra.qa.selenium.projects.ajax.ui.ContextMenu;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogAssistant;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogMove;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogTag;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.PageMain;



/**
 * @author Matt Rhoades
 *
 */
public class PageMail extends AbsTab {


	public static class Locators {

		public static final String zPrintIconBtnID 		= "zb__CLV__PRINT_left_icon";
		public static final String zTagMenuDropdownBtnID	= "zb__CLV__TAG_MENU_dropdown";
		public static final String zDetachIconBtnID		= "zb__TV__DETACH_left_icon";
		public static final String zViewMenuDropdownBtnID	= "zb__CLV__VIEW_MENU_dropdown";

		public static final String zCloseIconBtn_messageWindow 	= "css=td[id=zb__MSG__CLOSE_left_icon]";
		public static final String cssTVRowsLocator	= "css=div#zl__TV__rows";
		
		public static class CONTEXT_MENU {
			// TODO: Until https://bugzilla.zimbra.com/show_bug.cgi?id=56273 is fixed, ContextMenuItem will be defined using the text content
			public static String stringToReplace = "<ITEM_NAME>";
			public static final String zDesktopContextMenuItems = new StringBuffer("css=table[class$='MenuTable'] td[id$='_title']:contains(")
			.append(stringToReplace).append(")").toString();

			// Folder's context menu
			public static final ContextMenuItem NEW_FOLDER = new ContextMenuItem(
					zDesktopContextMenuItems.replace(stringToReplace, I18N.CONTEXT_MENU_ITEM_NEW_FOLDER),
					I18N.CONTEXT_MENU_ITEM_NEW_FOLDER,
					"div[class='ImgNewFolder']",
			":contains('nf')");

		}
	}





	public PageMail(AbsApplication application) {
		super(application);

		logger.info("new " + PageMail.class.getCanonicalName());

	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsPage#isActive()
	 */
	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the main page is active
		if ( !((AppAjaxClient)MyApplication).zPageMain.zIsActive() ) {
			((AppAjaxClient)MyApplication).zPageMain.zNavigateTo();
		}

		String locator;
		boolean loaded, visible;


		/**
		 * 8.0
		 * MLV:
		 * <div id="zb__TV__NEW_MENU" style="position: absolute; overflow: visible; z-index: 300; left: 5px; top: 78px; width: 159px; height: 24px;" class="ZToolbarButton ZWidget   ZHasDropDown       ZHasLeftIcon ZHasText" parentid="z_shell">
		 * CLV:
		 * <div id="zb__CLV__NEW_MENU" style="position: absolute; overflow: visible; z-index: 300; left: 5px; top: 78px; width: 159px; height: 24px;" class="ZToolbarButton ZWidget   ZHasDropDown       ZHasLeftIcon ZHasText" parentid="z_shell">
		 * 
		 */

		// If the "NEW" button is visible, then the app is visible

		// Check MLV first
		locator = "css=div#zb__TV__NEW_MENU";

		loaded = this.sIsElementPresent(locator);
		visible = this.zIsVisiblePerPosition(locator, 4, 74);
		if ( loaded && visible )
			return (true);

		// Check CLV next
		locator = "css=div#zb__CLV__NEW_MENU";
		loaded = this.sIsElementPresent(locator);
		visible = this.zIsVisiblePerPosition(locator, 4, 74);
		if ( loaded && visible )
			return (true);


		// We made it here, neither were active
		return (false);
	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsPage#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsPage#navigateTo()
	 */
	@Override
	public void zNavigateTo() throws HarnessException {

		// Check if this page is already active.
		if ( zIsActive() ) {
			return;
		}

		// Make sure we are logged into the Mobile app
		if ( !((AppAjaxClient)MyApplication).zPageMain.zIsActive() ) {
			((AppAjaxClient)MyApplication).zPageMain.zNavigateTo();
		}

		tracer.trace("Navigate to "+ this.myPageName());

		this.zClick(PageMain.Locators.zAppbarMail);

		this.zWaitForBusyOverlay();

		zWaitForActive();

	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton("+ button +")");

		tracer.trace("Press the "+ button +" button");

		if ( button == null )
			throw new HarnessException("Button cannot be null!");


		// Default behavior variables
		//
		String locator = null;			// If set, this will be clicked
		AbsPage page = null;	// If set, this page will be returned

		// Based on the button specified, take the appropriate action(s)
		//

		if ( button == Button.B_NEW ) {

			// New button
			locator = "css=div[id$='__NEW_MENU'] td[id$='__NEW_MENU_title']";

			// Create the page
			page = new FormMailNew(this.MyApplication);

			// FALL THROUGH

		} else if ( button == Button.B_GETMAIL || button == Button.B_LOADFEED || button == Button.B_REFRESH ) {

			locator = "css=td#CHECK_MAIL_left_icon";

		} else if ( button == Button.B_DELETE ) {

			String id;
			if ( zGetPropMailView() == PageMailView.BY_MESSAGE ) {
				id = "zb__TV__DELETE_left_icon";
			} else {
				id = "zb__CLV__DELETE_left_icon";
			}

			// Check if the button is enabled
			locator = "css=td[id='"+ id +"']>div[class*='ZDisabledImage']";
			if ( sIsElementPresent(locator) ) {
				throw new HarnessException("Tried clicking on "+ button +" but it was disabled: ZDisabledImage");
			}

			locator = "css=td#" + id;


		} else if ( button == Button.B_MOVE ) {

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//td[contains(@id, '__MOVE_left_icon')]/div)@class");
			if ( attrs.contains("ZDisabledImage") ) {
				throw new HarnessException("Tried clicking on "+ button +" but it was disabled "+ attrs);
			}

			locator = "css=td[id$='__MOVE_left_icon']";

			page = new DialogMove(MyApplication, this);

			// FALL THROUGH

		} else if ( button == Button.B_PRINT ) {

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//td[@id='"+ Locators.zPrintIconBtnID +"']/div)@class");
			if ( attrs.contains("ZDisabledImage") ) {
				throw new HarnessException("Tried clicking on "+ button +" but it was disabled "+ attrs);
			}

			locator = "id='"+ Locators.zPrintIconBtnID;
			page = null;	// TODO
			throw new HarnessException("implement Print dialog");

		} else if ( button == Button.B_REPLY ) {

			page = new FormMailNew(this.MyApplication);;
			locator = "css=div[id$='__REPLY']";

			if ( !this.sIsElementPresent(locator) ) {
				throw new HarnessException("Reply icon not present "+ button);
			}

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//div[contains(@id,'__REPLY')])@class");
			if ( attrs.contains("ZDisabled") ) {
				throw new HarnessException("Tried clicking on "+ button +" but it was disabled "+ attrs);
			}

		} else if ( button == Button.B_REPLYALL ) {

			page = new FormMailNew(this.MyApplication);;
			locator = "css=div[id$='__REPLY_ALL']";

			if ( !this.sIsElementPresent(locator) ) {
				throw new HarnessException("Reply All icon not present "+ button);
			}

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//div[contains(@id,'__REPLY_ALL')])@class");
			if ( attrs.contains("ZDisabled") ) {
				throw new HarnessException("Tried clicking on "+ button +" but it was disabled "+ attrs);
			}

		} else if ( button == Button.B_FORWARD ) {

			page = new FormMailNew(this.MyApplication);;
			locator = "css=div[id$='__FORWARD']";

			if ( !this.sIsElementPresent(locator) ) {
				throw new HarnessException("Forward icon not present "+ button);
			}

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//div[contains(@id,'__FORWARD')])@class");
			if ( attrs.contains("ZDisabled") ) {
				throw new HarnessException("Tried clicking on "+ button +" but it was disabled "+ attrs);
			}

		} else if ( (button == Button.B_RESPORTSPAM) || (button == Button.B_RESPORTNOTSPAM) ) {

			return (this.zToolbarPressPulldown(Button.B_ACTIONS, button));

		} else if ( button == Button.B_TAG ) {

			// For "TAG" without a specified pulldown option, just click on the pulldown
			// To use "TAG" with a pulldown option, see  zToolbarPressPulldown(Button, Button)
			//

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//td[@id='"+ Locators.zTagMenuDropdownBtnID +"']/div)@class");
			if ( attrs.contains("ZDisabledImage") ) {
				throw new HarnessException("Tried clicking on "+ button +" but it was disabled "+ attrs);
			}

			locator = "id='"+ Locators.zTagMenuDropdownBtnID +"'";

		} else if ( button == Button.B_NEWWINDOW ) {

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//td[@id='"+ Locators.zDetachIconBtnID +"']/div)@class");
			if ( attrs.contains("ZDisabledImage") ) {
				throw new HarnessException("Tried clicking on "+ button +" but it was disabled "+ attrs);
			}

			locator = "id='"+ Locators.zDetachIconBtnID;
			page = null;	// TODO
			throw new HarnessException("implement new window page ... probably just DisplayMail object?");


		} else if ( button == Button.B_LISTVIEW ) {

			// For "TAG" without a specified pulldown option, just click on the pulldown
			// To use "TAG" with a pulldown option, see  zToolbarPressPulldown(Button, Button)
			//

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//td[@id='"+ Locators.zViewMenuDropdownBtnID +"']/div)@class");
			if ( attrs.contains("ZDisabledImage") ) {
				throw new HarnessException("Tried clicking on "+ button +" but it was disabled "+ attrs);
			}

			locator = "id='"+ Locators.zViewMenuDropdownBtnID +"'";

		} else if ( button == Button.B_MAIL_LIST_SORTBY_FLAGGED ) {

			locator = "css=td[id='zlh__TV__fg'] div[class='ImgFlagRed']";
			this.zClick(locator);
			this.zWaitForBusyOverlay();
			return (null);

		} else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		if ( locator == null ) {
			throw new HarnessException("locator was null for button "+ button);
		}

		// Default behavior, process the locator by clicking on it
		//
		this.zClickAt(locator,"0,0");

		//need small wait so that next element gets appeared/visible  after click
		SleepUtil.sleepMedium();
		// If the app is busy, wait for it to become active
		this.zWaitForBusyOverlay();

		if (ZimbraSeleniumProperties.getAppType() == AppType.DESKTOP &&
				button == Button.B_GETMAIL) {


			// Wait for the spinner image
			zWaitForDesktopLoadingSpinner(5000);
		}

		// If page was specified, make sure it is active
		if ( page != null ) {

			// This function (default) throws an exception if never active
			page.zWaitForActive();

		}


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

		if (pulldown == Button.B_TAG) {
			if (option == Button.O_TAG_NEWTAG) {

				pulldownLocator = "css=td[id$='__TAG_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";

				optionLocator = "css=td[id$='__TAG_MENU|MENU|NEWTAG_title']";

				page = new DialogTag(this.MyApplication, this);

				// FALL THROUGH
			} else if (option == Button.O_TAG_REMOVETAG) {

				pulldownLocator = "css=td[id$='__TAG_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";

				optionLocator = "css=td[id$='__TAG_MENU|MENU|REMOVETAG_title']";

				page = null;

				// FALL THROUGH
			} else {
				throw new HarnessException(
						"no logic defined for pulldown/option " + pulldown
						+ "/" + option);
			}
		} else if (pulldown == Button.B_NEW) {

			pulldownLocator = "css=td[id$='__NEW_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";

			if (option == Button.O_NEW_TAG) {

				optionLocator = "css=td[id$='__NEW_MENU_NEW_TAG_left_icon']>div[class='ImgNewTag']";
				page = new DialogTag(this.MyApplication, this);

			} else if (option == Button.O_NEW_FOLDER) {

				optionLocator = "css=td[id$='__NEW_MENU_NEW_FOLDER_left_icon']>div[class='ImgNewFolder']";
				page = new DialogCreateFolder(this.MyApplication, this);

			}

		} else if ( (pulldown == Button.B_ACTIONS) && (option == Button.B_REDIRECT) ) {

			pulldownLocator = "css=td[id$='__ACTIONS_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
			optionLocator = "css=div[id$='__REDIRECT'] td[id$='__REDIRECT_title']";
			page = new DialogRedirect(this.MyApplication, this);

		} else if ( (pulldown == Button.B_ACTIONS) && ((option == Button.B_RESPORTSPAM) || (option == Button.B_RESPORTNOTSPAM)) ) {

			if ( this.zIsVisiblePerPosition("css=div#ztb__CLV", 0, 0) ) {
				pulldownLocator = "css=td[id$='zb__CLV__ACTIONS_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
				optionLocator = "css=div[id='zm__CLV'] tr[id='POPUP_SPAM'] td[id='zmi__CLV__SPAM_title']";
			} else {
				pulldownLocator = "css=td[id='zb__TV__ACTIONS_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
				optionLocator = "css=div[id='zm__TV'] tr[id='POPUP_SPAM'] td[id='zmi__TV__SPAM_title']";
			}
			page = null;

		} else if ((pulldown == Button.B_OPTIONS)&& (option == Button.O_ADD_SIGNATURE)) {

			pulldownLocator = "css=td[id$='_ADD_SIGNATURE_dropdown']>div[class='ImgSelectPullDownArrow']";
			//optionLocator = "//td[contains(@id,'_title') and contains (text(),'sigName')]";

			page = null;
			
		} else if ( pulldown == Button.B_MOVE ) {

			if ( option == Button.O_NEW_FOLDER ) {
				
				// Check if we are CLV or MV
				if ( this.zIsVisiblePerPosition("css=div#ztb__CLV", 0, 0) ) {
					pulldownLocator = "css=td#zb__CLV__MOVE_MENU_dropdown>div";
				} else {
					pulldownLocator = "css=td#zb__TV__MOVE_MENU_dropdown>div";
				}
				optionLocator = "css=div[class='DwtFolderChooser'] div[id$='_newButtonDivId'] td[id$='_title']";
				page = new DialogCreateFolder(this.MyApplication, this);

			} else {
				throw new HarnessException("no logic defined for B_MOVE and " + option);
			}

			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException(pulldownLocator + " not present!");
			}

			// 8.0 change ... need zClickAt()
			// this.zClick(pulldownLocator);
			this.zClickAt(pulldownLocator, "0,0");

			// If the app is busy, wait for it to become active
			zWaitForBusyOverlay();

			if (!this.sIsElementPresent(optionLocator)) {
				throw new HarnessException(optionLocator + " not present!");
			}

			this.zClick(optionLocator);

			// If the app is busy, wait for it to become active
			zWaitForBusyOverlay();
			
			page.zWaitForActive();

			return (page);


		} else {
			throw new HarnessException("no logic defined for pulldown/option "
					+ pulldown + "/" + option);
		}

		// Default behavior
		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option "
						+ option + " pulldownLocator " + pulldownLocator
						+ " not present!");
			}

			// 8.0 change ... need zClickAt()
			// this.zClick(pulldownLocator);
			this.zClickAt(pulldownLocator, "0,0");

			// If the app is busy, wait for it to become active
			zWaitForBusyOverlay();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown
							+ " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}

				// 8.0 change ... need zClickAt()
				// this.zClick(optionLocator);
				this.zClickAt(optionLocator, "0,0");

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




	public enum PageMailView {
		BY_MESSAGE, BY_CONVERSATION
	}

	/**
	 * Get the Page Property: ListView = By message OR By Conversation
	 * @return
	 * @throws HarnessException
	 */
	public PageMailView zGetPropMailView() throws HarnessException {
		if ( this.zIsVisiblePerPosition("css=div#zv__CLV", 0, 0) ) {
			return (PageMailView.BY_CONVERSATION);
		} else if ( this.zIsVisiblePerPosition("css=div#zv__TV", 0, 0) ) {
			return (PageMailView.BY_MESSAGE);
		}

		throw new HarnessException("Unable to determine the Page Mail View");
	}

	private MailItem parseMessageRow(String top) throws HarnessException {
		MailItem item = null;
		
		if ( top.contains("CLV") ) {
			item = new ConversationItem();
			
			if ( this.sIsElementPresent(top.trim() + "[class*='ZmConvExpanded']"))
				((ConversationItem)item).gIsConvExpanded = true;
			
		} else if ( top.contains("TV") )  {
			item = new MailItem();
		} else {
			throw new HarnessException("Unknown message row type "+ top);
		}
		
		String msglocator = top;
		String locator;
		
		// Is it checked?
		locator = msglocator + " div[class*='ImgCheckboxChecked']";
		item.gIsSelected = this.sIsElementPresent(locator);

		// Is it flagged
		// TODO: probably can't have boolean, need 'blank', 'disabled', 'red', and other states
		locator = msglocator + " div[class*='ImgFlagRed']";
		item.gIsFlagged = this.sIsElementPresent(locator);

		// Is it high priority?
		locator = msglocator + " div[id$='__pr'][class*=ImgPriorityHigh_list]";
		if ( this.sIsElementPresent(locator) )
			item.gPriority = "high";
		// TODO - handle other priorities


		locator = msglocator + " div[id$='__tg']";
		// TODO: handle tags

		// Get the From
		locator = msglocator + " [id$='__fr']";
		item.gFrom = this.sGetText(locator).trim();

		// Get the attachment
		locator = msglocator + " div[id$='__at'][class*=ImgBlank_16]";
		if ( this.sIsElementPresent(locator) ) {
			item.gHasAttachments = false;
		} else {
			// TODO - handle other attachment types
		}

		// Get the fragment and the subject
		locator = msglocator + " span[id$='__fm']";
		if ( this.sIsElementPresent(locator) ) {
			
			item.gFragment = this.sGetText(locator).trim();
			
			// Get the subject
			locator = msglocator + " td[id$='__su']";
			String subject = this.sGetText(locator).trim();

			// The subject contains the fragment, e.g. "subject - fragment", so
			// strip it off
			item.gSubject = subject.replace(item.gFragment, "").trim();

			
		} else {

			// Conversation items's fragment is in the subject field
			locator = msglocator + " td[id$='__su']";
			item.gFragment = this.sGetText(locator).trim();

			// TODO: should the subject be parsed from the conversation container?
			// For now, just set it to blank
			item.gSubject = "";

		}


		// Get the folder
		locator = msglocator + " nobr[id$='__fo']";
		if ( this.sIsElementPresent(locator) ) {
			item.gFolder = this.sGetText(locator).trim();
		} else {
			item.gFolder = "";
		}

		// Get the size
		locator = msglocator + " nobr[id$='__sz']";
		if ( this.sIsElementPresent(locator) ) {
			item.gSize = this.sGetText(locator).trim();
		} else {
			item.gSize = "";
		}

		// Get the received date
		locator = msglocator + " td[id$='__dt']";
		item.gReceived = this.sGetText(locator).trim();


		return (item);
	}
	
	/**
	 * Return a list of all messages in the current view.<p>
	 * <p>
	 * For conversations, a ConversationItem (extends MailItem) is returned for the containing row.  If the
	 * conversation is expanded, then the expanded messages are also returned in the list.<p>
	 * <p>
	 * 
	 * @return
	 * @throws HarnessException
	 */
	public List<MailItem> zListGetMessages() throws HarnessException {

		List<MailItem> items = new ArrayList<MailItem>();

		String listLocator = null;
		String rowLocator = null;
		if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
			listLocator = "css=div[id='zl__TV__rows']";
			rowLocator = "div[id^='zli__TV__']";
		} else {
			listLocator = "css=div[id='zl__CLV__rows']";
			rowLocator = "div[id^='zli__CLV__']";
		}

		// Make sure the button exists
		if ( !this.sIsElementPresent(listLocator) )
			throw new HarnessException("Message List View Rows is not present: " + listLocator);

		String tableLocator = listLocator + " " + rowLocator;
		// How many items are in the table?
		int count = this.sGetCssCount(tableLocator);
		logger.debug(myPageName() + " zListGetMessages: number of messages: "+ count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {

			// Add the new item to the list
			MailItem item = parseMessageRow(listLocator + " div:nth-of-type("+ i +") ");
			items.add(item);
			logger.info(item.prettyPrint());
		}

		// Return the list of items
		return (items);
	}



	@Override
	public AbsPage zListItem(Action action, String subject) throws HarnessException {
		logger.info(myPageName() + " zListItem("+ action +", "+ subject +")");

		tracer.trace(action +" on subject = "+ subject);

		if ( action == null )
			throw new HarnessException("action cannot be null");
		
		if ( subject == null )
			throw new HarnessException("subject cannot be null");
		
		AbsPage page = null;
		String listLocator;
		String rowLocator;
		String itemlocator = null;


		// Find the item locator
		//

		if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
			listLocator = "css=div[id='zl__TV__rows']";
			rowLocator = "div[id^='zli__TV__']";
		} else {
			listLocator = "css=div[id='zl__CLV__rows']";
			rowLocator = "div[id^='zli__CLV__']";
		}

		// TODO: how to handle both messages and conversations, maybe check the view first?
		if ( !this.sIsElementPresent(listLocator) )
			throw new HarnessException("List View Rows is not present "+ listLocator);

		// How many items are in the table?
		int count = this.sGetCssCount(listLocator + " " + rowLocator);
		logger.debug(myPageName() + " zListSelectItem: number of list items: "+ count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {

			itemlocator = listLocator + " div:nth-of-type("+ i +") ";
			String s = this.sGetText(itemlocator + " td[id$='__su']").trim();

			if ( s.contains(subject) ) {
				break; // found it
			}

			itemlocator = null;
		}

		if ( itemlocator == null ) {
			throw new HarnessException("Unable to locate item with subject("+ subject +")");
		}

		if ( action == Action.A_LEFTCLICK ) {

			// Left-Click on the item
			this.zClick(itemlocator);

			this.zWaitForBusyOverlay();

			// Return the displayed mail page object
			page = new DisplayMail(MyApplication);

			// FALL THROUGH

		} else if ( action == Action.A_DOUBLECLICK ) {

			// Double-Click on the item
			this.sDoubleClick(itemlocator);

			this.zWaitForBusyOverlay();

			page = new DisplayMail(MyApplication);
			
			// FALL THROUGH
		} else if ( action == Action.A_CTRLSELECT ) {

			throw new HarnessException("implement me!  action = "+ action);

		} else if ( action == Action.A_SHIFTSELECT ) {

			throw new HarnessException("implement me!  action = "+ action);

		} else if ( action == Action.A_RIGHTCLICK ) {

			// Right-Click on the item
			this.zRightClick(itemlocator);

			// Return the displayed mail page object
			page = new ContextMenu(MyApplication);

			// FALL THROUGH

		} else if ( action == Action.A_MAIL_CHECKBOX ) {

			String selectlocator = itemlocator + " div[id$='__se']";
			if ( !this.sIsElementPresent(selectlocator) )
				throw new HarnessException("Checkbox locator is not present "+ selectlocator);

			String image = this.sGetAttribute(selectlocator +"@class");
			if ( image.equals("ImgCheckboxChecked") )
				throw new HarnessException("Trying to check box, but it was already enabled");

			// Left-Click on the flag field
			this.zClick(selectlocator);

			this.zWaitForBusyOverlay();

			// No page to return
			page = null;

			// FALL THROUGH

		} else if ( action == Action.A_MAIL_UNCHECKBOX ) {

			String selectlocator = itemlocator + " div[id$='__se']";
			if ( !this.sIsElementPresent(selectlocator) )
				throw new HarnessException("Checkbox locator is not present "+ selectlocator);

			String image = this.sGetAttribute(selectlocator +"@class");
			if ( image.equals("ImgCheckboxUnchecked") )
				throw new HarnessException("Trying to uncheck box, but it was already disabled");

			// Left-Click on the flag field
			this.zClick(selectlocator);

			this.zWaitForBusyOverlay();

			// No page to return
			page = null;

			// FALL THROUGH

		} else if ( action == Action.A_MAIL_EXPANDCONVERSATION ) {

			String selectlocator = itemlocator + " div[id$='__ex']";
			if ( !this.sIsElementPresent(selectlocator) )
				throw new HarnessException("Checkbox locator is not present "+ selectlocator);

			String image = this.sGetAttribute(selectlocator +"@class");
			if ( image.equals("ImgNodeExpanded") )
				throw new HarnessException("Trying to expand, but conversation was alread expanded");

			// Left-Click on the flag field
			this.zClick(selectlocator);

			this.zWaitForBusyOverlay();

			// No page to return
			page = null;

		} else if ( action == Action.A_MAIL_COLLAPSECONVERSATION ) {

			String selectlocator = itemlocator + " div[$id$='__ex']";
			if ( !this.sIsElementPresent(selectlocator) )
				throw new HarnessException("Checkbox locator is not present "+ selectlocator);

			String image = this.sGetAttribute(selectlocator +"@class");
			if ( image.equals("ImgNodeCollapsed") )
				throw new HarnessException("Trying to collapse, but conversation was alread collapsed");

			// Left-Click on the flag field
			this.zClick(selectlocator);

			this.zWaitForBusyOverlay();

			// No page to return
			page = null;

		} else if ( (action == Action.A_MAIL_FLAG) || (action == Action.A_MAIL_UNFLAG) ) {
			// Both FLAG and UNFLAG have the same action and result

			String flaglocator = itemlocator + " div[id$='__fg']";

			// Left-Click on the flag field
			this.zClick(flaglocator);

			this.zWaitForBusyOverlay();

			// No page to return
			page = null;

			// FALL THROUGH

		} else {
			throw new HarnessException("implement me!  action = "+ action);
		}


		if ( page != null ) {
			page.zWaitForActive();
		}

		// default return command
		return (page);

	}

	public AbsPage zListItem(Action action, Button option, FolderItem folderItem)
	throws HarnessException {
		logger.info(myPageName() + " zListItem("+ action +", "+ option +")");
		tracer.trace(action +" then "+ option +" on Folder Item = "+ folderItem);

		if ( action == null )
			throw new HarnessException("action cannot be null");
		if ( option == null )
			throw new HarnessException("button cannot be null");
		if ( folderItem == null )
			throw new HarnessException("folderItem cannot be null");

		String treeItemLocator = null;
		boolean onRootFolder = false;

		if (folderItem.getName().equals("USER_ROOT")) {
			onRootFolder = true;
			switch (ZimbraSeleniumProperties.getAppType()) {
			case AJAX:
				treeItemLocator = TreeMail.Locators.ztih_main_Mail__FOLDER_ITEM_ID.replace(
						TreeMail.stringToReplace, "FOLDER");
				break;

			case DESKTOP:
				treeItemLocator = TreeMail.Locators.zTreeItems.replace(TreeMail.stringToReplace,
						AjaxCommonTest.defaultAccountName);
				break;
			default:
				throw new HarnessException("Implement me!");
			}
		} else {
			throw new HarnessException("Implement me!");
		}

		AbsPage page = null;
		if (treeItemLocator == null) throw new HarnessException("treeItemLocator is null, please check!");

		GeneralUtility.waitForElementPresent(this, treeItemLocator);

		if ( action == Action.A_RIGHTCLICK ) {

			if (option == Button.B_TREE_NEWFOLDER) {
				ContextMenu contextMenu = (ContextMenu)((AppAjaxClient)MyApplication).zTreeMail.zTreeItem(
						action, treeItemLocator);
				page = contextMenu.zSelect(CONTEXT_MENU_ITEM_NAME.NEW_FOLDER);
			}
			else {
				throw new HarnessException("implement action:"+ action +" option:"+ option);
			}
		} else if (action == Action.A_LEFTCLICK) {
			if (option == Button.B_TREE_NEWFOLDER) {
				if (ZimbraSeleniumProperties.getAppType() == AppType.AJAX) {
					if (((AppAjaxClient)MyApplication).zTreeMail.isCollapsed()) {
						// Expand it
						((AppAjaxClient)MyApplication).zTreeMail.zClick(
								TreeMail.Locators.treeExpandCollapseButton);
						GeneralUtility.waitFor(null, ((AppAjaxClient)MyApplication).zTreeMail, false,
								"isCollapsed", null, WAIT_FOR_OPERAND.EQ, false, 30000, 1000);
					} else {
						if (onRootFolder) {
							// TODO: Bug 57414
							// Collapse the tree and expand it again to select the root folder
							((AppAjaxClient)MyApplication).zTreeMail.zClick(
									TreeMail.Locators.treeExpandCollapseButton);

							GeneralUtility.waitFor(null, ((AppAjaxClient)MyApplication).zTreeMail, false,
									"isCollapsed", null, WAIT_FOR_OPERAND.EQ, true, 30000, 1000);

							((AppAjaxClient)MyApplication).zTreeMail.zClick(
									TreeMail.Locators.treeExpandCollapseButton);

							page = ((AppAjaxClient)MyApplication).zTreeMail.zPressButton(option);
						}  else {
							// Fall Through
						}
					}

				} else {
					// Not available for Desktop
					throw new HarnessException("Not Supported! Action:" + action + " Option:" + option);
				}

			} else {
				throw new HarnessException("implement action:"+ action +" option:"+ option);
			}
		} else {
			throw new HarnessException("implement action:"+ action +" option:"+ option);
		}

		return page;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption ,String item)
	throws HarnessException {
		tracer.trace(action +" then "+ option + "," + subOption + " on item = "+ item);

		throw new HarnessException("implement me!");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String subject) throws HarnessException {
		logger.info(myPageName() + " zListItem("+ action +", "+ option +", "+ subject +")");

		tracer.trace(action +" then "+ option +" on subject = "+ subject);


		if ( action == null )
			throw new HarnessException("action cannot be null");
		if ( option == null )
			throw new HarnessException("button cannot be null");
		if ( subject == null || subject.trim().length() == 0)
			throw new HarnessException("subject cannot be null or blank");

		AbsPage page = null;
		String listLocator;
		String rowLocator;
		String itemlocator = null;


		// Find the item locator
		//

		if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
			listLocator = "css=div[id='zl__TV__rows']";
			rowLocator = "div[id^='zli__TV__']";
		} else {
			listLocator = "css=div[id='zl__CLV__rows']";
			rowLocator = "div[id^='zli__CLV__']";
		}

		// TODO: how to handle both messages and conversations, maybe check the view first?
		if ( !this.sIsElementPresent(listLocator) )
			throw new HarnessException("List View Rows is not present "+ listLocator);

		// How many items are in the table?
		int count = this.sGetCssCount(listLocator + " " + rowLocator);
		logger.debug(myPageName() + " zListSelectItem: number of list items: "+ count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {

			itemlocator = listLocator + " div:nth-of-type("+ i +") ";
			String s = this.sGetText(itemlocator + " td[id$='__su']").trim();

			if ( s.contains(subject) ) {
				break; // found it
			}

			itemlocator = null;
		}

		if ( itemlocator == null ) {
			throw new HarnessException("Unable to locate item with subject("+ subject +")");
		}


		if ( action == Action.A_RIGHTCLICK ) {

			// Right-Click on the item
			this.zRightClickAt(itemlocator,"");

			// Now the ContextMenu is opened
			// Click on the specified option

			String optionLocator = null;

			if (option == Button.B_DELETE) {

				// <div id="zmi__TV_DELETE" ... By Message
				// <div id="zmi__CLV__Par__DELETE" ... By Conversation

				if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
					optionLocator = "css=div#zmi__TV__DELETE";
				} else {
					optionLocator = "css=div#zmi__CLV__Par__DELETE";
				}

				page = null;

			} else if (option == Button.B_TREE_NEWFOLDER) {

				String treeItemLocator = TreeMail.Locators.ztih_main_Mail__FOLDER_ITEM_ID.replace(TreeMail.stringToReplace, "FOLDER");

				GeneralUtility.waitForElementPresent(this, treeItemLocator);
				ContextMenu contextMenu = (ContextMenu)((AppAjaxClient)MyApplication).zTreeMail.zTreeItem(Action.A_RIGHTCLICK, treeItemLocator);
				page = contextMenu.zSelect(CONTEXT_MENU_ITEM_NAME.NEW_FOLDER);

			} else if ( option == Button.O_MARK_AS_READ ) {

				if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
					//optionLocator = "zmi__TV__MARK_READ_title";

					optionLocator="css=td[id^='zmi__TV__MARK_READ__']";
				} else {
					//optionLocator = "zmi__CLV__MARK_READ_title";
					optionLocator="css=td[id^='zmi__CLV__MARK_READ__']";
				}

				page = null;

				// FALLTHROUGH

			} else if ( option == Button.O_MARK_AS_UNREAD ) {

				if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
					//optionLocator = "zmi__TV__MARK_UNREAD_title";
					optionLocator="css=td[id^='zmi__TV__MARK_UNREAD__']";
				} else {
					//optionLocator = "zmi__CLV__MARK_UNREAD_title";
					optionLocator="css=td[id^='zmi__CLV__MARK_UNREAD__']";
				}

				page = null;


				// FALLTHROUGH

			} else if ( option == Button.B_REDIRECT ) {

				if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
					optionLocator="css=td[id^='zmi__TV__REDIRECT__'] div[class='ImgRedirect']";
				} else {
					optionLocator="css=td[id^='zmi__CLV__REDIRECT__'] div[class='ImgRedirect']";
				}

				page = new DialogRedirect(this.MyApplication, this);

				// FALLTHROUGH

			}
			else {
				throw new HarnessException("implement action:"+ action +" option:"+ option);
			}

			// click on the option
			this.zClickAt(optionLocator,"");

			this.zWaitForBusyOverlay();

			// FALL THROUGH

		} else {
			throw new HarnessException("implement me!  action = "+ action);
		}



		if ( page != null ) {
			page.zWaitForActive();
		}


		// Default behavior
		return (page);

	}

	@Override
	public AbsPage zKeyboardShortcut(Shortcut shortcut) throws HarnessException {
		String	keyCode;
		if (shortcut == null)
			throw new HarnessException("Shortcut cannot be null");

		tracer.trace("Using the keyboard, press the "+ shortcut.getKeys() +" keyboard shortcut");

		AbsPage page = null;

		if ( (shortcut == Shortcut.S_NEWITEM) ||
				(shortcut == Shortcut.S_NEWMESSAGE) ||
				(shortcut == Shortcut.S_NEWMESSAGE2) )
		{
			// "New Message" shortcuts result in a compose form opening
			page = new FormMailNew(this.MyApplication);
		}else if ( (shortcut == Shortcut.S_NEWTAG) ){

			// "New Message" shortcuts result in a compose form opening
			//page = new FormMailNew(this.MyApplication);
			page = new DialogTag(MyApplication,((AppAjaxClient) MyApplication).zPageMail);
		}else if ( (shortcut == Shortcut.S_NEWFOLDER) ){

			// "New Message" shortcuts result in a compose form opening
			//page = new FormMailNew(this.MyApplication);
			page = new DialogCreateFolder(MyApplication,((AppAjaxClient) MyApplication).zPageMail);
		} else if ( (shortcut == Shortcut.S_MAIL_HARDELETE) ) {

			// Hard Delete shows the Warning Dialog : Are you sure you want to permanently delete it?
			page = new DialogWarning(DialogWarning.DialogWarningID.PermanentlyDeleteTheItem,
					MyApplication, ((AppAjaxClient) MyApplication).zPageMail);
		} else if ( shortcut == Shortcut.S_ASSISTANT ) {
			
			page = new DialogAssistant(MyApplication, ((AppAjaxClient) MyApplication).zPageMail);

		}else if(shortcut== Shortcut.S_ESCAPE){
			page = new DialogWarning(
					DialogWarning.DialogWarningID.SaveCurrentMessageAsDraft,
					this.MyApplication,
					((AppAjaxClient)this.MyApplication).zPageMail);	
			
			keyCode = "27";
			zKeyDown(keyCode);
			return page;
			
		} else {
			
			throw new HarnessException("No logic for shortcut : "+ shortcut);
		}

		
		zKeyboard.zTypeCharacters(shortcut.getKeys());

		// If the app is busy, wait for it to become active
		this.zWaitForBusyOverlay();

		// If a page is specified, wait for it to become active
		if ( page != null ) {
			page.zWaitForActive();	// This method throws a HarnessException if never active
		}
		return (page);
	}

	public AbsPage zToolbarPressPulldown(Button pulldown, Button option,String dynamic) throws HarnessException {
		//logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ option +")");
		tracer.trace("Click pulldown "+ pulldown +" then "+ option);
		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");
		if (dynamic == null)
			throw new HarnessException("dynamic string cannot be null!");
		// Default behavior variables

		String pulldownLocator = null; // If set, this will be expanded
		String optionLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		if ((pulldown == Button.B_OPTIONS)&& (option == Button.O_ADD_SIGNATURE)) {
			String name = (String)dynamic;
			logger.info(name);
			//pulldownLocator = "css=td[id$='_ADD_SIGNATURE_dropdown']>div[class='ImgSelectPullDownArrow']";
			pulldownLocator="css=[id^=zb__COMPOSE][id$=__COMPOSE_OPTIONS_dropdown]";
			optionLocator="css=td[id$='_ADD_SIGNATURE_dropdown']>div[class='ImgCascade']";
			dynamic ="css=td[id*='_title']td:contains('"+ name + "')";
			page = null;

		} else {
			throw new HarnessException("no logic defined for pulldown/option "
					+ pulldown + "/" + option);
		}

		// Default behavior
		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option "
						+ option + " pulldownLocator " + pulldownLocator
						+ " not present!");
			}

			this.zClickAt(pulldownLocator,"");

			// If the app is busy, wait for it to become active
			zWaitForBusyOverlay();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException(" option " + option
							+ " optionLocator " + optionLocator
							+ " not present!");
				}

				this.zClickAt(optionLocator,"");

				// If the app is busy, wait for it to become active
				zWaitForBusyOverlay();
			}
			if (dynamic != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(dynamic)) {
					throw new HarnessException(dynamic+ " not present!");
				}

				this.zClickAt(dynamic,"");

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

	/**
	 * Activate a pulldown with dynamic values, such as "Move to folder" and "Add a tag".
	 * 
	 * @param pulldown the toolbar button to press
	 * @param dynamic the toolbar item to click such as FolderItem or TagItem
	 * @throws HarnessException 
	 */
	public AbsPage zToolbarPressPulldown(Button pulldown, Object dynamic) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ dynamic +")");
		
		tracer.trace("Click pulldown "+ pulldown +" then "+ dynamic);
		
		
		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (dynamic == null)
			throw new HarnessException("Option cannot be null!");
		
		
		// Default behavior variables
		String pulldownLocator = null; // If set, this will be expanded
		String optionLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		
		if ( pulldown == Button.B_MOVE ) {
			
			if ( !(dynamic instanceof FolderItem) ) 
				throw new HarnessException("if pulldown = " + Button.B_MOVE +", then dynamic must be FolderItem");

			FolderItem folder = (FolderItem)dynamic;
			
			// Check if we are CLV or MV
			if ( this.zIsVisiblePerPosition("css=div#ztb__CLV", 0, 0) ) {
				pulldownLocator = "css=td#zb__CLV__MOVE_MENU_dropdown>div";
				optionLocator = "css=td#zti__DwtFolderChooser_MailCLV__"+ folder.getId() + "_textCell";
			} else {
				pulldownLocator = "css=td#zb__TV__MOVE_MENU_dropdown>div";
				optionLocator = "css=td#zti__DwtFolderChooser_MailTV__"+ folder.getId() + "_textCell";
			}
			

			page = null;
			
			
		} else {

			throw new HarnessException("no logic defined for pulldown/dynamic " + pulldown + "/" + dynamic);
			
		}

		// Default behavior
		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " pulldownLocator " + pulldownLocator + " not present!");
			}

			this.zClickAt(pulldownLocator,"");

			// If the app is busy, wait for it to become active
			zWaitForBusyOverlay();
			
			SleepUtil.sleepSmall();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException(" dynamic " + dynamic + " optionLocator " + optionLocator + " not present!");
				}

				this.zClickAt(optionLocator,"");

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



}
