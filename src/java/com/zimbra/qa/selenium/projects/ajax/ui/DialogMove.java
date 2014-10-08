/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties.AppType;
import com.zimbra.qa.selenium.projects.ajax.ui.addressbook.PageAddressbook;
import com.zimbra.qa.selenium.projects.ajax.ui.briefcase.PageBriefcase;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.PageCalendar;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.PageMail;
import com.zimbra.qa.selenium.projects.ajax.ui.tasks.PageTasks;

public class DialogMove extends AbsDialog {
	public static class Locators {

		// TODO:  See https://bugzilla.zimbra.com/show_bug.cgi?id=54173
		public static final String zDialogId			= "ChooseFolderDialog";
		public static final String zTitleId	 			= "ChooseFolderDialog_title";
		public static final String zDialogContentId		= "ChooseFolderDialog_content";
		// TODO: Tree
		public static final String zDialogInputId		= "ChooseFolderDialog_inputDivId";
		public static final String zDialogInputLocator	= "css=div[id='"+ zDialogId +"'] div[id='"+ zDialogInputId +"'] > div > input";
		public static final String zDialogButtonsId		= "ChooseFolderDialog_buttons";

	}

	public DialogMove(AbsApplication application,AbsTab page) {
		super(application,page);

		logger.info("new "+ DialogMove.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton(" + button + ")");

		AbsPage page = null;
		String locator = null;

		if (button == Button.B_NEW) {

			locator = "css=div[id='ChooseFolderDialog_buttons'] td[id^='New_'] td[id$='_title']";

		} else if (button == Button.B_OK) {

			locator = "css=div[id='ChooseFolderDialog_buttons'] td[id^='OK_'] td[id$='_title']";

		} else if (button == Button.B_CANCEL) {

			locator = "css=div[id='ChooseFolderDialog_buttons'] td[id^='Cancel_'] td[id$='_title']";

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		// Default behavior, click the locator
		//

		// Make sure the locator was set
		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		this.zClick(locator);

		this.zWaitForBusyOverlay();

		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		logger.info(myPageName() + " zIsActive()");

		String locator = "css=div[id='"+ Locators.zDialogId +"']";

		if ( !this.sIsElementPresent(locator) ) {
			return (false); // Not even present
		}

		if ( !this.zIsVisiblePerPosition(locator, 0, 0) ) {
			return (false);	// Not visible per position
		}

		// Yes, visible
		logger.info(myPageName() + " zIsActive() = true");
		return (true);

	}

	/**
	 * Enter text into the move message dialog folder name field
	 * @param folder
	 */
	public void zEnterFolderName(String folder) throws HarnessException {
		String locator = "css=div[id='ChooseFolderDialog_inputDivId']>div>input";

		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("unable to find folder name field "+ locator);

		this.zClick(locator);
		zKeyboard.zTypeCharacters(folder);

		// SleepUtil.sleepSmall();
		this.zWaitForBusyOverlay();

	}


	/**
	 * Left-Click on a folder in the tree
	 * @param folder
	 * @throws HarnessException
	 */
	public void zClickTreeFolder(FolderItem folder) throws HarnessException {

		logger.info(myPageName() + " zClickTreeFolder(" + folder + ")");

		if (folder == null)

			throw new HarnessException("folder must not be null");

		String locator = null;

		if (MyTab instanceof PageMail) {

		   if (ZimbraSeleniumProperties.getAppType() == AppType.DESKTOP) {
		      locator = "css=div[id='" + Locators.zDialogId+ "'] td[id^='zti__" +
		            MyApplication.zGetActiveAccount().EmailAddress +
		            ":ZmChooseFolderDialog_Mail___'][id$=':" +
		            folder.getId() +
		            "_textCell']";
		   } else {
		      locator = "css=div[id='" + Locators.zDialogId+ "'] div[id^='zti__ZmChooseFolderDialog_Mail'] td[id$='"+ folder.getId() + "_textCell']";
		   }

		} else if (MyTab instanceof PageAddressbook) {

			   if (ZimbraSeleniumProperties.getAppType() == AppType.DESKTOP) {
			      locator = "css=div[id='" + Locators.zDialogId
	            + "'] td[id^='zti__" + MyApplication.zGetActiveAccount().EmailAddress +
	            ":ZmChooseFolderDialog_Contacts__'][id$=':"
	            + folder.getId() + "_textCell']";
			   } else {
	   			locator = "css=div[id='" + Locators.zDialogId
	   			+ "'] td[id='zti__ZmChooseFolderDialog_Contacts___"
	   			+ folder.getId() + "_textCell']";
			   }

		} else if (MyTab instanceof PageCalendar) {

			locator = String.format(
					"css=div[id='%s'] td[id='zti__ZmChooseFolderDialog_Calendar_CALENDAR___%s_textCell']",
					Locators.zDialogId,
					folder.getId());

		}else if (MyTab instanceof PageTasks){
			if (ZimbraSeleniumProperties.getAppType() == AppType.DESKTOP) {
			   locator = "css=div[id='" + Locators.zDialogId + "'] td[id^='zti__" +
			         MyApplication.zGetActiveAccount().EmailAddress +
			         ":ZmChooseFolderDialog_Tasks___'][id$='" +
			         folder.getId() +
			         "_textCell']";
			} else {
			   locator = "css=div[id='" + Locators.zDialogId + "'] div[class='DwtTreeItemLevel1ChildDiv'] td[id='zti__ZmChooseFolderDialog_Tasks___"+ folder.getId() + "_textCell']";
			}

		}else if (MyTab instanceof PageBriefcase ) {
			if (ZimbraSeleniumProperties.getAppType() == AppType.DESKTOP) {
			   locator = "css=div[id='" + Locators.zDialogId + "'] td[id='zti__"+
			         MyApplication.zGetActiveAccount().EmailAddress +
			         ":ZmChooseFolderDialog_Briefcase___'][id$='" +
			         folder.getId() +
			         "_textCell']";
			} else {
			   locator = "css=div[id='" + Locators.zDialogId + "'] td[id='zti__ZmChooseFolderDialog_Briefcase___"+ folder.getId() + "_textCell']";
			}

		} else {
			throw new HarnessException("Unknown app type!");
		}

		// For some reason, the text doesn't get entered on the first try

		this.sClickAt(locator,"0,0");

		this.zWaitForBusyOverlay(); // This method call seems to be missing from
		// the briefcase function

	}
}
