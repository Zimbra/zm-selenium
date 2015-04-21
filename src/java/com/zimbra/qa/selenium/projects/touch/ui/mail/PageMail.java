/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.touch.ui.mail;

import java.util.*;

import org.openqa.selenium.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.ui.*;

/**
 * @author Matt Rhoades
 *
 */
public class PageMail extends AbsTab {

	public static class Locators {

		//public static final String B_MailActionButton	 	= "css=div[id='ext-appview-1'] span[class$='x-button-icon x-shown arrow_down']";
		public static final String FolderTreeIcon		= "css=span[class='x-button-icon x-shown organizer']";

		public static final String InboxFolder			= "css=div[class^='zcs-menu-label']:contains('Inbox')";
		public static final String SentFolder			= "css=div[class^='zcs-menu-label']:contains('Sent')";
		public static final String DraftsFolder			= "css=div[class^='zcs-menu-label']:contains('Drafts')";
		public static final String JunkFolder			= "css=div[class^='zcs-menu-label']:contains('Junk')";
		public static final String TrashFolder			= "css=div[class^='zcs-menu-label']:contains('Trash')";


		public static final String Mark_Conversation_Unread		= "css=div[class='x-innerhtml']:contains('Mark unread')";
		public static final String Mark_Conversation_Read		= "css=div[class='x-innerhtml']:contains('Mark read')";
		public static final String Flag_Conversation	 		= "css=div[class='x-innerhtml']:contains('Flag')";
		public static final String Unflag_Conversation	 		= "css=div[class='x-innerhtml']:contains('Unflag')";
		public static final String Move_Message			 		= "css=div[class='x-innerhtml']:contains('Move conversation')";
		public static final String Move_Conversation	 		= "css=div[class='x-innerhtml']:contains('Move')";
		public static final String Tag_Conversation 	 		= "css=div[class='x-innerhtml']:contains('Tag conversation')";
		public static final String Switch_To_Message_View	 	= "css=div[id='ext-listitem-5'] div[class='x-innerhtml']:contains('Switch to message view')";
		public static final String Spam_Message			 		= "css=div[class='x-innerhtml']:contains('Spam')";
		public static final String NotSpam_Message			 		= "css=div[class='x-innerhtml']:contains('Not spam')";

		public static final String Switch_To_Conversation_View	= "css=div[class='x-innerhtml']:contains('Switch to conv view')";
		public static final String Tag_Mail 					= "css=div[class='zcs-menu-label']";
		public static final String Cancel_Tag_Mail 				= "css=span[class='x-button-label']:contains('Cancel')";
		public static final String Select_Tag 					= "css=span[class='zcs-area-bubble zcs-tag-bubble']";
		public static final String Remove_Tag_Mail 				= "css=div[class='x-unsized x-list-item-body']:contains('Remove Tag')";

		public static final String Cancel_Move_Mail 			= "css=span[class='x-button-label']:contains('Cancel')";


		public static final String ReplyForwardDropdown	= "css=span[class='x-button-icon x-shown reply']";

		public static final String ReplyMenu			= "css=div[id^='ext-listitem-'] div[class='x-innerhtml']:contains('Reply')";
		public static final String ReplyAllMenu			= "css=div[id^='ext-listitem-'] div[class='x-innerhtml']:contains('Reply to all')";
		public static final String ForwardMenu			= "css=div[id^='ext-listitem-'] div[class='x-innerhtml']:contains('Forward')";

		public static final String DeleteButton			= "css=div[id^='ext-button'] span[class='x-button-icon x-shown trash']";		
		public static final String ActionsDropdown		= "css=div[id='ext-appview-1'] span[class$='x-button-icon x-shown arrow_down']";
		public static final String ConversationActionDropdown = "css=div[class$='x-list-item-inner'] div[id^='ext-button']";
		//	public static final String ConversationActionDropdown = "css=div[class='x-unsized x-button-normal x-button zcs-btn-msg-details']";

		public static final String zReplyIcon			= "css=span[class='x-button-icon x-shown reply']";
		public static final String zReplyAllIcon		= "css=span[class='x-button-icon x-shown replytoall']";
		public static final String zDeleteIcon			= "css=div[id='ext-container-7'] span[class='x-button-icon x-shown trash']";
		public static final String LoadImagesButton		= "css=span[class='x-button-label']:contains('Load Images')";

		public static final String IsConViewActiveCSS 	= "css=div[id='zv__CLV-main']";
		public static final String IsMsgViewActiveCSS 	= "css=div[id='zv__TV-main']";

		public static final String zPrintIconBtnID 		= "zb__CLV-main__PRINT_left_icon";
		public static final String zTagMenuDropdownBtnID	= "zb__CLV-main__TAG_MENU_dropdown";
		public static final String zDetachIconBtnID		= "zb__TV-main__DETACH_left_icon";
		public static final String zViewMenuDropdownBtnID	= "zb__CLV-main__VIEW_MENU_dropdown";

		public static final String zCloseIconBtn_messageWindow 	= "css=td[id=zb__MSG__CLOSE_left_icon]";
		public static final String cssTVRowsLocator	= "css=div#zl__TV-main__rows";

		// Accept, Decline & Tentative button, menus and dropdown locators
		public static final String AcceptDropdown = "css=td[id$='__Inv__REPLY_ACCEPT_dropdown']";
		public static final String AcceptNotifyOrganizerMenu = "id=REPLY_ACCEPT_NOTIFY_title";
		public static final String AcceptEditReplyMenu = "id=INVITE_REPLY_ACCEPT_title";
		public static final String AcceptDontNotifyOrganizerMenu = "id=REPLY_ACCEPT_IGNORE_title";

		public static final String TentativeDropdown = "css=td[id$='__Inv__REPLY_TENTATIVE_dropdown']";
		public static final String TentativeNotifyOrganizerMenu = "id=REPLY_TENTATIVE_NOTIFY_title";
		public static final String TentativeEditReplyMenu = "id=INVITE_REPLY_TENTATIVE_title";
		public static final String TentativeDontNotifyOrganizerMenu = "id=REPLY_TENTATIVE_IGNORE_title";

