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
package com.zimbra.qa.selenium.projects.admin.items;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;

public class VoiceChatServiceItem implements IItem {

	protected String displayName;

	public VoiceChatServiceItem() {
		super();

		displayName = "service" + ConfigProperties.getUniqueString();
	}

	@Override
	public void createUsingSOAP(ZimbraAccount account) throws HarnessException {
	}

	@Override
	public String prettyPrint() {
		return null;
	}

	public void setDisplayName(String name) {
		displayName = name;
	}

	public String getDisplayName() {
		return (displayName);
	}

	@Override
	public String getName() {
		return null;
	}
}