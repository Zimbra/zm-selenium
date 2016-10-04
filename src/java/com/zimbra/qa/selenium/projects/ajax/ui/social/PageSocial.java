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
/**
 *
 */
package com.zimbra.qa.selenium.projects.ajax.ui.social;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.ajax.ui.AppAjaxClient;
import com.zimbra.qa.selenium.projects.ajax.ui.PageMain;


/**
 * @author Matt Rhoades
 *
 */
public class PageSocial extends AbsTab {

	public static class Locators {
		public static final String StatusTextAreaLocatorCSS = "css=textarea[id='social_statusTextArea']";
	}

	public PageSocial(AbsApplication application) {
		super(application);
		logger.info("new " + PageSocial.class.getCanonicalName());
	}

	private boolean zDismissWelcomeDialog() throws HarnessException {
		DialogSocialZimletWelcome dialog = new DialogSocialZimletWelcome(MyApplication, ((AppAjaxClient) MyApplication).zPageSocial);
		if ( dialog.zIsActive() ) {
			dialog.zClickButton(Button.B_OK);
			return (true);
		}
		return (false);
	}
	
	@Override
	public boolean zIsActive() throws HarnessException {

		if ( !((AppAjaxClient)MyApplication).zPageMain.zIsActive() ) {
			((AppAjaxClient)MyApplication).zPageMain.zNavigateTo();
		}

		this.zDismissWelcomeDialog();
		
		boolean present = this.sIsElementPresent(Locators.StatusTextAreaLocatorCSS);
		if ( !present ) {
			return (false);
		}
		
		boolean visible = this.zIsVisiblePerPosition(Locators.StatusTextAreaLocatorCSS, 0, 0);
		if ( !visible ) {
			return (false);
		}
		
		return (true);
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if ( zIsActive() ) {
			logger.info(myPageName() + " is already loaded");
			return;
		}

		if ( !((AppAjaxClient)MyApplication).zPageMain.zIsActive() ) {
			((AppAjaxClient)MyApplication).zPageMain.zNavigateTo();
		}

		logger.info("Navigate to "+ this.myPageName());

		this.zClick(PageMain.Locators.zSocialTab);
		SleepUtil.sleepSmall();

		this.zWaitForBusyOverlay();
		zWaitForActive();

		logger.info("Navigated to "+ this.myPageName() + " page");
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton("+ button +")");
		tracer.trace("Press the "+ button +" button");
		throw new HarnessException("implement me!");
	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		throw new HarnessException("implement me!");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		throw new HarnessException("implement me!");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException {
		throw new HarnessException("implement me!");
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		throw new HarnessException("implement me!");
	}
}