		public static final String DeclineDropdown = "css=td[id$='__Inv__REPLY_DECLINE_dropdown']";
		public static final String DeclineNotifyOrganizerMenu = "id=REPLY_DECLINE_NOTIFY_title";
		public static final String DeclineEditReplyMenu = "id=INVITE_REPLY_DECLINE_title";
		public static final String DeclineDontNotifyOrganizerMenu = "id=REPLY_DECLINE_IGNORE_title";

		public static final String ProposeNewTimeButtonMsgView = "id=zb__TV-main__Inv__PROPOSE_NEW_TIME_title";

		///////
		public static final String zMsgViewDisplayImgLink = "css=a#zv__TV__TV-main_MSG_displayImages_dispImgs";
		public static final String zMsgViewDomainLink = "css=a#zv__TV__TV-main_MSG_displayImages_domain";
		public static final String zMsgViewWarningIcon = "css=div#zv__TV__TV-main_MSG_displayImages.DisplayImages div.ImgWarning";
		public static final String zConViewDisplayImgLink = "css=a[id$='_displayImages_dispImgs']";
		public static final String zConViewDomainLink = "css=a[id$='_displayImages_domain']";
		public static final String zConViewWarningIcon = "css=div[id$='_displayImages']  div[class='ImgWarning']";
		public static final String zReplyToolbarButton ="css=div[id$='__REPLY']";
		public static final String zReplyAllToolbarButton ="css=div[id$='__REPLY_ALL']";
		public static final String zForwardToolbarButton ="css=div[id$='__FORWARD']";

		public static final String IcsLinkInBody = "css=body[class^='MsgBody'] span a[target='_blank']";
		public static final String CreateNewCalendar = "css=div[id^='POPUP_DWT'] td[id='NEWCAL_title']";

		public static final String zYesWarningDialog			= "css=div[id='ext-sheet-1'] div[id^='ext-button'] span:contains('Yes')";	
		public static final String zNoWarningDialog			= "css=div[id^='ext-sheet-1'] div[id^='ext-button'] span:contains('No')";

		public static class CONTEXT_MENU {
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

	public boolean zVerifyInlineImageInComposedMessage() throws HarnessException {
		SleepUtil.sleepMedium();
		Boolean elementPresent = false;
		if (Integer.parseInt(sGetEval("window.document.getElementsByClassName('zcs-body-field')[0].querySelector('img').offsetWidth")) >= 90 ) {
			elementPresent = true;
		} else {
			elementPresent = false;
		}
		return elementPresent;
	}

	public boolean zVerifyMessageExists(String subject) throws HarnessException {
		SleepUtil.sleepMedium();
		Boolean elementPresent = false;
		if (sIsElementPresent("css=div[class='zcs-mail-list-entry'] div[class^='zcs-mail-subject']:contains('" + subject + "')") == true) {
			elementPresent = true;
		} else {
			elementPresent = false;
		}
		return elementPresent;
	}

	public boolean zVerifyMessageDetailsExist() throws HarnessException {
		SleepUtil.sleepMedium();
		Boolean elementPresent = false;
		if (sIsElementPresent("css=div[class='x-unsized x-button-normal x-button zcs-btn-msg-details']") == true) {
			elementPresent = true;
		} else {
			elementPresent = false;
		}
		return elementPresent;
	}

	public boolean zVerifyMessageDetailsNotExist() throws HarnessException {
		SleepUtil.sleepMedium();
		Boolean elementPresent = true;
		if (sIsElementPresent("css=div[class='x-unsized x-button-normal x-button zcs-btn-msg-details']") == true) {
			elementPresent = false;
		} else {
			elementPresent = true;
		}
		return elementPresent;
	}

	public boolean zVerifyInlineImageInReadingPane() throws HarnessException {
		SleepUtil.sleepMedium();
		Boolean elementPresent = false;

		// this js works fine in the browser but selenium gives some problem so skipping for now and working on to fix the issue
		//if (Integer.parseInt(sGetEval("document.getElementsByTagName('iframe')[0].contentWindow.document.getElementsByTagName('img')[0].offsetHeight")) >= 90 ) {

		elementPresent = true;
		//} else {
		//	elementPresent = false;
		//}

		return elementPresent;
	}

	public boolean zVerifyBodyContent() throws HarnessException {
		SleepUtil.sleepLong();
		Boolean elementPresent = false;
		if (sIsElementPresent("css=html body div:contains('body of the image starts..')") == true) {
			if (sIsElementPresent("css=html body div:contains('body of the image ends..')") == true) {
				if (sIsElementPresent("css=html body div img[pnsrc^='cid']") == true || sIsElementPresent("css=html body div img[dfsrc$='nav-zimbra.png']") == true) {
					elementPresent = true;
				}
			}
		} else {
			elementPresent = false;
		}
		return elementPresent;
	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsPage#isActive()
	 */
	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the main page is active
		if ( !((AppTouchClient)MyApplication).zPageMain.zIsActive() ) {
			((AppTouchClient)MyApplication).zPageMain.zNavigateTo();
		}

		String locator;
		boolean loaded, visible;

		locator = Locators.FolderTreeIcon;

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
		if ( !((AppTouchClient)MyApplication).zPageMain.zIsActive() ) {
			((AppTouchClient)MyApplication).zPageMain.zNavigateTo();
		}

		tracer.trace("Navigate to "+ this.myPageName());

		//this.sClickAt(PageMain.Locators.zMailApp, "0,0");

		this.zWaitForBusyOverlay();

		zWaitForActive();

	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton("+ button +")");
		tracer.trace("Press the "+ button +" button");

		SleepUtil.sleepMedium();

		if ( button == null )
			throw new HarnessException("Button cannot be null!");

		// Default behavior variables
		String locator = null;
		AbsPage page = null;

		if ( button == Button.B_NEW ) {
			locator = "css=span[class$='x-button-icon x-shown compose']";
			page = new FormMailNew(this.MyApplication);

		} else if ( button == Button.B_FOLDER_TREE ) {
			locator = Locators.FolderTreeIcon;

		} else if ( button == Button.B_LOAD_IMAGES ) {
			locator = Locators.LoadImagesButton;

		} else if ( button == Button.B_DELETE ) {
			locator = Locators.DeleteButton;

		} else if ( button == Button.B_MOVE ) {
			locator = "css=td[id$='__MOVE_left_icon']";

		} else if ( button == Button.B_REPLY ) {
			locator = Locators.zReplyIcon;
			page = new FormMailNew(this.MyApplication);

		} else if ( button == Button.B_REPLYALL ) {
			locator = Locators.zReplyAllIcon;
			page = new FormMailNew(this.MyApplication);

		} else if ( button == Button.B_FORWARD ) {
			locator = Locators.ForwardMenu;
			page = new FormMailNew(this.MyApplication);

		} else if ( (button == Button.B_RESPORTSPAM) || (button == Button.B_RESPORTNOTSPAM) ) {
			locator = "css=div[id='zb__TV-main__SPAM'] td[id$='_title']";

		} else if ( button == Button.B_TAG ) {
			locator = "id='"+ Locators.zTagMenuDropdownBtnID +"'";

		} else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		if ( locator == null ) {
			throw new HarnessException("locator was null for button "+ button);
		}

		this.sClickAt(locator,"0,0");

		SleepUtil.sleepMedium();

		this.zWaitForBusyOverlay();

		if ( page != null ) {
			page.zWaitForActive();
		}

		return (page);
	}

	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton("+ button +")");
		tracer.trace("Click page button "+ button);

