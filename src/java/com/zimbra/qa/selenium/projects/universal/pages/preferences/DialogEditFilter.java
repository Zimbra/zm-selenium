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
package com.zimbra.qa.selenium.projects.universal.pages.preferences;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.universal.pages.DialogTag;
import com.zimbra.qa.selenium.projects.universal.pages.mail.DialogCreateFolder;


/**
 * Represents a "Rename Folder" dialog box
 * <p>
 * @author Matt Rhoades
 *
 */
public class DialogEditFilter extends AbsDialog {

	public static class Locators {

		public static final String MainDivID = "ZmFilterRuleDialog";
		public static final String ActionDropDownLocator = "css=table[id^='ZmFilterRuleDialog_actions']>tbody>tr>td div[class='ImgSelectPullDownArrow']";
		public static final String ConditionDropDownLocator = "css=table[id='ZmFilterRuleDialog_conditions'] div[id*='SELECT'] div[class='ImgSelectPullDownArrow']";
		public static final String ConstraintDropDownLocator = "css=table[id^='ZmFilterRuleDialog_conditions']>tbody>tr>td:nth-of-type(3) div[class='ImgSelectPullDownArrow']";
		public static final String BrowseButton = "css=div[id^='FilterRuleDialog_BUTTON'] td[id$='_title']";
		public static final String DialogNewButton = "css=div[id$='Dialog_buttons'] td[id^='New'] td[id$='_title']:contains(New)";
		public static final String dropdownOptionCss = "css=div[id^='FilterRuleDialog_SELECT'][id*='Menu'] tr";

	}


	// It is difficult to determine if the first criteria is already
	// filled out.  If not, then user needs to click "+" to add a
	// new one.
	//
	// Use this boolean to keep track.
	protected boolean IsFirstCriteria = true;
	protected boolean isFirstAction = true;


	public DialogEditFilter(AbsApplication application, AbsTab tab) {
		super(application, tab);
	}


	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton("+ button +")");

		String locator = null;
		AbsPage page = null;

