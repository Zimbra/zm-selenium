/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui.briefcase;

import java.util.List;
import org.openqa.selenium.WebElement;
import com.zimbra.qa.selenium.framework.items.DocumentItem;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsDisplay;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;

public class DocumentBriefcaseOpen extends AbsDisplay {

	public static class Locators {
		public static final String zFrame = "css=iframe[id='DWT9']";
		public static final String zSaveAndCloseIconBtn = "//*[@id='DWT8_left_icon']";
		public static final String zBodyField = "css=body";
		public static final String zDocumentBodyField = "css=td[class='ZhAppContent'] div[id='zdocument']";
		public static final String zFileBodyField = "//html/body";
		public static final String zNameField = "css=[class=DwtInputField] [input$=]";
		public static final String zDocumentNameField = "css=[class=TbTop] b";
	}

	public String pageTitle;
	public String pageText;

	public DocumentBriefcaseOpen(AbsApplication application) {
		super(application);
		logger.info("new " + DocumentBriefcaseOpen.class.getCanonicalName());
	}

	public DocumentBriefcaseOpen(AbsApplication application, IItem item) {
		super(application);
		pageTitle = item.getName();

		if (item instanceof DocumentItem)
			pageText = ((DocumentItem) item).getDocText();

		logger.info("new " + DocumentBriefcaseOpen.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return this.getClass().getName();
	}

	public String retriveFileText() throws HarnessException {
		String text = sGetText(Locators.zFileBodyField);

		return text;
	}

	public String retriveDocumentText() throws HarnessException {
		// ClientSessionFactory.session().selenium().selectFrame(Locators.zFrame);
		String text = sGetText(Locators.zDocumentBodyField);
		// if (zIsVisiblePerPosition(Locators.zDocumentBodyField, 0, 0)) {
		// text = zGetHtml(Locators.zBodyField);
		// text = sGetText(Locators.zBodyField);
		// text = sGetText(Locators.zDocumentBodyField);
		// }
		return text;
	}

	public String retriveDocumentName() throws HarnessException {
		String text = null;
		if(ZimbraSeleniumProperties.isWebDriver()) {
			WebElement we = getElement(Locators.zDocumentNameField);
			if(we !=null)
			text = we.getText();
		}
		else if(ZimbraSeleniumProperties.isWebDriverBackedSelenium())
			text = webDriverBackedSelenium().getText(Locators.zDocumentNameField);
		else
			text = sGetText(Locators.zDocumentNameField);

		return text;
	}

	public void typeDocumentName(String text) throws HarnessException {
		if (sIsElementPresent(Locators.zNameField))
			sType(Locators.zNameField, text);
	}

	public void zFill(IItem item) throws HarnessException {
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		zWaitForWindow(pageTitle);

		List<String> windows = sGetAllWindowNames();
		for (String window : windows) {
			if(window.indexOf(pageTitle.split("\\.")[0])!=-1){
				pageTitle = window;
			}
		}
		zSelectWindow(pageTitle);

		if (pageText != null)
		zWaitForElementPresent("css=td[class='ZhAppContent'] div:contains('"
				+ pageText + "')");

		return true;
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		return null;
	}
}
