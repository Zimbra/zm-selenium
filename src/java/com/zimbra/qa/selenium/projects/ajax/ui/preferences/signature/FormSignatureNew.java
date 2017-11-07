/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui.preferences.signature;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsForm;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.I18N;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.ajax.ui.AppAjaxClient;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;

public class FormSignatureNew extends AbsForm {

	public FormSignatureNew(AbsApplication application) {
		super(application);
	}

	public static class Locators {

		public static final String zsignatureNameLabel = "css=input[id*='_SIG_NAME']";
		public static final String signatureBody = "css=textarea[id*='TEXTAREA_SIGNATURE']";
		public static final String zToolbarSaveID = "zb__PREF__SAVE_title";
		public static final String zToolbarCancelID = "zb__PREF__CANCEL_title";
		public static final String formatAsText = "//td[contains(@id,'_title') and contains (text(),'"
				+ I18N.FORMAT_AS_PLAIN_TEXT + "')]";
		public static final String formatAsHtml = "//td[contains(@id,'_title') and contains (text(),'"
				+ I18N.FORMAT_AS_HTML_TEXT + "')]";
		public static final String zFrame = "css=iframe[id='TEXTAREA_SIGNATURE_ifr']";
		public static final String zHtmlBodyField = "css=body";
	}

	public static class Field {

		public static final Field SignatureName = new Field("SignatureName");
		public static final Field SignatureBody = new Field("SignatureBody");
		public static final Field SignatureHtmlBody = new Field("SignatureHtmlBody");

		private String field;

		private Field(String name) {
			field = name;
		}

		@Override
		public String toString() {
			return (field);
		}

	}

	@Override
	public String myPageName() {
		return null;
	}

	@Override
	public void zFill(IItem item) throws HarnessException {
	}

	@Override
	public void zSubmit() throws HarnessException {
		zToolbarPressButton(Button.B_SAVE);
	}

	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Click button " + button);

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_SAVE) {

			locator = "id=" + Locators.zToolbarSaveID;
			page = null;

		} else if (button == Button.B_CANCEL) {

			locator = "id=" + Locators.zToolbarCancelID;
			page = new DialogWarning(DialogWarning.DialogWarningID.SaveSignatureChangeMessage, this.MyApplication,
					((AppAjaxClient) this.MyApplication).zPageSignature);

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Button is not present locator=" + locator + " button=" + button);

		this.zClickAt(locator, "");
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		return (page);
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
	public boolean zIsActive() throws HarnessException {
		return false;
	}

	public void zFillField(Field field, String value) throws HarnessException {
		tracer.trace("Set " + field + " to " + value);

		String locator = null;

		if (field == Field.SignatureName) {
			locator = Locators.zsignatureNameLabel;

		} else if (field == Field.SignatureBody) {
			locator = Locators.signatureBody;
			this.sFocus(locator);
			this.zClickAt(locator, "");
			clearField(locator);
			sType(locator, value);
			return;

		} else if (field == Field.SignatureHtmlBody) {
			sClickAt("//div[contains(@class,'ZmHtmlEditor')]", "");
			zTypeFormattedText("css=iframe[id*=ifr]", value);
			return;

		} else {

			throw new HarnessException("not implemented for field " + field);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Field is not present field=" + field + " locator=" + locator);

		this.zClickAt(locator, "");
		sType(locator, value);
		this.zWaitForBusyOverlay();

	}

	public AbsPage zSelectFormat(String format) throws HarnessException {
		AbsPage page = null;
		if (format.equals("html")) {
			boolean isExists = this.sIsElementPresent(Locators.formatAsText);
			if (isExists) {
				this.zClick(Locators.formatAsText);

			} else {
				this.zClick(Locators.formatAsHtml);
			}
			this.zClick(Locators.formatAsHtml);
			return page;
		} else {

			this.zClick(Locators.formatAsHtml);
			this.zClick(Locators.formatAsText);
			page = new DialogWarning(DialogWarning.DialogWarningID.ComposeOptionsChangeWarning, this.MyApplication,
					((AppAjaxClient) this.MyApplication).zPagePreferences);
			return page;
		}
	}
}