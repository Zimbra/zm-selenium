package com.zimbra.qa.selenium.framework.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class OperatingSystem {
	private static Logger logger = LogManager.getLogger(OperatingSystem.class);
	
	public static boolean isWindows() {
		return (OperatingSystem.getSingleton().os.startsWith("windows"));
	}
	
	public static boolean isWindowsXP() {
		return (OperatingSystem.getSingleton().os.equals("windows xp"));
	}

	public static boolean isLinux() {
		return (OperatingSystem.getSingleton().os.startsWith("linux"));
	}
	
	public static boolean isMac() {
		return (OperatingSystem.getSingleton().os.startsWith("mac"));
	}

	public enum OsType {
      WINDOWS, WINDOWS_XP, LINUX, MAC
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

	/**
    * Get the OS type from the system information
    * @return (enum: OperatingSystem.OsType) OS Type (Windows, MAC, or Linux)
    */
   public static OsType getOSType() {
      logger.info("os.name is: " + getSingleton().os);
      OsType osType = null;
      if (isWindows()) {
         osType = OsType.WINDOWS;
      } else if (isWindowsXP()) {
         osType = OsType.WINDOWS_XP;
      } else if (isMac()) {
         osType = OsType.MAC;
      } else if (isLinux()) {
         osType = OsType.LINUX;
      }
      return osType;
   }

   private String os = null;
	
	// Singleton methods
	//
	
    private volatile static OperatingSystem singleton;
 
    private OperatingSystem() {
		os = System.getProperty("os.name").toLowerCase();
		logger.info("Operating System: "+ os);

    }
 
    private static OperatingSystem getSingleton() {
        if(singleton==null) {
            synchronized(OperatingSystem.class){
                if(singleton == null) {
                    singleton = new OperatingSystem();
                }
            }
        }
        return singleton;
    }	
}
