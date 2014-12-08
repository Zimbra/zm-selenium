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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class AttachmentItem {
	protected static Logger logger = LogManager.getLogger(AttachmentItem.class);

	public enum AttachmentIcon {
		ImgGenericDoc,
		ImgImageDoc,
		ImgDoc,
		ImgMSWordDoc,
		ImgMSExcelDoc,
	}
	
	private String locator = null;
	private String name = null;
	private AttachmentIcon icon = null;
	
	
	public AttachmentItem() {
		logger.info("new "+ AttachmentItem.class.getCanonicalName());
	}


	public void setLocator(String locator) {
		this.locator = locator;
	}


	public String getLocator() {
		return this.locator;
	}


	public void setAttachmentName(String attachmentName) {
		name = attachmentName;
	}


	public String getAttachmentName() {
		return name;
	}

	public void setAttachmentIcon(AttachmentIcon attachmentIcon) {
		icon = attachmentIcon;
	}


	public AttachmentIcon getAttachmentIcon() {
		return icon;
	}


	public String prettyPrint() {
		StringBuilder sb = new StringBuilder();
		sb.append(AttachmentItem.class.getSimpleName()).append('\n');
		sb.append("Name: ").append(name).append('\n');
		sb.append("Icon: ").append(icon).append('\n');
		sb.append("Locator: ").append(locator).append('\n');
		return (sb.toString());
	}
	
}
