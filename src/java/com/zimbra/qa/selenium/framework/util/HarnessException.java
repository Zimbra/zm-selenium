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
package com.zimbra.qa.selenium.framework.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.zimbra.qa.selenium.framework.ui.AbsTab;

public class HarnessException extends Exception {

	Logger logger = LogManager.getLogger(HarnessException.class);
	private static final long serialVersionUID = 4657095353247341818L;

	protected AbsTab startingPage = null;

	public HarnessException(String message) {
		super(message);
		logger.error(message, this);
	}

	public HarnessException(Throwable cause) {
		super(cause);
		logger.error(cause.getMessage(), cause);
	}

	public HarnessException(String message, Throwable cause) {
		super(message, cause);
		logger.error(message, cause);
	}
}