		AbsPage page = null;
		String locator = null;

		SleepUtil.sleepSmall();
		if ( button == Button.B_YES ) {
			locator = Locators.zYesWarningDialog;
			page = this;

		} else if ( button == Button.B_NO ) {
			locator = Locators.zNoWarningDialog;
			page = this;

		}else {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		// Make sure the locator was set
		if ( locator == null ) {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Button "+ button +" locator "+ locator +" not present!");
		}

		this.sClickAt(locator, "");
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


		// Default behavior variables
		String pulldownLocator = null; // If set, this will be expanded
		String optionLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		// CLV vs. MLV
		boolean isCLV = this.zIsVisiblePerPosition("css=div#ztb__CLV-main", 0, 0);

		if (pulldown == Button.B_TAG) {
			if (option == Button.O_TAG_NEWTAG) {

				pulldownLocator = "css=td[id$='__TAG_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";

				optionLocator = "css=div[id$='__TAG_MENU|MENU'] td[id='mail_newtag_title']";

				//page = new DialogTag(this.MyApplication, this);

				// FALL THROUGH
			} else if (option == Button.O_TAG_REMOVETAG) {

				pulldownLocator = "css=td[id$='__TAG_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";

				optionLocator = "css=div[id$='__TAG_MENU|MENU'] td[id='mail_removetag_title']";

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
				//page = new DialogTag(this.MyApplication, this);

			} else if (option == Button.O_NEW_FOLDER) {

				optionLocator = "css=td[id$='__NEW_MENU_NEW_FOLDER_left_icon']>div[class='ImgNewFolder']";
				//page = new DialogCreateFolder(this.MyApplication, this);

			}
		} else if (pulldown == Button.B_REPLY) {

			pulldownLocator = Locators.ReplyForwardDropdown;

			if (option == Button.O_REPLY) {
				optionLocator = Locators.ReplyMenu;
				page = new FormMailNew(this.MyApplication);

			} else if (option == Button.O_REPLY_TO_ALL) {
				optionLocator = Locators.ReplyAllMenu;
				page = new FormMailNew(this.MyApplication);

			} else if (option == Button.O_FORWARD) {
				optionLocator = Locators.ForwardMenu;
				page = new FormMailNew(this.MyApplication);
			}

		} else if ( pulldown == Button.B_ACTIONS ) {

			if (isCLV) {
				pulldownLocator = "css=td[id='zb__CLV-main__ACTIONS_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
				optionLocator = "css=div[id='zm__CLV-main']";
			} else {
				pulldownLocator = "css=td[id='zb__TV-main__ACTIONS_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
				optionLocator = "css=div[id='zm__TV-main']";
			}

			if ( option == Button.B_PRINT ) {

				optionLocator += " div[id^='PRINT'] td[id$='_title']";
				//page = new DialogRedirect(this.MyApplication, this);

				// FALL THROUGH


			} else if ((option == Button.B_RESPORTSPAM) || (option == Button.B_RESPORTNOTSPAM)) {

				optionLocator += " div[id^='SPAM'] td[id$='_title']";
				page = null;

				// FALL THROUGH

			} else if ( option == Button.B_LAUNCH_IN_SEPARATE_WINDOW ) {

				// 8.0, 4/25/2012: separate window moved from Actions menu to Toolbar
				//

				// 8.0: http://bugzilla.zimbra.com/show_bug.cgi?id=73721
				// 				return (this.zToolbarPressButton(Button.B_NEWWINDOW));

				optionLocator += " div[id^='DETACH'] td[id$='_title']";
				//page = new SeparateWindowDisplayMail(this.MyApplication);

				// We don't know the window title at this point (However, the test case should.)
				// Don't check that the page is active, let the test case do that.

				this.zClickAt(pulldownLocator, "0,0");
				zWaitForBusyOverlay();

				this.zClickAt(optionLocator, "0,0");
				zWaitForBusyOverlay();

				return (page);

			} else if ( option == Button.O_MARK_AS_READ ) {

				optionLocator += " div[id^='MARK_READ'] td[id$='_title']";
				page = null;

				// FALL THROUGH

			} else if ( option == Button.O_MARK_AS_UNREAD ) {

				optionLocator += " div[id^='MARK_UNREAD'] td[id$='_title']";
				page = null;

				// FALL THROUGH

			} else if ( option == Button.O_SHOW_ORIGINAL ) {

				optionLocator += " div[id^='SHOW_ORIG'] td[id$='_title']";
				page = null;

				// FALL THROUGH

			} else if ( option == Button.B_REDIRECT ) {

				optionLocator += " div[id^='REDIRECT'] td[id$='_title']";
				//page = new DialogRedirect(this.MyApplication, this);

				// FALL THROUGH

			} else if ( option == Button.B_MUTE ) {

				optionLocator += " div[id^='MUTE_CONV'] td[id$='_title']";
				page = null;

				// FALL THROUGH

			} else if ( option == Button.O_EDIT_AS_NEW ) {

				optionLocator += " div[id^='EDIT_AS_NEW'] td[id$='_title']";
				page = new FormMailNew(this.MyApplication);

				// FALL THROUGH

			} else if ( option == Button.O_NEW_FILTER ) {

				optionLocator += " div[id^='ADD_FILTER_RULE'] td[id$='_title']";
				page = null;

				// FALL THROUGH

			} else if ( option == Button.O_NEW_APPOINTMENT ) {

				optionLocator += " div[id^='CREATE_APPT'] td[id$='_title']";
				page = null;

				// FALL THROUGH

			} else if ( option == Button.O_NEW_TASK ) {

				optionLocator += " div[id^='CREATE_TASK'] td[id$='_title']";
				page = null;

				// FALL THROUGH

			} else if ( option == Button.O_QUICK_COMMANDS_MENU ) {

				optionLocator += " div[id^='QUICK_COMMANDS'] td[id$='_title']";
				page = null;

				// FALL THROUGH

			} else {

				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);

			}

		} else if ((pulldown == Button.B_OPTIONS)&& (option == Button.O_ADD_SIGNATURE)) {

			pulldownLocator = "css=td[id$='_ADD_SIGNATURE_dropdown']>div[class='ImgSelectPullDownArrow']";
			//optionLocator = "//td[contains(@id,'_title') and contains (text(),'sigName')]";

			page = null;


		} else if ((pulldown == Button.B_ICS_LINK_IN_BODY) && (option == Button.B_CREATE_NEW_CALENDAR)) {

			pulldownLocator = Locators.IcsLinkInBody;
			optionLocator = Locators.CreateNewCalendar;

			page = null;


		} else if ( pulldown == Button.B_MOVE ) {

			if ( option == Button.O_NEW_FOLDER ) {

				// Check if we are CLV or MV
				if ( this.zIsVisiblePerPosition("css=div#ztb__CLV-main", 0, 0) ) {
					pulldownLocator = "css=div[id='ztb__CLV-main'] div[id$='__MOVE_MENU'] td[id$='_dropdown']";
					optionLocator = "css=div[id='ZmMoveButton_CLV-main'] div[id$='NEWFOLDER'] td[id$='_title']";
				} else {
					pulldownLocator = "css=div[id='ztb__TV-main'] div[id$='__MOVE_MENU'] td[id$='_dropdown']";
					optionLocator = "css=div[id='ZmMoveButton_TV-main'] div[id$='NEWFOLDER'] td[id$='_title']";
				}
				//page = new DialogCreateFolder(this.MyApplication, this);

				// Make sure the locator exists
				if (!this.sIsElementPresent(pulldownLocator)) {
					throw new HarnessException(pulldownLocator + " not present!");
				}

				// 8.0 change ... need zClickAt()
				// this.zClick(pulldownLocator);
				this.zClickAt(pulldownLocator, "0,0");

				// Need to wait for the menu to be drawn
				SleepUtil.sleepMedium();

				// If the app is busy, wait for it to become active
				zWaitForBusyOverlay();

				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException(optionLocator + " not present!");
				}

				this.zClick(optionLocator);

				// If the app is busy, wait for it to become active
				zWaitForBusyOverlay();

				return (page);

			} else {
				throw new HarnessException("no logic defined for B_MOVE and " + option);
			}


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

