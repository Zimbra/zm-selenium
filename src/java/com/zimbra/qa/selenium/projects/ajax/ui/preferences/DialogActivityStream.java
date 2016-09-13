/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui.preferences;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;

/**
 * Represents the "Activity Stream" dialog box
 * <p>
 * @author Matt Rhoades
 *
 */
public class DialogActivityStream extends AbsDialog {

	public static class Locators {

		public static final String MainDivCSS = "css=div.ZmPriorityMessageFilterDialog";
		
	}
	

	// It is difficult to determine if the first criteria is already
	// filled out.  If not, then user needs to click "+" to add a
	// new one.
	//
	// Use this boolean to keep track.
	protected boolean IsFirstCriteria = true;
	protected boolean isFirstAction = true;
	
	
	public DialogActivityStream(AbsApplication application, AbsTab tab) {
		super(application, tab);
	}
	
	public AbsPage zClickCheckbox(Button button, boolean status) throws HarnessException {
		logger.info(myPageName() + " zClickCheckbox("+ button +", "+ status +")");

		String locator = null;
		AbsPage page = null;
		
		if ( button == Button.B_ACTIVITY_STREAM_ENABLE ) {
			
			locator = Locators.MainDivCSS + " input#PriorityInboxDialog_MOVE_MSG_STREAM";
			page = null;

		} else {
			throw new HarnessException("Button "+ button +" not implemented");
		}
		
		this.zCheckboxSet(locator, status);
		zWaitForBusyOverlay();
		
		return (page);
	}
	
	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton("+ button +")");

		String locator = null;
		AbsPage page = null;
		
		if ( button == Button.B_SAVE ) {
			
			locator = Locators.MainDivCSS + " td[id^='OK_'] td[id$='_title']";

		} else if ( button == Button.B_CANCEL ) {
				
			locator = Locators.MainDivCSS + " td[id^='Cancel_'] td[id$='_title']";

		} else {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		zClick(locator);
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

		String locator = DialogActivityStream.Locators.MainDivCSS;

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
		
	public void zSetConditionAnyOrAll(Condition type) throws HarnessException {
		
		// Click the pulldown to activate the menu
		String locator = "css=div[id='ZmFilterRuleDialog_condition'] td[id$='_select_container'] td[id$='_dropdown'] div[class='ImgSelectPullDownArrow']";
		
		this.zClick(locator);
		this.zWaitForBusyOverlay();
		
		throw new HarnessException("see https://bugzilla.zimbra.com/show_bug.cgi?id=63823");
	}
	
	
	public void zSetFilterName(String name) throws HarnessException {

		String locator = "css=input[id='ZmFilterRuleDialog_name']";

		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("Unable to locate filter name input box");
		
		this.sType(locator, name);
		this.zWaitForBusyOverlay();
		
	}


	public void zAddFilterCriteria(ConditionType type, ConditionConstraint constraint, String value) throws HarnessException {
		String rowLocator = "css=table[id='ZmFilterRuleDialog_conditions']>tbody>tr";
		String locator;
		
		if ( !this.IsFirstCriteria ) {
			
			int i = this.sGetCssCount(rowLocator);
			if ( i < 1) 
				throw new HarnessException("couldn't find any filter condition rows!");
			

			// Click the "+" to add a new row
			locator = rowLocator + ":nth-child("+i+") div[class='ImgPlus']";
			this.zClick(locator);
			this.zWaitForBusyOverlay();
				
			this.IsFirstCriteria = false;
			
		}
		
		int count = this.sGetCssCount(rowLocator);
		if ( count < 1) 
			throw new HarnessException("couldn't find any filter condition rows!");
		rowLocator = "css=table[id='ZmFilterRuleDialog_conditions']>tbody>tr:nth-child(" + count + ")"; // Use the last row
		
		
		if ( !type.equals(ConditionType.Subject) ) {
			// TODO!
			throw new HarnessException("implement me!");
		}
		
		if ( !constraint.equals(ConditionConstraint.Contains) ) {
			// TODO!
			throw new HarnessException("implement me!");
		}
		
		locator = rowLocator + " div[id^='FilterRuleDialog_INPUTFIELD_'] input[id^='FilterRuleDialog_INPUT_']";
		this.sType(locator, value);
		this.zWaitForBusyOverlay();
		
		
	}


	public void zAddFilterAction() throws HarnessException {
		throw new HarnessException("implement me!");
	}



}