		if ( button == Button.B_OK ) {

			locator = "css=div[id='ZmFilterRuleDialog_buttons'] td[id^='OK_'] td[id$='_title']";

		} else if ( button == Button.B_CANCEL ) {

			locator = "css=div[id='ZmFilterRuleDialog_buttons'] td[id^='Cancel_'] td[id$='_title']";

		} else {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		sClick(locator);
		zWaitForBusyOverlay();

		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {

		throw new HarnessException("implement me");

	}


	/* (non-Javadoc)
	 * @see framework.ui.AbsDialog#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = "css=div[id='"+ Locators.MainDivID +"']";

		boolean present = this.sIsElementPresent(locator);
		if ( !present ) {
			logger.info("Locator was not present: " + locator);
			return (false);
		}

		boolean visible = this.zIsVisiblePerPosition(locator, 0, 0);
		if ( !visible ) {
			logger.info("Locator was not visible: " + locator);
			return (false);
		}

		return (true);

	}

	public enum Condition {
		Any,
		All,
	}

	public enum ConditionType {
		From,
		To,
		Cc,
		ToOrCc,
		Subject,
		HeaderNamed,
		Size,
		Date,
		Body,
		Attachment,
		ReadReceipt,
		AddressIn,
		Calendar,
		Social,
		Message,
		Address,
	}

	public enum ConditionConstraint {
		MatchesExactly,
		DoesNotMatchExcactly,
		Contains,
		DoesNotContain,
		MatchesWildcard,
		DoesNotMatchWildCard,
	}

	public enum FilterAction {
		KeepInInbox,
		Discard,
		MoveIntoFolder,
		TagWith,
		MarkAs,
		RedirectToAddress,
	}

	public enum MarkAsFilterOption {
		Read,
		Flagged,
	}

	public void zSetConditionAnyOrAll(Condition type) throws HarnessException {		
		
		//When condition type is not All, no need to do any thing
		
		if (type.equals(Condition.All)) {
			
		// Click the pulldown to activate the menu
		String locator = "css=div[id='ZmFilterRuleDialog_condition'] td[id$='_select_container'] td[id$='_dropdown'] div[class='ImgSelectPullDownArrow']";
		this.sClick(locator);
		
		//Select the option 'All'		
		locator="css=div#FilterRuleGroupCondition_0_Menu_1_option_2";
		this.sClick(locator);
		
		} else if (!(type.equals(Condition.All)) && !(type.equals(Condition.Any))) {
			
			throw new HarnessException("Condition type is not valid! Please enter a valid Condition type");
		}
	}


	public void zSetFilterName(String name) throws HarnessException {

		String locator = "css=input[id='ZmFilterRuleDialog_name']";

		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("Unable to locate filter name input box");

		this.sType(locator, name);

	}


	public void zAddFilterCriteria(ConditionType type, ConditionConstraint constraint, String value) throws HarnessException {
		String rowLocator = "css=table[id='ZmFilterRuleDialog_conditions']>tbody>tr";
		String locator;
		String option=null;

		if ( !this.IsFirstCriteria ) {

			int i = this.sGetCssCount(rowLocator);
			if ( i < 1) 
				throw new HarnessException("couldn't find any filter condition rows!");


			// Click the "+" to add a new row
			locator = rowLocator + ":nth-child("+i+") div[class='ImgPlus']";
			this.sClick(locator);
			this.zWaitForBusyOverlay();

			this.IsFirstCriteria = false;

		}

		int count = this.sGetCssCount(rowLocator);
		if ( count < 1) 
			throw new HarnessException("couldn't find any filter condition rows!");
		rowLocator = "css=table[id='ZmFilterRuleDialog_conditions']>tbody>tr:nth-child(" + count + ")"; // Use the last row
		
		//Select Condition type
		switch(type) {

		case Subject: option="Subject"; //this option is selected by default
			 break; 		
		case From: option="From";			 
			 break;
		case To: option="To";	 
			 break;
		case Cc: option="Cc";
			 break;		
		case ToOrCc: option="To or";
			 break;
		case Message: option="Message";
			 break;
		case Size: option="Size";			
			 break;
		case Date: option="Date";			
			 break;
		case Body: option="Body";
			 break;
		case Attachment: option="Attachment";
			 break;
		case ReadReceipt: option="Read Receipt";
			 break;
		case AddressIn: option="Address in";
			 break;
		case Calendar: option="Calendar";
			 break;
		case Social: option="Social";
			 break;
		case HeaderNamed: option="Header Named";
			 break;
		default: throw new HarnessException("Condition type is not valid! Please enter a valid condition type.");
		}		
			
 		this.sClick(Locators.ConditionDropDownLocator);
		SleepUtil.sleepMedium();
		this.sMouseMoveAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");
		this.sClickAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");
		
		//Select the condition constraint option
		if (constraint.equals(ConditionConstraint.Contains)) {
			
			option="contains";
			this.sClick(Locators.ConstraintDropDownLocator);
			SleepUtil.sleepMedium();
			this.sMouseMoveAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");
			this.sClickAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");
			
		} else if (constraint.equals(ConditionConstraint.MatchesWildcard)) {
			
			option="matches wildcard";
			this.sClick(Locators.ConstraintDropDownLocator);
			SleepUtil.sleepMedium();
			this.sMouseMoveAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");
			this.sClickAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");
			
		} else {
			
			throw new HarnessException("Condition constraint is not implemented yet! Please enter condition constraint as 'Contains' or 'MatchesWildcard' only.");
		}
		
		//Enter the conditions string.
		locator = rowLocator + " div[id^='FilterRuleDialog_INPUTFIELD_'] input[id^='FilterRuleDialog_INPUT_']";
		this.sType(locator, value);
		this.zWaitForBusyOverlay();		
		
	}

	//Add a filter action
	public void zAddFilterAction(FilterAction action, String... name) throws HarnessException {		
		String option=null;

		switch(action) {

		case KeepInInbox:option="Keep in Inbox"; //This option is by default selected. No need to do any action.
		
			this.sClick(Locators.ActionDropDownLocator);
			SleepUtil.sleepMedium();
			this.sMouseMoveAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");
			this.sClickAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");										
			break;

		case Discard:option="Discard";

			this.sClick(Locators.ActionDropDownLocator);
			SleepUtil.sleepMedium();
			this.sMouseMoveAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");
			this.sClickAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");										
			break;

		case MoveIntoFolder: if (!name[0].equals(null)) {

			option="Move into folder";
			
			//Click on action drop down
			this.sClick(Locators.ActionDropDownLocator);
			SleepUtil.sleepMedium();
			
			//Go to the option and select it
			this.sClickAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");			
			SleepUtil.sleepSmall();
			
			//Click 'Browse' button
			this.sClick(Locators.BrowseButton); //Browse folder
			SleepUtil.sleepMedium();
			
			//Click on 'New' Button on Choose folder dialog to create new folder 
			this.sClickAt(DialogCreateFolder.Locators.zChooseNewFolderButton,"0,0");
			SleepUtil.sleepMedium();
			
			//Enter the name of the new folder
			this.sType(DialogCreateFolder.Locators.DialogNameLocatorCSS, name[0]);
			SleepUtil.sleepVerySmall();
			
			//Click Ok button to create new folder
			this.sClickAt(DialogCreateFolder.Locators.zOkButton,"0,0");
			SleepUtil.sleepSmall();
			
			//Click Ok to select the folder
			this.sClickAt("css=div[id$='FolderDialog_buttons'] td[id^='OK'] td[id$='_title']","0,0");
			break;

		} else {

			throw new HarnessException("Folder name cannot be null! Enter a folder name. ");
		}
		case TagWith: if (!name.equals(null)) {

			option="Tag with";
			
			//Click on action drop down
			this.sClick(Locators.ActionDropDownLocator);
			SleepUtil.sleepMedium();
			
			//Go to the option and select it
			this.sMouseMoveAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");
			this.sClickAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");			
			SleepUtil.sleepMedium();
			
			//Click 'Browse' button
			this.sClick(Locators.BrowseButton); //Browse Tag
			SleepUtil.sleepVerySmall();
			
			//Click New button on Choose Tag dialog
			this.sClick(DialogTag.Locators.zChooseNewTagButton);
			SleepUtil.sleepVerySmall();
			
			//Enter name for new tag
			this.sType(DialogTag.Locators.zTagNameFieldCss,name[0]);	
			
			this.sClick(DialogTag.Locators.zButtonOkCss);
			SleepUtil.sleepSmall();
			
			this.sMouseMoveAt("css=div[id^='zti__ZmPickTagDialog'] td[id$='_textCell']:contains(" +name[0] +")","0,0");
			this.sClickAt("css=div[id^='zti__ZmPickTagDialog'] td[id$='_textCell']:contains(" +name[0] +")","0,0");
			
			this.sClick("css=div[id$='TagDialog_buttons'] td[id^='OK'] td[id$='_title']");
			break;

		} else {

			throw new HarnessException("Tag name can not null! Enter a tag name.");	
		}

		case MarkAs: if (name[0].equalsIgnoreCase("Read") || name[0].equalsIgnoreCase("Flagged")) {

			option="Mark";			
			this.sClick(Locators.ActionDropDownLocator);
			SleepUtil.sleepMedium();
			this.sMouseMoveAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");
			this.sClickAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");			
			SleepUtil.sleepMedium();

			if (name[0].equalsIgnoreCase("Flagged")) {

				this.sClick("css=table[id='ZmFilterRuleDialog_actions'] div[id*='SELECT']:contains("+ name[0] +")");
				SleepUtil.sleepMedium();			
				this.sMouseMoveAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");
				this.sClickAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");

			}
			break;

		} else {

			throw new HarnessException("MarkAs option is not valid! Please enter a valid MarkAs option.");	
		}
		case RedirectToAddress:if (!name[0].equals(null)) {

			option="Redirect";
			this.sClick(Locators.ActionDropDownLocator);
			SleepUtil.sleepMedium();
			this.sMouseMoveAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");
			this.sClickAt(Locators.dropdownOptionCss+":contains(" + option +") td td[id$='_title']","0,0");					
			SleepUtil.sleepMedium();
			this.sType("css=table[id='ZmFilterRuleDialog_actions'] input", name[0]);			
			break;

		} else {

			throw new HarnessException("Email Address can't be null! Please entera a valid email address for redirection.");	
		}
		
		default:throw new HarnessException("Filter action is invalid");
		}
	}



}