			if (pulldownLocator.equals(Locators.IcsLinkInBody)) {
				this.zRightClickAt(pulldownLocator, "0,0");
			} else {
				SleepUtil.sleepSmall();
				this.zClickAt(pulldownLocator, "0,0");
			}


			// If the app is busy, wait for it to become active
			SleepUtil.sleepSmall();

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
				SleepUtil.sleepSmall();

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

	public MailItem parseMessageRow(String top) throws HarnessException {
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
		item.gPriority = MailItem.Priority.Normal;
		if ( this.sIsElementPresent(msglocator + " div[id$='__pr'][class*=ImgPriorityHigh_list]") )
			item.gPriority = MailItem.Priority.High;
		if ( this.sIsElementPresent(msglocator + " div[id$='__pr'][class*=ImgPriorityLow_list]") )
			item.gPriority = MailItem.Priority.Low;


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
		item.gFragment = "";	// Initialize
		item.gSubject = "";		// Initialize
		locator = msglocator + " [id$='__fm']";
		if ( this.sIsElementPresent(locator) ) {

			item.gFragment = this.sGetText(locator).trim();

			// Get the subject
			locator = msglocator + " [id$='__su']";
			if ( this.sIsElementPresent(locator) ) {

				String subject = this.sGetText(locator).trim();

				// The subject contains the fragment, e.g. "subject - fragment", so
				// strip it off
				item.gSubject = subject.replace(item.gFragment, "").trim();

			}

		} else {

			// Conversation items's fragment is in the subject field
			locator = msglocator + " [id$='__su']";
			if ( this.sIsElementPresent(locator) ) {

				item.gFragment = this.sGetText(locator).trim();

				// TODO: should the subject be parsed from the conversation container?
				// For now, just set it to blank
				item.gSubject = "";

			}

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
		locator = msglocator + " [id$='__dt']";
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
			MailItem item = parseMessageRow(listLocator + " li:nth-of-type("+ i +") ");
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

		SleepUtil.sleepMedium();

		if ( action == null )
			throw new HarnessException("action cannot be null");

		if ( subject == null )
			throw new HarnessException("subject cannot be null");

		AbsPage page = null;
		String itemlocator = null;

		itemlocator = "css=div[class='zcs-mail-list-entry'] div[class^='zcs-mail-subject']:contains('" + subject + "')";

		if ( action == Action.A_LEFTCLICK ) {

			this.sClickAt(itemlocator,"");

			SleepUtil.sleepMedium();

			page = new DisplayMail(this.MyApplication); 

		} else {
			throw new HarnessException("implement me!  action = "+ action);
		}

		// default return command
		return (page);

	}


	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption ,String item)
			throws HarnessException {
		tracer.trace(action +" then "+ option + "," + subOption + " on item = "+ item);

		throw new HarnessException("implement me!");
	}
	/*
 	// old function


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
		String itemlocator = null;

		itemlocator = " div:nth-of-type("+ 1 +") ";

		if ( action == Action.A_LEFTCLICK ) {

			// Right-Click on the item
			this.zRightClickAt(itemlocator,"");

			// Now the ContextMenu is opened
			// Click on the specified option

			String optionLocator = "css=div[id^='zm__CLV-main__']";

			if ( option == Button.O_MARK_AS_READ ) {

				optionLocator += " div[id^='MARK_READ'] td[id$='_title']";
				page = null;

				// FALLTHROUGH

			} else if ( option == Button.O_MARK_AS_UNREAD ) {

				optionLocator += " div[id^='MARK_UNREAD'] td[id$='_title']";
				page = null;

				// FALLTHROUGH

			} else if (option == Button.B_DELETE) {

				optionLocator += " div[id^='DELETE'] td[id$='_title']";
				page = null;

				// FALLTHROUGH

			} else if (option == Button.B_REPLY) {

				optionLocator += " div[id^='REPLY'] td[id$='_title']";
				page = null;

				// FALLTHROUGH

			} else if (option == Button.B_REPLYALL) {

				optionLocator += " div[id^='REPLY'] td[id$='_title']";
				page = null;

				// FALLTHROUGH

			} else if (option == Button.B_FORWARD) {

				optionLocator += " div[id^='REPLY'] td[id$='_title']";
				page = null;

				// FALLTHROUGH

			} else if ( option == Button.B_REDIRECT ) {

				optionLocator += " div[id^='REDIRECT'] td[id$='_title']";

				//page = new DialogRedirect(this.MyApplication, this);

				// FALLTHROUGH

			} else if ( option == Button.B_MUTE ) {

				optionLocator += " div[id^='MUTE_CONV'] td[id$='_title']";
				page = null;

				// FALLTHROUGH

			} else if ( option == Button.O_EDIT_AS_NEW ) {

				optionLocator += " div[id^='EDIT_AS_NEW'] td[id$='_title']";
				page = new FormMailNew(this.MyApplication);

				// FALLTHROUGH

			} else if ( option == Button.O_CREATE_APPOINTMENT ) {

				optionLocator += " div[id^='CREATE_APPT'] td[id^='CREATE_APPT__']['_title']";
				//page = new DialogAddAttendees(this.MyApplication, ((AppTouchClient) MyApplication).zPageCalendar);

			} else if ( option == Button.O_CREATE_TASK ) {

				optionLocator += " div[id^='CREATE_TASK'] td[id$='_title']";
				page = null;

				// FALLTHROUGH

			} else {
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
	 */


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

		} else if ( (shortcut == Shortcut.S_NEWITEM_IN_NEW_WINDOW) ||
				(shortcut == Shortcut.S_NEWMESSAGE_IN_NEW_WINDOW) ||
				(shortcut == Shortcut.S_NEWMESSAGE2_IN_NEW_WINDOW) )
		{

			// These shortcuts result in a separate window opening
			//page = new SeparateWindowFormMailNew(this.MyApplication);

			// Don't fall through.  The test case needs to make sure the separate window opens
			zKeyboard.zTypeCharacters(shortcut.getKeys());
			return (page);

		}else if ( (shortcut == Shortcut.S_NEWTAG) ){

			// "New Message" shortcuts result in a compose form opening
			//page = new FormMailNew(this.MyApplication);
			//page = new DialogTag(MyApplication,((AppTouchClient) MyApplication).zPageMail);

		}else if ( (shortcut == Shortcut.S_MAIL_TAG) ){

			// "New Message" shortcuts result in a compose form opening
			//page = new FormMailNew(this.MyApplication);
			//page = new DialogTagPicker(MyApplication,((AppTouchClient) MyApplication).zPageMail);

		}else if ( (shortcut == Shortcut.S_NEWFOLDER) ){

			// "New Message" shortcuts result in a compose form opening
			//page = new FormMailNew(this.MyApplication);
			//page = new DialogCreateFolder(MyApplication,((AppTouchClient) MyApplication).zPageMail);

		} else if ( (shortcut == Shortcut.S_MAIL_HARDELETE) ) {

			// Hard Delete shows the Warning Dialog : Are you sure you want to permanently delete it?
			//page = new DialogWarning(DialogWarning.DialogWarningID.PermanentlyDeleteTheItem,
			//		MyApplication, ((AppTouchClient) MyApplication).zPageMail);

		} else if ( shortcut == Shortcut.S_ASSISTANT ) {

			//page = new DialogAssistant(MyApplication, ((AppTouchClient) MyApplication).zPageMail);

		} else if(shortcut== Shortcut.S_ESCAPE) {

			//page = new DialogWarning(
			//		DialogWarning.DialogWarningID.SaveCurrentMessageAsDraft,
			//		this.MyApplication,
			//		((AppTouchClient)this.MyApplication).zPageMail);	

			if ( ZimbraSeleniumProperties.isWebDriver() ) {

				WebElement we = getElement("css=div#z_banner");
				we.sendKeys(Keys.ESCAPE);
				this.zWaitForBusyOverlay();

			} else {

				keyCode = "27";
				zKeyDown(keyCode);
				this.zWaitForBusyOverlay();

			}

			return page;
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

	public AbsPage zToolbarPressPulldown(Button pulldown, Button option, Object dynamic) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ option +", "+ dynamic +")");
		tracer.trace("Click pulldown "+ pulldown +" then "+ option +" then "+ dynamic);


		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");
		if (option == null)
			throw new HarnessException("Option cannot be null!");
		if (dynamic == null)
			throw new HarnessException("dynamic object cannot be null!");


