/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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

/**
 * A base class that sets
 *  1) the starting page to be the mail app,
 *  2) sets zimbraPrefGroupMailBy=message, and
 *  3) sets zimbraPrefMessageViewHtmlPreferred=TRUE
 *  
 * @author Matt Rhoades
 *
 */
public class PrefGroupMailByMessageTest extends AjaxCommonTest {

	public PrefGroupMailByMessageTest() {
		
		super.startingPage = app.zPageMail;

		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 3370780885378699878L;
		{
			put("zimbraPrefGroupMailBy", "message");
			put("zimbraPrefMessageViewHtmlPreferred", "TRUE");
		}};

	}
	
}
