/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.core;

import java.util.HashMap;

public class ContactsPrefShowSelectionCheckbox extends AjaxCommonTest {

	public ContactsPrefShowSelectionCheckbox() {
		
	    super.startingPage = app.zPageContacts;

	    super.startingAccountPreferences = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
		    put("zimbraPrefShowSelectionCheckbox", "TRUE");
		}};
	}
}