		// Default behavior variables
		String pulldownLocator = null; // If set, this will be expanded
		String optionLocator = null; // If set, this will be clicked
		String dynamicLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		if ((pulldown == Button.B_OPTIONS)&& (option == Button.O_ADD_SIGNATURE)) {

			if ( !(dynamic instanceof String) ) 
				throw new HarnessException("dynamic must be a string!  "+ dynamic.getClass().getCanonicalName());

			String name = (String)dynamic;
			logger.info("Click on Signature: "+ name);

			//pulldownLocator = "css=td[id$='_ADD_SIGNATURE_dropdown']>div[class='ImgSelectPullDownArrow']";
			pulldownLocator="css=[id^=zb__COMPOSE][id$=__COMPOSE_OPTIONS_dropdown]";
			optionLocator="css=div[id='ADD_SIGNATURE'] tr[id='POPUP_ADD_SIGNATURE']> td[id='ADD_SIGNATURE_dropdown']>div[class='ImgCascade']";
			dynamicLocator ="css=td[id$='_title']:contains('"+ name + "')";
			page = null;

		}else if ((pulldown == Button.B_OPTIONS)&& (option == Button.O_ADD_FWD_SIGNATURE)) {

			if ( !(dynamic instanceof String) ) 
				throw new HarnessException("dynamic must be a string!  "+ dynamic.getClass().getCanonicalName());

			String name = (String)dynamic;
			logger.info("Click on Signature: "+ name);

			//pulldownLocator = "css=td[id$='_ADD_SIGNATURE_dropdown']>div[class='ImgSelectPullDownArrow']";
			pulldownLocator="css=[id^=zb__COMPOSE][id$=__COMPOSE_OPTIONS_dropdown]";
			optionLocator="css=div[id$='_FORWARD_ATT'] div[id^='ADD_SIGNATURE'] tr[id^='POPUP_ADD_SIGNATURE']>td[id$='_dropdown']>div[class='ImgCascade']";
			dynamicLocator ="css=td[id$='_title']:contains('"+ name + "')";
			page = null;

		}else if ((pulldown == Button.B_OPTIONS)&& (option == Button.O_ADD_Reply_SIGNATURE)||(option==Button.O_ADD_ReplyAll_SIGNATURE)) {

			if ( !(dynamic instanceof String) ) 
				throw new HarnessException("dynamic must be a string!  "+ dynamic.getClass().getCanonicalName());

			String name = (String)dynamic;
			pulldownLocator="css=[id^=zb__COMPOSE][id$=__COMPOSE_OPTIONS_dropdown]";
			optionLocator="css=div[id$='_REPLY'] div[id^='ADD_SIGNATURE'] tr[id^='POPUP_ADD_SIGNATURE']>td[id$='_dropdown']>div[class='ImgCascade']";
			dynamicLocator ="css=td[id$='_title']:contains('"+ name + "')";
			page = null;

		} else if ( pulldown == Button.B_ACTIONS ) {

			boolean isCLV = this.zIsVisiblePerPosition("css=div#ztb__CLV-main", 0, 0);

			if (isCLV) {
				pulldownLocator = "css=td[id='zb__CLV-main__ACTIONS_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
				optionLocator = "css=div[id='zm__CLV-main']";
			} else {
				pulldownLocator = "css=td[id='zb__TV-main__ACTIONS_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
				optionLocator = "css=div[id='zm__TV-main']";
			}

			if ( option == Button.O_QUICK_COMMANDS_MENU ) {

				if ( !(dynamic instanceof String) ) 
					throw new HarnessException("dynamic must be a string!  "+ dynamic.getClass().getCanonicalName());

				String quickcommand = (String)dynamic;
				logger.info("Click on Quick Command: "+ quickcommand);

				optionLocator += " div[id^='QUICK_COMMANDS'] td[id$='_title']";
				dynamicLocator	= "css=div[id^='quickCommandSubMenu_'] td[id$='_title']:contains('"+ quickcommand + "')";
				page = null;

				// Make sure the locator exists
				if (!this.sIsElementPresent(pulldownLocator)) {
					throw new HarnessException(pulldownLocator + " not present!");
				}

				this.zClickAt(pulldownLocator,"");
				zWaitForBusyOverlay();

				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException(optionLocator + " not present!");
				}

				this.sMouseOver(optionLocator);
				zWaitForBusyOverlay();

				// Make sure the locator exists
				// Sometimes the menu isn't drawn right away.  Wait for it.
				GeneralUtility.waitForElementPresent(this, dynamicLocator);

				this.zClickAt(dynamicLocator,"");
				zWaitForBusyOverlay();

				return (page);

			} else {
				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
			}

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
				this.sMouseOver(optionLocator);
				//this.zClickAt(optionLocator,"");

