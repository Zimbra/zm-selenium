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
package com.zimbra.qa.selenium.framework.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class OperatingSystem {
	private static Logger logger = LogManager.getLogger(OperatingSystem.class);

	public static boolean isWindows() {
		return (OperatingSystem.getSingleton().os.startsWith("windows"));
	}
	
	public static boolean isWindows10() {
		return (OperatingSystem.getSingleton().os.startsWith("windows 10"));
	}

	public static boolean isLinux() {
		return (OperatingSystem.getSingleton().os.startsWith("linux"));
	}

	public static boolean isMac() {
		return (OperatingSystem.getSingleton().os.startsWith("mac"));
	}

	public enum OsType {
		WINDOWS, WINDOWS10, LINUX, MAC
	}

	public enum OsArch {
		X86, X64
	}

	public static OsArch getOsArch() {
		String osArch = System.getProperty("os.arch").toLowerCase();
		logger.info("os.arch is: " + osArch);
		if (osArch.equals("x86") || osArch.equals("i386")) {
			return OsArch.X86;
		} else {
			return OsArch.X64;
		}
	}

	public static OsType getOSType() {
		logger.info("os.name is: " + getSingleton().os);
		OsType osType = null;
		if (isWindows()) {
			osType = OsType.WINDOWS;
		} else if (isWindows10()) {
			osType = OsType.WINDOWS10;
		} else if (isMac()) {
			osType = OsType.MAC;
		} else if (isLinux()) {
			osType = OsType.LINUX;
		}
		return osType;
	}

	private String os = null;

	private volatile static OperatingSystem singleton;

	private OperatingSystem() {
		os = System.getProperty("os.name").toLowerCase();
		logger.info("Operating System: " + os);
	}

	private static OperatingSystem getSingleton() {
		if (singleton == null) {
			synchronized (OperatingSystem.class) {
				if (singleton == null) {
					singleton = new OperatingSystem();
				}
			}
		}
		return singleton;
	}
}
