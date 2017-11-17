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

public abstract class AItem {

	private String id = "0";

	protected AItem() {
	}

	/**
	 * Get the Zimbra ID of this item
	 * 
	 * @return
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Set the Zimbra ID of this item
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Get the name of this item, such as subject, fileas, folder name, etc.
	 * 
	 * @return
	 */
	public String getName() {
		return (getId());
	}

	/**
	 * Create a string version of this object suitable for using with a logger
	 */
	public abstract String prettyPrint();
}