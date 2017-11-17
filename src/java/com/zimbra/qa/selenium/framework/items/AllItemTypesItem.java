/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.framework.items;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;

/**
 * Used to define a Zimbra All Item Types Search Results
 *
 * @author Matt Rhoades
 *
 */
public class AllItemTypesItem implements IItem {
	protected static Logger logger = LogManager.getLogger(IItem.class);

	public String tag = null;
	public String imageType = null;
	public String from = null;
	public String attachment = null; // boolean??
	public String subject = null;
	public String date = null;

	public AllItemTypesItem() {
		super();
	}

	public AllItemTypesItem(String tag, String imageType, String from, String attachment, String subject, String date) {
		this.tag = tag;
		this.imageType = imageType;
		this.from = from;
		this.attachment = attachment;
		this.subject = subject;
		this.date = date;

	}

	@Override
	public String getName() {
		return "AllItemTypes: " + subject;
	}

	@Override
	public void createUsingSOAP(ZimbraAccount account) throws HarnessException {
	}

	private String myId;

	public String getId() {
		return (myId);
	}

	public void setId(String id) {
		myId = id;
	}

	@Override
	public String prettyPrint() {
		StringBuilder sb = new StringBuilder();
		sb.append(AllItemTypesItem.class.getSimpleName()).append('\n');

		sb.append(String.format("Tag:(%s)\n Image Type:(%s)\n From:(%s)\n Attachment:(%s)\n Subject:(%s)\n Date:(%s)\n",
				tag, imageType, from, attachment, subject, date));

		return (sb.toString());
	}
}