/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013 Zimbra Software, LLC.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.4 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.framework.util;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class StartDesktopClient extends Thread {
   protected static Logger logger = LogManager.getLogger(StartDesktopClient.class);
   public String[] executablePath = null;
   public String[] params = null;

   public StartDesktopClient(String[] executablePath, String[] params) {
      this.executablePath = executablePath;
      this.params = params;
   }

   public void run() {
      try {
         logger.info(CommandLine.cmdExecWithOutput(executablePath, params));
      } catch (HarnessException e) {
         logger.info("Getting Harness Exception ");
         logger.info(e.getMessage());
         e.printStackTrace();
         
      } catch (IOException e) {
         e.printStackTrace();
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
}