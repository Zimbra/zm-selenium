/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013 Zimbra, Inc.
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

public class HistoryItem extends AItem {

	private String historyLocator = null;
	private String historyText = null;
	private String historyUser = null;
	private String historyAction = null;
	private String historyItem = null;
	private String historyTime = null;
	private String ParentId = null;
	
	public HistoryItem() {
		
	}
	
	/**
	 * @param historyLocator 
	 */
	public void setLocator(String locator) {
		historyLocator = locator;
	}

	/**
	 * @return the theLocator
	 */
	public String getLocator() {
		return historyLocator;
	}

	public void setParentId(String parentId) {
		ParentId = parentId;
	}

	public String getParentId() {
		return ParentId;
	}

	public void setHistoryText(String historyText) {
		this.historyText = historyText;
	}

	public String getHistoryText() {
		return historyText;
	}

	public void setHistoryTime(String historyTime) {
		this.historyTime = historyTime;
	}

	public String getHistoryTime() {
		return historyTime;
	}

	public void setHistoryAction(String historyAction) {
		this.historyAction = historyAction;
	}

	public String getHistoryAction() {
		return historyAction;
	}
	
	public void setHistoryItem(String historyItem) {
		this.historyItem = historyItem;
	}

	public String getHistoryItem() {
		return historyItem;
	}
	
	public void setHistoryUser(String historyUser) {
		this.historyUser = historyUser;
	}

	public String getHistoryUser() {
		return historyUser;
	}

	@Override
	public String prettyPrint() {
		StringBuilder sb = new StringBuilder();
		sb.append(HistoryItem.class.getSimpleName()).append('\n');
		sb.append("GUI Data:\n");
		sb.append("HistoryText: ").append(getHistoryText()).append('\n');
		sb.append("HistoryUser: ").append(getHistoryUser()).append('\n');
		sb.append("HistoryTime: ").append(getHistoryTime()).append('\n');		
		return (sb.toString());
	}

}
