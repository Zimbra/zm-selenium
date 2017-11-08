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
package com.zimbra.qa.selenium.projects.ajax.ui.briefcase;

import org.openqa.selenium.WebElement;
import com.zimbra.qa.selenium.framework.items.DocumentItem;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsForm;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.ajax.ui.AppAjaxClient;

public class DocumentBriefcaseNew extends AbsForm {

	public static class Locators {
		public static final String zFrame = "css=iframe[id$='_body_ifr']";
		public static final String zSaveAndCloseIconBtn = "css=td[id$='_title']:contains('Close')";
		public static final String zBodyField = "css=body";
		public static final String zNameField = "css=td[class='ZmDocsEditViewHeaderCell'] td[id$='_item_1'] input";
		public static final String zEditNameField = "css=[class=DwtInputField] [input$=]";
		public static final String zEnableVersionNotes = "css=div[class=DwtComposite] input[id=enableDesc]";
	}

	public static class Field {
		public static final Field Name = new Field("Name");
		public static final Field Body = new Field("Body");

		private Field(String name) {
			// field = name;
		}
	}

	public static final String pageTitle = "Zimbra";

	public DocumentBriefcaseNew(AbsApplication application) {
		super(application);
		logger.info("new " + DocumentBriefcaseNew.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return this.getClass().getName();
	}

	public void typeDocumentText(String text) throws HarnessException {
		sSelectFrame(Locators.zFrame);
		logger.info("typing Document Text: ");
		sType(Locators.zBodyField, text);
		logger.info(text);
	}

	public void typeDocumentName(String text) throws HarnessException {
		this.zSelectWindow("Zimbra Docs");

		sType(Locators.zNameField, text);
	}

	public void editDocumentName(DocumentItem docItem) throws HarnessException {
		if (sIsElementPresent(Locators.zEditNameField))
			sType(Locators.zEditNameField, docItem.getName());
	}

	@Override
	public void zFill(IItem item) throws HarnessException {
	}

	public void zFillField(Field field, String value) throws HarnessException {

		if (field == Field.Name) {

			String nameFieldLocator = Locators.zNameField;

			zSelectWindow(pageTitle);

			if (!this.sIsElementPresent(nameFieldLocator))
				throw new HarnessException("Locator is not present: " + nameFieldLocator);

			this.sMouseOver(nameFieldLocator);
			this.sFocus(nameFieldLocator);
			this.sClickAt(nameFieldLocator, "0,0");
			this.sType(nameFieldLocator, value);
			logger.info("typed: " + value);

		} else if (field == Field.Body) {

			String iframeLocator = Locators.zFrame;

			if (!this.sIsElementPresent(iframeLocator))
				throw new HarnessException("Locator is not present: " + iframeLocator);

			WebElement we = getElement(iframeLocator);
			this.sMouseOver(iframeLocator);
			this.sFocus(iframeLocator);
			this.sClickAt(iframeLocator, "0,0");
			this.executeScript("var bodytext=\"" + value + "\";" + "var iframe_locator=\"" + iframeLocator + "\";"
					+ "var iframe_body=arguments[0].contentWindow.document.body;"
					+ "if (navigator.userAgent.indexOf('Firefox')!=-1 || navigator.userAgent.indexOf('Chrome')!=-1) {iframe_body.innerHTML=bodytext;}"
					+ "else if (navigator.userAgent.indexOf('MSIE')!=-1) {iframe_body.innerHTML=bodytext;}"
					+ "else {iframe_body.innerHTML=bodytext;}", we);
		} else {
			throw new HarnessException("Not implemented field: " + field);
		}

		this.zWaitForBusyOverlay();
		SleepUtil.sleepVerySmall();
	}

	@Override
	public void zSubmit() throws HarnessException {
		logger.info("DocumentBriefcaseNew.SaveAndClose()");

		// Look for "Save & Close"
		if (!this.sIsElementPresent(Locators.zSaveAndCloseIconBtn))
			throw new HarnessException("Save & Close button is not present " + Locators.zSaveAndCloseIconBtn);

		boolean visible = this.sIsVisible(Locators.zSaveAndCloseIconBtn);
		if (!visible)
			throw new HarnessException("Save & Close button is not visible " + Locators.zSaveAndCloseIconBtn);

		if (!(sIsElementPresent(Locators.zEnableVersionNotes) && sIsChecked(Locators.zEnableVersionNotes))) {
			sClickAt(Locators.zSaveAndCloseIconBtn, "0,0");

		} else {
			sClickAt(Locators.zSaveAndCloseIconBtn, "0,0");

			// add version notes
			DialogAddVersionNotes dlgAddNotes = new DialogAddVersionNotes(MyApplication,
					((AppAjaxClient) MyApplication).zPageBriefcase);

			dlgAddNotes.zDismissAddVersionNotesDlg(pageTitle);

		}
		SleepUtil.sleepMedium();
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		zWaitForWindow(pageTitle);

		zSelectWindow(pageTitle);

		zWaitForElementPresent("css=div[class='ZDToolBar ZWidget']");

		zWaitForElementPresent("css=table[class='ZToolbarTable']");

		zWaitForElementPresent("css=iframe[id$='_body_ifr']");

		zWaitForIframeText("css=iframe[id$='_body_ifr']", "");

		return true;
	}
}