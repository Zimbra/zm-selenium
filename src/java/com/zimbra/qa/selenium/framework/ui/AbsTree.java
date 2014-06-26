/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.framework.ui;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;


/**
 * A <code>AbsTree</code> object represents a "tree panel", 
 * such as a Folder tree, Addressbook tree, Calendar tree, etc.
 * <p>
 * 
 * @author Matt Rhoades
 *
 */
public abstract class AbsTree extends AbsPage {
	protected static Logger logger = LogManager.getLogger(AbsTree.class);

	public static final String zNewTagIcon = "css=td[class='overviewHeader-Text FakeAnchor']>div[class^=ImgNewTag]";


	/**
	 * Create this page object that exists in the specified application
	 * @param application
	 */
	public AbsTree(AbsApplication application) {
		super(application);
		
		logger.info("new AbsTree");
	}
	
	/**
	 * Click on a button
	 * @param button
	 * @return
	 * @throws HarnessException
	 */
	public abstract AbsPage zPressButton(Button button) throws HarnessException;

	/**
	 * Apply the specified action on the specified item
	 * @param action
	 * @param addressbook
	 * @return
	 * @throws HarnessException
	 */
	public abstract AbsPage zTreeItem(Action action, IItem item) throws HarnessException;

	/**
	 * Apply the specified action with option on the specified item
	 * <p>
	 * For example, use this method to take an action using the context method.  The
	 * Action is Action.A_LEFTCLICK and the Button would be the context menu item, such
	 * as Button.B_DELETE
	 * <p>
	 * @param action
	 * @param addressbook
	 * @return
	 * @throws HarnessException
	 */
	public abstract AbsPage zTreeItem(Action action, Button option, IItem item) throws HarnessException;


	/**
	 * Return the unique name for this page class
	 * @return
	 */
	public abstract String myPageName();
	
	
}
