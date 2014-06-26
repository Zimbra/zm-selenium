/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.framework.util;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class StartDesktopClient extends Thread {
	protected static final Logger logger = LogManager.getLogger(StartDesktopClient.class);


	private String[] executablePath = null;
	private String[] params = null;

	public StartDesktopClient(String[] executablePath, String[] params) {

		this.executablePath = (String[])executablePath.clone();
		this.params = (String [])params.clone();

	}

	public void run() {
		try {
			logger.info(CommandLine.cmdExecWithOutput(executablePath, params));
		} catch (HarnessException e) {
			logger.error("Getting Harness Exception", e);
		} catch (IOException e) {
			logger.error("Getting IOException", e);
		} catch (InterruptedException e) {
			logger.error("Getting InterruptedException", e);
		}
	}
}