/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013 Zimbra Software, LLC.
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
/**
 * 
 */
package com.zimbra.qa.selenium.projects.zcs.ui;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;



/**
 * @author Matt Rhoades
 *
 */
public class AppPage extends CommonTest {

	
	/**
	 * This method navigates the GUI to this application page
	 *
	 * @param method The method to use
	 * @throws HarnessException 
	 */
	public void navigateTo(ActionMethod method)throws HarnessException {
		throw new HarnessException("implement me");
	}

	/**
	 * This method creates an item
	 * 
	 * The method to use must correspond to the page-specific
	 * object type, e.g. a ABComposeActionMethod must be passed to 
	 * ABCompose.createItem()
	 * 
	 * The item to create must correspond to the page-specific
	 * object type, e.g. a ContactItem must be passed to 
	 * ABCompose.createItem()
	 * 
	 * 
	 * @param method The method to use
	 * @param item The object (page-specific type) to create
	 * @return
	 * @throws HarnessException 
	 */
	public IItem createItem(ActionMethod method, IItem item) throws HarnessException {
		throw new HarnessException("implement me");
	}
	
	/**
	 * This method modifies an item
	 * 
	 * @param method The method to use
	 * @param oldItem The old object (page-specific type) to modify
	 * @param newItem The new object containing values to set
	 * @return
	 * @throws HarnessException 
	 */
	public IItem modifyItem(ActionMethod method, IItem oldItem, IItem newItem) throws HarnessException {
		throw new HarnessException("implement me");
	}


	/**
	 * This method deletes an item
	 * 
	 * @param method The method to use
	 * @param item The object (page-specific type) to delete
	 * @throws HarnessException 
	 */
	public void deleteItem(ActionMethod method, IItem item) throws HarnessException  {
		throw new HarnessException("implement me");
	}
	
}
