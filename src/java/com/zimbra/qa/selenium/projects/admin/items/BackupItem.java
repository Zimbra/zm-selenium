/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.admin.items;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;

public class BackupItem implements IItem {

	protected String EmailAddress;

	public BackupItem() {
		super();

	}

	@Override
	public void createUsingSOAP(ZimbraAccount account) throws HarnessException {
	}

	@Override
	public String prettyPrint() {
		return null;
	}

	public void setEmailAddress(String email) {
		EmailAddress = email;
	}

	public String getEmailAddress() {
		return (EmailAddress);
	}

	@Override
	public String getName() {
		return null;
	}
}