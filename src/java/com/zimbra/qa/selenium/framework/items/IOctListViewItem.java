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
package com.zimbra.qa.selenium.framework.items;

import com.zimbra.qa.selenium.framework.util.HarnessException;

/**
 * This Interface is used by the Octopus application for any
 * item that can be represented in the Octopus file list
 * view, such as folders, files, shares
 * 
 * @author Matt Rhoades
 *
 */
public interface IOctListViewItem {

	/**
	 * Get the List View icon
	 * @return
	 * @throws HarnessException
	 */
	public String getListViewIcon() throws HarnessException;

	
	/**
	 * Set the List View icon
	 * @throws HarnessException
	 */
	public void setListViewIcon(String icon) throws HarnessException;
	
	/**
	 * Get the List View name
	 * @return
	 * @throws HarnessException
	 */
	public String getListViewName() throws HarnessException;

	
	/**
	 * Set the List View icon
	 * @throws HarnessException
	 */
	public void setListViewName(String name) throws HarnessException;
	
}