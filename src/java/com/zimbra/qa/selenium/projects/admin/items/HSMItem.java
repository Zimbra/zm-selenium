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

	protected String volumeName;
	protected String volumePath;
	protected String volumeType;
	protected Boolean isCurrent;
	protected Map<String, String> HSMAttrs;

	public HSMItem(String volumeName, String volumePath) {
		HSMAttrs = new HashMap<String, String>();
		this.volumeName = volumeName;
		this.volumePath = volumePath;
	}

	public Map<String, String> getHSMAttrs() {
		return HSMAttrs;
	}

	public Boolean getIsCurrent() {
		return isCurrent;
	}

	public String getVolumeName() {
		return volumeName;
	}

	public String getVolumePath() {
		return volumePath;
	}

	public String getVolumeType() {
		return volumeType;
	}

	public void setVolumeName(String volName) {
		this.volumeName = volName;
	}

	public void setVolumePath(String volPath) {
		this.volumePath = volPath;
	}

	public void setVolumeType(String volType) {
		this.volumeType = volType;
	}

	public void setIsCurrent(Boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	@Override
	public String getName() {
		return volumeName;
	}

	@Override
	public void createUsingSOAP(ZimbraAccount account) throws HarnessException {
	}

	@Override
	public String prettyPrint() {
		return null;
	}

}
