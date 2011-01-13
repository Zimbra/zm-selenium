//helper class for logging
package com.zimbra.qa.selenium.framework.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ZimbraSeleniumLogger {

	public static Logger mLog = LogManager.getLogger(new GetCurClass().getCurrentClass().getSimpleName());

	public static <T> void setmLog (Class <T> clazz) {
		if(clazz!=null)
		mLog = LogManager.getLogger(clazz);
	}

	private static class GetCurClass extends SecurityManager {
		private Class<?> getCurrentClass() {
			return getClassContext()[2];
		}
	}
}