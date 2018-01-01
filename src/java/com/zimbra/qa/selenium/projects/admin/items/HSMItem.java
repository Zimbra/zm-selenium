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
package com.zimbra.qa.selenium.projects.admin.items;

import org.apache.log4j.Logger;
import java.util.HashMap;
import org.apache.log4j.LogManager;
import java.util.Map;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;

public class HSMItem implements IItem {
	protected static Logger logger = LogManager.getLogger(IItem.class);

	protected String volName; // Volume name of storage to be added/edited
	protected String volPath;
	protected String volType; // Local or S3 bucket
	protected Boolean isCurrent;
	protected Map<String, String> HSMAttrs;

	public HSMItem(String volName, String volPath) {
		HSMAttrs = new HashMap<String, String>();
		this.volName = volName;
		this.volPath = volPath;
	}

	public Map<String, String> getHSMAttrs() {
		return HSMAttrs;
	}

	public Boolean getIsCurrent() {
		return isCurrent;
	}

	public String getVolName() {
		return volName;
	}

	public String getVolPath() {
		return volPath;
	}

	public String getVolType() {
		return volType;
	}

	public void setVolName(String volName) {
		this.volName = volName;
	}

	public void setVolPath(String volPath) {
		this.volPath = volPath;
	}

	public void setVolType(String volType) {
		this.volType = volType;
	}

	public void setIsCurrent(Boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	@Override
	public String getName() {
		return volName;
	}

	@Override
	public void createUsingSOAP(ZimbraAccount account) throws HarnessException {
	}

	@Override
	public String prettyPrint() {
		return null;
	}

}
