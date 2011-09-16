/*
 * ***** BEGIN LICENSE BLOCK *****
 * 
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
 * 
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.framework.items;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class StringItem {
	protected static Logger logger = LogManager.getLogger(IItem.class);

	private String string;
	private int order; //the order of the element within the outer list
	
	public StringItem (String string,int order) {
		this.string=string;
		this.order=order;
	}
	
	public String getItem(){
		return string;
	}
	
	public void setItem(String string) {
		this.string=string;
	}

	public void setOrder(int order){
		this.order=order;
	}
	
	public int getOrder(){
		return order;
	}
	
}
