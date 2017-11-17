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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ContextMenuItem {
	protected static Logger logger = LogManager.getLogger(IItem.class);

	public static class Locators {
		public static final String zNewFolderItem = "css=tr#POPUP_NEW_FOLDER";
		public static final String zMarkAllReadItem = "css=tr#POPUP_MARK_ALL_READ";
		public static final String zDeleteItem = "css=tr#POPUP_DELETE";
		public static final String zRenameFolderItem = "css=tr#POPUP_RENAME_FOLDER";
		public static final String zMoveItem = "css=tr#POPUP_MOVE";
		public static final String zShareFolderItem = "css=tr#POPUP_SHARE_FOLDER";
		public static final String zEditPropertiesItem = "css=tr#POPUP_EDIT_PROPS";
		public static final String zExpandAllItem = "css=tr#POPUP_EXPAND_ALL";
		public static final String zEmptyFolderItem = "css=tr#POPUP_EMPTY_FOLDER";
		public static final String zSendReceiveItem = "css=tr#POPUP_SYNC";
	}

	// FIXME
	public static final ContextMenuItem C_SEPARATOR = new ContextMenuItem("css=div[id='DWT']", "", "", "");

	public String locator;
	public final String image;
	public final String text;
	public final String shortcut;
	public String parentLocator;

	public ContextMenuItem(String locator, String text, String image, String shortcut) {
		this.locator = locator;
		this.image = image;
		this.text = text;
		this.shortcut = shortcut;
	}

	public enum CONTEXT_MENU_ITEM_NAME {
		NEW_FOLDER, MARK_ALL_AS_READ, DELETE, RENAME_FOLDER, MOVE, SHARE_FOLDER, EDIT_PROPERTIES, EXPAND_ALL, EMPTY_FOLDER, TURN_SYNC_OFF, SEND_RECEIVE
	}
}