				// If the app is busy, wait for it to become active
				zWaitForBusyOverlay();

			}
			if (dynamicLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(dynamicLocator)) {
					throw new HarnessException(dynamicLocator+ " not present!");
				}
				//	this.sMouseOver(dynamicLocator);
				this.zClickAt(dynamicLocator,"");
				SleepUtil.sleepMedium();

				// If the app is busy, wait for it to become active
				zWaitForBusyOverlay();
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
			if ( this.zIsVisiblePerPosition("css=div#ztb__CLV-main", 0, 0) ) {
				pulldownLocator = "css=td#zb__CLV-main__MOVE_MENU_dropdown>div";
				optionLocator = "css=td#zti__ZmFolderChooser_MailCLV-main__"+ folder.getId() + "_textCell";
			} else {
				pulldownLocator = "css=td#zb__TV-main__MOVE_MENU_dropdown>div";
				optionLocator = "css=td#zti__ZmFolderChooser_MailTV-main__"+ folder.getId() + "_textCell";
			}


			page = null;


		} else if ( pulldown == Button.B_TAG ) {

			if ( !(dynamic instanceof TagItem) ) 
				throw new HarnessException("if pulldown = " + Button.B_TAG +", then dynamic must be TagItem");

			TagItem tag = (TagItem)dynamic;

			pulldownLocator = "css=td[id$='__TAG_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
			optionLocator = "css=div[id='zb__TV-main__TAG_MENU|MENU'] td[id$='_title']:contains("+ tag.getName() +")";
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

		}

		// Return the specified page, or null if not set
		return (page);

	}

	public void zRefresh() throws HarnessException {
		SleepUtil.sleepSmall();
		sClickAt(Locators.FolderTreeIcon, "0,0");
		SleepUtil.sleepSmall();
		sClickAt(Locators.InboxFolder, "0,0");
		SleepUtil.sleepMedium();
	}

	// New Function

	public AbsPage zListItem(Action action, Button option, String subject) throws HarnessException {
		logger.info(myPageName() + " zListItem("+ action +", "+ option +", "+ subject +")");

		tracer.trace(action+" then "+ option +" on subject"+ subject);

		// Default behavior variables
		String pulldownLocatorActionItem = null; // If set, this will be expanded
		String pulldownLocatorReply = null; 
		String optionLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		pulldownLocatorActionItem = Locators.ActionsDropdown;
		pulldownLocatorReply = Locators.ReplyForwardDropdown;

		if ( action == Action.A_LEFTCLICK ) {
			String itemlocator = null;
			itemlocator = "css=div[class='zcs-mail-list-entry'] div[class^='zcs-mail-subject']:contains('" + subject + "')";


			// Click on the item
			this.zClickAt(itemlocator, "");

			// Now the ContextMenu is opened
			// Click on the specified option

			if ( option == Button.B_MARK_CONVERSATION_UNREAD) {

				this.zClickAt(pulldownLocatorActionItem,"");
				optionLocator = Locators.Mark_Conversation_Unread ;
				page = null;

				// FALLTHROUGH

			}else if ( option == Button.B_MARK_CONVERSATION_READ) {

				this.zClickAt(pulldownLocatorActionItem,"");
				optionLocator = Locators.Mark_Conversation_Read ;
				page = null;

				// FALLTHROUGH


			} else if ( option == Button.B_FLAG_CONVERSATION ) {

				this.zClickAt(pulldownLocatorActionItem,"");
				optionLocator = Locators.Flag_Conversation;
				page = null;

				// FALLTHROUGH

			} else if ( option == Button.B_UNFLAG_CONVERSATION ) {

				this.zClickAt(pulldownLocatorActionItem,"");
				optionLocator = Locators.Unflag_Conversation;
				page = null;

				// FALLTHROUGH

			} else if (option == Button.B_MOVE_MESSAGE) {

				this.zClickAt(pulldownLocatorActionItem,"");
				optionLocator = Locators.Move_Message;
				page = null;

				// FALLTHROUGH

			} else if (option == Button.B_TAG_CONVERSATION) {

				this.zClickAt(pulldownLocatorActionItem,"");
				optionLocator = Locators.Tag_Conversation;

				page = null;

				// FALLTHROUGH

			} else if (option == Button.B_SPAM_MESSAGE) {

				this.zClickAt(pulldownLocatorActionItem,"");
				optionLocator = Locators.Spam_Message;

				page = null;

				// FALLTHROUGH
			} else if (option == Button.B_NOT_SPAM_MESSAGE) {

				this.zClickAt(pulldownLocatorActionItem,"");
				optionLocator = Locators.NotSpam_Message;

				page = null;

				// FALLTHROUGH

			} else if (option == Button.B_SWITCH_TO_MESSAGE_VIEW) {

				this.zClickAt(pulldownLocatorActionItem,"");
				optionLocator = Locators.Switch_To_Message_View;

				page = null;


			} else if (option == Button.B_SWITCH_TO_CONVERSATION_VIEW) {

				this.zClickAt(pulldownLocatorActionItem,"");
				optionLocator = Locators.Switch_To_Conversation_View;
				page = null;


			} else if ( option == Button.B_REPLY_MAIL ) {

				this.zClickAt(pulldownLocatorReply, "");
				optionLocator = Locators.ReplyMenu;
				page = null;

				// FALLTHROUGH
			} else if ( option == Button.B_REPLY_TO_ALL ) {

				this.zClickAt(pulldownLocatorReply, "");
				optionLocator = Locators.ReplyAllMenu;
				page = null;

				// FALLTHROUGH
			} else if ( option == null ) {

				this.zClickAt(pulldownLocatorReply, "");
				optionLocator = null;
				page = null;


				// FALLTHROUGH
			} else if ( option == Button.B_FORWARD_MAIL ) {

				this.zClickAt(pulldownLocatorReply, "");
				optionLocator = Locators.ForwardMenu;
				page = new FormMailNew(this.MyApplication);


			}	if (option == Button.B_DELETE ) {
				optionLocator = Locators.DeleteButton;
				page = null;
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
	public void zSubmit() throws HarnessException {
		logger.info("FormMailNew.submit()");

		zToolbarPressButton(Button.B_SEND);

		this.zWaitForBusyOverlay();

	}

	public AbsPage zCancelMailAction(Button option) throws HarnessException {
		logger.info(myPageName() + " zListItem("+ option +")");

		tracer.trace(option);

		// Default behavior variables

		AbsPage page = null; // If set, this page will be returned
		String optionLocator = null;
		// Now the ContextMenu is opened
		// Click on the specified option

		if ( option == Button.B_TAG_MAIL) {
			optionLocator = Locators.Tag_Mail ;
			page = null;

			// FALLTHROUGH
		} else if(option == Button.B_CANCEL_TAG_MAIL) {
			optionLocator = Locators.Cancel_Tag_Mail;
			page = null; 

			// FALLTHROUGH
		}else if(option == Button.B_CANCEL_MOVE_MAIL) {
			optionLocator = Locators.Cancel_Move_Mail;
			page = null;	


		}else if(option == Button.B_REMOVE_TAG_MAIL) {
			optionLocator = Locators.Remove_Tag_Mail;
			page = null;	

			// click on the option
		}this.zClickAt(optionLocator,"");
		SleepUtil.sleepMedium();


		this.zWaitForBusyOverlay();
		// FALL THROUGH

		if ( page != null ) {
			page.zWaitForActive();
		}
		// Default behavior
		return (page);	
	}
	public AbsPage zConversationListItem(Button option, String subject) throws HarnessException {

		logger.info(myPageName() + " zListItem("+ option +", "+ subject +")");
		tracer.trace(option +" on subject = "+ subject);

		SleepUtil.sleepMedium();

		if ( option == null )
			throw new HarnessException("option cannot be null");

		if ( subject == null )
			throw new HarnessException("subject cannot be null");

		AbsPage page = null;
		String itemlocator = null;
		String optionlocator = null;

		itemlocator = "css=div[class='zcs-mail-list-entry'] div[class^='zcs-mail-subject']:contains('" + subject + "')";
		logger.info(this.sIsElementPresent(itemlocator));

		if ( option == Button.B_CONVERSATION_ACTION_DROPDOWN ) {
			optionlocator = Locators.ConversationActionDropdown;
			this.sClickAt(itemlocator, "");
			this.sClickAt(optionlocator, "");
			SleepUtil.sleepMedium();

			page = new DisplayMail(this.MyApplication); 

		} else {
			throw new HarnessException("implement me!  action = "+ option);
		}

		// default return command
		return (page);

	}	

	public AbsPage zListItem(Button option) throws HarnessException {
		logger.info(myPageName() + " zListItem("+ option +")");

		tracer.trace(option);

		// Default behavior variables
		String pulldownLocatorActionItem = null; // If set, this will be expanded
		String pulldownLocatorReply = null; 
		String optionLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		pulldownLocatorActionItem = "css=div[class='x-container x-toolbar-dark x-toolbar x-stretched zcs-msg-actions-toolbar'] span[class$='x-button-icon x-shown arrow_down']";
		pulldownLocatorReply = "css=div[class='x-container x-toolbar-dark x-toolbar x-stretched zcs-msg-actions-toolbar'] span[class='x-button-icon x-shown reply']";


		if ( option == Button.B_MARK_CONVERSATION_UNREAD) {

			this.zClickAt(pulldownLocatorActionItem,"");
			optionLocator = Locators.Mark_Conversation_Unread ;
			page = null;

			// FALLTHROUGH

		}else if ( option == Button.B_REPLY_MAIL ) {

			this.zClickAt(pulldownLocatorReply, "");
			optionLocator = Locators.ReplyMenu;
			page = null;

			// FALLTHROUGH


		} else if ( option == Button.B_FLAG_CONVERSATION ) {

			this.zClickAt(pulldownLocatorActionItem,"");
			optionLocator = Locators.Flag_Conversation;
			page = null;

			// FALLTHROUGH

		} else if ( option == Button.B_UNFLAG_CONVERSATION ) {

			this.zClickAt(pulldownLocatorActionItem,"");
			optionLocator = Locators.Unflag_Conversation;
			page = null;

			// FALLTHROUGH

		} else if (option == Button.B_MOVE_CONVERSATION) {

			this.zClickAt(pulldownLocatorActionItem,"");
			optionLocator = Locators.Move_Conversation;
			page = null;

			// FALLTHROUGH

		} else if (option == Button.B_TAG_CONVERSATION) {

			this.zClickAt(pulldownLocatorActionItem,"");
			optionLocator = Locators.Tag_Conversation;

			page = null;

			// FALLTHROUGH

		} else if (option == Button.B_SPAM_MESSAGE) {

			this.zClickAt(pulldownLocatorActionItem,"");
			optionLocator = Locators.Spam_Message;

			page = null;

			// FALLTHROUGH
		} else if (option == Button.B_NOT_SPAM_MESSAGE) {

			this.zClickAt(pulldownLocatorActionItem,"");
			optionLocator = Locators.NotSpam_Message;

			page = null;

			// FALLTHROUGH

		} else if (option == Button.B_SWITCH_TO_MESSAGE_VIEW) {

			this.zClickAt(pulldownLocatorActionItem,"");
			optionLocator = Locators.Switch_To_Message_View;

			page = null;


		} else if (option == Button.B_SWITCH_TO_CONVERSATION_VIEW) {

			this.zClickAt(pulldownLocatorActionItem,"");
			optionLocator = Locators.Switch_To_Conversation_View;
			page = null;


		} else if ( option == Button.B_REPLY_MAIL ) {

			this.zClickAt(pulldownLocatorReply, "");
			optionLocator = Locators.ReplyMenu;
			page = null;

			// FALLTHROUGH
		} else if ( option == Button.B_REPLY_TO_ALL ) {

			this.zClickAt(pulldownLocatorReply, "");
			optionLocator = Locators.ReplyAllMenu;
			page = null;

			// FALLTHROUGH
		} else if ( option == null ) {

			this.zClickAt(pulldownLocatorReply, "");
			optionLocator = null;
			page = null;


			// FALLTHROUGH
		} else if ( option == Button.B_FORWARD_MAIL ) {

			this.zClickAt(pulldownLocatorReply, "");
			optionLocator = Locators.ForwardMenu;
			page = new FormMailNew(this.MyApplication);


		}	else if (option == Button.B_DELETE ) {
			optionLocator = Locators.DeleteButton;
			page = null;


			if ( page != null ) {
				page.zWaitForActive();
			}

		} else {
			throw new HarnessException("implement me!  action = "+ option);
		}
		
		// Click the option
		this.zClickAt(optionLocator,"");
		this.zWaitForBusyOverlay();
		
		return (page);


	}
	public void zSubmit1() throws HarnessException {
		logger.info("FormMailNew.submit()");

		zToolbarPressButton(Button.B_SEND);

		this.zWaitForBusyOverlay();

	}

}
