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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.filters;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.DialogEditFilter;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.PagePreferences;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.DialogEditFilter.FilterAction;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class EditFilter extends AjaxCommonTest {

	public EditFilter() {
		super.startingPage = app.zPagePreferences;
	}


    @Test (description = "Edit an incoming filter",
			groups = { "functional", "L2" })

	public void EditFilter_01() throws HarnessException {

		String filterName = "filterIN"+ ConfigProperties.getUniqueString();
		String conditionValue = "contains"+ ConfigProperties.getUniqueString();

		// Create a filter
		app.zGetActiveAccount().soapSend(
				"<ModifyFilterRulesRequest xmlns='urn:zimbraMail'>"
		                +	"<filterRules>"
						+		"<filterRule active='1' name='" + filterName + "'>"
						+			"<filterTests condition='anyof' >"
						+				"<headerTest header='subject' stringComparison='contains' value='"+conditionValue+"' />"
						+			"</filterTests>"
						+			"<filterActions>"
						+				"<actionKeep />"
						+				"<actionStop />"
						+			"</filterActions>"
						+		"</filterRule>"
						+	"</filterRules>"
		                +"</ModifyFilterRulesRequest>");

		// Verify the filter is created successfully with given conditions.
		app.zGetActiveAccount().soapSend(
				"<GetFilterRulesRequest xmlns='urn:zimbraMail'/>");

		Element[] rulesNew = app.zGetActiveAccount().soapSelectNodes("//mail:GetFilterRulesResponse//mail:filterRule[@name='" + filterName +"']//mail:actionKeep[@index='0']");
		ZAssert.assertEquals(rulesNew.length, 1, "Incoming filter is not created successfully in server!");

		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailFilters);

		// Select the filter to be Edited
		app.zPagePreferences.sClick(PagePreferences.Locators.zFilterRowCss +":contains("+ filterName +")");

		// Click "Edit Filter"
		DialogEditFilter dialog = (DialogEditFilter)app.zPagePreferences.zToolbarPressButton(Button.B_EDIT_IN_FILTER);

		// Give an action (if necessary)
		dialog.zAddFilterAction(FilterAction.Discard);

		// Save
		dialog.zClickButton(Button.B_OK);

		// Verify the filter is created
		app.zGetActiveAccount().soapSend("<GetFilterRulesRequest xmlns='urn:zimbraMail'/>");

		rulesNew = app.zGetActiveAccount().soapSelectNodes("//mail:GetFilterRulesResponse//mail:filterRule[@name='" + filterName +"']//mail:actionDiscard[@index='0']");
		ZAssert.assertEquals(rulesNew.length, 1, "Incoming filter is not edited and updated successfully in server!");
	}


    @Test(	description = "Edit a outgoing filter",
			groups = { "functional", "L2" })

	public void EditFilter_02() throws HarnessException {

		String filterName = "filterOUT"+ ConfigProperties.getUniqueString();
		String conditionValue = "contains"+ ConfigProperties.getUniqueString();

		// Create a filter
		app.zGetActiveAccount().soapSend(
				"<ModifyOutgoingFilterRulesRequest xmlns='urn:zimbraMail'>"
		                +	"<filterRules>"
						+		"<filterRule active='1' name='" + filterName + "'>"
						+			"<filterTests condition='anyof' >"
						+				"<headerTest header='subject' stringComparison='contains' value='"+conditionValue+"'/>"
						+			"</filterTests>"
						+			"<filterActions>"
						+				"<actionKeep />"
						+				"<actionStop />"
						+			"</filterActions>"
						+		"</filterRule>"
						+	"</filterRules>"
		                +"</ModifyOutgoingFilterRulesRequest>");

		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailFilters);

		// Go to outgoing filter tab
		app.zPagePreferences.sClick(PagePreferences.Locators.zOutGoingFilterTab);
		SleepUtil.sleepSmall();

		// Select the filter to be Edited
		app.zPagePreferences.sClick(PagePreferences.Locators.zFilterRowCss +":contains("+ filterName +")");

		// Click Edit Filter
		DialogEditFilter dialog = (DialogEditFilter)app.zPagePreferences.zToolbarPressButton(Button.B_EDIT_OUT_FILTER);

		// Give an action (if necessary)
		dialog.zAddFilterAction(FilterAction.Discard);

		// Save
		dialog.zClickButton(Button.B_OK);

		// Verify the filter is created
		app.zGetActiveAccount().soapSend("<GetOutgoingFilterRulesRequest xmlns='urn:zimbraMail'/>");

		Element[] rulesNew = app.zGetActiveAccount().soapSelectNodes("//mail:GetOutgoingFilterRulesResponse//mail:filterRule[@name='" + filterName +"']//mail:actionDiscard[@index='0']");
		ZAssert.assertEquals(rulesNew.length, 1, "Incoming filter is not edited and updated successfully in server!");
	}
}