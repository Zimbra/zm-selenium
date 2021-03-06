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
package com.zimbra.qa.selenium.projects.ajax.pages.mail;

import java.util.*;
import com.zimbra.qa.selenium.framework.items.AttachmentItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;

public class DisplayConversationMessage extends DisplayMail {

	private String itemId = null;

	protected DisplayConversationMessage(AbsApplication application) {
		super(application);

		logger.info("new " + DisplayConversationMessage.class.getCanonicalName());
	}

	public void setItemId(String id) {
		this.itemId = id;
	}

	public String getItemId() {
		return itemId;
	}

	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zDisplayPressButton(" + button + ")");

		tracer.trace("Click " + button);

		AbsPage page = this;
		String locator = null;
		boolean doPostfixCheck = false;

		if (button == Button.B_QUICK_REPLY_REPLY) {

			locator = "css=div#" + this.itemId + "__footer" + " a[id$='__footer_reply']";
			this.sClick(locator);
			this.zWaitForBusyOverlay();

			return (null);

		} else if (button == Button.B_QUICK_REPLY_REPLY_ALL) {

			locator = "css=div#" + this.itemId + "__footer" + " a[id$='__footer_replyAll']";
			this.sClick(locator);
			this.zWaitForBusyOverlay();

			return (null);

		} else if (button == Button.B_QUICK_REPLY_FORWARD) {

			locator = "css=div#" + this.itemId + " a[id$='__footer_forward']";
			page = new FormMailNew(this.MyApplication);

			this.sClick(locator);
			this.zWaitForBusyOverlay();

			page.zWaitForActive();

			return (page);

		} else if (button == Button.B_QUICK_REPLY_MORE_ACTIONS) {

			locator = "css=div#" + this.itemId + " a[id$='__footer_moreActions']";
			this.sClick(locator);
			this.zWaitForBusyOverlay();

			return (null);

		} else if (button == Button.B_QUICK_REPLY_SEND) {

			locator = "css=div[id='zb__CV2__Rep__SEND'] td[id$='_title']";
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.B_QUICK_REPLY_CANCEL) {

			locator = "css=div[id='zb__CV2__Rep__CANCEL'] td[id$='_title']";
			page = null;
			doPostfixCheck = false;

		} else if (button == Button.B_QUICK_REPLY_MORE) {

			locator = "css=div[id='zb__CV2__Rep__REPLY_ALL'] td[id$='_title']";
			page = new FormMailNew(this.MyApplication);
			doPostfixCheck = false;

		} else {
			return (super.zPressButton(button));
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("locator is not present for button " + button + " : " + locator);

		this.sClick(locator);

		this.zWaitForBusyOverlay();

		if (page != null) {
			page.zWaitForActive();
		}

		if (doPostfixCheck) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}

		return (page);
	}

	public void zFillField(Field field, String value) throws HarnessException {

		tracer.trace("Set " + field + " to " + value);

		String locator = null;

		if (field == Field.Body) {

			locator = "css=div#zv__CLV-main textarea[id$='_replyInput']";

		} else {

			throw new HarnessException("not implemented for field " + field);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Field is not present field=" + field + " locator=" + locator);

		this.sFocus(locator);
		this.sClick(locator);
		this.zWaitForBusyOverlay();

		// Enter text
		this.sType(locator, value);
		this.zWaitForBusyOverlay();
	}

	public List<AttachmentItem> zListGetAttachments() throws HarnessException {
		logger.info(myPageName() + " zListGetAttachments()");

		List<AttachmentItem> items = new ArrayList<AttachmentItem>();

		String listLocator = "css=div#" + this.itemId + " table[id$='_attLinks_table']";

		if (!this.sIsElementPresent(listLocator)) {
			return (items);
		}

		String tableLocator = listLocator + ">tbody>tr";
		int count = this.sGetCssCount(tableLocator);
		logger.debug(myPageName() + " zListGetMessages: number of messages: " + count);

		for (int i = 1; i <= count; i++) {

			// Add the new item to the list
			AttachmentItem item = parseAttachmentRow(listLocator + ">tbody>tr:nth-of-type(" + i + ") ");
			items.add(item);
			logger.info(item.prettyPrint());
		}

		return (items);
	}

	public String prettyPrint() {
		StringBuilder sb = new StringBuilder();
		sb.append(itemId);
		return (sb.toString());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		return false;
	}
}