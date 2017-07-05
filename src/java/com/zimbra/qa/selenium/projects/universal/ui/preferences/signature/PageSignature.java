/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.ui.preferences.signature;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.I18N;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.universal.ui.AppAjaxClient;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.DialogSelectContact;

public class PageSignature extends AbsTab {

	public PageSignature(AbsApplication application) {
		super(application);
	}

	public static class Locators {

		// Preferences Toolbar: Save, Cancel
		public static final String zToolbarSaveID = "zb__PREF__SAVE_title";
		public static final String zToolbarCancelID = "zb__PREF__CANCEL_title";
		public static final String zSignatureListView = "//div[@class='ZmSignatureListView']";
		public static final String zNewSignature = "css=td[class='ZOptionsField'] td[id$='_title']:contains('"
				+ I18N.NEW_SIGNATURE + "')";
		public static final String zDeleteSignature = "css=td[class='ZOptionsField'] td[id$='_title']:contains('"
				+ I18N.DELETE + "')";
	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		return null;
	}

	public String zGetSignatureNameFromListView() throws HarnessException {
		String locator = null;
		locator = Locators.zSignatureListView;
		String sigListViewName = this.sGetText(locator);
		return sigListViewName;

	}

	public String zGetSignatureBodyText() throws HarnessException {

		// bug 59078
		String locator = null;
		locator = "return document.getElementById('TEXTAREA_SIGNATURE').value";
		String textsig = this.sGetEval(locator);
		return textsig;

	}

	public String zGetHtmlSignatureBody() throws HarnessException {
		try {
			sSelectFrame("css=iframe[id='TEXTAREA_SIGNATURE_ifr']");
			String sigbodyhtml = this.sGetHtmlSource();
			return sigbodyhtml;
		} finally {
			this.sSelectFrame("relative=top");
		}
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException {
		return null;
	}

	@Override
	public void zNavigateTo() throws HarnessException {
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_NEW) {
			locator = Locators.zNewSignature;
			page = new FormSignatureNew(this.MyApplication);

		} else if (button == Button.B_DELETE) {
			locator = Locators.zDeleteSignature;
			page = null;

		} else if (button == Button.B_BROWSE) {
			locator = "css=div[id='Prefs_Pages_SIGNATURES'] div[class='DwtComposite'] div[class^='ZButton'] td[id$='_title']:contains('Browse...')";
			page = new DialogSelectContact(MyApplication, ((AppAjaxClient) MyApplication).zPageSignature);

		} else {

			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		// Default behavior, process the locator by clicking on it

		this.zClickAt(locator, "");

		// If the app is busy, wait for it to become active
		this.zWaitForBusyOverlay();

		return (page);
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		return null;
	}

	@Override
	public String myPageName() {
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		return false;
	}

	public void zSelectSignature(String signatureName) throws HarnessException {
		String locator = "css=div[class='ZmSignatureListView'] td:contains('" + signatureName + "')";
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Signature name locator " + locator + " doesn't exists.");
		}
		this.sClick(locator);
		SleepUtil.sleepMedium();
	}

}
