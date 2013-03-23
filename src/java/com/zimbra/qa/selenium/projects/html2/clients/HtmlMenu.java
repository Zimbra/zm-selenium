/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011 VMware, Inc.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.html2.clients;

import com.zimbra.qa.selenium.framework.util.HarnessException;



/**
 * @author raodv
 * This works for html-menus(not ajax menus) with ids and/or name value. It has built-in time-sync and also works for 
 * multiple items with same name. 
 */
public class HtmlMenu extends ZObject {
	public HtmlMenu() {
		super("htmlMenuCore_html", "Html Menu");
	} 	
	public void zClick(String objNameOrId,  String itemToSelect)  throws HarnessException  {
		 ZObjectCore(objNameOrId,  "click", true, itemToSelect);
	}
	public void zClick(String objNameOrId,  String itemToSelect, String itemNumber)  throws HarnessException  {
		 ZObjectCore(objNameOrId,  "click", true, itemToSelect, itemNumber);
	}
	/**
	 * @param objNameOrId
	 * @param itemToSelect
	 * @param itemNumber if there are multiple items in the menu, enter the item number. starts from 1
	 * @param menuNumber if there are multiple menus iteslf, pass the menu number. Count starts from top-left of the screen to bottom-right
	 */
	public void zClick(String objNameOrId,  String itemToSelect, String itemNumber, String menuNumber)  throws HarnessException  {
		 ZObjectCore(objNameOrId,  "click", true, itemToSelect, itemNumber, menuNumber);
	}
	
	/**
	 * @param objNameOrId
	 * @param itemToSelect
	 * @param itemNumber if there are multiple items in the menu, enter the item number. starts from 1
	 * @param menuNumber if there are multiple menus iteslf, pass the menu number. Count starts from top-left of the screen to bottom-right
	 */
	public void zClickMenuByLocation(String objNameOrId,  String itemToSelect, String menuNumber)  throws HarnessException  {
		 ZObjectCore( objNameOrId,  "click", true, itemToSelect, "", menuNumber);
	}	
	/**
	 * @param objNameOrId
	 * @param itemToSelect
	 * @param itemNumber if there are multiple items in the menu, enter the item number. starts from 1
	 * @param menuNumber if there are multiple menus iteslf, pass the menu number. Count starts from top-left of the screen to bottom-right
	 */
	public void zClickMenuByLocation(String objNameOrId,  String itemToSelect, String itemNumber, String menuNumber) throws HarnessException   {
		 ZObjectCore( objNameOrId,  "click", true, itemToSelect, itemNumber, menuNumber);
	}		
	public String zGetCount(String objNameOrId) throws HarnessException   {
		 return ZObjectCore(objNameOrId,  "getCount");
	}
	public String zGetAllItemNames(String objNameOrId)  throws HarnessException  {
		 return ZObjectCore( objNameOrId,  "getAllItems");
	}	
	public String zGetSelectedItemName(String objNameOrId) throws HarnessException   {
		 return ZObjectCore( objNameOrId,  "getSelected");
	}	
	
	public String zGetCount(String objNameOrId, String menuNumber) throws HarnessException   {
		 return ZObjectCore(objNameOrId,  "getCount", true, "",  "", menuNumber);
	}
	public String zGetAllItemNames(String objNameOrId, String menuNumber)  throws HarnessException  {
		 return ZObjectCore( objNameOrId,  "getAllItems", true, "",  "", menuNumber);
	}	
	public String zGetSelectedItemName(String objNameOrId, String menuNumber)  throws HarnessException  {
		 return ZObjectCore( objNameOrId,  "getSelected", true, "",  "", menuNumber);
	}	
	
}
