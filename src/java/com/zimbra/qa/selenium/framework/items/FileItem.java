/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
 * This class represents a new document item
 *
 *
 */
public class FileItem implements IItem, IOctListViewItem {
	protected static Logger logger = LogManager.getLogger(IItem.class);

	/**
	 * Create a file item
	 */
	public FileItem() {
	}

	/**
	 * Create a file item
	 */
	public FileItem(String path) {
		filePath = path;
		if (filePath.contains("/")) {
		    fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		} else if (filePath.contains("\\")) {
		    fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
		} else {
		    fileName = filePath;
		}
	}

	/**
	 * The file name
	 */
	private String fileName;

	/**
	 * The file path
	 */
	private String filePath;

	/**
	 * The file if
	 */
	//private String fileId;

	/**
	 * The status of this file
	 */
	public boolean isSaved;

	/**
	 * The version of this file
	 */
	public int version;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String path) {
		filePath = path;;
	}

	@Override
	public String getName() {
		return fileName;
	}

	public void setFileName(String name) {
		fileName = name;;
	}

	@Override
	public String prettyPrint() {
		StringBuilder sb = new StringBuilder();
		sb.append(DocumentItem.class.getSimpleName()).append('\n');
		sb.append("Doc name: \n").append(fileName).append('\n');
		sb.append("Doc text: \n").append(fileName).append("\n");
		return (sb.toString());
	}

	@Override
	public void createUsingSOAP(ZimbraAccount account) throws HarnessException {
	}

	/////////
	// IListViewItem: Start
	/////////

	private String ListViewIcon = null;
	private String ListViewName = null;

	@Override
	public String getListViewIcon() throws HarnessException {
		return (ListViewIcon);
	}

	@Override
	public String getListViewName() throws HarnessException {
		return (ListViewName);
	}


	@Override
	public void setListViewIcon(String icon) throws HarnessException {
		ListViewIcon = icon;
	}

	@Override
	public void setListViewName(String name) throws HarnessException {
		ListViewName = name;
	}

	/////////
	// IListViewItem: End
	/////////


}
