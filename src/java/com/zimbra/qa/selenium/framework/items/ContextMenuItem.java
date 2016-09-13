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

import com.zimbra.qa.selenium.framework.ui.I18N;
import com.zimbra.qa.selenium.framework.util.HarnessException;

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

	//FIXME
	public static final ContextMenuItem C_SEPARATOR = new ContextMenuItem("css=div[id='DWT']","","","");
    
	public  String locator;
	public final String image;
	public final String text;
	public final String shortcut;
	public  String parentLocator;

	public ContextMenuItem (String locator, String text, String image, String shortcut) {
		this.locator=locator;
		this.image=image;
		this.text=text;
		this.shortcut=shortcut;	
	}

	public enum CONTEXT_MENU_ITEM_NAME {
	   NEW_FOLDER,
	   MARK_ALL_AS_READ,
	   DELETE,
	   RENAME_FOLDER,
	   MOVE,
	   SHARE_FOLDER,
	   EDIT_PROPERTIES,
	   EXPAND_ALL,
	   EMPTY_FOLDER,
	   TURN_SYNC_OFF,
	   SEND_RECEIVE
	}

	public static ContextMenuItem getDesktopContextMenuItem(CONTEXT_MENU_ITEM_NAME cmiName)
	throws HarnessException {
	   String locator = null;
	   String text = null;
	   String image = null;
	   String shortcut = null;
	   switch (cmiName) {
	   case NEW_FOLDER:
	      locator = Locators.zNewFolderItem;
	      text = I18N.CONTEXT_MENU_ITEM_NEW_FOLDER;
	      break;
	   case MARK_ALL_AS_READ:
	      locator = Locators.zMarkAllReadItem;
	      text = I18N.CONTEXT_MENU_ITEM_MARK_ALL_AS_READ;
	      break;
	   case DELETE:
	      locator = Locators.zDeleteItem;
         text = I18N.CONTEXT_MENU_ITEM_DELETE;
         break;
	   case RENAME_FOLDER:
	      locator = Locators.zRenameFolderItem;
	      text = I18N.CONTEXT_MENU_ITEM_RENAME_FOLDER;
	      break;
	   case MOVE:
	      locator = Locators.zMoveItem;
	      text = I18N.CONTEXT_MENU_ITEM_MOVE;
	      break;
	   case SHARE_FOLDER:
	      locator = Locators.zShareFolderItem;
	      text = I18N.CONTEXT_MENU_ITEM_SHARE_FOLDER;
	      break;
	   case EDIT_PROPERTIES:
	      locator = Locators.zEditPropertiesItem;
	      text = I18N.CONTEXT_MENU_ITEM_EDIT_PROPERTIES;
	      break;
	   case EXPAND_ALL:
	      locator = Locators.zExpandAllItem;
	      text = I18N.CONTEXT_MENU_ITEM_EXPAND_ALL;
	      break;
	   case EMPTY_FOLDER:
	      locator = Locators.zEmptyFolderItem;
	      text = I18N.CONTEXT_MENU_ITEM_EMPTY_FOLDER;
	      break;
	   case TURN_SYNC_OFF:
	      throw new HarnessException("Implement me!");
	   case SEND_RECEIVE:
	      locator = Locators.zSendReceiveItem;
	      text = I18N.CONTEXT_MENU_ITEM_SEND_RECEIVE;
	      break;
	   }

	   return new ContextMenuItem(locator, text, image, shortcut);
	}
}
