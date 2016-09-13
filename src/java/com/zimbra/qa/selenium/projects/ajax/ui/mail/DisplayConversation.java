/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui.mail;

import java.util.*;

import org.openqa.selenium.WebDriverException;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.util.*;

/**
 * @author zimbra
 *
 */
public class DisplayConversation extends DisplayMail {

	protected DisplayConversation(AbsApplication application) {
		super(application);

		logger.info("new " + DisplayConversation.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	private DisplayConversationMessage parseMessageRow(String locator) throws HarnessException {
		
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("can't find that message row: "+ locator);
		}
		
		DisplayConversationMessage item = new DisplayConversationMessage(this.MyApplication);

		String id = this.sGetAttribute(locator + "@id");
		item.setItemId(id);

		return (item);
	}


	public List<DisplayConversationMessage> zListGetMessages() throws HarnessException {
		List<DisplayConversationMessage> items = new ArrayList<DisplayConversationMessage>();
		
		String listLocator = "css=div#zv__CLV-main__CV_messages";
		String rowLocator = "div";

		// Make sure the button exists
		if ( !this.sIsElementPresent(listLocator) )
			throw new HarnessException("Message List View Rows is not present: " + listLocator);

		
		// How many items are in the table?
		int count = this.sGetCssCount(listLocator + ">" + rowLocator);
		logger.debug(myPageName() + " zListGetMessages: number of messages: "+ count);

		this.zGetHtml(listLocator);
		
		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {

			//String locator = listLocator + ">" + rowLocator +">"+ rowLocator+":nth-child("+ i +")";
			String locator = listLocator + ">" + rowLocator +":nth-child("+ i +")";


			try {
				
				if ( !this.sIsElementPresent(locator) ) {
					continue;
				}
				
				String clazz = this.sGetAttribute(locator + ">"+  rowLocator +"@class");
				if ( (clazz == null) || (!clazz.contains("ZmMailMsgCapsuleView")) ) {
					continue;
				}
				
			} catch (WebDriverException e) {
				continue;
			}
			
			DisplayConversationMessage item = parseMessageRow(locator + ">"+  rowLocator);
			items.add(item);
			logger.info(item.prettyPrint());
			
		}

		return (items);
	}

	public List<DisplayConversationMessage> zListItem() throws HarnessException {
		List<DisplayConversationMessage> items = new ArrayList<DisplayConversationMessage>();
		return (items);
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		SleepUtil.sleep(5000);
		return (true);
	}
}
