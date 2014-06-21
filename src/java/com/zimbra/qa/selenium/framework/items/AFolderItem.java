/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.framework.items;

import java.util.List;

public abstract class AFolderItem extends AItem {

	private String _ParentId = null;
	private String _Name = null;
	private String _Color = null;
	private List<AFolderItem> _Subfolders = null;

	public void setParentId(String id) {
		_ParentId = id;
	}

	public String getParentId() {
		return (_ParentId);
	}


	public void setName(String name) {
		_Name = name;
	}

	public String getName() {
		return (_Name);
	}

	public void setColor(String color) {
		_Color = color;
	}

	public String getColor() {
		return (_Color);
	}

	public List<AFolderItem> getSubfolders() {
		return (_Subfolders);
	}

	public void setSubfolders(List<AFolderItem> subfolders) {
		_Subfolders = subfolders;
	}

	public String getView() {
		return null;
	}


}
