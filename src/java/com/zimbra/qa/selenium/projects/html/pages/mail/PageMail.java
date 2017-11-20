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
package com.zimbra.qa.selenium.projects.html.pages.mail;

import java.util.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.html.pages.*;


/**
 * @author Matt Rhoades
 *
 */
public class PageMail extends AbsTab {


	public static class Locators {
		public static final String LocatorGetMail = "css=img[alt='Refresh']";
	}





	public PageMail(AbsApplication application) {
		super(application);

		logger.info("new " + PageMail.class.getCanonicalName());

	}

	/* (non-Javadoc)
	 * @see projects.admin.pages.AbsPage#isActive()
	 */
	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the main page is active
		if ( !((HtmlPages)MyApplication).zPageMain.zIsActive() ) {
			((HtmlPages)MyApplication).zPageMain.zNavigateTo();
		}

		// If the "folders" tree is visible, then mail is active
		String locator = Locators.LocatorGetMail;

		boolean loaded = this.sIsElementPresent(locator);
		if ( !loaded )
			return (false);

		return (true);

	}

	/* (non-Javadoc)
	 * @see projects.admin.pages.AbsPage#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	/* (non-Javadoc)
	 * @see projects.admin.pages.AbsPage#navigateTo()
	 */
	@Override
	public void zNavigateTo() throws HarnessException {

		if ( zIsActive() ) {
			return;
		}

		// Make sure we are logged into the Mobile app
		if ( !((HtmlPages)MyApplication).zPageMain.zIsActive() ) {
			((HtmlPages)MyApplication).zPageMain.zNavigateTo();
		}

		tracer.trace("Navigate to "+ this.myPageName());

		this.sClick(PageMain.Locators.zAppbarMail);

		this.zWaitForBusyOverlayHTML();

		zWaitForActive();

	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton("+ button +")");

		tracer.trace("Press the "+ button +" button");

		if ( button == null )
			throw new HarnessException("Button cannot be null!");


		
		//
		String locator = null;		
		AbsPage page = null;

		// Based on the button specified, take the appropriate action(s)
		//

		if ( button == Button.B_NEW ) {

			locator = "implement me";
			page = null; // new FormMailNew(this.MyApplication);

			

		} else if ( button == Button.B_REFRESH ) {

			locator = Locators.LocatorGetMail;
			page = null;

		} else if ( button == Button.B_DELETE ) {

			locator = "implement me";
			page = null; // new FormMailNew(this.MyApplication);

			


		} else if ( button == Button.B_MOVE ) {

			locator = "implement me";
			page = null; // new FormMailNew(this.MyApplication);

			

		} else if ( button == Button.B_PRINT ) {

			locator = "implement me";
			page = null; // new FormMailNew(this.MyApplication);

			

		} else if ( button == Button.B_REPLY ) {

			locator = "implement me";
			page = null; // new FormMailNew(this.MyApplication);

			

		} else if ( button == Button.B_REPLYALL ) {

			locator = "implement me";
			page = null; // new FormMailNew(this.MyApplication);

			

		} else if ( button == Button.B_FORWARD ) {

			locator = "implement me";
			page = null; // new FormMailNew(this.MyApplication);

			

		} else if ( (button == Button.B_RESPORTSPAM) || (button == Button.B_RESPORTNOTSPAM) ) {

			locator = "implement me";
			page = null; // new FormMailNew(this.MyApplication);

			

		} else if ( button == Button.B_TAG ) {

			locator = "implement me";
			page = null; // new FormMailNew(this.MyApplication);

			

		} else if ( button == Button.B_NEWWINDOW ) {

			locator = "implement me";
			page = null; // new FormMailNew(this.MyApplication);

						

		} else if ( button == Button.B_LISTVIEW ) {

			locator = "implement me";
			page = null; // new FormMailNew(this.MyApplication);

			

		} else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		if ( locator == null ) {
			throw new HarnessException("locator was null for button "+ button);
		}

		// Default behavior, process the locator by clicking on it
		//
		this.sClick(locator);

		// If the app is busy, wait for it to become active
		this.zWaitForBusyOverlayHTML();
		
		return (page);
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ option +")");

		tracer.trace("Click pulldown "+ pulldown +" then "+ option);

		if ( pulldown == null )
			throw new HarnessException("Pulldown cannot be null!");

		if ( option == null )
			throw new HarnessException("Option cannot be null!");

		
		//
		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		// Based on the button specified, take the appropriate action(s)
		//

		if ( pulldown == Button.B_NEW ) {

			if ( option == Button.O_NEW_CONTACTS_FOLDER ) {
				throw new HarnessException("implement me!");
			} else if ( option == Button.O_NEW_APPOINTMENT ) {
				throw new HarnessException("implement me!");
			} else if ( option == Button.O_NEW_BRIEFCASE ) {
				throw new HarnessException("implement me!");
			} else if ( option == Button.O_NEW_CALENDAR ) {
				throw new HarnessException("implement me!");
			} else if ( option == Button.O_NEW_CONTACT ) {
				throw new HarnessException("implement me!");
			} else if ( option == Button.O_NEW_CONTACTGROUP ) {
				throw new HarnessException("implement me!");
			} else if ( option == Button.O_NEW_DOCUMENT ) {
				throw new HarnessException("implement me!");
			} else if ( option == Button.O_NEW_FOLDER ) {
				throw new HarnessException("implement me!");
			} else if ( option == Button.O_NEW_MESSAGE ) {

				// TODO: should this actually click New followed by Message?

				pulldownLocator = null;
				optionLocator = null;
				page = zToolbarPressButton(pulldown);

				

			} else if ( option == Button.O_NEW_TAG ) {
				throw new HarnessException("implement me!");
			} else if ( option == Button.O_NEW_TASK ) {
				throw new HarnessException("implement me!");
			} else if ( option == Button.O_NEW_TASK_FOLDER ) {
				throw new HarnessException("implement me!");
			} else {
				throw new HarnessException("no logic defined for pulldown/option "+ pulldown +"/"+ option);
			}

		} else if ( pulldown == Button.B_LISTVIEW ) { 

			if ( option == Button.O_LISTVIEW_BYCONVERSATION ) {
				throw new HarnessException("implement me!");
			} else if ( option == Button.O_LISTVIEW_BYMESSAGE ) {
				throw new HarnessException("implement me!");
			} else if ( option == Button.O_LISTVIEW_READINGPANEBOTTOM ) {
				throw new HarnessException("implement me!");
			} else if ( option == Button.O_LISTVIEW_READINGPANEOFF ) {
				throw new HarnessException("implement me!");
			} else if ( option == Button.O_LISTVIEW_READINGPANERIGHT ) {
				throw new HarnessException("implement me!");
			} else {
				throw new HarnessException("no logic defined for pulldown/option "+ pulldown +"/"+ option);
			}

		} else if ( pulldown == Button.B_TAG ) {

			if ( option == Button.O_TAG_NEWTAG ) {

				pulldownLocator = "implement me";
				optionLocator = "implement me";
				page = null; // new DialogTag(this.MyApplication, this);

				

			} else if ( option == Button.O_TAG_REMOVETAG ) {

				pulldownLocator = "implement me";
				optionLocator = "implement me";
				page = null; 

				

			} else {
				throw new HarnessException("no logic defined for pulldown/option "+ pulldown +"/"+ option);
			}

		} else {
			throw new HarnessException("no logic defined for pulldown "+ pulldown);
		}

		// Default behavior
		if ( pulldownLocator != null ) {

			// Make sure the locator exists
			if ( !this.sIsElementPresent(pulldownLocator) ) {
				throw new HarnessException("Button "+ pulldown +" option "+ option +" pulldownLocator "+ pulldownLocator +" not present!");
			}

			this.sClick(pulldownLocator);

			// If the app is busy, wait for it to become active
			this.zWaitForBusyOverlayHTML();


			if ( optionLocator != null ) {

				// Make sure the locator exists
				if ( !this.sIsElementPresent(optionLocator) ) {
					throw new HarnessException("Button "+ pulldown +" option "+ option +" optionLocator "+ optionLocator +" not present!");
				}

				this.sClick(optionLocator);

				// If the app is busy, wait for it to become active
				this.zWaitForBusyOverlayHTML();

			}


			// If we click on pulldown/option and the page is specified, then
			// wait for the page to go active
			if ( page != null ) {
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
	 * Return a list of all messages in the current view
	 * @return
	 * @throws HarnessException 
	 */
	public List<MailItem> zListGetMessages() throws HarnessException {

		List<MailItem> items = new ArrayList<MailItem>();

		// int count = this.sGetXpathCount("//tbody[@id='mess_list_tbody']//tr");
		int count = this.sGetCssCount("css=tbody#mess_list_tbody tr");
		for (int row = 0; row < count; row++) {
			// String itemLocator = "//tbody[@id='mess_list_tbody']//tr["+ i +"]";
			String itemLocator = "css=tbody#mess_list_tbody tr#R"+ row;
			String locator;
			
			MailItem item = new MailItem();
			
			// Column 1 is checkbox
			
			// Column 2 is 'flagged'
			
			// Column 3 is 'priority'

			// Column 4 is 'tags'

			// Column 5 is 'sent/reply/received/read/etc.' icon
			
			// Column 6 is 'From'
			locator = itemLocator + ">td:nth-of-type(6)";
			item.gFrom = this.sGetText(locator);

			// Column 7 is 'attachments'
			
			// Column 8 is 'subject'
			locator = itemLocator + ">td:nth-of-type(8) span:nth-of-type(1)";
			item.gSubject = this.sGetText(locator);
			
			// Column 9 is 'size'
			
			// Column 10 is 'received date'
			
			items.add(item);
			
		}
		// Return the list of items
		return (items);
	}

	/**
	 * Return a list of all conversations in the current view
	 * @return
	 * @throws HarnessException 
	 */
	public List<ConversationItem> zListGetConversations() throws HarnessException {
		logger.info(myPageName() + " getConversationList");

		List<ConversationItem> items = new ArrayList<ConversationItem>();


		// Return the list of items
		return (items);
	}




	@Override
	public AbsPage zListItem(Action action, String subject) throws HarnessException {
		logger.info(myPageName() + " zListItem("+ action +", "+ subject +")");

		tracer.trace(action +" on subject = "+ subject);

		AbsPage page = null;

		// default return command
		return (page);

	}

	public AbsPage zListItem(Action action, Button option, FolderItem folderItem) throws HarnessException {
		logger.info(myPageName() + " zListItem("+ action +", "+ option +")");
		tracer.trace(action +" then "+ option +" on Folder Item = "+ folderItem);

		AbsPage page = null;
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

		// Default behavior
		return (page);

	}

	@Override
	public AbsPage zKeyboardShortcut(Shortcut shortcut) throws HarnessException {

		if (shortcut == null)
			throw new HarnessException("Shortcut cannot be null");

		tracer.trace("Using the keyboard, press the "+ shortcut.getKeys() +" keyboard shortcut");

		AbsPage page = null;

		if ( (shortcut == Shortcut.S_NEWITEM) ||
				(shortcut == Shortcut.S_NEWMESSAGE) ||
				(shortcut == Shortcut.S_NEWMESSAGE2) )
		{
			// "New Message" shortcuts result in a compose form opening
			page = null; // new FormMailNew(this.MyApplication);
		}

		zKeyboard.zTypeCharacters(shortcut.getKeys());

		// If the app is busy, wait for it to become active
		this.zWaitForBusyOverlayHTML();

		return (page);
	}




}
