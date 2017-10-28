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
package com.zimbra.qa.selenium.framework.ui;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;


/**
 * A <code>AbsForm</code> object represents a "compose page", 
 * such as a new message, new contact, new appointment, new document, etc.
 * <p>
 * Form objects are usually returned after clicking NEW from the toolbar.
 * <p>
 * As a shortcut, form objects take a {@link ZimbraItem} object in the 
 * {@link AbsForm#zFill(ZimbraItem)} and attempts to fill in the form
 * automatically based on the item's previously set properties.
 * <p>
 * 
 * @author Matt Rhoades
 *
 */
public abstract class AbsForm extends AbsPage {
	protected static Logger logger = LogManager.getLogger(AbsForm.class);


	/**
	 * Create this page object that exists in the specified application
	 * @param application
	 */
	public AbsForm(AbsApplication application) {
		super(application);
		
		logger.info("new AbsForm");
	}
	
	/**
	 * Fill out the form (but don't submit)
	 * @throws HarnessException on error
	 */
	public abstract void zFill(IItem item) throws HarnessException;
	
	
	/**
	 * Click on "submit" button
	 * @throws HarnessException on error
	 */
	public abstract void zSubmit() throws HarnessException;
	
	
	/**
	 * Fill and submit the form
	 * @throws HarnessException on error
	 */
	public void zComplete(IItem item) throws HarnessException {
		zFill(item);
		zSubmit();
	}
	
	
	/**
	 * Return the unique name for this page class
	 * @return
	 */
	public abstract String myPageName();
	
}
