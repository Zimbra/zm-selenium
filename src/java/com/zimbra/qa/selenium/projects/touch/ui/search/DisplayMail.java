/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.touch.ui.search;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;

public class DisplayMail extends com.zimbra.qa.selenium.projects.touch.ui.mail.DisplayMail {

	public DisplayMail(AbsApplication application) {
		super(application);
		
		logger.info("new " + DisplayMail.class.getCanonicalName());
	}

	
	@Override
	public boolean zIsActive() throws HarnessException {
		//logger.warn("implement me", new Throwable());
		zWaitForZimlets();
		
		// Determine which <div/> contains this preview
		// Use this 'top' css for all subsequent parsing
		// zv__TV-SR-Mail-1__MSG
		
		if ( this.zIsVisiblePerPosition("css=div[id^='zv__TV-SR-Mail-'][id$='__MSG']", 0, 0) ) {
			
			int count = this.sGetCssCount("css=div[id^='zv__TV-SR-Mail-'][id$='__MSG']");
			if ( count > 1 ) {
				throw new HarnessException("Too many message views open: "+ count);
			}
			ContainerLocator = "css=div#" + this.sGetAttribute("css=div[id^='zv__TV-SR-Mail-'][id$='__MSG']" + "@id");
			
//		} else if ( this.zIsVisiblePerPosition("css=div[id^='zv__TV-SR-Mail']", 0, 0)) {
//			
//			zv__TV-SR-Mail-1__MSG
//			
//			if ( this.zIsVisiblePerPosition(Locators.MessageViewPreviewAtBottomCSS, 0, 0) ) {
//				ContainerLocator = Locators.MessageViewPreviewAtBottomCSS;
//			} else if ( this.zIsVisiblePerPosition(Locators.MessageViewPreviewAtRightCSS, 0, 0) ) {
//				ContainerLocator = Locators.MessageViewPreviewAtRightCSS;
//			} else {
//				throw new HarnessException("Unable to determine the current open view");				
//			}
//			
//		} else if ( this.zIsVisiblePerPosition("css=div[id^='zv__CLV-SR-Mail']", 0, 0) ) {
//			
//			if ( this.zIsVisiblePerPosition(Locators.ConversationViewPreviewAtBottomCSS, 0, 0) ) {
//				ContainerLocator = Locators.ConversationViewPreviewAtBottomCSS;
//			} else if ( this.zIsVisiblePerPosition(Locators.ConversationViewPreviewAtRightCSS, 0, 0) ){
//				ContainerLocator = Locators.ConversationViewPreviewAtRightCSS;
//			} else {
//				throw new HarnessException("Unable to determine the current open view");
//			}
			
		} else {
			
			throw new HarnessException("Unable to determine the current open view");
			
		}
		

		return (sIsElementPresent(this.ContainerLocator) );
				
	}

	
	public HtmlElement zGetMailPropertyAsHtml(Field field) throws HarnessException {

		String source = null;

		if ( field == Field.Body) {

			try {

				this.sSelectFrame("css=iframe[id^='zv__TV-SR-Mail'][id$='__MSG__body__iframe']");

				source = this.sGetHtmlSource();

				// For some reason, we don't get the <html/> tag.  Add it
				source = "<html>" + source + "</html>";

			} finally {
				// Make sure to go back to the original iframe
				this.sSelectFrame("relative=top");
			}

		} else {
			throw new HarnessException("not implemented for field "+ field);
		}

		logger.info("DisplayMail.zGetMailPropertyAsHtml() = "+ HtmlElement.clean(source).prettyPrint());

		// Clean up the HTML code to be valid
		return (HtmlElement.clean(source));


	}

}
