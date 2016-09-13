/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.filters;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.DialogEditFilter;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.DialogEditFilter.ConditionConstraint;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.DialogEditFilter.ConditionType;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.DialogEditFilter.FilterAction;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.PagePreferences;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class CreateFilter extends AjaxCommonTest {

	public CreateFilter() {
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = null;
	}

	@Bugs(ids="97040")
	@Test( description = "Create a basic Incoming Message Filter",	
			groups = { "sanity" } )
    
	public void CreateFilter_01() throws HarnessException {

		String filterName = "filter"+ ConfigProperties.getUniqueString();
		String conditionValue = "contains"+ ConfigProperties.getUniqueString();
		
		// Create a folder
		String folderName = "JiraIn";  //		
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailFilters);
		
		// Click "Add New"
		DialogEditFilter dialog = (DialogEditFilter)app.zPagePreferences.zToolbarPressButton(Button.B_NEW_IN_FILTER);		
		
		// Give a name
		dialog.zSetFilterName(filterName);
		
		// Give a criteria
		dialog.zAddFilterCriteria(ConditionType.From, ConditionConstraint.MatchesWildcard, conditionValue);	
		
		// Give an action (if necessary)
		dialog.zAddFilterAction(FilterAction.MoveIntoFolder,folderName);

		// Save
		dialog.zClickButton(Button.B_OK);		
		
		// Verify the filter is created through SOAP
		app.zGetActiveAccount().soapSend("<GetFilterRulesRequest xmlns='urn:zimbraMail'/>");
		
		Element[] rules = app.zGetActiveAccount().soapSelectNodes("//mail:GetFilterRulesResponse//mail:filterRule[@name='" + filterName +"']");
		ZAssert.assertEquals(rules.length, 1, "Verify the Incoming filter rule exists in the server");
		
		// Verify that filter is created through UI
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(PagePreferences.Locators.zFilterRowCss +":contains("+ filterName +")"), "Incoming filter is not created successfully!");
		
	}
    
    
    @Test( description = "Create a basic Outgoing Message Filter",	groups = { "smoke" } )
    
	public void CreateFilter_02() throws HarnessException {

		String filterName = "Outfilter";
		String conditionValue = "contains"+ ConfigProperties.getUniqueString();
		
		// Create a folder
		String folderName = "JiraOut";  //		
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailFilters);
		
		//Go to outgoing filter tab 
		app.zPagePreferences.sClick(PagePreferences.Locators.zOutGoingFilterTab);
		
		// Click "Add New"
		DialogEditFilter dialog = (DialogEditFilter)app.zPagePreferences.zToolbarPressButton(Button.B_NEW_OUT_FILTER);		
		
		// Give a name
		dialog.zSetFilterName(filterName);
		
		// Give a criteria
		dialog.zAddFilterCriteria(ConditionType.Subject, ConditionConstraint.MatchesWildcard, conditionValue);	
		
		// Give an action (if necessary)
		dialog.zAddFilterAction(FilterAction.TagWith,folderName);

		// Save
		dialog.zClickButton(Button.B_OK);		
		
		// Verify the filter is created through SOAP
		app.zGetActiveAccount().soapSend("<GetOutgoingFilterRulesRequest xmlns='urn:zimbraMail'/>");
		
		Element[] rules = app.zGetActiveAccount().soapSelectNodes("//mail:GetOutgoingFilterRulesResponse//mail:filterRule[@name='" + filterName +"']");
		ZAssert.assertEquals(rules.length, 1, "Verify the Outgoing filter rule exists in the server");
		
		//Verify that filter is created through UI		
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(PagePreferences.Locators.zFilterRowCss +":contains("+ filterName +")"), "Outgoing filter is not created successfully!");
	}
}